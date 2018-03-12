package com.jy.environment.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.kymjs.aframe.ui.widget.RoundImageView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.SQLiteDALModelNoiseHistory;
import com.jy.environment.database.model.ModelNoiseHistory;
import com.jy.environment.model.UserGetUerInfoModel;
import com.jy.environment.model.UserUpLoadPicResultModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.FileUtils;
import com.jy.environment.util.ImageUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户信息界面
 * 
 * @author baiyuchuan
 * 
 */
public class UserInfoActivity extends ActivityBase implements OnClickListener {

	private ProgressDialog prDialog;
	private Bitmap bMap = null;
	private Handler mHandler;
	private AsyncTask myUploadTask;
	// private final static int CROP = 400;
	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/weibao/Portrait/";
	private Uri origUri;
	private Uri cropUri;
	private File protraitFile;
	private Bitmap protraitBitmap;
	private String protraitPath;
	public final static String SDCARD_MNT = "/mnt/sdcard";
	public final static String SDCARD = "/sdcard";

	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 请求相机 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	private ImageView back;
	private TextView after_usename_tv;
	private Button after_loginRegister;
	private ImageView after_postImg;
	private Button afterLoginExit, afterLogin_change;
	public static final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	/** 如果登录成功后，保存用户名到sharePreferences 以便下次不在输入了 */
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";

	/** 如果登录成功，保存密码到sharePreferences 以便下次不在输入了 */
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";
	/** 如果登录成功，保存id到sharePreferences 以便下次不在输入了 */
	private String SHARE_LOGIN_ID = "MAP_LOGIN_USERID";

	private SQLiteDALModelNoiseHistory mSqLiteDALModelNoiseHistory;

	RoundImageView cover_user_photo;
	String[] items = { "相机拍摄", "手机相册" };
	String SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory()
			.toString();
	public static final String SAVE_IN_SDCARD = "/weibao/";
	private File mCurrentPhotoFile;
	private Bitmap share_bitmap;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	String file_name = "";
	String username;
	String userId;
	String userPic;
	String nicheng;
	String mail;
	String gender;
	String phone;
	String isEmailBind;
	String isPhoneBind;
	ImageLoadingListener animateFirstListener;

	private RelativeLayout nc_layout, mail_layout, gender_layout, phone_layout;
	private TextView nc_text, mail_text, gender_text, nc_title, phone_text,
			gender_text_null, nc_text_null, phone_text_null;

	private Handler mhandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				 try {
						MyLog.i("weibao userPic:" + userPic);
				if (!"".equals(userPic)) {
					imageLoader
							.displayImage(userPic, cover_user_photo, options);
				}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info1);
		username = WeiBaoApplication.getInstance().getUsename().toString();
		userId = WeiBaoApplication.getInstance().getUserId().toString();
		mSqLiteDALModelNoiseHistory = new SQLiteDALModelNoiseHistory(
				ModelNoiseHistory.class);
		cover_user_photo = (RoundImageView) findViewById(R.id.cover_user_photo);
		cover_user_photo.setOnClickListener(this);

