package com.mapuni.shangluo.bean;

/**
 * Created by 15225 on 2017/8/23.
 */

public class UploadTaskDetail {


    /**
     * status : 0
     * msg : success
     * data : {"uuid":"4028f8105e792c51015e792f8f980003","supervisionObjectName":"玖龙橡胶厂","supervisionObjectTypeName":"工业污染","gridName":"窑头社区居委会","userRealname":"李颜丽","number":"2017091300001","telephone":"185696956256","patrolProblemType":"企事业单位和其他生产经营者","description":"我是司机，我要上路","longitude":"113.563187","latitude":"34.824025","jurisdictionJudge":{"JudgeReport":1,"JudgeIssued":0,"JudgeTurn":1,"JudgeReturn":1,"JudgeComplete":1,"JudgeStop":0,"JudgeFeedback":0,"JudgeProcessing":0,"JudgeAudited":0,"JudgeDelayapply":0,"JudgeAgree":0,"JudgeDisagree":0,"JudgeAuditnotthrough":0},"status":1,"theGrid":"商州区","createTime":"2017-09-13 11:01:53"}
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
         * uuid : 4028f8105e792c51015e792f8f980003
         * supervisionObjectName : 玖龙橡胶厂
         * supervisionObjectTypeName : 工业污染
         * gridName : 窑头社区居委会
         * userRealname : 李颜丽
         * number : 2017091300001
         * telephone : 185696956256
         * patrolProblemType : 企事业单位和其他生产经营者
         * description : 我是司机，我要上路
         * longitude : 113.563187
         * latitude : 34.824025
         * jurisdictionJudge : {"JudgeReport":1,"JudgeIssued":0,"JudgeTurn":1,"JudgeReturn":1,"JudgeComplete":1,"JudgeStop":0,"JudgeFeedback":0,"JudgeProcessing":0,"JudgeAudited":0,"JudgeDelayapply":0,"JudgeAgree":0,"JudgeDisagree":0,"JudgeAuditnotthrough":0}
         * status : 1
         * theGrid : 商州区
         * createTime : 2017-09-13 11:01:53
         */

        private String uuid;
        private String supervisionObjectName;
        private String supervisionObjectTypeName;
        private String gridName;
        private String userRealname;
        private String number;
        private String telephone;
        private String patrolProblemType;
        private String description;
        private String longitude;
        private String latitude;
        private JurisdictionJudgeBean jurisdictionJudge;
        private int status;
        private String theGrid;
        private String createTime;
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getSupervisionObjectName() {
            return supervisionObjectName;
        }

        public void setSupervisionObjectName(String supervisionObjectName) {
            this.supervisionObjectName = supervisionObjectName;
        }

        public String getSupervisionObjectTypeName() {
            return supervisionObjectTypeName;
        }

        public void setSupervisionObjectTypeName(String supervisionObjectTypeName) {
            this.supervisionObjectTypeName = supervisionObjectTypeName;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public String getUserRealname() {
            return userRealname;
        }

        public void setUserRealname(String userRealname) {
            this.userRealname = userRealname;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getPatrolProblemType() {
            return patrolProblemType;
        }

        public void setPatrolProblemType(String patrolProblemType) {
            this.patrolProblemType = patrolProblemType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public JurisdictionJudgeBean getJurisdictionJudge() {
            return jurisdictionJudge;
        }

        public void setJurisdictionJudge(JurisdictionJudgeBean jurisdictionJudge) {
            this.jurisdictionJudge = jurisdictionJudge;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTheGrid() {
            return theGrid;
        }

        public void setTheGrid(String theGrid) {
            this.theGrid = theGrid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public static class JurisdictionJudgeBean {
            /**
             * JudgeReport : 1
             * JudgeIssued : 0
             * JudgeTurn : 1
             * JudgeReturn : 1
             * JudgeComplete : 1
             * JudgeStop : 0
             * JudgeFeedback : 0
             * JudgeProcessing : 0
             * JudgeAudited : 0
             * JudgeDelayapply : 0
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
