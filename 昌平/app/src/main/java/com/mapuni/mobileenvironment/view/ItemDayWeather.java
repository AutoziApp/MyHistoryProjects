package com.mapuni.mobileenvironment.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.mobileenvironment.R;

/**
 * Created by Mai on 2016/10/26.
 */

public class ItemDayWeather extends LinearLayout{

    //今天，明天
    private TextView item_day_dateName;
    //日期
    private TextView item_day_date;
    //天气状态
    private ImageView item_day_weather;

    public ItemDayWeather(Context context) {
        super(context);
        initView();
    }

    public ItemDayWeather(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ItemDayWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public void initView(){
        View view = View.inflate(getContext(), R.layout.item_day_weather, null);
        item_day_dateName = (TextView) view.findViewById(R.id.item_day_dateName);
        item_day_date = (TextView) view.findViewById(R.id.item_day_date);
        item_day_weather = (ImageView) view.findViewById(R.id.item_day_weather);
        addView(view);
    }

    public void setDaydate(String daydate){
        item_day_dateName.setText(daydate);
    }

    public void setItemdayDateNum(String dateNum){
        item_day_date.setText(dateNum);
    }

    public void setImageView(int dayWeatherId){
        Drawable imageView = getContext().getResources().getDrawable(dayWeatherId);
        item_day_weather.setImageDrawable(imageView);
    }
}
