package com.konka.appupdate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 应用获取最新版本实例
 * @author mj
 *
 */
public class AppUpdateDemoActivity extends Activity  implements OnClickListener
{
	private Button getCurretnVersionBtn;
	private TextView appVersionTv;
	private TextView appVersionInfoTv;
	private TextView downloadUrlTv;
	private String apk_name;
	private String apk_version;
	
	private ProgressDialog progressDialog = null;   
	
	private HttpClientUtil httpClientUtil;
	
	public static final String SERVER_URL = "http://dxkk.kkapks.com/apk/index.php";//测试统一升级服务地址
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
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
    	
    	
    	apk_name = this.getPackageName();
    	apk_version = getVersionName();

    	appVersionTv.setText("当前应用名：" + apk_version);
    	appVersionInfoTv.setText("当前版本号：" + apk_name);
    	
    	getCurretnVersionBtn.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			// creating new product in background thread
    			new AccessUpdateServer().execute();
    		}
    	});    	
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
    

	@Override
	public void onClick(View view)
	{

	}
	
	class AccessUpdateServer extends AsyncTask<String, String, String> {
		
		private String apk_name_new ;
		private String apk_version_new ;
		private String apk_url_new ;

		private HttpPost request = new HttpPost(SERVER_URL);
		/**
		 * Before starting background thread
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * get server info using json
		 * */
		protected String doInBackground(String... args) {			
			try {
				// 先封装一个 JSON 对象  
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("apk_name", apk_name + ""));
				params.add(new BasicNameValuePair("apk_version", apk_version + ""));
				
				request.setEntity(new UrlEncodedFormEntity(params));
				Log.d("url", request.getRequestLine().toString());
				// 发送请求  
				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
				
				// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
				String retSrc = EntityUtils.toString(httpResponse.getEntity());
				Log.d("Create 2", retSrc);
				// 生成 JSON 对象  
				JSONObject result = new JSONObject( retSrc);  
			
				apk_name_new = result.getString("apk_name");
				apk_version_new = result.getString("apk_version");
				apk_url_new = result.getString("apk_url");
				String success =  result.getString("success");
				String update =  result.getString("update");
				
				Log.d("Create end", apk_name_new);
				Log.d("Create end", apk_version_new);
				Log.d("Create end", apk_url_new);
				Log.d("Create end", success);
				Log.d("Create end", update);
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			new AlertDialog.Builder(AppUpdateDemoActivity.this)  
			.setTitle("服务器返回信息")  
			.setItems(new String[] {"apk_name_new: "+apk_name_new,"apk_version_new: "+apk_version_new,"apk_url_new: "+apk_url_new,}, null)  
			.setNegativeButton("确定", null)  
			.show();
		}

	}
}