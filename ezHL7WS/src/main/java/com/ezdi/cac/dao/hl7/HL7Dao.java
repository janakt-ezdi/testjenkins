package com.ezdi.cac.dao.hl7;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ezdi.cac.bean.hl7.EncounterStatusBean;
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
import com.ezdi.cac.search.listener.SearchEventInterceptor;

public interface HL7Dao
{
	PatientBean getPatient(String mrn) throws Exception;

	PatientBean insertPatient(PatientBean patientBean) throws Exception;

	PatientBean updatePatient(PatientBean patientBean) throws Exception;

	EncounterBean getEncounter(String mrn, String encounterNumber) throws Exception;

	EncounterBean insertEncounter(EncounterBean encounterBean) throws Exception;

	EncounterBean updateEncounter(EncounterBean encounterBean) throws Exception;

	EncounterHistoryBean insertEncounterHistory(EncounterHistoryBean bean) throws Exception;

	void insertEncounterEventSummaryBean(EncounterEventSummaryBean encounterEventSummaryBean) throws Exception;

	int getPhysicianLoginId(String globalId) throws Exception;

	Integer getUserLoginId(String userCode) throws Exception;

	List<EncounterPhysicianBean> getEncounterPhysicianBeanList(int encounterId) throws Exception;

	EncounterPhysicianBean insertEncounterPhysician(EncounterPhysicianBean encounterPhysicianBean) throws Exception;

	EncounterPhysicianBean updateEncounterPhysician(EncounterPhysicianBean encounterPhysicianBean) throws Exception;

	DocumentBean getDocument(int encounterId, String name) throws Exception;

	DocumentBean insertDocument(DocumentBean documentBean) throws Exception;

	DocumentPhysicianMapBean insertDocumentPhysician(DocumentPhysicianMapBean documentPhysicianMapBean) throws Exception;

	EncounterObservationRequestBean insertEncounterObservationRequest(EncounterObservationRequestBean encounterObservationRequestBean)
			throws Exception;

	Integer getDocumentId(String placerOrderNumber, Integer encounterId);

	EncounterObservationResultBean insertEncounterObservationResult(EncounterObservationResultBean encounterObservationResultBean) throws Exception;

	List<CodeMapByUserBean> getCodeList(int encounterId, int userId) throws Exception;

	int getDocumentCodingSystemCount(int documentId, String codingSystem, int userId) throws Exception;

	int getCodeTypeId(String codeType) throws Exception;

	MasterMapBean insertMasterId(MasterMapBean masterMapBean) throws Exception;

	CodeMasterBean getCodeMasterBean(String code, String codeType) throws Exception;

	CodeUpdateReasonBean getCodeUpdateReasonBean(String reason) throws Exception;

	CodeMapByUserBean insertCode(CodeMapByUserBean codeMapByUserBean) throws Exception;

	CodeMapByUserBean updateCode(CodeMapByUserBean codeMapByUserBean) throws Exception;

	RoleWiseEncounterStatusBean insertRoleWiseEncounterStatus(RoleWiseEncounterStatusBean roleWiseEncounterStatusBean) throws Exception;

	void updateRoleWiseEncounterStatus(RoleWiseEncounterStatusBean roleWiseEncounterStatusBean) throws Exception;

	Map<Integer, RoleWiseEncounterStatusBean> getRoleWiseEncounterStatus(Integer encounterId) throws Exception;

	LocationBean getLocation(String location) throws Exception;

	LocationBean insertLocation(LocationBean locationBean) throws Exception;

	PatientClassBean getPatientClass(String patientClass) throws Exception;

	PatientClassBean insertPatientClass(PatientClassBean patientClassBean) throws Exception;

	LocationPatientClassBean getLocationPatientClass(Integer locationId, Integer patientClassId) throws Exception;

	LocationPatientClassBean insertLocationPatientClass(LocationPatientClassBean locationPatientClassBean) throws Exception;

