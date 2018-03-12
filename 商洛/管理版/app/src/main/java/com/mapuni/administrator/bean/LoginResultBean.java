package com.mapuni.administrator.bean;

/**
 * Created by 15225 on 2017/8/19.
 */

public class LoginResultBean {


    /**
     * roleId : 143
     * status : 0
     * msg : 认证成功
     * sessionId : b291ea3c-563c-40cb-b1ac-3653ca27b8da
     */

    private String roleId;
    private int status;
    private String msg;
    private String sessionId;
    private int gridLevel;

    public int getGridLevel() {
        return gridLevel;
    }

    public void setGridLevel(int gridLevel) {
        this.gridLevel = gridLevel;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
