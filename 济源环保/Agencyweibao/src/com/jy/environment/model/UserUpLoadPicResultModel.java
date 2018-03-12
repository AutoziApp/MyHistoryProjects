package com.jy.environment.model;

import java.io.Serializable;

public class UserUpLoadPicResultModel implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1384531611482669912L;
    private String userpic;
    private boolean flag;
    
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getUserpic() {
        return userpic;
    }
    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
    
    
}
