package com.mapuni.shangluo.bean;

/**
 * Created by 15225 on 2017/8/31.
 */

public class ContactBean {

    /**
     * id : 40be8fdded7fdcfea13dbsd46e9fe0a2c
     * icon : null
     * text : 所有网格
     * type : 0
     * state : closed
     * leaf : false
     * level : 0
     */

    private String id;
    private Object icon;
    private String text;
    private int type;
    private String state;
    private boolean leaf;
    private int level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
