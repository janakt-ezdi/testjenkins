package com.ezdi.cac.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("masterServiceFactory")
public class MasterServiceFactory
{
	@Autowired
	private MasterService masterService;

	public MasterService getMasterService()
	{
		return masterService;
	}
}
