package cn.com.mapuni.gis.meshingtotal.model;

public class DbNews {
	private String id;
	private String importance;
	private String endDate;
	private String taskName;
	private String details;
	public DbNews(){}
	public DbNews(String id, String importance, String endDate, String taskName, String details){
		this.id=id;
		this.importance=importance;
		this.endDate=endDate;
		this.taskName=taskName;
		this.details=details;
	}
	public void setId( String id){
		this.id=id;
	}
	public void setImportance( String importance){
		this.importance=importance;
	}
	public void setTaskName( String taskName){
		this.taskName=taskName;
	}
	public void setDetails(String details){
		this.details=details;
	}
	public void setEndDate(String endDate){
		this.endDate=endDate;
	}
	public String getId(){
		return id;
	}
	public String getImportance(){
		return importance;
	}
	public String getTaskName(){
		return taskName;
	}
	public String getDetails(){
		return details;
	}
	public String getEndDate(){
		return endDate;
	}
}
