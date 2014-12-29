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
 * Ӧ�û�ȡ���°汾ʵ��
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
	
	public static final String SERVER_URL = "http://unionupdate.kkpush.net/UnionUpdateService";//����ͳһ���������ַ
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initUtil();
    }

    /**
     * ��ʼ��ui
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
    	appVersionTv.setText("�汾�ţ�" + nowVersion);
    	appVersionInfoTv.setText("�汾��Ϣ��" + packageName);
    	
    }
    
    private String getVersionName(){  
        //��ȡpackagemanager��ʵ��
        PackageManager packageManager = getPackageManager();  

        //getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
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
     * ��ʼ������
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
			progressDialog = ProgressDialog.show(this,"��ȴ�...", "���ڻ�ȡ�汾 ...", true);  
			new Thread(new GetAppCurrentVersionThread()).start();
		}
	}
	
	
	private Handler handler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			progressDialog.dismiss();
			//��ȡ�汾�ɹ�
			if (msg.what == 1) 
			{
				Bundle b = msg.getData();
        		String AppVersion = b.getString("AppVersion");
        		String AppVersionInfo = b.getString("AppVersionInfo");
        		String DownloadUrl = b.getString("DownloadUrl");
        		appVersionTv.setText("�汾�ţ�" + AppVersion);
        		appVersionInfoTv.setText("�汾��Ϣ��" + AppVersionInfo);
        		downloadUrlTv.setText("�汾���ص�ַ��" + DownloadUrl);
			}
			else if (msg.what == 2)// ��������,��ʾ�û�
			{
				Toast.makeText(AppUpdateDemoActivity.this, "��ȡ�汾ʧ��", Toast.LENGTH_LONG).show();
			}
		}
	};
	/**
	 * ֪ͨ�ɹ�
	 * @param appResult  Ӧ�ý��
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
	 * ֪ͨʧ��
	 */
	private void sendError()
	{
		Message message = Message.obtain();
		message.what = 2;
		handler.sendMessage(message);
	}
	/**
	 * ��ȡ�汾�߳�
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
				String param = "";//������Ҫ���ݵĲ���
				param = AppUpdateServiceRequestTemplate.getAppCurrentVersionParams("","","","","");//��ȡxml����
				param = URLEncoder.encode(param, "utf-8"); //��������
				params.put("param", param);
				xmlResult = httpClientUtil.getPostRequestResult(params);//�õ�xml����ֵ

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
				// �����汾��Ϣxml
				XMLReader reader = factory.newSAXParser().getXMLReader();
				reader.setContentHandler(new AppUpdateServiceHandler(responseParams));
				reader.parse(source);
				// �����õ��������Ķ�ȡ��
				if (responseParams == null) 
				{
					sendError();
					return;
				}
				//��ȡ����ȷ�Ľ��
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