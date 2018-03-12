package com.mapuni.car.mvp.searchcar.gotoview.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.ItemFactory.PhotoItemFactory;
import com.mapuni.car.mvp.detailcar.presenter.DetailPresenter;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;
import com.mapuni.car.mvp.searchcar.gotoview.assemblywidght.ChangeData;
import com.mapuni.car.mvp.searchcar.gotoview.contract.CarListContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.rxjava.CommonSubscriber;
import com.mapuni.core.rxjava.RxUtil;
import com.mapuni.core.utils.TelUtils;
import com.mapuni.core.widget.takephoto.photo.util.BitmapUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by yawei on 2017/8/25.
 */

public class CarListPresenter extends CarListContract.CarListPresenter implements CarListContract.OnItemClick {

    private Map map;
    private int pagerIndex = 2;
    private final int itemSize = 15;
    private boolean isEnd = false;
    private static Map<String, Object> uploadMap;

    public void setParams(Map params) {
        map = params;
    }

    private static MultipartBody.Builder builder;
    private Context context;
    private HashMap<String, String> infoMap = new HashMap<>();
    private String carColor;

    @Override
    public void onStart() {
//        initRecycleBean();
        context = mView.getContext();
        uploadMap = new HashMap();
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    }

    public static void setParams(String key, String value) {
        uploadMap.put(key, value);
    }

    private void setImg(String path) {
        File file = new File(path);
//        ToastUtils.showToast(mView.getContext(),"FILE_size:"+BitmapUtil.getSize(file));
        uploadMap.put(PhotoItemFactory.photoKey, file);

    }

    public static void delImg(String key) {
        uploadMap.remove(key);
    }

    public void commit() {
        commitAddDetail();

    }

    /**
     * 车辆信息保存
     */
    private void commitAddDetail() {
        CarBean carBean = (CarBean) ((Activity) mView.getContext()).getIntent().getSerializableExtra("carBean");
        String checkMode = ((Activity) mView.getContext()).getIntent().getStringExtra("checkMode");
        List<Map<String, String>> xszInfo = carBean.getXszInfo();
        Map<String, String> jcInfo = carBean.getJcInfo();
        Map<String, String> map = xszInfo.get(0);
        List<Map<String, String>> otherInfo = carBean.getOtherInfo();
        Map<String, String> map1 = otherInfo.get(0);
        String registoffice = map1.get("registoffice");
        uploadMap.put("userId", ConsTants.UserId);
        uploadMap.put("stationPkid", ConsTants.stationId);
        uploadMap.put("regionCode", ConsTants.regionCode);
        uploadMap.put("isChange", "1");
        uploadMap.put("tireNumber", map.get("tirenumber"));
        uploadMap.put("carPkid", map.get("carpkid"));
        uploadMap.put("checkMode", checkMode);
        uploadMap.put("registOffice",registoffice);
        uploadMap.putAll(jcInfo);
        addSubscribe(mModel.commitDetail(getBody())
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

    public HashMap<String, String> getVaule() {
        //当前日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());
        String carnumber = (String) uploadMap.get("carcardnumber");
        String cardcode = (String) uploadMap.get("cardcolor");
        HashMap<String, List<LoginCarTypeBean>> carMap = ConsTants.carMap;
        List<LoginCarTypeBean> cardcolor1 = carMap.get("cardcolor");
        for (int i = 0; i < cardcolor1.size(); i++) {
            String code = cardcolor1.get(i).getCode();
            if (cardcode.equals(code)) {
                carColor = cardcolor1.get(i).getName();
            }
        }
        infoMap.put("carCardNumber", carnumber);
        infoMap.put("carCardColor", carColor);
        infoMap.put("sqsj", date);
        return infoMap;
    }

    private MultipartBody getBody() {
        for (String key : uploadMap.keySet()) {
            Object obj = uploadMap.get(key);
            if (obj instanceof String) {
                //转成上传需要的字段
                String key1 = ChangeData.getKey(key);
                if (key1.equals("addrarea")) {
                    key1 = "addrArea";
                }
                Log.i("zqq", key1 + "------size:" +(String) obj);
                builder.addFormDataPart(key1, (String) obj);
            } else if (obj instanceof File) {
                builder.addFormDataPart(key, ((File) obj).getName(), RequestBody.create(MediaType.parse("image/*"), (File) obj));
                Log.i("Lybin", ((File) obj).getName() + "------size:" + BitmapUtil.getSize((File) obj));
            }
        }
        return builder.build();
    }

    //补录
    public void requestAddData(Map map) {
        map.put("userId", ConsTants.UserId);
        map.put("regionCode", ConsTants.regionCode);
        addSubscribe(mModel.getAddCarDetail(map)
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
                                String[] keys = mView.getContext().getResources().getStringArray(R.array.keys);
                                map.put("type", DetailPresenter.ADD_TYPE);
                                mView.jumpActivity(map, keys);
                            }
                        }
                ));
    }

    public boolean isEnd() {
        return isEnd;
    }

    private void flipPager(int size) {
        if (size >= itemSize) {
            pagerIndex++;
        } else {
            isEnd = true;
        }
    }

    /**
     * 判断必填项是否填写完整
     * @param infoMap
     * @param keyList
     * @return
     */
    public String allowRequest(Map<String, String> infoMap, List<String> keyList) {
        for(int i=0;i<keyList.size();i++){
         String s=   keyList.get(i);
            Log.e("ssss",s);
            String flag = infoMap.get(keyList.get(i));
            if("true".equals(flag)){
                String context = (String) uploadMap.get(keyList.get(i));
                if(TextUtils.isEmpty(context)){
                    return "信息填写不完整";
                }
            }
        }
        String tel = (String) uploadMap.get("tel");
        String agenttel = (String) uploadMap.get("agenttel");
//        String mileagenum = (String) uploadMap.get("mileagenum");
//        String ratedspeed = (String) uploadMap.get("ratedspeed");
//        String drivemode = (String) uploadMap.get("drivemode");
//        String speedchanger = (String) uploadMap.get("speedchanger");
//        String issyjhq = (String) uploadMap.get("issyjhq");
//        if(ratedspeed == null || ratedspeed.equals("")){
//            return "请输入额定转速";
//        }
//        if(drivemode == null || drivemode.equals("")){
//            return "请输入驱动方式";
//        }
//        if(speedchanger == null || speedchanger.equals("")){
//            return "请输入变速器型式";
//        }
//        if(issyjhq == null || issyjhq.equals("")){
//            return "请输入是否装备三元化";
//        }
        if (tel != null && !tel.equals("")) {
            boolean mobile = TelUtils.isMobile(tel);
            if (mobile == false) {
                return "请输入正确的手机号码";
            }
        }
        if (agenttel != null && !agenttel.equals("")) {
            boolean mobile = TelUtils.isMobile(agenttel);
            if (mobile == false) {
                return "请输入正确的手机号码";
            }
        }
//        if (mileagenum == null || mileagenum.equals("")) {
//            return "里程表读数不能为空";
//        }
        return "";
    }

    @Override
    public void Onclick(CarBean bean) {
//        HashMap map = new HashMap();
//        map.put("cphm",bean.getCphm());
//        map.put("hpzl",ConsTants.getCarTypeNum(bean.getHplb()));
//        requestAddData(map);
    }
}
