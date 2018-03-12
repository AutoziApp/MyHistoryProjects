package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.meshing.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.mapuni.meshing.util.FileDownUtil;

public class DocumentSearchActivity extends BaseActivity {
	LinearLayout middleLayout;
	View mainView;
	// ��ҳ���ص�����
	public ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	public ArrayList<HashMap<String, Object>> SearchList = new ArrayList<HashMap<String, Object>>();
	ArrayList<HashMap<String, Object>> Data;
	public int currage = 1;
	private boolean isMaxPage = false;
	private int PAGENUM = 12;
	private YutuLoading yutuLoading;
	// private String fileName = "";// �ļ�����
	private List<HttpHandler> httpHandlers = new ArrayList<HttpHandler>();
	MyTaskAdapter adapter;
	private ListView listView;
	private EditText edit_doc;
	private Button btn_search;
	Handler handler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			yutuLoading.dismissDialog();
			switch (msg.what) {
			case 0:
				list.clear();
				adapter.notifyDataSetChanged();
				Toast.makeText(DocumentSearchActivity.this, "��������", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				list.clear();
				list.addAll(SearchList);
				adapter.notifyDataSetChanged();
				
				break;
			case 3:
				Toast.makeText(DocumentSearchActivity.this, "�������쳣", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			return false;
		}
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SetBaseStyle("�ĵ�����");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.activity_document_search, null);
		middleLayout.addView(mainView);
		initData(1, "");
		edit_doc = (EditText) mainView.findViewById(R.id.edit_doc);
		// fileName = edit_doc.getText().toString();
		btn_search = (Button) mainView.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initData(1, edit_doc.getText().toString());
			}
		});
		listView = (ListView) mainView.findViewById(R.id.listView);
		adapter = new MyTaskAdapter(DocumentSearchActivity.this, list);
		addListLoadBar(listView);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new MyOnScrollListener());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				 FileDownUtil.getInstance(DocumentSearchActivity.this).downLoadWord(list.get(position).get("filepath").toString(), list.get(position).get("filename").toString());
			}

			

		});

	}

	// ��÷�ҳ����
	public void initData(int page, String fileName) {
		yutuLoading = new YutuLoading(DocumentSearchActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڼ������ݣ����Ժ�", "");
		yutuLoading.showDialog();
		isMaxPage = false;
		loadPagedata(page, fileName);
	}

	private void loadPagedata(final int page, final String fileName) {
		currage = page;
		int page_size = PAGENUM;
		Data = new ArrayList<HashMap<String, Object>>();
		getDocumentList(page, page_size, fileName);
	}

	/**
	 * ��������
	 */
	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";

	private void getDocumentList(final int page, final int pageSize, String fileName) {
		StringBuilder builder = new StringBuilder("?");
		builder.append("&page=").append(page).append("&rows=").append(pageSize).append("&name=").append(fileName);

		Log.i("DEMO", builder.toString());

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		// ���ó�ʱʱ��
		utils.configTimeout(60 * 1000);// ���ӳ�ʱ //ָ��������һ��url�����ӵȴ�ʱ�䡣
		utils.configSoTimeout(60 * 1000);// ��ȡ���ݳ�ʱ
											// //ָ����������һ��url����ȡresponse�ķ��صȴ�ʱ��

		 String url = PathManager.DOCSEARCH_URL_JINAN + builder.toString();
//		String url = "http://192.168.120.59:8184/JiNanhuanbaoms/grid/seleteByFileName" + builder.toString();
		HttpHandler httpHandler = utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.i("utils", String.valueOf(arg1));
				handler.sendEmptyMessage(3);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Log.i("utils", String.valueOf(arg0.result));
				String call = String.valueOf(arg0.result);
				// ��ȡData
				if (call != null && !call.equals("")) {
					JSONObject jobject;
					try {
						jobject = new JSONObject(call);
						// ��������1
						JSONArray jsonArray = jobject.getJSONArray("rows");
						Data = new ArrayList<HashMap<String, Object>>();
						if (jsonArray != null && jsonArray.length() > 0) {
							HashMap<String, Object> map;
							for (int i = 0; i < jsonArray.length(); i++) {
								map = new HashMap<String, Object>();
								map.put("UploadTime", jsonArray.getJSONObject(i).getString("UploadTime"));
								map.put("FileName", jsonArray.getJSONObject(i).getString("FileName"));
								map.put("ID", jsonArray.getJSONObject(i).getString("ID"));
								map.put("UploadPerson", jsonArray.getJSONObject(i).getString("UploadPerson"));
								map.put("FilePath",
										PathManager.DOC_URL_JINAN + jsonArray.getJSONObject(i).getString("FilePath"));
								// String string = "";
								// if
								// (jsonArray.getJSONObject(i).getJSONArray("problemImgs").length()
								// > 0) {
								// string =
								// jsonArray.getJSONObject(i).getJSONArray("problemImgs").getJSONObject(0)
								// .getString("imgPath");
								// }
								// map.put("imgPath", string);
								Data.add(map);
							}
							Data = DisplayUitl.parseLowerList(Data);
						}

					} catch (Exception e) {

					}
				}
				if (Data.size() > 0) {
					if (currage == 1) {
						SearchList.clear();
						SearchList.addAll(Data);
					} else {
						SearchList.addAll(Data);
					}
					if (Data.size() == 0 || Data.size() < pageSize) {// /���һҳ
						isMaxPage = true;
						// Toast.makeText(RwxxXcjlActivity.this, " isMaxPage =
						// true", 200).show();
						if (handler != null)
							handler.sendEmptyMessage(2);
					}
					if (handler != null)
						handler.sendEmptyMessage(1);
				} else {
					try {
						if (currage == 1) {
							handler.sendEmptyMessage(0);
						} else {
							handler.sendEmptyMessage(2);
						}
						isMaxPage = false;
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		});
		httpHandlers.add(httpHandler);
	}

	public void candleHttpConnect() {
		for (HttpHandler handler : httpHandlers) {
			handler.cancel();
		}
	}

	/**
	 * FileName: TaskManagerFlowActivity.java Description:�������������������
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
	 * @Create at: 2012-12-10 ����03:34:21
	 */
	public class MyTaskAdapter extends BaseAdapter {
		private final Context context;
		private LayoutInflater mInflater = null;
		private ArrayList<HashMap<String, Object>> data;
		private final int textSize;

		/**
		 * Description:
		 * 
		 * @param _Context
		 * @param
		 * @param ����״̬
		 * @return
		 * @author Administrator
		 * @Create at: 2013-4-9 ����3:27:39
		 */
		public MyTaskAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
			this.mInflater = LayoutInflater.from(context);
			this.context = context;
			this.data = data == null ? new ArrayList<HashMap<String, Object>>() : data;

			textSize = 21;
		}

		public void AddValue(ArrayList<HashMap<String, Object>> data) {
			if (this.data == null) {
				this.data = data;
			} else {
				this.data.addAll(data);
			}
		}

		public void shuaxin() {

			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			if (data == null || data.size() == 0) {
				return 0;
			}
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// if (data == null || data.size() == 0) {
			// YutuLoading loading = new YutuLoading(context);
			// // if (!Net.checkNet(TaskFlowSildeActivity.this)) {
			// // Toast.makeText(context, "������������,��ʱ�޷���ȡ������Ϣ",
			// // Toast.LENGTH_SHORT).show();
			// // }
			// loading.setLoadMsg("", "���޴�״̬����");
			// loading.setFailed();
			// return loading;
			// }
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.wd_document_search_listitem, null);
				holder = new ViewHolder();
				holder.tv_docName = (TextView) convertView.findViewById(R.id.tv_docName);
				holder.tv_UploadPerson = (TextView) convertView.findViewById(R.id.tv_UploadPerson);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_docName.setText(data.get(position).get("filename") + "");
			holder.tv_UploadPerson.setText(data.get(position).get("uploadperson") + "");
			holder.tv_time.setText(data.get(position).get("uploadtime") + "");

			return convertView;
		}
	}

	protected class ViewHolder {

		/** �ĵ����� */
		public TextView tv_docName = null;
		/** �ϱ��� */
		public TextView tv_UploadPerson = null;
		/** ʱ�� */
		public TextView tv_time = null;

		/** �б������б��е�λ�� */
		public int position = 0;
	}

	/** listview�ײ��������ݵĽ��ȿ� */
	protected LinearLayout mProgressLoadLayout;
	/** ����һ�����ֲ��� */
	protected final android.widget.LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	/** �������ݵȴ���ĸ����� */
	protected LinearLayout mLoadLayout;

	private void addListLoadBar(ListView listview) {
		/** ��ȡ�ֻ���� */

		DisplayMetrics dm = new DisplayMetrics();
		DocumentSearchActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

		/** ProgressBar�Ĳ��� */
		mLoadLayout = new LinearLayout(DocumentSearchActivity.this);
		mLoadLayout.setMinimumHeight(30);
		mLoadLayout.setMinimumWidth(dm.heightPixels);
		mLoadLayout.setGravity(Gravity.CENTER);
		mLoadLayout.setOrientation(LinearLayout.VERTICAL);
		mLoadLayout.setBackgroundResource(R.drawable.loading);
		mLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout = new LinearLayout(DocumentSearchActivity.this);
		mProgressLoadLayout.setMinimumHeight(30);
		mProgressLoadLayout.setGravity(Gravity.CENTER);

		ProgressBar mProgressBar = new ProgressBar(DocumentSearchActivity.this);
		mProgressBar.setPadding(0, 0, 15, 0);
		mProgressBar.setScrollBarStyle(DocumentSearchActivity.this.MODE_WORLD_READABLE);
		/** Ϊ������ӽ����� */
		mProgressLoadLayout.addView(mProgressBar, mLayoutParams);
		TextView mTipContent = new TextView(DocumentSearchActivity.this);
		mTipContent.setText("���ڼ�������,���Ժ�...");
		mTipContent.getPaint().setFakeBoldText(true);
		mTipContent.setGravity(Gravity.CENTER_HORIZONTAL);
		/** Ϊ��������ı� */
		mProgressLoadLayout.addView(mTipContent, mLayoutParams);
		/** Ĭ����Ϊ���ɼ���ע��View.GONE��View.INVISIBLE������ */
		mProgressLoadLayout.setVisibility(View.GONE);
		mProgressLoadLayout.setGravity(Gravity.CENTER);
		/** ���mProgressLoadLayout����Ϊһ��View */
		mLoadLayout.addView(mProgressLoadLayout,
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex && !isMaxPage) {
				// ������Զ�����,��������������첽�������ݵĴ���
				mProgressLoadLayout.setVisibility(View.VISIBLE);
				mLoadLayout.setVisibility(View.VISIBLE);
				loadPagedata(++currage, edit_doc.getText().toString());

			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			this.visibleItemCount = visibleItemCount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		}

	}

	/**
	 * ��������
	 */
}
