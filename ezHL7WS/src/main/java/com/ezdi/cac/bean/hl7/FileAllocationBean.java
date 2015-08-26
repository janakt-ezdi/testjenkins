package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.FileAllocationTableConstant;

@Entity
@Table(name = FileAllocationTableConstant.TABLE_NAME)
public class FileAllocationBean
{
	@Id
	@GeneratedValue
	@Column(name = FileAllocationTableConstant.ID)
	private Integer id;

	@Column(name = FileAllocationTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = FileAllocationTableConstant.CODER_ID)
	private Integer coderId;

	@Column(name = FileAllocationTableConstant.CODER_ENCOUNTER_STATUS)
	private String coderEncounterStatus;

	@Column(name = FileAllocationTableConstant.REVIEWER_ENCOUNTER_STATUS)
	private String reviewerEncounterStatus;

	@Column(name = FileAllocationTableConstant.REVIEWER_ID)
	private Integer reviewerId;

	@Column(name = FileAllocationTableConstant.REVIEWER_FIRSTNAME)
	private Integer reviewerfirstname;

	@Column(name = FileAllocationTableConstant.REVIEWER_LASTNAME)
	private Integer reviewerlastname;

	@Column(name = FileAllocationTableConstant.REVIEWERSTATUS_ID)
	private Integer reviewerStatusId;

	@Column(name = FileAllocationTableConstant.CDI_ENCOUNTER_STATUS)
	private String cdiEncounterStatus;

	@Column(name = FileAllocationTableConstant.UPDATE_DTS)
	private Date updatedDTS;

	@Column(name = FileAllocationTableConstant.UPDATE_USER_ID)
	private Integer updateUserId;

	@Column(name = FileAllocationTableConstant.LAST_UPDATED_ENCOUNTER_STATUS)
	private String lastUpdatedEncounterStatusCode;

	@Column(name = FileAllocationTableConstant.LAST_UPDATED_ROLE)
	private String lastUpdatedRole;

	public Date getUpdatedDTS()
	{
		return updatedDTS;
	}

	public void setUpdatedDTS(Date updatedDTS)
	{
		this.updatedDTS = updatedDTS;
	}

	public Integer getUpdateUserId()
	{
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId)
	{
		this.updateUserId = updateUserId;
	}

	public String getLastUpdatedEncounterStatusCode()
	{
		return lastUpdatedEncounterStatusCode;
	}

	public void setLastUpdatedEncounterStatusCode(String lastUpdatedEncounterStatusCode)
	{
		this.lastUpdatedEncounterStatusCode = lastUpdatedEncounterStatusCode;
	}

	public String getLastUpdatedRole()
	{
		return lastUpdatedRole;
	}

	public void setLastUpdatedRole(String lastUpdatedRole)
	{
		this.lastUpdatedRole = lastUpdatedRole;
	}

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

	public String getCoderEncounterStatus()
	{
		return coderEncounterStatus;
	}

	public void setCoderEncounterStatus(String coderEncounterStatus)
	{
		this.coderEncounterStatus = coderEncounterStatus;
	}

	public String getReviewerEncounterStatus()
	{
		return reviewerEncounterStatus;
	}

	public void setReviewerEncounterStatus(String reviewerEncounterStatus)
	{
		this.reviewerEncounterStatus = reviewerEncounterStatus;
	}

	public String getCdiEncounterStatus()
	{
		return cdiEncounterStatus;
	}

	public void setCdiEncounterStatus(String cdiEncounterStatus)
	{
		this.cdiEncounterStatus = cdiEncounterStatus;
	}

	public Integer getCoderId()
	{
		return coderId;
	}

	public void setCoderId(Integer coderId)
	{
		this.coderId = coderId;
	}

	public Integer getReviewerId()
	{
		return reviewerId;
	}

	public void setReviewerId(Integer reviewerId)
	{
		this.reviewerId = reviewerId;
	}
}
