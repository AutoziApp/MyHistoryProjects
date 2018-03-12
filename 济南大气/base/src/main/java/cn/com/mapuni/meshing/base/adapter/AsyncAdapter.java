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
 * ��ӦViewFlowCommonActivity.class��Adapter
 * 
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-11-30 ����11:54:27
 */
public class AsyncAdapter extends BaseAdapter implements TitleProvider {

	/** ��Ż�������ҳ���ϵ����� */
	private final ArrayList<String> names;
	/** ������Ż�������ʾ�� ���� */
	private final View[] views;
	/** ����һ���ӿ� */
	private final IViewFlowAsync async;
	/** ������ */
	Context context;
	/** ��¼����ҳ����سɹ�ʧ�ܵ���� */
	private final HashMap<Integer, Boolean> mmap = new HashMap<Integer, Boolean>();
	/** ViewFlow�ؼ� */
	private ViewFlow viewflow;
	/** ����һ�����������Ķ��� */
	private final Object lock = new Object();
	private boolean mAllowLoad = true;
	private boolean firstLoad = true;
	/** ��ǰviewFlow��λ�� */
	int currentPosition = -1;

	/**
	 * Description: ���ػ�����������ͼ
	 * 
	 * @return ����һ������ View[]
	 * @author ������ Create at: 2012-11-30 ����01:51:51
	 */
	public View[] getviews() {
		return views;
	}

	/**
	 * Description:
	 * 
	 * @author Administrator
	 * @Create at: 2013-6-8 ����3:54:41
	 */
	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	/**
	 * Description: ����
	 * 
	 * @author Administrator
	 * @Create at: 2013-6-8 ����4:39:26
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
	 * ���췽��
	 * 
	 * @param titles
	 *            ����ҳ������б�
	 * @param context
	 *            ������
	 * @param async
	 *            ʵ����IViewFlowAsync�ӿڵĶ���
	 */
	public AsyncAdapter(ArrayList<String> titles, Context context,
			IViewFlowAsync async) {

		this.names = titles;
		views = new View[titles.size()];
		for (int len = 0; len < titles.size(); len++) {
			YutuLoading yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("�����С���", "����ʧ�ܡ���", Color.BLACK);
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
			yutuLoading.setLoadMsg("�����С���", "����ʧ�ܡ���", Color.BLACK);
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
	 * Description: ������ѡ���ҳ������
	 * 
	 * @param position
	 *            ��ǰ��ѡ�����ͼλ��
	 * @param view
	 *            �����ص���ͼ
	 * @return ������Ҫ��ҳ������ View
	 * @author ������ Create at: 2012-11-30 ����01:53:53
	 */
	private View drawView(int position, View view) {

		View childView = (View) getItem(position);
		if (!mmap.containsKey(position)) {
			mmap.put(position, true);
			new LoadContentTask().execute(position, view);
		}
		return childView;
	}

	/** �õ���ǰλ�õ����� */
	@Override
	public String getTitle(int position) {
		return names.get(position);
	}

	/** ����һ��һ������ҳ�����ݵ��첽�ࡣִ���첽���� */
	private class LoadContentTask extends AsyncTask<Object, Object, Object> {

		/** ����һ����ǰ��ͼ��λ�� */
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
