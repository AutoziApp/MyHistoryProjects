package com.mapuni.android.ui;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;

public class ScanningFileActivity extends BaseActivity {
	GridView gridImage;
	TextView fileName;
	String[] imageList;
	Bitmap[] bitmaps;
	ImageAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		gridImage = new GridView(this);
		fileName = new TextView(this);
		/** 设置九宫格的列数*/
		gridImage.setNumColumns(3);
		/** 设置九宫格的行距*/
		gridImage.setVerticalSpacing(40);
		
		RelativeLayout linearLayout = (RelativeLayout) this
				.findViewById(R.id.parentLayout);
		super.SetBaseStyle(linearLayout, "扫描文件");
		super.setTitleLayoutVisiable(true);
		
		LinearLayout middleLayout = (LinearLayout) this.findViewById(R.id.middleLayout);
		
		
		
		File imageFile = new File("/mnt/sdcard/test");
		imageList = imageFile.list();
		if(imageList == null || imageList.length == 0) {
			Toast.makeText(this, "未找到扫描文件！", 0).show();
			finish();
		}
		adapter = new ImageAdapter();
		bitmaps = getSuoLueTuSS(imageList);
		gridImage.setAdapter(adapter);
		gridImage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ScanningFileActivity.this,ImageViewActivity.class);
				intent.putExtra("FileName", imageList[arg2]);
				startActivity(intent);
			}
		});
		middleLayout.addView(gridImage);
	}
	private Bitmap[] getSuoLueTuSS(String[] fileName) {
		Bitmap[] bitmaps = new Bitmap[fileName.length];
		for (int i = 0; i < fileName.length; i++) {
			bitmaps[i] = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile("/mnt/sdcard/test/" + fileName[i]), 100, 120);
		}
		return bitmaps;
	}
	
	
	class ImageAdapter extends BaseAdapter {

		LayoutInflater inflater;
		public ImageAdapter() {
			inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {

			return imageList.length;
		}

		@Override
		public Object getItem(int position) {
			return imageList[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout lin = new LinearLayout(ScanningFileActivity.this);
			lin.setOrientation(LinearLayout.VERTICAL);
			lin.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
			ImageView image = new ImageView(ScanningFileActivity.this);
			TextView fileName = new TextView(ScanningFileActivity.this);
			fileName.setText(imageList[position]);
			fileName.setSingleLine();
			fileName.setTextColor(android.graphics.Color.BLACK);
			fileName.setTextSize(18);
			fileName.setGravity(Gravity.CENTER_HORIZONTAL);
			image.setImageBitmap(bitmaps[position]);
			lin.addView(image);
			lin.addView(fileName);
			return lin;
		}

	}
}