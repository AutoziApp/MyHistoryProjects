package com.mapuni.mobileenvironment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.CompanyPollutionDetailActivity;
import com.mapuni.mobileenvironment.adapter.WeatherAdapter;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.EntModel.EnterpriseBean;
import com.mapuni.mobileenvironment.model.WeatherModel;
import com.mapuni.mobileenvironment.utils.ACache;
import com.mapuni.mobileenvironment.utils.CommonUtil;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.view.YutuLoading;
import com.mapuni.mobileenvironment.widget.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class HomeFragment extends SupportFragment {

    private OnFragmentInteractionListener mListener;
    private XListView xListView;
    private View mHeaderView;
    private LinearLayout weatherlay;
    private View view;
    private YutuLoading yutuLoading;
    WeatherModel model;
    private final int call_fail = -1;
    private TextView aqiHead;
    private TextView aqiTextHead;
    private TextView heatHead;
    private TextView weatherHead;
    private TextView dayHead;
    private TextView weekHead;
    private TextView monthHead;
    private TextView updateTimeHead;
    private TextView heatBottom;
    private TextView windBottom;
    private TextView waterBottom;
    private ImageView homeBg;
    private ImageView weatherIconHead;
    private ImageView qq_luo, qq_mianju,qq_heye,
            qq_qiufu, qq_mojing, qq_xiafu,
            qq_cunfu, qq_dongfu, qq_kouzhao;
    private int FIRSTINTO = 1;
    private int UPDATE = 2;
    WeatherAdapter mAdapter;
    private ItemListener listener;
    private ACache mCache;



    public static HomeFragment newFragment() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        aqiUpdated = true;
        updateAqi();
        initHeadView();
//        initData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mListener!=null&&view.getVisibility()==View.VISIBLE){
            mListener.onCloseToolbar(true);
            mListener.onChangeTitle(getResources().getString(R.string.tab_home_title));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            mListener.onCloseToolbar(true);
            mListener.onChangeTitle(getResources().getString(R.string.tab_home_title));
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    xListView.stopRefresh();
                    updateData();
                    break;
                case call_fail:
                    break;
                default:
                    break;
            }
        };
    };
    private void initView(View view){
        yutuLoading = new YutuLoading(getActivity());
        homeBg = (ImageView) view.findViewById(R.id.homeBg);
        xListView = (XListView) view.findViewById(R.id.xListView);
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.environment_current_weather_activity_item1, null);
        qq_qiufu = (ImageView) mHeaderView.findViewById(R.id.qq_qiufu);
        qq_xiafu = (ImageView) mHeaderView.findViewById(R.id.qq_xiafu);
        qq_cunfu = (ImageView) mHeaderView.findViewById(R.id.qq_cunfu);
        qq_dongfu = (ImageView) mHeaderView.findViewById(R.id.qq_dongfu);
        qq_kouzhao = (ImageView) mHeaderView.findViewById(R.id.qq_kouzhao);
        qq_heye = (ImageView) mHeaderView.findViewById(R.id.qq_heye);
        qq_mianju = (ImageView) mHeaderView.findViewById(R.id.qq_mianju);
        qq_mojing = (ImageView) mHeaderView.findViewById(R.id.qq_mojing);
        weatherlay = (LinearLayout) mHeaderView.findViewById(R.id.weatherlay);
        aqiHead = (TextView) mHeaderView.findViewById(R.id.aiq_detail);
        aqiTextHead = (TextView) mHeaderView.findViewById(R.id.aiq_detail_value);
        weatherIconHead = (ImageView) mHeaderView.findViewById(R.id.current_weather_imy);
        heatHead = (TextView) mHeaderView.findViewById(R.id.weather_temperature);
        weatherHead = (TextView) mHeaderView.findViewById(R.id.weather_climate);
        dayHead = (TextView) mHeaderView.findViewById(R.id.weather_date);
        weekHead = (TextView) mHeaderView.findViewById(R.id.weather_today);
        monthHead = (TextView) mHeaderView.findViewById(R.id.weather_lunar);
        updateTimeHead = (TextView) mHeaderView.findViewById(R.id.weather_time);
        heatBottom = (TextView) mHeaderView.findViewById(R.id.weather_temp);
        windBottom = (TextView) mHeaderView.findViewById(R.id.weather_wind_direction);
        waterBottom = (TextView) mHeaderView.findViewById(R.id.weather_sidu);
        search(FIRSTINTO);
    }
    private void initData(){
        if(model==null){
            return;
        }
        if(listener==null){
            listener = new ItemListener();
        }
        mAdapter = new WeatherAdapter(getActivity(), model,listener);
        xListView.setAdapter(mAdapter);
        xListView.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                aqiUpdated = true;
                updateAqi();
                search(UPDATE);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }
    private boolean aqiUpdated  = false;
    private void updateAqi(){
        OkHttpUtils.get().url(PathManager.WEATHER_CHANGPING_AQI).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
//                yutuLoading.showFailed();
//                handler.sendEmptyMessageDelayed(call_fail,2000);
            }

            @Override
            public void onResponse(String response, int id) {
                if (response==null||response.equals("")){
                    return;
                }
                HashMap map = (HashMap) JsonUtil.jsonToMap(response);
                List list = (List) map.get("data");
                Map _Map = (Map) list.get(list.size()-1);
                String aqi = _Map.get("aqi")+"";
                Double time = (Double) _Map.get("time");
//                String s = Double.parseDouble(time);
//                Log.i("Lybin",s);
                BigDecimal bd = new BigDecimal(time+"");
                String s = bd.toPlainString();
                String updateTime =DateUtils.formatUpdateTime(s)+"更新";

                if(aqi.contains(".")){
                    int index = aqi.indexOf(".");
                    aqi = aqi.substring(0,index);
                }
                aqiHead.setText(aqi);
                aqiTextHead.setBackgroundResource(CommonUtil.getRIdByAQI(aqi));
                aqiTextHead.setText(CommonUtil.getDengJiByAQI(aqi));
                updateTimeHead.setText(updateTime);
                aqiUpdated = false;
            }
        });

    }
    private void updateData(){

        if(model==null)
            return;
        if(mAdapter==null)
            initData();
        updateOverList();
        WeatherModel.NowWeatherBean nowBean = model.getNowWeather().get(0);
        updateQQ(nowBean);
        homeBg.setImageBitmap(CommonUtil.updateWeather(getActivity(),nowBean.getWeather()));
        if(aqiUpdated){
//            aqiTextHead.setBackgroundResource(CommonUtil.getRIdByAQI(nowBean.getPM2Dot5Data()));
//        homeBg.setBackgroundResource(R.mipmap.qin);
//            aqiHead.setText(nowBean.getPM2Dot5Data());
//            aqiTextHead.setText(nowBean.getLevel());
        }
        heatHead.setText(nowBean.getFeelTemp().replaceAll("℃",""));
        weatherHead.setText(nowBean.getWeather());
        dayHead.setText(nowBean.getDate());
        weekHead.setText(nowBean.getWeekday());
        monthHead.setText(nowBean.getLunar());
//        updateTimeHead.setText(DateUtils.getSystemNowTime()+"发布");
        windBottom.setText(nowBean.getWD()+nowBean.getWS());
        waterBottom.setText(nowBean.getSD());
        heatBottom.setText(nowBean.getTemp());
        weatherIconHead.setImageDrawable(getResources().getDrawable(mAdapter.getWeatherIcon(nowBean.getWeather())));
    }
    TextView aName,bName,cName,aAdress,bAdress,cAdress,aGrain,bGrain,cGrain;
    private void initOverList(View v){
        aName = (TextView) v.findViewById(R.id.aName);
        bName = (TextView) v.findViewById(R.id.bName);
        cName = (TextView) v.findViewById(R.id.cName);
        aAdress = (TextView) v.findViewById(R.id.aAdress);
        bAdress = (TextView) v.findViewById(R.id.bAdress);
        cAdress = (TextView) v.findViewById(R.id.cAdress);
        aGrain = (TextView) v.findViewById(R.id.aGrain);
        bGrain = (TextView) v.findViewById(R.id.bGrain);
        cGrain = (TextView) v.findViewById(R.id.cGrain);
    }
    public class ItemListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            EnterpriseBean bean = new EnterpriseBean();
            ArrayList<HashMap<String,String>> list = model.getOverList();
            Object obj;
            if(list==null||list.size()==0){
                Toast.makeText(getActivity(),"暂无数据",Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                return;
            }
            switch (v.getId()){
                case R.id.item_one:
                    obj= list.get(0).get("EntCode");
                    bean.setEntCode((new Double((double)obj)).intValue());
                    bean.setEntName(list.get(0).get("EntName"));
                    break;
                case R.id.item_two:
                    obj = list.get(1).get("EntCode");
                    bean.setEntCode((new Double((double)obj)).intValue());
                    bean.setEntName(list.get(1).get("EntName"));
                    break;
                case R.id.item_three:
                    obj = list.get(2).get("EntCode");
                    bean.setEntCode((new Double((double)obj)).intValue());
                    bean.setEntName(list.get(2).get("EntName"));
                    break;
            }
            Intent intent = new Intent(getActivity(), CompanyPollutionDetailActivity.class);
            intent.putExtra("data",bean);
            startActivity(intent);
        }
        public void setView(View v){
            initOverList(v);
            ArrayList<HashMap<String,String>> list = model.getOverList();
            if(list==null||list.size()==0)
                return;
            aName.setText(list.get(0).get("EntName"));
            aAdress.setText(list.get(0).get("Address"));
            aGrain.setText(list.get(0).get("PollutantName")+"");
            bName.setText(list.get(1).get("EntName"));
            bAdress.setText(list.get(1).get("Address"));
            bGrain.setText(list.get(1).get("PollutantName")+"");
            cName.setText(list.get(2).get("EntName"));
            cAdress.setText(list.get(2).get("Address"));
            cGrain.setText(list.get(2).get("PollutantName")+"");
        }
    }
    private void initHeadView(){
        int reheight = getDisplayHeight(getActivity())
                - getResources().getDimensionPixelOffset(R.dimen.DIMEN_55dp)
                - getResources().getDimensionPixelOffset(R.dimen.DIMEN_55dp);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        weatherlay.measure(w, h);
        int height = weatherlay.getMeasuredHeight();

        int screenHalfheighead = reheight + height ;

        mHeaderView.setMinimumHeight(screenHalfheighead);
        xListView.addHeaderView(mHeaderView, null, true);
    }
    public int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }

    private void updateQQ(WeatherModel.NowWeatherBean bean){
        try {
            int temp_qq = Integer.parseInt(bean.getFeelTemp().replaceAll("℃",""));
            qq_dongfu.setVisibility(View.GONE);
            qq_qiufu.setVisibility(View.GONE);
            qq_cunfu.setVisibility(View.GONE);
            qq_xiafu.setVisibility(View.GONE);
            if (temp_qq <= 0) {
                qq_dongfu.setVisibility(View.VISIBLE);
            } else if (temp_qq > 0 && temp_qq <= 10) {
                qq_qiufu.setVisibility(View.VISIBLE);
            } else if (temp_qq > 10 && temp_qq <= 25) {
                qq_cunfu.setVisibility(View.VISIBLE);
            } else if (temp_qq > 25) {
                qq_xiafu.setVisibility(View.VISIBLE);
            }
            String backgroundType = bean.getWeather();
            if (backgroundType.contains("晴")&&!backgroundType.contains("云")) {
                qq_mojing.setVisibility(View.VISIBLE);
            } else {
                qq_mojing.setVisibility(View.GONE);
            }
            if (backgroundType.equals("雨")) {
                qq_heye.setVisibility(View.VISIBLE);
            } else {
                qq_heye.setVisibility(View.GONE);
            }
            String qq_aqi = CommonUtil.getDengJiByAQI(bean.getPM2Dot5Data());
            if (qq_aqi.equals("优") || qq_aqi.equals("良")) {
                qq_kouzhao.setVisibility(View.GONE);
                qq_mianju.setVisibility(View.GONE);
            } else if (qq_aqi.equals("轻度") || qq_aqi.equals("中度")) {
                qq_mianju.setVisibility(View.GONE);
                qq_kouzhao.setVisibility(View.VISIBLE);
            } else if (qq_aqi.equals("重度") || qq_aqi.equals("严重")) {
                qq_mianju.setVisibility(View.VISIBLE);
                qq_kouzhao.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
    }
    private String WeatherCacheKey = "weather_cache";
    public void search(final int type){
        OkHttpUtils.get().url(PathManager.WEATHER_CHANGPING).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                if(mCache==null)
                    mCache = ACache.get(getActivity());
//                yutuLoading.showDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
//                yutuLoading.showFailed();
//                handler.sendEmptyMessageDelayed(call_fail,2000);
                String response = mCache.getAsString(WeatherCacheKey);
                model = (WeatherModel) JsonUtil.jsonToBean(response, WeatherModel.class);
                if(type==FIRSTINTO){
                    initData();
                }else{
                    Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                }
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                yutuLoading.dismissDialog();
                    model = (WeatherModel) JsonUtil.jsonToBean(response, WeatherModel.class);
                mCache.put(WeatherCacheKey,response);
                if(type==FIRSTINTO){
                    initData();
                }else{
                    Toast.makeText(getActivity(),"更新完成",Toast.LENGTH_SHORT).show();
                }
                handler.sendEmptyMessage(1);
//                Toast.makeText(getActivity(),model.getWeather().get(0).getWeather(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateOverList(){
        OkHttpUtils.get().url(PathManager.GetOverEnterList).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                ArrayList list = JsonUtil.jsonToListMap(response);
                Log.i("Lybin","..."+response);
                if(list!=null&&model!=null)
                    model.setOverList(list);
            }
        });
    }


}
