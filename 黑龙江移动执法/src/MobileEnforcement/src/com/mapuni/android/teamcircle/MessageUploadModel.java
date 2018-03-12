package com.mapuni.android.teamcircle;

import android.R.string;


public class MessageUploadModel {

    @Override
	public String toString() {
		return "MessageUploadModel [MsgCode=" + MsgCode + ", SenderId="
				+ SenderId + ", SenderCode=" + SenderCode + ", SenderName="
				+ SenderName + ", RegionCode=" + RegionCode + ", Type=" + Type
				+ ", Content=" + Content + "]";
	}

	public String getMsgCode() {
		return MsgCode;
	}

	public void setMsgCode(String string) {
		MsgCode = string;
	}

	public String getSenderId() {
		return SenderId;
	}

	public void setSenderId(String s) {
		SenderId = s;
	}

	public string getSenderCode() {
		return SenderCode;
	}

	public void setSenderCode(string senderCode) {
		SenderCode = senderCode;
	}

	public String getSenderName() {
		return SenderName;
	}

	public void setSenderName(String string) {
		SenderName = string;
	}

	public int getRegionCode() {
		return RegionCode;
	}

	public void setRegionCode(int i) {
		RegionCode = i;
	}

	public int getType() {
		return Type;
	}

	public void setType(int i) {
		Type = i;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String body) {
		Content = body;
	}

	public String MsgCode;

  
    public String SenderId;

 
    public string SenderCode;

    public String SenderName;

    public int RegionCode;

    public int Type;

    public String Content;

    

}
