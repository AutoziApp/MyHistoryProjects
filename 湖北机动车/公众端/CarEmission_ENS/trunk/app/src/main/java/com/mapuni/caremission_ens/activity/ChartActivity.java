package com.mapuni.caremission_ens.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mapuni.caremission_ens.R;

import java.util.ArrayList;

public class ChartActivity extends BaseActivity {
    private WebView mWeb;
    private ArrayList<String> values;
    private  ArrayList<String> laberX;
    private String grain;
    private String url;
    String[] s = new String[]{"2016-10-12 00:00:00","2016-10-12 01:00:00","2016-10-12 02:00:00"};
    double[] data = new double[]{1.156812,2.458956,1.526895};
    public int i = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent _Intent = getIntent();
        url = (String) _Intent.getExtras().get("url");
//        values = (ArrayList<String>) ((ArrayList) _Intent.getExtras().get("value")).get(0);
//        laberX = (ArrayList<String>) ((ArrayList) _Intent.getExtras().get("date")).get(0);
//        grain = (String) _Intent.getExtras().get("grain");
//        String title = (String) _Intent.getExtras().get("title");
        initWeb();
    }
    @JavascriptInterface
    public void toJS(String s){
       Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public int  getDataSize(){
        return values.size();
    }

    public void initWeb(){
        mWeb= (WebView) findViewById(R.id.webView);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(8 * 1024 * 1024);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
//        加入JS接口
        mWeb.addJavascriptInterface(this,"javatojs");
        mWeb.loadUrl(url);

        mWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
//                  向js传入json
//                mWeb.loadUrl("javascript:load_web("+i+")");
            }
        });
    }
}
