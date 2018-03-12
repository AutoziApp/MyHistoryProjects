package com.mapuni.mobileenvironment.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yawei on 2016/12/13.
 */
public class EntModel{

    /**
     * RegionCode : 110114000
     * RegionName : 昌平区
     * Count : 29
     * Enterprise : [{"EntCode":130300002,"EntName":"北京水泥厂责任有限公司"},{"EntCode":130300003,"EntName":"天通苑西区锅炉房（1-4号炉）"},{"EntCode":130300004,"EntName":"天通苑北区锅炉房（1-6号炉）"},{"EntCode":130300005,"EntName":"北京京能未来燃气热电有限公司"},{"EntCode":130300006,"EntName":"北京华都肉鸡公司"},{"EntCode":130300007,"EntName":"北京南口轨道交通机械有限责任公司"},{"EntCode":130300008,"EntName":"北京五星青岛啤酒有限公司"},{"EntCode":130300009,"EntName":"北京顺天通物业管理有限公司"},{"EntCode":130300010,"EntName":"北京东光物业管理有限公司"},{"EntCode":130300011,"EntName":"北京市第五肉类联合加工厂"},{"EntCode":130300012,"EntName":"北京市昌平污水处理中心"},{"EntCode":130300013,"EntName":"北京市昌平区水务局北七家地区污水处理中心（小汤山污水厂）"},{"EntCode":130300014,"EntName":"北京市昌平区水务局未来科技城再生水处理中心"},{"EntCode":130300015,"EntName":"北京市昌平区供暖服务管理处（水库路）"},{"EntCode":130300016,"EntName":"北京市昌平区水务局南口地区污水处理中心"},{"EntCode":130300017,"EntName":"北京首冶新元科技发展有限公司"},{"EntCode":130300018,"EntName":"北汽福田欧辉客车"},{"EntCode":130300019,"EntName":"北京吉利大学污水处理站"},{"EntCode":130300020,"EntName":"北京永丰国际信息园开发有限责任公司"},{"EntCode":130300021,"EntName":"太申祥和山庄"},{"EntCode":130300022,"EntName":"北京阳昊科技发展有限公司"},{"EntCode":130300023,"EntName":"北京金隅凤山温泉度假村"},{"EntCode":130300024,"EntName":"北京东联哈尔仪器制造有限公司"},{"EntCode":130300025,"EntName":"中关村生命科技园2座"},{"EntCode":130300026,"EntName":"北京稻香村"},{"EntCode":130300027,"EntName":"希杰（中国）食品"},{"EntCode":130300028,"EntName":"碧水庄园"},{"EntCode":130300029,"EntName":"龙城花园"},{"EntCode":130300030,"EntName":"北京福田发动机"}]
     */

    private int RegionCode;
    private String RegionName;
    private int Count;
    private List<EnterpriseBean> Enterprise;

    public int getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(int RegionCode) {
        this.RegionCode = RegionCode;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String RegionName) {
        this.RegionName = RegionName;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public List<EnterpriseBean> getEnterprise() {
        return Enterprise;
    }

    public void setEnterprise(List<EnterpriseBean> Enterprise) {
        this.Enterprise = Enterprise;
    }

    public static class EnterpriseBean implements Serializable{
        /**
         * EntCode : 130300002
         * EntName : 北京水泥厂责任有限公司
         */

        private int EntCode;
        private String EntName;

        public int getEntCode() {
            return EntCode;
        }

        public void setEntCode(int EntCode) {
            this.EntCode = EntCode;
        }

        public String getEntName() {
            return EntName;
        }

        public void setEntName(String EntName) {
            this.EntName = EntName;
        }
    }
}
