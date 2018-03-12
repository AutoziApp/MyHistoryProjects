package com.mapuni.mobileenvironment.model;

/**
 * Created by Mai on 2017/2/7.
 */

public class GasAndJuli {
    float juli;
    GasSite.DataBeanX.DataBean dataBean;

    public float getJuli() {
        return juli;
    }

    public void setJuli(float juli) {
        this.juli = juli;
    }

    public GasAndJuli(float juli, GasSite.DataBeanX.DataBean dataBean) {
        this.juli = juli;
        this.dataBean = dataBean;
    }

    public GasSite.DataBeanX.DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(GasSite.DataBeanX.DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public GasAndJuli() {
    }

}
