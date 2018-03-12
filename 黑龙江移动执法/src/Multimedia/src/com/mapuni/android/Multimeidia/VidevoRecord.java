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
	 * �������յķ������봫һ��requestCode��ʾ���������Ĭ����200
	 * @param context �����Ķ���
	 * @param path ·��
	 */
	public void takeVidevoRecord(Context context,String path){
	
	video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
	File file=new File(path+"/RaskVideo");
	if(!file.exists())//��һ��¼�񴴽�¼���ļ���
		file.mkdirs();
	video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
	((Activity)context).startActivityForResult(video_intent, 3);
}
	
	/**
	 * �������յķ������봫һ��requestCode��ʾ���������Ĭ����200
	 * @param context
	 */
	public void takeVidevoRecord(Context context){
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
		String wenjianm=dateFormat.format(now);
		video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		File file=new File("/mnt/sdcard/"+wenjianm);
		if(!file.exists())//��һ��¼�񴴽�¼���ļ���
			file.mkdirs();
		video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		((Activity)context).startActivityForResult(video_intent, 3);
		
	}
	
	/**
	 * onActivityResult �ص��ķ��� Ϊ�˵õ�ֵ
	 * @param context �����ʶ���
	 * @param data ����
	 * @param path ·��
	 */
	
	public void takeVidevoRecordForResult(Context context,Intent data,String path){
		
		if(data==null)//��ֹ��¼��ֱ�ӷ���
			return;

		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//	    String fileName = "mapuni"+dateFormat.format(now);
	    String fileName = dateFormat.format(now);
		String videofile=path+"/RaskVideo/"+fileName+"."+"mp4";
		File video_gp=new File(videofile);
		try {
			Uri uri=data.getData();
			if(uri==null){//��ֹ¼���ʱ����ɾ����ť
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
	 * onActivityResult �ص��ķ��� Ϊ�˵õ�ֵ
	 * @param context �����ʶ���
	 * @param data ����
	 * @param path ·��
	 */
	
	public void takeVidevoRecordForResult(Context context,Intent data){
		
		if(data==null)//��ֹ��¼��ֱ�ӷ���
			return;
		
		Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
//		String fileName = "mapuni"+dateFormat.format(now);
		String fileName = dateFormat.format(now);
		String videofile="/mnt/sdcard/"+dateFormat.format(now)+"/RaskVideo/"+fileName+"."+"mp4";
		File video_gp=new File(videofile);
		try {
			Uri uri=data.getData();
			if(uri==null){//��ֹ¼���ʱ����ɾ����ť
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
