package com.goldnut.app.sdk.tool;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileTool {

	public static File mkdirFile(String filePath) {

		File file = new File(filePath);
		if (!file.exists())
			file.mkdirs();

		return file;
	}

	/*
	 * 判断SD卡是否存在
	 */
	public static boolean isSDExist() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			return true;
		}

		return false;
	}

	public static boolean isExist(String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File f = new File(path);
			if (f.exists()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取SD卡剩余空间，单位 MB
	 */
	public static long getSDAvailaleSpace() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize) / 1024 / 1024;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位

	}

	// 递归 // 取得文件夹大小
	public static long getFileSize(File f) {

		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static long getFileFolderSize(String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			File file = new File(path);

			if (file.exists()) {
				return getFileSize(new File(path));
			} else {
				return 0;
			}
		}
		return 0;
	}

	public static long getFileSize(String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return new File(path).length();
		}
		return 0;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}

	public static String GetFileName(String URL) {
		try {
			int start = URL.lastIndexOf("/");

			return (URL.substring(start + 1));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}
	public static String GetFileName(String URL, String type) {
		try {

			int end = URL.indexOf(type);
			if (end != -1) {
				String temp = URL.substring(0, end + 3);
				int start = temp.lastIndexOf("/");

				return (temp.substring(start + 1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 递归删除文件和文件夹，注意，别乱用，防止誤殺
	 */
	public static void deleteFile(File file) {
		if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
            	deleteFile(f);
            }
            file.delete();
        }
	}
	
	/**
	 * 清空文件夹内容，但不删除该文件夹
	 * @param file
	 */
	public static void clearFolder(File file){
		
		if(!file.isDirectory()){
			return;
		}
		 File[] childFile = file.listFiles();
		 for(File f:childFile){
			 deleteFile(f);
		 }
	}

	/**
	 * 得到Assets的文本文件的文本
	 */
	public static String getStringAsAssets(String fileName, Context context) {
		String res = "";
		try {
			// InputStream in = getResources().openRawResource(R.raw.ansi);
			// 读取assets文件夹中的txt文件,将它放入输入流中
			InputStream in = context.getAssets().open("subscribe.json");
			// 获得输入流的长度
			int length = in.available();
			// 创建字节输入
			byte[] buffer = new byte[length];
			// 放入字节输入中
			in.read(buffer);
			// 获得编码格式
			// String type = codetype(buffer);
			// 设置编码格式读取TXT
			res = EncodingUtils.getString(buffer, "UTF-8");
			// 关闭输入流
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		return res;
	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 根据byte数组，生成文件
	 */
	public static void getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "/" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	/** 
	* @author lxh
	* @Desc: 获取文件的SHA1值
	* @param path 文件路径
	* @return
	*/ 
	public static String getFileSha1(String path) throws OutOfMemoryError,
			IOException {
		
		return getFileSha1MD5(path, "SHA-1");
	}
	/** 
	 * @author lxh
	 * @Desc: 获取文件的MD5值
	 * @param path 文件路径
	 * @return
	 */ 
	public static String getFileMD5(String path) throws OutOfMemoryError,
	IOException {
		return getFileSha1MD5(path, "MD5");
	}
	
	
	public static String getFileSha1MD5(String path,String type) throws OutOfMemoryError,
	IOException {
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		MessageDigest messagedigest;
		try {
			messagedigest = MessageDigest.getInstance(type);
			
			byte[] buffer = new byte[1024 * 1024 * 10];
			int len = 0;
			
			while ((len = in.read(buffer)) > 0) {
				// 该对象通过使用 update（）方法处理数据
				messagedigest.update(buffer, 0, len);
			}
			
			// 对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest
			// 对象被重新设置成其初始状态。
			return byte2hex(messagedigest.digest());
		} catch (NoSuchAlgorithmException e) {
		} catch (OutOfMemoryError e) {
			throw e;
		} finally {
			in.close();
		}
		return null;
	}

	/**
	 * * @Description計算二進制數據.
	 * <P>
	 * * @date 2012-05-17
	 */
	private static String byte2hex(byte[] b) {// 二行制转字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
}
