package com.mapuni.caremission_ens.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mai on 2017/2/6.
 */

public class SharepreferenceUtil {

    public static void setUpdateTime(Context context, String time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "UpdateTime", 0).edit();
        editor.putString("UpdateTime", time);
        editor.commit();
    }
    public static String getUpdateTime(Context context){
        SharedPreferences _Preferences = context.getSharedPreferences(
                "UpdateTime", 0);
            String UpdateTime =_Preferences.getString("UpdateTime","");
            return UpdateTime;
    }
}
