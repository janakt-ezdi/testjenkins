package com.ezdi.cac.search.listener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezdi.cac.bean.hl7.EncounterBean;
import com.ezdi.cac.bean.hl7.LocationBean;
import com.ezdi.cac.bean.hl7.PatientBean;
import com.ezdi.cac.bean.hl7.PatientClassBean;
import com.ezdi.cac.bean.hl7.PayerBean;
import com.ezdi.cac.bean.hl7.ServiceLineBean;
import com.ezdi.cac.bean.hl7.UserLoginBean;
import com.ezdi.cac.bean.hl7.UserRoleBean;
import com.ezdi.cac.constant.TenantConfigConstant;
import com.ezdi.cac.controller.hl7.TenantThreadLocal;
import com.ezdi.cac.service.hl7.HL7Service;
import com.ezdi.cac.service.master.MasterService;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;
import com.ezdi.solrservice.client.CoreInfo;
import com.ezdi.solrservice.client.SearchServer;
import com.ezdi.solrservice.client.authentication.token.TokenAuthentication;
import com.ezdi.solrservice.client.method.IndexMethod;
import com.ezdi.solrservice.client.method.Method;
import com.ezdi.solrservice.client.params.IndexingParams;
import com.ezdi.solrservice.client.query.IndexQuery;
import com.ezdi.solrservice.client.request.IndexRequest;
import com.ezdi.solrservice.client.response.IndexResponse;

/**
 * Created by smsoni on 3/19/2015.
 */
@Component(value = "searchInterceptor")
public class SearchServiceEventInterceptor extends EmptyInterceptor
{
	private static final EzdiLogger LOG = EzdiLogManager.getLogger(SearchServiceEventInterceptor.class);

	private static ConcurrentHashMap<Integer, Map<String, String>> TENANT_CONFIG;

	@Autowired
	private HL7Service hl7Service;

	public HL7Service getHl7Service()
	{
		return hl7Service;
	}

	@Autowired
	private MasterService masterService;

	public MasterService getMasterService()
	{
		return masterService;
	}

	private static Map<Class, String> stringMap = new HashMap<Class, String>();

	static
	{

		stringMap.put(EncounterBean.class, "ENCOUNTER");
		stringMap.put(LocationBean.class, "LOCATION");
		stringMap.put(PatientBean.class, "PATIENT");
		stringMap.put(UserRoleBean.class, "ROLE");
		stringMap.put(ServiceLineBean.class, "SERVICE_LINE");
		stringMap.put(PatientClassBean.class, "TYPE_CLASS");
		stringMap.put(UserLoginBean.class, "USER");
		stringMap.put(PayerBean.class, "PAYER");
	}

