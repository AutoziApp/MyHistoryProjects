package com.mapuni.shangluo.activity.xcAc;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.PatrolObject;
import com.mapuni.shangluo.bean.ProblemType;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.FileUtils;
import com.mapuni.shangluo.utils.SPUtils;
import com.mapuni.shangluo.utils.TimeUtil;
import com.mapuni.shangluo.view.imagePicker.ImagePickerAdapter;
import com.mapuni.shangluo.view.myDialog.MenuData;
import com.mapuni.shangluo.view.myDialog.ThreeMenuDialog;
import com.mapuni.shangluo.view.treeView.MyNodeViewFactory;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;
import okhttp3.Call;

public class XcsbActivity extends BaseActivity implements View.OnClickListener,ImagePickerAdapter.OnRecyclerViewItemClickListener{
    TextView tvLatlog, tvProblemType;
    EditText etAddress, etDesicripsion;
    Button btnUpload;
    ImageView ivLocation;
    TextView spPotrolObject;

    //上传接口的参数
    String sessionId;
    String supervisionObjectUuid="";
    String problemUuid="";
    String longitude="";
    String latitude="";
    String address="";
    String description="";
    Map<String, File> files=new HashMap<>();//图片文件

    List<PatrolObject> mPatrolObjects = new ArrayList<>();
    List<ProblemType> mProblemTypes = new ArrayList<>();
    AMapLocationClient aMapLocationClient;
    AMapLocationClientOption aMapLocationClientOption;

    //图片选择
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
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
    @Override
    public int setLayoutResID() {
        return R.layout.activity_xcsb;
    }

