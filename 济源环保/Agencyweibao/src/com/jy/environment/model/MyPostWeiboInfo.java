package com.jy.environment.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jy.environment.util.MyLog;

import android.content.Context;
import android.os.Environment;

public class MyPostWeiboInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int isShare;
    private String lat;
    private String lng;
    private String user_name;
    private String content;
    private int env;
    private int star;
    private String province;
    private String info_city;
    private String address;
    private int isopen;
    private List<String> paths;
    // private List<String> picStrings ;
    private long sendTime;

    
    private String userId = "";
    private String userPic = "";
	private String isAkey;
	private String pollutionType;
	private String isanonymous;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public long getSendTime() {
	return sendTime;
    }

    public void setSendTime(long sendTime) {
	this.sendTime = sendTime;
    }

    public String getLat() {
	return lat;
    }

    public void setLat(String lat) {
	this.lat = lat;
    }

    public String getLng() {
	return lng;
    }

    public void setLng(String lng) {
	this.lng = lng;
    }

    public String getUser_name() {
	return user_name;
    }

    public void setUser_name(String user_name) {
	this.user_name = user_name;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public int getEnv() {
	return env;
    }

    public void setEnv(int env) {
	this.env = env;
    }

    public int getStar() {
	return star;
    }

    public void setStar(int star) {
	this.star = star;
    }

    public String getProvince() {
	return province;
    }

    public void setProvince(String province) {
	this.province = province;
    }

    public String getInfo_city() {
	return info_city;
    }
    

    public void setInfo_city(String info_city) {
	this.info_city = info_city;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public int getIsShare() {
	return isShare;
    }

    public void setIsShare(int isShare) {
	this.isShare = isShare;
    }

    public int getIsopen() {
	return isopen;
    }

    public void setIsopen(int isopen) {
	this.isopen = isopen;
    }

    public List<String> getPaths() {
	return paths;
    }

    public void setPaths(List<String> paths) {
	this.paths = paths;
    }
    
    

    // public List<String> getPicStrings() {
    // return picStrings;
    // }
    //
    // public void setPicStrings(List<String> picStrings) {
    // this.picStrings = picStrings;
    // }

    public String getIsAkey() {
		return isAkey;
	}

	public void setIsAkey(String isAkey) {
		this.isAkey = isAkey;
	}

	public String getPollutionType() {
		return pollutionType;
	}

	public void setPollutionType(String pollutionType) {
		this.pollutionType = pollutionType;
	}

	public String getIsanonymous() {
		return isanonymous;
	}

	public void setIsanonymous(String isanonymous) {
		this.isanonymous = isanonymous;
	}

	public static boolean deleteInfo(Context context, MyPostWeiboInfo info) {
	FileOutputStream fileOutputStream = null;
	ObjectOutputStream objectOutputStream = null;
	List<MyPostWeiboInfo> infos = null;
	try {
	    // 存入数据
	    File file = new File(Environment.getExternalStorageDirectory()
		    .toString()
		    + File.separator
		    + "weibao"
		    + File.separator
		    + "weibodata.dat");
	    if (!file.getParentFile().exists()) {
		file.getParentFile().mkdirs();
	    }

	    MyLog.i("Weibo deleteInfo file.exists():" + file.exists());
	    if (file.exists()) {
		infos = getWeiboInfoHestory(context);
		MyLog.i("Weibo deleteInfo :" + infos);
		if (null != infos) {
		    for (int i = 0; i < infos.size(); i++) {
			MyLog.i("Weibo deleteInfo " + i + ":"
				+ infos.get(i).toString());
		    }
		    deleteInfoAll(context);
		}
	    }
	    MyLog.i("Weibo deleteInfo:" + !file.exists());
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    MyLog.i("Weibo deleteInfo info:" + info.toString());
	    MyLog.i("Weibo deleteInfo infos.contains(info):" + infos.contains(info));
	    if(null == infos){
		return true;
	    }
	    for (int i = 0; i < infos.size(); i++) {
		if(infos.get(i).getSendTime() == info.getSendTime()){
		    infos.remove(i) ;
		}
	    }
	    if (infos.size() > 0) {
		fileOutputStream = new FileOutputStream(file.toString());
		objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(infos);
	    }
	    return true;
	} catch (Exception e) {
	    MyLog.i("Weibo deleteInfo " + e.getMessage());
	    e.printStackTrace();
	    return false;
	} finally {

	    if (objectOutputStream != null) {
		try {
		    objectOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (fileOutputStream != null) {
		try {
		    fileOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public static boolean saveInfo(Context context, MyPostWeiboInfo info) {
	FileOutputStream fileOutputStream = null;
	ObjectOutputStream objectOutputStream = null;
	List<MyPostWeiboInfo> infos = null;
	try {
	    // 存入数据
	    File file = new File(Environment.getExternalStorageDirectory()
		    .toString()
		    + File.separator
		    + "weibao"
		    + File.separator
		    + "weibodata.dat");
	    if (!file.getParentFile().exists()) {
		file.getParentFile().mkdirs();
	    }

	    MyLog.i("Weibo info file.exists():" + file.exists());
	    if (file.exists()) {
		infos = getWeiboInfoHestory(context);
		MyLog.i("Weibo info infos:" + infos);
		if (null != infos) {
		    for (int i = 0; i < infos.size(); i++) {
			MyLog.i("Weibo info :" + infos.get(i).toString());
		    }
		    deleteInfoAll(context);
		}
	    }
	    MyLog.i("Weibo info !file.exists():" + !file.exists());
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    if (null != infos) {
		infos.add(info);
	    } else {
		infos = new ArrayList<MyPostWeiboInfo>();
		infos.add(info);
	    }
	    for (int i = 0; i < infos.size(); i++) {
		MyLog.i("Weibo infos add +" + i + "+:"
			+ infos.get(i).toString());
	    }
	    fileOutputStream = new FileOutputStream(file.toString());
	    objectOutputStream = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(infos);
	    return true;

	} catch (Exception e) {
	    MyLog.i("Weibo saveInfo " + e.getMessage());
	    e.printStackTrace();
	    return false;
	} finally {

	    if (objectOutputStream != null) {
		try {
		    objectOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (fileOutputStream != null) {
		try {
		    fileOutputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public static boolean deleteInfoAll(Context context) {
	try {
	    // 存入数据
	    File file = new File(Environment.getExternalStorageDirectory()
		    .toString()
		    + File.separator
		    + "weibao"
		    + File.separator
		    + "weibodata.dat");
	    if (!file.getParentFile().exists()) {
		file.getParentFile().mkdirs();
	    }

	    if (file.exists()) {
		return file.delete();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	return false;
    }

    public static ArrayList<MyPostWeiboInfo> getWeiboInfoHestory(Context context) {
	FileInputStream fileInputStream = null;
	ObjectInputStream objectInputStream = null;
	ArrayList<MyPostWeiboInfo> myPostWeiboInfos = null;
	try {
	    File file = new File(Environment.getExternalStorageDirectory()
		    .toString()
		    + File.separator
		    + "weibao"
		    + File.separator
		    + "weibodata.dat");

	    MyLog.i("Weibo getWeiboInfoHestory !file.exists():"
		    + !file.exists());
	    if (!file.exists()) {
		return myPostWeiboInfos;
	    }
	    // 取出数据
	    fileInputStream = new FileInputStream(file.toString());
	    objectInputStream = new ObjectInputStream(fileInputStream);
	    myPostWeiboInfos = (ArrayList<MyPostWeiboInfo>) objectInputStream
		    .readObject();
	    for (int i = 0; i < myPostWeiboInfos.size(); i++) {
		MyLog.i("Weibo getWeiboInfoHestory myPostWeiboInfos " + i + ":"
			+ myPostWeiboInfos.get(i).toString());
	    }
	    return myPostWeiboInfos;
	} catch (Exception e) {
	    MyLog.i("Weibo getWeiboInfoHestory " + e.getMessage());
	    e.printStackTrace();
	    return null;
	} finally {
	    if (objectInputStream != null) {
		try {
		    objectInputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (fileInputStream != null) {
		try {
		    fileInputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public MyPostWeiboInfo(int isShare, String lat, String lng,
	    String user_name, String content, int env, int star,
	    String province, String info_city, String address, int isopen,
	    List<String> paths, long sendTime,String userId,String userPic,
	    String isAkey,String pollutionType,String isanonymous) {
	super();
	this.isShare = isShare;
	this.lat = lat;
	this.lng = lng;
	this.user_name = user_name;
	this.content = content;
	this.env = env;
	this.star = star;
	this.province = province;
	this.info_city = info_city;
	this.address = address;
	this.isopen = isopen;
	this.paths = paths;
	this.sendTime = sendTime;
	this.userId = userId;
	this.userPic = userPic;
	this.isAkey = isAkey;
	this.pollutionType = pollutionType;
	this.isanonymous = isanonymous;
    }

	@Override
	public String toString() {
		return "MyPostWeiboInfo [isShare=" + isShare + ", lat=" + lat
				+ ", lng=" + lng + ", user_name=" + user_name + ", content="
				+ content + ", env=" + env + ", star=" + star + ", province="
				+ province + ", info_city=" + info_city + ", address="
				+ address + ", isopen=" + isopen + ", paths=" + paths
				+ ", sendTime=" + sendTime + ", userId=" + userId
				+ ", userPic=" + userPic + ", isAkey=" + isAkey
				+ ", pollutionType=" + pollutionType + ", isanonymous="
				+ isanonymous + "]";
	}



}