	private int updates;
	private int creates;
	private int loads;

	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{

		LOG.info("Into ON DELETE EVENT ... ");
		// do nothing
		// do nothing
		if (entity instanceof EncounterBean)
		{
			doSearchDelete(stringMap.get(EncounterBean.class), (Integer) id);
		} else if (entity instanceof LocationBean)
		{
			doSearchDelete(stringMap.get(LocationBean.class), (Integer) id);
		} else if (entity instanceof PatientBean)
		{
			doSearchDelete(stringMap.get(PatientBean.class), (Integer) id);
		} else if (entity instanceof UserRoleBean)
		{
			doSearchDelete(stringMap.get(UserRoleBean.class), (Integer) id);
		} else if (entity instanceof ServiceLineBean)
		{
			doSearchDelete(stringMap.get(ServiceLineBean.class), (Integer) id);
		} else if (entity instanceof PatientClassBean)
		{
			doSearchDelete(stringMap.get(PatientClassBean.class), (Integer) id);
		} else if (entity instanceof UserLoginBean)
		{
			doSearchDelete(stringMap.get(UserLoginBean.class), (Integer) id);
		} else if (entity instanceof PayerBean)
		{
			doSearchDelete(stringMap.get(PayerBean.class), (Integer) id);
		}

	}

	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types)
	{

		updates++;

		// do nothing
		if (entity instanceof EncounterBean)
		{
			doSearchIndexUpdate(stringMap.get(EncounterBean.class), (Integer) id);
		} else if (entity instanceof LocationBean)
		{
			doSearchIndexUpdate(stringMap.get(LocationBean.class), (Integer) id);
		} else if (entity instanceof PatientBean)
		{
			doSearchIndexUpdate(stringMap.get(PatientBean.class), (Integer) id);
		} else if (entity instanceof UserRoleBean)
		{
			doSearchIndexUpdate(stringMap.get(UserRoleBean.class), (Integer) id);
		} else if (entity instanceof ServiceLineBean)
		{
			doSearchIndexUpdate(stringMap.get(ServiceLineBean.class), (Integer) id);
		} else if (entity instanceof PatientClassBean)
		{
			doSearchIndexUpdate(stringMap.get(PatientClassBean.class), (Integer) id);
		} else if (entity instanceof UserRoleBean)
		{
			doSearchIndexUpdate(stringMap.get(UserRoleBean.class), (Integer) id);
		} else if (entity instanceof PayerBean)
		{
			doSearchIndexUpdate(stringMap.get(PayerBean.class), (Integer) id);
		}
		return false;
	}

	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{

		loads++;

		return false;
	}

	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
	{

		creates++;
		// do nothing
		try
		{
			if (entity instanceof EncounterBean)
			{
				doSearchIndexUpdate(stringMap.get(EncounterBean.class), (Integer) id);
			} else if (entity instanceof LocationBean)
			{
				doSearchIndexUpdate(stringMap.get(LocationBean.class), (Integer) id);

			} else if (entity instanceof PatientBean)
			{
				doSearchIndexUpdate(stringMap.get(PatientBean.class), (Integer) id);

			} else if (entity instanceof UserRoleBean)
			{
				doSearchIndexUpdate(stringMap.get(UserRoleBean.class), (Integer) id);

			} else if (entity instanceof ServiceLineBean)
			{
				doSearchIndexUpdate(stringMap.get(ServiceLineBean.class), (Integer) id);

			} else if (entity instanceof PatientClassBean)
			{
				doSearchIndexUpdate(stringMap.get(PatientClassBean.class), (Integer) id);

			} else if (entity instanceof UserRoleBean)
			{
				doSearchIndexUpdate(stringMap.get(UserRoleBean.class), (Integer) id);
			} else if (entity instanceof PayerBean)
			{
				doSearchIndexUpdate(stringMap.get(PayerBean.class), (Integer) id);
			}

		} catch (Exception ex)
		{
			LOG.error(ex);
		}
		return false;
	}

	private void doSearchIndexUpdate(final String searchType, final Integer entityId)
	{
		try
		{

			final Map<String, String> config = getTenantConfiguration();

			final String url = config.get(TenantConfigConstant.SEARCH_SERVICE_URL);
			final String coreName = config.get(TenantConfigConstant.SEARCH_SERVICE_CORE);
			final String module = config.get(TenantConfigConstant.SEARCH_SERVICE_MODULE);
			final String token = config.get(TenantConfigConstant.SEARCH_SERVICE_AUTH_TOKEN);

			Integer tenantId = getMasterService().getTenantByCode(TenantThreadLocal.getTenantCode()).getTenantId().intValue();

			SearchServer server = new SearchServer(url, module);
			IndexingParams param = new IndexingParams(searchType, entityId.toString());
			param.set("tenantId", tenantId.toString());
			IndexQuery query = new IndexQuery(param);
			IndexRequest request = new IndexRequest(query, token, coreName);
			IndexResponse response = (IndexResponse) server.execute(request);
			response.isOkay();
		} catch (Exception ex)
		{
			LOG.error(ex);
		}
	}

	private void doSearchDelete(final String searchType, final Integer entityId)
	{
		try
		{

			final Map<String, String> config = getTenantConfiguration();

			final String url = config.get(TenantConfigConstant.SEARCH_SERVICE_URL);
			final String coreName = config.get(TenantConfigConstant.SEARCH_SERVICE_CORE);
			final String module = config.get(TenantConfigConstant.SEARCH_SERVICE_MODULE);
			final String token = config.get(TenantConfigConstant.SEARCH_SERVICE_AUTH_TOKEN);

			Integer tenantId = getMasterService().getTenantByCode(TenantThreadLocal.getTenantCode()).getTenantId().intValue();

			SearchServer server = new SearchServer(url, module);
			IndexingParams param = new IndexingParams(searchType, entityId.toString());
			param.set("tenantId", tenantId.toString());
			IndexQuery query = new IndexQuery(param);
			TokenAuthentication auth = new TokenAuthentication(token);
			IndexMethod method = new IndexMethod(Method.HTTP_GET, "flushEntity");
			CoreInfo coreInfo = new CoreInfo(coreName);
			IndexRequest request = new IndexRequest(query, auth, method, coreInfo);
			IndexResponse response = (IndexResponse) server.execute(request);
			response.isOkay();
		} catch (Exception ex)
		{
			LOG.error(ex);
		}
	}

	/**
	 * This method is use to getTenantConfiguration tenant wise.
	 *
	 * @return Map<String,String>
	 */
	public Map<String, String> getTenantConfiguration()
	{

		Map<String, String> tenantConfigMap = null;
		try
		{
			final Integer tenantId = getMasterService().getTenantByCode(TenantThreadLocal.getTenantCode()).getTenantId().intValue();
			if (TENANT_CONFIG == null)
			{
				TENANT_CONFIG = new ConcurrentHashMap<Integer, Map<String, String>>();
				TENANT_CONFIG.putIfAbsent(tenantId, getHl7Service().getTenantConfigMap());
			} else if (TENANT_CONFIG.get(tenantId) == null)
			{
				TENANT_CONFIG.putIfAbsent(tenantId, getHl7Service().getTenantConfigMap());
			}
			tenantConfigMap = TENANT_CONFIG.get(tenantId);
		} catch (Exception e)
		{
			LOG.trace(e.getMessage(), e);
		}
		return tenantConfigMap;
	}
}