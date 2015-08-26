package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterStatusTableConstant;

@Entity
@Table(name = EncounterStatusTableConstant.TABLE_NAME)
public class EncounterStatusBean
{

	@Id
	@Column(name = EncounterStatusTableConstant.ENCOUNTER_STATUS_ID)
	private Integer encounterStatusId;

	@Column(name = EncounterStatusTableConstant.ENCOUNTER_STATUS_CODE)
	private String encounterStatusCode;

	@Column(name = EncounterStatusTableConstant.ENCOUNTER_STATUS_DESC)
	private String encounterStatusDesc;

	@Column(name = EncounterStatusTableConstant.PARENT_ENCOUNTER_STATUS_ID)
	private Integer parentEncounterStatusId;

	public Integer getEncounterStatusId()
	{
		return encounterStatusId;
	}

	public void setEncounterStatusId(Integer encounterStatusId)
	{
		this.encounterStatusId = encounterStatusId;
	}

	public String getEncounterStatusCode()
	{
		return encounterStatusCode;
	}

	public void setEncounterStatusCode(String encounterStatusCode)
	{
		this.encounterStatusCode = encounterStatusCode;
	}

	public String getEncounterStatusDesc()
	{
		return encounterStatusDesc;
	}

	public void setEncounterStatusDesc(String encounterStatusDesc)
	{
		this.encounterStatusDesc = encounterStatusDesc;
	}

	public Integer getParentEncounterStatusId()
	{
		return parentEncounterStatusId;
	}

	public void setParentEncounterStatusId(Integer parentEncounterStatusId)
	{
		this.parentEncounterStatusId = parentEncounterStatusId;
	}
}
