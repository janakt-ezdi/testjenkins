package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.PayerTableConstant;

@Entity
@Table(name = PayerTableConstant.TABLE_NAME)
public class PayerBean
{

	@Id
	@GeneratedValue
	@Column(name = PayerTableConstant.ID)
	private Integer id;

	@Column(name = PayerTableConstant.CODE)
	private String code;

	@Column(name = PayerTableConstant.DESC)
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
