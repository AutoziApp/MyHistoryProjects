package com.mapuni.mobileenvironment.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mapuni.mobileenvironment.R;


public class CommonUtil {
	public static String DETAILMODEL = "MONITORYFRAGMENT";
	private static HashMap<String, Integer> mWeatherIcon;// 天气图标


	// 判断是否为平板
	public static boolean isPad(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		// 屏幕高度
		float screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String value) {
		return null == value || value.equals("") || value.equals("null");
	}

	public static Dialog getCustomeDialog(Activity activity, int style, int customView) {
		Dialog dialog = new Dialog(activity, style);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(customView);
		Window window = dialog.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		lp.x = 0;
		lp.y = 0;
		window.setAttributes(lp);
		return dialog;
	}
	public static int StringToInt(String data) {
		int temp;
		try {
			temp = Integer.valueOf(data);
		} catch (Exception e) {
			// TODO: handle exception
			temp = 0;
		}
		return temp;
	}


	// 解压assest中的文件
	public static void unZip(Context context, String assetName, String outputDirectory) throws IOException {
		// 创建解压目标目录
		File file = new File(outputDirectory);
		// 如果目标目录不存在，则创建
		if (!file.exists()) {
			file.mkdirs();
		}
		InputStream inputStream = null;
		// 打开压缩文件
		inputStream = context.getAssets().open(assetName);
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		// 使用1Mbuffer
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
		while (zipEntry != null) {
			// 如果是一个目录
			if (zipEntry.isDirectory()) {
				// String name = zipEntry.getName();
				// name = name.substring(0, name.length() - 1);
				file = new File(outputDirectory + File.separator + zipEntry.getName());
				Log.i("CommonUtil","outputDirectory + File.separator + zipEntry.getName()" + outputDirectory + File.separator
						+ zipEntry.getName());
				file.mkdir();
			} else {
				// 如果是文件
				file = new File(outputDirectory + File.separator + zipEntry.getName());
				Log.i("CommonUtil","outputDirectory + File.separator + zipEntry.getName()" + outputDirectory + File.separator
						+ zipEntry.getName());
				// 创建该文件
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((count = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.close();
			}
			// 定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}

	/**
	 * 根据AQI获取背景色
	 * 
	 * @param Aqi
	 * @return
	 */
	public static int getRIdByAQIMap(String Aqi) {
		try {
			double aqi = Double.parseDouble(Aqi);
			if (aqi <= 50) {
				return R.drawable.k1;
			} else if (aqi <= 100) {
				return R.drawable.k2;
			} else if (aqi <= 150) {
				return R.drawable.k3;
			} else if (aqi <= 200) {
				return R.drawable.k4;
			} else if (aqi <= 300) {
				return R.drawable.k5;
			} else {
				return R.drawable.k6;
			}
		} catch (Exception e) {
		}
		return R.drawable.k1;
	}

	/**
	 * 根据AQI获取背景色
	 * 
	 * @param Aqi
	 * @return
	 */
	public static int getRIdByAQI(String Aqi) {
		try {
			double aqi = Double.parseDouble(Aqi);
			if (aqi <= 0) {
				return R.drawable.airkong;
			} else if (aqi <= 50) {
				return R.drawable.air1;
			} else if (aqi <= 100) {
				return R.drawable.air2;
			} else if (aqi <= 150) {
				return R.drawable.air3;
			} else if (aqi <= 200) {
				return R.drawable.air4;
			} else if (aqi <= 300) {
				return R.drawable.air5;
			} else {
				return R.drawable.air6;
			}
		} catch (Exception e) {
		}
		return R.drawable.air6;
	}

	/**
	 * 根据API获取等级
	 * 
	 * @param Aqi
	 * @return
	 */
	public static String getDengJiByAQI(String Aqi) {
		try {
			int aqi = Integer.parseInt(Aqi);
			if (aqi <= 50) {
				return "优";
			} else if (aqi <= 100) {
				return "良";
			} else if (aqi <= 150) {
				return "轻度";
			} else if (aqi <= 200) {
				return "中度";
			} else if (aqi <= 300) {
				return "重度";
			} else {
				return "严重";
			}
		} catch (Exception e) {
		}
		return "优";
	}

	/**
	 * 根据API获取等级
	 * 
	 * @param Aqi
	 * @return
	 * 
	 * 		优：#05a703
	 * 
	 *         良：#b5b90d
	 * 
	 *         轻度污染：#92420c
	 * 
	 *         中度污染：#f8000e
	 * 
	 *         重度污染：#7846a5
	 * 
	 *         严重污染：#4e0000 优：#00e400 良：#ffff00 轻度污染：#ff7e00 中度污染：#ff0000
	 *         重度污染：#800080 严重污染：#7e0000
	 */
	public static String getColorByDengji(String Aqi) {
		try {
			if ("优".equals(Aqi)) {
				// return R.color.you_color;
				return "#00e400";
			} else if ("良".equals(Aqi)) {
				// return R.color.liang_color;
				return "#ffff00";
			} else if ("轻度污染".equals(Aqi)) {
				// return R.color.qingdu_color;
				return "#ff7e00";
			} else if ("中度污染".equals(Aqi)) {
				// return R.color.zhongdu_color;
				return "#ff0000";
			} else if ("重度污染".equals(Aqi)) {
				return "#800080";
				// return R.color.zhongdu2_color;
			} else if ("严重污染".equals(Aqi)) {
				// return R.color.yanzhong_color;
				return "#7e0000";
			}
		} catch (Exception e) {
		}
		return "#05a703";
	}

	public static Drawable getColorByDengjiToInt(Context context, String Aqi) {
		try {
			if ("优".equals(Aqi)) {
				return context.getResources().getDrawable(R.drawable.air1);
			} else if ("良".equals(Aqi)) {
				return context.getResources().getDrawable(R.drawable.air2);
			} else if ("轻度污染".equals(Aqi)) {
				return context.getResources().getDrawable(R.drawable.air3);
			} else if ("中度污染".equals(Aqi)) {
				return context.getResources().getDrawable(R.drawable.air4);
			} else if ("重度污染".equals(Aqi)) {
				return context.getResources().getDrawable(R.drawable.air5);
			} else if ("严重污染".equals(Aqi)) {
				return context.getResources().getDrawable(R.drawable.air6);
			}
		} catch (Exception e) {
		}
		return context.getResources().getDrawable(R.drawable.air1);
	}

//	public static CityLocationModel getLocationByCity(String cityName) {
//		CityLocationModel locationModel = new CityLocationModel();
//		locationModel.setCityName(cityName);
//		if (cityName.equals("贵阳市")) {
//			locationModel.setLat("26.652747");
//			locationModel.setLon("106.710000");
//		} else if (cityName.equals("六盘水市")) {
//			locationModel.setLat("26.580000");
//			locationModel.setLon("104.820000");
//		} else if (cityName.equals("遵义市")) {
//			locationModel.setLat("27.700000");
//			locationModel.setLon("106.900000");
//		} else if (cityName.equals("安顺市")) {
//			locationModel.setLat("26.250000");
//			locationModel.setLon("105.920000");
//		} else if (cityName.equals("毕节市")) {
//			locationModel.setLat("27.320000");
//			locationModel.setLon("105.290000");
//		} else if (cityName.equals("铜仁市")) {
//			locationModel.setLat("27.730000");
//			locationModel.setLon("109.210000");
//		} else if (cityName.equals("黔西南州")) {
//			locationModel.setLat("25.095598");
//			locationModel.setLon("104.900439");
//		} else if (cityName.equals("黔东南州")) {
//			locationModel.setLat("26.584835");
//			locationModel.setLon("107.985220");
//		} else if (cityName.equals("黔南州")) {
//			locationModel.setLat("26.265428");
//			locationModel.setLon("107.522844");
//		} else if(cityName.equals("武汉")){
////			30.5984280000,114.3118310000
//			locationModel.setLat("30.5984280000");
//			locationModel.setLon("114.3118310000");
//		}else {
//			locationModel.setCityName("贵阳市");
//			locationModel.setLat("26.652747");
//			locationModel.setLon("106.710000");
//		}
//		return locationModel;
//	}

	// 获取城市对应的城市编码
	public static String getCode(String cityName) {
		String code = "";
		if (cityName.equals("贵阳市")) {
			code = "520100000";
		} else if (cityName.equals("六盘水市")) {
			code = "520200000";
		} else if (cityName.equals("遵义市")) {
			code = "520300000";
		} else if (cityName.equals("安顺市")) {
			code = "520400000";
		} else if (cityName.equals("铜仁市")) {
			code = "522200000";
		} else if (cityName.equals("毕节市")) {
			code = "522400000";
		} else if (cityName.equals("黔西南布依族苗族自治州")) {
			code = "522300000";
		} else if (cityName.equals("黔东南苗族侗族自治州")) {
			code = "522600000";
		} else if (cityName.equals("黔南布依族苗族自治州")) {
			code = "522700000";
		}
		return code;
	}

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

//	public static Drawable getBigImg(Context context, String weather) {
//		Drawable drawImg = context.getResources().getDrawable(R.drawable.big_tq_qing);
//		if (weather.equals("晴")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_qing);
//		} else if (weather.equals("阴")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_yin);
//		} else if (weather.equals("多云")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_duoyun);
//		} else if (weather.equals("冻雨")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_dongyu);
//		} else if (weather.equals("浮尘")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_fuchen);
//		} else if (weather.equals("雾")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_wu);
//		} else if (weather.equals("霾")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_mai);
//		} else if (weather.equals("扬沙")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_yangsha);
//		} else if (weather.contains("雷阵雨")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_leizhenyu);
//		} else if (weather.contains("阵雨")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_zhenyu);
//		} else if (weather.contains("雨") && !weather.contains("雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_yu);
//		} else if (weather.equals("阵雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_zhenxue);
//		} else if (weather.equals("雨夹雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_yujiaxue);
//		} else if (weather.contains("雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_xue);
//		} else {
//			drawImg = context.getResources().getDrawable(R.drawable.big_tq_qing);
//		}
//		return drawImg;
//	}

//	public static Drawable getSmallImg(Context context, String weather) {
//		Drawable drawImg = context.getResources().getDrawable(R.drawable.small_tq_qing);
//		if (weather.equals("晴")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_qing);
//		} else if (weather.equals("阴")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_yin);
//		} else if (weather.equals("多云")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_duoyun);
//		} else if (weather.equals("冻雨")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_dongyu);
//		} else if (weather.equals("浮尘")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_fuchen);
//		} else if (weather.equals("雾")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_wu);
//		} else if (weather.equals("霾")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_mai);
//		} else if (weather.equals("扬沙")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_yangsha);
//		} else if (weather.contains("雷阵雨")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_leizhenyu);
//		} else if (weather.contains("阵雨")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_zhenyu);
//		} else if (weather.contains("雨") && !weather.contains("雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_yu);
//		} else if (weather.equals("阵雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_zhenxue);
//		} else if (weather.equals("雨夹雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_yujiaxue);
//		} else if (weather.contains("雪")) {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_xue);
//		} else {
//			drawImg = context.getResources().getDrawable(R.drawable.small_tq_qing);
//		}
//		return drawImg;
//	}

//	public static Drawable getBackBround(Context context, String weather) {
//		Drawable drawable = context.getResources().getDrawable(R.drawable.bg_qing);
//		int m = getDateSx();
//		if (m == 1) {
//			drawable = context.getResources().getDrawable(R.drawable.bg_wanshang);
//		} else {
//			if (weather.equals("晴")) {
//				drawable = context.getResources().getDrawable(R.drawable.bg_qing);
//			} else if (weather.equals("多云")) {
//				drawable = context.getResources().getDrawable(R.drawable.bg_duoyun);
//			} else if (weather.equals("阴")) {
//				drawable = context.getResources().getDrawable(R.drawable.bg_yin);
//			} else if (weather.equals("霾")) {
//				drawable = context.getResources().getDrawable(R.drawable.bg_mai);
//			} else if (weather.contains("雨")) {
//				drawable = context.getResources().getDrawable(R.drawable.bg_yu);
//			} else if (weather.contains("雪")) {
//				drawable = context.getResources().getDrawable(R.drawable.bg_xue);
//			} else {
//				drawable = context.getResources().getDrawable(R.drawable.bg_qing);
//			}
//		}
//		return drawable;
//	}
public static HashMap<String, Integer> initWeatherIconMap() {
	if (mWeatherIcon != null && !mWeatherIcon.isEmpty())
		return mWeatherIcon;
	mWeatherIcon = new HashMap<String, Integer>();
	mWeatherIcon.put("暴雪", R.mipmap.weather_icon_baoxue);
	mWeatherIcon.put("暴雨", R.mipmap.weather_icon_baoyu);
	mWeatherIcon.put("大暴雨", R.mipmap.weather_icon_dabaoyu);
	mWeatherIcon.put("大雪", R.mipmap.weather_icon_daxue);
	mWeatherIcon.put("大雨", R.mipmap.weather_icon_dayu);
	mWeatherIcon.put("冻雨", R.mipmap.weather_icon_dongyu);
	mWeatherIcon.put("多云", R.mipmap.weather_icon_duoyun);
	mWeatherIcon.put("浮尘", R.mipmap.weather_icon_fuchen);
	mWeatherIcon.put("降雪", R.mipmap.weather_icon_jiangxue);
	mWeatherIcon.put("降雨", R.mipmap.weather_icon_jiangyu);
	mWeatherIcon.put("雷阵雨", R.mipmap.weather_icon_leizhenyu);
	mWeatherIcon.put("霾", R.mipmap.weather_icon_mai);
	mWeatherIcon.put("轻度霾", R.mipmap.weather_icon_qingdumai);
	mWeatherIcon.put("轻微霾", R.mipmap.weather_icon_qingweimai);
	mWeatherIcon.put("晴", R.mipmap.weather_icon_qingtian);
	mWeatherIcon.put("沙尘暴", R.mipmap.weather_icon_shachenbao);
	mWeatherIcon.put("特大暴雨", R.mipmap.weather_icon_tedabaoyu);
	mWeatherIcon.put("雾", R.mipmap.weather_icon_wu);
	mWeatherIcon.put("小雪", R.mipmap.weather_icon_xiaoxue);
	mWeatherIcon.put("阵雪", R.mipmap.weather_icon_daxue);

	mWeatherIcon.put("小雨", R.mipmap.weather_icon_xiaoyu);
	mWeatherIcon.put("阵雨", R.mipmap.weather_icon_xiaoyu);
	mWeatherIcon.put("扬沙", R.mipmap.weather_icon_yangsha);
	mWeatherIcon.put("阴", R.mipmap.weather_icon_yin);
	mWeatherIcon.put("雨", R.mipmap.weather_icon_yu);
	mWeatherIcon.put("雨夹雪", R.mipmap.weather_icon_yuajiaxue);
	mWeatherIcon.put("中雪", R.mipmap.weather_icon_zhongxue);
	mWeatherIcon.put("中雨", R.mipmap.weather_icon_zhongyu);

	return mWeatherIcon;
}
	public static String getWeatherIconString(String climate, int m) {
		// String weatherEnd = "晴";
		// MyLog.i("climate 原始:" + climate);
		if (m == 1) {
			if (climate.contains("：")) {
				climate = climate.substring(climate.indexOf("：") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("，")) {
				climate = climate.substring(0, climate.indexOf("，"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("、")) {
				climate = climate.substring(0, climate.indexOf("、"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("夜间")) {
				climate = climate.substring(0, climate.indexOf("夜间"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("白天")) {
				climate = climate.replaceAll("白天", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("大部")) {
				climate = climate.replaceAll("大部", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("早有")) {
				climate = climate.replaceAll("早有", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("短暂")) {
				climate = climate.replaceAll("短暂", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("有风")) {
				climate = climate.replaceAll("有风", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("零星")) {
				climate = climate.replaceAll("零星", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("分散性")) {
				climate = climate.replaceAll("分散性", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("转")) {
				climate = climate.substring(0, climate.indexOf("转"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("或")) {
				climate = climate.substring(0, climate.indexOf("或"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("伴有")) {
				climate = climate.substring(0, climate.indexOf("伴"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("有")) {
				climate = climate.substring(climate.indexOf("有") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("到")) {
				climate = climate.substring(climate.indexOf("到") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("-")) {
				climate = climate.substring(climate.indexOf("-") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("间")) {
				climate = climate.substring(climate.indexOf("间") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("霾")) {
				climate = "霾";
			}
		} else {
			if (climate.contains("：")) {
				climate = climate.substring(climate.indexOf("：") + 1);
			}
			if (climate.contains("，")) {
				climate = climate.substring(0, climate.indexOf("，"));
			}
			if (climate.contains("、")) {
				climate = climate.substring(0, climate.indexOf("、"));
			}
			if (climate.contains("夜间")) {
				climate = climate.substring(0, climate.indexOf("夜间"));
			}
			if (climate.contains("白天")) {
				climate = climate.replaceAll("白天", "");
			}
			if (climate.contains("大部")) {
				climate = climate.replaceAll("大部", "");
			}
			if (climate.contains("早有")) {
				climate = climate.replaceAll("早有", "");
			}
			if (climate.contains("短暂")) {
				climate = climate.replaceAll("短暂", "");
			}
			if (climate.contains("有风")) {
				climate = climate.replaceAll("有风", "");
			}
			if (climate.contains("零星")) {
				climate = climate.replaceAll("零星", "");
			}
			if (climate.contains("分散性")) {
				climate = climate.replaceAll("分散性", "");
			}
			if (climate.contains("转")) {
				climate = climate.substring(0, climate.indexOf("转"));
			}
			if (climate.contains("或")) {
				climate = climate.substring(0, climate.indexOf("或"));
			}
			if (climate.contains("伴有")) {
				climate = climate.substring(0, climate.indexOf("伴"));
			}
			if (climate.contains("有")) {
				climate = climate.substring(climate.indexOf("有") + 1);
			}
			if (climate.contains("到")) {
				climate = climate.substring(climate.indexOf("到") + 1);
			}
			if (climate.contains("-")) {
				climate = climate.substring(climate.indexOf("-") + 1);
			}
			if (climate.contains("间")) {
				climate = climate.substring(climate.indexOf("间") + 1);
			}
			if (climate.contains("霾")) {
				climate = "霾";
			}
		}
		// MyLog.i("climate :" + climate);
		return climate;
	}



	public static int getDengJiByType(int value, String type, double covalue) {
		int drawable = R.drawable.air1;
		if (type.equals("1")) {
			if (value <= 0) {
				return R.drawable.airkong;
			}
			// PM2.5标准
			else if (value <= 35) {
				return R.drawable.air1;
			} else if (value <= 75) {
				return R.drawable.air2;
			} else if (value <= 115) {
				return R.drawable.air3;
			} else if (value <= 150) {
				return R.drawable.air4;
			} else if (value <= 250) {
				return R.drawable.air5;
			} else {
				return R.drawable.airkong;
			}
		} else if (type.equals("0")) {
			// PM10标准
			if (value <= 0) {
				return R.drawable.airkong;
			} else if (value <= 50) {
				return R.drawable.air1;
			} else if (value <= 150) {
				return R.drawable.air2;
			} else if (value <= 250) {
				return R.drawable.air3;
			} else if (value <= 350) {
				return R.drawable.air4;
			} else if (value <= 420) {
				return R.drawable.air5;
			} else {
				return R.drawable.airkong;
			}
		} else if (type.equals("2")) {
			// o3标准
			if (value <= 0) {
				return R.drawable.airkong;
			} else if (value <= 160) {
				return R.drawable.air1;
			} else if (value <= 200) {
				return R.drawable.air2;
			} else if (value <= 300) {
				return R.drawable.air3;
			} else if (value <= 400) {
				return R.drawable.air4;
			} else if (value <= 800) {
				return R.drawable.air5;
			} else {
				return R.drawable.airkong;
			}
		} else if (type.equals("3")) {
			// so2标准
			if (value <= 0) {
				return R.drawable.airkong;
			} else if (value <= 150) {
				return R.drawable.air1;
			} else if (value <= 500) {
				return R.drawable.air2;
			} else if (value <= 650) {
				return R.drawable.air3;
			} else if (value <= 800) {
				return R.drawable.air4;
			} else {
				return R.drawable.airkong;
			}
		} else if (type.equals("4")) {
			// no2标准
			if (value <= 0) {
				return R.drawable.airkong;
			} else if (value <= 100) {
				return R.drawable.air1;
			} else if (value <= 200) {
				return R.drawable.air2;
			} else if (value <= 700) {
				return R.drawable.air3;
			} else if (value <= 1200) {
				return R.drawable.air4;
			} else if (value <= 2340) {
				return R.drawable.air5;
			} else {
				return R.drawable.airkong;
			}
		} else if (type.equals("5")) {
			// co标准
			if (covalue <= 0) {
				return R.drawable.airkong;
			} else if (covalue <= 5) {
				return R.drawable.air1;
			} else if (covalue <= 10) {
				return R.drawable.air2;
			} else if (covalue <= 35) {
				return R.drawable.air3;
			} else if (covalue <= 60) {
				return R.drawable.air4;
			} else if (covalue <= 90) {
				return R.drawable.air5;
			} else {
				return R.drawable.airkong;
			}
		}

		return drawable;
	}
	public static Bitmap updateWeather(Context context,String climate) {
		Bitmap bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.qin);
		Time t = new Time();
		t.setToNow();
		int hour = t.hour;
		if (climate.contains("晴")) {
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.heiye);
//				bgChange.setImageBitmap(bitmapDrawable);
			} else {
//				Animation sunAnimation = AnimationUtils.loadAnimation(context,
//						R.anim.sun);
				// ivSun.startAnimation(sunAnimation);
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.qin);
//				bgChange.setImageBitmap(bitmapDrawable);
			}
		} else if (climate.contains("雷阵")) {
			// 更新当前雨天
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);
//				bgChange.setImageBitmap(bitmapDrawable);
			} else {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.c_leizhenyu);
//				bgChange.setImageBitmap(bitmapDrawable);
			}

		} else if (climate.contains("雨")) {
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);

			} else {
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.c_yu);
//				bgChange.setImageBitmap(bitmapDrawable);

			}

		} else if (climate.contains("雪")) {

			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);
//				bgChange.setImageBitmap(bitmapDrawable);

			} else {
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.c_xue);
//				bgChange.setImageBitmap(bitmapDrawable);
			}
		} else if (climate.contains("云")) {
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);

//				bgChange.setImageBitmap(bitmapDrawable);

			} else {
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.c_duoyun);
//				bgChange.setImageBitmap(bitmapDrawable);
			}
		} else if (climate.contains("阴")) {
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);
			} else {
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.c_yin);

//				bgChange.setImageBitmap(bitmapDrawable);
			}
		} else if (climate.contains("雾")) {
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);
			} else {
				bitmapDrawable = ImageUtils.readBitmap(context, R.mipmap.wu_first);
			}
		} else if (climate.contains("霾")) {
			if (hour >= 19 || hour <= 6) {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.heiye_yin);
//				.setImageBitmap(bitmapDrawable);
			} else {
				bitmapDrawable = ImageUtils.readBitmap(context,
						R.mipmap.mai_first);
			}
		}
		return  bitmapDrawable;
	}
	// 判断是否是晚上,0代表不是晚上,1代表晚上
	public static int getDateSx() {
		int isNight = 0;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 6 && hour < 8) {
		} else if (hour >= 8 && hour < 11) {
		} else if (hour >= 11 && hour < 13) {
		} else if (hour >= 13 && hour < 18) {
		} else {
			isNight = 1;
		}
		return isNight;
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.i("CommonUtil",">>>>>>>>>>>>>>>isConnected" + wifiNetworkInfo.isConnected());
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
