package com.mapuni.android.teamcircle;

public class MessageReplyModel {
	  ///  主键ID
	public int Id;
	 ///  消息code
	public String MsgCode;
	 ///  回复人ID
	public int ReplyPeopleId;
	 ///  回复人Code
	public String ReplyPeopleCode;
	   ///  回复人名称
	public String ReplyPeopleName;
	 ///  回复类型(1.回复、2、点赞)
	public int Type;
	   ///  回复内容
	public String Content;
    /// 回复时间
	public String ReplyTime;
	


	@Override
	public String toString() {
		return "MessageReplyModel [  MsgCode=" + MsgCode
				+ ", ReplyPeopleId=" + ReplyPeopleId + ", ReplyPeopleCode="
				+ ReplyPeopleCode + ", ReplyPeopleName=" + ReplyPeopleName
				+ ", Type=" + Type + ", Content=" + Content + ", ReplyTime="
				+ ReplyTime + "]";
	}
	
	
	

}
