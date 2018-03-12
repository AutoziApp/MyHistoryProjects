package com.goldnut.app.sdk.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goldnut.app.sdk.R;
import com.goldnut.app.sdk.view.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.goldnut.app.sdk.view.pullrefresh.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class BaseListFragment extends Fragment implements
		OnRefreshListener {

	protected String TAG = BaseListFragment.class.getName();

	public PullToRefreshListView mPullRefreshListView;
	protected ListView actualListView;
	public TextView list_empty;
	private LinearLayout ll_empty;

	public BaseListFragment() {
		super();
	}

	private boolean isLoading = false;

	public static int Load_New_Data = 1;

	public static int Load_More_Data = 2;

	public static int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewRoot = inflater.inflate(R.layout.base_list_fragment, null);
		
		list_empty = (TextView) viewRoot.findViewById(R.id.empty);
		
		list_empty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reLoad();
			}
		});
		ll_empty = (LinearLayout) viewRoot.findViewById(R.id.ll_empty);

		mPullRefreshListView = (PullToRefreshListView) viewRoot
				.findViewById(R.id.listView);
		mPullRefreshListView.setAutoLoadOnBottomEnabled(true);
		actualListView = mPullRefreshListView.getRefreshableView();
		mPullRefreshListView.setOnRefreshListener(this);
		actualListView.setFadingEdgeLength(0);
		actualListView.setCacheColorHint(0);
		actualListView.setDividerHeight(1);
		//actualListView.setDivider(null);
		
		initView();
		
		return viewRoot;
	}
	
	/**
	 * 设置是否在拖动到底部时自动加载更多
	 * 默认[是]
	 * @param enable
	 */
	public void setAutoLoadOnBottomEnabled(boolean enable) {
		mPullRefreshListView.setAutoLoadOnBottomEnabled(enable);
	}
	
	public abstract void initView();

	/**
	 * 没有数据时的提示信息
	 */
	public void showEmptyMessage(String text) {
		if (!TextUtils.isEmpty(text)
				&& !list_empty.getText().toString().equals(text))
			list_empty.setText(text);
		if (ll_empty != null) {
			ll_empty.setVisibility(View.VISIBLE);
		}
		if (list_empty.getVisibility() != View.VISIBLE)
			list_empty.setVisibility(View.VISIBLE);
		if (actualListView.getEmptyView() == null)
			actualListView.setEmptyView(list_empty);
	}

	public void hideEmptyMessage() {
		if (ll_empty != null) {
			ll_empty.setVisibility(View.GONE);
		}
		list_empty.setVisibility(View.GONE);
	}

	// 加载更多
	protected abstract void loadMoreData();

	// 上拉刷新
	protected abstract void loadNewData();
	
	/**
	 * 重新加载
	 */
	protected abstract void reLoad();

	/**
	 * @author wkt
	 * @Desc: 重置加载的状态 1. 加载失败的状态 重置 2. 没有更多的状态重置
	 */
	public void resetLoadState() {
		loadMoreError(false);
		hasMoreData(true);
	}

	/**
	 * @author wkt
	 * @Desc: 上一次加载更多是否失败, 如果失败则不自动加载
	 * @param isError
	 *            true 表示失败，false 表示正常
	 */
	public void loadMoreError(boolean isError) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.setLoadMoreError(isError);
		}
	}

	/**
	 * @author wkt
	 * @Desc: 设置是否可加载更多
	 * @param hasMore
	 *            true 表示有更多，false 表示没有更多
	 */
	public void hasMoreData(boolean hasMore) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.setHasMoreData(hasMore);
		}
	}

	protected void initAdapterOrNotifyDataSet(boolean hasmore) {
		onPullDownUpRefreshComplete(hasmore);
	}

	public void onPullDownUpRefreshComplete(boolean hasmore) {
		// 刷新结束的时候
		onPullDownUpRefreshComplete();
		mPullRefreshListView.setHasMoreData(hasmore);
	}

	protected void onPullDownUpRefreshComplete() {
		// 刷新结束的时候
		if (mPullRefreshListView == null)
			return;
		mPullRefreshListView.onPullDownRefreshComplete();
		mPullRefreshListView.onPullUpRefreshComplete();
		mPullRefreshListView.setLastUpdatedLabel(getTime());
	}

	// 上拉获取更多
	protected void onPullUpRefreshComplete() {
		// 刷新结束的时候
		if (mPullRefreshListView != null)
			mPullRefreshListView.onPullUpRefreshComplete();
	}

	// 上拉取消
	protected void closePullUpRefresh() {
		// 刷新结束的时候
		if (mPullRefreshListView != null) {
			mPullRefreshListView.setPullLoadEnabled(false);
			mPullRefreshListView.setAutoLoadOnBottomEnabled(false);

		}
	}

	// 下拉取消
	protected void closePullDownRefresh() {
		// 刷新结束的时候
		if (mPullRefreshListView != null)
			mPullRefreshListView.setPullRefreshEnabled(false);
	}

	protected String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	@Override
	public void onPullDownToRefresh() {
		resetLoadState();
		loadNewData();
	}

	@Override
	public void onPullUpToRefresh() {
		loadMoreData();
	}

}
