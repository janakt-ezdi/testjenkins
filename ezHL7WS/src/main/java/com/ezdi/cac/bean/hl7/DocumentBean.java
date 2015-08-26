package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.DocumentTableConstant;

@Entity
@Table(name = DocumentTableConstant.TABLE_NAME)
public class DocumentBean
{
	@Id
	@GeneratedValue
	@Column(name = DocumentTableConstant.ID)
	private Integer id;

	@Column(name = DocumentTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = DocumentTableConstant.NAME)
	private String name;

	@Column(name = DocumentTableConstant.TYPE_ID)
	private Integer typeId;

	@Column(name = DocumentTableConstant.TYPE_NAME)
	private String typeName;

	@Column(name = DocumentTableConstant.DATE_OF_SERVICE)
	private Date dateOfService;

	@Column(name = DocumentTableConstant.FILE_FILTER)
	private Integer fileFilter;

	@Column(name = DocumentTableConstant.CONTENT_TYPE)
	private String contentType;

	@Column(name = DocumentTableConstant.PARENT_UNIQUE_NUMBER)
	private String parentUniqueNumber;

	@Column(name = DocumentTableConstant.ACTIVE_RECORD_FLAG)
	private Integer activeRecordFlag;

	@Column(name = DocumentTableConstant.ENCOUNTER_NUMBER)
	private String encounterNumber;

	@Column(name = DocumentTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = DocumentTableConstant.CREATION_DATE_TIME)
	private Date creatorDateTime;

	@Column(name = DocumentTableConstant.UPDATER_USER_ID)
	private Integer updaterUserId;

	@Column(name = DocumentTableConstant.UPDATION_DATE_TIME)
	private Date updationDateTime;

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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Integer typeId)
	{
		this.typeId = typeId;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public Date getDateOfService()
	{
		return dateOfService;
	}

	public void setDateOfService(Date dateOfService)
	{
		this.dateOfService = dateOfService;
	}

	public Integer getFileFilter()
	{
		return fileFilter;
	}

	public void setFileFilter(Integer fileFilter)
	{
		this.fileFilter = fileFilter;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getParentUniqueNumber()
	{
		return parentUniqueNumber;
	}

	public void setParentUniqueNumber(String parentUniqueNumber)
	{
		this.parentUniqueNumber = parentUniqueNumber;
	}

	public Integer getActiveRecordFlag()
	{
		return activeRecordFlag;
	}

	public void setActiveRecordFlag(Integer activeRecordFlag)
	{
		this.activeRecordFlag = activeRecordFlag;
	}

	public String getEncounterNumber()
	{
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber)
	{
		this.encounterNumber = encounterNumber;
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
