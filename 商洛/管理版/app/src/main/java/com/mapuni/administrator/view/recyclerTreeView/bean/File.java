package com.mapuni.administrator.view.recyclerTreeView.bean;


import com.mapuni.administrator.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class File implements LayoutItemType {
    public String fileName;
    public String uuid;
    public String state;

    public File(String fileName,String uuid,String state) {
        this.fileName = fileName;
        this.uuid=uuid;
        this.state=state;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }
}
