package com.mapuni.android.base;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.mapuni.android.base.business.LSGXXX;

/**
 * FileName: WebViewer.java
 * Description:显示网页的页面
 * @author 王振洋
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 上午11:24:04
 */
public class WebViewer extends BaseActivity {
	private  final int SCROLLBARS_OUTSIDE_OVERLAY = 0;
	private WebView mWebView;
	private Handler mHandler;
	private WebSettings mWebSettings;
	private String thehtml = "error.html"; 
	private String thehtmltitle = "详细";  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		RelativeLayout parentLayout=(RelativeLayout) findViewById(R.id.parentLayout);
	
		// 获取界面上VideoView组件
		// 传入参数：标题、页面名称，加载静态HTML
		Intent intent = getIntent();
		thehtml = intent.getStringExtra("name");
		thehtmltitle = intent.getStringExtra("title");
		if(thehtmltitle!=null &&!thehtmltitle.equals("关于系统")){
			SetBaseStyle(parentLayout, thehtmltitle);
		}else{//关于系统令当处理
			SetBaseStyle(parentLayout, "关于系统");
			rewriteSyncImgListener();
		}
		
		setSynchronizeButtonVisiable(false);
		mWebView = (WebView) this.findViewById(R.id.yutuWebViewer);
		mHandler = new Handler();
 
		// 设置支持JavaScript等
		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);//支持脚本
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setLightTouchEnabled(true);//
		mWebView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		//打开插件   控制可以播放Flash文件
//		mWebSettings.setPluginsEnabled(true);
		mWebSettings.setPluginState(PluginState.ON);

		// mWebView.setInitialScale(0); // 改变这个值可以设定初始大小
 
		
		if(null==thehtml){//用于判断是否为“其他相关内容”
			String otherstr=intent.getStringExtra("otherstr");
			mWebView.loadDataWithBaseURL("",otherstr,"text/html", "utf-8", "");
		}else{
		// 重要,用于与页面交互!
		mWebView.addJavascriptInterface(new Object() {
			@SuppressWarnings("unused")
			public void oneClick(final String locX, final String locY) {// 此处的参数可传入作为js参数
				mHandler.post(new Runnable() {
					public void run() {
						mWebView.loadUrl("javascript:shows('" + locX + "','"
								+ locY + "')");
					}
				});
			}
		}, "demo");// 此名称在页面中被调用,方法如下:
		// <body >

		String htmlPath = "file:///android_asset/"+thehtml;
		String baseUrl = "file:///android_asset";
		final String mimeType = "text/html";
		final String encoding = "utf-8";
		final String fileName = thehtml;
		mWebView.loadDataWithBaseURL(baseUrl, fileName, mimeType, encoding, null);
		mWebView.loadUrl(htmlPath);
		}
		
		
	/*	img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle=new Bundle();
				Intent intent=null;
				LSGXXX lsgxxx;
				try {
					lsgxxx = (LSGXXX) ObjectFactory
							.createObject(LSGXXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", lsgxxx);
				bundle.putBoolean("IsShowTitle", true);
				bundle.putBoolean("IsShowSyncBtn", false);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				lsgxxx.setFilter(filterMap);
				intent = new Intent(WebViewer.this, DailogListActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
//				finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		
	}
	/**
	 * Description: 关于系统跳进时，改变右上角按钮图片和监听事件
	 * 
	 * @author Administrator
	 * @return 
	 * @Create at: 2013-4-7 下午5:16:36
	 */
	public void rewriteSyncImgListener(){
		syncImg.setImageResource(R.drawable.icon_liebiao);
		syncImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle=new Bundle();
				Intent intent=null;
				
				
				 LSGXXX	lsgxxx = new LSGXXX();
				bundle.putSerializable("BusinessObj", lsgxxx);
				bundle.putBoolean("IsShowTitle", true);
				bundle.putBoolean("IsShowSyncBtn", false);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				lsgxxx.setFilter(filterMap);
				intent = new Intent(WebViewer.this, DailogListActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}
}