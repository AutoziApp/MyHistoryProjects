package cn.com.mapuni.meshing.activity.xxcx_activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.mapuni.meshing.adapter.RiskItemRecordsAdapter;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.business.BaseClass;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.model.RiskItemRecords;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;
import cn.com.mapuni.meshingtotal.R;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XxcxMainActivity extends BaseActivity implements OnClickListener{
	private ScrollView sv_content;
	private TextView xxcx_wghjg,xxcx_gssk,xxcx_wry,xxcx_xzwl,xxcx_jzyc,xxcx_ztyc,xxcx_zgdyc,xxcx_ztc,xxcx_jcqyc,xxcx_jcqklh;

	private YutuLoading yutuLoading;
	private EditText etSearch;
	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private List<RiskItemRecords> data = new ArrayList<>();
	private RiskItemRecordsAdapter adapter;
	private int page=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		setBACK_ISSHOW(false);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"信息查询");
		initView();
		initData();
	}

	protected void initData() {
		page=0;
		readRiskSources("");
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.xxcxmainactivity_layout, null);
		middleLayout.addView(mainView);

		etSearch = (EditText) findViewById(R.id.etSearch);
		findViewById(R.id.ivSearch).setOnClickListener(this);
		findViewById(R.id.fl_imageView).setOnClickListener(this);
		mPullRefreshListView=(PullToRefreshListView)findViewById(R.id.listView);
		mPullRefreshListView.setHasMoreData(true);
		listView=mPullRefreshListView.getRefreshableView();
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onPullDownToRefresh() {
				page=0;
				readRiskSources(etSearch.getText().toString().trim());
			}

			@Override
			public void onPullUpToRefresh() {
				readRiskSources(etSearch.getText().toString().trim());
			}
		});

		adapter=new RiskItemRecordsAdapter(this,data);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(XxcxMainActivity.this, WuRanYuanActivity.class);
				intent.putExtra("title", "污染源");
				startActivity(intent);
			}
		});

		sv_content = (ScrollView) mainView.findViewById(R.id.sv_content);
		xxcx_jcqklh = (TextView) mainView.findViewById(R.id.xxcx_jcqklh);
		xxcx_wghjg = (TextView) mainView.findViewById(R.id.xxcx_wghjg);
		xxcx_gssk = (TextView) mainView.findViewById(R.id.xxcx_gssk);
		xxcx_wry = (TextView) mainView.findViewById(R.id.xxcx_wry);
		xxcx_xzwl = (TextView) mainView.findViewById(R.id.xxcx_xzwl);
		xxcx_jzyc = (TextView) mainView.findViewById(R.id.xxcx_jzyc);
		xxcx_ztyc = (TextView) mainView.findViewById(R.id.xxcx_ztyc);
		xxcx_zgdyc = (TextView) mainView.findViewById(R.id.xxcx_zgdyc);
		xxcx_ztc = (TextView) mainView.findViewById(R.id.xxcx_ztc);
		xxcx_jcqyc = (TextView) mainView.findViewById(R.id.xxcx_jcqyc);
		xxcx_jcqklh.setOnClickListener(this);
		xxcx_wghjg.setOnClickListener(this);
		xxcx_gssk.setOnClickListener(this);
		xxcx_wry.setOnClickListener(this);
		xxcx_xzwl.setOnClickListener(this);
		xxcx_jzyc.setOnClickListener(this);
		xxcx_ztyc.setOnClickListener(this);
		xxcx_zgdyc.setOnClickListener(this);
		xxcx_ztc.setOnClickListener(this);
		xxcx_jcqyc.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
			case R.id.etSearch:
				page=0;
				readRiskSources(etSearch.getText().toString().trim());
				break;
			case R.id.fl_imageView:
				if (sv_content.getVisibility() == View.VISIBLE) {
					sv_content.setVisibility(View.GONE);
				} else {
					sv_content.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.xxcx_wghjg:
				intent = new Intent(XxcxMainActivity.this, WghjgActivity.class);
				intent.putExtra("title", "网格化监管");
				startActivity(intent);
				break;
			case R.id.xxcx_gssk:
				intent = new Intent(XxcxMainActivity.this, GsskActivity.class);
				intent.putExtra("title", "国省市控");
				startActivity(intent);
				break;
			case R.id.xxcx_wry:
				intent = new Intent(XxcxMainActivity.this, WuRanYuanActivity.class);
				intent.putExtra("title", "污染源");
				startActivity(intent);
				break;
			case R.id.xxcx_xzwl:
				intent = new Intent(XxcxMainActivity.this, XzwlActivity.class);
				intent.putExtra("title", "乡镇网络");
				startActivity(intent);
				break;
			case R.id.xxcx_jzyc:
				intent = new Intent(XxcxMainActivity.this, JzycActivity.class);
				intent.putExtra("title", "建筑扬尘");
				startActivity(intent);
				break;
			case R.id.xxcx_ztyc:
				intent = new Intent(XxcxMainActivity.this, ZtycActivity.class);
				intent.putExtra("title", "渣土扬尘");
				startActivity(intent);
				break;
			case R.id.xxcx_zgdyc:
				intent = new Intent(XxcxMainActivity.this, ZgdycActivity.class);
				intent.putExtra("title", "主干道扬尘");
				startActivity(intent);
				break;
			case R.id.xxcx_ztc:
				intent = new Intent(XxcxMainActivity.this, ZhaTuCheGPSActivity.class);
				intent.putExtra("title", "渣土车GPS");
				startActivity(intent);
				break;
			case R.id.xxcx_jcqyc:
				intent = new Intent(XxcxMainActivity.this, JianChengQuYangChenActivity.class);
				intent.putExtra("title", "建成区扬尘");
				startActivity(intent);
				break;
			case R.id.xxcx_jcqklh:
				intent = new Intent(XxcxMainActivity.this, LuoLouTuDiActivity.class);
				intent.putExtra("title", "建成区可绿化裸露土地");
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	/**
	 * 根据名称读取风险源
	 * @param name
	 */
	private void readRiskSources(final String name) {
		yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("正在获取信息，请稍候", "");
		yutuLoading.showDialog();
		//接口调用方法一
		String url = PathManager.JINAN_URL + PathManager.getAllEnterpriseInfo;

		RequestParams params = new RequestParams();// 添加提交参数
//        params.setContentType("application/x-www-form-urlencoded");
		params.addBodyParameter("EntType", "");
		params.addBodyParameter("Region", "");
		params.addBodyParameter("street", "");
		params.addBodyParameter("basin", "");

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(XxcxMainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
//				updateList();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = String.valueOf(arg0.result);

				try {
					ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
					SAXReader reader = new SAXReader();
					Document document = reader.read(inputStream);
					Element root = document.getRootElement();
					JSONArray jsonArray = new JSONArray( root.getText());
					if (jsonArray != null && jsonArray.length() > 0) {
						RiskItemRecords riskItemRecords;
						for (int i = 0; i < jsonArray.length(); i ++) {
							riskItemRecords = new RiskItemRecords();
							riskItemRecords.setName(jsonArray.getJSONObject(i).getString("EntName"));
							data.add(riskItemRecords);
						}
					}
				} catch (Exception e) {

				}
//				Toast.makeText(XxcxMainActivity.this, "数据请求成功", Toast.LENGTH_SHORT).show();
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				updateList();
			}
		});
		//接口调用方法二
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
//				Toast.makeText(XxcxMainActivity.this, "数据请求失败", Toast.LENGTH_LONG).show();
//			}
//
//			@Override
//			public void onFailure(Call arg0, IOException e) {
//				// TODO Auto-generated method stub
//				page++;
//				handler.sendEmptyMessage(0);
//				Toast.makeText(XxcxMainActivity.this, "数据请求失败", Toast.LENGTH_LONG).show();
//			}
//
//		});
		//接口调用方法三
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

	private android.os.Handler handler = new android.os.Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}
			updateList();
		}
	};

	private void updateList(){
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onPullUpRefreshComplete();
		mPullRefreshListView.onPullDownRefreshComplete();
	}

}
