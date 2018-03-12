package cn.com.mapuni.chart.meshingtotal.model;

/**
 * Created by YZP on 2017/7/23.
 */
public class DeptDetailItemBean {
    private String name;
    private String context;

    public DeptDetailItemBean(String name, String context) {
        this.name = name;
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "DeptDetailItemBean{" +
                "name='" + name + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
