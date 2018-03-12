package com.mapuni.android.Multimeidia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.provider.MediaStore;

public class Camera   {
	
	
	
  
	Intent photo_intent = null;
	/**
	 * 拍照的方法
	 * @param context 上下文对象
	 */
	public void takePhoto(Context context) {// 拍照
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
		String wenjianm=dateFormat.format(now);
//		String fileName = "mapuni" + dateFormat.format(now);
		String fileName = dateFormat.format(now);

		photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File("/mnt/sdcard/"+wenjianm);
		if (!file.exists())// 第一次拍照创建照片文件夹
			file.mkdirs();
		photo_intent.putExtra(
				MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File("/mnt/sdcard/"+wenjianm, fileName + "." + "jpg")));
		photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
		((Activity)context).startActivityForResult(photo_intent, 1);
	}
	
	/**
	 * 在onActivityResult中调用的方法
	 * @param context 上下文对象
	 * @param path  拍照的路径
	 */
	public void takePhoto(Context context,String path) {// 拍照
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
		String wenjianm=dateFormat.format(now);
//		String fileName = "mapuni" + dateFormat.format(now);
		String fileName = dateFormat.format(now);
		
		photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(path+"/RaskImage");
		if (!file.exists())// 第一次拍照创建照片文件夹
			file.mkdirs();
		photo_intent.putExtra(
				MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(path+"/RaskImage", fileName + "." + "jpg")));
		photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
		((Activity)context).startActivityForResult(photo_intent, 1);
	}
	
	
	
	
}
	
