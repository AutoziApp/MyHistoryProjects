package com.yutu.car.presenter;

import com.yutu.car.bean.DetailBean;
import com.yutu.car.utils.JsonUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yawei on 2017/3/30.
 */

public class MmsManageControl extends BaseControl{
    private String id;
    private String title;
    private NetControl netControl;
    private String[] keys = new String[]{"content",};
    private String[] names = new String[]{"内容:"};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public void requestData(StringCallback call){
        if(netControl==null){
            netControl = new NetControl();
        }
        netControl.requestForManageMmsDetails(id,call);
    }

    @Override
    public List transData(String response) {
        List list = new ArrayList();
        Map map = JsonUtil.jsonToMap(response);
        String content=map.get("content").toString();

            DetailBean bean = new DetailBean();
            bean.setTitle("内容");
            bean.setValue(content);
            list.add(bean);
        return list;
    }

    @Override
    public List transDataJX(JSONObject jsonObject) {
        return null;
    }


}
