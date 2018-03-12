package com.jy.environment.model;

import java.util.ArrayList;
import java.util.List;

public class ChatMsg {
	private boolean  flag;
	private List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public List<ChatMsgEntity> getList() {
		return list;
	}
	public void setList(List<ChatMsgEntity> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "ChatMsg [flag=" + flag + ", list=" + list + "]";
	}
	
}
