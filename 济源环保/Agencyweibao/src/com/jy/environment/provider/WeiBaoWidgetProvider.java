package com.jy.environment.provider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.activity.LoadingActivity;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.model.Sweather;
import com.jy.environment.services.WidgetService;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.webservice.UrlComponent;

@SuppressLint("NewApi")
public class WeiBaoWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    private GetWeatherBtnTask btnTask = new GetWeatherBtnTask();
    /* 上下文环境 */
    Context thisContext;
    public static RemoteViews views;
    private static Sweather sweather;
    // String cityName = "";
    /* 内部类 */
    private GetWeatherTask getWeatherTask;
    /* 时间格式 */
    private final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
	    "HH:mm");
    private static SimpleDateFormat date = new SimpleDateFormat("M月d日");
    private static SimpleDateFormat myFmt = new SimpleDateFormat("MM/dd");
    private static SimpleDateFormat myFmt1 = new SimpleDateFormat("E");
    private static SimpleDateFormat myFmt2 = new SimpleDateFormat("HH");
    private static Date now = new Date();
    private static String weather_date = date.format(new Date());
    /* 天气情况参数 */
    private static String weather_temperature, site, weather_temp, aqi_data,
	    pm_data, pm, weather_today, refresh_date, refresh_time;
    static AppWidgetManager appWidgetManager1 = null;
    static int appWidgetId = 0;
    /*
     * 如果是在onUpdate()方法外（一般为Service内）更新AppWidget界面，则需要定义几个变量 public RemoteViews
     * views;//RemoteView对象 publicComponentName thisWidget; //组件名 public
     * AppWidgetManager manager; // AppWidget管理器 thisWidget = new
     * ComponentName(this,PictureAppWidgetProvider.class); manager =
     * AppWidgetManager.getInstance(this); manager.updateAppWidget(thisWidget,
     * views);
     */
    private static ComponentName thisWidget; // 组件名
    private static AppWidgetManager manager; // AppWidget管理器
    CityDB mCityDB;
    /* 所关注的城市天气信息 */
    ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
    /* 所关注的城市名称 */
    public static List<String> city_name_list = new ArrayList<String>();
    public static int index = 0;

    /* 当前所选城市名称 */
    static String selectCity = "";
    Intent intent;
    public static SharedPreferences sharedPref;// 缓存
    private static SharedPreferences.Editor editor;

    @Override
    public void onDisabled(Context context) {
	MyLog.i("onDisabled");
	super.onDisabled(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context,
	    AppWidgetManager appWidgetManager, int appWidgetId,
	    Bundle newOptions) {
	MyLog.i("onAppWidgetOptionsChanged");
	super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
		newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	    int[] appWidgetIds) {

	MyLog.i("onUpdate");
	appWidgetManager1 = appWidgetManager;
	int N = appWidgetIds.length;
	getWeatherTask = new GetWeatherTask(context);
	for (int i = 0; i < N; i++) {
	    appWidgetId = appWidgetIds[i];

	    Log.i("widget", "widget" + "onUpdate启动");
	    // 获取RemoteViews对象
	    initView(context);
	    sharedPref = context.getSharedPreferences("sharedPref",
		    context.MODE_PRIVATE);
	    selectCity = sharedPref.getString("dingweiCity", selectCity);
	    if (selectCity == null || selectCity.equals("")) {
		selectCity = "北京";
	    }
	    loadCityWeather(context, selectCity);
	    appWidgetManager.updateAppWidget(appWidgetId, views);
	}
	context.startService(new Intent(context, WidgetService.class));
	super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static void initView(Context context) {
	views = new RemoteViews(context.getPackageName(),
		R.layout.widget_provider_remoteviews1);
	// 设置点击widget进入主界面
	Intent intent = new Intent(context, LoadingActivity.class);
	intent.putExtra("iswidget", true);
	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		intent, PendingIntent.FLAG_CANCEL_CURRENT);
	views.setOnClickPendingIntent(R.id.today_weather_layout, pendingIntent);
	// 设置手动刷新按钮
	Intent UPintent = new Intent("com.mapuni.weibao.UPDATE.BUTTON");
	PendingIntent intentUp = PendingIntent.getBroadcast(context, 0,
		UPintent, PendingIntent.FLAG_CANCEL_CURRENT);
	views.setOnClickPendingIntent(R.id.img_refresh_btn, intentUp);
	// 设置切换城市
	Intent switchIntent = new Intent("com.mapuni.weibao.SWITCH");
	PendingIntent intent_switch = PendingIntent.getBroadcast(context, 0,
		switchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	views.setOnClickPendingIntent(R.id.site, intent_switch);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
	try {
	    super.onReceive(context, intent);
	    MyLog.i("widget" + "onReceive启动,selectCity" + selectCity);
	    thisContext = context;
	    MyLog.i("intent.getAction() :" + intent.getAction());
	    if (intent.getAction().equals("com.mapuni.weibao.UPDATE.BUTTON")) {
		selectCity = WeiBaoApplication.getInstance().getDingweicity();
		if (selectCity == null || selectCity.equals("")) {
		    sharedPref = context.getSharedPreferences("sharedPref",
			    context.MODE_PRIVATE);
		    selectCity = sharedPref
			    .getString("dingweiCity", selectCity);
		}
		if (NetUtil.getNetworkState(context) == 0) {

		} else {
		    loadCityWeatherBtn(context, selectCity);
		}

		// 切换城市
	    } else if (intent.getAction().equals("com.mapuni.weibao.UPDATE")) {
		selectCity = WeiBaoApplication.getInstance().getDingweicity();
		MyLog.i("intent.getAction()selectCity :" + selectCity);
		if (selectCity == null || selectCity.equals("")) {
		    sharedPref = context.getSharedPreferences("sharedPref",
			    context.MODE_PRIVATE);
		    selectCity = sharedPref
			    .getString("dingweiCity", selectCity);
		}
		if (NetUtil.getNetworkState(context) == 0) {
		    // Toast.makeText(context, "更新失败，网络不可用！", 1000).show();
		} else {
		    // Toast.makeText(context, "正在更新",
		    // Toast.LENGTH_SHORT).show();
		    // views = new RemoteViews(context.getPackageName(),
		    // R.layout.widget_provider_remoteviews);
		    loadCityWeather(context, selectCity);
		    // views.setTextViewText(R.id.refresh_date,
		    // myFmt.format(now));
		    // views.setTextViewText(R.id.refresh_time,
		    // DATEFORMAT.format(now));
		    // Toast.makeText(context, "更新完成",
		    // Toast.LENGTH_SHORT).show();
		}

		// 切换城市
	    } else if (intent.getAction().equals("com.mapuni.weibao.SWITCH")) {
		initCityData();// 获取关注的城市的列表
		// views = new RemoteViews(context.getPackageName(),
		// R.layout.widget_provider_remoteviews);
		if (city_name_list.size() == 1) {
		    Toast.makeText(context, "当前您只关注了一个城市，不能切换了", 1000).show();
		} else {
		    if (WeiBaoApplication.getInstance().getIsUpdate() != null
			    && WeiBaoApplication.getInstance().getIsUpdate()) { // 为真表明有添加或删除关注城市的操作
			WeiBaoApplication.getInstance().setIsUpdate(false);
		    }
		    if (!"".equals(selectCity)) {// selecCity不为空表明不是第一次切换城市，走下面
			index = city_name_list.indexOf(selectCity);// 动态获取当前所选城市在城市列表中的索引，防止删除城市列表中的某一个城市时发生越界异常
		    }
		    if (index == (city_name_list.size() - 1)) {
			index = 0;
		    } else {
			index++;
		    }
		    selectCity = city_name_list.get(index);
		    loadCityWeather(context, selectCity);
		    // Toast.makeText(context, "正在切换到" + selectCity + ",请稍等...",
		    // 1000)
		    // .show();
		}

	    }
	    try {

		AppWidgetManager appWidgetManger = AppWidgetManager
			.getInstance(context);
		int[] appIds = appWidgetManger
			.getAppWidgetIds(new ComponentName(context,
				WeiBaoWidgetProvider.class));
		appWidgetManger.updateAppWidget(appIds, views);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void initCityData() {
	mCityDB = WeiBaoApplication.getInstance().getCityDB();
	initcitys = mCityDB
		.queryBySqlReturnArrayListHashMap("select * from addcity");
	initcitys = selectCitys(initcitys);
	city_name_list = new ArrayList<String>();
	for (int i = 0; i < initcitys.size(); i++) {
	    String cityname = (String) initcitys.get(i).get("name");
	    city_name_list.add(cityname);
	}
    }

    @Override
    public void onEnabled(Context context) {
	MyLog.i("onEnabled");
	super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
	MyLog.i("onDeleted");
	final int N = appWidgetIds.length;
	for (int i = 0; i < N; i++) {
	    int appWidgetId = appWidgetIds[i];

	}
    }

    private void loadCityWeather(Context context, String city) {
	if (null == getWeatherTask) {
	    getWeatherTask = new GetWeatherTask(context);
	}
	if (getWeatherTask.getStatus() == AsyncTask.Status.PENDING) {
	    // Toast.makeText(context, "正在更新", Toast.LENGTH_SHORT).show();
	    getWeatherTask.execute(city);
	} else if (getWeatherTask.getStatus() == AsyncTask.Status.RUNNING) {
	    Log.i("CurrentTq",
		    context.getResources().getString(R.string.loading_data));
	} else if (getWeatherTask.getStatus() == AsyncTask.Status.FINISHED) {
	    getWeatherTask = new GetWeatherTask(context);
	    getWeatherTask.execute(city);
	}
    }

    private void loadCityWeatherBtn(Context context, String city) {
	if (null == btnTask) {
	    btnTask = new GetWeatherBtnTask();
	}
	if (btnTask.getStatus() == AsyncTask.Status.PENDING) {
	    Toast.makeText(context, "正在更新", Toast.LENGTH_SHORT).show();
	    btnTask.execute(city);
	} else if (btnTask.getStatus() == AsyncTask.Status.RUNNING) {
	    Log.i("CurrentTq",
		    context.getResources().getString(R.string.loading_data));
	} else if (btnTask.getStatus() == AsyncTask.Status.FINISHED) {
	    btnTask = new GetWeatherBtnTask();
	    Toast.makeText(context, "正在更新", Toast.LENGTH_SHORT).show();
	    btnTask.execute(city);
	}
    }

    public class GetWeatherBtnTask extends AsyncTask<String, Void, Sweather> {
	@Override
	protected Sweather doInBackground(String... params) {
	    String url = UrlComponent.getWeatherInfoNowByCity_Get(params[0]);
	    BusinessSearch search = new BusinessSearch();
	    MyLog.i("getWeatherInfoNowByCity_Get load url:" + url);
	    try {
		sweather = search.getNowWeather(url, params[0], 3600);
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    return sweather;
	}

	@Override
	protected void onPostExecute(Sweather result) {
	    Toast.makeText(thisContext, "更新完成", Toast.LENGTH_SHORT).show();
	    if (null == result) {
		return;
	    }
	    if (null == views) {
		initView(thisContext);
	    }
	    sweather = result;
	    updateTimeService(thisContext);
	}
    }

    public static class GetWeatherTask extends
	    AsyncTask<String, Void, Sweather> {

	Context myContext;

	public GetWeatherTask(Context context) {
	    this.myContext = context;
	}

	@Override
	protected Sweather doInBackground(String... params) {
	    String url = UrlComponent.getWeatherInfoNowByCity_Get(params[0]);
	    BusinessSearch search = new BusinessSearch();
	    MyLog.i("getWeatherInfoNowByCity_Get load url:" + url);
	    try {
		sweather = search.getNowWeather(url, params[0], 3600);
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    // MyLog.i("url :" + url);
	    // String netResult = ApiClient.connServerForResult(url);
	    // MyLog.i("netResult :" + netResult);
	    // try {
	    // if (netResult.equals("")) {
	    // return null;
	    // }
	    // JSONObject jsonObject = new JSONObject(netResult);
	    // JSONObject jsonObject2 = jsonObject.getJSONObject("weather");
	    // sweather = new Sweather(jsonObject2.getString("weather"),
	    // jsonObject2.getString("city"),
	    // jsonObject2.getString("pm25"),
	    // jsonObject2.getString("weekday"),
	    // jsonObject2.getString("feelTemp"),
	    // jsonObject2.getString("realTime"),
	    // jsonObject2.getString("date"),
	    // jsonObject2.getString("level"),
	    // jsonObject2.getString("WS"),
	    // jsonObject2.getString("WD"),
	    // jsonObject2.getString("SD"),
	    // jsonObject2.getString("PM2Dot5Data"),
	    // jsonObject2.getString("pm25"));
	    // return sweather;
	    // } catch (Exception e) {
	    // }
	    return sweather;
	}

	@Override
	protected void onPostExecute(Sweather result) {
	    // Toast.makeText(thisContext, "更新完成", Toast.LENGTH_SHORT).show();
	    if (null == result) {
		return;
	    }
	    if (null == views) {
		initView(myContext);
	    }
	    sweather = result;
	    updateTimeService(myContext);
	    // loadDataToViews(thisContext,result);
	    // views.setTextViewText(R.id.refresh_date, myFmt.format(now));
	    // views.setTextViewText(R.id.refresh_time, DATEFORMAT.format(now));
	    // updateTime(thisContext);
	    // if (result != null) {
	    // weather_temperature = result.getFeelTemp();
	    //
	    // weather_temp = result.getWeather();
	    // pm = result.getLevel();
	    // site = result.getCity();
	    // aqi_data = result.getPM2Dot5Data();
	    // pm_data = result.getLunar();// pm2.5
	    // refresh_date = myFmt.format(now);
	    // refresh_time = DATEFORMAT.format(now);
	    // weather_today = result.getWeekday().toString();
	    // views.setTextViewText(R.id.weather_temperature,
	    // weather_temperature);
	    // views.setTextViewText(R.id.pm_data, pm_data);
	    // views.setTextViewText(R.id.weather_temp, weather_temp);
	    // views.setTextViewText(R.id.site, site);
	    // views.setTextViewText(R.id.aqi_data, aqi_data);
	    // views.setTextViewText(R.id.weather_date, weather_date);
	    // views.setTextViewText(R.id.weather_today, weather_today);
	    // views.setTextViewText(R.id.refresh_date, myFmt.format(now));
	    // views.setTextViewText(R.id.refresh_time, DATEFORMAT.format(now));
	    // // 写入sharedPref
	    // sharedPref = thisContext.getSharedPreferences("sharedPref",
	    // thisContext.MODE_PRIVATE);
	    // editor = sharedPref.edit();
	    // editor.putString(site + "weather_temperature",
	    // weather_temperature);
	    // editor.putString(site + "pm_data", pm_data);
	    // editor.putString(site + "weather_temp", weather_temp);
	    // editor.putString(site + "aqi_data", aqi_data);
	    // editor.putString(site, site);
	    // editor.putString(site + "weather_date", weather_date);
	    // editor.putString(site + "weather_today", weather_today);
	    // editor.putString(site + "refresh_date", refresh_date);
	    // editor.putString(site + "refresh_time", refresh_time);
	    // editor.commit();
	    // int aqi = Integer.parseInt(aqi_data);
	    // if (aqi > 0 && aqi < 50) {
	    // views.setImageViewResource(R.id.pm, R.drawable.you);
	    // } else if (aqi > 51 && aqi < 100) {
	    // views.setImageViewResource(R.id.pm, R.drawable.liang);
	    // } else if (aqi > 101 && aqi < 150) {
	    // views.setImageViewResource(R.id.pm, R.drawable.qingdu);
	    // } else if (aqi > 151 && aqi < 200) {
	    // views.setImageViewResource(R.id.pm, R.drawable.zhongdu);
	    // } else if (aqi > 201 && aqi < 300) {
	    // views.setImageViewResource(R.id.pm, R.drawable.yanzhongdu);
	    // }
	    // // 设置根据天气情况更换widget背景以及img图标
	    // Integer nowTime = Integer.parseInt(myFmt2.format(now));
	    // if (weather_temp.contains("晴")
	    // && (nowTime >= 19 || nowTime < 7)) {
	    // views.setImageViewResource(R.id.bg, R.drawable.qing_ye);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w30);
	    // } else if (weather_temp.contains("多云")
	    // && (nowTime >= 19 || nowTime < 7)) {
	    // views.setImageViewResource(R.id.bg, R.drawable.yintian_ye);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w31);
	    // } else if (weather_temp.contains("阴")
	    // && (nowTime >= 19 || nowTime < 7)) {
	    // views.setImageViewResource(R.id.bg, R.drawable.yintian_ye);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w31);
	    // } else if (weather_temp.contains("雨")
	    // && (nowTime >= 19 || nowTime < 7)) {
	    // views.setImageViewResource(R.id.bg, R.drawable.yu_ye);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w33);
	    // } else if (weather_temp.contains("晴")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.qing);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_qing);
	    // } else if (weather_temp.contains("多云")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.duoyun);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_duoyun);
	    // } else if (weather_temp.contains("阴")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.yin);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_yin);
	    // } else if (weather_temp.contains("雨")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.yutian);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_yu);
	    // } else if (weather_temp.contains("小雪")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.xiaoxuebai);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_xue);
	    // } else if (weather_temp.equals("大雪")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.daxuebai);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_xue);
	    // } else if (weather_temp.equals("霾")) {
	    // views.setImageViewResource(R.id.bg, R.drawable.mai);
	    // views.setImageViewResource(R.id.weather_image_widget,
	    // R.drawable.w_wumai);
	    // } else {
	    // views.setImageViewResource(R.id.bg, R.drawable.yin);
	    // }
	    // thisWidget = new ComponentName(thisContext,
	    // WidgetProvider.class);
	    // manager = AppWidgetManager.getInstance(thisContext);
	    // manager.updateAppWidget(thisWidget, views);
	    // super.onPostExecute(result);
	    // } else { // 如果由于网络不可用，从手机缓存里查询
	    // sharedPref = thisContext.getSharedPreferences("sharedPref",
	    // thisContext.MODE_PRIVATE);
	    // weather_temperature = sharedPref.getString(selectCity
	    // + "weather_temperature", weather_temperature);
	    //
	    // weather_temp = sharedPref.getString(
	    // selectCity + "weather_temp", weather_temp);
	    // aqi_data = sharedPref.getString(selectCity + "aqi_data",
	    // aqi_data);
	    // pm_data = sharedPref.getString(selectCity + "pm_data", pm_data);
	    // site = sharedPref.getString(selectCity, selectCity);
	    // weather_date = sharedPref.getString(
	    // selectCity + "weather_date", weather_date);
	    // weather_today = sharedPref.getString(selectCity
	    // + "weather_today", weather_today);
	    // refresh_date = sharedPref.getString(
	    // selectCity + "refresh_date", refresh_date);
	    // refresh_time = sharedPref.getString(
	    // selectCity + "refresh_time", refresh_time);
	    // views.setTextViewText(R.id.aqi_data, aqi_data);
	    // views.setTextViewText(R.id.weather_temperature,
	    // weather_temperature);
	    // views.setTextViewText(R.id.weather_temp, weather_temp);
	    // views.setTextViewText(R.id.site, site);
	    // views.setTextViewText(R.id.pm_data, pm_data);
	    // views.setTextViewText(R.id.weather_date, weather_date);
	    // views.setTextViewText(R.id.weather_today, weather_today);
	    // views.setTextViewText(R.id.refresh_date, refresh_date);
	    // views.setTextViewText(R.id.refresh_time, refresh_time);
	    // thisWidget = new ComponentName(thisContext,
	    // WidgetProvider.class);
	    // manager = AppWidgetManager.getInstance(thisContext);
	    // manager.updateAppWidget(thisWidget, views);
	    // }
	    super.onPostExecute(result);
	}
    }

    // }

    // public static RemoteViews updateTime(Context context) {
    // RemoteViews views = new RemoteViews(context.getPackageName(),
    // R.layout.widget_provider_remoteviews);
    // //闹钟启动延时一秒，这里应该加上去
    // // String str = DATEFORMAT.format(new Date());
    // String str = DATEFORMAT.format(new Date(System.currentTimeMillis() +
    // 1000));
    // views.setTextViewText(R.id.time, str);
    // return views;
    // }
    // public RemoteViews updateTime(Context context) {
    // MyLog.i("updateTime");
    // // RemoteViews views = new RemoteViews(context.getPackageName(),
    // // R.layout.widget_provider_remoteviews);
    // // 闹钟启动延时一秒，这里应该加上去
    // // String str = DATEFORMAT.format(new Date());
    // if (null == views) {
    // initView(context);
    // selectCity = WeiBaoApplication.getInstance().getDingweicity();
    // loadCityWeather(context, selectCity);
    // }
    // String str = DATEFORMAT.format(new Date(System.currentTimeMillis()));
    // views.setTextViewText(R.id.time, str);
    // return views;
    // }

    private static void loadDataToViews(Context context, Sweather result) {
	views.setTextViewText(R.id.refresh_date, myFmt.format(now));
	views.setTextViewText(R.id.refresh_time, DATEFORMAT.format(now));
	// updateTimeService(context);
	if (result != null) {
	    weather_temperature = result.getFeelTemp();
	    weather_temp = result.getWeather();
	    pm = result.getLevel();
	    site = result.getCity();
	    aqi_data = result.getPM2Dot5Data();
	    try {
	    	pm_data = result.getPm25();// pm2.5
		} catch (Exception e) {
			pm_data = "";
		}
	    System.out.println("pm25:+++++++++++++++"+pm_data);
	    System.out.println("aqi_data:+++++++++++++++"+aqi_data);
	    System.out.println("result:+++++++++++++++"+result);
	    refresh_date = myFmt.format(now);
	    refresh_time = DATEFORMAT.format(now);
	    weather_today = result.getWeekday().toString();
	    views.setTextViewText(R.id.weather_temperature, weather_temperature);
	    views.setTextViewText(R.id.weather_temp, weather_temp);
	    views.setTextViewText(R.id.site, site);
	    views.setTextViewText(R.id.aqi_data, aqi_data);
	    views.setTextViewText(R.id.weather_date, weather_date);
	    views.setTextViewText(R.id.weather_today, weather_today);
	    views.setTextViewText(R.id.refresh_date, myFmt.format(now));
	    views.setTextViewText(R.id.refresh_time, DATEFORMAT.format(now));
	    views.setTextViewText(R.id.text_pm25, pm_data);
	    // 写入sharedPref
	    sharedPref = context.getSharedPreferences("sharedPref",
		    Context.MODE_PRIVATE);
	    editor = sharedPref.edit();
	    editor.putString(site + "weather_temperature", weather_temperature);
	    editor.putString(site + "pm_data", pm_data);
	    editor.putString(site + "weather_temp", weather_temp);
	    editor.putString(site + "aqi_data", aqi_data);
	    editor.putString(site, site);
	    editor.putString(site + "weather_date", weather_date);
	    editor.putString(site + "weather_today", weather_today);
	    editor.putString(site + "refresh_date", refresh_date);
	    editor.putString(site + "refresh_time", refresh_time);
	    editor.commit();
	    try {
		int aqi = Integer.parseInt(aqi_data);
		if (aqi > 0 && aqi < 50) {
		    views.setImageViewResource(R.id.pm, R.drawable.you);
		} else if (aqi > 51 && aqi < 100) {
		    views.setImageViewResource(R.id.pm, R.drawable.liang);
		} else if (aqi > 101 && aqi < 150) {
		    views.setImageViewResource(R.id.pm, R.drawable.qingdu);
		} else if (aqi > 151 && aqi < 200) {
		    views.setImageViewResource(R.id.pm, R.drawable.zhongdu);
		} else if (aqi > 201 && aqi < 300) {
		    views.setImageViewResource(R.id.pm, R.drawable.yanzhongdu);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }

	    // 设置根据天气情况更换widget背景以及img图标
	    Integer nowTime = Integer.parseInt(myFmt2.format(now));
	    String climateString = CommonUtil.getWeatherIconString(weather_temp,0);
	    if (climateString.contains("晴") && (nowTime >= 19 || nowTime < 7)) {
		views.setImageViewResource(R.id.bg, R.drawable.qing_ye);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_qingtian);
	    } else if (climateString.contains("多云")
		    && (nowTime >= 19 || nowTime < 7)) {
		views.setImageViewResource(R.id.bg, R.drawable.duoyun_ye);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_yin);
	    } else if (climateString.contains("阴")
		    && (nowTime >= 19 || nowTime < 7)) {
		views.setImageViewResource(R.id.bg, R.drawable.yintian_ye);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_yin);
	    } else if (climateString.contains("雨")
		    && (nowTime >= 19 || nowTime < 7)) {
		views.setImageViewResource(R.id.bg, R.drawable.yu_ye);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_yu);
	    } else if (climateString.contains("雪")
		    && (nowTime >= 19 || nowTime < 7)) {
		views.setImageViewResource(R.id.bg, R.drawable.xue_ye);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_xiaoxue);
	    }else if (climateString.equals("霾")) {
		views.setImageViewResource(R.id.bg, R.drawable.mai_ye);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_mai);
	    }else if (climateString.contains("晴")) {
		views.setImageViewResource(R.id.bg, R.drawable.qing);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_qingtian);
	    } else if (climateString.contains("多云")) {
		views.setImageViewResource(R.id.bg, R.drawable.duoyun);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_duoyun);
	    } else if (climateString.contains("阴")) {
		views.setImageViewResource(R.id.bg, R.drawable.yin);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_yin);
	    } else if (climateString.contains("雨")) {
		views.setImageViewResource(R.id.bg, R.drawable.yutian);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_yu);
	    } else if (climateString.contains("雪")) {
		views.setImageViewResource(R.id.bg, R.drawable.xue);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_xiaoxue);
	    } else if (climateString.equals("霾")) {
		views.setImageViewResource(R.id.bg, R.drawable.mai);
		views.setImageViewResource(R.id.weather_image_widget,
			R.drawable.weather_icon_mai);
	    } else {
		views.setImageViewResource(R.id.bg, R.drawable.duoyun);
	    }
