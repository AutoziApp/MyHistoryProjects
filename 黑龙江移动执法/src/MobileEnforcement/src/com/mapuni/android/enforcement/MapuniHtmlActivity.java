package com.mapuni.android.enforcement;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.dataprovider.FileHelper;

/**
 * ���ڴ�html�ļ����а�htmlת��ͼƬ��ӡ���� ��Ҫ����html·�����ʹ��ͼƬ���ļ���·��
 * 
 * @author wanglg
 * 
 */
public class MapuniHtmlActivity extends Activity {
	private WebView wView;
	/** hTml·�� */
	private String url = "";
	/** ���ͼƬ���ļ���·�� */
	private String imageUri = "";
	long NOWTIME = System.currentTimeMillis();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapuni_html_main);
		wView = (WebView) findViewById(R.id.webview);
		/** ֧��webҳ������� */
		wView.getSettings().setBuiltInZoomControls(true);
		/** ֧��javaScript�ű� */
		wView.getSettings().setJavaScriptEnabled(true);
		wView.getSettings().setAllowFileAccess(true);
		wView.getSettings().setUseWideViewPort(true);
		wView.getSettings().setLoadWithOverviewMode(true);
		/** �õ��ϸ����洫�ݵ����� */
		Intent intent = this.getIntent();
		url = intent.getStringExtra("url");
		imageUri = intent.getStringExtra("imageUri");
		wView.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "��ӡ");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			if (htmlSaveToImage(wView)) {
				FileHelper.sendToBluetooth("file://" + imageUri + "/" + NOWTIME + ".png", MapuniHtmlActivity.this);
			}

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	/**
	 * Description: hTml תͼƬ
	 * 
	 * @param wView
	 * @return
	 * @author Administrator
	 * @Create at: 2013-7-12 ����9:34:10
	 */
	public Boolean htmlSaveToImage(WebView wView) {
		Boolean result = false;
		try {
			android.graphics.Picture pic = wView.capturePicture();
			/** �õ�����Ŀ�� */
			int width = pic.getWidth();
			/** �õ�����ĸ߶� */
			int height = pic.getHeight();
			/** ����һ��bitmap */
			Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			/** ��ȡ��������web���棬�����ֻ���Ļ */
			wView.setDrawingCacheEnabled(true);
			wView.getDrawingCache(true);
			pic.draw(canvas);
			/** ����һ���ļ� */
			File file = new File(imageUri, NOWTIME + ".png");
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			result = true;
			Toast.makeText(this, "����ͼƬ�ɹ�", 1).show();
			/** ģ��һ��SD�����صĲ��� */
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
			File sdfile = Environment.getExternalStorageDirectory();
			intent.setData(Uri.fromFile(sdfile));
			sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
			/** �����쳣����toast */
			Toast.makeText(getApplicationContext(), "����ʧ��", 1).show();
		}
		return result;
	}

}
