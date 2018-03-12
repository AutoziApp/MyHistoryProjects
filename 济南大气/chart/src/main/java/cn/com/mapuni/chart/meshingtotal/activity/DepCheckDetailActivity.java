package cn.com.mapuni.chart.meshingtotal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

import cn.com.mapuni.chart.meshingtotal.R;
import cn.com.mapuni.chart.meshingtotal.model.DepCheckDetailBean;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.interfaces.PathManager;

public class DepCheckDetailActivity extends BaseActivity {
    private View mainView;
    private YutuLoading yutuLoading;
    private TextView DeptName,OutportCount,hourtotalnum,daytotalnum,hournum,
                     hourrate,houralarm,houralarmrate,hourscore,houralarmscore,daynum,
                     dayrate,dayalarm,dayalarmrate,dayscore,dayalarmscore,totalscore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_mapuni);
        SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "部门考核详情");
        initView();
        getDetailData();
    }

    private void initView() {
        middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
        mainView = View.inflate(this, R.layout.activity_dep_check_detail, null);
        middleLayout.addView(mainView);
        DeptName= (TextView) findViewById(R.id.DeptName);
        OutportCount= (TextView) findViewById(R.id.OutportCount);
        hourtotalnum= (TextView) findViewById(R.id.hourtotalnum);
        daytotalnum= (TextView) findViewById(R.id.daytotalnum);
        hournum= (TextView) findViewById(R.id.hournum);
        hourrate= (TextView) findViewById(R.id.hourrate);
        houralarm= (TextView) findViewById(R.id.houralarm);
        houralarmrate= (TextView) findViewById(R.id.houralarmrate);
        hourscore= (TextView) findViewById(R.id.hourscore);
        houralarmscore= (TextView) findViewById(R.id.houralarmscore);
        daynum= (TextView) findViewById(R.id.daynum);
        dayrate= (TextView) findViewById(R.id.dayrate);
        dayalarm= (TextView) findViewById(R.id.dayalarm);
        dayalarmrate= (TextView) findViewById(R.id.dayalarmrate);
        dayscore= (TextView) findViewById(R.id.dayscore);
        dayalarmscore= (TextView) findViewById(R.id.dayalarmscore);
        totalscore= (TextView) findViewById(R.id.totalscore);
    }

    private void getDetailData() {
        yutuLoading = new YutuLoading(this);
        yutuLoading.setCancelable(true);
        yutuLoading.setLoadMsg("正在获取信息，请稍候", "");
        yutuLoading.showDialog();
        //接口调用方法一
        String url = PathManager.JINAN_URL + PathManager.getDepCheckDetail;
        RequestParams params = new RequestParams();// 添加提交参数
        params.addBodyParameter("StartTime",getIntent().getStringExtra("StartTime"));
        params.addBodyParameter("EndTime",getIntent().getStringExtra("EndTime"));
        params.addBodyParameter("DeptID",getIntent().getStringExtra("DeptID"));
        HttpUtils utils = new HttpUtils();
        utils.configCurrentHttpCacheExpiry(1000 * 5);
        utils.configTimeout(60 * 1000);//
        utils.configSoTimeout(60 * 1000);//
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(DepCheckDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
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
                    DepCheckDetailBean bean=new Gson().fromJson(root.getText(),DepCheckDetailBean.class);
                    DeptName.setText(bean.getDeptName());
                    OutportCount.setText(bean.getOutportCount());
                    hourtotalnum.setText(bean.getHourtotalnum());
                    daytotalnum.setText(bean.getDaytotalnum());
                    hournum.setText(bean.getHournum());
                    hourrate.setText(bean.getHourrate());
                    houralarm.setText(bean.getHouralarm());
                    houralarmrate.setText(bean.getHouralarmrate());
                    hourscore.setText(bean.getHourscore());
                    houralarmscore.setText(bean.getHouralarmscore());
                    daynum.setText(bean.getDaynum());
                    dayrate.setText(bean.getDayrate());
                    dayalarm.setText(bean.getDayalarm());
                    dayalarmrate.setText(bean.getDayalarmrate());
                    dayscore.setText(bean.getDayscore());
                    dayalarmscore.setText(bean.getDayalarmscore());
                    totalscore.setText(bean.getTotalscore());
                } catch (Exception e) {

                }
                if (yutuLoading != null) {
                    yutuLoading.dismissDialog();
                }

            }

        });
    }

}
