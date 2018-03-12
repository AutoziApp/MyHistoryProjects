package com.mapuni.android.enforcement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * 所有涉及取证操作的类的父类
 * 
 * 
 * 
 */
public class AttachmentBaseActivity extends BaseActivity {

	public final static int TAKE_PHOTOS = 1;
	public final static int RECORD_AUDIONS = 2;
	public final static int RECORD_VIDEOS = 3;
	public final static int SELECT_SDKARD_FILE = 4;
	/*********************** 权限 *********************/
	protected final String PZ_QX = "vmob2A3B1C";// 拍照权限
	public final String SX_QX = "vmob2A3B2C";// 摄像权限
	public final String LY_QX = "vmob2A3B3C";// 录音权限
	public static String imageGuid;
	public static String CURRENT_ID;
	public static String QYID;
	public static String FK_ID;

	public static String TASK_PATH = Global.SDCARD_RASK_DATA_PATH + "Attach/";
	public static String ROOT_TASK_PATH = Environment.getExternalStorageDirectory() + "/移动执法/";
	public static String ROOT_TASK_PATH_FILE = Environment.getExternalStorageDirectory() + "/移动执法/";

	/**
	 * 是否有新数据插入的标志
	 */
	public boolean updateFlag = false;
	/**
	 * 需要加载的标志
	 */
	protected boolean isNeedLoad = false;

