package com.jy.environment.activity;

import com.jy.environment.R;
import com.jy.environment.util.MyLog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DiscoverExposureListActivity extends Activity implements
		OnClickListener {

	private ImageView exposure_return_iv;
	private ImageView exposure_daqi_img;
	private ImageView exposure_shuiti_img;
	private ImageView exposure_turang_img;
	private ImageView exposure_gongye_img;
	private ImageView exposure_chengshi_img;
	private ImageView exposure_nongye_img;
	private ImageView exposure_haiyang_img;
	private ImageView exposure_huaxue_img;
	private ImageView exposure_shengwu_img;
	private ImageView exposure_zaoyin_img;
	private ImageView exposure_guti_img;
	private ImageView exposure_nengyuan_img;
	private boolean daqi_onclicked = true;
	private boolean shuiti_onclicked = true;
	private boolean turang_onclicked = true;
	private boolean gongye_onclicked = true;
	private boolean chengshi_onclicked = true;
	private boolean nongye_onclicked = true;
	private boolean haiyang_onclicked = true;
	private boolean huaxue_onclicked = true;
	private boolean shengwu_onclicked = true;
	private boolean zaoyin_onclicked = true;
	private boolean guti_onclicked = true;
	private boolean nengyuan_onclicked = true;
	private TextView exposure_next;
	private String pollutionType = "";
	static Activity dActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exposure_list);
		dActivity = this;
		initView();
		initListener();
	}

	private void initView() {
		exposure_return_iv = (ImageView) findViewById(R.id.exposure_return_iv);
		exposure_daqi_img = (ImageView) findViewById(R.id.exposure_daqi_img);
		exposure_shuiti_img = (ImageView) findViewById(R.id.exposure_shuiti_img);
		exposure_turang_img = (ImageView) findViewById(R.id.exposure_turang_img);
		exposure_gongye_img = (ImageView) findViewById(R.id.exposure_gongye_img);
		exposure_chengshi_img = (ImageView) findViewById(R.id.exposure_chengshi_img);
		exposure_nongye_img = (ImageView) findViewById(R.id.exposure_nongye_img);
		exposure_haiyang_img = (ImageView) findViewById(R.id.exposure_haiyang_img);
		exposure_huaxue_img = (ImageView) findViewById(R.id.exposure_huaxue_img);
		exposure_shengwu_img = (ImageView) findViewById(R.id.exposure_shengwu_img);
		exposure_zaoyin_img = (ImageView) findViewById(R.id.exposure_zaoyin_img);
		exposure_guti_img = (ImageView) findViewById(R.id.exposure_guti_img);
		exposure_nengyuan_img = (ImageView) findViewById(R.id.exposure_nengyuan_img);
		exposure_next = (TextView) findViewById(R.id.exposure_next);
	}

	private void initListener() {
		exposure_return_iv.setOnClickListener(this);
		exposure_daqi_img.setOnClickListener(this);
		exposure_shuiti_img.setOnClickListener(this);
		exposure_turang_img.setOnClickListener(this);
		exposure_gongye_img.setOnClickListener(this);
		exposure_chengshi_img.setOnClickListener(this);
		exposure_nongye_img.setOnClickListener(this);
		exposure_haiyang_img.setOnClickListener(this);
		exposure_huaxue_img.setOnClickListener(this);
		exposure_shengwu_img.setOnClickListener(this);
		exposure_zaoyin_img.setOnClickListener(this);
		exposure_guti_img.setOnClickListener(this);
		exposure_nengyuan_img.setOnClickListener(this);
		exposure_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exposure_return_iv:
			finish();
			break;
		case R.id.exposure_daqi_img:
			if (daqi_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_daqi_img.setBackgroundDrawable(drawable);
				daqi_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_daqi_img.setBackgroundDrawable(drawable);
				daqi_onclicked = true;
			}
			break;
		case R.id.exposure_shuiti_img:
			if (shuiti_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_shuiti_img.setBackgroundDrawable(drawable);
				shuiti_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_shuiti_img.setBackgroundDrawable(drawable);
				shuiti_onclicked = true;
			}
			break;
		case R.id.exposure_turang_img:
			if (turang_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_turang_img.setBackgroundDrawable(drawable);
				turang_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_turang_img.setBackgroundDrawable(drawable);
				turang_onclicked = true;
			}
			break;
		case R.id.exposure_gongye_img:
			if (gongye_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_gongye_img.setBackgroundDrawable(drawable);
				gongye_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_gongye_img.setBackgroundDrawable(drawable);
				gongye_onclicked = true;
			}
			break;
		case R.id.exposure_chengshi_img:
			if (chengshi_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_chengshi_img.setBackgroundDrawable(drawable);
				chengshi_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_chengshi_img.setBackgroundDrawable(drawable);
				chengshi_onclicked = true;
			}
			break;
		case R.id.exposure_nongye_img:
			if (nongye_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_nongye_img.setBackgroundDrawable(drawable);
				nongye_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_nongye_img.setBackgroundDrawable(drawable);
				nongye_onclicked = true;
			}
			break;
		case R.id.exposure_haiyang_img:
			if (haiyang_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_haiyang_img.setBackgroundDrawable(drawable);
				haiyang_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_haiyang_img.setBackgroundDrawable(drawable);
				haiyang_onclicked = true;
			}
			break;
		case R.id.exposure_huaxue_img:
			if (huaxue_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_huaxue_img.setBackgroundDrawable(drawable);
				huaxue_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_huaxue_img.setBackgroundDrawable(drawable);
				huaxue_onclicked = true;
			}
			break;
		case R.id.exposure_shengwu_img:
			if (shengwu_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_shengwu_img.setBackgroundDrawable(drawable);
				shengwu_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_shengwu_img.setBackgroundDrawable(drawable);
				shengwu_onclicked = true;
			}
			break;
		case R.id.exposure_zaoyin_img:
			if (zaoyin_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_zaoyin_img.setBackgroundDrawable(drawable);
				zaoyin_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_zaoyin_img.setBackgroundDrawable(drawable);
				zaoyin_onclicked = true;
			}
			break;
		case R.id.exposure_guti_img:
			if (guti_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_guti_img.setBackgroundDrawable(drawable);
				guti_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_guti_img.setBackgroundDrawable(drawable);
				guti_onclicked = true;
			}
			break;
		case R.id.exposure_nengyuan_img:
			if (nengyuan_onclicked) {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background_selected);
				exposure_nengyuan_img.setBackgroundDrawable(drawable);
				nengyuan_onclicked = false;
			} else {
				Resources resources = getResources();
				Drawable drawable = resources
						.getDrawable(R.drawable.exposure_background);
				exposure_nengyuan_img.setBackgroundDrawable(drawable);
				nengyuan_onclicked = true;
			}
			break;
		case R.id.exposure_next:
			MyLog.i(">>>>>>turan" + turang_onclicked);
			if (!daqi_onclicked) {
				pollutionType = ",气";
			}
			if (!shuiti_onclicked) {
				pollutionType = pollutionType + ",水";
			}
			if (!turang_onclicked) {
				pollutionType = pollutionType + ",土";
			}
			if (!gongye_onclicked) {
				pollutionType = pollutionType + ",工";
			}
			if (!chengshi_onclicked) {
				pollutionType = pollutionType + ",城";
			}
			if (!nongye_onclicked) {
				pollutionType = pollutionType + ",农";
			}
			if (!haiyang_onclicked) {
				pollutionType = pollutionType + ",海";
			}
			if (!huaxue_onclicked) {
				pollutionType = pollutionType + ",化";
			}
			if (!shengwu_onclicked) {
				pollutionType = pollutionType + ",生";
			}
			if (!zaoyin_onclicked) {
				pollutionType = pollutionType + ",噪";
			}
			if (!guti_onclicked) {
				pollutionType = pollutionType + ",固";
			}
			if (!nengyuan_onclicked) {
				pollutionType = pollutionType + ",能";
			}
			MyLog.i("===============dsfsaf" + pollutionType);
			Intent intent = new Intent(DiscoverExposureListActivity.this,
					DiscoverExposureActivity.class);
			intent.putExtra("pollutionType", pollutionType);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
