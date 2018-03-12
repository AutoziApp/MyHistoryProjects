package com.mapuni.caremission_ens.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.bean.NewsBean;
import com.mapuni.caremission_ens.presenter.NetControl;
import com.mapuni.caremission_ens.utils.JsonUtil;
import com.mapuni.caremission_ens.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class NewsContentActivity extends BaseActivity {
    NewsBean bean;
    YutuLoading yutuLoading;
    NetControl netControl;
    @Bind(R.id.sender)
    TextView sender;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        ButterKnife.bind(this);
        bean = (NewsBean) getIntent().getExtras().get("bean");
        setTitle(bean.getTITLE(), true, false);
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        netControl = new NetControl();
        netControl.newsContent(bean.getPKID(), call);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showFailed:
                yutuLoading.showDialog();
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        netControl.newsContent(bean.getPKID(), call);
                    }
                }, 2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
        }

    }

    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            Map map = JsonUtil.jsonToMap(response);
            yutuLoading.dismissDialog();
            if(map!=null){
                String _Content = (String) map.get("content");
                sender.setText("发送人:"+bean.getSENDPEOPLE());
                time.setText(bean.getSENDTIME());
                content.setText(_Content);
            }else{
                showFailed();
            }
        }
    };
}
