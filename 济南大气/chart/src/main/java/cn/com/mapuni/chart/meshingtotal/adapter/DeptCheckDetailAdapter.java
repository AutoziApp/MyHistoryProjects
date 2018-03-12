package cn.com.mapuni.chart.meshingtotal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.mapuni.chart.meshingtotal.R;
import cn.com.mapuni.chart.meshingtotal.model.DeptDetailItemBean;

/**
 * Created by YZP on 2017/7/23.
 */
public class DeptCheckDetailAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<DeptDetailItemBean> items;

    public DeptCheckDetailAdapter(Context context, ArrayList<DeptDetailItemBean> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_dept_check_detail, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name_dept.setText(items.get(position).getName()+":");
        viewHolder.tv_context_dept.setText(items.get(position).getContext());
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tv_name_dept;
        public TextView tv_context_dept;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_name_dept = (TextView) rootView.findViewById(R.id.tv_name_dept);
            this.tv_context_dept = (TextView) rootView.findViewById(R.id.tv_context_dept);
        }

    }
}
