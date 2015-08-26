package com.ezdi.cac.factory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

@Aspect
@Order(0)
public class TenantAdvice
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(TenantAdvice.class);

	@Autowired
	EzCacMultiTenantHibernateTxManager transactionManager;

	@Pointcut("within(@com.ezdi.cac.factory.Master *)")
	public void masterServicePointcut()
	{
	}

	@Pointcut("within(@com.ezdi.cac.factory.Tenant *)")
	public void tenantServicePointcut()
	{
	}

	@Before("masterServicePointcut()")
	public void beforeMasterServiceMethod(JoinPoint jp)
	{
		LOGGER.debug("Inside beforeMasterServiceMethod.");
		transactionManager.setIsTenant(false);
		transactionManager.setTempSessionFactory(transactionManager.getSessionFactory());
		LOGGER.debug("Exiting from beforeMasterServiceMethod.");
	}

	@After("masterServicePointcut()")
	public void afterMasterServiceMethod(JoinPoint jp)
	{
		LOGGER.debug("Inside afterMasterServiceMethod.");
		transactionManager.setIsTenant(true);
		transactionManager.setTempSessionFactory(transactionManager.getTenantSessionFactory());
		LOGGER.debug("Exiting from afterMasterServiceMethod.");
	}

	@Before("tenantServicePointcut()")
	public void beforeWebServiceMethod(JoinPoint jp)
	{
		LOGGER.debug("Inside beforeWebServiceMethod.");
		transactionManager.setIsTenant(true);
		transactionManager.setTempSessionFactory(transactionManager.getTenantSessionFactory());
		LOGGER.debug("Exiting from beforeWebServiceMethod.");
	}

	@After("tenantServicePointcut()")
	public void afterWebServiceMethod(JoinPoint jp)
	{
		LOGGER.debug("Inside afterWebServiceMethod.");
		transactionManager.setIsTenant(true);
		transactionManager.setTempSessionFactory(transactionManager.getTenantSessionFactory());
		LOGGER.debug("Exiting from afterWebServiceMethod.");
	}

}