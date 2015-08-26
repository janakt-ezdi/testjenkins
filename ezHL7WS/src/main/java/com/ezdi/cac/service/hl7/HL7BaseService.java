package com.ezdi.cac.service.hl7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezdi.cac.dao.hl7.HL7DaoFactory;
import com.ezdi.cac.factory.Tenant;
import com.ezdi.cac.search.listener.SearchEventInterceptor;
import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

@Service
@Tenant
public class HL7BaseService
{

	private static final EzdiLogger LOG = EzdiLogManager.getLogger(HL7BaseService.class);

	@Autowired
	private HL7DaoFactory hl7DaoFactory;

	public HL7DaoFactory getHl7DaoFactory()
	{
		return hl7DaoFactory;
	}

	public void setHl7DaoFactory(HL7DaoFactory hl7DaoFactory)
	{
		this.hl7DaoFactory = hl7DaoFactory;
	}

	@Autowired
	private SearchEventInterceptor searchEventInterceptor;

	public SearchEventInterceptor getSearchEventInterceptor()
	{
		return searchEventInterceptor;
	}

	public final void setSearchEventInterceptor(SearchEventInterceptor searchEventInterceptor)
	{
		this.searchEventInterceptor = searchEventInterceptor;
	}

	protected void notifyUpdateToIndex(Object o)
	{
		try
		{
			String searchType = getSearchEventInterceptor().getSearchType(o.getClass());
			Long id = getSearchEventInterceptor().getEntityId(o);
			if (searchType != null && id != null)
			{
				getSearchEventInterceptor().doSearchIndexUpdate(searchType, id);
			}
		} catch (Exception ex)
		{
			LOG.error(ex);
		}
	}

	protected void notifyUpdateToIndexById(Integer o, Class clazz)
	{
		try
		{
			String searchType = getSearchEventInterceptor().getSearchType(clazz);
			Long id = Long.parseLong(o.toString());
			if (searchType != null && id != null)
			{
				getSearchEventInterceptor().doSearchIndexUpdate(searchType, id);
			}
		} catch (Exception ex)
		{
			LOG.error(ex);
		}
	}

	protected void notifyDeleteToIndex(Object o)
	{
		try
		{
			String searchType = getSearchEventInterceptor().getSearchType(o.getClass());
			Long id = getSearchEventInterceptor().getEntityId(o);
			if (searchType != null && id != null)
			{
				getSearchEventInterceptor().doSearchDelete(searchType, id);
			}
		} catch (Exception ex)
		{
			LOG.error(ex);
		}
	}

	protected void notifyDeleteToIndexById(Integer o, Class clazz)
	{
		try
		{
			String searchType = getSearchEventInterceptor().getSearchType(clazz);
			Long id = Long.parseLong(o.toString());
			if (searchType != null && id != null)
			{
				getSearchEventInterceptor().doSearchDelete(searchType, id);
			}
		} catch (Exception ex)
		{
			LOG.error(ex);
		}
	}

}
