package cn.com.mapuni.meshingtotal.activity.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.List;

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.adapter.NoticeDetailAdapter;
import cn.com.mapuni.meshingtotal.model.NoticeBean;
import cn.com.mapuni.meshingtotal.model.NoticeDetailBean;

public class AlarmReportDetailActivity extends BaseActivity {

    private ListView mListView;
    private YutuLoading yutuLoading;
    private List<NoticeDetailBean.DataBean> data=new ArrayList<>();
    private NoticeDetailAdapter mAdapter;
    private NoticeBean.DataBean obj;
    private TextView tv_EntName,tv_OutportName,tv_PollutantName,tv_StandardVal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.com.mapuni.chart.meshingtotal.R.layout.ui_mapuni);
        setBACK_ISSHOW(true);
        obj= (NoticeBean.DataBean) getIntent().getSerializableExtra("obj");
        SetBaseStyle((RelativeLayout) findViewById(cn.com.mapuni.chart.meshingtotal.R.id.parentLayout), "超标详情");
        initView();
        getData();
    }
    private void initView() {
        middleLayout = ((LinearLayout) findViewById(cn.com.mapuni.chart.meshingtotal.R.id.middleLayout));
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.activity_alarm_report_detail, null);
        middleLayout.addView(mainView);
        mListView= (ListView) mainView.findViewById(R.id.lv);
        mAdapter=new NoticeDetailAdapter(AlarmReportDetailActivity.this,data,obj.getMonitorTime());
        mListView.setAdapter(mAdapter);
        tv_EntName= (TextView) mainView.findViewById(R.id.tv_EntName);
        tv_OutportName= (TextView) mainView.findViewById(R.id.tv_OutportName);
        tv_PollutantName= (TextView) mainView.findViewById(R.id.tv_PollutantName);
        tv_StandardVal= (TextView) mainView.findViewById(R.id.tv_StandardVal);
        tv_EntName.setText(obj.getEntName());
        tv_OutportName.setText(obj.getOutportName());
        tv_PollutantName.setText(obj.getPollutantName());
        tv_StandardVal.setText(obj.getStandardVal()+"");

    }


    private void getData() {
        yutuLoading = new YutuLoading(this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("正在获取信息，请稍候", "");
        yutuLoading.showDialog();
        //接口调用方法一
//        String url = "http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReportDetail";
        String url = PathManager.getAlarmDetail;

        RequestParams params = new RequestParams();// 添加提交参数
        params.addBodyParameter("top", "24");
        params.addBodyParameter("outportcode", ""+obj.getOutportCode());
        params.addBodyParameter("polltantcode", obj.getPollutantCode());
        params.addBodyParameter("begintime", obj.getMonitorTime());
        params.addBodyParameter("endtime", obj.getEndtime());
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(5 * 1000);//
        utils.configSoTimeout(5 * 1000);//
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(AlarmReportDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = String.valueOf(arg0.result);

                try {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBytes());
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(inputStream);
                    Element root = document.getRootElement();
                    String str=root.getText();
                    Gson gson=new Gson();
                    NoticeDetailBean bean=gson.fromJson(str,NoticeDetailBean.class);
                    List<NoticeDetailBean.DataBean> dataBeens=bean.getData();
                    data.addAll(dataBeens);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }
            }
        });
    }
}
