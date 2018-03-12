package com.mapuni.car.mvp.searchcar.gotoview.presenter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.model.EditValueBean;
import com.mapuni.car.mvp.detailcar.model.EditValueBean1;
import com.mapuni.car.mvp.detailcar.model.PhotoBean;
import com.mapuni.car.mvp.detailcar.model.SelectValueBean;
import com.mapuni.car.mvp.detailcar.model.SprinnerValueBean;
import com.mapuni.car.mvp.detailcar.model.TextValueBean;
import com.mapuni.car.mvp.detailcar.model.TimeValueBean;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.searchcar.gotoview.contract.CarListReciverContract;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;
import com.mapuni.core.utils.ImageUtil;
import com.mapuni.core.utils.SpUtil;
import com.mapuni.core.utils.TelUtils;
import com.mapuni.core.utils.ToastUtils;
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

public class CarListReciverPresenter extends CarListReciverContract.CarListReciverPresenter {
    private  Map dataMap;
    private  static Map<String,Object> uploadMap=new HashMap();;
    private  List imgParts;
    private String[] keys;
    private static MultipartBody.Builder builder;
    public static final int ADD_TYPE = 1;
    public static final int CHECK_TYPE = 2;
    private int  type;
    private String title;
    private String paid;
    private String select;
    private HashMap<String, List<LoginCarTypeBean>> carMap=new HashMap<>();
    private Context context;
    private String carPkid;
    private String carcardnumber;
    private int i;

    @Override
    public void onStart() {
        context = mView.getContext();
        dataMap=(Map) ((Activity)mView.getContext()).getIntent().getSerializableExtra("map");
        carcardnumber = (String) dataMap.get("carCardNumber");
        keys = (String[]) ((Activity)mView.getContext()).getIntent().getExtras().get("keys");
        title =  (String) ((Activity)mView.getContext()).getIntent().getExtras().get("title");
        type = (int) ((Activity)mView.getContext()).getIntent().getIntExtra("type",0);
        carPkid = ((Activity) mView.getContext()).getIntent().getStringExtra("carPkid");
        carMap = ConsTants.carMap;
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        title="车辆信息修改";
        initRecycleBean(dataMap);
   //    lookReciverDetail();
    }
    public static void setParams(String key,String value){
        uploadMap.put(key, value);
    }
    private void setImg(String path){
        File file = new File(path);
//        ToastUtils.showToast(mView.getContext(),"FILE_size:"+BitmapUtil.getSize(file));
        uploadMap.put(PhotoItemFactory.photoKey,file);
    }
    public static void delImg(String key){
        uploadMap.remove(key);
    }

    public void commit(){
            commitAddDetail();
    }

    public void commitAddDetail(){
            uploadMap.put("userId", ConsTants.UserId);
            uploadMap.put("stationPkid", ConsTants.stationId);
            uploadMap.put("regionCode", ConsTants.regionCode);
            uploadMap.put("carPkid", carPkid);
            uploadMap.put("carCardNumber", carcardnumber);
        addSubscribe(mModel.commitReciverDetail(getBody())
                    .compose(RxUtil.<MyHttpResponse>rxSchedulerHelper())
//                .compose(RxUtil.<MyHttpResponse>handleResult())
                    .subscribeWith(
                            new CommonSubscriber<MyHttpResponse>(mView) {
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }

                                @Override
                                public void onNext(MyHttpResponse map) {
                                   mView.updateComplete(map.getErrInfo());
                                }
                            }
                    ));
    }
    private MultipartBody getBody(){
        for (String key:uploadMap.keySet()){
            Object obj = uploadMap.get(key);
            if(obj instanceof String){
                builder.addFormDataPart(key, (String)obj);
            }else if(obj instanceof File){
                File file = (File) obj;
               builder.addFormDataPart(key, ((File)obj).getName(), RequestBody.create(MediaType.parse("image/*"), (File)obj));
               // Log.i("Lybin",((File) obj).getName()+"------size:"+BitmapUtil.getSize((File) obj));
            }
        }
        return  builder.build();
    }

    private void initRecycleBean(Map checkMap) {
        Context context = mView.getContext();
        List list = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            String name = mModel.getTitle(context, keys[i]);
            String color = mModel.getColor(context, keys[i]);
         //   String data = mModel.getTestData(context,keys[i]);
            String data = (String)checkMap.get(keys[i]);
                String[] values = mModel.getValues(context, keys[i]);
                select = data;
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
                        bean.setTitle(title);
                        if(keys[i].equals("applyTEL")){
                            bean.setFlag("true");
                        }
                        list.add(bean);
                    }else if (values != null && values.length == 1 && values[0].equals("Edit1")) {
                        EditValueBean1 bean = new EditValueBean1();
                        bean.setKey(keys[i]);
                        bean.setName(name);
                        bean.setValue(select);
                        bean.setMaxLength(mModel.getInputType(context, keys[i]));
                        bean.setTitle(title);
                        if(keys[i].equals("applyContent")){
                            bean.setFlag("true");
                        }
                        list.add(bean);
                    }else if (values != null && values.length == 1 && values[0].equals("Sprinner")) {
                    SprinnerValueBean bean = new SprinnerValueBean();
                    String s = keys[i].toLowerCase();
                        //车牌颜色key修改与车辆下拉列表的key一致
                        if(s.equals("carcardcolor")){
                            s="cardcolor";
                        }
                        List<LoginCarTypeBean> carTypeBeen = carMap.get(s);
                    if(carTypeBeen!=null) {
                        String[] values1 = new String[carTypeBeen.size()];
                        String[] code = new String[carTypeBeen.size()];
                        for (int k = 0; k < carTypeBeen.size(); k++) {
                            values1[k] = carTypeBeen.get(k).getName();
                            code[k]=carTypeBeen.get(k).getCode();
                        }
                        bean.setSelects(values1);
                        bean.setCode(code);
                        for(int m=0;m<values1.length;m++){
                            if(select!=null&&select.equals(values1[m])){
                              String  nameCode = code[m];
                                bean.setNameCode(nameCode);
                            }
                        }
                    }
                    bean.setName(name);
                    bean.setValue(select);//网络数据
                    bean.setKey(keys[i]);//name
                        bean.setTitle(title);
                    list.add(bean);

                } else {
                        TextValueBean bean = new TextValueBean();
                        if(select!=null) {
                        }
                        bean.setValue(select);
                        bean.setName(name);
                        bean.setColor(color);
                        bean.setKey(keys[i]);
                        list.add(bean);
                }
        }
        mView.initRecycle(list);
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
//        String path = BitmapUtil.getImageAbsolutePath((Activity) mView.getContext(),uri);
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
    public String allowRequest() {
        String applyContent = (String) uploadMap.get("applyContent");
        String applyTel = (String) uploadMap.get("applyTEL");
        if (applyContent == null || applyContent.equals("") || applyTel.equals("") || applyTel == null) {
            return "信息填写不完整";
        }
        if(applyTel!=null) {
            boolean mobile = TelUtils.isMobile(applyTel);
            if(mobile==false){
                return "请输入正确的手机号码";
            }
        }
        for(int i = 0; i< com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.PhotoItemFactory.PhotoKeys.length; i++){
            String key = com.mapuni.car.mvp.searchcar.gotoview.ItemFactory.PhotoItemFactory.PhotoKeys[i];
            File file = (File) uploadMap.get(key);
            if(file==null){
                return "上传图片不完整";
            }
        }
        return "";
    }

}
