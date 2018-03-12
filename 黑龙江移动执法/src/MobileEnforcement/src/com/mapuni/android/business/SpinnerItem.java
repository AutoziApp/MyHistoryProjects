package com.mapuni.android.business;

/**
 * FileName: Item.java
 * Description: ����SpinnerAdapter<Item>����ӵĶ���
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����10:43:51
 */
public class SpinnerItem {
	
	/**��spinner���value*/
	private String code;
	/**��spinner����ʾ������*/
	private String name;
	/**��spinner���λ��*/
	private int position;
	
	/**�޲����Ĺ��췽��*/
	public SpinnerItem() {
	}
	
	/**
	 * �������Ĺ��췽��
	 * @param code value
	 * @param name ��ʾ������
	 * @param position ��spinner���λ��
	 */
	public SpinnerItem(String code, String name,int position) {
		this.code = code;
		this.name = name;
		this.position = position;
	}
	
	/**
	 * ��д���ࡾObject���Ĺ��췽��
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**����ΪItem���Ե�get��set����*/
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
}
