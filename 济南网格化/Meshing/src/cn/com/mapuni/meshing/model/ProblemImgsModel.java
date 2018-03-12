package cn.com.mapuni.meshing.model;

public class ProblemImgsModel {
	private String id;
	private String patrolId;
	private String imgPath;
	private String createId;
	private String createTime;

	public ProblemImgsModel() {
		super();
	}

	public ProblemImgsModel(String id, String patrolId, String imgPath, String createId, String createTime) {
		super();
		this.id = id;
		this.patrolId = patrolId;
		this.imgPath = imgPath;
		this.createId = createId;
		this.createTime = createTime;
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

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ProblemImgsModel [id=" + id + ", patrolId=" + patrolId + ", imgPath=" + imgPath + ", createId="
				+ createId + ", createTime=" + createTime + "]";
	}

}
