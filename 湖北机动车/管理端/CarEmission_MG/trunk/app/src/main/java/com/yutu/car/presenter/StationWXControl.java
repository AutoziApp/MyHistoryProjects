package com.yutu.car.presenter;

import com.yutu.car.bean.AllStationBean;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by yawei on 2017/4/24.
 */

public class StationWXControl extends ExpandaListControl {
    private AllStationBean bean;
    private NetControl netControl;
    @Override
    public String getTitle() {
        return "检修机构";
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setTag(Object obj) {

    }

    @Override
    public int jsonToBean(String s) {
        bean = (AllStationBean) JsonUtil.jsonToBean(s,AllStationBean.class);
        return bean.getResult();
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
        return bean.getInfo().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return bean.getInfo().get(groupPosition).getStationinfo().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return  bean.getInfo().get(groupPosition).getQhmc();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bean.getInfo().get(groupPosition).getStationinfo().get(childPosition);
    }

    @Override
    public String getGroupName(int groupPosition) {
        return bean.getInfo().get(groupPosition).getQhmc();
    }

    @Override
    public String getChildName(int groupPosition, int childPosition) {
        AllStationBean.InfoBean.StationInfoBean station = bean.getInfo().get(groupPosition).getStationinfo().get(childPosition);
        return station.getStationname();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return bean.getInfo().get(groupPosition).getStationinfo().size();
    }

    @Override
    public void requestNet(StringCallback call) {
        if(netControl == null){
            netControl = new NetControl();
        }
        netControl.requestForAllWXStation(call);
    }

    @Override
    public ExpandaListItem getItemBean(int groupPosition, int childPosition) {
        AllStationBean.InfoBean.StationInfoBean station = bean.getInfo().get(groupPosition).getStationinfo().get(childPosition);
        ExpandaListItem item = new ExpandaListItem();
        item.setId(station.getStationpkid());
        item.setTitle(station.getStationname());
        return item;
    }

    @Override
    public BaseControl getDetailControl() {
        return new StationInfoWXControl();
    }
}
