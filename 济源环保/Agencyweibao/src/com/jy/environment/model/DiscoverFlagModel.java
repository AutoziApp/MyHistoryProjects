package com.jy.environment.model;

import java.io.Serializable;
/**
 * 点赞返回
 * @author baiyuchuan
 *
 */
public class DiscoverFlagModel implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2858038013070366327L;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
}
