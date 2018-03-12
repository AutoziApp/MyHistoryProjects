package cn.com.mapuni.meshing.base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.adapter.SlideOnLoadAdapter;

public class SlideView extends LinearLayout implements OnPageChangeListener, OnClickListener {
	private final List<SlideOnLoadAdapter> adapters;// 页数组
	private ViewPager pager;
	private LinearLayout indicateBar;
	private HorizontalScrollView scrollbar;
	private final DisplayMetrics dm;
	private int barItemW;
	private final int barItemH;
	private final int imageViewID = 10000;
	private final int textViewID = 10001;
	private int firstPosition = 0;
	private int currentPosition = 0;
	private Context mContext;

	/** 选中背景色 */
	private int selectBackground = R.drawable.wd_rwxx_xzbg;
	/** 未选中背景色 */
	private int loseBackground = R.drawable.wd_rwxx_wxzbg;

	public void setLoseBackColor(int loseBackColor) {
		this.loseBackground = loseBackColor;
	}

	public SlideView(Context context, int margin) {
		super(context);
		mContext = context;
		dm = getResources().getDisplayMetrics();// 最好用下边的获取方式

		barItemH = (int) (dm.density * 52);
		barItemW = (int) (dm.density * 125);
		int num = dm.widthPixels / barItemW;

		if (num > 5) {
			num = 5;
			barItemW = dm.widthPixels / num;
		} else if (num < 3) {
			num = 3;
			barItemW = dm.widthPixels / num;
		} else {
			int temp = dm.widthPixels % barItemW;
			barItemW += (temp / num);
		}

		adapters = new ArrayList<SlideOnLoadAdapter>();
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);

		MarginLayoutParams layoutParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT /*dip2px(mContext, 38)*/);

		margin = dip2px(mContext, margin);// 转换像素为DP
		layoutParams.setMargins(margin, margin, margin, margin);
		addView(CreateBar(), new LayoutParams(layoutParams));
		addView(CreatePager());
	}

	/**
	 * 根据需求手动设置title的宽度
	 * 
	 * @param barItemW
	 */
	public void setSlideViewWidth(int barItemW) {
		this.barItemW = barItemW;
	}

	public int getSlideViewWidth() {
		return barItemW;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	// FIXME 此处用于像素DP转换
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private View CreateBar() {
		if (indicateBar == null && scrollbar == null) {
			scrollbar = new HorizontalScrollView(getContext());
			scrollbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT/*dip2px(mContext, 38)*/));
			scrollbar.setHorizontalScrollBarEnabled(false); // 隐藏水平滚动条
			scrollbar.setHorizontalFadingEdgeEnabled(false);
			// scrollbar.setPadding(8, 8, 8, 8);

			if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			}
			indicateBar = new LinearLayout(getContext());
			indicateBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			indicateBar.setGravity(Gravity.CENTER);
			indicateBar.setOrientation(HORIZONTAL);
		}
		scrollbar.addView(indicateBar);

		return scrollbar;
	}

	public void setScrollBarVisibility(int visibility) {
		if (scrollbar != null) {
			scrollbar.setVisibility(visibility);
		}
	}

	private View CreatePager() {
		if (pager == null) {
			pager = new ViewPager(getContext());
			pager.setSaveEnabled(false);
			pager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			pager.setOnPageChangeListener(this);

			if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
				// pager.setOverScrollMode( View.OVER_SCROLL_NEVER );//
				// 禁止显示拖动至边界时的阴影
			}
		}

		return pager;
	}

	@SuppressLint("ResourceAsColor") public void SetBarFocused(int index) {
		currentPosition = index;
		int count = indicateBar.getChildCount() / 2 + 1;

		for (int i = 0; i < count; i++) {
			LinearLayout layout = ((LinearLayout) indicateBar.findViewById(i));
			if (i == index) {
				((ImageView) (layout.findViewById(imageViewID))).setVisibility(View.VISIBLE);
				((TextView) (layout.findViewById(textViewID))).setBackgroundResource(selectBackground);
				((TextView) (layout.findViewById(textViewID))).setTextColor(Color.parseColor("#000000")/*R.color.white*/);
			} else {
				((ImageView) (layout.findViewById(imageViewID))).setVisibility(View.GONE);
				((TextView) (layout.findViewById(textViewID))).setBackgroundResource(loseBackground);
				((TextView) (layout.findViewById(textViewID))).setTextColor(Color.parseColor("#000000"/*"#b2bded"*/));
			}

		}

		scrollbar.smoothScrollTo(index * barItemW - barItemW, 0);// 根据当前被选item距离左侧的距离计算设置滚动条的位置
	}

	public void AddPageView(SlideOnLoadAdapter adapter, String indiText) {
		adapters.add(adapter);

		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setId(adapters.size() - 1);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LayoutParams(barItemW, barItemH));
		linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		linearLayout.setOnClickListener(this);

		ImageView imageView = new ImageView(getContext());
		imageView.setId(imageViewID);
		imageView.setVisibility(View.GONE);

		TextView textView = new TextView(getContext());
		textView.setId(textViewID);
		textView.setLayoutParams(new LayoutParams(barItemW, barItemH));
		textView.setText(indiText);
		textView.setGravity(Gravity.CENTER);
		textView.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);// 字体
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(16);
		linearLayout.addView(textView);
		linearLayout.addView(imageView);

		indicateBar.addView(linearLayout);
		
		ImageView spiltiImageView = new ImageView(getContext());
		spiltiImageView.setVisibility(View.GONE);
		indicateBar.addView(spiltiImageView);

	}

	public void Reset() {
		adapters.clear();

		pager.removeAllViews();

		indicateBar.removeAllViews();
	}

	public void Display() {
		int count = indicateBar.getChildCount();
		if (count > 0) {
			indicateBar.removeViewAt(indicateBar.getChildCount() - 1);
		}
		pager.setOffscreenPageLimit(1);// 缓存页面个数
		pager.setAdapter(new ViewPageAdapter());
		pager.setCurrentItem(firstPosition);

		SetBarFocused(firstPosition);

	}

	public int getFirstPosition() {
		return firstPosition;
	}

	/**
	 * 设置默认第一个要现实view的位置,在Display方法前调用
	 * 
	 * @param firstPosition
	 */
	public void setFirstPosition(int firstPosition) {
		this.firstPosition = firstPosition;
	}

	@Override
	public void onClick(View v) {
		pager.setCurrentItem(v.getId());
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		SetBarFocused(position);
	}

	protected class ViewPageAdapter extends PagerAdapter {
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		// 销毁position位置的界面
		public void destroyItem(View container, int position, Object object) {
			View view = (View) object;
			((ViewPager) container).removeView(view);
			view = null;
		}

		@Override
		// 初始化第position个界面
		public Object instantiateItem(View container, int position) {
			View childView = adapters.get(position).view;// 获得view
			((ViewPager) container).addView(childView);// 把view添加至ViewPager

			return childView;
		}

		@Override
		// 跳转到每个页面都要执行该方法，每次滑动都执行
		public void setPrimaryItem(View container, int position, Object object) {
			SlideOnLoadAdapter adapter = adapters.get(position);

			if (adapter != null && !adapter.isDone) {
				Log.d("setPrimaryItem", "OnLoad() index = " + position);
				adapter.OnLoad();// position当前页面的索引
				adapter.isDone = true;
			}
		}

		@Override
		// 获取当前窗体界面数
		public int getCount() {
			return adapters.size();
		}

		@Override
		// 判断是否由对象生成界面
		public boolean isViewFromObject(View arg, Object object) {
			return arg == object;
		}
	}
}
