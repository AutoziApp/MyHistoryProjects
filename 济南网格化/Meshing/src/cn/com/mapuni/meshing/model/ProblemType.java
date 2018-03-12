package cn.com.mapuni.meshing.model;

import java.util.List;

public class ProblemType {
	  /**
     * status : 200
     * message : 成功
     * rows : [{"id":"26EF19FA-5CF3-4249-9EAB-CF5DBA0A6509","paramCode":"other","itemCode":"other01","itemName":"其他问题","itemOrder":1,"remark":"其他问题巡查问题内容","status":"1","createId":"NULL","createTime":1489204800000,"childs":null}]
     * content : null
     * total : 0
     */

    private String status;
    private String message;
    private String content;
    private int total;
    private List<RowsBean> rows;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
         * id : 26EF19FA-5CF3-4249-9EAB-CF5DBA0A6509
         * paramCode : other
         * itemCode : other01
         * itemName : 其他问题
         * itemOrder : 1
         * remark : 其他问题巡查问题内容
         * status : 1
         * createId : NULL
         * createTime : 1489204800000
         * childs : null
         */

        private String id;
        private String paramCode;
        private String itemCode;
        private String itemName;
        private String itemOrder;
        private String remark;
        private String status;
        private String createId;
        private String createTime;
        private String childs;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParamCode() {
            return paramCode;
        }

        public void setParamCode(String paramCode) {
            this.paramCode = paramCode;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemOrder() {
            return itemOrder;
        }

        public void setItemOrder(String itemOrder) {
            this.itemOrder = itemOrder;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getChilds() {
            return childs;
        }

        public void setChilds(String childs) {
            this.childs = childs;
        }
    }
}
