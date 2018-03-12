package com.mapuni.administrator.activity.myNewsAc;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.TaskType2;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TimePickerUtil;
import com.mapuni.administrator.utils.TimeUtil;
import com.mapuni.administrator.view.imagePicker.ImagePickerAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class AddTaskActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.et_taskName)
    EditText mEtTaskName;
    @BindView(R.id.sp_taskType)
    Spinner mSpTaskType;
    @BindView(R.id.tv_startTime)
    TextView mTvStartTime;
    @BindView(R.id.tv_endTime)
    TextView mTvEndTime;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    @BindView(R.id.btn_sure)
    Button mBtnSure;

    private Gson mGson;
    private String mSessionId;
    private TimePickerUtil mTimePickerUtil;

    List<TaskType2> taskTypes = new ArrayList<>();
    //请求参数
    String name="";
    String taskType="";
    String startTime="";
    String endTime="";
    String description="";
    String status="";
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    private ImagePickerAdapter adapter;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    ArrayList<ImageItem> images = null;
    Map<String, File> files=new HashMap<>();//图片文件

    @Override
    public int setLayoutResID() {
        return R.layout.activity_add_task;
    }

    @Override
    public void initView() {
        setToolbarTitle("添加任务");
        mTvStartTime.setText(TimeUtil.getStrToday());
        mTvEndTime.setText(TimeUtil.getStrToday());
        mSpTaskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskType=taskTypes.get(position).getUuid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initWidget();//初始化图片选择控件
    }
    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        mGson = new Gson();
        mSessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        mTimePickerUtil = new TimePickerUtil(mContext);
        getTaskType();//获取任务类型
    }



    @OnClick({R.id.tv_startTime, R.id.tv_endTime, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_startTime:
                mTimePickerUtil.showTimePicker(mTvStartTime);
                break;
            case R.id.tv_endTime:
                mTimePickerUtil.showTimePicker(mTvEndTime);
                break;
            case R.id.btn_sure:
                addTask();
                break;
        }
    }

    private boolean compareTime() {
        long startTimeL = TimeUtil.convert2long(mTvStartTime.getText().toString(), TimeUtil.DATE_FORMAT);
        long endTimeL = TimeUtil.convert2long(mTvEndTime.getText().toString(), TimeUtil.DATE_FORMAT);

        if (startTimeL >= endTimeL) {
            Toast.makeText(mContext, "结束时间应大于开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    private void addTask() {
        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        name=mEtTaskName.getText().toString();
        startTime=mTvStartTime.getText().toString();
        endTime=mTvEndTime.getText().toString();
        description=mEtDescription.getText().toString();
        //附件信息
        for (ImageItem item:selImageList){//添加图片
            String[] fileName = item.path.split("/");
            files.put(fileName[fileName.length-1],new File(item.path));
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"任务名称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
      if(TextUtils.isEmpty(taskType)){
            Toast.makeText(this,"任务类型不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(false==compareTime()){
            return;
        }

      if(TextUtils.isEmpty(description)){
            Toast.makeText(this,"任务描述不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String roleId=(String)SPUtils.getSp(this,"roleId","");
        if(roleId.contains("3")){//假如是审核员
            status="2";
        }else {
            status="1";
        }
        mSVProgressHUD.showWithStatus("正在添加任务，请稍等...");
        NetManager.createTask(mSessionId, name, taskType, startTime, endTime, description,status,files, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSVProgressHUD.dismiss();
                Toast.makeText(AddTaskActivity.this,"任务添加失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                mSVProgressHUD.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    if(0==obj.optInt("status")){
                        Toast.makeText(AddTaskActivity.this,"任务添加成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddTaskActivity.this,"任务添加失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddTaskActivity.this,"任务添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void getTaskType() {
        NetManager.requestTaskType(mSessionId, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

                Type type = new TypeToken<ArrayList<TaskType2>>() {}.getType();
                taskTypes=mGson.fromJson(response,type);
                List<String> list = new ArrayList<String>();
                if (taskTypes.size() > 0) {
                    for (TaskType2 object : taskTypes) {
                        list.add(object.getName());
                    }
                    taskType=taskTypes.get(0).getUuid();
                }
                Log.i("sss",list.toString());
                initSpinner(mSpTaskType, list);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent1 = new Intent(AddTaskActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
