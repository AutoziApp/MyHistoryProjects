package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class NoTaskSignBean {


    /**
     * total : 1
     * rows : [{"uuid":"4028f8fb5e11ef8c015e12dfea810039","task":{"uuid":"4028f8fb5e11ef8c015e12dd6c0d0030","taskTypeUuid":"4028f8fb5e02c3db015e02d4b90e0000","taskType":"施工工地","name":"测试任务","description":"测试任务","startTime":"2017-08-24 00:00:00","endTime":"2017-08-25 00:00:00","createUser":"O(∩_∩)O哈哈~","createTime":"2017-08-24 14:10:54"},"taskType":"施工工地","gridName":"城关街道办事处","startTime":"2017-08-24 00:00:00","endTime":"2017-08-25 00:00:00","status":0,"createTime":"2017-08-24 14:13:38"}]
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

    public static class RowsBean {
        /**
         * uuid : 4028f8fb5e11ef8c015e12dfea810039
         * task : {"uuid":"4028f8fb5e11ef8c015e12dd6c0d0030","taskTypeUuid":"4028f8fb5e02c3db015e02d4b90e0000","taskType":"施工工地","name":"测试任务","description":"测试任务","startTime":"2017-08-24 00:00:00","endTime":"2017-08-25 00:00:00","createUser":"O(∩_∩)O哈哈~","createTime":"2017-08-24 14:10:54"}
         * taskType : 施工工地
         * gridName : 城关街道办事处
         * startTime : 2017-08-24 00:00:00
         * endTime : 2017-08-25 00:00:00
         * status : 0
         * createTime : 2017-08-24 14:13:38
         */

        private String uuid;
        private TaskBean task;
        private String taskType;
        private String gridName;
        private String startTime;
        private String endTime;
        private int status;
        private String createTime;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public TaskBean getTask() {
            return task;
        }

        public void setTask(TaskBean task) {
            this.task = task;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public static class TaskBean {
            /**
             * uuid : 4028f8fb5e11ef8c015e12dd6c0d0030
             * taskTypeUuid : 4028f8fb5e02c3db015e02d4b90e0000
             * taskType : 施工工地
             * name : 测试任务
             * description : 测试任务
             * startTime : 2017-08-24 00:00:00
             * endTime : 2017-08-25 00:00:00
             * createUser : O(∩_∩)O哈哈~
             * createTime : 2017-08-24 14:10:54
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
}
