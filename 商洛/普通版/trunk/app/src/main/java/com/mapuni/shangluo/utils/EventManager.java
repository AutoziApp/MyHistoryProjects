package com.mapuni.shangluo.utils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;




/**
 * Created by Administrator on 2015/8/10.
 */
public class EventManager {

    public static void registEventBus(Object obj) {
        if (!getDefault().isRegistered(obj) && classContainsOnEventMethod(obj.getClass())) {
            getDefault().register(obj);
        }
    }

    public static void unRegistEventBus(Object obj) {
        if (getDefault().isRegistered(obj) && classContainsOnEventMethod(obj.getClass())) {
            getDefault().unregister(obj);
        }
    }

    public static EventBus getDefault() {
        return EventBus.getDefault();
    }

    @SuppressWarnings("rawtypes")
    public static boolean classContainsOnEventMethod(Class clz) {
        Method[] ms = clz.getDeclaredMethods();
        for (Method method : ms) {
            String methodName = method.getName();
            if (methodName.startsWith("onEvent")) {
                return true;
            }
        }
        return false;
    }

}
