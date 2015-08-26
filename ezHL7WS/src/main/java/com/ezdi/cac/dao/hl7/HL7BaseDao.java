package com.ezdi.cac.dao.hl7;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class HL7BaseDao
{

	@Resource(name = "tenantSessionFactory")
	protected SessionFactory tenantSessionFactory;

	public Session getCurrentSession()
	{
		return tenantSessionFactory.getCurrentSession();
	}

}
