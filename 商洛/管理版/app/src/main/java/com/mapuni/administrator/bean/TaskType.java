package com.mapuni.administrator.bean;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/19 17:16
 * @change
 * @chang time
 * @class describe
 */

public class TaskType {


    /**
     * total : 8
     * rows : [{"uuid":"1","name":"企事业单位和其他生产经营者","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de4553e930022","name":"施工工地","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de455a4310025","name":"各类道路","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de45608b30027","name":"散乱污企业","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de456864b0029","name":"公共水体","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de457ba1a002e","name":"畜牧养殖单位","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de4580daa0030","name":"秸秆垃圾焚烧","patrolProblemItemDict":null},{"uuid":"4028f8fb5de36841015de4583cbb0032","name":"其他问题","patrolProblemItemDict":null}]
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
         * uuid : 1
         * name : 企事业单位和其他生产经营者
         * patrolProblemItemDict : null
         */

        private String uuid;
        private String name;
        private Object patrolProblemItemDict;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPatrolProblemItemDict() {
            return patrolProblemItemDict;
        }

        public void setPatrolProblemItemDict(Object patrolProblemItemDict) {
            this.patrolProblemItemDict = patrolProblemItemDict;
        }
    }
}
