package com.ezdi.cac.dao.master;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MasterDAOFactoryTest
{
	@Autowired
	private MasterDAOFactory masterDaoFactory;

	@Test
	public void testGetMasterTenantDAO() throws Exception
	{
		MasterTenantDAO masterTenantDAO = masterDaoFactory.getMasterTenantDAO();
		Assert.assertNotNull(masterTenantDAO);
		Assert.assertEquals(MasterTenantDAOImpl.class, masterTenantDAO.getClass());
	}
}
