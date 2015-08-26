package com.ezdi.cac.bean.hl7;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.EncounterTableConstant;
import com.ezdi.cac.constant.table.PatientTableConstant;

@Entity
@Table(name = PatientTableConstant.TABLE_NAME)
public class PatientBean
{
	@Id
	@GeneratedValue
	@Column(name = PatientTableConstant.ID)
	private Integer id;

	@Column(name = PatientTableConstant.MRN)
	private String mrn;

	@Column(name = PatientTableConstant.FIRST_NAME)
	private String firstName;

	@Column(name = PatientTableConstant.LAST_NAME)
	private String lastName;

	@Column(name = PatientTableConstant.DATE_OF_BIRTH)
	private Date dateOfBirth;

	@Column(name = PatientTableConstant.GENDER)
	private String gender;

	@Column(name = EncounterTableConstant.CREATOR_USER_ID)
	private Integer creatorUserId;

	@Column(name = EncounterTableConstant.CREATION_DATE_TIME)
	private Date creatorDateTime;

	@Column(name = EncounterTableConstant.UPDATER_USER_ID)
	private Integer updaterUserId;

	@Column(name = EncounterTableConstant.UPDATION_DATE_TIME)
	private Date updationDateTime;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getMrn()
	{
		return mrn;
	}

	public void setMrn(String mrn)
	{
		this.mrn = mrn;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public Integer getCreatorUserId()
	{
		return creatorUserId;
	}

	public void setCreatorUserId(Integer creatorUserId)
	{
		this.creatorUserId = creatorUserId;
	}

	public Date getCreatorDateTime()
	{
		return creatorDateTime;
	}

	public void setCreatorDateTime(Date creatorDateTime)
	{
		this.creatorDateTime = creatorDateTime;
	}

	public Integer getUpdaterUserId()
	{
		return updaterUserId;
	}

	public void setUpdaterUserId(Integer updaterUserId)
	{
		this.updaterUserId = updaterUserId;
	}

	public Date getUpdationDateTime()
	{
		return updationDateTime;
	}

	public void setUpdationDateTime(Date updationDateTime)
	{
		this.updationDateTime = updationDateTime;
	}

}
