package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.PatientTypeTableConstant;

@Entity
@Table(name = PatientTypeTableConstant.TABLE_NAME)
public class PatientTypeBean
{
	@Id
	@GeneratedValue
	@Column(name = PatientTypeTableConstant.ID)
	private Integer id;

	@Column(name = PatientTypeTableConstant.CODE)
	private String code;

	@Column(name = PatientTypeTableConstant.DESC)
	private String desc;

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

}
