package com.jy.environment.widget;


import android.app.Activity;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

public class MyPopWindows extends PopupWindow {
	MyPopWindows popWindows;

	public MyPopWindows(final Activity context, View view, int h, int w,
			final int time) {
		// 设置SelectPicPopupWindow的View\
		popWindows = this;
		this.setContentView(view);
		Display mDisplay = context.getWindowManager().getDefaultDisplay();
		int W = mDisplay.getWidth();
		int H = mDisplay.getHeight();
		// 设置SelectPicPopupWindow弹出窗体的宽
		if (h == 700) {
//			this.setWidth(DensityUtil.dip2px(context, 100));
			this.setWidth(W/2);
			this.setHeight(LayoutParams.WRAP_CONTENT);
		} else if (h == 900) {
//			this.setWidth(DensityUtil.dip2px(context, 200));
			this.setWidth(W*2/3);
			this.setHeight(LayoutParams.WRAP_CONTENT);
		} else {
//			this.setWidth(LayoutParams.WRAP_CONTENT);
			this.setWidth(W/4);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
		}
		// else {

		// }
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				context.runOnUiThread(new Runnable() {
					public void run() {
						if (popWindows != null && popWindows.isShowing())
							popWindows.dismiss();
					}
				});
			}
		}, time * 1000);
	}
	
	public MyPopWindows(final Activity context, View view, int h, int w
			) {
		// 设置SelectPicPopupWindow的View\
		popWindows = this;
		this.setContentView(view);
		Display mDisplay = context.getWindowManager().getDefaultDisplay();
		int W = mDisplay.getWidth();
		int H = mDisplay.getHeight();
		if (h == 700) {
			this.setWidth(W/2);
			this.setHeight(LayoutParams.WRAP_CONTENT);
		} else if (h == 900) {
//			this.setWidth(W*2/3);
//			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setWidth(W*3/4);
//			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setHeight(H*2/7);
		} else {
			this.setWidth(W/4);
			this.setHeight(LayoutParams.WRAP_CONTENT);
		}
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
	}

}
