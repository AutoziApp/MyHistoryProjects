package com.mapuni.car.mvp.searchcar.model;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.mapuni.car.R;
import com.mapuni.car.api.ApiManager;
import com.mapuni.car.mvp.searchcar.contract.SearchCarContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.core.net.response.MyHttpResponse;
import com.mapuni.core.utils.XmlHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

/**
 * Created by yawei on 2017/7/3.
 */

public class SearchCarModel implements SearchCarContract.SearchCarModel,Serializable{
    public List CarListData;
    private Map titleMap ;
    private Map valuesMap ;
    private Map colorMap;
    private Map inputTypeMap;
    private HashMap dataMap;
    private Map codeMap;
    @Override
    public Flowable<CarBean> getCarList(Map map) {
        return ApiManager.getInstence().getCarService().getCarList(map);
    }

    @Override
    public Flowable<CarSelectBean> getCarSelect() {
        return ApiManager.getInstence().getCarService().getCarSelect();
    }

    @Override
    public Flowable<MyHttpResponse<Map>> getAddCarDetail(Map map) {
        return null;
    }


    @Override
    public Flowable<MyHttpResponse> commitDetail(RequestBody body) {
        return  ApiManager.getInstence().getCarService().commitData(body);
    }
//    @Override
//    public Flowable<MyHttpResponse> commitDetail(Map map) {
//        return  ApiManager.getInstence().getCarService().commitData(map);
//    }

    @Override
    public List getCarListData() {
        return CarListData;
    }
    @Override
    public void setCarListData(List carListData) {
        CarListData = carListData;
    }

    @Override
    public String getTitle(Context context, String s) {
        if(titleMap==null)
            initConfig(context);
        return (String) titleMap.get(s);
    }

    @Override
    public String[] getValues(Context context, String s) {
        if(valuesMap==null)
            initConfig(context);
        return (String[]) valuesMap.get(s);
//        return new String[0];
    }

    @Override
    public String[] getKeys(Context context) {
        return context.getResources().getStringArray(R.array.data);
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

    @Override
    public String getTestData(Context context, String s) {
        if(dataMap==null)
            initConfig(context);
        return (String) dataMap.get(s);
    }

    @Override
    public String[] getCodeValues(Context context, String s) {
        if(codeMap==null)
            initConfig(context);
        return (String[]) codeMap.get(s);
    }

    private void initConfig(Context context){
        titleMap = new HashMap();
        valuesMap = new HashMap();
        colorMap = new HashMap();
        inputTypeMap = new HashMap();
        //测试数据
        dataMap = new HashMap();
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
            InputStream code = context.getResources().getAssets().open("car_config.xml");
            codeMap = XmlHelper.getArrayValueByName("CODEVALUE",code);
            //测试数据
            InputStream istest = context.getResources().getAssets().open("car_config.xml");
            dataMap   = XmlHelper.getListStyleByName("KEY_VALUE",istest);

//            InputStream istest1 = context.getResources().getAssets().open("car_config.xml");
//            dataMap   = XmlHelper.getListStyleByName("keyd",istest1);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
