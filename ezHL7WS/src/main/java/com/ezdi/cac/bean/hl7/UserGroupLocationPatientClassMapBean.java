package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.UserGroupLocationPatientClassMapTableConstant;

@Entity
@Table(name = UserGroupLocationPatientClassMapTableConstant.TABLE_NAME)
public class UserGroupLocationPatientClassMapBean
{

	@Id
	@GeneratedValue
	@Column(name = UserGroupLocationPatientClassMapTableConstant.ID)
	private Integer id;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.USER_LOGIN_ID)
	private Integer User_Login_Id;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.GROUP_ID)
	private Integer Group_Id;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.LOCATION_PATIENT_CLASS_MAP_ID)
	private Integer Location_Patient_Class_Map_Id;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.CREATOR_DTS)
	private Date creatorDTS;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.UPDATER_USER_ID)
	private Integer updaterUserId;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.UPDATION_DTS)
	private Date updationDTS;

	@Column(name = UserGroupLocationPatientClassMapTableConstant.IS_ACTIVE)
	private Integer Is_Active;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UserGroupLocationPatientClassMapTableConstant.LOCATION_PATIENT_CLASS_MAP_ID, nullable = false, insertable = false, updatable = false)
    private LocationPatientClassBean locationPatientClassBean;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser_Login_Id() {
		return User_Login_Id;
	}

	public void setUser_Login_Id(Integer user_Login_Id) {
		User_Login_Id = user_Login_Id;
	}

	public Integer getGroup_Id() {
		return Group_Id;
	}

	public void setGroup_Id(Integer group_Id) {
		Group_Id = group_Id;
	}

	public Integer getLocation_Patient_Class_Map_Id() {
		return Location_Patient_Class_Map_Id;
	}

	public void setLocation_Patient_Class_Map_Id(
			Integer location_Patient_Class_Map_Id) {
		Location_Patient_Class_Map_Id = location_Patient_Class_Map_Id;
	}

	public Integer getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(Integer creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public Date getCreatorDTS() {
		return creatorDTS;
	}

	public void setCreatorDTS(Date creatorDTS) {
		this.creatorDTS = creatorDTS;
	}

	public Integer getUpdaterUserId() {
		return updaterUserId;
	}

	public void setUpdaterUserId(Integer updaterUserId) {
		this.updaterUserId = updaterUserId;
	}

	public Date getUpdationDTS() {
		return updationDTS;
	}

	public void setUpdationDTS(Date updationDTS) {
		this.updationDTS = updationDTS;
	}

	public Integer getIs_Active() {
		return Is_Active;
	}

	public void setIs_Active(Integer is_Active) {
		Is_Active = is_Active;
	}

	public LocationPatientClassBean getLocationPatientClassBean() {
		return locationPatientClassBean;
	}

	public void setLocationPatientClassBean(
			LocationPatientClassBean locationPatientClassBean) {
		this.locationPatientClassBean = locationPatientClassBean;
	}

	
	
	
}
