package com.ezdi.cac.service.master;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MasterServiceFactoryTest
{

	@Autowired
	private MasterServiceFactory masterServiceFactory;

	@Test
	public void testGetMasterTenantDAO() throws Exception
	{
		MasterService masterService = masterServiceFactory.getMasterService();
		Assert.assertNotNull(masterService);
		Assert.assertEquals(MasterServiceImpl.class, masterService.getClass());
	}

}
