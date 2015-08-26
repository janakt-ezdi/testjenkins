package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterPhysicianTableConstant;

@Entity
@Table(name = EncounterPhysicianTableConstant.TABLE_NAME)
public class EncounterPhysicianBean
{
	@Id
	@GeneratedValue
	@Column(name = EncounterPhysicianTableConstant.ID)
	private Integer id;

	@Column(name = EncounterPhysicianTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = EncounterPhysicianTableConstant.PHYSICIAN_LOGIN_ID)
	private Integer physicianLoginId;

	@Column(name = EncounterPhysicianTableConstant.PHYSICIAN_TYPE_CODE)
	private String physicianTypeCode;

	@Column(name = EncounterPhysicianTableConstant.IS_ACTIVE)
	private Integer isActive;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getEncounterId()
	{
		return encounterId;
	}

	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}

	public Integer getPhysicianLoginId()
	{
		return physicianLoginId;
	}

	public void setPhysicianLoginId(Integer physicianLoginId)
	{
		this.physicianLoginId = physicianLoginId;
	}

	public String getPhysicianTypeCode()
	{
		return physicianTypeCode;
	}

	public void setPhysicianTypeCode(String physicianTypeCode)
	{
		this.physicianTypeCode = physicianTypeCode;
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
