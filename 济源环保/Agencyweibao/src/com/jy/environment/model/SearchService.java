package com.jy.environment.model;

import java.util.List;
//搜索公共服务实体类
public class SearchService {
	private boolean flag;
	private List<SearchServiceItem> lists;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public List<SearchServiceItem> getList() {
		return lists;
	}
	public void setList(List<SearchServiceItem> lists) {
		this.lists = lists;
	}
	@Override
	public String toString() {
		return "SearchService [flag=" + flag + ", lists=" + lists + "]";
	}
	
}
