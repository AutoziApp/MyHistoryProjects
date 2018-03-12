package cn.com.mapuni.meshing.model;

public class ProblemDescModel {

	private String id;
	private String patrolId;
	private String problemCode;
	private String idproblemName;

	public ProblemDescModel() {
		super();
	}

	public ProblemDescModel(String id, String patrolId, String problemCode, String idproblemName) {
		super();
		this.id = id;
		this.patrolId = patrolId;
		this.problemCode = problemCode;
		this.idproblemName = idproblemName;
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

	public String getIdproblemName() {
		return idproblemName;
	}

	public void setIdproblemName(String idproblemName) {
		this.idproblemName = idproblemName;
	}

	@Override
	public String toString() {
		return "ProblemDescModel [id=" + id + ", patrolId=" + patrolId + ", problemCode=" + problemCode
				+ ", idproblemName=" + idproblemName + "]";
	}
}
