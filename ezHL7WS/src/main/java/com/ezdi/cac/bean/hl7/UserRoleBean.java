package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.UserRoleTableConstant;

@Entity
@Table(name = UserRoleTableConstant.TABLE_NAME)
public class UserRoleBean
{
	@Id
	@GeneratedValue
	@Column(name = UserRoleTableConstant.ID)
	private Integer id;

	@Column(name = UserRoleTableConstant.CODE)
	private String code;
	
	@Column(name = UserRoleTableConstant.ACTIVERECORD)
	private Integer activeRecord;

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

	public Integer getActiveRecord()
	{
		return activeRecord;
	}

	public void setActiveRecord(Integer activeRecord)
	{
		this.activeRecord = activeRecord;
	}

	
}
