package com.mapuni.android.thirdpart;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;



public class OpenFujian {

	
	
	
	/**
	 * Description: 打开不同格式的附件
	 * @param type 文件格式
	 * @param param  文件路径
	 * @param paramBoolean 
	 * @param context 上下文
	 * @param file 要打开的文件
	 * void
	 * @author 王红娟
	 * Create at: 2012-12-7 下午01:16:05
	 */
	public static void OpenFile(String type,String param,boolean paramBoolean,Context context,File file){
		FileType filetype = getFileType(type);
		Intent intent = new Intent("android.intent.action.VIEW");
		Uri uri = null;
	     switch (filetype) {
	        case HTML:
	        	uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
	            break;
	        case IMAGE:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
	            break;
	        case PDF:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
	            break;
	        case TXT:
	        	if (paramBoolean) {
					Uri uri1 = Uri.parse(param);
					intent.setDataAndType(uri1, type);
				}else{
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
				}
				break;
	        case AUDIO:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("oneshot", 0);
				intent.putExtra("configchange", 0);
				uri = Uri.fromFile(file);
	            break;
	        case VIDEO:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("oneshot", 0);
				intent.putExtra("configchange", 0);
				uri = Uri.fromFile(file);
	            break;
	        case CHM:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
	            break;
	        case WORD:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
	            break;
	        case EXCEL:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
	            break;
	        case PPT:
	        	intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				uri = Uri.fromFile(file);
	            break;
	        }
	     intent.setDataAndType(uri, type);
	     
		 try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "附件无法打开，请下载相关软件", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * FileName: FileHelper.java
	 * Description: 文件类型的枚举类
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-7 下午01:18:21
	 */
	public static enum FileType{
		
		HTML("text/html"),
		IMAGE("image/*"),
		PDF("application/pdf"),
		TXT("text/plain"),
		AUDIO("audio/*"),
		VIDEO("video/*"),
		CHM("application/x-chm"),
		WORD("application/msword"),
		EXCEL("application/vnd.ms-excel"),
		PPT("application/vnd.ms-powerpoint");
		
		private String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		private FileType(String type){
			this.type = type; 
		}
		
	}
	
	/**
	 * Description: 通过不同的文件类型返回不同的枚举对象
	 * @param type 系统的文件类型
	 * @return 文件类型枚举对象
	 * FileType 
	 * @author 王红娟
	 * Create at: 2012-12-7 下午01:18:46
	 */
	public static FileType getFileType(String type){
		if(type.equals("text/html")){
			return FileType.HTML;
		}
		if(type.equals("image/*")){
			return FileType.IMAGE;
		}
		if(type.equals("application/pdf")){
			return FileType.PDF;
		}
		if(type.equals("text/plain")){
			return FileType.TXT;
		}
		if(type.equals("audio/*")){
			return FileType.AUDIO;
		}
		if(type.equals("video/*")){
			return FileType.VIDEO;
		}
		if(type.equals("application/x-chm")){
			return FileType.CHM;
		}
		if(type.equals("application/msword")){
			return FileType.WORD;
		}
		if(type.equals("application/vnd.ms-excel")){
			return FileType.EXCEL;
		}
		if(type.equals("application/vnd.ms-powerpoint")){
			return FileType.PPT;
		}
		return null;
	}
	
	
	
	
	
	
}
