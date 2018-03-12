package com.jy.environment.model;


public class ForecastWeekly{
        private boolean flag;
	private int aqi=0;	
	private String DateString="";
	private String AQIlevel="";
	private String AQInumber="";
	private String update_time="";
	
	
	public boolean isFlag() {
	    return flag;
	}

	public void setFlag(boolean flag) {
	    this.flag = flag;
	}

	public String getDateString() {
		return DateString;
	}
	
	public void setDateString(String date) {
		
		if ("".equals(date)||date == null ) {
			return ;
			}
		
		this.DateString = date;
	}
	
	public String getAQIlevel() {
		return AQIlevel;
	}
	
	public void setAQIlevel(String aqilevel) {
		if ("".equals(aqilevel)||aqilevel == null ) {
			return ;
			}
		
		this.AQIlevel = aqilevel;
	}
	
	public String getAQInumber() {
		return AQInumber;
	}
	public void setAQInumber(String aqinumber) {
		
		if ("".equals(aqinumber)||aqinumber == null ) {
			return ;
			}
		
		this.AQInumber = aqinumber;
	}

	public int getAqi() {
		return aqi;
	}
	
	public void setAqi(int aqi) {
		
		if (aqi < 0) {
			return ;
			}
		
		this.aqi = aqi;
	}

	public String getUpdate_time() {
		return update_time;
	}
	
	public void setUpdate_time(String update_time) {
		
		if ("".equals(update_time)||update_time == null ) {
			return ;
			}
		
		this.update_time = update_time;
	}

	@Override
	public String toString() {
	    return "ForecastWeekly [flag=" + flag + ", aqi=" + aqi
		    + ", DateString=" + DateString + ", AQIlevel=" + AQIlevel
		    + ", AQInumber=" + AQInumber + ", update_time="
		    + update_time + "]";
	}	
	
	
	
}
