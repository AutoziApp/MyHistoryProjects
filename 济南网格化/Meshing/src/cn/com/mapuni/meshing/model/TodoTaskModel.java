package cn.com.mapuni.meshing.model;

import java.util.ArrayList;
import java.util.List;

import org.jaxen.function.StringFunction;

public class TodoTaskModel {
	private String id;
	private String patrolId;
	private String recordOrder;
	private String opinion;
	private String status;
	private String x;
	private String y;
	private String handlerGrid;
	private String createId;
	private String createTime;
	private String imgs;
	private String taskId;
	private String taskName;
	private String patrolObjectId;
	private String patrolObjectName;
	private String isHaveProblem;
	private String address;
	private String problemDesc;
	private String createUserName;
	private String createGridName;
	private String judgeTura;

	public String getJudgeTura() {
		return judgeTura;
	}

	public void setJudgeTura(String judgeTura) {
		this.judgeTura = judgeTura;
	}

	private List<ProblemDescModel> listProblemDesc = new ArrayList<ProblemDescModel>();
	private List<ProblemImgsModel> problemImgs = new ArrayList<ProblemImgsModel>();
	private List<ProblemsModel> problems = new ArrayList<ProblemsModel>();
	private String recordId;

	public TodoTaskModel() {
		super();
	}

	public TodoTaskModel(String id, String patrolId, String recordOrder, String opinion, String status, String x,
			String y, String handlerGrid, String createId, String createTime, String imgs, String taskId,
			String taskName, String patrolObjectId, String patrolObjectName, String isHaveProblem, String address,
			String problemDesc, String createUserName, String createGridName, String judgeTura,
			List<ProblemDescModel> listProblemDesc, List<ProblemImgsModel> problemImgs, String recordId,
			List<ProblemsModel> problems) {
		super();
		this.id = id;
		this.patrolId = patrolId;
		this.recordOrder = recordOrder;
		this.opinion = opinion;
		this.status = status;
		this.x = x;
		this.y = y;
		this.handlerGrid = handlerGrid;
		this.createId = createId;
		this.createTime = createTime;
		this.imgs = imgs;
		this.taskId = taskId;
		this.taskName = taskName;
		this.patrolObjectId = patrolObjectId;
		this.patrolObjectName = patrolObjectName;
		this.isHaveProblem = isHaveProblem;
		this.address = address;
		this.problemDesc = problemDesc;
		this.createUserName = createUserName;
		this.createGridName = createGridName;
		this.listProblemDesc = listProblemDesc;
		this.problemImgs = problemImgs;
		this.recordId = recordId;
		this.problems = problems;
		this.judgeTura = judgeTura;
	}

	public List<ProblemsModel> getProblems() {
		return problems;
	}

	public void setProblems(List<ProblemsModel> problems) {
		this.problems = problems;
	}

	public List<ProblemImgsModel> getProblemImgs() {
		return problemImgs;
	}

	public void setProblemImgs(List<ProblemImgsModel> problemImgs) {
		this.problemImgs = problemImgs;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getRecordOrder() {
		return recordOrder;
	}

	public void setRecordOrder(String recordOrder) {
		this.recordOrder = recordOrder;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getHandlerGrid() {
		return handlerGrid;
	}

	public void setHandlerGrid(String handlerGrid) {
		this.handlerGrid = handlerGrid;
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

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getPatrolObjectId() {
		return patrolObjectId;
	}

	public void setPatrolObjectId(String patrolObjectId) {
		this.patrolObjectId = patrolObjectId;
	}

	public String getPatrolObjectName() {
		return patrolObjectName;
	}

	public void setPatrolObjectName(String patrolObjectName) {
		this.patrolObjectName = patrolObjectName;
	}

	public String getIsHaveProblem() {
		return isHaveProblem;
	}

	public void setIsHaveProblem(String isHaveProblem) {
		this.isHaveProblem = isHaveProblem;
	}

	public String getProblemDesc() {
		return problemDesc;
	}

	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateGridName() {
		return createGridName;
	}

	public void setCreateGridName(String createGridName) {
		this.createGridName = createGridName;
	}

	public List<ProblemDescModel> getListProblemDesc() {
		return listProblemDesc;
	}

	public void setListProblemDesc(List<ProblemDescModel> listProblemDesc) {
		this.listProblemDesc = listProblemDesc;
	}

	@Override
	public String toString() {
		return "TodoTaskModel [id=" + id + ", patrolId=" + patrolId + ", recordOrder=" + recordOrder + ", opinion="
				+ opinion + ", status=" + status + ", x=" + x + ", y=" + y + ", handlerGrid=" + handlerGrid
				+ ", createId=" + createId + ", createTime=" + createTime + ", imgs=" + imgs + ", taskId=" + taskId
				+ ", taskName=" + taskName + ", patrolObjectId=" + patrolObjectId + ", patrolObjectName="
				+ patrolObjectName + ", isHaveProblem=" + isHaveProblem + ", address=" + address + ", problemDesc="
				+ problemDesc + ", createUserName=" + createUserName + ", createGridName=" + createGridName
				+ ", judgeTura=" + judgeTura + ", listProblemDesc=" + listProblemDesc + ", problemImgs=" + problemImgs
				+ ", problems=" + problems + ", recordId=" + recordId + "]";
	}

}
