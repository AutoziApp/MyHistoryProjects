package com.mapuni.android.photograph;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.mapuni.android.base.Global;

public class Cameras {
	public final static int num=24;
	public static Cameras camea;
	public Uri cropUri;
	public File protraitFile;
	public String protraitPath;
	public Uri photoUri;
	public Context mcontext;
	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 请求相机 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

	public Cameras(Context context) {
		super();
		this.mcontext = context;
	}

	public static Cameras getInstance(Context context) {
		return camea == null ? camea = new Cameras(context) : camea;
	}

	// 拍照保存的绝对路径
	private Uri getCameraTempFile() {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(Global.FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		// 照片命名
		String cropFileName = "osc_camera_" + timeStamp + ".jpg";
		// 裁剪头像的绝对路径
		protraitPath = Global.FILE_SAVEPATH + cropFileName;
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
	public void startActionCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
		((Activity) mcontext).startActivityForResult(intent,
				REQUEST_CODE_GETIMAGE_BYCAMERA);
	}
	
	/**
	 * 相机拍照
	 * 
	 * @param output
	 */
	public void startActionCamera(Cameras cameras) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameras.getCameraTempFile());
		((Activity) mcontext).startActivityForResult(intent,
				REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	public String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	public void startActionCrop(Uri data) {
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
		((Activity) mcontext).startActivityForResult(intent,
				REQUEST_CODE_GETIMAGE_BYSDCARD);
	}
	
	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	public void startActionCrop(Uri data,Cameras cameras) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", cameras.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);// 裁剪框比例
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", CROP);// 输出图片大小
		// intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		((Activity) mcontext).startActivityForResult(intent,
				REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	// 裁剪头像的绝对路径
	public Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(Global.FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			// UIHelper.ToastMessage(UserInfo.this, "无法保存上传的头像，请检查SD卡是否挂载");
			Toast.makeText(mcontext, "无法保存上传的头像，请检查SD卡是否挂载", 2000).show();
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// 如果是标准Uri
		if (ImageUtils.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath((Activity) mcontext, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = ImageUtils.isEmpty(ext) ? "jpg" : ext;
		// 照片命名
		String cropFileName = "osc_crop_" + timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = Global.FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);
		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

}
