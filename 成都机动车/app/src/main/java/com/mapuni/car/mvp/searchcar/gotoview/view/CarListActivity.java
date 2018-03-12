package com.mapuni.car.mvp.searchcar.gotoview.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.model.EditValueBean;
import com.mapuni.car.mvp.detailcar.model.SprinnerValueBean;
import com.mapuni.car.mvp.detailcar.model.TextValueBean;
import com.mapuni.car.mvp.detailcar.model.TimeValueBean;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.EditItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.SelectItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.SprinnerItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.TextItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.TimeItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.contract.CarListContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.car.mvp.searchcar.gotoview.presenter.CarListPresenter;
import com.mapuni.car.mvp.searchcar.model.CarSelectBean;
import com.mapuni.car.mvp.searchcar.model.SearchCarModel;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.core.base.CoreBaseActivity;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarListActivity extends CoreBaseActivity<CarListPresenter, SearchCarModel> implements CarListContract.CarListAvtivity {
    AssemblyRecyclerAdapter mAdapter;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_carinfo)
    ImageView ivCarinfo;
    @BindView(R.id.tv_name_carinfo)
    TextView tvNameCarinfo;
    @BindView(R.id.iv_select_dirver)
    ImageView ivSelectDirver;
    @BindView(R.id.activity_car_list)
    LinearLayout activityCarList;
    @BindView(R.id.iv_select_other)
    ImageView ivSelectOther;
    @BindView(R.id.iv_carother)
    ImageView ivCarother;
    @BindView(R.id.iv_select_owner)
    ImageView ivSelectOwner;
    @BindView(R.id.rl_driver_car)
    RelativeLayout rlDriverCar;
    @BindView(R.id.rl_other_car)
    RelativeLayout rlOtherCar;
    @BindView(R.id.rl_owner_car)
    RelativeLayout rlOwnerCar;
    @BindView(R.id.rv_car_owner)
    RecyclerView rvCarOwner;
    @BindView(R.id.rv_carlist)
    RecyclerView rvCarlist;
    @BindView(R.id.rv_car_other)
    RecyclerView rvCarOther;
    @BindView(R.id.divider)
    TextView divider;
    @BindView(R.id.searchBtn)
    Button searchBtn;
    private boolean isDriverFlag = true;
    private boolean isAther = true;
    private boolean isOwner = true;
    private String type = "dirver";
    private String nameCode;
    private String title;
    private Map data = new HashMap<>();
    private Map<String, String> xzInfomap = new HashMap<>();
    private Map<String, String> otherInfomap = new HashMap<>();
    private Map<String, String> czInfomap = new HashMap<>();
    private HashMap<String, List<LoginCarTypeBean>> carMap = new HashMap<>();
    private Map<String, String> selectInfo=new HashMap<>();
    private Map<String,String> infoMap = new HashMap<>();
    private List<String> keyList=new ArrayList<>();
    @Override
    public int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_car_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title = "车辆信息录入";
        tvTitle.setText(title);
        CarBean carBean = (CarBean) getIntent().getSerializableExtra("carBean");
        CarSelectBean carSelectBean = (CarSelectBean) getIntent().getSerializableExtra("carSelectBean");
        selectInfo = carSelectBean.getZxInfo();
        String alertinfo = carBean.getAlertinfo();
        if (!TextUtils.isEmpty(alertinfo)) {
            ToastUtils.showToast(CarListActivity.this, alertinfo);
        }
        carMap = ConsTants.carMap;
        List<Map<String, String>> xzInfo = carBean.getXszInfo();
        xzInfomap = xzInfo.get(0);
        List<Map<String, String>> otherInfo = carBean.getOtherInfo();
        otherInfomap = otherInfo.get(0);
        List<Map<String, String>> czInfo = carBean.getCzInfo();
        czInfomap = czInfo.get(0);
        if(infoMap.size()>0)
            infoMap.clear();
        if(keyList.size()>0)
            keyList.clear();
        initRecycleBean();
        initRecycleOtherBean();
        initRecycleOwnerBean();
        ivRight.setImageResource(R.mipmap.xiugai);
        ivLeft.setOnClickListener(l -> {
            finish();
        });
        rlDriverCar.setOnClickListener(l -> {
            isDriverFlag = !isDriverFlag;
            if (isDriverFlag == true) {
                rvCarlist.setVisibility(View.VISIBLE);
            } else {
                rvCarlist.setVisibility(View.GONE);
            }

        });
        rlOtherCar.setOnClickListener(l -> {
            isAther = !isAther;
            if (isAther == true) {
                rvCarOther.setVisibility(View.VISIBLE);
            } else {
                rvCarOther.setVisibility(View.GONE);
            }

        });
        rlOwnerCar.setOnClickListener(l -> {
            isOwner = !isOwner;
            if (isOwner == true) {
                rvCarOwner.setVisibility(View.VISIBLE);
            } else {
                rvCarOwner.setVisibility(View.GONE);
            }

        });
        ivRight.setOnClickListener(l -> {
            String[] keys = getResources().getStringArray(R.array.carinfoupdate);
            //从列表中获取数据
            HashMap<String, String> vaule = mPresenter.getVaule();
            Intent intent = new Intent(CarListActivity.this, CarlistReviseActivity.class);
            intent.putExtra("keys", keys);
            intent.putExtra("map", (Serializable) vaule);
            intent.putExtra("carPkid", xzInfomap.get("carpkid"));
            startActivity(intent);
            finish();
        });
        searchBtn.setOnClickListener(l -> {
            String allow = mPresenter.allowRequest(infoMap,keyList);
            if(allow.equals("")) {
                LoadingView.getInstance(CarListActivity.this).setMsg("正在上传数据...").showAtView(view);
                mPresenter.commit();
            }else{
                ToastUtils.showToast(CarListActivity.this,allow);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError(String msg) {
        LoadingView.getInstance(getContext()).dismiss();
        ToastUtils.showToast(this, msg);

    }

    @Override
    public void onLoadMore(List list) {
    }

    @Override
    public void updateComplete(String errinfo) {
        ToastUtils.showToast(this, errinfo);

        LoadingView.getInstance(this).dismiss();
        finish();
    }

    private void initRecycleBean() {
        Context context = getContext();
        String[] keys = getContext().getResources().getStringArray(R.array.cardriverinfo);
        List list = new ArrayList();
        list.clear();
        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String[] values = mModel.getValues(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
            String testData = mModel.getTestData(context, keys[i]);
            String select = xzInfomap.get(keys[i]);
            String infos = selectInfo.get(keys[i]);
            String[] split = infos.split(",");
            String flag=split[0];
            String type = split[1];
            infoMap.put(keys[i],flag);
            keyList.add(keys[i]);
            if (select == null && type != null && type.equals("Spinner")) {
                select = values[0];
            }
            if (type != null && type.equals("Spinner")) {
                SprinnerValueBean bean = new SprinnerValueBean();
                String s = keys[i];
                List<LoginCarTypeBean> carTypeBeen = carMap.get(s);
                if (carTypeBeen != null) {
                    String[] values1 = new String[carTypeBeen.size()];
                    String[] code = new String[carTypeBeen.size()];
                    for (int k = 0; k < carTypeBeen.size(); k++) {
                        values1[k] = carTypeBeen.get(k).getName();
                        code[k] = carTypeBeen.get(k).getCode();
                    }
                    bean.setSelects(values1);
                    bean.setCode(code);
                    for (int m = 0; m < values1.length; m++) {
                        if (select.equals(values1[m])) {
                            nameCode = code[m];
                            bean.setNameCode(nameCode);
                        }
                    }
                }
                bean.setName(name);
                bean.setValue(select);//网络数据
                bean.setKey(keys[i]);//name
                bean.setTitle(title);
                bean.setFlag(flag);
                list.add(bean);

            } else if (type != null&& type.equals("Date")) {
                TimeValueBean bean = new TimeValueBean();
                bean.setValue(select);
                bean.setName(name);
                bean.setKey(keys[i]);
                list.add(bean);
            } else if (type != null  && type.equals("Edit")) {
                EditValueBean bean = new EditValueBean();
                bean.setKey(keys[i]);
                bean.setName(name);
                bean.setValue(select);
                bean.setMaxLength(mModel.getInputType(context, keys[i]));
                bean.setTitle(title);
                bean.setFlag(flag);
                list.add(bean);
            } else {
                TextValueBean bean = new TextValueBean();
                List<LoginCarTypeBean> carTypeBeen = carMap.get(keys[i]);
                if (carTypeBeen != null) {
                    for (int k = 0; k < carTypeBeen.size(); k++) {
                        String name1 = carTypeBeen.get(k).getName();
                        if (name1.equals(select)) {
                            String code = carTypeBeen.get(k).getCode();
                            bean.setCode(code);
                        }
                    }
                }
                bean.setValue(select);
                bean.setName(name);
                bean.setColor(color);
                bean.setKey(keys[i]);
                list.add(bean);
            }
        }

        mAdapter = new AssemblyRecyclerAdapter(list);

        PhotoItemFactory photoFactory = new PhotoItemFactory(this);
        rvCarlist.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addItemFactory(new SelectItemFactory());
        mAdapter.addItemFactory(new SprinnerItemFactory());
        mAdapter.addItemFactory(new TextItemFactory());
        mAdapter.addItemFactory(new EditItemFactory());
        mAdapter.addItemFactory(new TimeItemFactory(this));
//        mAdapter.addItemFactory(photoFactory);
        rvCarlist.setAdapter(mAdapter);


    }

    private void initRecycleOtherBean() {
        Context context = getContext();
        String[] keys = getContext().getResources().getStringArray(R.array.carotherinfo);
        List list = new ArrayList();
        list.clear();

        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String[] values = mModel.getValues(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
            String testData = mModel.getTestData(context, keys[i]);
            String select = otherInfomap.get(keys[i]);
            String infos = selectInfo.get(keys[i]);
            String[] split = infos.split(",");
            String flag=split[0];
            String type = split[1];
            infoMap.put(keys[i],flag);
            keyList.add(keys[i]);
//            if (values != null && values.length > 1) {
//                select = values[0];
//            }
//            if (type != null && values.length > 1) {
//                SelectValueBean bean = new SelectValueBean();
//                bean.setName(name);
//                bean.setSelects(values);
//                bean.setValue(select);
//                bean.setKey(keys[i]);
//                list.add(bean);
//
//            } else
             if (type != null  && type.equals("Spinner")) {

                SprinnerValueBean bean = new SprinnerValueBean();
                String s = keys[i];
                List<LoginCarTypeBean> carTypeBeen = carMap.get(s);
                if (carTypeBeen != null) {
                    String[] values1 = new String[carTypeBeen.size()];
                    String[] code = new String[carTypeBeen.size()];
                    for (int k = 0; k < carTypeBeen.size(); k++) {
                        values1[k] = carTypeBeen.get(k).getName();
                        code[k] = carTypeBeen.get(k).getCode();
                    }
                    bean.setSelects(values1);
                    bean.setCode(code);
                    for (int m = 0; m < values1.length; m++) {
                        if (select.equals(values1[m])) {
                            nameCode = code[m];
                            bean.setNameCode(nameCode);
                        }
                    }
                }

                bean.setName(name);
//                bean.setCode(codeValues);
                bean.setValue(select);//网络数据
                bean.setKey(keys[i]);//name
                bean.setTitle(title);
                 bean.setFlag(flag);
                list.add(bean);
            } else if (type != null && type.equals("Date")) {
                TimeValueBean bean = new TimeValueBean();
                bean.setValue(select);
                bean.setName(name);
                bean.setKey(keys[i]);
                 bean.setFlag(flag);
                list.add(bean);
            } else if (type != null &&type.equals("Edit")) {
                EditValueBean bean = new EditValueBean();
                bean.setKey(keys[i]);
                bean.setName(name);
                bean.setValue(select);
                bean.setMaxLength(mModel.getInputType(context, keys[i]));
                bean.setTitle(title);
                 bean.setFlag(flag);
                list.add(bean);
            } else {
                TextValueBean bean = new TextValueBean();
                 List<LoginCarTypeBean> carTypeBeen = carMap.get(keys[i]);
                 if (carTypeBeen != null) {
                     for (int k = 0; k < carTypeBeen.size(); k++) {
                         String name1 = carTypeBeen.get(k).getName();
                         if (name1.equals(select)) {
                             String code = carTypeBeen.get(k).getCode();
                             bean.setCode(code);
                         }
                     }
                 }
                bean.setValue(select);
                bean.setName(name);
                bean.setColor(color);
                bean.setKey(keys[i]);
                list.add(bean);
            }
        }

        mAdapter = new AssemblyRecyclerAdapter(list);

        PhotoItemFactory photoFactory = new PhotoItemFactory(this);

        rvCarOther.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addItemFactory(new SelectItemFactory());
        mAdapter.addItemFactory(new TextItemFactory());
        mAdapter.addItemFactory(new EditItemFactory());
        mAdapter.addItemFactory(new TimeItemFactory(this));
        mAdapter.addItemFactory(new SprinnerItemFactory());
//        mAdapter.addItemFactory(photoFactory);
        rvCarOther.setAdapter(mAdapter);

    }

    private void initRecycleOwnerBean() {
        Context context = getContext();
        String[] keys = getContext().getResources().getStringArray(R.array.carownerinfo);
        List list = new ArrayList();
        list.clear();
        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String[] values = mModel.getValues(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
            String testData = mModel.getTestData(context, keys[i]);
            String select = czInfomap.get(keys[i]);
            String infos = selectInfo.get(keys[i]);
            String[] split = infos.split(",");
            String flag=split[0];
            String type = split[1];
            infoMap.put(keys[i],flag);
            keyList.add(keys[i]);
            if (type != null && type.equals("Spinner")) {
                SprinnerValueBean bean = new SprinnerValueBean();
                String s = keys[i];
                List<LoginCarTypeBean> carTypeBeen = carMap.get(s);
                if (carTypeBeen != null) {
                    String[] values1 = new String[carTypeBeen.size()];
                    String[] code = new String[carTypeBeen.size()];
                    for (int k = 0; k < carTypeBeen.size(); k++) {
                        values1[k] = carTypeBeen.get(k).getName();
                        code[k] = carTypeBeen.get(k).getCode();
                    }
                    bean.setCode(code);
                    bean.setSelects(values1);
                    for (int m = 0; m < values1.length; m++) {
                        if (select.equals(values1[m])) {
                            nameCode = code[m];
                            bean.setNameCode(nameCode);
                        }
                    }
                }
                bean.setName(name);
                bean.setValue(select);//网络数据
                bean.setKey(keys[i]);//name
                bean.setTitle(title);
                bean.setFlag(flag);
                list.add(bean);

            } else if (type != null && type.equals("Edit")) {
                EditValueBean bean = new EditValueBean();
                bean.setKey(keys[i]);
                bean.setName(name);
                bean.setValue(select);
                bean.setMaxLength(mModel.getInputType(context, keys[i]));
                bean.setTitle(title);
                bean.setFlag(flag);
                list.add(bean);
            } else {
                TextValueBean bean = new TextValueBean();
                List<LoginCarTypeBean> carTypeBeen = carMap.get(keys[i]);
                if (carTypeBeen != null) {
                    for (int k = 0; k < carTypeBeen.size(); k++) {
                        String name1 = carTypeBeen.get(k).getName();
                        if (name1.equals(select)) {
                            String code = carTypeBeen.get(k).getCode();
                            bean.setCode(code);
                        }
                    }
                }
                bean.setValue(select);
                bean.setName(name);
                bean.setColor(color);
                bean.setKey(keys[i]);
                list.add(bean);
            }
        }

        mAdapter = new AssemblyRecyclerAdapter(list);
        rvCarOwner.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addItemFactory(new SprinnerItemFactory());
        mAdapter.addItemFactory(new TextItemFactory());
        mAdapter.addItemFactory(new EditItemFactory());
        rvCarOwner.setAdapter(mAdapter);

    }


    @Override
    public void jumpActivity(Map data, String[] keys) {
        LoadingView.getInstance(getContext()).dismiss();
        this.data = data;
//        Intent intent = new Intent(this, CarDetailActivity.class);
//        intent.putExtra("map", (Serializable) data);
//        intent.putExtra("keys", keys);
//        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
