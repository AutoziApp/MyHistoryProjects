package com.mapuni.android.attachment;

public class TaskFile {
	/** �������� */
	private String guid;
	/** �������� */
	private String fileName;
	/** ������ԪID */
	private String unitId;
	/** ��������·�� */
	private String absolutePath;
	/** ������׺�� */
	private String extension;
	/** �������·�� */
	private String filePath;
	/**
	 * �����TaskId
	 */
	private String billid;
	/**
	 * ���븽������������
	 */
	private String biztype;
	
	/**
	 * ��������
	 * */
	private String FK_Unit;
	
	
	

	public String getFK_Unit() {
		return FK_Unit;
	}

	public void setFK_Unit(String fK_Unit) {
		FK_Unit = fK_Unit;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getBiztype() {
		return biztype;
	}

	public void setBiztype(String biztype) {
		this.biztype = biztype;
	}

}
