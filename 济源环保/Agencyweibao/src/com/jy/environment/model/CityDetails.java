package com.jy.environment.model;

import java.util.List;

public class CityDetails {
	private String ranking;
	private String position_name;
	private boolean flag;
	private EnvironmentForecastWeekModel weekModel;
	private List<WeatherInfo24> info24s;
	private Kongqizhishu zhishu;
	private List<WeatherInfo7_tian> weInfo7;
	private List<WeatherInfoMonth> weInfoMonth;
	private List<WeatherInfoYear> weInfoYear;
	private List<AqiStationModel> aqiStationModels;
	private String airlevel;
	private String aqi;
	private ListPolluctionModel polluctionModel;
	private List<ListHumiDityModel> humiDityModels;
	private List<WindModel> windModels;
	private List<PM10Info24H> pm10Info24Hs;
	private List<PM25> pm25Info24Hs;
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getPosition_name() {
		return position_name;
	}
	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public EnvironmentForecastWeekModel getWeekModel() {
		return weekModel;
	}
	public void setWeekModel(EnvironmentForecastWeekModel weekModel) {
		this.weekModel = weekModel;
	}
	public List<WeatherInfo24> getInfo24s() {
		return info24s;
	}
	public void setInfo24s(List<WeatherInfo24> info24s) {
		this.info24s = info24s;
	}
	public Kongqizhishu getZhishu() {
		return zhishu;
	}
	public void setZhishu(Kongqizhishu zhishu) {
		this.zhishu = zhishu;
	}
	public List<WeatherInfo7_tian> getWeInfo7() {
		return weInfo7;
	}
	public void setWeInfo7(List<WeatherInfo7_tian> weInfo7) {
		this.weInfo7 = weInfo7;
	}
	public List<WeatherInfoMonth> getWeInfoMonth() {
		return weInfoMonth;
	}
	public void setWeInfoMonth(List<WeatherInfoMonth> weInfoMonth) {
		this.weInfoMonth = weInfoMonth;
	}
	public List<WeatherInfoYear> getWeInfoYear() {
		return weInfoYear;
	}
	public void setWeInfoYear(List<WeatherInfoYear> weInfoYear) {
		this.weInfoYear = weInfoYear;
	}
	public List<AqiStationModel> getAqiStationModels() {
		return aqiStationModels;
	}
	public void setAqiStationModels(List<AqiStationModel> aqiStationModels) {
		this.aqiStationModels = aqiStationModels;
	}
	public String getAirlevel() {
		return airlevel;
	}
	public void setAirlevel(String airlevel) {
		this.airlevel = airlevel;
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	public ListPolluctionModel getPolluctionModel() {
		return polluctionModel;
	}
	public void setPolluctionModel(ListPolluctionModel polluctionModel) {
		this.polluctionModel = polluctionModel;
	}
	public List<ListHumiDityModel> getHumiDityModels() {
		return humiDityModels;
	}
	public void setHumiDityModels(List<ListHumiDityModel> humiDityModels) {
		this.humiDityModels = humiDityModels;
	}
	public List<WindModel> getWindModels() {
		return windModels;
	}
	public void setWindModels(List<WindModel> windModels) {
		this.windModels = windModels;
	}
	public List<PM10Info24H> getPm10Info24Hs() {
		return pm10Info24Hs;
	}
	public void setPm10Info24Hs(List<PM10Info24H> pm10Info24Hs) {
		this.pm10Info24Hs = pm10Info24Hs;
	}
	public List<PM25> getPm25Info24Hs() {
		return pm25Info24Hs;
	}
	public void setPm25Info24Hs(List<PM25> pm25Info24Hs) {
		this.pm25Info24Hs = pm25Info24Hs;
	}
	public CityDetails(String ranking, String position_name, boolean flag, EnvironmentForecastWeekModel weekModel,
			List<WeatherInfo24> info24s, Kongqizhishu zhishu, List<WeatherInfo7_tian> weInfo7,
			List<WeatherInfoMonth> weInfoMonth, List<WeatherInfoYear> weInfoYear,
			List<AqiStationModel> aqiStationModels, String airlevel, String aqi, ListPolluctionModel polluctionModel,
			List<ListHumiDityModel> humiDityModels, List<WindModel> windModels, List<PM10Info24H> pm10Info24Hs,
			List<PM25> pm25Info24Hs) {
		super();
		this.ranking = ranking;
		this.position_name = position_name;
		this.flag = flag;
		this.weekModel = weekModel;
		this.info24s = info24s;
		this.zhishu = zhishu;
		this.weInfo7 = weInfo7;
		this.weInfoMonth = weInfoMonth;
		this.weInfoYear = weInfoYear;
		this.aqiStationModels = aqiStationModels;
		this.airlevel = airlevel;
		this.aqi = aqi;
		this.polluctionModel = polluctionModel;
		this.humiDityModels = humiDityModels;
		this.windModels = windModels;
		this.pm10Info24Hs = pm10Info24Hs;
		this.pm25Info24Hs = pm25Info24Hs;
	}
	
	@Override
	public String toString() {
		return "CityDetails [ranking=" + ranking + ", position_name=" + position_name + ", flag=" + flag
				+ ", weekModel=" + weekModel + ", info24s=" + info24s + ", zhishu=" + zhishu + ", weInfo7=" + weInfo7
				+ ", weInfoMonth=" + weInfoMonth + ", weInfoYear=" + weInfoYear + ", aqiStationModels="
				+ aqiStationModels + ", airlevel=" + airlevel + ", aqi=" + aqi + ", polluctionModel=" + polluctionModel
				+ ", humiDityModels=" + humiDityModels + ", windModels=" + windModels + ", pm10Info24Hs=" + pm10Info24Hs
				+ ", pm25Info24Hs=" + pm25Info24Hs + "]";
	}
	public CityDetails() {
		super();
	}
	

}
