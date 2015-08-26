package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterHistoryTableConstant;

@Entity
@Table(name = EncounterHistoryTableConstant.TABLE_NAME)
public class EncounterHistoryBean
{
	@Id
	@GeneratedValue
	@Column(name = EncounterHistoryTableConstant.ID)
	private Integer id;

	@Column(name = EncounterHistoryTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = EncounterHistoryTableConstant.PATIENT_ID)
	private Integer patientId;

	@Column(name = EncounterHistoryTableConstant.ENCOUNER_NUMBER)
	private String encounterNumber;

	@Column(name = EncounterHistoryTableConstant.HOSPITAL_NAME)
	private String hospitalName;

	@Column(name = EncounterHistoryTableConstant.LOCATION_ID)
	private Integer locationId;

	@Column(name = EncounterHistoryTableConstant.LOCATION_NAME)
	private String locationName;

	@Column(name = EncounterHistoryTableConstant.PATIENT_CLASS_ID)
	private Integer patientClassId;

	@Column(name = EncounterHistoryTableConstant.PATIENT_CLASS_CODE)
	private String patientClassCode;

	@Column(name = EncounterHistoryTableConstant.PATIENT_TYPE_ID)
	private Integer patientTypeId;

	@Column(name = EncounterHistoryTableConstant.PATIENT_TYPE_CODE)
	private String patientTypeCode;

	@Column(name = EncounterHistoryTableConstant.SERVICE_LINE_ID)
	private Integer serviceLineId;

	@Column(name = EncounterHistoryTableConstant.SERVICE_LINE_CODE)
	private String serviceLineCode;

	@Column(name = EncounterHistoryTableConstant.ADMIT_SOURCE_CODE)
	private String admitSourceCode;

	@Column(name = EncounterHistoryTableConstant.DISCHARGE_DISPOSITION_ID)
	private Integer dischargeDispositionId;

	@Column(name = EncounterHistoryTableConstant.DISCHARGE_DISPOSITION_CODE)
	private String dischargeDispositionCode;

	@Column(name = EncounterHistoryTableConstant.DISCHARGE_DISPOSITION_DESC)
	private String dischargeDispositionDesc;

	@Column(name = EncounterHistoryTableConstant.ACCOUNT_NO)
	private String accountNumber;

	@Column(name = EncounterHistoryTableConstant.BILL_TYPE)
	private String billType;

	@Column(name = EncounterHistoryTableConstant.FINANCIAL_CLASSES)
	private String financialClasses;

	@Column(name = EncounterHistoryTableConstant.TOTAL_CHARGES)
	private String totalCharges;

	@Column(name = EncounterHistoryTableConstant.PAYER_FLAG)
	private Boolean payerFlag;

	@Column(name = EncounterHistoryTableConstant.PAYER)
	private String payer;

	@Column(name = EncounterHistoryTableConstant.PAYER_ID)
	private Integer payerId;

	@Column(name = EncounterHistoryTableConstant.NEW_BORN_WEIGHT)
	private String newBornWeight;

	@Column(name = EncounterHistoryTableConstant.DATE_OF_ADMITION)
	private Date dateOfAdmition;

	@Column(name = EncounterHistoryTableConstant.DATE_OF_DISCHARGE)
	private Date dateOfDischarge;

	@Column(name = EncounterHistoryTableConstant.OVERRIDE_LOS)
	private String overrideLOS;

	@Column(name = EncounterHistoryTableConstant.LENGTH_OF_STAY)
	private String lengthOfStay;

	@Column(name = EncounterHistoryTableConstant.PRIORITY)
	private String priority;

	@Column(name = EncounterHistoryTableConstant.QUERY_OPERTUNITY)
	private Integer queryOpertunity;

	@Column(name = EncounterHistoryTableConstant.DRG_IMPACT)
	private Integer drgImpact;

	@Column(name = EncounterHistoryTableConstant.QARE_BILL_STATUS)
	private String qarebillstatus;

	@Column(name = EncounterHistoryTableConstant.PATIENT_STATUS_COMMENT)
	private String patientStatusComment;

	@Column(name = EncounterHistoryTableConstant.BUILDING)
	private String building;

	@Column(name = EncounterHistoryTableConstant.FLOOR)
	private String floor;

	@Column(name = EncounterHistoryTableConstant.POINT_OF_CARE)
	private String pointOfCare;

	@Column(name = EncounterHistoryTableConstant.ROOM)
	private String room;

	@Column(name = EncounterHistoryTableConstant.BED)
	private String bed;

	@Column(name = EncounterHistoryTableConstant.LOCATION_STATUS)
	private String locationStatus;

	@Column(name = EncounterHistoryTableConstant.PERSON_LOCATION_TYPE)
	private String personLocationType;

