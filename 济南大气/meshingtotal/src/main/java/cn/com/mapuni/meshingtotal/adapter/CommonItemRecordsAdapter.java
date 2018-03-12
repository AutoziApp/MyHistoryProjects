package cn.com.mapuni.meshingtotal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.model.CommonItemRecords;

/**
 * Created by zhaijikui on 2016/4/7 0007.
 */
public class CommonItemRecordsAdapter extends BaseAdapter {

    private List<CommonItemRecords> data;
    private Context context;

    public CommonItemRecordsAdapter(Context context, List<CommonItemRecords> data) {
        this.context = context;
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
        ViewHodler viewHodler = new ViewHodler();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_common_sources, null);
            viewHodler.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHodler.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            viewHodler.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHodler.layoutRoot = (LinearLayout) convertView.findViewById(R.id.layoutRoot);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        if (position == 0) {
            viewHodler.layoutRoot.setBackgroundResource(R.drawable.bg_view_right_arrow_border);
        } else {
            viewHodler.layoutRoot.setBackgroundResource(R.drawable.bg_view_right_arrow_bottom_border);
        }
        viewHodler.tvName.setText(data.get(position).getName());
        viewHodler.tvDesc.setText(data.get(position).getDesc());
        Glide.with(context).load(String.valueOf(data.get(position).getImgPath())).placeholder(R.mipmap.pic_loading).error(R.mipmap.pic_loading_fail).into(viewHodler.ivImage);

        return convertView;
    }

    class ViewHodler {
        private TextView tvName;
        private TextView tvDesc;
        private ImageView ivImage;
        private LinearLayout layoutRoot;
    }
}
