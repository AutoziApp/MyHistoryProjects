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
	private final List<SlideOnLoadAdapter> adapters;// ҳ����
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

	/** ѡ�б���ɫ */
	private int selectBackground = R.drawable.wd_rwxx_xzbg;
	/** δѡ�б���ɫ */
	private int loseBackground = R.drawable.wd_rwxx_wxzbg;

	public void setLoseBackColor(int loseBackColor) {
		this.loseBackground = loseBackColor;
	}

	public SlideView(Context context, int margin) {
		super(context);
		mContext = context;
		dm = getResources().getDisplayMetrics();// ������±ߵĻ�ȡ��ʽ

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

		margin = dip2px(mContext, margin);// ת������ΪDP
		layoutParams.setMargins(margin, margin, margin, margin);
		addView(CreateBar(), new LayoutParams(layoutParams));
		addView(CreatePager());
	}

	/**
	 * ���������ֶ�����title�Ŀ��
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

	// FIXME �˴���������DPת��
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private View CreateBar() {
		if (indicateBar == null && scrollbar == null) {
			scrollbar = new HorizontalScrollView(getContext());
			scrollbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT/*dip2px(mContext, 38)*/));
			scrollbar.setHorizontalScrollBarEnabled(false); // ����ˮƽ������
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
				// ��ֹ��ʾ�϶����߽�ʱ����Ӱ
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

		scrollbar.smoothScrollTo(index * barItemW - barItemW, 0);// ���ݵ�ǰ��ѡitem�������ľ���������ù�������λ��
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
		textView.setTextAppearance(getContext(), android.R.style.TextAppearance_Small);// ����
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
		pager.setOffscreenPageLimit(1);// ����ҳ�����
		pager.setAdapter(new ViewPageAdapter());
		pager.setCurrentItem(firstPosition);

		SetBarFocused(firstPosition);

	}

	public int getFirstPosition() {
		return firstPosition;
	}

	/**
	 * ����Ĭ�ϵ�һ��Ҫ��ʵview��λ��,��Display����ǰ����
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
		// ����positionλ�õĽ���
		public void destroyItem(View container, int position, Object object) {
			View view = (View) object;
			((ViewPager) container).removeView(view);
			view = null;
		}

		@Override
		// ��ʼ����position������
		public Object instantiateItem(View container, int position) {
			View childView = adapters.get(position).view;// ���view
			((ViewPager) container).addView(childView);// ��view�����ViewPager

			return childView;
		}

		@Override
		// ��ת��ÿ��ҳ�涼Ҫִ�и÷�����ÿ�λ�����ִ��
		public void setPrimaryItem(View container, int position, Object object) {
			SlideOnLoadAdapter adapter = adapters.get(position);

			if (adapter != null && !adapter.isDone) {
				Log.d("setPrimaryItem", "OnLoad() index = " + position);
				adapter.OnLoad();// position��ǰҳ�������
				adapter.isDone = true;
			}
		}

		@Override
		// ��ȡ��ǰ���������
		public int getCount() {
			return adapters.size();
		}

		@Override
		// �ж��Ƿ��ɶ������ɽ���
		public boolean isViewFromObject(View arg, Object object) {
			return arg == object;
		}
	}
}
