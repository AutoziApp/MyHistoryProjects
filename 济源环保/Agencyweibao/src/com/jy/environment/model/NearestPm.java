package com.jy.environment.model;

/**
 * 附件位置的pm数据
 * @author baiyuchuan
 *
 */
public class NearestPm {

    private boolean flag = false ;
    private String  city;
    private String co ;
    private String so2 ;
    private String o3 ;
    private String no2 ;
    private String aqi ;
    private String pm10 ;
    private String longitude ;
    private String pm25 ; //pm2.5
    private String latitude ;
    private String primary_pollutant ;
    private String position_name ;
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getCo() {
        return co;
    }
    public void setCo(String co) {
        this.co = co;
    }
    public String getSo2() {
        return so2;
    }
    public void setSo2(String so2) {
        this.so2 = so2;
    }
    public String getO3() {
        return o3;
    }
    public void setO3(String o3) {
        this.o3 = o3;
    }
    public String getNo2() {
        return no2;
    }
    public void setNo2(String no2) {
        this.no2 = no2;
    }
    public String getAqi() {
        return aqi;
    }
    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
    public String getPm10() {
        return pm10;
    }
    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getPm25() {
        return pm25;
    }
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getPrimary_pollutant() {
        return primary_pollutant;
    }
    public void setPrimary_pollutant(String primary_pollutant) {
        this.primary_pollutant = primary_pollutant;
    }
    public String getPosition_name() {
        return position_name;
    }
    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }
    
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    @Override
    public String toString() {
	return "NearestPm [flag=" + flag + ", city=" + city + ", co=" + co
		+ ", so2=" + so2 + ", o3=" + o3 + ", no2=" + no2 + ", aqi="
		+ aqi + ", pm10=" + pm10 + ", longitude=" + longitude
		+ ", pm25=" + pm25 + ", latitude=" + latitude
		+ ", primary_pollutant=" + primary_pollutant
		+ ", position_name=" + position_name + "]";
    }
    public NearestPm(boolean flag, String city, String co, String so2,
	    String o3, String no2, String aqi, String pm10, String longitude,
	    String pm25, String latitude, String primary_pollutant,
	    String position_name) {
	super();
	this.flag = flag;
	this.city = city;
	this.co = co;
	this.so2 = so2;
	this.o3 = o3;
	this.no2 = no2;
	this.aqi = aqi;
	this.pm10 = pm10;
	this.longitude = longitude;
	this.pm25 = pm25;
	this.latitude = latitude;
	this.primary_pollutant = primary_pollutant;
	this.position_name = position_name;
    }
    
}
