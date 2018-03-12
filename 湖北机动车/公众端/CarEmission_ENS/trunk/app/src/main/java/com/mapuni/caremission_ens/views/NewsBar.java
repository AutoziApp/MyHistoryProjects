package com.mapuni.caremission_ens.views;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;

/**
 * Created by yawei on 2017/4/5.
 */

public class NewsBar extends RelativeLayout {
    private TextView newCount;
    private int msgCount;
    public NewsBar(Context context) {
        this(context,null);
    }

    public NewsBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NewsBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.news_layout,this,true);
        newCount = (TextView) rl.findViewById(R.id.count);
//        addView(rl);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMessageCount(int count) {
        msgCount = count;
        if (count == 0) {
            newCount.setVisibility(View.GONE);
        } else {
            newCount.setVisibility(View.VISIBLE);
            if (count < 100) {
                newCount.setText(count + "");
            } else {
                newCount.setText("99+");
            }
        }
        requestLayout();
        invalidate();
    }

    public void addMsg(int i) {
        setMessageCount(i);
    }

}
