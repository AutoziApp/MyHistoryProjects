package com.mapuni.administrator.utils;

import android.text.TextUtils;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.utils
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/1 10:54
 * @change
 * @chang time
 * @class describe
 */

public class TxtUtil {
    
    public static String isEmpty(String value){
        if (TextUtils.isEmpty(value)){
            return "暂无数据";
        }
        return value;
    }
}
