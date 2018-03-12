package com.jy.environment.model;

public class ManageCity {

	String cityName;
	String climate;
	String temp;
	
	
	
	public ManageCity(String cityName, String climate, String temp) {
		super();
		this.cityName = cityName;
		this.climate = climate;
		this.temp = temp;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
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
	    return "ManageCity [cityName=" + cityName + ", climate=" + climate
		    + ", temp=" + temp + "]";
	}
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
		    + ((cityName == null) ? 0 : cityName.hashCode());
	    result = prime * result
		    + ((climate == null) ? 0 : climate.hashCode());
	    result = prime * result + ((temp == null) ? 0 : temp.hashCode());
	    return result;
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    ManageCity other = (ManageCity) obj;
	    if (cityName == null) {
		if (other.cityName != null)
		    return false;
	    } else if (!cityName.equals(other.cityName))
		return false;
	    if (climate == null) {
		if (other.climate != null)
		    return false;
	    } else if (!climate.equals(other.climate))
		return false;
	    if (temp == null) {
		if (other.temp != null)
		    return false;
	    } else if (!temp.equals(other.temp))
		return false;
	    return true;
	}
	
	
}
