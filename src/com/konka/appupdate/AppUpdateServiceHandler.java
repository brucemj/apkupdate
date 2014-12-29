package com.konka.appupdate;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * app  xml读取类
 * @author jan
 *
 */
public class AppUpdateServiceHandler extends DefaultHandler 
{

	private String currentTagName;//当前标签名
	private String tagValue;//标签值
	
	private ResponseParams responseParams = null;
	private ResponseResult responseResult = null;
	private AppResult appResult = null;

	
	/**
	 * 构造函数
	 */
	public AppUpdateServiceHandler() 
	{
		super();
	}

	/**
	 * 构造函数
	 * @param appList
	 */
	public AppUpdateServiceHandler(ResponseParams responseParams) 
	{
		super();
		this.responseParams = responseParams;
	}
	
	@Override
	public void startDocument() throws SAXException 
	{
		super.startDocument();
	}
	@Override
	public void endDocument() throws SAXException 
	{
		super.endDocument();
	}
	
	/**
	 * XML解析器遇到XML里面的tag时就会调用这个函数
	 */
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentTagName = localName;
		if ("ResponseResult".equals(localName)) 
		{
			responseResult = new ResponseResult();

		}
		else if("AppResult".equals(localName))
		{
			appResult = new AppResult();
		}
		super.startElement(uri, localName, qName, attributes);
	}

	/**
	 * 这个方法与startElement()相对应，解析完一个tag节点后，执行这个方法
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		currentTagName = "";
		if ("AppResult".equals(localName)) 
		{
			responseResult.setAppResult(appResult);
		}
		else if("ResponseResult".equals(localName))
		{
			responseParams.setResponseResult(responseResult);
		}
		super.endElement(uri, localName, qName);
	}
	/**
	 * 这是一个回调方法。解析器执行完startElement()后，解析完节点的内容后就会执行这个方法，并且参数ch[]就是节点的内容
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		
		tagValue = new String(ch, start, length);
		if ("ResponseCode".equals(currentTagName))
		{
			responseParams.setResponseCode(tagValue);
		} 
		else if ("ResponseMessage".equals(currentTagName)) 
		{
			responseParams.setResponseMessage(tagValue);
		} 
		else if ("AppVersion".equals(currentTagName)) 
		{
			appResult.setAppVersion(tagValue);
		} 
		else if ("AppVersionInfo".equals(currentTagName)) 
		{
			appResult.setAppVersionInfo(tagValue);
		} 
		else if ("DownloadUrl".equals(currentTagName)) 
		{
			appResult.setDownloadUrl(tagValue);
		} 
		else if ("Paramater1".equals(currentTagName)) 
		{
			appResult.setParamater1(tagValue);
		} 
		else if ("Paramater2".equals(currentTagName)) 
		{
			appResult.setParamater2(tagValue);
		} 
		else if ("Paramater3".equals(currentTagName)) 
		{
			appResult.setParamater3(tagValue);
		} 
		super.characters(ch, start, length);
	}


}