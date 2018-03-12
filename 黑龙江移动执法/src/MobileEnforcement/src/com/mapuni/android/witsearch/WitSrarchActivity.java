package com.mapuni.android.witsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.f;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.taskmanager.TaskInformationActivity;

@SuppressLint("NewApi") public class WitSrarchActivity  extends BaseActivity{
	private LayoutInflater _LayoutInflater;
	private View taskRegisterView;
	private WebView web;
	private String resultResponseObj0;
	private YutuLoading yutuLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "智能搜索");
		_LayoutInflater = LayoutInflater.from(this);

		taskRegisterView = _LayoutInflater.inflate(R.layout.layout_wit_search,
				null);

		// 新加返回按钮 屏蔽同步按钮
		setBackButtonVisiable(true);
		// 设置出现同步抽屉
		setSynchronizeButtonVisiable(true);
		yutuLoading = new YutuLoading(WitSrarchActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.showDialog();
		
		initView();
		
		initData();
		
	}


	private void initView() {
	
		
//		_LayoutInflater = LayoutInflater.from(this);
		
		// 展示出来控件
		((LinearLayout) findViewById(R.id.middleLayout))
				.addView(taskRegisterView);
		web = (WebView) taskRegisterView.findViewById(R.id.web);

	}
	
	
 private void initData() {

	     WebSettings settings = web.getSettings();
	        settings.setJavaScriptEnabled(true);//支持javascript
	       settings.setUseWideViewPort(true);//适配屏幕
	        settings.setLoadWithOverviewMode(true);
	        settings.setBuiltInZoomControls(true);//支持放大缩小
	        settings.setDisplayZoomControls(true);//隐藏放大缩小的按钮
	      
	        settings .setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	        web.setInitialScale(50);
	        web.setWebViewClient(new WebViewClient(){
	        	@Override
	        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        		if (yutuLoading!=null) {
	        			yutuLoading.showDialog();
					}else{
						yutuLoading=new YutuLoading(WitSrarchActivity.this);
						yutuLoading.showDialog();
					}
	        		
	        		view.loadUrl(url);
	        	
	        		view.loadUrl(url);
	        		
	        
	        		
	        		return true;
	        	}
	        	
	           	//加载结束
	            @Override  
              public void onPageFinished(WebView view,String url)  
              {  
	            	yutuLoading.dismissDialog();  
              }
	            //加载开始
	            @Override
	            public void onPageStarted(WebView view, String url,
	            		Bitmap favicon) {
	            	yutuLoading.showDialog();
	            }
	            //加载失败
	            @Override
	            public void onReceivedError(WebView view, int errorCode,
	            		String description, String failingUrl) {
	            	super.onReceivedError(view, errorCode, description, failingUrl);
	            	yutuLoading.dismissDialog(); 
	            
	            }
	        	
	        });
	        
			ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param0 = new HashMap<String, Object>();
		
			param0.put("usercode", Global.getGlobalInstance().getUserid());
			params0.add(param0);
			
			try {
				resultResponseObj0 =(String) WebServiceProvider
						.callWebService(Global.NAMESPACE,
								"GetCometSearchUrl", params0, Global
										.getGlobalInstance()
										.getSystemurl()
										+ Global.WEBSERVICE_URL,
								WebServiceProvider.RETURN_STRING, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        if (!"".equals(resultResponseObj0)&&resultResponseObj0!=null) {
	        	web.loadUrl(resultResponseObj0);
	        	yutuLoading.dismissDialog();
			}else{
			OtherTools.showToast(WitSrarchActivity.this, "网络异常,请检查网络设置!");
				web.setVisibility(View.GONE);
				yutuLoading.dismissDialog();
			}
	        
		//web.loadUrl(witUrl+Global.getGlobalInstance().getUserid());
	        
	}
 
 @Override
 public boolean onKeyDown(int keyCode, KeyEvent event) {
     // 设置webview页面时设置后退时，回退到上一web页面。
     if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
  	   web.goBack();
         return true;
     }
     return super.onKeyDown(keyCode, event);
 }
 
	@Override
    protected void onDestroy() {
        super.onDestroy();
        web.setVisibility(View.GONE);
         
    }
}
