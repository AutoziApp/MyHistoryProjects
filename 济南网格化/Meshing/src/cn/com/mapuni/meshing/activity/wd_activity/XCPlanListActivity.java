package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.activity.wd_activity.RwxxXcjlActivity.MyOnScrollListener;
import cn.com.mapuni.meshing.adapter.XCPlanAdapter;
import cn.com.mapuni.meshing.model.XCPlanModel;
import cn.com.mapuni.meshing.model.XCPlanModel.RowsBean;

import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.JsonHelper;

public class XCPlanListActivity extends BaseActivity {
	private ListView listView;
	private List<XCPlanModel.RowsBean> list = new ArrayList<XCPlanModel.RowsBean>();
	private XCPlanAdapter adapter;
	/** �������ݵȴ���ĸ����� */
	protected LinearLayout mLoadLayout;
	/** listview�ײ��������ݵĽ��ȿ� */
	protected LinearLayout mProgressLoadLayout;
	
	/** ����һ�����ֲ��� */
	protected final android.widget.LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private boolean isMaxPage = false;
	private int pageNum = 1;
	private int pageSize = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		initView();
		getPlanData();
	}

	private void initView() {
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "Ѳ��ƻ�");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.xc_list_ac, null);
		middleLayout.addView(mainView);
		listView = (ListView) mainView.findViewById(R.id.lv);
		empty = (LinearLayout) mainView.findViewById(R.id.empty);
		addListLoadBar(listView);
		listView.setOnScrollListener(new MyOnScrollListener());
		adapter = new XCPlanAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				XCPlanModel.RowsBean rowsBean=list.get(position);
				Intent intent = new Intent(XCPlanListActivity.this,
						XCPlanDetailActivity.class);
				intent.putExtra("planBean", rowsBean);
				startActivity(intent);
			}
		});

	}

	private void addListLoadBar(ListView listview) {
		/** ��ȡ�ֻ���� */

		DisplayMetrics dm = new DisplayMetrics();
		XCPlanListActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		/** ProgressBar�Ĳ��� */
		mLoadLayout = new LinearLayout(XCPlanListActivity.this);
		mLoadLayout.setMinimumHeight(30);
		mLoadLayout.setMinimumWidth(dm.heightPixels);
		mLoadLayout.setGravity(Gravity.CENTER);
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);
		mLoadLayout.setBackgroundResource(R.drawable.loading);
		mLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout = new LinearLayout(XCPlanListActivity.this);
		mProgressLoadLayout.setMinimumHeight(30);
		mProgressLoadLayout.setGravity(Gravity.CENTER);

		ProgressBar mProgressBar = new ProgressBar(XCPlanListActivity.this);
		mProgressBar.setPadding(0, 0, 15, 0);
		mProgressBar
				.setScrollBarStyle(XCPlanListActivity.this.MODE_WORLD_READABLE);
		/** Ϊ������ӽ����� */
		mProgressLoadLayout.addView(mProgressBar, mLayoutParams);
		TextView mTipContent = new TextView(XCPlanListActivity.this);
		mTipContent.setText("���ڼ�������,���Ժ�...");
		mTipContent.getPaint().setFakeBoldText(true);
		mTipContent.setGravity(Gravity.CENTER_HORIZONTAL);
		/** Ϊ��������ı� */
		mProgressLoadLayout.addView(mTipContent, mLayoutParams);
		/** Ĭ����Ϊ���ɼ���ע��View.GONE��View.INVISIBLE������ */
		mProgressLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout.setGravity(Gravity.CENTER);
		/** ���mProgressLoadLayout����Ϊһ��View */
		mLoadLayout.addView(mProgressLoadLayout, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		listview.addFooterView(mLoadLayout, null, false);
	}

	// ��ҳ����
	class MyOnScrollListener implements OnScrollListener {
		private int visibleLastIndex = 0; // ���Ŀ���������
		private int visibleItemCount = 0; // ��ǰ���ڿɼ�������

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			int itemsLastIndex = adapter.getCount() - 1; // ���ݼ����һ�������
			int lastIndex = itemsLastIndex + 1;// ���ϵײ���loadMoreView��
			int a = OnScrollListener.SCROLL_STATE_IDLE;
			int b = OnScrollListener.SCROLL_STATE_FLING;
			int c = OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& visibleLastIndex == lastIndex && !isMaxPage) {
				// ������Զ�����,��������������첽�������ݵĴ���
				mProgressLoadLayout.setVisibility(View.VISIBLE);
				mLoadLayout.setVisibility(View.VISIBLE);
				getPlanData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			this.visibleItemCount = visibleItemCount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		}

	}

	/**
	 * ��������
	 */
	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	private LinearLayout empty;

	private void getPlanData() {
		final YutuLoading yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("�������ڼ��أ����Ժ�", "");
		yutuLoading.showDialog();
		StringBuilder builder = new StringBuilder("?sessionId=");
		builder.append(
				DisplayUitl.readPreferences(XCPlanListActivity.this,
						LAST_USER_SP_NAME, "sessionId"))
				.append("&workStatus=all").append("&page=")
				.append("" + pageNum).append("&rows=").append("" + pageSize);
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// ���ó�ʱʱ��
		utils.configTimeout(60 * 1000);// ���ӳ�ʱ //ָ��������һ��url�����ӵȴ�ʱ�䡣
		utils.configSoTimeout(60 * 1000);// ��ȡ���ݳ�ʱ
											// //ָ����������һ��url����ȡresponse�ķ��صȴ�ʱ��
		String url = PathManager.GETPATROPLAN_URL_JINAN+ builder.toString();
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				empty.setVisibility(View.VISIBLE);
				Toast.makeText(XCPlanListActivity.this, "���ݻ�ȡʧ��", 0).show();
				mProgressLoadLayout.setVisibility(View.GONE);
				mLoadLayout.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Log.i("utils", String.valueOf(arg0.result));
				String resoult = String.valueOf(arg0.result);
				Gson gson = new Gson();
				XCPlanModel xcPlanModel = gson.fromJson(resoult,
						XCPlanModel.class);
				if (xcPlanModel.getRows()==null) {
					empty.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					mProgressLoadLayout.setVisibility(View.GONE);
					mLoadLayout.setVisibility(View.GONE);
					return ;
				}
				if ("200".equals(xcPlanModel.getStatus())) {
					ArrayList<XCPlanModel.RowsBean> rowsBeans = (ArrayList<RowsBean>) xcPlanModel
							.getRows();
					if (rowsBeans != null && rowsBeans.size() > 0) {
						
						if (pageNum * pageSize >= xcPlanModel.getTotal()) {
							isMaxPage = true;
							Toast.makeText(XCPlanListActivity.this, "�����Ѿ�ȫ������",
									0).show();
						}
						list.addAll(rowsBeans);
						adapter.notifyDataSetChanged();
					}
					pageNum++;
				}
				mProgressLoadLayout.setVisibility(View.GONE);
				mLoadLayout.setVisibility(View.GONE);
			}

		});
	}

}
