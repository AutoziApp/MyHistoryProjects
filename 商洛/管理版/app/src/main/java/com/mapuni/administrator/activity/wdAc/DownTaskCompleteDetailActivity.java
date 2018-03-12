package com.mapuni.administrator.activity.wdAc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.DelayCompleteBean;
import com.mapuni.administrator.bean.TreatmentprocessBean;
import com.mapuni.administrator.fragment.PictureFragment;
import com.mapuni.administrator.itemFactory.DownTaskCompleteHandleProcessRecyclerItemFactory;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.activity.wdAc
 * @class 任务下发已办详情Activity
 * @anthor Tianfy
 * @time 2017/8/31 15:41
 * @change
 * @chang time
 * @class describe
 */

public class DownTaskCompleteDetailActivity extends BaseActivity {
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_taskName)
    TextView mTvTaskName;
    @BindView(R.id.tv_taskType)
    TextView mTvTaskType;
    @BindView(R.id.tv_startTime)
    TextView mTvStartTime;
    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_noAttachInfo)
    TextView tvNoAttachInfo;
    @BindView(R.id.ll_fragment_container)
    LinearLayout llFragmentContainer;


    private String sessionId;
    private String mHandlingRecordUuid;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvNoAttachInfo.setVisibility(View.VISIBLE);
            llFragmentContainer.setVisibility(View.GONE);
        }
    };
    @Override
    public int setLayoutResID() {
        return R.layout.activity_down_task_complete_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle("任务下发已办详情");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new DownTaskCompleteHandleProcessRecyclerItemFactory(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        mHandlingRecordUuid = getIntent().getStringExtra("handlingRecordUuid");
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        requestBaseData();//请求基本数据
        requestHandleProgressData();//请求处理流程信息
    }
    private void addFragment(String taskUuid) {
        tvNoAttachInfo.setVisibility(View.GONE);
        llFragmentContainer.setVisibility(View.VISIBLE);
        PictureFragment fragment=new PictureFragment();
        Bundle bundle=new Bundle();
        bundle.putString("taskUuid",taskUuid);
        bundle.putString("flag",DownTaskCompleteDetailActivity.class.getSimpleName());
        fragment.setHandler(mHandler);
        fragment.setArguments(bundle);
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment_container,fragment).commit();
    }


    private void requestBaseData(){


        NetManager.requestTaskCompleteDetail(sessionId, mHandlingRecordUuid,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(DownTaskCompleteDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        DelayCompleteBean delayCompleteBean = gson.fromJson(response.toString(), DelayCompleteBean.class);
                        initBaseData(delayCompleteBean.getRows());
                        addFragment(delayCompleteBean.getRows().getUuid());
                    }
                });
    }

    private void initBaseData(DelayCompleteBean.RowsBean rows) {
        mTvTaskName.setText(isEmpty(rows.getName()));
        mTvTaskType.setText(isEmpty(rows.getTaskType()));
        mTvStartTime.setText(isEmpty(rows.getStartTime()));
        mTvEndTime.setText(isEmpty(rows.getEndTime()));
        mTvDescription.setText(isEmpty(rows.getDescription()));
    }

    private String isEmpty(String value){
        if (value!=null&&value.length()>0){
            return value;
        }
        return "暂无数据";
    }

    private void requestHandleProgressData(){
        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.showWithStatus("正在加载详情信息，请稍等...");
        NetManager.requestHandleProcessComplete(sessionId, mHandlingRecordUuid,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSVProgressHUD.dismiss();
                        Toast.makeText(DownTaskCompleteDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mSVProgressHUD.dismiss();
                        TreatmentprocessBean treatmentprocessBean = gson.fromJson(response.toString(), TreatmentprocessBean.class);
                        List<TreatmentprocessBean.RowsBean> list = treatmentprocessBean.getRows();
                        if (list!=null&&list.size()>0){
                            adapter.setDataList(list);
                        }

                    }
                });
    }
}