    @Override
    public void initView() {
        setToolbarTitle("巡查上报");
        tvLatlog = (TextView) findViewById(R.id.tv_latlog);
        ivLocation = (ImageView) findViewById(R.id.dingwei_bu);
        etAddress = (EditText) findViewById(R.id.mbwz_eit);
        etDesicripsion = (EditText) findViewById(R.id.et_descripsion);
        spPotrolObject = (TextView) findViewById(R.id.sp_potrolobject);
        tvProblemType = (TextView) findViewById(R.id.tv_problemtype);
        btnUpload= (Button) findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(this);
        tvProblemType.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
//        spPotrolObject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(0!=position)
//                supervisionObjectUuid=mPatrolObjects.get(position-1).getUuid();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        List<String> list = new ArrayList<String>();
//        list.add("无");
//        initSpinner(spPotrolObject, list);
        spPotrolObject.setText("无");
        spPotrolObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreeMenuDialog dialog=new ThreeMenuDialog(XcsbActivity.this,sessionId);
                dialog.setonItemClickListener(new ThreeMenuDialog.MenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(MenuData menuData) {
                        if (menuData!=null)
                            spPotrolObject.setText(menuData.name); //选中第三个菜单后，主页面的name设置为选中的name
                        supervisionObjectUuid=menuData.uuid;
                    }
                });
                dialog.show();
            }
        });
        initAudioVideo();
        initWidget();//初始化图片选择控件
        initGDLocation();
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
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent1 = new Intent(XcsbActivity.this, ImageGridActivity.class);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dingwei_bu:
                aMapLocationClient.startLocation();
                break;
            case R.id.tv_problemtype:
                showProblemTypeDialog(mProblemTypes);
                break;
            case R.id.btn_upload:
                upload();
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
//                DialogUtil.dissmissDialog();
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, RECORD_AUDIO);
                }else{
                    Toast.makeText(XcsbActivity.this,"您的手机没有安装录音程序",Toast.LENGTH_SHORT).show();
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
        }

    }

    private void upload() {

        description=etDesicripsion.getText().toString();
        if(selImageList.size()==0&&uriAudio==null&&uriVideo==null){
            Toast.makeText(XcsbActivity.this,"附件信息不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //附件信息
        for (ImageItem item:selImageList){//添加图片
            String[] fileName = item.path.split("/");
            files.put(fileName[fileName.length-1],new File(item.path));
        }

        if (uriAudio != null) {//添加录音
            String audioPath= FileUtils.getPath(XcsbActivity.this, uriAudio);
            String audioName = audioPath.substring(audioPath.lastIndexOf("/") + 1, audioPath.length());
            files.put(audioName,new File(audioPath));
        }
        if (uriVideo != null) {//添加视频
            String videoPath= FileUtils.getPath(XcsbActivity.this, uriVideo);
            String videoName = videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.length());
            files.put(videoName,new File(videoPath));
        }

        //上报前的判空操作
        if(TextUtils.isEmpty(sessionId)){
            Toast.makeText(XcsbActivity.this,"登录信息失效，请重新登录",Toast.LENGTH_SHORT).show();
            return;
        }
//        if(TextUtils.isEmpty(supervisionObjectUuid)){
//            Toast.makeText(XcsbActivity.this,"监管对象不能为空",Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(TextUtils.isEmpty(problemUuid)){
            Toast.makeText(XcsbActivity.this,"问题类型不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(longitude)||TextUtils.isEmpty(longitude)||TextUtils.isEmpty(address)){
            Toast.makeText(XcsbActivity.this,"请重新获取位置信息",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(XcsbActivity.this,"问题描述不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.showWithStatus("正在上报任务，请稍等...");
        NetManager.uploadPotrol(sessionId, supervisionObjectUuid, problemUuid, longitude, latitude, address, description, files, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("bbb",e.getMessage());
                mSVProgressHUD.dismiss();
                Toast.makeText(XcsbActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("bbb",response.toString());
                mSVProgressHUD.dismiss();
                try {
                    JSONObject jsonObj=new JSONObject(response.toString());
                    if(200==jsonObj.optInt("status",0)){
                        Toast.makeText(XcsbActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(XcsbActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(XcsbActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(XcsbActivity.this, "sessionId", "");
//        getPotrolObject();
        getProblemType();
    }

    /**
     * 获取监管对象
     */
//    private void getPotrolObject() {
//        NetManager.requestPotrolObject(sessionId, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                Log.i("aaa", "获取监管对象失败" + e.getMessage());
//                Toast.makeText(XcsbActivity.this, "获取监管对象失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                Log.i("aaa", "获取监管对象成功" + response.toString());
//                Gson gson = new Gson();
//                Type type = new TypeToken<ArrayList<PatrolObject>>() {
//                }.getType();
//                mPatrolObjects = gson.fromJson(response.toString(), type);
//                List<String> list = new ArrayList<String>();
//                list.add("无");
//                if (mPatrolObjects.size() > 0) {
//                    for (PatrolObject object : mPatrolObjects) {
//                        list.add(object.getName());
//                    }
////                    supervisionObjectUuid=mPatrolObjects.get(0).getUuid();
//                }
//
//                initSpinner(spPotrolObject, list);
//
//            }
//        });
//    }

    /**
     * 获取问题类别
     */
    private void getProblemType() {
        NetManager.requestProblemTypes(sessionId, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa", "获取问题类别失败" + e.getMessage());
                Toast.makeText(XcsbActivity.this, "获取问题类别失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa", "获取问题类别成功" + response.toString());
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<ProblemType>>() {
                }.getType();
                mProblemTypes = gson.fromJson(response.toString(), type);
                if (mProblemTypes.size() > 0)
                    tvProblemType.setText(mProblemTypes.get(0).getName());
                    problemUuid=mProblemTypes.get(0).getUuid();
            }
        });
    }

    private void initGDLocation() {
        aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度定位
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        aMapLocationClientOption.setGpsFirst(true);//设置gps优先，只在高精度模式下有用
        aMapLocationClientOption.setHttpTimeOut(30000);
        aMapLocationClientOption.setNeedAddress(true);
        aMapLocationClientOption.setOnceLocation(true);
        aMapLocationClientOption.setOnceLocationLatest(true);
        aMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    tvLatlog.setText(aMapLocation.getLatitude() + "," + aMapLocation.getLongitude());
                    etAddress.setText(aMapLocation.getAddress());
                    longitude= aMapLocation.getLongitude()+"";
                    latitude= aMapLocation.getLatitude()+"";
                    address=aMapLocation.getAddress()+"";
                } else {
                    Log.i("aaa", "errCode:" + aMapLocation.getErrorCode()
                            + ",errInfo:" + aMapLocation.getErrorInfo());
                }
                aMapLocationClient.stopLocation();
            }
        });
    }


    private void showProblemTypeDialog(List<ProblemType> list) {
        if (list.size() < 1)
            return;//假如没请求到数据，直接返回
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(XcsbActivity.this);
        final View dialogView = LayoutInflater.from(XcsbActivity.this)
                .inflate(R.layout.multiselect, null);

        ViewGroup viewGroup = (RelativeLayout) dialogView.findViewById(R.id.container);
        TreeNode root = TreeNode.root();

        for (ProblemType type : list) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", type.getName());
            map.put("code", type.getUuid());
            TreeNode treeNode = new TreeNode(map);
            treeNode.setLevel(0);
            if (type.getPatrolProblemItemDict() != null && type.getPatrolProblemItemDict().size() > 0)
                for (ProblemType.PatrolProblemItemDictBean bean : type.getPatrolProblemItemDict()) {
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("name", bean.getName());
                    map1.put("code", bean.getUuid());
                    TreeNode treeNode1 = new TreeNode(map1);
                    treeNode1.setLevel(1);
                    treeNode.addChild(treeNode1);
                }
            root.addChild(treeNode);
        }

        final TreeView treeView = new TreeView(root, this, new MyNodeViewFactory());
        View view = treeView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(view);

        customizeDialog.setTitle("请选择问题类型");
        customizeDialog.setView(dialogView);
        customizeDialog.setNegativeButton("取消", null);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        List<TreeNode> selectedNodes = treeView.getSelectedNodes();
                        int typeCount = 0;
                        for (TreeNode node : selectedNodes) {
                            if (0 == node.getLevel()) {
                                typeCount++;
                            }
                        }
                        if (typeCount > 1) {
                            Toast.makeText(XcsbActivity.this, "只能选择一种任务类型", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if (selectedNodes.size() > 0) {
                                TreeNode treeNode = selectedNodes.get(0);
                                if (treeNode.getLevel() == 1) {//二级
                                    TreeNode parentTreeNode = treeNode.getParent();
                                    HashMap<String, String> map = (HashMap<String, String>) parentTreeNode.getValue();
//                                    Toast.makeText(XcsbActivity.this, map.get("name"), Toast.LENGTH_SHORT).show();
                                    tvProblemType.setText(map.get("name"));
                                    problemUuid=map.get("code");
                                } else {//一级
                                    HashMap<String, String> map = (HashMap<String, String>) treeNode.getValue();
//                                    Toast.makeText(XcsbActivity.this, map.get("name"), Toast.LENGTH_SHORT).show();
                                    tvProblemType.setText(map.get("name"));
                                    problemUuid=map.get("code");
                                }

                                //组装问题类别内容添加到任务描述
                                StringBuffer sb=new StringBuffer();
                                for(TreeNode node:selectedNodes){
                                    HashMap<String, String> map = (HashMap<String, String>) node.getValue();
                                    sb.append(map.get("name")+",");
                                }
                                sb.replace(sb.length()-1,sb.length(),"");
                                etDesicripsion.setText(sb.toString());
                            }
                        }

                    }
                });

        customizeDialog.show();
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
        }else if (requestCode == RECORD_AUDIO && resultCode == RESULT_OK) {

            uriAudio = data.getData();

            MediaPlayer player=MediaPlayer.create(XcsbActivity.this, uriAudio);
            int time=player.getDuration();
            audio_time.setText(TimeUtil.formatDuring(time));
            audio_info.setVisibility(View.VISIBLE);
        } else if (requestCode == RECORD_VIDEO && resultCode == RESULT_OK) {
            uriVideo = data.getData();
            MediaPlayer player=MediaPlayer.create(XcsbActivity.this, uriVideo);
            int time=player.getDuration();
            vedio_time.setText(TimeUtil.formatDuring(time));
            vedio_info.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationClient.onDestroy();
    }



}
