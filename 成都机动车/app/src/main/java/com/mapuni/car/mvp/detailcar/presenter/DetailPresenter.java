package com.mapuni.car.mvp.detailcar.presenter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.detailcar.contract.DetailContract;
import com.mapuni.car.mvp.detailcar.model.EditValueBean;
import com.mapuni.car.mvp.detailcar.model.EditValueBean1;
import com.mapuni.car.mvp.detailcar.model.PhotoBean;
import com.mapuni.car.mvp.detailcar.model.SelectValueBean;
import com.mapuni.car.mvp.detailcar.model.SprinnerValueBean;
import com.mapuni.car.mvp.detailcar.model.TextValueBean;
import com.mapuni.car.mvp.detailcar.model.TimeValueBean;
import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.core.net.response.CarHttpResponse;
import com.mapuni.core.net.response.ChangeResponse;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;
import com.mapuni.core.utils.ImageUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by lybin on 2017/6/27.
 */

public class DetailPresenter extends DetailContract.DetailPresenter {
    private Map dataMap;
    private static Map<String, Object> uploadMap;
    private List imgParts;
    private String[] keys;
    private static MultipartBody.Builder builder;
    public static final int ADD_TYPE = 1;
    public static final int CHECK_TYPE = 2;
    private int type;
    private String title;
    private String paid;
    private String select;
    private HashMap<String, List<LoginCarTypeBean>> carMap = new HashMap<>();
    private Serializable data;
    private CarCheckBean.DataBean dataBean;
    private Context context;
    private String carCardNumber;
    private String carCardColor;
    private String carPkid;

    @Override
    public void onStart() {
        context = mView.getContext();
        dataMap = (Map) ((Activity) mView.getContext()).getIntent().getSerializableExtra("map");
        keys = (String[]) ((Activity) mView.getContext()).getIntent().getExtras().get("keys");
        title = (String) ((Activity) mView.getContext()).getIntent().getExtras().get("title");
        type = (int) ((Activity) mView.getContext()).getIntent().getIntExtra("type", 0);
        paid = (String) ((Activity) mView.getContext()).getIntent().getStringExtra("pkid");
        carCardNumber = (String) ((Activity) mView.getContext()).getIntent().getStringExtra("carCardNumber");
        carCardColor = (String) ((Activity) mView.getContext()).getIntent().getStringExtra("carCardColor");
        uploadMap = new HashMap();
        carMap = ConsTants.carMap;
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    }

    public static void setParams(String key, String value) {
        uploadMap.put(key, value);
    }

    private void setImg(String path) {
        File file = new File(path);
        uploadMap.put(PhotoItemFactory.photoKey, file);
    }

    public static void delImg(String key) {
        uploadMap.remove(key);
    }

    public void commit() {
        if (title.equals("待查验列表")) {
            commitCheckDetail();
        } else if (title.equals("可修改列表")) {
            commitChageDetail();
        }
    }

