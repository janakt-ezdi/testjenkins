package com.ezdi.cac.constant.table;

public interface EncounterTableConstant
{

	String TABLE_NAME = "EncounterInformation";

	String ID = "encounterIndexid";
	String PATIENT_ID = "patientinfoid";
	String ENCOUNER_NUMBER = "encounterid";

	String HOSPITAL_NAME = "hospitalName";

	String LOCATION_ID = "locationId";
	String LOCATION_NAME = "locationName";

	String PATIENT_CLASS_ID = "patientclassid";
	String PATIENT_CLASS_CODE = "patientclasscode";
	String PATIENT_CLASS_DESC = "patientclassdesc";

	String PATIENT_TYPE_ID = "Patient_Type_Id";
	String PATIENT_TYPE_CODE = "Patient_Type_Code";

	String SERVICE_LINE_ID = "serviceLineId";
	String SERVICE_LINE_CODE = "serviceLineCode";
	String SERVICE_LINE_DESC = "serviceLineDesc";

	String ADMIT_SOURCE_CODE = "admitCode";

	String DISCHARGE_DISPOSITION_ID = "patient_status_id";
	String DISCHARGE_DISPOSITION_CODE = "dischargeDispositionCode";
	String DISCHARGE_DISPOSITION_DESC = "dischargeDispositionDesc";

	String ACCOUNT_NO = "accountno";
	String BILL_TYPE = "bill_type";
	String FINANCIAL_CLASSES = "financialClasses";
	String TOTAL_CHARGES = "total_charges";

	String PAYER_FLAG = "payer_flag";
	String PAYER_ID = "payerId";
	String PAYER_CODE = "payer";
	String PAYER_DESC = "payerDescription";

	String NEW_BORN_WEIGHT = "newborn_weight";
	String DATE_OF_ADMITION = "dateofadmition";
	String DATE_OF_DISCHARGE = "dateofdischarge";
	String OVERRIDE_LOS = "override_los";
	String LENGTH_OF_STAY = "length_of_stay";

	String PRIORITY = "priority";
	String QUERY_OPERTUNITY = "QueryOpertunity";
	String DRG_IMPACT = "DRGImpact";
	String QARE_BILL_STATUS = "qarebillstatus";
	String PATIENT_STATUS_COMMENT = "patient_status_comment";

	String BUILDING = "building";
	String FLOOR = "floor";
	String POINT_OF_CARE = "point_of_care";
	String ROOM = "room";
	String BED = "bed";
	String FACILITY = "facility";
	String LOCATION_STATUS = "location_status";
	String PERSON_LOCATION_TYPE = "person_location_type";

	String PATIENT_MRN = "mrn";
	String PATIENT_FIRST_NAME = "patientfirstName";
	String PATIENT_LAST_NAME = "patientlastName";
	String PATIENT_DATE_OF_BIRTH = "dateofbirth";
	String PATIENT_GENDER = "gender";

	String CREATOR_USER_ID = "creatorUserId";
	String CREATION_DATE_TIME = "creatorDTS";
	String UPDATER_USER_ID = "updaterUserId";
	String UPDATION_DATE_TIME = "updationDTS";

	String LOCK_BY = "lockedBy";
	String IS_ACTIVE = "isActive";
	String IS_ENCOUNTER_UPDATED = "isEncounterUpdated";
	// added by Prashant
	String ADMISSION_TYPE = "admission_type";

}