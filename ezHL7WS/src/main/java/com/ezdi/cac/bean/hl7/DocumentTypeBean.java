package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.DocumentTypeTableConstant;

@Entity
@Table(name = DocumentTypeTableConstant.TABLE_NAME)
public class DocumentTypeBean
{
	@Id
	@GeneratedValue
	@Column(name = DocumentTypeTableConstant.ID)
	private Integer id;

	@Column(name = DocumentTypeTableConstant.NAME)
	private String name;

	@Column(name = DocumentTypeTableConstant.DESC)
	private String desc;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

}