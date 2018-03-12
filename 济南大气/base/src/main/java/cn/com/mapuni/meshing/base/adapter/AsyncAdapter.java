package cn.com.mapuni.meshing.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.IViewFlowAsync;
import cn.com.mapuni.meshing.base.viewflow.TitleProvider;
import cn.com.mapuni.meshing.base.viewflow.ViewFlow;


/**
 * FileName: AsyncAdapter.java Description:
 * 适应ViewFlowCommonActivity.class的Adapter
 * 
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-11-30 上午11:54:27
 */
public class AsyncAdapter extends BaseAdapter implements TitleProvider {

	/** 存放滑动布局页面上的名字 */
	private final ArrayList<String> names;
	/** 用来存放滑动的显示的 布局 */
	private final View[] views;
	/** 定义一个接口 */
	private final IViewFlowAsync async;
	/** 上下文 */
	Context context;
	/** 记录滑动页面加载成功失败的情况 */
	private final HashMap<Integer, Boolean> mmap = new HashMap<Integer, Boolean>();
	/** ViewFlow控件 */
	private ViewFlow viewflow;
	/** 定义一个滑动加锁的对象 */
	private final Object lock = new Object();
	private boolean mAllowLoad = true;
	private boolean firstLoad = true;
	/** 当前viewFlow的位置 */
	int currentPosition = -1;

	/**
	 * Description: 返回滑动的所有视图
	 * 
	 * @return 返回一个数组 View[]
	 * @author 王留庚 Create at: 2012-11-30 下午01:51:51
	 */
	public View[] getviews() {
		return views;
	}

	/**
	 * Description:
	 * 
	 * @author Administrator
	 * @Create at: 2013-6-8 下午3:54:41
	 */
	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	/**
	 * Description: 解锁
	 * 
	 * @author Administrator
	 * @Create at: 2013-6-8 下午4:39:26
	 */
	public void unlock() {
		mAllowLoad = true;
		firstLoad = true;
		currentPosition = viewflow.getSelectedItemPosition();
		synchronized (lock) {
			lock.notifyAll();
		}

	}

	/**
	 * 构造方法
	 * 
	 * @param titles
	 *            滑动页面标题列表
	 * @param context
	 *            上下文
	 * @param async
	 *            实现了IViewFlowAsync接口的对象
	 */
	public AsyncAdapter(ArrayList<String> titles, Context context,
			IViewFlowAsync async) {

		this.names = titles;
		views = new View[titles.size()];
		for (int len = 0; len < titles.size(); len++) {
			YutuLoading yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("加载中……", "加载失败……", Color.BLACK);
			views[len] = yutuLoading;
		}
		this.context = context;
		this.async = async;
	}

	public AsyncAdapter(ArrayList<String> titles, Context context,
			IViewFlowAsync async, ViewFlow viewflow) {

		this.names = titles;
		views = new View[titles.size()];
		for (int len = 0; len < titles.size(); len++) {
			YutuLoading yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("加载中……", "加载失败……", Color.BLACK);
			views[len] = yutuLoading;
		}
		this.viewflow = viewflow;
		viewflow.setOnViewStateListener(new MyViewStateListener());
		this.context = context;
		this.async = async;

	}

	private class MyViewStateListener implements ViewFlow.ViewStateListener {

		@Override
		public void onStateChange(int state) {
			if (state == ViewFlow.TOUCH_STATE_REST) {
				unlock();
			} else {
				lock();
			}
		}
	}

	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public Object getItem(int position) {
		return views[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return drawView(position, convertView);
	}

	/**
	 * Description: 加载所选择的页面内容
	 * 
	 * @param position
	 *            当前所选择的视图位置
	 * @param view
	 *            被加载的视图
	 * @return 返回需要的页面内容 View
	 * @author 王留庚 Create at: 2012-11-30 下午01:53:53
	 */
	private View drawView(int position, View view) {

		View childView = (View) getItem(position);
		if (!mmap.containsKey(position)) {
			mmap.put(position, true);
			new LoadContentTask().execute(position, view);
		}
		return childView;
	}

	/** 得到当前位置的名字 */
	@Override
	public String getTitle(int position) {
		return names.get(position);
	}

	/** 定义一个一部加载页面内容的异步类。执行异步操作 */
	private class LoadContentTask extends AsyncTask<Object, Object, Object> {

		/** 定义一个当前视图的位置 */
		private Integer position;

		@Override
		protected Object doInBackground(Object... arg) {

			position = (Integer) arg[0];
			if (mAllowLoad && firstLoad) {
				firstLoad = false;
				return async.callBackground(position);
			}
			do {
				int currentPosition = viewflow.getSelectedItemPosition();
				if (Math.abs(currentPosition - position) == 1 && firstLoad) {
					break;
				} else {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} while (mAllowLoad);
			return async.callBackground(position);

		}

		@Override
		protected void onPostExecute(Object result) {
			View v = async.callOnUI(position, result);
			views[position] = v;
			notifyDataSetChanged();
		}
	}
}
