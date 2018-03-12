package com.jy.environment.map;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlay;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
//import com.baidu.mapapi.map.Ground;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.MKMapStatus;
//import com.baidu.mapapi.map.MKMapStatusChangeListener;
//import com.baidu.mapapi.map.MKMapTouchListener;
//import com.baidu.mapapi.map.MKMapViewListener;
import com.jy.environment.activity.MapMianActivity;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.AqiModel;
import com.jy.environment.model.City;
import com.jy.environment.model.PollutionPointModel;
import com.jy.environment.model.Province;
import com.jy.environment.model.SurfaceWaterModel;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.WbMapUtil;
import com.jy.environment.webservice.UrlComponent;

//import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @author shanglk 对生成的瓦片进行管理 对加载 的气泡进行管理
 */
public class TileControl {

	CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
	/**
	 * 标识当前要显示的AQI类别
	 */
	private EnvTypeOnMapEnum emun_envtype;
	/**
	 * 标识上次当前要显示的AQI类别
	 */
	private EnvTypeOnMapEnum emun_envtype_Old;
	/**
	 * 标识当前要显示的渲染类别
	 */
	private RenderEnum emun_rendertype;
	/**
	 * 标识当前天气的地图绽放级别
	 */
	private TianQiZoomLevelEnum emun_tqZoomLevel;
	/**
	 * 标识当前AQI---O3类别地图绽放显示的数据级别
	 */
	private AQIZoomLevelEmun emun_aqiZoomLevel;

	/**
	 * 这个变量是用来存储url地址的，规则是，在一个服务器调用请求后，将url保存到该变量，当异步请求返回结果后在处理数据之前进行判断，
	 * 变量里保存的是否还是之前的url 如果已经发生变化，则放弃此次数据，执行该url重新获取数据。如果没有变化则继续。
	 */
	private String waitingurl = "";

	protected static final int ADD_TILE = 0;
	protected static final int DELETE_TILE = 1;
	// protected static final int CHECK_TILE = 3;
	// protected static final int UPDATE_WEATHER_PROVINCE = 4;
	protected static final int UPDATE_WEATHER_MAINCITY = 5;
	protected static final int UPDATE_WEATHER_CITY = 6;

	/**
	 * 标识地图上显示的aqi类型点是否发生类型变化，如果在代码中调用判断，判断完后将其更改为false;
	 */
	public boolean aqiTypeChanged = false;

	// private childThread ct = null;

	// 存储当前显示的相关监测点数据信息的描述
	/**
	 * 注：存储的是已经获取到的有空气质量监测数据的城市点（不一定在显示范围内）
	 */
	public static List<AQIPoint> _aqiCitys = new ArrayList<AQIPoint>();
	public static List<AQIPoint> _aqiPoints = new ArrayList<AQIPoint>();
	private static List<City> _citys = new ArrayList<City>();
	// private static List<City> _maincitys = new ArrayList<City>();
	// private static List<Province> _provinces = new ArrayList<Province>();

	/**
	 * 保存的是渲染瓦片时用的数据，添加删除时均按城市为单位一组组操作
	 */
	// public static List<AQIPoint> renderAQIPoints = new ArrayList<AQIPoint>();

	public static List<String> aqiCitySendToServer = new ArrayList<String>();
	public static List<String> weatherCitySendToServer = new ArrayList<String>();

	// 标识函数是否是在类内部调用
	private boolean showTileOnMapRunInClass = false;

	// 当前处理的数据来源类型
	public static PollutantTypeEnum pollType = null;
	// 标识mapview是否从onResume状态切换回来
	public boolean mapOnResume = false;

	// map相关参数
	private static MapView _view;
	private LatLng _mapcenter = null;
	private float _mapOverLooking = 0;
	private float _mapRotation = 0;
	private float _zoomLevel = 0;// 记录的是当前所切瓦片的等级

	public static double mapLeft = 0;
	public static double mapRight = 0;
	public static double maptop = 0;
	public static double mapBottom = 0;

	public static double myMapLeft = 0;
	public static double myMapRight = 0;
	public static double myMaptop = 0;
	public static double myMapBottom = 0;

	// 地图渲染相关变量
	private GroundOverlay mGroundOverlay;
	private GroundOverlayOptions mGround;
	// private Bitmap _bitmap;
	// 保存上一次地图操作后的地图显示中心坐标点
	private LatLng preMapCenter = null;
	// 保存当前中心瓦片状态
	private LatLng centerGroundCenter = null;// 在渲染的九宫格中，实时处于中心的瓦片的中心点。
	private LatLng centerGroundLeftBottom = null;
	private LatLng centerGroundRightTop = null;
	private double centerGroundLaSpan = 0;
	private double centerGroundLoSpan = 0;
	List<AQIPoint> aqiPointsAdd = new ArrayList<AQIPoint>();
	GetMonitoringAqiTask getMonitoringAqiTask = new GetMonitoringAqiTask();

	// 用来引用在地图所在的activity所实现的句柄
	private static Handler _outHandler;

	// 地图事件接口
	// MKMapViewListener mapViewListener = null;
	// MKMapTouchListener mapTouchListener = null;
	// MKMapStatusChangeListener mapStatusChangeListener = null;
	private boolean listenIsReg = false;

	// 停止类内相关的功能操作
	public static boolean _start = true;

	public static boolean firstshowtile = false;// 控制初次开启显示渲染的功能
	private String[] cityArrays = new String[] { "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河",
			"三门峡", "南阳", "商丘", "信阳", "周口", "驻马店", "济源", "巩义", "兰考县", "汝州", "滑县", "长垣县", "邓州", "永城", "固始县", "鹿邑县",
			"新蔡县" };
	private String pinjieCity = "";
	private String pinjieAllCity = "";
	public static List<AQIPoint> _stationResult = new ArrayList<AQIPoint>();
	public static List<AQIPoint> cityResult = new ArrayList<AQIPoint>();
	public static List<AQIPoint> provinceResult = new ArrayList<AQIPoint>();
	private float firstView = 11;
	private float nextView = 12;
	private boolean zoomFlag = false;

	Timer timer = new Timer();

	public TianQiZoomLevelEnum getEmun_tqZoomLevel() {
		return emun_tqZoomLevel;
	}

	public void setEmun_tqZoomLevel(TianQiZoomLevelEnum emun_tqZoomLevel) {
		this.emun_tqZoomLevel = emun_tqZoomLevel;
	}

	public TileControl() {

	}

	private Context context;

