package com.jy.environment.model;

import org.litepal.crud.DataSupport;

public class BiaoGeModel extends DataSupport {
	private int id;
	private String grade;
	private String money;
	private String award;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	@Override
	public String toString() {
		return "BiaoGeModel [grade=" + grade + ", money=" + money + ", award="
				+ award + "]";
	}

}
