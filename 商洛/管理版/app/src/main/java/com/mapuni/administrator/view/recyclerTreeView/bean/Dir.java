package com.mapuni.administrator.view.recyclerTreeView.bean;


import com.mapuni.administrator.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class Dir implements LayoutItemType {
    public String dirName;
    public String uuid;
    public String state;


    public Dir(String dirName,String uuid,String state) {
        this.dirName = dirName;
        this.uuid=uuid;
        this.state=state;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
