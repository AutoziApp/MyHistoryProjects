package com.zhy.tree.bean;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367
 * @author zhy
 *
 * @param <T>
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter
{

	protected Context mContext;
	/**
	 * �洢���пɼ���Node
	 */
	protected List<Node> mNodes;
	protected LayoutInflater mInflater;
	/**
	 * �洢���е�Node
	 */
	protected List<Node> mAllNodes;

	/**
	 * ����Ļص��ӿ�
	 */
	private OnTreeNodeClickListener onTreeNodeClickListener;

	public interface OnTreeNodeClickListener
	{
		void onClick(Node node, int position);
	}

	public void setOnTreeNodeClickListener(
			OnTreeNodeClickListener onTreeNodeClickListener)
	{
		this.onTreeNodeClickListener = onTreeNodeClickListener;
	}

	/**
	 * 
	 * @param mTree
	 * @param context
	 * @param datas
	 * @param defaultExpandLevel
	 *            Ĭ��չ��������
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public TreeListViewAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		mContext = context;
		/**
		 * �����е�Node��������
		 */
		mAllNodes = TreeHelper.getSortedNodes(datas, 0);
		/**
		 * ���˳��ɼ���Node
		 */
		mNodes = TreeHelper.filterVisibleNode(mAllNodes);
		mInflater = LayoutInflater.from(context);

		/**
		 * ���ýڵ���ʱ������չ���Լ��رգ����ҽ�ItemClick�¼��������⹫��
		 */
		mTree.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				expandOrCollapse(position);

				if (onTreeNodeClickListener != null)
				{
					onTreeNodeClickListener.onClick(mNodes.get(position),
							position);
				}
			}

		});

	}

	/**
	 * ��ӦListView�ĵ���¼� չ����ر�ĳ�ڵ�
	 * 
	 * @param position
	 */
	public void expandOrCollapse(int position)
	{
		Node n = mNodes.get(position);

		if (n != null)// �ų�������������쳣
		{
			if (!n.isLeaf())
			{
				n.setExpand(!n.isExpand());
				mNodes = TreeHelper.filterVisibleNode(mAllNodes);
				notifyDataSetChanged();// ˢ����ͼ
			}
		}
	}

	@Override
	public int getCount()
	{
		return mNodes.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mNodes.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Node node = mNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		// �����ڱ߾�
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		return convertView;
	}

	public abstract View getConvertView(Node node, int position,
			View convertView, ViewGroup parent);

}
