package com.mapuni.car.mvp.lookcar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.listcar.activity.CarCheckListinfoActivity;
import com.mapuni.car.mvp.listcar.activity.CarListinfoActivity;
import com.mapuni.car.mvp.lookcar.contract.LookCarContract;
import com.mapuni.car.mvp.lookcar.model.LookCarModel;
import com.mapuni.car.mvp.lookcar.presenter.LookCarPresenter;
import com.mapuni.core.base.CoreBaseFragment;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.CarTypeUtil;
import com.mapuni.core.utils.DateUtils;
import com.mapuni.core.utils.InputLowerToUpper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;


public class LookCarFragment extends CoreBaseFragment<LookCarPresenter, LookCarModel> implements LookCarContract.MapFragment {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.view_title1)
    View viewTitle1;
    @BindView(R.id.ll_title1)
    LinearLayout llTitle1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.view_title2)
    View viewTitle2;
    @BindView(R.id.ll_title2)
    LinearLayout llTitle2;
    @BindView(R.id.toolBar)
    LinearLayout toolBar;
    @BindView(R.id.carNum)
    EditText carNum;
    @BindView(R.id.carType)
    TextView carType;
    @BindView(R.id.addBtn)
    Button addBtn;
    Unbinder unbinder;
    @BindView(R.id.timeTv_before_look)
    TextView timeTvBeforeLook;
    @BindView(R.id.timeTv_later_look)
    TextView timeTvLaterLook;
    private int flag = 0;
    private String title = "待查验列表";
    private String[] carTypeNums;
    private String[] keys1;
    private String[] keys2;
    private Intent intent;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_look_layout;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        tvTitle1.setText("待查验列表");
        tvTitle2.setText("可修改列表");
        keys1 = getResources().getStringArray(R.array.checkdetail);
        keys2 = getResources().getStringArray(R.array.reciverdetail);
        carNum.setTransformationMethod(new InputLowerToUpper());
        carTypeNums = ConsTants.getCarColors();
        timeTvBeforeLook.setText(DateUtils.getSystemDate());
        timeTvLaterLook.setText(DateUtils.getSystemDate());
        llTitle1.setOnClickListener(l -> isShow(0));
        llTitle2.setOnClickListener(l -> isShow(1));
        timeTvBeforeLook.setOnClickListener(l -> onYearMonthDayPicker(0));
        timeTvLaterLook.setOnClickListener(l -> onYearMonthDayPicker(1));
        carType.setOnClickListener(l -> CarTypeUtil.carTypeSelect(getActivity(), carTypeNums, carType));
        addBtn.setOnClickListener(l -> {
            mPresenter.requestAddData(checkInput());
        });
//        toolBar.inflateMenu(R.menu.base_toolbar_menu);
//        toolBar.setOnMenuItemClickListener(
//                (MenuItem item)->{
//                    SpUtil.putBoolean(mContext, SpUtil.AutoType,false);
//                    startActivity(new Intent(mActivity,LoginActivity.class));
//                    mActivity.finish();
//                    return true;});
    }

    private void isShow(int i) {
        flag = i;
        if (i == 0) {
            viewTitle2.setVisibility(View.INVISIBLE);
            viewTitle1.setVisibility(View.VISIBLE);
            title = "待查验列表";
        } else {
            viewTitle2.setVisibility(View.VISIBLE);
            viewTitle1.setVisibility(View.INVISIBLE);
            title = "可修改列表";
        }
    }

    private Map checkInput() {
        String carNum = this.carNum.getText().toString().toUpperCase();
        String carType = this.carType.getText().toString();
        HashMap map = new HashMap();
        map.put("cphm", carNum);
        map.put("hpzl", "");
        return map;
    }

    @Override
    public void showError(String msg) {
//        LoadingView.getInstance(getContext()).dismiss();
//        if(msg.contains("500")){
//            ToastUtils.showToast(mContext,"登录超时");
//            startActivity(new Intent(mActivity, LoginActivity.class));
//            mActivity.finish();
//            return;
//        }
//        ToastUtils.showToast(mContext,msg);
    }

    public void onYearMonthDayPicker(int flag) {
        final DatePicker picker = new DatePicker(mActivity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(mContext, 10));
        picker.setRangeEnd(2111, 1, 1);
        picker.setRangeStart(2001, 1, 1);
        Calendar calendar = Calendar.getInstance();
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (flag == 0) {
                    timeTvBeforeLook.setText(year + "-" + month + "-" + day);
                } else {
                    timeTvLaterLook.setText(year + "-" + month + "-" + day);
                }

//                onTimePicker();
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    @Override
    public void jumpActivity(Map data, String[] keys) {
        String carCardNumber = carNum.getText().toString();
        String carCardColor = carType.getText().toString();
        String timeStart = timeTvBeforeLook.getText().toString();
        String timeEnd = timeTvLaterLook.getText().toString();
        LoadingView.getInstance(getContext()).dismiss();

        if(title.equals("待查验列表")){
            intent = new Intent(mActivity, CarListinfoActivity.class);
            intent.putExtra("keys", keys1);
        }else {
             intent = new Intent(mActivity, CarListinfoActivity.class);
            intent.putExtra("keys", keys2);
        }
        intent.putExtra("map", (Serializable) data);
        intent.putExtra("title", title);
        intent.putExtra("type", 2);
        intent.putExtra("carCardNumber", carCardNumber);
        intent.putExtra("carCardColor", carCardColor);
        intent.putExtra("timeStart", timeStart);
        intent.putExtra("timeEnd", timeEnd);
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
