package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.CodeTypeTableConstant;

@Entity
@Table(name = CodeTypeTableConstant.TABLE_NAME)
public class CodeTypeBean
{
	@Id
	@GeneratedValue
	@Column(name = CodeTypeTableConstant.ID)
	private Integer id;

	@Column(name = CodeTypeTableConstant.CODE)
	private String code;

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

}
