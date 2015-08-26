package com.ezdi.cac.controller.hl7;

import com.ezdi.cac.search.listener.SearchEventInterceptor;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ezdi.cac.service.hl7.HL7ServiceFactory;
import com.ezdi.cac.service.master.MasterServiceFactory;

@Controller
public abstract class HL7BaseController
{

    private static final EzdiLogger LOG = EzdiLogManager.getLogger(HL7BaseController.class);

	@Autowired
	private HL7ServiceFactory hl7ServiceFactory;

	@Autowired
	private MasterServiceFactory masterServiceFactory;

	public HL7ServiceFactory getHl7ServiceFactory()
	{
		return hl7ServiceFactory;
	}

	public void setHl7ServiceFactory(HL7ServiceFactory hl7ServiceFactory)
	{
		this.hl7ServiceFactory = hl7ServiceFactory;
	}

	public MasterServiceFactory getMasterServiceFactory()
	{
		return masterServiceFactory;
	}

	public void setMasterServiceFactory(MasterServiceFactory masterServiceFactory)
	{
		this.masterServiceFactory = masterServiceFactory;
	}

}