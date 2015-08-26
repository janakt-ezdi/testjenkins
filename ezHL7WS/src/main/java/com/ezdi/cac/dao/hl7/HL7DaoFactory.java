package com.ezdi.cac.dao.hl7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("hl7DaoFactory")
public class HL7DaoFactory
{
	@Autowired
	private HL7Dao hl7Dao;

	public HL7Dao getHl7Dao()
	{
		return hl7Dao;
	}

}
