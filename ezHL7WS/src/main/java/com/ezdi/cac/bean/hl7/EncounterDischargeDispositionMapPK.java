package com.ezdi.cac.bean.hl7;

import java.io.Serializable;

import javax.persistence.Column;

import com.ezdi.cac.constant.table.EncounterDischargeDispositionMapTableConstant;

public class EncounterDischargeDispositionMapPK implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(name = EncounterDischargeDispositionMapTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = EncounterDischargeDispositionMapTableConstant.USER_ROLE_USER_ID)
	private Integer userRoleUserId;

	public Integer getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public Integer getUserRoleUserId()
	{
		return userRoleUserId;
	}

	public void setUserRoleUserId(final Integer userHospitalId)
	{
		this.userRoleUserId = userHospitalId;
	}

}
