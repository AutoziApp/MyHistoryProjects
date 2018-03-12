package com.atv.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.atv.model.TreeNode;
import com.atv.model.TreeNode.TreeNodeClickListener;
import com.goldnut.app.sdk.R;

import java.lang.reflect.Field;

/**
 * 可多选树列表
 * 
 * @author zxl
 * 
 */
public class MulticheckTreeViewHolder extends
		TreeNode.BaseNodeViewHolder<Object> {

	private static int textSize = 30;

	private CheckBox checkBox;

	public MulticheckTreeViewHolder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MulticheckTreeViewHolder(Context context,
			TreeNodeClickListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View createNodeView(final TreeNode node, final Object value) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_treenode_multicheck, null,
				false);
		TextView name = (TextView) view.findViewById(R.id.node_name);
		final ImageView ico = (ImageView) view.findViewById(R.id.node_icon);

		checkBox = (CheckBox) view.findViewById(R.id.node_check);

		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkChildNode(node);
				checkParentNode(node);
			}
		});
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
//				try {
//					setProperty(value, "checked", isChecked);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				node.setChecked(isChecked);
			}
		});

		if (node.getChildren().size() > 0) {
			ico.setVisibility(View.VISIBLE);
		} else {
			ico.setVisibility(View.INVISIBLE);
		}
		
		checkBox.setChecked(node.isChecked());
		name.setText(value.toString());
			
//		try {
//			if((boolean)getProperty(value, "checked")){
//				checkBox.setChecked(true);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}

		// if (node.isRoot()) {
		// space.setVisibility(View.GONE);
		// } else {
		// space.setVisibility(View.VISIBLE);
		// space.setX(node.getLevel());
		// }

		name.setTextSize(textSize - (node.getLevel() - 1) * 5);
		
		if (!node.isExpanded()) {
			ico.setImageResource(R.drawable.ic_remove);
		} else {
			ico.setImageResource(R.drawable.ic_add);
		}

		node.setClickListener(new TreeNodeClickListener() {

			@Override
			public void onClick(TreeNode node, Object value) {
				// TODO Auto-generated method stub
				if (!node.isExpanded()) {
					ico.setImageResource(R.drawable.ic_remove);
				} else {
					ico.setImageResource(R.drawable.ic_add);
				}
				if (mClickListener != null) {
					mClickListener.onClick(node, value);
				}
			}
		});

		return view;
	}
	
	public Object getProperty(Object owner, String fieldName) throws Exception { 
        Class ownerClass = owner.getClass(); 
        Field field = ownerClass.getDeclaredField(fieldName); 
        Object property = field.get(owner); 
        return property; 
    } 
	
	public Object setProperty(Object owner, String fieldName,Object value) throws Exception { 
        Class ownerClass = owner.getClass(); 
        Field field = ownerClass.getDeclaredField(fieldName); 
        field.setAccessible(true);
        field.set(owner, value);
        return owner; 
    } 
	
	private void checkChildNode(TreeNode node){
		if (node.getChildren().size() > 0) {// 选择父节点
			for (TreeNode temp : node.getChildren()) {
				((MulticheckTreeViewHolder) temp.getViewHolder())
						.setChecked(checkBox.isChecked());
				checkChildNode(temp);
			}
		}
		
	}
	
	private void checkParentNode(TreeNode node){
		if (node.getParent() != null && node.getLevel() >1) {
			boolean isAllChecked = true;
			for (TreeNode temp : node.getParent().getChildren()) {
				if (!((MulticheckTreeViewHolder) temp.getViewHolder()).getChecked()) {
					isAllChecked = false;
					break;
				}
			}
			((MulticheckTreeViewHolder) node.getParent().getViewHolder()).setChecked(isAllChecked);
			checkParentNode(node.getParent());
		}
	}

	public void setChecked(boolean isChecked) {
		this.checkBox.setChecked(isChecked);
	}

	public boolean getChecked() {
		return this.checkBox.isChecked();
	}

	public interface OnTreeNodeClickListener {
		public void onClick(TreeNode node);
	}
}
