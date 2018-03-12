package com.mapuni.core.widget.timepicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.core.R;
import com.mapuni.core.utils.DateUtils;
import com.mapuni.core.utils.DisplayUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TimePickerControl implements View.OnClickListener{
    private Context context;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView minite;
    public View timeView;
    private PopupWindow menuWindow;
    private String date = "";
    private TextView startTime,endTime;
    private int startId = 0;
    private int endId = 0;
    public TimePickerControl(Context context, TextView startTime, TextView endTime){
        this.context = context;
        this.startTime = startTime;
        this.endTime = endTime;
        startId = this.startTime.getId();
        endId = this.endTime.getId();
        this.startTime.setOnClickListener(this);
        this.endTime.setOnClickListener(this);
        initCalendar();
    }
    public TimePickerControl(Context context, TextView endTime){
        this.context = context;
        this.endTime = endTime;
        endId = this.endTime.getId();
        this.endTime.setOnClickListener(this);
        initCalendar();
    }

    public void showPopwindow(View view) {

        menuWindow = new PopupWindow(view, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        menuWindow.setFocusable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        setWindowAlpha((float) 0.5);
        menuWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha((float) 1);
                menuWindow = null;
            }
        });
    }
    //根据调用时刻的不同达到不同的时间精度
    public void dismissPopwindow(){
        setWindowAlpha(1f);
        if(menuWindow!=null&&menuWindow.isShowing())
            menuWindow.dismiss();
    }
    public PopupWindow showView(View view, int width, int height){
//        bindView(view,map);
        PopupWindow mPopWin = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWin.setHeight(DisplayUtil.dip2px(context,height));
        mPopWin.setWidth(DisplayUtil.dip2px(context,width));
//        mPopWin.setTouchable(true);
//        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
//        mPopWin.setBackgroundDrawable(getResources().getDrawable(R.drawable.airkong));
//        mPopWin.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        int[]  viewScreen = DisplayUtil.getViewScreen(view);
//        DisplayUtil.getMobileResolution((Activity) context);
        mPopWin.showAsDropDown(view,
//                DisplayUitl.ScreenWidth/2-viewScreen[0]/2,DisplayUitl.ScreenHeight/2-viewScreen[1]/2);
                DisplayUtil.getScreenWidth(context)/2- DisplayUtil.dip2px(context,width)/2, DisplayUtil.getScreenHeight(context)/2- DisplayUtil.dip2px(context,width)/2);
        mPopWin.update();
        setWindowAlpha((float) 0.5);
        mPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity)context).getWindow().setAttributes(lp);
            }
        });
        return mPopWin;
    }
    public void setWindowAlpha( float alpha){
        WindowManager.LayoutParams lp=((Activity)context).getWindow().getAttributes();
        lp.alpha=alpha;
        ((Activity)context).getWindow().setAttributes(lp);
    }
    public String getDate(View view ){
        String str = "";
        int dayy = day.getCurrentItem() + 1;
        String dayTime = dayy + "";
        if (dayTime.length() == 1) {
            dayTime = "0" + dayTime;
        }
        if ((month.getCurrentItem() + 1) < 10) {
            str = (year.getCurrentItem() + 1950) + "-" + "0" + (month.getCurrentItem() + 1) + "-" + dayTime;
        } else {
            str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1) + "-" + dayTime;
        }
        timeView.findViewById(R.id.date).setVisibility(View.GONE);
        timeView.findViewById(R.id.time).setVisibility(View.VISIBLE);
        return str;
    }
    public String getDateForDay(View view ){
        String str = "";
        int dayy = day.getCurrentItem() + 1;
        String dayTime = dayy + "";
        if (dayTime.length() == 1) {
            dayTime = "0" + dayTime;
        }
        if ((month.getCurrentItem() + 1) < 10) {
            str = (year.getCurrentItem() + 1950) + "-" + "0" + (month.getCurrentItem() + 1) + "-" + dayTime;
        } else {
            str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1) + "-" + dayTime;
        }
        str = str;
        dismissPopwindow();
