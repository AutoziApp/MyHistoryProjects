package cn.com.mapuni.meshing.model;

import java.util.List;

/**
 * Created by suncm on 2017/6/23.
 */

public class ZuJiModel {

    /**
     * status : 200
     * message : 成功
     * rows : [{"id":"9A71D3D9-3F66-4344-9E31-0D037574BE72","signedRecodId":"C2E35258-D360-411F-8AAD-7D27A9CFAB9D","name":"朱彤","gridName":"五里沟街道平安里居委会","gridCode":"370104030201","address":"河南省郑州市金水区三全路与丰庆路交叉口东南角银河之星动漫城","entName":"济南槐荫老来海鲜酒店","phone":"87942743","createId":"93EEE91D-47EE-460C-A636-A555E6645041","city":"槐荫区","signedRecodImg":null,"createTime":1498197365000,"latitude":34830441,"longitude":113642381},{"id":"869BD5F8-D01C-40A0-99F9-B3ED79C87261","signedRecodId":"218242C9-4784-4C8B-BAF2-487A7C98F4D4","name":"朱彤","gridName":"五里沟街道平安里居委会","gridCode":"370104030201","address":"河南省郑州市金水区三全路与丰庆路交叉口东南角银河之星动漫城","entName":"山东省立医院（营养科病员餐厅）","phone":"87942743","createId":"93EEE91D-47EE-460C-A636-A555E6645041","city":"槐荫区","signedRecodImg":null,"createTime":1498196956000,"latitude":34830450,"longitude":113642382},{"id":"6715AE3E-ABE7-4F95-9B5D-223872671084","signedRecodId":"CD66FC0D-81DA-4754-9939-5CB4EE936002","name":"朱彤","gridName":"五里沟街道平安里居委会","gridCode":"370104030201","address":"河南省郑州市金水区三全路与丰庆路交叉口东南角银河之星动漫城","entName":"山东省立医院（营养科康源餐厅）","phone":"87942743","createId":"93EEE91D-47EE-460C-A636-A555E6645041","city":"槐荫区","signedRecodImg":null,"createTime":1498196847000,"latitude":34830447,"longitude":113642380},{"id":"413D1847-9F66-4851-86BA-EA02289A9515","signedRecodId":"753157B1-5E4E-4086-8737-6FE5EC441D3E","name":"朱彤","gridName":"五里沟街道平安里居委会","gridCode":"370104030201","address":"河南省郑州市金水区三全路与丰庆路交叉口东南角银河之星动漫城","entName":"山东省立医院（营养科病员餐厅）","phone":"87942743","createId":"93EEE91D-47EE-460C-A636-A555E6645041","city":"槐荫区","signedRecodImg":null,"createTime":1498196739000,"latitude":34830437,"longitude":113642382}]
     * content : null
     * total : 4
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

    public static class RowsBean {
        /**
         * id : 9A71D3D9-3F66-4344-9E31-0D037574BE72
         * signedRecodId : C2E35258-D360-411F-8AAD-7D27A9CFAB9D
         * name : 朱彤
         * gridName : 五里沟街道平安里居委会
         * gridCode : 370104030201
         * address : 河南省郑州市金水区三全路与丰庆路交叉口东南角银河之星动漫城
         * entName : 济南槐荫老来海鲜酒店
         * phone : 87942743
         * createId : 93EEE91D-47EE-460C-A636-A555E6645041
         * city : 槐荫区
         * signedRecodImg : null
         * createTime : 1498197365000
         * latitude : 34830441
         * longitude : 113642381
         */

        private String id;
        private String signedRecodId;
        private String name;
        private String gridName;
        private String gridCode;
        private String address;
        private String entName;
        private String phone;
        private String createId;
        private String city;
        private Object signedRecodImg;
        private long createTime;
        private String latitude;
        private String longitude;
        private String imgurl;
        
        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGridName() {
            return gridName;
        }

        public void setGridName(String gridName) {
            this.gridName = gridName;
        }

        public String getGridCode() {
            return gridCode;
        }

        public void setGridCode(String gridCode) {
            this.gridCode = gridCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEntName() {
            return entName;
        }

        public void setEntName(String entName) {
            this.entName = entName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Object getSignedRecodImg() {
            return signedRecodImg;
        }

        public void setSignedRecodImg(Object signedRecodImg) {
            this.signedRecodImg = signedRecodImg;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