	public TileControl(Context context, MapView mapview, Handler mainhandler) {

		super();
		Log.v("function-tilecontrol-1", System.currentTimeMillis() + "");
		this.context = context;
		if (mapview == null) {
			return;
		}
		// if (getMonitoringAqiTask.getStatus() !=
		// com.pds.environment.controls.AsyncTask.Status.RUNNING) {
		// getMonitoringAqiTask.execute("");
		// }
		if (getMonitoringAqiTask.getStatus() != com.jy.environment.controls.AsyncTask.Status.RUNNING) {
			GetMonitoringAqiTask getMonitoringAqiTask = new GetMonitoringAqiTask();
			getMonitoringAqiTask.execute("");
		}
		List<AQIPoint> aqipointFromDB = WbMapUtil.getAQICityInMapExtentFromDB(0, 0, 0, 0);
		pinjieCity = "";
		for (int i = 0; i < aqipointFromDB.size(); i++) {
			String city = aqipointFromDB.get(i).getCity();
			String jiancedian = aqipointFromDB.get(i).getJiancedian();
			pinjieAllCity += "," + jiancedian;
			if (isContains(jiancedian, cityArrays)) {
				continue;
			}
			pinjieCity += "," + jiancedian;
		}
		UpdateAQIPointTask updateAQIPointTask = new UpdateAQIPointTask();
		updateAQIPointTask.execute("");
		String url = UrlComponent.AQIqueryURL_V2_POST + UrlComponent.token;
		if (JsonUtils.aqicitys_fromserver != null && JsonUtils.aqicitys_fromserver.size() > 0) {
			cityResult = JsonUtils.aqicitys_fromserver;
		} else {
			UpdateAQIcityTask task = new UpdateAQIcityTask();
			task.execute(url, pinjieCity);
		}
		getMonitoringAqiTask = new GetMonitoringAqiTask();
		getMonitoringAqiTask.execute("");
		updateAQIMapPoint();
		_view = mapview;
		_mapOverLooking = _view.getMap().getMapStatus().overlook;
		_mapRotation = _view.getMap().getMapStatus().rotate;
		// _view.getMaxZoomLevel();
		// _view.getMinZoomLevel();
		_zoomLevel = _view.getMap().getMapStatus().zoom;
		_mapcenter = _view.getMap().getMapStatus().target;
		// 初始化一些变量
		if (_zoomLevel >= 3.0 && _zoomLevel < 7.0)// 显示到省
		{
			emun_tqZoomLevel = TianQiZoomLevelEnum.PROVINCE;
		} else if (_zoomLevel >= 7.0 && _zoomLevel < 10.0) {
			emun_tqZoomLevel = TianQiZoomLevelEnum.MAINCITY;
		} else if (_zoomLevel >= 10.0) {
			emun_tqZoomLevel = TianQiZoomLevelEnum.CITY;
		}

		if (_zoomLevel >= 3.0 && _zoomLevel < 11.0)// 显示到省
		{
			emun_aqiZoomLevel = AQIZoomLevelEmun.CITYLEVEL;
		} else if (_zoomLevel >= 11.0) {
			emun_aqiZoomLevel = AQIZoomLevelEmun.POINTLEVEL;
		}

		_outHandler = mainhandler;

		new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ADD_TILE:

					RenderResultData rrdata = (RenderResultData) msg.obj;

					// 消除瓦片之间的间隙，方法是放大每一张瓦片的贴图范围，扩大1%
					LatLng lb = rrdata.get_rblock().getLeftBottomPT();
					LatLng rt = rrdata.get_rblock().getRightTopPT();

					if (lb.latitude >= rt.latitude || lb.longitude >= rt.longitude) {
						return;
					}
					// mGround = new Ground((Bitmap) rrdata.get_bitmap(),
					// lb,
					// rt);
					LatLngBounds bounds = new LatLngBounds.Builder().include(rt).include(lb).build();
					// 定义Ground显示的图片
					BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap((Bitmap) rrdata.get_bitmap());
					// 定义Ground覆盖物选项
					OverlayOptions ooGround = new GroundOverlayOptions().positionFromBounds(bounds).image(bdGround)
							.transparency(0.8f);

					// 添加渲染图
					// mGroundOverlay = new GroundOverlay(_view);
					// mGroundOverlay.addGround(mGround);
					// _view.getOverlays().add(mGroundOverlay);
					// _view.refresh();
					_view.getMap().addOverlay(ooGround);
					// sodu.setGround(rrdata.get_rblock().get_relativePos(),
					// mGroundOverlay);

					rrdata.get_bitmap().recycle();
					break;
				case DELETE_TILE:
					break;
				default:
					break;
				}
			}

		};
		initlistener();// 暂时注释，放在初次加载显示渲染或气泡的时候进行注册；
		// ct = new childThread();
		// ct.start();
		Log.v("function-tilecontrol-2", System.currentTimeMillis() + "");

	}

	public void endChildTh() {

	}

	/**
	 * 在地图上添加指定类型的渲染图
	 * 
	 * @param em
	 *            渲染类型，PollutantTypeEnum中的类型
	 */
	public void ShowTileOnMap(PollutantTypeEnum em, List<AQIPoint> jw) {

		if (em != null) {

		} else {

		}

		if (_start == false)
			return;
		if (pollType == null) {

		} else {

		}

		if (pollType == em && showTileOnMapRunInClass == false && mapOnResume == false) {

			return;
		}
		_aqiPoints = jw;

		pollType = em;
		showTileOnMapRunInClass = false;
		mapOnResume = false;
		// 更新中心瓦片状态!!!!!!暂未考虑旋转和倾斜
		centerGroundCenter = _view.getMap().getMapStatus().target;

		// 改用地图显示范围四个角点坐标，取外接矩形
		LatLng pt00 = _view.getMap().getProjection().fromScreenLocation(new android.graphics.Point(0, 0));
		LatLng ptw0 = _view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(_view.getWidth(), 0));
		LatLng pt0h = _view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(0, _view.getHeight()));
		LatLng ptwh = _view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(_view.getWidth(), _view.getHeight()));
		centerGroundLaSpan = Math.max(pt00.latitude, Math.max(ptw0.latitude, Math.max(pt0h.latitude, ptwh.latitude)))
				- Math.min(pt00.latitude, Math.min(ptw0.latitude, Math.min(pt0h.latitude, ptwh.latitude)));
		centerGroundLoSpan = Math.max(pt00.longitude,
				Math.max(ptw0.longitude, Math.max(pt0h.longitude, ptwh.longitude)))
				- Math.min(pt00.longitude, Math.min(ptw0.longitude, Math.min(pt0h.longitude, ptwh.longitude)));

		centerGroundLeftBottom = new LatLng(centerGroundCenter.latitude - centerGroundLaSpan / 2,
				centerGroundCenter.longitude - centerGroundLoSpan / 2);
		centerGroundRightTop = new LatLng(centerGroundCenter.latitude + centerGroundLaSpan / 2,
				centerGroundCenter.longitude + centerGroundLoSpan / 2);
		_view.getMap().getProjection().fromScreenLocation(new android.graphics.Point(0, 0));
		_view.getMap().getProjection().fromScreenLocation(new android.graphics.Point(_view.getWidth(), 0));
		_view.getMap().getProjection().fromScreenLocation(new android.graphics.Point(0, _view.getHeight()));
		_view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(_view.getWidth(), _view.getHeight()));

		if (!listenIsReg) {
			initlistener();
		}
		// 当地图放大到一定范围后再渲染空气质量瓦片
		// if(_view.getZoomLevel()<10.0)
		// {
		// return;
		// }
		// render(RelativeTileEnum.CENTER );
		// render(RelativeTileEnum.LEFT);
		// render(RelativeTileEnum.LEFT_BOTTOM);
		// render(RelativeTileEnum.LEFT_TOP);
		// render(RelativeTileEnum.RIGHT);
		// render(RelativeTileEnum.RIGHT_BOTTOM);
		// render(RelativeTileEnum.RIGHT_TOP);
		// render(RelativeTileEnum.TOP);
		// render(RelativeTileEnum.BOTTOM);
		// 根据类型加载指定的数据
		if (em == null) {
			return;
		}
		switch (em) {
		case AQI:
			break;
		case CO:
			break;
		case NO2:
			break;
		case O3:
			break;
		case PM10:
			break;
		case pm25:
			break;
		case SO2:
			break;
		default:
			break;
		}

	}

	public void pause() {
		_start = false;
	}

	public void restart() {
		_start = true;
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
	 * 当地图要显示的环境 信息类型发生变化时，就更新数据并显示到地图上：
	 * 
	 * @param
	 * 
	 * 
	 */
	public void updateMapPoints() {

		updateCenterGroundPara();
		if (emun_envtype == null) {
			Log.v("function-err", "emun_envtype is null");
			return;
		}
		Log.v("function", "updateMapPoints " + emun_envtype.toString());
		try {
			utpMapExtPara();
		} catch (Exception e) {
			Log.v("tilecontrol-updatemappoints-try-e", e.toString());
		}
		if (emun_envtype == null) {
			return;
		}
		switch (emun_envtype) {
		case AQI:
		case PM25:
		case PM10:
		case NO2:
		case SO2:
		case CO:
		case O3:
			// _view.getMap().clear();
			updateAQIMapPoint();// 是否显示渲染是在该函数中控制
			break;
		case TIANQI:
			_view.getMap().clear();
			updateWeatherMapPoints();
			break;
		case SHARE_PICTURE:
			_view.getMap().clear();
			updateUserPicture();
			break;
		case MAI:
			break;
		case NULL:
			break;
		case QIWEN:
			break;
		case TRAFFIC:
			break;
		case WU:
			break;
		case SURFACE_WATER:
			updateSurfaceWaterMapPoints();
			break;
		case SOURCE_OF_POLLUTION:
			updatePullotionMapPoints();
			break;
		default:
			break;
		}

	}

	/**
	 * 刷新显示用户分享图片在地图上
	 */
	private void updateUserPicture() {
		//
		Message toMapActivity = _outHandler.obtainMessage();
		toMapActivity.what = 10;//
		_outHandler.sendMessage(toMapActivity);
	}

	/**
	 * 
	 * 是否显示aqi值
	 */
	private void updateAQIMapPoint() {
		emun_envtype_Old = emun_envtype;
		if (_view == null) {
			return;
		}
		double level = (double) _view.getMap().getMapStatus().zoom;

		if (level >= 3.0 && level < 11.0)// 显示城市级
		{
			if (emun_aqiZoomLevel == AQIZoomLevelEmun.CITYLEVEL) {
				if (aqiTypeChanged) {
					aqiTypeChanged = false;

				} else {
					emun_aqiZoomLevel = AQIZoomLevelEmun.CITYLEVEL;
				}
			}
			boolean isfirstcity = true;
		}

	}

	private void updateWeatherMapPoints() {
		emun_envtype_Old = emun_envtype;
		Log.v("function", "updateWeatherMapPoints");
		/*********************************************
		 * 以下是针对气泡标注等处理显示操作
		 * 
		 * 首先准备三个寄存 数据列表： 1 保存当前已经显示的城市列表 保存当前已经显示的省列表 2 当前已经显示的监测站点 3 当前地图参数
		 * 
		 *********************************************/
		double level = (double) _view.getMap().getMapStatus().zoom;

		_view.getMap().clear();

		// 1 判断地图显示level,分省级和市级
		if (level >= 3.0 && level < 7.0)// 显示到省
		{
			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_PROVINCE)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.PROVINCE) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.PROVINCE;
				// updateMapLayer();
			}

			// 从数据库中读取显示范围 中的点
			List<Province> provinceFromDB = WbMapUtil.getProvinceWeatherInMapExtentFromDB((int) mapLeft, (int) mapRight,
					(int) mapBottom, (int) maptop);

			// provinceFromDB中判断天气数据是否已经过时，
			// 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// 如果 过时，调用子线程执行服务器更新（数据库和地图显示）

			List<Province> provinces_uselocal = new ArrayList<Province>();// 在有效时间内的天气点数据
			List<Province> provinces_toserver = new ArrayList<Province>();// 请求服务器要更新的天气点数据
			String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			boolean isthefirstcity = true;
			for (int i = 0; i < provinceFromDB.size(); i++) {
				Date nowdate = new Date();
				Date olddate = provinceFromDB.get(i).getWeatherUpdateTime();
				double dhour = (nowdate.getTime() - olddate.getTime()) / (60 * 60 * 1000);
				if (dhour < 3.0) {

					provinces_uselocal.add(provinceFromDB.get(i));

				} else if (dhour >= 3.0) {

					provinces_toserver.add(provinceFromDB.get(i));

					CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
					// String ccode =
					// citydb.getCityCode(provinceFromDB.get(i).getCity());
					String name = provinceFromDB.get(i).getCity();
					if (name.contains("自治州")) {
						name = citydb.getSuoSuo(name);
					}
					if (isthefirstcity) {
						citys = name;
						isthefirstcity = false;
					} else {
						citys += "," + name;
					}

				}
			}

			// 根据对provincefromdb中数据进行分类的情况分别执行
			// 1 provinces_uselocal 直接显示到地图上(不直接显示了，传递给异步请求，一块显示)
			// 2 provinces_toserver 请求服务器后再显示到地图上
			// Message toMapActivity = _outHandler.obtainMessage();
			// toMapActivity.what = 2;//
			// toMapActivity.obj = provinces_uselocal;
			// _outHandler.sendMessage(toMapActivity);
			Log.v("tianqi-1", citys);
			if (citys.equals(""))// 说明没有要请求服务器的 所以直接显示到地图上
			{
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 2;//
				toMapActivity.obj = provinces_uselocal;
				_outHandler.sendMessage(toMapActivity);
			} else {
				String url = UrlComponent.weatherURL_V2 + citys + UrlComponent.token;
				MyLog.i(">>>>>>>>>>>>>uuuruu" + url);
				UpdateProvinceWeatherTask task = new UpdateProvinceWeatherTask();
				task.execute(url, provinces_uselocal);
				waitingurl = url;
				Log.v("tianqi-2", url);
			}

		} else if (level >= 7.0 && level < 10.0)// 显示到地级市
		{

			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_MAINCITY)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.MAINCITY) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.MAINCITY;
				// updateMapLayer();

			}

			// 从数据库中读取显示范围 中的点
			List<City> maincityFromDB = WbMapUtil.getMaincityWeatherInMapExtentFromDB((int) mapLeft, (int) mapRight,
					(int) mapBottom, (int) maptop);

			// maincityFromDB中判断天气数据是否已经过时，
			// 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// 如果 过时，调用子线程执行服务器更新（数据库和地图显示）
			List<City> maincitys_uselocal = new ArrayList<City>();// 在有效时间内的天气点数据
			List<City> maincitys_toserver = new ArrayList<City>();// 请求服务器要更新的天气点数据
			String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			boolean isthefirstcity = true;
			for (int i = 0; i < maincityFromDB.size(); i++) {
				Date nowdate = new Date();
				Date olddate = maincityFromDB.get(i).getWeatherUpdateDate();
				double dhour = (nowdate.getTime() - olddate.getTime()) / (60 * 60 * 1000);
				if (dhour < 3.0) {
					// 显示到地图上,并添加到_maincitys中
					// Message toMapActivity = _outHandler.obtainMessage();
					// toMapActivity.what = 5;//
					// toMapActivity.obj = maincityFromDB.get(i);
					// _outHandler.sendMessage(toMapActivity);
					maincitys_uselocal.add(maincityFromDB.get(i));
					// _maincitys.add(maincityFromDB.get(i));

				} else if (dhour >= 3.0) {

					// //更新数据并，显示到地图上,并添加到_maincitys中
					// //调用
					// 检查是否已经向服务器发送过请求
					// String cityname = maincityFromDB.get(i).getName();
					// boolean isHave= false;
					// for(int j=0;j<weatherCitySendToServer.size();j++)
					// {
					// if(weatherCitySendToServer.get(j).equals(cityname))
					// {
					// isHave=true;
					// }
					// }
					// if(isHave)
					// {
					// return;
					// }
					maincitys_toserver.add(maincityFromDB.get(i));

					CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
					// String ccode =
					// citydb.getCityCode(maincityFromDB.get(i).getName());
					String name = maincityFromDB.get(i).getName();
					if (name.contains("自治州")) {
						name = citydb.getSuoSuo(name);
					}
					if (isthefirstcity) {
						citys = name;
						isthefirstcity = false;
					} else {
						citys += "," + name;
					}

					// weatherCitySendToServer.add(cityname);
				}
			}
			if (citys.equals("")) {
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 5;//
				toMapActivity.obj = maincitys_uselocal;
				_outHandler.sendMessage(toMapActivity);
			} else {
				String url = UrlComponent.weatherURL_V2 + citys + UrlComponent.token;
				MyLog.i(">>>>>>>>>>>>>uuuruu" + url);
				UpdateMainCityWeatherTask task = new UpdateMainCityWeatherTask();
				task.execute(url, maincitys_uselocal);
				waitingurl = url;
			}

		} else if (level >= 10.0)// 显示到地级市以下 && showPointType==dataType
		{

			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_CITY)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.CITY) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.CITY;
				// updateMapLayer();
			}

			// 从数据库中读取显示范围 中的点
			List<City> cityFromDB = WbMapUtil.getcityWeatherInMapExtentFromDB((int) mapLeft, (int) mapRight,
					(int) mapBottom, (int) maptop);

			// cityFromDB中判断天气数据是否已经过时，
			// 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// 如果 过时，调用子线程执行服务器更新（数据库和地图显示）
			List<City> citys_uselocal = new ArrayList<City>();// 在有效时间内的天气点数据
			List<City> citys_toserver = new ArrayList<City>();// 请求服务器要更新的天气点数据
			String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			boolean isthefirstcity = true;
			for (int i = 0; i < cityFromDB.size(); i++) {
				Date nowdate = new Date();
				Date olddate = cityFromDB.get(i).getWeatherUpdateDate();
				double dhour = (nowdate.getTime() - olddate.getTime()) / (60 * 60 * 1000);
				if (dhour < 3.0) {
					// 显示到地图上,并添加到_citys中
					// Message toMapActivity = _outHandler.obtainMessage();
					// toMapActivity.what = 7;//
					// toMapActivity.obj = cityFromDB.get(i);
					// _outHandler.sendMessage(toMapActivity);
					citys_uselocal.add(cityFromDB.get(i));
					// _maincitys.add(cityFromDB.get(i));

				} else if (dhour >= 3.0) {

					// //更新数据并，显示到地图上,并添加到_citys中
					// //调用

					citys_toserver.add(cityFromDB.get(i));

					CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
					// String ccode =
					// citydb.getCityCode(cityFromDB.get(i).getName());
					String name = cityFromDB.get(i).getName();
					if (name.contains("自治州")) {
						name = citydb.getSuoSuo(name);
					}
					if (isthefirstcity) {
						citys = name;
						isthefirstcity = false;
					} else {
						citys += "," + name;
					}

				}
			}
			if (citys.equals("")) {
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 7;//
				toMapActivity.obj = citys_uselocal;
				_outHandler.sendMessage(toMapActivity);
			} else {
				String url = UrlComponent.weatherURL_V2 + citys + UrlComponent.token;
				Log.v("function-url-xianjishi", url);
				UpdateCityWeatherTask task = new UpdateCityWeatherTask();
				task.execute(url, citys_uselocal);
				waitingurl = url;
			}

		} else {
			Log.v("tianqi-return", "return");
			return;
		}

		// 3 根据范围查询数据库获取当前范围内的城市列表，没有的进行添加（添加的同时，判断、更新天气情况）
		// ，并显示
	}

	/**
	 * 
	 */
	private void updateSurfaceWaterMapPoints() {
		emun_envtype_Old = emun_envtype;
		/*********************************************
		 * 以下是针对气泡标注等处理显示操作
		 * 
		 * 首先准备三个寄存 数据列表： 1 保存当前已经显示的城市列表 保存当前已经显示的省列表 2 当前已经显示的监测站点 3 当前地图参数
		 * 
		 *********************************************/
		double level = (double) _view.getMap().getMapStatus().zoom;
		Log.v("function", "updateSurfaceWaterMapPoints" + level);
		// _view.getMap().clear();
		// 1 判断地图显示level,分省级和市级
		if (level >= 3.0 && level < 4.0)// 显示到省
		{
			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_PROVINCE)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.PROVINCE) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.PROVINCE;
				updateMapLayer();
			}
		}
		if (level >= 4.0 && level < 10.0)// 显示到地级市
		{

			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_MAINCITY)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.MAINCITY) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.MAINCITY;
				// updateMapLayer();

			}
			Log.v("function", "mapLeft" + myMapLeft);
			Log.v("function", "mapRight" + myMapRight);
			Log.v("function", "maptop" + myMaptop);
			Log.v("function", "mapBottom" + myMapBottom);

			// // 从数据库中读取显示范围 中的点
			// List<City> maincityFromDB =
			// WbMapUtil.getcityWeatherInMapExtentFromDB(
			// (int) mapLeft, (int) mapRight, (int) mapBottom,
			// (int) maptop);
			List<City> maincityFromDB = WbMapUtil.getcityInMapExtentFromDB(myMapBottom, myMaptop, myMapLeft,
					myMapRight);

			for (int i = 0; i < maincityFromDB.size(); i++) {
				Log.v("function", "cityFromDB" + maincityFromDB.get(i).toString());
			}
			// // 从数据库中读取显示范围 中的点
			// List<City> maincityFromDB = WbMapUtil
			// .getMaincityWeatherInMapExtentFromDB((int) mapLeft,
			// (int) mapRight, (int) mapBottom, (int) maptop);
			// maincityFromDB中判断天气数据是否已经过时，
			// 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// 如果 过时，调用子线程执行服务器更新（数据库和地图显示）
			List<City> maincitys_uselocal = new ArrayList<City>();// 在有效时间内的天气点数据
			List<City> maincitys_toserver = new ArrayList<City>();// 请求服务器要更新的天气点数据
			String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			boolean isthefirstcity = true;
			for (int i = 0; i < maincityFromDB.size(); i++) {
				Date nowdate = new Date();
				Date olddate = maincityFromDB.get(i).getSurfaceWaterUpdateDate();
				double dhour = (nowdate.getTime() - olddate.getTime()) / (60 * 60 * 1000);
				if (dhour < 24.0 * 30) {
					// 显示到地图上,并添加到_maincitys中
					maincitys_uselocal.add(maincityFromDB.get(i));

				} else if (dhour >= 24.0 * 30) {
					// //更新数据并，显示到地图上,并添加到_maincitys中
					maincitys_toserver.add(maincityFromDB.get(i));
					String name = maincityFromDB.get(i).getName();
					if (isthefirstcity) {
						citys = name;
						isthefirstcity = false;
					} else {
						citys += "," + name;
					}

					// weatherCitySendToServer.add(cityname);
				}
			}
			if (citys.equals("")) {
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = MapMianActivity.SURFACE_WATER_HANDLER_OLD_SQL_TAG;//
				List<SurfaceWaterModel> listAllSurfaceWaterModels = new ArrayList<SurfaceWaterModel>();
				for (int i = 0; i < maincitys_uselocal.size(); i++) {
					List<SurfaceWaterModel> listCityData = citydb
							.getSurfaceWaterPointsByCity(maincitys_uselocal.get(i).getName());
					listAllSurfaceWaterModels.addAll(listCityData);
				}
				toMapActivity.obj = listAllSurfaceWaterModels;
				_outHandler.sendMessage(toMapActivity);
			} else {
				String url = UrlComponent.surfaceWaterURL_V2 + UrlComponent.token;
				MyLog.i(">>>>>>>>>>>>>uuuruug" + url);
				MyLog.i("baiMap url:" + url);
				UpdateMainCitySurfaceWaterTask task = new UpdateMainCitySurfaceWaterTask();
				List<SurfaceWaterModel> listAllSurfaceWaterModels = new ArrayList<SurfaceWaterModel>();
				for (int i = 0; i < maincitys_uselocal.size(); i++) {
					List<SurfaceWaterModel> listCityData = citydb
							.getSurfaceWaterPointsByCity(maincitys_uselocal.get(i).getName());
					listAllSurfaceWaterModels.addAll(listCityData);
				}
				task.execute(citys, listAllSurfaceWaterModels);
				waitingurl = url;
			}

		} else if (level >= 10.0)// 显示到地级市以下 && showPointType==dataType
		{

			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_CITY)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.CITY) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.CITY;
				// updateMapLayer();
			}

			Log.v("function", "mapLeft" + mapLeft);
			Log.v("function", "mapRight" + mapRight);
			Log.v("function", "maptop" + maptop);
			Log.v("function", "mapBottom" + mapBottom);

			// 从数据库中读取显示范围 中的点
			List<City> cityFromDB = WbMapUtil.getcityWeatherInMapExtentFromDB((int) mapLeft, (int) mapRight,
					(int) mapBottom, (int) maptop);

			for (int i = 0; i < cityFromDB.size(); i++) {
				Log.v("function", "cityFromDB" + cityFromDB.get(i).toString());
			}

			// cityFromDB中判断天气数据是否已经过时，
			// 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// 如果 过时，调用子线程执行服务器更新（数据库和地图显示）
			List<City> citys_uselocal = new ArrayList<City>();// 在有效时间内的天气点数据
			List<City> citys_toserver = new ArrayList<City>();// 请求服务器要更新的天气点数据
			String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			boolean isthefirstcity = true;
			CityDB cityDB = WeiBaoApplication.getInstance().getCityDB();
			for (int i = 0; i < cityFromDB.size(); i++) {
				Date nowdate = new Date();
				Date olddate = cityFromDB.get(i).getSurfaceWaterUpdateDate();
				double dhour = (nowdate.getTime() - olddate.getTime()) / (60 * 60 * 1000);
				if (dhour < 3.0) {
					// 显示到地图上,并添加到_citys中
					citys_uselocal.add(cityFromDB.get(i));

				} else if (dhour >= 3.0) {
					// //更新数据并，显示到地图上,并添加到_citys中
					citys_toserver.add(cityFromDB.get(i));
					String name = cityFromDB.get(i).getName();
					if (isthefirstcity) {
						citys = name;
						isthefirstcity = false;
					} else {
						citys += "," + name;
					}

				}
			}
			if (citys.equals("")) {
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = MapMianActivity.SURFACE_WATER_HANDLER_OLD_SQL_TAG;//
				List<SurfaceWaterModel> listAllSurfaceWaterModels = new ArrayList<SurfaceWaterModel>();
				for (int i = 0; i < citys_uselocal.size(); i++) {
					String city = citys_uselocal.get(i).getName();
					if (city.contains("自治州")) {
						city = cityDB.getSuoSuo(city);
					}
					List<SurfaceWaterModel> listCityData = citydb.getSurfaceWaterPointsByCity(city);
					listAllSurfaceWaterModels.addAll(listCityData);
				}
				toMapActivity.obj = listAllSurfaceWaterModels;
				_outHandler.sendMessage(toMapActivity);
			} else {
				// String url = UrlComponent.surfaceWaterURL_V2 + citys
				// + UrlComponent.token;
				// MyLog.i(">>>>>>>>>>>>>uuuruu" + url);
				// UpdateMainCitySurfaceWaterTask task = new
				// UpdateMainCitySurfaceWaterTask();
				// task.execute(url, maincitys_uselocal);
				// waitingurl = url;
				String url = UrlComponent.surfaceWaterURL_V2 + UrlComponent.token;
				MyLog.i(">>>>>>>>>>>>>uuuruug" + url);
				MyLog.i("baiMap url:" + url);
				UpdateMainCitySurfaceWaterTask task = new UpdateMainCitySurfaceWaterTask();

				List<SurfaceWaterModel> listAllSurfaceWaterModels = new ArrayList<SurfaceWaterModel>();
				for (int i = 0; i < citys_uselocal.size(); i++) {
					String city = citys_uselocal.get(i).getName();
					if (city.contains("自治州")) {
						city = cityDB.getSuoSuo(city);
					}
					List<SurfaceWaterModel> listCityData = citydb.getSurfaceWaterPointsByCity(city);
					listAllSurfaceWaterModels.addAll(listCityData);
				}

				task.execute(citys, listAllSurfaceWaterModels);
				waitingurl = url;
			}

		} else {
			Log.v("tianqi-return", "return");
			return;
		}

		// 3 根据范围查询数据库获取当前范围内的城市列表，没有的进行添加（添加的同时，判断、更新天气情况）
		// ，并显示
	}

	/**
	 * 
	 */
	private void updatePullotionMapPoints() {
		Log.v("function", "updatePullotionMapPoints");
		emun_envtype_Old = emun_envtype;
		/*********************************************
		 * 以下是针对气泡标注等处理显示操作
		 * 
		 * 首先准备三个寄存 数据列表： 1 保存当前已经显示的城市列表 保存当前已经显示的省列表 2 当前已经显示的监测站点 3 当前地图参数
		 * 
		 *********************************************/
		double level = (double) _view.getMap().getMapStatus().zoom;
		MyLog.i(">>>>>>>>>>>Level" + level);
		// _view.getMap().clear();
		// 1 判断地图显示level,分省级和市级
		if (level >= 3.0 && level < 7.0)// 显示到省
		{
			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_PROVINCE)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.PROVINCE) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.PROVINCE;
				updateMapLayer();
				_view.getMap().clear();
				Toast.makeText(context, "放大后可展示污染源", 100).show();
			}
		}
		if (level >= 7.0 && level < 10.0)// 显示到地级市
		{
			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_MAINCITY)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.MAINCITY) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.MAINCITY;
				updateMapLayer();
				_view.getMap().clear();
				Toast.makeText(context, "放大后可展示污染源", 100).show();

			}
			// // 从数据库中读取显示范围 中的点
			// List<City> maincityFromDB = WbMapUtil
			// .getMaincityWeatherInMapExtentFromDB((int) mapLeft,
			// (int) mapRight, (int) mapBottom, (int) maptop);
			// // maincityFromDB中判断天气数据是否已经过时，
			// // 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// // 如果 过时，调用子线程执行服务器更新（数据库和地图显示）
			// List<City> maincitys_uselocal = new ArrayList<City>();//
			// 在有效时间内的天气点数据
			// List<City> maincitys_toserver = new ArrayList<City>();//
			// 请求服务器要更新的天气点数据
			// String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			// boolean isthefirstcity = true;
			// for (int i = 0; i < maincityFromDB.size(); i++) {
			// Date nowdate = new Date();
			// Date olddate = maincityFromDB.get(i).getPollutionUpdateDate();
			// double dhour = (nowdate.getTime() - olddate.getTime())
			// / (60 * 60 * 1000);
			// if (dhour < 240.0) {
			// // 显示到地图上,并添加到_maincitys中
			// maincitys_uselocal.add(maincityFromDB.get(i));
			//
			// } else if (dhour >= 240.0) {
			// // //更新数据并，显示到地图上,并添加到_maincitys中
			// maincitys_toserver.add(maincityFromDB.get(i));
			// String name = maincityFromDB.get(i).getName();
			// if (isthefirstcity) {
			// citys = name;
			// isthefirstcity = false;
			// } else {
			// citys += "," + name;
			// }
			// }
			// }
			// if (citys.equals("")) {
			// Message toMapActivity = _outHandler.obtainMessage();
			// toMapActivity.what =
			// MapMianActivity.POLLUTION_HANDLER_OLD_SQL_TAG;//
			// List<PollutionPointModel> listAllSurfaceWaterModels = new
			// ArrayList<PollutionPointModel>();
			// for (int i = 0; i < maincitys_uselocal.size(); i++) {
			// List<PollutionPointModel> listCityData =
			// citydb.getPollutionPointByCity(maincitys_uselocal.get(i).getName());
			// listAllSurfaceWaterModels.addAll(listCityData);
			// }
			// toMapActivity.obj = listAllSurfaceWaterModels;
			// _outHandler.sendMessage(toMapActivity);
			// } else {
			// String url = UrlComponent.pullotion_URL_V2
			// + UrlComponent.token;
			// MyLog.i(">>>>>>>>>>>>>uuuruug" + url);
			// MyLog.i("baiMap url:" + url);
			// UpdateMainCityPollutionPointTask task = new
			// UpdateMainCityPollutionPointTask();
			// List<PollutionPointModel> listAllSurfaceWaterModels = new
			// ArrayList<PollutionPointModel>();
			// for (int i = 0; i < maincitys_uselocal.size(); i++) {
			// List<PollutionPointModel> listCityData =
			// citydb.getPollutionPointByCity(maincitys_uselocal.get(i).getName());
			// listAllSurfaceWaterModels.addAll(listCityData);
			// }
			// task.execute(citys, listAllSurfaceWaterModels);
			// waitingurl = url;
			// }

		} else if (level >= 10.0)// 显示到地级市以下 && showPointType==dataType
		{

			// 判断上一次显示的城市类型
			// if(tqLevelType == TQ_LEVEL_TYPE_CITY)
			if (emun_tqZoomLevel != TianQiZoomLevelEnum.CITY) {
				emun_tqZoomLevel = TianQiZoomLevelEnum.CITY;
				// updateMapLayer();
			}

			Log.v("function", "myMapBottom" + myMapBottom + "qqq myMaptop" + myMaptop + "qqq myMapRight" + myMapRight
					+ "qqqmyMapLeft" + myMapLeft);
			// // 从数据库中读取显示范围 中的点
			// List<City> cityFromDB =
			// WbMapUtil.getcityWeatherInMapExtentFromDB(
			// (int) mapLeft, (int) mapRight, (int) mapBottom,
			// (int) maptop);
			List<City> cityFromDB = WbMapUtil.getcityInMapExtentFromDB(myMapBottom, myMaptop, myMapLeft, myMapRight);

			for (int i = 0; i < cityFromDB.size(); i++) {
				Log.v("function", "cityFromDB" + cityFromDB.get(i).toString());
			}
			// cityFromDB中判断天气数据是否已经过时，
			// 如果不过时，直接显示至地图,天气数据每三个小时更新一次
			// 如果 过时，调用子线程执行服务器更新（数据库和地图显示）
			List<City> citys_uselocal = new ArrayList<City>();// 在有效时间内的天气点数据
			List<City> citys_toserver = new ArrayList<City>();// 请求服务器要更新的天气点数据
			String citys = "";// 用来保存要请求服务器的参数中的城市数组字符串
			boolean isthefirstcity = true;
			for (int i = 0; i < cityFromDB.size(); i++) {
				Date nowdate = new Date();
				Date olddate = cityFromDB.get(i).getPollutionUpdateDate();
				double dhour = (nowdate.getTime() - olddate.getTime()) / (60 * 60 * 1000);
				if (dhour < 24.0 * 30) {
					// 显示到地图上,并添加到_citys中
					MyLog.i("citys_uselocal.addcityFromDB.get(i) :" + cityFromDB.get(i));
					citys_uselocal.add(cityFromDB.get(i));

				} else if (dhour >= 24.0 * 30) {
					// //更新数据并，显示到地图上,并添加到_citys中
					citys_toserver.add(cityFromDB.get(i));
					MyLog.i("citys_toserver.addcityFromDB.get(i) :" + cityFromDB.get(i));
					String name = cityFromDB.get(i).getName();
					if (isthefirstcity) {
						citys = name;
						isthefirstcity = false;
					} else {
						citys += "," + name;
					}

				}
			}
			MyLog.i("citys :" + citys);
			for (int i = 0; i < citys_uselocal.size(); i++) {
				MyLog.i("citys_uselocal :" + citys_uselocal.get(i));
			}
			if (citys.equals("")) {
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = MapMianActivity.POLLUTION_HANDLER_OLD_SQL_TAG;//
				List<PollutionPointModel> listAllSurfaceWaterModels = new ArrayList<PollutionPointModel>();
				for (int i = 0; i < citys_uselocal.size(); i++) {
					String city = citys_uselocal.get(i).getName();
					if (city.contains("自治州")) {
						city = citydb.getSuoSuo(city);
					}
					List<PollutionPointModel> listCityData = citydb.getPollutionPointByCity(city);
					listAllSurfaceWaterModels.addAll(listCityData);
				}
				toMapActivity.obj = listAllSurfaceWaterModels;
				_outHandler.sendMessage(toMapActivity);
			} else {
				String url = UrlComponent.pullotion_URL_V2 + UrlComponent.token;
				MyLog.i(">>>>>>>>>>>>>uuuruug" + url);
				MyLog.i("baiMap url:" + url);
				UpdateMainCityPollutionPointTask task = new UpdateMainCityPollutionPointTask();
				List<PollutionPointModel> listAllSurfaceWaterModels = new ArrayList<PollutionPointModel>();
				for (int i = 0; i < citys_uselocal.size(); i++) {
					String city = citys_uselocal.get(i).getName();
					if (city.contains("自治州")) {
						city = citydb.getSuoSuo(city);
					}
					List<PollutionPointModel> listCityData = citydb.getPollutionPointByCity(city);
					listAllSurfaceWaterModels.addAll(listCityData);
				}
				task.execute(citys, listAllSurfaceWaterModels);
				waitingurl = url;

			}

		} else {
			Log.v("tianqi-return", "return");
			return;
		}

		// 3 根据范围查询数据库获取当前范围内的城市列表，没有的进行添加（添加的同时，判断、更新天气情况）
		// ，并显示
	}

	/**
	 * 目的是调用 hbdtactivity中的setpointlayer
	 */
	private void updateMapLayer() {
		Log.v("function", "updateMapLayer");
		Message toMapActivity = _outHandler.obtainMessage();
		toMapActivity.what = 9;//

		_outHandler.sendMessage(toMapActivity);
	}

	public void mapzoomFinish() {

		// 判断地图浏览操作是否超出中国范围
		if (isOutOfBrowerExt()) {
			// _view.getController().animateTo(preMapCenter);
			_view.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(preMapCenter));
			return;
		}
		preMapCenter = _view.getMap().getMapStatus().target;

		// //更新显示地图点
		updateMapPoints();

		if (_start == false)
			return;
		// 处理操作----首先判断执行的是什么操作
		// 拖动，绽放，旋转，倾斜
		if (_mapOverLooking != _view.getMap().getMapStatus().overlook) {
			_mapOverLooking = _view.getMap().getMapStatus().overlook;
		} else if (_mapRotation != _view.getMap().getMapStatus().rotate) {
			_mapRotation = _view.getMap().getMapStatus().rotate;
		} else if ((_zoomLevel != _view.getMap().getMapStatus().zoom)) {
			if (_view.getMap().getMapStatus().zoom < 5) {
				_view.getMap().setMapStatus(MapStatusUpdateFactory.zoomTo(5));
			}

			// 控制地图缩放范围在5-19

		}
		if (DistanceUtil.getDistance(_mapcenter, _view.getMap().getMapStatus().target) >= 0.001) {

		}
	}

	private void initlistener() {

		listenIsReg = true;
		/*********************
		 * 指定操作触发 的事件 1 点击：onmapclick,
		 * 双击：onmapDoubleClick,onMapAnimationFinish(缓冲动画),持续调用onMapStatusChange
		 * 2 长点击：onMapLongClick,onMapMoveFinish,onMapStatusChange 3
		 * 拖动：onMapMoveFinish,onMapAnimationFinish(缓冲动画),持续调用onMapStatusChange 4
		 * 绽放：onMapMoveFinish,持续调用onMapStatusChange 5
		 * 旋转：onMapMoveFinish,持续调用onMapStatusChange 6
		 * 倾斜：onMapMoveFinish,持续调用onMapStatusChange
		 * 
		 * zoom:最大比例尺level=19.0,全国范围大概level=5.0,最小比例尺level=3.0
		 */

		// 地图view事件
		final OnMapStatusChangeListener sclistener = new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {
				// TODO Auto-generated method stub

			}

			/**
			 * YYF 3.9.11 地图缩放完成时调用该方法
			 */
			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				// TODO Auto-generated method stub
				// 判断地图浏览操作是否超出中国范围
				double level = (double) _view.getMap().getMapStatus().zoom;
				_view.getMap().hideInfoWindow();
				MyLog.i(">>>>>>>>>>zoom" + level);
				if (isOutOfBrowerExt()) {
					// _view.getController().animateTo(preMapCenter);
					_view.getMap().setMapStatus(MapStatusUpdateFactory.newLatLng(preMapCenter));
					// return;
				}
				preMapCenter = _view.getMap().getMapStatus().target;
				// // //更新显示地图点
				// updateMapPoints();
				// _view.getMap().clear();
				if (level >= 3.0 && level < 9)// 显示城市级ff
				{
					if (zoomFlag)
						return;
					_view.getMap().clear();
					// Message msgMessage = _outHandler.obtainMessage();
					// msgMessage.what =29;
					// _outHandler.sendMessage(msgMessage);
					MyLog.i(">>>>>>>>>>>provinceResult" + provinceResult.size());
					Message cityMessage = _outHandler.obtainMessage();
					cityMessage.what = 6;
					cityMessage.obj = provinceResult;
					_outHandler.sendMessage(cityMessage);
					cityResult = JsonUtils.aqicitys_fromserver;
					if (cityResult != null && cityResult.size() > 0) {
						Thread thread = new Thread() {
							public void run() {
								for (int i = 0; i < cityResult.size(); i++) {
									List<AQIPoint> sinPoints = new ArrayList<AQIPoint>();
									sinPoints.add(cityResult.get(i));
									Message cityMessage = _outHandler.obtainMessage();
									cityMessage.what = 6;
									cityMessage.obj = sinPoints;
									_outHandler.sendMessage(cityMessage);
									try {
										sleep(30);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							};
						};
						thread.start();
						// List<AQIPoint> sinPoints = JsonUtils.sinPoints;
						// List<AQIPoint> zerPoints = JsonUtils.zerPoints;
						// List<AQIPoint> nextPoints = JsonUtils.nextPoints;
						// List<AQIPoint> fourPoints = JsonUtils.fourPoints;
						// List<AQIPoint> fivePoints = JsonUtils.fivePoints;
						// List<AQIPoint> sixPoints = JsonUtils.sinPoints;
						// List<AQIPoint> sevenPoints = JsonUtils.sevenPoints;
						// List<AQIPoint> eightPoints = JsonUtils.eightints;
						//
						// Message cityMessage = _outHandler.obtainMessage();
						// cityMessage.what = 15;
						// cityMessage.obj = zerPoints;
						// _outHandler.sendMessageDelayed(cityMessage,600);
						// Message cityMessage1 = _outHandler.obtainMessage();
						// cityMessage1.what = 19;
						// cityMessage1.obj = sinPoints;
						// _outHandler.sendMessageDelayed(cityMessage1,100);
						// Message cityMessage2 = _outHandler.obtainMessage();
						// cityMessage2.what = 20;
						// cityMessage2.obj = nextPoints;
						// _outHandler.sendMessageDelayed(cityMessage2,100);
						// Message cityMessage3 = _outHandler.obtainMessage();
						// cityMessage3.what = 21;
						// cityMessage3.obj = fourPoints;
						// _outHandler.sendMessageDelayed(cityMessage3,100);
						// Message cityMessage4 = _outHandler.obtainMessage();
						// cityMessage4.what = 22;
						// cityMessage4.obj = fivePoints;
						// _outHandler.sendMessageDelayed(cityMessage4,100);
						// Message cityMessage5 = _outHandler.obtainMessage();
						// cityMessage5.what = 23;
						// cityMessage5.obj = sixPoints;
						// _outHandler.sendMessageDelayed(cityMessage5,100);
						// Message cityMessage6 = _outHandler.obtainMessage();
						// cityMessage6.what = 24;
						// cityMessage6.obj = sevenPoints;
						// _outHandler.sendMessageDelayed(cityMessage6,100);
						// Message cityMessage7 = _outHandler.obtainMessage();
						// cityMessage7.what = 6;
						// cityMessage7.obj = eightPoints;
						// _outHandler.sendMessageDelayed(cityMessage7,100);
					}
					zoomFlag = true;
				} else if (level >= 11) {//显示省内监测点
					_view.getMap().clear();
					Message toMapActivity = _outHandler.obtainMessage();
					toMapActivity.what = 40;
					if (_stationResult != null && _stationResult.size() > 0) {
						toMapActivity.obj = _stationResult;
						_outHandler.sendMessage(toMapActivity);
					}
					zoomFlag = false;
				} else if (level > 10 && level < 11) {//显示省内城市级
					_view.getMap().clear();
					Message provincetoMapActivity = _outHandler.obtainMessage();
					provincetoMapActivity.what = 6;
					if (provinceResult != null && provinceResult.size() > 0) {
						provincetoMapActivity.obj = provinceResult;
						_outHandler.sendMessage(provincetoMapActivity);
					}
					zoomFlag = false;
				}
				if (_start == false)
					return;
				// 处理操作----首先判断执行的是什么操作
				// 拖动，绽放，旋转，倾斜
				if (_mapOverLooking != _view.getMap().getMapStatus().overlook) {
					_mapOverLooking = _view.getMap().getMapStatus().overlook;
				} else if (_mapRotation != _view.getMap().getMapStatus().rotate) {
					_mapRotation = _view.getMap().getMapStatus().rotate;
				} else if ((_zoomLevel != _view.getMap().getMapStatus().zoom)) {
					if (_view.getMap().getMapStatus().zoom < 5) {
						_view.getMap().setMapStatus(MapStatusUpdateFactory.zoomTo(5));
					}

				}
				if (DistanceUtil.getDistance(_mapcenter, _view.getMap().getMapStatus().target) >= 0.001) {

				}
			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {
				// TODO Auto-generated method stub
				// 更新相关状态数据
				LatLng pt00 = _view.getMap().getProjection().fromScreenLocation(new android.graphics.Point(0, 0));
				LatLng ptw0 = _view.getMap().getProjection()
						.fromScreenLocation(new android.graphics.Point(_view.getWidth(), 0));
				LatLng pt0h = _view.getMap().getProjection()
						.fromScreenLocation(new android.graphics.Point(0, _view.getHeight()));
				LatLng ptwh = _view.getMap().getProjection()
						.fromScreenLocation(new android.graphics.Point(_view.getWidth(), _view.getHeight()));
				if (pt00 == null) {
					return;
				}
				centerGroundLaSpan = Math.max(pt00.latitude,
						Math.max(ptw0.latitude, Math.max(pt0h.latitude, ptwh.latitude)))
						- Math.min(pt00.latitude, Math.min(ptw0.latitude, Math.min(pt0h.latitude, ptwh.latitude)));
				centerGroundLoSpan = Math.max(pt00.longitude,
						Math.max(ptw0.longitude, Math.max(pt0h.longitude, ptwh.longitude)))
						- Math.min(pt00.longitude, Math.min(ptw0.longitude, Math.min(pt0h.longitude, ptwh.longitude)));
				mapBottom = _view.getMap().getMapStatus().target.latitude - (double) centerGroundLaSpan / 2 + 0.4;// 加200000这个数是为了增加相关检索范围，aqi点是按城市中心点查的，放大后范围有时候包含不了中心点，相关aqi点就获取不到了。
				maptop = _view.getMap().getMapStatus().target.latitude + (double) centerGroundLaSpan / 2 + 0.4;
				mapLeft = _view.getMap().getMapStatus().target.longitude - (double) centerGroundLoSpan / 2 + 0.4;
				mapRight = _view.getMap().getMapStatus().target.longitude + (double) centerGroundLoSpan / 2 + 0.4;

				myMapBottom = ptwh.latitude;//
				myMaptop = pt00.latitude;
				myMapLeft = pt00.longitude;
				myMapRight = ptwh.longitude;

				// 发送消息修改指南针的方向
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 1;// 1--修改指南针的方向
				_outHandler.sendMessage(toMapActivity);
			}
		};

		// 地图点击事件设置
		final OnMapClickListener clistener = new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				Log.v("mappic-click", "onmappoiclick");
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				// 通知地图，如果面板处于打开状态就关掉。
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 10;//
				_outHandler.sendMessage(toMapActivity);

				// 清除地图分享点
				Message toMapActivity2 = _outHandler.obtainMessage();
				toMapActivity2.what = 4;//
				_outHandler.sendMessage(toMapActivity2);
			}
		};

		final OnMapDoubleClickListener dclistener = new OnMapDoubleClickListener() {

			@Override
			public void onMapDoubleClick(LatLng arg0) {
				// TODO Auto-generated method stub
				// 通知地图，如果面板处于打开状态就关掉。
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 10;//
				_outHandler.sendMessage(toMapActivity);

				if (_start == false)
					return;
			}
		};

		final OnMapLongClickListener lclistener = new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 3;//
				toMapActivity.obj = arg0;
				_outHandler.sendMessage(toMapActivity);
			}
		};

		// 地图加载完成事件
		OnMapLoadedCallback mlclistener = new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				Log.v("ttt", "加载事件注册成功");
				_view.getMap().setOnMapLongClickListener(lclistener);
				_view.getMap().setOnMapDoubleClickListener(dclistener);
				_view.getMap().setOnMapClickListener(clistener);
				_view.getMap().setOnMapStatusChangeListener(sclistener);
			}
		};
		_view.getMap().setOnMapLoadedCallback(mlclistener);
	}

	public void clearAllGroundOfMap() {

		MyLog.i("emun_envtype_Old :" + emun_envtype_Old);
		MyLog.i("emun_envtype :" + emun_envtype);
		// 新版代码测试
		// if(emun_envtype_Old == emun_envtype){
		// return;
		// }
		_view.getMap().clear();

	}

	/**
	 * @param lays
	 *            示例："1,2,5,6,7"
	 */
	// private void deleteGroundLay(String lays)
	// {
	// if(_start==false)
	// return;
	// String[] strArr = lays.split(",");
	// for (int i=0; i<strArr.length;i++)
	// {
	// int val = Integer.parseInt(strArr[i]);
	// switch(val)
	// {
	// case 0:
	// _view.getOverlays().remove(sodu.getg0());
	// sodu.setGroundIsNull(0);
	// break;
	// case 1:
	// _view.getOverlays().remove(sodu.getg1());
	// sodu.setGroundIsNull(1);
	// break;
	// case 2:
	// _view.getOverlays().remove(sodu.getg2());
	// sodu.setGroundIsNull(2);
	// break;
	// case 3:
	// _view.getOverlays().remove(sodu.getg3());
	// sodu.setGroundIsNull(3);
	// break;
	// case 4:
	// _view.getOverlays().remove(sodu.getg4());
	// sodu.setGroundIsNull(4);
	// break;
	// case 5:
	// _view.getOverlays().remove(sodu.getg5());
	// sodu.setGroundIsNull(5);
	// break;
	// case 6:
	// _view.getOverlays().remove(sodu.getg6());
	// sodu.setGroundIsNull(6);
	// break;
	// case 7:
	// _view.getOverlays().remove(sodu.getg7());
	// sodu.setGroundIsNull(7);
	// break;
	// case 8:
	// _view.getOverlays().remove(sodu.getg8());
	// sodu.setGroundIsNull(8);
	// break;
	// default:
	// break;
	// }
	// }
	// //deleteRubishLays();
	// //_view.refresh();
	// }

	// private static void deleteRubishLays()
	// {
	// if(_start==false)
	// return;
	// //清除因操作过快而没有及时清理的瓦片
	// int cleanNum=0;
	// //for(int i=0;i<_view.getOverlays().size();i++)
	// for(int i=_view.getOverlays().size()-1;i>=0;i--)
	// {
	// // Log.v("xunhanjiance", i+"");
	// if(!(_view.getOverlays().get(i) instanceof GroundOverlay))
	// continue;
	// GroundOverlay lay = (GroundOverlay)_view.getOverlays().get(i);
	// if( lay==sodu.getg0()
	// || lay==sodu.getg1()
	// || lay==sodu.getg2()
	// || lay==sodu.getg3()
	// || lay==sodu.getg4()
	// || lay==sodu.getg5()
	// || lay==sodu.getg6()
	// || lay==sodu.getg7()
	// || lay==sodu.getg8()
	// )//如果等于九宫格中其中一项就跳过
	// {
	// continue;
	// }
	// else//如果九宫格中没有记录，属于垃圾瓦片，进行清理
	// {
	// cleanNum++;
	// _view.getOverlays().remove(lay);
	//
	// for(int j=0;j<lay.size();j++)
	// {
	// lay.getGround(j);
	// }
	// lay=null;
	//
	// }
	// }
	//
	// if(cleanNum==0)
	// {
	// tileClean=false;
	// }
	// else
	// {
	// tileClean=true;
	// }
	// }

	private void updateCenterGroundPara() {

		try {
			centerGroundCenter = _view.getMap().getMapStatus().target;
			if (centerGroundCenter == null) {
				Log.v("function-render", "centergroundcenter is null");
			}
		} catch (Exception e) {
			return;
		}

		// 注：保持原来的span不变。。。
		centerGroundLeftBottom = new LatLng(centerGroundCenter.latitude - centerGroundLaSpan / 2,
				centerGroundCenter.longitude - centerGroundLoSpan / 2);
		centerGroundRightTop = new LatLng(centerGroundCenter.latitude + centerGroundLaSpan / 2,
				centerGroundCenter.longitude + centerGroundLoSpan / 2);
	}

	/**
	 * 渲染AQI数据
	 */
	private void renderAQI(List<AQIPoint> pts) {
		// 获取外接矩形，然后扩大1.5倍
		LatLng lb = null;
		LatLng rt = null;
		lb = centerGroundLeftBottom;
		rt = centerGroundRightTop;
		RenderBlock rb = new RenderBlock(lb, rt);
		rb.zoom(1.5);
		updateAQIRenderTask task = new updateAQIRenderTask();
		task.execute(rb, pts);
	}

	//
	// /**
	// * 渲染AQI数据
	// */
	// private void renderSurfaceWater(List<SurfaceWaterModel> pts) {
	// // 获取外接矩形，然后扩大1.5倍
	// LatLng lb = null;
	// LatLng rt = null;
	// lb = centerGroundLeftBottom;
	// rt = centerGroundRightTop;
	// RenderBlock rb = new RenderBlock(lb, rt);
	// rb.zoom(1.5);
	// updateAQIRenderTask task = new updateAQIRenderTask();
	// task.execute(rb, pts);
	// }

	private void utpMapExtPara() {
		LatLng pt00 = _view.getMap().getProjection().fromScreenLocation(new android.graphics.Point(0, 0));
		LatLng ptw0 = _view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(_view.getWidth(), 0));
		LatLng pt0h = _view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(0, _view.getHeight()));
		LatLng ptwh = _view.getMap().getProjection()
				.fromScreenLocation(new android.graphics.Point(_view.getWidth(), _view.getHeight()));
		centerGroundLaSpan = Math.max(pt00.latitude, Math.max(ptw0.latitude, Math.max(pt0h.latitude, ptwh.latitude)))
				- Math.min(pt00.latitude, Math.min(ptw0.latitude, Math.min(pt0h.latitude, ptwh.latitude)));
		centerGroundLoSpan = Math.max(pt00.longitude,
				Math.max(ptw0.longitude, Math.max(pt0h.longitude, ptwh.longitude)))
				- Math.min(pt00.longitude, Math.min(ptw0.longitude, Math.min(pt0h.longitude, ptwh.longitude)));
		mapBottom = _view.getMap().getMapStatus().target.latitude - centerGroundLaSpan / 2 - 0.4;
		maptop = _view.getMap().getMapStatus().target.latitude + centerGroundLaSpan / 2 + 0.4;
		mapLeft = _view.getMap().getMapStatus().target.longitude - centerGroundLoSpan / 2 - 0.4;
		mapRight = _view.getMap().getMapStatus().target.longitude + centerGroundLoSpan / 2 + 0.4;
		myMapBottom = ptwh.latitude;//
		myMaptop = pt00.latitude;
		myMapLeft = pt00.longitude;
		myMapRight = ptwh.longitude;
	}

	/**
	 * 判断是否在浏览时超出中国边界
	 * 
	 * @return true 超出；false 反之。
	 */
	private boolean isOutOfBrowerExt() {

		if (mapBottom > 53.55 || maptop < 18.22 || mapLeft > 135.083333 || mapRight < 73.55) {
			return true;
		}
		return false;
	}

	public EnvTypeOnMapEnum getEmun_envtype() {
		return emun_envtype;
	}

	/**
	 * 设置当前要在地图上展示的环境信息内容
	 * 
	 * @param emun_envtype
	 */
	public void setEmun_envtype(EnvTypeOnMapEnum em) {
		Log.v("function", "setEmun_envtype  " + em.toString());
		// 设置标识变量
		this.emun_envtype = em;
		// 设置要显示的图层
		// updateMapLayer();
		// 响应更新数据
		updateMapPoints();
	}

	/**
	 * 设置当前要在地图上展示的环境信息内容
	 * 
	 * @param emun_envtype
	 */
	public void setEmun_envtype_Old(EnvTypeOnMapEnum em) {
		Log.v("function", "setEmun_envtype_Old  " + em.toString());
		// 设置标识变量
		this.emun_envtype_Old = em;
	}

	public RenderEnum getEmun_rendertype() {
		return emun_rendertype;
	}

	public void setEmun_rendertype(RenderEnum emun_rendertype) {

		this.emun_rendertype = emun_rendertype;
		updateMapPoints();
	}

	public AQIZoomLevelEmun getEmun_aqiZoomLevel() {
		return emun_aqiZoomLevel;
	}

	public void setEmun_aqiZoomLevel(AQIZoomLevelEmun emun_aqiZoomLevel) {
		this.emun_aqiZoomLevel = emun_aqiZoomLevel;
	}

	/**
	 * 在地图上以城市 为单位显示和以监测点为单位显示这两种情况都用这一个， 发送给_outhandler的msg.obj是list-AQIPoint-,
	 * 以城市 为单位显示时，相当于只有一个点数据 以监测点为单位那就是，该城市包含的监测点数组数据
	 * 
	 * @author yutu802
	 * 
	 */
	class UpdateAQIcityTask extends AsyncTask<Object, Void, List<AQIPoint>> {

		@Override
		protected List<AQIPoint> doInBackground(Object... arg) {
			// arg[0]---url
			BusinessSearch search = new BusinessSearch();
			String url = UrlComponent.AQIqueryURL_V2_POST + UrlComponent.token;
			List<AQIPoint> _Result;
			try {
				MyLog.i(">>>>>>>>>>>shenghuiaqi" + "1");
				_Result = search.getShengHuiAqi((String) arg[0], pinjieCity);
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<AQIPoint> result) {
			MyLog.i("baiMap result.size() :" + result);
			// 先判断 是否还是显示该信息
			if (result != null && result.size() > 0) {
				cityResult = result;
			}
			// if ((!envTypeIsAQIType(emun_envtype))
			// || emun_aqiZoomLevel != AQIZoomLevelEmun.CITYLEVEL) {
			// return;
			// }
			if (result == null) {
				return;
			}
			// Message toMapActivity = _outHandler.obtainMessage();
			// toMapActivity.what = 6;
			// toMapActivity.obj = result;
			// _outHandler.sendMessage(toMapActivity);
			super.onPostExecute(result);
		}

	}

	/**
	 * 在地图上以城市 为单位显示和以监测点为单位显示这两种情况都用这一个， 发送给_outhandler的msg.obj是list-AQIPoint-,
	 * 以城市 为单位显示时，相当于只有一个点数据 以监测点为单位那就是，该城市包含的监测点数组数据
	 * 
	 * @author yutu802
	 * 
	 */

	class UpdateAQIPointTask extends AsyncTask<Object, Void, List<AQIPoint>> {
		private List<AQIPoint> aqipoints_local = new ArrayList<AQIPoint>();
		private List<String> citys = new ArrayList<String>();// 保存城市名称列表

		// private String aqipointtype = "";//city,point
		// private String cityname="";
		// private String date="";

		@Override
		protected List<AQIPoint> doInBackground(Object... arg) {
			// arg[0]---url
			// String url = UrlComponent.AQIqueryURL_V2_POST +
			// UrlComponent.token;
			String url = UrlComponent.monitorData;
			String ret = null;
			MyLog.i("getWeatherPost>>>>>>>>>>" + "hhtthtrh");
			BusinessSearch search = new BusinessSearch();
			List<AQIPoint> _Result;
			try {
				_Result = search.getUpdateAqi(url);
				return _Result; // YYF 将需要的数据传递了过来
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			// JSONObject jsonObjectCitys = new JSONObject();
			// // try {
			// // jsonObjectCitys.put("citys", (String) arg[0]);
			// // MyLog.i("url req:" + jsonObjectCitys.toString());
			// // MyLog.i("citys req:" + jsonObjectCitys.toString());
			// // } catch (JSONException e2) {
			// // e2.printStackTrace();
			// // }
			// try {
			// ret = ApiClient.PostToServerForResult(url,
			// jsonObjectCitys.toString());
			// } catch (UnsupportedEncodingException e1) {
			// e1.printStackTrace();
			// }
			// if (!waitingurl.equals(url)) {
			// MyLog.i("baiMap ret :" + ret);
			// return null;
			// }
			// // if (!waitingurl.equals(url)) {
			// // return null;
			// // }
			// aqipoints_local = (List<AQIPoint>) arg[1];
			// List<AQIPoint> aqipoints_fromserver = new
			// ArrayList<AQIPoint>();// 这个对象
			// // 只用来传递到onPostExecute，用来更新数据库
			// try {
			// JSONArray cityarray = new JSONArray(ret);
			// for (int i = 0; i < cityarray.length(); i++) {
			// JSONObject cityObject = cityarray.getJSONObject(i);
			// String name = cityObject.getString("city");
			// citys.add(name);
			// // JSONObject mainobj = null;//每个城市
			// // 都会返回所有监测点，该对象保存与城市名称相同的那一个
			// JSONArray pointlist = cityObject.getJSONArray("val");
			//
			// for (int j = 0; j < pointlist.length(); j++)//
			// 遍历城市中的监测站点列表信息，找到有城市名的那个赋给mainobj.
			// {
			//
			// JSONObject obj = (JSONObject) pointlist.get(j);
			// if (obj == null) {
			// continue;
			// }
			// AQIPoint pt = new AQIPoint(0, 0, obj.getString("aqi"),
			// obj.getString("station"),
			// obj.getString("pm25"), obj.getString("so2"),
			// obj.getString("no2"), obj.getString("o3"),
			// obj.getString("co"), obj.getString("o3"),
			// obj.getString("pm10"));
			// pt.setCity(name);
			// //
			// pt.setUpdateTime(WbMapUtil.strToDate(jsonObject.getString("update_time")));
			// pt.setUpdateTime(new Date());
			// aqipoints_fromserver.add(pt);
			// }
			//
			// }
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// return null;
			// }
		}

		@Override
		protected void onPostExecute(List<AQIPoint> result) {
			MyLog.i("baiMap result.size() :" + result);
			if (result == null)
				return;
			_stationResult = result;
			Message toMapActivity = _outHandler.obtainMessage();
			toMapActivity.what = 40;
			if (_stationResult != null && _stationResult.size() > 0) {
				// toMapActivity.obj = _stationResult;
				// _outHandler.sendMessage(toMapActivity);
				zoomFlag = false;
			}
			if (emun_rendertype == RenderEnum.AQI) {
				Log.v("function-render", "updateaqipointtask is null");
				// renderAQI(result);
			}
			super.onPostExecute(result);
		}

	}

	/**
	 * 任务是获取指定省会城市的天气，提取当天天气，存储至数据库，并显示至地图
	 * 
	 */
	class UpdateProvinceWeatherTask extends AsyncTask<Object, Void, List<Province>> {
		private List<Province> res_local = new ArrayList<Province>();// 这个是保存了传递进来的从本地库中获取的有时效的数据，最后与从服务器请求的合并。

		@Override
		protected List<Province> doInBackground(Object... params) {
			// params[0] url
			String ret = ApiClient.connServerForResult((String) params[0]);
			res_local = (List<Province>) params[1];
			if (!waitingurl.equals((String) params[0])) {

				return null;
			}

			List<Province> provinces = new ArrayList<Province>();// 用来存储获取到的城市天气信息的列表

			try {
				// JSONArray jsonarr1 = new JSONArray(ret);
				// JSONArray jsonarr = jsonarr1.getJSONArray(0);
				JSONArray jsonarr = new JSONArray(ret);
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject obj = jsonarr.getJSONObject(i);
					Province province = new Province();
					province.setCity(obj.getString("city"));
					String climate = obj.getString("weather0");
					if (climate.contains("转")) {// 天气带转字，取前面那部分
						String[] strs = climate.split("转");
						climate = strs[0];
						if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
							strs = climate.split("到");
							climate = strs[1];
							if (climate.contains("有")) {
								strs = climate.split("有");
								climate = strs[0];
							}
						} else {
							if (climate.contains("有")) {
								strs = climate.split("有");
								climate = strs[0];
							}
						}

					}
					if (climate.equals("雨")) {
						climate = "小雨";
					}
					province.setWeather_chname(climate);
					province.setWeather(WbMapUtil.weatherName2Code(climate));
					province.setWeatherUpdateTime(new Date());
					provinces.add(province);
				}

			} catch (Exception e) {
				Log.v("tianqi-err", e.toString());
				return null;
			}
			Log.v("tianqi-3", provinces.size() + "");
			return provinces;

		}

		@Override
		protected void onPostExecute(List<Province> result) {
			// 先判断 是否还是显示该信息
			if (emun_envtype != EnvTypeOnMapEnum.TIANQI || emun_tqZoomLevel != TianQiZoomLevelEnum.PROVINCE) {
				return;
			}
			super.onPostExecute(result);
			if (result == null) {
				// 直接发原来的数据显示到地图上
				clearAllGroundOfMap();
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 2;//
				toMapActivity.obj = res_local;
				_outHandler.sendMessage(toMapActivity);
				return;
			}
			List<Province> res_withloc = new ArrayList<Province>();// result没有坐标信息，所以从前端库中获取
			// 根据result中的city补全province的信息,顺便更新数据库数据
			// 1 先更新数据库中的天气数据和时间
			for (int i = 0; i < result.size(); i++) {
				Province province = new Province();
				// province = result.get(i);
				CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
				citydb.updateWeather(result.get(i).getCity(), result.get(i).getWeather());

				province = citydb.getProvinceByCity(result.get(i).getCity());
				res_withloc.add(province);
			}

			//
			// 直接发原来的数据显示到地图上
			clearAllGroundOfMap();
			res_withloc.addAll(res_local);
			// 发送至outhandler,显示到地图上
			Message toMapActivity = _outHandler.obtainMessage();
			toMapActivity.what = 2;//
			toMapActivity.obj = res_withloc;
			_outHandler.sendMessage(toMapActivity);
			Log.v("tianqi-4", res_withloc.size() + "");
			// 添加到provinces,
			// _provinces.add(province);

		}

	}

	/**
	 * 任务是获取指定地级城市的天气，提取当天天气，存储至数据库，并显示至地图
	 * 
	 */
	class UpdateMainCityWeatherTask extends AsyncTask<Object, Void, List<City>> {
		private List<City> res_local = new ArrayList<City>();// 这个是保存了传递进来的从本地库中获取的有时效的数据，最后与从服务器请求的合并。

		@Override
		protected List<City> doInBackground(Object... params) {

			String ret = ApiClient.connServerForResult((String) params[0]);
			res_local = (List<City>) params[1];
			if (!waitingurl.equals((String) params[0])) {

				return null;
			}

			List<City> maincitys = new ArrayList<City>();// 用来存储获取到的城市天气信息的列表

			try {
				// JSONArray jsonarr1 = new JSONArray(ret);
				// JSONArray jsonarr = jsonarr1.getJSONArray(0);
				JSONArray jsonarr = new JSONArray(ret);
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject obj = jsonarr.getJSONObject(i);
					City maincity = new City();
					maincity.setName(obj.getString("city"));
					String climate = obj.getString("weather0");
					if (climate.contains("转")) {// 天气带转字，取前面那部分
						String[] strs = climate.split("转");
						climate = strs[0];
						if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
							strs = climate.split("到");
							climate = strs[1];
							if (climate.contains("有")) {
								strs = climate.split("有");
								climate = strs[0];
							}
						} else {
							if (climate.contains("有")) {
								strs = climate.split("有");
								climate = strs[0];
							}
						}

					}
					if (climate.equals("雨")) {
						climate = "小雨";
					}
					maincity.setWeatherchname(climate);
					maincity.setWeather(WbMapUtil.weatherName2Code(climate));
					maincity.setWeatherUpdateDate(new Date());
					maincitys.add(maincity);
				}

			} catch (Exception e) {

				return null;
			}

			return maincitys;
		}

		@Override
		protected void onPostExecute(List<City> result) {
			// 先判断 是否还是显示该信息
			if (emun_envtype != EnvTypeOnMapEnum.TIANQI || emun_tqZoomLevel != TianQiZoomLevelEnum.MAINCITY) {
				return;
			}
			if (result == null) {
				// 直接发原来的数据显示到地图上
				clearAllGroundOfMap();
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 5;
				toMapActivity.obj = res_local;
				_outHandler.sendMessage(toMapActivity);
				return;
			}
			super.onPostExecute(result);
			// 输出服务器查询时间log

			List<City> res_withlocation = new ArrayList<City>();
			// 根据result中的city补全province的信息,顺便更新数据库数据
			// 1 先更新数据库中的天气数据和时间
			for (int i = 0; i < result.size(); i++) {
				City maincity = new City();
				// province = result.get(i);
				CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
				citydb.updateWeather(result.get(i).getName(), result.get(i).getWeather());

				maincity = citydb.getCityByCityname(result.get(i).getName());
				res_withlocation.add(maincity);
			}
			// 直接发原来的数据显示到地图上
			clearAllGroundOfMap();
			res_withlocation.addAll(res_local);
			Message toMapActivity = _outHandler.obtainMessage();
			toMapActivity.what = 5;
			toMapActivity.obj = res_withlocation;
			_outHandler.sendMessage(toMapActivity);

		}

	}

	/**
	 * 任务是获取指定地级城市的天气，提取当天天气，存储至数据库，并显示至地图
	 * 
	 */
	class UpdateMainCitySurfaceWaterTask extends AsyncTask<Object, Void, List<SurfaceWaterModel>> {
		/**
		 * 在地图上以城市 为单位显示和以监测点为单位显示这两种情况都用这一个，
		 * 发送给_outhandler的msg.obj是list-AQIPoint-, 以城市 为单位显示时，相当于只有一个点数据
		 * 以监测点为单位那就是，该城市包含的监测点数组数据
		 * 
		 * @author yutu802
		 * 
		 */
		private List<SurfaceWaterModel> aqipoints_local = new ArrayList<SurfaceWaterModel>();
		private List<String> citys = new ArrayList<String>();// 保存城市名称列表

		@Override
		protected List<SurfaceWaterModel> doInBackground(Object... arg) {
			// arg[0]---url
			String url = UrlComponent.surfaceWaterURL_V2 + UrlComponent.token;
			String ret = null;
			JSONObject jsonObjectCitys = new JSONObject();
			try {
				jsonObjectCitys.put("citys", (String) arg[0]);
				MyLog.i("url req:" + url);
				MyLog.i("citys req:" + jsonObjectCitys.toString());
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
			try {
				ret = ApiClient.PostToServerForResult(url, jsonObjectCitys.toString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			MyLog.i("baiMap ret :" + ret);
			// if (!waitingurl.equals((String) arg[0])) {
			if (!waitingurl.equals(url)) {
				MyLog.i("baiMap ret :" + ret);
				return null;
			}
			aqipoints_local = (List<SurfaceWaterModel>) arg[1];
			List<SurfaceWaterModel> aqipoints_fromserver = new ArrayList<SurfaceWaterModel>();// 这个对象
			// 只用来传递到onPostExecute，用来更新数据库
			try {
				JSONArray cityarray = new JSONArray(ret);
				for (int i = 0; i < cityarray.length(); i++) {
					JSONObject cityObject = cityarray.getJSONObject(i);
					String name = cityObject.getString("city");
					citys.add(name);
					// JSONObject mainobj = null;//每个城市
					// 都会返回所有监测点，该对象保存与城市名称相同的那一个
					JSONArray pointlist = cityObject.getJSONArray("surface");

					for (int j = 0; j < pointlist.length(); j++)// 遍历城市中的监测站点列表信息，找到有城市名的那个赋给mainobj.
					{

						JSONObject obj = (JSONObject) pointlist.get(j);
						if (obj == null) {
							continue;
						}
						SurfaceWaterModel pt = new SurfaceWaterModel(obj.getString("target_quality"),
								obj.getString("code"), obj.getString("mow_quality"), obj.getString("monitor_point"),
								obj.getString("baidu_lng_d"), obj.getString("baidu_lat_d"), obj.getString("c_function"),
								obj.getString("c_river"), obj.getString("c_water"), obj.getString("city"),
								obj.getString("resource"), obj.getString("r_time"), obj.getString("r_week"));
						// pt.setUpdateTime(WbMapUtil.strToDate(jsonObject.getString("update_time")));
						pt.setUpdateTime(new Date());
						aqipoints_fromserver.add(pt);
					}

				}

			} catch (Exception e) {

				return null;
			}
			return aqipoints_fromserver;
		}

		@Override
		protected void onPostExecute(List<SurfaceWaterModel> result) {
			try {
				if (result == null) {
					result = new ArrayList<SurfaceWaterModel>();
				}
				final List<SurfaceWaterModel> dbResult = result;
				// 先判断 是否还是显示该信息
				if (emun_envtype != EnvTypeOnMapEnum.SURFACE_WATER
						|| emun_aqiZoomLevel != AQIZoomLevelEmun.POINTLEVEL) {
					return;
				}
				try {
					new Thread() {
						public void run() {
							// 更新数据库
							citydb.updateCitySurfaceWater(dbResult);
							removeDuplicate(citys);
							String cityName = "";
							for (int i = 0; i < citys.size(); i++) {
								if (cityName != citys.get(i)) {
									cityName = citys.get(i);
									citydb.updateSurfaceWaterTime(citys.get(i));
								}

							}
						};
					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 更新数据库
				// citydb.updateCitySurfaceWater(result);
				// // 发送消息显示到地图上
				// // ------注：result里没有坐标，所以重新从地图获取
				// List<SurfaceWaterModel> newres = new
				// ArrayList<SurfaceWaterModel>();
				// for (int j = 0; j < citys.size(); j++)// 获取城市点的坐标数据
				// {
				// List<SurfaceWaterModel> newpoints = citydb
				// .getSurfaceWaterPointsByCity(citys.get(j));
				// citydb.updateSurfaceWaterTime(citys.get(j));
				// newres.addAll(newpoints);
				// }
				// newres.addAll(aqipoints_local);
				result.addAll(aqipoints_local);
				// // //显示到地图上,
				clearAllGroundOfMap();
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = MapMianActivity.SURFACE_WATER_HANDLER_SEVER_TAG;
				toMapActivity.obj = result;
				_outHandler.sendMessage(toMapActivity);
				super.onPostExecute(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
	}

	/**
	 * 任务是获取指定地级城市的天气，提取当天天气，存储至数据库，并显示至地图
	 * 
	 */
	class UpdateMainCityPollutionPointTask extends AsyncTask<Object, Void, List<PollutionPointModel>> {
		/**
		 * 在地图上以城市 为单位显示和以监测点为单位显示这两种情况都用这一个，
		 * 发送给_outhandler的msg.obj是list-AQIPoint-, 以城市 为单位显示时，相当于只有一个点数据
		 * 以监测点为单位那就是，该城市包含的监测点数组数据
		 * 
		 * @author yutu802
		 * 
		 */
		private List<PollutionPointModel> aqipoints_local = new ArrayList<PollutionPointModel>();
		private List<String> citys = new ArrayList<String>();// 保存城市名称列表

		@Override
		protected List<PollutionPointModel> doInBackground(Object... arg) {
			// arg[0]---url
			String url = UrlComponent.pullotion_URL_V2 + UrlComponent.token;
			String ret = null;
			JSONObject jsonObjectCitys = new JSONObject();
			try {
				jsonObjectCitys.put("citys", (String) arg[0]);
				MyLog.i("url req:" + url);
				MyLog.i("citys req:" + jsonObjectCitys.toString());
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
			try {
				ret = ApiClient.PostToServerForResult(url, jsonObjectCitys.toString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			MyLog.i("baiMap ret :" + ret);
			// if (!waitingurl.equals((String) arg[0])) {
			if (!waitingurl.equals(url)) {
				MyLog.i("baiMap ret waitingurl:" + ret);
				return null;
			}
			aqipoints_local = (List<PollutionPointModel>) arg[1];
			List<PollutionPointModel> aqipoints_fromserver = new ArrayList<PollutionPointModel>();// 这个对象
			// 只用来传递到onPostExecute，用来更新数据库
			try {
				JSONArray cityarray = new JSONArray(ret);
				for (int i = 0; i < cityarray.length(); i++) {
					JSONObject cityObject = cityarray.getJSONObject(i);
					String name = cityObject.getString("city");
					citys.add(name);
					// JSONObject mainobj = null;//每个城市
					// 都会返回所有监测点，该对象保存与城市名称相同的那一个
					JSONArray pointlist = cityObject.getJSONArray("minitor");

					for (int j = 0; j < pointlist.length(); j++)// 遍历城市中的监测站点列表信息，找到有城市名的那个赋给mainobj.
					{

						JSONObject obj = (JSONObject) pointlist.get(j);
						if (obj == null) {
							continue;
						}
						PollutionPointModel pollutionPointModel = new PollutionPointModel(obj.getString("name"),
								obj.getString("type"), obj.getString("sort"), obj.getString("city"),
								obj.getString("district"), obj.getString("address"), obj.getString("lat"),
								obj.getString("lng"), obj.getString("usid"));
						// pt.setUpdateTime(WbMapUtil.strToDate(jsonObject.getString("update_time")));
						pollutionPointModel.setUpdateTime(new Date());
						aqipoints_fromserver.add(pollutionPointModel);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return aqipoints_fromserver;
		}

		@Override
		protected void onPostExecute(List<PollutionPointModel> result) {
			try {

				if (null == result) {
					result = new ArrayList<PollutionPointModel>();
				}
				final List<PollutionPointModel> dbResult = result;
				MyLog.i("baiMap ret result:" + result.size());

				if (emun_envtype != EnvTypeOnMapEnum.SOURCE_OF_POLLUTION) {
					return;
				}
				try {
					new Thread() {
						public void run() {
							// 更新数据库
							citydb.updateCityPollutionPoints(dbResult);
							removeDuplicate(citys);
							String cityName = "";
							for (int i = 0; i < citys.size(); i++) {
								if (cityName != citys.get(i)) {
									cityName = citys.get(i);
									citydb.updatepollutionTime(citys.get(i));
								}

							}
						};
					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// // 更新数据库
				// citydb.updateCityPollutionPoints(result);
				// for (int i = 0; i < result.size(); i++) {
				// citydb.updatepollutionTime(result.get(i).getName());
				// }
				// 发送消息显示到地图上
				// // ------注：result里没有坐标，所以重新从地图获取
				// List<PollutionPointModel> newres = new
				// ArrayList<PollutionPointModel>();
				// for (int j = 0; j < citys.size(); j++)// 获取城市点的坐标数据
				// {
				// // List<PollutionPointModel> newpoints = citydb
				// // .getPollutionPointByCity(citys.get(j));
				// citydb.updatepollutionTime(citys.get(j));
				// // newres.addAll(newpoints);
				// }
				// newres.addAll(aqipoints_local);
				result.addAll(aqipoints_local);
				// // //显示到地图上,
				// clearAllGroundOfMap();
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = MapMianActivity.POLLUTION_HANDLER_SEVER_TAG;
				toMapActivity.obj = result;
				_outHandler.sendMessage(toMapActivity);
				super.onPostExecute(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 任务是获取指定地级城市的天气，提取当天天气，存储至数据库，并显示至地图
	 * 
	 */
	class UpdateCityWeatherTask extends AsyncTask<Object, Void, List<City>> {
		private List<City> res_local = new ArrayList<City>();// 这个是保存了传递进来的从本地库中获取的有时效的数据，最后与从服务器请求的合并。

		@Override
		protected List<City> doInBackground(Object... params) {

			String ret = ApiClient.connServerForResult((String) params[0]);
			Log.v("function-url", (String) params[0]);
			res_local = (List<City>) params[1];
			if (!waitingurl.equals((String) params[0])) {

				return null;
			}

			List<City> citys = new ArrayList<City>();// 用来存储获取到的城市天气信息的列表

			try {
				// JSONArray jsonarr1 = new JSONArray(ret);
				// JSONArray jsonarr = jsonarr1.getJSONArray(0);
				JSONArray jsonarr = new JSONArray(ret);
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject obj = jsonarr.getJSONObject(i);
					City city = new City();
					city.setName(obj.getString("city"));
					String climate = obj.getString("weather0");
					if (climate.contains("转")) {// 天气带转字，取前面那部分
						String[] strs = climate.split("转");
						climate = strs[0];
						if (climate.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
							strs = climate.split("到");
							climate = strs[1];
							if (climate.contains("有")) {
								strs = climate.split("有");
								climate = strs[0];
							}
						} else {
							if (climate.contains("有")) {
								strs = climate.split("有");
								climate = strs[0];
							}
						}

					}
					if (climate.equals("雨")) {
						climate = "小雨";
					}
					city.setWeatherchname(climate);
					city.setWeather(WbMapUtil.weatherName2Code(climate));
					city.setWeatherUpdateDate(new Date());
					citys.add(city);
				}

			} catch (Exception e) {

				return null;
			}

			return citys;
		}

		@Override
		protected void onPostExecute(List<City> result) {
			// 先判断 是否还是显示该信息
			if (emun_envtype != EnvTypeOnMapEnum.TIANQI || emun_tqZoomLevel != TianQiZoomLevelEnum.CITY) {
				return;
			}
			if (result == null) {
				// 意思就是从服务器返回数据失败，那我们就直接显示缓存的数据
				// 直接发原来的数据显示到地图上
				clearAllGroundOfMap();
				Message toMapActivity = _outHandler.obtainMessage();
				toMapActivity.what = 7;
				toMapActivity.obj = res_local;
				_outHandler.sendMessage(toMapActivity);
				return;
			}
			super.onPostExecute(result);
			// 输出服务器查询时间log

			List<City> res_withlocation = new ArrayList<City>();
			// 根据result中的city补全province的信息,顺便更新数据库数据
			// 1 先更新数据库中的天气数据和时间
			for (int i = 0; i < result.size(); i++) {
				City city = new City();
				// province = result.get(i);
				citydb.updateWeather(result.get(i).getName(), result.get(i).getWeather());

				city = citydb.getCityByCityname(result.get(i).getName());
				res_withlocation.add(city);
			}
			// 直接发原来的数据显示到地图上
			clearAllGroundOfMap();
			res_withlocation.addAll(res_local);
			Message toMapActivity = _outHandler.obtainMessage();
			toMapActivity.what = 7;
			toMapActivity.obj = res_withlocation;
			_outHandler.sendMessage(toMapActivity);

		}

	}

	/**
	 * @author shang 生成一张基于aqi数据的渲染图，返回给前端显示 Object 装入类型
	 *         [RenderBlock,List(AQIPoint)]
	 */
	class updateAQIRenderTask extends AsyncTask<Object, Void, GroundOverlayOptions> {

		@Override
		protected void onPostExecute(GroundOverlayOptions result) {
			// TODO Auto-generated method stub
			Message toMapActivity = _outHandler.obtainMessage();
			toMapActivity.what = 11;
			toMapActivity.obj = result;
			_outHandler.sendMessage(toMapActivity);
			super.onPostExecute(result);
		}

		@Override
		protected GroundOverlayOptions doInBackground(Object... para) {
			// TODO Auto-generated method stub
			RenderBlock rblock = (RenderBlock) para[0];
			List<AQIPoint> aqipoints = (List<AQIPoint>) para[1];
			// 添加处理过程，处理完后给主线程发送消息和数据
			try {

				// 调用计算模块进行分析计算
				RenderResultData rrData = new RenderResultData();

				// sleep(300);
				double minx = rblock.get_minLongitude();
				double maxx = rblock.get_maxLongitude();
				double miny = rblock.get_minLatitude();
				double maxy = rblock.get_maxLatitude();

				Point pt1 = new Point(minx, maxy);// 左上
				Point pt2 = new Point(maxx, miny);
				List<Point> points = new ArrayList<Point>();
				points.add(pt1);
				points.add(pt2);

				// 接入真实数据
				List<PollutantStation> pss = new ArrayList<PollutantStation>();

				for (int i = 0; i < aqipoints.size(); i++) {

					PollutantStation ps = new PollutantStation();
					ps.set_point(
							new Point((double) aqipoints.get(i).getJingdu(), (double) aqipoints.get(i).getWeidu()));
					HashMap<String, Double> hm = new HashMap<String, Double>();

					// 检测空值
					AQIPoint p = aqipoints.get(i);
					if (p.getAqi() == null || p.getCO() == null || p.getNO2() == null || p.getO3() == null
							|| p.getPM10() == null || p.getPm2_5() == null || p.getSO2() == null) {

						continue;
					}

					switch (emun_envtype) {
					case AQI:

						hm.put("AQI", Double.parseDouble(aqipoints.get(i).getAqi()));
						break;
					case CO:
						hm.put("CO", Double.parseDouble(aqipoints.get(i).getCO()));
						break;
					case NO2:
						hm.put("NO2", Double.parseDouble(aqipoints.get(i).getNO2()));
						break;
					case O3:
						hm.put("O3", Double.parseDouble(aqipoints.get(i).getO3()));
						break;
					case PM10:
						hm.put("PM10", Double.parseDouble(aqipoints.get(i).getPM10()));
						break;
					case PM25:
						hm.put("pm25", Double.parseDouble(aqipoints.get(i).getPm2_5()));
						break;
					case SO2:
						hm.put("SO2", Double.parseDouble(aqipoints.get(i).getSO2()));
						break;
					default:
						hm.put("AQI", Double.parseDouble(aqipoints.get(i).getAqi()));
						break;
					}
					ps.set_pollutantValue(hm);
					pss.add(ps);
				}
				// 设置类型，将渲染类型传递给PollutantTypeEnum
				PollutantTypeEnum polltype = null;
				switch (emun_envtype) {
				case AQI:
					polltype = PollutantTypeEnum.AQI;
					break;
				case CO:
					polltype = PollutantTypeEnum.CO;
					break;
				case MAI:

					break;
				case NO2:
					polltype = PollutantTypeEnum.NO2;
					break;
				case NULL:
					break;
				case O3:
					polltype = PollutantTypeEnum.O3;
					break;
				case PM10:
					polltype = PollutantTypeEnum.PM10;
					break;
				case PM25:
					polltype = PollutantTypeEnum.pm25;
					break;
				case QIWEN:
					break;
				case SHARE_PICTURE:
					break;
				case SO2:
					polltype = PollutantTypeEnum.SO2;
					break;
				case TIANQI:
					break;
				case TRAFFIC:
					break;
				case WU:
					break;
				default:
					break;

				}

				InterPolation ip = new InterPolation(50, 50, points,
						// PollutantTypeEnum.NO2,
						polltype, pss);

				Bitmap bp = ip.Get_Bitmap();
				// bp=WbMapUtil.zoombitmap(bp, 10.0f, 10.0f);

				rrData.set_rblock(rblock);
				rrData.set_bitmap(bp);

				// 消除瓦片之间的间隙，方法是放大每一张瓦片的贴图范围，扩大1%
				LatLng lb = rrData.get_rblock().getLeftBottomPT();
				LatLng rt = rrData.get_rblock().getRightTopPT();

				if (lb.latitude >= rt.latitude || lb.longitude >= rt.longitude) {
					return null;
				}
				// mGround = new Ground((Bitmap) rrData.get_bitmap(),
				// lb,
				// rt);
				LatLngBounds bounds = new LatLngBounds.Builder().include(rt).include(lb).build();
				// 定义Ground显示的图片
				BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap((Bitmap) rrData.get_bitmap());
				// 定义Ground覆盖物选项
				GroundOverlayOptions ooGround = new GroundOverlayOptions().positionFromBounds(bounds).image(bdGround);

				// 向主线程回发分析结果数据
				// Message toMain= _mainHandler.obtainMessage();
				// toMain.what = ADD_TILE;
				// toMain.obj = rrData;
				// _mainHandler.sendMessage(toMain);
				// bp.recycle();
				// bp=null;
				return ooGround;

			} catch (Exception e) {

			}
			return null;
		}

	}

	/**
	 * TODO YYF此处获取了河南城市数据
	 */
	// 获取监测站数据
	private class GetMonitoringAqiTask extends AsyncTask<String, Void, List<AqiModel>> {

		@Override
		protected List<AqiModel> doInBackground(String... params) {
			// TODO Auto-generated method stub
			BusinessSearch search = new BusinessSearch();
			String url = "";
			url = UrlComponent.getLeiJiNongDuValue;
			List<AqiModel> _Result;
			MyLog.i(">>>>>>>>>>urgsddsgl" + url);
			try {
				_Result = search.getMonitoringAqi(url, 0, "", "", "");
				return _Result;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * TODO YYF此处AQIPoint应选择参数多的构造方法，数据才会出来；
		 */
		@Override
		protected void onPostExecute(List<AqiModel> result) {
			super.onPostExecute(result);
			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					AqiModel aqiModel = result.get(i);
					AQIPoint point;
//					if (aqiModel.getCITY().equals("济源市")) {
//						point = new AQIPoint(112.57, 35.05, aqiModel.getAQI() + "", "济源", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10(), aqiModel.getMONIDATE());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("巩义市")) {
//						point = new AQIPoint(113.02, 34.75, aqiModel.getAQI() + "", "巩义", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("兰考县")) {
//						point = new AQIPoint(114.82, 34.82, aqiModel.getAQI() + "", "兰考", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("汝州市")) {
//						point = new AQIPoint(112.85, 34.17, aqiModel.getAQI() + "", "汝州", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("滑县")) {
//						point = new AQIPoint(114.52, 35.58, aqiModel.getAQI() + "", "滑县", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("长垣县")) {
//						point = new AQIPoint(114.67, 35.20, aqiModel.getAQI() + "", "长垣", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("邓州市")) {
//						point = new AQIPoint(112.09, 32.69, aqiModel.getAQI() + "", "邓州", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("永城市")) {
//						point = new AQIPoint(116.45, 33.93, aqiModel.getAQI() + "", "永城", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("鹿邑县")) {
//						point = new AQIPoint(115.49, 33.86, aqiModel.getAQI() + "", "鹿邑", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("固始县")) {
//						point = new AQIPoint(115.66, 32.17, aqiModel.getAQI() + "", "固始", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("新蔡县")) {
//						point = new AQIPoint(114.99, 32.75, aqiModel.getAQI() + "", "新蔡", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("郑州市")) {
//						point = new AQIPoint(113.62, 34.75, aqiModel.getAQI() + "", "郑州", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10(), aqiModel.getMONIDATE());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("开封市")) {
//						point = new AQIPoint(114.3, 34.8, aqiModel.getAQI() + "", "开封", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("洛阳市")) {
//						point = new AQIPoint(112.45, 34.60, aqiModel.getAQI() + "", "洛阳", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("平顶山市")) {
//						point = new AQIPoint(113.18, 33.77, aqiModel.getAQI() + "", "平顶山", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("安阳市")) {
//						point = new AQIPoint(114.38, 36.1, aqiModel.getAQI() + "", "安阳", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("鹤壁市")) {
//						point = new AQIPoint(114.28, 35.75, aqiModel.getAQI() + "", "鹤壁", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("新乡市")) {
//						point = new AQIPoint(113.9, 35.3, aqiModel.getAQI() + "", "新乡", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("焦作市")) {
//						point = new AQIPoint(113.25, 35.22, aqiModel.getAQI() + "", "焦作", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("濮阳市")) {
//						point = new AQIPoint(115.02, 35.7, aqiModel.getAQI() + "", "濮阳", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("许昌市")) {
//						point = new AQIPoint(113.85, 34.03, aqiModel.getAQI() + "", "许昌", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("漯河市")) {
//						point = new AQIPoint(114.02, 33.58, aqiModel.getAQI() + "", "漯河", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("三门峡市")) {
//						point = new AQIPoint(111.2, 34.78, aqiModel.getAQI() + "", "三门峡", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("南阳市")) {
//						point = new AQIPoint(112.52, 33, aqiModel.getAQI() + "", "南阳", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("商丘市")) {
//						point = new AQIPoint(115.65, 34.45, aqiModel.getAQI() + "", "商丘", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("信阳市")) {
//						point = new AQIPoint(114.07, 32.13, aqiModel.getAQI() + "", "信阳", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("周口市")) {
//						point = new AQIPoint(114.65, 33.62, aqiModel.getAQI() + "", "周口", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					} else if (aqiModel.getCITY().equals("驻马店市")) {
//						point = new AQIPoint(114.02, 32.98, aqiModel.getAQI() + "", "驻马店", aqiModel.getPM25(), "", "",
//								aqiModel.getO3(), "", "", aqiModel.getPM10());
//						aqiPointsAdd.add(point);
//					}
					 if (aqiModel.getCITY().equals("济源市")) {
					 point = new AQIPoint(112.57, 35.05, aqiModel.getAQI() +
					 "", "济源", aqiModel.getMONIDATE());
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("巩义市")) {
					 point = new AQIPoint(113.02, 34.75, aqiModel.getAQI() +
					 "", "巩义");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("兰考县")) {
					 point = new AQIPoint(114.82, 34.82, aqiModel.getAQI() +
					 "", "兰考");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("汝州市")) {
					 point = new AQIPoint(112.85, 34.17, aqiModel.getAQI() +
					 "", "汝州");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("滑县")) {
					 point = new AQIPoint(114.52, 35.58, aqiModel.getAQI() +
					 "", "滑县");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("长垣县")) {
					 point = new AQIPoint(114.67, 35.20, aqiModel.getAQI() +
					 "", "长垣");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("邓州市")) {
					 point = new AQIPoint(112.09, 32.69, aqiModel.getAQI() +
					 "", "邓州");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("永城市")) {
					 point = new AQIPoint(116.45, 33.93, aqiModel.getAQI() +
					 "", "永城");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("鹿邑县")) {
					 point = new AQIPoint(115.49, 33.86, aqiModel.getAQI() +
					 "", "鹿邑");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("固始县")) {
					 point = new AQIPoint(115.66, 32.17, aqiModel.getAQI() +
					 "", "固始");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("新蔡县")) {
					 point = new AQIPoint(114.99, 32.75, aqiModel.getAQI() +
					 "", "新蔡");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("郑州市")) {
					 point = new AQIPoint(113.62, 34.75, aqiModel.getAQI() +
					 "", "郑州", aqiModel.getMONIDATE());
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("开封市")) {
					 point = new AQIPoint(114.3, 34.8, aqiModel.getAQI() + "",
					 "开封");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("洛阳市")) {
					 point = new AQIPoint(112.45, 34.60, aqiModel.getAQI() +
					 "", "洛阳");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("平顶山市")) {
					 point = new AQIPoint(113.18, 33.77, aqiModel.getAQI() +
					 "", "平顶山");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("安阳市")) {
					 point = new AQIPoint(114.38, 36.1, aqiModel.getAQI() +
					 "", "安阳");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("鹤壁市")) {
					 point = new AQIPoint(114.28, 35.75, aqiModel.getAQI() +
					 "", "鹤壁");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("新乡市")) {
					 point = new AQIPoint(113.9, 35.3, aqiModel.getAQI() + "",
					 "新乡");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("焦作市")) {
					 point = new AQIPoint(113.25, 35.22, aqiModel.getAQI() +
					 "", "焦作");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("濮阳市")) {
					 point = new AQIPoint(115.02, 35.7, aqiModel.getAQI() +
					 "", "濮阳");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("许昌市")) {
					 point = new AQIPoint(113.85, 34.03, aqiModel.getAQI() +
					 "", "许昌");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("漯河市")) {
					 point = new AQIPoint(114.02, 33.58, aqiModel.getAQI() +
					 "", "漯河");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("三门峡市")) {
					 point = new AQIPoint(111.2, 34.78, aqiModel.getAQI() +
					 "", "三门峡");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("南阳市")) {
					 point = new AQIPoint(112.52, 33, aqiModel.getAQI() + "",
					 "南阳");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("商丘市")) {
					 point = new AQIPoint(115.65, 34.45, aqiModel.getAQI() +
					 "", "商丘");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("信阳市")) {
					 point = new AQIPoint(114.07, 32.13, aqiModel.getAQI() +
					 "", "信阳");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("周口市")) {
					 point = new AQIPoint(114.65, 33.62, aqiModel.getAQI() +
					 "", "周口");
					 aqiPointsAdd.add(point);
					 } else if (aqiModel.getCITY().equals("驻马店市")) {
					 point = new AQIPoint(114.02, 32.98, aqiModel.getAQI() +
					 "", "驻马店");
					 aqiPointsAdd.add(point);
					 }
				}
				MyLog.i(">>>>>>>>>>aqiPointsAdd" + aqiPointsAdd);
				provinceResult = aqiPointsAdd;
				if (aqiPointsAdd.size() > 1) {
					Thread thread = new Thread() {
						public void run() {
							for (int i = 0; i < aqiPointsAdd.size(); i++) {
								List<AQIPoint> sinPoints = new ArrayList<AQIPoint>();
								sinPoints.add(aqiPointsAdd.get(i));
								Message cityMessage = _outHandler.obtainMessage();
								cityMessage.what = 6;
								cityMessage.obj = sinPoints;
								_outHandler.sendMessage(cityMessage);
								if (i == 0) {
									Message cityMessager = _outHandler.obtainMessage();
									cityMessager.what = 50;
									cityMessager.obj = sinPoints.get(0).getUpdateTime();
									_outHandler.sendMessage(cityMessager);
								}

								try {
									sleep(30);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
					};
					thread.start();
				}
			}
		}
	}

	public boolean isContains(String a, String[] str) {
		MyLog.i(">>>>>>>>>maintain" + a);
		for (String s : str) {
			if (s.contains(a)) {
				return true;
			}

		}
		return false;
	}
	
}
