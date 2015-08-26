package com.ezdi.cac.constant;

public interface CommonConstant
{
	String DOCUMENT_CODE_INDEX_FORMAT = "%03d";
	String ICD9_DIAGNOSIS_DOCUMENT_ID_FORMAT = "%015d";
	String ICD9_PROCEDURE_DOCUMENT_ID_FORMAT = "%015d";
	String ICD10_DIAGNOSIS_DOCUMEN_ID_FORMAT = "%015d";
	String ICD10_PROCEDURE_DOCUMENT_ID_FORMAT = "%015d";
	String CPT4_PROCEDURE_DOCUMENT_ID_FORMAT = "%015d";
	String ENM_PROCEDURE_DOCUMENT_ID_FORMAT = "%015d";
	String HCPCS_PROCEDURE_DOCUMENT_ID_FORMAT = "%015d";

	int ACTIVE_ENCOUNTER = 1;
	int INACTIVE_ENCOUNTER = 0;
	int ACTIVE_ENCOUNTER_PHYSICIAN = 1;
	int INACTIVE_ENCOUNTER_PHYSICIAN = 1;
	int ACTIVE_DOCUMENT = 1;
	int INACTIVE_DOCUMENT = 0;
	int ACTIVE_DOCUMENT_PHYSICIAN = 1;

	int ACTIVE_LOCATION = 1;
	int ACTIVE_LOCATION_PATIENT_CLASS = 1;
	int ACTIVE_LOCATION_PATIENT_CLASS_TYPE_SERVICE_LINE = 1;
	int ACTIVE_SERVICE_LINE_USER_ROLE_DOCUMENT_TYPE = 1;
	int REQUIRED_DOCUMENT_TYPE_FLAG = 1;

	int ACTIVE_USER_ROLE = 1;
	String OBSERVATION_DOCUMENT_TYPE = "OBS";
	int DEFAULT_DRG_IMPACT = 0;
	int DEFAULT_QUERY_OPPORTUNITY = 0;

	Integer TRANSCRIBED_DOCUMENT_FILE_FILTER = 1;
	Integer SCANNED_DOCUMENT_FILE_FILTER = 2;
	Integer OBSERVATION_DOCUMENT_FILE_FILTER = 3;

	String PHYSICIAN = "Physician";
	String HL7_USER_CODE = "hl7";
	String HL7_WS_FILE_PATH_ENV_VARIABLE = "HL7_WS_FILE_PATH";
	String APPLICATION_PROPERTIES_FILE_NAME = "hl7ws.application.properties";

	String HL7_NULL_FIELD = "\"\"";
}