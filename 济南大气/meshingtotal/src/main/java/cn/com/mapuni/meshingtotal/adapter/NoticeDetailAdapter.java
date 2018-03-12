package cn.com.mapuni.meshingtotal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.mapuni.meshing.base.util.DateTimeHelper;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.model.NoticeDetailBean;

/**
 * Created by 15225 on 2017/7/5.
 */

public class NoticeDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<NoticeDetailBean.DataBean> data=new ArrayList<>();
    private String contentTime;

    public NoticeDetailAdapter(Context context, List<NoticeDetailBean.DataBean> data,String contentTime) {
        mContext = context;
        this.data = data;
        this.contentTime=contentTime;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.notice_detail_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_Time= (TextView) convertView.findViewById(R.id.tv_Time);
            viewHolder.tv_OverValue= (TextView) convertView.findViewById(R.id.tv_OverValue);
            viewHolder.tv_Overtimes= (TextView) convertView.findViewById(R.id.tv_Overtimes);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        DecimalFormat df =new DecimalFormat("#.000");
        Date date=DateTimeHelper.parseStringToDate(contentTime);
        int hour=date.getHours();
        date.setHours(hour+position<=23?hour+position:(hour+position)-23);
        viewHolder.tv_Time.setText(DateTimeHelper.formatToString(date,"yyyy-MM-dd HH:mm:ss"));
        viewHolder.tv_OverValue.setText(df.format(data.get(position).getOverValue()));
        viewHolder.tv_Overtimes.setText(data.get(position).getOvertimes()+"");
        return convertView;
    }

    static class ViewHolder{
        TextView tv_Time;
        TextView tv_OverValue;
        TextView tv_Overtimes;
    }
}
