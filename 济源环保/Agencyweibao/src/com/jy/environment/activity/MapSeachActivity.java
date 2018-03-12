package com.jy.environment.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jy.environment.R;
import com.jy.environment.adapter.MapSearchPOIAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.widget.LoadDataDialog;
//import com.baidu.mapapi.map.MKEvent;
//import com.baidu.mapapi.search.MKAddrInfo;
//import com.baidu.mapapi.search.MKBusLineResult;
//import com.baidu.mapapi.search.MKDrivingRouteResult;
//import com.baidu.mapapi.search.MKPoiInfo;
//import com.baidu.mapapi.search.MKPoiResult;
//import com.baidu.mapapi.search.MKSearch;
//import com.baidu.mapapi.search.MKSearchListener;
//import com.baidu.mapapi.search.MKShareUrlResult;
//import com.baidu.mapapi.search.MKSuggestionResult;
//import com.baidu.mapapi.search.MKTransitRouteResult;
//import com.baidu.mapapi.search.MKWalkingRouteResult;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * 地图搜索界面
 * @author baiyuchuan
 *
 */
public class MapSeachActivity extends ActivityBase implements OnClickListener,
		 OnItemClickListener {
//	private MKSearch mMKSearch = null;

	private Button btn_seach;
	private Button btn_return_map;
	private Dialog dialog;

	private ListView lst_poi;
	private EditText txt_search;

	private ListView lst_hisrecord;
	private LinearLayout map_search_type;
	private LinearLayout l_bus;
	private LinearLayout l_atm;
	private LinearLayout l_hotel;
	private LinearLayout l_food;
	private MapSearchPOIAdapter adpter;

	private boolean type_search = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_seach);
		setClickListener();
		lst_hisrecord = (ListView) findViewById(R.id.lst_hisrecord);
		map_search_type = (LinearLayout) findViewById(R.id.map_search_type);
//		mMKSearch = new MKSearch();
//
//		mMKSearch.init(WeiBaoApplication.getInstance().mBMapManager, this);


	}

	private void showpoilist() {
		lst_poi.setVisibility(View.VISIBLE);
		lst_hisrecord.setVisibility(View.GONE);
		// map_search_type.setVisibility(View.GONE);
	}

	private void showhisrecordlist() {
		lst_poi.setVisibility(View.GONE);
		lst_hisrecord.setVisibility(View.VISIBLE);
		// map_search_type.setVisibility(View.VISIBLE);
	}

	private void setClickListener() {
		btn_seach = (Button) findViewById(R.id.btn_seach);
		btn_return_map = (Button) findViewById(R.id.btn_return_map);
		btn_return_map.setOnClickListener(this);
		btn_seach.setOnClickListener(this);
		l_food = (LinearLayout) findViewById(R.id.l_food);
		l_hotel = (LinearLayout) findViewById(R.id.l_hotel);
		l_atm = (LinearLayout) findViewById(R.id.l_atm);
		l_bus = (LinearLayout) findViewById(R.id.l_bus);
		l_food.setOnClickListener(this);
		l_hotel.setOnClickListener(this);
		l_atm.setOnClickListener(this);
		l_bus.setOnClickListener(this);
		lst_poi = (ListView) findViewById(R.id.lst_poi);
		lst_poi.setOnItemClickListener(this);
		txt_search = (EditText) findViewById(R.id.txt_search);
		txt_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				int l = txt_search.getText().length();
				if (l == 0) {
					showhisrecordlist();
				} else {
					search_poi(txt_search.getText().toString());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_seach:
			String s = txt_search.getText().toString();
			if (s.length() == 0) {
				ToastUtil.showShort(this, "请输入搜索内容！");
				return;
			}
			search_poi_dialog(txt_search.getText().toString());
			// showdialog();
			break;
		case R.id.btn_return_map:
			finish();
			break;
		case R.id.l_hotel:
			search_poi_dialog("酒店");
			break;
		case R.id.l_atm:
			search_poi_dialog("ATM");
			break;
		case R.id.l_bus:
			search_poi_dialog("公交");
			break;
		case R.id.l_food:
			search_poi_dialog("美食");
			break;

		default:
			break;
		}

	}

	private void search_poi_dialog(String text) {
		type_search = true;
		// 开始搜索
		int lon = this.getIntent().getIntExtra("lon", 0);
		int lat = this.getIntent().getIntExtra("lat", 0);
		int lon1 = this.getIntent().getIntExtra("lon1", 0);
		int lat1 = this.getIntent().getIntExtra("lat1", 0);
		
		
		
//		GeoPoint gp1 = new GeoPoint(lat1, lon);
//		GeoPoint gp2 = new GeoPoint(lat, lon1);
//		mMKSearch.setPoiPageCapacity(49);
////		mMKSearch.poiSearchNearBy(text, gp1, 2000000);
//		mMKSearch.poiSearchInbounds(text,gp1,gp2);
		showdialog();
	}

	private void search_poi(String text) {
		// 开始搜索
		int lon = this.getIntent().getIntExtra("lon", 0);
		int lat = this.getIntent().getIntExtra("lat", 0);
		int lon1 = this.getIntent().getIntExtra("lon1", 0);
		int lat1 = this.getIntent().getIntExtra("lat1", 0);
//		GeoPoint gp1 = new GeoPoint(lat1, lon);
//		GeoPoint gp2 = new GeoPoint(lat, lon1);
//		mMKSearch.setPoiPageCapacity(49);
////		mMKSearch.poiSearchNearBy(text, gp1, 8000000);
//		mMKSearch.poiSearchInbounds(text,gp1,gp2);
		// showdialog();
	}

	// 显示锁屏搜索
	private void showdialog() {
		dialog = new LoadDataDialog(this);
		dialog.show();
	}

	// 隐藏锁屏搜索
	private void hidedialog() {
		if (dialog != null) {
			dialog.hide();
		}

	}

//	@Override
//	public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetPoiDetailSearchResult(int arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}

//	@Override
//	public void onGetPoiResult(MKPoiResult res, int type, int error) {
//		hidedialog();
//		// 错误号可参考MKEvent中的定义
//		if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
//			if (type_search) {
//				Toast.makeText(MapSeachActivity.this, "抱歉，未找到结果",
//						Toast.LENGTH_LONG).show();
//			}
//			// finish();MapSeachActivit
//			return;
//		} else if (error != 0 || res == null) {
//			if (type_search) {
//				Toast.makeText(MapSeachActivity.this, "搜索出错啦..",
//						Toast.LENGTH_LONG).show();
//			}
//			// finish();
//			return;
//		}
//		// int l=txt_search.getText().length();
//		if (type_search) {
//			// showhisrecordlist();
//			MapMianActivity.lstMkPoiInfo = res.getAllPoi();
//			Intent intent = new Intent();
//			intent.putExtra("type", 1);
//			setResult(RESULT_OK, intent);
//			finish();
//			return;
//		}
//		adpter = new MapSearchPOIAdapter(this, res.getAllPoi());
//		lst_poi.setAdapter(adpter);
//		showpoilist();
//		type_search = false;
//
//	}
//
//	@Override
//	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
//		MKPoiInfo poi = adpter.getItem(position);
//		Intent intent = new Intent();
//		intent.putExtra("type", 2);
//		MapMianActivity.mMkPoiInfo = poi;
//		setResult(RESULT_OK, intent);
//		finish();

	}
}
