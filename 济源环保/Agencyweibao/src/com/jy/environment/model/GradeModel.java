package com.jy.environment.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GradeModel implements Parcelable {
	private String level;
	private String coin;
	private String exp;
	private String full;
	private String need;

	public GradeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GradeModel(String level, String coin, String exp, String full,
			String need) {
		super();
		this.level = level;
		this.coin = coin;
		this.exp = exp;
		this.full = full;
		this.need = need;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getFull() {
		return full;
	}

	public void setFull(String full) {
		this.full = full;
	}

	public String getNeed() {
		return need;
	}

	public void setNeed(String need) {
		this.need = need;
	}

	@Override
	public String toString() {
		return "GradeModel [level=" + level + ", coin=" + coin + ", exp=" + exp
				+ ", full=" + full + ", need=" + need + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(level);
		dest.writeString(coin);
		dest.writeString(exp);
		dest.writeString(full);
		dest.writeString(need);
	}

	public static final Creator<GradeModel> CREATOR = new Creator<GradeModel>() {
		public GradeModel createFromParcel(Parcel source) {
			// 先构造位置，后构造名称(与构造方法的顺序关联)
			return new GradeModel(source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString());
		}

		public GradeModel[] newArray(int size) {
			return new GradeModel[size];
		}
	};
}
