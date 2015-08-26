package com.ezdi.cac.service.master;

import com.ezdi.cac.bean.master.TenantInfoBean;

public interface MasterService
{
	TenantInfoBean getTenantByCode(String tenantCode) throws Exception;
}
