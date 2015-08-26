package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.RoleWiseEncounterStatusTableConstant;

@Entity
@Table(name = RoleWiseEncounterStatusTableConstant.TABLE_NAME)
public class RoleWiseEncounterStatusBean
{
	@Id
	@GeneratedValue
	@Column(name = RoleWiseEncounterStatusTableConstant.ID)
	private Integer id;

	@Column(name = RoleWiseEncounterStatusTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = RoleWiseEncounterStatusTableConstant.USER_ROLE_ID)
	private Integer userRoleId;

	@Column(name = RoleWiseEncounterStatusTableConstant.USER_ROLE_NAME)
	private String userRoleName;

	@Column(name = RoleWiseEncounterStatusTableConstant.STATUS)
	private String status;

	@Column(name = RoleWiseEncounterStatusTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = RoleWiseEncounterStatusTableConstant.CREATION_DATE_TIME)
	private Date creationDateTime;

	@Column(name = RoleWiseEncounterStatusTableConstant.UPDATOR_USER_ID)
	private Integer updatotUserId;

	@Column(name = RoleWiseEncounterStatusTableConstant.UPDATION_DATE_TIME)
	private Date updationDateTime;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(int encounterId)
	{
		this.encounterId = encounterId;
	}

	public int getUserRoleId()
	{
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId)
	{
		this.userRoleId = userRoleId;
	}

	public String getUserRoleName()
	{
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName)
	{
		this.userRoleName = userRoleName;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getCreatorUserId()
	{
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	public Date getCreationDateTime()
	{
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime)
	{
		this.creationDateTime = creationDateTime;
	}

	public int getUpdatotUserId()
	{
		return updatotUserId;
	}

	public void setUpdatotUserId(int updatotUserId)
	{
		this.updatotUserId = updatotUserId;
	}

	public Date getUpdationDateTime()
	{
		return updationDateTime;
	}

	public void setUpdationDateTime(Date updationDateTime)
	{
		this.updationDateTime = updationDateTime;
	}

}
