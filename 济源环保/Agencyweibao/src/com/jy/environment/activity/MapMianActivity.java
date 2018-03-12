package com.jy.environment.activity;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIMediaObject;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQFlagType;
import com.baidu.frontia.api.FrontiaSocialShareContent.FrontiaIQQReqestType;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlay;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.map.BaseMapTypeEmun;
import com.jy.environment.map.EnvTypeOnMapEnum;
import com.jy.environment.map.Get_MapImgdata;
import com.jy.environment.map.MapImageView;
import com.jy.environment.map.MapPop_city_a;
import com.jy.environment.map.PicturePointImageItem;
import com.jy.environment.map.PicturePointItem;
import com.jy.environment.map.PollutantTypeEnum;
import com.jy.environment.map.RenderEnum;
import com.jy.environment.map.TileControl;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.City;
import com.jy.environment.model.PollutionPointModel;
import com.jy.environment.model.Province;
import com.jy.environment.model.SurfaceWaterModel;
import com.jy.environment.model.WeatherEnum;
import com.jy.environment.services.LocationService;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.WbMapUtil;
import com.jy.environment.webservice.UrlComponent;
import com.jy.environment.widget.DragViwe;
import com.umeng.analytics.MobclickAgent;

/**
 * 地图首页
 * 
 * @author baiyuchuan
 * 
 */
public class MapMianActivity extends ActivityBase implements OnClickListener {

	public static final int SURFACE_WATER_HANDLER_SEVER_TAG = 16;
	public static final int SURFACE_WATER_HANDLER_OLD_SQL_TAG = 14;
	public static final int POLLUTION_HANDLER_SEVER_TAG = 17;
	public static final int POLLUTION_HANDLER_OLD_SQL_TAG = 18;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private Uri uri;
	// public static ArrayList<MKPoiInfo> lstMkPoiInfo = null;
	// public static MKPoiInfo mMkPoiInfo = null;

	// private static boolean isExit = false;
	private final static int MESSAGE_EXIT = 0x00991;

	private final int default_dp = 100;
	private static final int CAMERA_WITH_DATA = 3023;
	private static final int CHARE_DATA = 3024;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int MAP_SEACH = 1021;
	// private int search_type = 0;
	private File mCurrentPhotoFile;// 照相机拍照得到的图片
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	DragViwe dv;
	// List<AQIPoint> pointData;
	/**
	 * 用于位置检索、周边检索、范围检索、公交检索、驾乘检索、步行检索
	 */
	// MKSearch mSearch = null;
	// WeiBaoApplication app;
	MapView mMapView = null; // 地图View
	// TextView titleView;//title
	ImageView meigeimage;
	Handler mainHandler;
	/**
	 * 定位按钮
	 */
	ImageView img_location;

	/**
	 * 用来标识是否在显示定位
	 */
	boolean showlocation = false;

	/**
	 * 标识当前显示的环境信息类别
	 */
	EnvTypeOnMapEnum enum_envtype = EnvTypeOnMapEnum.NULL;
	/**
	 * 标识当前显示的渲染信息类别
	 */
	RenderEnum enum_rendertype;

	/**
	 * 环境详情信息面板的灰色背景
	 */
	View envpanel_back;
	/**
	 * 环境详情信息面板的关闭按钮
	 */
	ImageView envpanel_close_button;
	/**
	 * 环境详情信息面板空气质量分布图选择框
	 */
	CheckBox cbox_showrender;
	/**
	 * 地图页面上方显示环境信息的内容的标题
	 */
	TextView txt_mapEnvInfoTitle;

	RelativeLayout hbdtrelayout;
	RelativeLayout maplegendpanel;
	TextView maplegendname;
	TextView maplegendtime;

	HeatMap heatmap;
	private Context context;

	private Bitmap cutscreenBitmap = Bitmap.createBitmap(100, 100,
			Config.ARGB_8888);

	TileControl tc = null;

	CityDB mCityDB = null;

	int quanju = 0;
	String[] index;
	Spinner spinner;

	TextView txt_titleShow;
	TextView txt_mapseach;
	/**
	 * 环境信息面板 AQI按钮
	 */
	TextView txt_button_aqi;
	/**
	 * 环境信息面板pm2.5按钮
	 */
	TextView txt_button_pm25;
	/**
	 * 环境信息面板pm10按钮
	 */
	TextView txt_button_pm10;
	/**
	 * 环境信息面板so2按钮
	 */
	TextView txt_button_so2;
	/**
	 * 环境信息面板no2按钮
	 */
	TextView txt_button_no2;
	/**
	 * 环境信息面板co按钮
	 */
	TextView txt_button_co;
	/**
	 * 环境信息面板o3按钮
	 */
	TextView txt_button_o3;
	/**
	 * 环境信息面板显示天气按钮
	 */
	TextView txt_button_tianqi;
	/**
	 * 环境信息面板 雾按钮
	 */
	TextView txt_button_wu;
	/**
	 * 环境信息面板霾按钮
	 */
	TextView txt_button_mai;
	/**
	 * 环境信息面板气温按钮
	 */
	TextView txt_button_qiwen;
	/**
	 * 环境信息面板交通实况按钮
	 */
	TextView txt_button_jiaotong;
	/**
	 * 环境信息面板分享图片按钮
	 */
	TextView txt_button_sharepicture;

	// TextView txt_dataupttime;

	TextView mapdiscription;

	HorizontalScrollView hscroll_legend = null;
	LinearLayout llay_legendinner = null;

	Button btn_dragview, btn_rdragview;
	Button btn_mapzoomin, btn_mapzoomout;
	Button btn_mapoffline;
	RelativeLayout map_tool_box;
	/**
	 * 环境详情信息面板
	 */
	RelativeLayout map_panel_env;
	ImageView mapLengend;
	ImageView btn_maptraffic;
	ImageView btn_mapclear;
	ImageView btn_mappicture;
	int mapstyletype = 1;// 0-weixing;1-ditu

	/**
	 * 用来保存请求显示用户分享图片的服务请求url，最近一次的，用来做比较，过滤频繁的请求操作
	 */
	String url_getuserpicture = "";

	// 获取图片Handler
	Handler mhandler;
	/**
	 * 长按地图弹出面板
	 */
	MapLongclickPopupWindow longclickpopwin;

	ImageView maploading;

	// /**
	// * 地图面板打开环境信息面板按钮
	// */
	// ImageView img_env_button;
	// /**
	// * 切换地图底图按钮
	// */
	// ImageView img_basemap_button;
	/**
	 * 清除地图上的环境信息的按钮
	 */
	TextView img_envclear_button;

	/**
	 * 标识地图底图的类型
	 */
	BaseMapTypeEmun basemaptype = BaseMapTypeEmun.WEIXING;

	AnimationDrawable anim;
	Boolean animflag = false;

	// 定位相关
	LocationClient mLocClient;
	MyLocationData locData = null;

	// 定位图层
	// MyLocationOverlay myLocationOverlay = null;

	// boolean isRequest = false;//是否手动触发请求定位
	// boolean isFirstLoc = true;//是否首次定位

	// 标记toolbox是否显示
	Boolean toolboxvisible = false;
	// 标记是否在执行动画
	Boolean isanimtion = false;
	/**
	 * 标识是否正在定位，当点击地图上的定位按钮的时候才设置为true，在定位完成后置为false，在定位响应时先检测该标识
	 */
	boolean islocationing = false;

	public String city = "北京";

	// 图片弱引用键值存放序列
	private Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	// private Map<String,WeakReference<GroundOverlayOptions>> groundCache = new
	// HashMap<String, WeakReference<GroundOverlayOptions>>();

	private Map<String, WeakReference<GroundOverlayOptions>> goverlayCache = new HashMap<String, WeakReference<GroundOverlayOptions>>();

	SDKReceiver mReceiver;
	/**
	 * 省级天气显示图层
	 */
	private MarkerOptions wOL_PROVINCE = null;
	/**
	 * 地级市天气显示图层
	 */
	private MarkerOptions wOL_MAINCITY = null;
	/**
	 * 县级市天气显示图层
	 */
	private MarkerOptions wOL_CITY = null;
	/**
	 * AQI---O3城市显示图层
	 */
	private MarkerOptions aOL_CITY = null;
	/**
	 * AQI---O3按监测站点显示图层
	 */
	private MarkerOptions aOL_POINT = null;
	/**
	 * 用户分享图片显示图层
	 */
	private MarkerOptions myPictureOverlay = null;
	/**
	 * 地图渲染图层
	 */
	private GroundOverlay renderGroundOverlay = null;
	// private MyOverlay_LTouch ltOverlay = null;
	private Marker poioverlay = null;
	private Button button = null;
	// private MapController mMapController = null;
	private InfoWindow pop = null;

	private PollutantTypeEnum polltype = PollutantTypeEnum.AQI;

	private TextView aqi_show_btn;
	private TextView source_pollution_show_btn;
	// private TextView map_more_show_btn;
	private LinearLayout map_more_detail_id;
	private TextView ditu_water, map_quality_show_btn, map_weahter_show_btn,
			map_temperature_btn;
	private TextView ditu_weather;
	private TextView ditu_tem;
	private ImageView surfacewater_pic;
	private Drawable[] checkbefor;
	private Drawable[] checkafter;
	private TextView[] checktextViews;
	private ProgressBar jindu_progressbar;
	Dialog dialog;
	private TextView update_time;
	private ImageView change_map;
	private LinearLayout change_map_layout;
	private ImageView change_map_normol;
	private ImageView change_map_weixing;
	private BaiduMap mBaiduMap;  

	/**
	 * 配合timer事件，延迟至一定时间后再进行渲染，在往地图添加aqi点时执行该函数。
	 */
	private void renderAQI() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Auto-generated method stub

		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.map_miann_activity);
			context = this;
			dialog = CommonUtil.getCustomeDialog(this, R.style.load_dialog,
					R.layout.custom_progress_dialog);
			dialog.setCanceledOnTouchOutside(false);
			initBitMap();
			checkbefor = new Drawable[] {
					getResources().getDrawable(R.drawable.map_air_default),
					getResources().getDrawable(R.drawable.map_water_default),
					getResources().getDrawable(R.drawable.map_weather_default),
					getResources().getDrawable(R.drawable.map_temp_default) };
			checkafter = new Drawable[] {
					getResources().getDrawable(R.drawable.map_air_press),
					getResources().getDrawable(R.drawable.map_water_press),
					getResources().getDrawable(R.drawable.map_weather_press),
					getResources().getDrawable(R.drawable.map_temp_press) };
			initView();// 初始化右侧一排按钮。。

			mCityDB = WeiBaoApplication.getInstance().getCityDB();
			// initView();// 初始化右侧一排按钮。。。
			mMapView = (MapView) findViewById(R.id.bmapView);
			MyLog.i(">>>>>>>>>>>>>>mappoi" + "1");
			mBaiduMap= mMapView.getMap();
			mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub
					MyLog.i(">>>>>>>>>>>>>>mappoi" + arg0.latitude + ">>>>"
							+ arg0.longitude);

				}
			});
			mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			update_time = (TextView) findViewById(R.id.update_time);
			MapStatus ms = new MapStatus.Builder().zoom(12).build();
			mMapView.getMap().setMapStatus(
					MapStatusUpdateFactory.newMapStatus(ms));

			mapLengend = (ImageView) findViewById(R.id.map_lengend);
			city = WeiBaoApplication.selectedCity; // app.selectedCity;
			/**
			 * 获取地图控制器
			 */
			// mMapController = mMapView.getController();

			/**
			 * 设置地图是否响应点击事件 .
			 */
			// mMapController.enableClick(true);

			/**
			 * 设置地图缩放级别
			 */
			// mMapController.setZoom(11);
			mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomTo(9));
			/**
			 * 显示内置缩放控件
			 */
			// mMapView.setBuiltInZoomControls(false);
			mMapView.showZoomControls(false);
