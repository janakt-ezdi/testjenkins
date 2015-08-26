package com.ezdi.cac.service.hl7;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HL7ServiceFactoryTest
{

	@Autowired
	private HL7ServiceFactory hL7ServiceFactory;

	@Test
	public void testGetMasterTenantDAO() throws Exception
	{
		HL7Service hl7Service = hL7ServiceFactory.getHl7Service();
		Assert.assertNotNull(hl7Service);
		Assert.assertEquals(HL7ServiceImpl.class, hl7Service.getClass());
	}

}
