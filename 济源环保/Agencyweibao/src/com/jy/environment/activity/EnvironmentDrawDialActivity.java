package com.jy.environment.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import root.gast.audio.record.AudioClipListener;
import root.gast.audio.record.OneDetectorManyObservers;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIMediaObject;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQFlagType;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQReqestType;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.jy.environment.R;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.SQLiteDALModelNoiseHistory;
import com.jy.environment.database.model.ModelNoiseHistory;
import com.jy.environment.model.RecordData;
import com.jy.environment.model.Sweather;
import com.jy.environment.model.uploadRecordresult;
import com.jy.environment.util.AudioClipLogWrapper;
import com.jy.environment.util.ImageUtils;
import com.jy.environment.util.KjhttpUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.RecordAudioTask;
import com.jy.environment.util.RecordThread;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.util.StringUtil;
import com.jy.environment.util.KjhttpUtils.DownGet;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.DialView;
import com.umeng.analytics.MobclickAgent;

public class EnvironmentDrawDialActivity extends Activity implements
		OnClickListener, SynthesizerListener {
	/** Called when the activity is first created. */
	DialView dialView;
	Random random = new Random();
	private UploadTask uploadRecord;
	private UploadDBTask uploadDB;
	private Handler handler;
	private String vv = "0";
	private RecordThread rt;
	private int intvv = 0;
	private ImageView back;
	private TextView sharebtn;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private Uri uri;
	private Sweather sweather;
	private String content;
	private LinearLayout layout_bac;
	private String backgroundType;
	private TextView zaosheng_text, zaosheng_text1, zaosheng_text2,
			zaosheng_text3, zaosheng_text4, zaosheng_text5;
	private TextView zaosheng_btn;
	private Handler mHandler;
	private int text_val1, text_val2, text_val3, text_val4, text_val5,
			text_avr;
	private int j = 0;
	private TextView zaosheng_suggestion;
	private WeiBaoApplication mApplication;
	private String xiangxiString;
	private String lat;
	private String lng;
	private static final String FONT_DIGITAL_7 = "fonts" + File.separator
			+ "digital-7.ttf";
	private ImageView zaosheng_qq_pic;
	private Animation translateAnimation1, translateAnimation2,
			translateAnimation3, translateAnimation4, translateAnimation5;
	// private ChartView chartView;
	private RecordData recorddata;
	private String userid;
	private LocationClient mLocationClient;/* 定位 */
	private SQLiteDALModelNoiseHistory mLiteDALModelNoiseHistory;
	private RelativeLayout noise_history_button;
	private SharedPreferencesUtil mSpUtil;
	private KjhttpUtils http;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.environment_record1);
		mApplication = WeiBaoApplication.getInstance();
		mSpUtil = SharedPreferencesUtil
				.getInstance(EnvironmentDrawDialActivity.this);
		mLocationClient = mApplication.getLocationClient();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		http = new KjhttpUtils(this, null);
		mLocationClient.setLocOption(option);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
			mLocationClient.requestLocation();
		} else {
			mLocationClient.requestLocation();
		}
		lat = mApplication.getCurrentCityLatitude();
		lng = mApplication.getCurrentCityLongitude();
		userid = mApplication.getUserId();
		mLiteDALModelNoiseHistory = new SQLiteDALModelNoiseHistory(
				ModelNoiseHistory.class);
		if (userid == "") {
			userid = "0";
		}
		initView();
		initListener();
		Intent intent = getIntent();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				try {
					int i = msg.what;
					switch (i) {
					case 1:
						try {
							if (null != vv) {
								vv = msg.obj.toString();
								intvv = (int) Math
										.round(Integer.parseInt(vv) * 1.8);
							} else {
								vv = "0";
								intvv = 0;
							}
							zaosheng_text.setText(vv);
						} catch (Exception e) {
							// TODO: handle exception
							vv = "0";
							intvv = 0;
						}
						break;

					default:
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		};
		// rt = new RecordThread(handler);
		// rt.start();

		// handler name = new handler();
		// name.refresh(300);
	}

	protected void onDestroy() {
		super.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
	};

	BDLocationListener mLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLog.i(">>>>>>>>>>bdlocation" + location.getCity());
			if (location == null || TextUtils.isEmpty(location.getCity())) {
				return;
			}
			// 获取当前详细地址
			xiangxiString = "" + location.getAddrStr();
			MyLog.i(">>>>>>>>>>bdlocation" + xiangxiString);
			lat = "" + location.getLatitude();
			lng = "" + location.getLongitude();
			mLocationClient.stop();

		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startTask(createAudioLogger(), "Audio Logger");
		MobclickAgent.onResume(this);
	}

	class handler extends Handler {
		public void handleMessage(android.os.Message msg) {
			// chartView.refresh(77);
			refresh(300);
		}

		public void refresh(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	}

	private void initView() {
		dialView = (DialView) findViewById(R.id.dialView);
		// chartView = (ChartView) findViewById(R.id.zaosheng_imageView5);
		back = (ImageView) findViewById(R.id.record_return_iv);
		sharebtn = (TextView) findViewById(R.id.record_title_share);
		layout_bac = (LinearLayout) findViewById(R.id.zaosheng_bac);
		zaosheng_text = (TextView) findViewById(R.id.zaosheng_textView3);
		zaosheng_btn = (TextView) findViewById(R.id.zaosheng_btn);
		zaosheng_text1 = (TextView) findViewById(R.id.zaosheng_text1);
		zaosheng_text2 = (TextView) findViewById(R.id.zaosheng_text2);
		zaosheng_text3 = (TextView) findViewById(R.id.zaosheng_text3);
		zaosheng_text4 = (TextView) findViewById(R.id.zaosheng_text4);
		zaosheng_text5 = (TextView) findViewById(R.id.zaosheng_text5);
		noise_history_button = (RelativeLayout) findViewById(R.id.noise_history_button);
		// 移动动画效果
		translateAnimation1 = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		translateAnimation2 = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		translateAnimation3 = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		translateAnimation4 = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		translateAnimation5 = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		zaosheng_suggestion = (TextView) findViewById(R.id.zaosheng_suggestion);
		zaosheng_qq_pic = (ImageView) findViewById(R.id.zaosheng_qq_pic);
		AssetManager assets = getAssets();
		final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
		zaosheng_text.setTypeface(font);// 设置字体
	}

	private void initListener() {
		sharebtn.setOnClickListener(this);
		back.setOnClickListener(this);
		zaosheng_btn.setOnClickListener(this);
		noise_history_button.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RecordThread.isRun = false;
			onDestroy();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.record_return_iv:
			RecordThread.isRun = false;
			onDestroy();
			finish();
			break;
		case R.id.record_title_share:
			if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
				Toast.makeText(EnvironmentDrawDialActivity.this, "请检查您的网络", 0)
						.show();
				return;
			}
			Bitmap bitmap = GetCurrentScreen(EnvironmentDrawDialActivity.this);
			try {
				uri = Uri.parse(MediaStore.Images.Media.insertImage(
						getContentResolver(), bitmap, null, null));
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				Toast.makeText(EnvironmentDrawDialActivity.this, "截图失败", 0)
						.show();
				return;
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(EnvironmentDrawDialActivity.this, "截图失败", 0)
						.show();
				return;
			}
			content = "赶紧使用微保测一下，你附近的噪声有多大！";

			mSocialShare = Frontia.getSocialShare();
			mSocialShare.setContext(this);
			mSocialShare = Frontia.getSocialShare();
			mSocialShare.setContext(this);
			mSocialShare.setClientId(MediaType.WEIXIN.toString(),
					"wx541df6ed6e9babc0");
			mSocialShare.setClientId(MediaType.SINAWEIBO.toString(),
					"991071488");
			mSocialShare
					.setClientId(MediaType.QQFRIEND.toString(), "100358052");
			mSocialShare.setParentView(getWindow().getDecorView());
			mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "微保");
			mImageContent.setTitle("微保");
			mImageContent.setContent(content);
			mImageContent.setLinkUrl("http://www.wbapp.com.cn");
			// mImageContent.setImageUri(uri);
			// mImageContent.setImageUri(Uri.parse("http://apps.bdimg.com/developer/static/04171450/developer/images/icon/terminal_adapter.png"));
			mImageContent.setImageUri(uri);
			mImageContent.setLinkUrl("http://www.wbapp.com.cn");
			mImageContent.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
			mImageContent.setQQRequestType(FrontiaIQQReqestType.TYPE_IMAGE);
			mImageContent.setQQFlagType(FrontiaIQQFlagType.TYPE_DEFAULT);
			mImageContent.setImageData(bitmap);
			mSocialShare.show(this.getWindow().getDecorView(), mImageContent,
					FrontiaTheme.LIGHT, new ShareListener());

			break;
		case R.id.noise_history_button:
			MobclickAgent.onEvent(EnvironmentDrawDialActivity.this,
					"HJOpenNoiseHisPanel");
			Intent intent = new Intent(EnvironmentDrawDialActivity.this,
					EnvironmentNoiseHistoryActivity.class);
			startActivity(intent);
			break;
		case R.id.zaosheng_btn:
			try {
				if (zaosheng_btn.getText().equals("测一测平均值")) {
					MobclickAgent.onEvent(EnvironmentDrawDialActivity.this,
							"HJGetMeanNoise");
				} else {
					MobclickAgent.onEvent(EnvironmentDrawDialActivity.this,
							"HJGetMeanNoiseAgain");
				}
				zaosheng_text1.setVisibility(View.INVISIBLE);
				zaosheng_text2.setVisibility(View.INVISIBLE);
				zaosheng_text3.setVisibility(View.INVISIBLE);
				zaosheng_text4.setVisibility(View.INVISIBLE);
				zaosheng_text5.setVisibility(View.INVISIBLE);
				try {
					zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(ImageUtils
							.readBitmap(EnvironmentDrawDialActivity.this,
									R.drawable.zaosheng_qq_congjing)));

				} catch (OutOfMemoryError e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				zaosheng_suggestion.setVisibility(View.GONE);
				zaosheng_btn.setVisibility(View.GONE);
				sharebtn.setVisibility(View.GONE);
				noise_history_button.setVisibility(View.GONE);
				mHandler = new Handler();
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						int i = 1;
						i = j + 1;
						if (i == 1) {
							text_val1 = Integer.parseInt(vv);
							zaosheng_text1.setText(vv);
							translateAnimation1.setDuration(1000);
							zaosheng_text1.setAnimation(translateAnimation1);
							translateAnimation1.startNow();
							zaosheng_text1.setVisibility(View.VISIBLE);
							j = i;
						} else if (i == 2) {
							text_val2 = Integer.parseInt(vv);
							zaosheng_text2.setText(vv);
							translateAnimation2.setDuration(1000);
							zaosheng_text2.setAnimation(translateAnimation2);
							translateAnimation2.startNow();
							zaosheng_text2.setVisibility(View.VISIBLE);
							j = i;
						} else if (i == 3) {
							text_val3 = Integer.parseInt(vv);
							zaosheng_text3.setText(vv);
							translateAnimation3.setDuration(1000);
							zaosheng_text3.setAnimation(translateAnimation3);
							translateAnimation3.startNow();
							zaosheng_text3.setVisibility(View.VISIBLE);
							j = i;
						} else if (i == 4) {
							text_val4 = Integer.parseInt(vv);
							zaosheng_text4.setText(vv);
							translateAnimation4.setDuration(1000);
							zaosheng_text4.setAnimation(translateAnimation4);
							translateAnimation4.startNow();
							zaosheng_text4.setVisibility(View.VISIBLE);
							j = i;
						} else if (i == 5) {
							text_val5 = Integer.parseInt(vv);
							zaosheng_text5.setText(vv);
							translateAnimation5.setDuration(1000);
							zaosheng_text5.setAnimation(translateAnimation5);
							translateAnimation5.startNow();
							zaosheng_text5.setVisibility(View.VISIBLE);
							j = i;
						} else if (i == 6) {
							j = 0;
						}
						if (i != 6) {
							mHandler.postDelayed(this, 1000);
						} else {
							int sum = text_val1 + text_val2 + text_val3 + text_val4
									+ text_val5;
							text_avr = sum / 5;
							// 获取当前时间
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							String currentdate = sdf.format(new Date());
							String date_current = getDate();
							if (null == xiangxiString) {
								xiangxiString = "";
							}
							if (text_avr < 30) {
								zaosheng_suggestion.setText(xiangxiString
										+ date_current + "的噪声为" + text_avr + "分贝，"
										+ "好安静啊，是地球吗？");
								try {
									zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils.readBitmap(
													EnvironmentDrawDialActivity.this,
													R.drawable.zaosheng_start)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							} else if (text_avr >= 30 && text_avr < 50) {
								zaosheng_suggestion.setText(xiangxiString
										+ date_current + "的噪声为" + text_avr + "分贝，"
										+ "趁现在安静，安心学习和工作吧。");
								try {
									zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils.readBitmap(
													EnvironmentDrawDialActivity.this,
													R.drawable.zaosheng_qq_congjing)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							} else if (text_avr >= 50 && text_avr <= 60) {
								zaosheng_suggestion.setText(xiangxiString
										+ date_current + "的噪声为" + text_avr + "分贝，"
										+ "不要因为小小的嘈杂影响愉悦的心情。");
								try {
									zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils.readBitmap(
													EnvironmentDrawDialActivity.this,
													R.drawable.zaosheng_qq_wunai)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							} else if (text_avr > 60 && text_avr <= 70) {
								zaosheng_suggestion.setText(xiangxiString
										+ date_current + "的噪声为" + text_avr + "分贝，"
										+ "能给我个耳机不？有点吵~");
								try {
									zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils.readBitmap(
													EnvironmentDrawDialActivity.this,
													R.drawable.zaosheng_qq_nuhuo)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							} else if (text_avr > 70 && text_avr <= 80) {
								zaosheng_suggestion.setText(xiangxiString
										+ date_current + "的噪声为" + text_avr + "分贝，"
										+ "快带我走吧，不想在这待了!");
								try {
									zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils.readBitmap(
													EnvironmentDrawDialActivity.this,
													R.drawable.zaosheng_qq_daku)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							} else if (text_avr > 80) {
								zaosheng_suggestion.setText(xiangxiString
										+ date_current + "的噪声为" + text_avr + "分贝，"
										+ "你这是在嗨吗？。。。。。。");
								try {
									zaosheng_qq_pic.setBackgroundDrawable(new BitmapDrawable(
											ImageUtils.readBitmap(
													EnvironmentDrawDialActivity.this,
													R.drawable.zaosheng_qq_qifan)));

								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									e.printStackTrace();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								
							}
							zaosheng_btn.setText("再测一把？");
							zaosheng_suggestion.setVisibility(View.VISIBLE);
							sharebtn.setVisibility(View.VISIBLE);
							zaosheng_btn.setVisibility(View.VISIBLE);
							noise_history_button.setVisibility(View.VISIBLE);
							ModelNoiseHistory modelNoiseHistory = new ModelNoiseHistory();
							userid = mApplication.getUserId();
							MyLog.i(">>>>>lat" + lat + ">>>>>>lng" + lng
									+ ">>>>>xiangxi" + xiangxiString
									+ ">>>>>userId" + userid);
							if (!userid.equals("") && mSpUtil.getIsAutoUpload()) {
								if (!StringUtil.isEmpty(lat)
										&& !StringUtil.isEmpty(lng)
										&& !StringUtil.isEmpty(xiangxiString)) {
									recorddata = new RecordData(userid, text_avr
											+ "", xiangxiString, lat, lng,
											currentdate);
									modelNoiseHistory.setUserId(userid);
									modelNoiseHistory.setmResult(text_avr + "");
									modelNoiseHistory.setLocation(xiangxiString);
									modelNoiseHistory.setLatitude(lat);
									modelNoiseHistory.setLongitude(lng);
									modelNoiseHistory.setTime(currentdate);
									modelNoiseHistory.setIsupload("1");
									modelNoiseHistory.setUserId(userid);
									uploadDB = new UploadDBTask();
									uploadRecord = new UploadTask();
									uploadDB.execute(modelNoiseHistory);
									MobclickAgent.onEvent(
											EnvironmentDrawDialActivity.this,
											"HJSynOneNoise");
									uploadRecord.execute(recorddata);
									String url = UrlComponent.getShare(userid,
											"noise");
									http.getString(url, 0, new DownGet() {

										@Override
										public void downget(String arg0) {
											// TODO Auto-generated method stub

										}
									});
								}
							} else {
								if (!StringUtil.isEmpty(lat)
										&& !StringUtil.isEmpty(lng)
										&& !StringUtil.isEmpty(xiangxiString)) {
									modelNoiseHistory.setUserId(userid);
									modelNoiseHistory.setmResult(text_avr + "");
									modelNoiseHistory.setLocation(xiangxiString);
									modelNoiseHistory.setLatitude(lat);
									modelNoiseHistory.setLongitude(lng);
									modelNoiseHistory.setTime(currentdate);
									modelNoiseHistory.setIsupload("0");
									uploadDB = new UploadDBTask();
									uploadDB.execute(modelNoiseHistory);
								}
							}
						}
					}
				});
				break;
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		}
	}

	// 分享成功还是失败
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			Log.d("Test", "share success");
			MobclickAgent.onEvent(EnvironmentDrawDialActivity.this,
					"HJShareNoise");
			Toast.makeText(EnvironmentDrawDialActivity.this, "分享成功", 2000)
					.show();
			String userId = WeiBaoApplication.getUserId();
			if (!userId.equals("")) {
				http.getString(UrlComponent.getShare(userId, "noise"), 0,
						new DownGet() {

							@Override
							public void downget(String arg0) {
								// TODO Auto-generated method stub

							}
						});
			}
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test", "share errCode " + errCode);
			Toast.makeText(EnvironmentDrawDialActivity.this, "分享失败", 2000)
					.show();
		}

		@Override
		public void onCancel() {
			Log.d("Test", "cancel ");
			// Toast.makeText(CurrentTq.this, "分享取消", 2000).show();
		}
	}

	private String getDate() {
		Time t = new Time();
		t.setToNow();
		int hour = t.hour;
		int minuter = t.minute;
		String dateString = hour + "点" + minuter + "分";
		return dateString;

	}

	private class UploadDBTask extends AsyncTask<ModelNoiseHistory, Void, Void> {
		ModelNoiseHistory modelNoiseHistory;

		@Override
		protected Void doInBackground(ModelNoiseHistory... params) {
			try {
				modelNoiseHistory = params[0];
				mLiteDALModelNoiseHistory
						.insertOrUpdateModel(modelNoiseHistory);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	private class UploadTask extends AsyncTask<RecordData, Void, Void> {

		RecordData recordData;

		@Override
		protected Void doInBackground(RecordData... params) {
			recordData = params[0];
			String url = UrlComponent.uploadRecordData();
			BusinessSearch search = new BusinessSearch();
			uploadRecordresult _Result = null;
			try {
				MyLog.i(">>>>>ceyixia" + "1");
				_Result = search.uploadrecordpost(url, recordData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	private RecordAudioTask recordAudioTask;

	private void stopAll() {
		shutDownTaskIfNecessary(recordAudioTask);
	}

	private void shutDownTaskIfNecessary(final RecordAudioTask task) {
		if ((task != null) && (!task.isCancelled())) {
			// if ((task.getStatus().equals(AsyncTask.Status.RUNNING))
			// || (task.getStatus()
			// .equals(AsyncTask.Status.PENDING)))
			// {
			// MyLog.i( "CANCEL " + task.getClass().getSimpleName());
			// task.cancel(true);
			// }
			try {
				if (null != task) {
					MyLog.i("CANCEL " + task.getClass().getSimpleName());
					task.cancel(true);
				} else {
					MyLog.i("task not running");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void startTask(AudioClipListener detector, String name) {
		stopAll();

		recordAudioTask = new RecordAudioTask(EnvironmentDrawDialActivity.this,
				new TextView(this), new TextView(this), name);
		// wrap the detector to show some output
		List<AudioClipListener> observers = new ArrayList<AudioClipListener>();
		observers
				.add(new AudioClipLogWrapper(handler, new TextView(this), this));
		OneDetectorManyObservers wrapped = new OneDetectorManyObservers(
				detector, observers);
		recordAudioTask.execute(wrapped);
	}

	private AudioClipListener createAudioLogger() {
		AudioClipListener audioLogger = new AudioClipListener() {
			@Override
			public boolean heard(short[] audioData, int sampleRate) {
				if (audioData == null || audioData.length == 0) {
					return true;
				}

				// returning false means the recording won't be stopped
				// users have to manually stop it via the stop button
				return false;
			}
		};

		return audioLogger;
	}

	@Override
	protected void onPause() {
		stopAll();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private Bitmap GetCurrentScreen(
			EnvironmentDrawDialActivity environmentDrawDialActivity) {

		int w = layout_bac.getWidth();
		int h = layout_bac.getHeight();
		layout_bac.refreshDrawableState();
		Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		layout_bac.setDrawingCacheEnabled(true);
		bmp = layout_bac.getDrawingCache();

		return bmp;
	}

}
