package com.yutu.car.utils;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by lenovo on 2017/4/13.
 */

public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context);
    }
    public MyDialog(Context context, int theme) {
        super(context, theme);
    }
    public static class Builder{
        private Context context;
        private String title;

    }



}
