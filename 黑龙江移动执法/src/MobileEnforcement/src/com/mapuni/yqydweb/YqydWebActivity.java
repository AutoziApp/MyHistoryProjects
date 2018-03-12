package com.mapuni.yqydweb;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.witsearch.WitSrarchActivity;

@SuppressLint("NewApi")
 public class YqydWebActivity extends BaseActivity {

	private LayoutInflater _LayoutInflater;
	private View taskRegisterView;
	private WebView web;
	private String resultResponseObj0;
	private YutuLoading yutuLoading;


	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
//
//			if (boo) {
//				boo = false;
//				//initData();
//				String string = msg.obj.toString();
//				web.loadUrl(string);
//			}

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "һ��һ����Ϣ");
		_LayoutInflater = LayoutInflater.from(this);

		taskRegisterView = _LayoutInflater.inflate(R.layout.layout_wit_search,
				null);

		// �¼ӷ��ذ�ť ����ͬ����ť
		setBackButtonVisiable(true);
		// ���ó���ͬ������
		setSynchronizeButtonVisiable(true);
		yutuLoading = new YutuLoading(YqydWebActivity.this);
		yutuLoading.setCancelable(true);
	//	yutuLoading.showDialog();
		initView();

		initData();
	}

	 private void initView() {
		// չʾ�����ؼ�
		((LinearLayout) findViewById(R.id.middleLayout))
				.addView(taskRegisterView);
		web = (WebView) taskRegisterView.findViewById(R.id.web);

		WebSettings settings = web.getSettings();

		settings.setJavaScriptEnabled(true);// ֧��javascript
		settings.setUseWideViewPort(true);// ������Ļ
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);// ֧�ַŴ���С
		settings.setDisplayZoomControls(true);// ���طŴ���С�İ�ť

		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		web.setInitialScale(50);

		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				// if (yutuLoading!=null&&!yutuLoading.isShown()) {
				yutuLoading.showDialog();
				// }
				//
				return true;
			}

			// ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url) {
				yutuLoading.dismissDialog();
				
//				if (boo) {
//					boo=false;
//				initData();
//				}
			}

			// ���ؿ�ʼ
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				yutuLoading.showDialog();
			}

			// ����ʧ��
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				yutuLoading.dismissDialog();

			}

		});
	}

	private void initData() {

		String qyid = getIntent().getStringExtra("qyid");

		ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> param0 = new HashMap<String, Object>();

		param0.put("pollutionsourcecode", qyid);
		param0.put("userid", Global.getGlobalInstance().getUserid());
		params0.add(param0);

		try {
			resultResponseObj0 = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, "GetPollutionSourceUrl", params0, Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!"".equals(resultResponseObj0) && resultResponseObj0 != null) {

		//	yutuLoading.showDialog();
			web.loadUrl(resultResponseObj0);
	

		//	web.loadUrl(resultResponseObj0);
		} else {
			OtherTools.showToast(YqydWebActivity.this, "�����쳣,������������!");
			web.setVisibility(View.GONE);
			yutuLoading.dismissDialog();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ����webviewҳ��ʱ���ú���ʱ�����˵���һwebҳ�档
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
