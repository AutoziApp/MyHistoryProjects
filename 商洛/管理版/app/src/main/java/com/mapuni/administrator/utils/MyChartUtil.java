package com.mapuni.administrator.utils;

import android.graphics.Color;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.utils
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/22 14:14
 * @change
 * @chang time
 * @class describe
 */

public class MyChartUtil{
    public static final int task_color1 = Color.parseColor("#00FFFF");
    public static final int task_color2 = Color.parseColor("#54FF9F");
    public static final int task_color3 = Color.parseColor("#00CD66");
    public static final int task_color4 = Color.parseColor("#9ACD32");
    public static final int task_color5 = Color.parseColor("#CD5555");
    public static final int task_color6 = Color.parseColor("#EE3B3B");
    public static final int task_color7 = Color.parseColor("#FF34B3");
    public static final int task_color8 = Color.parseColor("#0000FF");
    public static final int[] COLORS = new int[]{
            task_color1,task_color2,task_color3,task_color4,
            task_color5,task_color6,task_color7,task_color8};

    public static final int mypickColor(int position) {
        return COLORS[position%8];
    }






}
