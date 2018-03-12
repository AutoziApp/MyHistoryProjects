package com.jy.environment.util;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;

import com.baidu.mapapi.model.LatLng;
import com.jy.environment.R;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.map.PollutantTypeEnum;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.City;
import com.jy.environment.model.Province;
import com.jy.environment.model.WeatherEnum;

//import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @author shang
 * 地图显示处理相关操作函数集合
 *
 */
/**
 * @author yutu
 *
 */
/**
 * @author yutu
 * 
 */
public class WbMapUtil {

	public static List<AQIPoint> clearAQICityOutMapExtent(
			List<AQIPoint> aqipoints, double left, double right, double bottom,
			double top) {
		List<AQIPoint> newpoints = new ArrayList<AQIPoint>();
		for (int i = 0; i < aqipoints.size(); i++) {
			AQIPoint p = aqipoints.get(i);
			LatLng geo = new LatLng(p.getWeidu(), p.getJingdu());
			if (geo.latitude >= bottom && geo.latitude <= top
					&& geo.longitude >= left && geo.longitude <= right) {
				newpoints.add(p);
			}
		}
		return newpoints;
	}

	/**
	 * 从province列表中清除掉指定地图范围外的点
	 * 
	 * @param province
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @return 点列表List
	 */
	public static List<Province> clearProvinceOutMapExtent(
			List<Province> province, double left, double right, double bottom,
			double top) {
		List<Province> newpro = new ArrayList<Province>();
		for (int i = 0; i < province.size(); i++) {
			Province p = province.get(i);
			LatLng geo = p.getLocation();
			if (geo.latitude >= bottom && geo.latitude <= top
					&& geo.longitude >= left && geo.longitude <= right) {
				newpro.add(p);
			}
		}
		return newpro;
	}

	/**
	 * 从city列表中清除掉指定地图范围外的点
	 * 
	 * @param city
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @return 点列表List
	 */
	public static List<City> clearCityOutMapExtent(List<City> city,
			double left, double right, double bottom, double top) {
		List<City> newcity = new ArrayList<City>();
		for (int i = 0; i < city.size(); i++) {
			City c = city.get(i);
			LatLng geo = c.getLocation();
			if (geo.latitude >= bottom && geo.latitude <= top
					&& geo.longitude >= left && geo.longitude <= right) {
				newcity.add(c);
			}
		}
		return newcity;
	}

	public static List<AQIPoint> getAQIPointInMapExtentFromDB(double left,
			double right, double bottom, double top) {
		List<AQIPoint> points = new ArrayList<AQIPoint>();
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		points = citydb.getAQIPointByMapExt(left, right, bottom, top);
		Log.v("ttt", "points.size:" + points.size());
		return points;
	}

	/**
	 * 获取地图范围内的以城市 为单位的aqi点数据
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @return
	 */
	public static List<AQIPoint> getAQICityInMapExtentFromDB(double left,
			double right, double bottom, double top) {
		List<AQIPoint> points = new ArrayList<AQIPoint>();
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		points = citydb.getAQICityByMapExt(left, right, bottom, top);
		return points;
	}

