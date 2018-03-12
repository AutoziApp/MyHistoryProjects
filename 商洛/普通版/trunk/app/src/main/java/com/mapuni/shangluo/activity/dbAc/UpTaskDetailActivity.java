package com.mapuni.shangluo.activity.dbAc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.FenpeiBean;
import com.mapuni.shangluo.bean.HandleProcess;
import com.mapuni.shangluo.bean.SubGrids;
import com.mapuni.shangluo.bean.UploadTaskDetail;
import com.mapuni.shangluo.bean.ZhuanbanBean;
import com.mapuni.shangluo.divider.DividerItemDecoration;
import com.mapuni.shangluo.itemFactory.HandleProcessRecyclerItemFactory;
import com.mapuni.shangluo.manager.MessageEvent;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.FileUtils;
import com.mapuni.shangluo.utils.SPUtils;
import com.mapuni.shangluo.utils.TimeUtil;
import com.mapuni.shangluo.view.RadioGroupEx;
import com.mapuni.shangluo.view.imagePicker.ImagePickerAdapter;
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

public class UpTaskDetailActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener,HandleProcessRecyclerItemFactory.EventListener {
    private TextView tvObject,tvObjectType,tvGridName,tvOperator,tvPhone,tvEventNo,tvProblemType,
                     tvDescription,tvLog,tvLat,tvCreatTime,tvSelectStartTime,tvSelectEndTime,tvZhuanBan,tvAddress;
    private RadioButton rbPass,rbNoPass,rbUpload,rbZhuanban,rbBack,rbFinish,rbApplyTime,rbXiaFa;
    private RadioGroupEx radioGroup;
    private EditText etOpinion;
    private Button btnHandle;
    private LinearLayout distributionLayout,applyTimeLayout,attachLayout,zhuanBanLayout,xiafaLayout;
    private Spinner spDistribution,spXiaFa;
    List<ZhuanbanBean.DataBean> listZhuanban;
    private RadioButton rbDistribution,rbEnd;

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

