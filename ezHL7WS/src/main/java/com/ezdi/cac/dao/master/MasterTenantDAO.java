package com.ezdi.cac.dao.master;

import com.ezdi.cac.bean.master.TenantInfoBean;

public interface MasterTenantDAO
{
	TenantInfoBean getTenant(String tenantId) throws Exception;
}
