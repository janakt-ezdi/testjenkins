package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterObservationRequestTableConstant;

@Entity
@Table(name = EncounterObservationRequestTableConstant.TABLE_NAME)
public class EncounterObservationRequestBean
{

	@Id
	@GeneratedValue
	@Column(name = EncounterObservationRequestTableConstant.ID)
	private Integer id;

	@Column(name = EncounterObservationRequestTableConstant.ENCOUNTER_ID)
	private Integer encounterId;

	@Column(name = EncounterObservationRequestTableConstant.ENCOUNTER_NUMBER)
	private String encounterNumber;

	@Column(name = EncounterObservationRequestTableConstant.DOCUMENT_ID)
	private Integer documentId;

	@Column(name = EncounterObservationRequestTableConstant.PLACER_ORDER_NUMBER)
	private String placerOrderNumber;

	@Column(name = EncounterObservationRequestTableConstant.UNIVERSAL_SERVICE_ID)
	private String universalServiceId;

	@Column(name = EncounterObservationRequestTableConstant.OBSERVATION_DATE_TIME)
	private Date observationDateTime;

	@Column(name = EncounterObservationRequestTableConstant.ORDERING_PROVIDER)
	private String orderingProvider;

	@Column(name = EncounterObservationRequestTableConstant.RESULT_STATUS)
	private String resultStatus;

	@Column(name = EncounterObservationRequestTableConstant.REASON_FOR_STUDY)
	private String reasonForStudy;

	@Column(name = EncounterObservationRequestTableConstant.COMMENT)
	private String comment;

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

	public String getEncounterNumber()
	{
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber)
	{
		this.encounterNumber = encounterNumber;
	}

	public Integer getDocumentId()
	{
		return documentId;
	}

	public void setDocumentId(Integer documentId)
	{
		this.documentId = documentId;
	}

	public String getPlacerOrderNumber()
	{
		return placerOrderNumber;
	}

	public void setPlacerOrderNumber(String placerOrderNumber)
	{
		this.placerOrderNumber = placerOrderNumber;
	}

	public String getUniversalServiceId()
	{
		return universalServiceId;
	}

	public void setUniversalServiceId(String universalServiceId)
	{
		this.universalServiceId = universalServiceId;
	}

	public Date getObservationDateTime()
	{
		return observationDateTime;
	}

	public void setObservationDateTime(Date observationDateTime)
	{
		this.observationDateTime = observationDateTime;
	}

	public String getOrderingProvider()
	{
		return orderingProvider;
	}

	public void setOrderingProvider(String orderingProvider)
	{
		this.orderingProvider = orderingProvider;
	}

	public String getResultStatus()
	{
		return resultStatus;
	}

	public void setResultStatus(String resultStatus)
	{
		this.resultStatus = resultStatus;
	}

	public String getReasonForStudy()
	{
		return reasonForStudy;
	}

	public void setReasonForStudy(String reasonForStudy)
	{
		this.reasonForStudy = reasonForStudy;
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