	@Column(name = EncounterHistoryTableConstant.PATIENT_MRN)
	private String patientMrn;

	@Column(name = EncounterHistoryTableConstant.PATIENT_FIRST_NAME)
	private String patientFirstName;

	@Column(name = EncounterHistoryTableConstant.PATIENT_LAST_NAME)
	private String patientLastName;

	@Column(name = EncounterHistoryTableConstant.PATIENT_DATE_OF_BIRTH)
	private Date patientDateOfBirth;

	@Column(name = EncounterHistoryTableConstant.PATIENT_GENDER)
	private String patientGender;

	@Column(name = EncounterHistoryTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = EncounterHistoryTableConstant.CREATION_DATE_TIME)
	private Date creatorDateTime;

	@Column(name = EncounterHistoryTableConstant.UPDATER_USER_ID)
	private Integer updaterUserId;

	@Column(name = EncounterHistoryTableConstant.UPDATION_DATE_TIME)
	private Date updationDateTime;

	@Column(name = EncounterHistoryTableConstant.LOCK_BY)
	private String lockBy;

	@Column(name = EncounterHistoryTableConstant.IS_ACTIVE)
	private Integer isActive;

	@Column(name = EncounterHistoryTableConstant.IS_ENCOUNTER_UPDATED)
	private Integer isEncounterUpdated;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public Integer getPatientId()
	{
		return patientId;
	}

	public void setPatientId(Integer patientId)
	{
		this.patientId = patientId;
	}

	public String getEncounterNumber()
	{
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber)
	{
		this.encounterNumber = encounterNumber;
	}

	public String getHospitalName()
	{
		return hospitalName;
	}

	public void setHospitalName(String hospitalName)
	{
		this.hospitalName = hospitalName;
	}

	public Integer getLocationId()
	{
		return locationId;
	}

	public void setLocationId(Integer locationId)
	{
		this.locationId = locationId;
	}

