package com.mapuni.mobileenvironment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.activity.ActivityBase;
import com.mapuni.mobileenvironment.activity.CompanyActivity;
import com.mapuni.mobileenvironment.activity.CompanyPollutionDetailActivity;
import com.mapuni.mobileenvironment.activity.CompanySearchResultActivity;
import com.mapuni.mobileenvironment.activity.NewAirActivity;
import com.mapuni.mobileenvironment.activity.NewAirDayActivity;
import com.mapuni.mobileenvironment.activity.YouYanActivity;
import com.mapuni.mobileenvironment.activity.newAirCurActivity;
import com.mapuni.mobileenvironment.adapter.MyAdapter;
import com.mapuni.mobileenvironment.adapter.MyAdapter2;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.model.EntModel;
import com.mapuni.mobileenvironment.model.GasSite;
import com.mapuni.mobileenvironment.model.HistoryRecord;
import com.mapuni.mobileenvironment.model.ItemDetailModel;
import com.mapuni.mobileenvironment.utils.ACacheUtil;
import com.mapuni.mobileenvironment.utils.DateUtils;
import com.mapuni.mobileenvironment.utils.JsonUtil;
import com.mapuni.mobileenvironment.utils.MyDecoration;
import com.mapuni.mobileenvironment.utils.SignUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class WatchFragment extends SupportFragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecycle, mRRecycle;
    private ArrayList<ItemDetailModel> airList;
    private ArrayList<ItemDetailModel> pollutionList;
    private RecyclerView.Adapter messageAdapter, planAdapter;
    private LinearLayout water_jk, air_jk, company_cx, pollution_source_jk;
    private LinearLayout pollutionMore;
    private LinearLayout airMore;
    private LinearLayout tableColum;

    public WatchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WatchFragment newFragment() {
        WatchFragment fragment = new WatchFragment();
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mListener.onCloseToolbar(false);
            mListener.onChangeTitle(getResources().getString(R.string.tab_watch));
        }
