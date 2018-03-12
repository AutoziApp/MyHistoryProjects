package cn.com.mapuni.meshing.base.util;


import cn.com.mapuni.meshing.base.R;

/**
 * Created by Mai on 2016/12/26.
 * 污染级别计算工具�?
 */

@SuppressWarnings("LossyEncoding")
public class PollutionLevelCalUtil {
    /*
    * return
    * 1:0-50      �?   绿色
    * 2�?51-100   �?   黄色
    * 3�?101-150  轻度污染 橙色
    * 4�?151-200  中度污染  红色
    * 5�?201-300  重度污染   紫色
    * 6�?>300     严重污染  褐红�?
    * 以小时平均浓度做参�??
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


    //以日平均浓度做参�?
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

    public static int getLevelIcon(String type,double nd){
        int level = getHourLevel(type,nd);
        switch (level){
            case 1:
                return R.drawable.shape_recround_green1;
            case 2:
                return R.drawable.shape_recround_yello1;
            case 3:
                return R.drawable.shape_recround_orange1;
            case 4:
                return R.drawable.shape_recround_red1;
            case 5:
                return R.drawable.shape_recround_purple1;
            case 6:
                return R.drawable.shape_recround_maroon1;
        }
        return R.drawable.shape_recround_green1;
    }

    public static String getLevelDes(String type,double nd){
        int level = getHourLevel(type,nd);
        switch (level){
            case 1:
                return "��";
            case 2:
                return "��";
            case 3:
                return "�����Ⱦ";
            case 4:
                return "�ж���Ⱦ";
            case 5:
                return "�ض���Ⱦ";
            case 6:
                return "������Ⱦ";
        }
        return "��";
    }
}
