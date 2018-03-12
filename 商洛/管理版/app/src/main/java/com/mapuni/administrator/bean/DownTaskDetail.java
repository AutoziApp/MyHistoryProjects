package com.mapuni.administrator.bean;

/**
 * Created by 15225 on 2017/8/25.
 */

public class DownTaskDetail {


    /**
     * status : 0
     * msg : success
     * data : {"uuid":"4028f8055e78d56c015e78dce9bc0006","task":{"createTime":"2017-09-13 09:31:27","description":"测试1234","endTime":"2017-09-14 00:00:00","name":"测试123","startTime":"2017-09-13 00:00:00","taskType":{"description":"非紧急情况下的日常工作任务。","name":"普通任务","uuid":"4028b88e5dcee431015dcf0161bd0003"},"users":{"address":"","createTime":"1900-01-01 00:00:00","email":"","grid":{"code":"20170725","createTime":null,"grid":null,"gridType":1,"isDelete":0,"isEnable":null,"level":0,"name":"所有网格","sort":null,"updateTime":null,"uuid":"40be8fdded7fdcfea13dbsd46e9fe0a2c"},"isDelete":0,"isEnable":0,"loginName":"admin","password":"111111","sex":0,"sort":0,"telephone":"15023594529","updateTime":"2017-08-09 11:23:06","userRealname":"管理员-小雪","uuid":"40288fc25d77dbff015d77e107c20002"},"uuid":"4028f8055e78d56c015e78dcc4180005"},"taskType":"普通任务","gridName":"商洛市","startTime":"2017-09-13 00:00:00","endTime":"2017-09-14 00:00:00","status":1,"jurisdictionJudge":{"JudgeReport":0,"JudgeIssued":1,"JudgeTurn":1,"JudgeReturn":0,"JudgeComplete":1,"JudgeStop":0,"JudgeFeedback":0,"JudgeProcessing":0,"JudgeAudited":0,"JudgeDelayapply":1,"JudgeAgree":0,"JudgeDisagree":0,"JudgeAuditnotthrough":0},"judgeDetailsStatus":0,"issuedTaskStatus":0,"createTime":"2017-09-13 09:31:37"}
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
         * uuid : 4028f8055e78d56c015e78dce9bc0006
         * task : {"createTime":"2017-09-13 09:31:27","description":"测试1234","endTime":"2017-09-14 00:00:00","name":"测试123","startTime":"2017-09-13 00:00:00","taskType":{"description":"非紧急情况下的日常工作任务。","name":"普通任务","uuid":"4028b88e5dcee431015dcf0161bd0003"},"users":{"address":"","createTime":"1900-01-01 00:00:00","email":"","grid":{"code":"20170725","createTime":null,"grid":null,"gridType":1,"isDelete":0,"isEnable":null,"level":0,"name":"所有网格","sort":null,"updateTime":null,"uuid":"40be8fdded7fdcfea13dbsd46e9fe0a2c"},"isDelete":0,"isEnable":0,"loginName":"admin","password":"111111","sex":0,"sort":0,"telephone":"15023594529","updateTime":"2017-08-09 11:23:06","userRealname":"管理员-小雪","uuid":"40288fc25d77dbff015d77e107c20002"},"uuid":"4028f8055e78d56c015e78dcc4180005"}
         * taskType : 普通任务
         * gridName : 商洛市
         * startTime : 2017-09-13 00:00:00
         * endTime : 2017-09-14 00:00:00
         * status : 1
         * jurisdictionJudge : {"JudgeReport":0,"JudgeIssued":1,"JudgeTurn":1,"JudgeReturn":0,"JudgeComplete":1,"JudgeStop":0,"JudgeFeedback":0,"JudgeProcessing":0,"JudgeAudited":0,"JudgeDelayapply":1,"JudgeAgree":0,"JudgeDisagree":0,"JudgeAuditnotthrough":0}
         * judgeDetailsStatus : 0
         * issuedTaskStatus : 0
         * createTime : 2017-09-13 09:31:37
         */

        private String uuid;
        private TaskBean task;
        private String taskType;
        private String gridName;
        private String startTime;
        private String endTime;
        private int status;
        private JurisdictionJudgeBean jurisdictionJudge;
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

        public JurisdictionJudgeBean getJurisdictionJudge() {
            return jurisdictionJudge;
        }

