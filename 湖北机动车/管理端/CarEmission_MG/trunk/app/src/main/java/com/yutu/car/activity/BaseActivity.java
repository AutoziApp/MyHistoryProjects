package com.yutu.car.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yutu.car.R;

/**
 * Created by yawei on 2017/3/30.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView backView , rightIcon;
    public TextView titleView;
    private Handler handler;
    public Spinner mTitleSpinner;

    public void setTitle(String title,boolean leftIconIsShow,boolean rightIconIsShow){
        backView = (ImageView) findViewById(R.id.leftIcon);
        titleView = (TextView) findViewById(R.id.titleView);
        rightIcon = (ImageView) findViewById(R.id.rightIconA);
        mTitleSpinner = (Spinner) findViewById(R.id.title_spinner);
        if(leftIconIsShow){
            backView.setOnClickListener(this);
            backView.setVisibility(View.VISIBLE);
        }else{
            backView.setVisibility(View.GONE);
        }
        if(rightIconIsShow){
            rightIcon.setVisibility(View.VISIBLE);
        }else{
            rightIcon.setVisibility(View.GONE);
        }
        titleView.setText(title);
    }

    public void showFailed(){

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.showFailed);
        layout.setOnClickListener(this);
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

    }
    public void requestAgain(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.showFailed);
        if(layout.getVisibility()==View.VISIBLE)
            layout.setVisibility(View.GONE);
    }
    public void delayedPost(Runnable runnable,int time){
        if(handler==null)
            handler = new Handler();
        handler.postDelayed(runnable, time);
    }
}
