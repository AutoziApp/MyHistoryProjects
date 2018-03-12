package com.mapuni.car.mvp.view;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mapuni.car.R;


public class YutuLoading extends LinearLayout {
    private final Context mContext;
    private Dialog dialog;
    private Boolean IsCancelable = false;

    public YutuLoading(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public YutuLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        LayoutInflater.from(mContext).inflate(
                R.layout.auto_dialog, this);
//       ProgressBar progressBar= (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setIndeterminateTintList( ColorStateList.valueOf(Color.parseColor("#01aa92")));
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(IsCancelable);
            dialog.setContentView(this);
        }
        if (!dialog.isShowing())
            dialog.show();
    }

    /**
     * 消失对话框
     */
    public void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }


}