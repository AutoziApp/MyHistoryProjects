package com.mapuni.car.mvp.ywrequest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.model.EventBean;
import com.mapuni.car.mvp.ywrequest.ItemFactory.BigEditItemFactory;
import com.mapuni.car.mvp.ywrequest.ItemFactory.EditItemFactory;
import com.mapuni.car.mvp.ywrequest.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.ywrequest.ItemFactory.SprinnerItemFactory;
import com.mapuni.car.mvp.ywrequest.ItemFactory.TextItemFactory;
import com.mapuni.car.mvp.ywrequest.contract.DetailContract;
import com.mapuni.car.mvp.ywrequest.model.DetailModel;
import com.mapuni.car.mvp.ywrequest.presenter.DetailPresenter;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.core.base.CoreBaseActivity;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.ToastUtils;
import com.mapuni.core.widget.takephoto.photo.util.PhotoLook;
import com.melnykov.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class CarRequestDetailActivity extends CoreBaseActivity<DetailPresenter,DetailModel>implements DetailContract.CarDetailActivity {
    @BindView(R.id.carInfoRecycle)
    RecyclerView recycle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.commitBtn)
    FloatingActionButton commitBtn;
    AssemblyRecyclerAdapter mAdapter;
    public PhotoItemFactory photoFactory;
    private String title1;

    @Override
    public int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_car_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolBar.setNavigationOnClickListener(l->{finish();});
       // commitBtn.attachToRecyclerView(recycle);
        commitBtn.setOnClickListener(l->{
            String allow = mPresenter.allowRequest();
            if(allow.equals("")){
                LoadingView loadingView = LoadingView.getInstance((CarRequestDetailActivity.this));
                loadingView.setMsg("正在上传数据...");
                loadingView .showAtView(view);
                if(mPresenter.title.equals("修改检测方法")){
                    mPresenter.commitMethod();
                }else{
                    mPresenter.commitDetail();
                }

            }else{
                ToastUtils.showToast(CarRequestDetailActivity.this,allow);
            }
        });
        getFragmentManager().beginTransaction();
        title1 = (String) getIntent().getExtras().get("title");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initRecycle(List list) {
        int  type = getIntent().getIntExtra("type",0);
        if(mAdapter==null){
            mAdapter = new AssemblyRecyclerAdapter(list);
        }
//        if(title.equals("跨站检测解锁")||title.equals("修改检测方法")&&type==3){
//            recycle.setLayoutManager(new LinearLayoutManager(this));
//            mAdapter.addItemFactory(new SelectItemFactory());
//            mAdapter.addItemFactory(new TextItemFactory());
//            mAdapter.addItemFactory(new EditItemFactory1());
//            mAdapter.addItemFactory(new FileFactory(this));
//        }else {
            photoFactory  = new PhotoItemFactory(this);
            recycle.setLayoutManager(new LinearLayoutManager(this));
//            mAdapter.addItemFactory(new SelectItemFactory());
            mAdapter.addItemFactory(new TextItemFactory());
            mAdapter.addItemFactory(new EditItemFactory());
            mAdapter.addItemFactory(new BigEditItemFactory());
            mAdapter.addItemFactory(new SprinnerItemFactory());
            mAdapter.addItemFactory(photoFactory);
     //   }
        recycle.setAdapter(mAdapter);
    }

    @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoItemFactory.PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                mPresenter.decodeUriAsBitmap(uri);
                photoFactory.setSelectPhoto(mPresenter.getRealPathFromURI(uri));
            }
        } else if (requestCode == PhotoItemFactory.TAKE_PICTURE) {
            // 从相机返回的数据
            mPresenter.decodeUriAsBitmap(PhotoItemFactory.photoUri);
            photoFactory.setSelectPhoto(mPresenter.getRealPathFromURI(PhotoItemFactory.photoUri));
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void updateComplete(String msg,String isExist) {
        ToastUtils.showToast(this,msg);
        LoadingView.getInstance(this).dismiss();
        if(title1.equals("修改检测方法")&&isExist!=null&&isExist.equals("1")){
            EventBus.getDefault().postSticky(new EventBean(isExist));
        }
        finish();
    }

    @Override
    public void showError(String msg) {
        LoadingView.getInstance(this).dismiss();
        ToastUtils.showToast(this,msg);
    }

    @Override
    public void onBackPressedSupport() {
        if(!PhotoLook.getInstance(this).dismiss()){
            super.onBackPressedSupport();
        }
    }
}
