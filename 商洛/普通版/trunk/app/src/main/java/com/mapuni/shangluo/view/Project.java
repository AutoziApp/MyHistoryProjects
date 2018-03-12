package com.mapuni.shangluo.view;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {
	private int icon;
	private String itemName;
	private int tabPosition;
	private Intent intent;
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getTabPosition() {
		return tabPosition;
	}

	public void setTabPosition(int tabPosition) {
		this.tabPosition = tabPosition;
	}
	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(icon);
		dest.writeInt(tabPosition);
		dest.writeString(itemName);
	}

	public Project() {

	}

	public Project(Parcel dest) {
		icon = dest.readInt();
		tabPosition = dest.readInt();
		itemName = dest.readString();
	}

	public static final Creator<Project> CREATOR = new Creator<Project>() {

		/**
		 * ���ⲿ�෴���л���������ʹ��
		 */
		@Override
		public Project[] newArray(int size) {
			return new Project[size];
		}

		/**
		 * ��Parcel�ж�ȡ����
		 */
		@Override
		public Project createFromParcel(Parcel source) {
			return new Project(source);
		}
	};

}
