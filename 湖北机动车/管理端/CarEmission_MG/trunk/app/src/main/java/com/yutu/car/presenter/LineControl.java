package com.yutu.car.presenter;

import com.yutu.car.bean.AllLineBean;
import com.yutu.car.bean.AllStationBean;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by yawei on 2017/4/24.
 */

public class LineControl extends ExpandaListControl {
    private AllLineBean bean;
    private NetControl netControl;
    @Override
    public String getTitle() {
        return "检测监控";
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setTag(Object obj) {

    }

    @Override
    public int jsonToBean(String s) {
        bean = (AllLineBean) JsonUtil.jsonToBean(s,AllLineBean.class);
        return bean.getFlag();
    }

    @Override
    public String clickForId() {
        return null;
    }

    @Override
    public String clickForName() {
        return null;
    }

    @Override
    public int getGroupCount() {
        return bean.getData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return bean.getData().get(groupPosition).getLINES().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return  bean.getData().get(groupPosition).getSTATIONNAME();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bean.getData().get(groupPosition).getLINES().get(childPosition);
    }

    @Override
    public String getGroupName(int groupPosition) {
        return bean.getData().get(groupPosition).getSTATIONNAME();
    }

    @Override
    public String getChildName(int groupPosition, int childPosition) {
        AllLineBean.DataBean.LINESBean line = bean.getData().get(groupPosition).getLINES().get(childPosition);
        return line.getLINENAME();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return bean.getData().get(groupPosition).getLINES().size();
    }

    @Override
    public void requestNet(StringCallback call) {
        if(netControl == null){
            netControl = new NetControl();
        }
        netControl.requestForCheckStationLine(call);
    }

    @Override
    public ExpandaListItem getItemBean(int groupPosition, int childPosition) {
        AllLineBean.DataBean.LINESBean line = bean.getData().get(groupPosition).getLINES().get(childPosition);
        ExpandaListItem item = new ExpandaListItem();
        item.setId(line.getPKID());
        item.setTitle(line.getLINENAME());
        return item;
    }

    @Override
    public BaseControl getDetailControl() {
        return new LineInfoControl();
    }
}
