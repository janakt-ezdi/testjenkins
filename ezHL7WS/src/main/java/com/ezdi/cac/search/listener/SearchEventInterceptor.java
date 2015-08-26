package com.ezdi.cac.search.listener;

import com.ezdi.cac.bean.hl7.*;
import com.ezdi.cac.constant.TenantConfigConstant;
import com.ezdi.cac.controller.hl7.TenantThreadLocal;
import com.ezdi.cac.dao.hl7.HL7DaoFactory;
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
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by smsoni on 3/19/2015.
 */
@Component(value = "searchEventInterceptor")

public class SearchEventInterceptor  {
    private static final EzdiLogger LOG = EzdiLogManager.getLogger(SearchEventInterceptor.class);
    static Map<Class, String> stringMap = new HashMap<>();

    static {

        stringMap.put(EncounterBean.class, "ENCOUNTER");
       // stringMap.put(TypeEncounterStatusBean.class, "ENCOUNTER_STATUS");
        stringMap.put(LocationBean.class, "LOCATION");
        stringMap.put(PatientBean.class, "PATIENT");
        stringMap.put(UserRoleBean.class, "ROLE");
        stringMap.put(ServiceLineBean.class, "SERVICE_LINE");
        stringMap.put(PatientClassBean.class, "TYPE_CLASS");
        stringMap.put(UserLoginBean.class, "USER");
        stringMap.put(PayerBean.class, "PAYER");
    }
    private static ConcurrentHashMap<Integer, Map<String, String>> TENANT_CONFIG;

    @Autowired
    private HL7DaoFactory hl7DaoFactory;

    private int updates;
    private int creates;
    private int loads;

    public HL7DaoFactory getDaoFactory() {
        return hl7DaoFactory;
    }

    public final void setDaoFactory(HL7DaoFactory daoFactory) {
        this.hl7DaoFactory = daoFactory;
    }

