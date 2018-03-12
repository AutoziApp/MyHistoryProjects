package cn.com.mapuni.meshing.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.IViewFlowAsync;
import cn.com.mapuni.meshing.base.viewflow.TitleProvider;


/**
 * FileName: AsyncAdapter.java Description:
 * ��ӦViewFlowCommonActivity.class��Adapter
 * 
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-11-30 ����11:54:27
 */
public class TaskAsyncAdapter extends BaseAdapter implements TitleProvider {

	/** ����һ������ */
	@SuppressWarnings("unused")
	private final LayoutInflater inflater;
	/** ��Ż�������ҳ���ϵ����� */
	private final ArrayList<String> names;
	/** ������Ż�������ʾ�� ���� */
	private final View[] views;
	/** ����һ���ӿ� */
	private final IViewFlowAsync async;
	/** ������ */
	private final Context context;
	/** ��¼����ҳ����سɹ�ʧ�ܵ���� */
	private final HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();

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
	 * ���췽��
	 * 
	 * @param titles
	 *            ����ҳ������б�
	 * @param context
	 *            ������
	 * @param async
	 *            ʵ����IViewFlowAsync�ӿڵĶ���
	 */
	public TaskAsyncAdapter(ArrayList<String> titles, Context context,
			IViewFlowAsync async) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.names = titles;
		views = new View[titles.size()];
		this.context = context;
		this.async = async;
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
		if (childView == null && !map.containsKey(position)) {
			map.put(position, true);
			YutuLoading yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("�����С���", "����ʧ�ܡ���", Color.BLACK);
			view = yutuLoading;
			new LoadContentTask().execute(position, view);
		} else if (childView == null) {
			YutuLoading yutuLoading = new YutuLoading(context);
			yutuLoading.setLoadMsg("�����С���", "����ʧ�ܡ���", Color.BLACK);
			view = yutuLoading;
		} else {
			view = childView;
		}
		return view;
	}

	/** �õ���ǰλ�õ����� */
	@Override
	public String getTitle(int position) {
		return names.get(position);
	}

	/** ����һ���첽����ҳ�����ݵ��첽�ࡣִ���첽���� */
	private class LoadContentTask extends AsyncTask<Object, Object, Object> {
		/** ����һ����ǰ��ͼ��λ�� */
		private Integer position;
		/** ����һ����ͼ */
		private View view;

		@Override
		protected Object doInBackground(Object... arg) {
			position = (Integer) arg[0];
			view = (View) arg[1];
			return async.callBackground(position);
		}

		@Override
		protected void onPostExecute(Object result) {
			view = async.callOnUI(position, result);
			views[position] = view;
			notifyDataSetChanged();
		}
	}
}
