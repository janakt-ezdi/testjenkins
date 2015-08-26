package com.ezdi.cac.dao.hl7;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezdi.cac.bean.hl7.BeginEndMasterByUserBean;
import com.ezdi.cac.bean.hl7.CodeMapByUserBean;
import com.ezdi.cac.bean.hl7.CodeMasterBean;
import com.ezdi.cac.bean.hl7.CodeTypeBean;
import com.ezdi.cac.bean.hl7.CodeUpdateReasonBean;
import com.ezdi.cac.bean.hl7.DRGResponseValuesBean;
import com.ezdi.cac.bean.hl7.DischargeDispositionBean;
import com.ezdi.cac.bean.hl7.DocumentBean;
import com.ezdi.cac.bean.hl7.DocumentPhysicianMapBean;
import com.ezdi.cac.bean.hl7.DocumentTypeBean;
import com.ezdi.cac.bean.hl7.EditOptionsBean;
import com.ezdi.cac.bean.hl7.EncounterBean;
import com.ezdi.cac.bean.hl7.EncounterDischargeDispositionMapBean;
import com.ezdi.cac.bean.hl7.EncounterEventSummaryBean;
import com.ezdi.cac.bean.hl7.EncounterHistoryBean;
import com.ezdi.cac.bean.hl7.EncounterObservationRequestBean;
import com.ezdi.cac.bean.hl7.EncounterObservationResultBean;
import com.ezdi.cac.bean.hl7.EncounterPhysicianBean;
import com.ezdi.cac.bean.hl7.EncounterStatusBean;
import com.ezdi.cac.bean.hl7.EncounterWorkListMapBean;
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
import com.ezdi.cac.bean.hl7.TenantConfigBean;
import com.ezdi.cac.bean.hl7.UserLoginBean;
import com.ezdi.cac.bean.hl7.UserRoleBean;
import com.ezdi.cac.bean.hl7.WorkListBean;
import com.ezdi.cac.constant.CommonConstant;
import com.ezdi.cac.constant.EncounterStatusConstant;
import com.ezdi.cac.constant.UserRoleConstant;
import com.ezdi.cac.constant.WorkListConstants;
import com.ezdi.cac.search.listener.SearchEventInterceptor;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

