package com.jy.environment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.webservice.UrlComponent;
/**
 * 公众服务webview展示界面
 * @author baiyuchuan
 *
 */
public class DiscoverPubServiceWebViewActivity extends ActivityBase {
	private WebView wv;
	private ProgressBar news_erji_bar;
	WebSettings settings;
	private Intent intent;
	private TextView news_tv1;
	private String xiaoxi_id, title;
	private String path;
	private ImageView news_iv1;
	int fontSize = 1;
	public static final String TAG ="NewsErActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.discover_pubservice_webview_activity);
		init();

		intent = getIntent();
		xiaoxi_id = intent.getStringExtra("xiaoxi_id");
		title = intent.getStringExtra("title");
		path = UrlComponent.getNewsDetail_Get(xiaoxi_id);
		Log.i(TAG, "消息的路径"+path);
		news_tv1.setText(title);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				news_erji_bar.setVisibility(View.INVISIBLE);
			}

		});
		WebSettings settings = wv.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
//		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		wv.loadUrl(path);
		settings.setJavaScriptEnabled(true);

	}

	private void init() {
		wv = (WebView) findViewById(R.id.news_erji);
		news_iv1 = (ImageView) findViewById(R.id.news_iv1);
		news_tv1 = (TextView) findViewById(R.id.news_tv1);
		news_erji_bar = (ProgressBar) findViewById(R.id.news_erji_bar);
		news_iv1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
