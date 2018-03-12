package cn.com.mapuni.meshing.model;

import java.util.List;

public class PotrlObject {

	/**
	 * status : 200 message : 成功 rows :
	 * [{"id":"7BA773B8-E079-44C7-82E8-B274C4A69631","code":"AA000333","name":
	 * "污染企业","address":null,"contact":null,"x":null,"y":null,"industryType":
	 * null,"economyType":null,"superviseType":null,"pollutant":null,"gridCode":
	 * "B37019911"}]
	 */

	private String status;
	private String message;
	private List<RowsBean> rows;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<RowsBean> getRows() {
		return rows;
	}

	public void setRows(List<RowsBean> rows) {
		this.rows = rows;
	}

	public static class RowsBean {

		/**
		 * id : 7BA773B8-E079-44C7-82E8-B274C4A69631 code : AA000333 name : 污染企业
		 * address : null contact : null x : null y : null industryType : null
		 * economyType : null superviseType : null pollutant : null gridCode :
		 * B37019911
		 */

		private String id;
		private String code;
		private String name;
		private String address;
		private String contact;
		private String x;
		private String y;
		private String industryType;
		private String economyType;
		private String superviseType;
		private String pollutant;
		private String gridCode;
		private String signedStatus;
		private String signedSanStatus;

		public String getSignedStatus() {
			return signedStatus;
		}

		public void setSignedStatus(String signedStatus) {
			this.signedStatus = signedStatus;
		}

		public String getSignedSanStatus() {
			return signedSanStatus;
		}

		public void setSignedSanStatus(String signedSanStatus) {
			this.signedSanStatus = signedSanStatus;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getX() {
			return x;
		}

		public void setX(String x) {
			this.x = x;
		}

		public String getY() {
			return y;
		}

		public void setY(String y) {
			this.y = y;
		}

		public String getIndustryType() {
			return industryType;
		}

		public void setIndustryType(String industryType) {
			this.industryType = industryType;
		}

		public String getEconomyType() {
			return economyType;
		}

		public void setEconomyType(String economyType) {
			this.economyType = economyType;
		}

		public String getSuperviseType() {
			return superviseType;
		}

		public void setSuperviseType(String superviseType) {
			this.superviseType = superviseType;
		}

		public String getPollutant() {
			return pollutant;
		}

		public void setPollutant(String pollutant) {
			this.pollutant = pollutant;
		}

		public String getGridCode() {
			return gridCode;
		}

		public void setGridCode(String gridCode) {
			this.gridCode = gridCode;
		}
	}
}
