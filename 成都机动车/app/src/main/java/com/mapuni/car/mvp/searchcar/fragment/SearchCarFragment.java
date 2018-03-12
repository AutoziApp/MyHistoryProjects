package com.mapuni.car.mvp.searchcar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.searchcar.contract.SearchCarContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.car.mvp.searchcar.gotoview.view.CarListActivity;
import com.mapuni.car.mvp.searchcar.model.CarSelectBean;
import com.mapuni.car.mvp.searchcar.model.SearchCarModel;
import com.mapuni.car.mvp.searchcar.presenter.SearchCarPresenter;
import com.mapuni.car.mvp.view.YutuLoading;
import com.mapuni.core.base.CoreBaseFragment;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.CarTypeUtil;
import com.mapuni.core.utils.InputLowerToUpper;
import com.mapuni.core.utils.ToastUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/** * Created by yawei on 2017/6/27.
 */

public class SearchCarFragment extends CoreBaseFragment<SearchCarPresenter, SearchCarModel> implements SearchCarContract.SearchCarFragment {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.carType)
    TextView carType;
    @BindView(R.id.carNum)
    EditText carNum;
    @BindView(R.id.searchBtn)
    Button searchBtn;
    @BindView(R.id.carColor)
    TextView carColor;
    Unbinder unbinder;
    @BindView(R.id.carVin)
    EditText carVin;
    String[] carColors;
    private String checkMode;
    LinkedHashMap map = new LinkedHashMap();
    YutuLoading yutuLoading;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_layout;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
           if(yutuLoading==null)
               yutuLoading=new YutuLoading(getContext());
        carNum.setTransformationMethod(new InputLowerToUpper());
        carVin.setTransformationMethod(new InputLowerToUpper());
        String[] carColors = ConsTants.getCarColors();
        String[] carCheckMode = ConsTants.getCarCheckMode();
        if(carCheckMode!=null&&carCheckMode.length>0) {
            carType.setOnClickListener(l -> CarTypeUtil.carTypeSelect(getActivity(), carCheckMode, carType));
            try {
                carType.setText(carCheckMode[2]);
            } catch (Exception e) {

            }
        }
        if(carColors!=null&&carColors.length>0) {
            carColor.setOnClickListener(l -> CarTypeUtil.carTypeSelect(getActivity(), carColors, carColor));
            try {
                carColor.setText(carColors[0]);
            } catch (Exception e) {

            }
        }

        //设置光标位置
        carNum.setSelection(carNum.getText().toString().length());
        searchBtn.setOnClickListener(
                l -> {
//                   LoadingView.getInstance(mActivity).showAtView(view);
                    String clhp = carNum.getText().toString().toUpperCase();
                    String jcms = carType.getText().toString();
                    String vin = carVin.getText().toString().toUpperCase();
                    String color = carColor.getText().toString();
                    //根据颜色获取颜色code
                    LinkedHashMap<String, String> carColorMap = ConsTants.getCarColorMap();
                    LinkedHashMap<String, String> carCheckMap = ConsTants.getCarCheckMap();
//        String code = HashmapUtils.getKey(carColorMap, color);
                    checkMode = carCheckMap.get(jcms);
                    if (!TextUtils.isEmpty(clhp) && !TextUtils.isEmpty(vin)) {
                        map.put("userId", ConsTants.UserId);
                        map.put("stationPkid", ConsTants.stationId);
                        map.put("regionCode", ConsTants.regionCode);
                        map.put("carCardNumber", clhp);
                        map.put("carCardColor", carColorMap.get(color));
                        map.put("VIN", vin);
                        map.put("checkMode", carCheckMap.get(jcms));
                       // LoadingView.getInstance(getContext()).showAtView(view);
                        yutuLoading.showDialog();
                        mPresenter.requestSelect(map);

                    }
                    if (TextUtils.isEmpty(clhp)) {
                        ToastUtils.showToast(getActivity(), "请输入车牌号");
                        return;
                    }
                    if (TextUtils.isEmpty(vin)) {
                        ToastUtils.showToast(getActivity(), "请输入VIN后四位");
                        return;
                    }
//                    Intent intent = new Intent(mActivity, CarListActivity.class);
//                    mActivity.startActivity(intent);

                });
//        toolBar.inflateMenu(R.menu.base_toolbar_menu);
//        toolBar.setOnMenuItemClickListener(
//                (MenuItem item)->{
//                    SpUtil.putBoolean(mContext, SpUtil.AutoType,false);
//                    startActivity(new Intent(mActivity,LoginActivity.class));
//                    mActivity.finish();
//                    return true;});

    }


    @Override
    public void showError(String msg) {
        yutuLoading.dismissDialog();
       // LoadingView.getInstance(getContext()).dismiss();
//        if(msg.contains("500")){
//            ToastUtils.showToast(mContext,"登录超时");
//            startActivity(new Intent(mActivity, LoginActivity.class));
//            mActivity.finish();
//            return;
//        }
        if (!TextUtils.isEmpty(msg)) {
            if (msg.contains("Failed")) {
                ToastUtils.showToast(mContext, "网络状态不佳");
                return;
            }
            ToastUtils.showToast(mContext, msg);
        }
    }

    @Override
    public void updateComplete(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showToast(getContext(), msg);
           // LoadingView.getInstance(getContext()).dismiss();
            yutuLoading.dismissDialog();
        }
    }

    @Override
    public void startCarListActivity(CarBean carBean,CarSelectBean carSelectBean) {
        yutuLoading.dismissDialog();
      //  LoadingView.getInstance(getContext()).dismiss();
//        if (model.getCarListData() == null || model.getCarListData().size() == 0) {
//            ToastUtils.showToast(mActivity, "无检测数据");
//            return;
//        }
        Map<String, String> xsInfo = carSelectBean.getZxInfo();
        if (carBean.getIsExist() == 1) {
            Intent intent = new Intent(mActivity, CarListActivity.class);
            intent.putExtra("carBean", (Serializable) carBean);
            intent.putExtra("carSelectBean", (Serializable) carSelectBean);
            intent.putExtra("checkMode", checkMode);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void initRecycle(List list) {

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
