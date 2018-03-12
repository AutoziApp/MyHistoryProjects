package com.mapuni.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YZP on 2018/1/8.
 */
public class TelUtils {
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\d{8}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        System.out.println(b);
        return b;
    }
}
