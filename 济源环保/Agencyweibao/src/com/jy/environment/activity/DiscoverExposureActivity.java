package com.jy.environment.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.jy.environment.R;
import com.jy.environment.adapter.DiscoverFaceGridViewAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.City;
import com.jy.environment.model.DiscoverBlogUpLoadResult;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.model.MyPostExposure;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.BimpHelper;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.ImageUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.StringUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.MyGridView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiscoverExposureActivity extends ActivityBase implements
		OnClickListener {

	private UpLoadTask loadTask;
	private Uri cropUri;
	private File protraitFile;
	private String protraitPath;
	private Uri photoUri;
	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory()
			.toString();
	public static final String SAVE_PATH_IN_SDCARD = "/weibao/life/";
	String IMAGE_CAPTURE_NAME = getPhotoFileName();

	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 请求相机 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/weibao/Portrait/";
	private Button send;
	private ImageView cancel;
	private TextView nickname_text, location_ok_tv, star_rate,
			tongbuditu__share;

	private int numStars2 = 1;
	private RatingBar room_ratingbar2;
	private WeiBaoApplication application;
	private String locatecity;
	private ImageView img_share;
	private EditText say_sth;
	private boolean flag;
	private int isopen = 1;
	private TextView location_text;
	private TextView locate_tv, open_tv;
	private LocationClient mLocationClient;/* 定位 */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 拍照的照片存储位置 */
	public static final File PHOTO_DIR = new File("/sdcard/DCIM/Camera");
	// 照相机拍照得到的图片
	public File mCurrentPhotoFile;
	private String username = "";

	private Context mContext;
	private InputMethodManager imm;
	private ImageView mFace;
	private DiscoverFaceGridViewAdapter mGVFaceAdapter;
	private MyGridView noScrollgridview;
	private GridAdapter adapter;
	private RelativeLayout baoguangchengsi_post, kejianfanwei_post,
			tongbuditu_post;
	private double lat, lng;
	private WeiBaoApplication mApplication;
	private RadioGroup post_rg;
	private RadioButton rb1, rb2, rb3;
	private int huanjing = 1;
	private CheckBox isexposureString;
	private int isanonymous;
	private String pollutionType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		try {
			setContentView(R.layout.discover_exposure);
			mApplication = WeiBaoApplication.getInstance();
			Intent intent = getIntent();
			pollutionType = intent.getStringExtra("pollutionType");
			mLocationClient = mApplication.getLocationClient();
			mLocationClient.registerLocationListener(mLocationListener);
			loadTask = new UpLoadTask();
			mLocationClient.start();
			mLocationClient.requestLocation();
			LocationService.sendGetLocationBroadcast(this);
			baoguangchengsi_post = (RelativeLayout) findViewById(R.id.exposure_baoguangchengsi_post);
			baoguangchengsi_post.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tanchu();
				}
			});
			// 新添加的同步到地图
			tongbuditu_post = (RelativeLayout) findViewById(R.id.exposure_tongbuditu_post);
			tongbuditu_post.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tongbu();
				}
			});

			kejianfanwei_post = (RelativeLayout) findViewById(R.id.exposure_kejianfanwei_post);
			kejianfanwei_post.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tanchuopen();
				}
			});
			application = WeiBaoApplication.getInstance();
			username = application.getUsename();
			locatecity = application.getXiangxidizhi();
			mCityDB = application.getCityDB();

			qu();
			init();
			initView();
			initListener();
			mContext = this;
			imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

			mFace = (ImageView) findViewById(R.id.exposure_comment_iv);
			// mContent = (EditText)findViewById(R.id.tweet_pub_content);
			mFace.setOnClickListener(faceClickListener);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.e("weibao Exception" + e);
		}

	}

	BDLocationListener mLocationListener = new BDLocationListener() {
		double longitude;
		double latitude;

		// @Override
		// public void onReceivePoi(BDLocation arg0) {
		// // do nothing
		// }

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location == null || TextUtils.isEmpty(location.getCity())) {
				return;
			}
			// 获取当前城市，
			String cityName = location.getCity();
			String xiangxidi = location.getAddrStr();
			String province = location.getProvince();
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			String district = location.getDistrict();
			if (cityName.endsWith("自治州")) {
				cityName = district;
			}
			if (cityName.contains("市")) {
				cityName = cityName.substring(0, cityName.length() - 1);
			}
			if (cityName.contains("地区")) {
				cityName = cityName.substring(0, cityName.length() - 2);
			}
			if(cityName.equals("黔西南布依族苗族自治州"))
			{
				cityName = "兴义";
			}
			mApplication.setDingweicity(cityName);
			mApplication.setCurrentCityLatitude(latitude + "");
			mApplication.setCurrentCityLongitude(longitude + "");
			// 把定位城市存入到缓存里
			SharedPreferences sharedPref = getSharedPreferences("sharedPref",
					MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("dingweiCity", cityName);
			editor.putString("CurrentCityLongitude", longitude + "");
			editor.putString("CurrentCityLatitude", latitude + "");
			editor.putString("province", province);
			editor.putString("xiangxidi", xiangxidi);
			editor.commit();
			mApplication.setXiangxidizhi(xiangxidi);
			mApplication.setProvince(province);
			if (xiangxidi != null) {
				location_ok_tv.setText(xiangxidi);
			}
			lat = latitude;
			lng = longitude;
			mLocationClient.stop();

		}

	};

	private void init() {
		// TODO Auto-generated method stub

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.exposure_post_rg);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioid = arg0.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) DiscoverExposureActivity.this
						.findViewById(radioid);
				rb.setChecked(true);
			}
		});
		tongbuditu__share = (TextView) findViewById(R.id.exposure_tongbuditu__share);
		noScrollgridview = (MyGridView) findViewById(R.id.exposure_noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				imm.hideSoftInputFromWindow(say_sth.getWindowToken(), 0);
				if (arg2 == BimpHelper.bmp.size()) {
					new PopupWindows(DiscoverExposureActivity.this,
							noScrollgridview);
				} else {
					Intent intent = new Intent(DiscoverExposureActivity.this,
							DiscoverPostBlogCheckPicActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (BimpHelper.bmp.size() <= 8) {
				return (BimpHelper.bmp.size() + 1);
			} else {
				return 9;
			}

		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				final int coord = position;
				ViewHolder holder = null;
				if (convertView == null) {

					convertView = inflater.inflate(
							R.layout.discover_postblog_img_item, parent, false);
					holder = new ViewHolder();
					holder.image = (ImageView) convertView
							.findViewById(R.id.item_grida_image);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position == BimpHelper.bmp.size()) {
					holder.image.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.icon_addpic_unfocused));
					if (position == 9 || position > 9) {
						holder.image.setVisibility(View.INVISIBLE);
					}
				} else {
					holder.image.setImageBitmap(BimpHelper.bmp.get(position));
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (BimpHelper.max == BimpHelper.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = BimpHelper.drr
										.get(BimpHelper.max);
								/**
								 * // * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转 //
								 */
								Bitmap bm = BimpHelper.revitionImageSize(path);
								BimpHelper.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								BimpHelper.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (OutOfMemoryError e) {
								MyLog.e("weibao Exception" + e);
							} catch (Exception e) {
								MyLog.e("weibao Exception" + e);
							}

						}
					}
				}
			}).start();
		}
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext,
					R.layout.discover_postblog_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			try {
				showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// photo();
					// doTakePhoto();
					startActionCamera();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(DiscoverExposureActivity.this,
							DiscoverAlbumSelectActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			System.out.println(">>>>>>>>set" + 1);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/myimage/", String.valueOf(System.currentTimeMillis()));
			// File file = new File(getFilesDir()
			// + "/myimage/", String.valueOf(System.currentTimeMillis())
			// + ".jpg");
			System.out.println(">>>>>>>>set" + 2);
			path = file.getPath();
			System.out.println(">>>>>path" + path);
			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.e("weibao Exception" + e);
		}

	}

	private String path = "";
	private static final int TAKE_PICTURE = 0x000000;

	private void showFace() {
		// mFace.setImageResource(R.drawable.widget_bar_keyboard);
		mFace.setTag(1);
		createdia();
		mGridView.setVisibility(View.VISIBLE);
	}

	private void showOrHideIMM() {
		if (mFace.getTag() == null) {
			// 隐藏软键盘
			imm.hideSoftInputFromWindow(say_sth.getWindowToken(), 0);
			// 显示表情
			showFace();
		} else {
			// 显示软键盘
			imm.showSoftInput(say_sth, 0);
			// 隐藏表情
			hideFace();
		}
	}

	void createdia() {

		AlertDialog.Builder builder = new Builder(DiscoverExposureActivity.this);
		LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.discover_face, null);
		mGVFaceAdapter = new DiscoverFaceGridViewAdapter(this);
		mGridView = (GridView) linearLayout.findViewById(R.id.tweet_pub_faces);
		mGridView.setAdapter(mGVFaceAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 插入的表情
				SpannableString ss = new SpannableString(view.getTag()
						.toString());
				Drawable d = getResources().getDrawable(
						(int) mGVFaceAdapter.getItemId(position));
				d.setBounds(0, 0, 50, 50);// 设置表情图片的显示大小
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, view.getTag().toString().length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 在光标所在处插入表情
				say_sth.getText().insert(say_sth.getSelectionStart(), ss);
			}
		});

		AlertDialog dialog = builder.create();
		dialog.setView(linearLayout, 0, 0, 0, 0);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	@Override
	protected void onDestroy() {
		mContext = null;
		BimpHelper.clear();
		super.onDestroy();
	}

	private View.OnClickListener faceClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			showOrHideIMM();
		}
	};

	void tanchu() {
		int xx = citynames.size();
		final String[] citys = new String[xx];
		new ArrayAdapter<String>(DiscoverExposureActivity.this,
				android.R.layout.simple_spinner_item, citynames);
		for (int i = 0; i < citynames.size(); i++) {
			citys[i] = citynames.get(i);
		}

		new AlertDialog.Builder(this).setTitle("曝光城市")// 对话框标题
				.setItems(citys, new DialogInterface.OnClickListener() {// 每一条的名称
							public void onClick(DialogInterface dialog,
									int which) {// 响应点击事件
								locate_tv.setText(citys[which]);
							}
						}).show();
	}

	void tongbu() {
		final String[] tongbu = { "是", "否" };
		new ArrayAdapter<String>(DiscoverExposureActivity.this,
				android.R.layout.simple_spinner_item, tongbu);
		new AlertDialog.Builder(this).setTitle("分享到地图")// 对话框标题
				.setItems(tongbu, new DialogInterface.OnClickListener() {// 每一条的名称
							public void onClick(DialogInterface dialog,
									int which) {// 响应点击事件
								tongbuditu__share.setText(tongbu[which]);
							}
						}).show();
	}

	void tanchuopen() {

		final String[] limits = new String[] { "公开", "私有" };
		new ArrayAdapter<String>(DiscoverExposureActivity.this,
				android.R.layout.simple_spinner_item, limits);

		new AlertDialog.Builder(this).setTitle("是否公开")// 对话框标题
				.setItems(limits, new DialogInterface.OnClickListener() {// 每一条的名称
							public void onClick(DialogInterface dialog,
									int which) {// 响应点击事件

								switch (which) {
								case 0:
									open_tv.setText("公开");
									isopen = 1;
									break;
								case 1:
									open_tv.setText("私有");
									isopen = 0;
									break;
								default:
									break;
								}

							}
						}).show();
	}

	GridView mGridView;

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void hideFace() {
		// mFace.setImageResource(R.drawable.widget_bar_face);
		mFace.setTag(null);
		// mGridView.setVisibility(View.GONE);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		/*
		 * if (username.equals("")) { nickname_text.setText("游客"); } else {
		 * nickname_text.setText(username); }
		 */

		adapter.update();
	}

	ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
	List<City> citys = new ArrayList<City>();
	List<String> citynames = new ArrayList<String>();
	CityDB mCityDB;

	void qu() {
		initcitys.clear();
		citys.clear();
		citynames.clear();
		initcitys = mCityDB
				.queryBySqlReturnArrayListHashMap("select * from addcity");
		for (int i = 0; i < initcitys.size(); i++) {

			City city = new City(initcitys.get(i).get("province").toString(),
					initcitys.get(i).get("name").toString(), initcitys.get(i)
							.get("number").toString(), initcitys.get(i)
							.get("pinyin").toString(), initcitys.get(i)
							.get("py").toString());

			if ("1".equals(initcitys.get(i).get("islocation"))
					&& citys.size() > 0) {
				City mCityFirst = citys.get(0);
				citys.set(0, city);
				citys.add(mCityFirst);
			} else {
				citys.add(city);
			}
			citys.add(city);
			String cityname = initcitys.get(i).get("name").toString();
			citynames.add(cityname);

		}
	}

	private void showIMM() {
		mFace.setTag(1);
		showOrHideIMM();
	}

	private void initView() {
		// TODO Auto-generated method stub

		isexposureString = (CheckBox) findViewById(R.id.isexposure);
		locate_tv = (TextView) findViewById(R.id.exposure_locate_tv);
		open_tv = (TextView) findViewById(R.id.exposure_open_tv);
		if (!"".equals(application.getDingweicity())
				&& application.getDingweicity() != null) {
			locate_tv.setText(application.getDingweicity());
		} else {
			if (!"".equals(application.selectedCity)
					&& application.selectedCity != null) {
				locate_tv.setText(application.selectedCity);
			} else {
				locate_tv.setText("nocity");
			}

		}

		String[] limits = new String[] { "公开", "私有" };

		say_sth = (EditText) findViewById(R.id.exposure_say_new_content);

		say_sth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 显示软键盘
				showIMM();
			}
		});
		star_rate = (TextView) findViewById(R.id.exposure_public_status);
		cancel = (ImageView) findViewById(R.id.exposure_cancelWeibo);
		cancel.setOnClickListener(this);
		send = (Button) findViewById(R.id.exposure_next_like);
		send.setOnClickListener(this);

		rb1 = (RadioButton) findViewById(R.id.exposure_post_rb1);
		rb2 = (RadioButton) findViewById(R.id.exposure_post_rb2);
		rb3 = (RadioButton) findViewById(R.id.exposure_post_rb3);
		post_rg = (RadioGroup) findViewById(R.id.exposure_post_rg);

		post_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.exposure_post_rb1:
					rb1.setChecked(true);

					huanjing = 1;
					rb1.setTextColor(Color.argb(255, 106, 161, 0));
					rb2.setTextColor(Color.BLACK);
					rb3.setTextColor(Color.BLACK);

					break;
				case R.id.exposure_post_rb2:
					rb2.setChecked(true);
					huanjing = 2;

					rb2.setTextColor(Color.argb(255, 106, 161, 0));
					rb3.setTextColor(Color.BLACK);
					rb1.setTextColor(Color.BLACK);

					break;
				case R.id.exposure_post_rb3:
					rb3.setChecked(true);
					huanjing = 3;
					rb3.setTextColor(Color.argb(255, 106, 161, 0));
					rb1.setTextColor(Color.BLACK);
					rb2.setTextColor(Color.BLACK);

					break;
				default:
					break;
				}
			}
		});
		room_ratingbar2 = (RatingBar) findViewById(R.id.exposure_room_ratingbar2);
		room_ratingbar2
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						numStars2 = (int) rating;

					}
				});

		location_text = (TextView) findViewById(R.id.exposure_location_text);
		if (!"".equals(application.getDingweicity())
				&& application.getDingweicity() != null) {
			location_text.setText(application.getDingweicity());
		} else {
			if (!"".equals(application.selectedCity)
					&& application.selectedCity != null) {
				location_text.setText(application.selectedCity);
			} else {
				location_text.setText("nocity");
			}

		}

		location_ok_tv = (TextView) findViewById(R.id.exposure_location_ok_tv);
		location_ok_tv.setText(locatecity);
	}

	private void initListener() {
	}

	ProgressDialog prDialog;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exposure_cancelWeibo:
			finish();
			break;

		case R.id.exposure_location_ok_tv:

			if (flag) {
				location_ok_tv.setText(locatecity);
			} else {
				location_ok_tv.setText("");
			}
			flag = !flag;
			break;
		case R.id.exposure_next_like:
			MyPostExposure myPostExposure;
			locatecity = mApplication.getXiangxidizhi();
			if (StringUtil.isEmpty(locatecity)) {
				location_ok_tv.setText(locatecity);
			}

			final String content = say_sth.getText().toString();
			if ("".equals(content)) {
				ToastUtil.showLong(this, "你没有输入任何内容！");
				break;
			}
			if (content.length() > 140) {
				ToastUtil.showLong(this, "文字数应小于140！");
				break;
			}
			if (lat == 0 || lng == 0) {
				if ((null != mApplication.getCurrentCityLatitude() && !""
						.equals(mApplication.getCurrentCityLatitude()))
						&& (null != mApplication.getCurrentCityLongitude() && !""
								.equals(mApplication.getCurrentCityLongitude()))) {
					try {
						lat = Double.parseDouble(mApplication
								.getCurrentCityLatitude());
						lng = Double.parseDouble(mApplication
								.getCurrentCityLongitude());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.showLong(this, "暂未获取到您的位置信息，无法发表");
					break;
				}
			}
			if (StringUtil.isEmpty(locatecity)) {
				ToastUtil.showLong(this, "暂未获取到您的位置信息，无法发表");
				break;
			}
			String city_name = locate_tv.getText().toString();
			String province = mCityDB.getprovicecity(city_name);
			// String user_name = nickname_text.getText().toString();
			String address = location_ok_tv.getText().toString();
			String share = tongbuditu__share.getText().toString();
			int isShare = 0;
			if (share.equals("是")) {
				isShare = 1;
			} else {
				isShare = 0;
			}
			if (isexposureString.isChecked()) {
				isanonymous = 0;
			} else {
				isanonymous = 1;
			}
			myPostExposure = new MyPostExposure(isShare, lat + "", lng + "",
					mApplication.getUsename(), content, huanjing, numStars2,
					province, city_name, address, isopen, BimpHelper.drr,
					System.currentTimeMillis(), mApplication.getUserId(),
					mApplication.getUserPic(), pollutionType, isanonymous);
			try {
				if (NetUtil.getNetworkState(DiscoverExposureActivity.this) == NetUtil.NETWORN_NONE) {
					Toast.makeText(DiscoverExposureActivity.this, "请检查您的网络", 0)
							.show();
					return;
				}
				// try {
				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE);
				// boolean isOpen = imm.isActive();
				// // isOpen若返回true，则表示输入法打开
				// if (isOpen && getCurrentFocus() != null) {
				// ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				// .hideSoftInputFromWindow(getCurrentFocus()
				// .getWindowToken(),
				// InputMethodManager.HIDE_NOT_ALWAYS);
				// }
				// } catch (Exception e) {
				// e.printStackTrace();
				// }

				loadTask.execute(myPostExposure);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		}
	}

	public void doTakePhoto() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs();// 创建照片的存储目录
			}
			String FilePath = PHOTO_DIR + File.separator
					+ String.valueOf(System.currentTimeMillis());
			mCurrentPhotoFile = new File(FilePath);// 给新照的照片文件命名
			Uri imageUri = Uri.fromFile(mCurrentPhotoFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TAKE_PICTURE);
		} catch (OutOfMemoryError e) {
			MyLog.e("weibao Exception" + e);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.e("weibao Exception" + e);
		}

	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	// 请求Gallery程序
	public void doPickPhotoFromGallery1() {
		try {
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
		}
	}

	public void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);

	}

	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "false");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	public static final String KEY_PHOTO_PATH = "photo_path";

