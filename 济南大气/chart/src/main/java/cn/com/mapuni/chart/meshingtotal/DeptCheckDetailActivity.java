package cn.com.mapuni.chart.meshingtotal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import cn.com.mapuni.chart.meshingtotal.adapter.DeptCheckDetailAdapter;
import cn.com.mapuni.chart.meshingtotal.model.DeptCheckDetailBean;
import cn.com.mapuni.chart.meshingtotal.model.DeptDetailItemBean;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;

public class DeptCheckDetailActivity extends BaseActivity {

    private YutuLoading yutuLoading;
    private String startTime;
    private String endTime;
    private String deptID;
    private ListView lvDeptcheck;
    private ArrayList<DeptDetailItemBean> items=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
        setBACK_ISSHOW(true);
        SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "部门区域考核详情");
        initView();
        initData();
    }

    private void initView() {
        middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.activity_dept_check_detail, null);
        middleLayout.addView(mainView);
        lvDeptcheck = (ListView) findViewById(R.id.lv_deptcheck);
    }

    private void initData() {
        yutuLoading = new YutuLoading(DeptCheckDetailActivity.this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("正在获取数据，请稍候", "");
        yutuLoading.showDialog();
        String url = PathManager.JINAN_URL+PathManager.getDepCheckDetail;
        startTime=getIntent().getStringExtra("StartTime");
        endTime=getIntent().getStringExtra("EndTime");
        deptID=getIntent().getStringExtra("DeptID");
        if(!TextUtils.isEmpty(startTime)&&!TextUtils.isEmpty(endTime)&&!TextUtils.isEmpty(deptID)){
            RequestParams params = new RequestParams();// 添加提交参数
            params.addBodyParameter("StartTime", startTime);
            params.addBodyParameter("EndTime",endTime);
            params.addBodyParameter("DeptID", deptID);
            HttpUtils utils = new HttpUtils();
            utils.configCurrentHttpCacheExpiry(1000 * 5);
            utils.configTimeout(5 * 1000);//
            utils.configSoTimeout(5 * 1000);//
            utils.send(HttpRequest.HttpMethod.POST, url, params,   new RequestCallBack<String>() {
                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    if (yutuLoading != null) {
                        yutuLoading.dismissDialog();
                    }
                    Toast.makeText(DeptCheckDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    if (yutuLoading != null) {
                        yutuLoading.dismissDialog();
                    }

                    String result = String.valueOf(arg0.result);
                    try {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                        SAXReader reader = new SAXReader();
                        Document document = reader.read(inputStream);
                        Element root = document.getRootElement();
                        String text = root.getText();
                        Gson gson=new Gson();
                        DeptCheckDetailBean deptCheckDetailBean = gson.fromJson(text, DeptCheckDetailBean.class);
                        items.add(new DeptDetailItemBean("部门名称",deptCheckDetailBean.getDeptName()));
                        items.add(new DeptDetailItemBean("监测点总数", deptCheckDetailBean.getOutportCount()+""));
                        items.add(new DeptDetailItemBean("小时应上传总数", deptCheckDetailBean.getHourtotalnum()+""));
                        items.add(new DeptDetailItemBean("日应上传总数", deptCheckDetailBean.getDaytotalnum()+""));
                        items.add(new DeptDetailItemBean("小时上传个数", deptCheckDetailBean.getHournum()+""));
                        items.add(new DeptDetailItemBean("小时上传率", deptCheckDetailBean.getHourrate()));
                        items.add(new DeptDetailItemBean("小时超标个数", deptCheckDetailBean.getHouralarm()+""));
                        items.add(new DeptDetailItemBean("小时超标率", deptCheckDetailBean.getHouralarmrate()+""));
                        items.add(new DeptDetailItemBean("小时得分", deptCheckDetailBean.getHourscore()+""));
                        items.add(new DeptDetailItemBean("小时超标得分", deptCheckDetailBean.getHouralarmscore()+""));
                        items.add(new DeptDetailItemBean("日应个数", deptCheckDetailBean.getDaynum()+""));
                        items.add(new DeptDetailItemBean("日上传率", deptCheckDetailBean.getDayrate()));
                        items.add(new DeptDetailItemBean("日超标个数", deptCheckDetailBean.getDayalarm()+""));
                        items.add(new DeptDetailItemBean("日超标率", deptCheckDetailBean.getDayalarmrate()));
                        items.add(new DeptDetailItemBean("日得分", deptCheckDetailBean.getDayscore()+""));
                        items.add(new DeptDetailItemBean("日超标得分", deptCheckDetailBean.getDayalarmscore()+""));
                        items.add(new DeptDetailItemBean("总得分", deptCheckDetailBean.getTotalscore()+""));
                        lvDeptcheck.setAdapter(new DeptCheckDetailAdapter(DeptCheckDetailActivity.this,items));
                    } catch (Exception e) {
                        Toast.makeText(DeptCheckDetailActivity.this, "解析数据异常", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
}
