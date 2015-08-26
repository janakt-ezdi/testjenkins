package com.ezdi.cac.dao.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("masterDAOFactory")
public class MasterDAOFactory
{

	@Autowired
	private MasterTenantDAO masterTenantDAO;

	public MasterTenantDAO getMasterTenantDAO()
	{
		return masterTenantDAO;
	}

}
