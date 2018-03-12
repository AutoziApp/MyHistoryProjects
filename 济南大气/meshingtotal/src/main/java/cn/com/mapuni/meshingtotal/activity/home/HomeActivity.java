package cn.com.mapuni.meshingtotal.activity.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.PollutionLevelCalUtil;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.adapter.StationListAdapter;
import cn.com.mapuni.meshingtotal.model.PollutionInfo;
import cn.com.mapuni.meshingtotal.model.WeatherInfo;
import cn.com.mapuni.meshingtotal.view.ListViewForScrollView;

public class HomeActivity extends AppCompatActivity {

    ImageView ivBack;//背景图片
    ListViewForScrollView listview;//站点list
    List<PollutionInfo.MsgBean.StationInfoBean> list=new ArrayList<>();
    StationListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新布局
    TextView tvAqi;//头部aqi
    TextView tvTiShi;//头部提示语
    TextView tvAqiLevel;//头部aqi污染级别
    TextView tvTemp;//天气 温度
    TextView tvWeather;//天气 天气情况
    ImageView ivWeather;//天气 天气图标
    TextView tvDate;//天气  日期
    TextView tvTempRange;//天气 温度范围
    TextView tvWind;//天气 风向风级
    TextView tvShidu;//天气 湿度
    TextView tvPM25Value;
    TextView tvPM10Value;
    TextView tvO3Value;
    TextView tvNO2Value;
    TextView tvSO2Value;
    TextView tvCOValue;
    boolean weatherIsRefreshed;
    boolean pollutionIsRefreshed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去除标题栏
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initData() {
         weatherIsRefreshed=false;
         pollutionIsRefreshed=false;
        getWeatherInfo();
        getPollutionInfo();
    }

    private void initView() {
        ivBack= (ImageView) findViewById(R.id.img_back);
        Glide.with(this).load(R.drawable.homepage_back).into(ivBack);//添加背景图片
        listview= (ListViewForScrollView) findViewById(R.id.station_lv);
        listview.setFocusable(false);//避免数据更新后listview抢占焦点
        adapter=new StationListAdapter(HomeActivity.this,list);
        listview.setAdapter(adapter);
        tvAqi= (TextView) findViewById(R.id.tv_aqi);
        tvTiShi= (TextView) findViewById(R.id.tv_tishi);
        tvTiShi= (TextView) findViewById(R.id.tv_tishi);
        tvAqiLevel= (TextView) findViewById(R.id.tv_pollution_level);
        tvTemp= (TextView) findViewById(R.id.tv_temp);
        tvWeather= (TextView) findViewById(R.id.tv_weather);
        ivWeather= (ImageView) findViewById(R.id.iv_weather);
        tvDate= (TextView) findViewById(R.id.tv_date);
        tvTempRange= (TextView) findViewById(R.id.tv_temp_range);
        tvWind= (TextView) findViewById(R.id.tv_wind);
        tvShidu= (TextView) findViewById(R.id.tv_shidu);
        tvPM25Value= (TextView) findViewById(R.id.tv_pm25_value);
        tvPM10Value= (TextView) findViewById(R.id.tv_pm10_value);
        tvO3Value= (TextView) findViewById(R.id.tv_o3_value);
        tvNO2Value= (TextView) findViewById(R.id.tv_no2_value);
        tvSO2Value= (TextView) findViewById(R.id.tv_so2_value);
        tvCOValue= (TextView) findViewById(R.id.tv_co_value);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.topbar_background);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void getWeatherInfo() {
//        String url = "http://www.micromap.com.cn:8080/epservice/v1.2/api/weather/getAll/济南/0/0?token=YFJYeRKQouE0bWylekXl";
        String url = PathManager.getHomeWeather;
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        utils.send(HttpRequest.HttpMethod.GET, url,new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(HomeActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                weatherIsRefreshed=true;
                stopRefresh();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = String.valueOf(arg0.result);
                Gson gson=new Gson();
                WeatherInfo weatherInfo=gson.fromJson(result,WeatherInfo.class);
                WeatherInfo.NowWeatherBean nowWeatherBean= weatherInfo.getNowWeather().get(0);
                tvTemp.setText(nowWeatherBean.getFeelTemp());
                tvWeather.setText(nowWeatherBean.getWeather());
                tvDate.setText(nowWeatherBean.getDate()+nowWeatherBean.getWeekday()+nowWeatherBean.getLunar());
                tvTempRange.setText(nowWeatherBean.getTemp());
                tvWind.setText(nowWeatherBean.getWD()+nowWeatherBean.getWS());
                tvShidu.setText(nowWeatherBean.getSD());
                WeatherInfo.IndexBean indexBean=  weatherInfo.getIndex().get(0);
                tvTiShi.setText("温馨提示：\n"+indexBean.getIndex_co_xs());
                weatherIsRefreshed=true;
                stopRefresh();
            }
        });



    }

    private void getPollutionInfo() {
//        String url = "http://119.164.253.237:8033/Service/AppService.asmx/GetAQIOfAllSite";
        String url = PathManager.getHomeAQI;
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        RequestParams params=new RequestParams();
        params.addBodyParameter("userid","");
        utils.send(HttpRequest.HttpMethod.POST, url,params,new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(HomeActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                pollutionIsRefreshed=true;
                stopRefresh();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = String.valueOf(arg0.result);
                PollutionInfo pollutionInfo=new Gson().fromJson(result,PollutionInfo.class);
                PollutionInfo.MsgBean.WetherInfoBean wetherInfoBean=pollutionInfo.getMsg().get(0).getWetherInfo();
                PollutionInfo.MsgBean.AirInfoBean airInfoBean=pollutionInfo.getMsg().get(0).getAirInfo();
                tvAqi.setText(wetherInfoBean.getAqi());
                tvAqiLevel.setText(PollutionLevelCalUtil.getLevelDes("aqi",Double.parseDouble(wetherInfoBean.getAqi())));
                tvAqiLevel.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("aqi",Double.parseDouble(wetherInfoBean.getAqi())));
                tvPM25Value.setText(airInfoBean.getPm25());
                tvPM25Value.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("pm25",Double.parseDouble(airInfoBean.getPm25())));
                tvPM10Value.setText(airInfoBean.getPm10());
                tvPM10Value.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("pm10",Double.parseDouble(airInfoBean.getPm10())));
                tvO3Value.setText(airInfoBean.getO3());
                tvO3Value.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("o3",Double.parseDouble(airInfoBean.getO3())));
                tvSO2Value.setText(airInfoBean.getSo2());
                tvSO2Value.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("so2",Double.parseDouble(airInfoBean.getSo2())));
                tvNO2Value.setText(airInfoBean.getNo2());
                tvNO2Value.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("no2",Double.parseDouble(airInfoBean.getNo2())));
                tvCOValue.setText(airInfoBean.getCo());
                tvCOValue.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("co",Double.parseDouble(airInfoBean.getCo())));
                list.clear();
                list.addAll(pollutionInfo.getMsg().get(0).getStationInfo());
                adapter.notifyDataSetChanged();
                pollutionIsRefreshed=true;
                stopRefresh();
            }
        });



    }

    private void stopRefresh(){
        if(weatherIsRefreshed==true&&pollutionIsRefreshed==true){
            mSwipeRefreshLayout.setRefreshing(false);
        }else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


}
