package cn.com.mapuni.meshing.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * FileName: ReflectionImage.java
 * Description:
 * @author 留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午8:44:23
 */
public class ReflectionImage extends ImageView {
	
	/** 是否为Reflection模式 */
	private boolean mReflectionMode = true;
	private static final int DEFAULT_PX = 100;

	public ReflectionImage(Context context) {
		super(context);
	}

	public ReflectionImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ReflectionImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setReflectionMode(boolean isRef) {
		mReflectionMode = isRef;
	}

	public boolean getReflectionMode() {
		return mReflectionMode;
	}

	@Override
	public void setImageResource(int resId) {
		int width = getResources().getDrawable(resId).getIntrinsicWidth();
		int height = getResources().getDrawable(resId).getIntrinsicHeight();
		int scale = 1;
		while (width > DEFAULT_PX || height > DEFAULT_PX) {
			width /= 2;
			height /= 2;
			scale++;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = scale;
		Bitmap originalImage = BitmapFactory.decodeResource(getResources(),
				resId, opts);

		DoReflection(originalImage);
	}

	private void DoReflection(Bitmap originalImage) {
		/** 原始图片和反射图片中间的间距 */
		final int reflectionGap = 2; 
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		/** 反转 */
		Matrix matrix = new Matrix();
		/*
		 * float a = 100.0F,b = 100.0F; matrix = new Matrix();
		 * matrix.setTranslate(a,b); matrix.preRotate(30);
		 * matrix.preTranslate(-a,-b);
		 */

		matrix.preScale(1f, -1f);
		/** reflectionImage就是下面透明的那部分,设置影像的尺寸 */
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 3, width, height / 2, matrix, false);
		/** 创建一个新的bitmap */
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				height * 3 / 2, Config.ARGB_8888);
		Canvas canvasRef = new Canvas(bitmapWithReflection);
		/** 先画原始的图片 */
		canvasRef.drawBitmap(originalImage, 0, 0, null);

		/** 画间距 */
		Paint deafaultPaint = new Paint();
		canvasRef.drawRect(0, height, width, height + reflectionGap,
				deafaultPaint);
		/** 画被反转以后的图片 */
		canvasRef.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		/** 创建一个渐变的蒙版放在下面被反转的图片上面 */
		Paint paint = new Paint();
		/* 设置影像的透明度 */
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x80ffffff, 0x00ffffff, TileMode.CLAMP);
		if (!originalImage.isRecycled())
			originalImage.recycle();
		/** Set the paint to use this shader (linear gradient) */
		paint.setShader(shader);
		/** Set the Transfer mode to be porter duff and destination in */
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		/** Draw a rectangle using the paint with our linear gradient */
		canvasRef.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		/** 调用ImageView中的setImageBitmap */
		this.setImageBitmap(bitmapWithReflection);
	}
}
