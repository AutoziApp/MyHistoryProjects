package com.mapuni.car.mvp.searchcar.gotoview.assemblywidght;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.searchcar.gotoview.model.CarListBean;

import java.util.List;

/**
 * Created by YZP on 2017/11/22.
 */
public class CarInfoOwnerAdapter extends BaseAdapter{
    private final List<CarListBean> carListBeen;
    private static final int TYPE_1 = 0;
    private static final int TYPE_2 = 1;
    private static final int TYPE_3 = 2;
    private final Activity activity;
    private String[] colors;

    public CarInfoOwnerAdapter(Activity activity, List<CarListBean> carListBeen) {
        this.activity=activity;
        this.carListBeen=carListBeen;
    }


    @Override
    public int getCount() {
        return carListBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (carListBeen.get(position).getFlag() == 0) {
            return TYPE_1;
        }
        if (carListBeen.get(position).getFlag() == 1) {
            return TYPE_2;
        }
        if(carListBeen.get(position).getFlag() == 2){
            return TYPE_1;
        }

        return TYPE_1;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);
        switch (itemViewType){
            case TYPE_1:
                view = View.inflate(activity, R.layout.item_carinfo_type1, null);
                initType1(view,i);
                break;
            case TYPE_2:
                view = View.inflate(activity,R.layout.item_carinfo_type2, null);
                initType2(view,i);
                break;
            case TYPE_3:
                view = View.inflate(activity,R.layout.item_carinfo_type1, null);
                initType1(view,i);
                break;
        }
        return view;
    }

    private void initType2(View view, int i) {
        TextView tvName= (TextView) view.findViewById(R.id.tv_name_type2);
//        TextView tvSelect2 = (TextView) view.findViewById(R.id.tv_select_type2);
        tvName.setText(carListBeen.get(i).getName());
//        tvSelect2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    private void initType1(View view, int i) {
        TextView tvName= (TextView) view.findViewById(R.id.tv_name_carinfo);
        tvName.setText(carListBeen.get(i).getName()+":"+carListBeen.get(i).getValue());
    }

}
