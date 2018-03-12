package cn.com.mapuni.meshingtotal.activity.xxcx;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldnut.app.sdk.view.pullrefresh.PullToRefreshBase;
import com.goldnut.app.sdk.view.pullrefresh.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tianditu.android.maps.MapView;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.gis.meshingtotal.AriQualityActivity;
import cn.com.mapuni.gis.meshingtotal.JianChengQuYangChenActivity;
import cn.com.mapuni.gis.meshingtotal.JzycActivity;
import cn.com.mapuni.gis.meshingtotal.LuoLouTuDiActivity;
import cn.com.mapuni.gis.meshingtotal.SearchDetailActivity;
import cn.com.mapuni.gis.meshingtotal.WghjgActivity;
import cn.com.mapuni.gis.meshingtotal.WuRanYuanActivity;
import cn.com.mapuni.gis.meshingtotal.ZgdycActivity;
import cn.com.mapuni.gis.meshingtotal.ZhaTuCheGPSActivity;
import cn.com.mapuni.gis.meshingtotal.ZtycActivity;
import cn.com.mapuni.gis.meshingtotal.model.PollutionSourceBean1;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.adapter.RiskItemRecordsAdapter1;

public class XxcxMainActivity extends BaseActivity implements OnClickListener {
	private TextView xxcx_wghjg, xxcx_gssk, xxcx_wry, xxcx_jzyc, xxcx_ztyc, xxcx_zgdyc, xxcx_ztc, xxcx_jcqyc, xxcx_jcqklh;

	private View searchView;