		initView();
		initListener();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.logo31)
				.showImageForEmptyUri(R.drawable.logo31)
				.showImageOnFail(R.drawable.logo31).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();

		animateFirstListener = new AnimateFirstDisplayListener();
		after_usename_tv = (TextView) findViewById(R.id.after_usename_tv);
		after_usename_tv.setText(WeiBaoApplication.getUsename().toString());
		afterLoginExit = (Button) findViewById(R.id.after_exit);
		back = (ImageView) findViewById(R.id.afterlogin_return_iv);
		afterLoginExit.setOnClickListener(this);
		back.setOnClickListener(this);
		GetUserInfoTask getUserInfoTask = new GetUserInfoTask();
		getUserInfoTask.execute();
	}

	private void initListener() {
		nc_layout.setOnClickListener(this);
		mail_layout.setOnClickListener(this);
		gender_layout.setOnClickListener(this);
		phone_layout.setOnClickListener(this);
	}

	private void initView() {
		nc_layout = (RelativeLayout) findViewById(R.id.user_info_nc);
		mail_layout = (RelativeLayout) findViewById(R.id.user_info_mail);
		gender_layout = (RelativeLayout) findViewById(R.id.user_info_sex);
		phone_layout = (RelativeLayout) findViewById(R.id.user_info_phone);
		nc_text = (TextView) findViewById(R.id.user_info_nc_text);
		mail_text = (TextView) findViewById(R.id.user_info_mail_text);
		gender_text = (TextView) findViewById(R.id.user_info_sex_text);
		phone_text = (TextView) (TextView) findViewById(R.id.user_info_phone_text);
		gender_text_null = (TextView) findViewById(R.id.user_info_sex_text_null);
		nc_text_null = (TextView) findViewById(R.id.user_info_nc_text_null);
		phone_text_null = (TextView) findViewById(R.id.user_info_phone_text_null);
		nc_title = (TextView) findViewById(R.id.nickname_title);
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;

				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.afterlogin_return_iv:
			finish();
			break;

		case R.id.after_exit:
			MobclickAgent.onEvent(UserInfoActivity.this, "WExit");
			clearShareName();
			clearSharePassword();
			clearShareId();
			WeiBaoApplication.setUsename("");
			WeiBaoApplication.setUserId("");
			WeiBaoApplication.setUserNc("");
			WeiBaoApplication.setUserPic("");
			MyLog.i("xu1123:" + "0");
			WeiBaoApplication.setIsEmailBind("0");
			saveInfoSharePreferences(UserInfoActivity.this,"", "", "", "0",
					"0");
//			WeatherAdapter.userName = "";
//			WeatherAdapter.userPwd = "";
			ClearDataTask clearDataTask = new ClearDataTask();
			clearDataTask.execute();
			finish();
			break;
		case R.id.cover_user_photo:
			MobclickAgent.onEvent(UserInfoActivity.this, "WHead");
			CharSequence[] items = { getString(R.string.img_from_album),
					getString(R.string.img_from_camera) };
			imageChooseItem(items);
			break;
		case R.id.user_info_nc:
			MobclickAgent.onEvent(UserInfoActivity.this, "WPetName");
			Intent intent = new Intent(UserInfoActivity.this,
					UserInfoncActivity.class);
			intent.putExtra("content", nicheng);
			intent.putExtra("userid", userId);
			intent.putExtra("type", "1");
			startActivity(intent);
			break;
		case R.id.user_info_mail:
			MobclickAgent.onEvent(UserInfoActivity.this, "WEmail");
			if (null == isEmailBind || isEmailBind.equals("0")) {
				Intent intent2 = new Intent(UserInfoActivity.this,
						UserInfomailActitvity.class);
				intent2.putExtra("content", mail);
				intent2.putExtra("userid", userId);
				intent2.putExtra("type", "2");
				startActivity(intent2);
			}

			if (isEmailBind.equals("1")) {
				WeiBaoApplication.setIsEmailBind("1");
				AlertDialog.Builder builder = new Builder(this);
				builder.setMessage("您已绑定此邮箱，如设置过登录密码，可以通过邮箱+密码的方式登录空气质量");
				builder.setTitle("提示");
				builder.setPositiveButton("我知道了",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}

						});
				builder.setNegativeButton("修改邮箱",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent5 = new Intent(
										UserInfoActivity.this,
										UserInfoUnbindMailActivity.class);
								intent5.putExtra("content", mail);
								intent5.putExtra("userid", userId);
								intent5.putExtra("type", "5");
								startActivity(intent5);

							}
						});
				builder.create().show();

			}

			break;
		case R.id.user_info_sex:
			MobclickAgent.onEvent(UserInfoActivity.this, "WSex");
			Intent intent3 = new Intent(UserInfoActivity.this,
					UserInfogenderActitvity.class);
			intent3.putExtra("content", gender);
			intent3.putExtra("userid", userId);
			intent3.putExtra("type", "3");
			startActivity(intent3);
			break;
		case R.id.user_info_phone:
			MobclickAgent.onEvent(UserInfoActivity.this, "WPhone");
			if (null == isPhoneBind || isPhoneBind.equals("0")) {
				Intent intent4 = new Intent(UserInfoActivity.this,
						UserValidatePhoneActivity.class);
				intent4.putExtra("content", phone);
				intent4.putExtra("userid", userId);
				intent4.putExtra("type", 4);
				startActivity(intent4);

			}

			if (isPhoneBind.equals("1")) {
				AlertDialog.Builder builder = new Builder(this);
				builder.setMessage("您已绑定此手机号，如设置过登录密码，可以通过手机号+密码的方式登录空气质量");
				builder.setTitle("提示");
				builder.setPositiveButton("我知道了",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}

						});
				builder.setNegativeButton("修改手机号",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent6 = new Intent(
										UserInfoActivity.this,
										UserUnbindPhoneActivity.class);
								intent6.putExtra("content", phone);
								intent6.putExtra("userid", userId);
								intent6.putExtra("type", 6);
								startActivity(intent6);

							}
						});
				builder.create().show();

			}

		default:
			break;
		}
	}

	public class ClearDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				WeiBaoApplication.getInstance().setTag_page(0);
				mSqLiteDALModelNoiseHistory
						.execSQL("delete from ModelNoiseHistory where isupload = '1'");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}

	}

	@Override
	protected void onResume() {
		nicheng = WeiBaoApplication.getUserNc();
		mail = WeiBaoApplication.getUserMail();
		gender = WeiBaoApplication.getUserGener();
		phone = WeiBaoApplication.getPhone();
		isEmailBind = WeiBaoApplication.getIsEmailBind();
		isPhoneBind = WeiBaoApplication.getIsPhoneBind();
		imageLoader.displayImage(WeiBaoApplication.getUserPic(),
				cover_user_photo, options, animateFirstListener);
		if (gender.equals("男") || gender.equals("女")) {
			gender_text_null.setVisibility(View.GONE);
			gender_text.setVisibility(View.VISIBLE);
		} else {
			gender_text_null.setVisibility(View.VISIBLE);
			gender_text.setVisibility(View.GONE);
		}
		if (nicheng == null) {
			nc_text_null.setVisibility(View.VISIBLE);
			nc_text.setVisibility(View.GONE);
		} else {
			nc_text_null.setVisibility(View.GONE);
			nc_text.setVisibility(View.VISIBLE);
		}
		nc_text.setText(nicheng);
		nc_title.setText(nicheng);
		setMailTextValue(mail);
		setPhoneTextValue(phone);
		gender_text.setText(gender);
		super.onResume();
	}

	public static final int SELECT_BY_PICK_PHOTO = 2;

	public void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_BY_PICK_PHOTO);

	}

	String IMAGE_CAPTURE_NAME = getPhotoFileName();
	public static final int SELECT_BY_TACK_PHOTO = 1;

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		return new UUID(16, 16).toString() + ".jpg";
	}

	public void doTakePhoto() {

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

		File file = new File(SDCARD_ROOT_PATH + SAVE_IN_SDCARD);
		if (!file.exists()) {
			file.mkdirs();
		}

		mCurrentPhotoFile = new File(file, IMAGE_CAPTURE_NAME);// 给新照的照片文件命名
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentPhotoFile));

		startActivityForResult(intent, SELECT_BY_TACK_PHOTO);

	}

	private void getImage(String imagePath) {

		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			int orientation;
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[32 * 1024];
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap bMap;
			bMap = BitmapFactory.decodeFile(imagePath, options);
			if (bMap.getHeight() < bMap.getWidth()) {
				orientation = 90;
			} else {
				orientation = 0;
			}

			Bitmap bMapRotate;
			if (orientation != 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(orientation);
				bMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(),
						bMap.getHeight(), matrix, true);
			} else
				bMapRotate = Bitmap.createScaledBitmap(bMap, bMap.getWidth(),
						bMap.getHeight(), true);
			share_bitmap = bMapRotate;
			file_name = CommonUtil.BitmapToHexString(share_bitmap);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 清除密码 */
	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
		share = null;

	}

	/** 清除用户信息 */
	private void clearShareName() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_USERNAME, "").commit();
		share.edit().putString("MAP_LOGIN_USERNC", "").commit();
		share.edit().putString("MAP_LOGIN_USERPIC", "").commit();

	}

	/** 清除用户id */
	private void clearShareId() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_ID, "").commit();
		share = null;

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
			ToastMessage(UserInfoActivity.this, "无法保存上传的头像，请检查SD卡是否挂载");
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// 如果是标准Uri
		if (ImageUtils.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath(UserInfoActivity.this,
					uri);
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
			ToastMessage(UserInfoActivity.this, "无法保存上传的头像，请检查SD卡是否挂载");
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
		this.origUri = this.cropUri;
		return this.cropUri;
	}

	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(this)
				.setTitle("上传头像").setIcon(android.R.drawable.btn_star)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 相册选图
						if (item == 0) {
							startImagePick();
						}
						// 手机拍照
						else if (item == 1) {
							startActionCamera();
						}
					}
				}).create();

		imageDialog.show();
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	private void startImagePick() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
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
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", CROP);// 输出图片大小
		// intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent,
				ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	/**
	 * 上传新照片
	 */

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
			startActionCrop(origUri);// 拍照后裁剪
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
			startActionCrop(data.getData());// 选图后裁剪
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
			upload();
			break;
		}
	}

	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	private void upload() {
		JSONObject jsobject = new JSONObject();
		try {
			if (!ImageUtils.isEmpty(protraitPath) && protraitFile.exists()) {
				getImage(protraitPath);
			} else {
				ToastMessage(UserInfoActivity.this, "图像不存在，上传失败·");
			}
			if ("".equals(file_name)) {
				ToastMessage(UserInfoActivity.this, "请选择头像");
				return;
			}
			UpLoadUserPicTask loadUserPicTask = new UpLoadUserPicTask();
			loadUserPicTask.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveSharePreferences(String userPic) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString("MAP_LOGIN_USERPIC", userPic).commit();
		share = null;
	}

	private class UpLoadUserPicTask extends
			AsyncTask<String, Void, UserUpLoadPicResultModel> {

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(UserInfoActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("正在努力上传中……");

			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected UserUpLoadPicResultModel doInBackground(String... params) {
			String url = UrlComponent.postPicUrl_Post;
			BusinessSearch search = new BusinessSearch();
			UserUpLoadPicResultModel _Result = null;
			try {
				_Result = search.upLoadUserPic(url, userId, file_name);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(UserUpLoadPicResultModel result) {
			super.onPostExecute(result);
			try {
				MyLog.i("weibao result:" + result);
			prDialog.cancel();
			if (null != result && result.isFlag()) {
				WeiBaoApplication.setUserPic(result.getUserpic());
				saveSharePreferences(result.getUserpic());
				Toast.makeText(UserInfoActivity.this, "上传成功", 0).show();
				cover_user_photo.setImageURI(Uri.fromFile(protraitFile));
			} else {
				Toast.makeText(UserInfoActivity.this, "上传失败", 0).show();
			}
			
		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}
	}

	private class GetUserInfoTask extends
			AsyncTask<String, Void, UserGetUerInfoModel> {
		@Override
		protected UserGetUerInfoModel doInBackground(String... params) {
			String url = UrlComponent.getUserInfoById_Get(userId);
			BusinessSearch search = new BusinessSearch();
			UserGetUerInfoModel _Result = null;
			try {
				_Result = search.getUserInfo(url);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return _Result;
		}

		@Override
		protected void onPostExecute(UserGetUerInfoModel result) {
			super.onPostExecute(result);
			try {
				MyLog.i("weibao result:" + result);
			if (null != result) {
				MyLog.i("reuslt" + result);
				userPic = result.getUserpic();
				nicheng = result.getNc();
				mail = result.getMail();
				MyLog.i("<<>>mail" + mail);
				gender = result.getGender();
				phone = result.getPhone();
				MyLog.i("<<>>phone" + phone);
				isEmailBind = result.getIsEmailBind();
				MyLog.i("<<>>isEmailBind" + isEmailBind);
				isPhoneBind = result.getIsPhoneBind();
				MyLog.i("<<>>isPhoneBind" + isPhoneBind);
				if (nicheng != null) {
					if (nicheng.equals("")) {
						nc_text_null.setVisibility(View.VISIBLE);
						nc_text.setVisibility(View.GONE);
					} else {
						nc_text.setText(nicheng);
						nc_title.setText(nicheng);
					}
				}
				setMailTextValue(mail);
				setPhoneTextValue(phone);
				if (gender != null) {
					if (gender.equals("无")) {
						gender_text.setVisibility(View.GONE);
						gender_text_null.setVisibility(View.VISIBLE);
					} else {
						gender_text.setText(gender);
					}
				}
				// WeiBaoApplication.setUserNc(nicheng);
				WeiBaoApplication.setUserMail(mail);
				WeiBaoApplication.setUserGener(gender);
				WeiBaoApplication.setPhone(phone);
				MyLog.i("xu1123:" + isEmailBind);
				WeiBaoApplication.setIsEmailBind(isEmailBind);
				WeiBaoApplication.setIsPhoneBind(isPhoneBind);
				// WeiBaoApplication.setUserPic(userPic);
				saveInfoSharePreferences(UserInfoActivity.this,nicheng, mail, phone, isEmailBind,
						isPhoneBind);
				mhandler.sendEmptyMessage(0);
			}
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
		}
	}

	public static void saveInfoSharePreferences(Context context,String userNc, String mail,
			String phone, String isEmailBind, String isPhoneBind) {
		SharedPreferences share = context.getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString("MAP_LOGIN_USERNC", userNc).commit();
		share.edit().putString("MAP_LOGIN_USERMAIL", mail).commit();
		share.edit().putString("MAP_LOGINPHONE", phone).commit();
		share.edit().putString("MAP_LOGIN_USERISEMAIL", isEmailBind).commit();
		share.edit().putString("MAP_LOGIN_USERISPHONE", isPhoneBind).commit();
		share = null;
	}

	private void setMailTextValue(String m) {
		if (null == mail) {
			mail_text.setText("还没验证哦！");
			mail_text.setTextColor(getResources().getColor(R.color.green));
		} else {
			if ("".equals(mail)) {
				mail_text.setText("还没验证哦！");
				mail_text.setTextColor(getResources().getColor(R.color.green));
			} else {
				if (isEmailBind.equals("0")) {
					mail_text.setText(mail + "（未激活）");
					mail_text.setTextColor(getResources().getColor(
							R.color.green));
				} else {
					mail_text.setText(mail);
					mail_text.setTextColor(Color.BLACK);
				}
			}
		}

	}

	private void setPhoneTextValue(String p) {
		if (null == phone) {
			phone_text.setText("还没验证哦！");
			phone_text.setTextColor(getResources().getColor(R.color.green));

		} else {
			if ("".equals(p)) {
				phone_text.setText("还没验证哦！");
				phone_text.setTextColor(getResources().getColor(R.color.green));
			} else {
				if (isPhoneBind.equals("0")) {
					phone_text.setText(p + "（未激活）");
					phone_text.setTextColor(getResources().getColor(
							R.color.green));
					// phone_text_null.setVisibility(View.GONE);
				} else {
					phone_text.setText(p);
					phone_text.setTextColor(Color.BLACK);
					// phone_text_null.setVisibility(View.GONE);

				}
			}
		}

	}
}
