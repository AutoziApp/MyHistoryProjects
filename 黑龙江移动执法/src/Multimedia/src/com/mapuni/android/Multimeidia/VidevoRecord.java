package com.mapuni.android.Multimeidia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.mapuni.android.base.util.ExceptionManager;

public class VidevoRecord {

	
	Intent video_intent;
	/**
	 * 调用拍照的方法必须传一个requestCode标示，拍照这个默认是200
	 * @param context 上下文对象
	 * @param path 路径
	 */
	public void takeVidevoRecord(Context context,String path){
	
	video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
	File file=new File(path+"/RaskVideo");
	if(!file.exists())//第一次录像创建录像文件夹
		file.mkdirs();
	video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
	((Activity)context).startActivityForResult(video_intent, 3);
}
	
	/**
	 * 调用拍照的方法必须传一个requestCode标示，拍照这个默认是200
	 * @param context
	 */
	public void takeVidevoRecord(Context context){
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
		String wenjianm=dateFormat.format(now);
		video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		File file=new File("/mnt/sdcard/"+wenjianm);
		if(!file.exists())//第一次录像创建录像文件夹
			file.mkdirs();
		video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		((Activity)context).startActivityForResult(video_intent, 3);
		
	}
	
	/**
	 * onActivityResult 回调的方法 为了得到值
	 * @param context 上下问对象
	 * @param data 数据
	 * @param path 路径
	 */
	
	public void takeVidevoRecordForResult(Context context,Intent data,String path){
		
		if(data==null)//防止不录制直接返回
			return;

		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//	    String fileName = "mapuni"+dateFormat.format(now);
	    String fileName = dateFormat.format(now);
		String videofile=path+"/RaskVideo/"+fileName+"."+"mp4";
		File video_gp=new File(videofile);
		try {
			Uri uri=data.getData();
			if(uri==null){//防止录像的时候点击删除按钮
				return;
			}
			AssetFileDescriptor videoasset=context.getContentResolver().openAssetFileDescriptor(uri,"r");
			InputStream in=videoasset.createInputStream();
			OutputStream out=new FileOutputStream(video_gp); 
			byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            context.getContentResolver().delete(data.getData(), null, null);
		} catch (FileNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "SiteEvidenceActivity");
			e.printStackTrace();
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "SiteEvidenceActivity");
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * onActivityResult 回调的方法 为了得到值
	 * @param context 上下问对象
	 * @param data 数据
	 * @param path 路径
	 */
	
	public void takeVidevoRecordForResult(Context context,Intent data){
		
		if(data==null)//防止不录制直接返回
			return;
		
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//		String fileName = "mapuni"+dateFormat.format(now);
		String fileName = dateFormat.format(now);
		String videofile="/mnt/sdcard/"+dateFormat.format(now)+"/RaskVideo/"+fileName+"."+"mp4";
		File video_gp=new File(videofile);
		try {
			Uri uri=data.getData();
			if(uri==null){//防止录像的时候点击删除按钮
				return;
			}
			AssetFileDescriptor videoasset=context.getContentResolver().openAssetFileDescriptor(uri,"r");
			InputStream in=videoasset.createInputStream();
			OutputStream out=new FileOutputStream(video_gp); 
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			context.getContentResolver().delete(data.getData(), null, null);
		} catch (FileNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "SiteEvidenceActivity");
			e.printStackTrace();
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "SiteEvidenceActivity");
			e.printStackTrace();
		}
		
		
	}
	
	
	

}