	private YutuLoading yutuLoading;
	private EditText etSearch;
	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private List<PollutionSourceBean1> data = new ArrayList<>();
	private RiskItemRecordsAdapter1 adapter;
	private int pagerIndex = 1;
	private String entName = "";



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		setBACK_ISSHOW(false);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "��Ϣ��ѯ");

		//YYF �¼�
		setSearchButtonVisiable(true);
		queryImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopwindow(searchView.findViewById(R.id.etSearch));
			}
		});

		initView();
		initData();
	}

	protected void initData() {

		readRiskSources();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		//		View mainView = inflater.inflate(R.layout.xxcxmainactivity_layout, null);
		//		mainView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
		//		middleLayout.addView(mainView);
		View mainView = inflater.inflate(R.layout.grid_xxcx, null);
		mainView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		xxcx_jcqklh = (TextView) mainView.findViewById(R.id.xxcx_jcqklh);
		xxcx_wghjg = (TextView) mainView.findViewById(R.id.xxcx_wghjg);
		xxcx_gssk = (TextView) mainView.findViewById(R.id.xxcx_gssk);
		xxcx_wry = (TextView) mainView.findViewById(R.id.xxcx_wry);
		xxcx_jzyc = (TextView) mainView.findViewById(R.id.xxcx_jzyc);
		xxcx_ztyc = (TextView) mainView.findViewById(R.id.xxcx_ztyc);
		xxcx_zgdyc = (TextView) mainView.findViewById(R.id.xxcx_zgdyc);
		xxcx_ztc = (TextView) mainView.findViewById(R.id.xxcx_ztc);
		xxcx_jcqyc = (TextView) mainView.findViewById(R.id.xxcx_jcqyc);
		xxcx_jcqklh.setOnClickListener(this);
		xxcx_wghjg.setOnClickListener(this);
		xxcx_gssk.setOnClickListener(this);
		xxcx_wry.setOnClickListener(this);
		xxcx_jzyc.setOnClickListener(this);

		xxcx_ztyc.setOnClickListener(this);
		xxcx_zgdyc.setOnClickListener(this);
		xxcx_ztc.setOnClickListener(this);
		xxcx_jcqyc.setOnClickListener(this);

		middleLayout.addView(mainView);

		searchView = LayoutInflater.from(XxcxMainActivity.this).inflate(R.layout.xxcxmainactivity_layout, null);

		etSearch = (EditText) searchView.findViewById(R.id.etSearch);
		etSearch.setOnClickListener(this);
		searchView.findViewById(R.id.ivSearch).setOnClickListener(this);
		searchView.findViewById(R.id.fl_imageView).setOnClickListener(this);
		mPullRefreshListView = (PullToRefreshListView) searchView.findViewById(R.id.listView);
		mPullRefreshListView.setHasMoreData(true);
		listView = mPullRefreshListView.getRefreshableView();
		//		listView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onPullDownToRefresh() {
				pagerIndex++;
				readRiskSources();
			}

			@Override
			public void onPullUpToRefresh() {
				pagerIndex++;
				readRiskSources();
			}
		});

		adapter = new RiskItemRecordsAdapter1(this, data);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(XxcxMainActivity.this, SearchDetailActivity.class);
				intent.putExtra("bean", data.get(position));
				intent.putExtra("title", "��ȾԴ");
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
			case R.id.ivSearch:
				data.clear();
				entName = etSearch.getText().toString().trim();
				pagerIndex = 1;
				readRiskSources();
				break;
			case R.id.fl_imageView:
				//				if (sv_content.getVisibility() == View.VISIBLE) {
				//					sv_content.setVisibility(View.GONE);
				//				} else {
				//					sv_content.setVisibility(View.VISIBLE);
				//				}
				showPopwindow(searchView.findViewById(R.id.etSearch));
				break;
			case R.id.xxcx_wghjg:
				intent = new Intent(XxcxMainActivity.this, WghjgActivity.class);
				intent.putExtra("title", "���񻯼��");
				startActivity(intent);
				break;
			case R.id.xxcx_gssk:
				intent = new Intent(XxcxMainActivity.this, AriQualityActivity.class);
				intent.putExtra("title", "��ʡ�п�");
				startActivity(intent);
				break;
			case R.id.xxcx_wry:
				intent = new Intent(XxcxMainActivity.this, WuRanYuanActivity.class);
				intent.putExtra("title", "��ȾԴ");
				startActivity(intent);
				break;
			case R.id.xxcx_jzyc:
				intent = new Intent(XxcxMainActivity.this, JzycActivity.class);
				intent.putExtra("title", "�����ﳾ");
				startActivity(intent);
				break;
			case R.id.xxcx_ztyc:
				intent = new Intent(XxcxMainActivity.this, ZtycActivity.class);
				intent.putExtra("title", "�����ﳾ");
				startActivity(intent);
				break;
			case R.id.xxcx_zgdyc:
				intent = new Intent(XxcxMainActivity.this, ZgdycActivity.class);
				intent.putExtra("title", "���ɵ��ﳾ");
				startActivity(intent);
				break;
			case R.id.xxcx_ztc:
				intent = new Intent(XxcxMainActivity.this, ZhaTuCheGPSActivity.class);
				intent.putExtra("title", "������GPS");
				startActivity(intent);
				break;
			case R.id.xxcx_jcqyc:
				intent = new Intent(XxcxMainActivity.this, JianChengQuYangChenActivity.class);
				intent.putExtra("title", "�������ﳾ");
				startActivity(intent);
				break;
			case R.id.xxcx_jcqklh:
				intent = new Intent(XxcxMainActivity.this, LuoLouTuDiActivity.class);
				intent.putExtra("title", "���������̻���¶����");
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	private PopupWindow menuWindow;

	public void showPopwindow(final View tempView) {

		menuWindow = new PopupWindow(searchView, MapView.LayoutParams.MATCH_PARENT,
				MapView.LayoutParams.MATCH_PARENT);
		menuWindow.setAnimationStyle(R.style.popwin_anim_style);
		// ʹ��ۼ�
		menuWindow.setFocusable(true);
		// ����������������ʧ
		menuWindow.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		menuWindow.setBackgroundDrawable(new BitmapDrawable());

		//		menuWindow.showAtLocation(tempView, Gravity.BOTTOM, 0, 0);
		menuWindow.showAsDropDown(tempView, 0, 50);
		menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
	}

	/**
	 * �������ƶ�ȡ����Դ
	 *
	 * @param
	 */
	private void readRiskSources() {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ��Ϣ�����Ժ�", "");
		yutuLoading.showDialog();
		//�ӿڵ��÷���һ
		String url = PathManager.JINAN_URL + "/getEnterpriseInfo_pageList";

		RequestParams params = new RequestParams();// ����ύ����
		params.addBodyParameter("PageIndex", pagerIndex + "");
		params.addBodyParameter("PageNum", "10");
		params.addBodyParameter("EntCode", "");
		params.addBodyParameter("EntName", entName);

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(XxcxMainActivity.this, "��������ʧ��", Toast.LENGTH_SHORT).show();
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				updateList();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = String.valueOf(arg0.result);

				try {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
					SAXReader reader = new SAXReader();
					Document document = reader.read(inputStream);
					Element root = document.getRootElement();
					JSONArray jsonArray = new JSONArray(root.getText());
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							String EntName = jsonArray.getJSONObject(i).optString("EntName");
							double Latitude = jsonArray.getJSONObject(i).optDouble("Latitude");
							double Longitude = jsonArray.getJSONObject(i).optDouble("Longitude");
							String enterprisetypr = jsonArray.getJSONObject(i).optString("enterprisetypr");
							PollutionSourceBean1 bean = new PollutionSourceBean1(EntName, Latitude, Longitude, enterprisetypr);
							data.add(bean);
						}
					}
					adapter.notifyDataSetChanged();
				} catch (Exception e) {

				}
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				updateList();
			}
		});
		//�ӿڵ��÷�����
		//		String url = "http://119.164.253.236:8112/webservice/JLWebServiceService.asmx/getAllEnterpriseInfo";
		//
		//		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
		//				.addFormDataPart("EntType", "").addFormDataPart("Region", "")
		//				.addFormDataPart("street", "").addFormDataPart("basin", "");
		//
		//		RequestBody requestBody = builder.build();
		//		Request request = new Request.Builder().url(url).post(requestBody).build();
		//
		//		OkHttpClient client = new OkHttpClient();
		//		client.newCall(request).enqueue(new okhttp3.Callback() {
		//
		//			@Override
		//			public void onResponse(Call call, Response response) throws IOException {
		//				String result =response.body().string();
		//				boolean ok = response.isSuccessful();
		//				page++;
		//				handler.sendEmptyMessage(0);
		//				Toast.makeText(XxcxMainActivity.this, "��������ʧ��", Toast.LENGTH_LONG).show();
		//			}
		//
		//			@Override
		//			public void onFailure(Call arg0, IOException e) {
		//				// TODO Auto-generated method stub
		//				page++;
		//				handler.sendEmptyMessage(0);
		//				Toast.makeText(XxcxMainActivity.this, "��������ʧ��", Toast.LENGTH_LONG).show();
		//			}
		//
		//		});
		//�ӿڵ��÷�����
		//		new Thread(new Runnable() {
		//			@Override
		//			public void run() {
		//
		//				if(page==0){
		//					data.clear();
		//				}
		//
		//				ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
		//				HashMap<String, Object> param = new HashMap<String, Object>();
		//				param.put("EntType", "");
		//				param.put("Region", "");
		//				param.put("street", "");
		//				param.put("basin", "");
		//				params.add(param);
		//				String result = "";
		//				try {
		//					result = (String) WebServiceProvider
		//							.callWebService(
		//									"http://tempuri.org/",
		//									"getAllEnterpriseInfo",
		//									params,
		//									"http://119.164.253.236:8112/webservice/JLWebServiceService.asmx",
		//									WebServiceProvider.RETURN_STRING, true);
		//
		//					if (result == null || result.equals("[]")
		//							|| result.equals("")) {
		//						handler.sendEmptyMessage(3);
		//						return;
		//					}
		//
		//					JSONArray jsonArray = new JSONArray(result);
		//					if (jsonArray != null && jsonArray.length() > 0) {
		//						RiskItemRecords riskItemRecords;
		//						for (int i = 0; i < jsonArray.length(); i ++) {
		//							riskItemRecords = new RiskItemRecords();
		//							riskItemRecords.setName(jsonArray.getJSONObject(i).getString("EntName"));
		//							data.add(riskItemRecords);
		//						}
		//					}
		//				} catch (Exception e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
		//
		//				page++;
		//				handler.sendEmptyMessage(0);
		//			}
		//		}).start();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}
			updateList();
		}
	};

	private void updateList() {
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onPullUpRefreshComplete();
		mPullRefreshListView.onPullDownRefreshComplete();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}


}
