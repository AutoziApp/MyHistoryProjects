package cn.com.mapuni.meshingtotal.model;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by zhaijikui on 2016/4/9.
 */
public class CommonItemRecords implements java.io.Serializable{

    private String id;
    private String name;
    private String desc;
    private String imgPath;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
