package cn.com.mapuni.meshing.model;

import java.util.List;

/**
 * Created by Mai on 2017/2/22.
 */

public class DbTaskList {

	 private List<RowsBean> rows;

	    public List<RowsBean> getRows() {
	        return rows;
	    }

	    public void setRows(List<RowsBean> rows) {
	        this.rows = rows;
	    }

	    public static class RowsBean {
	        /**
	         * id : AEDC5E2E-0B3F-4456-A878-147FAB1DFC75
	         * entName : null
	         * entAddress : null
	         * latitude : 36.713376
	         * longitude : 116.935927
	         * createTime : 2017-02-19 09:58:03.0
	         * importance : null
	         * userId : f4ea4165-d42f-4e8c-9b5e-4ae478f4d918
	         * remark : null
	         * creator : 李学良
	         * taskName : 药山街道黄岗居委会卫生突击监察
	         * raskStatus : 0
	         * gridCode : 药山街道黄岗居委会
	         * taskFinshTime : 2017-03-09 10:39:33.0
	         * updateTime : null
	         * taskcontent : null
	         * createrID : null
	         */

	        private String id;
	        private String entName;
	        private String entAddress;
	        private double latitude;
	        private double longitude;
	        private String createTime;
	        private String importance;
	        private String userId;
	        private String remark;
	        private String creator;
	        private String taskName;
	        private String raskStatus;
	        private String gridCode;
	        private String taskFinshTime;
	        private String updateTime;
	        private String taskcontent;
	        private String createrID;

	        public String getId() {
	            return id;
	        }

	        public void setId(String id) {
	            this.id = id;
	        }

	        public String getEntName() {
	            return entName;
	        }

	        public void setEntName(String entName) {
	            this.entName = entName;
	        }

	        public String getEntAddress() {
	            return entAddress;
	        }

	        public void setEntAddress(String entAddress) {
	            this.entAddress = entAddress;
	        }

	        public double getLatitude() {
	            return latitude;
	        }

	        public void setLatitude(double latitude) {
	            this.latitude = latitude;
	        }

	        public double getLongitude() {
	            return longitude;
	        }

	        public void setLongitude(double longitude) {
	            this.longitude = longitude;
	        }

	        public String getCreateTime() {
	            return createTime;
	        }

	        public void setCreateTime(String createTime) {
	            this.createTime = createTime;
	        }

	        public String getImportance() {
	            return importance;
	        }

	        public void setImportance(String importance) {
	            this.importance = importance;
	        }

	        public String getUserId() {
	            return userId;
	        }

	        public void setUserId(String userId) {
	            this.userId = userId;
	        }

	        public String getRemark() {
	            return remark;
	        }

	        public void setRemark(String remark) {
	            this.remark = remark;
	        }

	        public String getCreator() {
	            return creator;
	        }

	        public void setCreator(String creator) {
	            this.creator = creator;
	        }

	        public String getTaskName() {
	            return taskName;
	        }

	        public void setTaskName(String taskName) {
	            this.taskName = taskName;
	        }

	        public String getRaskStatus() {
	            return raskStatus;
	        }

	        public void setRaskStatus(String raskStatus) {
	            this.raskStatus = raskStatus;
	        }

	        public String getGridCode() {
	            return gridCode;
	        }

	        public void setGridCode(String gridCode) {
	            this.gridCode = gridCode;
	        }

	        public String getTaskFinshTime() {
	            return taskFinshTime;
	        }

	        public void setTaskFinshTime(String taskFinshTime) {
	            this.taskFinshTime = taskFinshTime;
	        }

	        public String getUpdateTime() {
	            return updateTime;
	        }

	        public void setUpdateTime(String updateTime) {
	            this.updateTime = updateTime;
	        }

	        public String getTaskcontent() {
	            return taskcontent;
	        }

	        public void setTaskcontent(String taskcontent) {
	            this.taskcontent = taskcontent;
	        }

	        public String getCreaterID() {
	            return createrID;
	        }

	        public void setCreaterID(String createrID) {
	            this.createrID = createrID;
	        }
	    }
}

