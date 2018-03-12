package com.mapuni.android.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: TreeNode.java
 * Description: ִ���嵥��� ���ݻ���ר��Ľ�㣬��װ��Ҫ����Ϣ
 * @author ��˼Զ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����01:57:40
 */
public class TreeNode implements Serializable, Comparable<TreeNode> {

	/**���л��ĺ���*/
	private  final long serialVersionUID = 19881121L;
	/**���ڵ�*/
	public TreeNode parent;
	/**�ӽڵ�*/
	public List<TreeNode> children = new ArrayList<TreeNode>(); 

	/* ����*/
	/**�ڵ�����*/
	public String title;
	/**�Ƿ���ʾСͼ��,-1��ʾ����ͼ��*/
	public int icon = -1;
	/**�Ƿ���չ��״̬*/
	public boolean isExpanded = true;
	/**��ɫ*/
	public int color = 0;

	/* �������*/
	/**����id*/
	public String rwID;
	/**��ҵid*/
	public String qyID;
	/**��ҵID*/
	public String TID;
	/**ר��ID*/
	public String SpecialItemID;
	/**ר���*/
	public String SpecialItemLevel;
	/**ר������*/
	public String SpecialItemContent;
	/**ֵ�Ƿ��������ͬһ��Ԫ���ڣ�ֱ����д�����*/
	public String ValueInColumn;
	/**�������*/
	public String ResultTypeID;
	/**�Ƿ�����ϴ�*/
	public String isLoadLastResult;
	/**�Ƿ�������*/
	public String isContent;
	/**��ע��Ϣ*/
	public String RemarkTip;
	/**״̬*/
	public String status; 
	/**����*/
	public String sortIndex;

	// ����ɫֵ��û������
	// public static final int DEFAULT = 0;
	// public static final int GREEN = 1;
	// public static final int RED = 2;
	// public static final int BLUE = 3;
	/**�жϵ�ǰ�ڵ��Ƿ��޸Ĺ�*/
	public boolean state = false;

	/**
	 * Description: ���õ�ǰ�ڵ��״̬
	 * @param state
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:02:27
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * Description: �õ���ǰ�ڵ��״̬
	 * @return  ture��չ����false����չ��
	 * boolean
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:03:22
	 */
	public boolean getState() {
		return this.state;
	}

	/**
	 * �չ���
	 */
	public TreeNode() {

	}

	/**
	 * TreeNode���캯��
	 * @param title �ڵ���ʾ������
	 * @param SpecialItemContent ר������
	 */
	public TreeNode(String title, String SpecialItemContent) {
		this.title = title;
		this.SpecialItemContent = SpecialItemContent;
	}

	/**
	 * ���������
	 * 
	 * @param text �ڵ������
	 * @param specialItemID ר��id
	 * @param specialItemLevel ר���
	 * @param specialItemContent ר������
	 * @param valueInColumn �����ݿ��Ӧ�����ֵ
	 * @param resultTypeID �������
	 * @param isLoadLastResult �Ƿ��������
	 * @param remarkTip ��ע��Ϣ
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
	 * Description: ���ø��ڵ�
	 * @param node �ڵ�
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:08:07
	 */
	public void setParent(TreeNode node) {
		this.parent = node;
	}

	/**
	 * Description: ��ø��ڵ�
	 * @return ����һ���ڵ�
	 * TreeNode
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:08:27
	 */
	public TreeNode getParent() {
		return this.parent;
	}

	/**
	 * Description: �Ƿ���ڵ�
	 * @return true���ǣ�false����
	 * boolean
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:08:48
	 */
	public boolean isRoot() {
		return parent == null ? true : false;
	}

	/**
	 * Description: ����ӽڵ�
	 * @return �ӽڵ㼯��
	 * List<TreeNode>
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:09:22
	 */
	public List<TreeNode> getChildren() {
		return this.children;
	}

	/**
	 * Description: ����ӽڵ�
	 * @param node �ڵ�
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:09:46
	 */
	public void add(TreeNode node) {
		if (!children.contains(node)) {
			children.add(node);
		}
	}

	/**
	 * ��дequals������ʵ������
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
	 * Description: ��������ӽڵ�
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:10:17
	 */
	public void clear() {
		children.clear();
	}

	/**
	 * Description: ɾ��һ���ӽڵ�
	 * @param node �ڵ�
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:10:30
	 */
	public void remove(TreeNode node) {
		if (!children.contains(node)) {
			children.remove(node);
		}
	}

	/**
	 * Description: ɾ��ָ��λ�õ��ӽڵ�
	 * @param location λ��
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:10:48
	 */
	public void remove(int location) {
		children.remove(location);
	}

	/**
	 * Description: ��ýڵ�ļ���,���ڵ�Ϊ0
	 * @return �ڵ�ļ���
	 * int
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:11:05
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * Description: �Ƿ�Ҷ�ڵ�,��û���ӽڵ�Ľڵ�
	 * @return true���ǣ�false����
	 * boolean
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:11:35
	 */
	public boolean isLeaf() {
		return children.size() < 1 ? true : false;
	}

	/**
	 * Description: ��ǰ�ڵ��Ƿ���չ��״̬
	 * @return
	 * boolean ture��չ����false����
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:11:59
	 */
	public boolean isExpanded() {
		return isExpanded;
	}

	/**
	 * Description: ���ýڵ�չ��״̬
	 * @param isExpanded չ��״̬
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-4 ����02:12:32
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