@Component("hl7Dao")
public class HL7DaoImpl extends HL7BaseDao implements HL7Dao
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(HL7DaoImpl.class);

	@Autowired
	private SearchEventInterceptor searchInterceptor;

	@Override
	public PatientBean getPatient(String mrn) throws Exception
	{
		LOGGER.debug("Inside getPatient");
		PatientBean patientBean = null;
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(PatientBean.class, "patient");
		criteria.add(Restrictions.eq("patient.mrn", mrn));
		Object result = criteria.uniqueResult();
		if (result != null)
		{
			patientBean = (PatientBean) criteria.uniqueResult();
		}
		LOGGER.debug("Exiting from getPatient");
		return patientBean;
	}

	@Override
	public PatientBean insertPatient(PatientBean patientBean) throws Exception
	{
		LOGGER.debug("Inside insertPatient");
		Session session = getCurrentSession();
		session.save(patientBean);
		LOGGER.debug("Exiting from insertPatient");
		return patientBean;
	}

	@Override
	public PatientBean updatePatient(PatientBean patientBean) throws Exception
	{
		LOGGER.debug("Inside updatePatient");
		Session session = getCurrentSession();
		session.update(patientBean);
		LOGGER.debug("Exiting from updatePatient");
		return patientBean;
	}

	@Override
	public EncounterBean getEncounter(String mrn, String encounterNumber) throws Exception
	{
		LOGGER.debug("Inside getEncounterBean");
		EncounterBean encounterBean = null;
		Session session = getCurrentSession();
		Criteria encounterCriteria = session.createCriteria(EncounterBean.class, "encounter");
		encounterCriteria.add(Restrictions.eq("encounter.patientMrn", mrn));
		encounterCriteria.add(Restrictions.eq("encounter.encounterNumber", encounterNumber));
		Object result = encounterCriteria.uniqueResult();
		if (result != null)
		{
			encounterBean = (EncounterBean) result;
		}
		LOGGER.debug("Exiting from getEncounterBean");
		return encounterBean;
	}

	@Override
	public EncounterBean insertEncounter(EncounterBean encounterBean) throws Exception
	{
		LOGGER.debug("Inside insertEncounter");
		Session session = getCurrentSession();
		session.save(encounterBean);
		LOGGER.debug("Exiting from insertEncounter");
		return encounterBean;
	}

	@Override
	public EncounterBean updateEncounter(EncounterBean encounterBean) throws Exception
	{
		LOGGER.debug("Inside updateEncounter");
		Session session = getCurrentSession();
		session.update(encounterBean);
		LOGGER.debug("Exiting from updateEncounter");
		return encounterBean;
	}

	@Override
	public EncounterHistoryBean insertEncounterHistory(EncounterHistoryBean bean) throws Exception
	{
		LOGGER.debug("Inside insertEncounterHistory");
		Session session = getCurrentSession();
		session.saveOrUpdate(bean);
		LOGGER.debug("Exiting from insertEncounterHistory");
		return bean;
	}

	@Override
	public void insertEncounterEventSummaryBean(EncounterEventSummaryBean encounterEventSummaryBean) throws Exception
	{
		LOGGER.debug("Inside insertEncounterEventSummaryBean.");
		getCurrentSession().save(encounterEventSummaryBean);
		LOGGER.debug("Exiting from insertEncounterEventSummaryBean.");
	}

	@Override
	public int getPhysicianLoginId(String globalId) throws Exception
	{
		LOGGER.debug("Inside getPhysicianLoginId");
		int physicianLoginId = 0;
		Session session = getCurrentSession();
		Criteria userLoginCriteria = session.createCriteria(UserLoginBean.class, "userLogin");
		userLoginCriteria.add(Restrictions.eq("userLogin.globalId", globalId));
		Object result = userLoginCriteria.uniqueResult();
		if (result != null)
		{
			UserLoginBean userLoginBean = (UserLoginBean) result;
			physicianLoginId = userLoginBean.getId();
		}
		LOGGER.debug("Exiting from getPhysicianLoginId");
		return physicianLoginId;
	}

	@Override
	public Integer getUserLoginId(String userCode) throws Exception
	{
		LOGGER.debug("Inside getUserLoginId");
		Integer userLoginId = null;
		Session session = getCurrentSession();
		Criteria userLoginCriteria = session.createCriteria(UserLoginBean.class, "userLogin");
		userLoginCriteria.add(Restrictions.eq("userLogin.userCode", userCode));
		Object result = userLoginCriteria.uniqueResult();
		if (result != null)
		{
			userLoginId = ((UserLoginBean) result).getId();
		}
		LOGGER.debug("Exiting from getUserLoginId");
		return userLoginId;
	}

	@SuppressWarnings("unchecked")
	public List<EncounterPhysicianBean> getEncounterPhysicianBeanList(int encounterId) throws Exception
	{
		LOGGER.debug("Inside getEncounterPhysicianBeanList.");
		List<EncounterPhysicianBean> encounterPhysicianBeanList = null;
		Session session = getCurrentSession();
		Criteria encounterPhysicianCriteria = session.createCriteria(EncounterPhysicianBean.class, "encounterPhysician");
		encounterPhysicianCriteria.add(Restrictions.eq("encounterPhysician.encounterId", encounterId));
		@SuppressWarnings("rawtypes")
		List resultList = encounterPhysicianCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			encounterPhysicianBeanList = (List<EncounterPhysicianBean>) resultList;
		}
		LOGGER.info("Exiting from getEncounterPhysicianBeanList.");
		return encounterPhysicianBeanList;
	}

	@Override
	public EncounterPhysicianBean insertEncounterPhysician(EncounterPhysicianBean encounterPhysicianBean) throws Exception
	{
		LOGGER.debug("Inside insertEncounterPhysician");
		Session session = getCurrentSession();
		session.save(encounterPhysicianBean);
		LOGGER.debug("Exiting from insertEncounterPhysician");
		return encounterPhysicianBean;
	}

	@Override
	public EncounterPhysicianBean updateEncounterPhysician(EncounterPhysicianBean encounterPhysicianBean) throws Exception
	{
		LOGGER.debug("Inside updateEncounterPhysician");
		Session session = getCurrentSession();
		session.update(encounterPhysicianBean);
		LOGGER.debug("Exiting from updateEncounterPhysician");
		return encounterPhysicianBean;
	}

	@Override
	public DocumentBean getDocument(int encounterId, String name) throws Exception
	{
		LOGGER.debug("Inside getDocument");
		DocumentBean documentBean = null;
		Session session = getCurrentSession();
		Criteria documentCriteria = session.createCriteria(DocumentBean.class, "document");
		documentCriteria.add(Restrictions.eq("encounterId", encounterId));
		documentCriteria.add(Restrictions.eq("name", name));
		Object result = documentCriteria.uniqueResult();
		if (result != null)
		{
			documentBean = (DocumentBean) result;
		}
		LOGGER.debug("Exiting from getDocument");
		return documentBean;
	}

	@Override
	public DocumentBean insertDocument(DocumentBean documentBean) throws Exception
	{
		LOGGER.debug("Inside insertDocument");
		Session session = getCurrentSession();
		session.save(documentBean);
		LOGGER.debug("Exiting from insertDocument");
		return documentBean;

	}

	@Override
	public DocumentPhysicianMapBean insertDocumentPhysician(DocumentPhysicianMapBean documentPhysicianMapBean) throws Exception
	{
		LOGGER.debug("Inside insertDocumentPhysician");
		Session session = getCurrentSession();
		session.save(documentPhysicianMapBean);
		LOGGER.debug("Exiting from insertDocumentPhysician");
		return documentPhysicianMapBean;
	}

	@Override
	public EncounterObservationRequestBean insertEncounterObservationRequest(EncounterObservationRequestBean encounterObservationRequestBean)
			throws Exception
	{
		LOGGER.debug("Inside insertEncounterObservationRequest");
		Session session = getCurrentSession();
		session.save(encounterObservationRequestBean);
		LOGGER.debug("Exiting from insertEncounterObservationRequest");
		return encounterObservationRequestBean;
	}

	@Override
	public Integer getDocumentId(String placerOrderNumber, Integer encounterId)
	{
		LOGGER.debug("Inside getDocumentId");
		Integer documentId = null;
		try
		{
			Session session = getCurrentSession();
			Criteria encounterObservationRequestCriteria = session.createCriteria(EncounterObservationRequestBean.class,
					"encounterObservationRequest");
			encounterObservationRequestCriteria.add(Restrictions.eq("encounterObservationRequest.placerOrderNumber", placerOrderNumber));
			encounterObservationRequestCriteria.add(Restrictions.eq("encounterObservationRequest.encounterId", encounterId));

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("encounterObservationRequest.documentId"), "documentId");
			encounterObservationRequestCriteria.setProjection(projectionList);

			encounterObservationRequestCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Object result = encounterObservationRequestCriteria.uniqueResult();
			if (result != null)
			{
				documentId = (Integer) result;
			}
		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
		LOGGER.debug("Exiting from getDocumentId.");
		return documentId;
	}

	@Override
	public EncounterObservationResultBean insertEncounterObservationResult(EncounterObservationResultBean encounterObservationResultBean)
			throws Exception
	{
		LOGGER.debug("Inside insertEncounterObservationResult");
		Session session = getCurrentSession();
		session.save(encounterObservationResultBean);
		LOGGER.debug("Exiting from insertEncounterObservationResult");
		return encounterObservationResultBean;
	}

	@SuppressWarnings("unchecked")
	public List<CodeMapByUserBean> getCodeList(int encounterId, int userId) throws Exception
	{
		LOGGER.debug("Inside getCodeList.");
		List<CodeMapByUserBean> codeMapByUserBeanList = null;
		Session session = getCurrentSession();
		Criteria codeMapByUserCriteria = session.createCriteria(CodeMapByUserBean.class, "codeMapByUser");
		codeMapByUserCriteria.add(Restrictions.eq("codeMapByUser.encounterId", encounterId));
		codeMapByUserCriteria.add(Restrictions.eq("codeMapByUser.creatorUserId", userId));
		@SuppressWarnings("rawtypes")
		List resultList = codeMapByUserCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			codeMapByUserBeanList = (List<CodeMapByUserBean>) resultList;
		}
		LOGGER.info("Exiting from getCodeList.");
		return codeMapByUserBeanList;
	}

	public int getDocumentCodingSystemCount(int documentId, String codingSystem, int userId) throws Exception
	{
		LOGGER.debug("Inside getDocumentCodingSystemCount");
		int documentCodingSystemCount = 0;
		Session session = getCurrentSession();
		Criteria codeMapByUserCriteria = session.createCriteria(CodeMapByUserBean.class, "codeMapByUser");
		codeMapByUserCriteria.add(Restrictions.eq("codeMapByUser.documentId", documentId));
		codeMapByUserCriteria.add(Restrictions.eq("codeMapByUser.codingSystem", codingSystem));
		codeMapByUserCriteria.add(Restrictions.eq("codeMapByUser.creatorUserId", userId));
		@SuppressWarnings("rawtypes")
		List list = codeMapByUserCriteria.list();
		if (list != null && list.size() > 0)
		{
			documentCodingSystemCount = list.size();
		}
		LOGGER.debug("Exiting from getDocumentCodingSystemCount");
		return documentCodingSystemCount;
	}

	@Override
	public int getCodeTypeId(String codeType) throws Exception
	{
		LOGGER.debug("Inside getCodeTypeId");
		int codeTypeId = 0;
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(CodeTypeBean.class);
		criteria.add(Restrictions.eq("code", codeType));
		Object result = criteria.uniqueResult();
		if (result != null)
		{
			CodeTypeBean codeTypeBean = (CodeTypeBean) result;
			codeTypeId = codeTypeBean.getId();
		}
		LOGGER.debug("Exiting from getCodeTypeId");
		return codeTypeId;
	}

	@Override
	public MasterMapBean insertMasterId(MasterMapBean masterMapBean) throws Exception
	{
		LOGGER.debug("Inside insertMasterId");
		Session session = getCurrentSession();
		session.save(masterMapBean);
		LOGGER.debug("Exiting from insertMasterId");
		return masterMapBean;
	}

	@Override
	public CodeMasterBean getCodeMasterBean(String code, String codeType) throws Exception
	{
		LOGGER.debug("Inside getCodeMasterBean");
		CodeMasterBean codeMasterBean = null;
		Session session = getCurrentSession();
		Criteria codeMasterCriteria = session.createCriteria(CodeMasterBean.class, "codeMaster");
		codeMasterCriteria.add(Restrictions.eq("codeMaster.code", code));
		codeMasterCriteria.add(Restrictions.eq("codeMaster.codeType", codeType));

		codeMasterCriteria.setResultTransformer(Criteria.ROOT_ENTITY);
		Object result = codeMasterCriteria.uniqueResult();
		if (result != null)
		{
			codeMasterBean = (CodeMasterBean) result;
		}
		LOGGER.debug("Exiting from getCodeMasterBean");
		return codeMasterBean;
	}

	@Override
	public CodeUpdateReasonBean getCodeUpdateReasonBean(String reason) throws Exception
	{
		LOGGER.debug("Inside getCodeUpdateReasonBean");
		CodeUpdateReasonBean codeUpdateReasonBean = null;

		Session session = getCurrentSession();
		Criteria codeUpdateReasonCriteria = session.createCriteria(CodeUpdateReasonBean.class, "codeUpdateReason");
		codeUpdateReasonCriteria.add(Restrictions.eq("codeUpdateReason.reason", reason));

		Object result = codeUpdateReasonCriteria.uniqueResult();
		if (result != null)
		{
			codeUpdateReasonBean = (CodeUpdateReasonBean) result;
		}
		LOGGER.debug("Exiting from getCodeUpdateReasonBean");
		return codeUpdateReasonBean;
	}

	@Override
	public CodeMapByUserBean insertCode(CodeMapByUserBean codeMapByUserBean) throws Exception
	{
		LOGGER.debug("Inside insertCode");
		Session session = getCurrentSession();
		session.save(codeMapByUserBean);
		LOGGER.debug("Exiting from insertCode");
		return codeMapByUserBean;
	}

	@Override
	public CodeMapByUserBean updateCode(CodeMapByUserBean codeMapByUserBean) throws Exception
	{
		LOGGER.debug("Inside updateCode");
		Session session = getCurrentSession();
		session.update(codeMapByUserBean);
		LOGGER.debug("Exiting from updateCode");
		return codeMapByUserBean;
	}

	public RoleWiseEncounterStatusBean insertRoleWiseEncounterStatus(RoleWiseEncounterStatusBean roleWiseEncounterStatusBean) throws Exception
	{
		LOGGER.debug("Inside insertRoleWiseEncounterStatus");
		Session session = getCurrentSession();
		session.save(roleWiseEncounterStatusBean);
		LOGGER.debug("Exiting from insertRoleWiseEncounterStatus");
		return roleWiseEncounterStatusBean;
	}

	@Override
	public void updateRoleWiseEncounterStatus(RoleWiseEncounterStatusBean roleWiseEncounterStatusBean) throws Exception
	{
		LOGGER.debug("Inside updateRoleWiseEncounterStatus.");
		getCurrentSession().update(roleWiseEncounterStatusBean);
		LOGGER.debug("Exiting from updateRoleWiseEncounterStatus.");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<Integer, RoleWiseEncounterStatusBean> getRoleWiseEncounterStatus(Integer encounterId) throws Exception
	{
		LOGGER.debug("Inside getRoleWiseEncounterStatus.");
		Map<Integer, RoleWiseEncounterStatusBean> roleWiseEncounterStatusBeanMap = null;
		Session session = getCurrentSession();
		Criteria roleWiseEncounterStatusCriteria = session.createCriteria(RoleWiseEncounterStatusBean.class, "roleWiseEncounterStatus");
		roleWiseEncounterStatusCriteria.add(Restrictions.eq("roleWiseEncounterStatus.encounterId", encounterId));
		List list = roleWiseEncounterStatusCriteria.list();
		if (list != null && list.size() > 0)
		{
			roleWiseEncounterStatusBeanMap = new HashMap<Integer, RoleWiseEncounterStatusBean>();
			List<RoleWiseEncounterStatusBean> roleWiseEncounterStatusBeanList = (List<RoleWiseEncounterStatusBean>) list;
			for (RoleWiseEncounterStatusBean roleWiseEncounterStatusBean : roleWiseEncounterStatusBeanList)
			{
				roleWiseEncounterStatusBeanMap.put(roleWiseEncounterStatusBean.getUserRoleId(), roleWiseEncounterStatusBean);
			}
		}
		LOGGER.debug("Exiting from getRoleWiseEncounterStatus.");
		return roleWiseEncounterStatusBeanMap;
	}

	@Override
	public LocationBean getLocation(String location) throws Exception
	{
		LOGGER.debug("Inside getLocation");
		LocationBean locationBean = null;
		Session session = getCurrentSession();
		Criteria locationCriteria = session.createCriteria(LocationBean.class, "location");
		locationCriteria.add(Restrictions.eq("location.code", location));
		Object result = locationCriteria.uniqueResult();
		if (result != null)
		{
			locationBean = (LocationBean) result;
		}
		LOGGER.debug("Exiting from getLocation");
		return locationBean;
	}

	@Override
	public LocationBean insertLocation(LocationBean locationBean) throws Exception
	{
		LOGGER.debug("Inside insertLocation");
		Session session = getCurrentSession();
		session.save(locationBean);
		LOGGER.debug("Exiting from insertLocation");
		return locationBean;
	}

	@Override
	public PatientClassBean getPatientClass(String patientClass) throws Exception
	{
		LOGGER.debug("Inside getPatientClass");
		PatientClassBean patientClassBean = null;
		Session session = getCurrentSession();
		Criteria patientClassCriteria = session.createCriteria(PatientClassBean.class, "patientClass");
		patientClassCriteria.add(Restrictions.eq("patientClass.code", patientClass));
		Object result = patientClassCriteria.uniqueResult();
		if (result != null)
		{
			patientClassBean = (PatientClassBean) result;
		}
		LOGGER.debug("Exiting from getPatientClass");
		return patientClassBean;
	}

	@Override
	public PatientClassBean insertPatientClass(PatientClassBean patientClassBean) throws Exception
	{
		LOGGER.debug("Inside insertPatientClass");
		Session session = getCurrentSession();
		session.save(patientClassBean);
		LOGGER.debug("Exiting from insertPatientClass");
		return patientClassBean;
	}

	@Override
	public LocationPatientClassBean getLocationPatientClass(Integer locationId, Integer patientClassId) throws Exception
	{
		LOGGER.debug("Inside getLocationPatientClass");
		LocationPatientClassBean locationPatientClassBean = null;
		Session session = getCurrentSession();
		Criteria locationPatientClassCriteria = session.createCriteria(LocationPatientClassBean.class, "locationPatientClass");
		locationPatientClassCriteria.add(Restrictions.eq("locationPatientClass.locationId", locationId));
		locationPatientClassCriteria.add(Restrictions.eq("locationPatientClass.patientClassId", patientClassId));
		Object result = locationPatientClassCriteria.uniqueResult();
		if (result != null)
		{
			locationPatientClassBean = (LocationPatientClassBean) result;
		}
		LOGGER.debug("Exiting from getLocationPatientClass");
		return locationPatientClassBean;
	}

	@Override
	public LocationPatientClassBean insertLocationPatientClass(LocationPatientClassBean locationPatientClassBean) throws Exception
	{
		LOGGER.debug("Inside insertLocationPatientClass");
		Session session = getCurrentSession();
		session.save(locationPatientClassBean);
		LOGGER.debug("Exiting from insertLocationPatientClass");
		return locationPatientClassBean;
	}

	@Override
	public LocationPatientClassBean updateLocationPatientClass(LocationPatientClassBean locationPatientClassBean) throws Exception
	{
		LOGGER.debug("Inside updateLocationPatientClass");
		Session session = getCurrentSession();
		session.update(locationPatientClassBean);
		LOGGER.debug("Exiting from updateLocationPatientClass");
		return locationPatientClassBean;
	}

	@Override
	public LocationPatientClassTypeServiceLineBean getLocationPatientClassTypeServiceLine(Integer locationPatientClassId, Integer patientTypeId,
			Integer serviceLineId) throws Exception
	{
		LOGGER.debug("Inside getLocationPatientClassTypeServiceLine");
		LocationPatientClassTypeServiceLineBean locationPatientClassTypeServiceLineBean = null;
		Session session = getCurrentSession();
		Criteria locationPatientClassTypeServiceLineCriteria = session.createCriteria(LocationPatientClassTypeServiceLineBean.class,
				"locationPatientClassTypeServiceLine");
		locationPatientClassTypeServiceLineCriteria.add(Restrictions.eq("locationPatientClassTypeServiceLine.locationPatientClassId",
				locationPatientClassId));
		locationPatientClassTypeServiceLineCriteria.add(Restrictions.eq("locationPatientClassTypeServiceLine.patientTypeId", patientTypeId));
		locationPatientClassTypeServiceLineCriteria.add(Restrictions.eq("locationPatientClassTypeServiceLine.serviceLineId", serviceLineId));
		Object result = locationPatientClassTypeServiceLineCriteria.uniqueResult();
		if (result != null)
		{
			locationPatientClassTypeServiceLineBean = (LocationPatientClassTypeServiceLineBean) result;
		}
		LOGGER.debug("Exiting from getLocationPatientClassTypeServiceLine");
		return locationPatientClassTypeServiceLineBean;
	}

	@Override
	public LocationPatientClassTypeServiceLineBean insertLocationPatientClassTypeServiceLine(
			LocationPatientClassTypeServiceLineBean locationPatientClassTypeServiceLineBean) throws Exception
	{
		LOGGER.debug("Inside insertLocationPatientClass");
		Session session = getCurrentSession();
		session.save(locationPatientClassTypeServiceLineBean);
		LOGGER.debug("Exiting from insertLocationPatientClass");
		return locationPatientClassTypeServiceLineBean;
	}

	@Override
	public LocationPatientClassTypeServiceLineBean updateLocationPatientClassTypeServiceLine(
			LocationPatientClassTypeServiceLineBean locationPatientClassTypeServiceLineBean) throws Exception
	{
		LOGGER.debug("Inside updateLocationPatientClassTypeServiceLine");
		Session session = getCurrentSession();
		session.update(locationPatientClassTypeServiceLineBean);
		LOGGER.debug("Exiting from updateLocationPatientClassTypeServiceLine");
		return locationPatientClassTypeServiceLineBean;
	}

	@Override
	public PatientTypeBean getPatientType(String patientType) throws Exception
	{
		LOGGER.debug("Inside getPatientType");
		PatientTypeBean patinetTypeBean = null;
		Session session = getCurrentSession();
		Criteria patientTypeCriteria = session.createCriteria(PatientTypeBean.class, "patientType");
		patientTypeCriteria.add(Restrictions.eq("patientType.code", patientType));
		Object result = patientTypeCriteria.uniqueResult();
		if (result != null)
		{
			patinetTypeBean = (PatientTypeBean) result;
		}
		LOGGER.debug("Exiting from getPatientType");
		return patinetTypeBean;
	}

	@Override
	public PatientTypeBean insertPatientType(PatientTypeBean patientTypeBean) throws Exception
	{
		LOGGER.debug("Inside insertPatientType");
		Session session = getCurrentSession();
		session.save(patientTypeBean);
		LOGGER.debug("Exiting from insertPatientType");
		return patientTypeBean;
	}

	@Override
	public ServiceLineBean getServiceLine(String serviceLine) throws Exception
	{
		LOGGER.debug("Inside getServiceLine");
		ServiceLineBean serviceLineBean = null;
		Session session = getCurrentSession();
		Criteria serviceLineCriteria = session.createCriteria(ServiceLineBean.class, "serviceLine");
		serviceLineCriteria.add(Restrictions.eq("serviceLine.code", serviceLine));
		Object result = serviceLineCriteria.uniqueResult();
		if (result != null)
		{
			serviceLineBean = (ServiceLineBean) result;
		}
		LOGGER.debug("Exiting from getServiceLine");
		return serviceLineBean;
	}

	@Override
	public ServiceLineBean insertServiceLine(ServiceLineBean serviceLineBean) throws Exception
	{
		LOGGER.debug("Inside insertServiceLine");
		Session session = getCurrentSession();
		session.save(serviceLineBean);
		LOGGER.debug("Exiting from insertServiceLine");
		return serviceLineBean;
	}

	@Override
	public DocumentTypeBean getDocumentType(String documentType) throws Exception
	{
		LOGGER.debug("Inside getDocumentType");
		DocumentTypeBean documentTypeBean = null;
		Session session = getCurrentSession();

		Criteria documentTypeCriteria = session.createCriteria(DocumentTypeBean.class, "documentType");
		documentTypeCriteria.add(Restrictions.eq("documentType.name", documentType));

		Object result = documentTypeCriteria.uniqueResult();
		if (result != null)
		{
			documentTypeBean = (DocumentTypeBean) result;
		}
		LOGGER.debug("Exiting from getDocumentType");
		return documentTypeBean;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DocumentTypeBean> getDocumentTypeList() throws Exception
	{
		LOGGER.debug("Inside getDocumentTypeList");
		List<DocumentTypeBean> documentTypeBeanList = null;

		Session session = getCurrentSession();
		Criteria documentTypeCriteria = session.createCriteria(DocumentTypeBean.class, "documentType");
		documentTypeCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = documentTypeCriteria.list();
		if (resultList != null)
		{
			documentTypeBeanList = (List<DocumentTypeBean>) resultList;
		}
		LOGGER.debug("Exiting from getDocumentTypeList");
		return documentTypeBeanList;
	}

	@Override
	public DocumentTypeBean insertDocumentType(DocumentTypeBean documentTypeBean) throws Exception
	{
		LOGGER.debug("Inside insertDocumentType");
		Session session = getCurrentSession();
		session.save(documentTypeBean);
		LOGGER.debug("Exiting from insertDocumentType");
		return documentTypeBean;
	}

	@Override
	public int getDocumentTypeMaxSequenceNumber(Integer userRoleId, Integer patientClassId, Integer serviceLineId) throws Exception
	{
		int maxSequenceNumber = 0;
		LOGGER.debug("Inside getDocumentTypeMaxSequenceNumber");
		Session session = getCurrentSession();

		Criteria serviceLineUserRoleDocumentTypeCriteria = session.createCriteria(ServiceLineUserRoleDocumentTypeBean.class,
				"serviceLineUserRoleDocumentType");
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.userRoleId", userRoleId));
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.patientClassId", patientClassId));
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.serviceLineId", serviceLineId));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.max("serviceLineUserRoleDocumentType.sequenceNumber"));
		serviceLineUserRoleDocumentTypeCriteria.setProjection(projectionList);

		Object result = serviceLineUserRoleDocumentTypeCriteria.uniqueResult();
		if (result != null)
		{
			maxSequenceNumber = (Integer) result;
		}
		LOGGER.debug("Exiting from getDocumentTypeMaxSequenceNumber");
		return maxSequenceNumber;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Integer> getEncounterDocumentTypeSet(Integer encounterId) throws Exception
	{
		LOGGER.debug("Inside getEncounterDocumentTypeSet.");
		List<Integer> documentTypeList = null;
		Session session = getCurrentSession();
		Criteria documentCriteria = session.createCriteria(DocumentBean.class, "document");
		documentCriteria.add(Restrictions.eq("document.encounterId", encounterId));
		documentCriteria.add(Restrictions.eq("document.activeRecordFlag", CommonConstant.ACTIVE_DOCUMENT));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("document.typeId"), "typeId");
		documentCriteria.setProjection(projectionList);
		documentCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List list = documentCriteria.list();
		if (list != null && list.size() > 0)
		{
			documentTypeList = (List<Integer>) list;
		}
		LOGGER.debug("Exiting from getEncounterDocumentTypeSet.");
		return documentTypeList;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<Integer, Set<Integer>> getUserRoleRequiredDocumentTypeSet(Integer patientClassId, Integer serviceLineId) throws Exception
	{
		LOGGER.debug("Inside getUserRoleRequiredDocumentTypeSet.");
		Map<Integer, Set<Integer>> userRoleIdDocumentTypeIdSetMap = null;
		Session session = getCurrentSession();
		Criteria serviceLineUserRoleDocumentTypeCriteria = session.createCriteria(ServiceLineUserRoleDocumentTypeBean.class,
				"serviceLineUserRoleDocumentType");
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.patientClassId", patientClassId));
		if (serviceLineId != null)
		{
			serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.serviceLineId", serviceLineId));
		}
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.isRequiredDocumentType",
				CommonConstant.REQUIRED_DOCUMENT_TYPE_FLAG));
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.isActive",
				CommonConstant.ACTIVE_SERVICE_LINE_USER_ROLE_DOCUMENT_TYPE));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("serviceLineUserRoleDocumentType.userRoleId"), "userRoleId");
		projectionList.add(Projections.property("serviceLineUserRoleDocumentType.documentTypeId"), "documentTypeId");

		serviceLineUserRoleDocumentTypeCriteria.setProjection(Projections.distinct(projectionList));
		serviceLineUserRoleDocumentTypeCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List list = serviceLineUserRoleDocumentTypeCriteria.list();
		if (list != null && list.size() > 0)
		{
			userRoleIdDocumentTypeIdSetMap = new HashMap<Integer, Set<Integer>>();
			List<Map<String, Object>> recordList = (List<Map<String, Object>>) list;
			for (Map<String, Object> record : recordList)
			{
				Integer userRoleId = (Integer) record.get("userRoleId");
				Integer documentTypeId = (Integer) record.get("documentTypeId");
				if (userRoleIdDocumentTypeIdSetMap.containsKey(userRoleId))
				{
					userRoleIdDocumentTypeIdSetMap.get(userRoleId).add(documentTypeId);
				} else
				{
					Set<Integer> documentTypeIdSet = new HashSet<Integer>();
					documentTypeIdSet.add(documentTypeId);
					userRoleIdDocumentTypeIdSetMap.put(userRoleId, documentTypeIdSet);
				}
			}
		}
		LOGGER.debug("Exiting from getUserRoleRequiredDocumentTypeSet.");
		return userRoleIdDocumentTypeIdSetMap;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserRoleBean> getActiveUserRole() throws Exception
	{
		LOGGER.debug("Inside getActiveUserRole");
		List<UserRoleBean> userRoleBeanList = null;
		Session session = getCurrentSession();
		Criteria userRoleBeanCriteria = session.createCriteria(UserRoleBean.class, "userRole");
		userRoleBeanCriteria.add(Restrictions.eq("userRole.activeRecord", CommonConstant.ACTIVE_USER_ROLE));
		userRoleBeanCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = userRoleBeanCriteria.list();
		if (resultList != null)
		{
			userRoleBeanList = (List<UserRoleBean>) resultList;
		}
		LOGGER.debug("Exiting from getActiveUserRole");
		return userRoleBeanList;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PatientClassBean> getPatientClassList() throws Exception
	{
		LOGGER.debug("Inside getPatientClassList");
		List<PatientClassBean> patientClassBeanList = null;

		Session session = getCurrentSession();
		Criteria patientClassCriteria = session.createCriteria(PatientClassBean.class, "patientClass");
		patientClassCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = patientClassCriteria.list();
		if (resultList != null)
		{
			patientClassBeanList = (List<PatientClassBean>) resultList;
		}
		LOGGER.debug("Exiting from getPatientClassList");
		return patientClassBeanList;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ServiceLineBean> getServiceLineBeanList() throws Exception
	{
		LOGGER.debug("Inside getServiceLineBeanList");
		List<ServiceLineBean> serviceLineBeanList = null;
		Session session = getCurrentSession();
		Criteria serviceLineCriteria = session.createCriteria(ServiceLineBean.class, "serviceLine");
		serviceLineCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = serviceLineCriteria.list();
		if (resultList != null)
		{
			serviceLineBeanList = (List<ServiceLineBean>) resultList;
		}
		LOGGER.debug("Exiting from getServiceLineBeanList");
		return serviceLineBeanList;
	}

	@Override
	public ServiceLineUserRoleDocumentTypeBean getServiceLineUserRoleDocumentType(Integer documentTypeId, Integer userRoleId, Integer patientClassId,
			Integer serviceLineId) throws Exception
	{
		LOGGER.debug("Inside getServiceLineUserRoleDocumentType");
		ServiceLineUserRoleDocumentTypeBean serviceLineUserRoleDocumentTypeBean = null;
		Session session = getCurrentSession();
		Criteria serviceLineUserRoleDocumentTypeCriteria = session.createCriteria(ServiceLineUserRoleDocumentTypeBean.class,
				"serviceLineUserRoleDocumentType");
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.documentTypeId", documentTypeId));
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.userRoleId", userRoleId));
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.patientClassId", patientClassId));
		serviceLineUserRoleDocumentTypeCriteria.add(Restrictions.eq("serviceLineUserRoleDocumentType.serviceLineId", serviceLineId));
		serviceLineUserRoleDocumentTypeCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Object result = serviceLineUserRoleDocumentTypeCriteria.uniqueResult();
		if (result != null)
		{
			serviceLineUserRoleDocumentTypeBean = (ServiceLineUserRoleDocumentTypeBean) result;
		}
		LOGGER.debug("Exiting from getServiceLineUserRoleDocumentType");
		return serviceLineUserRoleDocumentTypeBean;
	}

	@Override
	public ServiceLineUserRoleDocumentTypeBean insertServiceLineUserRoleDocumentType(
			ServiceLineUserRoleDocumentTypeBean serviceLineUserRoleDocumentTypeBean) throws Exception
	{
		LOGGER.debug("Inside insertServiceLineUserRoleDocumentType");
		Session session = getCurrentSession();
		session.save(serviceLineUserRoleDocumentTypeBean);
		LOGGER.debug("Exiting from insertServiceLineUserRoleDocumentType");
		return serviceLineUserRoleDocumentTypeBean;
	}

	@Override
	public DischargeDispositionBean getDischargeDisposition(String dischargeDisposition) throws Exception
	{
		LOGGER.debug("Inside getDischargeDisposition");
		DischargeDispositionBean dischargeDispositionBean = null;
		Session session = getCurrentSession();
		Criteria dischargeDispositionCriteria = session.createCriteria(DischargeDispositionBean.class, "dischargeDisposition");
		dischargeDispositionCriteria.add(Restrictions.eq("dischargeDisposition.code", dischargeDisposition));
		Object result = dischargeDispositionCriteria.uniqueResult();
		if (result != null)
		{
			dischargeDispositionBean = (DischargeDispositionBean) result;
		}
		LOGGER.debug("Exiting from getDischargeDisposition");
		return dischargeDispositionBean;
	}

	@Override
	public PayerBean getPayer(String code) throws Exception
	{
		LOGGER.debug("Inside getPayer");
		PayerBean payerBean = null;
		Session session = getCurrentSession();
		Criteria payerCriteria = session.createCriteria(PayerBean.class, "payerBean");
		payerCriteria.add(Restrictions.eq("payerBean.code", code));
		Object result = payerCriteria.uniqueResult();
		if (result != null)
		{
			payerBean = (PayerBean) result;
		}
		LOGGER.debug("Exiting from getPayer");
		return payerBean;
	}

	@Override
	public PayerBean insertPayer(PayerBean payerBean) throws Exception
	{
		LOGGER.debug("Inside insertPayer");
		Session session = getCurrentSession();
		session.save(payerBean);
		LOGGER.debug("Exiting from insertPayer");
		return payerBean;
	}

	public UserRoleBean getUserRole(String userRole) throws Exception
	{
		LOGGER.debug("Inside getUserRole");
		UserRoleBean userRoleBean = null;
		Session session = getCurrentSession();
		Criteria userRoleCriteria = session.createCriteria(UserRoleBean.class, "userRole");
		userRoleCriteria.add(Restrictions.eq("userRole.code", userRole));
		userRoleCriteria.setResultTransformer(Criteria.ROOT_ENTITY);
		Object result = userRoleCriteria.uniqueResult();
		if (result != null)
		{
			userRoleBean = (UserRoleBean) result;
		}
		LOGGER.debug("Exiting from getUserRole");
		return userRoleBean;
	}

	public boolean isEncounterBilled(int encounterId) throws Exception
	{
		LOGGER.debug("Inside isEncounterBilled");
		boolean check = false;
		Session session = getCurrentSession();
		Criteria fileAllocationCriteria = session.createCriteria(FileAllocationBean.class, "fileAllocation");
		fileAllocationCriteria.add(Restrictions.eq("fileAllocation.encounterId", encounterId));
		fileAllocationCriteria.add(Restrictions.or(Restrictions.eq("fileAllocation.coderEncounterStatus", EncounterStatusConstant.BILLED),
				Restrictions.eq("fileAllocation.reviewerEncounterStatus", EncounterStatusConstant.BILLED)));
		if (fileAllocationCriteria.uniqueResult() != null)
		{
			check = true;
		}
		LOGGER.debug("Exiting from isEncounterBilled");
		return check;
	}

	public boolean isEncounterInQueueORUnassign(int encounterId) throws Exception
	{
		LOGGER.debug("Inside isEncounterInQueueORUnassign");
		boolean check = true;
		Session session = getCurrentSession();
		Criteria fileAllocationCriteria = session.createCriteria(FileAllocationBean.class, "fileAllocation");
		fileAllocationCriteria.add(Restrictions.eq("encounterId", encounterId));
		Disjunction encounterStatusDisjunction = Restrictions.disjunction();
		encounterStatusDisjunction.add(Restrictions.ne("fileAllocation.coderEncounterStatus", EncounterStatusConstant.IN_QUEUE));
		encounterStatusDisjunction.add(Restrictions.ne("fileAllocation.coderEncounterStatus", EncounterStatusConstant.UN_ASSIGNED));
		encounterStatusDisjunction.add(Restrictions.ne("fileAllocation.reviewerEncounterStatus", EncounterStatusConstant.IN_QUEUE));
		encounterStatusDisjunction.add(Restrictions.ne("fileAllocation.reviewerEncounterStatus", EncounterStatusConstant.UN_ASSIGNED));
		encounterStatusDisjunction.add(Restrictions.ne("fileAllocation.cdiEncounterStatus", EncounterStatusConstant.IN_QUEUE));
		encounterStatusDisjunction.add(Restrictions.ne("fileAllocation.cdiEncounterStatus", EncounterStatusConstant.UN_ASSIGNED));
		fileAllocationCriteria.add(encounterStatusDisjunction);
		if (fileAllocationCriteria.uniqueResult() != null)
		{
			check = false;
		}
		LOGGER.debug("Exiting from isEncounterInQueueORUnassign");
		return check;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getTenantConfigMap() throws Exception
	{
		LOGGER.debug("Inside getTenantConfigMap");
		Map<String, String> tenantConfigMap = null;
		Session session = getCurrentSession();
		Criteria tenantConfigCriteria = session.createCriteria(TenantConfigBean.class, "tenantConfig");
		@SuppressWarnings("rawtypes")
		List resultList = tenantConfigCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			tenantConfigMap = new HashMap<String, String>();
			List<TenantConfigBean> recordList = (List<TenantConfigBean>) resultList;
			for (TenantConfigBean tenantConfigBean : recordList)
			{
				tenantConfigMap.put(tenantConfigBean.getName(), tenantConfigBean.getValue());
			}
		}
		LOGGER.debug("Exiting from getTenantConfigMap");
		return tenantConfigMap;
	}

	@Override
	public EncounterStatusBean getEncounterStatus(String status) throws Exception
	{
		LOGGER.debug("Inside getEncounterStatus");
		EncounterStatusBean encounterStatusBean = null;
		Session session = getCurrentSession();

		Criteria encounterStatusBeanCriteria = session.createCriteria(EncounterStatusBean.class, "EncounterStatusBean");
		encounterStatusBeanCriteria.add(Restrictions.eq("EncounterStatusBean.encounterStatusCode", status));

		Object result = encounterStatusBeanCriteria.uniqueResult();
		if (result != null)
		{
			encounterStatusBean = (EncounterStatusBean) result;
		}
		LOGGER.debug("Exiting from getEncounterStatus");
		return encounterStatusBean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getTenantConfigMapWithNewSession() throws Exception
	{
		Session session = null;
		try
		{
			LOGGER.debug("Inside getTenantConfigMap");
			Map<String, String> tenantConfigMap = null;
			session = tenantSessionFactory.openSession();
			Criteria tenantConfigCriteria = session.createCriteria(TenantConfigBean.class, "tenantConfig");
			@SuppressWarnings("rawtypes")
			List resultList = tenantConfigCriteria.list();
			if (resultList != null && resultList.size() > 0)
			{
				tenantConfigMap = new HashMap<String, String>();
				List<TenantConfigBean> recordList = (List<TenantConfigBean>) resultList;
				for (TenantConfigBean tenantConfigBean : recordList)
				{
					tenantConfigMap.put(tenantConfigBean.getName(), tenantConfigBean.getValue());
				}
			}
			LOGGER.debug("Exiting from getTenantConfigMap");
			return tenantConfigMap;
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		} finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				} catch (Exception ex)
				{
					LOGGER.error(ex);
				}
			}
		}
	}

	public SearchEventInterceptor getSearchEventInterceptor()
	{
		return searchInterceptor;
	}

	public void setSearchEventInterceptor(SearchEventInterceptor searchEventInterceptor)
	{
		this.searchInterceptor = searchEventInterceptor;
	}

	@Override
	public Integer getUserRoleIdFromCode(String role) throws Exception
	{
		try
		{
			LOGGER.info("inside getUserRoleIdFromCode dao");
			final Session session = getCurrentSession();
			final Criteria criteria = session.createCriteria(UserRoleBean.class);
			criteria.setProjection(Projections.property("id"));
			criteria.add(Restrictions.eq("code", role));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			final Integer userRoleId = (Integer) criteria.uniqueResult();
			LOGGER.info("exit from getUserRoleIdFromCode dao");
			return userRoleId;
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}

	}

	@Override
	public boolean deleteIssues(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		try
		{
			LOGGER.info("inside deleteIssues(LandingPageDao) dao");
			final Session session = getCurrentSession();

			final String sqlQuery = "update QueryMaster set activerecord =:inActive where encounterid in (:encounterIds) and userRoleId =:roleId and activerecord =:isActive";

			final Query query = session.createSQLQuery(sqlQuery);
			query.setInteger("inActive", 0);
			query.setParameterList("encounterIds", encounterIdSet);
			query.setInteger("roleId", roleId);
			query.setInteger("isActive", 1);
			final int updateCount = query.executeUpdate();

			LOGGER.info("exit from deleteIssues(LandingPageDao) dao");
			return updateCount > 0;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	public List<Map> getBillingRecodedEncounters(Set<Integer> encounterIdSet) throws Exception
	{
		try
		{
			Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    max(\n"
					+ "\t\tif(c.coder_encounterstatuscode = 'Billed', c.codingdate , \n" + "             c.reviewer_dts)\n" + "             \n"
					+ "\t ) as date\n" + " FROM\n" + "    EncounterInformation e\n" + "        INNER JOIN\n"
					+ "    FileAllocation f ON f.encounterid = e.encounterIndexid\n" + "        INNER JOIN\n"
					+ "    CaseAllocationHistory c ON c.caseAllocationID = f.fileallocationid\n"
					+ "        AND ((c.coder_encounterstatuscode = 'Billed'\n" + "        AND f.coder_encounterstatuscode != 'Billed')\n"
					+ "        OR (c.reviewer_encounterstatuscode = 'Billed'\n" + "        AND f.reviewer_encounterstatuscode != 'Billed'))\n"
					+ "        AND e.isActive = 1\n" + "        AND e.encounterIndexid IN (:encounterIds)\n" + " GROUP BY f.encounterid ";

			return session.createSQLQuery(sql).setParameterList("encounterIds", encounterIdSet)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	public List<Map> getCompletedDateForReviewEncounters(Set<Integer> encounterIdSet) throws Exception
	{
		try
		{
			Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    f.codingdate as date\n" + " FROM\n"
					+ "    EncounterInformation e\n" + "        INNER JOIN\n" + "    FileAllocation f ON f.encounterid = e.encounterIndexid\n"
					+ "        AND \n" + "        f.coder_encounterstatuscode = 'Completed' and \n"
					+ "        f.reviewer_encounterstatuscode != null\n" + "        AND e.isActive = 1\n"
					+ "        AND e.encounterIndexid IN (:encounterIds)\n" + " GROUP BY f.encounterid ";

			return session.createSQLQuery(sql).setParameterList("encounterIds", encounterIdSet)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	public List<Map> getWorkingDateForCoderEncounters(Set<Integer> encounterIdSet) throws Exception
	{
		try
		{
			Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    f.codingdate as date\n" + "   \n" + "FROM\n"
					+ "    EncounterInformation e\n" + "        INNER JOIN\n" + "    FileAllocation f ON f.encounterid = e.encounterIndexid\n"
					+ "        AND \n" + "        (f.coder_encounterstatuscode = 'In Progress' or f.coder_encounterstatuscode = 'In Queue' \n"
					+ "        or f.coder_encounterstatuscode = 'On Hold' or f.coder_encounterstatuscode = 'Updated')\n" + "        \n"
					+ "        AND e.isActive = 1\n" + "        AND e.encounterIndexid IN (:encounterIds)\n" + "GROUP BY f.encounterid ";

			return session.createSQLQuery(sql).setParameterList("encounterIds", encounterIdSet)
					.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	@Override
	public boolean deleteDRGValues(Set<Integer> encounterIdSet, Integer roleId) throws Exception
	{
		try
		{
			LOGGER.info("inside deleteDRGValues(LandingPageDao) dao");
			final Session session = getCurrentSession();
			final String sqlQuery = "update DRGResponseValues set activerecord =:inActive where encounterIndexid in (:encounterIds) and creatorUserRoleId =:roleId and activerecord =:isActive";

			final Query query = session.createSQLQuery(sqlQuery);
			query.setInteger("inActive", 0);
			query.setParameterList("encounterIds", encounterIdSet);
			query.setInteger("roleId", roleId);
			query.setInteger("isActive", 1);
			final int updateCount = query.executeUpdate();

			LOGGER.info("exit from deleteDRGValues(LandingPageDao) dao");
			return updateCount > 0;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	@Override
	public List<Long> getCodeMapByUserIdList(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		try
		{
			LOGGER.info("inside getCodeMapByUserIdList(LandingPageDao) dao");
			final Session session = getCurrentSession();

			final Criteria criteria = session.createCriteria(CodeMapByUserBean.class);
			criteria.add(Restrictions.in("encounterId", encounterIdSet));
			criteria.add(Restrictions.eq("roleId", roleId));
			criteria.setProjection(Projections.distinct(Projections.property("codeMapByUserId")));
			final List<Long> codeMapByUserIdList = criteria.list();

			LOGGER.info("exit from getCodeMapByUserIdList(LandingPageDao) dao");
			return codeMapByUserIdList;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	@Override
	public boolean deleteCodeHistory(final List<Long> codeMapByUserIdList) throws Exception
	{
		LOGGER.info("inside deleteCodeHistory(LandingPageDao) dao");
		final Session session = getCurrentSession();
		final String hql = "DELETE FROM CodeMapTransactionBean " + "WHERE codeMapId in (:codeMapByUserIds) ";
		Query query = session.createQuery(hql);
		query.setParameterList("codeMapByUserIds", codeMapByUserIdList);
		final int deletedCount = query.executeUpdate();
		LOGGER.info("exiting from deleteCodeHistory(LandingPageDao) dao");
		return deletedCount > 0;

	}

	/*
	 * @Override public boolean deleteCodeHistory(final List<Long> codeMapByUserIdList) throws Exception { try {
	 * LOGGER.info("inside deleteCodeHistory(LandingPageDao) dao"); final Session session = getCurrentSession(); final String sqlQuery =
	 * "delete from CodeMapTransaction where codeMapId in (:codeMapByUserIds)"; final Query query = session.createSQLQuery(sqlQuery);
	 * query.setParameterList("codeMapByUserIds", codeMapByUserIdList); final int deletedCount = query.executeUpdate();
	 * LOGGER.info("exit from deleteCodeHistory(LandingPageDao) dao"); return deletedCount > 0; } catch (Exception exception) {
	 * LOGGER.error(exception); throw exception; } }
	 */

	@Override
	public boolean deleteEncoderEdits(final List<Long> codeMapByUserIdList) throws Exception
	{
		LOGGER.info("inside deleteEncoderEdits(LandingPageDao) dao");
		final Session session = getCurrentSession();
		final String hql = "DELETE FROM EncoderEditsBean " + "WHERE codeMapByUserID in (:codeMapByUserIds) ";
		Query query = session.createQuery(hql);
		query.setParameterList("codeMapByUserIds", codeMapByUserIdList);
		final int deletedCount = query.executeUpdate();
		LOGGER.info("exiting from deleteEncoderEdits(LandingPageDao) dao");
		return deletedCount > 0;

	}

	/*
	 * @Override public boolean deleteEncoderEdits(final List<Long> codeMapByUserIdList) throws Exception { try {
	 * LOGGER.info("inside deleteEncoderEdits(LandingPageDao) dao"); final Session session = getCurrentSession(); final String sqlQuery =
	 * "delete from EncoderEdits where  codeMapByUserID in (:codeMapByUserIds)"; final Query query = session.createSQLQuery(sqlQuery);
	 * query.setParameterList("codeMapByUserIds", codeMapByUserIdList); final int deletedCount = query.executeUpdate();
	 * LOGGER.info("exit from deleteEncoderEdits(LandingPageDao) dao"); return deletedCount > 0; } catch (Exception exception) {
	 * LOGGER.trace(exception); throw exception; } }
	 */

	@Override
	public boolean deleteCodes(final List<Long> codeMapByUserIdList) throws Exception
	{
		LOGGER.info("inside deleteCodes(LandingPageDao) dao");
		final Session session = getCurrentSession();
		final String hql = "DELETE FROM CodeMapByUserBean " + "WHERE ID in (:codeMapByUserIds) ";
		Query query = session.createQuery(hql);
		query.setParameterList("codeMapByUserIds", codeMapByUserIdList);
		final int deletedCount = query.executeUpdate();
		LOGGER.info("exiting from deleteCodes(LandingPageDao) dao");
		return deletedCount > 0;

	}

	/*
	 * @Override public boolean deleteCodes(final List<Long> codeMapByUserIdList) throws Exception { try {
	 * LOGGER.info("inside deleteCodes(LandingPageDao) dao"); final Session session = getCurrentSession();
	 * 
	 * final String sqlQuery = "delete from CodeMapByUser where  codeMapByUserId in (:codeMapByUserIds)"; final Query query =
	 * session.createSQLQuery(sqlQuery); query.setParameterList("codeMapByUserIds", codeMapByUserIdList); final int deletedCount =
	 * query.executeUpdate(); LOGGER.info("exit from deleteCodes(LandingPageDao) dao"); return deletedCount > 0; } catch (Exception
	 * exception) { LOGGER.trace(exception); throw exception; } }
	 */
	@Override
	public List<Integer> getEvidenceIdList(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		try
		{
			LOGGER.info("inside getEvidenceIdList(LandingPageDao) dao");
			final Session session = getCurrentSession();
			final Criteria criteria = session.createCriteria(BeginEndMasterByUserBean.class, "beginEndMap");
			criteria.createAlias("beginEndMap.beginEndDocumentInformationByUser", "document");
			criteria.add(Restrictions.in("document.encounterIndexId", encounterIdSet));
			criteria.add(Restrictions.eq("beginEndMap.roleId", roleId));
			criteria.setProjection(Projections.property("beginEndMap.beginEndUserId"));
			final List<Integer> beginEndIdList = criteria.list();
			LOGGER.info("exit from getEvidenceIdList(LandingPageDao) dao");
			return beginEndIdList;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	@Override
	public boolean deleteQueryEvidence(final List<Integer> evidenceIdList) throws Exception
	{
		try
		{
			LOGGER.info("inside deleteQueryEvidence(LandingPageDao) dao");
			final Session session = getCurrentSession();

			final String sqlQuery = "delete from QueryBeginEndMap where beginEndId in (:beginEndId)";
			final Query query = session.createSQLQuery(sqlQuery);
			query.setParameterList("beginEndId", evidenceIdList);
			final int updatedCount = query.executeUpdate();

			LOGGER.info("exit from deleteQueryEvidence(LandingPageDao) dao");
			return updatedCount > 0;
		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
	}

	@Override
	public boolean deleteEvidence(final List<Integer> evidenceIdList) throws Exception
	{
		try
		{
			LOGGER.info("inside deleteEvidence(LandingPageDao) dao");
			final Session session = getCurrentSession();

			final String sqlQuery = "delete from BeginEndMasterByUser where beginEndUserId in (:beginEndId)";
			final Query query = session.createSQLQuery(sqlQuery);
			query.setParameterList("beginEndId", evidenceIdList);
			final int updatedCount = query.executeUpdate();

			LOGGER.info("exit from deleteEvidence(LandingPageDao) dao");
			return updatedCount > 0;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	@Override
	public List<Integer> getUserRoleUserMapIdList(final Set<Integer> encounterIdSet, final Integer roleId) throws Exception
	{
		try
		{
			LOGGER.info("inside getUserRoleUserMapIdList(LandingPageDao) dao");
			final Session session = getCurrentSession();

			final Criteria criteria = session.createCriteria(EncounterDischargeDispositionMapBean.class, "encounterDDMap");
			criteria.createAlias("encounterDDMap.encounterDDMapUserRoleUserMapInformation", "userRoleUserMap");
			criteria.add(Restrictions.in("encounterDDMap.encounterDischargeDispositionMap.encounterIndexId", encounterIdSet));
			criteria.add(Restrictions.eq("userRoleUserMap.userRoleId", roleId));
			criteria.setProjection(Projections.property("encounterDDMap.encounterDischargeDispositionMap.userRoleUserId"));
			final List<Integer> userRoleUserIdList = criteria.list();

			LOGGER.info("exit from getUserRoleUserMapIdList(LandingPageDao) dao");
			return userRoleUserIdList;
		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
	}

	@Override
	public boolean deleteEncounterDDMapEntries(final Set<Integer> encounterIdSet, final List<Integer> encounterUserRoleMapIdList) throws Exception
	{
		try
		{
			LOGGER.info("inside deleteEncounterDDMapEntries(LandingPageDao) dao");
			final Session session = getCurrentSession();

			final String sqlQuery = "delete from EncounterDischargeDispositionMap where userRoleUserId in (:userRoleUserId) and encounterId in (:encounterId)";
			final Query query = session.createSQLQuery(sqlQuery);
			query.setParameterList("encounterId", encounterIdSet);
			query.setParameterList("userRoleUserId", encounterUserRoleMapIdList);
			final int updatedCount = query.executeUpdate();

			LOGGER.info("exit from deleteEncounterDDMapEntries(LandingPageDao) dao");
			return updatedCount > 0;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	@Override
	public boolean deleteGroupingSourcesEntries(final Set<Integer> encounterIdSet, final List<Integer> encounterUserRoleMapIdList) throws Exception
	{
		try
		{
			LOGGER.info("inside deleteGroupingSourcesEntries(LandingPageDao) dao");
			final Session session = getCurrentSession();
			final String sqlQuery = "delete from GroupingSources where userRoleUserId in (:userRoleUserId) and encounterID in (:encounterId)";
			final Query query = session.createSQLQuery(sqlQuery);
			query.setParameterList("encounterId", encounterIdSet);
			query.setParameterList("userRoleUserId", encounterUserRoleMapIdList);
			final int updatedCount = query.executeUpdate();
			LOGGER.info("exit from deleteGroupingSourcesEntries(LandingPageDao) dao");
			return updatedCount > 0;
		} catch (Exception exception)
		{
			LOGGER.error(exception);
			throw exception;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GroupingSourcesBean> getDefaultGroupingSource() throws Exception
	{
		LOGGER.debug("Inside getDefaultGroupingSource");
		List<GroupingSourcesBean> groupingSourcesBeanList = null;

		Session session = getCurrentSession();
		Criteria groupingSourcesBeanCriteria = session.createCriteria(GroupingSourcesBean.class, "GroupingSourcesBean");
		groupingSourcesBeanCriteria.add(Restrictions.eq("GroupingSourcesBean.activeRecord", 1));
		groupingSourcesBeanCriteria.add(Restrictions.eq("GroupingSourcesBean.isDefault", 1));
		groupingSourcesBeanCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = groupingSourcesBeanCriteria.list();
		if (resultList != null)
		{
			groupingSourcesBeanList = (List<GroupingSourcesBean>) resultList;
		}
		LOGGER.debug("Exiting from getDefaultGroupingSource");
		return groupingSourcesBeanList;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ResequenceOptionsBean> getDefaultReseqOptions() throws Exception
	{
		LOGGER.debug("Inside getDefaultReseqOptions");
		List<ResequenceOptionsBean> resequenceOptionsBeans = null;

		Session session = getCurrentSession();
		Criteria resequenceOptionsBeanCriteria = session.createCriteria(ResequenceOptionsBean.class, "ResequenceOptionsBean");
		resequenceOptionsBeanCriteria.add(Restrictions.eq("ResequenceOptionsBean.activeRecord", 1));
		resequenceOptionsBeanCriteria.add(Restrictions.eq("ResequenceOptionsBean.isDefault", 1));
		resequenceOptionsBeanCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = resequenceOptionsBeanCriteria.list();
		if (resultList != null)
		{
			resequenceOptionsBeans = (List<ResequenceOptionsBean>) resultList;
		}
		LOGGER.debug("Exiting from getDefaultReseqOptions");
		return resequenceOptionsBeans;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EditOptionsBean> getDefaultEditOptions() throws Exception
	{
		LOGGER.debug("Inside getDefaultReseqOptions");
		List<EditOptionsBean> editOptionsBeans = null;

		Session session = getCurrentSession();
		Criteria editOptionsBeanCriteria = session.createCriteria(EditOptionsBean.class, "EditOptionsBean");
		editOptionsBeanCriteria.add(Restrictions.eq("EditOptionsBean.activeRecord", 1));
		editOptionsBeanCriteria.add(Restrictions.eq("EditOptionsBean.isDefault", 1));
		editOptionsBeanCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = editOptionsBeanCriteria.list();
		if (resultList != null)
		{
			editOptionsBeans = (List<EditOptionsBean>) resultList;
		}
		LOGGER.debug("Exiting from getDefaultReseqOptions");
		return editOptionsBeans;
	}

	public boolean hasNewPatientClassAccess(int encounterId, String newPatientClass, Integer newPatientClassId, Integer locationId, Integer userId)
			throws Exception
	{
		boolean cnt = false;
		LOGGER.debug("Inside hasNewPatientClassAccess");
		Session session = getCurrentSession();

		Criteria locationPatientClassBeanCriteria = session.createCriteria(LocationPatientClassBean.class, "locationPatientClass");
		locationPatientClassBeanCriteria.createAlias("locationPatientClass.userGroupLocationPatientClassMapBeanList", "userGroupBeanList",
				JoinType.INNER_JOIN);
		locationPatientClassBeanCriteria.add(Restrictions.eq("locationPatientClass.locationId", locationId));
		locationPatientClassBeanCriteria.add(Restrictions.eq("locationPatientClass.patientClassId", newPatientClassId));
		locationPatientClassBeanCriteria.add(Restrictions.eq("userGroupBeanList.User_Login_Id", userId));

		List resultList = locationPatientClassBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			cnt = true;
		}
		LOGGER.debug("Exiting from hasNewPatientClassAccess.");

		return cnt;
	}

	public FileAllocationBean getFileAllocationBean(int encounterId) throws Exception
	{
		LOGGER.debug("Inside getFileAllocationBean");
		FileAllocationBean fileAllocationBean = null;
		Session session = getCurrentSession();
		Criteria fileAllocationCriteria = session.createCriteria(FileAllocationBean.class, "fileAllocation");
		fileAllocationCriteria.add(Restrictions.eq("fileAllocation.encounterId", encounterId));
		Object result = fileAllocationCriteria.uniqueResult();
		if (result != null)
		{
			fileAllocationBean = (FileAllocationBean) result;
		}
		LOGGER.debug("Exiting from getFileAllocationBean");
		return fileAllocationBean;
	}

	public FileAllocationBean updateFileAllocationBean(FileAllocationBean bean) throws Exception
	{
		LOGGER.debug("Inside updateFileAllocationBean");
		Session session = getCurrentSession();
		session.saveOrUpdate(bean);
		LOGGER.debug("Exiting from updateFileAllocationBean");
		return bean;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DRGResponseValuesBean> getDrgResponses(int encounterId) throws Exception
	{
		LOGGER.debug("Inside getDrgResponses");
		List<DRGResponseValuesBean> drgResponseValuesBeansList = null;

		Session session = getCurrentSession();
		Criteria dRGResponseValuesBeanCriteria = session.createCriteria(DRGResponseValuesBean.class, "DRGResponseValuesBean");
		dRGResponseValuesBeanCriteria.add(Restrictions.eq("DRGResponseValuesBean.encounterIndexId", encounterId));
		dRGResponseValuesBeanCriteria.add(Restrictions.eq("DRGResponseValuesBean.activeRecord", 1));
		dRGResponseValuesBeanCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List resultList = dRGResponseValuesBeanCriteria.list();
		if (resultList != null)
		{
			drgResponseValuesBeansList = (List<DRGResponseValuesBean>) resultList;
		}
		LOGGER.debug("Exiting from getDrgResponses");
		return drgResponseValuesBeansList;
	}

	public List<DRGResponseValuesBean> updateDrgResponses(List<DRGResponseValuesBean> beans) throws Exception
	{
		LOGGER.debug("Inside updateDrgResponses");
		Session session = getCurrentSession();

		for (DRGResponseValuesBean d : beans)
		{
			session.saveOrUpdate(d);
		}

		LOGGER.debug("Exiting from updateDrgResponses");
		return beans;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GroupingSourcesBean> getDefaultGroupingSource(String patientClassCode) throws Exception
	{
		LOGGER.debug("Inside getDefaultGroupingSource.");
		List<GroupingSourcesBean> groupingSourcesBeansList = null;
		Session session = getCurrentSession();
		Criteria groupingSourcesBeanCriteria = session.createCriteria(GroupingSourcesBean.class, "GroupingSourcesBean");
		groupingSourcesBeanCriteria.add(Restrictions.eq("GroupingSourcesBean.activeRecord", 1));
		groupingSourcesBeanCriteria.add(Restrictions.eq("GroupingSourcesBean.patientClassCode", patientClassCode));
		List resultList = groupingSourcesBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			groupingSourcesBeansList = (List<GroupingSourcesBean>) resultList;
		}
		LOGGER.info("Exiting from getDefaultGroupingSource.");
		return groupingSourcesBeansList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<GroupingSourcesBean> getGroupingSource(int encounterId) throws Exception
	{
		LOGGER.debug("Inside getGroupingSource.");
		List<GroupingSourcesBean> groupingSourcesBeansList = null;
		Session session = getCurrentSession();
		Criteria groupingSourcesBeanCriteria = session.createCriteria(GroupingSourcesBean.class, "GroupingSourcesBean");
		groupingSourcesBeanCriteria.add(Restrictions.eq("GroupingSourcesBean.activeRecord", 1));
		groupingSourcesBeanCriteria.add(Restrictions.eq("GroupingSourcesBean.encounterId", encounterId));
		List resultList = groupingSourcesBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			groupingSourcesBeansList = (List<GroupingSourcesBean>) resultList;
		}
		LOGGER.info("Exiting from getGroupingSource.");
		return groupingSourcesBeansList;
	}

	@Override
	public List<GroupingSourcesBean> saveUpdateGroupingSource(List<GroupingSourcesBean> beans) throws Exception
	{
		LOGGER.debug("Inside saveUpdateGroupingSource");
		Session session = getCurrentSession();
		for (GroupingSourcesBean d : beans)
		{
			session.saveOrUpdate(d);
		}

		LOGGER.debug("Exiting from saveUpdateGroupingSource");
		return beans;
	}

	@Override
	public List<ResequenceOptionsBean> getDefaultReseqOptions(String patientClassCode) throws Exception
	{
		LOGGER.debug("Inside getDefaultReseqOptions.");
		List<ResequenceOptionsBean> resequenceOptionsBeansList = null;
		Session session = getCurrentSession();
		Criteria resequenceOptionsBeanCriteria = session.createCriteria(ResequenceOptionsBean.class, "ResequenceOptionsBean");
		resequenceOptionsBeanCriteria.add(Restrictions.eq("ResequenceOptionsBean.isDefault", 1));
		resequenceOptionsBeanCriteria.add(Restrictions.eq("ResequenceOptionsBean.patientClassCode", patientClassCode));
		List resultList = resequenceOptionsBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			resequenceOptionsBeansList = (List<ResequenceOptionsBean>) resultList;
		}
		LOGGER.info("Exiting from getDefaultReseqOptions.");
		return resequenceOptionsBeansList;
	}

	@Override
	public List<ResequenceOptionsBean> getReseqOptions(int encounterId) throws Exception
	{
		LOGGER.debug("Inside getDefaultReseqOptions.");
		List<ResequenceOptionsBean> resequenceOptionsBeansList = null;
		Session session = getCurrentSession();
		Criteria resequenceOptionsBeanCriteria = session.createCriteria(ResequenceOptionsBean.class, "ResequenceOptionsBean");
		resequenceOptionsBeanCriteria.add(Restrictions.eq("ResequenceOptionsBean.activeRecord", 1));
		resequenceOptionsBeanCriteria.add(Restrictions.eq("ResequenceOptionsBean.encounterId", encounterId));
		List resultList = resequenceOptionsBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			resequenceOptionsBeansList = (List<ResequenceOptionsBean>) resultList;
		}
		LOGGER.info("Exiting from getDefaultReseqOptions.");
		return resequenceOptionsBeansList;
	}

	@Override
	public List<ResequenceOptionsBean> saveUpdateReseqOptions(List<ResequenceOptionsBean> beans) throws Exception
	{
		LOGGER.debug("Inside saveUpdateReseqOptions");
		Session session = getCurrentSession();
		for (ResequenceOptionsBean d : beans)
		{
			session.saveOrUpdate(d);
		}

		LOGGER.debug("Exiting from saveUpdateReseqOptions");
		return beans;
	}

	@Override
	public List<EditOptionsBean> getDefaultEditOptions(String patientClassCode) throws Exception
	{
		LOGGER.debug("Inside getDefaultEditOptions.");
		List<EditOptionsBean> editOptionsBeanList = null;
		Session session = getCurrentSession();
		Criteria editOptionsBeanCriteria = session.createCriteria(EditOptionsBean.class, "EditOptionsBean");
		editOptionsBeanCriteria.add(Restrictions.eq("EditOptionsBean.isDefault", 1));
		editOptionsBeanCriteria.add(Restrictions.eq("EditOptionsBean.patientClassCode", patientClassCode));
		List resultList = editOptionsBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			editOptionsBeanList = (List<EditOptionsBean>) resultList;
		}
		LOGGER.info("Exiting from getDefaultEditOptions.");
		return editOptionsBeanList;
	}

	@Override
	public List<EditOptionsBean> getEditOptions(int encounterId) throws Exception
	{
		LOGGER.debug("Inside getEditOptions.");
		List<EditOptionsBean> editOptionsBeanList = null;
		Session session = getCurrentSession();
		Criteria editOptionsBeanCriteria = session.createCriteria(EditOptionsBean.class, "EditOptionsBean");
		editOptionsBeanCriteria.add(Restrictions.eq("EditOptionsBean.activeRecord", 1));
		editOptionsBeanCriteria.add(Restrictions.eq("EditOptionsBean.encounterId", encounterId));
		List resultList = editOptionsBeanCriteria.list();
		if (resultList != null && resultList.size() > 0)
		{
			editOptionsBeanList = (List<EditOptionsBean>) resultList;
		}
		LOGGER.info("Exiting from getEditOptions.");
		return editOptionsBeanList;
	}

	@Override
	public List<EditOptionsBean> saveUpdateEditOptions(List<EditOptionsBean> beans) throws Exception
	{
		LOGGER.debug("Inside saveUpdateEditOptions");
		Session session = getCurrentSession();
		for (EditOptionsBean d : beans)
		{
			session.saveOrUpdate(d);
		}

		LOGGER.debug("Exiting from saveUpdateEditOptions");
		return beans;
	}

	public Map getBillingRecodedEncounters(Integer encounterId) throws Exception
	{
		try
		{
			Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    max(\n"
					+ "\t\tif(c.coder_encounterstatuscode = 'Billed', c.codingdate , \n" + "             c.reviewer_dts)\n" + "             \n"
					+ "\t ) as date\n" + " FROM\n" + "    EncounterInformation e\n" + "        INNER JOIN\n"
					+ "    FileAllocation f ON f.encounterid = e.encounterIndexid\n" + "        INNER JOIN\n"
					+ "    CaseAllocationHistory c ON c.caseAllocationID = f.fileallocationid\n"
					+ "        AND ((c.coder_encounterstatuscode = 'Billed'\n" + "        AND f.coder_encounterstatuscode != 'Billed')\n"
					+ "        OR (c.reviewer_encounterstatuscode = 'Billed'\n" + "        AND f.reviewer_encounterstatuscode != 'Billed'))\n"
					+ "        AND e.isActive = 1\n" + "        AND e.encounterIndexid = :encounterId\n" + " GROUP BY f.encounterid ";

			return (Map) session.createSQLQuery(sql).setParameter("encounterId", encounterId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	public Map getCompletedDateForReviewEncounters(Integer encounterId) throws Exception
	{
		try
		{
			Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    f.codingdate as date\n" + " FROM\n"
					+ "    EncounterInformation e\n" + "        INNER JOIN\n" + "    FileAllocation f ON f.encounterid = e.encounterIndexid\n"
					+ "        AND \n" + "        f.coder_encounterstatuscode = 'Completed' and \n"
					+ "        f.reviewer_encounterstatuscode != null\n" + "        AND e.isActive = 1\n"
					+ "        AND e.encounterIndexid = :encounterId \n" + " GROUP BY f.encounterid ";

			return (Map) session.createSQLQuery(sql).setParameter("encounterId", encounterId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	public Map getWorkingDateForCoderEncounters(Integer encounterId) throws Exception
	{
		try
		{
			Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    f.codingdate as date\n" + "   \n" + "FROM\n"
					+ "    EncounterInformation e\n" + "        INNER JOIN\n" + "    FileAllocation f ON f.encounterid = e.encounterIndexid\n"
					+ "        AND \n" + "        (f.coder_encounterstatuscode = 'In Progress' or f.coder_encounterstatuscode = 'In Queue' \n"
					+ "        or f.coder_encounterstatuscode = 'On Hold' or f.coder_encounterstatuscode = 'Updated')\n" + "        \n"
					+ "        AND e.isActive = 1\n" + "        AND e.encounterIndexid = :encounterId \n" + "GROUP BY f.encounterid ";

			return (Map) session.createSQLQuery(sql).setParameter("encounterId", encounterId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	public boolean doCaseRelatedChangesForClassChange(Integer encounterId, Date date) throws Exception
	{
		try
		{
			final Session session = getCurrentSession();
			LOGGER.info("INACTIVE DRGResponseValues for encounterId : " + encounterId);
			String sql = "update DRGResponseValues set activerecord = 0 where encounterIndexid = :id and creatorDTS > :date and activerecord = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			LOGGER.info("INACTIVE CodeMapTransaction for encounterId : " + encounterId);
			sql = "update CodeMapTransaction set isActive = 0 where encounterindexid = :id and creationTime >= :date and isActive = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			LOGGER.info("FETCHING EncoderEdits for encounterId : " + encounterId);
			sql = "select e.editsId from EncoderEdits e inner join CodeMapByUser c on e.codeMapByUserID = c.codeMapByUserId and c.encounterindexid = :id and e.creatorDTS > :date";
			List<Integer> list = session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).list();

			LOGGER.info("DELETING EncoderEdits for encounterId : " + encounterId);
			sql = "delete EncoderEdits  where editsId in ( :edits ) and e.creatorDTS > :date";
			session.createSQLQuery(sql).setParameterList("edits", list).setParameter("date", date).executeUpdate();

			LOGGER.info("INACTIVE CodeMapByUser for encounterId : " + encounterId);
			sql = "update CodeMapByUser set isActive = 0 where encounterindexid = :id and creationTime >= :date and isActive = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			LOGGER.info("FETCHING BeginEndMasterByUser for encounterId : " + encounterId);
			sql = "select b.beginEnduserId from BeginEndMasterByUser b \n"
					+ " inner join DocumentInformation d on b.docId = d.documentid and d.encounterIndexId = :id and b.creatorDTS > :date  and b.activeRecord = 1";
			list = session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).list();

			LOGGER.info("INACTIVE BeginEndMasterByUser for encounterId : " + encounterId);
			sql = "update BeginEndMasterByUser set activeRecord = 0 where beginEndUserId in ( :list )  and creatorDTS > :date and activeRecord = 1";
			session.createSQLQuery(sql).setParameterList("list", list).setParameter("date", date).executeUpdate();

			LOGGER.info("INACTIVE QueryMaster for encounterId : " + encounterId);
			sql = "update QueryMaster set activerecord = 0 where encounterid = :id   and datetime > :date and activerecord = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			LOGGER.info("INACTIVE Edit_Options for encounterId : " + encounterId);
			sql = "update Edit_Options set activeRecord = 0  where encounterID = :id and creatorDTS > :date and isDefault = 0 and activeRecord = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			LOGGER.info("INACTIVE Resequence_Options for encounterId : " + encounterId);
			sql = "update Resequence_Options set activeRecord = 0  where encounterID = :id and creatorDTS > :date and isDefault = 0 and activeRecord = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			LOGGER.info("INACTIVE GroupingSources for encounterId : " + encounterId);
			sql = "update GroupingSources set activeRecord = 0  where encounterID = :id and creatorDTS > :date and isDefault = 0 and activeRecord = 1";
			session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("date", date).executeUpdate();

			return true;
		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	public boolean deleteFileAllocation(Integer encounterId) throws Exception
	{
		LOGGER.info("inside deleteFileAllocation(LandingPageDao) dao");
		final Session session = getCurrentSession();
		final String hql = "DELETE FROM FileAllocationBean " + "WHERE encounterId = (:encounterId) ";
		Query query = session.createQuery(hql);
		query.setParameter("encounterId", encounterId);
		final int deletedCount = query.executeUpdate();
		LOGGER.info("exiting from deleteFileAllocation(LandingPageDao) dao");
		return deletedCount > 0;

	}

	/*
	 * 
	 * public boolean deleteFileAllocation(Integer encounterId) throws Exception { try { final Session session = getCurrentSession(); String
	 * sql = "delete FileAllocation where encounterid = :id"; return session.createSQLQuery(sql).setParameter("id",
	 * encounterId).executeUpdate() == 1; } catch (Exception ex) { LOGGER.error(ex); throw ex; } }
	 */

	public boolean removeReviewerFileAllocation(Integer encounterId) throws Exception
	{
		final Session session = getCurrentSession();
		String hql = "UPDATE FileAllocationBean set reviewerId = null , reviewerfirstname = null, reviewerlastname= null, reviewerEncounterStatus = null, reviewerStatusId = null "
				+ "WHERE encounterId = :encounterId";
		Query query = session.createQuery(hql);
		query.setParameter("encounterId", encounterId);
		int result = query.executeUpdate();
		return result == 1;
	}

	public boolean setCoderAsBilled(Integer encounterId) throws Exception
	{
		try
		{
			final Session session = getCurrentSession();
			String sql = "SELECT DISTINCT\n" + "    (f.encounterid) as encounterId, \n" + "    max(\n"
					+ "\t\tif(c.coder_encounterstatuscode = 'Billed', c.codingdate , \n" + "             c.reviewer_dts)\n" + "             \n"
					+ "\t ) as date,\n" + "     \n" + "     \tif(c.coder_encounterstatuscode = 'Billed', 'Coder' , \n"
					+ "             'Reviewer') as role,\n" + "             \n" + "     if(c.coder_encounterstatuscode = 'Billed', c.coderId , \n"
					+ "             c.reviewerId) as userId\n" + "         \n" + "FROM\n" + "    EncounterInformation e\n" + "        INNER JOIN\n"
					+ "    FileAllocation f ON f.encounterid = e.encounterIndexid\n" + "        INNER JOIN\n"
					+ "    CaseAllocationHistory c ON c.caseAllocationID = f.fileallocationid\n"
					+ "        AND ((c.coder_encounterstatuscode = 'Billed'\n" + "        AND f.coder_encounterstatuscode != 'Billed')\n"
					+ "        OR (c.reviewer_encounterstatuscode = 'Billed'\n" + "        AND f.reviewer_encounterstatuscode != 'Billed'))\n"
					+ "        AND e.isActive = 1\n" + "        AND e.encounterIndexid = :id \n" + "GROUP BY f.encounterid";

			Map map = (Map) session.createSQLQuery(sql).setParameter("id", encounterId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.uniqueResult();

			final String role = map.get("role").toString();
			final Integer userId = Integer.parseInt(map.get("userId").toString());
			final Date date = (Date) map.get("date");

			map = (Map) session
					.createSQLQuery("select userfirstname as firstName , userlastname  as lastName from UserLogin where userloginid = :userid ")
					.setParameter("userid", userId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();

			final String firstName = map.get("firstName").toString();
			final String lastName = map.get("lastName").toString();

			if (UserRoleConstant.CODER.equalsIgnoreCase(role))
			{
				sql = "update FileAllocation set " + " coderid = :userId ," + " coderfirstname = :firstName ," + " coderlastname = :lastName,"
						+ " coder_encounterstatuscode = 'Billed', " + " codingdate = :date " + "  reviewerId = null," + "  reviewerfirstname = null,"
						+ "  reviewerlastname = null," + "  reviewer_encounterstatuscode = null," + "  reviewer_dts = null"
						+ " where encounterid = :id ";

				return session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("userId", userId)
						.setParameter("firstName", firstName).setParameter("lastName", lastName).setParameter("date", date).executeUpdate() == 1;

			} else if (UserRoleConstant.REVIEWER.equalsIgnoreCase(role))
			{
				sql = "update FileAllocation set " + "  reviewerId = :userId," + "  reviewerfirstname = :firstName,"
						+ "  reviewerlastname = :lastName," + "  reviewer_encounterstatuscode = 'Billed'," + "  reviewer_dts = :date"
						+ " where encounterid = :id ";

				return session.createSQLQuery(sql).setParameter("id", encounterId).setParameter("userId", userId)
						.setParameter("firstName", firstName).setParameter("lastName", lastName).setParameter("date", date).executeUpdate() == 1;
			}

			return false;

		} catch (Exception ex)
		{
			LOGGER.error(ex);
			throw ex;
		}
	}

	@Override
	public List<Map<String, Object>> getExistingRoleForGivenEncounterIdHL7(Integer encounterId) throws Exception
	{
		try
		{
			LOGGER.info("Getting role information for given WorkList Id");
			List<Map<String, Object>> worklistRoleList = null;
			if (encounterId != null)
			{
				Session session = getCurrentSession();
				ProjectionList projectList = Projections.projectionList();
				projectList.add(Projections.distinct(Projections.property("roleId")), "roleId");
				projectList.add(Projections.property("roleName"), "roleName");
				Criteria criteria = session.createCriteria(EncounterWorkListMapBean.class);
				criteria.add(Restrictions.eq("encounterId", encounterId));
				criteria.setProjection(projectList);
				criteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				worklistRoleList = criteria.list();
			}
			LOGGER.info("Exit from Getting role information for given WorkList Id");
			return worklistRoleList;

		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
	}

	@Override
	public Boolean removeAllEncounterWorklistMapForGivenEncounterIdHL7(Integer encounterId) throws Exception
	{
		try
		{
			LOGGER.info("removing encounterWorklistMap for given encounterId dao.");
			boolean isDataUpsdated = false;
			if (encounterId != null)
			{
				Session session = getCurrentSession();
				String hqlString = "DELETE FROM EncounterWorkListMapBean where encounterId=:encounterId";
				Query query = session.createQuery(hqlString);
				query.setParameter("encounterId", encounterId);
				query.executeUpdate();
				isDataUpsdated = true;
			}
			LOGGER.info("Exit from removing encounterWorklistMap for given encounterId dao.");
			return isDataUpsdated;
		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
	}

	@Override
	public List<WorkListBean> getWorkListListForGivenEncounterHL7(EncounterBean encounter) throws Exception
	{
		try
		{
			LOGGER.info("Getting worklist information for given encounter");
			List<WorkListBean> workListList = null;
			if (encounter != null)
			{
				Session session = getCurrentSession();
				Criteria criteria = session.createCriteria(WorkListBean.class);
				if (encounter.getLocationId() != null)
				{
					criteria.add(Restrictions.or(Restrictions.eq("locationId", encounter.getLocationId()), Restrictions.isNull("locationId")));
				} else
				{
					criteria.add(Restrictions.isNull("locationId"));
				}
				if (encounter.getServiceLineId() != null)
				{
					criteria.add(Restrictions.or(Restrictions.eq("serviceLineId", encounter.getServiceLineId()), Restrictions.isNull("serviceLineId")));
				} else
				{
					criteria.add(Restrictions.isNull("serviceLineId"));
				}
				if (encounter.getPayerId() != null)
				{
					criteria.add(Restrictions.or(Restrictions.eq("payerId", encounter.getPayerId()), Restrictions.isNull("payerId")));
				} else
				{
					criteria.add(Restrictions.isNull("payerId"));
				}
				if (encounter.getPatientClassId() != null)
				{
					criteria.add(Restrictions.or(Restrictions.eq("patientClassId", encounter.getPatientClassId()),
							Restrictions.isNull("patientClassId")));
				} else
				{
					criteria.add(Restrictions.isNull("patientClassId"));
				}
				if (encounter.getPatientTypeId() != null)
				{
					criteria.add(Restrictions.or(Restrictions.eq("patientTypeId", encounter.getPatientTypeId()), Restrictions.isNull("patientTypeId")));
				} else
				{
					criteria.add(Restrictions.isNull("patientTypeId"));
				}
				workListList = criteria.list();
			}
			LOGGER.info("exitn from Getting worklist information for given encounter");
			return workListList;
		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
	}

	@Override
	public boolean insertEncounterWorkListMapHL7(Integer encounterId, Map<Integer, String> workListPriorityMap, Map<Integer, String> roleRoleIdMap)
			throws Exception
	{
		try
		{
			LOGGER.info("Inserting Encounter Worklist information DAO.");
			boolean isDataInserted = false;
			if (encounterId != null && roleRoleIdMap != null && !roleRoleIdMap.isEmpty())
			{
				Session session = getCurrentSession();
				EncounterWorkListMapBean bean = null;
				Set<Integer> roleIdSet = roleRoleIdMap.keySet();

				int count = 0;
				if (workListPriorityMap != null && !workListPriorityMap.isEmpty())
				{
					Set<Integer> workListIdList = workListPriorityMap.keySet();
					for (Integer worklistId : workListIdList)
					{
						for (Integer roleId : roleIdSet)
						{
							bean = new EncounterWorkListMapBean();
							bean.setEncounterId(encounterId);
							bean.setWorkListId(worklistId);
							bean.setRoleId(roleId);
							bean.setRoleName(roleRoleIdMap.get(roleId));
							bean.setPriority(workListPriorityMap.get(worklistId));
							session.save(bean);
							count++;
							if (count % 50 == 0)
							{
								session.flush();
								session.clear();
								count = 0;
							}
						}
					}
				} else
				{
					/* This block of code add encounter which is not satisfied by any worklist. */
					for (Integer roleId : roleIdSet)
					{
						bean = new EncounterWorkListMapBean();
						bean.setEncounterId(encounterId);
						bean.setWorkListId(null);
						bean.setRoleId(roleId);
						bean.setRoleName(roleRoleIdMap.get(roleId));
						bean.setPriority(WorkListConstants.ENCOUNTER_PRIORITY_LOW);
						session.save(bean);
						count++;
						if (count % 50 == 0)
						{
							session.flush();
							session.clear();
							count = 0;
						}
					}
				}
				isDataInserted = true;
			}
			LOGGER.info("Exit from Inserting Encounter Worklist information DAO.");
			return isDataInserted;
		} catch (Exception exception)
		{
			LOGGER.trace(exception);
			throw exception;
		}
	}
}
