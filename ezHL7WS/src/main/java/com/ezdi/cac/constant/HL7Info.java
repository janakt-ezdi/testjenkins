package com.ezdi.cac.constant;

public interface HL7Info
{
	String ACTION = "action";
	String TENANT_CODE = "tenantCode";

	String ENCOUNTER = "encounter";
	String ENCOUNTER_NUMBER = "encounterNumber";

	String ENCOUNTER_PATIENT_MRN = "mrn";
	String ENCOUNTER_PATIENT_FIRST_NAME = "firstName";
	String ENCOUNTER_PATIENT_LAST_NAME = "lastName";
	String ENCOUNTER_PATIENT_DATE_OF_BIRTH = "dateOfBirth";
	String ENCOUNTER_PATIENT_GENDER = "gender";
	
	String ENCOUNTER_PATIENT_NEW_MRN = "newmrn"; 

	String ENCOUNTER_ADMISSION_TYPE = "admissionType";
	
	String ENCOUNTER_LIST = "encounterList"; 

	String ENCOUNTER_LOCATION = "location";
	String ENCOUNTER_PATIENT_CLASS = "patientClass";
	String ENCOUNTER_PATIENT_TYPE = "patientType";
	String ENCOUNTER_SERVICE_LINE = "serviceLine";

	String ENCOUNTER_DISCHARGE_DISPOSITION = "dischargeDisposition";
	String ENCOUNTER_ADMIT_SOURCE = "admitSource";

	String ENCOUNTER_DATE_OF_ADMISSION = "dateOfAdmition";
	String ENCOUNTER_DATE_OF_DISCHARGE = "dateOfDischarge";
	String ENCOUNTER_ACCOUNT_NUMBER = "accountNumber";
	String ENCOUNTER_TOTAL_CHARGES = "totalCharges";
	String ENCOUNTER_BILL_TYPE = "billType";
	String ENCOUNTER_PAYER = "payer";
	String ENCOUNTER_PAYER_DESC = "payerDesc";
	String ENCOUNTER_NEW_BORN_WEIGHT = "newBornWeight";
	String ENCOUNTER_FINANCIAL_CLASSES = "financialClasses";

	String PATIENT_BUILDING = "building";
	String PATIENT_FLOOR = "floor";
	String PATIENT_POINT_OF_CARE = "pointOfCare";
	String PATIENT_ROOM = "room";
	String PATIENT_BED = "bed";
	String FACILITY = "facility";
	String PATIENT_LOCATION_STATUS = "locationStatus";
	String PATIENT_PERSON_LOCATION_TYPE = "personLocationType";

	String PHYSICIAN_LIST = "physicianList";
	String PHYSICIAN_GLOBAL_ID = "globalId";
	String PHYSICIAN_TYPE = "type";

	String DOCUMENT = "document";
	String DOCUMENT_NAME = "name";
	String DOCUMENT_TYPE = "type";
	String DOCUMENT_CONTENT_TYPE = "contentType";
	String DOCUMENT_DATE_OF_SERVICE = "dateOfService";
	String DOCUMENT_PARENT_UNIQUE_NUMBER = "parentUniqueNumber";
	String DOCUMENT_CONTENT = "content";
	String DOCUMENT_PHYSICIAN_LIST = "physicianList";
	String DOCUMENT_PHYSICIAN_GLOBAL_ID = "globalId";

	String CODE_LIST = "codeList";
	String CODE = "code";
	String CODE_TEXT = "text";
	String CODING_SYSTEM = "codingSystem";
	String CODE_TYPE = "type";
	String CODE_IS_ADMITTING = "isAdmitting";
	String CODE_STATUS_CODE = "statusCode";
	String CODE_ORDINALITY = "ordinality";
	String PROCEDURE_DATE = "procedureDate";
	String PROCEDURE_ANESTHESIA_TYPE = "anesthesiaType";
	String PROCEDURE_ANESTHESIA_PROVIDER = "anesthesiaProvider";
	String PROCEDURE_ANESTHESIA_TIME = "anesthesiaTime";

	String OBSERVATION_LIST = "observationList";
	String OBSERVATION_REQUEST = "observationRequest";
	String OBSERVATION_RESULT_LIST = "observationResultList";

	String OBSERVATION_REQUEST_PLACER_ORDER_NUMBER = "placerOrderNumber";
	String OBSERVATION_REQUEST_UNIVERSAL_SERVICE_ID = "universalServiceId";
	String OBSERVATION_REQUEST_DATE_TIME = "observationDateTime";
	String OBSERVATION_REQUEST_ORDERING_PROVIDER = "orderingProviderList";
	String OBSERVATION_REQUEST_RESULT_STATUS = "resultStatus";
	String OBSERVATION_REQUEST_REASON_FOR_STUDY = "reasonForStudyList";
	String OBSERVATION_REQUEST_COMMENT = "comment";

	String OBSERVATION_RESULT_IDENTIFIER = "observationIdentifier";
	String OBSERVATION_RESULT_SUB_ID = "observationSubId";
	String OBSERVATION_RESULT_VALUE = "observationValue";
	String OBSERVATION_RESULT_UNITS = "units";
	String OBSERVATION_RESULT_REFERENCE_RANGE = "referenceRange";
	String OBSERVATION_RESULT_ABNORMAL_FLAGS = "abnormalFlagList";
	String OBSERVATION_RESULT_COMMENT = "comment";

}