package com.ezdi.cac.dao.master;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class MasterBaseDAO
{

	@Resource(name = "sessionFactory")
	protected SessionFactory sessionFactory;

	public Session getCurrentSession()
	{
		return sessionFactory.getCurrentSession();
	}

	public Session openSession()
	{
		return sessionFactory.openSession();
	}

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public SQLQuery getHibernateQuery(final String sqlQuery)
	{
		return getCurrentSession().createSQLQuery(sqlQuery);
	}
}