        public void setJurisdictionJudge(JurisdictionJudgeBean jurisdictionJudge) {
            this.jurisdictionJudge = jurisdictionJudge;
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
             * createTime : 2017-09-13 09:31:27
             * description : 测试1234
             * endTime : 2017-09-14 00:00:00
             * name : 测试123
             * startTime : 2017-09-13 00:00:00
             * taskType : {"description":"非紧急情况下的日常工作任务。","name":"普通任务","uuid":"4028b88e5dcee431015dcf0161bd0003"}
             * users : {"address":"","createTime":"1900-01-01 00:00:00","email":"","grid":{"code":"20170725","createTime":null,"grid":null,"gridType":1,"isDelete":0,"isEnable":null,"level":0,"name":"所有网格","sort":null,"updateTime":null,"uuid":"40be8fdded7fdcfea13dbsd46e9fe0a2c"},"isDelete":0,"isEnable":0,"loginName":"admin","password":"111111","sex":0,"sort":0,"telephone":"15023594529","updateTime":"2017-08-09 11:23:06","userRealname":"管理员-小雪","uuid":"40288fc25d77dbff015d77e107c20002"}
             * uuid : 4028f8055e78d56c015e78dcc4180005
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
                 * createTime : 1900-01-01 00:00:00
                 * email :
                 * grid : {"code":"20170725","createTime":null,"grid":null,"gridType":1,"isDelete":0,"isEnable":null,"level":0,"name":"所有网格","sort":null,"updateTime":null,"uuid":"40be8fdded7fdcfea13dbsd46e9fe0a2c"}
                 * isDelete : 0
                 * isEnable : 0
                 * loginName : admin
                 * password : 111111
                 * sex : 0
                 * sort : 0
                 * telephone : 15023594529
                 * updateTime : 2017-08-09 11:23:06
                 * userRealname : 管理员-小雪
                 * uuid : 40288fc25d77dbff015d77e107c20002
                 */

                private String address;
                private String createTime;
                private String email;
                private GridBean grid;
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

                public GridBean getGrid() {
                    return grid;
                }

                public void setGrid(GridBean grid) {
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

                public static class GridBean {
                    /**
                     * code : 20170725
                     * createTime : null
                     * grid : null
                     * gridType : 1
                     * isDelete : 0
                     * isEnable : null
                     * level : 0
                     * name : 所有网格
                     * sort : null
                     * updateTime : null
                     * uuid : 40be8fdded7fdcfea13dbsd46e9fe0a2c
                     */

                    private String code;
                    private Object createTime;
                    private Object grid;
                    private int gridType;
                    private int isDelete;
                    private Object isEnable;
                    private int level;
                    private String name;
                    private Object sort;
                    private Object updateTime;
                    private String uuid;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public Object getCreateTime() {
                        return createTime;
                    }

                    public void setCreateTime(Object createTime) {
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

                    public Object getIsEnable() {
                        return isEnable;
                    }

                    public void setIsEnable(Object isEnable) {
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

                    public Object getSort() {
                        return sort;
                    }

                    public void setSort(Object sort) {
                        this.sort = sort;
                    }

                    public Object getUpdateTime() {
                        return updateTime;
                    }

                    public void setUpdateTime(Object updateTime) {
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

        public static class JurisdictionJudgeBean {
            /**
             * JudgeReport : 0
             * JudgeIssued : 1
             * JudgeTurn : 1
             * JudgeReturn : 0
             * JudgeComplete : 1
             * JudgeStop : 0
             * JudgeFeedback : 0
             * JudgeProcessing : 0
             * JudgeAudited : 0
             * JudgeDelayapply : 1
             * JudgeAgree : 0
             * JudgeDisagree : 0
             * JudgeAuditnotthrough : 0
             * JudgeDistribution:1
             */

            private int JudgeReport;
            private int JudgeIssued;
            private int JudgeTurn;
            private int JudgeReturn;
            private int JudgeComplete;
            private int JudgeStop;
            private int JudgeFeedback;
            private int JudgeProcessing;
            private int JudgeAudited;
            private int JudgeDelayapply;
            private int JudgeAgree;
            private int JudgeDisagree;
            private int JudgeAuditnotthrough;
            private int JudgeDistribution;

            public int getJudgeDistribution() {
                return JudgeDistribution;
            }

            public void setJudgeDistribution(int judgeDistribution) {
                JudgeDistribution = judgeDistribution;
            }

            public int getJudgeReport() {
                return JudgeReport;
            }

            public void setJudgeReport(int JudgeReport) {
                this.JudgeReport = JudgeReport;
            }

            public int getJudgeIssued() {
                return JudgeIssued;
            }

            public void setJudgeIssued(int JudgeIssued) {
                this.JudgeIssued = JudgeIssued;
            }

            public int getJudgeTurn() {
                return JudgeTurn;
            }

            public void setJudgeTurn(int JudgeTurn) {
                this.JudgeTurn = JudgeTurn;
            }

            public int getJudgeReturn() {
                return JudgeReturn;
            }

            public void setJudgeReturn(int JudgeReturn) {
                this.JudgeReturn = JudgeReturn;
            }

            public int getJudgeComplete() {
                return JudgeComplete;
            }

            public void setJudgeComplete(int JudgeComplete) {
                this.JudgeComplete = JudgeComplete;
            }

            public int getJudgeStop() {
                return JudgeStop;
            }

            public void setJudgeStop(int JudgeStop) {
                this.JudgeStop = JudgeStop;
            }

            public int getJudgeFeedback() {
                return JudgeFeedback;
            }

            public void setJudgeFeedback(int JudgeFeedback) {
                this.JudgeFeedback = JudgeFeedback;
            }

            public int getJudgeProcessing() {
                return JudgeProcessing;
            }

            public void setJudgeProcessing(int JudgeProcessing) {
                this.JudgeProcessing = JudgeProcessing;
            }

            public int getJudgeAudited() {
                return JudgeAudited;
            }

            public void setJudgeAudited(int JudgeAudited) {
                this.JudgeAudited = JudgeAudited;
            }

            public int getJudgeDelayapply() {
                return JudgeDelayapply;
            }

            public void setJudgeDelayapply(int JudgeDelayapply) {
                this.JudgeDelayapply = JudgeDelayapply;
            }

            public int getJudgeAgree() {
                return JudgeAgree;
            }

            public void setJudgeAgree(int JudgeAgree) {
                this.JudgeAgree = JudgeAgree;
            }

            public int getJudgeDisagree() {
                return JudgeDisagree;
            }

            public void setJudgeDisagree(int JudgeDisagree) {
                this.JudgeDisagree = JudgeDisagree;
            }

            public int getJudgeAuditnotthrough() {
                return JudgeAuditnotthrough;
            }

            public void setJudgeAuditnotthrough(int JudgeAuditnotthrough) {
                this.JudgeAuditnotthrough = JudgeAuditnotthrough;
            }
        }
    }
}
