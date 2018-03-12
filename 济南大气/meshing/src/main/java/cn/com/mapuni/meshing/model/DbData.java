package cn.com.mapuni.meshing.model;

public class DbData {

	private int aqi;
	private String cityName;
	private double Latitude; // 中心经度
	private double Longitude; // 中心维度
	private String FK_PolluterSuperviseType;// 监控等级
	private String PollutionRate;// 污染等级

	public DbData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DbData(int aqi, String cityName, double latitude, double longitude,
			String fK_PolluterSuperviseType, String pollutionRate) {
		super();
		this.aqi = aqi;
		this.cityName = cityName;
		Latitude = latitude;
		Longitude = longitude;
		FK_PolluterSuperviseType = fK_PolluterSuperviseType;
		PollutionRate = pollutionRate;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public String getFK_PolluterSuperviseType() {
		return FK_PolluterSuperviseType;
	}

	public void setFK_PolluterSuperviseType(String fK_PolluterSuperviseType) {
		FK_PolluterSuperviseType = fK_PolluterSuperviseType;
	}

	public String getPollutionRate() {
		return PollutionRate;
	}

	public void setPollutionRate(String pollutionRate) {
		PollutionRate = pollutionRate;
	}

}
