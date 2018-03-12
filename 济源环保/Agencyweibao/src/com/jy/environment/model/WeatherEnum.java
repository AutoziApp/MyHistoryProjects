package com.jy.environment.model;

public class WeatherEnum {
	
	/*----------------资源参考-------------------
	 * mWidgetWeatherIcon.put("暴雪", R.drawable.w17);
	mWidgetWeatherIcon.put("暴雨", R.drawable.w10);
	mWidgetWeatherIcon.put("大暴雨", R.drawable.w10);
	mWidgetWeatherIcon.put("大雪", R.drawable.w16);
	mWidgetWeatherIcon.put("大雨", R.drawable.w9);

	mWidgetWeatherIcon.put("多云", R.drawable.w1);
	mWidgetWeatherIcon.put("雷阵雨", R.drawable.w4);
	mWidgetWeatherIcon.put("雷阵雨冰雹", R.drawable.w19);
	mWidgetWeatherIcon.put("晴", R.drawable.w0);
	mWidgetWeatherIcon.put("沙尘暴", R.drawable.w20);

	mWidgetWeatherIcon.put("特大暴雨", R.drawable.w10);
	mWidgetWeatherIcon.put("雾", R.drawable.w18);
	mWidgetWeatherIcon.put("小雪", R.drawable.w14);
	mWidgetWeatherIcon.put("小雨", R.drawable.w7);
	mWidgetWeatherIcon.put("阴", R.drawable.w2);

	mWidgetWeatherIcon.put("雨夹雪", R.drawable.w6);
	mWidgetWeatherIcon.put("阵雪", R.drawable.w13);
	mWidgetWeatherIcon.put("阵雨", R.drawable.w3);
	mWidgetWeatherIcon.put("中雪", R.drawable.w15);
	mWidgetWeatherIcon.put("中雨", R.drawable.w8);*/
	
	public static final int SUN_SHINE 			= 1;//晴
	public static final int CLOUDY 				= 2;//多云
	public static final int OVERCAST 			= 3;//阴
	
	public static final int SMALL_RAIN 			= 4;//小雨 FLURRY SPIT SPRINKLE
	public static final int MIDDLE_RAIN 		= 5;//中雨
	public static final int BIG_RAIN 			= 6;//大雨 DRENCHER DING-ON DOWNFALL SOAK SPATE
	public static final int RAIN_STORM 			= 7;//暴雨
	public static final int BIG_RAIN_STORM 		= 8;//大暴雨
	public static final int HUGE_RAIN_STORM 	= 9;//特大暴雨
	public static final int SHOWERS 			= 10;//阵雨
	public static final int T_SHOWERS 			= 11;//雷阵雨
	
	public static final int LIGHT_SNOW 			= 12;//小雪
	public static final int MIDDLE_SNOW 		= 13;//中雪
	public static final int HEAVY_SNOW 			= 14;//大雪
	public static final int SNOW_STROM 			= 15;//暴雪
	public static final int RAIN_AND_SNOW 		= 16;//雨加雪
	public static final int SNOW_SHOWERS 		= 17;//阵雪
	
	public static final int FOG 				= 18;//雾
	public static final int HAZE 				= 19;//霾
	public static final int FROST 				= 20;//霜
	public static final int HAIL 				= 21;//冰雹
	public static final int SAND_STORM 			= 22;//沙尘暴
	public static final int FLY_ASH 			= 23;//浮尘
	public static final int SAND_BLOWING 		= 24;//扬沙
	public static final int T_SAND_STORM 		= 25;//强沙尘暴
	//
	//
	public static final int OTHER				= 26;	/**除了以上类别之外的*/
}
