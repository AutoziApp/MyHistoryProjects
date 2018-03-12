package com.mapuni.car.mvp.ywrequest.ItemFactory;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.ywrequest.model.EditValueBean;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by yawei on 2017/8/25.
 */

public class EditItemFactory2 extends AssemblyRecyclerItemFactory<EditItemFactory2.CarItem> {

    @Override
    public boolean isTarget(Object data) {
        return data instanceof EditValueBean;
    }


    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_edit1_layout,parent);
    }

    public class CarItem extends AssemblyRecyclerItem<EditValueBean> {
        TextView name, timeSelect, divider;
        EditText value;
        Context context;

        public CarItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
            value = (EditText) findViewById(R.id.value);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;
        }

        @Override
        protected void onSetData(int position, EditValueBean bean) {
                if (bean.getColor() != null) {
                    value.setTextColor(Color.parseColor(bean.getColor()));

//                value.setTextColor(context.getResources().getColor(R.color.Blue));
                }
            value.setText(bean.getValue());
            value.setEnabled(false);
               // value.setFilters(new InputFilter[]{new InputFilter.LengthFilter((Integer.parseInt(bean.getMaxLength())))});
                name.setText(bean.getName());
//                value.setText(bean.getValue());


        }


    }


}
