package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterEventSummaryTableConstant;

@Entity
@Table(name = EncounterEventSummaryTableConstant.TABLE_NAME)
public class EncounterEventSummaryBean
{

	@Id
	@GeneratedValue
	@Column(name = EncounterEventSummaryTableConstant.EVENTID)
	private Integer eventId;

	@Column(name = EncounterEventSummaryTableConstant.ENCOUNTERID)
	private Integer encounterId;

	@Column(name = EncounterEventSummaryTableConstant.USERID)
	private Integer userId;

	@Column(name = EncounterEventSummaryTableConstant.USERROLEID)
	private Integer userRoleId;

	@Column(name = EncounterEventSummaryTableConstant.USERROLE)
	private String userRole;

	@Column(name = EncounterEventSummaryTableConstant.CSSCLASSNAME)
	private String cssClassName;

	@Column(name = EncounterEventSummaryTableConstant.DESCRIPTION)
	private String description;

	@Column(name = EncounterEventSummaryTableConstant.DATETIME)
	private Date dateTime;

	@Column(name = EncounterEventSummaryTableConstant.EVENTTYPE)
	private String eventType;

	public Integer getEventId()
	{
		return eventId;
	}

	public void setEventId(Integer eventId)
	{
		this.eventId = eventId;
	}

	public Integer getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public Integer getUserRoleId()
	{
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId)
	{
		this.userRoleId = userRoleId;
	}

	public String getUserRole()
	{
		return userRole;
	}

	public void setUserRole(String userRole)
	{
		this.userRole = userRole;
	}

	public String getCssClassName()
	{
		return cssClassName;
	}

	public void setCssClassName(String cssClassName)
	{
		this.cssClassName = cssClassName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
	}

	public String getEventType()
	{
		return eventType;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
}
