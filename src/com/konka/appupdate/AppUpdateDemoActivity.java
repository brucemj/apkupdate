package com.konka.appupdate;


import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 应用获取最新版本实例
 * @author jan
 *
 */
public class AppUpdateDemoActivity extends Activity  implements OnClickListener
{
	private Button getCurretnVersionBtn;
	private TextView appVersionTv;
	private TextView appVersionInfoTv;
	private TextView downloadUrlTv;
	private String packageName;
	
	private ProgressDialog progressDialog = null;   
	
	private HttpClientUtil httpClientUtil;
	
	public static final String SERVER_URL = "http://unionupdate.kkpush.net/UnionUpdateService";//测试统一升级服务地址
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initUtil();
    }

    /**
     * 初始化ui
     */
    private void initView()
    {
    	getCurretnVersionBtn = (Button) findViewById(R.id.getCurretnVersionBtn);
    	appVersionTv = (TextView) findViewById(R.id.appVersionTv);
    	appVersionInfoTv = (TextView) findViewById(R.id.appVersionInfoTv);
    	downloadUrlTv = (TextView) findViewById(R.id.downloadUrlTv);
    	getCurretnVersionBtn.setOnClickListener(this);
    	
    	packageName = this.getPackageName();
    	String nowVersion = getVersionName();
    	appVersionTv.setText("版本号：" + nowVersion);
    	appVersionInfoTv.setText("版本信息：" + packageName);
    	
    }
    
    private String getVersionName(){  
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();  

        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return packInfo.versionName;
    }
    
    /**
     * 初始化工具
     */
    private void initUtil()
    {
    	httpClientUtil = new HttpClientUtil(SERVER_URL);
    }
	@Override
	public void onClick(View view)
	{
		if(view.getId() == getCurretnVersionBtn.getId())
		{
			progressDialog = ProgressDialog.show(this,"请等待...", "正在获取版本 ...", true);  
			new Thread(new GetAppCurrentVersionThread()).start();
		}
	}
	
	
	private Handler handler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			progressDialog.dismiss();
			//获取版本成功
			if (msg.what == 1) 
			{
				Bundle b = msg.getData();
        		String AppVersion = b.getString("AppVersion");
        		String AppVersionInfo = b.getString("AppVersionInfo");
        		String DownloadUrl = b.getString("DownloadUrl");
        		appVersionTv.setText("版本号：" + AppVersion);
        		appVersionInfoTv.setText("版本信息：" + AppVersionInfo);
        		downloadUrlTv.setText("版本下载地址：" + DownloadUrl);
			}
			else if (msg.what == 2)// 发生错误,提示用户
			{
				Toast.makeText(AppUpdateDemoActivity.this, "获取版本失败", Toast.LENGTH_LONG).show();
			}
		}
	};
	/**
	 * 通知成功
	 * @param appResult  应用结果
	 */
	private void sendSuccess(AppResult appResult)
	{
		Message msg = new Message();
		msg.what = 1;
        Bundle b = new Bundle();
        b.putString("AppVersion",appResult.getAppVersion());
        b.putString("AppVersionInfo",appResult.getAppVersionInfo());
        b.putString("DownloadUrl",appResult.getDownloadUrl());
        msg.setData(b);
        handler.sendMessage(msg);
	}
	/**
	 * 通知失败
	 */
	private void sendError()
	{
		Message message = Message.obtain();
		message.what = 2;
		handler.sendMessage(message);
	}
	/**
	 * 获取版本线程
	 * @author jan
	 *
	 */
	class GetAppCurrentVersionThread implements Runnable 
	{
		@Override
		public void run() 
		{
			String xmlResult = "";
			try
			{
				
				HashMap<String, String> params = new HashMap<String, String>();
				String param = "";//服务需要传递的参数
				param = AppUpdateServiceRequestTemplate.getAppCurrentVersionParams("","","","","");//获取xml参数
				param = URLEncoder.encode(param, "utf-8"); //参数编码
				params.put("param", param);
				xmlResult = httpClientUtil.getPostRequestResult(params);//得到xml返回值

			}
			catch(Exception e)
			{
				sendError();
				return;
			}
			if (xmlResult == null || "".equals(xmlResult))
            {
				sendError();
				return;
			}
			StringReader read = new StringReader(xmlResult);
			InputSource source = new InputSource(read);
			ResponseParams responseParams = new ResponseParams();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			try
			{
				// 解析版本信息xml
				XMLReader reader = factory.newSAXParser().getXMLReader();
				reader.setContentHandler(new AppUpdateServiceHandler(responseParams));
				reader.parse(source);
				// 解析得到不正常的都取消
				if (responseParams == null) 
				{
					sendError();
					return;
				}
				//获取到正确的结果
				if("0000".equals(responseParams.getResponseCode()))
				{
					AppResult appResult = responseParams.getResponseResult().getAppResult();
					sendSuccess(appResult);
				}
				else
				{
					sendError();
				}
				
			}
			catch(Exception e)
			{
				sendError();;
				return;
			}
			
		}
	}
}