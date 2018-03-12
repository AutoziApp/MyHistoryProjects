package cn.com.mapuni.meshing.model;

public class ProblemsModel {
	
	private String id;
	private String patrolId;
	private String problemCode;
	private String problemName;
	
	
	
	public ProblemsModel(String id, String patrolId, String problemCode,
			String problemName) {
		super();
		this.id = id;
		this.patrolId = patrolId;
		this.problemCode = problemCode;
		this.problemName = problemName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPatrolId() {
		return patrolId;
	}
	public void setPatrolId(String patrolId) {
		this.patrolId = patrolId;
	}
	public String getProblemCode() {
		return problemCode;
	}
	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}
	public String getProblemName() {
		return problemName;
	}
	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}
	@Override
	public String toString() {
		return "ProblemsModel [id=" + id + ", patrolId=" + patrolId
				+ ", problemCode=" + problemCode + ", problemName="
				+ problemName + "]";
	}
	
	
	
	
}
