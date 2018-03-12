package com.mapuni.administrator.activity.wdAc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.TreatmentprocessBean;
import com.mapuni.administrator.bean.UpTaskCompleteBean;
import com.mapuni.administrator.itemFactory.UpTaskCompleteHandleProcessRecyclerItemFactory;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

import static com.mapuni.administrator.R.id.recyclerView;


/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.activity.wdAc
 * @class 巡查上报已办详情Activity
 * @anthor Tianfy
 * @time 2017/8/31 14:40
 * @change
 * @chang time
 * @class describe
 */

public class UpTaskCompleteDetailActivity extends BaseActivity {
    @BindView(R.id.tv_SupervisionName)
    TextView mTvSupervisionName;
    @BindView(R.id.tv_SupervisionType)
    TextView mTvSupervisionType;
    @BindView(R.id.tv_reportGridName)
    TextView mTvReportGridName;
    @BindView(R.id.tv_reportName)
    TextView mTvReportName;
    @BindView(R.id.tv_reportNumber)
    TextView mTvReportNumber;
    @BindView(R.id.tv_TaskNumber)
    TextView mTvTaskNumber;
    @BindView(R.id.tv_questType)
    TextView mTvQuestType;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_longitude)
    TextView mTvLongitude;
    @BindView(R.id.tv_latitude)
    TextView mTvLatitude;
    @BindView(R.id.tv_quest_reason)
    TextView mTvQuestReason;
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    private String sessionId;
    private String mHandlingRecordUuid;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源
    @Override
    public int setLayoutResID() {
        return R.layout.activity_uptask_complete_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle("巡查上报已办详情");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new UpTaskCompleteHandleProcessRecyclerItemFactory(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

        mHandlingRecordUuid = getIntent().getStringExtra("handlingRecordUuid");
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        requestBaseData();//请求基本数据
        requestHandleProgressData();//请求处理流程信息

    }
    private void requestBaseData(){


        NetManager.requestTaskCompleteDetail(sessionId, mHandlingRecordUuid,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(UpTaskCompleteDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        UpTaskCompleteBean upTaskCompleteBean = gson.fromJson(response.toString(), UpTaskCompleteBean.class);
                            initBaseData(upTaskCompleteBean.getRows());
                    }
                });
    }
    private void initBaseData(UpTaskCompleteBean.RowsBean data) {
        mTvSupervisionName.setText(isEmpty(data.getSupervisionObjectName()));
        mTvSupervisionType.setText(isEmpty(data.getSupervisionObjectTypeName()));
        mTvReportGridName.setText(isEmpty(data.getGridName()));
        mTvReportName.setText(isEmpty(data.getUserRealname()));
        mTvReportNumber.setText(isEmpty(data.getTelephone()));
        mTvTaskNumber.setText(isEmpty(data.getNumber()));
        mTvQuestType.setText(isEmpty(data.getPatrolProblemType()));
        mTvLongitude.setText(isEmpty(data.getLongitude()));
        mTvLatitude.setText(isEmpty(data.getLatitude()));
        mTvQuestReason.setText(isEmpty(data.getDescription()));
        mTvAddress.setText(isEmpty(data.getAddress()));
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
                        Toast.makeText(UpTaskCompleteDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
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
