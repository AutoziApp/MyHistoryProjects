package com.jy.environment.model;

import java.util.List;

public class ResultBlogList {

    private boolean flag;
    private List<Weib> weibs ;
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public List<Weib> getWeibs() {
        return weibs;
    }
    public void setWeibs(List<Weib> weibs) {
        this.weibs = weibs;
    }
    
}
