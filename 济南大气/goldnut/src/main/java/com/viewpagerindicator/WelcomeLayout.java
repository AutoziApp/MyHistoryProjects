package com.viewpagerindicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.goldnut.app.sdk.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class WelcomeLayout extends RelativeLayout {

	ViewPager mPager;
	PageIndicator mIndicator;
	WelcomeFragmentAdapter mAdapter;
	private ImageLoader loader;
	private int count;
	private Timer timer;
	
	private OnWelcomeFragmentClickListener listener;

	public WelcomeLayout(Context context,FragmentManager fm,
			ImageLoader imageLoader,List<WelcomePageItem> items,boolean autoPlay) {
		super(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.welcomelayout, null);
		addView(view);

		mAdapter = new WelcomeFragmentAdapter(fm,items,imageLoader,handler2);

		mPager = (ViewPager) findViewById(R.id.welcome_pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.welcome_indicator);
		mIndicator.setViewPager(mPager);
		
		count = items.size();
		if(autoPlay){
			autoPlay();
		}
	}
	
	public void setListener(OnWelcomeFragmentClickListener listener){
		this.listener = listener;
	}
	
	public void destroy(){
		if(timer !=null){
			timer.cancel();
		}
	}
	
	public int getCurrentItem(){
		return mPager.getCurrentItem();
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int index = mPager.getCurrentItem();
			mPager.setCurrentItem((index +1 )%count,true);
		};
	};
	
	
	private void autoPlay(){
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
			}
		}, 1000*5, 1000*5);
	}
	
	Handler handler2 = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(listener !=null){
				listener.onClick(msg.what);
			}
		};
	};

}
