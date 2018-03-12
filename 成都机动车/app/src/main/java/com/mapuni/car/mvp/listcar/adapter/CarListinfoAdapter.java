package com.mapuni.car.mvp.listcar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;
import com.mapuni.car.mvp.listcar.model.CarCheckBean;
import com.mapuni.core.utils.CarColorUtils;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZP on 2017/11/23.
 */
public class CarListinfoAdapter extends BaseAdapter {
    private final Context context;
    private final String title1;
    private final int type;
    private final List<CarCheckBean.DataBean> data;
    String carcardcolor = "";

    public CarListinfoAdapter(Context context, String title1, int type, List<CarCheckBean.DataBean> data) {
        this.context = context;
        this.title1 = title1;
        this.type = type;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        // view = View.inflate(context, R.layout.item_car_listinfo, null);
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(context, R.layout.item_car_listinfo2, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvUnmCarinfo.setText(data.get(i).getCARCARDNUMBER());
        if (type == 4) {
            holder.viewRight.setVisibility(View.VISIBLE);
            holder.tvDealInfo.setVisibility(View.VISIBLE);
            LinkedHashMap<String, String> checkMethodMap = ConsTants.getCheckMethodMap();
            if (data.get(i).getSTATE().contains("未处理")) {
                holder.viewRight.setBackgroundResource(R.color.green);
            } else {
                holder.viewRight.setBackgroundResource(R.color.yellow);
            }
            holder.tvDealInfo.setText(data.get(i).getSTATE());
            if (title1.equals("修改检测方法")) {
                carcardcolor = data.get(i).getCHARCARDCOLOR();
                String checkmethod = data.get(i).getNEWCHECKMETHOD();
                holder.tvMuthCarinfo.setText("检测方法:" + checkMethodMap.get(checkmethod));
                holder.tvDateCarinfo.setText(data.get(i).getXGSJ());
            } else if (title1.equals("修改车辆信息")) {
                carcardcolor = data.get(i).getCARCARDCOLOR();
                holder.tvMuthCarinfo.setVisibility(View.GONE);
                holder.tvDateCarinfo.setText(data.get(i).getAPPLYTIME());
            } else if (title1.equals("跨站检测解锁")) {
                carcardcolor = data.get(i).getCARCARDCOLOR();
                holder.tvMuthCarinfo.setVisibility(View.GONE);
                holder.tvDateCarinfo.setText(data.get(i).getAPPLYTIME());
//                if (data.get(i).getSTATE().contains("未处理")) {
//                    holder.viewRight.setBackgroundResource(R.color.green);
//                } else {
//                    holder.viewRight.setBackgroundResource(R.color.yellow);
//                }
                //          holder.tvDealInfo.setText(data.get(i).getSTATE());

            }

        } else {
            holder.viewRight.setVisibility(View.GONE);
            carcardcolor = data.get(i).getCARCARDCOLOR();
            holder.tvMuthCarinfo.setText("检测方法:" + data.get(i).getCHECKMETHOD());
            if (title1.equals("修改检测方法")) {
                holder.tvDateCarinfo.setText(data.get(i).getCHECKDATE());
                holder.tvDealInfo.setText(data.get(i).getSTATE());
                holder.tvDealInfo.setVisibility(View.VISIBLE);
            } else if (title1.equals("可修改列表")) {
                holder.tvDateCarinfo.setText(data.get(i).getCHECKTIME());
               holder.tvMuthCarinfo.setVisibility(View.GONE);
                holder.tvDealInfo.setVisibility(View.GONE);
            } else {
                holder.tvDealInfo.setVisibility(View.GONE);
                holder.tvDateCarinfo.setText(data.get(i).getINPUTTIME());
            }
        }
        int icon = CarColorUtils.getIcon(carcardcolor);
        holder.ivTypeCarinfo.setBackgroundResource(icon);
        holder.tvTypeCarinfo.setText(carcardcolor);
        if ("白牌".equals(carcardcolor)) {
            holder.tvTypeCarinfo.setTextColor(context.getResources().getColor(R.color.txt_gray));
        }
//        String carcardcolor = data.get(i).getCARCARDCOLOR();
//        int icon = CarColorUtils.getIcon(carcardcolor);
//        holder.ivTypeCarinfo.setBackgroundResource(icon);
//        holder.tvTypeCarinfo.setText(carcardcolor);
//        holder.tvUnmCarinfo.setText(data.get(i).getCARCARDNUMBER());
//        if("白牌".equals( carcardcolor)){
//            holder.tvTypeCarinfo.setTextColor(context.getResources().getColor(R.color.txt_gray));
//        }
//        if ((title1.equals("修改车辆信息") || title1.equals("跨站检测解锁") || title1.equals("修改检测方法") && type == 4)) {
//            holder.viewRight.setVisibility(View.VISIBLE);
//            holder.tvDealInfo.setVisibility(View.VISIBLE);
//            holder.tvMuthCarinfo.setText("检测方法:" + data.get(i).getCHECKMETHOD());
//        } else {
//            holder.viewRight.setVisibility(View.GONE);
//            holder.tvDealInfo.setVisibility(View.GONE);
//            holder.tvUnmCarinfo.setText(data.get(i).getCARCARDNUMBER());
//            if (title1.equals("可修改列表")) {
//                holder.tvDateCarinfo.setText(data.get(i).getCHECKTIME());
//                holder.tvMuthCarinfo.setVisibility(View.GONE);
//            } else {
//                holder.tvDateCarinfo.setText(data.get(i).getINPUTTIME());
//                holder.tvMuthCarinfo.setText("检测方法:" + data.get(i).getCHECKMETHOD());
//            }
//        }
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.iv_type_carinfo)
        ImageView ivTypeCarinfo;
        @BindView(R.id.tv_type_carinfo)
        TextView tvTypeCarinfo;
        @BindView(R.id.rl_carinfo)
        RelativeLayout rlCarinfo;
        @BindView(R.id.tv_unm_carinfo)
        TextView tvUnmCarinfo;
        @BindView(R.id.tv_muth_carinfo)
        TextView tvMuthCarinfo;
        @BindView(R.id.view_right)
        TextView viewRight;
        @BindView(R.id.tv_date_carinfo)
        TextView tvDateCarinfo;
        @BindView(R.id.tv_deal_info)
        TextView tvDealInfo;
        @BindView(R.id.divider)
        TextView divider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
