package com.mapuni.shangluo.bean;

import java.util.List;

/**
 * Created by 15225 on 2017/8/19.
 */

public class ProblemType {

    /**
     * uuid : 1
     * name : 企事业单位和其他生产经营者
     * patrolProblemItemDict : [{"description":"禁止露天烧烤区域是否存在此类行为","name":"禁止露天烧烤区域是否存在此类行为","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4d772c8008f"},{"description":"存在小锅炉、小燃煤炉具各类低空燃烧设施","name":"存在小锅炉、小燃煤炉具各类低空燃烧设施","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4d882b60091"},{"description":"其他易产生扬尘的物料堆、渣土堆、废渣、建材等未采用防尘网或防尘布覆盖","name":"其他易产生扬尘的物料堆、渣土堆、废渣、建材等未采用防尘网或防尘布覆盖","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4da8f2a0093"},{"description":"存在医疗废(废注射器、带血纱布、人体组织)丢弃、遗撒","name":"存在医疗废丢弃、遗撒","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4dc46570095"},{"description":"有工业废渣(废液)倾倒","name":"有工业废渣(废液)倾倒","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4e173e50098"},{"description":"存在其他危险废物(如废油漆桶、废漆渣、含油废物、废有机溶剂、废试剂等)倾倒","name":"存在其他危险废物倾倒","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4e3515b009a"},{"description":"存在新增污染源或建设项目","name":"存在新增污染源或建设项目","patrolProblemDict":{"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"},"uuid":"4028f8fb5de49ad6015de4e3cd79009c"}]
     */

    private String uuid;
    private String name;
    private List<PatrolProblemItemDictBean> patrolProblemItemDict;

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

    public List<PatrolProblemItemDictBean> getPatrolProblemItemDict() {
        return patrolProblemItemDict;
    }

    public void setPatrolProblemItemDict(List<PatrolProblemItemDictBean> patrolProblemItemDict) {
        this.patrolProblemItemDict = patrolProblemItemDict;
    }

    public static class PatrolProblemItemDictBean {
        /**
         * description : 禁止露天烧烤区域是否存在此类行为
         * name : 禁止露天烧烤区域是否存在此类行为
         * patrolProblemDict : {"name":"其他问题","uuid":"4028f8fb5de36841015de4583cbb0032"}
         * uuid : 4028f8fb5de49ad6015de4d772c8008f
         */

        private String description;
        private String name;
        private PatrolProblemDictBean patrolProblemDict;
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

        public PatrolProblemDictBean getPatrolProblemDict() {
            return patrolProblemDict;
        }

        public void setPatrolProblemDict(PatrolProblemDictBean patrolProblemDict) {
            this.patrolProblemDict = patrolProblemDict;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public static class PatrolProblemDictBean {
            /**
             * name : 其他问题
             * uuid : 4028f8fb5de36841015de4583cbb0032
             */

            private String name;
            private String uuid;

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
    }
}
