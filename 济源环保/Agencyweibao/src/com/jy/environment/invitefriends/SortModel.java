package com.jy.environment.invitefriends;

import android.os.Parcel;
import android.os.Parcelable;

public class SortModel implements Parcelable {

	public SortModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SortModel [id=" + id + ", name=" + name + ", contactId="
				+ contactId + ", strPhoneNumber=" + strPhoneNumber
				+ ", sortLetters=" + sortLetters + ", isCheacked=" + isCheacked
				+ "]";
	}

	private int id;
	private String name; //
	private String contactId;
	private String strPhoneNumber; //
	private String sortLetters; //
	private int isCheacked;

	public int isCheacked() {
		return isCheacked;
	}

	public void setCheacked(int isCheacked) {
		this.isCheacked = isCheacked;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getStrPhoneNumber() {
		return strPhoneNumber;
	}

	public void setStrPhoneNumber(String strPhoneNumber) {
		this.strPhoneNumber = strPhoneNumber;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	// private int id;
	// private String name; //
	// private String contactId;
	// private String strPhoneNumber; //
	// private String sortLetters; //
	// private int isCheacked;
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(contactId);
		dest.writeString(strPhoneNumber);
		dest.writeString(sortLetters);
		dest.writeInt(isCheacked);
	}

	public SortModel(int id, String name, String contactId,
			String strPhoneNumber, String sortLetters, int isCheacked) {
		super();
		this.id = id;
		this.name = name;
		this.contactId = contactId;
		this.strPhoneNumber = strPhoneNumber;
		this.sortLetters = sortLetters;
		this.isCheacked = isCheacked;
	}

	public static final Creator<SortModel> CREATOR = new Creator<SortModel>() {
		public SortModel createFromParcel(Parcel source) {
			// 先构造位置，后构造名称(与构造方法的顺序关联)
			return new SortModel(source.readInt(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readInt());
		}

		public SortModel[] newArray(int size) {
			return new SortModel[size];
		}
	};
}