	public static double getDistanceOfGeoPoint(LatLng p1, LatLng p2) {
		double x1 = p1.longitude;
		double x2 = p2.longitude;
		double y1 = p1.latitude;
		double y2 = p2.latitude;

		double dis = (double) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));

		return dis;
	}

	/**
	 * 从数据库中读取在地图显示范围内的省份，每个省份的坐标参考以省会城市的坐标点为基准
	 * 
	 * @param left
	 *            地图经纬范围
	 * @param right
	 * @param bottom
	 * @param top
	 * @return
	 */
	public static List<Province> getProvinceWeatherInMapExtentFromDB(
			double left, double right, double bottom, double top) {
		List<Province> provinces = new ArrayList<Province>();
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		provinces = citydb.getProvincesWeatherByMapExt((float) left,
				(float) right, (float) bottom, (float) top);
		return provinces;
	}

	public static List<City> getMaincityWeatherInMapExtentFromDB(double left,
			double right, double bottom, double top) {
		List<City> maincitys = new ArrayList<City>();
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		maincitys = citydb.getMaincitysWeatherByMapExt((float) left,
				(float) right, (float) bottom, (float) top);
		return maincitys;
	}

	public static List<City> getcityWeatherInMapExtentFromDB(double left,
			double right, double bottom, double top) {
		List<City> citys = new ArrayList<City>();
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		citys = citydb.getCitysWeatherByMapExt((float) left, (float) right,
				(float) bottom, (float) top);
		return citys;
	}

	public static List<City> getcityInMapExtentFromDB(double fbottom,
			double ftop, double fleft, double fright) {
		List<City> citys = new ArrayList<City>();
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();
		citys = citydb.getCitysByMapExt(fbottom, ftop, fleft, fright);
		return citys;
	}

	public static Bitmap drawableToBitmap(Drawable drawable)// drawable
															// 转换成bitmap
	{
		int width = drawable.getIntrinsicWidth(); // 取drawable的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把drawable内容画到画布中
		return bitmap;
	}

	/**
	 * @param str
	 *            ---"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date strToDate(String str) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回VIEW的截图
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {
		// view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		// view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		// System.out.println("convertViewToBitmap=====");

		return bitmap;
	}

	public static void setLayoutX(View view, int x) {
		MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view
				.getLayoutParams();
		margin.setMargins(x, margin.topMargin, margin.rightMargin,
				margin.bottomMargin);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	public static void setLayoutY(View view, int y) {
		MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view
				.getLayoutParams();
		margin.setMargins(margin.leftMargin, y, margin.rightMargin,
				margin.bottomMargin);
		Log.i("layoutMargin", "left:" + margin.leftMargin + " top:" + y
				+ " right:" + margin.rightMargin + " bottom:"
				+ margin.bottomMargin);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	public static void setLayout(View view, int x, int y) {
		MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view
				.getLayoutParams();
		margin.setMargins(x, y, margin.rightMargin, margin.bottomMargin);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	@SuppressLint("SimpleDateFormat")
	public static String Date2Str(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 */
	@SuppressLint("SimpleDateFormat")
	public static String Date2Str2(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	/**
	 * 转换drawable的大小
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);// drawable转换成bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的Matrix对象
		float scaleWidth = ((float) w / width); // 计算缩放比例
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		BitmapDrawable bmd = null;
		try {
			Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
					matrix, true); // 建立新的bitmap，其内容是对原bitmap的缩放后的图
			bmd = new BitmapDrawable(newbmp);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return bmd; // 把bitmap转换成drawable并返回
	}

	/**
	 * @param bitmap
	 * @param sx
	 *            1.0 为不放大也不缩小
	 * @param sy
	 *            同上
	 * @return
	 */
	public static Bitmap zoombitmap(Bitmap bitmap, float sx, float sy) {
		Matrix matrix = new Matrix();
		matrix.postScale(1.5f, 1.5f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/**
	 * 设置hscroll的内容滚动至中间位置
	 * 
	 * @param scroll
	 *            水平滚动条控件
	 * @param inner
	 *            滚动条内部包含控件
	 */
	public static void hscroll2Middle(final View scroll, final View inner) {
		Handler mhandler = new Handler();
		mhandler.post(new Runnable() {

			@Override
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}

				int offset = inner.getMeasuredWidth() - scroll.getWidth();
				if (offset < 0) {
					offset = 0;
				}
				scroll.scrollTo(offset / 2, 0);
			}
		});
	}

	/**
	 * 使用bitmap时，在有可能发生OOM的地方调用该函数
	 * 
	 * @param bitmap
	 */
	public static void bitmapRecycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	// mWeatherIcon.put("暴雪", R.drawable.biz_plugin_weather_baoxue);
	// mWeatherIcon.put("暴雨", R.drawable.biz_plugin_weather_baoyu);
	// mWeatherIcon.put("大暴雨", R.drawable.biz_plugin_weather_dabaoyu);
	// mWeatherIcon.put("大雪", R.drawable.biz_plugin_weather_daxue);
	// mWeatherIcon.put("大雨", R.drawable.biz_plugin_weather_dayu);
	//
	// mWeatherIcon.put("多云", R.drawable.biz_plugin_weather_duoyun);
	// mWeatherIcon.put("雷阵雨", R.drawable.biz_plugin_weather_leizhenyu);
	// mWeatherIcon.put("雷阵雨冰雹",
	// R.drawable.biz_plugin_weather_leizhenyubingbao);
	// mWeatherIcon.put("晴", R.drawable.biz_plugin_weather_qing);
	// mWeatherIcon.put("沙尘暴", R.drawable.biz_plugin_weather_shachenbao);
	//
	// mWeatherIcon.put("特大暴雨", R.drawable.biz_plugin_weather_tedabaoyu);
	// mWeatherIcon.put("雾", R.drawable.biz_plugin_weather_wu);
	// mWeatherIcon.put("小雪", R.drawable.biz_plugin_weather_xiaoxue);
	// mWeatherIcon.put("小雨", R.drawable.biz_plugin_weather_xiaoyu);
	// mWeatherIcon.put("阴", R.drawable.biz_plugin_weather_yin);
	//
	// mWeatherIcon.put("雨夹雪", R.drawable.biz_plugin_weather_yujiaxue);
	// mWeatherIcon.put("阵雪", R.drawable.biz_plugin_weather_zhenxue);
	// mWeatherIcon.put("阵雨", R.drawable.biz_plugin_weather_zhenyu);
	// mWeatherIcon.put("中雪", R.drawable.biz_plugin_weather_zhongxue);
	// mWeatherIcon.put("中雨", R.drawable.biz_plugin_weather_zhongyu);

	public static int getWeatherMarker(int weathercode) {
		int maker = R.drawable.w0;
		switch (weathercode) {
		case WeatherEnum.BIG_RAIN:
			maker = R.drawable.w9;
			break;
		case WeatherEnum.BIG_RAIN_STORM:
			maker = R.drawable.w10;
			break;
		case WeatherEnum.CLOUDY:
			maker = R.drawable.w1;
			break;
		case WeatherEnum.FLY_ASH:

			break;
		case WeatherEnum.FOG:
			maker = R.drawable.w18;
			break;
		case WeatherEnum.FROST:

			break;
		case WeatherEnum.HAIL:
			maker = R.drawable.w19;
			break;
		case WeatherEnum.HAZE:

			break;
		case WeatherEnum.HEAVY_SNOW:
			maker = R.drawable.w16;
			break;
		case WeatherEnum.HUGE_RAIN_STORM:
			maker = R.drawable.w10;
			break;
		case WeatherEnum.LIGHT_SNOW:
			maker = R.drawable.w14;
			break;
		case WeatherEnum.MIDDLE_RAIN:
			maker = R.drawable.w8;
			break;
		case WeatherEnum.MIDDLE_SNOW:
			maker = R.drawable.w15;
			break;
		case WeatherEnum.OTHER:

			break;
		case WeatherEnum.OVERCAST:
			maker = R.drawable.w2;
			break;
		case WeatherEnum.RAIN_AND_SNOW:
			maker = R.drawable.w6;
			break;
		case WeatherEnum.RAIN_STORM:
			maker = R.drawable.w10;
			break;
		case WeatherEnum.SAND_BLOWING:

			break;
		case WeatherEnum.SAND_STORM:
			maker = R.drawable.w20;
			break;
		case WeatherEnum.SHOWERS:
			maker = R.drawable.w3;
			break;
		case WeatherEnum.SMALL_RAIN:
			maker = R.drawable.w7;
			break;
		case WeatherEnum.SNOW_SHOWERS:
			maker = R.drawable.w13;
			break;
		case WeatherEnum.SNOW_STROM:
			maker = R.drawable.w17;
			break;
		case WeatherEnum.SUN_SHINE:
			maker = R.drawable.w0;
			break;
		case WeatherEnum.T_SAND_STORM:
			maker = R.drawable.w20;
			break;
		case WeatherEnum.T_SHOWERS:
			maker = R.drawable.w4;
			break;
		default:
			break;

		}
		return maker;
	}

	public static int weatherName2Code(String name) {
		int code = 0;
		if (name.contains("转")) {// 天气带转字，取前面那部分
			String[] strs = name.split("转");
			name = strs[0];
			if (name.contains("到")) {// 如果转字前面那部分带到字，则取它的后部分
				strs = name.split("到");
				name = strs[1];
			}
		}

		HashMap<String, Integer> mWidgetWeatherIcon = initWidgetWeather();
		if (mWidgetWeatherIcon.containsKey(name)) {
			code = mWidgetWeatherIcon.get(name);
		}
		return code;
	}

	/**
	 * 图片融合 给图片加边框
	 * 
	 * @param src
	 *            图片
	 * @param watermark
	 *            边框
	 * @return
	 */
	public static Drawable addbiankuangtoPic(Drawable dsrc, Drawable dwatermark) {
		BitmapDrawable bdsrc = (BitmapDrawable) dsrc;
		BitmapDrawable bdwatermark = (BitmapDrawable) dwatermark;
		Bitmap src = bdsrc.getBitmap();
		Bitmap watermark = bdwatermark.getBitmap();
		if (src == null) {
			return null;
		}

		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(ww, wh, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, 0, 0, null);// 在 0，0坐标
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存
		BitmapDrawable res = null;
		try {
			res = new BitmapDrawable(newb);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据图片的URL获取drawable对象，如有问题则返回为null.
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static Drawable loadImageFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.jpg");
		} catch (IOException e) {
			// Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			// Log.d("test", "null drawable");
		} else {
			// Log.d("test", "not null drawable");
		}

		return drawable;
	}

	// 获取网络连接状态
	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager.getActiveNetworkInfo() != null) {
			return manager.getActiveNetworkInfo().isAvailable();
		} else {
			return false;
		}
	}

	// 获取WIFI状态
	public static boolean isWifiConn(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager.getActiveNetworkInfo() == null) {
			return false;
		}
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private static HashMap<String, Integer> initWidgetWeather() {
		HashMap<String, Integer> mWidgetWeatherIcon = new HashMap<String, Integer>();
		mWidgetWeatherIcon.put("暴雪", 15);
		mWidgetWeatherIcon.put("暴雨", 7);
		mWidgetWeatherIcon.put("大暴雨", 8);
		mWidgetWeatherIcon.put("大雪", 14);
		mWidgetWeatherIcon.put("大雨", 6);

		mWidgetWeatherIcon.put("多云", 2);
		mWidgetWeatherIcon.put("雷阵雨", 11);
		mWidgetWeatherIcon.put("雷阵雨冰雹", 21);
		mWidgetWeatherIcon.put("晴", 1);
		mWidgetWeatherIcon.put("沙尘暴", 22);

		mWidgetWeatherIcon.put("特大暴雨", 9);
		mWidgetWeatherIcon.put("雾", 18);
		mWidgetWeatherIcon.put("小雪", 12);
		mWidgetWeatherIcon.put("小雨", 4);
		mWidgetWeatherIcon.put("阴", 3);

		mWidgetWeatherIcon.put("雨夹雪", 16);
		mWidgetWeatherIcon.put("阵雪", 17);
		mWidgetWeatherIcon.put("阵雨", 10);
		mWidgetWeatherIcon.put("中雪", 13);
		mWidgetWeatherIcon.put("中雨", 5);
		return mWidgetWeatherIcon;
	}

	private static int[] _IAQIWeight = { 0, 5, 25, 75, 125, 175, 250, 350, 450 };
	private static int[] _SO2Weight = { 0, 5, 75, 325, 575, 725, 1200, 1850,
			2620 };
	private static int[] _NO2Weight = { 0, 5, 50, 150, 450, 950, 1770, 2735,
			3840 };
	private static int[] _COWeight = { 0, 2, 4, 8, 22, 47, 75, 105, 150 };
	private static int[] _O3Weight = { 0, 5, 80, 180, 250, 350, 600, 900, 1200 };
	private static int[] _PM10Weight = { 0, 5, 25, 100, 200, 300, 385, 460, 600 };
	private static int[] _PM25Weight = { 0, 5, 17, 55, 95, 132, 200, 300, 500 };

	public static int getIAQI(PollutantTypeEnum em, int value) {

		int iaqi;
		int[] list = new int[] {};
		switch (em) {
		case AQI:
			list = _IAQIWeight;
			break;
		case CO:
			list = _COWeight;
			break;
		case NO2:
			list = _NO2Weight;
			break;
		case O3:
			list = _O3Weight;
			break;
		case PM10:
			list = _PM10Weight;
			break;
		case pm25:
			list = _PM25Weight;
			break;
		case SO2:
			list = _SO2Weight;
			break;
		}
		iaqi = (getIAQIH(list, value) - getIAQIL(list, value))
				* (value - getBPL(list, value))
				/ (getBPH(list, value) - getBPL(list, value))
				+ getIAQIL(list, value);

		return iaqi;
	}

	/**
	 * 与BPH对应的空气质量分指数
	 * 
	 * @param em
	 * @param value
	 * @return
	 */
	private static int getIAQIH(int[] list, int value) {

		int high = 0;
		if (value > list[list.length - 1]) {
			high = _IAQIWeight[list.length - 1];
		} else {
			for (int i = 0; i < list.length; i++) {
				if (value <= list[i]) {
					if (i == 0) {
						high = _IAQIWeight[1];
						break;
					} else {
						high = _IAQIWeight[i];
						break;
					}
				}
			}
		}

		return high;
	}

	/**
	 * 与BPL对应的空气质量分指数
	 * 
	 * @param em
	 * @param value
	 * @return
	 */
	private static int getIAQIL(int[] list, int value) {

		int low = 0;
		if (value > list[list.length - 1]) {
			low = _IAQIWeight[list.length - 2];
		} else {
			for (int i = 0; i < list.length; i++) {
				if (value <= list[i]) {
					if (i == 0) {
						low = _IAQIWeight[0];
						break;
					} else {
						low = _IAQIWeight[i - 1];
						break;
					}
				}
			}
		}
		return low;
	}

	private static int getBPL(int[] list, int value) {
		int low = 0;
		if (value > list[list.length - 1]) {
			low = list[list.length - 2];
		} else {
			for (int i = 0; i < list.length; i++) {
				if (value <= list[i]) {
					if (i == 0) {
						low = list[0];
						break;
					} else {
						low = list[i - 1];
						break;
					}
				}
			}
		}

		return low;
	}

	private static int getBPH(int[] list, int value) {
		int high = 0;
		if (value > list[list.length - 1]) {
			high = list[list.length - 1];
		} else {
			for (int i = 0; i < list.length; i++) {
				if (value <= list[i]) {
					if (i == 0) {
						high = list[1];
						break;
					} else {
						high = list[i];
						break;
					}
				}
			}
		}

		return high;
	}

}
