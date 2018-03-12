package com.mapuni.android.teamcircle;

import java.util.List;

public class MessageBodyModel {
	///  ��ϢCode
	public String MsgCode;
	///  ������Id
	public String SenderId;
	///  ������Code
	public String SenderCode;
	///  ����������
	public String SenderName;
	///  ������	
	public String RegionCode;
	///  ��Ϣ����(1.���֡�2��ͼƬ��3����Ƶ)
	public int Type;
	///  ��Ϣ����
	public String Content;
	///  ����ʱ��
	public String SendTime;
	/// ��Ϣ����ͼƬ����Ƶ�ϴ�·��
    public List<String> FilePathList;
    /// ��Ϣ�ظ��б�
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
