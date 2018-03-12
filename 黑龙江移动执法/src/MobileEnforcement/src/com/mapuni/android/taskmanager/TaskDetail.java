package com.mapuni.android.taskmanager;

import java.util.Date;

public class TaskDetail {
	private String guid = ""; /*唯一标示*/
	private String qYDM; /*企业代码*/
    private String rWBH; /*任务编号*/
	private Date fBSJ;/* 发布时间*/
	private String bJQX = "";/* 办结期限*/
	private String rWZT = "";/* 任务状态*/
	private String fBR = ""; /*发布人*/
	private Date wCSJ; /*完成时间*/
	private String rWLY = ""; /*任务来源*/
	private String rWLX = ""; /*任务类型*/
	private String bZ = ""; /*备注*/
	private String jJCD = ""; /*紧急程度*/
	private String qSYJ = ""; /*起始意见*/
	private String sSKS = ""; /*所属科室*/
    private Date updateTime;
    private String rWMC = ""; /*任务名称*/
    private String sSJH = "";/* 所属计划*/
    private String jzshyj = ""; /*局长审核意见*/
    private String jzshsj = "";
    private String shjz = "";
    private String shkz = ""; /*审核科长*/
    private String kzshsj = "";
    private String specialTemplateID = ""; /*执法模板ID*/
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getqYDM() {
		return qYDM;
	}
	public void setqYDM(String qYDM) {
		this.qYDM = qYDM;
	}
	public String getrWBH() {
		return rWBH;
	}
	public void setrWBH(String rWBH) {
		this.rWBH = rWBH;
	}
	public Date getfBSJ() {
		return fBSJ;
	}
	public void setfBSJ(Date fBSJ) {
		this.fBSJ = fBSJ;
	}
	public String getbJQX() {
		return bJQX;
	}
	public void setbJQX(String bJQX) {
		this.bJQX = bJQX;
	}
	public String getrWZT() {
		return rWZT;
	}
	public void setrWZT(String rWZT) {
		this.rWZT = rWZT;
	}
	public String getfBR() {
		return fBR;
	}
	public void setfBR(String fBR) {
		this.fBR = fBR;
	}
	public Date getwCSJ() {
		return wCSJ;
	}
	public void setwCSJ(Date wCSJ) {
		this.wCSJ = wCSJ;
	}
	public String getrWLY() {
		return rWLY;
	}
	public void setrWLY(String rWLY) {
		this.rWLY = rWLY;
	}
	public String getrWLX() {
		return rWLX;
	}
	public void setrWLX(String rWLX) {
		this.rWLX = rWLX;
	}
	public String getbZ() {
		return bZ;
	}
	public void setbZ(String bZ) {
		this.bZ = bZ;
	}
	public String getjJCD() {
		return jJCD;
	}
	public void setjJCD(String jJCD) {
		this.jJCD = jJCD;
	}
	public String getqSYJ() {
		return qSYJ;
	}
	public void setqSYJ(String qSYJ) {
		this.qSYJ = qSYJ;
	}
	public String getsSKS() {
		return sSKS;
	}
	public void setsSKS(String sSKS) {
		this.sSKS = sSKS;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getrWMC() {
		return rWMC;
	}
	public void setrWMC(String rWMC) {
		this.rWMC = rWMC;
	}
	public String getsSJH() {
		return sSJH;
	}
	public void setsSJH(String sSJH) {
		this.sSJH = sSJH;
	}
	public String getJzshyj() {
		return jzshyj;
	}
	public void setJzshyj(String jzshyj) {
		this.jzshyj = jzshyj;
	}
	public String getJzshsj() {
		return jzshsj;
	}
	public void setJzshsj(String jzshsj) {
		this.jzshsj = jzshsj;
	}
	public String getShjz() {
		return shjz;
	}
	public void setShjz(String shjz) {
		this.shjz = shjz;
	}
	public String getShkz() {
		return shkz;
	}
	public void setShkz(String shkz) {
		this.shkz = shkz;
	}
	public String getKzshsj() {
		return kzshsj;
	}
	public void setKzshsj(String kzshsj) {
		this.kzshsj = kzshsj;
	}
	public String getSpecialTemplateID() {
		return specialTemplateID;
	}
	public void setSpecialTemplateID(String specialTemplateID) {
		this.specialTemplateID = specialTemplateID;
	}
	@Override
	public String toString() {
		return "TaskDetail [guid=" + guid + ", qYDM=" + qYDM + ", rWBH=" + rWBH
				+ ", fBSJ=" + fBSJ + ", bJQX=" + bJQX + ", rWZT=" + rWZT
				+ ", fBR=" + fBR + ", wCSJ=" + wCSJ + ", rWLY=" + rWLY
				+ ", rWLX=" + rWLX + ", bZ=" + bZ + ", jJCD=" + jJCD
				+ ", qSYJ=" + qSYJ + ", sSKS=" + sSKS + ", updateTime="
				+ updateTime + ", rWMC=" + rWMC + ", sSJH=" + sSJH
				+ ", jzshyj=" + jzshyj + ", jzshsj=" + jzshsj + ", shjz="
				+ shjz + ", shkz=" + shkz + ", kzshsj=" + kzshsj
				+ ", specialTemplateID=" + specialTemplateID + "]";
	}
	
	
}
