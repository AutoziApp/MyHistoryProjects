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
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** ����ü� */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

	public Cameras(Context context) {
		super();
		this.mcontext = context;
	}

	public static Cameras getInstance(Context context) {
		return camea == null ? camea = new Cameras(context) : camea;
	}

	// ���ձ���ľ���·��
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
		// ��Ƭ����
		String cropFileName = "osc_camera_" + timeStamp + ".jpg";
		// �ü�ͷ��ľ���·��
		protraitPath = Global.FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);
		cropUri = Uri.fromFile(protraitFile);
		this.photoUri = this.cropUri;
		return this.cropUri;
	}
	
	/**
	 * �������
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
	 * �������
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
	 * ���պ�ü�
	 * 
	 * @param data
	 *            ԭʼͼƬ
	 * @param output
	 *            �ü���ͼƬ
	 */
	public void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);// �ü������
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", CROP);// ���ͼƬ��С
		// intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// ȥ�ڱ�
		intent.putExtra("scaleUpIfNeeded", true);// ȥ�ڱ�
		((Activity) mcontext).startActivityForResult(intent,
				REQUEST_CODE_GETIMAGE_BYSDCARD);
	}
	
	/**
	 * ���պ�ü�
	 * 
	 * @param data
	 *            ԭʼͼƬ
	 * @param output
	 *            �ü���ͼƬ
	 */
	public void startActionCrop(Uri data,Cameras cameras) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", cameras.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);// �ü������
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", CROP);// ���ͼƬ��С
		// intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// ȥ�ڱ�
		intent.putExtra("scaleUpIfNeeded", true);// ȥ�ڱ�
		((Activity) mcontext).startActivityForResult(intent,
				REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	// �ü�ͷ��ľ���·��
	public Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(Global.FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			// UIHelper.ToastMessage(UserInfo.this, "�޷������ϴ���ͷ������SD���Ƿ����");
			Toast.makeText(mcontext, "�޷������ϴ���ͷ������SD���Ƿ����", 2000).show();
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// ����Ǳ�׼Uri
		if (ImageUtils.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath((Activity) mcontext, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = ImageUtils.isEmpty(ext) ? "jpg" : ext;
		// ��Ƭ����
		String cropFileName = "osc_crop_" + timeStamp + "." + ext;
		// �ü�ͷ��ľ���·��
		protraitPath = Global.FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);
		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

}
