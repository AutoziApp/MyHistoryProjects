package com.jy.environment.model;

import java.util.List;

public class CurrentWeather {
	private boolean flag;
	private Sweather sweather;
	private List<Trend> trends;
	private LifeItem life;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Sweather getSweather() {
		return sweather;
	}

	public void setSweather(Sweather sweather) {
		this.sweather = sweather;
	}

	public List<Trend> getTrends() {
		return trends;
	}

	public void setTrends(List<Trend> trends) {
		this.trends = trends;
	}

	public LifeItem getLife() {
		return life;
	}

	public void setLife(LifeItem life) {
		this.life = life;
	}

	@Override
	public String toString() {
		return "CurrentWeather [flag=" + flag + ", sweather=" + sweather
				+ ", trends=" + trends + ", life=" + life + "]";
	}

}
