package com.mapuni.shangluo.bean;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.bean
 * @class 查询记录 下发、延时 任务详情 通用Bean
 * @anthor Tianfy
 * @time 2017/9/1 10:36
 * @change
 * @chang time
 * @class describe
 */

public class DelayCompleteBean {


    /**
     * total : 0
     * rows : {"uuid":"4028f8fb5e30acff015e30b4834f0006","taskTypeUuid":"4028b88e5de893f3015de8c6aa310004","taskType":"日常工作","name":"8.30-3","description":"测试任务-3","startTime":"2017-08-30 00:00:00","endTime":"2017-09-04 00:00:00","createUser":"李熙平","createTime":"2017-08-30 09:14:50"}
     */

    private int total;
    private RowsBean rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public RowsBean getRows() {
        return rows;
    }

    public void setRows(RowsBean rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * uuid : 4028f8fb5e30acff015e30b4834f0006
         * taskTypeUuid : 4028b88e5de893f3015de8c6aa310004
         * taskType : 日常工作
         * name : 8.30-3
         * description : 测试任务-3
         * startTime : 2017-08-30 00:00:00
         * endTime : 2017-09-04 00:00:00
         * createUser : 李熙平
         * createTime : 2017-08-30 09:14:50
         */

        private String uuid;
        private String taskTypeUuid;
        private String taskType;
        private String name;
        private String description;
        private String startTime;
        private String endTime;
        private String createUser;
        private String createTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTaskTypeUuid() {
            return taskTypeUuid;
        }

        public void setTaskTypeUuid(String taskTypeUuid) {
            this.taskTypeUuid = taskTypeUuid;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
