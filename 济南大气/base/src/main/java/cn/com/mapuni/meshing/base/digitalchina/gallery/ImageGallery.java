package cn.com.mapuni.meshing.base.digitalchina.gallery;

import cn.com.mapuni.meshing.base.util.LogUtil;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Gallery;

public class ImageGallery extends Gallery {
	private static final String TAG = "ImageGallery";
	
	private GestureDetector gestureScanner;
	private ZoomImageView imageView;
	
	public ImageGallery(Context context) {
		super(context);
	}

	public ImageGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ImageGallery(Context context, AttributeSet attrs) {
		super(context, attrs);

		gestureScanner = new GestureDetector(new MySimpleGesture());
		this.setOnTouchListener(new OnTouchListener() {

			float baseValue;//����ľ���
			float originalScale;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View view = ImageGallery.this.getSelectedView();
				if (view instanceof ZoomImageView) {
					imageView = (ZoomImageView) view;

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						baseValue = 0;
						originalScale = imageView.getScale();
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						LogUtil.i("manager", "event.getPointerCount()=="+event.getPointerCount());
						if (event.getPointerCount() == 2) {
							float x = event.getX(0) - event.getX(1);
							float y = event.getY(0) - event.getY(1);
							float value = (float) Math.sqrt(x * x + y * y);// ��������ľ���
							// System.out.println("value:" + value);
							if (baseValue == 0) {
								baseValue = value;
							} else {
								float scale = value / baseValue;// ��ǰ�����ľ��������ָ����ʱ�����ľ��������Ҫ���ŵı�����
								// scale the image
								imageView.zoomTo(originalScale * scale, x + event.getX(1), y + event.getY(1));

							}
						}
					}
				}
				return false;
			}

		});
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		View view = ImageGallery.this.getSelectedView();
		if (view instanceof ZoomImageView) {
			imageView = (ZoomImageView) view;
			
			float v[] = new float[9];
			Matrix m = imageView.getImageMatrix();
			m.getValues(v);
			// ͼƬʵʱ��������������
			float left, right;
			// ͼƬ��ʵʱ����
			float width, height;
			width = imageView.getScale() * imageView.getImageWidth();
			height = imageView.getScale() * imageView.getImageHeight();
			LogUtil.i("manager", "onScroll--width:"+width+",height:"+height);
			LogUtil.i(TAG, "distanceX-distanceY-"+distanceX+"*"+distanceY);
			// һ���߼�Ϊ�ƶ�ͼƬ�ͻ���gallery�������߼������û����������˽�ķǳ��������Ķ����µĴ���ǰ����˼������������
			if ((int) width <= ImageGalleryActivity.screenWidth && (int) height <= ImageGalleryActivity.screenHeight)// ���ͼƬ��ǰ��С<��Ļ��С��ֱ�Ӵ������¼�
			{
				super.onScroll(e1, e2, distanceX, distanceY);
			} else {
				left = v[Matrix.MTRANS_X];
				right = left + width;
				Rect r = new Rect();
				imageView.getGlobalVisibleRect(r);
				LogUtil.i(TAG, "distanceX--"+distanceX);
				if (distanceX > 0) {
					if (r.left > 0) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (right < ImageGalleryActivity.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						//imageView.postTranslate(-distanceX, -distanceY);
						//���ͼƬ��ʾ�ĸ߶�С����Ļ�߶ȣ���ֻ�������һ���
						if((int) width > ImageGalleryActivity.screenWidth && (int) height <= ImageGalleryActivity.screenHeight){
							imageView.postTranslate(-distanceX, 0);
						}else if((int) width > ImageGalleryActivity.screenWidth && (int) height > ImageGalleryActivity.screenHeight){
							imageView.postTranslate(-distanceX, -distanceY);
						}
					}
				} else if (distanceX < 0) {
					if (r.right < ImageGalleryActivity.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (left > 0) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						//imageView.postTranslate(-distanceX, -distanceY);
						//���ͼƬ��ʾ�ĸ߶�С����Ļ�߶ȣ���ֻ�������һ���
						if((int) width > ImageGalleryActivity.screenWidth && (int) height <= ImageGalleryActivity.screenHeight){
							imageView.postTranslate(-distanceX, 0);
						}else if((int) width > ImageGalleryActivity.screenWidth && (int) height > ImageGalleryActivity.screenHeight){
							imageView.postTranslate(-distanceX, -distanceY);
						}
					}
				}
			}

		} else {
			super.onScroll(e1, e2, distanceX, distanceY);
		}
		return false;
	}
	float cos15 = 0.96f;
	private boolean shoulOnScrollLeft(float dx, float dy){
		float cos = dx/(float)Math.sqrt(dx*dx+dy*dy);
		LogUtil.i(TAG, "cosleft--"+cos);
		if(cos>15){
			return true;
		} 
		
		return false;
	}
	float cos75 = 0.25f;
	private boolean shoulOnScrollRight(float dx, float dy){
		float cos = dx/(float)Math.sqrt(dx*dx+dy*dy);
		LogUtil.i(TAG, "cosright--"+cos);
		if(cos<cos75){
			return true;
		} 
		
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureScanner.onTouchEvent(event);
		View view = ImageGallery.this.getSelectedView();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (view instanceof ZoomImageView) {
				/*imageView = (ZoomImageView) view;
				//ͼƬ��ǰʵ�����ش�С
				float width = imageView.getScale() * imageView.getImageWidth();
				float height = imageView.getScale() * imageView.getImageHeight();
				// ���ͼƬ��ǰ��С<��Ļ��С���жϱ߽�
				if ((int) height <= ImageGalleryActivity.screenHeight){
					break;
				}*/
				 
			} 
			break;
		case MotionEvent.ACTION_UP:
			// �ж����±߽��Ƿ�Խ��
			if (view instanceof ZoomImageView) {
				imageView = (ZoomImageView) view;
				//ͼƬ��ǰʵ�����ش�С
				float width = imageView.getScale() * imageView.getImageWidth();
				float height = imageView.getScale() * imageView.getImageHeight();
				// ���ͼƬ��ǰ��С<��Ļ��С���жϱ߽�
				if ((int) height <= ImageGalleryActivity.screenHeight){
					break;
				}
				float v[] = new float[9];
				Matrix m = imageView.getImageMatrix();
				m.getValues(v);
				float top = v[Matrix.MTRANS_Y];
				float bottom = top + height;
				if (top > 0&& height >= ImageGalleryActivity.screenHeight) {
					imageView.postTranslateDur(-top, 200f);
				} 
				LogUtil.i("manga", "bottom:" + bottom);
				if (bottom < ImageGalleryActivity.screenHeight && height >= ImageGalleryActivity.screenHeight) {
					imageView.postTranslateDur(ImageGalleryActivity.screenHeight - bottom, 200f);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		// �����µĵڶ���Touch downʱ����
		public boolean onDoubleTap(MotionEvent e) {
			View view = ImageGallery.this.getSelectedView();
			if (view instanceof ZoomImageView) {
				imageView = (ZoomImageView) view;
				if (imageView.getScale() > imageView.getScaleRate()) {
					imageView.zoomTo(imageView.getScaleRate(), ImageGalleryActivity.screenWidth / 2, ImageGalleryActivity.screenHeight / 2, 200f);
					// imageView.layoutToCenter();
				} else {
					float scale = imageView.getScale();
					scale = scale+2;
					if(scale>=imageView.getMaxScale()){
						scale = imageView.getMaxScale();
					}
					imageView.zoomTo(scale, ImageGalleryActivity.screenWidth / 2, ImageGalleryActivity.screenHeight / 2, 200f);
					LogUtil.i(TAG, "xxxxxxxxxxxxx");
				}

			} else {

			}
			return true;
		}
	}
}
