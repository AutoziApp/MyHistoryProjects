package com.jy.environment.database.model;

public class ModelWebServiceRequest
{
	private long mID;
	private int editTime;
	private int mUrlHashCode;
	private int mUrlParamHashCode;
	private int mBodyParamHashCode;
	private String mResult;
	
	public long getID()
	{
		return mID;
	}
	public void setID(long pID)
	{
		mID = pID;
	}
	
	
	public long getEditTime() {
	    return editTime;
	}
	public void setEditTime(int editTime) {
	    this.editTime = editTime;
	}
	public int getUrlHashCode()
	{
		return mUrlHashCode;
	}
	public void setUrlHashCode(int pUrlHashCode)
	{
		mUrlHashCode = pUrlHashCode;
	}
	public String getResult()
	{
		return mResult;
	}
	public void setResult(String pResult)
	{
		mResult = pResult;
	}
	public int getUrlParamHashCode()
	{
		return mUrlParamHashCode;
	}
	public void setUrlParamHashCode(int pUrlParamHashCode)
	{
		mUrlParamHashCode = pUrlParamHashCode;
	}
	public int getBodyParamHashCode()
	{
		return mBodyParamHashCode;
	}
	public void setBodyParamHashCode(int pBodyParamHashCode)
	{
		mBodyParamHashCode = pBodyParamHashCode;
	}
	public long getmID() {
		return mID;
	}
	public void setmID(long mID) {
		this.mID = mID;
	}
	public int getmUrlHashCode() {
		return mUrlHashCode;
	}
	public void setmUrlHashCode(int mUrlHashCode) {
		this.mUrlHashCode = mUrlHashCode;
	}
	public int getmUrlParamHashCode() {
		return mUrlParamHashCode;
	}
	public void setmUrlParamHashCode(int mUrlParamHashCode) {
		this.mUrlParamHashCode = mUrlParamHashCode;
	}
	public int getmBodyParamHashCode() {
		return mBodyParamHashCode;
	}
	public void setmBodyParamHashCode(int mBodyParamHashCode) {
		this.mBodyParamHashCode = mBodyParamHashCode;
	}
	public String getmResult() {
		return mResult;
	}
	public void setmResult(String mResult) {
		this.mResult = mResult;
	}
	@Override
	public String toString() {
	    return "ModelWebServiceRequest [mID=" + mID + ", editTime="
		    + editTime + ", mUrlHashCode=" + mUrlHashCode
		    + ", mUrlParamHashCode=" + mUrlParamHashCode
		    + ", mBodyParamHashCode=" + mBodyParamHashCode
		    + ", mResult=" + mResult + "]";
	}
	
	

}
