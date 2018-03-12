package com.mapuni.android.printershare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

public class Printershare {
  
	
	/**
	 * 判断Printershare是否安装
	 * @param context 上下文
	 * @param pageName Printershare 的包名
	 * @return
	 */
	public static boolean appIsInstalled(Context context, String pageName) {
		try {
			context.getPackageManager().getPackageInfo(pageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	
	
	/**
	 * 安装Printershare
	 * @param context
	 */
	public   static void  insatll(Context context){
		Intent intent = new Intent(Intent.ACTION_VIEW);

		File file = getAssetFileToCacheDir(context,"printer.apk");
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		context.startActivity(intent);
		
	}
	
	/**
	 * 从Asset目录下copy到CacheDir
	 * @param context
	 * @param fileName
	 * @return
	 */
	
	
	public static File getAssetFileToCacheDir(Context context, String fileName) {
		  try {
		    File cacheDir = getCacheDir(context);
		    final String cachePath = cacheDir.getAbsolutePath()+ File.separator + fileName;
		    InputStream is = context.getAssets().open(fileName);
		    File file = new File(cachePath);
		    file.createNewFile();
		    FileOutputStream fos = new FileOutputStream(file);
		    byte[] temp = new byte[1024];

		    int i = 0;
		    while ((i = is.read(temp)) > 0) {
		      fos.write(temp, 0, i);
		    }
		    fos.close();
		    is.close();
		    return file;
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    return null; 
		}
	/**
	 * 得到CacheDir目录
	 * @param context
	 * @return
	 */
	public static File getCacheDir(Context context) {
		  String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
		  File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
		  if (!dir.exists()) {
		    dir.mkdirs();
		  }
		  return dir; 
		}
	
	
	
	
	
	/**
	 * 打印html
	 * @param context 上下文
	 * @param path html的路径
	 */
	public static  void startprintshare(Context context,String path){

		
		
		if (!appIsInstalled(context, "com.dynamixsoftware.printershare")) {
			insatll(context);
			return;
		}
		
		
		
		
		Intent intent = new Intent();
		ComponentName comp = new ComponentName(
				"com.dynamixsoftware.printershare",
				"com.dynamixsoftware.printershare.ActivityWeb");

		intent = new Intent();
		intent.setComponent(comp);
		intent.setAction("android.intent.action.VIEW");
		intent.setType("text/html");

		File pdf = new File(path);

		intent.setData(Uri.fromFile(pdf));
		context.startActivity(intent);
		
	}
	
	
}