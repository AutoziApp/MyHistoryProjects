package cn.com.mapuni.meshing.model;

import java.util.List;

public class Xcxq {

    /**
     * status : 200
     * message : 成功
     * rows : null
     * content : {"id":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","taskId":null,"taskName":"临时巡查","patrolObjectId":null,"patrolObjectName":"无","isHaveProblem":"1","problemDesc":"测试污染问题","x":116.934076,"y":36.657047,"createGrid":"2000001","createGridName":"某某街道办事处","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createUserName":"徐群","createTime":"2017-03-11 10:13:19","problems":[{"id":"D0007310-ADF3-4BDF-B4D4-52886F72DF3F","patrolId":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","problemCode":"daolu04","problemName":null}],"problemImgs":[{"id":"FCA426DB-A1BC-48E5-85E6-D674F9861948","patrolId":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","imgPath":"img_path","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createTime":"2017-03-11 10:13:21"}],"records":[{"id":"7882F08D-38FB-4C44-B4CF-C44F73F39DCE","patrolId":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","recordOrder":1,"opinion":"测试处理意见","status":"6","x":116.934076,"y":36.657047,"handlerGrid":"2000001","handlerGridName":"某某街道办事处","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createUserName":"徐群","createTime":"2017-03-11 10:14:22","imgs":[{"id":"0424B3AC-F046-4966-8596-2409455BC19A","patrolRecordId":"7882F08D-38FB-4C44-B4CF-C44F73F39DCE","imgPath":"imgPath","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createTime":"2017-03-11 10:15:14"}]}]}
     * total : 0
     */

