package com.mapuni.car.mvp.ywrequest.model;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import com.mapuni.car.R;
import com.mapuni.car.api.ApiManager;
import com.mapuni.car.mvp.ywrequest.contract.DetailContract;
import com.mapuni.core.net.response.RequestHttpResponse;
import com.mapuni.core.utils.XmlHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

/**
 * Created by lybin on 2017/6/27.
 */

public class DetailModel implements DetailContract.DetailModel {
    private Map titleMap ;
    private Map valuesMap ;
    private Map colorMap;
    private Map inputTypeMap;
    private HashMap dataMap;
    private Map reciverMap;



    //业务申请跨站检测解锁详情
    @Override
    public Flowable<RequestHttpResponse> commitUnlockData(RequestBody body) {
        return ApiManager.getInstence().getCarService().commitUnlockData(body);
    }

    @Override
    public Flowable<RequestHttpResponse> commitMethod(RequestBody body) {
        return ApiManager.getInstence().getCarService().commitMethod(body);
    }

    @Override
    public String getTitle(Context context,String s) {
        if(titleMap==null)
            initConfig(context);
        return (String) titleMap.get(s);
    }

    @Override
    public String[] getValues(Context context,String s) {
        if(valuesMap==null)
            initConfig(context);
        return (String[]) valuesMap.get(s);
    }

    @Override
    public String[] getKeys(Context context) {
        return context.getResources().getStringArray(R.array.keys);
    }

    @Override
    public String getColor(Context context, String s) {
        if(colorMap==null)
            initConfig(context);
        return (String) colorMap.get(s);
    }
    @Override
    public String getInputType(Context context, String s) {
        if(inputTypeMap==null)
            initConfig(context);
        return (String) inputTypeMap.get(s);
    }
//
//    @Override
//    public String[] getReciverValues(Context context, String s) {
//        return new String[0];
//    }
   //可修改列表values
    @Override
    public String[] getReciverValues(Context context, String s) {
        if(reciverMap==null)
            initConfig(context);
        return (String[]) reciverMap.get(s);
    }

    @Override
    public String getTestData(Context context, String s) {
        if(dataMap==null)
            initConfig(context);
        return (String) dataMap.get(s);
    }

    private void initConfig(Context context){
        titleMap = new HashMap();
        valuesMap = new HashMap();
        colorMap = new HashMap();
        inputTypeMap = new HashMap();
        dataMap = new HashMap();
        reciverMap=new HashMap<>();
        EditText et = new EditText(context);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        try {
            InputStream is = context.getResources().getAssets().open("car_config.xml");
            valuesMap = XmlHelper.getArrayValueByName("VALUE",is);
            InputStream ins = context.getResources().getAssets().open("car_config.xml");
            colorMap = XmlHelper.getListStyleByName("TEXT_COLOR",ins);
            InputStream input = context.getResources().getAssets().open("car_config.xml");
            titleMap = XmlHelper.getListStyleByName("KEY_NAME",input);
            InputStream type = context.getResources().getAssets().open("car_config.xml");
            inputTypeMap = XmlHelper.getListStyleByName("INPUT_MAX",type);
            InputStream isdata = context.getResources().getAssets().open("car_config.xml");
            dataMap = XmlHelper.getListStyleByName("KEY_VALUE",isdata);
            InputStream is1 = context.getResources().getAssets().open("car_config.xml");
            reciverMap = XmlHelper.getArrayValueByName("keyd",is1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
