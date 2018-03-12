package com.yutu.car.presenter;

import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;

/**
 * Created by yawei on 2017/4/24.
 */

public abstract class ExpandaListControl implements Serializable {
    private String title;
    private Object obj;
    public abstract String getTitle();
    public abstract void setTitle(String title);
    public abstract void setTag(Object obj);
    public abstract int jsonToBean(String s);
    public abstract String clickForId();
    public abstract String clickForName();
    public abstract int getGroupCount();
    public abstract int getChildrenCount(int groupPosition);
    public abstract Object getGroup(int groupPosition);
    public abstract Object getChild(int groupPosition, int childPosition);
    public abstract String getGroupName(int groupPosition);
    public abstract String getChildName(int groupPosition,int childPosition);
    public abstract int getChildCount(int groupPosition);
    public abstract void requestNet(StringCallback call);
    public abstract ExpandaListItem getItemBean(int groupPosition, int childPosition);
    public abstract BaseControl getDetailControl();
    public  class ExpandaListItem{
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String id;
    }
}
