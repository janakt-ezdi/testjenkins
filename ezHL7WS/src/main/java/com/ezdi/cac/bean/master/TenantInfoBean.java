package com.ezdi.cac.bean.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ezdi.cac.constant.table.master.TenantInfoBeanTableConstant;

@Entity
@Table(name = TenantInfoBeanTableConstant.TABLE_NAME)
public class TenantInfoBean
{
	@Id
	@Column(name = TenantInfoBeanTableConstant.TENANT_ID)
	private Long tenantId;

	@Transient
	private Integer id;

	@Column(name = TenantInfoBeanTableConstant.TENANT_CODE)
	private String tenantCode;

	@Column(name = TenantInfoBeanTableConstant.TENANT_NAME)
	private String tenantName;

	@Column(name = TenantInfoBeanTableConstant.DB_URL)
	private String dbUrl;

	@Column(name = TenantInfoBeanTableConstant.DB_USERNAME)
	private String dbUserName;

	@Column(name = TenantInfoBeanTableConstant.DB_PWD)
	private String dbPassword;

	@Column(name = TenantInfoBeanTableConstant.STATUS)
	private Boolean status;

	@Column(name = TenantInfoBeanTableConstant.DATE_TIME)
	private Date dateTime;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Long getTenantId()
	{
		return tenantId;
	}

	public void setTenantId(Long tenantId)
	{
		this.tenantId = tenantId;
	}

	public String getTenantCode()
	{
		return tenantCode;
	}

	public void setTenantCode(String tenantCode)
	{
		this.tenantCode = tenantCode;
	}

	public String getTenantName()
	{
		return tenantName;
	}

	public void setTenantName(String tenantName)
	{
		this.tenantName = tenantName;
	}

	public String getDbUrl()
	{
		return dbUrl;
	}

	public void setDbUrl(String dbUrl)
	{
		this.dbUrl = dbUrl;
	}

	public String getDbUserName()
	{
		return dbUserName;
	}

	public void setDbUserName(String dbUserName)
	{
		this.dbUserName = dbUserName;
	}

	public void setDbPassword(String dbPassword)
	{
		this.dbPassword = dbPassword;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}

	public Boolean getStatus()
	{
		return status;
	}

	public void setStatus(Boolean status)
	{
		this.status = status;
	}

	public Date getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
	}

}
