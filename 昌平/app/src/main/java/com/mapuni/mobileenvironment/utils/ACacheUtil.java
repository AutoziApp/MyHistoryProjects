package com.mapuni.mobileenvironment.utils;

import android.content.Context;

import com.mapuni.mobileenvironment.model.HistoryRecord;

import java.util.ArrayList;

/**
 * Created by Mai on 2017/2/8.
 */

public class ACacheUtil {
    /*
    * 保存设备id 最大保存5条
    *
    */
    public static void saveHistoryRecord(Context context, HistoryRecord historyRecord) {

        ACache aCache = ACache.get(context);
        //只能使用List的子类
        ArrayList<HistoryRecord> newList = new ArrayList();
        ArrayList<HistoryRecord> oldList = (ArrayList<HistoryRecord>) aCache.getAsObject("historyRecord");
        if (oldList != null) {//当取出的记录不为空是说明有记录
            for (HistoryRecord historyRecord1 : oldList) {//避免重复储存
                if (historyRecord1.getDeviceId().equals(historyRecord.getDeviceId())) {//加入存储的记录中有对应的deviceid，那么就删除旧的记录
                    newList.add(historyRecord);
                    oldList.remove(historyRecord1);
                    newList.addAll(oldList);
                    aCache.put("historyRecord", newList);
                    return;
                }
            }
            if (oldList.size() < 5) {//当所存储的历史记录条数小于5时候，直接增加记录
                newList.add(historyRecord);
                newList.addAll(oldList);
            } else {
                oldList.remove(4);//移除最后一条
                newList.add(historyRecord);//先将最新浏览记录存入第一条
                newList.addAll(oldList);
            }
        } else {//当取出的集合为空时说明先前没存过
            newList.add(historyRecord);
        }
        aCache.put("historyRecord", newList);
    }

    /*
       * 读取缓存的浏览记录所对应的设备id
       *
       */
    public static ArrayList<HistoryRecord> readHistoryRecord(Context context) {

        ACache aCache = ACache.get(context);
        //使用getAsObject()，直接进行强转
        ArrayList<HistoryRecord> list = (ArrayList<HistoryRecord>) aCache.getAsObject("historyRecord");
        return list;
    }
}
