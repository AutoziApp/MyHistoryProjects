package com.jy.environment.model;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Administrator
 * @time 2017/11/30 9:58
 * @change
 * @chang time
 * @class describe
 */

public class MonthYearTimeBean {
	
	private String flag;
	private String msg;
	private DetailBean detail;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DetailBean getDetail() {
		return detail;
	}

	public void setDetail(DetailBean detail) {
		this.detail = detail;
	}

	public static class DetailBean {

		private CityAirBean citymean;
		private CityAirBean countymean;
		private List<CityAirBean> municipalities;
		private List<CityAirBean> straightcounty;

		public CityAirBean getCitymean() {
			return citymean;
		}

		public void setCitymean(CityAirBean citymean) {
			this.citymean = citymean;
		}

		public CityAirBean getCountymean() {
			return countymean;
		}

		public void setCountymean(CityAirBean countymean) {
			this.countymean = countymean;
		}

		public List<CityAirBean> getMunicipalities() {
			return municipalities;
		}

		public void setMunicipalities(List<CityAirBean> municipalities) {
			this.municipalities = municipalities;
		}

		public List<CityAirBean> getStraightcounty() {
			return straightcounty;
		}

		public void setStraightcounty(List<CityAirBean> straightcounty) {
			this.straightcounty = straightcounty;
		}

		public static class CityAirBean {
			// "CITYNAME": "巩义市",
			// "PM10": "92",
			// "PM25": "46",
			// "EXCELLENTDAY": "14",
			// "ANPM10": "113",
			// "ANPM25": "71"

			private String CITYNAME;
			private String PM10;
			private String PM25;
			private String EXCELLENTDAY;
			private String ANPM10;
			private String ANPM25;

			public String getCITYNAME() {
				return CITYNAME;
			}

			public void setCITYNAME(String cITYNAME) {
				CITYNAME = cITYNAME;
			}

			public String getPM10() {
				return PM10;
			}

			public void setPM10(String pM10) {
				PM10 = pM10;
			}

			public String getPM25() {
				return PM25;
			}

			public void setPM25(String pM25) {
				PM25 = pM25;
			}

			public String getEXCELLENTDAY() {
				return EXCELLENTDAY;
			}

			public void setEXCELLENTDAY(String eXCELLENTDAY) {
				EXCELLENTDAY = eXCELLENTDAY;
			}

			public String getANPM10() {
				return ANPM10;
			}

			public void setANPM10(String aNPM10) {
				ANPM10 = aNPM10;
			}

			public String getANPM25() {
				return ANPM25;
			}

			public void setANPM25(String aNPM25) {
				ANPM25 = aNPM25;
			}

		}

	}
}