//        timeView.findViewById(R.id.date).setVisibility(View.GONE);
//        timeView.findViewById(R.id.time).setVisibility(View.GONE);
        return str;
    }
    public String getTime(){
        String format ="%02d";
        int hour1 = hour.getCurrentItem();
        int minite1=minite.getCurrentItem();
        String sHour = format != null ? String.format(format, hour1) : Integer.toString(hour1);
        String sMinite1 = format != null ? String.format(format, minite1) : Integer.toString(minite1);
        menuWindow.dismiss();
        return  sHour+":"+sMinite1;
    }

    public View getDataPick() {
        final int count = 0;
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        //在这里添加小时和分钟的初始化
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMimete = c.get(Calendar.MINUTE);
        timeView = LayoutInflater.from(context).inflate(R.layout.datapick, null);
        year = (WheelView) timeView.findViewById(R.id.year);
        year.setAdapter(new NumericWheelAdapter(1950, curYear));
        year.setLabel("年");
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month = (WheelView) timeView.findViewById(R.id.month);
        month.setAdapter(new NumericWheelAdapter(1, 12));
        month.setLabel("月");
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) timeView.findViewById(R.id.day);
        initDay(curYear, curMonth - 1);
        day.setLabel("日");
        day.setCyclic(true);
        curYear = Integer.parseInt(date.substring(0, 4));
        curMonth = Integer.parseInt(date.substring(5, 7));
        curDate = Integer.parseInt(date.substring(8));
        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth);
//        day.setCurrentItem(curDate - 1);
        day.setCurrentItem(curDate);

        hour = (WheelView) timeView.findViewById(R.id.hour);
        hour.setAdapter(new NumericWheelAdapter(00, 23,"%02d"));
        hour.setLabel("时");
        hour.setCyclic(true);
        hour.setCurrentItem(curHour);

        minite = (WheelView) timeView.findViewById(R.id.minite);
//        new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d")
        minite.setAdapter(new NumericWheelAdapter(00, 59,"%02d"));
        minite.setLabel("分");
        minite.setCyclic(true);
//        minite.setCurrentItem(curMimete - 1);
        minite.setCurrentItem(curMimete);

        Button bt = (Button) timeView.findViewById(R.id.set);
        bt.setOnClickListener(this);
        Button cancel = (Button) timeView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return timeView;
    }


    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            int n_year = year.getCurrentItem() + 1950;//
            int n_month = month.getCurrentItem() + 1;//
            initDay(n_year, n_month);
        }
    };
    public void initCalendar() {
        Date datee = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datee);
        calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
        datee = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = formatter.format(datee);
        if(startTime != null) {
            startTime.setText(DateUtils.getYesterdayDate());
        }
        endTime.setText(DateUtils.getDate());
    }
    public void initTime(){
        startTime.setText(DateUtils.getYesterdayDate());
        endTime.setText(DateUtils.getDate());
    }

    public void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
    }

    public int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }
    final int StartTimePicker = 0;
    final int EndTimePicker = 1;
    int currentTimePicker = 0;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==startId){
            currentTimePicker = StartTimePicker;
            showPopwindow(getDataPick());
        }else if(id==endId){
            currentTimePicker = EndTimePicker;
            showPopwindow(getDataPick());
        }else if(id == R.id.set){
            if(currentTimePicker==StartTimePicker){
                if(timeView.findViewById(R.id.date).getVisibility()== View.VISIBLE){
                    startTime.setText(getDate(view));
                }else{
                    startTime.append(" "+getTime());
                    dismissPopwindow();
                }
            }else if(currentTimePicker==EndTimePicker){
                if(timeView.findViewById(R.id.date).getVisibility()== View.VISIBLE){
                    endTime.setText(getDate(view));
                }else{
                    endTime.append(" "+getTime());
                    dismissPopwindow();
                }
            }
        }

    }

}
