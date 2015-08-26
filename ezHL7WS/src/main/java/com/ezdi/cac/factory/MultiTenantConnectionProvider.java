package com.ezdi.cac.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import com.ezdi.cac.bean.master.TenantInfoBean;
import com.ezdi.cac.controller.hl7.TenantThreadLocal;
import com.ezdi.cac.listener.AppCtxProvider;
import com.ezdi.cac.service.master.MasterService;
import com.ezdi.cac.util.Encryption;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(MultiTenantConnectionProvider.class);
	private static final long serialVersionUID = 3148980372602243897L;
	private static ConcurrentHashMap<String, DataSource> cachedDataSourceMap = new ConcurrentHashMap<String, DataSource>();
	private static ConcurrentHashMap<String, Integer> cachedTenantCodeToIdMap = new ConcurrentHashMap<String, Integer>();

	protected DataSource selectAnyDataSource()
	{
		LOGGER.debug("Inside selectAnyDataSource.");
		try
		{
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			return dataSource;
		} catch (Exception e)
		{
			LOGGER.error("Error while connection dummy Data Source in multiTenant. Exception message : " + e.getMessage(), e);
		}
		LOGGER.debug("Exiting from selectAnyDataSource.");
		return null;

	}

	protected DataSource selectDataSource(String tenantCode)
	{
		LOGGER.debug("Inside selectDataSource.");
		LOGGER.info("Tenant code : " + tenantCode);
		try
		{

			LOGGER.info("Loading C3P0 properties.....");
			Properties c3p0Properties = new Properties();
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("c3p0.properties");
			try
			{
				c3p0Properties.load(inputStream);
			} catch (IOException ioException)
			{
				LOGGER.error(ioException);
			}
			LOGGER.info("C3P0 properties loaded successfully.");
			LOGGER.info("C3P0 properties. c3p0Properties : " + c3p0Properties);

			LOGGER.info("Checking whether data source is cached.....");
			if (!cachedDataSourceMap.containsKey(tenantCode))
			{
				LOGGER.info("Data source not found in cache. Will load from common database.");
				if (TenantThreadLocal.getTenantCode() != null)
				{
					MasterService masterService = (MasterService) AppCtxProvider.getApplicationContext().getBean("masterService");
					TenantInfoBean tenantInfoBean = masterService.getTenantByCode(TenantThreadLocal.getTenantCode());
                    cachedTenantCodeToIdMap.putIfAbsent(TenantThreadLocal.getTenantCode(), tenantInfoBean.getTenantId().intValue());
					ComboPooledDataSource dataSource = new ComboPooledDataSource();
					dataSource.setDriverClass("com.mysql.jdbc.Driver");
					dataSource.setJdbcUrl(tenantInfoBean.getDbUrl() + "?characterEncoding=utf-8");
					dataSource.setUser(tenantInfoBean.getDbUserName());
					dataSource.setPassword(Encryption.decrypt(tenantInfoBean.getDbPassword()));

					/* Added for setting connection pooling parameters */

					dataSource.setIdleConnectionTestPeriod(Integer.valueOf((c3p0Properties.getProperty("idleConnectionTestPeriod"))));
					dataSource.setMaxIdleTime(Integer.valueOf((c3p0Properties.getProperty("maxIdleTime"))));
					dataSource.setMaxIdleTimeExcessConnections(Integer.valueOf((c3p0Properties.getProperty("maxIdleTimeExcessConnections"))));
					dataSource.setMinPoolSize(Integer.valueOf((c3p0Properties.getProperty("minPoolSize"))));
					dataSource.setMaxPoolSize(Integer.valueOf((c3p0Properties.getProperty("maxPoolSize"))));
					dataSource.setTestConnectionOnCheckout(Boolean.valueOf((c3p0Properties.getProperty("testConnectionOnCheckout"))));

					/* Added for setting connection pooling parameters */

					cachedDataSourceMap.putIfAbsent(tenantCode, dataSource);
					LOGGER.info("dataSource loaded from common database.");
				}
			}
			LOGGER.debug("Exiting from selectDataSource.");
            TenantThreadLocal.setTenantId(cachedTenantCodeToIdMap.get(tenantCode));
			return cachedDataSourceMap.get(tenantCode);
		} catch (Exception e)
		{
			LOGGER.error(
					"Error while connecting ACTIVE TENANT DATA SOURCE for tenantCode : " + Encryption.encrypt(tenantCode) + " " + e.getMessage(), e);
			return null;
		}
	}
}