	/**
	 * 后台运行标志
	 */
	protected boolean backgroundRunFlag = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
		String fileName = dateFormat.format(now);
		switch (requestCode) {
		case TAKE_PHOTOS:
			Log.i("info", "take photo");
			if (resultCode == -1) {
				String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) " + "values ('" + imageGuid + "','" + fileName + "','" + imageGuid
						+ ".jpg','.jpg','" + T_Attachment.RWZX + "','" + FK_ID + "')";
				SqliteUtil.getInstance().execute(sql);
			}
			String jpgfile = TASK_PATH + "RWZX/" + imageGuid + ".jpg";
			File jpg_gp = new File(jpgfile);
			if (jpg_gp.exists()) {
				try {
					updateFlag = true;
					FileHelper.copyFile(jpg_gp, new File(ROOT_TASK_PATH + fileName + ".jpg"));
					takePhoto();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			break;
		case RECORD_AUDIONS:
			Log.i("info", "record audio");
			try {
				String fileName1 = data.getStringExtra("filename");
				String guidamr = data.getStringExtra("guidamr");
				String amrfile = TASK_PATH + "RWZX/" + guidamr + ".amr";
				File amr_gp = new File(amrfile);
				FileHelper.copyFile(amr_gp, new File(ROOT_TASK_PATH + fileName1));

				updateFlag = true;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case RECORD_VIDEOS:
			Log.i("info", "record video");
			if (data == null)// 防止不录制直接返回
				return;

			// 插入数据
			String guid = UUID.randomUUID().toString();

			String sqlsql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) " + "values ('" + guid + "','" + fileName + "','" + guid + ".mp4','.mp4','"
					+ T_Attachment.RWZX + "','" + FK_ID + "')";
			SqliteUtil.getInstance().execute(sqlsql);

			String videofile = TASK_PATH + "RWZX/" + guid + "." + "mp4";
			File video_gp = new File(videofile);
			try {
				Uri uri = data.getData();
				if (uri == null) {// 防止录像的时候点击删除按钮
					return;
				}
				AssetFileDescriptor videoasset = getContentResolver().openAssetFileDescriptor(uri, "r");
				InputStream in = videoasset.createInputStream();
				OutputStream out = new FileOutputStream(video_gp);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				getContentResolver().delete(data.getData(), null, null);
				FileHelper.copyFile(video_gp, new File(ROOT_TASK_PATH + fileName + ".mp4"));

				updateFlag = true;
			} catch (FileNotFoundException e) {
				ExceptionManager.WriteCaughtEXP(e, "SiteEvidenceActivity");
				e.printStackTrace();
			} catch (IOException e) {
				ExceptionManager.WriteCaughtEXP(e, "SiteEvidenceActivity");
				e.printStackTrace();
			}
			break;
		case SELECT_SDKARD_FILE:
			selectSDcardFile(data, this, T_Attachment.RWZX, FK_ID);
			updateFlag = true;
		}
	}

	
	
	@TargetApi(19)
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	    	if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = MediaStore.Images.Media.DATA;  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
	
	
	
	/**
	 * @param data
	 * @param activity
	 * @param RWZX
	 *            任务附件所属模块
	 * @param RWBH
	 * @return
	 */
	public static boolean selectSDcardFile(Intent data, Activity activity, int RWZX, String RWBH) {
		boolean flag = false;
		if (data == null)// 防止不选择附件出错
			return flag;
		Uri uri = data.getData();
		ArrayList<CharSequence> paths = data.getCharSequenceArrayListExtra("data");
		String path = "";
		if (uri != null) {
//			String[] proj = { MediaStore.Images.Media.DATA };
//			Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
//			int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			actualimagecursor.moveToFirst();
//			path = actualimagecursor.getString(actual_image_column_index);
			path = getPath(activity, uri);
			insertFileInfo(path, RWZX, RWBH);
		} else if (paths != null) {
			for (CharSequence charSequence : paths) {
				insertFileInfo((String) charSequence, RWZX, RWBH);
			}
			flag = true;
		}
		return flag;
	}

	public static void insertFileInfo(String path, int RWZX, String RWBH) {
		try {
			File souceFile = new File(path);
			String guidAttach = UUID.randomUUID().toString();
			String fileNameAttach = souceFile.getName();
			String extension = "";
			if (path.contains(".")) {
				extension = path.substring(path.lastIndexOf("."));
				fileNameAttach = fileNameAttach.substring(0, fileNameAttach.lastIndexOf("."));
			}
//			File targetFile = new File(TASK_PATH + "RWZX/");
//			if (!targetFile.exists()) {
//				targetFile.mkdirs();
//			}
			File attachFile = new File(ROOT_TASK_PATH);
			if (!attachFile.exists()) {
				attachFile.mkdirs();
			}
			File targetFile = new File(TASK_PATH + T_Attachment.transitToChinese(RWZX) + "/");
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			targetFile = new File(targetFile, guidAttach + extension);
			FileHelper.copyFile(souceFile, targetFile);
			FileHelper.copyFile(targetFile, new File(ROOT_TASK_PATH + fileNameAttach + extension));
			String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) " + "values ('" + guidAttach + "','" + fileNameAttach + "','" + guidAttach
					+ extension + "','" + extension + "','" + RWZX + "','" + RWBH + "')";
			SqliteUtil.getInstance().execute(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description:选照
	 * 
	 * @author Administrator Create at: 2012-12-4 上午10:50:12
	 */
	public void takefigure() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要启动的应用"), SELECT_SDKARD_FILE);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Description:拍照
	 * 
	 * @author Administrator Create at: 2012-12-4 上午10:50:12
	 */
	public void takePhoto() {// 拍照
		imageGuid = UUID.randomUUID().toString();
		Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(TASK_PATH + "RWZX/");
		if (!file.exists())// 第一次拍照创建照片文件夹
			file.mkdirs();
		photo_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TASK_PATH + "RWZX/" + imageGuid + "." + "jpg")));
		photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
		startActivityForResult(photo_intent, TAKE_PHOTOS);

	}

	/**
	 * Description:摄像
	 * 
	 * @author Administrator Create at: 2012-12-4 上午10:50:21
	 */
	public void recordVideo() {// 录像

		Intent video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		File file = new File(TASK_PATH + "RWZX/");
		if (!file.exists())// 第一次录像创建录像文件夹
			file.mkdirs();
		video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		startActivityForResult(video_intent, RECORD_VIDEOS);

	}

	/**
	 * Description:录音
	 * 
	 * @author Administrator Create at: 2012-12-4 上午10:50:29
	 */
	public void recordAudio() {
		Intent audio_intent = new Intent(this, SiteRecorder.class);
		audio_intent.putExtra("currentTaskID", CURRENT_ID);
		audio_intent.putExtra("qyid", QYID);
		startActivityForResult(audio_intent, RECORD_AUDIONS);

	}

}
