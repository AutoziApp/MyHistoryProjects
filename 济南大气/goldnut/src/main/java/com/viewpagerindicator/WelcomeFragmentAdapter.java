package com.viewpagerindicator;

import java.util.List;

import com.goldnut.app.sdk.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class WelcomeFragmentAdapter extends FragmentPagerAdapter {
	
	
	private List<WelcomePageItem> items;
	
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.default_img)
	// url爲空會显示该图片，自己放在drawable里面的
	.showImageOnFail(R.drawable.default_img)
	// 加载图片出现问题，会显示该图片
	.showImageOnLoading(R.drawable.default_img)
	.resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
	.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
	.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
	.displayer(new SimpleBitmapDisplayer()).build();

	
	private ImageLoader loader;
	private Handler mHandler;

	public WelcomeFragmentAdapter(FragmentManager fm,List<WelcomePageItem> items
			,ImageLoader loader,Handler handler) {
		super(fm);
		this.items = items;
		this.loader = loader;
		mHandler = handler;
	}

	@Override
	public Fragment getItem(int position) {
		WelcomeFragment fragment = WelcomeFragment.newInstance(items.get(position % items.size()),
				this.loader,options,mHandler);
		fragment.setPosition(position);
		return fragment;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
	

//	public void setCount(int count) {
//		if (count > 0 && count <= 10) {
//			mCount = count;
//			notifyDataSetChanged();
//		}
//	}
}