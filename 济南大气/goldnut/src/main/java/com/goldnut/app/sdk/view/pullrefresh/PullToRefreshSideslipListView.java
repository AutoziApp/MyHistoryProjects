package com.goldnut.app.sdk.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.goldnut.app.sdk.view.SideslipListView;
import com.goldnut.app.sdk.view.pullrefresh.ILoadingLayout.State;

/**
 * 
 * @Desc  这个类实现了SideslipListView侧滑删除,下拉刷新,
 *        上加载更多和滑到底部自动加载
 * @author wkt 
 * @date 2015年3月13日  
 */ 
public class PullToRefreshSideslipListView extends PullToRefreshListView {

	/**
	 * 
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshSideslipListView(Context context) {
		this(context, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshSideslipListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public PullToRefreshSideslipListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}
	@Override
	protected SideslipListView createRefreshableView(Context context, AttributeSet attrs) {
		SideslipListView listView = new SideslipListView(context);
		mListView = listView;
		listView.setOnScrollListener(this);
		return listView;
	}
	
}