	LocationPatientClassBean updateLocationPatientClass(LocationPatientClassBean locationPatientClassBean) throws Exception;

	LocationPatientClassTypeServiceLineBean getLocationPatientClassTypeServiceLine(Integer locationPatientClassId, Integer patientTypeId,
			Integer serviceLineId) throws Exception;

	LocationPatientClassTypeServiceLineBean insertLocationPatientClassTypeServiceLine(
			LocationPatientClassTypeServiceLineBean locationPatientClassTypeServiceLineBean) throws Exception;

	LocationPatientClassTypeServiceLineBean updateLocationPatientClassTypeServiceLine(
			LocationPatientClassTypeServiceLineBean locationPatientClassTypeServiceLineBean) throws Exception;

	PatientTypeBean getPatientType(String patientType) throws Exception;

	PatientTypeBean insertPatientType(PatientTypeBean patientTypeBean) throws Exception;

	ServiceLineBean getServiceLine(String serviceLine) throws Exception;

	ServiceLineBean insertServiceLine(ServiceLineBean serviceLineBean) throws Exception;

	DocumentTypeBean getDocumentType(String documentType) throws Exception;

	List<DocumentTypeBean> getDocumentTypeList() throws Exception;

	DocumentTypeBean insertDocumentType(DocumentTypeBean documentTypeBean) throws Exception;

	int getDocumentTypeMaxSequenceNumber(Integer userRoleId, Integer patientClassId, Integer serviceLineId) throws Exception;

	List<Integer> getEncounterDocumentTypeSet(Integer encounterId) throws Exception;

	Map<Integer, Set<Integer>> getUserRoleRequiredDocumentTypeSet(Integer patientClassId, Integer serviceLineId) throws Exception;

	List<UserRoleBean> getActiveUserRole() throws Exception;

	List<PatientClassBean> getPatientClassList() throws Exception;

	List<ServiceLineBean> getServiceLineBeanList() throws Exception;

	ServiceLineUserRoleDocumentTypeBean getServiceLineUserRoleDocumentType(Integer documentTypeId, Integer userRoleId, Integer patientClassId,
			Integer serviceLineId) throws Exception;

	ServiceLineUserRoleDocumentTypeBean insertServiceLineUserRoleDocumentType(ServiceLineUserRoleDocumentTypeBean serviceLineUserRoleDocumentTypeBean)
			throws Exception;

	DischargeDispositionBean getDischargeDisposition(String dischargeDisposition) throws Exception;

	PayerBean getPayer(String code) throws Exception;

	PayerBean insertPayer(PayerBean payerBean) throws Exception;

	UserRoleBean getUserRole(String userRole) throws Exception;

	boolean isEncounterBilled(int encounterId) throws Exception;

	boolean isEncounterInQueueORUnassign(int encounterId) throws Exception;

	Map<String, String> getTenantConfigMap() throws Exception;

	FileAllocationBean getFileAllocationBean(int encounterId) throws Exception;

	FileAllocationBean updateFileAllocationBean(FileAllocationBean bean) throws Exception;

	List<DRGResponseValuesBean> getDrgResponses(int encounterId) throws Exception;

	List<DRGResponseValuesBean> updateDrgResponses(List<DRGResponseValuesBean> beans) throws Exception;

	EncounterStatusBean getEncounterStatus(String status) throws Exception;

	Map<String, String> getTenantConfigMapWithNewSession() throws Exception;

	List<WorkListBean> getWorkListListForGivenEncounterHL7(EncounterBean encounter) throws Exception;

	List<Map<String, Object>> getExistingRoleForGivenEncounterIdHL7(Integer encounterId) throws Exception;

	Boolean removeAllEncounterWorklistMapForGivenEncounterIdHL7(Integer encounterId) throws Exception;

