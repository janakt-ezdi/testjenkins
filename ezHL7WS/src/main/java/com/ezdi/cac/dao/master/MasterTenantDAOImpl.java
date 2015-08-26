package com.ezdi.cac.dao.master;

import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.ezdi.cac.bean.master.TenantInfoBean;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

@Component("masterTenantDAO")
public class MasterTenantDAOImpl extends MasterBaseDAO implements MasterTenantDAO
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(MasterTenantDAOImpl.class);
	private ConcurrentHashMap<String, TenantInfoBean> tenantLookUpConnectionMap = new ConcurrentHashMap<String, TenantInfoBean>();

	@Override
	public TenantInfoBean getTenant(String tenantCode) throws Exception
	{
		LOGGER.debug("Inside getTenant");
		if (!tenantLookUpConnectionMap.containsKey(tenantCode))
		{
			try
			{
				Session session = openSession();
				Criteria criteria = session.createCriteria(TenantInfoBean.class);
				criteria.add(Restrictions.eq("tenantCode", tenantCode));
				TenantInfoBean tenantInfoBean = (TenantInfoBean) criteria.uniqueResult();
				tenantLookUpConnectionMap.putIfAbsent(tenantCode, tenantInfoBean);
				session.close();

			} catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
		LOGGER.debug("Exiting from getTenant");
		return tenantLookUpConnectionMap.get(tenantCode);
	}
}
