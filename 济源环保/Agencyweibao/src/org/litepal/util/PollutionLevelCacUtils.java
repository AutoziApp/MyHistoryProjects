package org.litepal.util;

import com.jy.environment.R;

import android.graphics.Color;

//计算污染级别的工具类
public class PollutionLevelCacUtils {

	/*
	 * return 1:0-50 伿 绿色 #39B711 2＿51-100 艿 黄色 #FFD112 3＿101-150 轻度污染 橙色
	 * #FF7E00 4＿151-200 中度污染 红色 #FF0000 5＿201-300 重度污染 紫色 #99004C 6＿>300 严重污染
	 * 褐红艿 #7E0000 以小时平均浓度做参迿
	 */
	public static int getHourLevel(String type, double nd) {

		if ("aqi".equals(type)) {
			if (nd >= 300) {
				return 6;
			} else if (nd >= 200) {
				return 5;
			} else if (nd >= 150) {
				return 4;
			} else if (nd >= 100) {
				return 3;
			} else if (nd >= 50) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("so2".equals(type)) {
			if (nd >= 800) {
				return 5;
			} else if (nd >= 650) {
				return 4;
			} else if (nd >= 500) {
				return 3;
			} else if (nd >= 150) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("no2".equals(type)) {
			if (nd >= 2340) {
				return 6;
			} else if (nd >= 1200) {
				return 5;
			} else if (nd >= 700) {
				return 4;
			} else if (nd >= 200) {
				return 3;
			} else if (nd >= 100) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("pm10".equals(type)) {
			if (nd >= 420) {
				return 6;
			} else if (nd >= 350) {
				return 5;
			} else if (nd >= 250) {
				return 4;
			} else if (nd >= 150) {
				return 3;
			} else if (nd >= 50) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("co".equals(type)) {
			if (nd >= 90) {
				return 6;
			} else if (nd >= 60) {
				return 5;
			} else if (nd >= 35) {
				return 4;
			} else if (nd >= 10) {
				return 3;
			} else if (nd >= 5) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("o3".equals(type)) {
			if (nd >= 800) {
				return 6;
			} else if (nd >= 265) {
				return 5;
			} else if (nd >= 215) {
				return 4;
			} else if (nd >= 160) {
				return 3;
			} else if (nd >= 100) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("pm25".equals(type)) {
			if (nd >= 250) {
				return 6;
			} else if (nd >= 150) {
				return 5;
			} else if (nd >= 115) {
				return 4;
			} else if (nd >= 75) {
				return 3;
			} else if (nd >= 35) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		}
		return -1;
	}

	// 以日平均浓度做参耿
	public static int getDayLevel(String type, double nd) {
		if ("aqi".equals(type)) {
			if (nd >= 300) {
				return 6;
			} else if (nd >= 200) {
				return 5;
			} else if (nd >= 150) {
				return 4;
			} else if (nd >= 100) {
				return 3;
			} else if (nd >= 50) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("so2".equals(type)) {
			if (nd >= 1600) {
				return 6;
			} else if (nd >= 800) {
				return 5;
			} else if (nd >= 475) {
				return 4;
			} else if (nd >= 150) {
				return 3;
			} else if (nd >= 50) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("no2".equals(type)) {
			if (nd >= 565) {
				return 6;
			} else if (nd >= 280) {
				return 5;
			} else if (nd >= 180) {
				return 4;
			} else if (nd >= 80) {
				return 3;
			} else if (nd >= 40) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("pm10".equals(type)) {
			if (nd >= 420) {
				return 6;
			} else if (nd >= 350) {
				return 5;
			} else if (nd >= 250) {
				return 4;
			} else if (nd >= 150) {
				return 3;
			} else if (nd >= 50) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("co".equals(type)) {
			if (nd >= 36) {
				return 6;
			} else if (nd >= 24) {
				return 5;
			} else if (nd >= 14) {
				return 4;
			} else if (nd >= 4) {
				return 3;
			} else if (nd >= 2) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("o3".equals(type)) {
			if (nd >= 800) {
				return 6;
			} else if (nd >= 265) {
				return 5;
			} else if (nd >= 215) {
				return 4;
			} else if (nd >= 160) {
				return 3;
			} else if (nd >= 100) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		} else if ("pm25".equals(type)) {
			if (nd >= 250) {
				return 6;
			} else if (nd >= 150) {
				return 5;
			} else if (nd >= 115) {
				return 4;
			} else if (nd >= 75) {
				return 3;
			} else if (nd >= 35) {
				return 2;
			} else if (nd >= 0) {
				return 1;
			} else {

			}
		}
		return -1;
	}

	// 得到小时数据污染等级对应的色值
	public static int getHourLevelColor(String type, double nd) {
		int level = getHourLevel(type, nd);
		switch (level) {
		case 1:
			return Color.parseColor("#39B711");
		case 2:
			return Color.parseColor("#FFD112");
		case 3:
			return Color.parseColor("#FF7E00");
		case 4:
			return Color.parseColor("#FF0000");
		case 5:
			return Color.parseColor("#99004C");
		case 6:
			return Color.parseColor("#7E0000");
		}
		return Color.GRAY;
	}
	
	// 得到小时数据污染等级对应的图片资源id
		public static int getHourLevelicon(String type, double nd) {
			int level = getHourLevel(type, nd);
			switch (level) {
			case 1:
				return  R.drawable.aqi_level_1;
			case 2:
				return  R.drawable.aqi_level_2;
			case 3:
				return  R.drawable.aqi_level_3;
			case 4:
				return  R.drawable.aqi_level_4;
			case 5:
				return  R.drawable.aqi_level_5;
			case 6:
				return  R.drawable.aqi_level_6;
			}
			return  R.drawable.aqi_level_1;
		}
		
		public static int getLeveliconYY(int level) {
			switch (level) {
			case 1:
				return  R.drawable.aqi_level_1;
			case 2:
				return  R.drawable.aqi_level_2;
			case 3:
				return  R.drawable.aqi_level_3;
			case 4:
				return  R.drawable.aqi_level_4;
			case 5:
				return  R.drawable.aqi_level_5;
			case 6:
				return  R.drawable.aqi_level_6;
			}
			return  R.drawable.aqi_level_1;
		}
	
}
