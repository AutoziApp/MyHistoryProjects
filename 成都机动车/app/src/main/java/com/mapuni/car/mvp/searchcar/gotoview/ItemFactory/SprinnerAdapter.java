package com.mapuni.car.mvp.searchcar.gotoview.ItemFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mapuni.car.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZP on 2017/12/9.
 */
public class SprinnerAdapter extends BaseAdapter {
    private final Context context;
    private final String[] selectes;

    public SprinnerAdapter(Context context, String[] selectes) {
        this.context = context;
        this.selectes = selectes;
    }

    @Override
    public int getCount() {
        return selectes.length;
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
        ViewHolder holder=null;
        if (view == null) {
            view = View.inflate(context, R.layout.item_sprinner_layout, null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.name.setText( selectes[i]);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
