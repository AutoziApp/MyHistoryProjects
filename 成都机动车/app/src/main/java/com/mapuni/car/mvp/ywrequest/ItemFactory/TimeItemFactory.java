package com.mapuni.car.mvp.ywrequest.ItemFactory;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.ywrequest.model.TimeValueBean;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by yawei on 2017/8/25.
 */

public class TimeItemFactory extends AssemblyRecyclerItemFactory<TimeItemFactory.CarItem> {
    Activity act;
    @Override
    public boolean isTarget(Object data) {
        return data instanceof TimeValueBean;
    }

    public TimeItemFactory(Activity act){
        this.act = act;
    }

    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_text_layout,parent);
    }

    public class CarItem extends AssemblyRecyclerItem<TimeValueBean>{
        TextView name,value,timeSelect,divider;
        Context context;
        public CarItem(int itemLayoutId, ViewGroup parent){
            super(itemLayoutId,parent);
        }
        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
            value = (TextView) findViewById(R.id.value);
            timeSelect = (TextView) findViewById(R.id.timeSelect);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;
            value.setVisibility(View.GONE);
            timeSelect.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onSetData(int position, TimeValueBean bean) {

            name.setText(bean.getName());
            timeSelect.setOnClickListener(l->onYearMonthDayPicker());
            timeSelect.setText(bean.getValue());
        }

        public void onYearMonthDayPicker() {
            final DatePicker picker = new DatePicker(act);
            picker.setCanceledOnTouchOutside(true);
            picker.setUseWeight(true);
            picker.setTopPadding(ConvertUtils.toPx(context, 10));
            picker.setRangeEnd(2111, 1, 1);
            picker.setRangeStart(2001, 1, 1);
            Calendar calendar = Calendar.getInstance();
            picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            picker.setResetWhileWheel(false);
            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                @Override
                public void onDatePicked(String year, String month, String day) {
                    timeSelect.setText(year+"-"+month+"-"+day);
                    getData().setValue(year+"-"+month+"-"+day);
                    //暂时注销
//                    LoginPresenter.setParams(getData().getKey(),year+"-"+month+"-"+day);
//                onTimePicker();
                }
            });
            picker.setOnWheelListener(new DatePicker.OnWheelListener() {
                @Override
                public void onYearWheeled(int index, String year) {
                    picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
                }

                @Override
                public void onMonthWheeled(int index, String month) {
                    picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
                }

                @Override
                public void onDayWheeled(int index, String day) {
                    picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
                }
            });
            picker.show();
        }
    }
}
