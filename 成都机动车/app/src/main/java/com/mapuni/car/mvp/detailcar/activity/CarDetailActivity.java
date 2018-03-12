package com.mapuni.car.mvp.detailcar.activity;

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
import android.widget.Toast;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.ItemFactory.EditItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.EditItemFactory1;
import com.mapuni.car.mvp.detailcar.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.SelectItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.SprinnerItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.TextItemFactory;
import com.mapuni.car.mvp.detailcar.ItemFactory.TimeItemFactory;
import com.mapuni.car.mvp.detailcar.contract.DetailContract;
import com.mapuni.car.mvp.detailcar.model.DetailModel;
import com.mapuni.car.mvp.detailcar.model.EventBean;
import com.mapuni.car.mvp.detailcar.presenter.DetailPresenter;
import com.mapuni.car.mvp.view.YutuLoading;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.core.base.CoreBaseActivity;
import com.mapuni.core.net.loadingview.LoadingView;
import com.mapuni.core.utils.ToastUtils;
import com.mapuni.core.widget.takephoto.photo.util.PhotoLook;
import com.melnykov.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class CarDetailActivity extends CoreBaseActivity<DetailPresenter, DetailModel> implements DetailContract.CarDetailActivity {
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
    YutuLoading yutuLoading;
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
//        toolBar.inflateMenu(R.menu.base_toolbar_menu);
        if (yutuLoading == null)
            yutuLoading = new YutuLoading(CarDetailActivity.this);
        yutuLoading.showDialog();
        title1 = getIntent().getStringExtra("title");
        if (title1.equals("待查验列表")) {
            mPresenter.lookCheckDetail();
        } else if (title1.equals("可修改列表")) {
            mPresenter.lookReciverDetail();
        }
        toolBar.setNavigationOnClickListener(l -> {
            finish();
        });
        //控制悬浮按钮显示与隐藏
//        commitBtn.attachToRecyclerView(recycle);
        commitBtn.setOnClickListener(l -> {
            if (title1.equals("待查验列表")) {
                String allow = mPresenter.allowRequest();

                if (allow.equals("")) {
                    LoadingView.getInstance(CarDetailActivity.this).setMsg("正在上传数据...").showAtView(view);
                    mPresenter.commit();
                } else {
                    ToastUtils.showToast(CarDetailActivity.this, allow);
                }
            } else if (title1.equals("可修改列表")) {
                String allow = mPresenter.allow();
                if (allow.equals("")) {
                    LoadingView.getInstance(CarDetailActivity.this).setMsg("正在上传数据...").showAtView(view);
                    mPresenter.commit();
                } else {
                    ToastUtils.showToast(CarDetailActivity.this, allow);
                }
            }
        });
//        toolBar.setOnMenuItemClickListener(
//                (MenuItem item)->{
//                    LoadingView.getInstance(CarDetailActivity.this).setMsg("正在上传数据...").showAtView(view);
//                    mPresenter.commit();
//                    return true;});
        getFragmentManager().beginTransaction();
//        InputStream is = null;
//        Map map;
//        try {
//            is = this.getResources().getAssets().open("car_config.xml");
////            map = XmlHelper.getArrayValueByName("VALUE",is);
//            map = XmlHelper.getListStyleByName("KEY_NAME",is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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
            photoFactory = new PhotoItemFactory(this);
            recycle.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.addItemFactory(new SelectItemFactory());
            mAdapter.addItemFactory(new TextItemFactory());
            mAdapter.addItemFactory(new EditItemFactory());
            mAdapter.addItemFactory(new EditItemFactory1());
            mAdapter.addItemFactory(new TimeItemFactory(this));
            mAdapter.addItemFactory(new SprinnerItemFactory());
            mAdapter.addItemFactory(photoFactory);
        }

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
//            if (data != null) {
//                Bitmap bmap2 = data.getParcelableExtra("data");
//                photoFactory.setSelectPhoto(bmap2);
//            }

//            Bitmap bmp = mPresenter.decodeUriAsBitmap(PhotoItemFactory.photoUri);
//            if(bmp!=null){
//                photoFactory.setSelectPhoto(bmp);
//            }
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
            Toast.makeText(CarDetailActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateComplete(String msg,String isExist) {
        ToastUtils.showToast(this, msg);
        LoadingView.getInstance(this).dismiss();
        if(title1.equals("待查验列表")&&isExist.equals("1")) {
            EventBus.getDefault().postSticky(new EventBean(isExist));
        }
            finish();

    }

    @Override
    public void updataParts(int size) {
        if (size == 0) {
            ToastUtils.showToast(this, "暂无数据");
            finish();
        }
        yutuLoading.dismissDialog();
    }

    @Override
    public void showError(String msg) {
        LoadingView.getInstance(this).dismiss();
        ToastUtils.showToast(this, msg);
        yutuLoading.dismissDialog();
        finish();
    }

    @Override
    public void onBackPressedSupport() {
        if (!PhotoLook.getInstance(this).dismiss()) {
            super.onBackPressedSupport();
        }
    }
}
