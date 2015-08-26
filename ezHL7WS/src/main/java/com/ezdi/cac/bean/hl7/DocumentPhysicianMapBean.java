package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.DocumentPhysicianMapTableConstant;

@Entity
@Table(name = DocumentPhysicianMapTableConstant.TABLE_NAME)
public class DocumentPhysicianMapBean
{
	@Id
	@GeneratedValue
	@Column(name = DocumentPhysicianMapTableConstant.ID)
	private Integer Id;

	@Column(name = DocumentPhysicianMapTableConstant.DOCUMENT_ID)
	private Integer documentId;

	@Column(name = DocumentPhysicianMapTableConstant.PHYSICIAN_LOGIN_ID)
	private Integer physicianLoginId;

	@Column(name = DocumentPhysicianMapTableConstant.IS_ACTIVE)
	private Integer isActive;

	public Integer getId()
	{
		return Id;
	}

	public void setId(Integer id)
	{
		Id = id;
	}

	public Integer getDocumentId()
	{
		return documentId;
	}

	public void setDocumentId(Integer documentId)
	{
		this.documentId = documentId;
	}

	public Integer getPhysicianLoginId()
	{
		return physicianLoginId;
	}

	public void setPhysicianLoginId(Integer physicianLoginId)
	{
		this.physicianLoginId = physicianLoginId;
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