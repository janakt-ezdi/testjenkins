package com.ezdi.cac.bean.hl7;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.UserRoleUserMapTableConstant;

@Entity
@Table(name = UserRoleUserMapTableConstant.TABLE_NAME)
public class UserRoleUserMapBean
{
	@Id
	@GeneratedValue
	@Column(name = UserRoleUserMapTableConstant.ID)
	private Integer id;

	@Column(name = UserRoleUserMapTableConstant.USER_LOGIN_ID)
	private Integer userLoginId;

	@Column(name = UserRoleUserMapTableConstant.USER_ROLE_ID)
	private Integer userRoleId;

	@Column(name = UserRoleUserMapTableConstant.IS_ACTIVE)
	private Integer isActive;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userRoleUserMapBean")
	private List<EncounterDischargeDispositionMapBean> encounterDischargeDispositionMapBeanList;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getUserLoginId()
	{
		return userLoginId;
	}

	public void setUserLoginId(Integer userLoginId)
	{
		this.userLoginId = userLoginId;
	}

	public Integer getUserRoleId()
	{
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId)
	{
		this.userRoleId = userRoleId;
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
