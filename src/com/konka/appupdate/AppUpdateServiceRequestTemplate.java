package com.konka.appupdate;


/**
 * 服务xml参数模板
 * @author jan
 *
 */
public class AppUpdateServiceRequestTemplate 
{
	public static String getTemplate()
	{
		StringBuffer templateBuffer = new StringBuffer();
		templateBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		
		templateBuffer.append("<Request>");
		templateBuffer.append("<RequestType>");
		templateBuffer.append("#RequestType#");
		templateBuffer.append("</RequestType>");
		
		templateBuffer.append("<AppParams>");
		
		templateBuffer.append("<OpType>");
		templateBuffer.append("#MobileVersionOpType#");
		templateBuffer.append("</OpType>");
		
		templateBuffer.append("<UserName>");
		templateBuffer.append("#UserName#");
		templateBuffer.append("</UserName>");
		templateBuffer.append("<AppName>");
		templateBuffer.append("#AppName#");
		templateBuffer.append("</AppName>");
		templateBuffer.append("<PackageName>");
		templateBuffer.append("#PackageName#");
		templateBuffer.append("</PackageName>");
		templateBuffer.append("<SystemVersion>");
		templateBuffer.append("#SystemVersion#");
		templateBuffer.append("</SystemVersion>");
		templateBuffer.append("<AppVersion>");
		templateBuffer.append("#AppVersion#");
		templateBuffer.append("</AppVersion>");
		templateBuffer.append("<Paramater1>");
		templateBuffer.append("#Paramater1#");
		templateBuffer.append("</Paramater1>");
		templateBuffer.append("<Paramater2>");
		templateBuffer.append("#Paramater2#");
		templateBuffer.append("</Paramater2>");
		templateBuffer.append("<Paramater3>");
		templateBuffer.append("#Paramater3#");
		templateBuffer.append("</Paramater3>");
		
		
		templateBuffer.append("</AppParams>");
		
		templateBuffer.append("</Request>");
		
		
		return templateBuffer.toString();
	}
	public static String getAppCurrentVersionParams(String UserName,String AppName,String PackageName,String SystemVersion,String AppVersion)
	{
		String template = getTemplate();
		template = template.replace("#RequestType#", "App");
		template = template.replace("#OpType#", "getAppCurrentVersion");
		template = template.replace("#UserName#", UserName);
		template = template.replace("#AppName#", AppName);
		template = template.replace("#PackageName#", PackageName);
		template = template.replace("#SystemVersion#", SystemVersion);
		template = template.replace("#AppVersion#", AppVersion);
		return template;
		
	}

}
