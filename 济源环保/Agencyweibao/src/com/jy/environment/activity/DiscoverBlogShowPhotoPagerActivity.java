package com.jy.environment.activity;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.util.MyLog;
import com.jy.environment.widget.HackyViewPager;
import com.jy.environment.widget.ImageDetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;
/**
 * 环境说列表页查看图片
 * @author baiyuchuan
 *
 */
public class DiscoverBlogShowPhotoPagerActivity extends ActivityBase {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_blog_showphoto_pager_activity);
		try {
			pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
			String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);


			mPager = (HackyViewPager) findViewById(R.id.pager);
			ImagePagerAdapter mAdapter = new ImagePagerAdapter(
					getSupportFragmentManager(), urls);
			mPager.setAdapter(mAdapter);
			indicator = (TextView) findViewById(R.id.indicator);

			CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
					.getAdapter().getCount());
			indicator.setText(text);
			// 更新下标
			mPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageSelected(int arg0) {
					CharSequence text = getString(R.string.viewpager_indicator,
							arg0 + 1, mPager.getAdapter().getCount());
					indicator.setText(text);
				}

			});
			if (savedInstanceState != null) {
				pagerPosition = savedInstanceState.getInt(STATE_POSITION);
			}

			mPager.setCurrentItem(pagerPosition);
		} catch (Exception e) {
			// TODO: handle exception
			MyLog.e("weibao Exception" + e);
		}
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}


		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
			return ImageDetailFragment.newInstance(url);
		}
	}
}