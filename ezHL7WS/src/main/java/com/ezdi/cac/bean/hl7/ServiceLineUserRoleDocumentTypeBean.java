package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.ServiceLineUserRoleDocumentTypeTableConstant;

@Entity
@Table(name = ServiceLineUserRoleDocumentTypeTableConstant.TABLE_NAME)
public class ServiceLineUserRoleDocumentTypeBean
{

	@Id
	@GeneratedValue
	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.ID)
	private Integer id;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.DOCUMENT_TYPE_ID)
	private Integer documentTypeId;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.DOCUMENT_TYPE_NAME)
	private String documentTypeName;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.DOCUMENT_TYPE_DISPLAY_NAME)
	private String documentTypeDisplayName;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.PATIENT_CLASS)
	private Integer patientClassId;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.PATIENT_CLASS_CODE)
	private String patientClassCode;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.SERVICE_LINE_ID)
	private Integer serviceLineId;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.SERVICE_LINE_CODE)
	private String serviceLineCode;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.USER_ROLE_ID)
	private Integer userRoleId;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.USER_ROLE_NAME)
	private String userRoleName;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.SEQUENCE_NUMBER)
	private Integer sequenceNumber;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.IS_ACTIVE)
	private Integer isActive;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.IS_REQUIRED_DOCUMENT_TYPE)
	private Integer isRequiredDocumentType;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.CREATION_DATE_TIME)
	private Date creatorDateTime;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.UPDATER_USER_ID)
	private Integer updaterUserId;

	@Column(name = ServiceLineUserRoleDocumentTypeTableConstant.UPDATION_DATE_TIME)
	private Date updationDateTime;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getDocumentTypeId()
	{
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId)
	{
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentTypeName()
	{
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName)
	{
		this.documentTypeName = documentTypeName;
	}

	public String getDocumentTypeDisplayName()
	{
		return documentTypeDisplayName;
	}

	public void setDocumentTypeDisplayName(String documentTypeDisplayName)
	{
		this.documentTypeDisplayName = documentTypeDisplayName;
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

	public Integer getUserRoleId()
	{
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId)
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

	public Integer getSequenceNumber()
	{
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	public Integer getIsActive()
	{
		return isActive;
	}

	public void setIsActive(Integer isActive)
	{
		this.isActive = isActive;
	}

	public Integer getIsRequiredDocumentType()
	{
		return isRequiredDocumentType;
	}

	public void setIsRequiredDocumentType(Integer isRequiredDocumentType)
	{
		this.isRequiredDocumentType = isRequiredDocumentType;
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

}