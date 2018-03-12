package com.mapuni.car.mvp.ywrequest.model;

import android.util.Log;

import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.login.model.LoginCarTypeBean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by YZP on 2018/1/10.
 */
public class CheckMethodValue {
   public static  String [] names;
    public static  String [] codes;
    public static HashMap<String,String[]> datas;
    public static HashMap<String, String[]> getValues(String data){
        if(datas==null)
            datas=new HashMap<>();
      if("简易瞬态工况法".equals(data)){
          names=new String[]{"双怠速法"};
      }else if("双怠速法".equals(data)){
          names=new String[]{"简易瞬态工况法","混合动力车辆检测"};
      }else if("加载减速工况法".equals(data)){
          names=new String[]{"不透光烟度法","滤纸烟度法","混合动力车辆检测"};
      }else if("不透光烟度法".equals(data)){
          names=new String[]{"加载减速工况法","混合动力车辆检测"};
      }else if("滤纸烟度法".equals(data)){
          names=new String[]{"加载减速工况法"};
      }else if("混合动力车辆检测".equals(data)){
          names=new String[]{"双怠速法","不透光烟度法"};
      }
        codes=new String[names.length];
        datas.clear();
        LinkedHashMap<String, String> checkMethodMap = ConsTants.getCheckMethodMap1();
        for(int i=0;i<codes.length;i++) {
            String data1 = names[i];
            for (int j=0;j<checkMethodMap.size();j++){
                String code = checkMethodMap.get(data1);
                codes[i]=code;
            }
            datas.put("names",names);
            datas.put("codes",codes);
        }
//        for(int i=0;i<codes.length;i++){
//            String data1 = names[i];
//            for (int j=0;j<carTypeBeen.size();j++){
//               if(carTypeBeen.get(i).getName().equals(data1)) {
//                    codes[i] =carTypeBeen.get(i).getCode();
//                   datas.put("names",names);
//                }
//                datas.put("codes",codes);
//            }
//        }
        return datas;
    }
}