    //查看待查验列表详情
    public void lookCheckDetail() {
        HashMap map = new HashMap<>();
        map.put("pkid", paid);
        map.put("carCardNumber", carCardNumber);
        map.put("carCardColor", carCardColor);
        addSubscribe(mModel.lookCheckDetail(map)
                .compose(RxUtil.<CarHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleCarResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(Map map) {
                                mView.updataParts(map.size());
                                carPkid = (String) map.get("carPkid");
                                initRecycleBean(map);
                            }
                        }
                ));
    }

    //查看可修改详情
    public void lookReciverDetail() {
        HashMap map = new HashMap<>();
        map.put("pkid", paid);
        map.put("carCardNumber", carCardNumber);
        map.put("carCardColor", carCardColor);
        addSubscribe(mModel.lookReciverDetail(map)
                .compose(RxUtil.<CarHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleCarResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(Map map) {
                                mView.updataParts(map.size());
                                carPkid = (String) map.get("carPkid");
                                initRecycleBean(map);

                            }
                        }
                ));
    }

    //业务申请修改检测方法
    private void requestMethodDetail() {
        HashMap map = new HashMap<>();
        map.put("pkid", paid);
        addSubscribe(mModel.requestMethodDetail(map)
                .compose(RxUtil.<MyHttpResponse<Map>>rxSchedulerHelper())
                .compose(RxUtil.<Map>handleResult())
                .subscribeWith(
                        new CommonSubscriber<Map>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(Map map) {
//                                Log.e("zqq==",map.size()+"");
//                                mView.updateComplete();
                            }
                        }
                ));
    }

    //可修改详情保存
    private void commitChageDetail() {
        uploadMap.put("userId", ConsTants.UserId);
        uploadMap.put("stationPkid", ConsTants.stationId);
        uploadMap.put("regionCode", ConsTants.regionCode);
        uploadMap.put("pkid", paid);
        uploadMap.put("carPkid", carPkid);//"1512699683109_cd01"
        uploadMap.put("carCardNumber", carCardNumber);
        uploadMap.put("carCardColor", carCardColor);
        addSubscribe(mModel.chageDetail(getBody())
                .compose(RxUtil.<ChangeResponse>rxSchedulerHelper())
//                .compose(RxUtil.<Map>handleCarResult())
                .subscribeWith(
                        new CommonSubscriber<ChangeResponse>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(ChangeResponse map) {
//                                initRecycleBean(map);
                                String errInfo = (String) map.getCarInfo();
                                String isExist = map.getIsExist();
                                mView.updateComplete(errInfo, isExist);
                            }
                        }
                ));
    }

    //待查验详情保存
    private void commitCheckDetail() {
        uploadMap.put("userId", ConsTants.UserId);
        uploadMap.put("pkid", paid);
        uploadMap.put("stationPkid", ConsTants.stationId);
        uploadMap.put("regionCode", ConsTants.regionCode);
        uploadMap.put("carCardNumber", carCardNumber);
        uploadMap.put("carCardColor", carCardColor);
        uploadMap.put("carPkid", carPkid);//"1512699683109_cd01"
        addSubscribe(mModel.commitCheckDetail(getBody())
                .compose(RxUtil.<ChangeResponse>rxSchedulerHelper())
//                .compose(RxUtil.<MyHttpResponse>handleResult())
                .subscribeWith(
                        new CommonSubscriber<ChangeResponse>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }

                            @Override
                            public void onNext(ChangeResponse map) {

                                mView.updateComplete(map.getCarInfo(), map.getIsExist());
                            }
                        }
                ));
    }

    private MultipartBody getBody() {
        for (String key : uploadMap.keySet()) {
            Object obj = uploadMap.get(key);
            if (obj instanceof String) {
                builder.addFormDataPart(key, (String) obj);
            } else if (obj instanceof File) {
                builder.addFormDataPart(key, ((File) obj).getName(), RequestBody.create(MediaType.parse("image/*"), (File) obj));
            }
        }
        return builder.build();
    }

    private void initRecycleBean(Map checkMap) {
        Context context = mView.getContext();
        List list = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
//            String data = mModel.getTestData(context,keys[i]);
            String data = (String) checkMap.get(keys[i]);
            String[] values = mModel.getReciverValues(context, keys[i]);
            select = data;
            if (select != null && select.equals("2")) {
                select = "不通过";
            } else if (select != null && select.equals("1")) {
                select = "通过";
            }
            if (values != null && values.length > 1) {
                SelectValueBean bean = new SelectValueBean();
                bean.setName(name);
                bean.setSelects(values);
                bean.setValue(select);
                bean.setKey(keys[i]);
                bean.setFlag("true");
                list.add(bean);
            } else if (values != null && values.length == 1 && values[0].equals("Date")) {
                TimeValueBean bean = new TimeValueBean();
                bean.setValue(select);
                bean.setName(name);
                bean.setKey(keys[i]);
                list.add(bean);
            } else if (i == keys.length - 1) {
                PhotoBean bean = new PhotoBean();
                String[] photoKeys = mView.getContext().getResources().getStringArray(R.array.photo_keys);
                Map map = new HashMap();
                for (int index = 0; index < photoKeys.length; index++) {
                    String address = "";
                    if (checkMap.get(photoKeys[index]) != null) {
                        address = (String) checkMap.get(photoKeys[index]);
                    }
                    if (!address.equals("0") && title.equals("可修改列表")) {
                        String st1 = ConsTants.Base_Url + "/jdcapp/common/get-view-img.action?imgPath=" + address;
                        map.put(photoKeys[index], st1);
                    } else {
                        map.put(photoKeys[index], address);
                    }
                }
                bean.setPhotoAdress(map);
                list.add(bean);

            } else if (values != null && values.length == 1 && values[0].equals("Edit")) {
                EditValueBean bean = new EditValueBean();
                bean.setKey(keys[i]);
                bean.setName(name);
                bean.setValue(select);
                bean.setMaxLength(mModel.getInputType(context, keys[i]));
                list.add(bean);
            } else if (values != null && values.length == 1 && values[0].equals("Edit1")) {
                EditValueBean1 bean = new EditValueBean1();
                bean.setKey(keys[i]);
                bean.setName(name);
                bean.setValue(select);
                bean.setMaxLength(mModel.getInputType(context, keys[i]));
                list.add(bean);
            } else if (values != null && values.length == 1 && values[0].equals("Sprinner")) {
                SprinnerValueBean bean = new SprinnerValueBean();
                String s = keys[i].toLowerCase();
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
                            String nameCode = code[m];
                            bean.setNameCode(nameCode);
                        }
                    }
                }
                bean.setName(name);
                bean.setValue(select);//网络数据
                bean.setKey(keys[i]);//name
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
            //   bitmap = MediaStore.Images.Media.getBitmap(mView.getContext().getContentResolver(), uri);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = ((Activity) mView.getContext()).managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            bitmap = ImageUtil.getBitmap(img_path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (bitmap != null) {
            String path = getRealPathFromURI(uri);
            if(!TextUtils.isEmpty(path)) {
                saveBitmapFile(bitmap, path);
                setImg(path);
            }
        }
