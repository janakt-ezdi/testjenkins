package com.ezdi.cac.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppCtxProvider implements ApplicationContextAware
{

	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(final ApplicationContext appContext) throws BeansException
	{
		ctx = appContext;
	}

	public static ApplicationContext getApplicationContext()
	{
		return ctx;
	}
}
