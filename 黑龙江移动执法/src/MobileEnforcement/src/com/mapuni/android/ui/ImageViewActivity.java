package com.mapuni.android.ui;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.imageview.ScrollZoomImageView;

/**
 * 
 * @author whj
 * 
 */
public class ImageViewActivity extends BaseActivity {

	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;
	private LinearLayout bottomLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
		SetBaseStyle(fatherRelativeLayout, "扫描图片操作");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScrollZoomImageView imageView = new ScrollZoomImageView(this);
		Intent intent = getIntent();
		final String imageFileName = intent.getStringExtra("FileName");
		Bitmap bigImage = BitmapFactory.decodeFile("/mnt/sdcard/test/"
				+ imageFileName);
		imageView.setImageBitmap(bigImage);

		LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		middleLayout.addView(imageView, param);

		bottomLayout.setVisibility(View.VISIBLE);

		bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		bottomLayout.setVisibility(View.VISIBLE);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = (int) (dm.widthPixels / (double) 2);

		// 上传
		Button upLoad = new Button(this);
		upLoad = new Button(this);
		upLoad.setBackgroundResource(R.drawable.btn_shap);
		upLoad.setText("上传");
		upLoad.setWidth(1);
		upLoad.setId(2);
		upLoad.setGravity(Gravity.CENTER);
		upLoad.setTextColor(android.graphics.Color.WHITE);
		upLoad.setVisibility(View.VISIBLE);
		upLoad.getPaint().setFakeBoldText(true);// 加粗
		upLoad.setLayoutParams(new LinearLayout.LayoutParams(width,
				LinearLayout.LayoutParams.FILL_PARENT, 0));
		upLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		// 打印
		Button printImage = new Button(this);
		printImage = new Button(this);
		printImage.setBackgroundResource(R.drawable.btn_shap);
		printImage.setText("打印");
		printImage.setWidth(1);
		printImage.setId(4);
		printImage.setGravity(Gravity.CENTER);
		printImage.setTextColor(android.graphics.Color.WHITE);
		printImage.setVisibility(View.VISIBLE);
		printImage.getPaint().setFakeBoldText(true);// 加粗
		printImage.setLayoutParams(new LinearLayout.LayoutParams(width,
				LinearLayout.LayoutParams.FILL_PARENT, 0));

		printImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Uri uri = Uri.fromFile(new File("/mnt/sdcard/test/"+imageFileName));
				intent.setDataAndType(uri, "image/*");
				startActivity(intent);
			}
		});
		// 间隔图片
		ImageView splite = new ImageView(this);
		splite.setScaleType(ScaleType.FIT_XY);
		splite.setLayoutParams(new LinearLayout.LayoutParams(2,
				LinearLayout.LayoutParams.FILL_PARENT));
		splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);

		ImageView splite1 = new ImageView(this);
		splite1.setScaleType(ScaleType.FIT_XY);
		splite1.setLayoutParams(new LinearLayout.LayoutParams(2,
				LinearLayout.LayoutParams.FILL_PARENT));
		splite1.setBackgroundResource(R.drawable.bg_bottombutton_splite);

		bottomLayout.addView(upLoad);
		bottomLayout.addView(splite);
		bottomLayout.addView(printImage);
		bottomLayout.setGravity(Gravity.CENTER);

	}

}
