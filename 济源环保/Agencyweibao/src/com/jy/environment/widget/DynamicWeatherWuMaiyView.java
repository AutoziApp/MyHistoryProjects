package com.jy.environment.widget;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jy.environment.activity.EnvironmentCurrentWeatherPullActivity;

public class DynamicWeatherWuMaiyView extends View implements Runnable {

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

	public DynamicWeatherWuMaiyView(Context context, int resource, int left,
			int top, int sleepTime) {
		super(context);

		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		bitmap = BitmapFactory.decodeResource(getResources(), resource);
		this.left = left;
		this.top = top;
		this.sleepTime = sleepTime;

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
					DynamicWeatherWuMaiyView.this.invalidate();
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
//		if (!EnvironmentCurrentWeatherActivity.is_wumai) {
//			return;
//		}
		if (!EnvironmentCurrentWeatherPullActivity.is_wumai) {
			return;
		}
		canvas.drawBitmap(bitmap, left, top, null);
	}

	@Override
	public void run() {
//if (!EnvironmentCurrentWeatherActivity.is_wumai) {
	    if (!EnvironmentCurrentWeatherPullActivity.is_wumai) {
		return;
}
		while (DynamicWeatherWuMaiyView.IsRunning) {
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
