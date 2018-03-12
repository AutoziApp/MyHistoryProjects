package com.jy.environment.widget;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.jy.environment.R;
import com.jy.environment.adapter.ShareAdapter;
import com.jy.environment.model.ShareInfo;
import com.jy.environment.services.HbdtServices;
import com.jy.environment.util.HttpGetTask;
import com.jy.environment.webservice.UrlComponent;
//import com.baidu.mapapi.search.MKAddrInfo;
//import com.baidu.mapapi.search.MKBusLineResult;
//import com.baidu.mapapi.search.MKDrivingRouteResult;
//import com.baidu.mapapi.search.MKPoiResult;
//import com.baidu.mapapi.search.MKSearch;
//import com.baidu.mapapi.search.MKSearchListener;
//import com.baidu.mapapi.search.MKShareUrlResult;
//import com.baidu.mapapi.search.MKSuggestionResult;
//import com.baidu.mapapi.search.MKTransitRouteResult;
//import com.baidu.mapapi.search.MKWalkingRouteResult;
//import com.baidu.platform.comapi.basestruct.GeoPoint;

public class DragViwe extends RelativeLayout implements OnTouchListener,
		OnClickListener {
	private final int SHARE_LIST = 999;

	private final float dis = 100;
	private float _maxy = 0f;
	private Context mcontext;
	private float dy = 0f;
	private float ly = 0f;
	private float y2 = 0f;
	private float y1 = 0f;
	private float y = 0f;
	private int _lon = 0;
	private int _lat = 0;
	private RelativeLayout re;
//	private MKSearch mMKSearch = null;
	private MapView mMapView = null;
	private TextView txt_addr;
	private ListView lv;

	private Handler mhandler;

	String[] items = { "相机拍摄", "手机相册" };
	DragViwe dv;

	// 标记动画是否开始
	private boolean isstart = false;

	// 标记移动的值
	private float yd = 0f;

	// 分享按钮
//	ImageView btn_share;

	// 标记是否开始拖拽
	private boolean isdrag = false;

	public DragViwe(Context context, float maxy, int lon, int lat,
			MapView mapview) {
		super(context);
		mcontext = context;
		_maxy = maxy;
		setOnTouchListener(this);
		dv = this;
		this._lat = lat;
		this._lon = lon;
		mMapView = mapview;
		mhandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				try {
					switch (msg.what) {
					
					case SHARE_LIST:
						stopload1();
						JSONArray ja = new JSONArray( msg.obj.toString());
						List<ShareInfo> lst = new ArrayList<ShareInfo>();
						for (int i = 0; i < ja.length(); i++) {

							JSONObject js = (JSONObject) ja.get(i);
							ShareInfo s=ShareInfo.GetShareInfoByJson(js);
							s.set_headimg(BitmapFactory.decodeResource(
									mcontext.getResources(), R.drawable.touxiang));
							lst.add(s);
//							btn_share.setImageBitmap(s.get_contentimg());
//							btn_share.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.aqi));
						}
						ShareAdapter sp = new ShareAdapter(mcontext, lst);
						lv.setAdapter(sp);
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
	}

	public void init() {
		re = (RelativeLayout) LayoutInflater.from(mcontext).inflate(
				R.layout.map_drag_layout, null);
		addView(re);
		lv = (ListView) findViewById(R.id.lst_share);
		txt_addr = (TextView) findViewById(R.id.txt_addr);
		addData();

		lv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	public void UpdateDragView(int lon, int lat) {
		this._lat = lat;
		this._lon = lon;
		addData();
	}

	// 从服务器加载数据
	private void addData() {
		 if(lv!=null){
		 lv.setAdapter(null);
		 }

		txt_addr.setText("");
		startload();
		startload1();
		String url = HbdtServices.ServerIp + HbdtServices.GetShare + _lon + "/"
				+ _lat+UrlComponent.token;
		HttpGetTask task = new HttpGetTask(mhandler, url, SHARE_LIST);
		task.execute();
//		mMKSearch = new MKSearch();
//		mMKSearch.init(WeiBaoApplication.getInstance().mBMapManager, this);
//		mMKSearch.reverseGeocode(new GeoPoint(_lat, _lon));
	}

	private void startload() {
		ImageView title_update = (ImageView) findViewById(R.id.title_update);
		if (title_update != null) {
			title_update.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(mcontext,
					R.anim.refresh_rotate);
			title_update.startAnimation(animation);
		}
	}

	private void stopload() {
		ImageView title_update = (ImageView) findViewById(R.id.title_update);
		if (title_update != null) {
			title_update.clearAnimation();
			title_update.setVisibility(View.INVISIBLE);
		}
	}
	
	private void startload1() {
		ProgressBar title_update = (ProgressBar) findViewById(R.id.title_update1);
		if (title_update != null) {
			title_update.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(mcontext,
					R.anim.refresh_rotate);
//			title_update.startAnimation(animation);
		}
	}

	private void stopload1() {
		ProgressBar title_update = (ProgressBar) findViewById(R.id.title_update1);
		if (title_update != null) {
//			title_update.clearAnimation();
			title_update.setVisibility(View.INVISIBLE);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// if(isstart){
		// return true;
		// }

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isdrag = true;
			ly = event.getRawY();
			dy = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (isdrag) {
				float y1 = event.getRawY();
				float cy = y1 - ly;
				y = getY();
				if (cy > 0) {
					if (y + cy >= _maxy) {
						// setY(_maxy);
					} else {
						setY(y + cy);
					}
				} else {
					if (y + cy <= 0) {
						setY(0);
					} else {
						setY(y + cy);
					}
				}
				ly = event.getRawY();
			}
			break;
		case MotionEvent.ACTION_UP:
			isdrag = false;
			float w = this.getHeight();
			y = getY();
			// 确定是向上还是向下
			y1 = event.getRawY();
			TranslateAnimation ta;
			float h1 = 0f;
			float w1 = w / 5;
			if (y1 - dy > 0) {// 向下移动
				// 判断y是否大于高度的1/3
				if (y < w1) {
					h1 = w1 - y;
				} else if (y > w1 && y < _maxy) {
					h1 = _maxy - y;
				} else {
					h1 = 0f;
				}
				// y2 = y + w / 2;
				// if (y2 >= _maxy) {
				// y2 = _maxy;
				// h1 = w - (w - _maxy) - y;
				// } else {
				// h1 = w / 2;
				// }
				ta = new TranslateAnimation(0, 0, 0, h1);
				yd = h1;
			} else if (y1 - dy < 0) {
				if (y < w1) {
					h1 = -y;
				} else if (y > w1 && y < _maxy) {
					h1 = w1 - y;
				} else {
					h1 = 0f;
				}
				// y2 = y - w / 2;
				// if (y2 <= 0) {
				// y2 = 0;
				// h1 = w - (w - y);
				// } else {
				// h1 = w / 2;
				// }
				// h1 = -h1;
				ta = new TranslateAnimation(0, 0, 0, h1);
				yd = h1;
			} else {
				return true;
			}

			// ta=new TranslateAnimation(0, 150, 0, h1);
			ta.setDuration(200);
			ta.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					isstart = true;
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					isstart = false;
					dv.clearAnimation();
					if (y + yd >= _maxy) {
						// setY(_maxy);
					} else if (y + yd <= 0) {
						// setY(0);
					}
					setY(y + yd);
				}
			});
			y2 = 0f;
			ly = 0f;
			dy = 0f;
			if (isstart)
				break;
			this.startAnimation(ta);

			break;

		}
		return true;
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
//		case R.id.btn_share:
//			MapMianActivity ac = (MapMianActivity) mcontext;
//			// Intent intent=new Intent(ac, ShareActivity.class);
//			// intent.putExtra("ditu", "1");
//			ac.startshareactivity(_lon, _lat);
//
//			break;
		}
	}

	public int get_lon() {
		return _lon;
	}

	public void set_lon(int _lon) {
		this._lon = _lon;
	}

	public int get_lat() {
		return _lat;
	}

	public void set_lat(int _lat) {
		this._lat = _lat;
	}

//	@Override
//	public void onGetAddrResult(MKAddrInfo res, int error) {
//		stopload();
//		if (error != 0) {
//			// String str = String.format("错误号：%d", error);
//			// Toast.makeText(GeoCoderDemo.this, str, Toast.LENGTH_LONG).show();
//			return;
//		}
//		// 地图移动到该点
//		mMapView.getController().animateTo(res.geoPt);
//		if (res.type == MKAddrInfo.MK_GEOCODE) {
//			// 地理编码：通过地址检索坐标点
//			// String strInfo = String.format("纬度：%f 经度：%f",
//			// res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);
//			// Toast.makeText(GeoCoderDemo.this, strInfo,
//			// Toast.LENGTH_LONG).show();
//		}
//		if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
//			// 反地理编码：通过坐标点检索详细地址及周边poi
//			String strInfo = res.strAddr;
//			txt_addr.setText(strInfo);
//			// Toast.makeText(GeoCoderDemo.this, strInfo,
//			// Toast.LENGTH_LONG).show();
//
//		}
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
//
//	@Override
//	public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
//		// TODO Auto-generated method stub
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

}