//			mMapView.getMap().setOnMapClickListener(new OnMapClickListener() {
//
//				@Override
//				public boolean onMapPoiClick(MapPoi arg0) {
//					// TODO Auto-generated method stub
//					MyLog.i(">>>>>>>>>>>>>>>>hghhssh" + "3");
//					return false;
//				}
//
//				@Override
//				public void onMapClick(LatLng arg0) {
//					// TODO Auto-generated method stub
//					MyLog.i(">>>>>>>>>>>>>>>>hghhssh" + "2");
//
//				}
//			});
			// mMapView.getController().setCompassMargin(WbMapUtil.dip2px(this,
			// 40),
			// WbMapUtil.dip2px(this, 100));

			// pop = new PopupOverlay(mMapView, new PopupClickListener() {
			//
			// @Override
			// public void onClickedPopup(int arg0) {
			//
			// }
			// });

			// myPictureOverlay = new PictureOverlay(
			// getResources().getDrawable(R.drawable.icon_markf), mMapView);
			// wOL_PROVINCE = new MyOverlay(getResources().getDrawable(
			// R.drawable.icon_markf), mMapView);
			// wOL_MAINCITY = new MyOverlay(getResources().getDrawable(
			// R.drawable.icon_markf), mMapView);
			// wOL_CITY = new MyOverlay(getResources().getDrawable(
			// R.drawable.icon_markf), mMapView);
			// aOL_CITY = new MyOverlay(getResources().getDrawable(
			// R.drawable.icon_markf), mMapView);
			// aOL_POINT = new MyOverlay(getResources().getDrawable(
			// R.drawable.icon_markf), mMapView);
			// ltOverlay = new MyOverlay_LTouch(getResources().getDrawable(
			// R.drawable.icon_markf), mMapView);
			// mMapView.getOverlays().add(ltOverlay);
			// poioverlay = new PoiOverlay(MapMianActivity.this, mMapView);
			// renderGroundOverlay = new GroundOverlay(mMapView);
			// mMapView.getOverlays().add(renderGroundOverlay);

			// aqiRendertimer.schedule(task, 500, 500);

			mhandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {

					switch (msg.what) {
					case -1:
						break;// 获取图片失败
					case 1:// air 空气质量
							// Bitmap mbitmap = (Bitmap) msg.obj;
							//
							// if(mbitmap==null)
							// {
							// }
							// if(getBitmapByKey("air")==null)
							// {
							// addBitmapToCache("air", mbitmap);
							// }
							// if(tc.getEmun_envtype()==EnvTypeOnMapEnum.NULL)
							// showWholeCountry("air");
						break;
					case 2:// temp 温度
						Bitmap tbitmap = (Bitmap) msg.obj;
						if (tbitmap == null) {
						}
						if (getBitmapByKey("temp") == null) {
							addBitmapToCache("temp", tbitmap);
						}
						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.QIWEN) {
							showWholeCountry("temp");
						}

						break;
					case 3:// fog
						Bitmap fbitmap = (Bitmap) msg.obj;
						if (fbitmap == null) {
						}
						if (getBitmapByKey("fog") == null) {
							addBitmapToCache("fog", fbitmap);
						}

						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.WU) {
							showWholeCountry("fog");
						}
						break;
					case 4:// haze
						Bitmap hbitmap = (Bitmap) msg.obj;
						if (hbitmap == null) {
						}
						if (getBitmapByKey("haze") == null) {
							addBitmapToCache("haze", hbitmap);
						}

						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.MAI) {
							showWholeCountry("haze");
						}
						break;
					}
					/** 将地图定位绽放到全国 **/
					// LatLng pt = new LatLng(40.0, 100.0);//取了个大概的中心点，中国的中心点
					// MapStatus ms = new MapStatus.Builder()
					// .zoom(5)
					// .target(pt).build();
					// mMapView.getMap().animateMapStatus(
					// MapStatusUpdateFactory.newMapStatus(ms)
					// );

					// MKMapStatus mms = new MKMapStatus(5, 0, 0, pt, null);
					// mMapView.getController().setMapStatusWithAnimation(mms);
				}

			};

			/**
			 * 实例化mainHandler
			 */
			mainHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {

					// -------------------------------
					// 接收子线程过来的消息
					// 消息内容：
					//
					// -------------------------------

					switch (msg.what) {
					case MESSAGE_EXIT:
						// isExit = false;
						break;
					case 1: // 设置指南针的方向
						break;
					case 2: // 添加省会城市天气地图点

						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.TIANQI) {
							List<Province> provinces = (List<Province>) msg.obj;
							List<MarkerOptions> province_items = new ArrayList<MarkerOptions>();// 存储处理完的OverlayItem集合，显示到地图上的点信息
							for (int i = 0; i < provinces.size(); i++) {
								if (provinces.get(i).getLocation() == null) {
									continue;
								}

								// OverlayItem item = new
								// OverlayItem(provinces.get(i).getLocation(),
								// "wOL_PROVINCE", provinces.get(i).getCity());
								// Drawable marker = getResources().getDrawable(
								// getWeatherMarker(provinces.get(i).getWeather()));
								//
								// item.setMarker(marker);
								// province_items.add(item);
								// 定义Maker坐标点
								LatLng point = provinces.get(i).getLocation();

								Bitmap aqimakertext = LayoutToBitmap(
										R.layout.map_hbdt_point_drawabletext,
										provinces.get(i).getCity(),
										getResources().getDrawable(
												getWeatherMarker(provinces.get(
														i).getWeather())));

								// 构建Marker图标
								// BitmapDescriptor bitmap =
								// BitmapDescriptorFactory
								// .fromResource(getWeatherMarker(provinces.get(i).getWeather()));
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromBitmap(aqimakertext);
								// 构建MarkerOption，用于在地图上添加Marker
								OverlayOptions option = new MarkerOptions()
										.position(point)
										.title("wOL_PROVINCE" + "-"
												+ provinces.get(i).getCity())
										.icon(bitmap);
								// 在地图上添加Marker，并显示
								mMapView.getMap().addOverlay(option);
							}
							// wOL_PROVINCE.removeAll();
							// wOL_PROVINCE.addItem(province_items);
							// mMapView.refresh();
						}

						break;
					case 3: // 地图长按事件消息通道
						MyLog.i("地图长按事件消息通道");

						// 弹出一个新面板

						// longclickpopwin = new MapLongclickPopupWindow(
						// MapMianActivity.this, longclickpanellistener);
						// longclickpopwin.showAtLocation(MapMianActivity.this
						// .findViewById(R.id.hbdtlayout), Gravity.BOTTOM
						// | Gravity.CENTER_HORIZONTAL, 0, 0);

						break;
					case 4:// 地图点击事件
							// 清除掉longtouch的点
							// ltOverlay.removeAll();
						// mMapView.getOverlays().remove(ltOverlay);
						// //
						removeDragView();
						break;
					case 5:// 添加地级市城市天气点
						Log.v("function", "case 5 添加地级城市天气");
						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.TIANQI) {
							List<City> maincitys = (List<City>) msg.obj;
							List<MarkerOptions> maincity_items = new ArrayList<MarkerOptions>();// 存储处理完的OverlayItem集合，显示到地图上的点信息
							for (int i = 0; i < maincitys.size(); i++) {
								if (maincitys.get(i).getLocation() == null) {
									continue;
								}
								// OverlayItem mcitem = new OverlayItem(
								// maincitys.get(i).getLocation(),
								// "wOL_MAINCITY",
								// maincitys.get(i).getName());
								// Drawable mcmarker =
								// getResources().getDrawable(
								// getWeatherMarker(maincitys.get(i).getWeather()));
								// mcitem.setMarker(mcmarker);
								// maincity_items.add(mcitem);

								// 定义Maker坐标点
								LatLng point = maincitys.get(i).getLocation();

								Bitmap aqimakertext = LayoutToBitmap(
										R.layout.map_hbdt_point_drawabletext,
										maincitys.get(i).getName(),
										getResources().getDrawable(
												getWeatherMarker(maincitys.get(
														i).getWeather())));

								// 构建Marker图标
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromBitmap(aqimakertext);
								// .fromResource(getWeatherMarker(maincitys.get(i).getWeather()));
								// 构建MarkerOption，用于在地图上添加Marker
								OverlayOptions option = new MarkerOptions()
										.position(point)
										.title("wOL_MAINCITY" + "-"
												+ maincitys.get(i).getName())
										.icon(bitmap);
								// 在地图上添加Marker，并显示
								mMapView.getMap().addOverlay(option);
							}

							// wOL_MAINCITY.removeAll();
							// wOL_MAINCITY.addItem(maincity_items);
							// mMapView.refresh();
						}

						break;
					case 6:
					case 15:
					case 19:
					case 20:
					case 21:
					case 22:
					case 23:
					case 24:
					case 40:// 在地图上显示城市监测值
						if (envTypeIsAQIType(tc.getEmun_envtype())) {

							List<AQIPoint> citys = (List<AQIPoint>) msg.obj;
							MyLog.i(">>>>>>>>aqiqghghpint" + citys.size());
							if (citys == null) {
								return;
							}
							List<MarkerOptions> aqicityitems = new ArrayList<MarkerOptions>();
							for (int i = 0; i < citys.size(); i++) {
								AQIPoint aqipt = citys.get(i);

								// OverlayItem aqiitem = new OverlayItem(new
								// GeoPoint(
								// aqipt.getWeidu(), aqipt.getJingdu()),
								// "aOL_CITY",
								// aqipt.getJiancedian());
								// aqiitem.setAnchor((float)0,(float)1);
								// 1
								Drawable aqimaker = getResources().getDrawable(
										getAQIMarker(aqipt));
								Drawable aqimakertext = null;
								if (mMapView.getMap().getMapStatus().zoom < 8f)// 如果地图范围太大，点太多就不显示文字了
								{
									// aqimakertext = aqimaker;
									aqimakertext = LayoutToDrawableMapDengJi(
											R.layout.map_dengji_layout,
											aqipt.getJiancedian(),
											(getIAQIvalue(aqipt) + "").replace(
													".0", ""), aqimaker);

								} else {
									// aqimakertext = LayoutToDrawable(
									// R.layout.map_hbdt_point_drawabletext,
									// aqipt.getJiancedian() + "("
									// + (int) getIAQIvalue(aqipt)
									// + ")", aqimaker);
									aqimakertext = LayoutToDrawableMapDengJi(
											R.layout.map_dengji_layout,
											aqipt.getJiancedian(),
											(getIAQIvalue(aqipt) + "").replace(
													".0", ""), aqimaker);

								}

								// aqiitem.setMarker(aqimakertext);
								// aqicityitems.add(aqiitem);

								// 定义Maker坐标点
								LatLng point = new LatLng(aqipt.getWeidu(),
										aqipt.getJingdu());
								// 构建Marker图标
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromBitmap(((BitmapDrawable) aqimakertext)
												.getBitmap());
								// 构建MarkerOption，用于在地图上添加Marker
								OverlayOptions option;
								if (msg.what != 40) {
									option = new MarkerOptions()
											.position(point)
											.title("aOL_CITY" + "-"
													+ aqipt.getJiancedian()
													+ "-" + getIAQIvalue(aqipt))
											.icon(bitmap);
								} else {
									option = new MarkerOptions()
											.position(point)
											.title("aOL_JIANCEZHAN" + "-"
													+ aqipt.getJiancedian()
													+ "-" + getIAQIvalue(aqipt))
											.icon(bitmap);
								}

								// OverlayOptions option = new MarkerOptions()
								// .position(point)
								// .title("aOL_CITY" + "-"
								// + aqipt.getJiancedian())
								// .icon(bitmap);
								// 在地图上添加Marker，并显示
								mMapView.getMap().addOverlay(option);
							}
							// if (null != dialog && dialog.isShowing()) {
							// dialog.dismiss();
							// }
							// aOL_CITY.removeAll();
							// aOL_CITY.addItem(aqicityitems);
							//
							// mMapView.refresh();
							renderAQI();
						}

						break;
					case 29:
						jindu_progressbar.setVisibility(View.VISIBLE);
						break;
					case 30:
						jindu_progressbar.setVisibility(View.GONE);
						break;
					case 50:
						update_time.setText((String) msg.obj);
						break;
					case 7:// 添加县级市城市天气点
						Log.v("function", "case 7 添加县级城市天气");
						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.TIANQI) {
							Log.v("function", "case 7 添加县级城市天气-2");
							List<City> citys = (List<City>) msg.obj;
							Log.v("function",
									"case 7 添加县级城市天气-size-" + citys.size());
							List<MarkerOptions> city_items = new ArrayList<MarkerOptions>();// 存储处理完的OverlayItem集合，显示到地图上的点信息
							for (int i = 0; i < citys.size(); i++) {
								if (citys.get(i).getLocation() == null) {
									continue;
								}
								// OverlayItem citem = new OverlayItem(
								// citys.get(i).getLocation(), "wOL_CITY",
								// citys.get(i).getName());
								// Drawable cmarker =
								// getResources().getDrawable(
								// getWeatherMarker(citys.get(i).getWeather()));
								// citem.setMarker(cmarker);
								// city_items.add(citem);

								// 定义Maker坐标点
								LatLng point = citys.get(i).getLocation();

								Bitmap aqimakertext = LayoutToBitmap(
										R.layout.map_hbdt_point_drawabletext,
										citys.get(i).getName(),
										getResources().getDrawable(
												getWeatherMarker(citys.get(i)
														.getWeather())));

								// 构建Marker图标
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromBitmap(aqimakertext);
								// .fromResource(getWeatherMarker(citys.get(i).getWeather()));
								// 构建MarkerOption，用于在地图上添加Marker
								OverlayOptions option = new MarkerOptions()
										.position(point)
										.title("wOL_CITY" + "-"
												+ citys.get(i).getName())
										.icon(bitmap);
								// 在地图上添加Marker，并显示
								mMapView.getMap().addOverlay(option);
							}

							// Log.v("function", "case 7 添加县级城市天气-3");
							//
							//
							// wOL_CITY.removeAll();
							// wOL_CITY.addItem(city_items);
							// mMapView.refresh();
						}

						break;
					case 8:// 在地图上显示具体AQI监测点

						if (envTypeIsAQIType(tc.getEmun_envtype())) {

							List<AQIPoint> points = (List<AQIPoint>) msg.obj;

							List<MarkerOptions> aqiptitems = new ArrayList<MarkerOptions>();

							for (int i = 0; i < points.size(); i++) {
								AQIPoint aqipt = points.get(i);
								try {
									MyLog.i("baiMap AQIPoint :"
											+ aqipt.toString());
								} catch (Exception e) {
									e.printStackTrace();
								}

								if (null != aqipt.getAqi()) {

									Drawable aqimaker = getResources()
											.getDrawable(getAQIMarker(aqipt));
									// Drawable
									// aqimakertext=LayoutToDrawable(R.layout.map_hbdt_point_drawabletext,aqipt.getJiancedian(),aqimaker);

									Drawable aqimakertext = null;
									if (mMapView.getMap().getMapStatus().zoom < 8f)// 如果地图范围太大，点太多就不显示文字了
									{
										// aqimakertext = aqimaker;
										aqimakertext = LayoutToDrawableMapDengJi(
												R.layout.map_dengji_layout,
												aqipt.getJiancedian(),
												(getIAQIvalue(aqipt) + "")
														.replace(".0", ""),
												aqimaker);
									} else {
										// aqimakertext = LayoutToDrawable(
										// R.layout.map_hbdt_point_drawabletext,
										// aqipt.getJiancedian()
										// + "("
										// + (int) getIAQIvalue(aqipt)
										// + ")", aqimaker);
										// aqimakertext =
										// LayoutToDrawableMapDengJi(R.layout.map_dengji_layout,
										// getIAQIvalue(aqipt)+"", aqimaker);
										aqimakertext = LayoutToDrawableMapDengJi(
												R.layout.map_dengji_layout,
												aqipt.getJiancedian(),
												(getIAQIvalue(aqipt) + "")
														.replace(".0", ""),
												aqimaker);
									}

									// 定义Maker坐标点
									LatLng point = new LatLng(aqipt.getWeidu(),
											aqipt.getJingdu());
									// 构建Marker图标
									BitmapDescriptor bitmap = BitmapDescriptorFactory
											.fromBitmap(((BitmapDrawable) aqimakertext)
													.getBitmap());
									// 构建MarkerOption，用于在地图上添加Marker
									OverlayOptions option = new MarkerOptions()
											.position(point).icon(bitmap);
									// 在地图上添加Marker，并显示
									mMapView.getMap().addOverlay(option);

								}
							}
							renderAQI();

						}

						break;
					case 9:// 调用setPointLayer()
						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.SOURCE_OF_POLLUTION) {
							return;
						}
						setPointLayer();
						break;
					case 10:// onmapmovefinshed
						if (tc.getEmun_envtype() == EnvTypeOnMapEnum.SHARE_PICTURE) {
							getmappictures();
						}

						break;
					case 11:
						Log.v("function-render", "case 11 add ground");
						OverlayOptions ground = (OverlayOptions) msg.obj;
						if (ground == null) {
							break;
						}
						mMapView.getMap().addOverlay(ground);
						// renderGroundOverlay.remove();

						// renderGroundOverlay=new GroundOverlay(mMapView);
						// renderGroundOverlay.addGround(ground);

						// mMapView.getOverlays().add(renderGroundOverlay);
						// mMapView.refresh();
						break;
					case 101:
						tc.mapzoomFinish();
						break;
					case 201:// 循环执行截屏

						Bitmap bitmap = (Bitmap) msg.obj;

						mSocialShare = Frontia.getSocialShare();
						mSocialShare.setContext(MapMianActivity.this);
						mSocialShare.setClientId(MediaType.WEIXIN.toString(),
								"wx541df6ed6e9babc0");
						mSocialShare.setClientId(
								MediaType.SINAWEIBO.toString(), "991071488");
						mSocialShare.setClientId(MediaType.QQFRIEND.toString(),
								"100358052");
						mSocialShare.setParentView(getWindow().getDecorView());
						mSocialShare.setClientName(
								MediaType.QQFRIEND.toString(), "空气质量");
						mImageContent.setTitle("空气质量");
						mImageContent.setContent("空气质量地图，口袋里的环保专家！");
						mImageContent.setLinkUrl("http://www.wbapp.com.cn/");
						// mImageContent.setImageUri(uri);
						mImageContent
								.setWXMediaObjectType(FrontiaIMediaObject.TYPE_IMAGE);
						mImageContent
								.setQQRequestType(FrontiaIQQReqestType.TYPE_IMAGE);
						mImageContent
								.setQQFlagType(FrontiaIQQFlagType.TYPE_DEFAULT);
						mImageContent.setImageData(bitmap);

						mSocialShare.show(MapMianActivity.this.getWindow()
								.getDecorView(), mImageContent,
								FrontiaTheme.LIGHT, new ShareListener());
						break;
					case SURFACE_WATER_HANDLER_SEVER_TAG:// 在地图上显示具体地表水监测点
						MyLog.i("SURFACE_WATER_HANDLER_SEVER_TAG");
						try {
							if (tc.getEmun_envtype() == EnvTypeOnMapEnum.SURFACE_WATER) {
								List<SurfaceWaterModel> points = (List<SurfaceWaterModel>) msg.obj;
								if (null == points || points.size() == 0) {
									return;
								}
								for (int i = 0; i < points.size(); i++) {
									SurfaceWaterModel aqipt = points.get(i);
									try {
										MyLog.i("baiMap AQIPoint :"
												+ aqipt.toString());
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (null != aqipt.getMow_quality()) {
										// 定义Maker坐标点
										double lng = 0;
										double lon = 0;
										try {
											lng = Double.parseDouble(aqipt
													.getBaidu_lng_d());
											lon = Double.parseDouble(aqipt
													.getBaidu_lat_d());
										} catch (Exception e) {
											// TODO: handle exception
										}
										LatLng point = new LatLng(lon, lng);
										MyLog.i("point" + point.toString());

										// Drawable aqimaker =
										// getSurfaceWaterMarkerId(aqipt.getMow_quality());
										// Drawable aqimakertext = null;
										// if
										// (mMapView.getMap().getMapStatus().zoom
										// < 8f)// 如果地图范围太大，点太多就不显示文字了
										// {
										// aqimakertext = aqimaker;
										// } else {
										// aqimakertext = LayoutToDrawable(
										// R.layout.map_hbdt_point_drawabletext,
										// aqipt.getC_river(), aqimaker);
										// }
										// // 构建Marker图标
										// BitmapDescriptor bitmapWater =
										// BitmapDescriptorFactory
										// .fromBitmap(((BitmapDrawable)
										// aqimakertext)
										// .getBitmap());
										// // 构建MarkerOption，用于在地图上添加Marker
										// OverlayOptions options = new
										// MarkerOptions()
										// .position(point).icon(bitmapWater);
										// // 在地图上添加Marker，并显示
										// mMapView.getMap().addOverlay(options);
										Bundle bundle = new Bundle();
										bundle.putString("river",
												aqipt.getC_river());
										bundle.putString("water",
												aqipt.getC_water());
										bundle.putString("monitor",
												aqipt.getMonitor_point());
										bundle.putString("time",
												aqipt.getR_time());

										BitmapDescriptor water = BitmapDescriptorFactory
												.fromBitmap(getSurfaceWaterMarker(aqipt
														.getMow_quality()));
										// 构建MarkerOption，用于在地图上添加Marker
										OverlayOptions option = new MarkerOptions()
												.position(point).icon(water)
												.extraInfo(bundle);
										// 在地图上添加Marker，并显示
										mMapView.getMap().addOverlay(option);
									}

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case SURFACE_WATER_HANDLER_OLD_SQL_TAG:// 在地图上显示具体AQI监测点
						try {
							MyLog.i("SURFACE_WATER_HANDLER_OLD_SQL_TAG");
							if (tc.getEmun_envtype() == EnvTypeOnMapEnum.SURFACE_WATER) {
								List<SurfaceWaterModel> points = (List<SurfaceWaterModel>) msg.obj;
								if (null == points || points.size() == 0) {
									return;
								}
								for (int i = 0; i < points.size(); i++) {
									SurfaceWaterModel aqipt = points.get(i);
									try {
										MyLog.i("baiMap AQIPoint :"
												+ aqipt.toString());
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (null != aqipt.getMow_quality()) {
										// 定义Maker坐标点
										double lng = 0;
										double lon = 0;
										try {
											lng = Double.parseDouble(aqipt
													.getBaidu_lng_d());
											lon = Double.parseDouble(aqipt
													.getBaidu_lat_d());
										} catch (Exception e) {
											// TODO: handle exception
										}
										LatLng point = new LatLng(lon, lng);
										MyLog.i("point" + point.toString());
										Bundle bundle = new Bundle();
										bundle.putString("river",
												aqipt.getC_river());
										bundle.putString("water",
												aqipt.getC_water());
										bundle.putString("monitor",
												aqipt.getMonitor_point());
										bundle.putString("time",
												aqipt.getR_time());
										BitmapDescriptor water = BitmapDescriptorFactory
												.fromBitmap(getSurfaceWaterMarker(aqipt
														.getMow_quality()));
										// 构建MarkerOption，用于在地图上添加Marker
										OverlayOptions option = new MarkerOptions()
												.position(point).icon(water)
												.extraInfo(bundle);
										// 在地图上添加Marker，并显示
										mMapView.getMap().addOverlay(option);
									}

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					case POLLUTION_HANDLER_SEVER_TAG:// 在地图上显示具体AQI监测点
						try {
							MyLog.i("POLLUTION_HANDLER_SEVER_TAG"
									+ tc.getEmun_envtype());
							if (tc.getEmun_envtype() == EnvTypeOnMapEnum.SOURCE_OF_POLLUTION) {
								List<PollutionPointModel> points = (List<PollutionPointModel>) msg.obj;
								MyLog.i("POLLUTION_HANDLER_SEVER_TAG points+ "
										+ points.size());
								if (null == points || points.size() == 0) {
									return;
								}
								for (int i = 0; i < points.size(); i++) {
									PollutionPointModel aqipt = points.get(i);
									// try {
									// MyLog.i("baiMap AQIPoint :" +
									// aqipt.toString());
									// } catch (Exception e) {
									// e.printStackTrace();
									// }
									if (null != aqipt.getUsid()) {
										// 定义Maker坐标点
										double lng = 0;
										double lon = 0;
										try {
											lng = Double.parseDouble(aqipt
													.getLng());
											lon = Double.parseDouble(aqipt
													.getLat());
										} catch (Exception e) {
											// TODO: handle exception
										}
										LatLng point = new LatLng(lon, lng);
										Bundle bundle = new Bundle();
										bundle.putString("address",
												aqipt.getAddress());
										bundle.putString("city",
												aqipt.getCity());
										bundle.putString("district",
												aqipt.getDistrict());
										bundle.putString("name",
												aqipt.getName());
										bundle.putString("type",
												aqipt.getType());
										BitmapDescriptor water = BitmapDescriptorFactory
												.fromBitmap(pollutionPointBitmap);
										// 构建MarkerOption，用于在地图上添加Marker
										OverlayOptions option = new MarkerOptions()
												.position(point).icon(water)
												.extraInfo(bundle);
										// 在地图上添加Marker，并显示
										mMapView.getMap().addOverlay(option);
									}

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case POLLUTION_HANDLER_OLD_SQL_TAG:// 在地图上显示具体AQI监测点
						try {
							MyLog.i("POLLUTION_HANDLER_OLD_SQL_TAG"
									+ tc.getEmun_envtype());
							if (tc.getEmun_envtype() == EnvTypeOnMapEnum.SOURCE_OF_POLLUTION) {
								List<PollutionPointModel> points = (List<PollutionPointModel>) msg.obj;
								MyLog.i("POLLUTION_HANDLER_OLD_SQL_TAG points+ "
										+ points.size());
								if (null == points || points.size() == 0) {
									return;
								}
								for (int i = 0; i < points.size(); i++) {
									PollutionPointModel aqipt = points.get(i);
									// try {
									// MyLog.i("baiMap PollutionPointModel :" +
									// aqipt.toString());
									// } catch (Exception e) {
									// e.printStackTrace();
									// }
									if (null != aqipt.getUsid()) {
										// 定义Maker坐标点
										double lng = 0;
										double lon = 0;
										try {
											lng = Double.parseDouble(aqipt
													.getLng());
											lon = Double.parseDouble(aqipt
													.getLat());
										} catch (Exception e) {
											// TODO: handle exception
										}
										LatLng point = new LatLng(lon, lng);
										Bundle bundle = new Bundle();
										bundle.putString("address",
												aqipt.getAddress());
										bundle.putString("city",
												aqipt.getCity());
										bundle.putString("district",
												aqipt.getDistrict());
										bundle.putString("name",
												aqipt.getName());
										bundle.putString("type",
												aqipt.getType());
										BitmapDescriptor water = BitmapDescriptorFactory
												.fromBitmap(pollutionPointBitmap);
										// 构建MarkerOption，用于在地图上添加Marker
										OverlayOptions option = new MarkerOptions()
												.position(point).icon(water)
												.extraInfo(bundle);
										// 在地图上添加Marker，并显示
										mMapView.getMap().addOverlay(option);
									}

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					default:
						break;
					}
				}

			};

			/**
			 * 添加地图渲染显示管理器 -------shang
			 */
			tc = new TileControl(this, mMapView, mainHandler);

			centerMapToCity(city);
			SleepAstask slp = new SleepAstask();
			slp.execute("");
			// mMapView.getCurrentMap();
			// 定位初始化
			mLocClient = WeiBaoApplication.getInstance().getLocationClient();
			// LocationClientOption option = new LocationClientOption();
			// option.setOpenGps(true);
			// option.setAddrType("all");
			// option.setServiceName(WeiBaoApplication.getInstance().getPackageName());
			//
			// option.disableCache(true);
			// mLocClient = new LocationClient(MapMianActivity.this, option);
			locData = new MyLocationData.Builder().build();
			mLocClient.registerLocationListener(mLocationListener);

			// 定位图层初始化
			// myLocationOverlay = new MyLocationOverlay(mMapView);
			// 设置定位数据
			// myLocationOverlay.setData(locData);
			// 添加定位图层
			// mMapView.getOverlays().add(myLocationOverlay);

			// myLocationOverlay.enableCompass();
			// 修改定位数据后刷新图层生效
			// mMapView.refresh();

			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
			iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
			mReceiver = new SDKReceiver();
			registerReceiver(mReceiver, iFilter);

		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
	}

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action
					.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				// key 验证失败，相应处理
			}
		}
	}

	/**
	 * 判断类型是否属于是AQI----O3中的一项，如果是则true,反之false
	 * 
	 * @param em
	 * @return
	 */
	private boolean envTypeIsAQIType(EnvTypeOnMapEnum em) {
		if (em == null) {
			return false;
		}
		switch (em) {
		case AQI:
		case CO:
		case NO2:
		case O3:
		case PM10:
		case PM25:
		case SO2:
			return true;

		}
		return false;
	}

	/**
	 * 添加标注的覆盖物
	 * 
	 * @param layout_id
	 * @return
	 */
	public Drawable LayoutToDrawableMapDengJi(int layout_id, String pointname,
			String aqi, Drawable pointdrawable) {
		LayoutInflater inflator = LayoutInflater.from(this);
		View viewHelp = inflator.inflate(layout_id, null);
		TextView textView = (TextView) viewHelp.findViewById(R.id.pointtext);
		TextView pointvalue = (TextView) viewHelp.findViewById(R.id.pointvalue);
		textView.setText(aqi);
		textView.setText(aqi);
		int intAqi = Integer.parseInt(aqi);
		if (intAqi <= 150) {
			textView.setTextColor(Color.parseColor("#000033"));
		}
		pointvalue.setText(pointname);
		textView.setBackgroundDrawable(pointdrawable);
		int size = (int) textView.getText().length();
		Drawable drawable = null;
		try {
			Bitmap snapshot = convertViewToBitmap(viewHelp, size);
			drawable = (Drawable) new BitmapDrawable(snapshot);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return drawable;
	}

	/**
	 * 添加标注的覆盖物
	 * 
	 * @param layout_id
	 * @return
	 */
	public Drawable LayoutToDrawable(int layout_id, String pointname,
			Drawable pointdrawable) {
		LayoutInflater inflator = LayoutInflater.from(this);
		View viewHelp = inflator.inflate(layout_id, null);
		TextView textView = (TextView) viewHelp.findViewById(R.id.pointtext);
		ImageView imageView = (ImageView) viewHelp.findViewById(R.id.display);
		textView.setText(pointname);
		imageView.setBackgroundDrawable(pointdrawable);
		int size = (int) textView.getText().length();
		Drawable drawable = null;
		try {
			Bitmap snapshot = convertViewToBitmap(viewHelp, size);
			drawable = (Drawable) new BitmapDrawable(snapshot);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return drawable;
	}

	/**
	 * 添加标注的覆盖物
	 * 
	 * @param layout_id
	 * @return
	 */
	public Bitmap LayoutToBitmap(int layout_id, String pointname,
			Drawable pointdrawable) {
		LayoutInflater inflator = LayoutInflater.from(this);
		View viewHelp = inflator.inflate(layout_id, null);
		TextView textView = (TextView) viewHelp.findViewById(R.id.pointtext);
		ImageView imageView = (ImageView) viewHelp.findViewById(R.id.display);
		textView.setText(pointname);
		imageView.setBackgroundDrawable(pointdrawable);
		int size = (int) textView.getText().length();
		Bitmap snapshot = convertViewToBitmap(viewHelp, size);
		return snapshot;
	}

	/**
	 * 添加标注的覆盖物
	 * 
	 * @param layout_id
	 * @return
	 */
	private Bitmap sharePicureLayoutToDrawable(int layout_id, Drawable img) {
		LayoutInflater inflator = LayoutInflater.from(this);
		View viewHelp = inflator.inflate(layout_id, null);

		ImageView imageView = (ImageView) viewHelp
				.findViewById(R.id.sharepicture);

		imageView.setImageDrawable(img);

		viewHelp.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		viewHelp.layout(0, 0, WbMapUtil.dip2px(this, 44),
				WbMapUtil.dip2px(this, 44));

		viewHelp.buildDrawingCache();
		viewHelp.setDrawingCacheEnabled(true);
		Bitmap snapshot = viewHelp.getDrawingCache();
		// viewHelp.setDrawingCacheEnabled(false);
		// Bitmap snapshot = convertViewToBitmap(viewHelp, size);
		// Drawable drawable = (Drawable)new BitmapDrawable(snapshot);
		return snapshot;
	}

	/**
	 * 将加标注的覆盖物的View转成BitMap
	 * 
	 * @param view
	 * @param size
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view, int size) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int width = size * 100;
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight()); // 根据字符串的长度显示view的宽度
		view.buildDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
			final Bitmap photo = data.getParcelableExtra("data");
			// 下面就是显示照片了

			break;
		}
		case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			doCropPhoto(mCurrentPhotoFile);

			break;
		}
		case CHARE_DATA: {// 分享完图片后回调刷新
			if (dv != null) {
				dv.UpdateDragView(dv.get_lon(), dv.get_lat());
			}
			break;
		}
		// case MAP_SEACH: {// 搜索完成后回调
		// if (data != null) {
		// int l = data.getIntExtra("type", 0);
		// switch (l) {
		// case 1:
		// search_type = 1;
		//
		// break;
		// case 2:
		// search_type = 2;
		//
		// break;
		//
		// }
		//
		//
		// }
		// break;
		// }
		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		// 注销地图事件相关的广播
		unregisterReceiver(mReceiver);
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		mMapView.onDestroy();
	}

	@Override
	protected void onPause() {

		super.onPause();
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		// changeEnvInfoOnMapByType(EnvTypeOnMapEnum.NULL);
		mMapView.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();

	}

	@Override
	protected void onResume() {

		super.onResume();

		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		mMapView.onResume();
		mMapView.getMap().hideInfoWindow();
		// tc.mapOnResume = true;

		// // 更新定位到的城市
		// if (mCityDB != null && city != WeiBaoApplication.selectedCity) {
		//
		// // city = mCityDB.getRealtimeCityName();
		city = WeiBaoApplication.selectedCity; // app.selectedCity;
		// centerMapToCity(city);
		// mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomTo(11));
		// if(TileControl._stationResult!=null&&TileControl._stationResult.size()>0)
		// {
		// mMapView.getMap().clear();
		// Message toMapActivity = mainHandler.obtainMessage();
		// toMapActivity.what = 6;
		// toMapActivity.obj = TileControl._stationResult;
		// mainHandler.sendMessage(toMapActivity);
		// }
		changeEnvInfoOnMapByType(EnvTypeOnMapEnum.AQI);

		// } else {
		// if (TileControl.pollType != null
		// // && tc.pointType != TileControl.POINT_TYPE_NULL)// 第一次打开地图时跳过
		// && tc.getEmun_envtype() != EnvTypeOnMapEnum.NULL)// 第一次打开地图时跳过
		// {
		// // tc.ShowTileOnMap(TileControl.pollType,
		// // TileControl.renderAQIPoints);
		// }
		// }
		//

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// mMapView.onRestoreInstanceState(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// mMapView.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	/**
	 * 注册相关按钮控件的点击事件
	 */
	private void initView() {

		// img_env_button = (ImageView) findViewById(R.id.mapbtn_env);
		// try {
		// img_env_button.setOnClickListener(this);
		// } catch (Exception e) {
		//
		// }
		// img_basemap_button = (ImageView) findViewById(R.id.mapbtn_basemap);
		// img_basemap_button.setOnClickListener(this);

		change_map_layout = (LinearLayout) findViewById(R.id.change_map_layout);
		change_map = (ImageView) findViewById(R.id.change_map);
		change_map_normol = (ImageView) findViewById(R.id.change_map_normol);
		change_map_weixing = (ImageView) findViewById(R.id.change_map_weixing);
		map_more_detail_id = (LinearLayout) findViewById(R.id.map_more_detail_id);
		surfacewater_pic = (ImageView) findViewById(R.id.surfacewater_pic);
		jindu_progressbar = (ProgressBar) findViewById(R.id.jindu_progressbar);
		ditu_water = (TextView) findViewById(R.id.ditu_water);
		ditu_water.setOnClickListener(this);
		map_quality_show_btn = (TextView) findViewById(R.id.map_quality_show_btn);
		map_quality_show_btn.setOnClickListener(this);
		map_weahter_show_btn = (TextView) findViewById(R.id.map_weahter_show_btn);
		map_weahter_show_btn.setOnClickListener(this);
		map_temperature_btn = (TextView) findViewById(R.id.map_temperature_btn);
		map_temperature_btn.setOnClickListener(this);

		ditu_weather = (TextView) findViewById(R.id.ditu_weather);
		ditu_weather.setOnClickListener(this);

		ditu_tem = (TextView) findViewById(R.id.ditu_tem);
		ditu_tem.setOnClickListener(this);

		aqi_show_btn = (TextView) findViewById(R.id.aqi_show_btn);
		aqi_show_btn.setOnClickListener(this);

		source_pollution_show_btn = (TextView) findViewById(R.id.source_pollution_show_btn);
		source_pollution_show_btn.setOnClickListener(this);

		// map_more_show_btn = (TextView) findViewById(R.id.map_more_show_btn);
		// map_more_show_btn.setOnClickListener(this);

		img_envclear_button = (TextView) findViewById(R.id.mapbtn_envclear);
		img_envclear_button.setOnClickListener(this);

		map_panel_env = (RelativeLayout) findViewById(R.id.mapactivity_envbtn_panel);

		envpanel_back = (View) findViewById(R.id.envpanel_backclick);
		envpanel_back.setOnClickListener(this);

		envpanel_close_button = (ImageView) findViewById(R.id.hbdtactivity_envpanel_close_button);
		envpanel_close_button.setOnClickListener(this);
		checktextViews = new TextView[] { aqi_show_btn, map_quality_show_btn,
				map_weahter_show_btn, map_temperature_btn };
		img_location = (ImageView) findViewById(R.id.map_location_button);
		img_location.setOnClickListener(this);

		txt_button_aqi = (TextView) findViewById(R.id.envpanel_txt_aqi);
		txt_button_aqi.setOnClickListener(this);

		txt_button_pm25 = (TextView) findViewById(R.id.envpanel_txt_pm25);
		txt_button_pm25.setOnClickListener(this);

		txt_button_pm10 = (TextView) findViewById(R.id.envpanel_txt_pm10);
		txt_button_pm10.setOnClickListener(this);

		txt_button_so2 = (TextView) findViewById(R.id.envpanel_txt_so2);
		txt_button_so2.setOnClickListener(this);

		txt_button_no2 = (TextView) findViewById(R.id.envpanel_txt_no2);
		txt_button_no2.setOnClickListener(this);

		txt_button_co = (TextView) findViewById(R.id.envpanel_txt_co);
		txt_button_co.setOnClickListener(this);

		txt_button_o3 = (TextView) findViewById(R.id.envpanel_txt_o3);
		txt_button_o3.setOnClickListener(this);

		txt_button_tianqi = (TextView) findViewById(R.id.envpanel_txt_tianqi);
		txt_button_tianqi.setOnClickListener(this);

		txt_button_wu = (TextView) findViewById(R.id.envpanel_txt_wu);
		txt_button_wu.setOnClickListener(this);

		txt_button_mai = (TextView) findViewById(R.id.envpanel_txt_mai);
		txt_button_mai.setOnClickListener(this);

		txt_button_qiwen = (TextView) findViewById(R.id.envpanel_txt_qiwen);
		txt_button_qiwen.setOnClickListener(this);

		txt_button_jiaotong = (TextView) findViewById(R.id.envpanel_txt_jiaotongshikuang);
		txt_button_jiaotong.setOnClickListener(this);

		txt_button_sharepicture = (TextView) findViewById(R.id.envpanel_txt_fenxiangtupian);
		txt_button_sharepicture.setOnClickListener(this);

		cbox_showrender = (CheckBox) findViewById(R.id.envpanel_childtitle_air_checkbox);
		cbox_showrender.setOnClickListener(this);

		txt_mapEnvInfoTitle = (TextView) findViewById(R.id.map_titlepanel_text);

		// txt_dataupttime = (TextView)findViewById(R.id.mapdatatime);

		btn_mapzoomin = (Button) findViewById(R.id.map_zoomin);
		btn_mapzoomin.setOnClickListener(this);
		btn_mapzoomout = (Button) findViewById(R.id.map_zoomout);
		btn_mapzoomout.setOnClickListener(this);

		mapdiscription = (TextView) findViewById(R.id.hbdtactivity_discription);

		maplegendpanel = (RelativeLayout) findViewById(R.id.map_legend_panel);
		maplegendname = (TextView) findViewById(R.id.map_legend_name);
		maplegendtime = (TextView) findViewById(R.id.map_legend_time);

		// btn_maptraffic = (ImageView)findViewById(R.id.map_traffic);
		// btn_maptraffic.setOnClickListener(this);
		//
		// btn_mappicture = (ImageView)findViewById(R.id.map_picture_show);
		// btn_mappicture.setOnClickListener(this);

		hscroll_legend = (HorizontalScrollView) findViewById(R.id.map_legend_scroll);
		llay_legendinner = (LinearLayout) findViewById(R.id.map_legend_scrollinner);

		btn_mapoffline = (Button) findViewById(R.id.btn_map_offline);
		btn_mapoffline.setOnClickListener(this);

		maploading = (ImageView) findViewById(R.id.maploading);
		maploading.setBackgroundResource(R.drawable.map_loading);
		anim = (AnimationDrawable) maploading.getBackground();
		anim.setOneShot(false);
		change_map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(MapMianActivity.this, "MapStyleChange");
				if (change_map_layout.getVisibility() == View.VISIBLE) {
					change_map_layout.setVisibility(View.GONE);
				} else {
					change_map_layout.setVisibility(View.VISIBLE);
				}
			}
		});
		change_map_normol.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(MapMianActivity.this, "MapStyleNormal");
				mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
				change_map_layout.setVisibility(View.GONE);
				change_map_normol.setImageResource(R.drawable.map_img1_checked);
				change_map_weixing.setImageResource(R.drawable.map_img2);
				// change_map_normol.setBackgroundResource(R.drawable.map_img1_checked);
				// change_map_weixing.setBackgroundResource(R.drawable.map_img2);
			}
		});
		change_map_weixing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(MapMianActivity.this, "MapStyleSatelite");
				mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				change_map_layout.setVisibility(View.GONE);
				change_map_normol.setImageResource(R.drawable.map_img1);
				change_map_weixing
						.setImageResource(R.drawable.map_img2_checked);
			}
		});

		// private ImageView change_map;
		// private ImageView change_map_layout;
		// private ImageView change_map_normol;
		// private ImageView change_map_weixing;
	}

	/**
	 * 设置地图标题面板 的内容，最上方的标题面板
	 * 
	 * @param 环境类型
	 */
	private void setMapTitle(EnvTypeOnMapEnum em) {
		String title = "";
		switch (em) {
		case AQI:
			title = "AQI指数实时数据";
			break;
		case PM10:
			title = "大气PM10浓度实时数据";
			break;
		case PM25:
			title = "大气PM2.5浓度实时数据";
			break;
		case SO2:
			title = "大气SO2浓度实时数据";
			break;
		case NO2:
			title = "大气NO2浓度实时数据";
			break;
		case CO:
			title = "大气CO浓度实时数据";
			break;
		case O3:
			title = "大气O3浓度实时数据";
			break;
		case TIANQI:
			title = "全国天气实况";
			break;
		case WU:
			title = "全国雾预报图";
			break;
		case MAI:
			title = "全国霾预报图";
			break;
		case QIWEN:
			title = "全国气温预报图";
			break;
		case TRAFFIC:
			title = "交通实况";
			break;
		case SHARE_PICTURE:
			title = "用户分享照片";
			break;
		case NULL:
			title = "空气质量地图，展示全面的环境信息！";
			break;
		}
		txt_mapEnvInfoTitle.setText(title);
	}

	public void xunazhe(View view) {
		final Dialog dialog = new Dialog(MapMianActivity.this);

		View companyDialog = LayoutInflater.from(MapMianActivity.this).inflate(
				R.layout.dialog_wuranwlist, null);
		final ListView companyListView = (ListView) companyDialog
				.findViewById(R.id.companyListView);
		List<Map<String, String>> s = new ArrayList<Map<String, String>>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("wury", "aqi");
		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("wury", "pm2_5");
		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("wury", "SO2");
		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("wury", "NO2");
		s.add(data);
		s.add(data2);
		s.add(data3);
		s.add(data4);
		SimpleAdapter adapter = new SimpleAdapter(MapMianActivity.this, s,
				R.layout.map_wurw_item, new String[] { "wury" },
				new int[] { R.id.wury_textView });
		companyListView.setAdapter(adapter);
		companyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				dialog.cancel();

			}
		});
		dialog.setContentView(companyDialog);
		dialog.show();

	}

	private void check(int i) {
		for (int j = 0; j < 4; j++) {
			if (i == j) {
				Drawable checkAfterDrawable = checkafter[j];
				checkAfterDrawable.setBounds(
						checkAfterDrawable.getMinimumWidth(),
						checkAfterDrawable.getMinimumHeight(), 0, 0);
				checktextViews[j].setCompoundDrawables(null,
						checkAfterDrawable, null, null);
			} else {
				Drawable checkBeterDrawable = checkbefor[j];
				checkBeterDrawable.setBounds(
						checkBeterDrawable.getMinimumWidth(),
						checkBeterDrawable.getMinimumHeight(), 0, 0);
				checktextViews[j].setCompoundDrawables(null,
						checkBeterDrawable, null, null);
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// private TextView aqi_show_btn ;
		// private TextView source_pollution_show_btn ;
		// private TextView map_more_show_btn ;
		// private TextView ditu_water;
		// private TextView ditu_weather;
		// private TextView ditu_tem;
		case R.id.ditu_water:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.SURFACE_WATER);
			map_more_detail_id.setVisibility(View.GONE);
			// map_more_show_btn.setCompoundDrawablesWithIntrinsicBounds(
			// null,
			// this.getResources().getDrawable(
			// R.drawable.map_more_show_btn_style), null, null);
			break;
		case R.id.ditu_weather:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.TIANQI);
			map_more_detail_id.setVisibility(View.GONE);
			// map_more_show_btn.setCompoundDrawablesWithIntrinsicBounds(
			// null,
			// this.getResources().getDrawable(
			// R.drawable.map_more_show_btn_style), null, null);
			break;
		case R.id.ditu_tem:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.QIWEN);
			map_more_detail_id.setVisibility(View.GONE);
			// map_more_show_btn.setCompoundDrawablesWithIntrinsicBounds(
			// null,
			// this.getResources().getDrawable(
			// R.drawable.map_more_show_btn_style), null, null);
			break;
		case R.id.aqi_show_btn:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			// check(0);
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.AQI);
			break;
		case R.id.source_pollution_show_btn:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.SOURCE_OF_POLLUTION);
			break;
		case R.id.map_more_show_btn:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			if (View.GONE == map_more_detail_id.getVisibility()) {
				openMapMore();
			} else {
				closeMapMore();
			}

			break;
		case R.id.mapbtn_env:// 打开环境面板
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			openMapEnvPanel();
			break;
		case R.id.mapbtn_basemap:// 切换地图底图
			changemapstyle(basemaptype);
			break;
		case R.id.mapbtn_envclear:// 清除地图元素
			MobclickAgent.onEvent(MapMianActivity.this, "DTClear");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.NULL);
			break;
		case R.id.envpanel_backclick:// 点击环境信息面板黑色背景
			closeMapEnvPanel();
			break;
		case R.id.hbdtactivity_envpanel_close_button:// 点击环境信息面板关闭按钮
			closeMapEnvPanel();
			break;
		case R.id.map_location_button:// 地图定位
			MobclickAgent.onEvent(MapMianActivity.this, "DTLocation");
			showyourlocation();
			break;
		case R.id.envpanel_txt_aqi:// 点击环境信息面板AQI按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTAQI");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.AQI);
			break;
		case R.id.envpanel_txt_pm25:// 点击环境信息面板PM25按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTPM25");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.PM25);
			break;
		case R.id.envpanel_txt_pm10:// 点击环境信息面板PM10按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTPM10");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.PM10);
			break;
		case R.id.envpanel_txt_so2:// 点击环境信息面板SO2按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTSO2");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.SO2);
			break;
		case R.id.envpanel_txt_no2:// 点击环境信息面板NO2按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTNO2");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.NO2);
			break;
		case R.id.envpanel_txt_co:// 点击环境信息面板CO按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTCO");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.CO);
			break;
		case R.id.envpanel_txt_o3:// 点击环境信息面板O3按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTO3");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.O3);
			break;
		case R.id.envpanel_txt_tianqi:// 点击环境信息面板天气按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTWeather");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.TIANQI);
			break;
		case R.id.envpanel_txt_wu:// 点击环境信息面板雾按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTFog");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.WU);
			break;
		case R.id.envpanel_txt_mai:// 点击环境信息面板霾按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTMist");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.MAI);
			break;
		case R.id.envpanel_txt_qiwen:// 点击环境信息面板气温按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTTemp");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.QIWEN);
			break;
		case R.id.envpanel_txt_jiaotongshikuang:// 点击环境信息面板交通实况按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTTraffic");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.TRAFFIC);
			break;
		case R.id.envpanel_txt_fenxiangtupian:// 点击环境信息面板分享图片按钮
			MobclickAgent.onEvent(MapMianActivity.this, "DTPicture");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.SHARE_PICTURE);
			break;
		case R.id.envpanel_childtitle_air_checkbox:// 点击环境信息面板的空气质量分布图选择框
			showAirRender(cbox_showrender.isChecked());
			break;

		case R.id.map_zoomin:
			// mMapController.zoomIn();
			MobclickAgent.onEvent(MapMianActivity.this, "DTZoomIn");
			mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomIn());
			// 向主线程回发分析结果数据
			Message toMain = mainHandler.obtainMessage();
			toMain.what = 101;
			mainHandler.sendMessage(toMain);

			break;
		case R.id.map_zoomout:
			MobclickAgent.onEvent(MapMianActivity.this, "DTZoomOut");
			// mMapController.zoomOut();
			mMapView.getMap().setMapStatus(MapStatusUpdateFactory.zoomOut());
			// 向主线程回发分析结果数据
			Message toMain2 = mainHandler.obtainMessage();
			toMain2.what = 101;
			mainHandler.sendMessage(toMain2);

			break;

		case R.id.btn_map_offline:
			Intent intent_offline = null;
			intent_offline = new Intent(MapMianActivity.this,
					SettingOfflineMapActivity.class);
			this.startActivity(intent_offline);
			break;
		case R.id.map_quality_show_btn:
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			// check(1);
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.SURFACE_WATER);
			break;
		case R.id.map_weahter_show_btn:
			// check(2);
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.TIANQI);
			break;
		case R.id.map_temperature_btn:
			// check(3);
			MobclickAgent.onEvent(MapMianActivity.this, "DTShowAirPop");
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.QIWEN);
			break;

		}
	}

	/**
	 * 设置是否显示AQI的实时渲染图
	 * 
	 * @param bool
	 *            为true则显示AQI，为false则不显示
	 */
	private void showAirRender(boolean bool) {
		if (bool) {
			tc.setEmun_rendertype(RenderEnum.AQI);
		} else {
			tc.setEmun_rendertype(RenderEnum.NULL);
			// 直接在此处清除渲染图层
			if (renderGroundOverlay != null) {
				renderGroundOverlay.remove();
			}

			// renderGroundOverlay.clear();
			// mMapView.getOverlays().remove(renderGroundOverlay);
			// mMapView.refresh();
		}
	}

	/**
	 * 切换地图面板要显示的环境信息类别。
	 * 
	 * @param em
	 *            环境信息类别
	 */
	private void changeEnvInfoOnMapByType(EnvTypeOnMapEnum em) {
		if (enum_envtype == em && tc.mapOnResume == false) {
			return;
		}
		tc.setEmun_envtype_Old(enum_envtype);
		tc.mapOnResume = false;
		Log.v("function", "changeEnvInfoOnMapByType");
		// 在修改类型之前先修改按钮的状态背景图片
		setmappanelicon(enum_envtype, em);
		enum_envtype = em;
		tc.setEmun_envtype(em);

		setMapTitle(em);
		showLegend(em);
		setPointLayer();
		showMapTrafficLine();
		showMapPicture();
		switch (em) {
		case AQI:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			// Toast.makeText(this, "开始显示AQI监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case CO:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			Toast.makeText(this, "开始显示CO监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case MAI:
			Toast.makeText(this, "开始显示全国霾分布图", Toast.LENGTH_SHORT).show();
			startmaploading();
			showMapRenderImg(4, new Date());
			break;
		case NO2:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			Toast.makeText(this, "开始显示NO2监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case NULL:
			break;
		case O3:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			Toast.makeText(this, "开始显示O3监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case PM10:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			Toast.makeText(this, "开始显示PM10监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case PM25:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			Toast.makeText(this, "开始显示PM2.5监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case QIWEN:
			Toast.makeText(this, "开始显示全国气温分布图", Toast.LENGTH_SHORT).show();
			startmaploading();
			showMapRenderImg(2, new Date());
			break;
		case SHARE_PICTURE:
			Toast.makeText(this, "开始显示用户分享图片图层", Toast.LENGTH_SHORT).show();
			break;
		case SO2:
			mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_aqi);
			Toast.makeText(this, "开始显示SO2监测点图层", Toast.LENGTH_SHORT).show();
			break;
		case TIANQI:
			mMapView.getMap().setOnMarkerClickListener(
					myMarkerClistener_weather);
			// Toast.makeText(this, "开始显示天气点图层", Toast.LENGTH_SHORT).show();
			break;
		case TRAFFIC:
			Toast.makeText(this, "开始显示交通实况图层", Toast.LENGTH_SHORT).show();
			break;
		case WU:
			Toast.makeText(this, "开始显示全国雾分布图层", Toast.LENGTH_SHORT).show();
			startmaploading();
			showMapRenderImg(3, new Date());
			break;
		case SOURCE_OF_POLLUTION:
			Toast.makeText(this, "开始显示污染源监测点图层", Toast.LENGTH_SHORT).show();
			mMapView.getMap().setOnMarkerClickListener(
					myMarkerClistener_pullotiPoints);
			break;
		case SURFACE_WATER:
			// Toast.makeText(this, "开始显示地表水监测点图层", Toast.LENGTH_SHORT).show();
			mMapView.getMap().setOnMarkerClickListener(
					myMarkerClistener_surfaceWater);
			break;
		default:
			break;
		}

	}

	public void setmappanelicon(EnvTypeOnMapEnum oldem, EnvTypeOnMapEnum newem) {

		Drawable draw = null;
		// 设置原来皮肤
		switch (oldem) {
		case AQI:
			// txt_button_aqi.setTextColor(Color.parseColor("#000000"));
			aqi_show_btn.setCompoundDrawablesWithIntrinsicBounds(null, this
					.getResources().getDrawable(R.drawable.aqi_show_btn_style),
					null, null);
			break;
		case CO:
			txt_button_co.setTextColor(Color.parseColor("#000000"));
			break;
		case MAI:
			// txt_button_mai.setBackgroundResource(R.drawable.icon_map_render_haze);
			draw = getResources().getDrawable(R.drawable.icon_map_render_haze);
			draw.setBounds(0, 0, draw.getMinimumWidth(),
					draw.getMinimumHeight());
			txt_button_mai.setCompoundDrawables(null, draw, null, null);
			break;
		case NO2:
			txt_button_no2.setTextColor(Color.parseColor("#000000"));
			break;
		case NULL:
			break;
		case O3:
			txt_button_o3.setTextColor(Color.parseColor("#000000"));
			break;
		case PM10:
			txt_button_pm10.setTextColor(Color.parseColor("#000000"));
			break;
		case PM25:
			txt_button_pm25.setTextColor(Color.parseColor("#000000"));
			break;
		case QIWEN:
			map_temperature_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.ditu_tem_default_btn_pre_style), null,
					null);
			// txt_button_qiwen.setBackgroundResource(R.drawable.wd);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.wd), null, null);
			draw = getResources().getDrawable(R.drawable.wd);
			draw.setBounds(0, 0, draw.getMinimumWidth(),
					draw.getMinimumHeight());
			txt_button_qiwen.setCompoundDrawables(null, draw, null, null);
			break;
		case SHARE_PICTURE:
			// txt_button_sharepicture.setBackgroundResource(R.drawable.icon_map_sharepicture);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_sharepicture), null, null);
			draw = getResources().getDrawable(R.drawable.icon_map_sharepicture);
			draw.setBounds(0, 0, draw.getMinimumWidth(),
					draw.getMinimumHeight());
			txt_button_sharepicture
					.setCompoundDrawables(null, draw, null, null);
			break;
		case SO2:
			txt_button_so2.setTextColor(Color.parseColor("#000000"));
			break;
		case TIANQI:
			// txt_button_tianqi.setBackgroundResource(R.drawable.tq);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.tq), null, null);
			// draw = getResources().getDrawable(R.drawable.tq);
			// draw.setBounds(0, 0, draw.getMinimumWidth(),
			// draw.getMinimumHeight());
			// txt_button_tianqi.setCompoundDrawables(null, draw, null, null);
			map_weahter_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.ditu_watert_btn_pre_style), null, null);
			break;
		case TRAFFIC:
			// txt_button_jiaotong.setBackgroundResource(R.drawable.icon_map_traffic);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_traffic), null, null);
			draw = getResources().getDrawable(R.drawable.icon_map_traffic);
			draw.setBounds(0, 0, draw.getMinimumWidth(),
					draw.getMinimumHeight());
			txt_button_jiaotong.setCompoundDrawables(null, draw, null, null);
			break;
		case WU:
			// txt_button_wu.setBackgroundResource(R.drawable.icon_map_render_fog);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_render_fog), null, null);
			draw = getResources().getDrawable(R.drawable.icon_map_render_fog);
			draw.setBounds(0, 0, draw.getMinimumWidth(),
					draw.getMinimumHeight());
			txt_button_wu.setCompoundDrawables(null, draw, null, null);
			break;
		case SOURCE_OF_POLLUTION:
			source_pollution_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.source_pollution_show_btn_style), null,
					null);
			break;
		case SURFACE_WATER:
			surfacewater_pic.setVisibility(View.GONE);
			map_quality_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.ditu_water_default_btn_pre_style), null,
					null);
			break;
		default:
			break;

		}
		Drawable draw2 = null;
		// 设置选中的皮肤
		switch (newem) {
		case AQI:
			// txt_button_aqi.setTextColor(Color.parseColor("#00cc00"));
			aqi_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.aqi_show_btn_pre_style), null, null);
			break;
		case CO:
			txt_button_co.setTextColor(Color.parseColor("#00cc00"));
			break;
		case MAI:
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_render_haze_selected), null, null);
			draw2 = getResources().getDrawable(
					R.drawable.icon_map_render_haze_selected);
			draw2.setBounds(0, 0, draw2.getMinimumWidth(),
					draw2.getMinimumHeight());
			txt_button_mai.setCompoundDrawables(null, draw2, null, null);
			break;
		case NO2:
			txt_button_no2.setTextColor(Color.parseColor("#00cc00"));
			break;
		case NULL:
			break;
		case O3:
			txt_button_o3.setTextColor(Color.parseColor("#00cc00"));
			break;
		case PM10:
			txt_button_pm10.setTextColor(Color.parseColor("#00cc00"));
			break;
		case PM25:
			txt_button_pm25.setTextColor(Color.parseColor("#00cc00"));
			break;
		case QIWEN:

			map_temperature_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.ditu_tem_default_btn_style), null, null);
			// txt_button_qiwen.setBackgroundResource(R.drawable.wd_selected);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.wd_selected), null, null);
			draw2 = getResources().getDrawable(R.drawable.wd_selected);
			draw2.setBounds(0, 0, draw2.getMinimumWidth(),
					draw2.getMinimumHeight());
			txt_button_qiwen.setCompoundDrawables(null, draw2, null, null);
			break;
		case SHARE_PICTURE:
			// txt_button_sharepicture.setBackgroundResource(R.drawable.icon_map_sharepicture_selected);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_sharepicture_selected), null, null);
			draw2 = getResources().getDrawable(
					R.drawable.icon_map_sharepicture_selected);
			draw2.setBounds(0, 0, draw2.getMinimumWidth(),
					draw2.getMinimumHeight());
			txt_button_sharepicture.setCompoundDrawables(null, draw2, null,
					null);
			break;
		case SO2:
			txt_button_so2.setTextColor(Color.parseColor("#00cc00"));
			break;
		case TIANQI:
			map_weahter_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.ditu_weather_default_btn_pre_style),
					null, null);
			// txt_button_tianqi.setBackgroundResource(R.drawable.tq_selected);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.tq_selected), null, null);
			// draw2 = getResources().getDrawable(R.drawable.tq_selected);
			// draw2.setBounds(0, 0, draw2.getMinimumWidth(),
			// draw2.getMinimumHeight());
			// txt_button_tianqi.setCompoundDrawables(null, draw2, null, null);
			break;
		case TRAFFIC:
			// txt_button_jiaotong.setBackgroundResource(R.drawable.icon_map_traffic_selected);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_traffic_selected), null, null);
			draw2 = getResources().getDrawable(
					R.drawable.icon_map_traffic_selected);
			draw2.setBounds(0, 0, draw2.getMinimumWidth(),
					draw2.getMinimumHeight());
			txt_button_jiaotong.setCompoundDrawables(null, draw2, null, null);
			break;
		case WU:
			// txt_button_wu.setBackgroundResource(R.drawable.icon_map_render_fog_selected);
			// txt_button_mai.setCompoundDrawables(null,getResources().getDrawable(
			// R.drawable.icon_map_render_fog_selected), null, null);
			draw2 = getResources().getDrawable(
					R.drawable.icon_map_render_fog_selected);
			draw2.setBounds(0, 0, draw2.getMinimumWidth(),
					draw2.getMinimumHeight());
			txt_button_wu.setCompoundDrawables(null, draw2, null, null);
			break;
		case SOURCE_OF_POLLUTION:

			source_pollution_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.source_pollution_show_btn_pre_style),
					null, null);
			break;
		case SURFACE_WATER:

			surfacewater_pic.setVisibility(View.VISIBLE);
			map_quality_show_btn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					this.getResources().getDrawable(
							R.drawable.ditu_water_default_btn_style), null,
					null);
			break;

		default:
			break;

		}
		closeMapEnvPanel();
	}

	/**
	 * 显示环境信息详情面板
	 */
	private void openMapEnvPanel() {
		map_panel_env.setVisibility(RelativeLayout.VISIBLE);
	}

	/**
	 * 显示环境信息详情面板
	 */
	private void closeMapMore() {
		map_more_detail_id.setVisibility(View.GONE);
		// map_more_show_btn.setCompoundDrawablesWithIntrinsicBounds(
		// null,
		// this.getResources().getDrawable(
		// R.drawable.map_more_show_btn_style), null, null);
	}

	/**
	 * 显示环境信息详情面板
	 */
	private void openMapMore() {
		map_more_detail_id.setVisibility(RelativeLayout.VISIBLE);
		// map_more_show_btn.setCompoundDrawablesWithIntrinsicBounds(
		// null,
		// this.getResources().getDrawable(
		// R.drawable.map_more_show_btn_pre_style), null, null);
	}

	private void showyourlocation() {
		if (showlocation) {
			// 准备关闭显示定位
			showlocation = false;
			img_location.setImageResource(R.drawable.map_dingwei);
		} else {
			// 准备打开显示定位
			showlocation = true;
			img_location.setImageResource(R.drawable.map_dingwei);

			requestLocClick();
		}
	}

	/**
	 * 关闭环境信息详情面板
	 */
	private void closeMapEnvPanel() {
		map_panel_env.setVisibility(RelativeLayout.GONE);
	}

	long waitTime = 2000;
	long touchTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getRepeatCount() == 0 && keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
			if (pop != null) {
				pop = null;
				return true;
			}
			if (animflag) {
				endmaploading();
				return true;
			}

			if (dv != null) {
				// 如果显示拖拽view移除退拽
				removeDragView();

				// ltOverlay.removeAll();
				// mMapView.refresh();
				return true;
			}
			long currentTime = System.currentTimeMillis();
			if (View.VISIBLE == map_panel_env.getVisibility()) {
				map_panel_env.setVisibility(View.GONE);
				return true;
			}
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
				finish();
				Intent intent = new Intent(
						WeiBaoApplication.LOCATION_SERVICEINTENT);
				MapMianActivity.this.stopService(intent);
				System.exit(0);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@SuppressLint("NewApi")
	// public void AddDragView(LatLng point) {
	// hbdtrelayout = (RelativeLayout) this.getParent().findViewById(
	// R.id.mainlayout);
	// DisplayMetrics metric = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(metric);
	// LayoutParams lp = new LayoutParams(metric.widthPixels,
	// metric.heightPixels);
	// float h = ((getResources().getDisplayMetrics().density * default_dp) +
	// 0.5f);
	// if (dv != null) {
	// hbdtrelayout.removeView(dv);
	// }
	// dv = new DragViwe(this, metric.heightPixels - h,
	// point.longitude, point.latitude, mMapView);
	// dv.setLayoutParams(lp);
	// dv.setY(metric.heightPixels - h);
	// dv.setBackgroundColor(Color.WHITE);
	// dv.init();
	// hbdtrelayout.addView(dv);
	// }
	public void addBitmapToCache(String key, Bitmap bitmap) {

		// 软引用的Bitmap对象

		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);

		// 添加该对象到Map中使其缓存

		imageCache.put(key, softBitmap);

	}

	// public void addGroundToCache(String key,GroundOverlayOptions ground)
	// {
	// WeakReference<GroundOverlayOptions> softground = new
	// WeakReference<GroundOverlayOptions>(ground);
	// groundCache.put(key, softground);
	// }

	public void addGOverlayToCache(String key, GroundOverlayOptions overlay) {
		WeakReference<GroundOverlayOptions> weaklay = new WeakReference<GroundOverlayOptions>(
				overlay);
		goverlayCache.put(key, weaklay);
	}

	public Bitmap getBitmapByKey(String key) {

		Log.v("function", "getBitmapByKey");
		// 从缓存中取软引用的Bitmap对象

		SoftReference<Bitmap> softBitmap = imageCache.get(key);

		// 判断是否存在软引用

		if (softBitmap == null) {

			return null;

		}

		// 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空

		Bitmap bitmap = softBitmap.get();

		return bitmap;

	}

	public GroundOverlayOptions getGroundByKey(String key) {
		WeakReference<GroundOverlayOptions> softground = goverlayCache.get(key);
		if (softground == null) {
			return null;
		}
		GroundOverlayOptions ground = softground.get();
		return ground;
	}

	public GroundOverlayOptions getGOverlayByKey(String key) {
		WeakReference<GroundOverlayOptions> weaklay = goverlayCache.get(key);
		if (weaklay == null) {
			return null;
		}
		GroundOverlayOptions lay = weaklay.get();
		return lay;
	}

	private void removeDragView() {
		if (dv != null) {
			hbdtrelayout = (RelativeLayout) this.getParent().findViewById(
					R.id.mainlayout);
			hbdtrelayout.removeView(dv);
			dv = null;
		}
	}

	/**
	 * 清除地图上所有事务图层
	 * 
	 * 
	 */
	private void clearAllLayer() {

		// 清除图层，然后隐藏图例，然后清除面板上选中项的对号
		mMapView.getMap().clear();
		// if (pop != null) {
		// mMapView.getMap().hideInfoWindow();
		// }
		// if (mMapView.getOverlays().contains(wOL_PROVINCE)) {
		// mMapView.getOverlays().remove(wOL_PROVINCE);
		// }
		// if (mMapView.getOverlays().contains(wOL_MAINCITY)) {
		// mMapView.getOverlays().remove(wOL_MAINCITY);
		// }
		// if (mMapView.getOverlays().contains(wOL_CITY)) {
		// mMapView.getOverlays().remove(wOL_CITY);
		// }
		// if (mMapView.getOverlays().contains(aOL_CITY)) {
		// mMapView.getOverlays().remove(aOL_CITY);
		// }
		// if (mMapView.getOverlays().contains(aOL_POINT)) {
		// mMapView.getOverlays().remove(aOL_POINT);
		// }
		// if (mMapView.getOverlays().contains(poioverlay)) {
		// mMapView.getOverlays().remove(poioverlay);
		// }

		// tc.clearAllGroundOfMap();

		hideLengend();

	}

	/**
	 * 设置要显示 的点图层
	 * 
	 * @param type
	 *            [weather,aqi]
	 * 
	 */
	private void setPointLayer() {

		Log.v("function", "setPointLayer " + tc.getEmun_envtype().toString()
				+ " " + tc.getEmun_tqZoomLevel().toString());
		mMapView.getMap().clear();

	}

	private void startmaploading() {
		Log.v("funtion", "startmaploading");
		maploading.setVisibility(ImageView.VISIBLE);

		anim.stop();
		anim.start();
		animflag = true;
	}

	private void endmaploading() {
		anim.stop();
		maploading.setVisibility(ImageView.GONE);
		animflag = false;
	}

	/**
	 * 显示图例
	 * 
	 * @param type
	 *            [fog,haze,temp,air,aqi,pm25,pm10,no2,so2,co,o3]
	 */
	@SuppressLint("SimpleDateFormat")
	private void showLegend(EnvTypeOnMapEnum em) {

		String date = WbMapUtil.Date2Str2(new Date());

		Drawable dra = null;
		switch (em) {
		case AQI:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("AQI实时监测  ");
			date = "";
			break;
		case CO:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("CO污染实时监测  ");
			date = "";
			break;
		case MAI:
			dra = getResources().getDrawable(R.drawable.map_legend_haze);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("全国霾24小时预报  ");
			date += " 08:00";
			break;
		case NO2:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("NO2污染实时监测  ");
			date = "";
			break;
		case NULL:
			hideLengend();
			return;
		case O3:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("O3污染实时监测  ");
			date = "";
			break;
		case PM10:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("PM10污染实时监测  ");
			date = "";
			break;
		case PM25:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("PM2.5污染实时监测  ");
			date = "";
			break;
		case QIWEN:
			dra = getResources().getDrawable(R.drawable.map_legend_temp);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("未来24小时平均气温预报分布图 ℃  ");
			date += " 13:00";
			break;
		case SHARE_PICTURE:
			hideLengend();
			return;
		case SO2:
			dra = getResources().getDrawable(R.drawable.map_legend_air);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("SO2污染实时监测  ");
			date = "";
			break;
		case TIANQI:
			hideLengend();
			return;
		case TRAFFIC:
			hideLengend();
			return;
		case WU:
			dra = getResources().getDrawable(R.drawable.map_legend_fog);
			mapLengend.setImageDrawable(dra);
			maplegendname.setText("全国雾24小时预报  ");
			date += " 08:00";
			break;
		default:
			hideLengend();
			return;

		}
		// maplegendpanel.setVisibility(RelativeLayout.VISIBLE);

		// 用来显示数据更新时间
		if (!date.equals("")) {
			Date pushdate = new Date();
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date1 = sd.parse(date);
				Date date2 = new Date();

				long s1 = date1.getTime();
				long s2 = date2.getTime();
				long ds = s2 - s1;
				if (ds < 0)// 当前时间小于发布时间，时间调至前一天
				{
					Calendar cal = Calendar.getInstance();
					cal.setTime(date1);
					cal.add(Calendar.DATE, -1);
					pushdate = cal.getTime();
					date = "发布时间：" + WbMapUtil.Date2Str(pushdate);
				} else {
					pushdate = date1;
					date = "发布时间：" + WbMapUtil.Date2Str(pushdate);
				}

			} catch (ParseException e) {

				e.printStackTrace();

			}
			// txt_dataupttime.setText(date);
			// 更新预报显示时间
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(pushdate);
			cal2.add(Calendar.DATE, 1);
			Date next = cal2.getTime();
			maplegendtime.setText(cal2.get(Calendar.YEAR) + "年"
					+ (cal2.get(Calendar.MONTH) + 1) + "月" + pushdate.getDate()
					+ "日" + pushdate.getHours() + "时 - " + next.getDate() + "日"
					+ next.getHours() + "时");
		}

		// 图例滚动条滚动到中间位置
		WbMapUtil.hscroll2Middle(hscroll_legend, llay_legendinner);

	}

	/**
	 * 切换地图底图
	 * 
	 * @param em
	 */
	@SuppressWarnings("deprecation")
	private void changemapstyle(BaseMapTypeEmun em) {
		Drawable dra = null;
		switch (em) {
		case DIANZI:
			MobclickAgent.onEvent(MapMianActivity.this, "DTElectronicMap");
			basemaptype = BaseMapTypeEmun.WEIXING;
			dra = getResources().getDrawable(R.drawable.map_sallite);
			// img_basemap_button.setBackgroundDrawable(dra);
			mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case WEIXING:
			MobclickAgent.onEvent(MapMianActivity.this, "DTSatelliteMap");
			basemaptype = BaseMapTypeEmun.DIANZI;
			dra = getResources().getDrawable(R.drawable.map_map2d);
			// img_basemap_button.setBackgroundDrawable(dra);
			mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		default:
			break;

		}

	}

	private void showMapTrafficLine() {

		if (tc.getEmun_envtype() == EnvTypeOnMapEnum.TRAFFIC)// 准备隐藏
		{
			Log.v("function-traffic", "xianshi");
			mMapView.getMap().setTrafficEnabled(true);
		} else {
			Log.v("function-traffic", "guanbi");
			mMapView.getMap().setTrafficEnabled(false);
		}
	}

	private void showMapPicture() {
		// 注册点击分享图片的响应事件
		mMapView.getMap().setOnMarkerClickListener(myMarkerClistener_picture);

		if (tc.getEmun_envtype() != EnvTypeOnMapEnum.SHARE_PICTURE)// 准备隐藏
		{
			mMapView.getMap().clear();
		} else {

			mMapView.getMap().clear();

			getmappictures();
		}
	}

	private void getmappictures() {

		getMapPictureAstask task = new getMapPictureAstask();
		// 获取地图可视范围的最小外接矩形
		LatLng pt00 = mMapView.getMap().getProjection()
				.fromScreenLocation(new Point(0, 0));
		LatLng ptw0 = mMapView.getMap().getProjection()
				.fromScreenLocation(new Point(mMapView.getWidth(), 0));
		LatLng pt0h = mMapView.getMap().getProjection()
				.fromScreenLocation(new Point(0, mMapView.getHeight()));
		LatLng ptwh = mMapView
				.getMap()
				.getProjection()
				.fromScreenLocation(
						new Point(mMapView.getWidth(), mMapView.getHeight()));
		double centerGroundLaSpan = Math
				.max(pt00.latitude,
						Math.max(ptw0.latitude,
								Math.max(pt0h.latitude, ptwh.latitude)))
				- Math.min(
						pt00.latitude,
						Math.min(ptw0.latitude,
								Math.min(pt0h.latitude, ptwh.latitude)));
		double centerGroundLoSpan = Math.max(
				pt00.longitude,
				Math.max(ptw0.longitude,
						Math.max(pt0h.longitude, ptwh.longitude)))
				- Math.min(
						pt00.longitude,
						Math.min(ptw0.longitude,
								Math.min(pt0h.longitude, ptwh.longitude)));
		// 获取经纬度范围，构建URL

		LatLng newcenter = mMapView.getMap().getMapStatus().target;
		double maxb = (double) (newcenter.longitude + centerGroundLoSpan);
		double minb = (double) (newcenter.longitude - centerGroundLoSpan);
		double maxl = (double) (newcenter.latitude + centerGroundLaSpan);
		double minl = (double) (newcenter.latitude - centerGroundLaSpan);
		int count = 20;// 返回的记录数
		String url = UrlComponent.getMapPicByBLURL + maxb + "/" + minb + "/"
				+ maxl + "/" + minl + "/" + count + UrlComponent.token;
		url_getuserpicture = url;

		task.execute(url);
		MyLog.i(">>>>>>>>>>>>>>>>>userPicture");
		Log.v("mappic-url", url);

	}

	private void hideLengend() {

		maplegendpanel.setVisibility(RelativeLayout.GONE);

	}

	/**
	 * 获取要渲染到地图上的图像
	 * 
	 * @param type
	 *            [1-air,2-temp,3-fog,4-haze]
	 * @param date
	 */
	private void showMapRenderImg(int type, Date date) {
		Log.v("function", "showMapRenderImg");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String datestr = cal.get(Calendar.YEAR) + "_" + (date.getMonth() + 1)
				+ "_" + date.getDate() + "_";

		String strtype = "";
		if (type == 1) {
			strtype = "air";
			datestr += "08";
		} else if (type == 2) {
			strtype = "temp";
			datestr += "13";
		} else if (type == 3) {
			strtype = "fog";
			datestr += "08";
		} else if (type == 4) {
			strtype = "haze";
			datestr += "08";
		}

		if (getBitmapByKey(strtype) == null) {

			Get_MapImgdata getimgdata = new Get_MapImgdata(mhandler, type,
					datestr);
			getimgdata.execute();
		} else {
			endmaploading();
			showWholeCountry(strtype);
			LatLng pt = new LatLng(40.0, 100.0);
			// new GeoPoint(40000000, 100000000);//取了个大概的中心点，中国的中心点
			MapStatus ms = new MapStatus.Builder().zoom(5).target(pt).build();
			mMapView.getMap().setMapStatus(
					MapStatusUpdateFactory.newMapStatus(ms));
			// MKMapStatus mms = new MKMapStatus(5, 0, 0, pt, null);
			// mMapView.getController().setMapStatusWithAnimation(mms);
		}
	}

	private int getAQIMarker(AQIPoint point) {

		int marker = R.drawable.aqi_1;
		int value = 0;
		switch (tc.getEmun_envtype()) {
		case AQI:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.AQI,
						(int) Double.parseDouble(point.getAqi()));

			} catch (Exception e) {

				value = 0;
			}

			break;
		case PM25:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.pm25,
						(int) Double.parseDouble(point.getPm2_5()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case PM10:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.PM10,
						(int) Double.parseDouble(point.getPM10()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case NO2:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.NO2,
						(int) Double.parseDouble(point.getNO2()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case SO2:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.SO2,
						(int) Double.parseDouble(point.getSO2()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case CO:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.CO,
						(int) Double.parseDouble(point.getCO()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case O3:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.O3,
						(int) Double.parseDouble(point.getO3()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		}
		// if (value <= 0) {
		// marker = R.drawable.aqi_wrong;
		// } else if (value <= 50) {
		// marker = R.drawable.aqi_1;
		// } else if (value <= 100) {
		// marker = R.drawable.aqi_2;
		// } else if (value <= 150) {
		// marker = R.drawable.aqi_3;
		// } else if (value <= 200) {
		// marker = R.drawable.aqi_4;
		// } else if (value <= 251) {
		// marker = R.drawable.aqi_5;
		// } else {
		// marker = R.drawable.aqi_6;
		// }
		if (value <= 0) {
			marker = R.drawable.map_7hui;
		} else if (value <= 50) {
			marker = R.drawable.map_1you;
		} else if (value <= 100) {
			marker = R.drawable.map_2liang;
		} else if (value <= 150) {
			marker = R.drawable.map_3qingdu;
		} else if (value <= 200) {
			marker = R.drawable.map_4zhong;
		} else if (value <= 300) {
			marker = R.drawable.map_5zhongdu;
		} else {
			marker = R.drawable.map_6yanzhong;
		}
		return marker;
	}

	private float getIAQIvalue(AQIPoint point) {

		int marker = R.drawable.aqi_1;
		int value = 0;
		switch (tc.getEmun_envtype()) {
		case AQI:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.AQI,
						(int) Double.parseDouble(point.getAqi()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case PM25:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.pm25,
						(int) Double.parseDouble(point.getPm2_5()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case PM10:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.PM10,
						(int) Double.parseDouble(point.getPM10()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case NO2:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.NO2,
						(int) Double.parseDouble(point.getNO2()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case SO2:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.SO2,
						(int) Double.parseDouble(point.getSO2()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case CO:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.CO,
						(int) Double.parseDouble(point.getCO()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		case O3:
			try {
				value = WbMapUtil.getIAQI(PollutantTypeEnum.O3,
						(int) Double.parseDouble(point.getO3()));
			} catch (Exception e) {

				value = 0;
			}

			break;
		}
		return value;
	}

	private int getWeatherMarker(int weathercode) {

		int maker = R.drawable.w0_map;
		switch (weathercode) {
		case WeatherEnum.BIG_RAIN:
			maker = R.drawable.w9_map;
			break;
		case WeatherEnum.BIG_RAIN_STORM:
			maker = R.drawable.w10_map;
			break;
		case WeatherEnum.CLOUDY:
			maker = R.drawable.w1_map;
			break;
		case WeatherEnum.FLY_ASH:

			break;
		case WeatherEnum.FOG:
			maker = R.drawable.w18_map;
			break;
		case WeatherEnum.FROST:

			break;
		case WeatherEnum.HAIL:
			maker = R.drawable.w19_map;
			break;
		case WeatherEnum.HAZE:

			break;
		case WeatherEnum.HEAVY_SNOW:
			maker = R.drawable.w16_map;
			break;
		case WeatherEnum.HUGE_RAIN_STORM:
			maker = R.drawable.w10_map;
			break;
		case WeatherEnum.LIGHT_SNOW:
			maker = R.drawable.w14_map;
			break;
		case WeatherEnum.MIDDLE_RAIN:
			maker = R.drawable.w8_map;
			break;
		case WeatherEnum.MIDDLE_SNOW:
			maker = R.drawable.w15_map;
			break;
		case WeatherEnum.OTHER:

			break;
		case WeatherEnum.OVERCAST:
			maker = R.drawable.w2_map;
			break;
		case WeatherEnum.RAIN_AND_SNOW:
			maker = R.drawable.w6_map;
			break;
		case WeatherEnum.RAIN_STORM:
			maker = R.drawable.w10_map;
			break;
		case WeatherEnum.SAND_BLOWING:

			break;
		case WeatherEnum.SAND_STORM:
			maker = R.drawable.w20_map;
			break;
		case WeatherEnum.SHOWERS:
			maker = R.drawable.w3_map;
			break;
		case WeatherEnum.SMALL_RAIN:
			maker = R.drawable.w7_map;
			break;
		case WeatherEnum.SNOW_SHOWERS:
			maker = R.drawable.w13_map;
			break;
		case WeatherEnum.SNOW_STROM:
			maker = R.drawable.w17_map;
			break;
		case WeatherEnum.SUN_SHINE:
			maker = R.drawable.w0_map;
			break;
		case WeatherEnum.T_SAND_STORM:
			maker = R.drawable.w20_map;
			break;
		case WeatherEnum.T_SHOWERS:
			maker = R.drawable.w4_map;
			break;
		default:
			break;
		}
		return maker;

	}

	private void maptoolclick() {
		if (isanimtion) {
			return;
		}
		if (toolboxvisible) {
			hidemaptoolbox();
		} else {
			showmaptoolbox();
		}
	}

	private void showmaptoolbox() {
		btn_mapclear.setVisibility(ImageView.GONE);
		Animation showanimation = AnimationUtils.loadAnimation(this,
				R.anim.map_box_showscale);
		showanimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				map_tool_box.setVisibility(View.VISIBLE);
				isanimtion = false;
			}
		});
		if (!isanimtion) {
			isanimtion = true;
			map_tool_box.startAnimation(showanimation);
		}
		toolboxvisible = true;
	}

	private void hidemaptoolbox() {
		if (!toolboxvisible) {
			return;
		}
		Animation showanimation = AnimationUtils.loadAnimation(this,
				R.anim.map_box_scale);
		showanimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// Auto-generated method stub
				map_tool_box.setVisibility(View.INVISIBLE);
				isanimtion = false;
			}
		});
		if (!isanimtion) {
			isanimtion = true;
			map_tool_box.startAnimation(showanimation);

		}
		toolboxvisible = false;
		btn_mapclear.setVisibility(ImageView.VISIBLE);
	}

	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	public void doTakePhoto() {
		try {
			// Launch camera to take photo for selected contact
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);

			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {

		}
	}

	// 请求Gallery程序
	public void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			// Toast.makeText(this, R.string.photoPickerNotFoundText1,
			// Toast.LENGTH_LONG).show();
		}
	}

	protected void doCropPhoto(File f) {
		try {
			// 启动gallery去剪辑这个照片
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {

		}
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	// 封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "false");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 显示全国特定数据分布图
	 * 
	 * @param type
	 */
	private void showWholeCountry(String type) {
		endmaploading();
		// 设置标记
		// tc.pointType = TileControl.POINT_TYPE_NULL;
		tc.setEmun_rendertype(RenderEnum.NULL);
		if (type.equals("tempreture")) {
			// tc.tileType = TileControl.TILE_TYPE_WD;
			tc.setEmun_envtype(EnvTypeOnMapEnum.QIWEN);
		} else if (type.equals("fog")) {
			// tc.tileType = TileControl.TILE_TYPE_FOG;
			tc.setEmun_envtype(EnvTypeOnMapEnum.WU);
		} else if (type.equals("haze")) {
			// tc.tileType = TileControl.TILE_TYPE_HAZE;
			tc.setEmun_envtype(EnvTypeOnMapEnum.MAI);
		}

		// tc.clearAllGroundOfMap();
		// mMapView.refresh();
		// mMapView.getController().setZoom((float) 5.0);
		// tc.clearAllPointsOfMap();

		// 显示全图空气质量
		// mGroundOverlay = new GroundOverlay(mMapView);
		LatLng leftBottom = new LatLng(18.224167, 73.55);
		LatLng rightTop = new LatLng(53.55, 135.083333);

		// if(getGroundByKey(type)==null)
		// {
		// if(getBitmapByKey(type)==null)
		// {
		//
		// return;
		// }
		// Ground ground = new Ground(getBitmapByKey(type), leftBottom,
		// rightTop);
		// addGroundToCache(type, ground);
		// }

		mMapView.getMap().clear();

		/**
		 * 将overlay 添加至MapView中
		 */
		// if(getGOverlayByKey(type) != null)
		// {
		// // GroundOverlay lay = new GroundOverlay(mMapView);
		// // addGOverlayToCache(type, lay);
		// mMapView.getMap().addOverlay(getGOverlayByKey(type));
		// }

		if (getBitmapByKey(type) != null) {
			Bitmap bmp = getBitmapByKey(type);
			LatLngBounds bounds = new LatLngBounds.Builder().include(rightTop)
					.include(leftBottom).build();
			// 定义Ground显示的图片
			BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap(bmp);
			// 定义Ground覆盖物选项
			GroundOverlayOptions ooGround = new GroundOverlayOptions()
					.positionFromBounds(bounds).image(bdGround);

			mMapView.getMap().addOverlay(ooGround);
		}

	}

	Drawable drawBitmap(String title) {
		Bitmap bmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.map_marker_view, null);
		TextView titleView = (TextView) layout.findViewById(R.id.title);
		titleView.setText(title);
		layout.setDrawingCacheEnabled(true);
		layout.measure(View.MeasureSpec.makeMeasureSpec(canvas.getWidth(),
				View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
				canvas.getHeight(), View.MeasureSpec.EXACTLY));
		layout.layout(0, 0, layout.getMeasuredWidth(),
				layout.getMeasuredHeight());
		Paint paint = new Paint();
		Drawable drawable = null;
		try {
			canvas.drawBitmap(layout.getDrawingCache(), 0, 0, paint);

			drawable = new BitmapDrawable(bmp);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return drawable;
	}

	List<PicturePointItem> ppts = new ArrayList<PicturePointItem>();

	class getMapPictureAstask extends
			AsyncTask<Object, Void, Map<String, Object>> {

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null || tc == null || mMapView == null)
				return;
			// 判断还是否是显示该功能
			if (tc.getEmun_envtype() != EnvTypeOnMapEnum.SHARE_PICTURE) {
				return;
			}
			Log.v("mappic-onpostexecute", "pass");
			List<OverlayOptions> addlist = (List<OverlayOptions>) result
					.get("add");

			// 显示分享图片，分享前先清空地图
			mMapView.getMap().clear();
			for (int i = 0; i < addlist.size(); i++) {
				mMapView.getMap().addOverlay(addlist.get(i));
			}

		}

		@Override
		protected Map<String, Object> doInBackground(Object... params) {

			Map<String, Object> resobj = new HashMap<String, Object>();

			try {
				MyLog.i("weibao result:" + params[0]);
				// 判断该请求是否已经过时

				Log.v("mappic-picture-3", System.currentTimeMillis() + "");
				// 请求服务器获取图片集。
				String ret = ApiClient.connServerForResult(params[0] + "");
				if (!url_getuserpicture.equals((String) params[0]))// 过时
				{
					// 跳过
					Log.v("mappic-picture", "url is old");
					return null;
				}
				Log.v("mappic-ret", ret);
				ppts = new ArrayList<PicturePointItem>();
				try {
					JSONObject jo = new JSONObject(ret);

					JSONArray ja = jo.getJSONArray("shares");

					for (int i = 0; i < ja.length(); i++) {
						JSONArray pics = ja.getJSONObject(i).getJSONArray(
								"images");
						// 获取图片列表
						List<PicturePointImageItem> piclist = new ArrayList<PicturePointImageItem>();

						for (int j = 0; j < pics.length(); j++) {
							JSONObject pic = pics.getJSONObject(j);
							int id = pic.getInt("imgid");
							String baseUrl = UrlComponent.baseurl;
							int location = baseUrl.indexOf("v1.2");
							baseUrl = baseUrl.substring(0, location);
							String smallpath = baseUrl + pic.getString("path");
							String realpath = baseUrl + pic.getString("rpath");
							Log.v("function-url", realpath);
							PicturePointImageItem item = new PicturePointImageItem(
									id, smallpath, realpath);
							piclist.add(item);
						}

						// 获取其他信息
						int userid = ja.getJSONObject(i).getInt("userid");
						String username = ja.getJSONObject(i).getString(
								"username");
						String province = ja.getJSONObject(i).getString(
								"province");
						String city = ja.getJSONObject(i).getString("city");
						double b = ja.getJSONObject(i).getDouble("b");
						double l = ja.getJSONObject(i).getDouble("l");
						String area = ja.getJSONObject(i).getString("area");
						String time = ja.getJSONObject(i).getString("time");
						int postid = ja.getJSONObject(i).getInt("postid");
						String post = ja.getJSONObject(i).getString("post");
						PicturePointItem pictureitem = new PicturePointItem(
								userid, username, province, city, b, l, area,
								time, postid, post, piclist);
						ppts.add(pictureitem);
					}
				} catch (JSONException e) {

					e.printStackTrace();
					Log.v("mappic-picture-e", e.toString());
				}
				// Log.v("mappic-picture-5",System.currentTimeMillis()+"");
				Log.v("mappic-picture-ppts.size", ppts.size() + "");

				List<OverlayOptions> reslist = new ArrayList<OverlayOptions>();
				for (int i = 0; i < ppts.size(); i++) {

					// 存范围

					Drawable loaddra;
					try {

						loaddra = WbMapUtil.loadImageFromNetwork(ppts.get(i)
								.getPic().get(0).getSmallpath());
					} catch (Exception e) {
						Log.v("mappic-picture-try1-e", e.toString());
						continue;

					}

					if (loaddra == null) {
						Log.v("mappic-picture-loaddra", "null");
						continue;
					}

					Bitmap res = sharePicureLayoutToDrawable(
							R.layout.map_hbdt_point_sharepicture, loaddra);
					// item.setMarker(res);
					// 构建MarkerOption，用于在地图上添加Marker
					try {
						OverlayOptions item = new MarkerOptions()
								.position(ppts.get(i).getbaiduptLoc())
								.title(ppts.get(i).getPostid() + "")
								.icon(BitmapDescriptorFactory.fromBitmap(res));
						reslist.add(item);
					} catch (Exception e) {

					}

				}
				// resobj.put("del", oldreslist);
				resobj.put("add", reslist);
				Log.v("function-picture-6", System.currentTimeMillis() + "");
			} catch (Exception e) {
				MyLog.e("weibao Exception", e);
			}
			return resobj;
		}

	}

	/**
	 * 定位地图到城市
	 * 
	 * @param city
	 */
	private void centerMapToCity(String cityname) {
		float[] loc = mCityDB.getLocationOfCity(cityname);
		if (loc[0] == 0 && loc[1] == 0) {
			Toast.makeText(MapMianActivity.this, "获取" + cityname + "的坐标失败！",
					Toast.LENGTH_SHORT).show();

			return;
		}
		LatLng gp = new LatLng(loc[1], loc[0]);
		// mMapView.getController().animateTo(gp);
		// mMapView.getController().setCenter(gp);
		mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(gp));
	}

	/**
	 * @author shang 异步，功能是延迟一段时间执行功能
	 */
	class SleepAstask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// tc.updateMapPoints("aqi");
			// tc.setEmun_envtype(EnvTypeOnMapEnum.AQI);
			changeEnvInfoOnMapByType(EnvTypeOnMapEnum.AQI);
		}

	}

	//
	// public void startshareactivity(int lon, int lat) {
	// Intent intent1 = new Intent(MapMianActivity.this, ShareActivity.class);
	// intent1.putExtra("lon", String.valueOf(lon));
	// intent1.putExtra("lat", String.valueOf(lat));
	// startActivityForResult(intent1, CHARE_DATA);
	// // startActivity(intent1);
	// }

	/**
	 * @author shang 百度社会化分享对象
	 */
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			MyLog.i("share success");
			MobclickAgent.onEvent(MapMianActivity.this, "DTShare");
			Toast.makeText(MapMianActivity.this, "分享成功", 2000).show();
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			MyLog.i("share errCode " + errCode);
			Toast.makeText(MapMianActivity.this, "分享失败", 2000).show();
		}

		@Override
		public void onCancel() {
			MyLog.i("cancel ");
			// Toast.makeText(CurrentTq.this, "分享取消", 2000).show();
		}
	}

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		// isRequest = true;
		mLocClient.start();
		islocationing = true;
		mLocClient.requestLocation();
		LocationService.sendGetLocationBroadcast(MapMianActivity.this);
		Toast.makeText(MapMianActivity.this, "正在定位……", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 修改位置图标
	 * 
	 * @param drawableid
	 */
	public void modifyLocationOverlayIcon(int drawableid) {

		mMapView.getMap().setMyLocationEnabled(true);
		// 当传入marker为null时，使用默认图标绘制
		// myLocationOverlay.setMarker(marker);
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(drawableid);
		MyLocationConfiguration config = new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.FOLLOWING, true,
				mCurrentMarker);

		// mBaiduMap.setMyLocationConfiguration();
		mMapView.getMap().setMyLocationConfigeration(config);
		// 修改图层，需要刷新MapView生效
		// mMapView.refresh();
	}

	private OnClickListener longclickpanellistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.txt_find_about:// 点击查附近按钮
				Intent intent = new Intent(MapMianActivity.this,
						DiscoverNearbyActivity.class);
				intent.putExtra("from", "mapMainActivity");
				intent.putExtra("maplat",
						mMapView.getMap().getMapStatus().target.latitude);
				intent.putExtra("maplong",
						mMapView.getMap().getMapStatus().target.longitude);
				startActivity(intent);
				break;
			case R.id.txt_share:// 点击分享按钮
				// mMapView.getCurrentMap();
				mMapView.getMap().snapshot(
						new BaiduMap.SnapshotReadyCallback() {

							@Override
							public void onSnapshotReady(Bitmap arg0) {
								// 发送消息修改指南针的方向
								try {
									Message toMapActivity = mainHandler
											.obtainMessage();
									toMapActivity.what = 201;//
									toMapActivity.obj = arg0;
									mainHandler.sendMessage(toMapActivity);
								} catch (OutOfMemoryError e) {
									MyLog.e("weibao Exception", e);
								} catch (Exception e) {
									MyLog.e("weibao Exception", e);
								}
							}
						});
				break;
			}
		}
	};

	/**
	 * 
	 * 注册点击天气图标的弹出框事件
	 */
	OnMarkerClickListener myMarkerClistener_weather = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {
			// TODO Auto-generated method stub

			// String[] str = arg0.getTitle().split("-");
			//
			// String title = str[0];// type
			//
			// String snippet = str[1];// city name
			// LatLng loc = arg0.getPosition();
			// if (title.equals("wOL_PROVINCE")) {
			// MapPop_province_w mpop = new MapPop_province_w(
			// MapMianActivity.this, snippet);
			// // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
			// InfoWindow mInfoWindow = new InfoWindow(mpop, loc, -47);
			// // 显示InfoWindow
			// // mMapView.getMap().showInfoWindow(mInfoWindow);
			//
			// } else if (title.equals("wOL_MAINCITY")) {
			// MapPop_province_w mpop = new MapPop_province_w(
			// MapMianActivity.this, snippet);
			// // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
			// InfoWindow mInfoWindow = new InfoWindow(mpop, loc, -47);
			// // 显示InfoWindow
			// // mMapView.getMap().showInfoWindow(mInfoWindow);
			// } else if (title.equals("wOL_CITY")) {
			// MapPop_province_w mpop = new MapPop_province_w(
			// MapMianActivity.this, snippet);
			// // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
			// InfoWindow mInfoWindow = new InfoWindow(mpop, loc, -47);
			// // 显示InfoWindow
			// // mMapView.getMap().showInfoWindow(mInfoWindow);
			//
			// }
			return false;
		}
	};

	/**
	 * 
	 * 注册点击AQI点弹窗事件
	 */
	OnMarkerClickListener myMarkerClistener_aqi = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {
			// TODO Auto-generated method stub
			MyLog.i(">>>>>>>>>>>>>marker" + arg0);
			if (arg0.getTitle() == null) {
				Log.v("mappic-markerclick", "gettitle is null");
				return false;
			}
			String[] str = arg0.getTitle().split("-");

			if (str.length < 2) {
				Log.v("mappic-markerclick", "str.length<2 " + arg0.getTitle());
				return false;
			}

			String title = str[0];// type
			String snippet = str[1];// city name
			String aqi = null;
			LatLng loc = arg0.getPosition();
			MyLog.i(">>>>>>title" + title);
			if (str.length == 3) {
				aqi = str[2];
				if (title.equals("aOL_CITY")) {
					MapPop_city_a mpop = new MapPop_city_a(
							MapMianActivity.this, snippet, aqi,
							tc.getEmun_envtype());
					// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
					pop = new InfoWindow(mpop, loc, -47);
					// 显示InfoWindow
					mMapView.getMap().showInfoWindow(pop);
				} else if (title.equals("aOL_POINT")) {

				}
				return true;
			}

			if (title.equals("aOL_CITY")) {
				MyLog.i(">>>>>>>>>>lengng" + "2");
				MapPop_city_a mpop = new MapPop_city_a(MapMianActivity.this,
						snippet, tc.getEmun_envtype());
				// 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
				pop = new InfoWindow(mpop, loc, -47);
				// 显示InfoWindow
				mMapView.getMap().showInfoWindow(pop);

			} else if (title.equals("aOL_POINT")) {
				//
				// String stop
				// MapPop_city_a mpop = new MapPop_city_a(HbdtActivity.this,
				// snippet, tc.aqiType);
				// //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
				// InfoWindow mInfoWindow = new InfoWindow(mpop, loc, -47);
				// //显示InfoWindow
				// mMapView.getMap().showInfoWindow(mInfoWindow);
			}
			return true;
		}
	};

	/**
	 * 
	 * 注册点击AQI点弹窗事件
	 */
	OnMarkerClickListener myMarkerClistener_surfaceWater = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {

			Bundle bundle = arg0.getExtraInfo();
			if (null == bundle) {
				MyLog.i("null == bundle");
				return false;
			}
			LatLng loc = arg0.getPosition();
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater
					.inflate(R.layout.map_surface_water_click, null);
			TextView river = (TextView) view.findViewById(R.id.river);
			TextView water = (TextView) view.findViewById(R.id.water);
			TextView monitor = (TextView) view.findViewById(R.id.monitor);
			TextView time = (TextView) view.findViewById(R.id.time);

			river.setText(bundle.getString("river"));
			water.setText(bundle.getString("water"));
			monitor.setText(bundle.getString("monitor"));
			time.setText(bundle.getString("time"));
			// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT);
			// view.setLayoutParams(params);
			pop = new InfoWindow(view, loc, -30);
			// 显示InfoWindow
			mMapView.getMap().showInfoWindow(pop);
			return false;
		}
	};

	/**
	 * 
	 * 注册点击AQI点弹窗事件
	 */
	OnMarkerClickListener myMarkerClistener_pullotiPoints = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {

			Bundle bundle = arg0.getExtraInfo();
			if (null == bundle) {
				MyLog.i("null == bundle");
				return false;
			}
			LatLng loc = arg0.getPosition();
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.map_pullotion_point_click,
					null);
			TextView name = (TextView) view.findViewById(R.id.name);
			TextView type = (TextView) view.findViewById(R.id.type);
			TextView address = (TextView) view.findViewById(R.id.address);

			// bundle.putString("address", aqipt.getAddress());
			// bundle.putString("city", aqipt.getCity());
			// bundle.putString("district", aqipt.getDistrict());
			// bundle.putString("name", aqipt.getName());
			// bundle.putString("type", aqipt.getType());

			name.setText(bundle.getString("name"));
			type.setText(bundle.getString("type"));
			String addressValue = bundle.getString("address");
			address.setText(addressValue.substring(addressValue.indexOf(bundle
					.getString("district"))));

			pop = new InfoWindow(view, loc, -30);
			// 显示InfoWindow
			mMapView.getMap().showInfoWindow(pop);
			return false;
		}
	};

	/**
	 * 
	 * 注册点击用户分享图片的事件
	 */
	OnMarkerClickListener myMarkerClistener_picture = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker arg0) {
			// TODO Auto-generated method stub
			String title = arg0.getTitle();

			//
			PicturePointItem item = null;
			for (int i = 0; i < ppts.size(); i++) {

				if (title.equals(ppts.get(i).getPostid() + "")) {
					item = ppts.get(i);

					break;
				}
			}

			if (item == null || item.getPic().size() < 1) {
				return false;
			}
			// 打开分享图片集
			Intent intent = new Intent(MapMianActivity.this, MapImageView.class);
			String[] urls = new String[item.getPic().size()];
			for (int m = 0; m < item.getPic().size(); m++) {
				urls[m] = item.getPic().get(m).getRealpath();
			}
			intent.putExtra(WeiBaoApplication.MAP_PICTURE_LIST, urls);
			intent.putExtra("username", item.getUsername());
			intent.putExtra("usercontent", item.getPost());
			startActivity(intent);
			return false;
		}
	};

	BDLocationListener mLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null || islocationing == false) {
				Log.v("function-location",
						"location null or islocationing is false");
				return;
			}
			Log.v("function-location", "location");
			Log.v("function-location-1", location.getLatitude() + "");

			// locData.latitude = location.getLatitude();
			// locData.longitude = location.getLongitude();
			// //如果不显示定位精度圈，将accuracy赋值为0即可
			// locData.accuracy = location.getRadius();
			// // 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
			// locData.direction = location.getDirection();
			locData = new MyLocationData.Builder()
					.latitude(location.getLatitude())
					.longitude(location.getLongitude())
					.accuracy(location.getRadius())
					.direction(location.getDirection()).build();
			modifyLocationOverlayIcon(R.drawable.ic_userlocation);
			// 更新定位数据
			// myLocationOverlay.setData(locData);
			mMapView.getMap().setMyLocationData(locData);
			// 更新图层数据执行刷新后生效
			// mMapView.refresh();
			// LatLng cenpt = new GeoPoint((int)(location.getLatitude()*1E6),
			// (int)(location.getLongitude()*1E6));
			LatLng cenpt = new LatLng(location.getLatitude(),
					location.getLongitude());
			// mMapView.getController().animateTo(cenpt);
			// mMapView.getController().z
			MapStatus ms = new MapStatus.Builder().zoom(16).target(cenpt)
					.build();
			mMapView.getMap().setMapStatus(
					MapStatusUpdateFactory.newMapStatus(ms));
			// MKMapStatus mms = new MKMapStatus(16, 0, 0, cenpt, null);
			// mMapView.getController().setMapStatusWithAnimation(mms);
			showyourlocation();// 目的是切换一下定位按钮的图片。
			mLocClient.stop();
			islocationing = false;
		}
	};

	private void initBitMap() {
		water_1Bitmap = drawableToBitamp(context.getResources().getDrawable(
				R.drawable.water_1));
		water_2Bitmap = drawableToBitamp(context.getResources().getDrawable(
				R.drawable.water_2));
		water_3Bitmap = drawableToBitamp(context.getResources().getDrawable(
				R.drawable.water_3));
		water_4Bitmap = drawableToBitamp(context.getResources().getDrawable(
				R.drawable.water_4));
		water_5Bitmap = drawableToBitamp(context.getResources().getDrawable(
				R.drawable.water_5));
		water_6Bitmap = drawableToBitamp(context.getResources().getDrawable(
				R.drawable.water_6));
		pollutionPointBitmap = drawableToBitamp(context.getResources()
				.getDrawable(R.drawable.ditu_work));
	}

	private static Bitmap water_1Bitmap;
	private static Bitmap water_2Bitmap;
	private static Bitmap water_3Bitmap;
	private static Bitmap water_4Bitmap;
	private static Bitmap water_5Bitmap;
	private static Bitmap water_6Bitmap;
	private static Bitmap pollutionPointBitmap;

	private Bitmap getSurfaceWaterMarker(String mow_quality) {
		MyLog.i("mow_quality :" + mow_quality);
		if (mow_quality.equals("I类")) {
			return water_1Bitmap;
		} else if (mow_quality.equals("II类")) {
			return water_2Bitmap;
		} else if (mow_quality.equals("III类")) {
			return water_3Bitmap;
		} else if (mow_quality.equals("IV类")) {
			return water_4Bitmap;
		} else if (mow_quality.equals("V类")) {
			return water_5Bitmap;
		} else if (mow_quality.equals("劣V类")) {
			return water_6Bitmap;
		} else {
			return water_1Bitmap;
		}
	}

	private Drawable getSurfaceWaterMarkerId(String mow_quality) {
		if (mow_quality.equals("I类")) {
			return context.getResources().getDrawable(R.drawable.water_1);
		} else if (mow_quality.equals("II类")) {
			return context.getResources().getDrawable(R.drawable.water_2);
		} else if (mow_quality.equals("III类")) {
			return context.getResources().getDrawable(R.drawable.water_3);
		} else if (mow_quality.equals("IV类")) {
			return context.getResources().getDrawable(R.drawable.water_4);
		} else if (mow_quality.equals("V类")) {
			return context.getResources().getDrawable(R.drawable.water_5);
		} else if (mow_quality.equals("劣V类")) {
			return context.getResources().getDrawable(R.drawable.water_6);
		} else {
			return context.getResources().getDrawable(R.drawable.water_1);
		}
	}

	private Bitmap drawableToBitamp(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bitmap = bd.getBitmap();
		return bitmap;
	}
}
