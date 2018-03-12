package com.mapuni.administrator.activity.myNewsAc;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.utils.LogUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.DownTaskDetail;
import com.mapuni.administrator.bean.FenpeiBean;
import com.mapuni.administrator.bean.HandleProcess;
import com.mapuni.administrator.bean.XiafaBean;
import com.mapuni.administrator.bean.ZhuanbanBean;
import com.mapuni.administrator.fragment.PictureFragment;
import com.mapuni.administrator.itemFactory.HandleProcessRecyclerItemFactory;
import com.mapuni.administrator.manager.MessageEvent;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.FileUtils;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TimeUtil;
import com.mapuni.administrator.view.RadioGroupEx;
import com.mapuni.administrator.view.imagePicker.ImagePickerAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

public class DownTaskDetailActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener {
    private TextView tvTaskName,tvTaskType,tvStartTime,tvEndTime,tvDescription,tvSelectStartTime,tvSelectEndTime,tvShenheGrid,
            tvXiaFa,tvZhuanBan;
    private RadioButton rbPass,rbNoPass,rbUpload,rbDown,rbZhuanban,rbBack,rbFinish,rbApplyTime;
    private RadioGroupEx radioGroup;
    private EditText etOpinion;
    private Button btnHandle;
    private LinearLayout distributionLayout,applyTimeLayout,attachLayout,shenheLayout,xiafaLayout,zhuanbanLayout;
    private Spinner spDistribution;
    List<ZhuanbanBean.DataBean> listZhuanban;
    List<XiafaBean.DataBean> listXiafa;

    //请求参数
    String sessionId;
    String handlingRecordUuid_upload="";
    String operationType="";
    String opinion="";
    String subGridsUuids="";
    String transferGridsUuids="";
    String distributionUserUuid="";
    String startTime="";
    String endTime="";
    Map<String, File> files=new HashMap<>();//图片文件

    private RecyclerView recyclerView;//处理流程
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源


    //图片选择
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter_img;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    ArrayList<ImageItem> images = null;

    //录音视频
    private LinearLayout audio_view;
    private LinearLayout vedio_view;
    private TextView tvAudio;
    private TextView tvVideo;
    private LinearLayout audio_info;
    private LinearLayout vedio_info;
    private TextView audio_time;
    private TextView vedio_time;
    private ImageView playAudio;
    private ImageView playVedio;
    private ImageView deleteAudio;
    private ImageView deleteVedio;
    private final int RECORD_AUDIO = 1002;
    private final int RECORD_VIDEO = 1003;
    private Uri uriAudio;
    private Uri uriVideo;
    private RadioButton rbDistribution;
    private List<FenpeiBean.RowsBean> mFenpeiList;

