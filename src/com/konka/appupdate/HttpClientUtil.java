package com.konka.appupdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

/**
 * HTTP工具类
 * @author jan
 *
 */
public class HttpClientUtil 
{
	private String serviceUrl;//服务地址
	private static final int timeout = 40000;//超时
	public HttpClientUtil(String serviceUrl)
	{
		this.serviceUrl = serviceUrl;
	}
	public String getPostRequestResult(HashMap <String,String> params) throws Exception
	{
		String result = "";
		HttpPost httpPost = new HttpPost(serviceUrl);
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator iter = params.entrySet().iterator();
		while (iter.hasNext()) 
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			String val = (String)entry.getValue();
			list.add(new BasicNameValuePair(key,val));
		}
		HttpEntity httpEntity = new UrlEncodedFormEntity(list, "UTF-8"); 
		httpPost.setEntity(httpEntity);
		BasicHttpParams httpParams = new BasicHttpParams();  
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);  
	    HttpConnectionParams.setSoTimeout(httpParams, timeout);  
    	HttpClient httpclient = new DefaultHttpClient(httpParams); 
    	HttpResponse httpResponse = httpclient.execute(httpPost);
    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
    	{
    		result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
    	}
    	return result;
	}

}
