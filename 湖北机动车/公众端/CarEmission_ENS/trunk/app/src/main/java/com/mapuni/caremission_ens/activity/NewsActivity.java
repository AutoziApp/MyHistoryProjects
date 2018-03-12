package com.mapuni.caremission_ens.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mapuni.caremission_ens.R;
import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.caremission_ens.adapter.assemblyadapter.OnRecyclerLoadMoreListener;
import com.mapuni.caremission_ens.bean.NewsBean;
import com.mapuni.caremission_ens.itemfactory.LoadMoreRecyclerItemFactory;
import com.mapuni.caremission_ens.itemfactory.NewsItemFactory;
import com.mapuni.caremission_ens.presenter.NetControl;
import com.mapuni.caremission_ens.utils.DateUtils;
import com.mapuni.caremission_ens.utils.JsonUtil;
import com.mapuni.caremission_ens.utils.SharepreferenceUtil;
import com.mapuni.caremission_ens.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class NewsActivity extends BaseActivity implements OnRecyclerLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    NetControl netControl;
    int currentPage = 1;
    ArrayList<NewsBean> list;
    AssemblyRecyclerAdapter adapter;
    YutuLoading yutuLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setTitle("信息", true, false);
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        netControl = new NetControl();
        requestNewsContent();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftIcon:
                finish();
                break;
            case R.id.showFailed:
                yutuLoading.showDialog();
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                      requestNewsContent();
                    }
                },2000);
                requestAgain();
                break;
        }
    }
    private void initAdapter(ArrayList list) {
        this.list = new ArrayList<>(list);
//        this.list.addAll(list);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssemblyRecyclerAdapter(this.list);
        adapter.addItemFactory(new NewsItemFactory(this));
        adapter.setLoadMoreItem(new LoadMoreRecyclerItemFactory(this));
        recycle.setAdapter(adapter);

    }

    private void refreshAdapter(ArrayList list){
        if(list.size()<=0){
            setLoadItem(list.size());
            return;
        }
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
        setLoadItem(list.size());
    }
    private StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
            Toast.makeText(NewsActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            Map map = JsonUtil.jsonToMap(response);
            if(map!=null){
                double i = (double) map.get("flag");
                if(map!=null&&i==1){
                    Object obj = map.get("data");
                    String result = JsonUtil.objectToJson(obj);
                    ArrayList list = JsonUtil.fromJsonList(result, NewsBean.class);
                    SharepreferenceUtil.setUpdateTime(NewsActivity.this, DateUtils.getNowDate());
                    if(adapter==null){
                        initAdapter(list);
                    }
                    else{
                        refreshAdapter(list);
                    }
                }else{
                    showFailed();
                }
            }else{
                showFailed();
            }
        }
    };
    private void setLoadItem(int size){
            boolean loadMoreEnd = size < 10;
            adapter.loadMoreFinished(loadMoreEnd);
    }
    private void requestNewsContent(){
        netControl.allNews(currentPage, call);
        currentPage ++;
    }
    @Override
    public void onLoadMore(AssemblyRecyclerAdapter adapter) {
        requestNewsContent();
    }
}
