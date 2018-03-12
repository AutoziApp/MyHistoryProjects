package com.jy.environment.database.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ModelAlarmHistory implements Parcelable {
	private String province;
	private String town;
	private String title;
	private String message;
	private String time;
	private String url;
	private String alarm;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	@Override
	public String toString() {
		return "ModelAlarmHistory [province=" + province + ", town=" + town
				+ ", title=" + title + ", message=" + message + ", time="
				+ time + ", url=" + url + ", alarm=" + alarm + "]";
	}

	public ModelAlarmHistory(String province, String town, String title,
			String message, String time, String url, String alarm) {
		super();
		this.province = province;
		this.town = town;
		this.title = title;
		this.message = message;
		this.time = time;
		this.url = url;
		this.alarm = alarm;
	}

	public ModelAlarmHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(province);
		dest.writeString(town);
		dest.writeString(title);
		dest.writeString(message);
		dest.writeString(time);
		dest.writeString(url);
		dest.writeString(alarm);
	}

	public static final Creator<ModelAlarmHistory> CREATOR = new Creator<ModelAlarmHistory>() {
		public ModelAlarmHistory createFromParcel(Parcel source) {
			// 先构造位置，后构造名称(与构造方法的顺序关联)
			return new ModelAlarmHistory(source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
		}

		public ModelAlarmHistory[] newArray(int size) {
			return new ModelAlarmHistory[size];
		}
	};
}