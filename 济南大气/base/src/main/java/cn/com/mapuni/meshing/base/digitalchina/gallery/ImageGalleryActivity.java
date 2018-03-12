package cn.com.mapuni.meshing.base.digitalchina.gallery;

//import com.digitalchina.gallery.R;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.util.LogUtil;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ImageGalleryActivity extends Activity {
	// ��Ļ���
	public static int screenWidth;
	// ��Ļ�߶�
	public static int screenHeight;
	private ImageGallery gallery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image_gallery);
		ArrayList<String> arraylist = (ArrayList<String>) getIntent().getExtras().get("arrayTotal" );
		String attch = getIntent().getExtras().getString("attch");
		screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
		gallery = (ImageGallery) findViewById(R.id.gallery);
		gallery.setVerticalFadingEdgeEnabled(false);// ȡ����ֱ����߿�
		gallery.setHorizontalFadingEdgeEnabled(false);// ȡ��ˮƽ����߿�
		gallery.setAdapter(new GalleryAdapter(this,arraylist,attch));
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		LogUtil.i("manager", "onConfigurationChanged...");
		screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
		super.onConfigurationChanged(newConfig);
	}
	 

	
}