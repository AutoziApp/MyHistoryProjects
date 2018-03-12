package com.goldnut.app.sdk.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private boolean slideable = false; // 默认false时，不可左右滑动

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param slideable
	 *            为true时，viewpager可左右滑动
	 */
	public void setSlideable(boolean slideable) {
		this.slideable = slideable;
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		try {
			if (!slideable) {
				return false;
			}
			return super.onInterceptTouchEvent(arg0);
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (!slideable) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}

}
