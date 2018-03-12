package com.mapuni.administrator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mapuni.administrator.R;
import com.mapuni.administrator.bean.AttachmentBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.itemFactory.PicAttachmentRecyclerItemFactory;
import com.mapuni.administrator.manager.MessageEvent;
import com.mapuni.administrator.manager.NetManager;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 * @name shangluoAdminstor
 * @class name：com.mapuni.administrator.fragment
 * @class describe
 * @anthor Administrator
 * @time 2017/12/21 9:20
 * @change
 * @chang time
 * @class describe
 */

public class PictureFragment extends Fragment{


    private static final int DATA_ISEMPTY=20171221;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    List<Object> dataList = new ArrayList<Object>();//数据源
    private String taskUuid;
    private Context mContext;
    public Gson gson;
    private RecyclerView mRecyclerView;
    private Handler mHandler;
    private String flag;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_pictrue, null);
        mContext=getActivity();
        gson = new Gson();
        initView(view);
        initData();
        EventBus.getDefault().register(this);
        return view;
    }
    public void setHandler(Handler mHandler){
        this.mHandler=mHandler;
    }


    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new PicAttachmentRecyclerItemFactory(mContext,getArguments().getString("flag")));
        mRecyclerView.setAdapter(adapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            taskUuid = bundle.getString("taskUuid");
            flag = bundle.getString("flag");
            if (taskUuid !=null&& taskUuid.length()>0){
                loadData();
            }else{
                Toast.makeText(getActivity(), "暂无附件信息...", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void loadData() {
        NetManager.requestPicAttachmentList(taskUuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("111", response.toString());
                try {
                    AttachmentBean attachmentBean = gson.fromJson(response, AttachmentBean.class);
                    List<AttachmentBean.RowsBean> list = attachmentBean.getRows();
                    if (list != null && list.size() > 0) {
                        adapter.setDataList(list);
                    }else{
                        mHandler.sendEmptyMessage(DATA_ISEMPTY);
                    }
                }catch (Exception e){

                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        String message = event.getMessage();
        if (message.equals("deleteSuccess")){
            loadData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


}
