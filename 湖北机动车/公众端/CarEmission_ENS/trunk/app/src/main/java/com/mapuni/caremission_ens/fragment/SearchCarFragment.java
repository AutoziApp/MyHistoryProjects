package com.mapuni.caremission_ens.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.activity.DetailActivity;
import com.mapuni.caremission_ens.presenter.CarInfoControl;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchCarFragment extends BaseFragment {

    @Bind(R.id.carNumEdit)
    EditText carNumEdit;
    @Bind(R.id.vinEdit)
    EditText vinEdit;
    @Bind(R.id.searchBtn)
    Button searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_car, container, false);
        setTitle(view, "车辆查询");
        ButterKnife.bind(this, view);
        searchBtn.setOnClickListener(this);
        return view;
    }

    public static SearchCarFragment newInstance(String s) {
        SearchCarFragment fragment = new SearchCarFragment();
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
        switch (v.getId()){
            case R.id.searchBtn:
                String vin = vinEdit.getText().toString();
                String carNum = carNumEdit.getText().toString();
//                String vin="LZYTETB27G1024450";
//                String carNum="鄂FLM788";
                if(vin.equals("")||carNum.equals("")){
                    Toast.makeText(mAct,"输入信息不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                CarInfoControl model = new CarInfoControl(vin,carNum);
                Intent intent = new Intent(mAct, DetailActivity.class);
                intent.putExtra("class",model);
                intent.putExtra("carNum",carNum);
                intent.putExtra("vin",vin);
                startActivity(intent);
            break;
            default:
                super.onClick(v);
                break;
        }
    }
}
