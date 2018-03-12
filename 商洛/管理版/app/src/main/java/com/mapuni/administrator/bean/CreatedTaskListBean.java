package com.mapuni.administrator.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Administrator
 * @time 2017/10/18 11:33
 * @change
 * @chang time
 * @class describe
 */

public class CreatedTaskListBean {


    /**
     * rows : [{"createTime":"2017-10-18 10:13:49","createUser":"李娜","description":"推销","endTime":"2017-10-18 23:59:59","name":"手机再来一个","startTime":"2017-10-18 00:00:00","taskType":"普通任务","taskTypeUuid":"4028b88e5dcee431015dcf0161bd0003","uuid":"4028f88e5f29983c015f2d4222160070"},{"createTime":"2017-10-18 10:09:19","createUser":"李娜","description":"手机创建1","endTime":"2017-10-31 23:59:59","name":"手机创建1","startTime":"2017-10-18 00:00:00","taskType":"普通任务","taskTypeUuid":"4028b88e5dcee431015dcf0161bd0003","uuid":"4028f88e5f29983c015f2d3e016a006e"},{"createTime":"2017-10-18 10:08:59","createUser":"李娜","description":"手机创建1","endTime":"2017-10-31 23:59:59","name":"手机创建1","startTime":"2017-10-18 00:00:00","taskType":"普通任务","taskTypeUuid":"4028b88e5dcee431015dcf0161bd0003","uuid":"4028f88e5f29983c015f2d3db3e0006d"},{"createTime":"2017-10-18 10:01:50","createUser":"李娜","description":"手机床来哦的","endTime":"2017-10-20 23:59:59","name":"手机创建","startTime":"2017-10-18 00:00:00","taskType":"普通任务","taskTypeUuid":"4028b88e5dcee431015dcf0161bd0003","uuid":"4028f88e5f29983c015f2d3726f40067"},{"createTime":"2017-10-18 08:39:42","createUser":"李娜","description":"哈哈哈哈哈","endTime":"2017-10-25 23:59:59","name":"测试任务","startTime":"2017-10-18 00:00:00","taskType":"普通任务","taskTypeUuid":"4028b88e5dcee431015dcf0161bd0003","uuid":"4028f88e5f29983c015f2cebf7af003f"}]
     * total : 5
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable{
        /**
         * createTime : 2017-10-18 10:13:49
         * createUser : 李娜
         * description : 推销
         * endTime : 2017-10-18 23:59:59
         * name : 手机再来一个
         * startTime : 2017-10-18 00:00:00
         * taskType : 普通任务
         * taskTypeUuid : 4028b88e5dcee431015dcf0161bd0003
         * uuid : 4028f88e5f29983c015f2d4222160070
         */

        private String createTime;
        private String createUser;
        private String description;
        private String endTime;
        private String name;
        private String startTime;
        private String taskType;
        private String taskTypeUuid;
        private String uuid;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getTaskTypeUuid() {
            return taskTypeUuid;
        }

        public void setTaskTypeUuid(String taskTypeUuid) {
            this.taskTypeUuid = taskTypeUuid;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
