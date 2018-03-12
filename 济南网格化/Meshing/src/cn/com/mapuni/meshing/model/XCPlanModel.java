package cn.com.mapuni.meshing.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 15225 on 2017/7/4.
 */

public class XCPlanModel {

    /**
     * status : 200
     * message :
     * rows : [{"offset":null,"pageSize":null,"orderByClause":null,"uuid":"F566CA5D-2A29-4975-A75B-000442CAEA01","enterpriseId":"00066094-1690-4684-9CB7-568D90F4972A","enterpriseType":1,"entname":"濞村骸宕＄粋鎹愬珝妞佹劙銈張澶愭閸忣剙寰�","fourthGridCode":null,"status":1,"planType":1,"describe":"閸栨ぞ鍚▎銏ｇ箣娴ｏ拷","createUser":"1231","createTime":1499071849000,"updateTime":1499071852000}]
     * content : null
     * total : 1
     */

    private String status;
    private String message;
    private Object content;
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

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
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

    public static class RowsBean implements Serializable{
        /**
         * offset : null
         * pageSize : null
         * orderByClause : null
         * uuid : F566CA5D-2A29-4975-A75B-000442CAEA01
         * enterpriseId : 00066094-1690-4684-9CB7-568D90F4972A
         * enterpriseType : 1
         * entname : 濞村骸宕＄粋鎹愬珝妞佹劙銈張澶愭閸忣剙寰�
         * fourthGridCode : null
         * status : 1
         * planType : 1
         * describe : 閸栨ぞ鍚▎銏ｇ箣娴ｏ拷
         * createUser : 1231
         * createTime : 1499071849000
         * updateTime : 1499071852000
         */

        private String offset;
        private String pageSize;
        private String orderByClause;
        private String uuid;
        private String enterpriseId;
        private String enterpriseType;
        private String entname;
        private String fourthGridCode;
        private int status;
        private int planType;
        private String describe;
        private String createUser;
        private long createTime;
        private long updateTime;

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getOrderByClause() {
            return orderByClause;
        }

        public void setOrderByClause(String orderByClause) {
            this.orderByClause = orderByClause;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getEnterpriseId() {
            return enterpriseId;
        }

        public void setEnterpriseId(String enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        public String getEnterpriseType() {
            return enterpriseType;
        }

        public void setEnterpriseType(String enterpriseType) {
            this.enterpriseType = enterpriseType;
        }

        public String getEntname() {
            return entname;
        }

        public void setEntname(String entname) {
            this.entname = entname;
        }

        public String getFourthGridCode() {
            return fourthGridCode;
        }

        public void setFourthGridCode(String fourthGridCode) {
            this.fourthGridCode = fourthGridCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getPlanType() {
            return planType;
        }

        public void setPlanType(int planType) {
            this.planType = planType;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
