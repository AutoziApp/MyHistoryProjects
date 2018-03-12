package com.mapuni.car.mvp.detailcar.ItemFactory;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.detailcar.model.ImageValueBean;
import com.mapuni.car.mvp.view.NoScrollGridView;
import com.mapuni.car.mvp.view.ZoomImageView;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItem;
import com.mapuni.core.assemblyadapter.AssemblyRecyclerItemFactory;
import com.mapuni.core.utils.DisplayUtil;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yawei on 2017/8/25.
 */

public class ImageItemFactory extends AssemblyRecyclerItemFactory<ImageItemFactory.CarItem> {
    PopupWindow popupWindow;
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
                                        showPopwindow(list.get(position),tv_name,context);
                                    }
                                });
                           }
                       });
                   }
               }
                name.setText(bean.getName());

        }


    }
    private void showPopwindow(String path,View view,Context context) {
               String imgUrl=ConsTants.Base_Url+"/jdcapp/common/get-view-img.action?imgPath="+path;
                if(popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                   }
                View zjtx_layout = View.inflate(context, R.layout.item_img, null);
                ImageView ivCancle = (ImageView) zjtx_layout.findViewById(R.id.iv_cancle);
                ZoomImageView iv_zjzp = (ZoomImageView) zjtx_layout.findViewById(R.id.iv_zjzp);
                Picasso.with(context).load(imgUrl).resize(DisplayUtil.dip2px(context,300),DisplayUtil.dip2px(context,400)).into(iv_zjzp);
                popupWindow = new PopupWindow(zjtx_layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                popupWindow.setTouchable(true);
                ColorDrawable cds = new ColorDrawable(0x000000);
               popupWindow.setBackgroundDrawable(cds);
//        	        setBackgroundAlpha(TestActivity.this,(float)0.5);
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                ivCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                popupWindow.dismiss();
                           }
                   });

           }


}
