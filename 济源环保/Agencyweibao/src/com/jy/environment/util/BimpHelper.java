package com.jy.environment.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BimpHelper {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();	
	
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static List<String> drr = new ArrayList<String>();
	

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		int orientation;
		Bitmap bMapRotate;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				
				
//				if(bitmap.getHeight() < bitmap.getWidth()){
//		            orientation = 90;
//		        } else {
//		            orientation = 0;
//		        }
				orientation = 0;
				
				
			   if (orientation != 0) {
		            Matrix matrix = new Matrix();
		            matrix.postRotate(orientation);
		            bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		            		bitmap.getHeight(), matrix, true);
		        } else{
		            bMapRotate = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
		            		bitmap.getHeight(), true);
			   }

				break;
			}
			i += 1;
		}
		return bMapRotate;
	}
	public static void clear(){
	    try {
		 drr.clear();
		 for (int i = 0; i < bmp.size(); i++) {
		     bmp.get(i).recycle();
		}
		 bmp.clear();
		 max = 0;
		 act_bool = true;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	   
	}
}
