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
 * Description:��ʾ��ҳ��ҳ��
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����11:24:04
 */
public class WebViewer extends BaseActivity {
	private  final int SCROLLBARS_OUTSIDE_OVERLAY = 0;
	private WebView mWebView;
	private Handler mHandler;
	private WebSettings mWebSettings;
	private String thehtml = "error.html"; 
	private String thehtmltitle = "��ϸ";  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		RelativeLayout parentLayout=(RelativeLayout) findViewById(R.id.parentLayout);
	
		// ��ȡ������VideoView���
		// ������������⡢ҳ�����ƣ����ؾ�̬HTML
		Intent intent = getIntent();
		thehtml = intent.getStringExtra("name");
		thehtmltitle = intent.getStringExtra("title");
		if(thehtmltitle!=null &&!thehtmltitle.equals("����ϵͳ")){
			SetBaseStyle(parentLayout, thehtmltitle);
		}else{//����ϵͳ�����
			SetBaseStyle(parentLayout, "����ϵͳ");
			rewriteSyncImgListener();
		}
		
		setSynchronizeButtonVisiable(false);
		mWebView = (WebView) this.findViewById(R.id.yutuWebViewer);
		mHandler = new Handler();
 
		// ����֧��JavaScript��
		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);//֧�ֽű�
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setLightTouchEnabled(true);//
		mWebView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		//�򿪲��   ���ƿ��Բ���Flash�ļ�
//		mWebSettings.setPluginsEnabled(true);
		mWebSettings.setPluginState(PluginState.ON);

		// mWebView.setInitialScale(0); // �ı����ֵ�����趨��ʼ��С
 
		
		if(null==thehtml){//�����ж��Ƿ�Ϊ������������ݡ�
			String otherstr=intent.getStringExtra("otherstr");
			mWebView.loadDataWithBaseURL("",otherstr,"text/html", "utf-8", "");
		}else{
		// ��Ҫ,������ҳ�潻��!
		mWebView.addJavascriptInterface(new Object() {
			@SuppressWarnings("unused")
			public void oneClick(final String locX, final String locY) {// �˴��Ĳ����ɴ�����Ϊjs����
				mHandler.post(new Runnable() {
					public void run() {
						mWebView.loadUrl("javascript:shows('" + locX + "','"
								+ locY + "')");
					}
				});
			}
		}, "demo");// ��������ҳ���б�����,��������:
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
	 * Description: ����ϵͳ����ʱ���ı����Ͻǰ�ťͼƬ�ͼ����¼�
	 * 
	 * @author Administrator
	 * @return 
	 * @Create at: 2013-4-7 ����5:16:36
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