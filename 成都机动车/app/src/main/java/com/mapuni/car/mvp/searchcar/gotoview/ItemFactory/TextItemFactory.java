package com.mapuni.car.mvp.searchcar.gotoview.ItemFactory;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.detailcar.model.TextValueBean;
import com.mapuni.car.mvp.searchcar.gotoview.presenter.CarListPresenter;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * Created by yawei on 2017/8/25.
 */

public class TextItemFactory extends AssemblyRecyclerItemFactory<TextItemFactory.CarItem> {

    @Override
    public boolean isTarget(Object data) {
        return data instanceof TextValueBean;
    }


    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_text_layout,parent);
    }

    public class CarItem extends AssemblyRecyclerItem<TextValueBean> {
        TextView name, value, timeSelect, divider;
        Context context;

        public CarItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
            value = (TextView) findViewById(R.id.value);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;
        }

        @Override
        protected void onSetData(int position, TextValueBean bean) {
            if (bean.getColor() != null) {
                value.setTextColor(Color.parseColor(bean.getColor()));
//                value.setTextColor(context.getResources().getColor(R.color.Blue));
            }
            name.setText(bean.getName());
            value.setText(bean.getValue());
            String code = bean.getCode();
            if(!TextUtils.isEmpty(code)){
                CarListPresenter.setParams(bean.getKey(),code);
            }else {
                CarListPresenter.setParams(bean.getKey(), bean.getValue());
            }
        }
    }


}
