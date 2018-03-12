package com.mapuni.shangluo.utils;

import com.mapuni.shangluo.bean.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15225 on 2017/8/17.
 */

public class TempDataUtil {

    public static List<Task> getTaskData(int num){
        String[] names={"古建纸箱厂","欧本化工","棉发化工"};
        String[] dates={"2017-01-12","2017-01-15","2017-01-19"};
        String[] source={"上报","下发"};
        List<Task> list=new ArrayList<>();
        for (int i=0;i<num;i++){
            list.add(new Task(i+1,names[i%3],dates[i%3],source[i%2],"详情"));
        }
        return list;
    }

}
