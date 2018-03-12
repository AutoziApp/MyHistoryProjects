package com.mapuni.shangluo.activity.wdAc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.bean.MessageListBean;
import com.mapuni.shangluo.divider.DividerItemDecoration;
import com.mapuni.shangluo.itemFactory.MessageListRecyclerItemFactory;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.Logger;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

public class MessageActivity extends BaseActivity {
    XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源
    String sessionId;
    String rows = "10";
    int page = 1;
    private boolean isLoadMore = false;
    private TextView mTvNoData;


    @Override
    public int setLayoutResID() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        setToolbarTitle("我的消息");
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvNoData = (TextView) findViewById(R.id.tv_noData);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new MessageListRecyclerItemFactory(mContext));
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
                initData();
                xRefreshView.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {//上拉加载
                super.onLoadMore(isSilence);
                isLoadMore = true;
                page++;
                initData();
                xRefreshView.stopLoadMore();
            }
        });
    }

    @Override
    public void initData() {
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.requestMessageList(sessionId, rows, page + "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Logger.e(MessageActivity.class.getSimpleName(),e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(MessageActivity.class.getSimpleName(), response.toString());
                mSvProgressHUD.dismiss();
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                
                MessageListBean messageListBean = gson.fromJson(response, MessageListBean.class);
                List<MessageListBean.RowsBean> list = messageListBean.getRows();
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
}
