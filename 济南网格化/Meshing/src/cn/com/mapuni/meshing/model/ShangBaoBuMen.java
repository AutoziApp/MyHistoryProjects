package cn.com.mapuni.meshing.model;

import java.util.List;

public class ShangBaoBuMen {
	 /**
     * status : 200
     * message :
     * rows : [{"code":"37010403","name":"槐荫区","parentcode":"370100","childs":[{"code":"3701040307","name":"营市街","parentcode":"37010403","childs":null},{"code":"3701040304","name":"南辛","parentcode":"37010403","childs":null},{"code":"3701040308","name":"西市场","parentcode":"37010403","childs":null},{"code":"3701040305","name":"中大","parentcode":"37010403","childs":null},{"code":"3701040306","name":"道德街","parentcode":"37010403","childs":null},{"code":"3701040301","name":"振兴街","parentcode":"37010403","childs":null},{"code":"3701040302","name":"五里沟","parentcode":"37010403","childs":null},{"code":"3701040303","name":"青年公园","parentcode":"37010403","childs":null},{"code":"3701040309","name":"张庄路","parentcode":"37010403","childs":null},{"code":"3701040311","name":"匡山","parentcode":"37010403","childs":null},{"code":"3701040310","name":"段北","parentcode":"37010403","childs":null},{"code":"3701040316","name":"吴家堡","parentcode":"37010403","childs":null},{"code":"3701040312","name":"美里湖","parentcode":"37010403","childs":null},{"code":"3701040313","name":"兴福","parentcode":"37010403","childs":null},{"code":"3701040314","name":"玉清湖","parentcode":"37010403","childs":null},{"code":"3701040315","name":"腊山","parentcode":"37010403","childs":null}]}]
     * content : null
     * total : 0
     */

    private String status;
    private String message;
    private String content;
    private String total;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
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
         * code : 37010403
         * name : 槐荫区
         * parentcode : 370100
         * childs : [{"code":"3701040307","name":"营市街","parentcode":"37010403","childs":null},{"code":"3701040304","name":"南辛","parentcode":"37010403","childs":null},{"code":"3701040308","name":"西市场","parentcode":"37010403","childs":null},{"code":"3701040305","name":"中大","parentcode":"37010403","childs":null},{"code":"3701040306","name":"道德街","parentcode":"37010403","childs":null},{"code":"3701040301","name":"振兴街","parentcode":"37010403","childs":null},{"code":"3701040302","name":"五里沟","parentcode":"37010403","childs":null},{"code":"3701040303","name":"青年公园","parentcode":"37010403","childs":null},{"code":"3701040309","name":"张庄路","parentcode":"37010403","childs":null},{"code":"3701040311","name":"匡山","parentcode":"37010403","childs":null},{"code":"3701040310","name":"段北","parentcode":"37010403","childs":null},{"code":"3701040316","name":"吴家堡","parentcode":"37010403","childs":null},{"code":"3701040312","name":"美里湖","parentcode":"37010403","childs":null},{"code":"3701040313","name":"兴福","parentcode":"37010403","childs":null},{"code":"3701040314","name":"玉清湖","parentcode":"37010403","childs":null},{"code":"3701040315","name":"腊山","parentcode":"37010403","childs":null}]
         */

        private String code;
        private String name;
        private String parentcode;
        private List<ChildsBean> childs;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParentcode() {
            return parentcode;
        }

        public void setParentcode(String parentcode) {
            this.parentcode = parentcode;
        }

        public List<ChildsBean> getChilds() {
            return childs;
        }

        public void setChilds(List<ChildsBean> childs) {
            this.childs = childs;
        }

        public static class ChildsBean {
            /**
             * code : 3701040307
             * name : 营市街
             * parentcode : 37010403
             * childs : null
             */

            private String code;
            private String name;
            private String parentcode;
            private String childs;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getParentcode() {
                return parentcode;
            }

            public void setParentcode(String parentcode) {
                this.parentcode = parentcode;
            }

            public String getChilds() {
                return childs;
            }

            public void setChilds(String childs) {
                this.childs = childs;
            }
        }
    }
}
