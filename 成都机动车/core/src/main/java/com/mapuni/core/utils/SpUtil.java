package com.mapuni.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by hpw on 16/10/28.
 */

public class SpUtil {
    static SharedPreferences prefs;
    private static final boolean DEFAULT_NO_IMAGE = false;
    private static final boolean DEFAULT_AUTO_SAVE = true;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getThemeIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("ThemeIndex", 9);
    }

    public static void setThemeIndex(Context context, int index) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt("ThemeIndex", index).apply();
    }

    public static boolean getNightModel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("pNightMode", false);
    }

    public static void setNightModel(Context context, boolean nightModel) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("pNightMode", nightModel).apply();
    }

    public static String RemmemberType = "REMEMBER_TYPE";
    public static String AutoType = "AUTO_TYPE";
    public static String MM = "MM";
    public static String UserName = "UserName";
    public static String BaseIp = "BaseIp";
    public static void putString(Context context,String name,String s) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                name, 0).edit();
        editor.putString(name, s);
        editor.commit();
    }
    public static void removeString(Context context,String name) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                name, 0).edit();
        editor.remove(name);
        editor.commit();
    }
    public static String getString(Context context,String name){
        SharedPreferences _Preferences = context.getSharedPreferences(
                name, 0);
        String UpdateTime =_Preferences.getString(name,"");
        return UpdateTime;
    }
    public static void putBoolean(Context context,String name,Boolean bool) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                name, 0).edit();
        editor.putBoolean(name,bool);
        editor.commit();
    }
    public static boolean getBoolean(Context context,String name){
        SharedPreferences _Preferences = context.getSharedPreferences(
                name, 0);
        boolean bool =_Preferences.getBoolean(name,false);
        return bool;
    }


}

