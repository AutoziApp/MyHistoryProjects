package cn.com.mapuni.meshing.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	public static File createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				dir.mkdirs();
			}
		}
		return dir;
	}

	// public static boolean isFileExist(String fileName) {
	// File file = new File(PathManager.APPSDPATH + fileName);
	// file.isFile();
	// return file.exists();
	// }
	//
	// public static void delFile(String fileName) {
	// File file = new File(PathManager.APPSDPATH + fileName);
	// if (file.isFile()) {
	// file.delete();
	// }
	// file.exists();
	// }

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir(path);
		}
		dir.delete();
	}

	public static void deleteDirContent(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDirContent(file.getPath());
		}
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	public static void saveBitmapFile(Bitmap bitmap, String path) {
		File file = new File(path);// 将要保存图片的路径
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getPath(Context context, Uri uri) {

		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {

			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * 压缩图片
	 * @param path 原路径
	 * @param newPath 新路径
	 * @param rate 压缩率百分比
     * @return
     */
	public static boolean compressBitmap(String path,String newPath,int rate){
		try {
			FileOutputStream fos = new FileOutputStream(newPath);
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			bitmap.compress(Bitmap.CompressFormat.JPEG,rate,fos);
			fos.flush();
			fos.close();
			return true;
		}
		catch (Exception e){
			return  false;
		}

	}

	/**
	 * 压缩图片到指定大小
	 * @param path 原路径
	 * @param newPath 新路径
	 * @param minSize 最小Kb
     * @return
     */
	public static boolean compressBitmapToMinsize(String path,String newPath,int minSize){
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(path);

			if(bitmap.getByteCount()/1024 <= minSize){
				return false;
			}

			FileOutputStream fos = new FileOutputStream(newPath);

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			int rate = 80;
			bitmap.compress(Bitmap.CompressFormat.JPEG,rate,os);
			while (os.size()/1024 > minSize){
				os.reset();
				rate = rate /2;
				if(rate == 0){
					break;
				}
				bitmap.compress(Bitmap.CompressFormat.JPEG,rate,os);
			}
			os.writeTo(fos);
			fos.flush();
			fos.close();
			os.flush();
			os.close();
			return true;
		}
		catch (Exception e){
			return  false;
		}

	}

}
