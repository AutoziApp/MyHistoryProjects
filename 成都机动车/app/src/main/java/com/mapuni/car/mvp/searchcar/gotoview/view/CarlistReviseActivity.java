package com.mapuni.car.mvp.searchcar.gotoview.view;

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
import com.mapuni.car.mvp.listcar.activity.CarListinfoActivity;
import com.mapuni.car.mvp.searchcar.contract.SearchCarContract;
import com.mapuni.car.mvp.searchcar.fragment.SearchCarFragment;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.EditItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.EditItemFactory1;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.SelectItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.SprinnerItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.TextItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.TimeItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.contract.CarListReciverContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarListReciverModel;
import com.mapuni.car.mvp.searchcar.gotoview.presenter.CarListReciverPresenter;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.core.base.CoreBaseActivity;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.ToastUtils;
import com.mapuni.core.widget.takephoto.photo.util.PhotoLook;
import com.melnykov.fab.FloatingActionButton;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CarlistReviseActivity extends CoreBaseActivity<CarListReciverPresenter, CarListReciverModel> implements CarListReciverContract.CarListReciverActivity {
    @BindView(R.id.carInfoRecycle)
    RecyclerView recycle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.commitBtn)
    FloatingActionButton commitBtn;
    AssemblyRecyclerAdapter mAdapter;
    PhotoItemFactory photoFactory;

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
        toolBar.setNavigationOnClickListener(l -> {
            finish();
        });
        commitBtn.attachToRecyclerView(recycle);
        commitBtn.setOnClickListener(l -> {
            String allow = mPresenter.allowRequest();
            if (allow.equals("")) {

                LoadingView.getInstance(CarlistReviseActivity.this).setMsg("正在上传数据...").showAtView(view);
                mPresenter.commit();
            } else {
                ToastUtils.showToast(CarlistReviseActivity.this, allow);
            }
        });

        getFragmentManager().beginTransaction();

    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initRecycle(List list) {
        if (mAdapter == null) {
            mAdapter = new AssemblyRecyclerAdapter(list);
        }
        photoFactory = new PhotoItemFactory(this);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addItemFactory(new SelectItemFactory());
        mAdapter.addItemFactory(new TextItemFactory());
        mAdapter.addItemFactory(new EditItemFactory());
        mAdapter.addItemFactory(new EditItemFactory1());
        mAdapter.addItemFactory(new TimeItemFactory(this));
        mAdapter.addItemFactory(new SprinnerItemFactory());
        mAdapter.addItemFactory(photoFactory);
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
        } else if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
//            String[] proj = {MediaStore.Images.Media.DATA};
//            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            actualimagecursor.moveToFirst();
//            String img_path = actualimagecursor.getString(actual_image_column_index);
//            File file = new File(img_path);
            //  Toast.makeText(CarLi.this, uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateComplete(String msg) {
        LoadingView.getInstance(this).dismiss();
        ToastUtils.showToast(this, msg);
            finish();

    }

    @Override
    public void jumpActivity(Map map) {
        Intent intent = new Intent(CarlistReviseActivity.this, CarListinfoActivity.class);
        intent.putExtra("map", (Serializable) map);
        intent.putExtra("title", "修改检测方法");
        startActivity(intent);
    }

    @Override
    public void showError(String msg) {
        LoadingView.getInstance(this).dismiss();
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void onBackPressedSupport() {
        if (!PhotoLook.getInstance(this).dismiss()) {
            super.onBackPressedSupport();
        }
    }
}
