package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.LocationTableConstant;

@Entity
@Table(name = LocationTableConstant.TABLE_NAME)
public class LocationBean
{
	@Id
	@GeneratedValue
	@Column(name = LocationTableConstant.ID)
	private Integer id;

	@Column(name = LocationTableConstant.CODE)
	private String code;

	@Column(name = LocationTableConstant.DESC)
	private String desc;
	
	@Column(name = LocationTableConstant.IS_ACTIVE)
	private Integer isActive;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
