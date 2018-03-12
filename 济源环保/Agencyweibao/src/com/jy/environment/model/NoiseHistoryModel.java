package com.jy.environment.model;

import java.util.List;

public class NoiseHistoryModel {
	private String flag;
	private List<NoiseItemModel> list;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<NoiseItemModel> getList() {
		return list;
	}
	public void setList(List<NoiseItemModel> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "NoiseHistoryModel [flag=" + flag + ", list=" + list + "]";
	}



	
	

}
