package com.jy.environment.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.util.MyLog;
import com.umeng.analytics.MobclickAgent;

/**
 * 欢迎页面
 * 
 * @author baiyuchuan
 * 
 */
public class WelcomePagerActivity extends ActivityBase {

	private ViewPager mViewPager;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;

	private int currIndex = 0;

	protected void onPause() {
		MyLog.i("onPause");
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("WelcomePagerActivity");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("WelcomePagerActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_pager_activity);
		MyLog.i("onCreate");
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);

		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.welcome_pager_item1, null);
		// view1.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
		// WelcomePagerActivity.this, R.drawable.xd1)));
		// View view2 = mLi.inflate(R.layout.welcome_pager_item2, null);
		// view2.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
		// WelcomePagerActivity.this, R.drawable.xd2)));
		// View view3 = mLi.inflate(R.layout.welcome_pager_item3, null);
		// view3.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
		// WelcomePagerActivity.this, R.drawable.xd3)));
		View view4 = mLi.inflate(R.layout.welcome_pager_item4, null);
		// view4.setBackgroundDrawable(new BitmapDrawable(ImageUtils.readBitmap(
		// WelcomePagerActivity.this, R.drawable.xd4)));
		ImageView layout = (ImageView) (view4).findViewById(R.id.dianjijingru);
		try {
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					MobclickAgent.onEvent(WelcomePagerActivity.this,
							"jinruyingyong");
					if (getIntent().getExtras().getString("bangzhu")
							.equals("bangzhu")) {
						finish();
					} else {
						Intent intent = new Intent();
						intent.setClass(WelcomePagerActivity.this,
								EnvironmentMainActivity.class);
						intent.putExtra("from", "lo");
						intent.putExtra("load", "loading");
						startActivityForResult(intent, 100);
						overridePendingTransition(
								R.anim.addcity_activity_enter,
								R.anim.addcity_activity_enter);
						finish();
					}
					// TODO Auto-generated method stub
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		// views.add(view2);
		// views.add(view3);
		views.add(view4);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mViewPager.setAdapter(mPagerAdapter);

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 3:
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page01));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page01));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page01));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page01));
				break;
			}
			currIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 100 && arg1 == 20) {
			// boolean xuanze=data.getBooleanExtra("xuanze", false);
			final String city = arg2.getExtras().getString("xuanze");
			Intent intent;
			if (!"".equals(city)) {
				WeiBaoApplication.addJPushAliasAndTags(getApplicationContext(),
						false, city);
			}
			intent = new Intent(WelcomePagerActivity.this,
					EnvironmentMainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		}

	}
}
