package com.mapuni.android.uitl;

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

public class PanduanDayin {

	public static boolean appIsInstalled(Context context, String pageName) {
		try {
			context.getPackageManager().getPackageInfo(pageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static void insatll(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);

		File file = getAssetFileToCacheDir(context, "printer.apk");
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	public static File getAssetFileToCacheDir(Context context, String fileName) {
		try {
			File cacheDir = getCacheDir(context);
			final String cachePath = cacheDir.getAbsolutePath() + File.separator + fileName;
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

	public static File getCacheDir(Context context) {
		String APP_DIR_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
		File dir = new File(APP_DIR_NAME + context.getPackageName() + "/cache/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public static void startprintshare(Context context, String path) {
		Intent intent3 = new Intent();
		ComponentName comp3 = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
		intent3 = new Intent();
		intent3.setComponent(comp3);
		intent3.setAction("android.intent.action.VIEW");
		intent3.setType("text/html");
		File pdf3 = new File(path);
		intent3.setData(Uri.fromFile(pdf3));
		context.startActivity(intent3);
	}

}
