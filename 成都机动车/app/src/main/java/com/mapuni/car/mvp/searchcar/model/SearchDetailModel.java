package com.mapuni.car.mvp.searchcar.model;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import com.mapuni.car.R;
import com.mapuni.car.api.ApiManager;
import com.mapuni.car.mvp.listcar.contract.SearchDetailContract;
import com.mapuni.core.utils.XmlHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by lybin on 2017/6/27.
 */

public class SearchDetailModel implements SearchDetailContract.DetailModel {
    private Map titleMap ;
    private Map valuesMap ;
    private Map colorMap;
    private Map inputTypeMap;
    private HashMap dataMap;


    @Override
    public Flowable<CarSearchDetailBean> getCarDetailData(Map map,int type) {
        if(type == 1) {
            return ApiManager.getInstence().getCarService().getChangeCarInfoDetail(map);
        }else if(type == 2) {
            return ApiManager.getInstence().getCarService().getJcffsqDetail(map);
        }else  {
            return ApiManager.getInstence().getCarService().getKzjssqDetail(map);
        }

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

    @Override
    public String getTestData(Context context, String s) {
        return "";
    }

    private void initConfig(Context context){
        titleMap = new HashMap();
        valuesMap = new HashMap();
        colorMap = new HashMap();
        inputTypeMap = new HashMap();

        EditText et = new EditText(context);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        try {
            InputStream is = context.getResources().getAssets().open("car_config.xml");
            valuesMap = XmlHelper.getArrayValueByName("VALUE",is);
            InputStream ins = context.getResources().getAssets().open("car_config.xml");
            colorMap = XmlHelper.getListStyleByName("TEXT_COLOR",ins);
            InputStream input = context.getResources().getAssets().open("car_config.xml");
            titleMap = XmlHelper.getListStyleByName("KEY_TITLE",input);
            InputStream type = context.getResources().getAssets().open("car_config.xml");
            inputTypeMap = XmlHelper.getListStyleByName("INPUT_MAX",type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
