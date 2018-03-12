package com.mapuni.shangluo.activity.dbAc;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.DelayTaskBean;
import com.mapuni.shangluo.bean.DownDelayTaskBean;
import com.mapuni.shangluo.manager.MessageEvent;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author Tianfy
 * @time 2017/8/30  16:10
 * @describe 延时申请任务Activity
 */
public class DelayApplicationActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    String sessionId;
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
    @BindView(R.id.tv_longitude)
    TextView mTvLongitude;
    @BindView(R.id.tv_latitude)
    TextView mTvLatitude;
    @BindView(R.id.tv_quest_reason)
    TextView mTvQuestReason;
    @BindView(R.id.tv_applyTime)
    TextView mTvApplyTime;
    @BindView(R.id.tv_apply_end_Time)
    TextView mTvApplyEndTime;
    @BindView(R.id.tv_apply_reason)
    TextView mTvApplyReason;
    @BindView(R.id.btn_agree)
    RadioButton mBtnAgree;
    @BindView(R.id.btn_disagree)
    RadioButton mBtnDisagree;
    @BindView(R.id.radioGroupID)
    RadioGroup mRadioGroupID;
    @BindView(R.id.et_opinion)
    EditText mEtOpinion;
    @BindView(R.id.btn_handle)
    Button mBtnHandle;
    @BindView(R.id.base_layout1)
    LinearLayout mBaseLayout1;
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
    @BindView(R.id.base_layout2)
    LinearLayout mBaseLayout2;
    @BindView(R.id.activity_delay_application)
    LinearLayout mActivityDelayApplication;
    private String mHandlingRecordUuid;
    private int delayApplyTaskType;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_delay_application;
    }

    @Override
    public void initView() {
        setToolbarTitle("延时申请任务");
        mRadioGroupID.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        requestBaseData();//请求基本数据
    }

    /**
     * @author Tianfy
     * @time 2017/8/30  11:50
     * @describe 联网请求延时申请数据
     */
    private void requestBaseData() {
//        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        mSvProgressHUD.showWithStatus("正在加载详情信息，请稍等...");
        mHandlingRecordUuid = getIntent().getStringExtra("handlingRecordUuid");
        String taskType = getIntent().getStringExtra("taskType");
        NetManager.requestDbTaskDetail(sessionId, mHandlingRecordUuid, taskType,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSvProgressHUD.dismiss();
                        Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MessageEvent("success"));
                        finish();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSvProgressHUD.dismissImmediately();
                        if (0 == getIntent().getIntExtra("delayApplyTaskType", 0)) {//上报的延时申请任务
                            DelayTaskBean delayTaskBean = gson.fromJson(response.toString(), DelayTaskBean.class);
                            if (delayTaskBean.getStatus() == 0) {//请求数据成功
                                mBaseLayout1.setVisibility(View.VISIBLE);
                                mBaseLayout2.setVisibility(View.GONE);
                                initBaseData1(delayTaskBean.getData());
                            }
                        } else {//下发的延时申请任务
                            DownDelayTaskBean downDelayTaskBean=gson.fromJson(response.toString(),DownDelayTaskBean.class);
                            if (downDelayTaskBean.getStatus() == 0) {//请求数据成功
                                mBaseLayout1.setVisibility(View.GONE);
                                mBaseLayout2.setVisibility(View.VISIBLE);
                                initBaseData2(downDelayTaskBean.getData());
                            }

                        }
                    }
                });
    }

    /**
     * @author Tianfy
     * @time 2017/8/30  15:22
     * @describe 初始化控件数据
     */
    private void initBaseData1(DelayTaskBean.DataBean data) {
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
        mTvApplyTime.setText(data.getApplyStartTime());
        mTvApplyEndTime.setText(data.getApplyEndTime());
        mTvApplyReason.setText(isEmpty(data.getApplyReason()));
    }
 /**
     * @author Tianfy
     * @time 2017/8/30  15:22
     * @describe 初始化控件数据
     */
    private void initBaseData2(DownDelayTaskBean.DataBean data) {
        DownDelayTaskBean.DataBean.TaskBean taskBean=data.getTask();
        mTvTaskName.setText(taskBean.getName());
        mTvTaskType.setText(data.getTaskType());
        mTvStartTime.setText(data.getStartTime());
        mTvEndTime.setText(data.getEndTime());
        mTvDescription.setText(taskBean.getDescription());
        mTvApplyTime.setText(data.getApplyStartTime());
        mTvApplyEndTime.setText(data.getApplyEndTime());
        mTvApplyReason.setText(isEmpty(data.getApplyReason()));
    }

    /**
     * @author Tianfy
     * @time 2017/8/30  16:02
     * @describe 判断请求的数据是否为空
     */
    private String isEmpty(String value) {
        if (value != null && value.length() > 0) {
            return value;
        }
        return "暂无数据";
    }

    @OnClick(R.id.btn_handle)
    public void onViewClicked() {
//        uploadTaskMsg();
        taskCheck();
    }

    private void taskCheck() {
        NetManager.judgeHandlingRecordIsExists(sessionId, mHandlingRecordUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络错误，处理失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("bbb",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int status=jsonObject.optInt("status",0);
                    switch (status){
                        case 0://未办
                            uploadTaskMsg();
                            break;
                        case -1://已办
                            Toast.makeText(DelayApplicationActivity.this,"任务已被他人处理",Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new MessageEvent("success"));
                            finish();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @author Tianfy
     * @time 2017/8/30  17:14
     * @describe 提交处理信息
     */
    private void uploadTaskMsg() {
        String opinion = mEtOpinion.getText().toString().trim();
        if (!mBtnAgree.isChecked() && !mBtnDisagree.isChecked()) {
            Toast.makeText(mContext, "请选择处理方式", Toast.LENGTH_SHORT).show();
            return;
        }
        String startTime = mTvApplyTime.getText().toString();
        String endTime = mTvApplyEndTime.getText().toString();
        if (TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime)) {
            Toast.makeText(mContext, "申请时间为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(operationType)){
            Toast.makeText(DelayApplicationActivity.this,"请选择操作类型",Toast.LENGTH_SHORT).show();
            return;
        }
        mSvProgressHUD.showWithStatus("正在处理，请稍等...");
        NetManager.handleTask("", mHandlingRecordUuid, operationType, opinion, "", "", startTime, endTime, new HashMap<String, File>(), "",new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Toast.makeText(mContext, "处理失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(mContext, "处理成功", Toast.LENGTH_SHORT).show();
                mSvProgressHUD.dismiss();
                EventBus.getDefault().post(new MessageEvent("success"));
                finish();
            }
        });
    }

    public static final String AGREE_APPLY = "11";//同意
    public static final String DISS_AGREE_APPLY = "12";//不同意
    private String operationType = "";

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_agree:
                operationType = AGREE_APPLY;
                break;
            case R.id.btn_disagree:
                operationType = DISS_AGREE_APPLY;
                break;
            default:
                break;
        }
    }

}
