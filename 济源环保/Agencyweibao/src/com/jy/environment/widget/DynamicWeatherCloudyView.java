package com.jy.environment.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jy.environment.activity.EnvironmentCurrentWeatherPullActivity;
import com.jy.environment.util.ImageUtils;

public class DynamicWeatherCloudyView extends View implements Runnable {

	/**
	 * 要处理的图
	 */
	private Bitmap bitmap;

	private int left;
	private int top;

	/**
	 * 图片移动频率
	 */
	private int dx = 1;
	private int dy = 1;

	private int sleepTime;

	/**
	 * 图片是否在移动
	 */
	private static boolean IsRunning = true;

	private Handler handler;

	public DynamicWeatherCloudyView(Context context, int resource, int left,
			int top, int sleepTime) {
		super(context);

		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		try {
//		    bitmap = BitmapFactory.decodeResource(getResources(), resource);
		    bitmap = ImageUtils.readBitmap(context, resource);
		} catch (OutOfMemoryError e) {
		    e.printStackTrace();
		}
		this.left = left;
		this.top = top;
		this.sleepTime = sleepTime;

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
					DynamicWeatherCloudyView.this.invalidate();
			};
		};

	}

	public void move() {
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
//		if (!EnvironmentCurrentWeatherActivity.is_cloud) {
//			return;
//		}
		if (!EnvironmentCurrentWeatherPullActivity.is_cloud) {
			return;
		}
		canvas.drawBitmap(bitmap, left, top, null);
	}

	@Override
	public void run() {
//if (!EnvironmentCurrentWeatherActivity.is_cloud) {
	    if (!EnvironmentCurrentWeatherPullActivity.is_cloud) {
	return;
}
		while (DynamicWeatherCloudyView.IsRunning) {
				if ((bitmap != null) && (left > (getWidth()))) {
					left = -bitmap.getWidth();
				}
			left = left + dx;
			handler.sendMessage(handler.obtainMessage());
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