    public void onDelete(Object entity,
                         Serializable id,
                         Object[] state,
                         String[] propertyNames,
                         Type[] types) {

        LOG.info("Into ON DELETE EVENT ... ");
        // do nothing
        // do nothing
        try {
            if (entity instanceof EncounterBean) {
                doSearchDelete(stringMap.get(EncounterBean.class), (Long) id);
            } /*else if (entity instanceof TypeEncounterStatusBean) {
                doSearchDelete(stringMap.get(TypeEncounterStatusBean.class), (Long) id);
            }*/ else if (entity instanceof LocationBean) {
                doSearchDelete(stringMap.get(LocationBean.class), (Long) id);
            } else if (entity instanceof PatientBean) {
                doSearchDelete(stringMap.get(PatientBean.class), (Long) id);
            } else if (entity instanceof UserRoleBean) {
                doSearchDelete(stringMap.get(UserRoleBean.class), (Long) id);
            } else if (entity instanceof ServiceLineBean) {
                doSearchDelete(stringMap.get(ServiceLineBean.class), (Long) id);
            } else if (entity instanceof PatientClassBean) {
                doSearchDelete(stringMap.get(PatientClassBean.class), (Long) id);
            } else if (entity instanceof UserLoginBean) {
                doSearchDelete(stringMap.get(UserLoginBean.class), (Long) id);
            } else if (entity instanceof PayerBean) {
                doSearchDelete(stringMap.get(PayerBean.class), (Long) id);
            }
        } catch (Exception ex) {
            LOG.error(ex);
        }

    }


    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) {
        try {


            updates++;

            // do nothing
            if (entity instanceof EncounterBean) {
                doSearchIndexUpdate(stringMap.get(EncounterBean.class), (Long) id);
            } /*else if (entity instanceof TypeEncounterStatusBean) {
                doSearchIndexUpdate(stringMap.get(TypeEncounterStatusBean.class), (Long) id);
            } */else if (entity instanceof LocationBean) {
                doSearchIndexUpdate(stringMap.get(LocationBean.class), (Long) id);
            } else if (entity instanceof PatientBean) {
                doSearchIndexUpdate(stringMap.get(PatientBean.class), (Long) id);
            } else if (entity instanceof UserRoleBean) {
                doSearchIndexUpdate(stringMap.get(UserRoleBean.class), (Long) id);
            } else if (entity instanceof ServiceLineBean) {
                doSearchIndexUpdate(stringMap.get(ServiceLineBean.class), (Long) id);
            } else if (entity instanceof PatientClassBean) {
                doSearchIndexUpdate(stringMap.get(PatientClassBean.class), (Long) id);
            } else if (entity instanceof UserLoginBean) {
                doSearchIndexUpdate(stringMap.get(UserLoginBean.class), (Long) id);
            } else if (entity instanceof PayerBean) {
                doSearchIndexUpdate(stringMap.get(PayerBean.class), (Long) id);
            }
        } catch (Exception ex) {
            LOG.error(ex);
        }


        return false;
    }

    public boolean onLoad(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) {

        loads++;

        return false;
    }

    public String getSearchType(Class clazz){
        return stringMap.get(clazz);
    }

    public Long getEntityId(Object entity){
        try {
            if (entity instanceof EncounterBean) {
                return Long.parseLong(((EncounterBean) entity).getId().toString());
            } /*else if (entity instanceof TypeEncounterStatusBean) {
                return Long.parseLong(((TypeEncounterStatusBean) entity).getEncounterStatusId().toString());
            } */else if (entity instanceof LocationBean) {
                return Long.parseLong(((LocationBean) entity).getId().toString());


            } else if (entity instanceof PatientBean) {
                return Long.parseLong(((PatientBean) entity).getId().toString());

            } else if (entity instanceof UserRoleBean) {
                return Long.parseLong(((UserRoleBean) entity).getId().toString());

            } else if (entity instanceof ServiceLineBean) {
                return Long.parseLong(((ServiceLineBean) entity).getId().toString());

            } else if (entity instanceof PatientClassBean) {
                return Long.parseLong(((PatientClassBean) entity).getId().toString());

            } else if (entity instanceof UserLoginBean) {
                return Long.parseLong(((UserLoginBean) entity).getId().toString());

            } else if(entity instanceof PayerBean){
                return Long.parseLong(((PayerBean) entity).getId().toString());
            }

        }catch (Exception ex){
            LOG.error(ex);
        }
        return null;
    }

    public boolean onSave(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) {


        creates++;
        // do nothing
        try {
            if (entity instanceof EncounterBean) {
                doSearchIndexUpdate(stringMap.get(EncounterBean.class), (Long) id);
            }/* else if (entity instanceof TypeEncounterStatusBean) {
                doSearchIndexUpdate(stringMap.get(TypeEncounterStatusBean.class), (Long) id);

            }*/ else if (entity instanceof LocationBean) {
                doSearchIndexUpdate(stringMap.get(LocationBean.class), (Long) id);

            } else if (entity instanceof PatientBean) {
                doSearchIndexUpdate(stringMap.get(PatientBean.class), (Long) id);

            } else if (entity instanceof UserRoleBean) {
                doSearchIndexUpdate(stringMap.get(UserRoleBean.class), (Long) id);

            } else if (entity instanceof ServiceLineBean) {
                doSearchIndexUpdate(stringMap.get(ServiceLineBean.class), (Long) id);

            } else if (entity instanceof PatientClassBean) {
                doSearchIndexUpdate(stringMap.get(PatientClassBean.class), (Long) id);

            } else if (entity instanceof UserLoginBean) {
                doSearchIndexUpdate(stringMap.get(UserLoginBean.class), (Long) id);

            } else if(entity instanceof PayerBean){
                doSearchIndexUpdate(stringMap.get(PayerBean.class), (Long) id);
            }

        }catch (Exception ex){
            LOG.error(ex);
        }
        return false;
    }


    public  void doSearchIndexUpdate(final String searchType,final Long entityId){
        try {


            final Map<String,String> config = getTenantConfiguration();

            final String url = config.get(TenantConfigConstant.SEARCH_SERVICE_URL);
            final String coreName = config.get(TenantConfigConstant.SEARCH_SERVICE_CORE);
            final String module = config.get(TenantConfigConstant.SEARCH_SERVICE_MODULE);
            final String token = config.get(TenantConfigConstant.SEARCH_SERVICE_AUTH_TOKEN);

            final Integer tenantId = TenantThreadLocal.getTenantId();

            new Thread(){
                public void run() {
                    try {

                        SearchServer server = new SearchServer(url, module);
                        IndexingParams param = new IndexingParams(searchType, entityId.toString());
                        param.set("tenantId", tenantId.toString());
                        IndexQuery query = new IndexQuery(param);
                        IndexRequest request = new IndexRequest(query, token, coreName);
                        IndexResponse response = (IndexResponse) server.execute(request);
                        response.isOkay();
                    } catch (Exception ex) {
                        LOG.error(ex);
                    }
                }
            }.start();

        }catch (Exception ex){
            LOG.error(ex);
        }
    }


    public void doSearchDelete(final String searchType,final Long entityId){
        try {


            final Map<String,String> config = getTenantConfiguration();

            final String url = config.get(TenantConfigConstant.SEARCH_SERVICE_URL);
            final String coreName = config.get(TenantConfigConstant.SEARCH_SERVICE_CORE);
            final String module = config.get(TenantConfigConstant.SEARCH_SERVICE_MODULE);
            final String token = config.get(TenantConfigConstant.SEARCH_SERVICE_AUTH_TOKEN);

            final Integer tenantId = TenantThreadLocal.getTenantId();

            new Thread(){
                public void run(){
                    try {


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
                    }catch (Exception ex){
                        LOG.error(ex);
                    }
                }
            }.start();

        }catch (Exception ex){
            LOG.error(ex);
        }
    }

    /**
     * This method is use to getTenantConfiguration tenant wise.
     *
     * @return Map<String,String>
     */
    public Map<String, String> getTenantConfiguration() {

        Map<String, String> tenantConfigMap = null;
        try {

            Integer id = TenantThreadLocal.getTenantId();
            if (TENANT_CONFIG == null) {
                TENANT_CONFIG = new ConcurrentHashMap<Integer, Map<String, String>>();
                TENANT_CONFIG.putIfAbsent(id, getDaoFactory().getHl7Dao().getTenantConfigMapWithNewSession());
            } else if (TENANT_CONFIG.get(id) == null) {
                TENANT_CONFIG.putIfAbsent(id, getDaoFactory().getHl7Dao().getTenantConfigMapWithNewSession());
            }
            tenantConfigMap = TENANT_CONFIG.get(id);
        } catch (Exception se) {
            LOG.error(se.getMessage(), se);
        }
        return tenantConfigMap;
    }


}