//	    if (weather_temp.contains("晴") && (nowTime >= 19 || nowTime < 7)) {
//		views.setImageViewResource(R.id.bg, R.drawable.qing_ye);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w30);
//	    } else if (weather_temp.contains("多云")
//		    && (nowTime >= 19 || nowTime < 7)) {
//		views.setImageViewResource(R.id.bg, R.drawable.yintian_ye);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w31);
//	    } else if (weather_temp.contains("阴")
//		    && (nowTime >= 19 || nowTime < 7)) {
//		views.setImageViewResource(R.id.bg, R.drawable.yintian_ye);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w31);
//	    } else if (weather_temp.contains("雨")
//		    && (nowTime >= 19 || nowTime < 7)) {
//		views.setImageViewResource(R.id.bg, R.drawable.yu_ye);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w33);
//	    } else if (weather_temp.contains("晴")) {
//		views.setImageViewResource(R.id.bg, R.drawable.qing);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_qing);
//	    } else if (weather_temp.contains("多云")) {
//		views.setImageViewResource(R.id.bg, R.drawable.duoyun);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_duoyun);
//	    } else if (weather_temp.contains("阴")) {
//		views.setImageViewResource(R.id.bg, R.drawable.yin);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_yin);
//	    } else if (weather_temp.contains("雨")) {
//		views.setImageViewResource(R.id.bg, R.drawable.yutian);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_yu);
//	    } else if (weather_temp.contains("小雪")) {
//		views.setImageViewResource(R.id.bg, R.drawable.xiaoxuebai);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_xue);
//	    } else if (weather_temp.equals("大雪")) {
//		views.setImageViewResource(R.id.bg, R.drawable.daxuebai);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_xue);
//	    } else if (weather_temp.equals("霾")) {
//		views.setImageViewResource(R.id.bg, R.drawable.mai);
//		views.setImageViewResource(R.id.weather_image_widget,
//			R.drawable.w_wumai);
//	    } else {
//		views.setImageViewResource(R.id.bg, R.drawable.yin);
//	    }
	    thisWidget = new ComponentName(context, WeiBaoWidgetProvider.class);
	    manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(thisWidget, views);
	} else { // 如果由于网络不可用，从手机缓存里查询
	    sharedPref = context.getSharedPreferences("sharedPref",
		    Context.MODE_PRIVATE);
	    weather_temperature = sharedPref.getString(selectCity
		    + "weather_temperature", weather_temperature);

	    weather_temp = sharedPref.getString(selectCity + "weather_temp",
		    weather_temp);
	    aqi_data = sharedPref.getString(selectCity + "aqi_data", aqi_data);
	    pm_data = sharedPref.getString(selectCity + "pm_data", pm_data);
	    site = sharedPref.getString(selectCity, selectCity);
	    weather_date = sharedPref.getString(selectCity + "weather_date",
		    weather_date);
	    weather_today = sharedPref.getString(selectCity + "weather_today",
		    weather_today);
	    refresh_date = sharedPref.getString(selectCity + "refresh_date",
		    refresh_date);
	    refresh_time = sharedPref.getString(selectCity + "refresh_time",
		    refresh_time);
	    views.setTextViewText(R.id.aqi_data, aqi_data);
	    views.setTextViewText(R.id.weather_temperature, weather_temperature);
	    views.setTextViewText(R.id.weather_temp, weather_temp);
	    views.setTextViewText(R.id.site, site);
	    views.setTextViewText(R.id.text_pm25, pm_data);
	    views.setTextViewText(R.id.weather_date, weather_date);
	    views.setTextViewText(R.id.weather_today, weather_today);
	    views.setTextViewText(R.id.refresh_date, refresh_date);
	    views.setTextViewText(R.id.refresh_time, refresh_time);
	    thisWidget = new ComponentName(context, WeiBaoWidgetProvider.class);
	    manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(thisWidget, views);
	}
    }

    public static RemoteViews updateTimeService(Context context) {
	MyLog.i("updateTime views：" + views);
	// RemoteViews views = new RemoteViews(context.getPackageName(),
	// R.layout.widget_provider_remoteviews);
	// RemoteViews remoteViews ;
	if (null == views) {
	    views = new RemoteViews(context.getPackageName(),
		    R.layout.widget_provider_remoteviews1);
	}
	String str = DATEFORMAT.format(new Date(System.currentTimeMillis()));
	views.setTextViewText(R.id.time, str);
	if (null != sweather) {
	    loadDataToViews(context, sweather);
	} else {
	    sharedPref = context.getSharedPreferences("sharedPref",
		    context.MODE_PRIVATE);
	    selectCity = sharedPref.getString("dingweiCity", selectCity);
	    if (selectCity == null || selectCity.equals("")) {
		selectCity = "北京";
	    }
	    GetWeatherTask getWeatherTask = new GetWeatherTask(context);
	    getWeatherTask.execute(selectCity);
	}

	return views;
    }

    private ArrayList<HashMap<String, Object>> selectCitys(
	    ArrayList<HashMap<String, Object>> citys) {
	try {
	    if (null != citys && citys.size() > 0) {
		for (int i = 0; i < citys.size(); i++) {
		    if ("1".equals(citys.get(i).get("islocation"))) {
			HashMap<String, Object> hashMapFirst = citys.get(0);
			HashMap<String, Object> hashMapLocation = citys.get(i);
			citys.set(0, hashMapLocation);
			citys.set(i, hashMapFirst);
		    }
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return citys;
    }
}