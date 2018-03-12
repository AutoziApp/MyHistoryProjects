package cn.com.mapuni.meshingtotal.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.adapter.NoticeListAdapter;
import cn.com.mapuni.meshingtotal.model.NoticeBean;

public class AlarmReportListActivity  extends BaseActivity {

    private ListView mListView;
    private YutuLoading yutuLoading;
    private List<NoticeBean.DataBean> data=new ArrayList<>();
    private NoticeListAdapter mAdapter;
    /** 最后登录的用户信息SP name */
    private final String LAST_USER_SP_NAME = "lastuser";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.com.mapuni.chart.meshingtotal.R.layout.ui_mapuni);
        setBACK_ISSHOW(true);
        SetBaseStyle((RelativeLayout) findViewById(cn.com.mapuni.chart.meshingtotal.R.id.parentLayout), "预警专报");
        initView();
        getData();
    }
    private void initView() {
        middleLayout = ((LinearLayout) findViewById(cn.com.mapuni.chart.meshingtotal.R.id.middleLayout));
        LayoutInflater inflater = LayoutInflater.from(this);
        View mainView = inflater.inflate(R.layout.activity_alarm_report_list, null);
        middleLayout.addView(mainView);
        mListView= (ListView) mainView.findViewById(R.id.lv);
        mAdapter=new NoticeListAdapter(AlarmReportListActivity.this,data);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AlarmReportListActivity.this,AlarmReportDetailActivity.class);
                intent.putExtra("obj",data.get(position));
                startActivity(intent);
            }
        });

    }


    private void getData() {
        yutuLoading = new YutuLoading(this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("正在获取信息，请稍候", "");
        yutuLoading.showDialog();
        //接口调用方法一
//        String url = "http://119.164.253.237:8033/service/appservice.asmx/GetAlarmReport";
        String url = PathManager.getAlarmList;
        String userid= DisplayUitl.readPreferences(AlarmReportListActivity.this,LAST_USER_SP_NAME,"userid");
        RequestParams params = new RequestParams();// 添加提交参数
        params.addBodyParameter("userid", userid);
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(5 * 1000);//
        utils.configSoTimeout(5 * 1000);//
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(AlarmReportListActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
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
                    NoticeBean bean=gson.fromJson(str,NoticeBean.class);
                    List<NoticeBean.DataBean> dataBeens=bean.getData();
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
