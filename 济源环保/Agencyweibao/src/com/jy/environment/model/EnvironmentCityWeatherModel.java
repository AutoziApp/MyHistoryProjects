package com.jy.environment.model;

import java.io.Serializable;

public class EnvironmentCityWeatherModel implements Serializable{

    private String climate;
    private String temp;
    public String getClimate() {
        return climate;
    }
    public void setClimate(String climate) {
        this.climate = climate;
    }
    public String getTemp() {
        return temp;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }
	@Override
	public String toString() {
		return "EnvironmentCityWeatherModel [climate=" + climate + ", temp="
				+ temp + "]";
	}
    
    
}
