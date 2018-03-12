package com.jy.environment.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBInfo;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.model.GuanZhu;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.SwitchButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 公众服务取消关注
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverPubServiceAttentionCancelActivity extends ActivityBase
		implements OnClickListener, OnCheckedChangeListener {
	private ImageView particular_iv, particular1_icon;
	private TextView particular1_tv1, particular1_tv3, particular1_sum,
			particular1_tv2;
	private Intent intent;
	private Button particular1_guanzhu;
	// 传过来的值
	private String name, fuction, public_photo;
	SharedPreferencesUtil mSpUtil;
	private ImageLoader loader;
	DisplayImageOptions defaultOptions;
	private SwitchButton particular1_mSlideButton;
	/** 用来操作sharePreferences标识 */
	// 用户userID,公众号id
	private String userID, publicID, userpublicID;
	private String id;
	String flag_bz;
	boolean flag = false, jieshouflag;
	// private
	private int[] guanzhu = new int[] { R.drawable.guanzhu,
			R.drawable.guanzhucancel };
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_pubservice_addattention_activity);
		mSpUtil = SharedPreferencesUtil
				.getInstance(DiscoverPubServiceAttentionCancelActivity.this);
		intent = getIntent();
		userID = WeiBaoApplication.getUserId();
		name = intent.getStringExtra("name");
		publicID = intent.getStringExtra("publicID");
		fuction = intent.getStringExtra("fuction");
		flag_bz = intent.getStringExtra("flag_bz");
		public_photo = intent.getStringExtra("public_photo");
		init();
		userpublicID = userID + "*" + publicID;
		jieshouflag = mSpUtil.getSwitchButton(userpublicID);
		particular1_mSlideButton.setChecked(jieshouflag);
		defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).build();
		loader = ImageLoader.getInstance();
		loader.displayImage(public_photo, particular1_icon, defaultOptions,
				animateFirstListener);
		particular1_sum.setText(fuction);
		particular1_tv2.setText(name);
		particular_iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		particular1_mSlideButton = (SwitchButton) findViewById(R.id.particular1_mSlideButton);
		particular1_mSlideButton.setOnCheckedChangeListener(this);
		particular_iv = (ImageView) findViewById(R.id.particular1_iv);
		particular1_icon = (ImageView) findViewById(R.id.particular1_icon);
		particular1_tv1 = (TextView) findViewById(R.id.particular1_tv1);
		particular1_tv2 = (TextView) findViewById(R.id.particular1_tv2);
		particular1_tv3 = (TextView) findViewById(R.id.particular1_tv3);
		particular1_sum = (TextView) findViewById(R.id.particular1_sum);
		particular1_guanzhu = (Button) findViewById(R.id.particular1_guanzhu);
		if (publicID.equals("27") || publicID.equals("28")
				|| publicID.equals("29") || publicID.equals("30")
				|| publicID.equals("32") )
		{
			particular1_guanzhu.setVisibility(View.GONE);
		}
		else {
			particular1_guanzhu.setVisibility(View.VISIBLE);
		}
		particular1_tv1.setOnClickListener(this);
		particular1_tv3.setOnClickListener(this);
		particular1_guanzhu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.particular1_tv1:
			MobclickAgent.onEvent(
					DiscoverPubServiceAttentionCancelActivity.this,
					"FXServerHistory");
			news();
			break;
		case R.id.particular1_tv3:

			pastNews();
			break;
		case R.id.particular1_guanzhu:
			// 关注按钮事件
			follow();
			break;
		default:
			break;
		}
	}

	// 取消关注按钮事件
	private void follow() {
		if (userID.equals("")) {
			userID = "0";
		}
		final String guanzhuPathCancel = UrlComponent.getGuanZhuPathCancel_Get(
				userID, publicID);
		// TODO Auto-generated method stub
		if (NetUtil
				.getNetworkState(DiscoverPubServiceAttentionCancelActivity.this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(DiscoverPubServiceAttentionCancelActivity.this,
					"网络不给力", 0).show();
			return;
		}
		// 取消关注
		new AsyncTask<String, Void, GuanZhu>() {

			@Override
			protected GuanZhu doInBackground(String... params) {
				// TODO Auto-generated method stub
				GuanZhu g1 = new GuanZhu();
				if (NetUtil
						.getNetworkState(DiscoverPubServiceAttentionCancelActivity.this) == NetUtil.NETWORN_NONE) {
					Toast.makeText(
							DiscoverPubServiceAttentionCancelActivity.this,
							"你的网络出错", 0).show();
					return null;
				} else {
					String json = ApiClient.connServerForResult(params[0]);
					MyLog.i(">>>>>>cancel" + json);
					try {
						JSONObject j1 = new JSONObject(json);
						g1.setStatus(j1.getBoolean("status"));
						g1.setMsg(j1.getString("msg"));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					return g1;
				}
			}

			protected void onPostExecute(GuanZhu result) {
				if (null == result) {
					return;
				}
				if (result.isStatus()) {
					mSpUtil.Setbutton(userpublicID, true);
					DBManager manager = DBManager
							.getInstances(DiscoverPubServiceAttentionCancelActivity.this);
					manager.deleteSQLiteQuanZhu(
							DiscoverPubServiceAttentionCancelActivity.this,
							"uic", userID, publicID);
					manager.deleteSQLiteQuanZhu(
							DiscoverPubServiceAttentionCancelActivity.this,
							DBInfo.TABLE_USERPUBLIC, userID, publicID);
					if (flag_bz.equals("PublicServiceActivity")) {
						finish();
					} else if (flag_bz.equals("ParticularCancelActivity")) {
						Intent intent = new Intent(
								DiscoverPubServiceAttentionCancelActivity.this,
								DiscoverPubServiceSearchActivity.class);
						startActivity(intent);
						finish();
					} else {
						DiscoverPubServiceNewsActivity.sm.finish();
						finish();
					}
				} else {
					MyLog.i("");
					flag = true;
					ToastUtil.showLong(
							DiscoverPubServiceAttentionCancelActivity.this,
							result.getMsg());
				}
			};

		}.execute(guanzhuPathCancel);

	}

	private void pastNews() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(
				DiscoverPubServiceAttentionCancelActivity.this,
				DiscoverPubServiceNewsActivity.class);
		startActivity(intent);
	}

	private void news() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(
				DiscoverPubServiceAttentionCancelActivity.this,
				DiscoverPubServiceHistoryNewsActivity.class);
		intent.putExtra("name", name);
		intent.putExtra("publicID", publicID);
		intent.putExtra("public_photo", public_photo);
		intent.putExtra("fuction", fuction);
		intent.putExtra("biaozhi", "biaozhi");
		intent.putExtra("guanzhu", true);
		startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.particular1_mSlideButton:
			userpublicID = userID + "*" + publicID;
			MobclickAgent.onEvent(
					DiscoverPubServiceAttentionCancelActivity.this,
					"FXServerReceive");
			mSpUtil.Setbutton(userpublicID, isChecked);
			break;

		default:
			break;
		}
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

}
