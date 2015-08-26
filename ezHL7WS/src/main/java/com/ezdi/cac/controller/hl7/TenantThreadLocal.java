package com.ezdi.cac.controller.hl7;


public class TenantThreadLocal
{
	private static final ThreadLocal<String> TENANT_CODE = new ThreadLocal<String>();

    private static final ThreadLocal<Integer> TENANT_ID = new ThreadLocal<>();

	public static void setTenantCode(String tenantCode)
	{
		TENANT_CODE.set(tenantCode);
	}

	public static String getTenantCode()
	{
		return TENANT_CODE.get();
	}


    public static void setTenantId(Integer tenantId)
    {
        TENANT_ID.set(tenantId);
    }

    public static Integer getTenantId()
    {
        return TENANT_ID.get();
    }

}
