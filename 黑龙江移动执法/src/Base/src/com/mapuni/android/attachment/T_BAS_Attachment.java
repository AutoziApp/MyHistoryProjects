package com.mapuni.android.attachment;

public class T_BAS_Attachment {
	private String Guid;
	private String FileName;
	private String FilePath;
	private String Extension;
	private String FK_Unit;
	private String FK_Id;
	private String Remark;
	private String LinkUrl;
	private String Thumbnail;
	private String FileType;
	public String getGuid() {
		return Guid;
	}
	public void setGuid(String guid) {
		Guid = guid;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getExtension() {
		return Extension;
	}
	public void setExtension(String extension) {
		Extension = extension;
	}
	public String getFK_Unit() {
		return FK_Unit;
	}
	public void setFK_Unit(String fK_Unit) {
		FK_Unit = fK_Unit;
	}
	public String getFK_Id() {
		return FK_Id;
	}
	public void setFK_Id(String fK_Id) {
		FK_Id = fK_Id;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getLinkUrl() {
		return LinkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		LinkUrl = linkUrl;
	}
	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public String getFileType() {
		return FileType;
	}
	public void setFileType(String fileType) {
		FileType = fileType;
	}
	public T_BAS_Attachment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public T_BAS_Attachment(String guid, String fileName, String filePath,
			String extension, String fK_Unit, String fK_Id, String remark,
			String linkUrl, String thumbnail, String fileType) {
		super();
		Guid = guid;
		FileName = fileName;
		FilePath = filePath;
		Extension = extension;
		FK_Unit = fK_Unit;
		FK_Id = fK_Id;
		Remark = remark;
		LinkUrl = linkUrl;
		Thumbnail = thumbnail;
		FileType = fileType;
	}
	
}
