package com.yutu.car.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.activity.EnvironmentaProtectionActivity;
import com.yutu.car.utils.DataPickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/4/12.
 */

public class EnvironmentaProtectionFragment extends BaseFragment {
    private DataPickDialogUtil dataPickDialogUtil;
    @Bind(R.id.start_time)
    EditText startTime;
    @Bind(R.id.end_time)
    EditText endTime;
    @Bind(R.id.search_btn)
    Button searchButton;
    @Bind(R.id.rightIconA)
    ImageView rightIconA;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enpr, container, false);
        dataPickDialogUtil=new DataPickDialogUtil(getActivity());
        setTitle(view, "检测数据验证");
        ButterKnife.bind(this, view);
        searchButton.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        rightIconA.setOnClickListener(this);
        touchEvent();
        return view;
    }
    private void touchEvent(){
        startTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startTime.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });
        endTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                endTime.setInputType(InputType.TYPE_NULL);
                return false;
            }
        });


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_time:
                dataPickDialogUtil.dateTimePicKDialog(startTime);
                break;
            case R.id.end_time:
                dataPickDialogUtil.dateTimePicKDialog(endTime);
                break;
            case R.id.search_btn:
                String startT=startTime.getText().toString();
                String endT=endTime.getText().toString();
                SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd");
                try{
                    Date beginTime=CurrentTime.parse(startT);
                    Date endTime=CurrentTime.parse(endT);
                    if(endTime.getTime()<=beginTime.getTime()){
                        Toast.makeText(mAct,"输入时间有问题，请重新输入",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(startT.equals("")||endT.equals("")){
                    Toast.makeText(mAct,"输入信息不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(mAct, EnvironmentaProtectionActivity.class);
                intent.putExtra("startTime",startT);
                intent.putExtra("endTime",endT);
                startActivity(intent);
                break;
            case  R.id.rightIconA:
                showPoupMenu();
                break;
        }
    }

    public static EnvironmentaProtectionFragment newInstance(String s) {
        EnvironmentaProtectionFragment fragment = new EnvironmentaProtectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}
