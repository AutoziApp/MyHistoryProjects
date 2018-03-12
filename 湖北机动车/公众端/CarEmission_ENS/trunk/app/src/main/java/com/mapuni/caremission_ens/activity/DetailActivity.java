package com.mapuni.caremission_ens.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.caremission_ens.itemfactory.DetailItemFactory;
import com.mapuni.caremission_ens.presenter.BaseControl;
import com.mapuni.caremission_ens.presenter.CarDBInfoControl;
import com.mapuni.caremission_ens.views.MyDecoration;
import com.mapuni.caremission_ens.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class DetailActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    @Bind(R.id.recycle)
    RecyclerView recycle;
    List data;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    private ImageView backView;
    private TextView titleView;
    private BaseControl control;
    private YutuLoading yutuLoading;
    private AssemblyRecyclerAdapter adapter;
    private Spinner mTitleSpinner;
    private Context mContext;
    private CarDBInfoControl mDBInfoControl;
    private TextView mTitleView;

    private TextView tvFailed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        this.mContext=this;
        tvFailed= (TextView) findViewById(R.id.textView);
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        control = (BaseControl) getIntent().getExtras().get("class");
        setTitle(control.getTitle().trim(), true, false);
        mTitleSpinner = (Spinner) findViewById(R.id.title_spinner);
        if (control.getTitle().equals("车辆公开信息")){
            mTitleView = (TextView) findViewById(R.id.titleView);
            mTitleView.setVisibility(View.GONE);
            Intent intent = getIntent();
            String carNum = intent.getStringExtra("carNum");
            String vin = intent.getStringExtra("vin");
            mDBInfoControl = new CarDBInfoControl(vin,carNum);
            initSpinner();
        }
        control.requestData(call);
    }

    private void initSpinner() {
        mTitleSpinner.setVisibility(View.VISIBLE);

        String[] mItems = getResources().getStringArray(R.array.carMassage);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner .setAdapter(adapter);
        mTitleSpinner.setOnItemSelectedListener(this);
    }

    private void initAdapter() {
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(data);
        adapter.addItemFactory(new DetailItemFactory(this));
        recycle.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showFailed:
                yutuLoading.showDialog();
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        if ("车辆达标信息".equals(carMessage)){
                            mDBInfoControl.requestData(call);
                        }else{
                            control.requestData(call);
                        }
                    }
                }, 2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
        }

    }

    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            tvFailed.setText("加载失败，点击重试");
            showFailed();
//            String s = e.toString();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.i("tianfy", "response>>>>>>>>>>" + response);
//            StationDetailBean stationDetailBean= (StationDetailBean) JsonUtil.jsonToBean(response, StationDetailBean.class);
//            String flag = stationDetailBean.getResult().toString();

            try {
                JSONObject jsonObject=new JSONObject(response.toString());
                String result = jsonObject.getString("result");
                JSONArray info = jsonObject.getJSONArray("info");
                if ("1".equals(result)&&info.length()>0){
                    JSONObject object = info.getJSONObject(0);
                    if ("车辆达标信息".equals(carMessage)){
                        data=mDBInfoControl.transDataJX(object);
                    }else if("车辆公开信息".equals(carMessage)){
                        mDBInfoControl.setVin(object.optString("VIN",""));
                        data = control.transDataJX(object);
                    }else{
                        data = control.transDataJX(object);
                    }
                    if (data != null && data.size() > 0)
                        initAdapter();
                } else {
                    tvFailed.setText("暂无数据");
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.showFailed);
                    layout.setOnClickListener(null);
                    layout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


//            List<StationDetailBean.InfoBean> info = stationDetailBean.getInfo();
            yutuLoading.dismissDialog();
//            Log.i("tianfy", "mapSize>>>>>>>" + info.size());
//            if (flag.equals("1") && info.size() > 1) {
//                data = control.transDataJX(info.get(0));
//                if (data != null && data.size() > 0)
//                    initAdapter();
//            } else {
//                showFailed();
//            }
        }
    };
    private String carMessage="";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        requestAgain();
        switch (position){
            case 0://车辆公开信息
                yutuLoading.showDialog();
                carMessage="车辆公开信息";
                control.requestData(call);
                break;
            case 1://车辆达标信息
                yutuLoading.showDialog();
                carMessage="车辆达标信息";
                mDBInfoControl.requestData(call);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
