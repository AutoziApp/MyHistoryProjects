package com.mapuni.core.net.loadingview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapuni.core.R;
import com.mapuni.core.utils.DisplayUtil;

/**
 * Created by lybin on 2017/8/25.
 */

public class LoadingView {
    private View view;
    private Context context;
    private PopupWindow mPopWindow;
    private String msg;
    public static LoadingView mLoadingView;

    public LoadingView(Context context){
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showAtView(View parent){
//        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int size = context.getResources().getDimensionPixelSize(R.dimen.avatarSize);
//                outline.setOval(0, 1, size, size);
//            }
//        };
        if(view==null){
            creatView(context);
        }

        if(mPopWindow==null){
            mPopWindow = new PopupWindow(view,
                    DisplayUtil.dip2px(context,300),
                    DisplayUtil.dip2px(context,90), true);
            mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopWindow.setOutsideTouchable(false);
            mPopWindow.setElevation(60f);
            view.setElevation(60f);
//            view.setTranslationZ(60f);
            mPopWindow.setFocusable(false);
        }
        setWindowBackgroundAlpha(context,0.6f);
        mPopWindow.showAtLocation(parent,Gravity.CENTER,0,0);
    }

    public void dismiss(){
        if(mPopWindow!=null){
            msg="";
            mPopWindow.dismiss();
        }
        setWindowBackgroundAlpha(context,1f);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void creatView(Context context){
       view  = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        int margin = DisplayUtil.dip2px(context,20);
        params.setMargins(margin,0,margin,0);
        view.setLayoutParams(params);
        view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.loading_view_bg));
        ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);
        ((LinearLayout)view).setGravity(Gravity.CENTER_VERTICAL);


        ProgressBar progress = new ProgressBar(context);
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context,40),
                DisplayUtil.dip2px(context,40));
        progress.setIndeterminateTintList( ColorStateList.valueOf(Color.parseColor("#01aa92")));
        int progressMargin = DisplayUtil.dip2px(context,30);
        progressParams.setMargins(progressMargin,0,0,0);
        ((LinearLayout) view).addView(progress,progressParams);


        TextView tv = new TextView(context);
        tv.setTextColor(Color.parseColor("#8d8d8d"));
        tv.setTextSize(14);
        if(msg==null||msg.equals("")){
            tv.setText("正在请求数据...");
        }else{
            tv.setText(msg);
        }
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int tvMargin = DisplayUtil.dip2px(context,40);
        tvParams.setMargins(tvMargin,0,0,0);
        ((LinearLayout) view).addView(tv,tvParams);

    }
    public LoadingView setMsg(String s){
        msg = s;
        return mLoadingView;
    }
    public static LoadingView getInstance(Context context){
        if(mLoadingView==null){
            mLoadingView = new LoadingView(context);
        }
        return mLoadingView;
    }
    private void setWindowBackgroundAlpha(Context context,float alpha) {
        Window window = ((Activity)context).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

}
