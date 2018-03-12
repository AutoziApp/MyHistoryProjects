package com.jy.environment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;

/**
 * 公众服务webview展示界面
 * 
 * @author baiyuchuan
 * 
 */
public class WebViewActivity extends ActivityBase {
    private WebView wv;
    private ProgressBar news_erji_bar;
    WebSettings settings;
    private Intent intent;
    private TextView news_tv1;
    private String url;
    private ImageView news_iv1;
    int fontSize = 1;
    public static final String TAG = "NewsErActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.discover_pubservice_webview_activity);
	init();

	intent = getIntent();
	url = intent.getStringExtra("url");
	// news_tv1.setText(title);
	wv.setWebViewClient(new WebViewClient() {
	    @Override
	    public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		news_erji_bar.setVisibility(View.INVISIBLE);
	    }

	    @Override
	    public void onReceivedError(WebView view, int errorCode,
		    String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
	    }

	});
	WebSettings settings = wv.getSettings();
	settings.setSupportZoom(true);
	settings.setBuiltInZoomControls(true);
	settings.setUseWideViewPort(true);
	wv.getSettings().setRenderPriority(RenderPriority.HIGH);
	wv.getSettings().setBlockNetworkImage(true);
	wv.setWebChromeClient(new WebChromeClientImpl());
	wv.loadUrl(url);
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
		// onBackPressed();
		finish();
	    }
	});
    }

    private class WebChromeClientImpl extends WebChromeClient {
	@Override
	public void onReceivedTitle(WebView view, String title) {
	    news_tv1.setText(title);
	    super.onReceivedTitle(view, title);
	}

    }

    protected void onResume() {
	super.onResume();
    }

    protected void onDestroy() {
	wv.stopLoading();
	wv.destroy();
	super.onDestroy();
    }

    @Override
    public void onBackPressed() {
	if (wv.canGoBack()) {
	    Log.i("lv", "onBackPressed can go back");
	    wv.goBack(); // goBack()表示返回webView的上一页面
	} else if (!wv.canGoBack()) {
	    finish();
	}
    }

}
