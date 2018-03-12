package com.goldnut.app.sdk.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.GridView;

import com.goldnut.app.sdk.view.pullrefresh.ILoadingLayout.State;

/**
 * 这个类实现了GridView下拉刷新，上加载更多和滑到底部自动加载
 * 
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshGridView extends PullToRefreshBase<GridView>
implements OnScrollListener {

	/** ListView */
	private GridView mGridView;
	/** 用于滑到底部自动加载的Footer */
	private FooterLoadingLayout mLoadMoreFooterLayout;
	/** 滚动的监听器 */
	private OnScrollListener mScrollListener;

	public boolean loadMoreError = false;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshGridView(Context context) {
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
	public PullToRefreshGridView(Context context, AttributeSet attrs) {
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
	public PullToRefreshGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		setPullLoadEnabled(false);
	}

	@Override
	protected GridView createRefreshableView(Context context, AttributeSet attrs) {
		GridView gridView = new GridView(context);
		mGridView = gridView;
		gridView.setOnScrollListener(this);

		return gridView;
	}

	/**
	 * 上一次加载是否更多失败 isError 返回true时失败
	 * */
	public void setLoadMoreError(boolean isError) {
		loadMoreError = isError;
		if(mLoadMoreFooterLayout!=null){
			if(isError){
				mLoadMoreFooterLayout.setVisibility(View.VISIBLE);
				mLoadMoreFooterLayout.onClickToReload();
				mLoadMoreFooterLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//状态重置
						loadMoreError = false;
						//开始加载
						startLoading();
					}
				});
			}else{
				mLoadMoreFooterLayout.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置是否有更多数据的标志
	 * 
	 * @param hasMoreData
	 *            true表示还有更多的数据，false表示没有更多数据了
	 */
	public void setHasMoreData(boolean hasMoreData) {
		if (null != mLoadMoreFooterLayout) {
			if(hasMoreData){
				mLoadMoreFooterLayout.setVisibility(View.GONE);
			}else{
				mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
			}
		}
	}

	/**
	 * 设置滑动的监听器
	 * 
	 * @param l
	 *            监听器
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	/**
	 * 判断是否满足底部自动刷新的其中四个条件， 因为外部重写listview的滑动监听时，onscroll方法被覆盖
	 * 
	 * @return true表示还有更多数据
	 */
	public boolean isNeedToLoadOnBottom() {
		return (isAutoLoadOnBottom() && !isRefreshing() && hasMoreData() && isLastItemVisible());
	}

	@Override
	protected boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	@Override
	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	@Override
	public void onPullUpRefreshComplete() {
		super.onPullUpRefreshComplete();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.RESET);
		}
	}

	@Override
	public void setAutoLoadOnBottomEnabled(boolean isAutoLoadOnBottom) {
		super.setAutoLoadOnBottomEnabled(isAutoLoadOnBottom);

		if (isAutoLoadOnBottom) {
			// 设置Footer
			setPullLoadEnabled(true);
			if (null == mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
			}

			if (null == mLoadMoreFooterLayout.getParent()) {
				//				mGridView.addFooterView(mLoadMoreFooterLayout, null, false);
			}
			mLoadMoreFooterLayout.show(false);
		} else {
			if (null != mLoadMoreFooterLayout) {
				//				mGridView.removeFooterView(mLoadMoreFooterLayout);
			}
		}

		//		if (isAutoLoadOnBottom) {
		//			// 设置Footer
		//			if (null == mLoadMoreFooterLayout) {
		//				mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
		//			}
		//
		////			 mGridView.removeFooterView(mFooterLayout);
		//			// mGridView.addFooterView(mFooterLayout, null, false);
		//		} else {
		//			if (null != mLoadMoreFooterLayout) {
		//				// mGridView.removeFooterView(mFooterLayout);
		//			}
		//		}
	}

	@Override
	public void startLoading() {
		super.startLoading();
		if (mPullLoadEnabled && null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.LOADING);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isAutoLoadOnBottom() && hasMoreData()) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (isReadyForPullUp()) {
					startLoading();
				}
			}
		}

		if (null != mScrollListener) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (null != mScrollListener) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	/**
	 * 表示是否还有更多数据
	 * 
	 * @return true表示还有更多数据
	 */
	private boolean hasMoreData() {
		if ((null != mLoadMoreFooterLayout)
				&& (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
			return false;
		}

		return true;
	}

	/**
	 * 判断第一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mGridView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		int mostTop = (mGridView.getChildCount() > 0) ? mGridView.getChildAt(0)
				.getTop() : 0;
				if (mostTop >= 0) {
					return true;
				}

				return false;
	}

	/**
	 * 判断最后一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = mGridView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return false;
		}
		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = mGridView.getLastVisiblePosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition == lastItemPosition) {
			final int childIndex = lastVisiblePosition
					- mGridView.getFirstVisiblePosition();
			final int childCount = mGridView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = mGridView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() >= mGridView.getBottom();
			}
		}
		return false;
	}

}
