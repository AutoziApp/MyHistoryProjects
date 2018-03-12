package com.atv.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.atv.model.TreeNode;
import com.atv.model.TreeNode.TreeNodeClickListener;

/**
 * Created by Bogdan Melnychuk on 2/11/15.
 */
public class SimpleViewHolder extends TreeNode.BaseNodeViewHolder<Object> {

    public SimpleViewHolder(Context context) {
        super(context);
    }
    
    public SimpleViewHolder(Context context,TreeNodeClickListener listener) {
        super(context,listener);
    }

    @Override
    public View createNodeView(TreeNode node, Object value) {
        final TextView tv = new TextView(context);
        tv.setText(String.valueOf(value));
        return tv;
    }

    @Override
    public void toggle(boolean active) {

    }
}
