package com.mapuni.android.attachment;

public class TaskFile {
	/** 附件主键 */
	private String guid;
	/** 附件名称 */
	private String fileName;
	/** 附件单元ID */
	private String unitId;
	/** 附件绝对路径 */
	private String absolutePath;
	/** 附件后缀名 */
	private String extension;
	/** 附件相对路径 */
	private String filePath;
	/**
	 * 任务的TaskId
	 */
	private String billid;
	/**
	 * 插入附件的任务类型
	 */
	private String biztype;
	
	/**
	 * 附件区别
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
