package com.mapuni.android.business;

/**
 * FileName: Item.java
 * Description: 用于SpinnerAdapter<Item>里添加的对象
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午10:43:51
 */
public class SpinnerItem {
	
	/**在spinner里的value*/
	private String code;
	/**在spinner里显示的名字*/
	private String name;
	/**在spinner里的位置*/
	private int position;
	
	/**无参数的构造方法*/
	public SpinnerItem() {
	}
	
	/**
	 * 带参数的构造方法
	 * @param code value
	 * @param name 显示的名字
	 * @param position 在spinner里的位置
	 */
	public SpinnerItem(String code, String name,int position) {
		this.code = code;
		this.name = name;
		this.position = position;
	}
	
	/**
	 * 重写父类【Object】的构造方法
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**以下为Item属性的get和set方法*/
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
