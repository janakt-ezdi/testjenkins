package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterDischargeDispositionMapTableConstant;

@Entity
@Table(name = EncounterDischargeDispositionMapTableConstant.TABLE_NAME)
public class EncounterDischargeDispositionMapBean
{
	@EmbeddedId
	private EncounterDischargeDispositionMapPK encounterDischargeDispositionMap;

	@Column(name = EncounterDischargeDispositionMapTableConstant.DISCHARGE_DISPOSITION_ID)
	private Integer dischargeDispositionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EncounterDischargeDispositionMapTableConstant.ENCOUNTER_ID, nullable = false, insertable = false, updatable = false)
	private EncounterBean encounterBean;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EncounterDischargeDispositionMapTableConstant.DISCHARGE_DISPOSITION_ID, nullable = false, insertable = false, updatable = false)
	private DischargeDispositionBean dischargeDispositionBean;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = EncounterDischargeDispositionMapTableConstant.USER_ROLE_USER_ID, nullable = false, insertable = false, updatable = false)
	private UserRoleUserMapBean userRoleUserMapBean;

	public EncounterDischargeDispositionMapPK getEncounterDischargeDispositionMap()
	{
		return encounterDischargeDispositionMap;
	}

	public void setEncounterDischargeDispositionMap(EncounterDischargeDispositionMapPK encounterDischargeDispositionMap)
	{
		this.encounterDischargeDispositionMap = encounterDischargeDispositionMap;
	}

	public Integer getDischargeDispositionId()
	{
		return dischargeDispositionId;
	}

	public void setDischargeDispositionId(Integer dischargeDispositionId)
	{
		this.dischargeDispositionId = dischargeDispositionId;
	}

	public EncounterBean getEncounterBean()
	{
		return encounterBean;
	}

	public void setEncounterBean(EncounterBean encounterBean)
	{
		this.encounterBean = encounterBean;
	}

	public DischargeDispositionBean getDischargeDispositionBean()
	{
		return dischargeDispositionBean;
	}

	public void setDischargeDispositionBean(DischargeDispositionBean dischargeDispositionBean)
	{
		this.dischargeDispositionBean = dischargeDispositionBean;
	}

	public UserRoleUserMapBean getUserRoleUserMapBean()
	{
		return userRoleUserMapBean;
	}

	public void setUserRoleUserMapBean(UserRoleUserMapBean userRoleUserMapBean)
	{
		this.userRoleUserMapBean = userRoleUserMapBean;
	}

}