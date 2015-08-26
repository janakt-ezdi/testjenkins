package com.ezdi.cac.bean.hl7;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.LocationPatientClassTableConstant;

@Entity
@Table(name = LocationPatientClassTableConstant.TABLE_NAME)
public class LocationPatientClassBean
{
	@Id
	@GeneratedValue
	@Column(name = LocationPatientClassTableConstant.ID)
	private Integer id;

	@Column(name = LocationPatientClassTableConstant.LOCATION_ID)
	private Integer locationId;

	@Column(name = LocationPatientClassTableConstant.PATIENT_CLASS_ID)
	private Integer patientClassId;
	
	@Column(name = LocationPatientClassTableConstant.PATIENT_CLASS_CODE)
	private String patientClassCode;
	
	@Column(name = LocationPatientClassTableConstant.IS_ACTIVE)
	private Integer isActive;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationPatientClassBean")
	private List<UserGroupLocationPatientClassMapBean> userGroupLocationPatientClassMapBeanList;
		 
	

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getLocationId()
	{
		return locationId;
	}

	public void setLocationId(Integer locationId)
	{
		this.locationId = locationId;
	}

	public Integer getPatientClassId()
	{
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId)
	{
		this.patientClassId = patientClassId;
	}

	public String getPatientClassCode() {
		return patientClassCode;
	}

	public void setPatientClassCode(String patientClassCode) {
		this.patientClassCode = patientClassCode;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public List<UserGroupLocationPatientClassMapBean> getUserGroupLocationPatientClassMapBeanList() {
		return userGroupLocationPatientClassMapBeanList;
	}

	public void setUserGroupLocationPatientClassMapBeanList(
			List<UserGroupLocationPatientClassMapBean> userGroupLocationPatientClassMapBeanList) {
		this.userGroupLocationPatientClassMapBeanList = userGroupLocationPatientClassMapBeanList;
	}

}
