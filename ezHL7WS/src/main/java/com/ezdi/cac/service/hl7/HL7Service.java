package com.ezdi.cac.service.hl7;

import java.util.Map;

public interface HL7Service
{
	void createOrUpdateCase(Map<String, Object> hl7InfoMap) throws Exception;

	void cancelAdmit(Map<String, Object> hl7InfoMap) throws Exception;

	void addDocument(Map<String, Object> hl7InfoMap) throws Exception;

	void addScannedDocument(Map<String, Object> hl7InfoMap) throws Exception;

	void addObservation(Map<String, Object> hl7InfoMap) throws Exception;

	Map<String, String> getTenantConfigMap() throws Exception;

	void lockEncounter(Map<String, Object> hl7InfoMap) throws Exception;

	void unLockEncounter(Map<String, Object> hl7InfoMap) throws Exception;

	void mergePatientInformation(Map<String, Object> hl7InfoMap) throws Exception;

	void swapPatientLocation(Map<String, Object> hl7InfoMap) throws Exception;

}
