package com.jy.environment.util;

import android.content.Context;
import android.view.WindowManager;

public class ScreenUtils {

	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static int getScreenW(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public static int getScreenH(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	// {
	// WindowManager wm = (WindowManager)
	// getSystemService(Context.WINDOW_SERVICE);
	// int width = wm.getDefaultDisplay().getWidth();
	// int height = wm.getDefaultDisplay().getHeight();
	//
	//
	// 2、WindowManager wm1 = this.getWindowManager();
	// int width1 = wm1.getDefaultDisplay().getWidth();
	// int height1 = wm1.getDefaultDisplay().getHeight();
	//
	//
	// 3、DisplayMetrics dm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(dm);
	// int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
	// int mScreenHeight = dm.heightPixels;
	// }
}
