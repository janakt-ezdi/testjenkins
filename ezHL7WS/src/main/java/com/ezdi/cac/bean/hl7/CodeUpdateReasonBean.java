package com.ezdi.cac.bean.hl7;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ezdi.cac.constant.table.CodeUpdateReasonTableConstant;

@Entity
@Table(name = CodeUpdateReasonTableConstant.TABLE_NAME)
public class CodeUpdateReasonBean
{

	@Id
	@GeneratedValue
	@Column(name = CodeUpdateReasonTableConstant.ID)
	private Integer id;

	@Column(name = CodeUpdateReasonTableConstant.REASON)
	private String reason;

	@Column(name = CodeUpdateReasonTableConstant.REASON_DETAIL)
	private String reasonDetail;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getReasonDetail()
	{
		return reasonDetail;
	}

	public void setReasonDetail(String reasonDetail)
	{
		this.reasonDetail = reasonDetail;
	}

	@Override
	public String toString()
	{
		return "CodeUpdateReasonBean [id=" + id + ", reason=" + reason + ", reasonDetail=" + reasonDetail + "]";
	}

}
