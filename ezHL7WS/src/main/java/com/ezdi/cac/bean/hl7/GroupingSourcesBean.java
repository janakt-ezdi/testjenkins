package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.GroupingSourcesTableConstant;

@Entity
@Table(name = GroupingSourcesTableConstant.TABLE_NAME)
public class GroupingSourcesBean
{

	@Id
	@Column(name = GroupingSourcesTableConstant.GROUPINGSOURCESID)
	@GeneratedValue
	private Integer groupingSourcesID;

	@Column(name = GroupingSourcesTableConstant.COST_OUTLIER_PAYMENTS)
	private String costOutlierPayments;

	@Column(name = GroupingSourcesTableConstant.MEDICARE_PROVIDER_NUMBER)
	private String medicareProviderNumber;

	@Column(name = GroupingSourcesTableConstant.PRIMARY)
	private String primary;

	@Column(name = GroupingSourcesTableConstant.USE_ALTER_GROUPER)
	private String userAlterGrouper;

	@Column(name = GroupingSourcesTableConstant.GROUPER_TYPE)
	private String grouperType;

	@Column(name = GroupingSourcesTableConstant.GROUPER_VERSION)
	private String grouperVersion;

	@Column(name = GroupingSourcesTableConstant.USE_CMS_HAC_LOGIC)
	private String useCMSHASLogin;

	@Column(name = GroupingSourcesTableConstant.CALCULATE_APC_FOR_ALL_CLAIM_DISPOSITIONS)
	private String calculateAPCForAllClaimDispositions;

	@Column(name = GroupingSourcesTableConstant.EXCLUDE_ALL_FEE_SCHEDULE_PRICING)
	private String excludeAllFeeSchedulePricing;

	@Column(name = GroupingSourcesTableConstant.PRICER_TYPE)
	private String pricerType;

	@Column(name = GroupingSourcesTableConstant.PRICER_VALUES)
	private String pricerValue;

	@Column(name = GroupingSourcesTableConstant.RATE)
	private String rate;

	@Column(name = GroupingSourcesTableConstant.SIMPLE_PRICING)
	private String simplePricing;

	@Column(name = GroupingSourcesTableConstant.USE_CMS24_REIMBURSEMENT)
	private String useCMS24Reimbursement;

	@Column(name = GroupingSourcesTableConstant.DRG_PREPOPULATE)
	private String drgPrepopulate;

	@Column(name = GroupingSourcesTableConstant.ENCOUNTERID)
	private Integer encounterId;

	@Column(name = GroupingSourcesTableConstant.USE_CAH_LOGIC)
	private String useCahLogic;

	@Column(name = GroupingSourcesTableConstant.UPDATEDTS)
	private Date updateDTS;

	@Column(name = GroupingSourcesTableConstant.CREATORDTS)
	private Date creatorDTS;

	@Column(name = GroupingSourcesTableConstant.UPDATEUSERID)
	private Integer updateUserId;

	@Column(name = GroupingSourcesTableConstant.PATIENTCLASSID)
	private Integer patientClassId;

	@Column(name = GroupingSourcesTableConstant.CREATORUSERID)
	private Integer creatorUserId;

	@Column(name = GroupingSourcesTableConstant.ACTIVERECORD)
	private Integer activeRecord;

	@Column(name = GroupingSourcesTableConstant.ISDEFAULT, nullable = false, columnDefinition = "int default 0")
	private Integer isDefault = 0;

	@Column(name = GroupingSourcesTableConstant.APR_ADMIT_LOGIC)
	private String aprAdmitLogic;

	@Column(name = GroupingSourcesTableConstant.PRICER_DESCRIPTION)
	private String pricerDescription;

	@Column(name = GroupingSourcesTableConstant.CODING_SYSTEM)
	private String codingSystem;

	@Column(name = GroupingSourcesTableConstant.USERROLEUSERID)
	private Integer userRoleUserId;

	@Column(name = GroupingSourcesTableConstant.PATIENTCLASSCODE)
	private String patientClassCode;

	@Column(name = GroupingSourcesTableConstant.PATIENTCLASSDESC)
	private String patientClassDesc;

	public Integer getGroupingSourcesID()
	{
		return groupingSourcesID;
	}

	public void setGroupingSourcesID(Integer groupingSourcesID)
	{
		this.groupingSourcesID = groupingSourcesID;
	}

	public String getCostOutlierPayments()
	{
		return costOutlierPayments;
	}

	public void setCostOutlierPayments(String costOutlierPayments)
	{
		this.costOutlierPayments = costOutlierPayments;
	}

	public String getMedicareProviderNumber()
	{
		return medicareProviderNumber;
	}

	public void setMedicareProviderNumber(String medicareProviderNumber)
	{
		this.medicareProviderNumber = medicareProviderNumber;
	}

	public String getPrimary()
	{
		return primary;
	}

	public void setPrimary(String primary)
	{
		this.primary = primary;
	}

