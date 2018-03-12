/**  
 * GalleryAdapter.java
 * @version 1.0
 */
package com.digitalchina.gallery;

//import com.digitalchina.gallery.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.mapuni.android.base.Global;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

public class GalleryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> arraylist;
	private String attch;
//	private int images[] = { R.drawable.image0, R.drawable.image1, R.drawable.image2,
//			R.drawable.image7,R.drawable.image4,R.drawable.image5,R.drawable.image6,
//			R.drawable.image3};
	
//	private String images[] = {"5adab56e-8fc1-48e5-bf87-99de7b091810.jpg",
//			"dc426a63-07d2-404a-bab9-ab5cfef0ec8b.jpg",
//			"48039540-1e4d-412a-8f5d-27b0d739c96b.jpg"};

	public GalleryAdapter(Context context,ArrayList<String> arraylist,String attch) {
		this.context = context;
		this.arraylist = arraylist;
		this.attch = attch;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
//		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), images[position]);
	//	Bitmap bmp = loadmap(attch+"/"+arraylist.get(position));
		Bitmap bmp = loadmap(arraylist.get(position));
		ZoomImageView view = new ZoomImageView(context, bmp.getWidth(), bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

	public Bitmap loadmap(String fileName){
//		fileName   -->  headers/1.jpg
		Bitmap bit = null;
		try {
//			bit = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/mapuni/MobileEnforcement/Attach/"+fileName);
			//ByteArrayOutputStream os = (ByteArrayOutputStream) decodeBitmap(Global.SDCARD_RASK_DATA_PATH +"/Attach/HBSC/UploadFile/Image/"+fileName);
			
			//ByteArrayOutputStream os = (ByteArrayOutputStream) decodeBitmap(Environment.getExternalStorageDirectory() + "/mapuni/MobileEnforcement/Attach/"+fileName);
			ByteArrayOutputStream os = (ByteArrayOutputStream) decodeBitmap(fileName);
			byte[] toByteArray  = os.toByteArray();
			bit = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return bit;
	}
	
	public static OutputStream decodeBitmap(String path) {

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);

		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight, 1024 * 600);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);
			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bmp2.recycle();
			return baos;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null){
					is.close();
				}
				if(baos!=null){
					baos.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return baos;
	}
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);
	 
	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	 
	    return roundedSize;
	}
	
	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;
	 
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));
	 
	    if (upperBound < lowerBound) {
	        return lowerBound;
	    }
	 
	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}  
	private static double getScaling(int src, int des) {
	/**
	 * 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比
	 */
	    double scale = Math.sqrt((double) des / (double) src);
	    return scale;
	}
	
	
}
