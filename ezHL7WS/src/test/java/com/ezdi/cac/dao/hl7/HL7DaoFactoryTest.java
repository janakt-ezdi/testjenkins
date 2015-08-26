package com.ezdi.cac.dao.hl7;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ezdi.cac.controller.hl7.HL7Controller;
import com.ezdi.cac.service.hl7.HL7ServiceImpl;

public class HL7DaoFactoryTest
{

	private static HL7DaoFactory hl7DaoFactory;

	@BeforeClass
	public static void beforeGetHl7Dao() throws Exception
	{
		HL7Controller hl7Controller = new HL7Controller();
		HL7ServiceImpl hl7ServiceImpl = (HL7ServiceImpl) hl7Controller.getHl7ServiceFactory().getHl7Service();
		hl7DaoFactory = hl7ServiceImpl.getHl7DaoFactory();

	}

	@Test
	public void testGetHl7Dao() throws Exception
	{
		HL7Dao hl7Dao = hl7DaoFactory.getHl7Dao();
		Assert.assertNotNull(hl7Dao);
		Assert.assertEquals(HL7DaoImpl.class, hl7Dao.getClass());
	}
}
