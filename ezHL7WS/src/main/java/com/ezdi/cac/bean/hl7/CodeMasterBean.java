package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.CodeMasterTableConstant;

@Entity
@Table(name = CodeMasterTableConstant.TABLE_NAME)
public class CodeMasterBean
{
	@Id
	@GeneratedValue
	@Column(name = CodeMasterTableConstant.ID)
	private Integer id;

	@Column(name = CodeMasterTableConstant.CODE)
	private String code;

	@Column(name = CodeMasterTableConstant.DESC)
	private String desc;

	@Column(name = CodeMasterTableConstant.CODE_TYPE)
	private String codeType;

	@Column(name = CodeMasterTableConstant.CODE_TYPE_ID)
	private Integer codeTypeId;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getCodeType()
	{
		return codeType;
	}

	public void setCodeType(String codeType)
	{
		this.codeType = codeType;
	}

	public Integer getCodeTypeId()
	{
		return codeTypeId;
	}

	public void setCodeTypeId(Integer codeTypeId)
	{
		this.codeTypeId = codeTypeId;
	}

}