package com.ezdi.cac.service.hl7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("hl7ServiceFactory")
public class HL7ServiceFactory
{
	@Autowired
	private HL7Service hl7Service;

	public HL7Service getHl7Service()
	{
		return hl7Service;
	}
}
