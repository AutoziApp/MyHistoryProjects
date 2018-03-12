package com.mapuni.car.mvp.ywrequest.fragment;

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
import com.mapuni.car.mvp.listcar.activity.CarListinfoActivity;
import com.mapuni.car.mvp.ywrequest.activity.CarRequestDetailActivity;
import com.mapuni.car.mvp.ywrequest.contract.RequestContract;
import com.mapuni.car.mvp.ywrequest.model.RequestModel;
import com.mapuni.car.mvp.ywrequest.presenter.RequestPresenter;
import com.mapuni.core.base.CoreBaseFragment;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.CarTypeUtil;
import com.mapuni.core.utils.DateUtils;
import com.mapuni.core.utils.InputLowerToUpper;
import com.mapuni.core.utils.ToastUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;


public class RequestFragment extends CoreBaseFragment<RequestPresenter, RequestModel> implements RequestContract.RequestFragment {
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
    @BindView(R.id.carVinNum)
    EditText carVinNum;
    @BindView(R.id.addBtn)
    Button addBtn;
    Unbinder unbinder;
    @BindView(R.id.ll_debock_requst)
    LinearLayout llDebockRequst;
    @BindView(R.id.timeTv_before_rq)
    TextView timeTvBeforeRq;
    @BindView(R.id.timeTv_later_rq)
    TextView timeTvLaterRq;
    @BindView(R.id.ll_revice_request)
    LinearLayout llReviceRequest;
    private int flag = 0;
    private String title = "跨站检测解锁";
    String[] keys;
    private String[] carColors;
    private LoadingView loadingView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_request_layout;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        loadingView = LoadingView.getInstance(getContext());
        carColors = ConsTants.getCarColors();
        if (carColors != null && carColors.length > 0) {
            carType.setText(carColors[0]);
        }
        //全部转换成大写
        carNum.setTransformationMethod(new InputLowerToUpper());
        carVinNum.setTransformationMethod(new InputLowerToUpper());
        carNum.setText("川A");
        //设置光标位置
        carNum.setSelection(carNum.getText().toString().length());
        carType.setOnClickListener(l -> CarTypeUtil.carTypeSelect(getActivity(), carColors, carType));
        llTitle1.setOnClickListener(l -> isShow(0));
        llTitle2.setOnClickListener(l -> isShow(1));
        timeTvBeforeRq.setText(DateUtils.getSystemDate());
        timeTvLaterRq.setText(DateUtils.getSystemDate());
        timeTvBeforeRq.setOnClickListener(l -> onYearMonthDayPicker(0));
        timeTvLaterRq.setOnClickListener(l -> onYearMonthDayPicker(1));
        addBtn.setOnClickListener(l -> {
            if (checkInput() != null && flag == 0) {
                if (loadingView != null)
                    loadingView.showAtView(view);
                mPresenter.getCarInfo(checkInput());
//                String[] keys =mActivity.getResources().getStringArray(R.array.keys);
//                jumpActivity(checkInput(),keys);
            } else if (checkInput() != null && flag == 1) {
                //修改检测方法
//                ToastUtils.showToast(mContext,"开发中");
//                Intent intent = new Intent(mActivity,CarListinfoActivity.class);
//                intent.putExtra("params",checkInput());
//                startActivity(intent);
                jumpActivity(checkInput());
            } else {
                ToastUtils.showToast(mContext, "请输入完整信息");
            }
        });
    }

    private void isShow(int i) {
        flag = i;
        if (flag == 0) {
            carNum.setText("川A");
            if (carColors != null && carColors.length > 0) {
                carType.setText(carColors[0]);
            }
        } else {
            carNum.setText("");
            carType.setText("");
        }
        carNum.setSelection(carNum.getText().toString().length());
        if (i == 0) {
            viewTitle2.setVisibility(View.INVISIBLE);
            viewTitle1.setVisibility(View.VISIBLE);
            llDebockRequst.setVisibility(View.VISIBLE);
            llReviceRequest.setVisibility(View.GONE);
            title = "跨站检测解锁";
        } else {
            viewTitle2.setVisibility(View.VISIBLE);
            viewTitle1.setVisibility(View.INVISIBLE);
            llDebockRequst.setVisibility(View.GONE);
            llReviceRequest.setVisibility(View.VISIBLE);
            title = "修改检测方法";
        }
    }


    @Override
    public void showError(String msg) {
        if (loadingView != null)
            loadingView.dismiss();
        ToastUtils.showToast(mContext, msg);
    }

    private HashMap checkInput() {
        //车牌号码
        String carNum = this.carNum.getText().toString().toUpperCase();
        //车牌颜色
        String carType = this.carType.getText().toString();

        String carVin = "";

        String startTime = "";

        String endTime = "";
        //车牌vin
//        if(!carNum.isEmpty()&&carNum.length()==7){
        HashMap map = new HashMap();
        map.put("cphm", carNum);
        map.put("hpzl", ConsTants.getCarColorMap().get(carType));
        if (flag == 0) {
            carVin = this.carVinNum.getText().toString().toUpperCase();
            if (carType.isEmpty() || carNum.isEmpty() || carVin.isEmpty()) {
                return null;
            }
            map.put("vin", carVin);
        } else if (flag == 1) {
            startTime = this.timeTvBeforeRq.getText().toString();
            endTime = this.timeTvLaterRq.getText().toString();
            if (startTime.isEmpty() || endTime.isEmpty()) {
                return null;
            }
            map.put("timeStart", startTime);
            map.put("timeEnd", endTime);
        }

        return map;
//        }
//        return null;
    }

    @Override
    public void jumpActivity(Map data) {
        if (loadingView != null)
            loadingView.dismiss();
        if (flag == 0) {
            keys = getResources().getStringArray(R.array.lockdata);
            Intent intent = new Intent(mActivity, CarRequestDetailActivity.class);
            intent.putExtra("map", (Serializable) data);
            intent.putExtra("keys", keys);
            intent.putExtra("title", title);
            intent.putExtra("type", 3);
            startActivity(intent);
        } else {
            keys = getResources().getStringArray(R.array.data);
//            for(int i=0;i<keys.length;)
            String carCardNumber = carNum.getText().toString();
            String carCardColor = carType.getText().toString();
            String timeStart = timeTvBeforeRq.getText().toString();
            String timeEnd = timeTvLaterRq.getText().toString();
            Intent intent = new Intent(mActivity, CarListinfoActivity.class);
            intent.putExtra("map", (Serializable) data);
            intent.putExtra("keys", keys);
            intent.putExtra("title", title);
            intent.putExtra("type", 3);
            intent.putExtra("carCardNumber", carCardNumber);
            intent.putExtra("carCardColor", carCardColor);
            intent.putExtra("timeStart", timeStart);
            intent.putExtra("timeEnd", timeEnd);
            startActivity(intent);
        }
        //    }

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
                    timeTvBeforeRq.setText(year + "-" + month + "-" + day);
                } else {
                    timeTvLaterRq.setText(year + "-" + month + "-" + day);
                }

                //  onTimePicker();
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
                timeTvBeforeRq.append(" " + hour + ":" + minute);
            }
        });
        picker.show();
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
