package com.mapuni.mobileenvironment.model;

import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.util.Log;

import com.mapuni.mobileenvironment.app.DataFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PagerModel implements Serializable {
    private String ComPanyCode;
    private ArrayList titleList;

    public String getComPanyCode() {
        return ComPanyCode;
    }

    public void setComPanyCode(String comPanyCode) {
        ComPanyCode = comPanyCode;
    }

    private HashMap<String,ArrayList<HashMap>> twoColumnData;
    private HashMap<String,HashMap> threeColumnData;
    private HashMap<String,ArrayList> oneSelectHeadData;
    private HashMap<String,HashMap<String,ArrayList>> twoSelectHeadData;
    private HashMap<String,Object> oneSelectAndOtherHeadData;
    public Handler mHandler;
    public PagerModel(Handler handler){
        mHandler = handler;
    }
    public ArrayList<HashMap> getTwoColumnData(String s) {
        return twoColumnData.get(s);
    }

    public void setTwoColumnData(Object obj,String s) {
        if(twoColumnData==null){
            twoColumnData = new HashMap<String, ArrayList<HashMap>>();
        }
        ArrayList nameList = DataFactory.getListByName(s);
        ArrayList keyList = DataFactory.getKeysByName(s);
        ArrayList data = new ArrayList();
        for(int i=0;i<nameList.size();i++){
            HashMap map = new HashMap();
            String key = (String) keyList.get(i);
            String value = (String) ((HashMap)obj).get(key.toLowerCase());
            map.put("name",nameList.get(i));
            map.put("value",value);
            data.add(map);
        }
        twoColumnData.put(s,data);
    }
    public ArrayList getSelectValue(){
        ArrayList<Object> list =  new ArrayList();
        list.add("4912B13D-6707-4386-BB83-42503DAA2CB8"); //建设项目环评
        list.add("F005E774-8BF1-4A4E-AF16-16278D550573");//建设项目验收
        HashMap map  =  new HashMap();
        map.put("ITEM1","0000077");
        map.put("ITEM2","2014");
        list.add(map);
        return list;
    }
    public HashMap<String, HashMap> getThreeColumnData() {
        return threeColumnData;
    }

    public void setThreeColumnData(HashMap<String, HashMap> threeColumnData) {
        this.threeColumnData = threeColumnData;
    }

    public ArrayList getOneSelectHeadData(String s) {
        return (ArrayList) oneSelectHeadData.get(s);
    }

    public void setOneSelectHeadData(Object obj,String s) {
        if(oneSelectHeadData==null){
            oneSelectHeadData = new HashMap<>();
        }
        ArrayList<HashMap> data = (ArrayList<HashMap>) obj;
        oneSelectHeadData.put(s,data);
    }

    public HashMap<String, ArrayList> getTwoSelectHeadData(String s) {
        return (HashMap<String, ArrayList>) twoSelectHeadData.get(s);
    }

    public void setTwoSelectHeadData(Object obj,String s,String tag) {
        if(twoSelectHeadData==null){
            twoSelectHeadData = new HashMap<>();
        }
        ArrayList data = (ArrayList) obj;
        HashMap map = new HashMap();
        if(tag.contains("nf")){
            for(int i=0;i<data.size();i++){
                HashMap _Map = (HashMap) data.get(i);
                String value = (String) _Map.get("nf");
                data.remove(i);
                data.add(i,value);
            }
        }
        map.put(tag,data);
        twoSelectHeadData.put(s,map);
    }

    public HashMap<String, Object> getOneSelectAndOtherHeadData() {
        return oneSelectAndOtherHeadData;
    }

    public void setOneSelectAndOtherHeadData(HashMap<String, Object> oneSelectAndOtherHeadData) {
        this.oneSelectAndOtherHeadData = oneSelectAndOtherHeadData;
    }
    public ArrayList getTitleList(int i) {
        return DataFactory.getTitleList(i);
    }

//    public void setTitleList(int i) {
//        this.titleList =
//    }

}
