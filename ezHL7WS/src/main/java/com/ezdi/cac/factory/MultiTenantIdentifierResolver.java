package com.ezdi.cac.factory;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import com.ezdi.cac.controller.hl7.TenantThreadLocal;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

public class MultiTenantIdentifierResolver implements CurrentTenantIdentifierResolver
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(MultiTenantIdentifierResolver.class);

	public String resolveCurrentTenantIdentifier()
	{
		String tenantCode = null;
		LOGGER.debug("Inside resolveCurrentTenantIdentifier.");
		if (TenantThreadLocal.getTenantCode() != null && !TenantThreadLocal.getTenantCode().isEmpty())
		{
			tenantCode = TenantThreadLocal.getTenantCode();
		}
		LOGGER.debug("Exiting from resolveCurrentTenantIdentifier.");
		return tenantCode;
	}

	public boolean validateExistingCurrentSessions()
	{
		return true;
	}
}
