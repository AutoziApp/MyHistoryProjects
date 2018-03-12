package com.mapuni.administrator.view.treeViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.administrator.R;
import com.mapuni.administrator.bean.AreaBean;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.view.treeViewHolder
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/14 17:32
 * @change
 * @chang time
 * @class describe
 */

public class CustomViewHolder extends TreeNode.BaseNodeViewHolder<AreaBean> {

    private ImageView mImgArrow;
    private CheckBox mCbCity;

    public CustomViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node,  AreaBean value) {
        View view = View.inflate(context, R.layout.layout_custon_view_holder, null);
        /** 根据层级来设置左边的缩进 */
        view.setPadding(value.getLevel() * 50, view.getPaddingTop(), view.getRight(), view.getPaddingBottom());
        TextView tvAreaName = (TextView) view.findViewById(R.id.tv_areaName);
        mImgArrow = (ImageView) view.findViewById(R.id.img_arrow);
        mCbCity = (CheckBox) view.findViewById(R.id.cb_city);

        mCbCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
                //将上一次选中的节点取消选中
                AndroidTreeView treeView=getTreeView();
                for(TreeNode treeNode:treeView.getSelected()){
                    treeView.selectNode(treeNode,false);
                }
                //将当前节点选中
                node.setSelected(isChecked);
            }
        });
        
        mCbCity.setChecked(node.isSelected());

        tvAreaName.setText(value.getText());
        return view;
    }


    @Override
    public void toggle(boolean active) {
        if (active) {
            mImgArrow.animate().rotation(90).setDuration(200).start();
        } else {
            mImgArrow.animate().rotation(0).setDuration(200).start();
        }
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {

        mCbCity.setChecked(mNode.isSelected());
    }
}
