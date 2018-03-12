package com.mapuni.shangluo.activity.wdAc;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.MessageListBean;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;

    String id;
    String sessionId;
    @Override
    public int setLayoutResID() {
        return R.layout.activity_message_detail;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        setToolbarTitle("消息详情");
        id=getIntent().getStringExtra("id");
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.getNoticeDetail(sessionId, id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Log.i("aaa",e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                mSvProgressHUD.dismiss();
                Log.i("aaa",response);
                MessageListBean.RowsBean bean=new Gson().fromJson(response,MessageListBean.RowsBean.class);

                tvName.setText(bean.getSenderName());
                tvTime.setText(bean.getCreateTime());
                tvTitle.setText(bean.getTitle());
//                tvContent.setText(bean.getContent());
                CharSequence charSequence= Html.fromHtml(bean.getContent());
                tvContent.setText(charSequence);
                //该语句在设置后必加，不然没有任何效果
                tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
