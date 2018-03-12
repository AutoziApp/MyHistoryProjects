package com.mapuni.car.mvp.searchcar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.ItemFactory.EditItemFactory2;
import com.mapuni.car.mvp.detailcar.ItemFactory.ImageItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.TextItemFactory;
import com.mapuni.car.mvp.listcar.contract.SearchDetailContract;
import com.mapuni.car.mvp.searchcar.model.SearchDetailModel;
import com.mapuni.car.mvp.searchcar.presenter.SearchDetailPresenter;
import com.mapuni.car.mvp.view.YutuLoading;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.core.base.CoreBaseActivity;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.ToastUtils;
import com.mapuni.core.widget.takephoto.photo.util.PhotoLook;

import java.util.List;

import butterknife.BindView;

public class CarSearchDetailActivity extends CoreBaseActivity<SearchDetailPresenter, SearchDetailModel> implements SearchDetailContract.CarSearchDetailActivity {
    @BindView(R.id.carInfoRecycle)
    RecyclerView recycle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    AssemblyRecyclerAdapter mAdapter;
    PhotoItemFactory photoFactory;
    YutuLoading yutuLoading;

    @Override
    public int getLayoutId() {
        return R.layout.activity_car_search_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolBar.setNavigationOnClickListener(l -> {
            finish();
        });
        if (yutuLoading == null) ;
        yutuLoading = new YutuLoading(CarSearchDetailActivity.this);
        yutuLoading.showDialog();

    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initRecycle(List list) {
        String title = getIntent().getStringExtra("title");
        int type = getIntent().getIntExtra("type", 0);
        if (mAdapter == null) {
            mAdapter = new AssemblyRecyclerAdapter(list);
        }
        recycle.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addItemFactory(new TextItemFactory());
        mAdapter.addItemFactory(new EditItemFactory2());
        mAdapter.addItemFactory(new ImageItemFactory());
        recycle.setAdapter(mAdapter);
    }

    @Override
    public void updateComplete(String msg) {
        yutuLoading.dismissDialog();
        ToastUtils.showToast(CarSearchDetailActivity.this, msg);
        finish();
    }

    @Override
    public void updateColose() {
        yutuLoading.dismissDialog();
    }


    @Override
    public void showError(String msg) {
        LoadingView.getInstance(this).dismiss();
        ToastUtils.showToast(this, msg);
        yutuLoading.dismissDialog();
    }

    @Override
    public void onBackPressedSupport() {
        if (!PhotoLook.getInstance(this).dismiss()) {
            super.onBackPressedSupport();
        }
    }
}
