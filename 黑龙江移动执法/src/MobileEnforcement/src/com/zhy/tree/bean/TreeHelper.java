package com.zhy.tree.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mapuni.android.MobileEnforcement.R;

/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367
 * @author zhy
 *
 */
public class TreeHelper
{
	/**
	 * 浼犲叆鎴戜滑鐨勬櫘閫歜ean锛岃浆鍖栦负鎴戜滑鎺掑簭鍚庣殑Node
	 * 
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> List<Node> getSortedNodes(List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException

	{
		List<Node> result = new ArrayList<Node>();
		// 灏嗙敤鎴锋暟鎹浆鍖栦负List<Node>
		List<Node> nodes = convetData2Node(datas);
		// 鎷垮埌鏍硅妭鐐�
		List<Node> rootNodes = getRootNodes(nodes);
		// 鎺掑簭浠ュ強璁剧疆Node闂村叧绯�
		for (Node node : rootNodes)
		{
			addNode(result, node, 2, 1);
		}
		return result;
	}

	/**
	 * 杩囨护鍑烘墍鏈夊彲瑙佺殑Node
	 * 
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNode(List<Node> nodes)
	{
		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes)
		{
			// 濡傛灉涓鸿窡鑺傜偣锛屾垨鑰呬笂灞傜洰褰曚负灞曞紑鐘舵��
			if (node.isRoot() || node.isParentExpand())
			{
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 灏嗘垜浠殑鏁版嵁杞寲涓烘爲鐨勮妭鐐�
	 * 
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static <T> List<Node> convetData2Node(List<T> datas)
			throws IllegalArgumentException, IllegalAccessException

	{
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;

		for (T t : datas)
		{
			int id = -1;
			int pId = -1;
			String label = null;
			Class<? extends Object> clazz = t.getClass();
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field f : declaredFields)
			{
				if (f.getAnnotation(TreeNodeId.class) != null)
				{
					f.setAccessible(true);
					id = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodePid.class) != null)
				{
					f.setAccessible(true);
					pId = f.getInt(t);
				}
				if (f.getAnnotation(TreeNodeLabel.class) != null)
				{
					f.setAccessible(true);
					label = (String) f.get(t);
				}
				if (id != -1 && pId != -1 && label != null)
				{
					break;
				}
			}
			node = new Node(id, pId, label);
			nodes.add(node);
		}

		/**
		 * 璁剧疆Node闂达紝鐖跺瓙鍏崇郴;璁╂瘡涓や釜鑺傜偣閮芥瘮杈冧竴娆★紝鍗冲彲璁剧疆鍏朵腑鐨勫叧绯�
		 */
		for (int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++)
			{
				Node m = nodes.get(j);
				if (m.getpId() == n.getId())
				{
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getpId())
				{
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}

		// 璁剧疆鍥剧墖
		for (Node n : nodes)
		{
			setNodeIcon(n);
		}
		return nodes;
	}

	private static List<Node> getRootNodes(List<Node> nodes)
	{
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes)
		{
			if (node.isRoot())
				root.add(node);
		}
		return root;
	}

	/**
	 * 鎶婁竴涓妭鐐逛笂鐨勬墍鏈夌殑鍐呭閮芥寕涓婂幓
	 */
	private static void addNode(List<Node> nodes, Node node,
			int defaultExpandLeval, int currentLevel)
	{

		nodes.add(node);
		if (defaultExpandLeval >= currentLevel)
		{
			node.setExpand(true);
		}

		if (node.isLeaf())
			return;
		for (int i = 0; i < node.getChildren().size(); i++)
		{
			addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
					currentLevel + 1);
		}
	}

	/**
	 * 璁剧疆鑺傜偣鐨勫浘鏍�
	 * 
	 * @param node
	 */
	private static void setNodeIcon(Node node)
	{
		if (node.getChildren().size() > 0 && node.isExpand())
		{
			node.setIcon(R.drawable.tree_ex);
		} else if (node.getChildren().size() > 0 && !node.isExpand())
		{
			node.setIcon(R.drawable.tree_ec);
		} else
			node.setIcon(-1);

	}

}
