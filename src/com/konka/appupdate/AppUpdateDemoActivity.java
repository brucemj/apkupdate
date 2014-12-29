package com.konka.appupdate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
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
 * @author jan
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
	
	JSONParser jsonParser = new JSONParser();
	
	public static final String SERVER_URL = "http://unionupdate.kkpush.net/UnionUpdateService";//测试统一升级服务地址
	private static String url_create_product = "http://dxkk.kkapks.com/apk";
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private ProgressDialog pDialog;
		
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
    	
    	
    	apk_name = this.getPackageName();
    	apk_version = getVersionName();
    	appVersionTv.setText("版本号：" + apk_version);
    	appVersionInfoTv.setText("版本信息：" + apk_name);
    	
    	getCurretnVersionBtn.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			// creating new product in background thread
    			new CreateNewProduct().execute();
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
			//new Thread(new GetAppCurrentVersionThread()).start();
		}
	}
	
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AppUpdateDemoActivity.this);
			pDialog.setMessage("Creating Product..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("apk_name", apk_name + ""));
			params.add(new BasicNameValuePair("apk_version", apk_version + ""));
			

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"GET", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					//Intent i = new Intent(getApplicationContext(),
					//		AllProductsActivity.class);
					//startActivity(i);
					Log.d("Create Response2", "success2");
					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
}