package com.jy.environment.model;

import java.util.List;

public class CityRank {
	private List<Item> _Result;
	private int rank;
	private long time;

	public List<Item> get_Result() {
		return _Result;
	}

	public void set_Result(List<Item> _Result) {
		this._Result = _Result;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "CityRank [_Result=" + _Result + ", rank=" + rank + "]";
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	

}
