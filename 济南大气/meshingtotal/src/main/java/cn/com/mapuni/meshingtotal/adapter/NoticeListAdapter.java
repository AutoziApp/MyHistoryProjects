package cn.com.mapuni.meshingtotal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.model.NoticeBean;

/**
 * Created by 15225 on 2017/7/5.
 */

public class NoticeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<NoticeBean.DataBean> data=new ArrayList<>();

    public NoticeListAdapter(Context context, List<NoticeBean.DataBean> data) {
        mContext = context;
        this.data = data;
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
            convertView=View.inflate(mContext,R.layout.notice_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_EntName= (TextView) convertView.findViewById(R.id.tv_EntName);
            viewHolder.tv_OutportName= (TextView) convertView.findViewById(R.id.tv_OutportName);
            viewHolder.tv_PollutantName= (TextView) convertView.findViewById(R.id.tv_PollutantName);
            viewHolder.tv_Time= (TextView) convertView.findViewById(R.id.tv_Time);
            viewHolder.tv_alarmType= (TextView) convertView.findViewById(R.id.tv_alarmType);
            viewHolder.tv_alarmData= (TextView) convertView.findViewById(R.id.tv_alarmData);
            viewHolder.tv_alarmDataMutiple= (TextView) convertView.findViewById(R.id.tv_alarmDataMutiple);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        DecimalFormat df =new DecimalFormat("#.00");
        viewHolder.tv_EntName.setText(data.get(position).getEntName());
        viewHolder.tv_OutportName.setText(data.get(position).getOutportName());
        viewHolder.tv_PollutantName.setText(data.get(position).getPollutantName());
        viewHolder.tv_Time.setText(data.get(position).getMonitorTime()+"-"+data.get(position).getEndtime());
        viewHolder.tv_alarmType.setText(data.get(position).getAlarmType());
        viewHolder.tv_alarmData.setText(df.format(data.get(position).getMinData())+"-"+df.format(data.get(position).getMaxData())+"("+data.get(position).getUnit()+")");
        viewHolder.tv_alarmDataMutiple.setText(0+df.format((data.get(position).getMinData()-data.get(position).getStandardVal())/data.get(position).getStandardVal())+"-0"+df.format((data.get(position).getMaxData()-data.get(position).getStandardVal())/data.get(position).getStandardVal()));
        return convertView;
    }

    static class ViewHolder{
        TextView tv_EntName;
        TextView tv_OutportName;
        TextView tv_PollutantName;
        TextView tv_Time;
        TextView tv_alarmType;
        TextView tv_alarmData;
        TextView tv_alarmDataMutiple;
    }
}
