package cn.com.mapuni.meshing.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.com.mapuni.meshing.base.util.DisplayUitl;

/**
 * FileName: AboutActivity.java
 * Description:关于系统页面
 * @author 王振洋
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午9:06:21
 */
public class AboutActivity extends BaseActivity {
	// 父页面布局
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	
	private  final int SCROLLBARS_OUTSIDE_OVERLAY = 0;
	private WebView mWebView;
	private Handler mHandler;
	private WebSettings mWebSettings;
	private String thehtmltitle = "详细";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//setContentView(R.layout.about);
		setContentView(R.layout.ui_mapuni);
		//父类布局
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
		linearLayout.setVisibility(View.GONE);
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
	
		//设置页面标题
		Intent intent = getIntent();
		thehtmltitle = intent.getStringExtra("title");	
		SetBaseStyle(fatherRelativeLayout, thehtmltitle);
		
		mWebView = new WebView(this);//(WebView) this.findViewById(R.id.yutuWebViewer);
		middleLayout.addView(mWebView);
		mHandler = new Handler();

		// 设置支持JavaScript、不支持缩放等
		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setBuiltInZoomControls(false);
		mWebSettings.setLightTouchEnabled(true);
		mWebSettings.setSupportZoom(false);
		mWebView.setHapticFeedbackEnabled(false);
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		//打开插件   控制可以播放Flash文件
//		mWebSettings.setPluginsEnabled(true);
//		mWebSettings.setPluginState(WebSettings.PluginState.OFF);
		// mWebView.setInitialScale(0); // 改变这个值可以设定初始大小

		// 重要,用于与页面交互!

		mWebView.addJavascriptInterface(new Object() {
			@SuppressWarnings("unused")
			public void oneClick(final String locX, final String locY) {// 此处的参数可传入作为js参数
				mHandler.post(new Runnable() {
					public void run() {
						mWebView.loadUrl("javascript:shows('" 
								+ locX + "','"+ locY 
								+ "')");
					}
				});
			}
		}, "demo");// 此名称在页面中被调用,方法如下:

		final String mimeType = "text/html";
		final String encoding = "utf-8";
		mWebView.loadData(getData(), mimeType, encoding);
	}
	
	/**
	 * Description:从assets中的about_1.txt、about_2.txt中读取内容，并组装成要显示的页面内容
	 * @return String 显示内容
	 * @author 王振洋
	 * Create at: 2012-12-3 上午10:10:10
	 */
	private String getData(){
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(getResources().getAssets().open("about_1.txt"),"GBK"));
			BufferedReader br2=new BufferedReader(new InputStreamReader(getResources().getAssets().open("about_2.txt"),"GBK"));
			String line="";
			String line2="";
			StringBuilder sb=new StringBuilder();
			StringBuilder sb2=new StringBuilder();
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			while((line2=br2.readLine())!=null){
				sb2.append(line2);
			}
			String s=new String(sb.toString().getBytes("GBK"), "utf-8");
			String s1=new String(sb2.toString().getBytes("GBK"), "utf-8");
			String versonname= DisplayUitl.getName(this);
			String result=s+versonname+s1;
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			
			return result;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}