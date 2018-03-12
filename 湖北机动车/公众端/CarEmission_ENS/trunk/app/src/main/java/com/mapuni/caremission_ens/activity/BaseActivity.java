package com.mapuni.caremission_ens.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;

/**
 * Created by yawei on 2017/3/30.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView backView ;
    private LinearLayout rightLayout;
    private TextView titleView;
    private Handler handler;
    public void setTitle(String title,boolean leftIconIsShow,boolean rightIconIsShow){
        backView = (ImageView) findViewById(R.id.leftIcon);
        titleView = (TextView) findViewById(R.id.titleView);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        if(leftIconIsShow){
            backView.setOnClickListener(this);
            backView.setVisibility(View.VISIBLE);
        }else{
            backView.setVisibility(View.GONE);
        }
        if(rightIconIsShow){
            rightLayout.setVisibility(View.VISIBLE);
        }else{
            rightLayout.setVisibility(View.GONE);
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
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
        }
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
