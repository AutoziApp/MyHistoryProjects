package com.mapuni.administrator.bean;

/**
 * Created by 15225 on 2017/10/16.
 */

public class DownDelayTaskBean {


    /**
     * status : 0
     * msg : success
     * data : {"uuid":"4028f88e5f2328c2015f23301a44000a","task":{"createTime":"2017-10-16 11:17:48","description":"1111","endTime":"2017-10-17 23:59:59","name":"下发","startTime":"2017-10-17 00:00:00","taskType":{"description":"非紧急情况下的日常工作任务。","name":"普通任务","uuid":"4028b88e5dcee431015dcf0161bd0003"},"users":{"address":"","createTime":"2017-09-22 09:26:06","email":"","grid":{"code":"","createTime":"2017-09-22 09:26:06","grid":{"code":"","createTime":"2017-09-22 09:26:05","grid":{"code":"611000","createTime":"2017-09-22 09:26:03","grid":{"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"},"gridType":1,"isDelete":0,"isEnable":0,"level":1,"name":"商洛市","sort":0,"updateTime":"2017-09-25 15:24:20","uuid":"ed4bcf033ed742718044541021fcb569"},"gridType":1,"isDelete":0,"isEnable":0,"level":2,"name":"丹凤县","sort":0,"updateTime":"2017-09-22 09:26:05","uuid":"7fa664940cbb4185983c82e72c2279eb"},"gridType":1,"isDelete":0,"isEnable":0,"level":3,"name":"竹林关镇","sort":0,"updateTime":"2017-09-22 09:26:06","uuid":"253579fcb7904a89aa2f1fe5d1354b72"},"isDelete":0,"isEnable":0,"loginName":"lina","password":"wg123456","sex":2,"sort":0,"telephone":"15309141902","updateTime":"2017-09-22 09:26:06","userRealname":"李娜","uuid":"57e3d2cc5f6742a88361ec926d58faa4"},"uuid":"4028f88e5f2328c2015f232ffc2b0009"},"taskType":"普通任务","gridName":"州河北村","startTime":"2017-10-17 00:00:00","endTime":"2017-10-17 23:59:59","status":1,"jurisdictionJudge":null,"applyStartTime":"2017-10-19 00:00:00","applyEndTime":"2017-10-19 23:59:59","applyReason":"测试1111","handledRecordUuid":null,"handlingRecordUuid":null,"handledOrhandlingStatus":0,"judgeDetailsStatus":0,"issuedTaskStatus":0,"createTime":"2017-10-16 11:17:55"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uuid : 4028f88e5f2328c2015f23301a44000a
         * task : {"createTime":"2017-10-16 11:17:48","description":"1111","endTime":"2017-10-17 23:59:59","name":"下发","startTime":"2017-10-17 00:00:00","taskType":{"description":"非紧急情况下的日常工作任务。","name":"普通任务","uuid":"4028b88e5dcee431015dcf0161bd0003"},"users":{"address":"","createTime":"2017-09-22 09:26:06","email":"","grid":{"code":"","createTime":"2017-09-22 09:26:06","grid":{"code":"","createTime":"2017-09-22 09:26:05","grid":{"code":"611000","createTime":"2017-09-22 09:26:03","grid":{"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"},"gridType":1,"isDelete":0,"isEnable":0,"level":1,"name":"商洛市","sort":0,"updateTime":"2017-09-25 15:24:20","uuid":"ed4bcf033ed742718044541021fcb569"},"gridType":1,"isDelete":0,"isEnable":0,"level":2,"name":"丹凤县","sort":0,"updateTime":"2017-09-22 09:26:05","uuid":"7fa664940cbb4185983c82e72c2279eb"},"gridType":1,"isDelete":0,"isEnable":0,"level":3,"name":"竹林关镇","sort":0,"updateTime":"2017-09-22 09:26:06","uuid":"253579fcb7904a89aa2f1fe5d1354b72"},"isDelete":0,"isEnable":0,"loginName":"lina","password":"wg123456","sex":2,"sort":0,"telephone":"15309141902","updateTime":"2017-09-22 09:26:06","userRealname":"李娜","uuid":"57e3d2cc5f6742a88361ec926d58faa4"},"uuid":"4028f88e5f2328c2015f232ffc2b0009"}
         * taskType : 普通任务
         * gridName : 州河北村
         * startTime : 2017-10-17 00:00:00
         * endTime : 2017-10-17 23:59:59
         * status : 1
         * jurisdictionJudge : null
         * applyStartTime : 2017-10-19 00:00:00
         * applyEndTime : 2017-10-19 23:59:59
         * applyReason : 测试1111
         * handledRecordUuid : null
         * handlingRecordUuid : null
         * handledOrhandlingStatus : 0
         * judgeDetailsStatus : 0
         * issuedTaskStatus : 0
         * createTime : 2017-10-16 11:17:55
         */

