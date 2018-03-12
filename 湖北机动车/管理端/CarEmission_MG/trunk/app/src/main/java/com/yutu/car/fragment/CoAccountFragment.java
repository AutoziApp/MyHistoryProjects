package com.yutu.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.activity.CoAccountDetailActivity;
import com.yutu.car.presenter.NetControl;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.yutu.car.R.layout.my_simple_spinner_item;


public class CoAccountFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.rightIconA)
    ImageView rightIconA;
    @Bind(R.id.spinner_car_type)
    AppCompatSpinner spinnerCarType;
    @Bind(R.id.car_total)
    TextView mCarTotal;
    @Bind(R.id.emission_amount)
    TextView mEmissionAmount;
    @Bind(R.id.searchBtn)
    Button mSearchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_co_account, container, false);
        setTitle(view, "碳氧化物核算");
        ButterKnife.bind(this, view);
        rightIconA.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        initData();
        return view;
    }

    private void initData() {
        //请求车辆数和排放总量
        yutuLoading.showDialog();
        initSpinner();
        NetControl.requestCOtotal("",call);
    }

    private void initSpinner() {
        String[] carTypes = mAct.getResources().getStringArray(R.array.carType);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mAct, R.layout.co_simple_spinner_item, carTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarType.setAdapter(adapter);
        spinnerCarType.setOnItemSelectedListener(this);
    }


    private StringCallback call=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            Toast.makeText(mAct, "请求失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            try {
                JSONObject jsonObject=new JSONObject(response);
                String flag = jsonObject.getString("flag");
                if ("1".equals(flag)){
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONObject object= (JSONObject) data.get(0);
                    mCarTotal.setText(object.getString("CLS").trim());
                    mEmissionAmount.setText(object.getString("PFL"));
                }else{
                    Toast.makeText(mAct, "请求失败", Toast.LENGTH_SHORT).show(); 
                }
            } catch (JSONException e) {
                Toast.makeText(mAct, "请求失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static CoAccountFragment newInstance(String s) {
        CoAccountFragment fragment = new CoAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:
                Intent intent = new Intent(mAct, CoAccountDetailActivity.class);
                intent.putExtra("carType",carType+"");
                startActivity(intent);
                break;
            
            case R.id.rightIconA:
                showPoupMenu();
                break;
        }
    }

    private int carType=1;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                carType=1;//载客
                break;
            case 1:
                carType=2;//载货

                break;
            case 2:
                carType=3;//摩托
                break;
            case 3:
                carType=9;//其他
                break;
        }
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
