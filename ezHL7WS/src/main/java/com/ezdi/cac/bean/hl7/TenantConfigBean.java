package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.TenantConfigTableConstant;

@Entity
@Table(name = TenantConfigTableConstant.TABLE_NAME)
public class TenantConfigBean
{
	@Id
	@GeneratedValue
	@Column(name = TenantConfigTableConstant.ID)
	private Integer id;

	@Column(name = TenantConfigTableConstant.NAME)
	private String name;

	@Column(name = TenantConfigTableConstant.VALUE)
	private String value;

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

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

}