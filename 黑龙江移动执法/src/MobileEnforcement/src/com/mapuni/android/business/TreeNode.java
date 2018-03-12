package com.mapuni.android.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: TreeNode.java
 * Description: 执法清单结点 内容或者专项的结点，封装需要的信息
 * @author 柳思远
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 下午01:57:40
 */
public class TreeNode implements Serializable, Comparable<TreeNode> {

	/**序列化的号码*/
	private  final long serialVersionUID = 19881121L;
	/**父节点*/
	public TreeNode parent;
	/**子节点*/
	public List<TreeNode> children = new ArrayList<TreeNode>(); 

	/* 属性*/
	/**节点名称*/
	public String title;
	/**是否显示小图标,-1表示隐藏图标*/
	public int icon = -1;
	/**是否处于展开状态*/
	public boolean isExpanded = true;
	/**颜色*/
	public int color = 0;

	/* 添加属性*/
	/**任务id*/
	public String rwID;
	/**企业id*/
	public String qyID;
	/**行业ID*/
	public String TID;
	/**专项ID*/
	public String SpecialItemID;
	/**专项级别*/
	public String SpecialItemLevel;
	/**专项内容*/
	public String SpecialItemContent;
	/**值是否和内容在同一单元格内（直接填写意见）*/
	public String ValueInColumn;
	/**结果类型*/
	public String ResultTypeID;
	/**是否加载上次*/
	public String isLoadLastResult;
	/**是否是内容*/
	public String isContent;
	/**备注信息*/
	public String RemarkTip;
	/**状态*/
	public String status; 
	/**排序*/
	public String sortIndex;

	// 结点标色值：没有启用
	// public static final int DEFAULT = 0;
	// public static final int GREEN = 1;
	// public static final int RED = 2;
	// public static final int BLUE = 3;
	/**判断当前节点是否被修改过*/
	public boolean state = false;

	/**
	 * Description: 设置当前节点的状态
	 * @param state
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:02:27
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * Description: 得到当前节点的状态
	 * @return  ture：展开，false：不展开
	 * boolean
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:03:22
	 */
	public boolean getState() {
		return this.state;
	}

	/**
	 * 空构造
	 */
	public TreeNode() {

	}

	/**
	 * TreeNode构造函数
	 * @param title 节点显示的文字
	 * @param SpecialItemContent 专项内容
	 */
	public TreeNode(String title, String SpecialItemContent) {
		this.title = title;
		this.SpecialItemContent = SpecialItemContent;
	}

	/**
	 * 多参数构造
	 * 
	 * @param text 节点的文字
	 * @param specialItemID 专项id
	 * @param specialItemLevel 专项级别
	 * @param specialItemContent 专项内容
	 * @param valueInColumn 在数据库对应列里的值
	 * @param resultTypeID 结果类型
	 * @param isLoadLastResult 是否是最后结果
	 * @param remarkTip 备注信息
	 */
	public TreeNode(String title, String specialItemID,
			String specialItemLevel, String specialItemContent,
			String valueInColumn, String resultTypeID, String isLoadLastResult,
			String remarkTip) {
		super();
		this.title = title;
		SpecialItemID = specialItemID;
		SpecialItemLevel = specialItemLevel;
		SpecialItemContent = specialItemContent;
		ValueInColumn = valueInColumn;
		ResultTypeID = resultTypeID;
		this.isLoadLastResult = isLoadLastResult;
		RemarkTip = remarkTip;
	}

	/**
	 * Description: 设置父节点
	 * @param node 节点
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:08:07
	 */
	public void setParent(TreeNode node) {
		this.parent = node;
	}

	/**
	 * Description: 获得父节点
	 * @return 返回一个节点
	 * TreeNode
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:08:27
	 */
	public TreeNode getParent() {
		return this.parent;
	}

	/**
	 * Description: 是否根节点
	 * @return true：是，false：否
	 * boolean
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:08:48
	 */
	public boolean isRoot() {
		return parent == null ? true : false;
	}

	/**
	 * Description: 获得子节点
	 * @return 子节点集合
	 * List<TreeNode>
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:09:22
	 */
	public List<TreeNode> getChildren() {
		return this.children;
	}

	/**
	 * Description: 添加子节点
	 * @param node 节点
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:09:46
	 */
	public void add(TreeNode node) {
		if (!children.contains(node)) {
			children.add(node);
		}
	}

	/**
	 * 重写equals方法，实现排序
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null)
			return false;
		if (o instanceof TreeNode) {
			TreeNode node = (TreeNode) o;
			if (this.SpecialItemID == node.SpecialItemID) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * Description: 清除所有子节点
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:10:17
	 */
	public void clear() {
		children.clear();
	}

	/**
	 * Description: 删除一个子节点
	 * @param node 节点
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:10:30
	 */
	public void remove(TreeNode node) {
		if (!children.contains(node)) {
			children.remove(node);
		}
	}

	/**
	 * Description: 删除指定位置的子节点
	 * @param location 位置
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:10:48
	 */
	public void remove(int location) {
		children.remove(location);
	}

	/**
	 * Description: 获得节点的级数,根节点为0
	 * @return 节点的级别
	 * int
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:11:05
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * Description: 是否叶节点,即没有子节点的节点
	 * @return true：是，false：否
	 * boolean
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:11:35
	 */
	public boolean isLeaf() {
		return children.size() < 1 ? true : false;
	}

	/**
	 * Description: 当前节点是否处于展开状态
	 * @return
	 * boolean ture：展开，false：否
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:11:59
	 */
	public boolean isExpanded() {
		return isExpanded;
	}

	/**
	 * Description: 设置节点展开状态
	 * @param isExpanded 展开状态
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-4 下午02:12:32
	 */
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	@Override
	public int compareTo(TreeNode node) {
		return Integer.parseInt(this.sortIndex)
				- Integer.parseInt(node.sortIndex);
	}

	@Override
	public String toString() {
		return "TreeNode [serialVersionUID=" + serialVersionUID + ", parent="
				+ parent + ", children=" + children + ", title=" + title
				+ ", icon=" + icon + ", isExpanded=" + isExpanded + ", color="
				+ color + ", rwID=" + rwID + ", qyID=" + qyID + ", TID=" + TID
				+ ", SpecialItemID=" + SpecialItemID + ", SpecialItemLevel="
				+ SpecialItemLevel + ", SpecialItemContent="
				+ SpecialItemContent + ", ValueInColumn=" + ValueInColumn
				+ ", ResultTypeID=" + ResultTypeID + ", isLoadLastResult="
				+ isLoadLastResult + ", isContent=" + isContent
				+ ", RemarkTip=" + RemarkTip + ", status=" + status
				+ ", sortIndex=" + sortIndex + ", state=" + state + "]";
	}
	
	

}
