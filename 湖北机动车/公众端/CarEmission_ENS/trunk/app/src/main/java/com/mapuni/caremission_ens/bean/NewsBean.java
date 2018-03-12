package com.mapuni.caremission_ens.bean;

import java.io.Serializable;

/**
 * Created by yawei on 2017/4/6.
 */

public class NewsBean implements Serializable{


    /**
     * PKID : 1489627029111_baixg
     * SENDPEOPLE : baixg
     * SENDTIME : 2017-03-16 09:17:09
     * TITLE : 111
     */

    private String PKID;
    private String SENDPEOPLE;
    private String SENDTIME;
    private String TITLE;

    public String getPKID() {
        return PKID;
    }

    public void setPKID(String PKID) {
        this.PKID = PKID;
    }

    public String getSENDPEOPLE() {
        return SENDPEOPLE;
    }

    public void setSENDPEOPLE(String SENDPEOPLE) {
        this.SENDPEOPLE = SENDPEOPLE;
    }

    public String getSENDTIME() {
        return SENDTIME;
    }

    public void setSENDTIME(String SENDTIME) {
        this.SENDTIME = SENDTIME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}
