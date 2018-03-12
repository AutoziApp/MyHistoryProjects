package com.mapuni.car.mvp.ywrequest.presenter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.ywrequest.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.ywrequest.contract.DetailContract;
import com.mapuni.car.mvp.ywrequest.model.BigEditValueBean;
import com.mapuni.car.mvp.ywrequest.model.CheckMethodValue;
import com.mapuni.car.mvp.ywrequest.model.EditValueBean;
import com.mapuni.car.mvp.ywrequest.model.PhotoBean;
import com.mapuni.car.mvp.ywrequest.model.SelectValueBean;
import com.mapuni.car.mvp.ywrequest.model.SprinnerValueBean;
import com.mapuni.car.mvp.ywrequest.model.TextValueBean;
import com.mapuni.car.mvp.ywrequest.model.TimeValueBean;
import com.mapuni.core.net.response.RequestHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;
import com.mapuni.core.utils.ImageUtil;
import com.mapuni.core.utils.TelUtils;
import com.mapuni.core.widget.takephoto.photo.util.BitmapUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private  Map dataMap;
    private  static Map<String,Object> uploadMap;
    private String[] keys;
    private static MultipartBody.Builder builder;
    public static final int ADD_TYPE = 1;
    public static final int CHECK_TYPE = 2;
    public String title;
    private String select ;
    private HashMap<String, List<LoginCarTypeBean>> carMap=new HashMap<>();
    private String data;

    @Override
    public void onStart() {
        dataMap=(Map) ((Activity)mView.getContext()).getIntent().getSerializableExtra("map");
        keys = (String[]) ((Activity)mView.getContext()).getIntent().getExtras().get("keys");
        title =  (String) ((Activity)mView.getContext()).getIntent().getExtras().get("title");
        uploadMap = new HashMap();
        carMap = ConsTants.carMap;
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        initRecycleBean(dataMap);
//        lookCheckDetail();
    }
    public static void setParams(String key,String value){
        uploadMap.put(key, value);
    }
    private void setImg(String path){
        File file = new File(path);
        uploadMap.put(PhotoItemFactory.photoKey,file);
    }
    public static void delImg(String key){
        uploadMap.remove(key);
    }
    public void commitDetail(){
        uploadMap.put("regionCode",ConsTants.regionCode);
        uploadMap.put("userId",ConsTants.UserId);
        uploadMap.put("stationPkid",ConsTants.stationId);
        uploadMap.putAll(dataMap);
        addSubscribe(mModel.commitUnlockData(getBody())
                .compose(RxUtil.<RequestHttpResponse>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<RequestHttpResponse>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(RequestHttpResponse  map) {
                                Log.i("Lybin",map.toString());
                                mView.updateComplete(map.getErrInfo(),map.getIsExist());
//                                String color = (String) map.get("carCardColor");
//                                String colorName = ConsTants.getCarColorMap().get(color);
//                                map.put("carCardColor",colorName);
//                                initRecycleBean(map);
                            }}
                ));
    }
    public void commitMethod(){
        uploadMap.put("regionCode",ConsTants.regionCode);
        uploadMap.put("userId",ConsTants.UserId);
        uploadMap.put("stationPkid",ConsTants.stationId);
        uploadMap.putAll(dataMap);
        addSubscribe(mModel.commitMethod(getBody())
                .compose(RxUtil.<RequestHttpResponse>rxSchedulerHelper())
                .subscribeWith(
                        new CommonSubscriber<RequestHttpResponse>(mView) {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                            }
                            @Override
                            public void onNext(RequestHttpResponse  map) {
                                Log.i("Lybin",map.toString());
                                mView.updateComplete(map.getErrInfo(),map.getIsSuccess());
//                                String color = (String) map.get("carCardColor");
//                                String colorName = ConsTants.getCarColorMap().get(color);
//                                map.put("carCardColor",colorName);
//                                initRecycleBean(map);
                            }}
                ));
    }
    private MultipartBody getBody(){
        for (String key:uploadMap.keySet()){
            Object obj = uploadMap.get(key);
            if(obj instanceof String){
                builder.addFormDataPart(key, (String)obj);
                Log.i("Lybin",key+"------"+(String)obj);
            }else if(obj instanceof File){
                builder.addFormDataPart(key, ((File)obj).getName(), RequestBody.create(MediaType.parse("image/*"), (File)obj));
                Log.i("Lybin",((File) obj).getName()+"------size:"+BitmapUtil.getSize((File) obj));
            }
        }
        return  builder.build();
    }
    public String allowRequest() {
        if (title.equals("修改检测方法")) {
            String xgyy = (String) uploadMap.get("xgyy");
            if (xgyy == null || xgyy.equals("")) {
                return "请填写修改原因";
            }
        } else {
            String applyReason = (String) uploadMap.get("applyReason");
            String telephone = (String) uploadMap.get("telephone");
            if (applyReason == null || applyReason.equals("") || telephone.equals("") || telephone == null) {
                return "信息填写不完整";
            }
            if (telephone != null) {
                boolean mobile = TelUtils.isMobile(telephone);
                if (mobile == false) {
                    return "请输入正确的手机号码";
                }
            }
        }
        if (title.equals("修改检测方法")) {
            int k = 0;
            for (int i = 0; i < PhotoItemFactory.PhotoKeys.length; i++) {
                String key = PhotoItemFactory.PhotoKeys[i];
                File file = (File) uploadMap.get(key);
                if (file == null) {
                    k++;
                }
            }
            if (k == 3) {
                return "至少上传一张";
            }
            return "";
        }else {
            for (int i = 0; i < PhotoItemFactory.PhotoKeys.length; i++) {
                String key = PhotoItemFactory.PhotoKeys[i];
                File file = (File) uploadMap.get(key);
                if (file == null) {
                    return "请上传照片";
                }
            }
            return "";
        }
    }


    private void initRecycleBean(Map checkMap) {
        Context context = mView.getContext();
        List list = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
//            String data = mModel.getTestData(context,keys[i]);
               select = (String) checkMap.get(keys[i]);
            if(keys[i].equals("oldCheckMethod")){
                data = select;
            }
                String[] values = mModel.getValues(context, keys[i]);
                if (values != null && values.length > 1) {
                    select = values[0];
                }

                    if (values != null && values.length > 1) {
                        SelectValueBean bean = new SelectValueBean();
                        bean.setName(name);
                        bean.setSelects(values);
                        bean.setValue(select);
                        bean.setKey(keys[i]);
                        list.add(bean);
                    }//禁止修改车牌颜色
                    else if (values != null && values.length == 1 && values[0].equals("Sprinner")
                            &&!keys[i].equals("carCardColor")) {
                        SprinnerValueBean bean = new SprinnerValueBean();
                        String s = keys[i].toLowerCase();
                        //车牌颜色key修改与车辆下拉列表的key一致
                        if(s.equals("newcheckmethod")){
                            s="checkmethod";
                        }
                        List<LoginCarTypeBean> carTypeBeen = carMap.get(s);
                            if (carTypeBeen != null) {
                                //检测方法一一对应
                                if("checkmethod".equals(s)){
                                    HashMap<String, String[]> checkmethodValues = CheckMethodValue.getValues(data);
                                    String[] values1 = checkmethodValues.get("names");
                                    String[] code = checkmethodValues.get("codes");
                                    bean.setSelects(values1);
                                    bean.setCode(code);
                                }else {
                                String[] values1 = new String[carTypeBeen.size()];
                                String[] code = new String[carTypeBeen.size()];
                                for (int k = 0; k < carTypeBeen.size(); k++) {
                                    values1[k] = carTypeBeen.get(k).getName();
                                    code[k] = carTypeBeen.get(k).getCode();
                                }
                                bean.setSelects(values1);
                                bean.setCode(code);
                                for (int m = 0; m < values1.length; m++) {
                                    if (select != null && select.equals(values1[m])) {
                                        String nameCode = code[m];
                                        bean.setNameCode(nameCode);
                                    }
                                }
                            }
                        }
                        bean.setName(name);
                        bean.setValue(select);//网络数据
                        bean.setKey(keys[i]);//name
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
                        String[] photoKeys = mView.getContext().getResources().getStringArray(R.array.photo_Lock);
                        Map map = new HashMap();
                        for (int index = 0; index < photoKeys.length; index++) {
                            String address = "";
                            if (dataMap.get(photoKeys[index]) != null) {
                                address = (String) dataMap.get(photoKeys[index]);
                            }
                            if (address != null) {
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
                    }//BigEdit
                    else if(values != null && values.length == 1 && values[0].equals("BigEdit")){
                        BigEditValueBean bean = new BigEditValueBean();
                        bean.setKey(keys[i]);
                        bean.setName(name);
                        bean.setValue(select);
                        bean.setMaxLength(mModel.getInputType(context, keys[i]));
                        bean.setFlag("true");
                        list.add(bean);
                    }else {
                        TextValueBean bean = new TextValueBean();
                        bean.setValue(select);
                        bean.setName(name);
                        bean.setColor(color);
                        bean.setKey(keys[i]);
                        list.add(bean);
                    }
                }
//        }
        mView.initRecycle(list);
        if(title.equals("修改检测方法")){
            mView.photoFactory.setphotoName(new String[]{"证明1","证明2","证明3"});

            mView.photoFactory.setphotoKeys(new String[]{"applyImgs1","applyImgs2","applyImgs3"}) ;
        }
    }
    public Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap;
        try {
//            bitmap = BitmapFactory.decodeStream(mView.getContext().getContentResolver().openInputStream(uri));
//            bitmap = MediaStore.Images.Media.getBitmap(mView.getContext().getContentResolver(), uri);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = ((Activity)mView.getContext()).managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            bitmap = ImageUtil.getBitmap(img_path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(bitmap!=null){
            String path = getRealPathFromURI(uri);
            saveBitmapFile(bitmap,path);
            setImg(path);
        }
        //String path = BitmapUtil.getImageAbsolutePath((Activity) mView.getContext(),uri);
        return bitmap;
    }

    public void saveBitmapFile(Bitmap bitmap,String path){
        File file=new File(path);//将要保存图片的路径
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
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = mView.getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }




}
