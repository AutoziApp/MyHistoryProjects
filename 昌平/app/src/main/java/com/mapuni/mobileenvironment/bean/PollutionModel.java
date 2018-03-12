package com.mapuni.mobileenvironment.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PollutionModel implements Parcelable {
	private double LONGITUDE;
	private double LATITUDE;
	private String Name;
	private String Cod;
	private String SO2;
	private String NOx;
	private String nh;
	public double getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(double lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}
	public double getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(double lATITUDE) {
		LATITUDE = lATITUDE;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCod() {
		return Cod;
	}
	public void setCod(String cod) {
		Cod = cod;
	}
	public String getSO2() {
		return SO2;
	}
	public void setSO2(String sO2) {
		SO2 = sO2;
	}
	public String getNOx() {
		return NOx;
	}
	public void setNOx(String nOx) {
		NOx = nOx;
	}
	public String getNh() {
		return nh;
	}
	public void setNh(String nh) {
		this.nh = nh;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
}
