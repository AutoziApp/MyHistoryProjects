package cn.com.mapuni.meshing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import cn.com.mapuni.meshing.model.RiskItemRecords;
import cn.com.mapuni.meshingtotal.R;

/**
 * Created by zhaijikui on 2016/4/7 0007.
 */
public class RiskItemRecordsAdapter extends BaseAdapter {

    private List<RiskItemRecords> data;
    private Context context;
    public RiskItemRecordsAdapter(Context context, List<RiskItemRecords> data){
        this.context=context;
        this.data=data;
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
        ViewHodler viewHodler = new ViewHodler();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_risk_sources, null);
            viewHodler.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHodler.layoutRoot=(LinearLayout)convertView.findViewById(R.id.layoutRoot);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

       if(position==0){
           viewHodler.layoutRoot.setBackgroundResource(R.drawable.bg_view_right_arrow_border);
       }else{
           viewHodler.layoutRoot.setBackgroundResource(R.drawable.bg_view_right_arrow_bottom_border);
       }
        viewHodler.tvName.setText(data.get(position).getName());

        return convertView;
    }

    class ViewHodler {

        private TextView tvName;
        private LinearLayout layoutRoot;

    }
}
