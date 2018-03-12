package com.jy.environment.map;

/**
 * @author shang
 * 记录当前地图显示的环境信息类别的变量，目前包括：
 * AQI,PM2.5,PM10,SO2,NO2,CO,O3
 * 天气，雾，霾，气温
 * 分享图片
 */
public enum EnvTypeOnMapEnum {

	/**
	 * 标识什么也不显示
	 */
	NULL,
	/**
	 * 标识显示的是AQI点数据
	 */
	AQI,
	/**
	 * 标识显示的是PM2.5的点数据
	 */
	PM25,
	/**
	 * 标识显示的是PM10的点数据
	 */
	PM10,
	/**
	 * 标识显示的是SO2的点数据
	 */
	SO2,
	/**
	 * 标识显示的是NO2的点数据
	 */
	NO2,
	/**
	 * 标识显示的是co的点数据
	 */
	CO,
	/**
	 * 标识显示的是O3的点数据
	 */
	O3,
	/**
	 * 标识显示天气点数据
	 */
	TIANQI,
	/**
	 * 标识显示雾分布图
	 */
	WU,
	/**
	 * 标识显示霾分布图
	 */
	MAI,
	/**
	 * 标识显示气温分布图
	 */
	QIWEN,
	/**
	 * 标识显示交通实况
	 */
	TRAFFIC,
	/**
	 * 标识显示分享图片的分布图
	 */
	SHARE_PICTURE,
	/**
	 * 地表水
	 */
	SURFACE_WATER,
	/**
	 * 污染源
	 */
	SOURCE_OF_POLLUTION;
}
