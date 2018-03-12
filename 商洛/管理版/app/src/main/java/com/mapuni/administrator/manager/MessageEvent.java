package com.mapuni.administrator.manager;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.manager
 * @class describe
 * @anthor Tianfy
 * @time 2017/8/30 18:10
 * @change
 * @chang time
 * @class describe
 */

public class MessageEvent {

    private String message;

    public MessageEvent(String message) {
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
