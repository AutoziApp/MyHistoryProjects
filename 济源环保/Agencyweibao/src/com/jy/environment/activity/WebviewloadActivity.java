package com.jy.environment.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.util.MyLog;

public class WebviewloadActivity extends Activity {

	private WebView webView;
	private ImageView finish_weibaoshop;
	private TextView webview_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webview_title = (TextView) findViewById(R.id.webview_title);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		MyLog.i(">>>>>>url"+url);
		CookieSyncManager.createInstance(this); 
		CookieSyncManager.getInstance().startSync(); 
		CookieManager.getInstance().removeSessionCookie(); 
		
		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}

		});
		webView.setWebChromeClient(new WebChromeClientImpl());
		WebSettings settings = webView.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(false);
		settings.setJavaScriptEnabled(true);
		settings.setAllowFileAccess(true); 
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.requestFocusFromTouch();
//		webView.requestFocus();
		webView.setDownloadListener(new DownloadListener() {  
	            @Override  
	            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {  
	                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载  
	                Uri uri = Uri.parse(url);  
	                Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	                startActivity(intent);  
	            }  
	        });  
//		webView.setWebViewClient(new WebViewClient() {
////			@Override
////			public boolean shouldOverrideUrlLoading(WebView view, String url) {
////				Log.i("bai", "shouldOverrideUrlLoading + url :" + url);
////				if (url.indexOf("tel:") < 0) {// 页面上有数字会导致连接电话
////					view.loadUrl(url);
////					return false;
////				}else{
////					return super.shouldOverrideUrlLoading(view, url);
////				}
//////				return true;
////			}
//
//			@Override
//			public void onReceivedHttpAuthRequest(WebView view,
//					HttpAuthHandler handler, String host, String realm) {
//				Log.i("bai", "onReceivedHttpAuthRequest + realm :" + realm);
//				super.onReceivedHttpAuthRequest(view, handler, host, realm);
//			}
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				Log.i("bai", "onPageFinished + url :" + url);
//				super.onPageFinished(view, url);
//			}
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//			}
//
//			@Override
//			public void onLoadResource(WebView view, String url) {
//				Log.i("bai", "onLoadResource + url :" + url);
//					super.onLoadResource(view, url);
//			}
//
//		});

		webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

		// settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.loadUrl(url);
		
		finish_weibaoshop = (ImageView) findViewById(R.id.finish_weibaoshop);
		finish_weibaoshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (webView.canGoBack()) {
//					webView.goBack();
//				} else {
					finish();
//				}
			}
		});
	}

	private class WebChromeClientImpl extends WebChromeClient {
		@Override
		public void onReceivedTitle(WebView view, String title) {
			webview_title.setText(title);
			super.onReceivedTitle(view, title);
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		
		webView.removeAllViews();
		webView.clearCache(true);
		webView.clearHistory();
		webView.setVisibility(View.GONE);
		webView.destroy();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		} else {
			finish();
			return false;
		}
	}

	// Web视图
//	private class URLWebViewClient extends WebViewClient {
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			view.loadUrl(url);
//			return true;
//		}
//	}

}
