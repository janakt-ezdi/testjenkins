package com.ezdi.cac.bean.hl7;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.DischargeDispositionTableConstant;

@Entity
@Table(name = DischargeDispositionTableConstant.TABLE_NAME)
public class DischargeDispositionBean
{
	@Id
	@GeneratedValue
	@Column(name = DischargeDispositionTableConstant.ID)
	private Integer id;

	@Column(name = DischargeDispositionTableConstant.CODE)
	private String code;

	@Column(name = DischargeDispositionTableConstant.DESC)
	private String desc;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dischargeDispositionBean")
	private List<EncounterDischargeDispositionMapBean> encounterDischargeDispositionMapBeanList;

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

	public List<EncounterDischargeDispositionMapBean> getEncounterDischargeDispositionMapBeanList()
	{
		return encounterDischargeDispositionMapBeanList;
	}

	public void setEncounterDischargeDispositionMapBeanList(List<EncounterDischargeDispositionMapBean> encounterDischargeDispositionMapBeanList)
	{
		this.encounterDischargeDispositionMapBeanList = encounterDischargeDispositionMapBeanList;
	}

}
