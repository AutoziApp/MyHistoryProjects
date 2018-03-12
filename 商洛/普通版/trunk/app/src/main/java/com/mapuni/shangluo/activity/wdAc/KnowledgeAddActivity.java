package com.mapuni.shangluo.activity.wdAc;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.manager.MessageEvent;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.EventManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.mapuni.shangluo.view.imagePicker.ImagePickerAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.activity.wdAc
 * @class describe 知识库更新Activity
 * @anthor Administrator
 * @time 2018/1/19 10:34
 * @change
 * @chang time
 * @class describe
 */

public class KnowledgeAddActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    //图片选择
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    @BindView(R.id.tv_knowTitle)
    EditText mTvKnowTitle;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.btn_upload)
    Button mBtnUpload;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    ArrayList<ImageItem> images = null;

    private String sessionId = "";
    private String title = "";
    private String content = "";
    private Map<String, File> files=new HashMap<>();//图片文件


    @Override
    public int setLayoutResID() {
        return R.layout.activity_knowledge_add;
    }

    @Override
    public void initView() {
        setToolbarTitle("添加知识库");
        initWidget();//初始化图片选择控件
    }


    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
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
                Intent intent1 = new Intent(mContext, ImageGridActivity.class);
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

    @OnClick(R.id.btn_upload)
    public void onViewClicked() {
        upload();
    }

    private void upload() {
        title=mTvKnowTitle.getText().toString().trim();
        content=mEtContent.getText().toString().trim();
        //附件信息
        for (ImageItem item:selImageList){//添加图片
            String[] fileName = item.path.split("/");
            files.put(fileName[fileName.length-1],new File(item.path));
        }
        if(TextUtils.isEmpty(title)){
            Toast.makeText(KnowledgeAddActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(content)){
            Toast.makeText(KnowledgeAddActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        final SVProgressHUD mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.showWithStatus("正在添加知识库，请稍等...");
        NetManager.addKnowledge(sessionId, title, content, files, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSVProgressHUD.dismiss();
                Toast.makeText(KnowledgeAddActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                mSVProgressHUD.dismiss();
                try {
                    JSONObject jsonObj=new JSONObject(response.toString());
                    if(200==jsonObj.optInt("status",0)){
                        Toast.makeText(KnowledgeAddActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        EventManager.getDefault().post(new MessageEvent("KnowledgeupdateSuccess"));
                        finish();
                    }else {
                        Toast.makeText(KnowledgeAddActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(KnowledgeAddActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
