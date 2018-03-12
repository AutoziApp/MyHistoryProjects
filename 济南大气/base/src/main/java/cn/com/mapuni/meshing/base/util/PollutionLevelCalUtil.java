package cn.com.mapuni.meshing.base.util;


import cn.com.mapuni.meshing.base.R;

/**
 * Created by Mai on 2016/12/26.
 * Ê±°ÊüìÁ∫ßÂà´ËÆ°ÁÆóÂ∑•ÂÖ∑Á±?
 */

@SuppressWarnings("LossyEncoding")
public class PollutionLevelCalUtil {
    /*
    * return
    * 1:0-50      ‰º?   ÁªøËâ≤
    * 2Ôº?51-100   Ëâ?   ÈªÑËâ≤
    * 3Ôº?101-150  ËΩªÂ∫¶Ê±°Êüì Ê©ôËâ≤
    * 4Ôº?151-200  ‰∏≠Â∫¶Ê±°Êüì  Á∫¢Ëâ≤
    * 5Ôº?201-300  ÈáçÂ∫¶Ê±°Êüì   Á¥´Ëâ≤
    * 6Ôº?>300     ‰∏•ÈáçÊ±°Êüì  Ë§êÁ∫¢Ëâ?
    * ‰ª•Â∞èÊó∂Âπ≥ÂùáÊµìÂ∫¶ÂÅöÂèÇË??
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


    //‰ª•Êó•Âπ≥ÂùáÊµìÂ∫¶ÂÅöÂèÇËÄ?
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
                return "”≈";
            case 2:
                return "¡º";
            case 3:
                return "«·∂»Œ€»æ";
            case 4:
                return "÷–∂»Œ€»æ";
            case 5:
                return "÷ÿ∂»Œ€»æ";
            case 6:
                return "—œ÷ÿŒ€»æ";
        }
        return "”≈";
    }
}