//	public Bitmap convertToBitmap(String path, int w, int h) {
//		try {
//			BitmapFactory.Options opts = new BitmapFactory.Options();
//			// 设置为ture只获取图片大小
//			opts.inJustDecodeBounds = true;
//			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//			// 返回为空
//			BitmapFactory.decodeFile(path, opts);
//			int width = opts.outWidth;
//			int height = opts.outHeight;
//			float scaleWidth = 0.f, scaleHeight = 0.f;
//			if (width > w || height > h) {
//				// 缩放
//				scaleWidth = ((float) width) / w;
//				scaleHeight = ((float) height) / h;
//			}
//			opts.inJustDecodeBounds = false;
//			float scale = Math.max(scaleWidth, scaleHeight);
//			opts.inSampleSize = (int) scale;
//			WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
//					BitmapFactory.decodeFile(path, opts));
//		} catch (OutOfMemoryError e) {
//			// TODO: handle exception
//			MyLog.e("weibao Exception" + e);
//		} catch (Exception e) {
//			// TODO: handle exception
//			MyLog.e("weibao Exception" + e);
//		}
//
//		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
//	}

	public Bitmap convertToBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), width, height, true);
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_CODE_GETIMAGE_BYCAMERA:
			startActionCrop(photoUri);// 拍照后裁剪
			break;
		case REQUEST_CODE_GETIMAGE_BYCROP:
			startActionCrop(data.getData());// 选图后裁剪
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:
			// uploadNewPhoto();// 上传新照片
			path = protraitPath;
			//
			if (BimpHelper.drr.size() < 8 && resultCode == -1) {
				BimpHelper.drr.add(path);
			}
			break;
		}
	}

	public void showimg(Bitmap photo) {

		// 缓存用户选择的图片
		img_share.setImageBitmap(photo);
		img_share.setVisibility(View.VISIBLE);
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	// 裁剪头像的绝对路径
	private Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			// UIHelper.ToastMessage(UserInfo.this, "无法保存上传的头像，请检查SD卡是否挂载");
			Toast.makeText(mContext, "无法保存上传的头像，请检查SD卡是否挂载", 2000).show();
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// 如果是标准Uri
		if (ImageUtils.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath(
					DiscoverExposureActivity.this, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = ImageUtils.isEmpty(ext) ? "jpg" : ext;
		// 照片命名
		String cropFileName = "osc_crop_" + timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);

		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

	// 拍照保存的绝对路径
	private Uri getCameraTempFile() {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			// To.ToastMessage(UserInfo.this, "无法保存上传的头像，请检查SD卡是否挂载");
			Toast.makeText(mContext, "无法保存上传的头像，请检查SD卡是否挂载", 2000).show();
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		// 照片命名
		String cropFileName = "osc_camera_" + timeStamp + ".jpg";
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);
		cropUri = Uri.fromFile(protraitFile);
		this.photoUri = this.cropUri;
		return this.cropUri;
	}

	/**
	 * 相机拍照
	 * 
	 * @param output
	 */
	private void startActionCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
		startActivityForResult(intent,
				ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	private void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);// 裁剪框比例
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", CROP);// 输出图片大小
		// intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	private class UpLoadTask extends AsyncTask<MyPostExposure, Void, Boolean> {
		MyPostExposure myPostExposure;

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(DiscoverExposureActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("正在努力上传中……");
			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(MyPostExposure... params) {
			myPostExposure = (MyPostExposure) params[0];
			MyLog.i(" (myPostWeiboInfo) :" + myPostExposure.toString());
			String url = UrlComponent.exposure_Post;
			if (null == myPostExposure) {
				return false;
			}
			BusinessSearch search = new BusinessSearch();
			DiscoverBlogUpLoadResult _Result;
			DiscoverFlagModel _ResultFlag;
			try {
				_Result = search.exposureUpLoadTask(url, myPostExposure);
				MyLog.i("<<<<<<<<<post" + myPostExposure.getPollutionType());
				String postid = _Result.getPostid();
				if (myPostExposure.getPaths().size() < 1) {
					if (!"".equals(postid)) {
						return true;
					} else {
						return false;
					}
				}
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < myPostExposure.getPaths().size(); i++) {
					String Str = myPostExposure
							.getPaths()
							.get(i)
							.substring(
									myPostExposure.getPaths().get(i)
											.lastIndexOf("/") + 1,
									myPostExposure.getPaths().get(i)
											.lastIndexOf("."));
					list.add(FileUtils.SDPATH + Str + ".JPEG");
					Bitmap bitmap = BimpHelper.revitionImageSize(myPostExposure
							.getPaths().get(i));
					String file_name = CommonUtil.BitmapToHexString(bitmap);
					String ur = UrlComponent.uploadPic_Post;
					_ResultFlag = search.blogpPostPic(ur, postid, file_name);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.i("提交数据 Exception" + e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			prDialog.cancel();
			if (result) {
				BimpHelper.bmp.clear();
				BimpHelper.drr.clear();
				BimpHelper.max = 0;
				FileUtils.deleteDir();
				adapter.update();
				noScrollgridview.setAdapter(adapter);
				Toast.makeText(DiscoverExposureActivity.this, "曝光成功", 0).show();
				if (DiscoverExposureListActivity.dActivity != null) {
					DiscoverExposureListActivity.dActivity.finish();
				}
				Intent intent = new Intent();
				intent.setAction(DiscoverBlogListActivity.MESSAGE_RECEIVED_ACTION2);
				getApplicationContext().sendBroadcast(intent);
				finish();
			} else {
				if (myPostExposure.saveInfo(mContext, myPostExposure)) {
					MyLog.i("Weibo save success currentmyPostWeiboInfo = "
							+ myPostExposure.toString());
					Intent intent = new Intent();
					intent.setAction(DiscoverBlogListActivity.MESSAGE_RECEIVED_ACTION2);
					getApplicationContext().sendBroadcast(intent);
				}
				;

				Toast.makeText(DiscoverExposureActivity.this, "曝光失败", 0).show();
			}
			send.setClickable(true);
			Intent intentList = new Intent(DiscoverExposureActivity.this,
					DiscoverBlogListActivity.class);
			startActivity(intentList);
		}
	}

}
