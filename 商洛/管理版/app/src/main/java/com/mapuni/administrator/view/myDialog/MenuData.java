package com.mapuni.administrator.view.myDialog;

import java.io.Serializable;

/**
 * Created by LaiYingtang on 2016/5/22.
 * 列数据的bean
 */
public class MenuData implements Serializable {
    public int id;
    public String name;
    public int flag;
    public String uuid;
    public int isQY;


    public MenuData(int id, String name, int flag,String uuid,int isQY) {
        this.id = id;
        this.name = name;
        this.flag = flag;
        this.uuid=uuid;
        this.isQY=isQY;
    }

    public MenuData() {
    }
}
