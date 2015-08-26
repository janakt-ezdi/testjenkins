package com.ezdi.cac.bean.hl7;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.ezdi.cac.constant.table.WorkListConstant;

@Entity
@Table(name = WorkListConstant.TABLE_NAME)
public class WorkListBean implements Cloneable
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = WorkListConstant.ID)
	@GeneratedValue
	private Integer id;

	@Column(name = WorkListConstant.LOCATIONID)
	private Integer locationId;

	@Column(name = WorkListConstant.LOCATIONNAME)
	private String locationName;

	@Column(name = WorkListConstant.SERVICELINEID)
	private Integer serviceLineId;

	@Column(name = WorkListConstant.SERVICELINECODE)
	private String serviceLineCode;

	@Column(name = WorkListConstant.PAYERID)
	private Integer payerId;

	@Column(name = WorkListConstant.PAYERNAME)
	private String payerName;

	@Column(name = WorkListConstant.WORKLISTNAME)
	private String workListName;

	@Column(name = WorkListConstant.CONDITIONS, columnDefinition = "LONGVARCHAR")
	private String conditions;

	@Column(name = WorkListConstant.CREATORUSERID)
	private Integer creatorUserId;

	@Column(name = WorkListConstant.CREATORDTS)
	private Date creatorDTS;

	@Column(name = WorkListConstant.UPDATEUSERID)
	private Integer updateUserId;

	@Column(name = WorkListConstant.UPDATEDTS)
	private Date updateDTS;

	@Column(name = WorkListConstant.PATIENT_CLASS_ID)
	private Integer patientClassId;

	@Column(name = WorkListConstant.PATIENT_CLASS_CODE)
	private String patientClassCode;

	@Column(name = WorkListConstant.PATIENT_TYPE_ID)
	private Integer patientTypeId;

	@Column(name = WorkListConstant.PATIENT_TYPE_CODE)
	private String patientTypeCode;

	@Column(name = WorkListConstant.ISACTIVE, columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean active;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "encounterWorkListMapWorkList")
	private List<EncounterWorkListMapBean> encounterWorkListMapWorkList;

	/**
	 * @return the id
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId()
	{
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setLocationId(Integer locationId)
	{
		this.locationId = locationId;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName()
	{
		return locationName;
	}

	/**
	 * @param locationName
	 *            the locationName to set
	 */
	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}

	/**
	 * @return the serviceLineId
	 */
	public Integer getServiceLineId()
	{
		return serviceLineId;
	}

	/**
	 * @param serviceLineId
	 *            the serviceLineId to set
	 */
	public void setServiceLineId(Integer serviceLineId)
	{
		this.serviceLineId = serviceLineId;
	}

	/**
	 * @return the serviceLineCode
	 */
	public String getServiceLineCode()
	{
		return serviceLineCode;
	}

	/**
	 * @param serviceLineCode
	 *            the serviceLineCode to set
	 */
	public void setServiceLineCode(String serviceLineCode)
	{
		this.serviceLineCode = serviceLineCode;
	}

	/**
	 * @return the payerId
	 */
	public Integer getPayerId()
	{
		return payerId;
	}

	/**
	 * @param payerId
	 *            the payerId to set
	 */
	public void setPayerId(Integer payerId)
	{
		this.payerId = payerId;
	}

	/**
	 * @return the payerName
	 */
	public String getPayerName()
	{
		return payerName;
	}

	/**
	 * @param payerName
	 *            the payerName to set
	 */
	public void setPayerName(String payerName)
	{
		this.payerName = payerName;
	}

	/**
	 * @return the workListName
	 */
	public String getWorkListName()
	{
		return workListName;
	}

	/**
	 * @param workListName
	 *            the workListName to set
	 */
	public void setWorkListName(String workListName)
	{
		this.workListName = workListName;
	}

	/**
	 * @return the conditions
	 */
	public String getConditions()
	{
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(String conditions)
	{
		this.conditions = conditions;
	}

	/**
	 * @return the creatorUserId
	 */
	public Integer getCreatorUserId()
	{
		return creatorUserId;
	}

	/**
	 * @param creatorUserId
	 *            the creatorUserId to set
	 */
	public void setCreatorUserId(Integer creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	/**
	 * @return the creatorDTS
	 */
	public Date getCreatorDTS()
	{
		return creatorDTS;
	}

	/**
	 * @param creatorDTS
	 *            the creatorDTS to set
	 */
	public void setCreatorDTS(Date creatorDTS)
	{
		this.creatorDTS = creatorDTS;
	}

	/**
	 * @return the updateUserId
	 */
	public Integer getUpdateUserId()
	{
		return updateUserId;
	}

	/**
	 * @param updateUserId
	 *            the updateUserId to set
	 */
	public void setUpdateUserId(Integer updateUserId)
	{
		this.updateUserId = updateUserId;
	}

	/**
	 * @return the updateDTS
	 */
	public Date getUpdateDTS()
	{
		return updateDTS;
	}

	/**
	 * @param updateDTS
	 *            the updateDTS to set
	 */
	public void setUpdateDTS(Date updateDTS)
	{
		this.updateDTS = updateDTS;
	}

	/**
	 * @return the active
	 */
	public Boolean isActive()
	{
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active)
	{
		this.active = active;
	}

	public List<EncounterWorkListMapBean> getEncounterWorkListMapWorkList()
	{
		return encounterWorkListMapWorkList;
	}

	public void setEncounterWorkListMapWorkList(List<EncounterWorkListMapBean> encounterWorkListMapWorkList)
	{
		this.encounterWorkListMapWorkList = encounterWorkListMapWorkList;
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

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

}
