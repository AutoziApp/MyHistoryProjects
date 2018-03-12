package com.mapuni.mobileenvironment.utils;

import com.mapuni.mobileenvironment.R;

/**
 * Created by Mai on 2016/12/26.
 * 污染级别计算工具类
 */

public class PollutionLevelCalUtil {
    /*
    * return
    * 1:0-50      优   绿色
    * 2：51-100   良   黄色
    * 3：101-150  轻度污染 橙色
    * 4：151-200  中度污染  红色
    * 5：201-300  重度污染   紫色
    * 6：>300     严重污染  褐红色
    * 以小时平均浓度做参考
    */
    public static int getHourLevel(String type, double nd) {
        switch (type) {
            case "aqi":
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
                break;
            case "so2":
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
                break;
            case "no2":
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
                break;
            case "pm10":
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
                break;
            case "co":
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
                break;
            case "o3":
                if (nd >= 800) {
                    return 6;
                } else if (nd >= 400) {
                    return 5;
                } else if (nd >= 300) {
                    return 4;
                } else if (nd >= 200) {
                    return 3;
                } else if (nd >= 160) {
                    return 2;
                } else if (nd >= 0) {
                    return 1;
                } else {

                }
                break;
            case "pm25":
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
                break;
        }
        return -1;
    }


    //以日平均浓度做参考
    public static int getDayLevel(String type, double nd) {
        switch (type) {
            case "aqi":
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
                break;
            case "so2":
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
                break;
            case "no2":
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
                break;
            case "pm10":
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
                break;
            case "co":
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
                break;
            case "o3":
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
                break;
            case "pm25":
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
                break;
        }
        return -1;
    }

    public static int getLevelIconForMap(String type,double nd){
        int level = getHourLevel(type,nd);
        switch (level){
            case 1:
                return R.mipmap.level_a;
            case 2:
                return R.mipmap.level_b;
            case 3:
                return R.mipmap.level_c;
            case 4:
                return R.mipmap.level_d;
            case 5:
                return R.mipmap.level_e;
            case 6:
                return R.mipmap.level_f;
        }
        return R.mipmap.level_a;
    }
}
