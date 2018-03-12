package cn.com.mapuni.meshingtotal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 15225 on 2017/7/5.
 */

public class NoticeListTranstanceBean implements Serializable{
    public NoticeListTranstanceBean(List<NoticeBean.DataBean> data) {
        this.data = data;
    }

    public List<NoticeBean.DataBean> getData() {
        return data;
    }

    public void setData(List<NoticeBean.DataBean> data) {
        this.data = data;
    }

    private List<NoticeBean.DataBean> data;
}
