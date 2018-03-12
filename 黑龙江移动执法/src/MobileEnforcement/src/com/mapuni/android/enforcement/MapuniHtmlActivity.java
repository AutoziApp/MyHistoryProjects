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
 * 用于打开html文件，有把html转车图片打印功能 需要传入html路径，和存放图片的文件夹路径
 * 
 * @author wanglg
 * 
 */
public class MapuniHtmlActivity extends Activity {
	private WebView wView;
	/** hTml路径 */
	private String url = "";
	/** 存放图片的文件夹路径 */
	private String imageUri = "";
	long NOWTIME = System.currentTimeMillis();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapuni_html_main);
		wView = (WebView) findViewById(R.id.webview);
		/** 支持web页面的缩放 */
		wView.getSettings().setBuiltInZoomControls(true);
		/** 支持javaScript脚本 */
		wView.getSettings().setJavaScriptEnabled(true);
		wView.getSettings().setAllowFileAccess(true);
		wView.getSettings().setUseWideViewPort(true);
		wView.getSettings().setLoadWithOverviewMode(true);
		/** 得到上个界面传递的数据 */
		Intent intent = this.getIntent();
		url = intent.getStringExtra("url");
		imageUri = intent.getStringExtra("imageUri");
		wView.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "打印");
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
	 * Description: hTml 转图片
	 * 
	 * @param wView
	 * @return
	 * @author Administrator
	 * @Create at: 2013-7-12 上午9:34:10
	 */
	public Boolean htmlSaveToImage(WebView wView) {
		Boolean result = false;
		try {
			android.graphics.Picture pic = wView.capturePicture();
			/** 得到界面的宽度 */
			int width = pic.getWidth();
			/** 得到界面的高度 */
			int height = pic.getHeight();
			/** 穿件一个bitmap */
			Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			/** 获取的是整个web界面，不是手机屏幕 */
			wView.setDrawingCacheEnabled(true);
			wView.getDrawingCache(true);
			pic.draw(canvas);
			/** 创建一个文件 */
			File file = new File(imageUri, NOWTIME + ".png");
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			result = true;
			Toast.makeText(this, "保存图片成功", 1).show();
			/** 模拟一个SD卡挂载的操作 */
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
			File sdfile = Environment.getExternalStorageDirectory();
			intent.setData(Uri.fromFile(sdfile));
			sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
			/** 出现异常给个toast */
			Toast.makeText(getApplicationContext(), "保存失败", 1).show();
		}
		return result;
	}

}
