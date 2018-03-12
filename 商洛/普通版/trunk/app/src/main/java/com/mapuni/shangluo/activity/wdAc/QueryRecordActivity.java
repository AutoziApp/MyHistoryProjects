package com.mapuni.shangluo.activity.wdAc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.QueryRecordBean;
import com.mapuni.shangluo.divider.DividerItemDecoration;
import com.mapuni.shangluo.itemFactory.TaskRecordRecyclerItemFactory;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.Logger;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * @author Tianfy
 * @time 2017/8/31  10:04
 * @describe 查询记录Activity
 */
public class QueryRecordActivity extends BaseActivity {
    XRefreshView xRefreshView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    private RecyclerView recyclerView;
    List<Object> dataList = new ArrayList<Object>();//数据源
    String sessionId;
    String rows = "10";
    int page = 1;
    private boolean isLoadMore = false;
    private TextView mTvNoData;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_query_record;
    }

    @Override
    public void initView() {
        setToolbarTitle("查询记录");

        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvNoData = (TextView) findViewById(R.id.tv_noData);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new TaskRecordRecyclerItemFactory(this));
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);//解决横向移动冲突
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {//下拉刷新
                super.onRefresh(isPullDown);
                isLoadMore = false;
                page = 1;
                loadData();
                xRefreshView.stopRefresh();
            }


            @Override
            public void onLoadMore(boolean isSilence) {//上拉加载
                super.onLoadMore(isSilence);
                isLoadMore = true;
                page++;
                loadData();
                xRefreshView.stopLoadMore();
            }
        });
    }

    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        loadData();
    }


    private void loadData() {
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.requestRecordTask(sessionId, rows, page + "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Logger.e(QueryRecordActivity.class.getSimpleName(),e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(QueryRecordActivity.class.getSimpleName(),response.toString());
                mSvProgressHUD.dismiss();
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                QueryRecordBean queryRecordBean = gson.fromJson(response.toString(), QueryRecordBean.class);
                List<QueryRecordBean.RowsBean> list = queryRecordBean.getRows();
                if (!isLoadMore){
                    if (list!=null&&list.size()>0){
                        adapter.setDataList(list);
                    }else{
                        adapter.setDataList(null);
                        mTvNoData.setVisibility(View.VISIBLE);
//                        xRefreshView.setVisibility(View.GONE);
                    }
                }else{
                    adapter.addAll(list);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventManager.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventManager.getDefault().unregister(this);
    }
}
