package cn.com.mapuni.meshingtotal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.com.mapuni.meshing.base.util.PollutionLevelCalUtil;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.model.PollutionInfo;

/**
 * Created by 15225 on 2017/7/12.
 */

public class StationListAdapter extends BaseAdapter {
    private Context mContext;
    private List<PollutionInfo.MsgBean.StationInfoBean> mList;

    public StationListAdapter(Context context, List<PollutionInfo.MsgBean.StationInfoBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.item_station_list,null);
            viewHolder=new ViewHolder();
            viewHolder.tvStationName= (TextView) convertView.findViewById(R.id.tv_station_name);
            viewHolder.tvStationAqiValue= (TextView) convertView.findViewById(R.id.tv_station_aqi_value);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.tvStationName.setText(mList.get(position).getStationName());
        if(mList.get(position).getAqi()!=null){
        viewHolder.tvStationAqiValue.setText(PollutionLevelCalUtil.getLevelDes("aqi",Double.parseDouble(mList.get(position).getAqi()))+" "+(int)Double.parseDouble(mList.get(position).getAqi().trim()));
        viewHolder.tvStationAqiValue.setBackgroundResource(PollutionLevelCalUtil.getLevelIcon("aqi",Double.parseDouble(mList.get(position).getAqi())));}

            return convertView;
    }

    static class ViewHolder{
        TextView tvStationName;
        TextView tvStationAqiValue;
    }

}
