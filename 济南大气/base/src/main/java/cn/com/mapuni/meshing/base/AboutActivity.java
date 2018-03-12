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
 * Description:����ϵͳҳ��
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����9:06:21
 */
public class AboutActivity extends BaseActivity {
	// ��ҳ�沼��
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	
	private  final int SCROLLBARS_OUTSIDE_OVERLAY = 0;
	private WebView mWebView;
	private Handler mHandler;
	private WebSettings mWebSettings;
	private String thehtmltitle = "��ϸ";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//setContentView(R.layout.about);
		setContentView(R.layout.ui_mapuni);
		//���಼��
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
		linearLayout.setVisibility(View.GONE);
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
	
		//����ҳ�����
		Intent intent = getIntent();
		thehtmltitle = intent.getStringExtra("title");	
		SetBaseStyle(fatherRelativeLayout, thehtmltitle);
		
		mWebView = new WebView(this);//(WebView) this.findViewById(R.id.yutuWebViewer);
		middleLayout.addView(mWebView);
		mHandler = new Handler();

		// ����֧��JavaScript����֧�����ŵ�
		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setBuiltInZoomControls(false);
		mWebSettings.setLightTouchEnabled(true);
		mWebSettings.setSupportZoom(false);
		mWebView.setHapticFeedbackEnabled(false);
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		//�򿪲��   ���ƿ��Բ���Flash�ļ�
//		mWebSettings.setPluginsEnabled(true);
//		mWebSettings.setPluginState(WebSettings.PluginState.OFF);
		// mWebView.setInitialScale(0); // �ı����ֵ�����趨��ʼ��С

		// ��Ҫ,������ҳ�潻��!

		mWebView.addJavascriptInterface(new Object() {
			@SuppressWarnings("unused")
			public void oneClick(final String locX, final String locY) {// �˴��Ĳ����ɴ�����Ϊjs����
				mHandler.post(new Runnable() {
					public void run() {
						mWebView.loadUrl("javascript:shows('" 
								+ locX + "','"+ locY 
								+ "')");
					}
				});
			}
		}, "demo");// ��������ҳ���б�����,��������:

		final String mimeType = "text/html";
		final String encoding = "utf-8";
		mWebView.loadData(getData(), mimeType, encoding);
	}
	
	/**
	 * Description:��assets�е�about_1.txt��about_2.txt�ж�ȡ���ݣ�����װ��Ҫ��ʾ��ҳ������
	 * @return String ��ʾ����
	 * @author ������
	 * Create at: 2012-12-3 ����10:10:10
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