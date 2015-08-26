package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.MasterMapTableConstant;

@Entity
@Table(name = MasterMapTableConstant.TABLE_NAME)
public class MasterMapBean
{
	@Id
	@Column(name = MasterMapTableConstant.MASTER_ID)
	private Long masterId;

	@Column(name = MasterMapTableConstant.DOCUMENT_ID)
	private Integer documentId;

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public Integer getDocumentId()
	{
		return documentId;
	}

	public void setDocumentId(Integer documentId)
	{
		this.documentId = documentId;
	}

}