package com.ezdi.cac.controller.hl7;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezdi.cac.bean.hl7.HL7WSResponse;
import com.ezdi.cac.constant.CommonConstant;
import com.ezdi.cac.constant.HL7Action;
import com.ezdi.cac.constant.HL7Info;
import com.ezdi.cac.constant.HL7ProcessingStatus;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;
import com.ezdi.component.logger.EzdiMDC;

@Controller
public class HL7Controller extends HL7BaseController
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(HL7Controller.class);

	@RequestMapping(value = "/execute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody HL7WSResponse execute(@RequestBody Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.info("=============================================================================================================================================");
		LOGGER.debug("Inside execute");
		HL7WSResponse processingStatus = null;
		String responseCode = null;
		String responseMessage = null;
		try
		{
			LOGGER.debug("Inbound message : " + hl7InfoMap);
			boolean isValid = isValidHL7Information(hl7InfoMap);
			if (isValid)
			{
				String tenantCode = hl7InfoMap.get(HL7Info.TENANT_CODE).toString();
				LOGGER.info("Tenant code : " + tenantCode);

				TenantThreadLocal.setTenantCode(tenantCode);
				EzdiMDC.put("tenantInfo", tenantCode);
				EzdiMDC.put("userInfo", CommonConstant.HL7_USER_CODE);

				String hl7Action = hl7InfoMap.get(HL7Info.ACTION).toString();
				LOGGER.info("HL7 action : " + hl7Action);
				try
				{
					if (HL7Action.CREATE_OR_UPDATE_CASE.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().createOrUpdateCase(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					} else if (HL7Action.CANCEL_ADMIT.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().cancelAdmit(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					} else if (HL7Action.ADD_DOCUMENT.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().addDocument(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					} else if (HL7Action.ADD_SCANNED_DOCUMENT.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().addScannedDocument(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					} else if (HL7Action.ADD_OBSERVATION.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().addObservation(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					}else if (HL7Action.MERGE_PATIENT_INFORMATION.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().mergePatientInformation(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					}else if (HL7Action.SWAP_PATIENT_LOCATION.equals(hl7Action))
					{
						getHl7ServiceFactory().getHl7Service().lockEncounter(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().swapPatientLocation(hl7InfoMap);
						getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
					}
					else
					{
						throw new Exception("Unkonwn hl7 action. hl7Action : " + hl7Action);
					}
				} catch (Exception e)
				{
					throw e;
				} finally
				{
					getHl7ServiceFactory().getHl7Service().unLockEncounter(hl7InfoMap);
				}
				responseCode = HL7ProcessingStatus.SUCCESS;
				responseMessage = hl7Action + " successfully execute.";
				processingStatus = new HL7WSResponse(responseCode, responseMessage);
				LOGGER.info("processingStatus : " + processingStatus);
			}
		} catch (Exception e)
		{
			LOGGER.error(e);
			e.printStackTrace();
			responseCode = HL7ProcessingStatus.FAIL;
			responseMessage = e.getMessage();
			processingStatus = new HL7WSResponse(responseCode, responseMessage);
			LOGGER.error("processingStatus : " + processingStatus);
			throw e;
		}
		LOGGER.debug("Exiting from execute");
		LOGGER.info("=============================================================================================================================================");
		return processingStatus;
	}

	private boolean isValidHL7Information(Map<String, Object> hl7InfoMap) throws Exception
	{
		LOGGER.debug("Inside isValidHL7Information");
		boolean isValid = false;
		if (hl7InfoMap != null && !hl7InfoMap.isEmpty())
		{
			Object tenantCode = hl7InfoMap.get(HL7Info.TENANT_CODE);
			if (tenantCode != null && !tenantCode.toString().isEmpty())
			{
				Object hl7Action = hl7InfoMap.get(HL7Info.ACTION);
				if (hl7Action != null && !hl7Action.toString().isEmpty())
				{
					Object encounter = hl7InfoMap.get(HL7Info.ENCOUNTER);
					List<Object> encounterList = (List<Object>) hl7InfoMap.get(HL7Info.ENCOUNTER_LIST);
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
					} else if(encounterList != null)
					{
						for (Object encounter_ : encounterList)
						{
							@SuppressWarnings("unchecked")
							Map<String, Object> encounterMap = (Map<String, Object>) encounter_;
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
							}
							else
							{
								throw new Exception("MRN can not be a NULL or EMPTY. mrn : " + mrn);
							}
						}
						
					}else
					{
						throw new Exception("Encounter or EncounterList can not be a NULL or EMPTY. encounter : " + encounter);
					}
				} else
				{
					throw new Exception("HL7 action can not be a NULL or EMPTY. hl7Action : " + hl7Action);
				}
			} else
			{
				throw new Exception("Tenant code can not be a NULL or EMPTY. tenantCode : " + tenantCode);
			}
		} else
		{
			throw new Exception("HL7 information map can not be a NULL or EMPTY. hl7InfoMap : " + hl7InfoMap);
		}
		LOGGER.debug("Exiting from isValidHL7Information");
		return isValid;
	}
}