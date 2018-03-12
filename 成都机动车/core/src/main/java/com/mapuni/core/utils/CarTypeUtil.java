package com.mapuni.core.utils;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by YZP on 2017/11/28.
 */
public class CarTypeUtil {
    public static void carTypeSelect(Activity mActivity, String[] carTypeNums, TextView carType) {
        OptionPicker picker = new OptionPicker(mActivity,carTypeNums);
//        OptionPicker picker = new OptionPicker(mActivity,ConsTants.getCarTypeNames());
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.WRAP);
        picker.setShadowColor(Color.parseColor("#f3f3f3"),100);
        picker.setDividerVisible(false);
        picker.setSelectedIndex(1);
        picker.setCycleDisable(true);
        picker.setTextSize(15);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                carType.setText(item);
            }
        });
        picker.show();
    }
}
