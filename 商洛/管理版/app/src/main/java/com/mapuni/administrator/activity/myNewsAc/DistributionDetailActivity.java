package com.mapuni.administrator.activity.myNewsAc;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.DistributionDetailBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.itemFactory.DistributionDetailHandleProcessRecyclerItemFactory;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.activity.myNewsAc
 * @class describe
 * @anthor tianfy
 * @time 2017/10/19 14:50
 * @change
 * @chang time
 * @class describe
 */

public class DistributionDetailActivity extends BaseActivity{
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源

    @Override
    public int setLayoutResID() {
        return R.layout.activity_distribution_detail;
    }

    @Override
    public void initView() {
        setToolbarTitle("任务处理详情");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new DistributionDetailHandleProcessRecyclerItemFactory(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String taskUuid = intent.getStringExtra("taskUuid");
        String taskType = intent.getStringExtra("taskType");
        requestDistributionDetailData(taskUuid,taskType);

    }

    private void requestDistributionDetailData(String taskUuid, String taskType) {
        mSvProgressHUD.showWithStatus("正在加载...");

        NetManager.requestDistributionDetail(taskUuid, taskType, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                mSvProgressHUD.dismiss();
                Logger.i(DistributionDetailActivity.class.getSimpleName(),response.toString());
                DistributionDetailBean distributionDetailBean = gson.fromJson(response.toString(), DistributionDetailBean.class);
                List<DistributionDetailBean.RowsBean> rows = distributionDetailBean.getRows();
                if (rows!=null&&rows.size()>0){
                    adapter.setDataList(rows);
                }
            }
        });
    }
}
