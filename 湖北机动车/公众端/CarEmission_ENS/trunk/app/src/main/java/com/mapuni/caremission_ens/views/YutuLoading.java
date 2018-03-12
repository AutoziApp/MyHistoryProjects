package com.mapuni.caremission_ens.views;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.caremission_ens.R;


public class YutuLoading extends LinearLayout  {

    private final Context mContext;
    private TextView textView;
    private ImageView faildView;
    private AnimationDrawable ad;
    private CircleLoadingView circleView;
    private String loadingMsg = "加载中...", failMsg = "请求失败，暂无数据";
    private Dialog dialog;
    private Dialog failDialog;
    // private int textColor = Color.rgb(25, 108, 200);
    private int textColor = Color.BLACK;
    private LinearLayout layout;
    private Boolean IsCancelable=false;

    private boolean flag = true;

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
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    break;
            }
        }
    };

    private void init() {
        LayoutInflater.from(mContext).inflate(
                R.layout.base_controls_yutuloading, this);
        circleView = (CircleLoadingView) findViewById(R.id.circleView);
        circleView.start();
//        watch_dialog_layout = (LinearLayout) findViewById(R.id.yutu_loadling_out);
        textView = (TextView) findViewById(R.id.loadMessage);
        faildView = (ImageView) findViewById(R.id.faildView);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        ad = (AnimationDrawable) imageView.getBackground();
        textView.setText(loadingMsg);
//        textView.setTextColor(textColor);
//        watch_dialog_layout.setBackgroundResource(R.drawable.base_yutuloading_bg);
    }

    /**
     * 设置加载失败
     */
    public void showFailed() {
//        if (dialog.isShowing())
//            dialog.dismiss();

        if (dialog == null) {
//            watch_dialog_layout.setBackgroundResource(R.drawable.base_yutuloading_bg);
//            this.removeAllViews();
            dialog = new Dialog(mContext, R.style.base_loadingDialog);
            dialog.setCancelable(IsCancelable);
            dialog.setContentView(this);
            if(mContext instanceof DialogInterface.OnKeyListener){
                dialog.setOnKeyListener((DialogInterface.OnKeyListener)mContext);
            }
        }
        textView.setText(failMsg);
        circleView.setVisibility(GONE);
        faildView.setVisibility(VISIBLE);
        try {
            dialog.show();
        }catch (Exception e){

        }

    }
    /**
     * 设置对话框是否可以返回取消
     * @param IsCancelable
     */
    public void setCancelable(Boolean IsCancelable){
        this.IsCancelable=IsCancelable;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(mContext,"down",Toast.LENGTH_SHORT).show();
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(mContext,"back",Toast.LENGTH_SHORT).show();
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 显示对话框
     */
    public void showDialog() {
        if (dialog == null) {
//            watch_dialog_layout.setBackgroundResource(R.drawable.base_yutuloading_bg);
            dialog = new Dialog(mContext, R.style.base_loadingDialog);
            dialog.setCancelable(IsCancelable);
            dialog.setContentView(this);
            if(mContext instanceof DialogInterface.OnKeyListener){
                dialog.setOnKeyListener((DialogInterface.OnKeyListener)mContext);
            }
        }
        textView.setText(loadingMsg);
        circleView.setVisibility(VISIBLE);
        faildView.setVisibility(GONE);
        if (!dialog.isShowing())
            dialog.show();

    }
    public boolean isShow(){
        return dialog.isShowing();
    }


    /**
     * 消失对话框
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            if (flag) {
//                ad.stop();
//                ad.start();
            }
        }
        super.onWindowFocusChanged(hasWindowFocus);
    }
    /**
     * 设置加载时和加载失败时提示消息
     *
     * @param loadingMsg
     *            加载时提示消息
     * @param failMsg
     *            加载失败时提示消息
     */
    public void setLoadMsg(String loadingMsg, String failMsg) {
        this.loadingMsg = loadingMsg;
        this.failMsg = failMsg;
        textView.setTextColor(textColor);
        textView.setText(this.loadingMsg);
    }

    public TextView getShowTextView(){
        return textView;
    }

    /**
     * 设置加载时和加载失败时提示消息以及显示字体颜色
     *
     * @param loadingMsg
     *            加载时提示消息
     * @param failMsg
     *            加载失败时提示消息
     * @param color
     *            设置显示字体颜色
     */
    public void setLoadMsg(String loadingMsg, String failMsg, int color) {
        this.loadingMsg = loadingMsg;
        this.failMsg = failMsg;
        this.textColor = color;
        textView.setTextColor(textColor);
        textView.setText(this.loadingMsg);
    }
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (GONE == visibility) {
            ad.stop();
        }
    }


    /**
     * 加载失败时重新开始加载
     */
//    public void restart() {
//        if (!flag) {
//            imageView.setBackgroundResource(R.drawable.base_loadinganim);
//            ad = (AnimationDrawable) imageView.getBackground();
//            textView.setTextColor(textColor);
//            textView.setText(loadingMsg);
//            flag = true;
//            onWindowFocusChanged(true);
//        }
//    }


}
