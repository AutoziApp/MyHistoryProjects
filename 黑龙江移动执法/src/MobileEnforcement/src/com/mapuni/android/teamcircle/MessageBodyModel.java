package com.mapuni.android.teamcircle;

import java.util.List;

public class MessageBodyModel {
	///  消息Code
	public String MsgCode;
	///  发送人Id
	public String SenderId;
	///  发送人Code
	public String SenderCode;
	///  发送人名称
	public String SenderName;
	///  区域码	
	public String RegionCode;
	///  消息类型(1.文字、2、图片、3、视频)
	public int Type;
	///  消息内容
	public String Content;
	///  发送时间
	public String SendTime;
	/// 消息多张图片或视频上传路径
    public List<String> FilePathList;
    /// 消息回复列表
  	public List<ReplyList> MessageReplyList;   
	@Override
	public String toString() {
		return "Msg [Content=" + Content + ", MsgCode=" + MsgCode
				+ ", RegionCode=" + RegionCode + ", SendTime=" + SendTime
				+ ", SenderCode=" + SenderCode + ", SenderId=" + SenderId
				+ ", SenderName=" + SenderName + ", Type=" + Type
				+ ", MessageReplyList=" + MessageReplyList + "]";
	}



}
