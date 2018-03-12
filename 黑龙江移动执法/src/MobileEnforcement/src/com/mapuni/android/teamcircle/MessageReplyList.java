package com.mapuni.android.teamcircle;

import java.io.Serializable;

class ReplyList implements Serializable {
	public String Content;
	public int id;
	public String MsgCode;
	public int ReplyPeopleId;
	public String ReplyPeopleCode;
	public String ReplyPeopleName;
	public String ReplyTime;
	public int Type;
	@Override
	public String toString() {
		return "MessageReplyList [Content=" + Content + ", id=" + id
				+ ", MsgCode=" + MsgCode + ", ReplyPeopleId=" + ReplyPeopleId
				+ ", ReplyPeopleCode=" + ReplyPeopleCode + ", ReplyPeopleName="
				+ ReplyPeopleName + ", ReplyTime=" + ReplyTime + ", Type="
				+ Type + "]";
	}

	

}