        private String uuid;
        private TaskBean task;
        private String taskType;
        private String gridName;
        private String startTime;
        private String endTime;
        private int status;
        private Object jurisdictionJudge;
        private String applyStartTime;
        private String applyEndTime;
        private String applyReason;
        private Object handledRecordUuid;
        private Object handlingRecordUuid;
        private int handledOrhandlingStatus;
        private int judgeDetailsStatus;
        private int issuedTaskStatus;
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

        public Object getJurisdictionJudge() {
            return jurisdictionJudge;
        }

        public void setJurisdictionJudge(Object jurisdictionJudge) {
            this.jurisdictionJudge = jurisdictionJudge;
        }

        public String getApplyStartTime() {
            return applyStartTime;
        }

        public void setApplyStartTime(String applyStartTime) {
            this.applyStartTime = applyStartTime;
        }

        public String getApplyEndTime() {
            return applyEndTime;
        }

        public void setApplyEndTime(String applyEndTime) {
            this.applyEndTime = applyEndTime;
        }

        public String getApplyReason() {
            return applyReason;
        }

        public void setApplyReason(String applyReason) {
            this.applyReason = applyReason;
        }

        public Object getHandledRecordUuid() {
            return handledRecordUuid;
        }

        public void setHandledRecordUuid(Object handledRecordUuid) {
            this.handledRecordUuid = handledRecordUuid;
        }

        public Object getHandlingRecordUuid() {
            return handlingRecordUuid;
        }

        public void setHandlingRecordUuid(Object handlingRecordUuid) {
            this.handlingRecordUuid = handlingRecordUuid;
        }

        public int getHandledOrhandlingStatus() {
            return handledOrhandlingStatus;
        }

        public void setHandledOrhandlingStatus(int handledOrhandlingStatus) {
            this.handledOrhandlingStatus = handledOrhandlingStatus;
        }

        public int getJudgeDetailsStatus() {
            return judgeDetailsStatus;
        }

        public void setJudgeDetailsStatus(int judgeDetailsStatus) {
            this.judgeDetailsStatus = judgeDetailsStatus;
        }

        public int getIssuedTaskStatus() {
            return issuedTaskStatus;
        }

