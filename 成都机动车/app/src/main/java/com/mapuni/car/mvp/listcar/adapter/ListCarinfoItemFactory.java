package com.mapuni.car.mvp.listcar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.listcar.contract.CarListInfoContract;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarBean;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;
import com.mapuni.core.net.loadingview.LoadingView;

/**
 * Created by yawei on 2017/8/25.
 */

public class ListCarinfoItemFactory extends AssemblyRecyclerItemFactory<ListCarinfoItemFactory.CarItem> {
    CarListInfoContract.OnItemClick click;
    @Override
    public boolean isTarget(Object data) {
        return data instanceof CarBean;
    }

    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_car_list,parent);
    }
    public void setOnclick(CarListInfoContract.OnItemClick click){
        this.click=click;
    }

    public class CarItem extends AssemblyRecyclerItem<CarBean>{
        TextView a,b,c,divider;
        LinearLayout item;
        public CarItem(int itemLayoutId, ViewGroup parent){
            super(itemLayoutId,parent);
        }
        @Override
        protected void onFindViews() {
            a = (TextView) findViewById(R.id.a);
            b = (TextView) findViewById(R.id.b);
            c = (TextView) findViewById(R.id.c);
            item = (LinearLayout) findViewById(R.id.item);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            item.setOnClickListener(l->{
                LoadingView.getInstance(context).showAtView(item);
                click.Onclick(getData());});
            a.setTextColor(Color.parseColor("#515050"));
            b.setTextColor(Color.parseColor("#515050"));
            c.setTextColor(Color.parseColor("#515050"));
            a.setTextSize(13);
            b.setTextSize(13);
            c.setTextSize(12);
        }

        @Override
        protected void onSetData(int position, CarBean carBean) {
//            a.setText(carBean.getCphm());
//            b.setText(carBean.getHplb());
//            c.setText(carBean.getJckssj());
            if(divider.getVisibility()==View.GONE&&position<getAdapter().getDataCount()-1){
                divider.setVisibility(View.VISIBLE);
            }
        }
    }
}
