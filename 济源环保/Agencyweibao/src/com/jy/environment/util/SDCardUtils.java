package com.jy.environment.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class SDCardUtils {

    private static final File PHOTO_DIR = new File(
	    Environment.getExternalStorageDirectory() + "/DCIM/Camera");

    /**
     * 保存内容到内部存储器中
     * 
     * @param filename
     *            文件名
     * @param content
     *            内容
     */

    public static void save(String filename, String content, Context context) {

	try {
	    File file = new File(context.getFilesDir().getPath() + filename);
	    FileOutputStream fos = context.openFileOutput(filename,
		    Context.MODE_PRIVATE);
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
		    fos));
	    writer.write(content);
	    writer.close();
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}

    }

    /**
     * 通过文件名获取内容
     * 
     * @param filename
     *            文件名
     * @return 文件内容
     */
    public static String get(String filename, Context context) {
	String line = null;
	try {
	    File file = new File(context.getFilesDir().getPath() + filename);
	    // if(!file.isDirectory())
	    // {
	    // return null;
	    // }
	    FileInputStream fis = context.openFileInput(filename);

	    BufferedReader reader = new BufferedReader(new InputStreamReader(
		    fis));
	    line = reader.readLine();
	    reader.close();
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}

	return line;
    }

    public static boolean storeInSD(Bitmap bitmap) {
	File file = new File(Environment.getExternalStorageDirectory()
		+ "/DCIM");

	if (!file.exists()) {

	    file.mkdir();

	}
	File imageFile = new File(PHOTO_DIR, getPhotoFileName());
	try {
	    imageFile.createNewFile();

	    BufferedOutputStream fos = new BufferedOutputStream(
		    new FileOutputStream(imageFile));

	    bitmap.compress(CompressFormat.JPEG, 100, fos);

	    fos.flush();

	    fos.close();
	    return true;

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return false;

	} catch (IOException e) {
	    e.printStackTrace();
	    return false;

	}

    }

    public static String getPhotoFileName() {
	Date date = new Date(System.currentTimeMillis());
	SimpleDateFormat dateFormat = new SimpleDateFormat(
		"'IMG'_yyyy-MM-dd HH:mm:ss");
	return dateFormat.format(date) + ".jpg";
    }

}