	boolean insertEncounterWorkListMapHL7(Integer encounterId, Map<Integer, String> workListPriorityMap, Map<Integer, String> roleRoleIdMap)
			throws Exception;

	SearchEventInterceptor getSearchEventInterceptor();

	void setSearchEventInterceptor(SearchEventInterceptor searchEventInterceptor);

	List<GroupingSourcesBean> getDefaultGroupingSource(String patientClassCode) throws Exception;

	List<GroupingSourcesBean> getDefaultGroupingSource() throws Exception;

	List<ResequenceOptionsBean> getDefaultReseqOptions(String patientClassCode) throws Exception;

	List<ResequenceOptionsBean> getDefaultReseqOptions() throws Exception;

	List<EditOptionsBean> getDefaultEditOptions(String patientClassCode) throws Exception;

	List<EditOptionsBean> getDefaultEditOptions() throws Exception;

	List<GroupingSourcesBean> getGroupingSource(int encounterId) throws Exception;

	List<ResequenceOptionsBean> getReseqOptions(int encounterId) throws Exception;

	List<EditOptionsBean> getEditOptions(int encounterId) throws Exception;

	List<GroupingSourcesBean> saveUpdateGroupingSource(List<GroupingSourcesBean> beans) throws Exception;

	List<ResequenceOptionsBean> saveUpdateReseqOptions(List<ResequenceOptionsBean> beans) throws Exception;

	List<EditOptionsBean> saveUpdateEditOptions(List<EditOptionsBean> beans) throws Exception;

	Map getBillingRecodedEncounters(Integer encounterId) throws Exception;

	List<Map> getBillingRecodedEncounters(Set<Integer> encounterId) throws Exception;

	List<Map> getCompletedDateForReviewEncounters(Set<Integer> encounterIdSet) throws Exception;

	Map getCompletedDateForReviewEncounters(Integer encounterId) throws Exception;

	List<Map> getWorkingDateForCoderEncounters(Set<Integer> encounterId) throws Exception;

	Map getWorkingDateForCoderEncounters(Integer encounterId) throws Exception;

	Integer getUserRoleIdFromCode(String role) throws Exception;

	boolean deleteIssues(Set<Integer> encounterIdSet, Integer roleId) throws Exception;

	boolean deleteDRGValues(Set<Integer> encounterIdSet, Integer roleId) throws Exception;

	List<Long> getCodeMapByUserIdList(Set<Integer> encounterIdSet, Integer roleId) throws Exception;

	boolean deleteCodeHistory(List<Long> codeMapByUserIdList) throws Exception;

	boolean deleteEncoderEdits(List<Long> codeMapByUserIdList) throws Exception;

	boolean deleteCodes(List<Long> codeMapByUserIdList) throws Exception;

	List<Integer> getEvidenceIdList(Set<Integer> encounterIdSet, Integer roleId) throws Exception;

	boolean deleteQueryEvidence(List<Integer> evidenceIdList) throws Exception;

	boolean deleteEvidence(List<Integer> evidenceIdList) throws Exception;

	List<Integer> getUserRoleUserMapIdList(Set<Integer> encounterIdSet, Integer roleId) throws Exception;

	boolean deleteEncounterDDMapEntries(Set<Integer> encounterIdSet, List<Integer> encounterUserRoleMapIdList) throws Exception;

	boolean deleteGroupingSourcesEntries(Set<Integer> encounterIdSet, List<Integer> encounterUserRoleMapIdList) throws Exception;

	boolean doCaseRelatedChangesForClassChange(Integer encounterId, Date date) throws Exception;

	boolean deleteFileAllocation(Integer encounterId) throws Exception;

	boolean removeReviewerFileAllocation(Integer encounterId) throws Exception;

	boolean setCoderAsBilled(Integer encounterId) throws Exception;

	boolean hasNewPatientClassAccess(int encounterId, String newPatientClass, Integer newPatientClassId, Integer locationId, Integer userId)
			throws Exception;
}
