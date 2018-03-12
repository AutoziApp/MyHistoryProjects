package com.jy.environment.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBInfo;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.model.GuanZhu;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 发现公众服务关注
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverPubServiceAddAttentionActivity extends ActivityBase
		implements OnClickListener {
	private ImageView particular2_icon;
	private TextView particular2_tv2, particular2_sum, particular2_tv1;
	private Button particular2_guanzhu;
	private Intent intent;
	// 传过来的值，关注页面
	private String name, fuction, public_photo;
	// 用户userID,公众号id
	private String userID, publicID;
	/** 用来操作sharePreferences标识 */
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private ImageLoader loader;
	private ImageView particular1_iv;
	DisplayImageOptions defaultOptions;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_pubservice_attention_cancel_activity);
		intent = getIntent();
		particular1_iv = (ImageView) findViewById(R.id.particular1_iv);
		particular1_iv.setOnClickListener(this);
		publicID = intent.getStringExtra("publicID");
		name = intent.getStringExtra("name");
		fuction = intent.getStringExtra("fuction");
		public_photo = intent.getStringExtra("public_photo");
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		userID = WeiBaoApplication.getUserId();
		init();
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).build();
		loader = ImageLoader.getInstance();
		particular2_sum.setText(name);
		particular2_tv2.setText(fuction);
		loader.displayImage(public_photo, particular2_icon, defaultOptions,
				animateFirstListener);

	}

	private void init() {
		// TODO Auto-generated method stub
		particular2_icon = (ImageView) findViewById(R.id.particular2_icon);
		particular2_tv2 = (TextView) findViewById(R.id.particular2_tv2);
		particular2_sum = (TextView) findViewById(R.id.particular2_sum);
		particular2_tv1 = (TextView) findViewById(R.id.particular2_tv1);
		particular2_guanzhu = (Button) findViewById(R.id.particular2_guanzhu);
		particular2_guanzhu.setOnClickListener(this);
		particular2_tv1.setOnClickListener(this);
	}

	private class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.particular2_guanzhu:
			GuanZhu();
			break;
		case R.id.particular2_tv1:
			MobclickAgent.onEvent(DiscoverPubServiceAddAttentionActivity.this, "FXServerHistory");
			history();
			break;
		case R.id.particular1_iv:
			finish();
			break;
		default:
			break;
		}
	}

	private void history() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(DiscoverPubServiceAddAttentionActivity.this,
				DiscoverPubServiceHistoryNewsActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("publicID", publicID);
		intent.putExtra("biaozhi", "biaozhi");
		intent.putExtra("fuction", fuction);

		intent.putExtra("public_photo", public_photo);
		startActivity(intent);
	}

	private void GuanZhu() {
		// TODO Auto-generated method stub
		final String guanzhuPath = UrlComponent.getGuanZhuPath_Get(userID,
				publicID);
		if (userID.equals("")) {
			ToastUtil.showShort(DiscoverPubServiceAddAttentionActivity.this,
					"请先登录");
			return;
		}
		new AsyncTask<String, Void, GuanZhu>() {

			@Override
			protected GuanZhu doInBackground(String... params) {
				// TODO Auto-generated method stub
				GuanZhu g1 = new GuanZhu();

				String json = ApiClient.connServerForResult(params[0]);
				DBManager manager = DBManager
						.getInstances(DiscoverPubServiceAddAttentionActivity.this);
				ContentValues values = new ContentValues();
				String xiaoxi_id = userID + "*" + publicID;
				values.put("xiaoxi_id", xiaoxi_id);
				// 第一次关注数据库表中为0
				values.put("isfirst", "0");
				values.put("isread", "0");
				values.put("userID", userID);
				values.put("account_id_num", publicID);
				values.put("message", "测试");
				try {
					JSONObject j1 = new JSONObject(json);
					g1.setFlag(j1.getBoolean("flag"));
					g1.setMessage(j1.getString("message"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return g1;
			}

			protected void onPostExecute(GuanZhu result) {
				try {
					if (null != result && result.isFlag()) {
						DBManager manager = DBManager
								.getInstances(DiscoverPubServiceAddAttentionActivity.this);
						ContentValues values = new ContentValues();
						values.put("publicID", publicID);
						values.put("fuction", fuction);
						values.put("public_photo", public_photo);
						values.put("name", name);
						values.put("userID", userID);
						manager.insertSQLite(
								DiscoverPubServiceAddAttentionActivity.this,
								DBInfo.TABLE_USERPUBLIC, null, values);
						Intent intent = new Intent(
								DiscoverPubServiceAddAttentionActivity.this,
								DiscoverPubServiceAttentionCancelActivity.class);
						intent.putExtra("publicID", publicID);
						intent.putExtra("name", name);
						intent.putExtra("fuction", fuction);
						intent.putExtra("public_photo", public_photo);
						intent.putExtra("flag_bz", "ParticularCancelActivity");
						startActivity(intent);
						finish();
					} else if (null != result && !result.isFlag()) {
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				
			};
		}.execute(guanzhuPath);
	}

}
