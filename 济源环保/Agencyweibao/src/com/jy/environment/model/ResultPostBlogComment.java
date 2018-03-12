package com.jy.environment.model;

public class ResultPostBlogComment {
    
    private boolean flag;
    private String commentId;

	public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (flag ? 1231 : 1237);
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ResultPostBlogComment other = (ResultPostBlogComment) obj;
	if (flag != other.flag)
	    return false;
	return true;
    }


	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}


    
}
