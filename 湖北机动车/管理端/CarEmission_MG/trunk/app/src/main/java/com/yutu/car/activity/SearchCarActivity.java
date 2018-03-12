package com.yutu.car.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.presenter.CarInfoControl;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.yutu.car.R.id.searchBtn;

/**
 * @name CarEmission_MG
 * @class name：com.yutu.car.activity
 * @class describe
 * @anthor Administrator
 * @time 2017/10/27 10:53
 * @change
 * @chang time
 * @class describe
 */

public class SearchCarActivity extends BaseActivity {

    @Bind(R.id.carNumEdit)
    EditText carNumEdit;
    @Bind(R.id.vinEdit)
    EditText vinEdit;
    @Bind(R.id.searchBtn)
    Button searchBtn;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);
        setTitle("车辆查询",true,false);
        ButterKnife.bind(this);
        this.mContext=this;
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        
        switch (v.getId()){
            case R.id.searchBtn:
                String vin = vinEdit.getText().toString();
                String carNum = carNumEdit.getText().toString();
//                String vin="LZYTETB27G1024450";
//                String carNum="鄂FLM788";
                if(vin.equals("")||carNum.equals("")){
                    Toast.makeText(mContext,"输入信息不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                CarInfoControl model = new CarInfoControl(vin,carNum);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("class",model);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
                break;
            case R.id.leftIcon:
                finish();
                break;
            default:
                super.onClick(v);
                break;
        }
    }
}
