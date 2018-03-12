package com.mapuni.administrator.view.myDialog;

/**
 * Created by yang on 2017/12/14.
 */

public class PatrolObject {

    /**
     * id : 9c4a28301d4f4fe681b672282e3442b7
     * icon : null
     * text : 庾岭镇
     * type : 2
     * state : closed
     * leaf : false
     * level : 3
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
