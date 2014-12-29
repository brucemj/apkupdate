package com.konka.appupdate;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * app  xml��ȡ��
 * @author jan
 *
 */
public class AppUpdateServiceHandler extends DefaultHandler 
{

	private String currentTagName;//��ǰ��ǩ��
	private String tagValue;//��ǩֵ
	
	private ResponseParams responseParams = null;
	private ResponseResult responseResult = null;
	private AppResult appResult = null;

	
	/**
	 * ���캯��
	 */
	public AppUpdateServiceHandler() 
	{
		super();
	}

	/**
	 * ���캯��
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
	 * XML����������XML�����tagʱ�ͻ�����������
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
	 * ���������startElement()���Ӧ��������һ��tag�ڵ��ִ���������
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
	 * ����һ���ص�������������ִ����startElement()�󣬽�����ڵ�����ݺ�ͻ�ִ��������������Ҳ���ch[]���ǽڵ������
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