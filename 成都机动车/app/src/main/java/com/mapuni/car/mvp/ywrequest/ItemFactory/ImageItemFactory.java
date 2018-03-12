package com.mapuni.car.mvp.ywrequest.ItemFactory;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.mvp.view.NoScrollGridView;
import com.mapuni.car.mvp.ywrequest.model.ImageValueBean;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yawei on 2017/8/25.
 */

public class ImageItemFactory extends AssemblyRecyclerItemFactory<ImageItemFactory.CarItem> {

    @Override
    public boolean isTarget(Object data) {
        return data instanceof ImageValueBean;
    }


    @Override
    public CarItem createAssemblyItem(ViewGroup parent) {
        return new CarItem(R.layout.item_img_layout,parent);
    }

    public class CarItem extends AssemblyRecyclerItem<ImageValueBean> {
        TextView name, timeSelect, divider;
        NoScrollGridView nsgv_img;
        Context context;

        public CarItem(int itemLayoutId, ViewGroup parent) {
            super(itemLayoutId, parent);
        }

        @Override
        protected void onFindViews() {
            name = (TextView) findViewById(R.id.name);
            nsgv_img = (NoScrollGridView) findViewById(R.id.nsgv_img);
            divider = (TextView) findViewById(R.id.divider);
        }

        @Override
        protected void onConfigViews(Context context) {
            this.context = context;
        }

        @Override
        protected void onSetData(int position, ImageValueBean bean) {
               if(!TextUtils.isEmpty(bean.getValue())) {
                   String[] imgs = bean.getValue().split(",");
                   List<String> list = Arrays.asList(imgs);
                   if(imgs != null) {
                       nsgv_img.setAdapter(new CommonAdapter<String>(context,R.layout.item_text,list) {
                           @Override
                           protected void convert(ViewHolder viewHolder, String item, int position) {
                                viewHolder.getView(R.id.tv_tinted_spinner);
                                viewHolder.setText(R.id.tv_tinted_spinner,"证件"+(position+1));
                                viewHolder.setTextColor(R.id.tv_tinted_spinner,R.color.txt_link_blue);
                                TextView tv_name = viewHolder.getView(R.id.tv_tinted_spinner);
                                tv_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                                viewHolder.setOnClickListener(R.id.tv_tinted_spinner, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                           }
                       });
                   }
               }
                name.setText(bean.getName());



        }


    }


}
