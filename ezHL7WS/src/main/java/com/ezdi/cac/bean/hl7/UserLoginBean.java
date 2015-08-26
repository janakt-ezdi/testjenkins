package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.UserLoginTableConstant;

@Entity
@Table(name = UserLoginTableConstant.TABLE_NAME)
public class UserLoginBean
{
	@Id
	@GeneratedValue
	@Column(name = UserLoginTableConstant.ID)
	private Integer id;

	@Column(name = UserLoginTableConstant.USER_CODE)
	private String userCode;

	@Column(name = UserLoginTableConstant.GLOBAL_ID)
	private String globalId;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getUserCode()
	{
		return userCode;
	}

	public void setUserCode(String userCode)
	{
		this.userCode = userCode;
	}

	public String getGlobalId()
	{
		return globalId;
	}

	public void setGlobalId(String globalId)
	{
		this.globalId = globalId;
	}

}
