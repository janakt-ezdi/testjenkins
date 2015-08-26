package com.ezdi.cac.service.hl7;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezdi.cac.bean.hl7.CodeMapByUserBean;
import com.ezdi.cac.bean.hl7.CodeMasterBean;
import com.ezdi.cac.bean.hl7.CodeUpdateReasonBean;
import com.ezdi.cac.bean.hl7.DRGResponseValuesBean;
import com.ezdi.cac.bean.hl7.DischargeDispositionBean;
import com.ezdi.cac.bean.hl7.DocumentBean;
import com.ezdi.cac.bean.hl7.DocumentPhysicianMapBean;
import com.ezdi.cac.bean.hl7.DocumentTypeBean;
import com.ezdi.cac.bean.hl7.EditOptionsBean;
import com.ezdi.cac.bean.hl7.EncounterBean;
import com.ezdi.cac.bean.hl7.EncounterEventSummaryBean;
import com.ezdi.cac.bean.hl7.EncounterHistoryBean;
import com.ezdi.cac.bean.hl7.EncounterObservationRequestBean;
import com.ezdi.cac.bean.hl7.EncounterObservationResultBean;
import com.ezdi.cac.bean.hl7.EncounterPhysicianBean;
import com.ezdi.cac.bean.hl7.FileAllocationBean;
import com.ezdi.cac.bean.hl7.GroupingSourcesBean;
import com.ezdi.cac.bean.hl7.LocationBean;
import com.ezdi.cac.bean.hl7.LocationPatientClassBean;
import com.ezdi.cac.bean.hl7.LocationPatientClassTypeServiceLineBean;
import com.ezdi.cac.bean.hl7.MasterMapBean;
import com.ezdi.cac.bean.hl7.PatientBean;
import com.ezdi.cac.bean.hl7.PatientClassBean;
import com.ezdi.cac.bean.hl7.PatientTypeBean;
import com.ezdi.cac.bean.hl7.PayerBean;
import com.ezdi.cac.bean.hl7.ResequenceOptionsBean;
import com.ezdi.cac.bean.hl7.RoleWiseEncounterStatusBean;
import com.ezdi.cac.bean.hl7.ServiceLineBean;
import com.ezdi.cac.bean.hl7.ServiceLineUserRoleDocumentTypeBean;
import com.ezdi.cac.bean.hl7.UserRoleBean;
import com.ezdi.cac.bean.hl7.WorkListBean;
import com.ezdi.cac.constant.CodeStatusConstant;
import com.ezdi.cac.constant.CodeTypeConstant;
import com.ezdi.cac.constant.CodeUpdateReasonConstant;
import com.ezdi.cac.constant.CodingSystemConstant;
import com.ezdi.cac.constant.CommonConstant;
import com.ezdi.cac.constant.EncounterLockByConstant;
import com.ezdi.cac.constant.EncounterStatusConstant;
import com.ezdi.cac.constant.EventTypeConstant;
import com.ezdi.cac.constant.HL7Action;
import com.ezdi.cac.constant.HL7Info;
import com.ezdi.cac.constant.TenantConfigConstant;
import com.ezdi.cac.constant.UserRoleConstant;
import com.ezdi.cac.constant.WorkListConstants;
import com.ezdi.cac.dao.hl7.HL7Dao;
import com.ezdi.cac.factory.Tenant;
import com.ezdi.cac.s3.AmazonS3Service;
import com.ezdi.cac.util.AWSSQSServiceUtils;
import com.ezdi.cac.util.DateUtils;
import com.ezdi.cac.util.HL7PropertiesUtils;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;
import com.ezdi.server.common.component.CACMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("hl7Service")
@Tenant
public class HL7ServiceImpl extends HL7BaseService implements HL7Service
{

	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(HL7ServiceImpl.class);

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void createOrUpdateCase(Map<String, Object> hl7InfoMap) throws Exception
	{
		HL7Dao hl7DaoImpl;
		LOGGER.debug("Inside createOrUpdateCase.");
		if (isValidEncounter(hl7InfoMap.get(HL7Info.ENCOUNTER)))
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			boolean isEncounterBilled = false;
			hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
			EncounterBean encounterBean = hl7DaoImpl.getEncounter(mrn, encounterNumber);
			int encounterId = 0;
			if (encounterBean != null)
			{
				encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status. encounterId : " + encounterId);
				isEncounterBilled = hl7DaoImpl.isEncounterBilled(encounterId);
				LOGGER.info("Case billing status. isEncounterBilled : " + isEncounterBilled);
			}

			if (!isEncounterBilled)
			{
				boolean newCase = false;
				int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				Date utcDateTime = DateUtils.getUTCDateTime();
				Map<String, String> tenantConfigMap = hl7DaoImpl.getTenantConfigMap();
				String hospitalTimeZodeId = tenantConfigMap.get(TenantConfigConstant.HOSPITAL_TIMEZONE_ID);
				String hospitalName = tenantConfigMap.get(TenantConfigConstant.HOSPITAL_NAME);

				LOGGER.info("Checking patient. mrn : " + mrn);
				PatientBean patientBean = insrtOrUpdatePatient(encounter, mrn, hospitalTimeZodeId, hl7UserId, utcDateTime);
				int patientId = patientBean.getId();

				LOGGER.info("Now publishing Update Index Event for patientId: " + patientId);
				notifyUpdateToIndexById(patientId, PatientBean.class);
				LOGGER.info("Published Index Update Event for patientId: " + patientId);

				if (encounterBean == null)
				{
					LOGGER.info("Encounter does not exists. encounterNumber : " + encounterNumber);
					encounterBean = new EncounterBean();
					encounterBean.setCreatorUserId(hl7UserId);
					encounterBean.setCreatorDateTime(utcDateTime);
					encounterBean.setUpdaterUserId(hl7UserId);
					encounterBean.setUpdationDateTime(utcDateTime);
					encounterBean.setPatientId(patientId);
					encounterBean.setLockBy(EncounterLockByConstant.UNLOCKED);
					encounterBean.setIsActive(CommonConstant.ACTIVE_ENCOUNTER);
					encounterBean.setIsEncounterUpdated(0);
					encounterBean.setQueryOpertunity(CommonConstant.DEFAULT_QUERY_OPPORTUNITY);
					encounterBean.setDrgImpact(CommonConstant.DEFAULT_DRG_IMPACT);
					setEncounterBean(encounterBean, encounter, hospitalName, hospitalTimeZodeId);

					LOGGER.info("Inserting encounter information. encounterNumber : " + encounterNumber);
					hl7DaoImpl.insertEncounter(encounterBean);
					encounterId = encounterBean.getId();
					LOGGER.info("Encounter information inserted successfully. encounterId : " + encounterId);

					LOGGER.info("Inserting into encounter history. encounterId : " + encounterId);
					EncounterHistoryBean bean = createEncounterHistory(encounterBean);
					hl7DaoImpl.insertEncounterHistory(bean);
					LOGGER.info("EncounterHistory inserted successfully. encounterId : " + encounterId);

					LOGGER.info("Now publishing Update Index Event for encounterId: " + encounterId);
					notifyUpdateToIndexById(encounterId, EncounterBean.class);
					LOGGER.info("Published Index Update Event for encounterId: " + encounterId);

					newCase = true;
				} else
				{
					encounterId = encounterBean.getId();
					LOGGER.info("Encounter already exists. encounterId : " + encounterId);

					encounterBean.setUpdaterUserId(hl7UserId);
					encounterBean.setUpdationDateTime(utcDateTime);
					encounterBean.setIsActive(CommonConstant.ACTIVE_ENCOUNTER);

					String oldPatientClass = encounterBean.getPatientClassCode();
					setEncounterBean(encounterBean, encounter, hospitalName, hospitalTimeZodeId);
					String newPatientClass = encounterBean.getPatientClassCode();

					boolean isClassChanged = false;
					if (!oldPatientClass.equalsIgnoreCase(newPatientClass))
					{
						isClassChanged = true;
					}
					LOGGER.info(" The Patient Class Updated ? : " + isClassChanged);
					if (isClassChanged)
					{
						encounterBean.setIsEncounterUpdated(1);
					}

					LOGGER.info("Updating encounter information. encounterId : " + encounterId);
					hl7DaoImpl.updateEncounter(encounterBean);
					LOGGER.info("Encounter information updated successfully.");

					LOGGER.info("Inserting into encounter history. encounterId : " + encounterId);
					EncounterHistoryBean bean = createEncounterHistory(encounterBean);
					hl7DaoImpl.insertEncounterHistory(bean);
					LOGGER.info("EncounterHistory inserted successfully. encounterId : " + encounterId);

					LOGGER.info("Now publishing Update Index Event for encounterId: " + encounterId);
					notifyUpdateToIndexById(encounterId, EncounterBean.class);
					LOGGER.info("Published Index Update Event for encounterId: " + encounterId);

					if (isClassChanged)
					{
						changePatientClass(encounterBean, oldPatientClass);
					}
				}
				List<Map<String, Object>> physicianList = (List<Map<String, Object>>) encounter.get(HL7Info.PHYSICIAN_LIST);
				insertOrUpdatePhysician(physicianList, encounterId);

				DocumentBean hl7CodeDocumentBean = getHL7DocumentBean(encounterBean, hl7UserId, utcDateTime);

				List<Map<String, Object>> codeList = (List<Map<String, Object>>) hl7InfoMap.get(HL7Info.CODE_LIST);
				insertOrUpdateCode(codeList, encounterId, hl7CodeDocumentBean, hl7UserId, utcDateTime, hospitalTimeZodeId);
				if (newCase)
				{
					addEncounterInDocumentNotReceiveStatus(encounterId, hl7UserId, utcDateTime);
				}
				insertEncounterWorkListMapHL7(encounterBean, newCase);
			} else
			{
				throw new Exception("Case is already billed. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
						+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			}
		}
		LOGGER.debug("Exiting from createOrUpdateCase.");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void cancelAdmit(Map<String, Object> hl7InfoMap) throws Exception
	{
		HL7Dao hl7DaoImpl;
		LOGGER.debug("Inside cancelAdmit.");
		if (isValidEncounter(hl7InfoMap.get(HL7Info.ENCOUNTER)))
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			boolean isEncounterBilled = false;
			hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
			EncounterBean encounterBean = hl7DaoImpl.getEncounter(mrn, encounterNumber);
			int encounterId = 0;
			if (encounterBean != null)
			{
				encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status. encounterId : " + encounterId);
				isEncounterBilled = hl7DaoImpl.isEncounterBilled(encounterId);
				LOGGER.info("Case billing status. isEncounterBilled : " + isEncounterBilled);
			}
			if (!isEncounterBilled)
			{
				int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				Date utcDateTime = DateUtils.getUTCDateTime();
				if (encounterBean != null)
				{
					encounterBean.setIsActive(CommonConstant.INACTIVE_ENCOUNTER);
					encounterBean.setUpdaterUserId(hl7UserId);
					encounterBean.setUpdationDateTime(utcDateTime);

					LOGGER.info("Updating encounter information. encounterId : " + encounterId);
					hl7DaoImpl.updateEncounter(encounterBean);
					LOGGER.info("Encounter information updated successfully.");

					LOGGER.info("Inserting into encounter history. encounterId : " + encounterId);
					EncounterHistoryBean bean = createEncounterHistory(encounterBean);
					hl7DaoImpl.insertEncounterHistory(bean);
					LOGGER.info("EncounterHistory inserted successfully. encounterId : " + encounterId);

					LOGGER.info("Now publishing Update Index Event for encounterId: " + encounterId);
					notifyUpdateToIndexById(encounterId, EncounterBean.class);
					LOGGER.info("Published Index Update Event for encounterId: " + encounterId);
				}
			} else
			{
				throw new Exception("Case is already billed. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
						+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			}
		}
		LOGGER.debug("Exiting from cancelAdmit.");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void addDocument(Map<String, Object> hl7InfoMap) throws Exception

	{
		LOGGER.debug("Inside addDocument.");
		if (isValidEncounter(hl7InfoMap.get(HL7Info.ENCOUNTER)))
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			LOGGER.info("Checking case. mrn : " + mrn + " encounterNumber : " + encounterNumber);
			EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
			if (encounterBean != null)
			{
				int encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status. encounterId : " + encounterId);
				boolean isEncounterBilled = getHl7DaoFactory().getHl7Dao().isEncounterBilled(encounterId);
				LOGGER.info("Encounter billed status. isEncounterBilled : " + isEncounterBilled);
				if (!isEncounterBilled)
				{
					Map<String, String> tenantConfigMap = getHl7DaoFactory().getHl7Dao().getTenantConfigMap();
					String hospitalTimeZodeId = tenantConfigMap.get(TenantConfigConstant.HOSPITAL_TIMEZONE_ID);

					PatientBean patientBean = getHl7DaoFactory().getHl7Dao().getPatient(mrn);
					int patientId = patientBean.getId();

					if (isValidDocument(hl7InfoMap.get(HL7Info.DOCUMENT)))
					{
						Map<String, Object> document = (Map<String, Object>) hl7InfoMap.get(HL7Info.DOCUMENT);
						String documentName = document.get(HL7Info.DOCUMENT_NAME).toString();

						LOGGER.info("Checking document. encounterId : " + encounterId + " documentName : " + documentName);
						DocumentBean documentBean = getHl7DaoFactory().getHl7Dao().getDocument(encounterId, documentName);
						if (documentBean == null)
						{
							int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
							Date utcDateTime = DateUtils.getUTCDateTime();

							documentBean = new DocumentBean();
							documentBean.setEncounterNumber(encounterBean.getEncounterNumber());
							documentBean.setEncounterId(encounterId);
							documentBean.setFileFilter(CommonConstant.TRANSCRIBED_DOCUMENT_FILE_FILTER);
							documentBean.setActiveRecordFlag(0);
							documentBean.setCreatorUserId(hl7UserId);
							documentBean.setCreatorDateTime(utcDateTime);
							documentBean.setUpdaterUserId(hl7UserId);
							documentBean.setUpdationDateTime(utcDateTime);
							setDocumentBean(documentBean, document, hospitalTimeZodeId);

							LOGGER.info("Inserting document. encounterId : " + encounterId + " documentName : " + documentName);
							getHl7DaoFactory().getHl7Dao().insertDocument(documentBean);
							int documentId = documentBean.getId();
							LOGGER.info("Document inserted successfully. documentId : " + documentId);

							List<Map<String, Object>> documentPhysicianList = (List<Map<String, Object>>) document
									.get(HL7Info.DOCUMENT_PHYSICIAN_LIST);
							insertDocumentPhysician(documentPhysicianList, documentId);

							String tenantCode = hl7InfoMap.get(HL7Info.TENANT_CODE).toString();
							String bucketPath = HL7PropertiesUtils.getAWSS3OriginalTextBucketPath(tenantCode);
							String documentContent = document.get(HL7Info.DOCUMENT_CONTENT).toString();

							AmazonS3Service amazonS3Service = AmazonS3Service.getInstance(true, HL7PropertiesUtils.getAWSAccessKey(),
									HL7PropertiesUtils.getAWSSecretKey(), HL7PropertiesUtils.getAWSRegion());
							LOGGER.info("Uploading document. documentName : " + documentBean.getName() + "tenantCode : " + tenantCode
									+ " bucketPath : " + bucketPath);
							amazonS3Service.uploadData(bucketPath, documentBean.getName(), documentContent);
							LOGGER.info("Document uploded successfully.");

							sendMessageToSQS(documentBean.getName(), tenantCode, 1);
						} else
						{
							throw new Exception("Document already exists. patientId : " + patientId + " , encounterNumber : "
									+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
						}
					}
				} else
				{
					throw new Exception("Case is already billed. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
							+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
				}
			} else
			{
				throw new Exception("Case does not exists. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
						+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			}
		}
		LOGGER.debug("Exiting from addDocument.");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void addScannedDocument(Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.debug("Inside addaddScannedDocument.");
		if (isValidEncounter(hl7InfoMap.get(HL7Info.ENCOUNTER)))
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			LOGGER.info("Checking case. mrn : " + mrn + " encounterNumber : " + encounterNumber);
			EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
			if (encounterBean != null)
			{
				int encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status. encounterId : " + encounterId);
				boolean isEncounterBilled = getHl7DaoFactory().getHl7Dao().isEncounterBilled(encounterId);
				LOGGER.info("Encounter billed status. isEncounterBilled : " + isEncounterBilled);
				if (!isEncounterBilled)
				{
					Map<String, String> tenantConfigMap = getHl7DaoFactory().getHl7Dao().getTenantConfigMap();
					String hospitalTimeZodeId = tenantConfigMap.get(TenantConfigConstant.HOSPITAL_TIMEZONE_ID);

					PatientBean patientBean = getHl7DaoFactory().getHl7Dao().getPatient(mrn);
					int patientId = patientBean.getId();

					if (isValidDocument(hl7InfoMap.get(HL7Info.DOCUMENT)))
					{
						Map<String, Object> document = (Map<String, Object>) hl7InfoMap.get(HL7Info.DOCUMENT);
						String documentName = document.get(HL7Info.DOCUMENT_NAME).toString();

						LOGGER.info("Checking document. encounterId : " + encounterId + " documentName : " + documentName);
						DocumentBean documentBean = getHl7DaoFactory().getHl7Dao().getDocument(encounterId, documentName);
						if (documentBean == null)
						{
							int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
							Date utcDateTime = DateUtils.getUTCDateTime();

							documentBean = new DocumentBean();
							documentBean.setEncounterNumber(encounterBean.getEncounterNumber());
							documentBean.setEncounterId(encounterId);
							documentBean.setFileFilter(CommonConstant.SCANNED_DOCUMENT_FILE_FILTER);
							documentBean.setActiveRecordFlag(CommonConstant.ACTIVE_DOCUMENT);
							documentBean.setCreatorUserId(hl7UserId);
							documentBean.setCreatorDateTime(utcDateTime);
							documentBean.setUpdaterUserId(hl7UserId);
							documentBean.setUpdationDateTime(utcDateTime);
							setDocumentBean(documentBean, document, hospitalTimeZodeId);

							LOGGER.info("Inserting document. encounterId : " + encounterId + " documentName : " + documentName);
							getHl7DaoFactory().getHl7Dao().insertDocument(documentBean);
							int documentId = documentBean.getId();
							LOGGER.info("Document inserted successfully. documentId : " + documentId);

							String tenantCode = hl7InfoMap.get(HL7Info.TENANT_CODE).toString();
							String bucketPath = HL7PropertiesUtils.getAWSS3ScannedDocumentBucketPath(tenantCode);
							String documentContent = document.get(HL7Info.DOCUMENT_CONTENT).toString();

							AmazonS3Service amazonS3Service = AmazonS3Service.getInstance(true, HL7PropertiesUtils.getAWSAccessKey(),
									HL7PropertiesUtils.getAWSSecretKey(), HL7PropertiesUtils.getAWSRegion());
							LOGGER.info("Uploading document. documentName : " + documentBean.getName() + "tenantCode : " + tenantCode
									+ " bucketPath : " + bucketPath);
							amazonS3Service.uploadData(bucketPath, documentBean.getName(), Base64.decodeBase64(documentContent.getBytes()));
							LOGGER.info("Document uploded successfully.");

							insertNewDocumentArrivalEvent(encounterId, documentBean.getTypeName(), hl7UserId, utcDateTime);

							updateEncounterStatusOnNewDocumentArrivalForReadyNotReady(encounterId, encounterBean.getPatientClassId(),
									encounterBean.getServiceLineId(), documentBean.getTypeId(), hl7UserId, utcDateTime);

						} else
						{
							throw new Exception("Document already exists. patientId : " + patientId + " , encounterNumber : "
									+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
						}
					}
				} else
				{
					throw new Exception("Case is already billed. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
							+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
				}
			} else
			{
				throw new Exception("Case does not exists. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
						+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			}
		}
		LOGGER.debug("Exiting from addDocument.");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void addObservation(Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.debug("Inside addObservation.");
		if (isValidEncounter(hl7InfoMap.get(HL7Info.ENCOUNTER)))
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			LOGGER.info("Checking case. mrn : " + mrn + " encounterNumber : " + encounterNumber);
			EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
			if (encounterBean != null)
			{
				int encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status. encounterId : " + encounterId);
				boolean isEncounterBilled = getHl7DaoFactory().getHl7Dao().isEncounterBilled(encounterId);
				LOGGER.info("Encounter billed status. isEncounterBilled : " + isEncounterBilled);
				if (!isEncounterBilled)
				{
					int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
					Date utcDateTime = DateUtils.getUTCDateTime();
					Map<String, String> tenantConfigMap = getHl7DaoFactory().getHl7Dao().getTenantConfigMap();
					String hospitalTimeZodeId = tenantConfigMap.get(TenantConfigConstant.HOSPITAL_TIMEZONE_ID);

					if (encounter.containsKey(HL7Info.OBSERVATION_LIST) && encounter.get(HL7Info.OBSERVATION_LIST) != null)
					{
						List<Map<String, Object>> observationList = (List<Map<String, Object>>) encounter.get(HL7Info.OBSERVATION_LIST);
						insertOrUpdateObservation(observationList, encounterBean, hl7UserId, utcDateTime, hospitalTimeZodeId);
					}
				} else
				{
					throw new Exception("Case is already billed. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
							+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
				}
			} else
			{
				throw new Exception("Case does not exists. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
						+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			}
		}
		LOGGER.debug("Exiting from addObservation.");
	}

	//////////////////////////////////
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void swapPatientLocation(Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.debug("Inside swapPatientLocation.");
		
		HL7Dao hl7DaoImpl;
		List<Map<String, Object>> encounterBeans= (List<Map<String, Object>>) hl7InfoMap.get(HL7Info.ENCOUNTER_LIST);
		
		if (isValidEncounter(encounterBeans.get(0)) && isValidEncounter(encounterBeans.get(1)))
		{
			
			Map<String, Object> encounter = encounterBeans.get(0);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			Map<String, Object> encounter2 = encounterBeans.get(1);
			String mrn2 = encounter2.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber2 = encounter2.get(HL7Info.ENCOUNTER_NUMBER).toString();

			
			boolean isEncounterBilled = false ;
			boolean isEncounterBilled2 = false ;
			hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
			EncounterBean encounterBean = hl7DaoImpl.getEncounter(mrn, encounterNumber);
			EncounterBean encounterBean2 = hl7DaoImpl.getEncounter(mrn2, encounterNumber2);
			int encounterId = 0;
			int encounterId2 = 0;
			int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
			Date utcDateTime = DateUtils.getUTCDateTime();
			if (encounterBean != null && encounterBean2 != null)
			{
				encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status . encounterId : " + encounterId);
				isEncounterBilled = hl7DaoImpl.isEncounterBilled(encounterId);
				LOGGER.info("Case billing status. isEncounterBilled : " + isEncounterBilled);
				
				encounterId2 = encounterBean2.getId();
				LOGGER.info("Checking case billing status . encounterId : " + encounterId2);
				isEncounterBilled2 = hl7DaoImpl.isEncounterBilled(encounterId2);
				LOGGER.info("Case billing status. isEncounterBilled : " + isEncounterBilled2);
			}

			if (!isEncounterBilled && !isEncounterBilled2)
			{
				encounterBean.setUpdaterUserId(hl7UserId);
				encounterBean.setUpdationDateTime(utcDateTime);
				encounterBean.setIsActive(CommonConstant.ACTIVE_ENCOUNTER);
				
				encounterBean.setPointOfCare(encounter2.get(HL7Info.PATIENT_POINT_OF_CARE).toString());
				encounterBean.setRoom(encounter2.get(HL7Info.PATIENT_ROOM).toString());
				encounterBean.setBed(encounter2.get(HL7Info.PATIENT_BED).toString());
				encounterBean.setFacility(encounter2.get(HL7Info.FACILITY).toString());
				encounterBean.setLocationStatus(encounter2.get(HL7Info.PATIENT_LOCATION_STATUS).toString());
				encounterBean.setPersonLocationType(encounter2.get(HL7Info.PATIENT_PERSON_LOCATION_TYPE).toString());;
				encounterBean.setBuilding(encounter2.get(HL7Info.PATIENT_BUILDING).toString());
				encounterBean.setFloor(encounter2.get(HL7Info.PATIENT_FLOOR).toString());
			
				encounterBean2.setUpdaterUserId(hl7UserId);
				encounterBean2.setUpdationDateTime(utcDateTime);
				encounterBean2.setIsActive(CommonConstant.ACTIVE_ENCOUNTER);
				
				encounterBean2.setPointOfCare(encounter.get(HL7Info.PATIENT_POINT_OF_CARE).toString());
				encounterBean2.setRoom(encounter.get(HL7Info.PATIENT_ROOM).toString());
				encounterBean2.setBed(encounter.get(HL7Info.PATIENT_BED).toString());
				encounterBean2.setFacility(encounter.get(HL7Info.FACILITY).toString());
				encounterBean2.setLocationStatus(encounter.get(HL7Info.PATIENT_LOCATION_STATUS).toString());
				encounterBean2.setPersonLocationType(encounter.get(HL7Info.PATIENT_PERSON_LOCATION_TYPE).toString());;
				encounterBean2.setBuilding(encounter.get(HL7Info.PATIENT_BUILDING).toString());
				encounterBean2.setFloor(encounter.get(HL7Info.PATIENT_FLOOR).toString());
			
				
				LOGGER.info("Updating encounter information. encounterId : " + encounterId);
				hl7DaoImpl.updateEncounter(encounterBean);
				hl7DaoImpl.updateEncounter(encounterBean2);
				LOGGER.info("Encounter information updated successfully.");
				

				LOGGER.info("Inserting into encounter history. encounterId : " + encounterId);
				EncounterHistoryBean bean = createEncounterHistory(encounterBean);
				hl7DaoImpl.insertEncounterHistory(bean);
				EncounterHistoryBean bean2 = createEncounterHistory(encounterBean2);
				hl7DaoImpl.insertEncounterHistory(bean2);
				LOGGER.info("EncounterHistory inserted successfully. encounterId : " + encounterId);
				

				LOGGER.info("Now publishing Update Index Event for encounterId: " + encounterId);
				notifyUpdateToIndexById(encounterId, EncounterBean.class);
				notifyUpdateToIndexById(encounterId2, EncounterBean.class);
				LOGGER.info("Published Index Update Event for encounterId: " + encounterId);

			} else
			{
				throw new Exception("Eithe One or Both Case are already billed.");
			}
		}
		LOGGER.debug("Exiting from swapPatientLocation.");
	}
	
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void mergePatientInformation(Map<String, Object> hl7InfoMap) throws Exception
	{
		HL7Dao hl7DaoImpl;
		LOGGER.debug("Inside mergePatientInformation.");
		if (isValidEncounter(hl7InfoMap.get(HL7Info.ENCOUNTER)))
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

			boolean isEncounterBilled = false;
			hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
			EncounterBean encounterBean = hl7DaoImpl.getEncounter(mrn, encounterNumber);
			int encounterId = 0;
			if (encounterBean != null)
			{
				encounterId = encounterBean.getId();
				LOGGER.info("Checking case billing status. encounterId : " + encounterId);
				isEncounterBilled = hl7DaoImpl.isEncounterBilled(encounterId);
				LOGGER.info("Case billing status. isEncounterBilled : " + isEncounterBilled);
			}
			if (!isEncounterBilled)
			{
				int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				Date utcDateTime = DateUtils.getUTCDateTime();
				if (encounterBean != null)
				{
					
					PatientBean patientBean = getHl7DaoFactory().getHl7Dao().getPatient(mrn);
					int patientId = patientBean.getId();
					patientBean.setMrn(encounter.get(HL7Info.ENCOUNTER_PATIENT_NEW_MRN).toString());
					patientBean.setUpdaterUserId(hl7UserId);
					patientBean.setUpdationDateTime(utcDateTime);
					
					LOGGER.info("Now publishing Update Index Event for patientId: " + patientId);
					notifyUpdateToIndexById(patientId, PatientBean.class);
					LOGGER.info("Published Index Update Event for patientId: " + patientId);
					
					encounterBean.setPatientMrn(encounter.get(HL7Info.ENCOUNTER_PATIENT_NEW_MRN).toString());
					encounterBean.setUpdaterUserId(hl7UserId);
					encounterBean.setUpdationDateTime(utcDateTime);
					encounterBean.setIsActive(CommonConstant.ACTIVE_ENCOUNTER);
					
					LOGGER.info("Updating encounter information. encounterId : " + encounterId);
					hl7DaoImpl.updateEncounter(encounterBean);
					LOGGER.info("Encounter information updated successfully.");

					LOGGER.info("Inserting into encounter history. encounterId : " + encounterId);
					EncounterHistoryBean bean = createEncounterHistory(encounterBean);
					hl7DaoImpl.insertEncounterHistory(bean);
					LOGGER.info("EncounterHistory inserted successfully. encounterId : " + encounterId);

					LOGGER.info("Now publishing Update Index Event for encounterId: " + encounterId);
					notifyUpdateToIndexById(encounterId, EncounterBean.class);
					LOGGER.info("Published Index Update Event for encounterId: " + encounterId);
				}
			} else
			{
				throw new Exception("Case is already billed. mrn : " + encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString() + ", encounter : "
						+ encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			}
		}
		LOGGER.debug("Exiting from mergePatientInformation.");

	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void lockEncounter(Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.debug("Inside lockEncounter.");
		String action = (String) hl7InfoMap.get(HL7Info.ACTION);
		if(action.equals(HL7Action.SWAP_PATIENT_LOCATION))
		{
			List<Map<String, Object>> encounterList =  (List<Map<String, Object>>) hl7InfoMap.get(HL7Info.ENCOUNTER_LIST); 
			for(Map<String, Object> encounter : encounterList)
			{
				String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
				String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();
	
				EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
				if (encounterBean != null)
				{
					int encounterId = encounterBean.getId();
					String lockBy = encounterBean.getLockBy();
					if (EncounterLockByConstant.UNLOCKED.equals(lockBy))
					{
						LOGGER.info("Locking encounter. encounterId : " + encounterId + " lockBy : " + lockBy);
						int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
						Date utcDateTime = DateUtils.getUTCDateTime();
	
						encounterBean.setLockBy(EncounterLockByConstant.HL7);
						encounterBean.setUpdaterUserId(hl7UserId);
						encounterBean.setUpdationDateTime(utcDateTime);
						getHl7DaoFactory().getHl7Dao().updateEncounter(encounterBean);
						LOGGER.info("Encounter locked successfull.");
	
						LOGGER.info("Now publishing  Update Index Event for encounterId: " + encounterId);
						notifyUpdateToIndexById(encounterId, EncounterBean.class);
						LOGGER.info("Published Index Update  Event for encounterId: " + encounterId);
					}
				}
			}
		}else
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();
	
			EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
			if (encounterBean != null)
			{
				int encounterId = encounterBean.getId();
				String lockBy = encounterBean.getLockBy();
				if (EncounterLockByConstant.UNLOCKED.equals(lockBy))
				{
					LOGGER.info("Locking encounter. encounterId : " + encounterId + " lockBy : " + lockBy);
					int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
					Date utcDateTime = DateUtils.getUTCDateTime();
	
					encounterBean.setLockBy(EncounterLockByConstant.HL7);
					encounterBean.setUpdaterUserId(hl7UserId);
					encounterBean.setUpdationDateTime(utcDateTime);
					getHl7DaoFactory().getHl7Dao().updateEncounter(encounterBean);
					LOGGER.info("Encounter locked successfull.");
	
					LOGGER.info("Now publishing  Update Index Event for encounterId: " + encounterId);
					notifyUpdateToIndexById(encounterId, EncounterBean.class);
					LOGGER.info("Published Index Update  Event for encounterId: " + encounterId);
				}
			}
		}
		LOGGER.debug("Exiting from lockEncounter.");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	public void unLockEncounter(Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.debug("Inside unLockEncounter.");
		String action = (String) hl7InfoMap.get(HL7Info.ACTION);
		if(action.equals(HL7Action.SWAP_PATIENT_LOCATION))
		{
			List<Map<String, Object>> encounterList =  (List<Map<String, Object>>) hl7InfoMap.get(HL7Info.ENCOUNTER_LIST); 
			for(Map<String, Object> encounter : encounterList)
			{
				String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
				String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();

				EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
				if (encounterBean != null)
				{
					int encounterId = encounterBean.getId();
					String lockBy = encounterBean.getLockBy();
					if (EncounterLockByConstant.HL7.equals(lockBy))
					{
						LOGGER.info("Unlocking encounter. encounterId : " + encounterId + " lockBy : " + lockBy);
						int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
						Date utcDateTime = DateUtils.getUTCDateTime();

						encounterBean.setLockBy(EncounterLockByConstant.UNLOCKED);
						encounterBean.setUpdaterUserId(hl7UserId);
						encounterBean.setUpdationDateTime(utcDateTime);

						getHl7DaoFactory().getHl7Dao().updateEncounter(encounterBean);
						LOGGER.info("Encounter unlocked successfull.");

						LOGGER.info("Now publishing  Update Index Event for encounterId: " + encounterId);
						notifyUpdateToIndexById(encounterId, EncounterBean.class);
						LOGGER.info("Published Index Update  Event for encounterId: " + encounterId);
					}
				}
			}
		}else
		{
			Map<String, Object> encounter = (Map<String, Object>) hl7InfoMap.get(HL7Info.ENCOUNTER);
			String mrn = encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString();
			String encounterNumber = encounter.get(HL7Info.ENCOUNTER_NUMBER).toString();
	
			EncounterBean encounterBean = getHl7DaoFactory().getHl7Dao().getEncounter(mrn, encounterNumber);
			if (encounterBean != null)
			{
				int encounterId = encounterBean.getId();
				String lockBy = encounterBean.getLockBy();
				if (EncounterLockByConstant.HL7.equals(lockBy))
				{
					LOGGER.info("Unlocking encounter. encounterId : " + encounterId + " lockBy : " + lockBy);
					int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
					Date utcDateTime = DateUtils.getUTCDateTime();
	
					encounterBean.setLockBy(EncounterLockByConstant.UNLOCKED);
					encounterBean.setUpdaterUserId(hl7UserId);
					encounterBean.setUpdationDateTime(utcDateTime);
	
					getHl7DaoFactory().getHl7Dao().updateEncounter(encounterBean);
					LOGGER.info("Encounter unlocked successfull.");
	
					LOGGER.info("Now publishing  Update Index Event for encounterId: " + encounterId);
					notifyUpdateToIndexById(encounterId, EncounterBean.class);
					LOGGER.info("Published Index Update  Event for encounterId: " + encounterId);
				}
			}
		}
		LOGGER.debug("Exiting from unLockEncounter.");
	}

	private PatientBean setPatientBean(PatientBean patientBean, Map<String, Object> encounter, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside setPatientBean.");
		if (patientBean != null)
		{
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_MRN) && encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN) != null)
			{
				patientBean.setMrn(encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString());
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_FIRST_NAME) && encounter.get(HL7Info.ENCOUNTER_PATIENT_FIRST_NAME) != null)
			{
				patientBean.setFirstName(encounter.get(HL7Info.ENCOUNTER_PATIENT_FIRST_NAME).toString());
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_LAST_NAME) && encounter.get(HL7Info.ENCOUNTER_PATIENT_LAST_NAME) != null)
			{
				patientBean.setLastName(encounter.get(HL7Info.ENCOUNTER_PATIENT_LAST_NAME).toString());
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_DATE_OF_BIRTH) && encounter.get(HL7Info.ENCOUNTER_PATIENT_DATE_OF_BIRTH) != null)
			{
				String dateOfBirthString = encounter.get(HL7Info.ENCOUNTER_PATIENT_DATE_OF_BIRTH).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(dateOfBirthString, hospitalTimeZodeId);
				patientBean.setDateOfBirth(utcDateTime);
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_GENDER) && encounter.get(HL7Info.ENCOUNTER_PATIENT_GENDER) != null)
			{
				patientBean.setGender(encounter.get(HL7Info.ENCOUNTER_PATIENT_GENDER).toString());
			}
		}
		LOGGER.debug("Exiting from setPatientBean.");
		return patientBean;
	}

	private EncounterBean setEncounterBean(EncounterBean encounterBean, Map<String, Object> encounter, String hospitalName, String hospitalTimeZodeId)
			throws Exception
	{
		LOGGER.debug("Inside setEncounterBean.");
		if (encounterBean != null)
		{
			encounterBean.setEncounterNumber(encounter.get(HL7Info.ENCOUNTER_NUMBER).toString());
			encounterBean.setHospitalName(hospitalName);
			LocationBean locationBean = null;
			if (encounter.containsKey(HL7Info.ENCOUNTER_LOCATION) && encounter.get(HL7Info.ENCOUNTER_LOCATION) != null)
			{
				locationBean = getLocation(encounter.get(HL7Info.ENCOUNTER_LOCATION).toString());
				encounterBean.setLocationId(locationBean.getId());
				encounterBean.setLocationName(locationBean.getCode());
			} else
			{
				locationBean = getHl7DaoFactory().getHl7Dao().getLocation(encounterBean.getLocationName());
			}

			PatientClassBean patientClassBean;
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_CLASS) && encounter.get(HL7Info.ENCOUNTER_PATIENT_CLASS) != null)
			{
				patientClassBean = getPatientClass(encounter.get(HL7Info.ENCOUNTER_PATIENT_CLASS).toString());
				encounterBean.setPatientClassId(patientClassBean.getId());
				encounterBean.setPatientClassCode(patientClassBean.getCode());
				encounterBean.setPatientClassDesc(patientClassBean.getDesc());
			} else
			{
				patientClassBean = getHl7DaoFactory().getHl7Dao().getPatientClass(encounterBean.getPatientClassCode());
			}
			LocationPatientClassBean locationPatientClassBean = getLocationPatientClass(locationBean, patientClassBean);

			Integer patientTypeId = null;
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_TYPE) && encounter.get(HL7Info.ENCOUNTER_PATIENT_TYPE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_PATIENT_TYPE).toString()))
				{
					encounterBean.setPatientTypeId(null);
					encounterBean.setPatientTypeCode(null);
				} else
				{
					PatientTypeBean patientTypeBean = getPatientType(encounter.get(HL7Info.ENCOUNTER_PATIENT_TYPE).toString());
					encounterBean.setPatientTypeId(patientTypeBean.getId());
					encounterBean.setPatientTypeCode(patientTypeBean.getCode());
					patientTypeId = patientTypeBean.getId();
				}

			} else
			{
				patientTypeId = encounterBean.getPatientTypeId();
			}

			Integer serviceLineId = null;
			if (encounter.containsKey(HL7Info.ENCOUNTER_SERVICE_LINE) && encounter.get(HL7Info.ENCOUNTER_SERVICE_LINE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_SERVICE_LINE).toString()))
				{
					encounterBean.setServiceLineId(null);
					encounterBean.setServiceLineCode(null);
					encounterBean.setServiceLineDesc(null);
				} else
				{
					ServiceLineBean serviceLineBean = getServiceLine(encounter.get(HL7Info.ENCOUNTER_SERVICE_LINE).toString());
					encounterBean.setServiceLineId(serviceLineBean.getId());
					encounterBean.setServiceLineCode(serviceLineBean.getCode());
					encounterBean.setServiceLineDesc(serviceLineBean.getDesc());
					serviceLineId = serviceLineBean.getId();
				}
			} else
			{
				serviceLineId = encounterBean.getServiceLineId();
			}

			getLocationPatientClassTypeServiceLine(locationPatientClassBean.getId(), patientTypeId, serviceLineId);

			if (encounter.containsKey(HL7Info.ENCOUNTER_DISCHARGE_DISPOSITION) && encounter.get(HL7Info.ENCOUNTER_DISCHARGE_DISPOSITION) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_DISCHARGE_DISPOSITION).toString()))
				{
					encounterBean.setDischargeDispositionId(null);
					encounterBean.setDischargeDispositionCode(null);
					encounterBean.setDischargeDispositionDesc(null);
				} else
				{
					DischargeDispositionBean dischargeDispositionBean = getHl7DaoFactory().getHl7Dao().getDischargeDisposition(
							encounter.get(HL7Info.ENCOUNTER_DISCHARGE_DISPOSITION).toString());
					if (dischargeDispositionBean != null)
					{
						encounterBean.setDischargeDispositionId(dischargeDispositionBean.getId());
						encounterBean.setDischargeDispositionCode(dischargeDispositionBean.getCode());
						encounterBean.setDischargeDispositionDesc(dischargeDispositionBean.getDesc());
					} else
					{
						throw new Exception("Unknown dischargeDisposition. dischargeDisposition : "
								+ encounter.get(HL7Info.ENCOUNTER_DISCHARGE_DISPOSITION).toString());
					}
				}
			}

			if (encounter.containsKey(HL7Info.ENCOUNTER_ADMIT_SOURCE) && encounter.get(HL7Info.ENCOUNTER_ADMIT_SOURCE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_ADMIT_SOURCE).toString()))
				{
					encounterBean.setAdmitSourceCode(null);
				} else
				{
					encounterBean.setAdmitSourceCode(encounter.get(HL7Info.ENCOUNTER_ADMIT_SOURCE).toString());
				}

			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_DATE_OF_ADMISSION) && encounter.get(HL7Info.ENCOUNTER_DATE_OF_ADMISSION) != null)
			{
				String dateOfAdmissionString = encounter.get(HL7Info.ENCOUNTER_DATE_OF_ADMISSION).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(dateOfAdmissionString, hospitalTimeZodeId);
				encounterBean.setDateOfAdmition(utcDateTime);
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_DATE_OF_DISCHARGE) && encounter.get(HL7Info.ENCOUNTER_DATE_OF_DISCHARGE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_DATE_OF_DISCHARGE).toString()))
				{
					encounterBean.setDateOfDischarge(null);
				} else
				{
					String dateOfDischargeString = encounter.get(HL7Info.ENCOUNTER_DATE_OF_DISCHARGE).toString();
					Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(dateOfDischargeString, hospitalTimeZodeId);
					encounterBean.setDateOfDischarge(utcDateTime);
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_ACCOUNT_NUMBER) && encounter.get(HL7Info.ENCOUNTER_ACCOUNT_NUMBER) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_ACCOUNT_NUMBER).toString()))
				{
					encounterBean.setAccountNumber(null);
				} else
				{
					encounterBean.setAccountNumber(encounter.get(HL7Info.ENCOUNTER_ACCOUNT_NUMBER).toString());
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_TOTAL_CHARGES) && encounter.get(HL7Info.ENCOUNTER_TOTAL_CHARGES) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_TOTAL_CHARGES).toString()))
				{
					encounterBean.setTotalCharges(null);
				} else
				{
					encounterBean.setTotalCharges(encounter.get(HL7Info.ENCOUNTER_TOTAL_CHARGES).toString());
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_BILL_TYPE) && encounter.get(HL7Info.ENCOUNTER_BILL_TYPE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_BILL_TYPE).toString()))
				{
					encounterBean.setBillType(null);
				} else
				{
					encounterBean.setBillType(encounter.get(HL7Info.ENCOUNTER_BILL_TYPE).toString());
				}
			}

			if (encounter.containsKey(HL7Info.ENCOUNTER_PAYER) && encounter.get(HL7Info.ENCOUNTER_PAYER) != null)
			{
				PayerBean payerBean = insertPayer(encounter);
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_PAYER).toString()))
				{
					encounterBean.setPayerFlag(false);
					encounterBean.setPayer(null);
					encounterBean.setPayerId(null);
					encounterBean.setPayerDesc(null);
				} else
				{
					encounterBean.setPayerFlag(true);
					encounterBean.setPayerId(payerBean.getId());
					encounterBean.setPayer(payerBean.getCode());
					encounterBean.setPayerDesc(payerBean.getDesc());
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_NEW_BORN_WEIGHT) && encounter.get(HL7Info.ENCOUNTER_NEW_BORN_WEIGHT) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_NEW_BORN_WEIGHT).toString()))
				{
					encounterBean.setNewBornWeight(null);
				} else
				{
					encounterBean.setNewBornWeight(encounter.get(HL7Info.ENCOUNTER_NEW_BORN_WEIGHT).toString());
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_FINANCIAL_CLASSES) && encounter.get(HL7Info.ENCOUNTER_FINANCIAL_CLASSES) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_FINANCIAL_CLASSES).toString()))
				{
					encounterBean.setFinancialClasses(null);
				} else
				{
					encounterBean.setFinancialClasses(encounter.get(HL7Info.ENCOUNTER_FINANCIAL_CLASSES).toString());
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_MRN) && encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN) != null)
			{
				encounterBean.setPatientMrn(encounter.get(HL7Info.ENCOUNTER_PATIENT_MRN).toString());
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_FIRST_NAME) && encounter.get(HL7Info.ENCOUNTER_PATIENT_FIRST_NAME) != null)
			{
				encounterBean.setPatientFirstName(encounter.get(HL7Info.ENCOUNTER_PATIENT_FIRST_NAME).toString());
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_LAST_NAME) && encounter.get(HL7Info.ENCOUNTER_PATIENT_LAST_NAME) != null)
			{
				encounterBean.setPatientLastName(encounter.get(HL7Info.ENCOUNTER_PATIENT_LAST_NAME).toString());
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_DATE_OF_BIRTH) && encounter.get(HL7Info.ENCOUNTER_PATIENT_DATE_OF_BIRTH) != null)
			{
				String dateOfBirthString = encounter.get(HL7Info.ENCOUNTER_PATIENT_DATE_OF_BIRTH).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(dateOfBirthString, hospitalTimeZodeId);
				encounterBean.setPatientDateOfBirth(utcDateTime);
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_PATIENT_GENDER) && encounter.get(HL7Info.ENCOUNTER_PATIENT_GENDER) != null)
			{
				encounterBean.setPatientGender(encounter.get(HL7Info.ENCOUNTER_PATIENT_GENDER).toString());
			}

			if (encounter.containsKey(HL7Info.PATIENT_BUILDING) && encounter.get(HL7Info.PATIENT_BUILDING) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.PATIENT_BUILDING).toString()))
				{
					encounterBean.setBuilding(null);
				} else
				{
					encounterBean.setBuilding(encounter.get(HL7Info.PATIENT_BUILDING).toString());
				}
			}
			if (encounter.containsKey(HL7Info.PATIENT_FLOOR) && encounter.get(HL7Info.PATIENT_FLOOR) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.PATIENT_FLOOR).toString()))
				{
					encounterBean.setFloor(null);
				} else
				{
					encounterBean.setFloor(encounter.get(HL7Info.PATIENT_FLOOR).toString());
				}
			}
			if (encounter.containsKey(HL7Info.PATIENT_POINT_OF_CARE) && encounter.get(HL7Info.PATIENT_POINT_OF_CARE) != null)
			{
				encounterBean.setPointOfCare(encounter.get(HL7Info.PATIENT_POINT_OF_CARE).toString());
			}

			if (encounter.containsKey(HL7Info.PATIENT_ROOM) && encounter.get(HL7Info.PATIENT_ROOM) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.PATIENT_ROOM).toString()))
				{
					encounterBean.setRoom(null);
				} else
				{
					encounterBean.setRoom(encounter.get(HL7Info.PATIENT_ROOM).toString());
				}
			}
			if (encounter.containsKey(HL7Info.PATIENT_BED) && encounter.get(HL7Info.PATIENT_BED) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.PATIENT_BED).toString()))
				{
					encounterBean.setBed(null);
				} else
				{
					encounterBean.setBed(encounter.get(HL7Info.PATIENT_BED).toString());
				}
			}
			if (encounter.containsKey(HL7Info.FACILITY) && encounter.get(HL7Info.FACILITY) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.FACILITY).toString()))
				{
					encounterBean.setFacility(null);
				} else
				{
					encounterBean.setFacility(encounter.get(HL7Info.FACILITY).toString());
				}
			}

			if (encounter.containsKey(HL7Info.PATIENT_LOCATION_STATUS) && encounter.get(HL7Info.PATIENT_LOCATION_STATUS) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.PATIENT_LOCATION_STATUS).toString()))
				{
					encounterBean.setLocationStatus(null);
				} else
				{
					encounterBean.setLocationStatus(encounter.get(HL7Info.PATIENT_LOCATION_STATUS).toString());
				}
			}

			if (encounter.containsKey(HL7Info.PATIENT_PERSON_LOCATION_TYPE) && encounter.get(HL7Info.PATIENT_PERSON_LOCATION_TYPE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.PATIENT_PERSON_LOCATION_TYPE).toString()))
				{
					encounterBean.setPersonLocationType(null);
				} else
				{
					encounterBean.setPersonLocationType(encounter.get(HL7Info.PATIENT_PERSON_LOCATION_TYPE).toString());
				}
			}
			if (encounter.containsKey(HL7Info.ENCOUNTER_ADMISSION_TYPE) && encounter.get(HL7Info.ENCOUNTER_ADMISSION_TYPE) != null)
			{
				if (CommonConstant.HL7_NULL_FIELD.equals(encounter.get(HL7Info.ENCOUNTER_ADMISSION_TYPE).toString()))
				{
					encounterBean.setAdmissionType(null);
				} else
				{
					encounterBean.setAdmissionType(encounter.get(HL7Info.ENCOUNTER_ADMISSION_TYPE).toString());
				}

			}
			LOGGER.debug("Exiting from setEncounterBean.");
		}
		return encounterBean;
	}

	private EncounterPhysicianBean setEncounterPhysicianBean(EncounterPhysicianBean encounterPhysicianBean, Map<String, Object> physician)
			throws Exception
	{
		LOGGER.debug("Inside setEncounterPhysicianBean.");
		if (encounterPhysicianBean != null)
		{
			if (physician.containsKey(HL7Info.PHYSICIAN_GLOBAL_ID) && physician.get(HL7Info.PHYSICIAN_GLOBAL_ID) != null)
			{
				int physicianLoginId = getHl7DaoFactory().getHl7Dao().getPhysicianLoginId(physician.get(HL7Info.PHYSICIAN_GLOBAL_ID).toString());
				if (physicianLoginId != 0)
				{
					encounterPhysicianBean.setPhysicianLoginId(physicianLoginId);
				} else
				{
					throw new Exception("Unknown physician. physicianId : " + physician.get(HL7Info.PHYSICIAN_GLOBAL_ID).toString());
				}
			}
			if (physician.containsKey(HL7Info.PHYSICIAN_TYPE) && physician.get(HL7Info.PHYSICIAN_TYPE) != null)
			{
				encounterPhysicianBean.setPhysicianTypeCode(physician.get(HL7Info.PHYSICIAN_TYPE).toString());
			}
		}
		LOGGER.debug("Exiting from setEncounterPhysicianBean.");
		return encounterPhysicianBean;
	}

	private DocumentBean setObservationDocumentBean(DocumentBean documentBean, Map<String, Object> observationRequest, String hospitalTimeZodeId)
			throws Exception
	{
		LOGGER.debug("Inside setHL7CodeDocumentBean.");
		if (documentBean != null)
		{
			DocumentTypeBean documentTypeBean = getDocumentType(CommonConstant.OBSERVATION_DOCUMENT_TYPE);
			documentBean.setTypeId(documentTypeBean.getId());
			documentBean.setTypeName(documentTypeBean.getName());

			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_DATE_TIME)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_DATE_TIME) != null)
			{
				String observationDateTimeString = observationRequest.get(HL7Info.OBSERVATION_REQUEST_DATE_TIME).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(observationDateTimeString, hospitalTimeZodeId);
				documentBean.setDateOfService(utcDateTime);
			}
			documentBean.setFileFilter(CommonConstant.OBSERVATION_DOCUMENT_FILE_FILTER);
		}
		LOGGER.debug("Exiting from setHL7CodeDocumentBean.");
		return documentBean;
	}

	private DocumentBean setDocumentBean(DocumentBean documentBean, Map<String, Object> document, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside setDocumentBean.");
		if (documentBean != null)
		{
			documentBean.setName(document.get(HL7Info.DOCUMENT_NAME).toString());
			if (document.containsKey(HL7Info.DOCUMENT_DATE_OF_SERVICE) && document.get(HL7Info.DOCUMENT_DATE_OF_SERVICE) != null)
			{
				String dateOfServiceString = document.get(HL7Info.DOCUMENT_DATE_OF_SERVICE).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(dateOfServiceString, hospitalTimeZodeId);
				documentBean.setDateOfService(utcDateTime);
			}
			if (document.containsKey(HL7Info.DOCUMENT_TYPE) && document.get(HL7Info.DOCUMENT_TYPE) != null)
			{
				DocumentTypeBean documentTypeBean = getDocumentType(document.get(HL7Info.DOCUMENT_TYPE).toString());
				documentBean.setTypeId(documentTypeBean.getId());
				documentBean.setTypeName(documentTypeBean.getName());
			}
			if (document.containsKey(HL7Info.DOCUMENT_CONTENT_TYPE) && document.get(HL7Info.DOCUMENT_CONTENT_TYPE) != null)
			{
				documentBean.setContentType(document.get(HL7Info.DOCUMENT_CONTENT_TYPE).toString());
			}
			if (document.containsKey(HL7Info.DOCUMENT_PARENT_UNIQUE_NUMBER) && document.get(HL7Info.DOCUMENT_PARENT_UNIQUE_NUMBER) != null)
			{
				if (document.get(HL7Info.DOCUMENT_PARENT_UNIQUE_NUMBER) == CommonConstant.HL7_NULL_FIELD)
				{
					documentBean.setParentUniqueNumber(null);
				} else
				{
					documentBean.setParentUniqueNumber(document.get(HL7Info.DOCUMENT_PARENT_UNIQUE_NUMBER).toString());
				}
			}
		}
		LOGGER.debug("Exiting from setDocumentBean.");
		return documentBean;
	}

	private DocumentPhysicianMapBean setDocumentPhysicianBean(DocumentPhysicianMapBean documentPhysicianMapBean, Map<String, Object> documentPhysician)
			throws Exception
	{
		LOGGER.debug("Inside setDocumentPhysicianBean.");
		if (documentPhysicianMapBean != null)
		{
			if (documentPhysician.containsKey(HL7Info.PHYSICIAN_GLOBAL_ID) && documentPhysician.get(HL7Info.PHYSICIAN_GLOBAL_ID) != null)
			{
				int physicianLoginId = getHl7DaoFactory().getHl7Dao().getPhysicianLoginId(
						documentPhysician.get(HL7Info.PHYSICIAN_GLOBAL_ID).toString());
				if (physicianLoginId != 0)
				{
					documentPhysicianMapBean.setPhysicianLoginId(physicianLoginId);
				} else
				{
					throw new Exception("Unknown document physician. physicianId : " + documentPhysician.get(HL7Info.PHYSICIAN_GLOBAL_ID).toString());
				}
			}
		}
		LOGGER.debug("Exiting from setDocumentPhysicianBean.");
		return documentPhysicianMapBean;
	}

	private EncounterObservationRequestBean setObservationRequestBean(EncounterObservationRequestBean encounterObservationRequestBean,
			Map<String, Object> observationRequest, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside setObservationRequestBean.");
		if (encounterObservationRequestBean != null)
		{
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_PLACER_ORDER_NUMBER)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_PLACER_ORDER_NUMBER) != null)
			{
				encounterObservationRequestBean.setPlacerOrderNumber(observationRequest.get(HL7Info.OBSERVATION_REQUEST_PLACER_ORDER_NUMBER)
						.toString());
			}
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_UNIVERSAL_SERVICE_ID)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_UNIVERSAL_SERVICE_ID) != null)
			{
				encounterObservationRequestBean.setUniversalServiceId(observationRequest.get(HL7Info.OBSERVATION_REQUEST_UNIVERSAL_SERVICE_ID)
						.toString());
			}
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_DATE_TIME)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_DATE_TIME) != null)
			{
				String observationDateTimeString = observationRequest.get(HL7Info.OBSERVATION_REQUEST_DATE_TIME).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(observationDateTimeString, hospitalTimeZodeId);
				encounterObservationRequestBean.setObservationDateTime(utcDateTime);
			}
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_ORDERING_PROVIDER)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_ORDERING_PROVIDER) != null)
			{
				encounterObservationRequestBean.setOrderingProvider(observationRequest.get(HL7Info.OBSERVATION_REQUEST_ORDERING_PROVIDER).toString());
			}
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_RESULT_STATUS)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_RESULT_STATUS) != null)
			{
				encounterObservationRequestBean.setResultStatus(observationRequest.get(HL7Info.OBSERVATION_REQUEST_RESULT_STATUS).toString());
			}
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_REASON_FOR_STUDY)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_REASON_FOR_STUDY) != null)
			{
				encounterObservationRequestBean.setReasonForStudy(observationRequest.get(HL7Info.OBSERVATION_REQUEST_REASON_FOR_STUDY).toString());
			}
			if (observationRequest.containsKey(HL7Info.OBSERVATION_REQUEST_COMMENT)
					&& observationRequest.get(HL7Info.OBSERVATION_REQUEST_COMMENT) != null)
			{
				encounterObservationRequestBean.setComment(observationRequest.get(HL7Info.OBSERVATION_REQUEST_COMMENT).toString());
			}
		}
		LOGGER.debug("Exiting from setObservationRequestBean.");
		return encounterObservationRequestBean;
	}

	private EncounterObservationResultBean setObservationResultBean(EncounterObservationResultBean encounterObservationResultBean,
			Map<String, Object> observationResult) throws Exception
	{
		LOGGER.debug("Inside setObservationResultBean.");
		if (encounterObservationResultBean != null)
		{
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_IDENTIFIER)
					&& observationResult.get(HL7Info.OBSERVATION_RESULT_IDENTIFIER) != null)
			{
				encounterObservationResultBean.setObservationIdentifier(observationResult.get(HL7Info.OBSERVATION_RESULT_IDENTIFIER).toString());
			}
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_SUB_ID) && observationResult.get(HL7Info.OBSERVATION_RESULT_SUB_ID) != null)
			{
				encounterObservationResultBean.setObservationSubId(observationResult.get(HL7Info.OBSERVATION_RESULT_SUB_ID).toString());
			}
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_VALUE) && observationResult.get(HL7Info.OBSERVATION_RESULT_VALUE) != null)
			{
				encounterObservationResultBean.setObservationValue(observationResult.get(HL7Info.OBSERVATION_RESULT_VALUE).toString());
			}
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_UNITS) && observationResult.get(HL7Info.OBSERVATION_RESULT_UNITS) != null)
			{
				encounterObservationResultBean.setUnits(observationResult.get(HL7Info.OBSERVATION_RESULT_UNITS).toString());
			}
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_REFERENCE_RANGE)
					&& observationResult.get(HL7Info.OBSERVATION_RESULT_REFERENCE_RANGE) != null)
			{
				encounterObservationResultBean.setReferenceRange(observationResult.get(HL7Info.OBSERVATION_RESULT_REFERENCE_RANGE).toString());
			}
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_ABNORMAL_FLAGS)
					&& observationResult.get(HL7Info.OBSERVATION_RESULT_ABNORMAL_FLAGS) != null)
			{
				encounterObservationResultBean.setAbnormalFlags(observationResult.get(HL7Info.OBSERVATION_RESULT_ABNORMAL_FLAGS).toString());
			}
			if (observationResult.containsKey(HL7Info.OBSERVATION_RESULT_COMMENT)
					&& observationResult.get(HL7Info.OBSERVATION_RESULT_COMMENT) != null)
			{
				encounterObservationResultBean.setComment(observationResult.get(HL7Info.OBSERVATION_RESULT_COMMENT).toString());
			}
		}
		LOGGER.debug("Exiting from setObservationResultBean.");
		return encounterObservationResultBean;
	}

	private MasterMapBean setMasterMapBean(MasterMapBean masterMapBean, Map<String, Object> code, int documentId, int userId) throws Exception
	{
		LOGGER.debug("Inside setMasterMapBean.");
		if (masterMapBean != null)
		{
			long masterId = getMasterId(code, documentId, userId);
			masterMapBean.setMasterId(masterId);
		}
		LOGGER.debug("Exiting from setMasterMapBean.");
		return masterMapBean;
	}

	private CodeMapByUserBean setCodeMapByUserBean(CodeMapByUserBean codeMapByUserBean, Map<String, Object> code, String hospitalTimeZodeId)
			throws Exception
	{
		LOGGER.debug("Inside setCodeMapByUserBean.");
		CodeMasterBean codeMasterBean = getHl7DaoFactory().getHl7Dao().getCodeMasterBean(code.get(HL7Info.CODE).toString(),
				code.get(HL7Info.CODING_SYSTEM).toString());
		if (codeMasterBean != null)
		{
			codeMapByUserBean.setCodeId(codeMasterBean.getId());
			codeMapByUserBean.setCode(codeMasterBean.getCode());
			codeMapByUserBean.setCodeDesc(codeMasterBean.getDesc());

			codeMapByUserBean.setAcceptRejectFlag(-1);
			codeMapByUserBean.setCodeOrdinality(Integer.parseInt(code.get(HL7Info.CODE_ORDINALITY).toString()));

			codeMapByUserBean.setCodingSystem(code.get(HL7Info.CODING_SYSTEM).toString());
			codeMapByUserBean.setCodeStatusCode(code.get(HL7Info.CODE_STATUS_CODE).toString());

			if (code.containsKey(HL7Info.PROCEDURE_DATE))
			{
				String dateOfProcedure = code.get(HL7Info.PROCEDURE_DATE).toString();
				Date utcDateTime = DateUtils.convertHL7DateTimeStringToUTCDateTime(dateOfProcedure, hospitalTimeZodeId);
				codeMapByUserBean.setProcedureDate(utcDateTime);
			}
			if (code.containsKey(HL7Info.PROCEDURE_ANESTHESIA_PROVIDER))
			{
				codeMapByUserBean.setAnesthesiaProvider(code.get(HL7Info.PROCEDURE_ANESTHESIA_PROVIDER).toString());
			}
			if (code.containsKey(HL7Info.PROCEDURE_ANESTHESIA_TIME))
			{
				codeMapByUserBean.setAnesthesiaTime(code.get(HL7Info.PROCEDURE_ANESTHESIA_TIME).toString());
			}
			if (code.containsKey(HL7Info.PROCEDURE_ANESTHESIA_TYPE))
			{
				codeMapByUserBean.setAnesthesiaType(code.get(HL7Info.PROCEDURE_ANESTHESIA_TYPE).toString());
			}

			String codeType = code.get(HL7Info.CODE_TYPE).toString();
			String reason = null;
			if (CodeTypeConstant.PRINCIPAL.equals(codeType))
			{
				reason = CodeUpdateReasonConstant.PRINCIPAL_BY_HL7;
			} else if (CodeTypeConstant.ADMITTING.equals(codeType))
			{
				reason = CodeUpdateReasonConstant.ADMITTING_BY_HL7;
			} else if (CodeTypeConstant.SECONDARY.equals(codeType))
			{
				reason = CodeUpdateReasonConstant.ADDED_BY_HL7;
			} else
			{
				throw new Exception("Unkown code type. codeType : " + codeType);
			}

			CodeUpdateReasonBean codeUpdateReasonBean = getHl7DaoFactory().getHl7Dao().getCodeUpdateReasonBean(reason);
			codeMapByUserBean.setReasonId(codeUpdateReasonBean.getId());
			codeMapByUserBean.setReason(codeUpdateReasonBean.getReason());
			codeMapByUserBean.setReasonDetail(codeUpdateReasonBean.getReasonDetail());

		} else
		{
			throw new Exception("Code not found. code : " + code.get(HL7Info.CODE).toString() + " codeType : "
					+ code.get(HL7Info.CODING_SYSTEM).toString());
		}
		LOGGER.debug("Exiting from setCodeMapByUserBean.");
		return codeMapByUserBean;
	}

	private long getMasterId(Map<String, Object> code, int documentId, int userId) throws Exception
	{
		LOGGER.debug("Inside getMasterId.");
		long masterId = -1;
		String codingSystem = code.get(HL7Info.CODING_SYSTEM).toString();
		int codeTypeId = getHl7DaoFactory().getHl7Dao().getCodeTypeId(codingSystem);

		String documentIdString = "";
		if (CodingSystemConstant.ICD9_DIAGNOSIS_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.ICD9_DIAGNOSIS_DOCUMENT_ID_FORMAT, documentId);
		} else if (CodingSystemConstant.ICD10_DIAGNOSIS_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.ICD10_DIAGNOSIS_DOCUMEN_ID_FORMAT, documentId);
		} else if (CodingSystemConstant.ICD9_PROCEDURE_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.ICD9_PROCEDURE_DOCUMENT_ID_FORMAT, documentId);
		} else if (CodingSystemConstant.ICD10_PROCEDURE_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.ICD10_PROCEDURE_DOCUMENT_ID_FORMAT, documentId);
		} else if (CodingSystemConstant.CPT4_PROCEDURE_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.CPT4_PROCEDURE_DOCUMENT_ID_FORMAT, documentId);
		} else if (CodingSystemConstant.ENM_PROCEDURE_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.ENM_PROCEDURE_DOCUMENT_ID_FORMAT, documentId);
		} else if (CodingSystemConstant.HCPCS_PROCEDURE_CODE_TYPE_CODE.equals(codingSystem))
		{
			documentIdString = String.format(CommonConstant.HCPCS_PROCEDURE_DOCUMENT_ID_FORMAT, documentId);
		} else
		{
			throw new Exception("Unknown diagnosis coding system. codingSystem :" + code.get(HL7Info.CODING_SYSTEM).toString());
		}
		int documentCodingSystemCount = getHl7DaoFactory().getHl7Dao().getDocumentCodingSystemCount(documentId, codingSystem, userId);
		int documentCodingSystemIndex = documentCodingSystemCount + 1;
		String mappingIdString = String.format(CommonConstant.DOCUMENT_CODE_INDEX_FORMAT, documentCodingSystemIndex);

		masterId = Long.parseLong(codeTypeId + documentIdString + mappingIdString);
		LOGGER.debug("Exiting from getMasterId.");
		return masterId;
	}

	private DocumentBean getHL7DocumentBean(EncounterBean encounterBean, Integer hl7UserId, Date utcDateTime) throws Exception
	{
		LOGGER.debug("Inside getHL7DocumentBean.");
		int encounterId = encounterBean.getId();
		String hl7CodeDocumentName = encounterId + "_HL7_Code";
		LOGGER.info("Checking temp document for HL7 code. hl7CodeDocumentName : " + hl7CodeDocumentName);
		HL7Dao hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
		DocumentBean hl7CodeDocumentBean = hl7DaoImpl.getDocument(encounterId, hl7CodeDocumentName);
		if (hl7CodeDocumentBean == null)
		{
			hl7CodeDocumentBean = new DocumentBean();
			hl7CodeDocumentBean.setName(hl7CodeDocumentName);
			hl7CodeDocumentBean.setCreatorUserId(hl7UserId);
			hl7CodeDocumentBean.setCreatorDateTime(utcDateTime);
			hl7CodeDocumentBean.setUpdaterUserId(hl7UserId);
			hl7CodeDocumentBean.setUpdationDateTime(utcDateTime);
			hl7CodeDocumentBean.setEncounterId(encounterId);
			hl7CodeDocumentBean.setEncounterNumber(encounterBean.getEncounterNumber());
			hl7CodeDocumentBean.setActiveRecordFlag(CommonConstant.INACTIVE_DOCUMENT);

			LOGGER.info("Inserting HL7 code document information.");
			hl7DaoImpl.insertDocument(hl7CodeDocumentBean);
			LOGGER.info("HL7 code document information inserted successfully. hl7CodeDocumentId : " + hl7CodeDocumentBean.getId());
		}
		LOGGER.debug("Exiting from getHL7DocumentBean.");
		return hl7CodeDocumentBean;
	}

	private void sendMessageToSQS(String documentName, String tenantCode, int priority) throws Exception
	{
		LOGGER.debug("Inside sendMessage.");

		CACMessage cacMessage = new CACMessage();
		cacMessage.setDocumentName(documentName);
		cacMessage.setTenantCode(tenantCode);
		ObjectMapper objectMapper = new ObjectMapper();
		String cacMessageString = objectMapper.writeValueAsString(cacMessage);

		AWSSQSServiceUtils amazonSQSService = AWSSQSServiceUtils.getInstance();
		LOGGER.info("Sending message to SQS. cacMessageString : " + cacMessageString);
		amazonSQSService.sendMessageToHL7WSProducerQueue(cacMessageString);
		LOGGER.info("Message sent successfully.");

		LOGGER.debug("Exiting from sendMessage.");
	}

	private PatientBean insrtOrUpdatePatient(Map<String, Object> encounter, String mrn, String hospitalTimeZodeId, int hl7UserId, Date utcDateTime)
			throws Exception
	{
		LOGGER.debug("Inside insrtOrUpdatePatient.");

		LOGGER.info("Checking patient. mrn : " + mrn);
		PatientBean patientBean = getHl7DaoFactory().getHl7Dao().getPatient(mrn);
		if (patientBean == null)
		{
			LOGGER.info("Patient does not exists. mrn : " + mrn);
			patientBean = new PatientBean();
			patientBean.setCreatorUserId(hl7UserId);
			patientBean.setCreatorDateTime(utcDateTime);
			patientBean.setUpdaterUserId(hl7UserId);
			patientBean.setUpdationDateTime(utcDateTime);
			patientBean = setPatientBean(patientBean, encounter, hospitalTimeZodeId);
			LOGGER.info("Inserting patient information. mrn : " + mrn);
			getHl7DaoFactory().getHl7Dao().insertPatient(patientBean);
			LOGGER.info("Patient information inserted successfully. patientId : " + patientBean.getId());

		} else
		{
			LOGGER.info("Patient already exists. patientId : " + patientBean.getId());
			setPatientBean(patientBean, encounter, hospitalTimeZodeId);
			patientBean.setUpdaterUserId(hl7UserId);
			patientBean.setUpdationDateTime(utcDateTime);
			LOGGER.info("Updating patient information. patientId : " + patientBean.getId());
			getHl7DaoFactory().getHl7Dao().updatePatient(patientBean);
			LOGGER.info("Patient information updated successfully.");
			
		}
		LOGGER.info("Now publishing Update Index Event for patientId: " + patientBean.getId());
		notifyUpdateToIndexById(patientBean.getId(), PatientBean.class);
		LOGGER.info("Published Index Update Event for patientId: " + patientBean.getId());
		
		LOGGER.debug("Exiting from insrtOrUpdatePatient.");
		return patientBean;
	}

	private void insertOrUpdatePhysician(List<Map<String, Object>> physicianList, int encounterId) throws Exception
	{
		LOGGER.debug("Inside insertOrUpdatePhysician.");
		if (physicianList != null && physicianList.size() > 0)
		{
			List<EncounterPhysicianBean> currentEncounterPhysicianBeanList = new ArrayList<EncounterPhysicianBean>();
			for (Map<String, Object> physician : physicianList)
			{
				EncounterPhysicianBean currentEncounterPhysicianBean = new EncounterPhysicianBean();
				currentEncounterPhysicianBean.setEncounterId(encounterId);
				currentEncounterPhysicianBean.setIsActive(1);
				setEncounterPhysicianBean(currentEncounterPhysicianBean, physician);
				currentEncounterPhysicianBeanList.add(currentEncounterPhysicianBean);
			}

			List<EncounterPhysicianBean> oldEncounterPhysicianBeanList = getHl7DaoFactory().getHl7Dao().getEncounterPhysicianBeanList(encounterId);
			List<EncounterPhysicianBean> newEncounterPhysicianBeanList = new ArrayList<EncounterPhysicianBean>();
			if (oldEncounterPhysicianBeanList != null && oldEncounterPhysicianBeanList.size() > 0)
			{
				for (EncounterPhysicianBean currentEncounterPhysicianBean : currentEncounterPhysicianBeanList)
				{
					boolean isNewEncounterPhysician = true;
					for (EncounterPhysicianBean oldEncounterPhysicianBean : oldEncounterPhysicianBeanList)
					{
						if (currentEncounterPhysicianBean.getPhysicianLoginId().equals(oldEncounterPhysicianBean.getPhysicianLoginId())
								&& currentEncounterPhysicianBean.getPhysicianTypeCode().equals(oldEncounterPhysicianBean.getPhysicianTypeCode()))
						{
							isNewEncounterPhysician = false;
							break;
						}
					}
					if (isNewEncounterPhysician)
					{
						newEncounterPhysicianBeanList.add(currentEncounterPhysicianBean);
					}
				}
				currentEncounterPhysicianBeanList.removeAll(newEncounterPhysicianBeanList);

				List<EncounterPhysicianBean> updatedEncounterPhysicianBeanList = new ArrayList<EncounterPhysicianBean>();
				for (EncounterPhysicianBean oldEncounterPhysicianBean : oldEncounterPhysicianBeanList)
				{
					boolean isEncounterPhysicianPresent = false;
					for (EncounterPhysicianBean currentEncounterPhysicianBean : currentEncounterPhysicianBeanList)
					{
						if (oldEncounterPhysicianBean.getPhysicianLoginId().equals(currentEncounterPhysicianBean.getPhysicianLoginId())
								&& oldEncounterPhysicianBean.getPhysicianTypeCode().equals(currentEncounterPhysicianBean.getPhysicianTypeCode()))
						{
							isEncounterPhysicianPresent = true;
							break;
						}
					}
					if (isEncounterPhysicianPresent)
					{
						if (oldEncounterPhysicianBean.getIsActive() == CommonConstant.INACTIVE_ENCOUNTER_PHYSICIAN)
						{
							oldEncounterPhysicianBean.setIsActive(CommonConstant.ACTIVE_ENCOUNTER_PHYSICIAN);
							updatedEncounterPhysicianBeanList.add(oldEncounterPhysicianBean);
						}
					} else
					{
						oldEncounterPhysicianBean.setIsActive(CommonConstant.ACTIVE_ENCOUNTER_PHYSICIAN);
						updatedEncounterPhysicianBeanList.add(oldEncounterPhysicianBean);
					}
				}
				for (EncounterPhysicianBean updatedEncounterPhysicianBean : updatedEncounterPhysicianBeanList)
				{
					LOGGER.info("Updating encounter physician information. encounterPhysician : " + updatedEncounterPhysicianBean);
					getHl7DaoFactory().getHl7Dao().updateEncounterPhysician(updatedEncounterPhysicianBean);
					LOGGER.info("Encounter physician information updated successfully. encounterPhysicianId : "
							+ updatedEncounterPhysicianBean.getId());
				}
			} else
			{
				newEncounterPhysicianBeanList.addAll(currentEncounterPhysicianBeanList);
			}
			for (EncounterPhysicianBean newEncounterPhysicianBean : newEncounterPhysicianBeanList)
			{
				LOGGER.info("Inserting encounter physician information. encounterPhysician : " + newEncounterPhysicianBean);
				getHl7DaoFactory().getHl7Dao().insertEncounterPhysician(newEncounterPhysicianBean);
				LOGGER.info("Encounter physician information inserted successfully. encounterPhysicianId : " + newEncounterPhysicianBean.getId());
			}

		}
		LOGGER.debug("Exiting from insertOrUpdatePhysician.");
	}

	private void insertDocumentPhysician(List<Map<String, Object>> documentPhysicianList, int documentId) throws Exception
	{
		LOGGER.debug("Inside insertDocumentPhysician.");
		if (documentPhysicianList != null && documentPhysicianList.size() > 0)
		{
			for (Map<String, Object> documentPhysician : documentPhysicianList)
			{
				DocumentPhysicianMapBean documentPhysicianMapBean = new DocumentPhysicianMapBean();
				documentPhysicianMapBean.setDocumentId(documentId);
				documentPhysicianMapBean.setIsActive(1);
				setDocumentPhysicianBean(documentPhysicianMapBean, documentPhysician);
				LOGGER.info("Inserting document physician. documentId : " + documentId);
				getHl7DaoFactory().getHl7Dao().insertDocumentPhysician(documentPhysicianMapBean);
				LOGGER.info("Document physician inserted successfully. documentPhysicianId : " + documentPhysicianMapBean.getId());
			}
		}
		LOGGER.debug("Exiting from insertDocumentPhysician.");
	}

	@SuppressWarnings("unchecked")
	private void insertOrUpdateObservation(List<Map<String, Object>> observationList, EncounterBean encounterBean, Integer hl7UserId,
			Date utcDateTime, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside insertOrUpdateObservation.");
		if (observationList != null && observationList.size() > 0)
		{
			for (Map<String, Object> observation : observationList)
			{
				Map<String, Object> observationRequest = (Map<String, Object>) observation.get(HL7Info.OBSERVATION_REQUEST);
				Integer encounterId = encounterBean.getId();
				String placerOrderNumber = observationRequest.get(HL7Info.OBSERVATION_REQUEST_PLACER_ORDER_NUMBER).toString();
				String observationDocumentName = encounterId + "_" + placerOrderNumber;
				Integer documentId = getHl7DaoFactory().getHl7Dao().getDocumentId(observationDocumentName, encounterBean.getId());
				DocumentBean observationDocumentBean = null;
				if (documentId == null)
				{
					observationDocumentBean = new DocumentBean();
					observationDocumentBean.setEncounterId(encounterId);
					observationDocumentBean.setEncounterNumber(encounterBean.getEncounterNumber());
					observationDocumentBean.setName(observationDocumentName);
					observationDocumentBean.setCreatorUserId(hl7UserId);
					observationDocumentBean.setCreatorDateTime(utcDateTime);
					observationDocumentBean.setUpdaterUserId(hl7UserId);
					observationDocumentBean.setUpdationDateTime(utcDateTime);
					observationDocumentBean.setActiveRecordFlag(CommonConstant.ACTIVE_DOCUMENT);
					setObservationDocumentBean(observationDocumentBean, observationRequest, hospitalTimeZodeId);

					LOGGER.info("Inserting observation document information.");
					getHl7DaoFactory().getHl7Dao().insertDocument(observationDocumentBean);
					documentId = observationDocumentBean.getId();
					LOGGER.info("Observation document inserted successfully. observationDocumentId : " + documentId);

				}

				EncounterObservationRequestBean encounterObservationRequestBean = new EncounterObservationRequestBean();
				encounterObservationRequestBean.setEncounterId(encounterBean.getId());
				encounterObservationRequestBean.setEncounterNumber(encounterBean.getEncounterNumber());
				encounterObservationRequestBean.setDocumentId(documentId);
				setObservationRequestBean(encounterObservationRequestBean, observationRequest, hospitalTimeZodeId);

				LOGGER.info("Inserting observation request information.");
				getHl7DaoFactory().getHl7Dao().insertEncounterObservationRequest(encounterObservationRequestBean);
				Integer observationRequestId = encounterObservationRequestBean.getId();
				LOGGER.info("Observation request inserted successfully. observationRequestId : " + observationRequestId);

				List<Map<String, Object>> observationResultList = (List<Map<String, Object>>) observation.get(HL7Info.OBSERVATION_RESULT_LIST);
				if (observationResultList != null && observationResultList.size() > 0)
				{
					for (Map<String, Object> observationResult : observationResultList)
					{
						EncounterObservationResultBean encounterObservationResultBean = new EncounterObservationResultBean();
						encounterObservationResultBean.setObservationRequestId(observationRequestId);
						setObservationResultBean(encounterObservationResultBean, observationResult);

						LOGGER.info("Inserting observation result information.");
						getHl7DaoFactory().getHl7Dao().insertEncounterObservationResult(encounterObservationResultBean);
						Integer encounterObservationResultId = encounterObservationResultBean.getId();
						LOGGER.info("Observation result inserted successfully. encounterObservationResultId : " + encounterObservationResultId);
					}
				}

				insertNewDocumentArrivalEvent(encounterId, observationDocumentBean.getTypeName(), hl7UserId, utcDateTime);

				updateEncounterStatusOnNewDocumentArrivalForReadyNotReady(encounterId, encounterBean.getPatientClassId(),
						encounterBean.getServiceLineId(), observationDocumentBean.getTypeId(), hl7UserId, utcDateTime);

			}
		}
		LOGGER.debug("Exiting from insertOrUpdateObservation.");
	}

	private void insertOrUpdateCode(List<Map<String, Object>> currentCodeList, int encounterId, DocumentBean documentBean, int userId,
			Date utcDateTime, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside insertOrUpdateCode.");
		if (currentCodeList != null && currentCodeList.size() > 0)
		{
			boolean isEncounterInQueueORUnassign = getHl7DaoFactory().getHl7Dao().isEncounterInQueueORUnassign(encounterId);

			List<CodeMapByUserBean> oldCodeMapByUserBeanList = getHl7DaoFactory().getHl7Dao().getCodeList(encounterId, userId);
			List<Map<String, Object>> newCodeList = new ArrayList<Map<String, Object>>();
			List<CodeMapByUserBean> alredyPresentCodeMapByUserBeanList = new ArrayList<CodeMapByUserBean>();
			List<CodeMapByUserBean> rejectedCodeMapByUserBeanList = new ArrayList<CodeMapByUserBean>();
			if (oldCodeMapByUserBeanList != null && oldCodeMapByUserBeanList.size() > 0)
			{
				for (Map<String, Object> currentCode : currentCodeList)
				{
					boolean isNewCode = true;
					for (CodeMapByUserBean oldCodeMapByUserBean : oldCodeMapByUserBeanList)
					{
						String code = currentCode.get(HL7Info.CODE).toString();
						String codingSystem = currentCode.get(HL7Info.CODING_SYSTEM).toString();
						if (code.equals(oldCodeMapByUserBean.getCode()) && codingSystem.equals(oldCodeMapByUserBean.getCodingSystem()))
						{
							isNewCode = false;
							break;
						}
					}
					if (isNewCode)
					{
						newCodeList.add(currentCode);
					}
				}
				currentCodeList.removeAll(newCodeList);

				for (CodeMapByUserBean oldCodeMapByUserBean : oldCodeMapByUserBeanList)
				{
					boolean isCodePresent = false;
					for (Map<String, Object> currentCode : currentCodeList)
					{
						String code = currentCode.get(HL7Info.CODE).toString();
						String codingSystem = currentCode.get(HL7Info.CODING_SYSTEM).toString();
						if (oldCodeMapByUserBean.getCode().equals(code) && oldCodeMapByUserBean.getCodingSystem().equals(codingSystem))
						{
							isCodePresent = true;
							break;
						}
					}
					if (isCodePresent)
					{
						alredyPresentCodeMapByUserBeanList.add(oldCodeMapByUserBean);
					} else
					{
						rejectedCodeMapByUserBeanList.add(oldCodeMapByUserBean);
					}
				}
				if (alredyPresentCodeMapByUserBeanList.size() > 0)
				{
					LOGGER.info("Updating existing code.");
					updateAlreadyPresentCodeList(alredyPresentCodeMapByUserBeanList, currentCodeList, isEncounterInQueueORUnassign, utcDateTime,
							hospitalTimeZodeId);
					LOGGER.info("Existing code updated successfully.");
				}
				if (rejectedCodeMapByUserBeanList.size() > 0)
				{
					LOGGER.info("Rejecting existing code.");
					rejectCodeList(rejectedCodeMapByUserBeanList, encounterId, isEncounterInQueueORUnassign, utcDateTime);
					LOGGER.info("Existing code rejected successfully.");
				}
			} else
			{
				newCodeList.addAll(currentCodeList);
			}
			if (newCodeList.size() > 0)
			{
				LOGGER.info("Inserting new code.");
				insertNewCodeList(newCodeList, encounterId, documentBean, userId, isEncounterInQueueORUnassign, utcDateTime, hospitalTimeZodeId);
				LOGGER.info("New code inserted successfully.");
			}
		}
		LOGGER.debug("Exiting from insertOrUpdateCode.");

	}

	private void insertNewCodeList(List<Map<String, Object>> codeList, int encounterId, DocumentBean documentBean, int userId,
			boolean isEncounterInQueueORUnassign, Date creationDateTime, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside insertNewCodeList.");
		if (codeList != null && codeList.size() > 0)
		{
			for (Map<String, Object> code : codeList)
			{
				MasterMapBean masterMapBean = new MasterMapBean();
				masterMapBean.setDocumentId(documentBean.getId());
				setMasterMapBean(masterMapBean, code, documentBean.getId(), userId);
				LOGGER.info("Inserting masterId. masterId : " + masterMapBean.getMasterId());
				getHl7DaoFactory().getHl7Dao().insertMasterId(masterMapBean);
				LOGGER.info("MasterId inserted successfully. masterId : " + masterMapBean.getMasterId());

				CodeMapByUserBean codeMapByUserBean = new CodeMapByUserBean();
				codeMapByUserBean.setEncounterId(encounterId);
				codeMapByUserBean.setDocumentId(documentBean.getId());
				codeMapByUserBean.setDocumentName(documentBean.getName());
				codeMapByUserBean.setMasterId(masterMapBean.getMasterId());
				codeMapByUserBean.setIsActive(1);
				codeMapByUserBean.setCreatorUserId(userId);
				codeMapByUserBean.setCreationTime(creationDateTime);
				codeMapByUserBean.setUpdationTime(creationDateTime);
				setCodeMapByUserBean(codeMapByUserBean, code, hospitalTimeZodeId);
				if (isEncounterInQueueORUnassign)
				{
					codeMapByUserBean.setCodeStatus(CodeStatusConstant.ORIGINAL);
				} else
				{
					codeMapByUserBean.setCodeStatus(CodeStatusConstant.NEW);
				}
				LOGGER.info("Inserting code. code : " + codeMapByUserBean);
				getHl7DaoFactory().getHl7Dao().insertCode(codeMapByUserBean);
				int codeMapByUserId = codeMapByUserBean.getId();
				LOGGER.info("Code inserted successfully. codeMapByUserId : " + codeMapByUserId);
			}
		}
		LOGGER.debug("Exiting from insertNewCodeList.");
	}

	private void updateAlreadyPresentCodeList(List<CodeMapByUserBean> alredyPresentCodeMapByUserBeanList, List<Map<String, Object>> currentCodeList,
			boolean isEncounterInQueueORUnassign, Date updatedDateTime, String hospitalTimeZodeId) throws Exception
	{
		LOGGER.debug("Inside updateAlreadyPresentCodeList.");
		for (CodeMapByUserBean alredyPresentCodeMapByUserBean : alredyPresentCodeMapByUserBeanList)
		{
			for (Map<String, Object> currentCode : currentCodeList)
			{
				LOGGER.info("currentCode : " + currentCode);
				String code = currentCode.get(HL7Info.CODE).toString();
				String codingSystem = currentCode.get(HL7Info.CODING_SYSTEM).toString();
				if (alredyPresentCodeMapByUserBean.getCode().equals(code) && alredyPresentCodeMapByUserBean.getCodingSystem().equals(codingSystem))
				{
					LOGGER.info("alredyPresentCodeMapByUserBean : " + alredyPresentCodeMapByUserBean);
					String reason = null;
					String codeType = currentCode.get(HL7Info.CODE_TYPE).toString();
					if (CodeTypeConstant.PRINCIPAL.equals(codeType)
							&& !alredyPresentCodeMapByUserBean.getReason().equals(CodeUpdateReasonConstant.PRINCIPAL_BY_HL7))
					{
						reason = CodeUpdateReasonConstant.PRINCIPAL_BY_HL7;
					} else if (CodeTypeConstant.ADMITTING.equals(codeType)
							&& !alredyPresentCodeMapByUserBean.getReason().equals(CodeUpdateReasonConstant.ADMITTING_BY_HL7))
					{
						reason = CodeUpdateReasonConstant.ADMITTING_BY_HL7;
					} else if (CodeTypeConstant.SECONDARY.equals(codeType)
							&& !alredyPresentCodeMapByUserBean.getReason().equals(CodeUpdateReasonConstant.ADDED_BY_HL7))
					{
						reason = CodeUpdateReasonConstant.ADDED_BY_HL7;
					}
					if (reason != null)
					{
						CodeUpdateReasonBean codeUpdateReasonBean = getHl7DaoFactory().getHl7Dao().getCodeUpdateReasonBean(reason);
						alredyPresentCodeMapByUserBean.setReasonId(codeUpdateReasonBean.getId());
						alredyPresentCodeMapByUserBean.setReason(codeUpdateReasonBean.getReason());
						alredyPresentCodeMapByUserBean.setReasonDetail(codeUpdateReasonBean.getReasonDetail());
						if (isEncounterInQueueORUnassign)
						{
							alredyPresentCodeMapByUserBean.setCodeStatus(CodeStatusConstant.ORIGINAL);
						} else
						{
							alredyPresentCodeMapByUserBean.setCodeStatus(CodeStatusConstant.UPDATED);
						}
						alredyPresentCodeMapByUserBean.setUpdationTime(updatedDateTime);
						alredyPresentCodeMapByUserBean.setIsActive(1);
						LOGGER.info("Updating code. code : " + alredyPresentCodeMapByUserBean);
						getHl7DaoFactory().getHl7Dao().updateCode(alredyPresentCodeMapByUserBean);
						LOGGER.info("Code updated successfully. codeMapByUserId " + alredyPresentCodeMapByUserBean.getId());
					}
				}
			}
		}
		LOGGER.debug("Exiting from updateAlreadyPresentCodeList.");
	}

	private void rejectCodeList(List<CodeMapByUserBean> rejectedCodeMapByUserBeanList, int encounterId, boolean isEncounterInQueueORUnassign,
			Date rejectedDateTime) throws Exception
	{
		LOGGER.debug("Inside rejectCodeList.");
		if (rejectedCodeMapByUserBeanList != null && rejectedCodeMapByUserBeanList.size() > 0)
		{
			for (CodeMapByUserBean rejectedCodeMapByUserBean : rejectedCodeMapByUserBeanList)
			{
				CodeUpdateReasonBean codeUpdateReasonBean = getHl7DaoFactory().getHl7Dao().getCodeUpdateReasonBean(
						CodeUpdateReasonConstant.REJECTED_BY_HL7);
				rejectedCodeMapByUserBean.setReasonId(codeUpdateReasonBean.getId());
				rejectedCodeMapByUserBean.setReason(codeUpdateReasonBean.getReason());
				rejectedCodeMapByUserBean.setReasonDetail(codeUpdateReasonBean.getReasonDetail());
				if (isEncounterInQueueORUnassign)
				{
					rejectedCodeMapByUserBean.setCodeStatus(CodeStatusConstant.ORIGINAL);
					rejectedCodeMapByUserBean.setIsActive(0);
				} else
				{
					rejectedCodeMapByUserBean.setCodeStatus(CodeStatusConstant.UPDATED);
					rejectedCodeMapByUserBean.setIsActive(1);
				}
				rejectedCodeMapByUserBean.setUpdationTime(rejectedDateTime);
				LOGGER.info("Rejecting code. code : " + rejectedCodeMapByUserBean);
				getHl7DaoFactory().getHl7Dao().updateCode(rejectedCodeMapByUserBean);
				LOGGER.info("Code rejected successfully. codeMapBuUserId : " + rejectedCodeMapByUserBean.getId());
			}
		}
		LOGGER.debug("Exiting from rejectCodeList.");
	}

	private EncounterHistoryBean createEncounterHistory(EncounterBean encounterBean)
	{
		EncounterHistoryBean encounterHistoryBean = new EncounterHistoryBean();

		encounterHistoryBean.setEncounterId(encounterBean.getId());
		encounterHistoryBean.setPatientId(encounterBean.getPatientId());
		encounterHistoryBean.setEncounterNumber(encounterBean.getEncounterNumber());
		encounterHistoryBean.setHospitalName(encounterBean.getHospitalName());
		encounterHistoryBean.setLocationId(encounterBean.getLocationId());
		encounterHistoryBean.setLocationName(encounterBean.getLocationName());
		encounterHistoryBean.setPatientClassId(encounterBean.getPatientClassId());
		encounterHistoryBean.setPatientClassCode(encounterBean.getPatientClassCode());
		encounterHistoryBean.setPatientTypeId(encounterBean.getPatientTypeId());
		encounterHistoryBean.setPatientTypeCode(encounterBean.getPatientTypeCode());
		encounterHistoryBean.setServiceLineId(encounterBean.getServiceLineId());
		encounterHistoryBean.setServiceLineCode(encounterBean.getServiceLineCode());
		encounterHistoryBean.setAdmitSourceCode(encounterBean.getAdmitSourceCode());
		encounterHistoryBean.setDischargeDispositionId(encounterBean.getDischargeDispositionId());
		encounterHistoryBean.setDischargeDispositionCode(encounterBean.getDischargeDispositionCode());
		encounterHistoryBean.setDischargeDispositionDesc(encounterBean.getDischargeDispositionDesc());
		encounterHistoryBean.setAccountNumber(encounterBean.getAccountNumber());
		encounterHistoryBean.setBillType(encounterBean.getBillType());
		encounterHistoryBean.setFinancialClasses(encounterBean.getFinancialClasses());
		encounterHistoryBean.setTotalCharges(encounterBean.getTotalCharges());
		encounterHistoryBean.setPayerFlag(encounterBean.getPayerFlag());
		encounterHistoryBean.setPayer(encounterBean.getPayer());
		encounterHistoryBean.setPayerId(encounterBean.getPayerId());
		encounterHistoryBean.setNewBornWeight(encounterBean.getNewBornWeight());
		encounterHistoryBean.setDateOfAdmition(encounterBean.getDateOfAdmition());
		encounterHistoryBean.setDateOfDischarge(encounterBean.getDateOfDischarge());
		encounterHistoryBean.setOverrideLOS(encounterBean.getOverrideLOS());
		encounterHistoryBean.setLengthOfStay(encounterBean.getLengthOfStay());
		encounterHistoryBean.setPriority(encounterBean.getPriority());
		encounterHistoryBean.setQueryOpertunity(encounterBean.getQueryOpertunity());
		encounterHistoryBean.setDrgImpact(encounterBean.getDrgImpact());
		encounterHistoryBean.setQarebillstatus(encounterBean.getQarebillstatus());
		encounterHistoryBean.setPatientStatusComment(encounterBean.getPatientStatusComment());
		encounterHistoryBean.setBuilding(encounterBean.getBuilding());
		encounterHistoryBean.setFloor(encounterBean.getFloor());
		encounterHistoryBean.setPointOfCare(encounterBean.getPointOfCare());
		encounterHistoryBean.setRoom(encounterBean.getRoom());
		encounterHistoryBean.setBed(encounterBean.getBed());
		encounterHistoryBean.setLocationStatus(encounterBean.getLocationStatus());
		encounterHistoryBean.setPersonLocationType(encounterBean.getPersonLocationType());
		encounterHistoryBean.setPatientMrn(encounterBean.getPatientMrn());
		encounterHistoryBean.setPatientFirstName(encounterBean.getPatientFirstName());
		encounterHistoryBean.setPatientLastName(encounterBean.getPatientLastName());
		encounterHistoryBean.setPatientDateOfBirth(encounterBean.getPatientDateOfBirth());
		encounterHistoryBean.setPatientGender(encounterBean.getPatientGender());
		encounterHistoryBean.setCreatorUserId(encounterBean.getCreatorUserId());
		encounterHistoryBean.setCreatorDateTime(encounterBean.getCreatorDateTime());
		encounterHistoryBean.setUpdaterUserId(encounterBean.getUpdaterUserId());
		encounterHistoryBean.setUpdationDateTime(encounterBean.getUpdationDateTime());
		encounterHistoryBean.setLockBy(encounterBean.getLockBy());
		encounterHistoryBean.setIsActive(encounterBean.getIsActive());
		encounterHistoryBean.setIsEncounterUpdated(encounterBean.getIsEncounterUpdated());

		return encounterHistoryBean;

	}

	private void insertNewDocumentArrivalEvent(Integer encounterId, String documentType, Integer userId, Date dateTime) throws Exception
	{
		LOGGER.debug("Inside insertNewDocumentArrivalEvent.");

		EncounterEventSummaryBean encounterEventSummaryBean = new EncounterEventSummaryBean();
		encounterEventSummaryBean.setEncounterId(encounterId);
		encounterEventSummaryBean.setEventType(EventTypeConstant.DOCUMENT_ARRIVAL);
		String description = "New " + documentType + " arrive.";
		encounterEventSummaryBean.setDescription(description);
		encounterEventSummaryBean.setUserId(userId);
		encounterEventSummaryBean.setDateTime(dateTime);
		LOGGER.info("Inserting " + EventTypeConstant.DOCUMENT_ARRIVAL + " event into ENCOUNTER_EVENTS_SUMMARY table. encounterId" + encounterId);
		getHl7DaoFactory().getHl7Dao().insertEncounterEventSummaryBean(encounterEventSummaryBean);
		LOGGER.info(EventTypeConstant.DOCUMENT_ARRIVAL + " event inserted successfully into ENCOUNTER_EVENTS_SUMMARY table. encounterEventId : "
				+ encounterEventSummaryBean.getEventId());

		LOGGER.debug("Exiting from insertNewDocumentArrivalEvent.");

	}

	private void addEncounterInDocumentNotReceiveStatus(int encounterId, int userId, Date dateTime) throws Exception
	{
		LOGGER.debug("Inside addEncounterInDocumentNotReceiveStatus.");
		Set<String> userRoleSet = new HashSet<String>();
		userRoleSet.add(UserRoleConstant.CODER);
		userRoleSet.add(UserRoleConstant.REVIEWER);
		userRoleSet.add(UserRoleConstant.CDI);
		List<UserRoleBean> userRoleBeanList = getHl7DaoFactory().getHl7Dao().getActiveUserRole();
		for (UserRoleBean userRoleBean : userRoleBeanList)
		{
			RoleWiseEncounterStatusBean roleWiseEncounterStatusBean = new RoleWiseEncounterStatusBean();
			roleWiseEncounterStatusBean.setEncounterId(encounterId);
			roleWiseEncounterStatusBean.setUserRoleName(userRoleBean.getCode());
			roleWiseEncounterStatusBean.setUserRoleId(userRoleBean.getId());
			roleWiseEncounterStatusBean.setStatus(EncounterStatusConstant.DOCUMENT_NOT_RECEIVED);
			roleWiseEncounterStatusBean.setCreatorUserId(userId);
			roleWiseEncounterStatusBean.setCreationDateTime(dateTime);
			roleWiseEncounterStatusBean.setUpdatotUserId(userId);
			roleWiseEncounterStatusBean.setUpdationDateTime(dateTime);

			LOGGER.info("Inserting encounter status. userRole : " + userRoleBean.getCode() + " encounterStatus : "
					+ EncounterStatusConstant.DOCUMENT_NOT_RECEIVED);
			getHl7DaoFactory().getHl7Dao().insertRoleWiseEncounterStatus(roleWiseEncounterStatusBean);
			LOGGER.info("Encounter status inserted successfull. roleWiseEncounterStatusId : " + roleWiseEncounterStatusBean.getId());

		}
		LOGGER.debug("Exiting from addEncounterInDocumentNotReceiveStatus.");
	}

	private void updateEncounterStatusOnNewDocumentArrivalForReadyNotReady(Integer encounterId, Integer patientClassId, Integer serviceLineId,
			Integer documentTypeId, Integer userId, Date dateTime) throws Exception
	{
		LOGGER.debug("Inside updateEncounterStatusOnNewDocumentArrivalForReadyNotReady.");
		Map<Integer, RoleWiseEncounterStatusBean> roleWiseEncounterStatusMap = getHl7DaoFactory().getHl7Dao().getRoleWiseEncounterStatus(encounterId);
		if (roleWiseEncounterStatusMap != null && roleWiseEncounterStatusMap.size() > 0)
		{
			List<Integer> documentTypeIdList = getHl7DaoFactory().getHl7Dao().getEncounterDocumentTypeSet(encounterId);
			if (documentTypeIdList == null)
			{
				documentTypeIdList = new ArrayList<Integer>();
				documentTypeIdList.add(documentTypeId);
			} else
			{
				if (!documentTypeIdList.contains(documentTypeId))
				{
					documentTypeIdList.add(documentTypeId);
				}
			}
			Map<Integer, Set<Integer>> userRoleIdDocumentTypeIdSetMap = getHl7DaoFactory().getHl7Dao().getUserRoleRequiredDocumentTypeSet(
					patientClassId, serviceLineId);
			for (Integer userRoleId : roleWiseEncounterStatusMap.keySet())
			{
				boolean isUpdated = false;
				boolean isReady = false;
				RoleWiseEncounterStatusBean roleWiseEncounterStatusBean = roleWiseEncounterStatusMap.get(userRoleId);
				if (userRoleIdDocumentTypeIdSetMap != null && userRoleIdDocumentTypeIdSetMap.size() > 0)
				{
					Set<Integer> requiredDocumentTypeIdSet = userRoleIdDocumentTypeIdSetMap.get(userRoleId);
					if (requiredDocumentTypeIdSet != null && requiredDocumentTypeIdSet.size() > 0)
					{
						isReady = documentTypeIdList.containsAll(requiredDocumentTypeIdSet);
					} else
					{
						isReady = true;
					}
				}
				else
				{
					isReady = true;
				}
				if (isReady)
				{
					LOGGER.info("Updating encounter status " + roleWiseEncounterStatusBean.getStatus() + " to " + EncounterStatusConstant.READY
							+ " on new document arrival. encounterId : " + encounterId);
					roleWiseEncounterStatusBean.setStatus(EncounterStatusConstant.READY);
					isUpdated = true;
				} else if (EncounterStatusConstant.DOCUMENT_NOT_RECEIVED.equals(roleWiseEncounterStatusBean.getStatus()))
				{
					LOGGER.info("Updating encounter status " + roleWiseEncounterStatusBean.getStatus() + " to " + EncounterStatusConstant.NOT_READY
							+ " on new document arrival. encounterId : " + encounterId);
					roleWiseEncounterStatusBean.setStatus(EncounterStatusConstant.NOT_READY);
					isUpdated = true;
				}
				if (isUpdated)
				{
					roleWiseEncounterStatusBean.setUpdatotUserId(userId);
					roleWiseEncounterStatusBean.setUpdationDateTime(dateTime);
					getHl7DaoFactory().getHl7Dao().updateRoleWiseEncounterStatus(roleWiseEncounterStatusBean);
					LOGGER.info("Encounter status updated successfull to " + roleWiseEncounterStatusBean.getStatus() + ". roleWiseEncounterId : "
							+ roleWiseEncounterStatusBean.getId());
				}
			}
		}
		LOGGER.debug("Exiting from updateEncounterStatusOnNewDocumentArrivalForReadyNotReady.");
	}

	private PayerBean insertPayer(Map<String, Object> encounter) throws Exception
	{
		LOGGER.debug("Inside insertPayer.");
		PayerBean payerBean = null;
		String code = encounter.get(HL7Info.ENCOUNTER_PAYER).toString();
		payerBean = getHl7DaoFactory().getHl7Dao().getPayer(code);
		if (payerBean == null)
		{
			String desc = encounter.get(HL7Info.ENCOUNTER_PAYER_DESC).toString();
			payerBean = new PayerBean();
			payerBean.setCode(code);
			payerBean.setDesc(desc);
			getHl7DaoFactory().getHl7Dao().insertPayer(payerBean);

			LOGGER.info("Now publishing  Index Event for payerId: " + payerBean.getId());
			notifyUpdateToIndexById(payerBean.getId(), PayerBean.class);
			LOGGER.info("Published Index  Event for payerId: " + payerBean.getId());
		}
		LOGGER.debug("Exiting from insertPayer.");
		return payerBean;
	}

	@Override
	public Map<String, String> getTenantConfigMap() throws Exception
	{
		LOGGER.debug("Inside getTenantConfigMap.");
		Map<String, String> tenantConfigMap = getHl7DaoFactory().getHl7Dao().getTenantConfigMap();
		LOGGER.debug("Exiting from getTenantConfigMap.");
		return tenantConfigMap;
	}

	private int getUserLoginId(String userCode) throws Exception
	{
		LOGGER.debug("Inside getUserLoginId.");
		Integer hl7UserId = getHl7DaoFactory().getHl7Dao().getUserLoginId(CommonConstant.HL7_USER_CODE);
		if (hl7UserId == null)
		{
			throw new Exception("Unknown user code. userCode : " + userCode);
		}
		LOGGER.debug("Exiting from getUserLoginId.");
		return hl7UserId;
	}

	private boolean isValidDocument(Object document) throws Exception
	{
		LOGGER.debug("Inside isValidDocument.");
		boolean isValid = false;
		if (document != null)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> documentMap = (Map<String, Object>) document;
			Object documentName = documentMap.get(HL7Info.DOCUMENT_NAME);
			if (documentName != null && !documentName.toString().isEmpty())
			{
				isValid = true;
			}
		} else
		{
			throw new Exception("Document can not be a NULL or EMPTY. document : " + document);
		}
		LOGGER.debug("Exiting from isValidDocument.");
		return isValid;
	}

	private boolean isValidEncounter(Object encounter) throws Exception
	{
		boolean isValid = false;
		LOGGER.debug("Inside isValidEncounter.");
		if (encounter != null)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> encounterMap = (Map<String, Object>) encounter;
			Object mrn = encounterMap.get(HL7Info.ENCOUNTER_PATIENT_MRN);
			if (mrn != null && !mrn.toString().isEmpty())
			{
				Object encounterNumber = encounterMap.get(HL7Info.ENCOUNTER_NUMBER);
				if (encounterNumber != null && !encounterNumber.toString().isEmpty())
				{
					isValid = true;
				} else
				{
					throw new Exception("Encounter number can not be a NULL or EMPTY. encounterNumber : " + encounterNumber);
				}
			} else
			{
				throw new Exception("MRN can not be a NULL or EMPTY. mrn : " + mrn);
			}
		} else
		{
			throw new Exception("Encounter can not be a NULL or EMPTY. encounter : " + encounter);
		}
		LOGGER.debug("Exiting from isValidEncounter.");
		return isValid;
	}

	private LocationBean getLocation(String location) throws Exception
	{
		LOGGER.debug("Inside getLocation");
		LocationBean locationBean = getHl7DaoFactory().getHl7Dao().getLocation(location);
		if (locationBean == null)
		{
			locationBean = new LocationBean();
			locationBean.setCode(location);
			locationBean.setDesc(location);
			locationBean.setIsActive(CommonConstant.ACTIVE_LOCATION);
			locationBean = getHl7DaoFactory().getHl7Dao().insertLocation(locationBean);
		}
		LOGGER.debug("Exiting from getLocation");
		return locationBean;
	}

	private PatientClassBean getPatientClass(String patientClass) throws Exception
	{
		LOGGER.debug("Inside getPatientClass");
		PatientClassBean patientClassBean = getHl7DaoFactory().getHl7Dao().getPatientClass(patientClass);
		if (patientClassBean == null)
		{
			patientClassBean = new PatientClassBean();
			patientClassBean.setCode(patientClass);
			patientClassBean.setDesc(patientClass);
			patientClassBean = getHl7DaoFactory().getHl7Dao().insertPatientClass(patientClassBean);

			List<DocumentTypeBean> documentTypeBeanList = getHl7DaoFactory().getHl7Dao().getDocumentTypeList();
			List<UserRoleBean> userRoleBeanList = getHl7DaoFactory().getHl7Dao().getActiveUserRole();
			List<ServiceLineBean> serviceLineBeanList = getHl7DaoFactory().getHl7Dao().getServiceLineBeanList();
			if (documentTypeBeanList != null && documentTypeBeanList.size() > 0 && userRoleBeanList != null && userRoleBeanList.size() > 0)
			{
				for (DocumentTypeBean documentTypeBean : documentTypeBeanList)
				{
					for (UserRoleBean userRoleBean : userRoleBeanList)
					{
						if (serviceLineBeanList != null && serviceLineBeanList.size() > 0)
						{
							for (ServiceLineBean serviceLineBean : serviceLineBeanList)
							{
								insertServiceLineUserRoleDocumentType(documentTypeBean, userRoleBean, patientClassBean, serviceLineBean);
							}
						} else
						{
							insertServiceLineUserRoleDocumentType(documentTypeBean, userRoleBean, patientClassBean, null);
						}
					}

				}
			}
		}
		LOGGER.debug("Exiting from getPatientClass");
		return patientClassBean;
	}

	private LocationPatientClassBean getLocationPatientClass(LocationBean locationBean, PatientClassBean patientClassBean) throws Exception
	{
		LOGGER.debug("Inside getPatientClass");
		LocationPatientClassBean locationPatientClassBean = getHl7DaoFactory().getHl7Dao().getLocationPatientClass(locationBean.getId(),
				patientClassBean.getId());
		if (locationPatientClassBean == null)
		{
			locationPatientClassBean = new LocationPatientClassBean();
			locationPatientClassBean.setLocationId(locationBean.getId());
			locationPatientClassBean.setPatientClassId(patientClassBean.getId());
			locationPatientClassBean.setPatientClassCode(patientClassBean.getCode());
			locationPatientClassBean.setIsActive(CommonConstant.ACTIVE_LOCATION_PATIENT_CLASS);
			getHl7DaoFactory().getHl7Dao().insertLocationPatientClass(locationPatientClassBean);
		} else
		{
			if (CommonConstant.ACTIVE_LOCATION_PATIENT_CLASS != locationPatientClassBean.getIsActive())
			{
				locationPatientClassBean.setIsActive(CommonConstant.ACTIVE_LOCATION_PATIENT_CLASS);
				getHl7DaoFactory().getHl7Dao().updateLocationPatientClass(locationPatientClassBean);
			}
		}
		LOGGER.debug("Exiting from getPatientClass");
		return locationPatientClassBean;
	}

	private PatientTypeBean getPatientType(String patientType) throws Exception
	{
		LOGGER.debug("Inside getPatientType");
		PatientTypeBean patientTypeBean = getHl7DaoFactory().getHl7Dao().getPatientType(patientType);
		if (patientTypeBean == null)
		{
			patientTypeBean = new PatientTypeBean();
			patientTypeBean.setCode(patientType);
			patientTypeBean.setDesc(patientType);
			patientTypeBean = getHl7DaoFactory().getHl7Dao().insertPatientType(patientTypeBean);
		}
		LOGGER.debug("Exiting from getPatientType");
		return patientTypeBean;
	}

	private ServiceLineBean getServiceLine(String serviceLine) throws Exception
	{
		LOGGER.debug("Inside getServiceLine");
		ServiceLineBean serviceLineBean = getHl7DaoFactory().getHl7Dao().getServiceLine(serviceLine);
		if (serviceLineBean == null)
		{
			serviceLineBean = new ServiceLineBean();
			serviceLineBean.setCode(serviceLine);
			serviceLineBean.setDesc(serviceLine);
			serviceLineBean = getHl7DaoFactory().getHl7Dao().insertServiceLine(serviceLineBean);

			List<DocumentTypeBean> documentTypeBeanList = getHl7DaoFactory().getHl7Dao().getDocumentTypeList();
			List<UserRoleBean> userRoleBeanList = getHl7DaoFactory().getHl7Dao().getActiveUserRole();
			List<PatientClassBean> patientClassBeanList = getHl7DaoFactory().getHl7Dao().getPatientClassList();
			if (documentTypeBeanList != null && documentTypeBeanList.size() > 0 && userRoleBeanList != null && userRoleBeanList.size() > 0
					&& patientClassBeanList != null && patientClassBeanList.size() > 0)
			{
				for (DocumentTypeBean documentTypeBean : documentTypeBeanList)
				{
					for (UserRoleBean userRoleBean : userRoleBeanList)
					{
						for (PatientClassBean patientClassBean : patientClassBeanList)
						{
							insertServiceLineUserRoleDocumentType(documentTypeBean, userRoleBean, patientClassBean, serviceLineBean);
						}
					}
				}
			}
		}
		LOGGER.debug("Exiting from getServiceLine");
		return serviceLineBean;
	}

	private LocationPatientClassTypeServiceLineBean getLocationPatientClassTypeServiceLine(Integer locationPatientClassId, Integer patientTypeId,
			Integer serviceLineId) throws Exception
	{
		LOGGER.debug("Inside insertLocationPatientClassTypeServiceLine");
		LocationPatientClassTypeServiceLineBean locationPatientClassTypeServiceLineBean = getHl7DaoFactory().getHl7Dao()
				.getLocationPatientClassTypeServiceLine(locationPatientClassId, patientTypeId, serviceLineId);
		if (locationPatientClassTypeServiceLineBean == null)
		{
			locationPatientClassTypeServiceLineBean = new LocationPatientClassTypeServiceLineBean();
			locationPatientClassTypeServiceLineBean.setLocationPatientClassId(locationPatientClassId);
			locationPatientClassTypeServiceLineBean.setPatientTypeId(patientTypeId);
			locationPatientClassTypeServiceLineBean.setServiceLineId(serviceLineId);
			locationPatientClassTypeServiceLineBean.setIsActive(CommonConstant.ACTIVE_LOCATION_PATIENT_CLASS_TYPE_SERVICE_LINE);
			getHl7DaoFactory().getHl7Dao().insertLocationPatientClassTypeServiceLine(locationPatientClassTypeServiceLineBean);
		} else
		{
			if (CommonConstant.ACTIVE_LOCATION_PATIENT_CLASS_TYPE_SERVICE_LINE != locationPatientClassTypeServiceLineBean.getIsActive())
			{
				locationPatientClassTypeServiceLineBean.setIsActive(CommonConstant.ACTIVE_LOCATION_PATIENT_CLASS_TYPE_SERVICE_LINE);
				getHl7DaoFactory().getHl7Dao().updateLocationPatientClassTypeServiceLine(locationPatientClassTypeServiceLineBean);
			}
		}
		LOGGER.debug("Exiting from insertLocationPatientClassTypeServiceLine");
		return locationPatientClassTypeServiceLineBean;
	}

	private DocumentTypeBean getDocumentType(String documentType) throws Exception
	{
		LOGGER.debug("Inside getDocumentType");
		DocumentTypeBean documentTypeBean = getHl7DaoFactory().getHl7Dao().getDocumentType(documentType);
		if (documentTypeBean == null)
		{
			documentTypeBean = new DocumentTypeBean();
			documentTypeBean.setName(documentType);
			documentTypeBean = getHl7DaoFactory().getHl7Dao().insertDocumentType(documentTypeBean);

			List<UserRoleBean> userRoleBeanList = getHl7DaoFactory().getHl7Dao().getActiveUserRole();
			List<PatientClassBean> patientClassBeanList = getHl7DaoFactory().getHl7Dao().getPatientClassList();
			List<ServiceLineBean> serviceLineBeanList = getHl7DaoFactory().getHl7Dao().getServiceLineBeanList();

			for (UserRoleBean userRoleBean : userRoleBeanList)
			{
				for (PatientClassBean patientClassBean : patientClassBeanList)
				{
					if (serviceLineBeanList != null && serviceLineBeanList.size() > 0)
					{
						for (ServiceLineBean serviceLineBean : serviceLineBeanList)
						{
							insertServiceLineUserRoleDocumentType(documentTypeBean, userRoleBean, patientClassBean, serviceLineBean);
						}
					} else
					{
						insertServiceLineUserRoleDocumentType(documentTypeBean, userRoleBean, patientClassBean, null);
					}
				}
			}

		}
		LOGGER.debug("Exiting from getDocumentType");
		return documentTypeBean;
	}

	private ServiceLineUserRoleDocumentTypeBean insertServiceLineUserRoleDocumentType(DocumentTypeBean documentTypeBean, UserRoleBean userRoleBean,
			PatientClassBean patientClassBean, ServiceLineBean serviceLineBean) throws Exception
	{
		LOGGER.debug("Inside insertServiceLineUserRoleDocumentType");
		ServiceLineUserRoleDocumentTypeBean serviceLineUserRoleDocumentTypeBean = null;
		if (documentTypeBean != null && userRoleBean != null && patientClassBean != null)
		{
			if (serviceLineBean != null)
			{
				serviceLineUserRoleDocumentTypeBean = getHl7DaoFactory().getHl7Dao().getServiceLineUserRoleDocumentType(documentTypeBean.getId(),
						userRoleBean.getId(), patientClassBean.getId(), serviceLineBean.getId());
			} else
			{
				serviceLineUserRoleDocumentTypeBean = getHl7DaoFactory().getHl7Dao().getServiceLineUserRoleDocumentType(documentTypeBean.getId(),
						userRoleBean.getId(), patientClassBean.getId(), null);
			}
			if (serviceLineUserRoleDocumentTypeBean == null)
			{
				serviceLineUserRoleDocumentTypeBean = new ServiceLineUserRoleDocumentTypeBean();

				serviceLineUserRoleDocumentTypeBean.setDocumentTypeId(documentTypeBean.getId());
				serviceLineUserRoleDocumentTypeBean.setDocumentTypeName(documentTypeBean.getName());
				serviceLineUserRoleDocumentTypeBean.setDocumentTypeDisplayName(documentTypeBean.getName());

				serviceLineUserRoleDocumentTypeBean.setUserRoleId(userRoleBean.getId());
				serviceLineUserRoleDocumentTypeBean.setUserRoleName(userRoleBean.getCode());

				serviceLineUserRoleDocumentTypeBean.setPatientClassId(patientClassBean.getId());
				serviceLineUserRoleDocumentTypeBean.setPatientClassCode(patientClassBean.getCode());

				if (serviceLineBean != null)
				{
					serviceLineUserRoleDocumentTypeBean.setServiceLineId(serviceLineBean.getId());
					serviceLineUserRoleDocumentTypeBean.setServiceLineCode(serviceLineBean.getCode());
				}
				serviceLineUserRoleDocumentTypeBean.setIsActive(CommonConstant.ACTIVE_SERVICE_LINE_USER_ROLE_DOCUMENT_TYPE);

				int sequenceNumber = getHl7DaoFactory().getHl7Dao().getDocumentTypeMaxSequenceNumber(userRoleBean.getId(), patientClassBean.getId(),
						serviceLineBean.getId());
				sequenceNumber++;
				serviceLineUserRoleDocumentTypeBean.setSequenceNumber(sequenceNumber);
				getHl7DaoFactory().getHl7Dao().insertServiceLineUserRoleDocumentType(serviceLineUserRoleDocumentTypeBean);
			}
		}
		LOGGER.debug("Exiting from insertServiceLineUserRoleDocumentType");
		return serviceLineUserRoleDocumentTypeBean;
	}

	private void changePatientClass(EncounterBean encounterBean, String oldPatientClass) throws Exception
	{
		LOGGER.debug("Inside insertServiceLineUserRoleDocumentType");
		int encounterId = encounterBean.getId();
		String newPatientClass = encounterBean.getPatientClassCode();
		int newPatientClassId = encounterBean.getPatientClassId();
		int locationId = encounterBean.getLocationId();
		HL7Dao hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
		FileAllocationBean fileAllocationBean = hl7DaoImpl.getFileAllocationBean(encounterId);
		boolean isReviewerWorking = false;
		boolean isCoderWorking = false;
		if (fileAllocationBean != null)
		{
			isReviewerWorking = fileAllocationBean.getReviewerEncounterStatus() != null
					&& (!fileAllocationBean.getReviewerEncounterStatus().equalsIgnoreCase(EncounterStatusConstant.BILLED));

			isCoderWorking = (fileAllocationBean.getCoderEncounterStatus() != null
					&& (!fileAllocationBean.getCoderEncounterStatus().equalsIgnoreCase(EncounterStatusConstant.BILLED)) && (!fileAllocationBean
					.getCoderEncounterStatus().equalsIgnoreCase(EncounterStatusConstant.COMPLETED)));

			Integer userId = null;
			boolean stillHasAccess = false;
			if (isCoderWorking)
			{
				userId = fileAllocationBean.getCoderId();
			} else if (isReviewerWorking)
			{
				userId = fileAllocationBean.getReviewerId();
			}
			if (userId != null)
			{
				stillHasAccess = hl7DaoImpl.hasNewPatientClassAccess(encounterId, newPatientClass, newPatientClassId, locationId, userId);
			}
			if (stillHasAccess)
			{
				doProcessForPatientClassChange(encounterBean, oldPatientClass, newPatientClass);
			} else
			{
				doCaseAsFresh(encounterBean.getId());
			}
		} else
		{
			doProcessForPatientClassChange(encounterBean, oldPatientClass, newPatientClass);
		}
		insertEncounterWorkListMapHL7(encounterBean, false);
		LOGGER.debug("Inside insertServiceLineUserRoleDocumentType");
	}

	// satyam new code

	private void doProcessForPatientClassChange(EncounterBean encounterBean, final String oldPatientClass, String newPatientClass)
	{
		// FileAllocation : Make changes here , Change Case Status to Updated.
		// DrgResponseValues : Make the entry Inactive.
		// Reset GroupingSources entries to reset to default.
		// Reset Resequence_Options entries.
		// Reset Edit_Options entries.
		try
		{
			HL7Dao hl7DaoImpl = null;
			int encounterId = encounterBean.getId();
			LOGGER.info(" Checking FileAllocation.... for encounterId : " + encounterId);
			hl7DaoImpl = getHl7DaoFactory().getHl7Dao();
			FileAllocationBean fileAllocationBean = hl7DaoImpl.getFileAllocationBean(encounterId);
			LOGGER.info(" FileAllocation entry " + ((fileAllocationBean != null) ? "" : "doesnt") + "exists for encounterId : " + encounterId);
			if (fileAllocationBean != null)
			{
				LOGGER.info(" Now Checking for On Hold Status for encounter Id :" + encounterId);
				// EncounterStatusBean encounterStatusBean = dao.getEncounterStatus(EncounterStatusConstant.UPDATED);
				// final int updatedStatusId = encounterStatusBean.getEncounterStatusId();
				final Date utcDateTime = DateUtils.getUTCDateTime();
				final int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				if (fileAllocationBean.getCoderEncounterStatus().equalsIgnoreCase(EncounterStatusConstant.ON_HOLD))
				{
					LOGGER.info(" Found On Hold Status for CODER. encounter Id :" + encounterId);
					LOGGER.info("Updating Status to UPDATED for CODER. encounter Id :" + encounterId);

					fileAllocationBean.setCoderEncounterStatus(EncounterStatusConstant.UPDATED);
					fileAllocationBean.setLastUpdatedEncounterStatusCode(EncounterStatusConstant.UPDATED);
					fileAllocationBean.setLastUpdatedRole(UserRoleConstant.CODER);
					fileAllocationBean.setUpdatedDTS(utcDateTime);
					fileAllocationBean.setUpdateUserId(hl7UserId);
					hl7DaoImpl.updateFileAllocationBean(fileAllocationBean);

					LOGGER.info("Updated Status to UPDATED for CODER. encounter Id :" + encounterId);

					doProcessForDrgResponseValues(encounterBean);
					doProcessForResetGrouping(encounterBean);
					doProcessForResetResquence(encounterBean);
					doProcessForResetEditOptions(encounterBean);

				} else if (fileAllocationBean.getReviewerEncounterStatus().equalsIgnoreCase(EncounterStatusConstant.ON_HOLD))
				{

					LOGGER.info(" Found On Hold Status for REVIEWER. encounter Id :" + encounterId);
					LOGGER.info("Updating Status to UPDATED for REVIEWER. encounter Id :" + encounterId);

					fileAllocationBean.setReviewerEncounterStatus(EncounterStatusConstant.UPDATED);
					fileAllocationBean.setLastUpdatedEncounterStatusCode(EncounterStatusConstant.UPDATED);
					fileAllocationBean.setLastUpdatedRole(UserRoleConstant.REVIEWER);
					fileAllocationBean.setUpdatedDTS(utcDateTime);
					fileAllocationBean.setUpdateUserId(hl7UserId);
					hl7DaoImpl.updateFileAllocationBean(fileAllocationBean);

					LOGGER.info("Updated Status to UPDATED for REVIEWER. encounter Id :" + encounterId);

					doProcessForDrgResponseValues(encounterBean);
					doProcessForResetGrouping(encounterBean);
					doProcessForResetResquence(encounterBean);
					doProcessForResetEditOptions(encounterBean);

				} else if (fileAllocationBean.getCdiEncounterStatus().equalsIgnoreCase(EncounterStatusConstant.ON_HOLD))
				{

					LOGGER.info(" Found On Hold Status for CDI. encounter Id :" + encounterId);
					LOGGER.info("Updating Status to UPDATED for CDI. encounter Id :" + encounterId);

					fileAllocationBean.setCdiEncounterStatus(EncounterStatusConstant.UPDATED);
					fileAllocationBean.setLastUpdatedEncounterStatusCode(EncounterStatusConstant.UPDATED);
					fileAllocationBean.setLastUpdatedRole(UserRoleConstant.CDI);
					fileAllocationBean.setUpdatedDTS(utcDateTime);
					fileAllocationBean.setUpdateUserId(hl7UserId);
					hl7DaoImpl.updateFileAllocationBean(fileAllocationBean);

					LOGGER.info("Updated Status to UPDATED for CDI. encounter Id :" + encounterId);

					doProcessForDrgResponseValues(encounterBean);
					doProcessForResetGrouping(encounterBean);
					doProcessForResetResquence(encounterBean);
					doProcessForResetEditOptions(encounterBean);

				} else
				{

					LOGGER.info(" No On Hold Entries Found for encounterId :" + encounterId);
				}
			}

		} catch (Exception ex)
		{
			LOGGER.error(ex);
		}

	}

	private void doProcessForDrgResponseValues(final EncounterBean encounterBean)
	{
		try
		{
			final int encounterId = encounterBean.getId();
			List<DRGResponseValuesBean> drgs = getHl7DaoFactory().getHl7Dao().getDrgResponses(encounterId);
			if (drgs != null && !drgs.isEmpty())
			{
				LOGGER.info("Found Active Drgs count :" + drgs.size() + " for  encounter Id :" + encounterId);
				for (DRGResponseValuesBean d : drgs)
				{
					d.setActiveRecord(0);
				}
				getHl7DaoFactory().getHl7Dao().updateDrgResponses(drgs);
			} else
			{
				LOGGER.info("No Found Active Drgs for  encounter Id :" + encounterId);
			}

		} catch (Exception ex)
		{
			LOGGER.error(ex);
		}
	}

	private void doProcessForResetGrouping(final EncounterBean encounterBean)
	{
		try
		{
			final int encounterId = encounterBean.getId();
			final String classCode = encounterBean.getPatientClassCode();
			List<GroupingSourcesBean> defaults = getHl7DaoFactory().getHl7Dao().getDefaultGroupingSource(classCode);
			List<GroupingSourcesBean> grouping = getHl7DaoFactory().getHl7Dao().getGroupingSource(encounterId);
			List<GroupingSourcesBean> listToSave = new ArrayList<>();
			if (grouping != null && !grouping.isEmpty())
			{
				LOGGER.info("Found Active Grouping count :" + grouping.size() + " for  encounter Id :" + encounterId);
				LOGGER.info("Inactivating Old Grouping  for  encounter Id :" + encounterId);
				final Date utcDateTime = DateUtils.getUTCDateTime();
				final int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				for (GroupingSourcesBean e : grouping)
				{
					e.setActiveRecord(0);
					e.setUpdateUserId(hl7UserId);
					e.setUpdateDTS(utcDateTime);
					listToSave.add(e);

				}
				LOGGER.info("Adding reset Old Grouping  for  encounter Id :" + encounterId);
				GroupingSourcesBean g = null;
				for (GroupingSourcesBean e : defaults)
				{
					g = new GroupingSourcesBean();
					g.setActiveRecord(1);

					g.setUpdateDTS(utcDateTime);
					g.setCreatorDTS(utcDateTime);
					g.setUpdateUserId(hl7UserId);
					g.setCreatorUserId(hl7UserId);

					g.setPatientClassDesc(e.getPatientClassDesc());
					g.setPatientClassId(e.getPatientClassId());
					g.setPatientClassCode(e.getPatientClassCode());

					g.setCodingSystem(e.getCodingSystem());

					g.setAprAdmitLogic(e.getAprAdmitLogic());
					g.setCalculateAPCForAllClaimDispositions(e.getCalculateAPCForAllClaimDispositions());
					g.setCostOutlierPayments(e.getCostOutlierPayments());

					g.setDrgPrepopulate(e.getDrgPrepopulate());

					g.setEncounterId(encounterId);
					g.setExcludeAllFeeSchedulePricing(e.getExcludeAllFeeSchedulePricing());

					g.setGrouperType(e.getGrouperType());
					g.setGrouperVersion(e.getGrouperVersion());
					// g.setGroupingSourcesID(e.getGroupingSourcesID());

					g.setIsDefault(0);
					g.setMedicareProviderNumber(e.getMedicareProviderNumber());
					g.setUserRoleUserId(e.getUserRoleUserId());
					g.setUseCMSHASLogin(e.getUseCMSHASLogin());
					g.setUserAlterGrouper(e.getUserAlterGrouper());
					g.setUseCMS24Reimbursement(e.getUseCMS24Reimbursement());
					g.setUseCahLogic(e.getUseCahLogic());
					g.setSimplePricing(e.getSimplePricing());
					g.setRate(e.getRate());
					g.setPrimary(e.getPrimary());
					g.setPricerValue(e.getPricerValue());
					g.setPricerType(e.getPricerType());
					g.setPricerDescription(e.getPricerDescription());

					listToSave.add(g);

				}

				getHl7DaoFactory().getHl7Dao().saveUpdateGroupingSource(listToSave);

			} else
			{
				LOGGER.info("Not Found Grouping  for  encounter Id :" + encounterId);
			}

		} catch (Exception ex)
		{
			LOGGER.error(ex);
		}
	}

	private void doProcessForResetResquence(final EncounterBean encounterBean)
	{
		try
		{
			final int encounterId = encounterBean.getId();
			final String classCode = encounterBean.getPatientClassCode();
			List<ResequenceOptionsBean> defaults = getHl7DaoFactory().getHl7Dao().getDefaultReseqOptions(classCode);
			List<ResequenceOptionsBean> grouping = getHl7DaoFactory().getHl7Dao().getReseqOptions(encounterId);
			List<ResequenceOptionsBean> listToSave = new ArrayList<>();
			if (grouping != null && !grouping.isEmpty())
			{
				LOGGER.info("Found Active Resequence count :" + grouping.size() + " for  encounter Id :" + encounterId);
				LOGGER.info("Inactivating Old Resequence  for  encounter Id :" + encounterId);
				final Date utcDateTime = DateUtils.getUTCDateTime();
				final int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				for (ResequenceOptionsBean e : grouping)
				{
					e.setActiveRecord(0);
					e.setUpdateUserId(hl7UserId);
					e.setUpdateDTS(utcDateTime);
					listToSave.add(e);

				}
				LOGGER.info("Adding resetted Old Resequence  for  encounter Id :" + encounterId);
				ResequenceOptionsBean g = null;
				for (ResequenceOptionsBean e : defaults)
				{
					g = new ResequenceOptionsBean();
					g.setActiveRecord(1);

					g.setUpdateDTS(utcDateTime);
					g.setCreatorDTS(utcDateTime);
					g.setUpdateUserId(hl7UserId);
					g.setCreatorUserId(hl7UserId);

					g.setPatientClassDesc(e.getPatientClassDesc());
					g.setPatientClassId(e.getPatientClassId());
					g.setPatientClassCode(e.getPatientClassCode());

					g.setResequenceAll(e.getResequenceAll());

					g.setDiagnosesResequenceCount(e.getDiagnosesResequenceCount());
					g.setProceduresResequeceCount(e.getProceduresResequeceCount());
					g.setOnlyCheckProcedures(e.getOnlyCheckProcedures());

					g.setOnlyCheckDiagnoses(e.getOnlyCheckDiagnoses());

					g.setEncounterId(encounterId);
					g.setNeedProcedureResequence(e.getNeedProcedureResequence());

					g.setGrouperType(e.getGrouperType());
					g.setNeedDiagnosisResequence(e.getNeedDiagnosisResequence());

					g.setIsDefault(0);

					listToSave.add(g);

				}

				getHl7DaoFactory().getHl7Dao().saveUpdateReseqOptions(listToSave);

			} else
			{
				LOGGER.info("Not Found Resequence  for  encounter Id :" + encounterId);
			}

		} catch (Exception ex)
		{
			LOGGER.error(ex);
		}
	}

	private void doProcessForResetEditOptions(final EncounterBean encounterBean)
	{
		try
		{
			final int encounterId = encounterBean.getId();
			final String classCode = encounterBean.getPatientClassCode();
			List<EditOptionsBean> defaults = getHl7DaoFactory().getHl7Dao().getDefaultEditOptions(classCode);
			List<EditOptionsBean> grouping = getHl7DaoFactory().getHl7Dao().getEditOptions(encounterId);
			List<EditOptionsBean> listToSave = new ArrayList<>();
			if (grouping != null && !grouping.isEmpty())
			{
				LOGGER.info("Found Active EditOptionsBean count :" + grouping.size() + " for  encounter Id :" + encounterId);
				LOGGER.info("Inactivating Old EditOptionsBean  for  encounter Id :" + encounterId);
				final Date utcDateTime = DateUtils.getUTCDateTime();
				final int hl7UserId = getUserLoginId(CommonConstant.HL7_USER_CODE);
				for (EditOptionsBean e : grouping)
				{
					e.setActiveRecord(0);
					e.setUpdateUserId(hl7UserId);
					e.setUpdateDTS(utcDateTime);
					listToSave.add(e);

				}
				LOGGER.info("Adding resetted Old EditOptionsBean  for  encounter Id :" + encounterId);
				EditOptionsBean g = null;
				for (EditOptionsBean e : defaults)
				{
					g = new EditOptionsBean();
					g.setActiveRecord(1);

					g.setUpdateDTS(utcDateTime);
					g.setCreatorDTS(utcDateTime);
					g.setUpdateUserId(hl7UserId);
					g.setCreatorUserId(hl7UserId);

					g.setPatientClassDesc(e.getPatientClassDesc());
					g.setPatientClassId(e.getPatientClassId());
					g.setPatientClassCode(e.getPatientClassCode());

					g.setApplyDRGEdits(e.getApplyDRGEdits());
					g.setApplyECodeEdits(e.getApplyECodeEdits());
					g.setApplyECodePlaceOfOccurrenceEdits(e.getApplyECodePlaceOfOccurrenceEdits());
					g.setApplyMUEEdits(e.getApplyMUEEdits());
					g.setApplyRACEdits(e.getApplyRACEdits());
					g.setApplyTruCodeEdits(e.getApplyTruCodeEdits());

					g.setGrouperType(e.getGrouperType());
					// g.setEditOptionsId(e.getCalculateAPCForAllClaimDispositions());

					g.setEncounterId(encounterId);

					g.setGrouperType(e.getGrouperType());

					g.setIsDefault(0);

					listToSave.add(g);

				}

				getHl7DaoFactory().getHl7Dao().saveUpdateEditOptions(listToSave);

			} else
			{
				LOGGER.info("Not Found EditOptions  for  encounter Id :" + encounterId);
			}

		} catch (Exception ex)
		{
			LOGGER.error(ex);
		}
	}

	private boolean doCaseAsFresh(final Integer encounterId) throws Exception
	{
		try
		{
			final HL7Dao dao = getHl7DaoFactory().getHl7Dao();
			Map recodedBilledEnounter = dao.getBillingRecodedEncounters(encounterId);
			Map completedEnounter = dao.getCompletedDateForReviewEncounters(encounterId);
			Map workingEncounter = null;
			Map finalMap = null;
			boolean isRecoded = false;
			boolean isSendForReview = false;
			boolean isCodingStarted = false;

			if (recodedBilledEnounter != null && completedEnounter != null)
			{
				finalMap = completedEnounter;
				isRecoded = true;
				isSendForReview = true;
			} else if (recodedBilledEnounter != null)
			{
				finalMap = recodedBilledEnounter;
				isRecoded = true;
			} else if (completedEnounter != null)
			{
				finalMap = completedEnounter;
				isSendForReview = true;
			} else
			{
				workingEncounter = dao.getWorkingDateForCoderEncounters(encounterId);
				if (workingEncounter != null)
				{
					finalMap = workingEncounter;
					isCodingStarted = true;
				} else
				{
					finalMap = null;
				}
			}

			if (finalMap != null)
			{
				Date date = (Date) finalMap.get("date");
				boolean done = dao.doCaseRelatedChangesForClassChange(encounterId, date);
			}

			if (isCodingStarted)
			{
				dao.deleteFileAllocation(encounterId);
			} else if (isRecoded && isSendForReview)
			{
				dao.removeReviewerFileAllocation(encounterId);
			} else if (isRecoded)
			{
				dao.removeReviewerFileAllocation(encounterId);
				dao.setCoderAsBilled(encounterId);
			} else if (isSendForReview)
			{
				dao.removeReviewerFileAllocation(encounterId);
			}

			return false;
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}

	}

	// satyam old code

	private boolean makeCaseAsFresh(final Set<Integer> encounterIdSet, final String role, boolean isRecoded, boolean isBilledOnce, boolean isCompleted)
			throws Exception
	{
		LOGGER.info("inside makeCaseAsFresh(HL7ServiceImpl) service");
		try
		{
			final Integer userRoleId = getHl7DaoFactory().getHl7Dao().getUserRoleIdFromCode(role);

			deleteIssues(encounterIdSet, userRoleId);
			deleteDRGValues(encounterIdSet, userRoleId);
			final List<Long> codeMapByUserIdList = getCodeMapByUserIdList(encounterIdSet, userRoleId);
			if (codeMapByUserIdList != null && !codeMapByUserIdList.isEmpty())
			{
				deleteCodeHistory(codeMapByUserIdList);
				deleteEncoderEdits(codeMapByUserIdList);
				deleteCodes(codeMapByUserIdList);
			}
			final List<Integer> evidenceIdList = getEvidenceIdList(encounterIdSet, userRoleId);
			if (evidenceIdList != null && !evidenceIdList.isEmpty())
			{
				deleteQueryEvidence(evidenceIdList);
				deleteEvidence(evidenceIdList);
			}
			final List<Integer> userRoleMapIdList = getUserRoleUserMapIdList(encounterIdSet, userRoleId);
			if (encounterIdSet != null && !encounterIdSet.isEmpty() && userRoleMapIdList != null && !userRoleMapIdList.isEmpty())
			{
				deleteEncounterDDMapEntries(encounterIdSet, userRoleMapIdList);
				deleteGroupingSourcesEntries(encounterIdSet, userRoleMapIdList);
			}
			LOGGER.info("exit from makeCaseAsFresh(HL7ServiceImpl) service");
			return true;
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	private boolean deleteIssues(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		LOGGER.info("inside deleteIssues(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteIssues(encounterIdSet, roleId);
	}

	private boolean deleteDRGValues(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		LOGGER.info("inside deleteDRGValues(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteDRGValues(encounterIdSet, roleId);
	}

	private List<Long> getCodeMapByUserIdList(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		LOGGER.info("inside getCodeMapByUserIdList(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().getCodeMapByUserIdList(encounterIdSet, roleId);
	}

	private boolean deleteCodeHistory(final List<Long> codeMapByUserIdList) throws Exception
	{
		LOGGER.info("inside deleteCodeHistory(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteCodeHistory(codeMapByUserIdList);
	}

	private boolean deleteEncoderEdits(final List<Long> codeMapByUserIdList) throws Exception
	{
		LOGGER.info("inside deleteEncoderEdits(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteEncoderEdits(codeMapByUserIdList);
	}

	private boolean deleteCodes(final List<Long> codeMapByUserIdList) throws Exception
	{
		LOGGER.info("inside deleteCodes(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteCodes(codeMapByUserIdList);
	}

	private List<Integer> getEvidenceIdList(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		LOGGER.info("inside getEvidenceIdList(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().getEvidenceIdList(encounterIdSet, roleId);
	}

	private boolean deleteQueryEvidence(final List<Integer> evidenceIdList) throws Exception
	{
		LOGGER.info("inside deleteQueryEvidence(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteQueryEvidence(evidenceIdList);
	}

	private boolean deleteEvidence(final List<Integer> evidenceIdList) throws Exception
	{
		LOGGER.info("inside deleteEvidence(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteEvidence(evidenceIdList);
	}

	private List<Integer> getUserRoleUserMapIdList(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		LOGGER.info("insert into getUserHospitalMapIdList(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().getUserRoleUserMapIdList(encounterIdSet, roleId);
	}

	private boolean deleteEncounterDDMapEntries(final Set<Integer> encounterIdSet, final List<Integer> encounterUserRoleMapIdList) throws Exception
	{
		LOGGER.info("insert into deleteEncounterDDMapEntries(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteEncounterDDMapEntries(encounterIdSet, encounterUserRoleMapIdList);
	}

	private boolean deleteGroupingSourcesEntries(final Set<Integer> encounterIdSet, final List<Integer> encounterUserRoleMapIdList) throws Exception
	{
		LOGGER.info("insert into deleteGroupingSourcesEntries(LandingPageService) service");
		return getHl7DaoFactory().getHl7Dao().deleteGroupingSourcesEntries(encounterIdSet, encounterUserRoleMapIdList);
	}

	// chirag new code

	private boolean insertEncounterWorkListMapHL7(EncounterBean encounterInformationBean, final boolean isNewEncounter) throws Exception
	{
		try
		{
			boolean isEncounterWorkListUpdated = false;
			if (encounterInformationBean != null)
			{
				final Map<Integer, String> roleMap = new HashMap<>();
				if (!isNewEncounter)
				{
					List<Map<String, Object>> roleMapList = getHl7DaoFactory().getHl7Dao().getExistingRoleForGivenEncounterIdHL7(
							encounterInformationBean.getId());
					if (roleMapList != null && !roleMapList.isEmpty())
					{
						for (Map<String, Object> raw : roleMapList)
						{
							roleMap.put((Integer) raw.get("roleId"), raw.get("roleName").toString());
						}
					}
					getHl7DaoFactory().getHl7Dao().removeAllEncounterWorklistMapForGivenEncounterIdHL7(encounterInformationBean.getId());
				} else
				{
					List<UserRoleBean> allRoleList = getHl7DaoFactory().getHl7Dao().getActiveUserRole();
					if (allRoleList != null && !allRoleList.isEmpty())
					{
						for (UserRoleBean bean : allRoleList)
						{
							if (!(bean.getCode().contains("Supervisor") || bean.getCode().contains("Physician")))
							{
								roleMap.put(bean.getId(), bean.getCode());
							}
						}
					}
				}

				List<WorkListBean> workListList = getHl7DaoFactory().getHl7Dao().getWorkListListForGivenEncounterHL7(encounterInformationBean);
				Map<Integer, String> workListPriorityMap = null;
				if (workListList != null && !workListList.isEmpty())
				{
					workListPriorityMap = new HashMap<>();
					for (WorkListBean workListBean : workListList)
					{
						workListPriorityMap.put(workListBean.getId(),
								checkEncounterPriorityBasedOnWorkListCondition(null, workListBean.getConditions()));
					}
				}
				isEncounterWorkListUpdated = getHl7DaoFactory().getHl7Dao().insertEncounterWorkListMapHL7(encounterInformationBean.getId(),
						workListPriorityMap, roleMap);
			}
			return isEncounterWorkListUpdated;
		} catch (Exception e)
		{
			LOGGER.trace(e);
			throw e;
		}
	}

	private String checkEncounterPriorityBasedOnWorkListCondition(final EncounterBean encounterInfo, final String workListCondition) throws Exception
	{

		try
		{
			LOGGER.info("Calculating priority for given encounter based on worklist condition.");
			String priority;
			if (workListCondition == null)
			{
				priority = WorkListConstants.ENCOUNTER_PRIORITY_LOW;
			} else
			{
				final String[] conditionStringArray = workListCondition.split(" ");
				String priviousBreaketOperator = null;
				boolean priviousBreaketBoolean = false;

				String priousOperator = null;
				boolean priviousBoolean = false;

				boolean currentStatus = false;
				String left;
				String right;
				for (int index = conditionStringArray.length - 1; index >= 0; index--)
				{
					if (conditionStringArray[index].equalsIgnoreCase("(") && priviousBreaketOperator != null)
					{
						currentStatus = checkBooleanCondition(currentStatus, priviousBreaketBoolean, priviousBreaketOperator);
						priviousBreaketOperator = null;

					} else if (conditionStringArray[index].equalsIgnoreCase("and") || conditionStringArray[index].equalsIgnoreCase("or"))
					{
						if ("(".equalsIgnoreCase(conditionStringArray[index + 1]))
						{
							priviousBreaketOperator = conditionStringArray[index];
							priviousBreaketBoolean = currentStatus;
						} else
						{
							priousOperator = conditionStringArray[index];
							priviousBoolean = currentStatus;
						}
					} else if (conditionStringArray[index].equalsIgnoreCase(">="))
					{
						right = conditionStringArray[index + 1];
						left = conditionStringArray[index - 1];
						currentStatus = checkStringCondition(left, right, getEncounterInfoForPriority(encounterInfo));
						index--;
						if (priousOperator != null)
						{
							currentStatus = checkBooleanCondition(currentStatus, priviousBoolean, priousOperator);
							priousOperator = null;
						}
					}
				}
				priority = currentStatus ? WorkListConstants.ENCOUNTER_PRIORITY_HIGH : WorkListConstants.ENCOUNTER_PRIORITY_LOW;
			}
			return priority;
		} catch (Exception e)
		{
			LOGGER.trace(e);
			throw e;
		}
	}

	private boolean checkBooleanCondition(boolean left, boolean right, String operator) throws Exception
	{
		try
		{
			boolean returnData = false;
			if (WorkListConstants.AND.equalsIgnoreCase(operator))
			{
				returnData = left && right;
			} else if (WorkListConstants.OR.equalsIgnoreCase(operator))
			{
				returnData = left || right;
			}
			return returnData;
		} catch (Exception e)
		{
			LOGGER.error(e);
			throw e;
		}
	}

	private boolean checkStringCondition(String left, String right, Map<String, Object> encounterInfo) throws Exception
	{
		try
		{
			boolean result = false;

			if (encounterInfo != null && !encounterInfo.isEmpty())
			{
				int rightInt = Integer.parseInt(right);
				int DNFC = (Integer) encounterInfo.get("DNFC");
				int los = (Integer) encounterInfo.get("LOS");
				double caseValue = (Double) encounterInfo.get("caseValue");
				switch (left)
				{
				case WorkListConstants.DNFC:
					result = (DNFC >= rightInt);
					break;
				case WorkListConstants.LOS:
					result = (los >= rightInt);
					break;
				case WorkListConstants.CASE_VALUE:
					result = (caseValue >= rightInt);
					break;

				default:
					break;
				}
			}
			return result;
		} catch (Exception e)
		{
			LOGGER.trace(e);
			throw e;
		}
	}

	private Map<String, Object> getEncounterInfoForPriority(final EncounterBean encounter) throws Exception
	{
		try
		{
			Map<String, Object> response = null;
			if (encounter != null)
			{
				response = new HashMap<>();

				double caseValue = 0;
				final Date admissionDate = encounter.getDateOfAdmition();
				final Date dischargeDate = encounter.getDateOfDischarge();
				int DNFC = getDaysDifference(dischargeDate, new Date());
				int LOS = getDaysDifference(dischargeDate, admissionDate);
				if (encounter.getTotalCharges() != null)
				{
					caseValue = Double.parseDouble(encounter.getTotalCharges());
				}

				response.put("DNFC", DNFC);
				response.put("LOS", LOS);
				response.put("caseValue", caseValue);
			}
			return response;
		} catch (Exception e)
		{
			LOGGER.error(e);
			throw e;
		}
	}

	private int getDaysDifference(Date toDate, Date fromDate)
	{

		int los = 0;

		try
		{
			LocalDate from = new LocalDate(fromDate);
			LocalDate to;
			if (toDate == null)
			{
				to = new LocalDate(new Date());
			} else
			{
				to = new LocalDate(toDate);
			}
			los = Days.daysBetween(from, to).getDays();
		} catch (Exception e)
		{
			LOGGER.trace(e);
			LOGGER.trace("Error while calculating LOS … " + e);
		}
		return los;
	}
}