    private List<FenpeiBean.RowsBean> mFenpeiList;
    private List<SubGrids.DataBean> mSubList;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_up_task_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle("上报任务详情");
        handlingRecordUuid_upload=getIntent().getStringExtra("handlingRecordUuid");
        initBaseXX();//初始化基本信息
        initProcess();//初始化任务流程
        initWidget();//初始化图片选择
        initAudioVideo();//初始化录音视频
    }

    private void initProcess() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new HandleProcessRecyclerItemFactory(this));
        recyclerView.setAdapter(adapter);
    }

    private void initBaseXX() {
        tvZhuanBan= (TextView) findViewById(R.id.tv_zhuanBan);
        zhuanBanLayout= (LinearLayout) findViewById(R.id.ll_zhuanBan);
        attachLayout= (LinearLayout) findViewById(R.id.ll_attach);
        tvSelectStartTime= (TextView) findViewById(R.id.tv_selectStartTime);
        tvSelectEndTime= (TextView) findViewById(R.id.tv_selectEndTime);
        applyTimeLayout= (LinearLayout) findViewById(R.id.ll_applyTime);
        tvObject= (TextView) findViewById(R.id.tv_patrolObject);
        tvObjectType= (TextView) findViewById(R.id.tv_objectType);
        tvGridName= (TextView) findViewById(R.id.tv_grid);
        tvOperator= (TextView) findViewById(R.id.tv_operator);
        tvPhone= (TextView) findViewById(R.id.tv_phone);
        tvEventNo= (TextView) findViewById(R.id.tv_eventNo);
        tvProblemType= (TextView) findViewById(R.id.tv_problemType);
        tvDescription= (TextView) findViewById(R.id.tv_description);
        tvLog= (TextView) findViewById(R.id.tv_longitude);
        tvLat= (TextView) findViewById(R.id.tv_latitude);
        tvAddress= (TextView) findViewById(R.id.tv_address);
        tvCreatTime= (TextView) findViewById(R.id.tv_creattime);
        radioGroup= (RadioGroupEx) findViewById(R.id.radioGroupID);
        rbPass= (RadioButton) findViewById(R.id.rb_pass);
        rbNoPass= (RadioButton) findViewById(R.id.rb_no_pass);
        rbUpload= (RadioButton) findViewById(R.id.rb_upload);
        rbZhuanban = (RadioButton) findViewById(R.id.rb_zhuanban);
        rbXiaFa= (RadioButton) findViewById(R.id.rb_down);
        rbBack= (RadioButton) findViewById(R.id.rb_back);
        rbFinish= (RadioButton) findViewById(R.id.rb_finish);
        rbApplyTime= (RadioButton) findViewById(R.id.rb_applyTime);
        rbDistribution = (RadioButton) findViewById(R.id.rb_distribution);
        rbEnd = (RadioButton) findViewById(R.id.rb_end);

        etOpinion= (EditText) findViewById(R.id.et_opinion);
        btnHandle= (Button) findViewById(R.id.btn_handle);
        spDistribution= (Spinner) findViewById(R.id.sp_distribution);
        distributionLayout= (LinearLayout) findViewById(R.id.ll_distribution);
        spXiaFa= (Spinner) findViewById(R.id.sp_xiafa);
        xiafaLayout= (LinearLayout) findViewById(R.id.ll_xiafa);

        tvZhuanBan.setOnClickListener(this);
        btnHandle.setOnClickListener(this);
        tvSelectStartTime.setOnClickListener(this);
        tvSelectEndTime.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_pass://通过
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_no_pass://不通过
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_upload://上报
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_zhuanban://转办
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.VISIBLE);
                        attachLayout.setVisibility(View.VISIBLE);
                        applyTimeLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_back://回退
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_finish://办结
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_end://办结
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.GONE);
                        etOpinion.setHint("请输入处理理由");
                        break;
                    case R.id.rb_applyTime://延时申请
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        applyTimeLayout.setVisibility(View.VISIBLE);
                        etOpinion.setHint("请输入申请理由");
                        break;
                    case R.id.rb_distribution://分配
                        xiafaLayout.setVisibility(View.GONE);
                        zhuanBanLayout.setVisibility(View.GONE);
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
                    case R.id.rb_down://下发
                        zhuanBanLayout.setVisibility(View.GONE);
                        attachLayout.setVisibility(View.VISIBLE);
                        applyTimeLayout.setVisibility(View.GONE);
                        distributionLayout.setVisibility(View.GONE);
                        xiafaLayout.setVisibility(View.VISIBLE);
                        etOpinion.setHint("请输入下发意见");
                        if(mSubList!=null && mSubList.size()>0){
                            List<String> list=new ArrayList<String>();
                            for (SubGrids.DataBean dataBean:mSubList){
                                list.add(dataBean.getName());
                            }
                            initSpinner(spXiaFa,list);
                            subGridsUuids=mSubList.get(0).getUuid();
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

        spXiaFa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subGridsUuids=mSubList.get(position).getUuid();
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
                Intent intent1 = new Intent(UpTaskDetailActivity.this, ImageGridActivity.class);
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
        sessionId = (String) SPUtils.getSp(UpTaskDetailActivity.this, "sessionId", "");
        requestBaseData();//请求基本数据
        requestHandleProgressData();//请求处理流程信息
        getAimGrids();//获取转办单位
        getDistribution();//获取分配单位人员
//        getSubGrids();//获取下发单位
    }
    private void getSubGrids(String taskUuid) {
//        String taskUuid=getIntent().getStringExtra("handlingRecordUuid");
        NetManager.getGridsByCondition(sessionId, taskUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.i(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.i("下发"+response);
                SubGrids subGrids = gson.fromJson(response.toString(), SubGrids.class);
                mSubList = subGrids.getData();

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
                        Toast.makeText(UpTaskDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MessageEvent("success"));
                        finish();
                    }
                    @Override
                    public void onResponse(String response, int id) {
//                        mSVProgressHUD.dismiss();
                        Gson gson=new Gson();
                        UploadTaskDetail uploadTaskDetail=gson.fromJson(response.toString(),UploadTaskDetail.class);
                        if(uploadTaskDetail.getStatus()==0){//请求数据成功

                            initBaseData(uploadTaskDetail.getData());
                            getSubGrids(uploadTaskDetail.getData().getUuid());
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
                        Toast.makeText(UpTaskDetailActivity.this,"请求数据失败",Toast.LENGTH_SHORT).show();
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
    private void initBaseData(UploadTaskDetail.DataBean data1) {
        tvObject.setText(data1.getSupervisionObjectName()!=null?data1.getSupervisionObjectName():"");
        tvObjectType.setText(data1.getSupervisionObjectTypeName()!=null?data1.getSupervisionObjectTypeName():"");
        tvGridName.setText(data1.getGridName()!=null?data1.getGridName():"");
        tvOperator.setText(data1.getUserRealname()!=null?data1.getUserRealname():"");
        tvPhone.setText(data1.getTelephone());
        tvEventNo.setText(data1.getNumber()!=null?data1.getNumber():"");
        tvProblemType.setText(data1.getPatrolProblemType()!=null?data1.getPatrolProblemType():"");
        tvDescription.setText(data1.getDescription()!=null?data1.getDescription():"");
        tvLog.setText(data1.getLongitude()!=null?data1.getLongitude():"");
        tvLat.setText(data1.getLatitude()!=null?data1.getLatitude():"");
        tvAddress.setText(data1.getLatitude()!=null?data1.getAddress():"");
        tvCreatTime.setText(data1.getCreateTime()!=null?data1.getCreateTime():"");

        UploadTaskDetail.DataBean.JurisdictionJudgeBean bean=data1.getJurisdictionJudge();
        if(1==bean.getJudgeAudited()) rbPass.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeAuditnotthrough()) rbNoPass.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeReport()) rbUpload.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeTurn()) rbZhuanban.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeReturn()) rbBack.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeComplete()) rbFinish.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeStop()) rbEnd.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeDelayapply()) rbApplyTime.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeDistribution()) rbDistribution.setVisibility(View.VISIBLE);
        if(1==bean.getJudgeIssued()) rbXiaFa.setVisibility(View.VISIBLE);
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

            MediaPlayer player=MediaPlayer.create(UpTaskDetailActivity.this, uriAudio);
            int time=player.getDuration();
            audio_time.setText(TimeUtil.formatDuring(time));
            audio_info.setVisibility(View.VISIBLE);
        } else if (requestCode == RECORD_VIDEO && resultCode == RESULT_OK) {
            uriVideo = data.getData();
            MediaPlayer player=MediaPlayer.create(UpTaskDetailActivity.this, uriVideo);
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
                    Toast.makeText(UpTaskDetailActivity.this,"您的手机没有安装录音程序",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UpTaskDetailActivity.this,"任务已被他人处理",Toast.LENGTH_SHORT).show();
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
        builder.setTitle("请选择转办单位");
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
            String audioPath= FileUtils.getPath(UpTaskDetailActivity.this, uriAudio);
            String audioName = audioPath.substring(audioPath.lastIndexOf("/") + 1, audioPath.length());
            files.put(audioName,new File(audioPath));
        }
        if (uriVideo != null) {//添加视频
            String videoPath= FileUtils.getPath(UpTaskDetailActivity.this, uriVideo);
            String videoName = videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.length());
            files.put(videoName,new File(videoPath));
        }
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rb_down://下发
                operationType="2";
                break;
            case R.id.rb_pass://通过
                operationType="9";
                break;
            case R.id.rb_no_pass://不通过
                operationType="13";
                break;
            case R.id.rb_upload://上报
                operationType="1";
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
            case R.id.rb_end://终结
                operationType="6";
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
            Toast.makeText(UpTaskDetailActivity.this,"请选择操作类型",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpTaskDetailActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mSVProgressHUD.dismiss();
                        Log.i("aaa","处理成功"+response.toString());
                        try {
                            JSONObject jsonObj=new JSONObject(response.toString());
                            if(0==jsonObj.optInt("status",-1)){
                                Toast.makeText(UpTaskDetailActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new MessageEvent("success"));
                                finish();
                            }else {
                                Toast.makeText(UpTaskDetailActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpTaskDetailActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getAimGrids(){
        NetManager.getAimGrids(sessionId, "2", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa","转办下发"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa","转办下发"+response);
               ZhuanbanBean mZhuanbanBean=new Gson().fromJson(response.toString(),ZhuanbanBean.class);
                listZhuanban=mZhuanbanBean.getData();
            }
        });
    }

    @Override
    public void onClickDetail(int position, HandleProcess data) {
        Toast.makeText(mContext,"你点击了第"+position+"条的附件信息",Toast.LENGTH_SHORT).show();
        baseStartActivity(mContext,AttachmentActivity.class);
    }
}