	public String getLocationName()
	{
		return locationName;
	}

	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}

	public Integer getPatientClassId()
	{
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId)
	{
		this.patientClassId = patientClassId;
	}

	public String getPatientClassCode()
	{
		return patientClassCode;
	}

	public void setPatientClassCode(String patientClassCode)
	{
		this.patientClassCode = patientClassCode;
	}

	public Integer getPatientTypeId()
	{
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId)
	{
		this.patientTypeId = patientTypeId;
	}

	public String getPatientTypeCode()
	{
		return patientTypeCode;
	}

	public void setPatientTypeCode(String patientTypeCode)
	{
		this.patientTypeCode = patientTypeCode;
	}

	public Integer getServiceLineId()
	{
		return serviceLineId;
	}

	public void setServiceLineId(Integer serviceLineId)
	{
		this.serviceLineId = serviceLineId;
	}

	public String getServiceLineCode()
	{
		return serviceLineCode;
	}

	public void setServiceLineCode(String serviceLineCode)
	{
		this.serviceLineCode = serviceLineCode;
	}

	public String getAdmitSourceCode()
	{
		return admitSourceCode;
	}

	public void setAdmitSourceCode(String admitSourceCode)
	{
		this.admitSourceCode = admitSourceCode;
	}

	public Integer getDischargeDispositionId()
	{
		return dischargeDispositionId;
	}

	public void setDischargeDispositionId(Integer dischargeDispositionId)
	{
		this.dischargeDispositionId = dischargeDispositionId;
	}

	public String getDischargeDispositionCode()
	{
		return dischargeDispositionCode;
	}

	public void setDischargeDispositionCode(String dischargeDispositionCode)
	{
		this.dischargeDispositionCode = dischargeDispositionCode;
	}

	public String getDischargeDispositionDesc()
	{
		return dischargeDispositionDesc;
	}

	public void setDischargeDispositionDesc(String dischargeDispositionDesc)
	{
		this.dischargeDispositionDesc = dischargeDispositionDesc;
	}

	public String getAccountNumber()
	{
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	public String getBillType()
	{
		return billType;
	}

	public void setBillType(String billType)
	{
		this.billType = billType;
	}

	public String getFinancialClasses()
	{
		return financialClasses;
	}

	public void setFinancialClasses(String financialClasses)
	{
		this.financialClasses = financialClasses;
	}

	public String getTotalCharges()
	{
		return totalCharges;
	}

	public void setTotalCharges(String totalCharges)
	{
		this.totalCharges = totalCharges;
	}

	public Boolean getPayerFlag()
	{
		return payerFlag;
	}

	public void setPayerFlag(Boolean payerFlag)
	{
		this.payerFlag = payerFlag;
	}

	public String getPayer()
	{
		return payer;
	}

	public void setPayer(String payer)
	{
		this.payer = payer;
	}

	public Integer getPayerId()
	{
		return payerId;
	}

	public void setPayerId(Integer payerId)
	{
		this.payerId = payerId;
	}

	public String getNewBornWeight()
	{
		return newBornWeight;
	}

	public void setNewBornWeight(String newBornWeight)
	{
		this.newBornWeight = newBornWeight;
	}

	public Date getDateOfAdmition()
	{
		return dateOfAdmition;
	}

	public void setDateOfAdmition(Date dateOfAdmition)
	{
		this.dateOfAdmition = dateOfAdmition;
	}

	public Date getDateOfDischarge()
	{
		return dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge)
	{
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getOverrideLOS()
	{
		return overrideLOS;
	}

	public void setOverrideLOS(String overrideLOS)
	{
		this.overrideLOS = overrideLOS;
	}

	public String getLengthOfStay()
	{
		return lengthOfStay;
	}

	public void setLengthOfStay(String lengthOfStay)
	{
		this.lengthOfStay = lengthOfStay;
	}

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public Integer getQueryOpertunity()
	{
		return queryOpertunity;
	}

	public void setQueryOpertunity(Integer queryOpertunity)
	{
		this.queryOpertunity = queryOpertunity;
	}

	public Integer getDrgImpact()
	{
		return drgImpact;
	}

	public void setDrgImpact(Integer drgImpact)
	{
		this.drgImpact = drgImpact;
	}

	public String getQarebillstatus()
	{
		return qarebillstatus;
	}

	public void setQarebillstatus(String qarebillstatus)
	{
		this.qarebillstatus = qarebillstatus;
	}

	public String getPatientStatusComment()
	{
		return patientStatusComment;
	}

	public void setPatientStatusComment(String patientStatusComment)
	{
		this.patientStatusComment = patientStatusComment;
	}

	public String getBuilding()
	{
		return building;
	}

	public void setBuilding(String building)
	{
		this.building = building;
	}

	public String getFloor()
	{
		return floor;
	}

	public void setFloor(String floor)
	{
		this.floor = floor;
	}

	public String getPointOfCare()
	{
		return pointOfCare;
	}

	public void setPointOfCare(String pointOfCare)
	{
		this.pointOfCare = pointOfCare;
	}

	public String getRoom()
	{
		return room;
	}

	public void setRoom(String room)
	{
		this.room = room;
	}

	public String getBed()
	{
		return bed;
	}

	public void setBed(String bed)
	{
		this.bed = bed;
	}

	public String getLocationStatus()
	{
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus)
	{
		this.locationStatus = locationStatus;
	}

	public String getPersonLocationType()
	{
		return personLocationType;
	}

	public void setPersonLocationType(String personLocationType)
	{
		this.personLocationType = personLocationType;
	}

	public String getPatientMrn()
	{
		return patientMrn;
	}

	public void setPatientMrn(String patientMrn)
	{
		this.patientMrn = patientMrn;
	}

	public String getPatientFirstName()
	{
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName)
	{
		this.patientFirstName = patientFirstName;
	}

	public String getPatientLastName()
	{
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName)
	{
		this.patientLastName = patientLastName;
	}

	public Date getPatientDateOfBirth()
	{
		return patientDateOfBirth;
	}

	public void setPatientDateOfBirth(Date patientDateOfBirth)
	{
		this.patientDateOfBirth = patientDateOfBirth;
	}

	public String getPatientGender()
	{
		return patientGender;
	}

	public void setPatientGender(String patientGender)
	{
		this.patientGender = patientGender;
	}

	public Integer getCreatorUserId()
	{
		return creatorUserId;
	}

	public void setCreatorUserId(Integer creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	public Date getCreatorDateTime()
	{
		return creatorDateTime;
	}

	public void setCreatorDateTime(Date creatorDateTime)
	{
		this.creatorDateTime = creatorDateTime;
	}

	public Integer getUpdaterUserId()
	{
		return updaterUserId;
	}

	public void setUpdaterUserId(Integer updaterUserId)
	{
		this.updaterUserId = updaterUserId;
	}

	public Date getUpdationDateTime()
	{
		return updationDateTime;
	}

	public void setUpdationDateTime(Date updationDateTime)
	{
		this.updationDateTime = updationDateTime;
	}

	public String getLockBy()
	{
		return lockBy;
	}

	public void setLockBy(String lockBy)
	{
		this.lockBy = lockBy;
	}

	public Integer getIsActive()
	{
		return isActive;
	}

	public void setIsActive(Integer isActive)
	{
		this.isActive = isActive;
	}

	public Integer getIsEncounterUpdated()
	{
		return isEncounterUpdated;
	}

	public void setIsEncounterUpdated(Integer isEncounterUpdated)
	{
		this.isEncounterUpdated = isEncounterUpdated;
	}

}
