package com.atv.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atv.model.TreeNode;
import com.atv.model.TreeNode.TreeNodeClickListener;
import com.goldnut.app.sdk.R;

public class TreeViewHolder extends TreeNode.BaseNodeViewHolder<Object> {

	private static int textSize = 30;

	private static int[] colors = {30,60,90,120,150,180,210};

	public TreeViewHolder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public TreeViewHolder(Context context,TreeNodeClickListener listener) {
		super(context,listener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View createNodeView(final TreeNode node, final Object value) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_treenode, null, false);
		TextView name = (TextView) view.findViewById(R.id.node_name);
		final ImageView ico = (ImageView) view.findViewById(R.id.node_icon);

		if (node.getChildren().size() > 0) {
			ico.setVisibility(View.VISIBLE);
		} else {
			ico.setVisibility(View.INVISIBLE);
		}
		name.setText(value.toString());
		int color = colors[node.getLevel()%7];
		name.setTextColor(Color.rgb(color,color,color));
		

		node.setClickListener(new TreeNodeClickListener() {

			@Override
			public void onClick(TreeNode node, Object value) {
				// TODO Auto-generated method stub
				if (!node.isExpanded()) {
					ico.setImageResource(R.drawable.ic_remove);
				} else {
					ico.setImageResource(R.drawable.ic_add);
				}
				if(mClickListener!=null){
					mClickListener.onClick(node, value);
				}
			}
		});

		return view;
	}

}
