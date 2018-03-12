package com.mapuni.shangluo.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.bean.TaskGeneral;
import com.mapuni.shangluo.divider.DividerItemDecoration;
import com.mapuni.shangluo.itemFactory.TaskGeneralRecyclerItemFactory;
import com.mapuni.shangluo.manager.MessageEvent;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.Logger;
import com.mapuni.shangluo.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/11.
 */

public class AgencyFragment extends Fragment {
    XRefreshView xRefreshView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    private RecyclerView recyclerView;
    List<Object> dataList = new ArrayList<Object>();//数据源
    String sessionId;
    String rows="10";
    int page=1;
    private boolean isLoadMore = false;
    private TextView mTvNoData;

    public static AgencyFragment newInstance(String s) {
        AgencyFragment fragment = new AgencyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private Context context;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        this.context=getActivity();
        sessionId = (String) SPUtils.getSp(getActivity(), "sessionId", "");
        View view = inflater.inflate(R.layout.fragment_agency, container, false);
        xRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mTvNoData = (TextView) view.findViewById(R.id.tv_noData);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new TaskGeneralRecyclerItemFactory(getActivity()));
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);//解决横向移动冲突
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
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
                xRefreshView.stopLoadMore(true);
            }
        });
            loadData();
        EventBus.getDefault().register(this);
         return view;
    }

    private void loadData() {

        NetManager.requestDbTask(sessionId,rows,page+"", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(AgencyFragment.class.getSimpleName(),e.toString());
                Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(AgencyFragment.class.getSimpleName(),response.toString());
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                Gson gson=new Gson();
                Type type = new TypeToken<ArrayList<TaskGeneral>>() {}.getType();
                List<TaskGeneral> list=gson.fromJson(response.toString(),type);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        String message = event.getMessage();
        if (message.equals("success")){
                isLoadMore = false;
                page = 1;
                loadData();
                xRefreshView.stopRefresh();
            }
    };
    

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);
    }
}
