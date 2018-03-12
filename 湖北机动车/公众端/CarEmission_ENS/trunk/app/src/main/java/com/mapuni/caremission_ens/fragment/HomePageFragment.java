package com.mapuni.caremission_ens.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.activity.CityManagerActivity;
import com.mapuni.caremission_ens.bean.AirQualityBean;
import com.mapuni.caremission_ens.bean.IllegalOperationBean;
import com.mapuni.caremission_ens.divider.DividerItemDecoration;
import com.mapuni.caremission_ens.itemfactory.HomePageRecyclerItemFactory;
import com.mapuni.caremission_ens.utils.JsonUtil;
import com.mapuni.caremission_ens.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * @name CarEmission_ENS
 * @class name：com.mapuni.caremission_ens.fragment
 * @class describe
 * @anthor tianfy
 * @time 2017/10/23 15:13
 * @change
 * @chang time
 * @class describe
 */

public class HomePageFragment extends BaseFragment {
    @Bind(R.id.img_add)
    ImageView mImgAdd;
    @Bind(R.id.rightLayout)
    LinearLayout mRightLayout;
    @Bind(R.id.tv_aqi)
    TextView mTvAqi;
    @Bind(R.id.tv_pm25)
    TextView mTvpm25;
    @Bind(R.id.tv_pm10)
    TextView mTvPm10;
    @Bind(R.id.tv_no2)
    TextView mTvNo2;
    @Bind(R.id.tv_so2)
    TextView mTvSo2;
    @Bind(R.id.tv_co)
    TextView mTvCo;
    @Bind(R.id.tv_o3)
    TextView mTvO3;
    @Bind(R.id.txt_aqi)
    TextView mTxtAqi;
    @Bind(R.id.id_img)
    ImageView imageView;
    @Bind(R.id.tv_leve)
    TextView tvLeve;
    String cityName;
    XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源

    int page = 1;
    private String rows = "10";
    private boolean isLoadMore = false;
    private static final int mRequestCode = 1000;
    public static final int mResultCode = 2000;
    private View mView;

    public static HomePageFragment newInstance(String s) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, mView);
        initView(mView);
        initData();
        return mView;
    }



    @Override
    public void onResume() {
        super.onResume();
//        //请求空气质量数据
//        if (cityName == null) {
//            cityName = "武汉";
//        }
//        setTitle(mView, cityName);
//        requestAirQuality(cityName);
    }




    private void initView(View view) {
        imageView = (ImageView) view.findViewById(R.id.id_img);
        xRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mAct));
        recyclerView.addItemDecoration(new DividerItemDecoration(mAct, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new HomePageRecyclerItemFactory(mAct));
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        initRefreshView();

        mImgAdd.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.GONE);
        mImgAdd.setOnClickListener(this);
        //开始阳光转动的动画
        startSunAnimation();

    }

    private void initData() {
        cityName= (String) SPUtils.getSp(mAct, "cityName", "武汉");
        setTitle(mView, cityName);
        requestAirQuality(cityName);
    }

    /**
     * 请求空气质量数据
     *
     * @param cityName
     */
    private void requestAirQuality(String cityName) {
        mControl.requestAirQuality(cityName, call);
    }

    private void initRefreshView() {
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);//解决横向移动冲突
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                isLoadMore = false;
                page = 1;
                loadData();
                xRefreshView.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                page++;
                loadData();
                xRefreshView.stopLoadMore(true);
            }
        });
        loadData();
    }

    private void loadData() {
        yutuLoading.showDialog();
        mControl.requestIllegalOperation(illegalOperationcall, page, rows);
    }

    /**
     * @author tianfy
     * 开始阳光转动的动画
     */
    private void startSunAnimation() {
        Animation animation = AnimationUtils.loadAnimation(mAct, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        imageView.setAnimation(animation);
        animation.setFillAfter(true);//保存动画执行后的效果
    }


    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(String response, int id) {
            AirQualityBean airQualityBean = (AirQualityBean) JsonUtil.jsonToBean(response, AirQualityBean.class);
            List<AirQualityBean.InfoBean> info = airQualityBean.getInfo();
            String result = airQualityBean.getResult();
            if ("1".equals(result) && info.size() > 0) {
                initAirQuality(info.get(0));
            }else{
                initAirQuality2();
            }

        }
    };



    private StringCallback illegalOperationcall = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            IllegalOperationBean illegalOperationBean = (IllegalOperationBean) JsonUtil.jsonToBean(response, IllegalOperationBean.class);
            String result = illegalOperationBean.getResult();
            List<IllegalOperationBean.InfoBean> info = illegalOperationBean.getInfo();
            if ("1".equals(result) && info.size() > 0) {
                if (!isLoadMore) {
                    adapter.setDataList(info);
                } else {
                    adapter.addAll(info);
                }
            } else {
                adapter.setDataList(null);
            }
        }
    };

    private void initAirQuality(AirQualityBean.InfoBean infoBean) {
        mTvAqi.setText(infoBean.getAQI() + "");
        mTvpm25.setText(infoBean.getPM25());
        mTvPm10.setText(infoBean.getPM10());
        mTvNo2.setText(infoBean.getNO2());
        mTvSo2.setText(infoBean.getSO2());
        mTvCo.setText(infoBean.getCO());
        mTvO3.setText(infoBean.getO3());
        mTxtAqi.setText(infoBean.getAQI() + "");
        initLeve(infoBean.getAQI());//设置污染等级
    }

    private void initAirQuality2() {
        mTvAqi.setText("--");
        mTvpm25.setText("--");
        mTvPm10.setText("--");
        mTvNo2.setText("--");
        mTvSo2.setText("--");
        mTvCo.setText("--");
        mTvO3.setText("--");
        mTxtAqi.setText("--");
        tvLeve.setText("**");
    }

    /**
     * 设置污染等级
     *
     * @param leve
     */
    private void initLeve(int leve) {
        if (leve >= 0 && leve <= 50) {
            tvLeve.setText("优");
        } else if (leve <= 100) {
            tvLeve.setText("良");
        } else if (leve <= 150) {
            tvLeve.setText("轻度");
        } else if (leve <= 200) {
            tvLeve.setText("中度");
        } else if (leve <= 300) {
            tvLeve.setText("重度");
        } else {
            tvLeve.setText("严重");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_add:

                Intent intent = new Intent(mAct, CityManagerActivity.class);
                startActivityForResult(intent, mRequestCode);

                break;
            //            点击重试
            case R.id.showFailed:
                yutuLoading.showDialog();
//                延时发送
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        requestAirQuality(cityName);
                        isLoadMore = false;
                        page = 1;
                        loadData();
                    }
                }, 2000);
                requestAgain();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == mResultCode) {
            cityName = data.getStringExtra("cityName");
            //请求空气质量数据
            if (cityName == null) {
                cityName = "武汉";
            }
            SPUtils.setSP(mAct,"cityName",cityName);
            
            setTitle(mView, cityName);
            requestAirQuality(cityName);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
