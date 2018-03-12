package com.mapuni.mobileenvironment.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.utils.DisplayUitl;
import com.mapuni.mobileenvironment.utils.timepicker.NumericWheelAdapter;
import com.mapuni.mobileenvironment.utils.timepicker.OnWheelScrollListener;
import com.mapuni.mobileenvironment.utils.timepicker.WheelView;
import com.mapuni.mobileenvironment.view.YutuLoading;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class ActivityBase extends AppCompatActivity implements View.OnClickListener{
    TextView title ;
    public TextView titleIcon;
    public ImageView titleIconB;
    public ImageView titleIconA;
    public ImageView leftIcon;
    private PopupWindow menuWindow;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView minite;
    public  View timeView;
    private String date = "";
    public String imageGuid;
    public final static int ISFROMSEND = 1;
    public final static int ISFROMWAIT = 2;
    public final static int ISFROMLIST = 3;
    public final static int ISWATER = 4;
    public final static int ISAIR = 5;
    public final static int ISPOLLUTION = 6;
    public static final String TASK_PATH = PathManager.SDCARD_RASK_DATA_PATH + "Attach/CJCZ/";
    public final int SELECT_SDKARD_FILE = 2;
    public final  int TAKE_PHOTOS = 1;
    YutuLoading yutuLoading;
    public final int FIRSTBTN=100;
    public final int WATERBTN=101;
    public final int AIRBTN=102;
    public final int SEARCHBTN=103;
    public final int LOADINGBTN=104;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void setTitle(String s ){
        title = (TextView) findViewById(R.id.titleView);
        title.setText(s);
    }
    public void setTitle(String s ,String ss){
        title = (TextView) findViewById(R.id.titleView);
        title.setText(s);
        titleIcon = (TextView) findViewById(R.id.rightIcon);
        titleIcon.setText(ss);
        titleIcon.setVisibility(View.VISIBLE);
        titleIcon.setOnClickListener(this);
    }
    public void setTitle(String s ,int RightIcon,int leftIcon){
        title = (TextView) findViewById(R.id.titleView);
        title.setText(s);
        if(RightIcon==1){
            titleIconB = (ImageView) findViewById(R.id.rightIconB);
            titleIconB.setVisibility(View.VISIBLE);
            titleIconB.setOnClickListener(this);
        }
        if(leftIcon==1){
            this.leftIcon = (ImageView) findViewById(R.id.leftIcon);
            this.leftIcon.setVisibility(View.VISIBLE);
            this.leftIcon.setOnClickListener(this);
        }
    }
    public void setTitle(String s ,int RightIconA,int RightIconB,int i){
        title = (TextView) findViewById(R.id.titleView);
        title.setText(s);
        if(RightIconB==1){
            titleIconB = (ImageView) findViewById(R.id.rightIconB);
            titleIconB.setVisibility(View.VISIBLE);
            titleIconB.setOnClickListener(this);
        }
        if(RightIconA==1){
            titleIconA = (ImageView) findViewById(R.id.rightIconA);
            titleIconA.setVisibility(View.VISIBLE);
            titleIconA.setOnClickListener(this);
        }
    }

    public Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FIRSTBTN:
                    if(yutuLoading.isShow())
                        yutuLoading.dismissDialog();
                    finish();
                    break;
                case SEARCHBTN:
                    if(yutuLoading.isShow())
                        yutuLoading.dismissDialog();
                    break;
                case LOADINGBTN:
                    if(yutuLoading.isShow())
                        yutuLoading.dismissDialog();
                    break;
                case WATERBTN:
                    if(yutuLoading.isShow())
                        yutuLoading.dismissDialog();
                    break;
                case AIRBTN:
                    if(yutuLoading.isShow())
                        yutuLoading.dismissDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {

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
    public PopupWindow showView(View view,int width,int height){
//        bindView(view,map);
        PopupWindow mPopWin = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWin.setHeight(DisplayUitl.dip2px(this,height));
        mPopWin.setWidth(DisplayUitl.dip2px(this,width));
//        mPopWin.setTouchable(true);
//        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
//        mPopWin.setBackgroundDrawable(getResources().getDrawable(R.drawable.airkong));
//        mPopWin.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        int[]  viewScreen = DisplayUitl.getViewScreen(view);
        DisplayUitl.getMobileResolution(this);
        mPopWin.showAsDropDown(view,
//                DisplayUitl.ScreenWidth/2-viewScreen[0]/2,DisplayUitl.ScreenHeight/2-viewScreen[1]/2);
                DisplayUitl.ScreenWidth/2-DisplayUitl.dip2px(this,width)/2,DisplayUitl.ScreenHeight/2-DisplayUitl.dip2px(this,width)/2);
        mPopWin.update();
       setWindowAlpha((float) 0.5);
        mPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        return mPopWin;
    }
    public void setWindowAlpha( float alpha){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=alpha;
        getWindow().setAttributes(lp);
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

    public View getDataPick(final int resouseId) {
        final int count = 0;
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        //在这里添加小时和分钟的初始化
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMimete = c.get(Calendar.MINUTE);
        timeView = LayoutInflater.from(this).inflate(R.layout.datapick, null);
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
        month.setCurrentItem(curMonth - 1);
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

    public void takePhoto() {// 拍照

        imageGuid = UUID.randomUUID().toString();
        Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(TASK_PATH);
        if (!file.exists())// 第一次拍照创建照片文件夹
            file.mkdirs();
//       配置照片存放路径
        photo_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(TASK_PATH + imageGuid + "." + "jpg")));
        photo_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        photo_intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, false);
        startActivityForResult(photo_intent, TAKE_PHOTOS);

    }

    // 选照
    public void takefigure(View view) {
        imageGuid = UUID.randomUUID().toString();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"), SELECT_SDKARD_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }



}