//       String path = BitmapUtil.getImageAbsolutePath((Activity) mView.getContext(),uri);
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
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = mView.getContext().getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;

        if (!TextUtils.isEmpty(contentUri.getAuthority())) {
            String res = null;
            Cursor cursor =null;
             cursor = mView.getContext().getContentResolver().query(contentUri,
                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (null == cursor) {
                Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
            } else {
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    res = cursor.getString(column_index);
                }
                cursor.close();

            }
            return res;
        }
        return "";
    }

    public String allowRequest() {
        if (title.equals("修改检测方法")) {
            String xgyy = (String) uploadMap.get("xgyy");
            if (xgyy == null || xgyy.equals("")) {
                return "信息填写不完整";
            }
        } else {
            //  String checkIdea = (String) uploadMap.get("checkIdea");
            String checkResult = (String) uploadMap.get("checkResult");
            if (checkResult == null || checkResult.equals("")) {
                return "信息填写不完整";
            }
        }
        int k = 0;
        for (int i = 0; i < com.mapuni.car.mvp.detailcar.ItemFactory.PhotoItemFactory.PhotoKeys.length; i++) {
            String key = com.mapuni.car.mvp.detailcar.ItemFactory.PhotoItemFactory.PhotoKeys[i];
            if (key.equals("enprotectImgsOne") || key.equals("enprotectImgsTwo") || key.equals("enprotectImgsThree")) {
                File file = (File) uploadMap.get(key);
                if (file == null) {
                    k++;
                }
            } else if (key.equals("vinImgs") || key.equals("beforeCarAspectImgs") || key.equals("afterCarAspectImgs")) {
                File file = (File) uploadMap.get(key);
                if (file == null) {
                    return "车身外观或VIN图片必须上传，环保装置至少上传一张";
                }
            }
        }
        if (k == 3) {
            return "车身外观或VIN图片必须上传，环保装置至少上传一张";
        }
        return "";
    }

    public String allow() {
        //   String checkIdea = (String) uploadMap.get("checkIdea");
        String checkResult = (String) uploadMap.get("checkResult");
        if (checkResult == null || checkResult.equals("")) {
            return "信息填写不完整";
        }

        return "";
    }
}
