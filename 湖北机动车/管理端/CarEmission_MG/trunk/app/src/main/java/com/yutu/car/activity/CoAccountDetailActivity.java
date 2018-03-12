package com.yutu.car.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yutu.car.R;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.yutu.car.bean.CoAccountDetailBean;
import com.yutu.car.itemfactory.CoDetailItemFactory;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.MyDecoration;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yutu.car.R.id.recycle;

/**
 * @name CarEmission_MG
 * @class name：com.yutu.car.activity
 * @class describe
 * @anthor tianfy
 * @time 2017/10/28 13:52
 * @change
 * @chang time
 * @class describe
 */

public class CoAccountDetailActivity extends BaseActivity {
    
    @Bind(R.id.rightIconA)
    ImageView mRightIconA;
    @Bind(recycle)
    RecyclerView mRecycle;
    private Context mcontext;
    private YutuLoading yutuLoading;
    private AssemblyRecyclerAdapter adapter;
    List data;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaccount_detail);
        ButterKnife.bind(this);
        yutuLoading = new YutuLoading(this);
        this.mcontext = this;
        setTitle("车辆碳氧化物详情", true, false);
        mRightIconA.setOnClickListener(this);
        initAdapter();
        initData();
    }
    private void initAdapter() {
        mRecycle.setLayoutManager(new LinearLayoutManager(this));
        mRecycle.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(data);
        adapter.addItemFactory(new CoDetailItemFactory(this));
        mRecycle.setAdapter(adapter);
    }

    private void initData() {
        yutuLoading.showDialog();
        String carType = getIntent().getStringExtra("carType");
        NetControl.requestCOtotal(carType, call);
    }

    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            CoAccountDetailBean bean= (CoAccountDetailBean) JsonUtil.jsonToBean(response, CoAccountDetailBean.class);
            int flag = bean.getFlag();
            List<CoAccountDetailBean.DataBean> detailList = bean.getData();
            if (flag==1&&detailList.size()>0){
                adapter.setDataList(detailList);
            }else{
                
            }
        }
    };
    
    @OnClick(R.id.rightIconA)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightIconA:
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
        }
    }

}
