package com.mapuni.core.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * Created by YZP on 2018/1/3.
 */
public class InputLowerToUpper  extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
        return lower;
    }

    @Override
    protected char[] getReplacement() {
        char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        return upper;
    }

}

