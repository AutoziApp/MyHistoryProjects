package com.jy.environment.model;

import java.util.List;

public class ProvinceCityData {

    private boolean flag;
    private List<ProvinceCity> data;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<ProvinceCity> getData() {
        return data;
    }

    public void setData(List<ProvinceCity> data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "ProvinceCityData [flag=" + flag + ", data=" + data + "]";
	}
    
}
