package com.mapuni.android.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 
 * @author dell
 * 
 */
public class AvailableActionListxx implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3583721472904066180L;
	String Code;
	String IsMustChoose;
	String IsOnlyDepartment;
	
	String Name;
	String RegionCode;
	String TargetCollection;
	String TargetId;
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getIsMustChoose() {
		return IsMustChoose;
	}
	public void setIsMustChoose(String isMustChoose) {
		IsMustChoose = isMustChoose;
	}
	public String getIsOnlyDepartment() {
		return IsOnlyDepartment;
	}
	public void setIsOnlyDepartment(String isOnlyDepartment) {
		IsOnlyDepartment = isOnlyDepartment;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getRegionCode() {
		return RegionCode;
	}
	public void setRegionCode(String regionCode) {
		RegionCode = regionCode;
	}
	public String getTargetCollection() {
		return TargetCollection;
	}
	public void setTargetCollection(String targetCollection) {
		TargetCollection = targetCollection;
	}
	public String getTargetId() {
		return TargetId;
	}
	public void setTargetId(String targetId) {
		TargetId = targetId;
	}
	public String getTargetType() {
		return TargetType;
	}
	public void setTargetType(String targetType) {
		TargetType = targetType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	String TargetType;
	@Override
	public String toString() {
		return "AvailableActionListxx [Code=" + Code + ", IsMustChoose="
				+ IsMustChoose + ", IsOnlyDepartment=" + IsOnlyDepartment
				+ ", Name=" + Name + ", RegionCode=" + RegionCode
				+ ", TargetCollection=" + TargetCollection + ", TargetId="
				+ TargetId + ", TargetType=" + TargetType + "]";
	}
	
	

	
}
