package com.mapuni.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.netprovider.Net;

/**
 * FileName: WebViewFormActivity.java
 * Description:����HTMLҳ�桢��
 * @author Liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-5 ����04:16:56
 */
public class WebViewFormActivity extends BaseActivity {
	/** ��ҳ�沼��*/
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	private WebView webViewForm;
	
	private String url = "";
	private String title = "";
	private String otherstr = "";
	
	private  final int STATUS_URL = 0;
	private  final int STATUS_DATA = 1;
	private  final int LOCALFILE_URL = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.ui_mapuni);

		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		otherstr = getIntent().getStringExtra("otherstr");
		if(otherstr != null) {
			initViews(STATUS_DATA);
		} else if(url != null) {
			if(Net.checkNet(this) && Net.checkURL(url)){
				initViews(STATUS_URL);
				
			}else {
				initViews(LOCALFILE_URL);
			}
		}
	}

	/**
	 * Description:��ʼ�����棬��������
	 * @param loadType	0 URL���ӣ�1HTML�ַ���
	 * @author Administrator<br>
	 * Create at: 2012-12-5 ����04:17:58
	 */
	private void initViews(int loadType) {
		//���಼��
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
		linearLayout.setVisibility(View.GONE);
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		SetBaseStyle(fatherRelativeLayout, title);

		webViewForm = new WebView(this);
		webViewForm.setScrollBarStyle(0);
		WebSettings settings = webViewForm.getSettings();
		settings = webViewForm.getSettings();
		settings.setJavaScriptEnabled(true);// ֧�ֽű�
		settings.setBuiltInZoomControls(true);
		settings.setLightTouchEnabled(true);//
		webViewForm.setScrollBarStyle(0);
		// �򿪲�� ���ƿ��Բ���Flash�ļ�
//		settings.setPluginsEnabled(true);
		settings.setPluginState(PluginState.ON);

		webViewForm.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		middleLayout.addView(webViewForm);

		switch (loadType) {
		case STATUS_URL://����url
			if (url != null && !url.equals("") && title != null) {
//				SetBaseStyle(fatherRelativeLayout, title);
				setTitleLayoutVisiable(true);
				if (Net.checkNet(this) && Net.checkURL(url)) {
//					webViewForm.loadUrl(url);	
					webViewForm.loadUrl(url);	
					
				} else {
//					setFailureHtmlFile(webViewForm);
					middleLayout.removeAllViews();
					YutuLoading yutuLoading = new YutuLoading(this);
					yutuLoading.setLoadMsg("", "����ʧ��");
					yutuLoading.setFailed();
					middleLayout.setGravity(Gravity.CENTER);
					middleLayout.addView(yutuLoading,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				}
			}
			break;
		case STATUS_DATA://��������
			webViewForm.loadDataWithBaseURL("", otherstr, "text/html", "utf-8", "");
			break;
		case LOCALFILE_URL://���ر����ļ�·��
			if (url != null && !url.equals("") && title != null) {
				setTitleLayoutVisiable(true);
					webViewForm.loadUrl("file:///mnt"+url);	
				} else {
					middleLayout.removeAllViews();
					YutuLoading yutuLoading = new YutuLoading(this);
					yutuLoading.setLoadMsg("", "����ʧ��");
					yutuLoading.setFailed();
					middleLayout.setGravity(Gravity.CENTER);
					middleLayout.addView(yutuLoading,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				}
			break;
		default:
			break;
		}	
	}

	/**
	 * Description:�滻����ʧ�ܵ�ҳ��
	 * @param webView
	 * @author Administrator<br>
	 * Create at: 2012-12-5 ����04:22:40
	 */
	private void setFailureHtmlFile(WebView webView) {
		WebSettings settings = webView.getSettings();
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		// webView.setInitialScale(80);
		webView.loadUrl("file:///android_asset/nocontent.html");
	}

	/**
	 * ����
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewForm.canGoBack()) {
			webViewForm.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
