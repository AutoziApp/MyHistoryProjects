package com.mapuni.core.utils;

import com.mapuni.core.R;

/**
 * Created by YZP on 2017/12/7.
 */
public class CarColorUtils {
    public static int getIcon(String type){
        switch (type){
            case "蓝牌":
                return R.drawable.car_bule_im;
            case "白牌":
                return R.drawable.car_white_im;
            case "黄牌":
                return R.drawable.car_yellow_im;
            case "黑牌":
                return R.drawable.car_black_im;
            default:
                return R.drawable.car_bule_im;
        }

    }
}