	public String getUserAlterGrouper()
	{
		return userAlterGrouper;
	}

	public void setUserAlterGrouper(String userAlterGrouper)
	{
		this.userAlterGrouper = userAlterGrouper;
	}

	public String getGrouperType()
	{
		return grouperType;
	}

	public void setGrouperType(String grouperType)
	{
		this.grouperType = grouperType;
	}

	public String getGrouperVersion()
	{
		return grouperVersion;
	}

	public void setGrouperVersion(String grouperVersion)
	{
		this.grouperVersion = grouperVersion;
	}

	public String getUseCMSHASLogin()
	{
		return useCMSHASLogin;
	}

	public void setUseCMSHASLogin(String useCMSHASLogin)
	{
		this.useCMSHASLogin = useCMSHASLogin;
	}

	public String getCalculateAPCForAllClaimDispositions()
	{
		return calculateAPCForAllClaimDispositions;
	}

	public void setCalculateAPCForAllClaimDispositions(String calculateAPCForAllClaimDispositions)
	{
		this.calculateAPCForAllClaimDispositions = calculateAPCForAllClaimDispositions;
	}

	public String getExcludeAllFeeSchedulePricing()
	{
		return excludeAllFeeSchedulePricing;
	}

	public void setExcludeAllFeeSchedulePricing(String excludeAllFeeSchedulePricing)
	{
		this.excludeAllFeeSchedulePricing = excludeAllFeeSchedulePricing;
	}

	public String getPricerType()
	{
		return pricerType;
	}

	public void setPricerType(String pricerType)
	{
		this.pricerType = pricerType;
	}

	public String getPricerValue()
	{
		return pricerValue;
	}

	public void setPricerValue(String pricerValue)
	{
		this.pricerValue = pricerValue;
	}

	public String getRate()
	{
		return rate;
	}

	public void setRate(String rate)
	{
		this.rate = rate;
	}

	public String getSimplePricing()
	{
		return simplePricing;
	}

	public void setSimplePricing(String simplePricing)
	{
		this.simplePricing = simplePricing;
	}

	public String getUseCMS24Reimbursement()
	{
		return useCMS24Reimbursement;
	}

	public void setUseCMS24Reimbursement(String useCMS24Reimbursement)
	{
		this.useCMS24Reimbursement = useCMS24Reimbursement;
	}

	public String getDrgPrepopulate()
	{
		return drgPrepopulate;
	}

	public void setDrgPrepopulate(String drgPrepopulate)
	{
		this.drgPrepopulate = drgPrepopulate;
	}

	public Integer getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public String getUseCahLogic()
	{
		return useCahLogic;
	}

	public void setUseCahLogic(String useCahLogic)
	{
		this.useCahLogic = useCahLogic;
	}

	public Date getUpdateDTS()
	{
		return updateDTS;
	}

	public void setUpdateDTS(Date updateDTS)
	{
		this.updateDTS = updateDTS;
	}

	public Date getCreatorDTS()
	{
		return creatorDTS;
	}

	public void setCreatorDTS(Date creatorDTS)
	{
		this.creatorDTS = creatorDTS;
	}

	public Integer getUpdateUserId()
	{
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId)
	{
		this.updateUserId = updateUserId;
	}

	public Integer getPatientClassId()
	{
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId)
	{
		this.patientClassId = patientClassId;
	}

	public Integer getCreatorUserId()
	{
		return creatorUserId;
	}

	public void setCreatorUserId(Integer creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	public Integer getActiveRecord()
	{
		return activeRecord;
	}

	public void setActiveRecord(Integer activeRecord)
	{
		this.activeRecord = activeRecord;
	}

	public Integer getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(Integer isDefault)
	{
		this.isDefault = isDefault;
	}

	public String getAprAdmitLogic()
	{
		return aprAdmitLogic;
	}

	public void setAprAdmitLogic(String aprAdmitLogic)
	{
		this.aprAdmitLogic = aprAdmitLogic;
	}

	public String getPricerDescription()
	{
		return pricerDescription;
	}

	public void setPricerDescription(String pricerDescription)
	{
		this.pricerDescription = pricerDescription;
	}

	public String getCodingSystem()
	{
		return codingSystem;
	}

	public void setCodingSystem(String codingSystem)
	{
		this.codingSystem = codingSystem;
	}

	public Integer getUserRoleUserId()
	{
		return userRoleUserId;
	}

	public void setUserRoleUserId(Integer userRoleUserId)
	{
		this.userRoleUserId = userRoleUserId;
	}

	public String getPatientClassCode()
	{
		return patientClassCode;
	}

	public void setPatientClassCode(String patientClassCode)
	{
		this.patientClassCode = patientClassCode;
	}

	public String getPatientClassDesc()
	{
		return patientClassDesc;
	}

	public void setPatientClassDesc(String patientClassDesc)
	{
		this.patientClassDesc = patientClassDesc;
	}
}
