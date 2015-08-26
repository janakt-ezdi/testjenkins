package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterObservationResultTableConstant;

@Entity
@Table(name = EncounterObservationResultTableConstant.TABLE_NAME)
public class EncounterObservationResultBean
{

	@Id
	@GeneratedValue
	@Column(name = EncounterObservationResultTableConstant.ID)
	private Integer id;

	@Column(name = EncounterObservationResultTableConstant.OBSERVATION_REQUEST_ID)
	private Integer observationRequestId;

	@Column(name = EncounterObservationResultTableConstant.OBSERVATION_IDENTIFIER)
	private String observationIdentifier;

	@Column(name = EncounterObservationResultTableConstant.OBSERVATION_SUB_ID)
	private String observationSubId;

	@Column(name = EncounterObservationResultTableConstant.OBSERVATION_VALUE)
	private String observationValue;

	@Column(name = EncounterObservationResultTableConstant.UNITS)
	private String units;

	@Column(name = EncounterObservationResultTableConstant.REFERENCE_RANGE)
	private String referenceRange;

	@Column(name = EncounterObservationResultTableConstant.ABNORMAL_FLAGS)
	private String abnormalFlags;

	@Column(name = EncounterObservationResultTableConstant.COMMENT)
	private String comment;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getObservationRequestId()
	{
		return observationRequestId;
	}

	public void setObservationRequestId(Integer observationRequestId)
	{
		this.observationRequestId = observationRequestId;
	}

	public String getObservationIdentifier()
	{
		return observationIdentifier;
	}

	public void setObservationIdentifier(String observationIdentifier)
	{
		this.observationIdentifier = observationIdentifier;
	}

	public String getObservationSubId()
	{
		return observationSubId;
	}

	public void setObservationSubId(String observationSubId)
	{
		this.observationSubId = observationSubId;
	}

	public String getObservationValue()
	{
		return observationValue;
	}

	public void setObservationValue(String observationValue)
	{
		this.observationValue = observationValue;
	}

	public String getUnits()
	{
		return units;
	}

	public void setUnits(String units)
	{
		this.units = units;
	}

	public String getReferenceRange()
	{
		return referenceRange;
	}

	public void setReferenceRange(String referenceRange)
	{
		this.referenceRange = referenceRange;
	}

	public String getAbnormalFlags()
	{
		return abnormalFlags;
	}

	public void setAbnormalFlags(String abnormalFlags)
	{
		this.abnormalFlags = abnormalFlags;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

}