        public void setIssuedTaskStatus(int issuedTaskStatus) {
            this.issuedTaskStatus = issuedTaskStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public static class TaskBean {
            /**
             * createTime : 2017-10-16 11:17:48
             * description : 1111
             * endTime : 2017-10-17 23:59:59
             * name : 下发
             * startTime : 2017-10-17 00:00:00
             * taskType : {"description":"非紧急情况下的日常工作任务。","name":"普通任务","uuid":"4028b88e5dcee431015dcf0161bd0003"}
             * users : {"address":"","createTime":"2017-09-22 09:26:06","email":"","grid":{"code":"","createTime":"2017-09-22 09:26:06","grid":{"code":"","createTime":"2017-09-22 09:26:05","grid":{"code":"611000","createTime":"2017-09-22 09:26:03","grid":{"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"},"gridType":1,"isDelete":0,"isEnable":0,"level":1,"name":"商洛市","sort":0,"updateTime":"2017-09-25 15:24:20","uuid":"ed4bcf033ed742718044541021fcb569"},"gridType":1,"isDelete":0,"isEnable":0,"level":2,"name":"丹凤县","sort":0,"updateTime":"2017-09-22 09:26:05","uuid":"7fa664940cbb4185983c82e72c2279eb"},"gridType":1,"isDelete":0,"isEnable":0,"level":3,"name":"竹林关镇","sort":0,"updateTime":"2017-09-22 09:26:06","uuid":"253579fcb7904a89aa2f1fe5d1354b72"},"isDelete":0,"isEnable":0,"loginName":"lina","password":"wg123456","sex":2,"sort":0,"telephone":"15309141902","updateTime":"2017-09-22 09:26:06","userRealname":"李娜","uuid":"57e3d2cc5f6742a88361ec926d58faa4"}
             * uuid : 4028f88e5f2328c2015f232ffc2b0009
             */

            private String createTime;
            private String description;
            private String endTime;
            private String name;
            private String startTime;
            private TaskTypeBean taskType;
            private UsersBean users;
            private String uuid;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
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

            public TaskTypeBean getTaskType() {
                return taskType;
            }

            public void setTaskType(TaskTypeBean taskType) {
                this.taskType = taskType;
            }

            public UsersBean getUsers() {
                return users;
            }

            public void setUsers(UsersBean users) {
                this.users = users;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public static class TaskTypeBean {
                /**
                 * description : 非紧急情况下的日常工作任务。
                 * name : 普通任务
                 * uuid : 4028b88e5dcee431015dcf0161bd0003
                 */

                private String description;
                private String name;
                private String uuid;

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }
            }

            public static class UsersBean {
                /**
                 * address :
                 * createTime : 2017-09-22 09:26:06
                 * email :
                 * grid : {"code":"","createTime":"2017-09-22 09:26:06","grid":{"code":"","createTime":"2017-09-22 09:26:05","grid":{"code":"611000","createTime":"2017-09-22 09:26:03","grid":{"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"},"gridType":1,"isDelete":0,"isEnable":0,"level":1,"name":"商洛市","sort":0,"updateTime":"2017-09-25 15:24:20","uuid":"ed4bcf033ed742718044541021fcb569"},"gridType":1,"isDelete":0,"isEnable":0,"level":2,"name":"丹凤县","sort":0,"updateTime":"2017-09-22 09:26:05","uuid":"7fa664940cbb4185983c82e72c2279eb"},"gridType":1,"isDelete":0,"isEnable":0,"level":3,"name":"竹林关镇","sort":0,"updateTime":"2017-09-22 09:26:06","uuid":"253579fcb7904a89aa2f1fe5d1354b72"}
                 * isDelete : 0
                 * isEnable : 0
                 * loginName : lina
                 * password : wg123456
                 * sex : 2
                 * sort : 0
                 * telephone : 15309141902
                 * updateTime : 2017-09-22 09:26:06
                 * userRealname : 李娜
                 * uuid : 57e3d2cc5f6742a88361ec926d58faa4
                 */

                private String address;
                private String createTime;
                private String email;
                private GridBeanXXX grid;
                private int isDelete;
                private int isEnable;
                private String loginName;
                private String password;
                private int sex;
                private int sort;
                private String telephone;
                private String updateTime;
                private String userRealname;
                private String uuid;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public GridBeanXXX getGrid() {
                    return grid;
                }

                public void setGrid(GridBeanXXX grid) {
                    this.grid = grid;
                }

                public int getIsDelete() {
                    return isDelete;
                }

                public void setIsDelete(int isDelete) {
                    this.isDelete = isDelete;
                }

                public int getIsEnable() {
                    return isEnable;
                }

                public void setIsEnable(int isEnable) {
                    this.isEnable = isEnable;
                }

                public String getLoginName() {
                    return loginName;
                }

                public void setLoginName(String loginName) {
                    this.loginName = loginName;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getTelephone() {
                    return telephone;
                }

                public void setTelephone(String telephone) {
                    this.telephone = telephone;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }

                public String getUserRealname() {
                    return userRealname;
                }

                public void setUserRealname(String userRealname) {
                    this.userRealname = userRealname;
                }

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }

                public static class GridBeanXXX {
                    /**
                     * code :
                     * createTime : 2017-09-22 09:26:06
                     * grid : {"code":"","createTime":"2017-09-22 09:26:05","grid":{"code":"611000","createTime":"2017-09-22 09:26:03","grid":{"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"},"gridType":1,"isDelete":0,"isEnable":0,"level":1,"name":"商洛市","sort":0,"updateTime":"2017-09-25 15:24:20","uuid":"ed4bcf033ed742718044541021fcb569"},"gridType":1,"isDelete":0,"isEnable":0,"level":2,"name":"丹凤县","sort":0,"updateTime":"2017-09-22 09:26:05","uuid":"7fa664940cbb4185983c82e72c2279eb"}
                     * gridType : 1
                     * isDelete : 0
                     * isEnable : 0
                     * level : 3
                     * name : 竹林关镇
                     * sort : 0
                     * updateTime : 2017-09-22 09:26:06
                     * uuid : 253579fcb7904a89aa2f1fe5d1354b72
                     */

                    private String code;
                    private String createTime;
                    private GridBeanXX grid;
                    private int gridType;
                    private int isDelete;
                    private int isEnable;
                    private int level;
                    private String name;
                    private int sort;
                    private String updateTime;
                    private String uuid;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getCreateTime() {
                        return createTime;
                    }

                    public void setCreateTime(String createTime) {
                        this.createTime = createTime;
                    }

                    public GridBeanXX getGrid() {
                        return grid;
                    }

                    public void setGrid(GridBeanXX grid) {
                        this.grid = grid;
                    }

                    public int getGridType() {
                        return gridType;
                    }

                    public void setGridType(int gridType) {
                        this.gridType = gridType;
                    }

                    public int getIsDelete() {
                        return isDelete;
                    }

                    public void setIsDelete(int isDelete) {
                        this.isDelete = isDelete;
                    }

                    public int getIsEnable() {
                        return isEnable;
                    }

                    public void setIsEnable(int isEnable) {
                        this.isEnable = isEnable;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getSort() {
                        return sort;
                    }

                    public void setSort(int sort) {
                        this.sort = sort;
                    }

                    public String getUpdateTime() {
                        return updateTime;
                    }

                    public void setUpdateTime(String updateTime) {
                        this.updateTime = updateTime;
                    }

                    public String getUuid() {
                        return uuid;
                    }

                    public void setUuid(String uuid) {
                        this.uuid = uuid;
                    }

                    public static class GridBeanXX {
                        /**
                         * code :
                         * createTime : 2017-09-22 09:26:05
                         * grid : {"code":"611000","createTime":"2017-09-22 09:26:03","grid":{"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"},"gridType":1,"isDelete":0,"isEnable":0,"level":1,"name":"商洛市","sort":0,"updateTime":"2017-09-25 15:24:20","uuid":"ed4bcf033ed742718044541021fcb569"}
                         * gridType : 1
                         * isDelete : 0
                         * isEnable : 0
                         * level : 2
                         * name : 丹凤县
                         * sort : 0
                         * updateTime : 2017-09-22 09:26:05
                         * uuid : 7fa664940cbb4185983c82e72c2279eb
                         */

                        private String code;
                        private String createTime;
                        private GridBeanX grid;
                        private int gridType;
                        private int isDelete;
                        private int isEnable;
                        private int level;
                        private String name;
                        private int sort;
                        private String updateTime;
                        private String uuid;

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getCreateTime() {
                            return createTime;
                        }

                        public void setCreateTime(String createTime) {
                            this.createTime = createTime;
                        }

                        public GridBeanX getGrid() {
                            return grid;
                        }

                        public void setGrid(GridBeanX grid) {
                            this.grid = grid;
                        }

                        public int getGridType() {
                            return gridType;
                        }

                        public void setGridType(int gridType) {
                            this.gridType = gridType;
                        }

                        public int getIsDelete() {
                            return isDelete;
                        }

                        public void setIsDelete(int isDelete) {
                            this.isDelete = isDelete;
                        }

                        public int getIsEnable() {
                            return isEnable;
                        }

                        public void setIsEnable(int isEnable) {
                            this.isEnable = isEnable;
                        }

                        public int getLevel() {
                            return level;
                        }

                        public void setLevel(int level) {
                            this.level = level;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public int getSort() {
                            return sort;
                        }

                        public void setSort(int sort) {
                            this.sort = sort;
                        }

                        public String getUpdateTime() {
                            return updateTime;
                        }

                        public void setUpdateTime(String updateTime) {
                            this.updateTime = updateTime;
                        }

                        public String getUuid() {
                            return uuid;
                        }

                        public void setUuid(String uuid) {
                            this.uuid = uuid;
                        }

                        public static class GridBeanX {
                            /**
                             * code : 611000
                             * createTime : 2017-09-22 09:26:03
                             * grid : {"code":"","createTime":"2017-09-22 09:26:03","grid":null,"gridType":1,"isDelete":0,"isEnable":0,"level":0,"name":"所有网格","sort":0,"updateTime":"2017-09-22 09:26:03","uuid":"e49ec56b71ff44428d5957d73cc451cb"}
                             * gridType : 1
                             * isDelete : 0
                             * isEnable : 0
                             * level : 1
                             * name : 商洛市
                             * sort : 0
                             * updateTime : 2017-09-25 15:24:20
                             * uuid : ed4bcf033ed742718044541021fcb569
                             */

                            private String code;
                            private String createTime;
                            private GridBean grid;
                            private int gridType;
                            private int isDelete;
                            private int isEnable;
                            private int level;
                            private String name;
                            private int sort;
                            private String updateTime;
                            private String uuid;

                            public String getCode() {
                                return code;
                            }

                            public void setCode(String code) {
                                this.code = code;
                            }

                            public String getCreateTime() {
                                return createTime;
                            }

                            public void setCreateTime(String createTime) {
                                this.createTime = createTime;
                            }

                            public GridBean getGrid() {
                                return grid;
                            }

                            public void setGrid(GridBean grid) {
                                this.grid = grid;
                            }

                            public int getGridType() {
                                return gridType;
                            }

                            public void setGridType(int gridType) {
                                this.gridType = gridType;
                            }

                            public int getIsDelete() {
                                return isDelete;
                            }

                            public void setIsDelete(int isDelete) {
                                this.isDelete = isDelete;
                            }

                            public int getIsEnable() {
                                return isEnable;
                            }

                            public void setIsEnable(int isEnable) {
                                this.isEnable = isEnable;
                            }

                            public int getLevel() {
                                return level;
                            }

                            public void setLevel(int level) {
                                this.level = level;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public int getSort() {
                                return sort;
                            }

                            public void setSort(int sort) {
                                this.sort = sort;
                            }

                            public String getUpdateTime() {
                                return updateTime;
                            }

                            public void setUpdateTime(String updateTime) {
                                this.updateTime = updateTime;
                            }

                            public String getUuid() {
                                return uuid;
                            }

                            public void setUuid(String uuid) {
                                this.uuid = uuid;
                            }

                            public static class GridBean {
                                /**
                                 * code :
                                 * createTime : 2017-09-22 09:26:03
                                 * grid : null
                                 * gridType : 1
                                 * isDelete : 0
                                 * isEnable : 0
                                 * level : 0
                                 * name : 所有网格
                                 * sort : 0
                                 * updateTime : 2017-09-22 09:26:03
                                 * uuid : e49ec56b71ff44428d5957d73cc451cb
                                 */

                                private String code;
                                private String createTime;
                                private Object grid;
                                private int gridType;
                                private int isDelete;
                                private int isEnable;
                                private int level;
                                private String name;
                                private int sort;
                                private String updateTime;
                                private String uuid;

                                public String getCode() {
                                    return code;
                                }

                                public void setCode(String code) {
                                    this.code = code;
                                }

                                public String getCreateTime() {
                                    return createTime;
                                }

                                public void setCreateTime(String createTime) {
                                    this.createTime = createTime;
                                }

                                public Object getGrid() {
                                    return grid;
                                }

                                public void setGrid(Object grid) {
                                    this.grid = grid;
                                }

                                public int getGridType() {
                                    return gridType;
                                }

                                public void setGridType(int gridType) {
                                    this.gridType = gridType;
                                }

                                public int getIsDelete() {
                                    return isDelete;
                                }

                                public void setIsDelete(int isDelete) {
                                    this.isDelete = isDelete;
                                }

                                public int getIsEnable() {
                                    return isEnable;
                                }

                                public void setIsEnable(int isEnable) {
                                    this.isEnable = isEnable;
                                }

                                public int getLevel() {
                                    return level;
                                }

                                public void setLevel(int level) {
                                    this.level = level;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public int getSort() {
                                    return sort;
                                }

                                public void setSort(int sort) {
                                    this.sort = sort;
                                }

                                public String getUpdateTime() {
                                    return updateTime;
                                }

                                public void setUpdateTime(String updateTime) {
                                    this.updateTime = updateTime;
                                }

                                public String getUuid() {
                                    return uuid;
                                }

                                public void setUuid(String uuid) {
                                    this.uuid = uuid;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
