package com.mapuni.shangluo.activity.dbAc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.AttachmentBean;
import com.mapuni.shangluo.divider.DividerItemDecoration;
import com.mapuni.shangluo.itemFactory.AttachmentRecyclerItemFactory;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

import static com.mapuni.shangluo.R.id.recyclerView;

public class AttachmentActivity extends BaseActivity {


    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView mXrefreshview;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源
    private String mHandledRecordUuid;
    private String sessionId;
    @Override
    public int setLayoutResID() {
        return R.layout.activity_attachment_acitivty;
    }

    @Override
    public void initView() {
        setToolbarTitle("附件列表");
        ButterKnife.bind(this);
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new AttachmentRecyclerItemFactory(mContext));
        mRecyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        mXrefreshview.setPinnedTime(1000);
        mXrefreshview.setMoveForHorizontal(true);//解决横向移动冲突
        mXrefreshview.setPullLoadEnable(true);
        mXrefreshview.setAutoLoadMore(false);
        mXrefreshview.setPullRefreshEnable(false);
        mXrefreshview.enableReleaseToLoadMore(false);
        mXrefreshview.enableRecyclerViewPullUp(false);
        mXrefreshview.enablePullUpWhenLoadCompleted(true);
    }

    @Override
    public void initData() {

        mHandledRecordUuid = getIntent().getStringExtra("handledRecordUuid");
        if (mHandledRecordUuid!=null&&mHandledRecordUuid.length()>0){
            loadData();
        }else{
            Toast.makeText(mContext, "暂无附件信息...", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadData() {
        mSvProgressHUD.showWithStatus("正在加载...", SVProgressHUD.SVProgressHUDMaskType.Black);
        NetManager.requestAttachmentList(sessionId,mHandledRecordUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("111", response.toString());
                mSvProgressHUD.dismiss();
                AttachmentBean attachmentBean = gson.fromJson(response, AttachmentBean.class);
                List<AttachmentBean.RowsBean> list = attachmentBean.getRows();
                if (list != null && list.size() > 0) {
                    adapter.setDataList(list);
                    mXrefreshview.setLoadComplete(true);
                }
            }
        });
    }
}
