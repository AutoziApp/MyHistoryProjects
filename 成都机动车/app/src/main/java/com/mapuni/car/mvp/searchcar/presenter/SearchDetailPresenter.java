package com.mapuni.car.mvp.searchcar.presenter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.model.EditValueBean;
import com.mapuni.car.mvp.detailcar.model.ImageValueBean;
import com.mapuni.car.mvp.detailcar.model.TextValueBean;
import com.mapuni.car.mvp.listcar.contract.SearchDetailContract;
import com.mapuni.car.mvp.searchcar.model.CarSearchDetailBean;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lybin on 2017/6/27.
 */

public class SearchDetailPresenter extends SearchDetailContract.DetailPresenter {
    private String[] keys;
    private int type;
    private String title;

    @Override
    public void onStart() {

        title = (String) ((Activity) mView.getContext()).getIntent().getExtras().get("title");
        type = (int) ((Activity) mView.getContext()).getIntent().getIntExtra("type", 0);
        String pkid = (String) ((Activity) mView.getContext()).getIntent().getExtras().get("pkid");
        if (type == 1) {
            keys = mView.getContext().getResources().getStringArray(R.array.change_car_info);
        } else if (type == 2) {
            keys = mView.getContext().getResources().getStringArray(R.array.jcffsqxq);
        } else if (type == 3) {
            keys = mView.getContext().getResources().getStringArray(R.array.kzjssq);
        }
        getCarDetailData(pkid, type);

    }

    public void getCarDetailData(String pkid, int type) {
        Map map = new HashMap();
        map.put("pkid", pkid);
//        ConsTants.Version = getApkVersion(context);


        addSubscribe(mModel.getCarDetailData(map, type)
                .compose(RxUtil.<CarSearchDetailBean>rxSchedulerHelper())
//                    .compose(RxUtil.<String>handleResult())
                .subscribeWith(
                        new CommonSubscriber<CarSearchDetailBean>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(CarSearchDetailBean result) {
                                if (result.getIsExist() == 1) {
                                    initRecycleBean(result.getCarInfo());
                                    mView.updateColose();
                                } else if (result.getIsExist() == 0) {
                                    mView.updateComplete(result.getErrInfo());
                                }
                            }
                        }
                ));

    }


    private void initRecycleBean(Map data) {
        Context context = mView.getContext();
        List list = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String[] values = mModel.getValues(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
            String select = "";
            if (data.get(keys[i]) != null) {
                select = (String) data.get(keys[i]);
            }
            if ("申请内容:".equals(name) || "申请原因:".equals(name)||"修改原因:".equals(name)) {
                EditValueBean bean = new EditValueBean();
                bean.setName(name);
                bean.setValue(select);
                bean.setColor(color);
                bean.setKey(keys[i]);
                list.add(bean);
            } else if ("证件照片:".equals(name)) {
                ImageValueBean bean = new ImageValueBean();
                bean.setName(name);
                bean.setValue(select);
                list.add(bean);
            } else {
                TextValueBean bean = new TextValueBean();
                bean.setValue(select);
                bean.setName(name);
                bean.setColor(color);
                bean.setKey(keys[i]);
                list.add(bean);
            }

        }
        mView.initRecycle(list);
    }

    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap;
        try {
//            bitmap = BitmapFactory.decodeStream(mView.getContext().getContentResolver().openInputStream(uri));
            bitmap = MediaStore.Images.Media.getBitmap(mView.getContext().getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (bitmap != null) {
            String path = getRealPathFromURI(uri);
            saveBitmapFile(bitmap, path);
        }
//        String path = BitmapUtil.getImageAbsolutePath((Activity) mView.getContext(),uri);
        return bitmap;
    }

    public void saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mView.getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
