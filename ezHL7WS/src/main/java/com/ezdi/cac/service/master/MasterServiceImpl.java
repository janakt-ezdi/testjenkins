package com.ezdi.cac.service.master;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezdi.cac.bean.master.TenantInfoBean;
import com.ezdi.cac.factory.Master;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

@Service("masterService")
@Master
public class MasterServiceImpl extends MasterBaseService implements MasterService
{

	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(MasterServiceImpl.class);

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public TenantInfoBean getTenantByCode(String tenantCode) throws Exception
	{
		LOGGER.info("Inside getTenantByCode");
		TenantInfoBean tenantInfoBean = getMasterDAOFactory().getMasterTenantDAO().getTenant(tenantCode);
		LOGGER.info("Exiting from getTenantByCode");
		return tenantInfoBean;
	}
}
