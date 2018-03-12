package cn.com.mapuni.meshing.model;

import java.util.List;

/**
 * Created by suncm on 2017/6/30.
 */

public class ImageModel {

    /**
     * status : 200
     * message : ³É¹¦
     * rows : [{"id":"207402EE-3926-4470-A13A-0A1768AD65F6","signedRecodId":"4D3B9854-A021-426F-9C9A-F161021B9EDA","imgurl":"/2017/6/1498798035936.jpg","createId":"93EEE91D-47EE-460C-A636-A555E6645041","createDate":null},{"id":"582F2F3B-21A8-4A07-BAFD-4DEAA03809F0","signedRecodId":"4D3B9854-A021-426F-9C9A-F161021B9EDA","imgurl":"/2017/6/1498798035936.jpg","createId":"93EEE91D-47EE-460C-A636-A555E6645041","createDate":null},{"id":"6AD6A0C8-A6C3-4CC5-B146-794826EA9582","signedRecodId":"A203AA2E-427D-4678-8193-43AB978A5BBA","imgurl":"/2017/6/1498790152141.jpg","createId":"93EEE91D-47EE-460C-A636-A555E6645041","createDate":null},{"id":"8848E0E8-0679-4333-91EA-EDA3C71121BA","signedRecodId":"0343ACB5-2C1F-45D7-8B86-7EC0EF7371C0","imgurl":"/2017/6/1498793558346.jpg","createId":"93EEE91D-47EE-460C-A636-A555E6645041","createDate":null},{"id":"9F89CA54-4CD4-49B5-A7FB-395C6C2915CC","signedRecodId":"CE0675C6-8511-46FF-83FC-DCEB3647F2B0","imgurl":"/2017/6/1498798017939.jpg","createId":"93EEE91D-47EE-460C-A636-A555E6645041","createDate":null},{"id":"D1FD1EB0-FD63-4215-9D88-4154B8458EB8","signedRecodId":"0343ACB5-2C1F-45D7-8B86-7EC0EF7371C0","imgurl":"/2017/6/1498793558345.jpg","createId":"93EEE91D-47EE-460C-A636-A555E6645041","createDate":null}]
     * content : null
     * total : 0
     */

    private String status;
    private String message;
    private Object content;
    private int total;
    private List<ImaesBean> rows;

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

    public List<ImaesBean> getRows() {
        return rows;
    }

    public void setRows(List<ImaesBean> rows) {
        this.rows = rows;
    }

    public static class ImaesBean {
        /**
         * id : 207402EE-3926-4470-A13A-0A1768AD65F6
         * signedRecodId : 4D3B9854-A021-426F-9C9A-F161021B9EDA
         * imgurl : /2017/6/1498798035936.jpg
         * createId : 93EEE91D-47EE-460C-A636-A555E6645041
         * createDate : null
         */

        private String id;
        private String signedRecodId;
        private String imgurl;
        private String createId;
        private Object createDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSignedRecodId() {
            return signedRecodId;
        }

        public void setSignedRecodId(String signedRecodId) {
            this.signedRecodId = signedRecodId;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public Object getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Object createDate) {
            this.createDate = createDate;
        }
    }
}
