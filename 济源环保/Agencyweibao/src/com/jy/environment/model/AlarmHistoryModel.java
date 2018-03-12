package com.jy.environment.model;

import java.util.List;

import com.jy.environment.database.model.ModelAlarmHistory;

public class AlarmHistoryModel {
	private boolean flag;
	private List<ModelAlarmHistory> list;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public List<ModelAlarmHistory> getList() {
		return list;
	}

	public void setList(List<ModelAlarmHistory> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "AlarmHistoryModel [flag=" + flag + ", lists=" + list + "]";
	}
}