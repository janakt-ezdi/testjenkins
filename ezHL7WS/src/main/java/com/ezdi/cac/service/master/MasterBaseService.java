package com.ezdi.cac.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdi.cac.dao.master.MasterDAOFactory;
import com.ezdi.cac.factory.Master;

@Service
@Master
public class MasterBaseService
{

	@Autowired
	private MasterDAOFactory masterDAOFactory;

	public MasterDAOFactory getMasterDAOFactory()
	{
		return masterDAOFactory;
	}

	public void setMasterDAOFactory(MasterDAOFactory masterDAOFactory)
	{
		this.masterDAOFactory = masterDAOFactory;
	}
}
