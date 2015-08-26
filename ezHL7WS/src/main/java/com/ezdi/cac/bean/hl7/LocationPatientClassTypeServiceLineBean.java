package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.LocationPatientClassTypeServiceLineTableConstant;

@Entity
@Table(name = LocationPatientClassTypeServiceLineTableConstant.TABLE_NAME)
public class LocationPatientClassTypeServiceLineBean
{
	@Id
	@GeneratedValue
	@Column(name = LocationPatientClassTypeServiceLineTableConstant.ID)
	private Integer id;

	@Column(name = LocationPatientClassTypeServiceLineTableConstant.LOCATION_PATIENT_CLASS_ID)
	private Integer locationPatientClassId;

	@Column(name = LocationPatientClassTypeServiceLineTableConstant.PATIENT_TYPE_ID)
	private Integer patientTypeId;

	@Column(name = LocationPatientClassTypeServiceLineTableConstant.SERVICE_LINE_ID)
	private Integer serviceLineId;

	@Column(name = LocationPatientClassTypeServiceLineTableConstant.IS_ACTIVE)
	private Integer isActive;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getLocationPatientClassId()
	{
		return locationPatientClassId;
	}

	public void setLocationPatientClassId(Integer locationPatientClassId)
	{
		this.locationPatientClassId = locationPatientClassId;
	}

	public Integer getPatientTypeId()
	{
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId)
	{
		this.patientTypeId = patientTypeId;
	}

	public Integer getServiceLineId()
	{
		return serviceLineId;
	}

	public void setServiceLineId(Integer serviceLineId)
	{
		this.serviceLineId = serviceLineId;
	}

	public Integer getIsActive()
	{
		return isActive;
	}

	public void setIsActive(Integer isActive)
	{
		this.isActive = isActive;
	}

}