    String gridNames;
    private TextView tvNoAttachInfo;
    private LinearLayout llFragmentContainer;

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
        return R.layout.activity_down_task_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle("下发任务详情");
        handlingRecordUuid_upload=getIntent().getStringExtra("handlingRecordUuid");
        tvNoAttachInfo= (TextView) findViewById(R.id.tv_noAttachInfo);
        llFragmentContainer= (LinearLayout) findViewById(R.id.ll_fragment_container);
        initBaseXX();//初始化基本信息
        initProcess();//初始化任务流程
        initWidget();//初始化图片选择
        initAudioVideo();//初始化录音视频
    }

    private void addFragment(String taskUuid) {
        tvNoAttachInfo.setVisibility(View.GONE);
        llFragmentContainer.setVisibility(View.VISIBLE);
        PictureFragment fragment=new PictureFragment();
        Bundle bundle=new Bundle();
        bundle.putString("taskUuid",taskUuid);
        bundle.putString("flag",DownTaskDetailActivity.class.getSimpleName());
        fragment.setHandler(mHandler);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment_container,fragment).commit();
    }

    private void initProcess() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new HandleProcessRecyclerItemFactory(this));
        recyclerView.setAdapter(adapter);
    }

    private void initBaseXX() {
        tvZhuanBan= (TextView) findViewById(R.id.tv_zhuanBan);
        zhuanbanLayout= (LinearLayout) findViewById(R.id.ll_zhuanBan);
        tvXiaFa= (TextView) findViewById(R.id.tv_xiafa);
        xiafaLayout= (LinearLayout) findViewById(R.id.ll_xiafa);
        attachLayout= (LinearLayout) findViewById(R.id.ll_attach);
        shenheLayout= (LinearLayout) findViewById(R.id.ll_shenhe);
        tvSelectStartTime= (TextView) findViewById(R.id.tv_selectStartTime);
        tvSelectEndTime= (TextView) findViewById(R.id.tv_selectEndTime);
        tvShenheGrid= (TextView) findViewById(R.id.tv_shenheGrid);
        applyTimeLayout= (LinearLayout) findViewById(R.id.ll_applyTime);
        tvTaskName= (TextView) findViewById(R.id.tv_taskName);
        tvTaskType= (TextView) findViewById(R.id.tv_taskType);
        tvStartTime= (TextView) findViewById(R.id.tv_startTime);
        tvEndTime= (TextView) findViewById(R.id.tv_endTime);
        tvDescription= (TextView) findViewById(R.id.tv_description);
        radioGroup= (RadioGroupEx) findViewById(R.id.radioGroupID);
        rbPass= (RadioButton) findViewById(R.id.rb_pass);
        rbNoPass= (RadioButton) findViewById(R.id.rb_no_pass);
        rbUpload= (RadioButton) findViewById(R.id.rb_upload);
        rbDown= (RadioButton) findViewById(R.id.rb_down);
        rbZhuanban = (RadioButton) findViewById(R.id.rb_zhuanban);
        rbBack= (RadioButton) findViewById(R.id.rb_back);
        rbFinish= (RadioButton) findViewById(R.id.rb_finish);
        rbApplyTime= (RadioButton) findViewById(R.id.rb_applyTime);
        rbDistribution = (RadioButton) findViewById(R.id.rb_distribution);
        etOpinion= (EditText) findViewById(R.id.et_opinion);
        btnHandle= (Button) findViewById(R.id.btn_handle);
        spDistribution= (Spinner) findViewById(R.id.sp_distribution);
        distributionLayout= (LinearLayout) findViewById(R.id.ll_distribution);
        btnHandle.setOnClickListener(this);
        tvSelectStartTime.setOnClickListener(this);
        tvSelectEndTime.setOnClickListener(this);
        tvShenheGrid.setOnClickListener(this);
        tvXiaFa.setOnClickListener(this);
        tvZhuanBan.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_pass://通过
                        if(flag){
                            shenheLayout.setVisibility(View.VISIBLE);
                        }
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        attachLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_no_pass://不通过
                        if(flag){
                            shenheLayout.setVisibility(View.VISIBLE);
                        }
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        attachLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_upload://上报
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        shenheLayout.setVisibility(View.GONE);
                        break;
                    case R.id.rb_down://下发
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.VISIBLE);
                        shenheLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        applyTimeLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_zhuanban://转办
                        zhuanbanLayout.setVisibility(View.VISIBLE);
                        xiafaLayout.setVisibility(View.GONE);
                        shenheLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        applyTimeLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_back://回退
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        shenheLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_finish://办结
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        shenheLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_applyTime://延时申请
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        shenheLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.VISIBLE);
                        etOpinion.setHint("请输入申请理由");
                        break;
                    case R.id.rb_distribution://分配
                        zhuanbanLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.GONE);
                        shenheLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        applyTimeLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.VISIBLE);
                        etOpinion.setHint("请输入分配意见");
                        if(mFenpeiList!=null && mFenpeiList.size()>0){
                            List<String> list=new ArrayList<String>();
                            for (FenpeiBean.RowsBean dataBean:mFenpeiList){
                                list.add(dataBean.getUserRealname());
                            }
                            initSpinner(spDistribution,list);
                            distributionUserUuid=mFenpeiList.get(0).getUuid();
                        }
                        break;
                }
            }
        });
        spDistribution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    distributionUserUuid=mFenpeiList.get(position).getUuid();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initWidget() {
        RecyclerView recyclerViewImg = (RecyclerView) findViewById(R.id.recyclerView_img);
        selImageList = new ArrayList<>();
        adapter_img = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter_img.setOnItemClickListener(this);
        recyclerViewImg.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewImg.setHasFixedSize(true);
        recyclerViewImg.setAdapter(adapter_img);
    }
    private void initAudioVideo() {
        audio_view = (LinearLayout)findViewById(R.id.audio_view);
        vedio_view = (LinearLayout)findViewById(R.id.vedio_view);

        tvAudio = (TextView)findViewById(R.id.tvAudio);
        tvVideo = (TextView)findViewById(R.id.tvVideo);

        audio_info = (LinearLayout)findViewById(R.id.audio_info);
        vedio_info = (LinearLayout)findViewById(R.id.vedio_info);

        audio_time = (TextView)findViewById(R.id.audio_time);
        vedio_time = (TextView)findViewById(R.id.vedio_time);

        playAudio = (ImageView)findViewById(R.id.playAudio) ;
        playVedio = (ImageView)findViewById(R.id.playVedio) ;

        deleteAudio = (ImageView)findViewById(R.id.deleteAudio) ;
        deleteVedio = (ImageView)findViewById(R.id.deleteVedio) ;

        playAudio.setOnClickListener(this);
        playVedio.setOnClickListener(this);
        audio_view.setOnClickListener(this);
        vedio_view.setOnClickListener(this);
        deleteAudio.setOnClickListener(this);
        deleteVedio.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent1 = new Intent(DownTaskDetailActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter_img.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(DownTaskDetailActivity.this, "sessionId", "");
        requestBaseData();//请求基本数据
        requestHandleProgressData();//请求处理流程信息
        getBrotherGrids();//获取转办单位
        getDownGrids();//获取下发单位
        getDistribution();//获取分配单位人员
        getShenHeGridList();//获取审核时的网格人员列表
    }

    boolean flag=false;//判断通过与否下的网格筛选是否存在的标记
    private void getShenHeGridList() {
        String handlingRecordUuid=getIntent().getStringExtra("handlingRecordUuid");
        NetManager.requestGridUuidsAndGridNamesByHandlingRecordUuid(sessionId, handlingRecordUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("yyy",e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("yyy",response);
                try {
                    JSONObject jsobj=new JSONObject(response);
                    if(0==jsobj.optInt("status",-1)){
                        flag=true;
                    }else {
                        flag=false;
                    }
                     gridNames=jsobj.optString("gridNames");
                    subGridsUuids=jsobj.optString("gridUuids");
                    tvShenheGrid.setText(gridNames);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getDistribution() {
        NetManager.getDistribution(sessionId, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.i(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.i("分配"+response);
                FenpeiBean fenpeiBean = gson.fromJson(response.toString(), FenpeiBean.class);
                mFenpeiList = fenpeiBean.getRows();
                
            }
        });
    }
    

    private void requestBaseData(){
//        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
//        mSVProgressHUD.showWithStatus("正在加载详情信息，请稍等...");
        final String handlingRecordUuid=getIntent().getStringExtra("handlingRecordUuid");
        String taskType=getIntent().getStringExtra("taskType");
        NetManager.requestDbTaskDetail(sessionId, handlingRecordUuid, taskType,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        mSVProgressHUD.dismiss();
                        Toast.makeText(DownTaskDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MessageEvent("success"));
                        finish();
                    }
                    @Override
                    public void onResponse(String response, int id) {
//                        mSVProgressHUD.dismiss();
                        Log.i("aaa","下发详情"+response);
                        Gson gson=new Gson();
                        
                        
                        DownTaskDetail downTaskDetail=gson.fromJson(response.toString(),DownTaskDetail.class);
                        if(downTaskDetail.getStatus()==0){//请求数据成功

                            initBaseData(downTaskDetail.getData());
                            addFragment(downTaskDetail.getData().getTask().getUuid());
                        }
                    }
                });
    }

    private void requestHandleProgressData(){
        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.showWithStatus("正在加载详情信息，请稍等...");
        String handlingRecordUuid=getIntent().getStringExtra("handlingRecordUuid");
        NetManager.requestHandleProcess(sessionId, handlingRecordUuid,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSVProgressHUD.dismiss();
                        Toast.makeText(DownTaskDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mSVProgressHUD.dismiss();
                        Gson gson=new Gson();
                        Type type = new TypeToken<ArrayList<HandleProcess>>() {}.getType();
                        List<HandleProcess> handleProcesses=gson.fromJson(response.toString(),type);
                        if(handleProcesses!=null && handleProcesses.size()>0){//请求数据成功
                            adapter.addAll(handleProcesses);
                        }
                    }
                });
    }
    private void initBaseData(DownTaskDetail.DataBean data) {
        DownTaskDetail.DataBean.TaskBean taskBean=data.getTask();
        tvTaskName.setText(taskBean.getName()!=null?taskBean.getName():"");
        tvTaskType.setText(taskBean.getTaskType().getName()!=null?taskBean.getTaskType().getName():"");
        tvStartTime.setText(taskBean.getStartTime()!=null?taskBean.getStartTime():"");
        tvEndTime.setText(taskBean.getEndTime()!=null?taskBean.getEndTime():"");
        tvDescription.setText(taskBean.getDescription()!=null?taskBean.getDescription():"");
        DownTaskDetail.DataBean.JurisdictionJudgeBean bean=data.getJurisdictionJudge();
        if(1==bean.getJudgeAudited()) rbPass.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeAuditnotthrough()) rbNoPass.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeReport()) rbUpload.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeIssued()) rbDown.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeTurn()) rbZhuanban.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeReturn()) rbBack.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeComplete()) rbFinish.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeDelayapply()) rbApplyTime.setVisibility(View.VISIBLE);
        // TODO: 2017/11/21 需要从接口获取分配状态(此时暂时获取的是延时申请任务的状态) 
        if(1==bean.getJudgeDistribution()) rbDistribution.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter_img.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter_img.setImages(selImageList);
                }
            }
        }else if (requestCode == RECORD_AUDIO && resultCode == RESULT_OK) {

            uriAudio = data.getData();

            MediaPlayer player=MediaPlayer.create(DownTaskDetailActivity.this, uriAudio);
            int time=player.getDuration();
            audio_time.setText(TimeUtil.formatDuring(time));
            audio_info.setVisibility(View.VISIBLE);
        } else if (requestCode == RECORD_VIDEO && resultCode == RESULT_OK) {
            uriVideo = data.getData();
            MediaPlayer player=MediaPlayer.create(DownTaskDetailActivity.this, uriVideo);
            int time=player.getDuration();
            vedio_time.setText(TimeUtil.formatDuring(time));
            vedio_info.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhuanBan:
                showZhuanBanDialog();
                break;
            case R.id.tv_xiafa:
                showXiaFaDialog();
                break;

            case R.id.playVedio:{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String type = "video/mp4";
                intent.setDataAndType(uriVideo, type);
                startActivity(intent);
                break;
            }

            case R.id.playAudio:{
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(uriAudio, "audio/*");
                startActivity(it);
                break;
            }

            case R.id.vedio_view: {
                Intent video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                video_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//0 低质量 1 高质量
                video_intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 30*1024*1024);//20M
                video_intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);//录制时间上限  10s
                startActivityForResult(video_intent, RECORD_VIDEO);
                break;
            }

            case R.id.audio_view:{
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, RECORD_AUDIO);
                }else{
                    Toast.makeText(DownTaskDetailActivity.this,"您的手机没有安装录音程序",Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.deleteAudio:{
                audio_info.setVisibility(View.INVISIBLE);
                uriAudio = null;
                break;
            }
            case R.id.deleteVedio:{
                vedio_info.setVisibility(View.INVISIBLE);
                uriVideo = null;
                break;
            }
            case R.id.btn_handle:
//                upload();
                taskCheck();
                break;
            case R.id.tv_selectStartTime:
                showTimePicker(tvSelectStartTime);
                break;
            case R.id.tv_selectEndTime:
                showTimePicker(tvSelectEndTime);
                break;
            case R.id.tv_shenheGrid:
                showMutiDialog();
                break;
        }

    }
    private void taskCheck() {
        NetManager.judgeHandlingRecordIsExists(sessionId, handlingRecordUuid_upload, new StringCallback() {
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
                            upload();
                            break;
                        case -1://已办
                            Toast.makeText(DownTaskDetailActivity.this,"任务已被他人处理",Toast.LENGTH_SHORT).show();
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

    private void showZhuanBanDialog() {
        if(listZhuanban==null||listZhuanban.size()==0)
            return;

        String items[]=new String[listZhuanban.size()];
        for (int i=0;i<listZhuanban.size();i++){
            items[i]=listZhuanban.get(i).getName();
        }

        final List<ZhuanbanBean.DataBean> selectedList=new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("请选择网格");
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    selectedList.add(listZhuanban.get(which));
                }else {
                    selectedList.remove(listZhuanban.get(which));
                }

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gridNames="";
                transferGridsUuids="";
                for(int i=0;i<selectedList.size();i++){
                    gridNames=gridNames+selectedList.get(i).getName()+",";
                    transferGridsUuids=transferGridsUuids+selectedList.get(i).getUuid()+",";
                }
                if(!TextUtils.isEmpty(gridNames))
                    gridNames=gridNames.substring(0,gridNames.length()-1);
                tvZhuanBan.setText(gridNames);

            }
        });
        builder.show();
    }

    private void showXiaFaDialog() {
        if(listXiafa==null||listXiafa.size()==0)
            return;

        String items[]=new String[listXiafa.size()];
        for (int i=0;i<listXiafa.size();i++){
            items[i]=listXiafa.get(i).getName();
        }

        final List<XiafaBean.DataBean> selectedList=new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("请选择网格");
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    selectedList.add(listXiafa.get(which));
                }else {
                    selectedList.remove(listXiafa.get(which));
                }

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                gridNames="";
                subGridsUuids="";
                for(int i=0;i<selectedList.size();i++){
                    gridNames=gridNames+selectedList.get(i).getName()+",";
                    subGridsUuids=subGridsUuids+selectedList.get(i).getUuid()+",";
                }
                if(!TextUtils.isEmpty(gridNames))
                    gridNames=gridNames.substring(0,gridNames.length()-1);
                tvXiaFa.setText(gridNames);

            }
        });
        builder.show();
    }

    private void showMutiDialog() {
        if(listXiafa==null||listXiafa.size()==0)
        return;

         String items[]=new String[listXiafa.size()];
        for (int i=0;i<listXiafa.size();i++){
            items[i]=listXiafa.get(i).getName();
        }

        final List<XiafaBean.DataBean> selectedList=new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("请选择网格");
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    selectedList.add(listXiafa.get(which));
                }else {
                    selectedList.remove(listXiafa.get(which));
                }

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                gridNames="";
                subGridsUuids="";
                    for(int i=0;i<selectedList.size();i++){
                        gridNames=gridNames+selectedList.get(i).getName()+",";
                        subGridsUuids=subGridsUuids+selectedList.get(i).getUuid()+",";
                    }
                if(!TextUtils.isEmpty(gridNames))
                gridNames=gridNames.substring(0,gridNames.length()-1);
                tvShenheGrid.setText(gridNames);

            }
        });
        builder.show();
    }

    private void showTimePicker(final TextView tv) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(2017,0,1);
        endDate.set(2050,11,31);

        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv.setText(TimeUtil.formatDate(date,"yyyy-MM-dd"));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(16)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.show();
    }
    private void upload() {
        //附件信息
        for (ImageItem item:selImageList){//添加图片
            String[] fileName = item.path.split("/");
            files.put(fileName[fileName.length-1],new File(item.path));
        }

        if (uriAudio != null) {//添加录音
            String audioPath= FileUtils.getPath(DownTaskDetailActivity.this, uriAudio);
            String audioName = audioPath.substring(audioPath.lastIndexOf("/") + 1, audioPath.length());
            files.put(audioName,new File(audioPath));
        }
        if (uriVideo != null) {//添加视频
            String videoPath= FileUtils.getPath(DownTaskDetailActivity.this, uriVideo);
            String videoName = videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.length());
            files.put(videoName,new File(videoPath));
        }
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rb_pass://通过
                operationType="9";
                break;
            case R.id.rb_no_pass://不通过
                operationType="13";
                break;
            case R.id.rb_upload://上报
                operationType="1";
                break;
            case R.id.rb_down://下发
                operationType="2";
                break;
            case R.id.rb_zhuanban://转办
                operationType="3";
                break;
            case R.id.rb_back://回退
                operationType="4";
                break;
            case R.id.rb_finish://办结
                operationType="5";
                break;
            case R.id.rb_applyTime://延时申请
                operationType="10";
                startTime=tvSelectStartTime.getText().toString();
                endTime=tvSelectEndTime.getText().toString();
                break;
            case R.id.rb_distribution://分配
                operationType="14";
                break;
        }
        if("".equals(operationType)){
            Toast.makeText(DownTaskDetailActivity.this,"请选择操作类型",Toast.LENGTH_SHORT).show();
            return;
        }
        opinion=etOpinion.getText().toString();
        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.showWithStatus("任务处理中，请稍等...");
        NetManager.handleTask(sessionId, handlingRecordUuid_upload,operationType, opinion, subGridsUuids, transferGridsUuids,
                startTime, endTime, files,distributionUserUuid, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mSVProgressHUD.dismiss();
                        Log.i("aaa","处理失败"+e.getMessage());
                        Toast.makeText(DownTaskDetailActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSVProgressHUD.dismiss();
                        Log.i("aaa","处理成功"+response.toString());
                        try {
                            JSONObject jsonObj=new JSONObject(response.toString());
                            if(0==jsonObj.optInt("status",-1)){
                                Toast.makeText(DownTaskDetailActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new MessageEvent("success"));
                                finish();
                            }else {
                                Toast.makeText(DownTaskDetailActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DownTaskDetailActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getBrotherGrids(){
        NetManager.getAimGrids(sessionId, "2", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa","转办"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa","转办"+response);
               ZhuanbanBean mZhuanbanBean=new Gson().fromJson(response.toString(),ZhuanbanBean.class);
                listZhuanban=mZhuanbanBean.getData();
            }
        });
    }

    private void getDownGrids(){
        NetManager.getAimGrids(sessionId, "1", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa","下发"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa","下发"+response);
                XiafaBean xiafaBean=new Gson().fromJson(response.toString(),XiafaBean.class);
                listXiafa=xiafaBean.getData();
            }
        });
    }

}
