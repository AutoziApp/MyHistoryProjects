package com.jy.environment.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
//import com.baidu.mapapi.search.core.PoiInfo;

//import com.baidu.mapapi.search.MKPoiInfo;

public class PoiInfo extends com.baidu.mapapi.search.core.PoiInfo implements Serializable{
	public String aaa="";
	public PoiInfo(com.baidu.mapapi.search.core.PoiInfo mk){
		this.address=mk.address;
		this.city=mk.city;
		this.type=mk.type;
		this.hasCaterDetails=mk.hasCaterDetails;
		this.name=mk.name;
		this.phoneNum=mk.phoneNum;
		this.postCode=mk.postCode;
		this.location=mk.location;
		this.uid=mk.uid;
		aaa="bbb";
	}
	
	public static ArrayList<PoiInfo> getPOIinfoList(ArrayList<com.baidu.mapapi.search.core.PoiInfo> lstmkpoiinfo){
		ArrayList<PoiInfo> lst=new ArrayList<PoiInfo>();
		for(com.baidu.mapapi.search.core.PoiInfo info :lstmkpoiinfo){
			lst.add(new PoiInfo(info));
		}
		
		return lst;
	}

}