//        super.onHiddenChanged(hidden);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch, container, false);
        mRecycle = (RecyclerView) view.findViewById(R.id.weatherList);
        mRRecycle = (RecyclerView) view.findViewById(R.id.pollutList);
        water_jk = (LinearLayout) view.findViewById(R.id.water_jk);
        air_jk = (LinearLayout) view.findViewById(R.id.air_jk);
        company_cx = (LinearLayout) view.findViewById(R.id.company_cx);
        pollution_source_jk = (LinearLayout) view.findViewById(R.id.pollution_source_jk);
        pollutionMore = (LinearLayout) view.findViewById(R.id.pollutionMoreLayout);
        airMore = (LinearLayout) view.findViewById(R.id.airMore);
        tableColum = (LinearLayout) view.findViewById(R.id.table_coloum);
        // updateAirList();
        updateOverList();
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAirList();//刷新最近浏览
    }

    private void initListener() {
        water_jk.setOnClickListener(this);
        air_jk.setOnClickListener(this);
        company_cx.setOnClickListener(this);
        pollution_source_jk.setOnClickListener(this);
        pollutionMore.setOnClickListener(this);
        airMore.setOnClickListener(this);
    }

    public void initList() {
        planAdapter = new MyAdapter(_mActivity, pollutionList, new PollutionItemListener());

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);

        mRecycle.setLayoutManager(layoutManager);

        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        MyDecoration decoration1 = new MyDecoration(getActivity(), LinearLayout.VERTICAL, false);
        decoration1.setDivider(getActivity(), R.drawable.list_divider);
        mRecycle.addItemDecoration(decoration1);

        mRecycle.setAdapter(planAdapter);

    }

    public void initList2() {
        if (airList != null && airList.size() > 0) {
            tableColum.setVisibility(View.VISIBLE);
        } else {
            tableColum.setVisibility(View.GONE);
        }
        messageAdapter = new MyAdapter2(_mActivity, airList, new GasItemListener());
        LinearLayoutManager reManager = new LinearLayoutManager(_mActivity);
        mRRecycle.setLayoutManager(reManager);
        reManager.setOrientation(OrientationHelper.VERTICAL);
        MyDecoration decoration2 = new MyDecoration(getActivity(), LinearLayout.VERTICAL, false);
        decoration2.setDivider(getActivity(), R.drawable.list_divider);
        mRRecycle.addItemDecoration(decoration2);
        mRRecycle.setAdapter(messageAdapter);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.water_jk:
                startActivity(intent.setClass(getActivity(), YouYanActivity.class));
                break;
            case R.id.air_jk:
//                intent.setClass(getActivity(), WatchActivity.class);
//                intent.putExtra("before",ActivityBase.ISAIR);
//                startActivity(intent);
                startActivity(new Intent(getActivity(), NewAirActivity.class));
                break;
            case R.id.company_cx:
                startActivity(intent.setClass(getActivity(), CompanySearchResultActivity.class));
                break;
            case R.id.pollution_source_jk:
                intent.setClass(getActivity(), CompanyActivity.class);
//                intent.setClass(getActivity(), ChartActivity.class);
                intent.putExtra("before", ActivityBase.ISPOLLUTION);
                startActivity(intent);
                break;
            case R.id.pollutionMoreLayout:
                intent.setClass(getActivity(), CompanyActivity.class);
                intent.putExtra("before", ActivityBase.ISPOLLUTION);
                startActivity(intent);
                break;
            case R.id.airMore:
                startActivity(new Intent(getActivity(), NewAirActivity.class));
                break;
        }
    }

    public void updateOverList() {
        OkHttpUtils.get().url(PathManager.GetOverEnterList).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                ArrayList list = JsonUtil.jsonToListMap(response);
                if (pollutionList == null) {
                    pollutionList = new ArrayList<ItemDetailModel>();
                }
                for (int i = 0; i < list.size(); i++) {
                    ItemDetailModel model = new ItemDetailModel();
                    HashMap map = (HashMap) list.get(i);
                    String name = (String) map.get("EntName");
                    String tag = (String) map.get("OverTime");
                    Object obj = map.get("EntCode");
                    tag = tag.replace("T", " ");
                    model.setName(name);
                    model.setTag(DateUtils.timeFriendly(tag));
                    model.setId((new Double((double) obj)).intValue());
                    pollutionList.add(model);
                }
                initList();
                Log.i("Lybin", "..." + response);
            }
        });
    }

    private void updateAirList() {
        if (airList == null) {
            airList = new ArrayList<>();
        } else {
            airList.clear();
        }

//        String url = PathManager.GetSiteDataByTime;
//        Map<String, String> jo = new HashMap<>();
//        String nonce = SignUtil.getNonce();
//        String timestamp = new Date().getTime() + "";
//        jo.put("timestamp", timestamp);
//        jo.put("nonce", nonce);
//        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
////        jo.put("time", DateUtils.getSystemNowTime().replace("-", "/"));
//        jo.put("time", DateUtils.getYesterdayDate());
//        jo.put("type", "0");
        String url = PathManager.GetDataHour;
        Map<String, String> jo = new HashMap<>();
        String nonce = SignUtil.getNonce();
        String timestamp = new Date().getTime() + "";
        jo.put("timestamp", timestamp);
        jo.put("nonce", nonce);
        jo.put("signature", SignUtil.getSignature2(timestamp, nonce));
        OkHttpUtils
                .post()//
                .url(url)//
                .params(jo)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        AirCurData airCurData = (AirCurData) JsonUtil.jsonToBean(response, AirCurData.class);
                        GasSite airSite = (GasSite) JsonUtil.jsonToBean(response, GasSite.class);
                        if (0 == airSite.getRet()) {
                            //筛选数据，只要与所存储的deviceid不匹配的直接移除
                            if (ACacheUtil.readHistoryRecord(getActivity()) != null) {//当所取得的记录不为空时执行筛选操作
                                for (HistoryRecord historyRecord : ACacheUtil.readHistoryRecord(getActivity())) {//遍历所存储的浏览记录
                                    for (GasSite.DataBeanX.DataBean dataBean : airSite.getData().getData()) {
                                        if (historyRecord.getDeviceId().equals(dataBean.getId())) {//当匹配上了那么就往数据源中添加

                                            airList.add(new ItemDetailModel(dataBean.getName().replaceAll("\\d+", "").trim(),
                                                    -1==dataBean.getPm25()?"-":dataBean.getPm25()+"", dataBean.getId(),
                                                    dataBean.getMost().replaceAll("Average", " ").toUpperCase(), historyRecord.getTag()));
                                        }
                                    }
                                }
                            }
//                        if (0 == airCurData.getRet()) {
//                            //筛选数据，只要与所存储的deviceid不匹配的直接移除
//                            if (ACacheUtil.readHistoryRecord(getActivity()) != null) {//当所取得的记录不为空时执行筛选操作
//                                for (HistoryRecord historyRecord : ACacheUtil.readHistoryRecord(getActivity())) {//遍历所存储的浏览记录
//                                    for (AirCurData.DataBean dataBean : airCurData.getData()) {
//                                        if (historyRecord.getDeviceId().equals(dataBean.getDeviceid())) {//当匹配上了那么就往数据源中添加
//                                            airList.add(new ItemDetailModel(dataBean.getDeviceName().replaceAll("\\d+", "").trim(), dataBean.getPm25() + "", dataBean.getDeviceid(), dataBean.getMost().replaceAll("Average", " ").toUpperCase(), historyRecord.getTag()));
//                                        }
//                                    }
//                                }
//                            }
//                            Collections.sort(airList, new Comparator<ItemDetailModel>() {
//                                @Override
//                                public int compare(ItemDetailModel t0, ItemDetailModel t1) {
//                                    if (Double.parseDouble(t0.getTag()) > Double.parseDouble(t1.getTag())) {
//                                        return -1;
//                                    } else if(Double.parseDouble(t0.getTag()) < Double.parseDouble(t1.getTag())){
//                                        return 1;
//                                    }else{
//                                        return 0;
//                                    }
//                                }
//                            });
//                            ArrayList<ItemDetailModel> tempList = new ArrayList<>();//截取集合长度为5
//                            for (int i = 0; i < airList.size(); i++) {
//                                if (i < 5) {
//                                    tempList.add(airList.get(i));
//                                }
//                            }
//                            airList=tempList;
                        }
                        initList2();
                    }
                });

    }

    private class PollutionItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ItemDetailModel model = (ItemDetailModel) v.getTag();
            if (model == null) {
                Toast.makeText(getActivity(), "无数据", Toast.LENGTH_SHORT).show();
                return;
            }
//
            EntModel.EnterpriseBean bean = new EntModel.EnterpriseBean();
            Intent intent = new Intent(getActivity(), CompanyPollutionDetailActivity.class);
            bean.setEntCode((int) model.getId());
            bean.setEntName(model.getName());
            intent.putExtra("data", bean);
            startActivity(intent);
        }
    }

    private class GasItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ItemDetailModel model = (ItemDetailModel) v.getTag();
            if (model == null) {
                Toast.makeText(getActivity(), "无数据", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (model.getFlag()) {
                case "curData":
                    Intent intent = new Intent(getActivity(), newAirCurActivity.class);
                    intent.putExtra("Deviceid", model.getDeviceId());
                    intent.putExtra("DeviceName", model.getName());
                    startActivity(intent);
                    break;
                case "dayData":
                    Intent intent1 = new Intent(getActivity(), NewAirDayActivity.class);
                    intent1.putExtra("Deviceid",model.getDeviceId());
                    intent1.putExtra("DeviceName", model.getName());
                    startActivity(intent1);
                    break;
            }

        }
    }

}
