package cn.com.mapuni.meshing.activity.xc_activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrr;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrrPoint;
import cn.com.mapuni.meshing.model.ImageModel;
import cn.com.mapuni.meshing.model.PotrlObject;
import cn.com.mapuni.meshing.model.ShangBaoBuMen;
import cn.com.mapuni.meshing.model.ZuJiModel;
import cn.com.mapuni.meshing.model.ZuJiModel.RowsBean;
import cn.com.mapuni.meshing.util.DateTimePickDialogUtil;

import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.overlay.MarkerOverlay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class QiandaoFragment extends BaseFragment implements OnClickListener {
	private TextView time, adrr, ddwt_txt, qd_info_txt;
	private MapView mapview;
	private ImageView qd_bu, qd_stop;
	private String lat = "", lon = "";
	private static final OkHttpClient client = new OkHttpClient();
	Spinner sp_jianguan_object;// 监管对象
	/** 最后登录的用户信息SP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	List<PotrlObject.RowsBean> Objects = new ArrayList<PotrlObject.RowsBean>();// 监管对象全局
	private String entName;// 对象名称
	ArrayAdapter<String> adapter;
	private List<RowsBean> rows;
	String userGridLevel = "";
	PotrlObject potrlObject;
	@Override
	public View initView() {
		View mainView = View.inflate(context, R.layout.qiandaoactivity_layout, null);
		time = (TextView) mainView.findViewById(R.id.time);
		adrr = (TextView) mainView.findViewById(R.id.adrr);
		ddwt_txt = (TextView) mainView.findViewById(R.id.ddwt_txt);
		qd_info_txt = (TextView) mainView.findViewById(R.id.qd_info_txt);
		sp_jianguan_object = (Spinner) mainView.findViewById(R.id.sp_jianguan_object);
		mapview = (MapView) mainView.findViewById(R.id.mapview);
		qd_bu = (ImageView) mainView.findViewById(R.id.qd_bu);
		qd_stop = (ImageView) mainView.findViewById(R.id.qd_stop);
		qd_bu.setOnClickListener(this);
		ddwt_txt.setOnClickListener(this);
		return mainView;
	}

	@Override
	public void initData() {
		userGridLevel = DisplayUitl.readPreferences(context, LAST_USER_SP_NAME, "userGridLevel");
		time.setText(getDate());
		adrr.setText("");
		ddwt_txt.setText(Html.fromHtml("<u>点位微调</u>"));
		setadrr();
		setMyPostionMaker();
		initObjectData();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getObjectData();
		initObjectData();
	}

	private DateTimePickDialogUtil util;

	@Override
	public void initListener() {
		util = new DateTimePickDialogUtil(getActivity(), "");
		sp_jianguan_object.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position == 0) {
					qd_info_txt.setText("请先选择监管对象");
					entName = "";
					qd_bu.setVisibility(View.GONE);
					qd_stop.setVisibility(View.VISIBLE);

				} else {
					List<String> entNames = new ArrayList<String>();
					entName = adapter.getItem(position).toString().trim();

					for (int ii = 0; ii < tempRows.size(); ii++) {
						if (entName.equals(tempRows.get(ii).getName())) {
							if ((tempRows.get(ii).getSignedStatus().equals("1") && userGridLevel.equals("4"))
									|| (tempRows.get(ii).getSignedSanStatus().equals("1") && userGridLevel.equals("3"))) {
								qd_bu.setVisibility(View.GONE);
								qd_stop.setVisibility(View.VISIBLE);
								qd_info_txt.setText("当前监管企业本周已完成签到次数");
							} else {
								if (rows != null) {
									for (int i = 0; i < rows.size(); i++) {
										entNames.add(rows.get(i).getEntName());
									}
									if (entNames.contains(entName)) {
										qd_bu.setVisibility(View.GONE);
										qd_stop.setVisibility(View.VISIBLE);
										qd_info_txt.setText(util
												.initStringTime(rows.get(entNames.indexOf(entName)).getCreateTime()));
									} else {
										qd_bu.setVisibility(View.VISIBLE);
										qd_stop.setVisibility(View.GONE);
										qd_info_txt.setText("今天您还未签到");
										/*
										 * double shortDistance =
										 * getShortDistance(Double.parseDouble(
										 * Objects.get(position).getX()),
										 * Double.parseDouble(Objects.get(
										 * position).getY()),Double.parseDouble(
										 * lon)/1e6,Double.parseDouble(lat)/1e6)
										 * ; String
										 * a=Double.parseDouble(Objects.get(
										 * position).getX())+"[]"+Double.
										 * parseDouble(Objects.get(position).
										 * getY())+"[]"+Double.parseDouble(lon)/
										 * 1e6+"[]"+Double.parseDouble(lat)/1e6+
										 * "[]"; Toast.makeText(context,a+
										 * shortDistance,10000).show();
										 */
									}
								} else {
									qd_bu.setVisibility(View.GONE);
									qd_stop.setVisibility(View.VISIBLE);
									qd_info_txt.setText("请求签到次数失败，请检查网络后重试");
								}
							}
						}
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private HttpUtils httpUtils;

	public void initObjectData() {
		Calendar calendar = Calendar.getInstance(); // 获取当前时间，作为参数shang'chu
		String year = calendar.get(Calendar.YEAR) + "";
		int i = calendar.get(Calendar.MONTH);
		int d = calendar.get(Calendar.DAY_OF_MONTH);
		String month = "";
		String day = "";
		if (i < 9) {
			month = "0" + (i + 1);
		} else {
			month = i + 1 + "";
		}
		if (d < 10) {
			day = "0" + d;
		} else {
			day = d + "";
		}
		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sessionId", DisplayUitl.readPreferences(context, "lastuser", "sessionId"));
		params.addBodyParameter("gridCode", DisplayUitl.readPreferences(context, "lastuser", "organization_code"));
		// +"-"+calendar.get(Calendar.DAY_OF_MONTH)
		params.addBodyParameter("selectTime", year + "-" + month);
		params.addBodyParameter("dayTime", year + "-" + month + "-" + day);

		httpUtils.send(HttpMethod.POST, PathManager.ZUJI_URL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				rows = null;
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String data = arg0.result.toString().toString();
				try {
					if (data != "") {
						Gson gson = new Gson();
						ZuJiModel fromJson = gson.fromJson(data, ZuJiModel.class);
						rows = fromJson.getRows();
					}
				} catch (Exception e) {
				}
			}
		});
	}

	private void setadrr() {
		MapBinder.getInstance().getBinder().getLocationAddr(new CallBackAdrr() {

			@Override
			public void callbackadrr(String r, GeoPoint mGeoPoint) {
				adrr.setText(r);
				StringBuffer lonBuffer = new StringBuffer(String.valueOf(mGeoPoint.getLongitudeE6()));
				lon = lonBuffer.insert(3, ".").toString();
				StringBuffer latBuffer = new StringBuffer(String.valueOf(mGeoPoint.getLatitudeE6()));
				lat = latBuffer.insert(2, ".").toString();

			}
		});
	}

	private void setMyPostionMaker() {
		MapBinder.getInstance().getBinder().CallBackAdrrPoint(new CallBackAdrrPoint() {
			@Override
			public void CallBackAdrrPoint(GeoPoint mGeoPoint) {
				addMyOverlayItem(mGeoPoint);
				mapview.getController().setZoom(12);
			}
		});
	}

	private void addMyOverlayItem(GeoPoint point) {
		MarkerOverlay overlay = new MarkerOverlay();
		overlay.setPosition(point);
		try {
			overlay.setIcon(getResources().getDrawable(R.drawable.postion_icon));
			mapview.addOverlay(overlay);
		} catch (Exception e) {

		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.qd_bu:// 签到
			Intent intent = new Intent(context, SigninToServerActivity.class);
			intent.putExtra("createTime", time.getText().toString());
			intent.putExtra("latitude", lat);
			intent.putExtra("longitude", lon);
			intent.putExtra("location", adrr.getText().toString());
			intent.putExtra("entName", entName);
			for (int i = 0; i < Objects.size(); i++) {
				if (Objects.get(i).getName().equals(entName)) {
					intent.putExtra("industryType", Objects.get(i).getIndustryType());
					intent.putExtra("superviseType", Objects.get(i).getSuperviseType());
					intent.putExtra("address", Objects.get(i).getAddress());
				}
			}
			startActivityForResult(intent, 1);
			break;
		case R.id.ddwt_txt:
			setMyPostionMaker();
			break;
		}
	}

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { // TODO Auto-generated method stub
	 * super.onActivityResult(requestCode, resultCode, data); initObjectData();
	 * }
	 */
	private String getDate() {
		DateFormat format = new DateFormat();
		return format.format("yyyy年MM月dd日  HH点mm分ss秒", new Date()).toString();
	}

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 103:
				Toast.makeText(context, "请检查网络是否异常...", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(context, "今天已签到，无需多次提交...", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				// String stringTime =
				// util.initStringTime(Long.valueOf(beforTime));
				qd_info_txt.setText(beforTime);
				qd_bu.setBackgroundDrawable(getResources().getDrawable(R.drawable.qb_yes));
				break;

			default:
				break;
			}
			return false;
		}
	});

	private void submit() {

		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("userid", Global.getGlobalInstance().getUserid()).addFormDataPart("longitude", lon)
				.addFormDataPart("latitude", lat).addFormDataPart("location", String.valueOf(adrr.getText()))
				.addFormDataPart("date", String.valueOf(time.getText()));

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(PathManager.QIANDAO_URL).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				if (ok) {
					handler.sendEmptyMessage(102);
				} else {
					handler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(103);
			}
		});

	}

	ShangBaoBuMen shangBaoBuMen;
	private String beforTime;
	List<PotrlObject.RowsBean> tempRows = new ArrayList<PotrlObject.RowsBean>();

	List<String> list;
	/**
	 * 获取监管对象类型数据
	 * http://119.164.253.236:8184/JiNanhuanbaoms/patrolObject/list.do?sessionId
	 * =8a8f87ac5abd1242015abd3f10290020
	 */
	private void getObjectData() {
		final YutuLoading yutuLoading = new YutuLoading(context);
		yutuLoading.setCancelable(false);
		yutuLoading.setLoadMsg("正在获取监管对象数据，请稍候", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);
		utils.configSoTimeout(5 * 1000);
		RequestParams params = new RequestParams();// 添加提交参数
		String sessionId = DisplayUitl.readPreferences(context, LAST_USER_SP_NAME, "sessionId");
		params.addBodyParameter("sessionId", sessionId);
		Log.i("aaa", sessionId);
		String url = PathManager.GETXUNCHAOBJECT_URL_JINAN + "?sessionId=" + sessionId;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(context, "数据请求失败", 200).show();
				rows = null;
				if(list != null) {
					initSpinner(sp_jianguan_object, list);
				}
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				String resoult = String.valueOf(arg0.result);
				Gson gson = new Gson();
				potrlObject = gson.fromJson(resoult, PotrlObject.class);
				if ("200".equals(potrlObject.getStatus())) {//
					Objects = potrlObject.getRows();
					list = new ArrayList<String>();
					int i = 0;
					for (PotrlObject.RowsBean bean : Objects) {
						if(i==0) {
							list.add(bean.getName());
							tempRows.add(bean);
							i++;							
						}
						if (bean.getSuperviseType().equals("gywr") || bean.getSuperviseType().equals("yangzhi")
								|| bean.getSuperviseType().equals("sggd")
								|| bean.getSuperviseType().equals("sensitive")) {
							list.add(bean.getName());
							tempRows.add(bean);
						}
					}
					initSpinner(sp_jianguan_object, list);
				} else {
					Log.v("lfwang", "获取监管对象失败" + shangBaoBuMen.getMessage());
				}

			}
		});

	}

	// spinner初始化简单封装
	private void initSpinner(Spinner spinner, List<String> data) {
		// 建立Adapter并且绑定数据源
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
