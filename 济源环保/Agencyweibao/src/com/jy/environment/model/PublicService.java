package com.jy.environment.model;

import java.util.ArrayList;
import java.util.List;

public class PublicService {
	private boolean flag;
	private List<PublicServiceItem> list = new ArrayList<PublicServiceItem>() ;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public List<PublicServiceItem> getList() {
		return list;
	}
	public void setList(List<PublicServiceItem> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "PublicService [flag=" + flag + ", list=" + list + "]";
	}
	
}
