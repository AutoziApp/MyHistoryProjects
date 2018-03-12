package com.mapuni.administrator.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.bean
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/8 17:20
 * @change
 * @chang time
 * @class describe
 */

public class MyCityBean implements IPickerViewData {


    /**
     * name : 商洛市
     * city : [{"name":"商洛市","area":["商州区","辖县","洛南县","丹凤县","商南县","山阳县","镇安县","柞水县"]}]
     */

    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public static class CityBean {
        /**
         * name : 商洛市
         * area : ["商州区","辖县","洛南县","丹凤县","商南县","山阳县","镇安县","柞水县"]
         */

        private String name;
        private List<String> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getArea() {
            return area;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }
    }
}
