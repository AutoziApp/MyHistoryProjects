package com.mapuni.shangluo.activity.wdAc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.AttachmentBean;
import com.mapuni.shangluo.divider.DividerItemDecoration;
import com.mapuni.shangluo.itemFactory.KnowledgeAttachmentRecyclerItemFactory;
import com.mapuni.shangluo.manager.MessageEvent;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.EventManager;
import com.mapuni.shangluo.utils.ToastUtil;
import com.mapuni.shangluo.view.imagePicker.ImagePickerAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
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

public class KnowledgeUpdateActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

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
    @BindView(R.id.tv_noAttachInfo)
    TextView tvNoAttachInfo;
    @BindView(R.id.rv_attachmentlist)
    RecyclerView rvAttachmentlist;
    @BindView(R.id.ll_knowledge_attachment_container)
    LinearLayout llKnowledgeAttachmentContainer;
    @BindView(R.id.ll_picContainer)
    LinearLayout llPicContainer;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数
    ArrayList<ImageItem> images = null;

    private String sessionId = "";
    private String knowledgeUuid = "";
    private String title = "";
    private String content = "";


    private AssemblyRecyclerAdapter mAdapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源
    private String mTitle;
    private String mContent;
    Map<String, File> files=new HashMap<>();//图片文件

    @Override
    public int setLayoutResID() {
        return R.layout.activity_knowledge_update;
    }

    @Override
    public void initView() {
        setToolbarTitle("知识库更新");
        initWidget();//初始化图片选择控件
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        sessionId = bundle.getString("sessionId");
        knowledgeUuid = bundle.getString("knowledgeUuid");
        title = bundle.getString("title");
        content = bundle.getString("content");
        boolean role = intent.getBooleanExtra("role", true);
        if (!role){//role true管理员 false非管理员
            mTvKnowTitle.setEnabled(false);
            mEtContent.setEnabled(false);
            llPicContainer.setVisibility(View.GONE);
            mBtnUpload.setVisibility(View.GONE);
        }
        mTvKnowTitle.setText(title);
        mEtContent.setText(content);
        initRecycleView(role);//初始化recycleView
        requestAttachmentList();//请求附件列表
    }


    private void initRecycleView(boolean role) {

        rvAttachmentlist.setHasFixedSize(true);
        rvAttachmentlist.setLayoutManager(new LinearLayoutManager(mContext));
        rvAttachmentlist.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AssemblyRecyclerAdapter(dataList);
        mAdapter.addItemFactory(new KnowledgeAttachmentRecyclerItemFactory(mContext,role));
        rvAttachmentlist.setAdapter(mAdapter);
    }

    private void requestAttachmentList() {
        if (!TextUtils.isEmpty(sessionId)) {
            loadData();
        } else {
            Toast.makeText(mContext, "暂无附件信息...", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadData() {
        NetManager.requestKnowledgeAttachmentList(sessionId, knowledgeUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    AttachmentBean attachmentBean = gson.fromJson(response, AttachmentBean.class);
                    List<AttachmentBean.RowsBean> list = attachmentBean.getRows();
                    if (list != null && list.size() > 0) {
                        showRecycleView();
                        mAdapter.setDataList(list);
                    } else {
                        hideRecycleView();//隐藏Recycle,显示TextView
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void showRecycleView() {
        if (!llKnowledgeAttachmentContainer.isShown()){
            llKnowledgeAttachmentContainer.setVisibility(View.VISIBLE);
        }
        if (tvNoAttachInfo.isShown()){
            tvNoAttachInfo.setVisibility(View.GONE);
        }
    }

    private void hideRecycleView() {
        if (llKnowledgeAttachmentContainer.isShown()){
            llKnowledgeAttachmentContainer.setVisibility(View.GONE);
        }
        if (!tvNoAttachInfo.isShown()){
            tvNoAttachInfo.setVisibility(View.VISIBLE);
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        String message = event.getMessage();
        if (message.equals("KnowledgedeleteSuccess")){
            loadData();
        }
    }

    @OnClick(R.id.btn_upload)
    public void onViewClicked() {
        mTitle = mTvKnowTitle.getText().toString().trim();
        mContent = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(mTitle)){
            ToastUtil.showShort(mContext,"标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(mContent)){
            ToastUtil.showShort(mContext,"内容不能为空");
            return;
        }
        //附件信息
        for (ImageItem item:selImageList){//添加图片
            String[] fileName = item.path.split("/");
            files.put(fileName[fileName.length-1],new File(item.path));
        }

        mSvProgressHUD.showWithStatus("正在更新，请稍等...");
        NetManager.updateKnowledgeTask(sessionId, knowledgeUuid, mTitle, mContent,files, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Toast.makeText(mContext,"更新失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                mSvProgressHUD.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    if(200==obj.optInt("status")){
                        ToastUtil.showShort(mContext,"更新成功！");
                        EventManager.getDefault().post(new MessageEvent("KnowledgeupdateSuccess"));
                        finish();
                    }else {
                        ToastUtil.showShort(mContext,"更新失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mContext,"更新失败");
                }
            }
        });
    }
}
