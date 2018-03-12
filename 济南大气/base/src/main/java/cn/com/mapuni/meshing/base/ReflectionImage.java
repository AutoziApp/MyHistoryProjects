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
 * @author ����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����8:44:23
 */
public class ReflectionImage extends ImageView {
	
	/** �Ƿ�ΪReflectionģʽ */
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
		/** ԭʼͼƬ�ͷ���ͼƬ�м�ļ�� */
		final int reflectionGap = 2; 
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		/** ��ת */
		Matrix matrix = new Matrix();
		/*
		 * float a = 100.0F,b = 100.0F; matrix = new Matrix();
		 * matrix.setTranslate(a,b); matrix.preRotate(30);
		 * matrix.preTranslate(-a,-b);
		 */

		matrix.preScale(1f, -1f);
		/** reflectionImage��������͸�����ǲ���,����Ӱ��ĳߴ� */
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 3, width, height / 2, matrix, false);
		/** ����һ���µ�bitmap */
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				height * 3 / 2, Config.ARGB_8888);
		Canvas canvasRef = new Canvas(bitmapWithReflection);
		/** �Ȼ�ԭʼ��ͼƬ */
		canvasRef.drawBitmap(originalImage, 0, 0, null);

		/** ����� */
		Paint deafaultPaint = new Paint();
		canvasRef.drawRect(0, height, width, height + reflectionGap,
				deafaultPaint);
		/** ������ת�Ժ��ͼƬ */
		canvasRef.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		/** ����һ��������ɰ�������汻��ת��ͼƬ���� */
		Paint paint = new Paint();
		/* ����Ӱ���͸���� */
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
		/** ����ImageView�е�setImageBitmap */
		this.setImageBitmap(bitmapWithReflection);
	}
}
