package com.mapuni.car.mvp.ywsearch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.listcar.activity.CarListinfoActivity;
import com.mapuni.car.mvp.lookcar.contract.LookCarContract;
import com.mapuni.car.mvp.lookcar.model.LookCarModel;
import com.mapuni.car.mvp.lookcar.presenter.LookCarPresenter;
import com.mapuni.core.base.CoreBaseFragment;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.CarTypeUtil;
import com.mapuni.core.utils.DateUtils;
import com.mapuni.core.utils.InputLowerToUpper;
import com.mapuni.core.widget.nicespinner.NiceSpinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;


public class YWSearchFragment extends CoreBaseFragment<LookCarPresenter, LookCarModel> implements LookCarContract.MapFragment {
    @BindView(R.id.carNum_yw)
    EditText carNumYw;
    @BindView(R.id.carColor_yw)
    TextView carColorYw;
    @BindView(R.id.timeTv_before_yw)
    TextView timeTvBeforeYw;
    @BindView(R.id.timeTv_later_yw)
    TextView timeTvLaterYw;
    @BindView(R.id.searchBtn_yw)
    Button searchBtnYw;
    Unbinder unbinder;
    @BindView(R.id.portSpinner)
    NiceSpinner portSpinner;
    @BindView(R.id.toolBar)
    Toolbar toolBar;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_ywsearch_layout;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        List<String> titles = new ArrayList<>();
        titles.add("修改车辆信息");
        titles.add("跨站检测解锁");
        titles.add("修改检测方法");
        carNumYw.setTransformationMethod(new InputLowerToUpper());
        portSpinner.attachDataSource(titles);
        String[] carColor = ConsTants.getCarColors();
        carColorYw.setOnClickListener(l -> CarTypeUtil.carTypeSelect(getActivity(), carColor, carColorYw));
        timeTvBeforeYw.setText(DateUtils.getSystemDate());
        timeTvLaterYw.setText(DateUtils.getSystemDate());
        timeTvBeforeYw.setOnClickListener(l -> onYearMonthDayPicker(0));
        timeTvLaterYw.setOnClickListener(l -> onYearMonthDayPicker(1));
        searchBtnYw.setOnClickListener(l -> mPresenter.requestAddData(checkInput()));
    }

    @Override
    public void showError(String msg) {
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
                    timeTvBeforeYw.setText(year + "-" + month + "-" + day);
                } else {
                    timeTvLaterYw.setText(year + "-" + month + "-" + day);
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

    public void onTimePicker() {
        TimePicker picker = new TimePicker(mActivity, TimePicker.HOUR_24);
        picker.setUseWeight(true);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
            }
        });
        picker.show();
    }

    private Map checkInput() {
        String carNum = this.carNumYw.getText().toString().toUpperCase();
        String carType = this.carColorYw.getText().toString();
        HashMap map = new HashMap();
        map.put("cphm", carNum);
        map.put("hpzl", "");
        return map;
    }

    @Override
    public void jumpActivity(Map data, String[] keys) {
        LoadingView.getInstance(getContext()).dismiss();
        String carCardNumber = carNumYw.getText().toString();
        String carCardColor = carColorYw.getText().toString();
        String timeStart = timeTvBeforeYw.getText().toString();
        String timeEnd = timeTvLaterYw.getText().toString();
        Intent intent = new Intent(mActivity, CarListinfoActivity.class);
        intent.putExtra("map", (Serializable) data);
        intent.putExtra("keys", keys);
        intent.putExtra("title", portSpinner.getText().toString());
        intent.putExtra("type", 4);
        intent.putExtra("carCardNumber", carCardNumber);
        intent.putExtra("carCardColor", carCardColor);
        intent.putExtra("timeStart", timeStart);
        intent.putExtra("timeEnd", timeEnd);
        startActivity(intent);
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
