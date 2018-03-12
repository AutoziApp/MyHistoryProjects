package com.mapuni.android.teamcircle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

public class MessageModel implements Serializable{
@Override
	public String toString() {
		return "MessageModel [MsgCode=" + MsgCode + ", SenderId=" + SenderId
				+ ", SenderCode=" + SenderCode + ", SenderName=" + SenderName
				+ ", RegionCode=" + RegionCode + ", Type=" + Type
				+ ", Content=" + Content + ", SendTime=" + SendTime
				+ ", FilePathList=" + FilePathList + ", MessageReplyList="
				+ MessageReplyList + "]";
	}

	/*[{"MsgCode":"3fd835fe-3c5d-401d-8207-8449177349ad","SenderId":1030,"SenderCode":"us
 * er_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":2,"Content":"2
 * 1312312312","SendTime":"2017-04-25T13:42:19.217","FilePathList":[""],"MessageReplyL
 * ist":[]},{"MsgCode":"819c76f7-476d-433b-ae73-b98089a3195a","SenderId":1030,"SenderC
 * ode":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":2,"Con
 * tent":"13:19","SendTime":"2017-04-25T13:19:18.01","FilePathList":[""],"MessageReply
 * List":[]},{"MsgCode":"7f00016b-c119-4057-9b2a-14c7aa8f0116","SenderId":1030,"Sender
 * Code":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":2,"Co
 * ntent":"13:15","SendTime":"2017-04-25T13:15:07.947","FilePathList":[""],"MessageRep
 * lyList":[]},{"MsgCode":"c639862a-d20c-4127-87ef-6cfad4cb6afa","SenderId":1030,"Send
 * erCode":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":2,"Co
 * ntent":"13µ„07∑÷","SendTime":"2017-04-25T13:06:39.353","FilePathList":[""],"MessageRe
 * plyList":[]},{"MsgCode":"0834bca6-1072-4068-bb49-e470e0652975","SenderId":1030,"Sen
 * derCode":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":2,"
 * Content":"13µ„06∑÷","SendTime":"2017-04-25T13:05:36.997","FilePathList":["","/Uploa
 * dFile/BodyPart_5c51ddd5-98e2-4d78-afac-7adafd13754d.jpg"],"MessageReplyList":[]},{
 * "MsgCode":"37843bed-65a1-4ca7-ad14-446d7a42abf7","SenderId":1030,"SenderCode":"user
 * _201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":2,"Content":"13µ„0
 * 3∑÷","SendTime":"2017-04-25T13:02:43.733","FilePathList":["","/UploadFile/BodyPart_
 * cd67bf26-b0db-4b80-942a-2064ccc2773b.jpg","/UploadFile/BodyPart_cd67bf26-b0db-4b80-
 * 942a-2064ccc2773b.jpg/UploadFile/BodyPart_a233eb81-bece-488b-8687-ba53e63eabcc.jpg"
 * ,"/UploadFile/BodyPart_cd67bf26-b0db-4b80-942a-2064ccc2773b.jpg/UploadFile/BodyPart
 * _a233eb81-bece-488b-8687-ba53e63eabcc.jpg/UploadFile/BodyPart_6f70cf79-a0b2-432d-95
 * 66-127e840747d1.jpg"],"MessageReplyList":[]},{"MsgCode":"12043acb-e764-483a-ad29-5d
 * ca47955015","SenderId":1030,"SenderCode":"user_201404290011","SenderName":"¡ıµ§","Re
 * gionCode":"230000000","Type":2,"Content":"1231231231231","SendTime":"2017-04-25T13:
 * 00:31.367","FilePathList":["","/UploadFile/BodyPart_54ae2c42-b96f-4e61-b3f7-378526
 * 15644c.jpg","/UploadFile/BodyPart_54ae2c42-b96f-4e61-b3f7-37852615644c.jpg/UploadFi
 * le/BodyPart_edc83156-9816-4dc4-8733-2f36a1b2e979.jpg","/UploadFile/BodyPart_54ae2c4
 * 2-b96f-4e61-b3f7-37852615644c.jpg/UploadFile/BodyPart_edc83156-9816-4dc4-8733-2f36a
 * 1b2e979.jpg/UploadFile/BodyPart_8e00ad62-5f0b-4c66-b471-6ed89186f137.jpg"],"Message
 * ReplyList":[]},{"MsgCode":"112edb50-9107-4ae2-8fca-87ddd11ecad0","SenderId":1030,"S
 * enderCode":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000","Type":
 * 2,"Content":"213123123123","SendTime":"2017-04-25T12:58:35.29","FilePathList":[""]
 * ,"MessageReplyList":[]},{"MsgCode":"92ec3c99-5847-4419-8267-706221f94edb","SenderId
 * ":1030,"SenderCode":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"230000000"
 * ,"Type":2,"Content":"10µ„19∑÷","SendTime":"2017-04-25T10:19:06.58","FilePathList":nu
 * ll,"MessageReplyList":[]},{"MsgCode":"c19d8966-eb82-4666-901c-55a0b692e03a","Sender
 * Id":1030,"SenderCode":"user_201404290011","SenderName":"¡ıµ§","RegionCode":"23000000
 * 0","Type":2,"Content":"10µ„18∑÷","SendTime":"2017-04-25T10:17:57.297","FilePathList"
 * :null,"MessageReplyList":[]}]*/
    /**
     * MsgCode : 3fd835fe-3c5d-401d-8207-8449177349ad
     * SenderId : 1030
     * SenderCode : user_201404290011s
     * SenderName : ¡ıµ§
     * RegionCode : 230000000
     * Type : 2
     * Content : 21312312312
     * SendTime : 2017-04-25T13:42:19.217
     * FilePathList : [""]
     * MessageReplyList : []
     */

    private String MsgCode;
    private String SenderId;
    private String SenderCode;
    private String SenderName;
    private String RegionCode;
    private int Type;
    private String Content;
    private String SendTime;
    private ArrayList<String> FilePathList;
    private ArrayList<ReplyList> MessageReplyList;

    public String getMsgCode() {
        return MsgCode;
    }

    public void setMsgCode(String MsgCode) {
        this.MsgCode = MsgCode;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String SenderId) {
        this.SenderId = SenderId;
    }

    public String getSenderCode() {
        return SenderCode;
    }

    public void setSenderCode(String SenderCode) {
        this.SenderCode = SenderCode;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String SenderName) {
        this.SenderName = SenderName;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String RegionCode) {
        this.RegionCode = RegionCode;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }

    public ArrayList<String> getFilePathList() {
        return FilePathList;
    }

    public void setFilePathList(ArrayList<String> FilePathList) {
        this.FilePathList = FilePathList;
    }

    public ArrayList<ReplyList> getMessageReplyList() {
        return MessageReplyList;
    }

    public void setMessageReplyList(ArrayList<ReplyList> MessageReplyList) {
        this.MessageReplyList = MessageReplyList;
    }
    
	
}