    private String status;
    private String message;
    private Object rows;
    private ContentBean content;
    private int total;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class ContentBean {
        /**
         * id : FBF4C61A-B89B-46D3-BE6A-05286E7088ED
         * taskId : null
         * taskName : 临时巡查
         * patrolObjectId : null
         * patrolObjectName : 无
         * isHaveProblem : 1
         * problemDesc : 测试污染问题
         * x : 116.934076
         * y : 36.657047
         * createGrid : 2000001
         * createGridName : 某某街道办事处
         * createId : 7efb369b-90f0-42c9-9724-99f47f3903b8
         * createUserName : 徐群
         * createTime : 2017-03-11 10:13:19
         * problems : [{"id":"D0007310-ADF3-4BDF-B4D4-52886F72DF3F","patrolId":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","problemCode":"daolu04","problemName":null}]
         * problemImgs : [{"id":"FCA426DB-A1BC-48E5-85E6-D674F9861948","patrolId":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","imgPath":"img_path","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createTime":"2017-03-11 10:13:21"}]
         * records : [{"id":"7882F08D-38FB-4C44-B4CF-C44F73F39DCE","patrolId":"FBF4C61A-B89B-46D3-BE6A-05286E7088ED","recordOrder":1,"opinion":"测试处理意见","status":"6","x":116.934076,"y":36.657047,"handlerGrid":"2000001","handlerGridName":"某某街道办事处","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createUserName":"徐群","createTime":"2017-03-11 10:14:22","imgs":[{"id":"0424B3AC-F046-4966-8596-2409455BC19A","patrolRecordId":"7882F08D-38FB-4C44-B4CF-C44F73F39DCE","imgPath":"imgPath","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createTime":"2017-03-11 10:15:14"}]}]
         */

        private String id;
        private Object taskId;
        private String taskName;
        private Object patrolObjectId;
        private String patrolObjectName;
        private String isHaveProblem;
        private String problemDesc;
        private double x;
        private double y;
        private String createGrid;
        private String createGridName;
        private String createId;
        private String createUserName;
        private String address;
        private String createTime;
        private List<ProblemsBean> problems;
        private List<ProblemImgsBean> problemImgs;
        private List<RecordsBean> records;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getTaskId() {
            return taskId;
        }

        public void setTaskId(Object taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public Object getPatrolObjectId() {
            return patrolObjectId;
        }

        public void setPatrolObjectId(Object patrolObjectId) {
            this.patrolObjectId = patrolObjectId;
        }

        public String getPatrolObjectName() {
            return patrolObjectName;
        }

        public void setPatrolObjectName(String patrolObjectName) {
            this.patrolObjectName = patrolObjectName;
        }

        public String getIsHaveProblem() {
            return isHaveProblem;
        }

        public void setIsHaveProblem(String isHaveProblem) {
            this.isHaveProblem = isHaveProblem;
        }

        public String getProblemDesc() {
            return problemDesc;
        }

        public void setProblemDesc(String problemDesc) {
            this.problemDesc = problemDesc;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getCreateGrid() {
            return createGrid;
        }

        public void setCreateGrid(String createGrid) {
            this.createGrid = createGrid;
        }

        public String getCreateGridName() {
            return createGridName;
        }

        public void setCreateGridName(String createGridName) {
            this.createGridName = createGridName;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<ProblemsBean> getProblems() {
            return problems;
        }

        public void setProblems(List<ProblemsBean> problems) {
            this.problems = problems;
        }

        public List<ProblemImgsBean> getProblemImgs() {
            return problemImgs;
        }

        public void setProblemImgs(List<ProblemImgsBean> problemImgs) {
            this.problemImgs = problemImgs;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class ProblemsBean {
            /**
             * id : D0007310-ADF3-4BDF-B4D4-52886F72DF3F
             * patrolId : FBF4C61A-B89B-46D3-BE6A-05286E7088ED
             * problemCode : daolu04
             * problemName : null
             */

            private String id;
            private String patrolId;
            private String problemCode;
            private Object problemName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPatrolId() {
                return patrolId;
            }

            public void setPatrolId(String patrolId) {
                this.patrolId = patrolId;
            }

            public String getProblemCode() {
                return problemCode;
            }

            public void setProblemCode(String problemCode) {
                this.problemCode = problemCode;
            }

            public Object getProblemName() {
                return problemName;
            }

            public void setProblemName(Object problemName) {
                this.problemName = problemName;
            }
        }

        public static class ProblemImgsBean {
            /**
             * id : FCA426DB-A1BC-48E5-85E6-D674F9861948
             * patrolId : FBF4C61A-B89B-46D3-BE6A-05286E7088ED
             * imgPath : img_path
             * createId : 7efb369b-90f0-42c9-9724-99f47f3903b8
             * createTime : 2017-03-11 10:13:21
             */

            private String id;
            private String patrolId;
            private String imgPath;
            private String createId;
            private String createTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPatrolId() {
                return patrolId;
            }

            public void setPatrolId(String patrolId) {
                this.patrolId = patrolId;
            }

            public String getImgPath() {
                return imgPath;
            }

            public void setImgPath(String imgPath) {
                this.imgPath = imgPath;
            }

            public String getCreateId() {
                return createId;
            }

            public void setCreateId(String createId) {
                this.createId = createId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }

        public static class RecordsBean {
            /**
             * id : 7882F08D-38FB-4C44-B4CF-C44F73F39DCE
             * patrolId : FBF4C61A-B89B-46D3-BE6A-05286E7088ED
             * recordOrder : 1
             * opinion : 测试处理意见
             * status : 6
             * x : 116.934076
             * y : 36.657047
             * handlerGrid : 2000001
             * handlerGridName : 某某街道办事处
             * createId : 7efb369b-90f0-42c9-9724-99f47f3903b8
             * createUserName : 徐群
             * createTime : 2017-03-11 10:14:22
             * imgs : [{"id":"0424B3AC-F046-4966-8596-2409455BC19A","patrolRecordId":"7882F08D-38FB-4C44-B4CF-C44F73F39DCE","imgPath":"imgPath","createId":"7efb369b-90f0-42c9-9724-99f47f3903b8","createTime":"2017-03-11 10:15:14"}]
             */

            private String id;
            private String patrolId;
            private int recordOrder;
            private String opinion;
            private String status;
            private double x;
            private double y;
            private String handlerGrid;
            private String handlerGridName;
            private String createId;
            private String createUserName;
            private String createTime;
            private String judgeTura;
            private List<ImgsBean> imgs;

            public String getJudgeTura() {
        		return judgeTura;
        	}

        	public void setJudgeTura(String judgeTura) {
        		this.judgeTura = judgeTura;
        	}
            
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPatrolId() {
                return patrolId;
            }

            public void setPatrolId(String patrolId) {
                this.patrolId = patrolId;
            }

            public int getRecordOrder() {
                return recordOrder;
            }

            public void setRecordOrder(int recordOrder) {
                this.recordOrder = recordOrder;
            }

            public String getOpinion() {
                return opinion;
            }

            public void setOpinion(String opinion) {
                this.opinion = opinion;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }

            public String getHandlerGrid() {
                return handlerGrid;
            }

            public void setHandlerGrid(String handlerGrid) {
                this.handlerGrid = handlerGrid;
            }

            public String getHandlerGridName() {
                return handlerGridName;
            }

            public void setHandlerGridName(String handlerGridName) {
                this.handlerGridName = handlerGridName;
            }

            public String getCreateId() {
                return createId;
            }

            public void setCreateId(String createId) {
                this.createId = createId;
            }

            public String getCreateUserName() {
                return createUserName;
            }

            public void setCreateUserName(String createUserName) {
                this.createUserName = createUserName;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public List<ImgsBean> getImgs() {
                return imgs;
            }

            public void setImgs(List<ImgsBean> imgs) {
                this.imgs = imgs;
            }

            public static class ImgsBean {
                /**
                 * id : 0424B3AC-F046-4966-8596-2409455BC19A
                 * patrolRecordId : 7882F08D-38FB-4C44-B4CF-C44F73F39DCE
                 * imgPath : imgPath
                 * createId : 7efb369b-90f0-42c9-9724-99f47f3903b8
                 * createTime : 2017-03-11 10:15:14
                 */

                private String id;
                private String patrolRecordId;
                private String imgPath;
                private String createId;
                private String createTime;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getPatrolRecordId() {
                    return patrolRecordId;
                }

                public void setPatrolRecordId(String patrolRecordId) {
                    this.patrolRecordId = patrolRecordId;
                }

                public String getImgPath() {
                    return imgPath;
                }

                public void setImgPath(String imgPath) {
                    this.imgPath = imgPath;
                }

                public String getCreateId() {
                    return createId;
                }

                public void setCreateId(String createId) {
                    this.createId = createId;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }
            }
        }

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
    }
}
