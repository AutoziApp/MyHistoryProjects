package com.mapuni.android.teamcircle;

public class MessageReplyModel {
	  ///  ����ID
	public int Id;
	 ///  ��Ϣcode
	public String MsgCode;
	 ///  �ظ���ID
	public int ReplyPeopleId;
	 ///  �ظ���Code
	public String ReplyPeopleCode;
	   ///  �ظ�������
	public String ReplyPeopleName;
	 ///  �ظ�����(1.�ظ���2������)
	public int Type;
	   ///  �ظ�����
	public String Content;
    /// �ظ�ʱ��
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
