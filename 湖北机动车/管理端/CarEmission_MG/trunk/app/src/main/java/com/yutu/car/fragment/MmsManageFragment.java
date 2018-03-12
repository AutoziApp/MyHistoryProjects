package com.yutu.car.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.activity.DetailActivity;
import com.yutu.car.activity.InformationDisplayActivity;
import com.yutu.car.adapter.ListActivityAdapter;
import com.yutu.car.adapter.MmsListActivityAdapter;
import com.yutu.car.bean.AllStationBean;
import com.yutu.car.bean.CareManageBean;
import com.yutu.car.bean.MapBean;
import com.yutu.car.bean.MmsBean;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.MmsManageControl;
import com.yutu.car.presenter.StationInfoControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.LoadMoreItem;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by lenovo on 2017/4/11.
 */

public class MmsManageFragment extends BaseFragment implements ListView.OnItemClickListener{
    private View rootView;
    private ListView listView;
    private MmsBean bean;
    private MmsListActivityAdapter madapter;
    private double totle;
    private View v;
    private Handler handler;
    private LoadMoreItem loadMoreItem;
    private boolean is_divPage;
    private ProgressDialog dialog;
    private static int pageNo = 1;
    private ArrayList<MmsBean> list;
    private int Totlepage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null ==rootView ) {
            rootView = inflater.inflate(R.layout.activity_care_manage, container, false);
        }
        yutuLoading.showDialog();
        initView(rootView);
        mControl.requestForManageMMs(pageNo,call);
        return rootView;
    }


    private void initView(View view){
        setTitle(view,"检测过程监控");
        listView = (ListView) view.findViewById(R.id.caremanageList);
        listView.setOnItemClickListener(this);
        ListScroll();
    }

    private void ListScroll(){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                Log.d("lvcheng","Totlepage="+Totlepage);
                if(is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&pageNo<=Totlepage){
                    Toast.makeText(getActivity(), "正在获取更多数据...",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(0);

                }else if(pageNo>Totlepage){
                    Toast.makeText(getActivity(), "没有更多数据啦....",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);

        }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0){
                    pageNo++;
                    mControl.requestForManageMMs(pageNo,call);
;
                }
            }
        };

    }


    public static MmsManageFragment newInstance(String s) {
        MmsManageFragment fragment = new MmsManageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            点击重试
            case R.id.showFailed:
                yutuLoading.showDialog();
//                延时发送
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        mControl.requestForManageMMs(pageNo,call);
                    }
                },2000);
                requestAgain();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    private void setAdapter(ArrayList list){
        this.list = new ArrayList<>(list);
        madapter=new MmsListActivityAdapter(getContext() ,this.list);
        listView.setAdapter(madapter);


    }
    private ArrayList addList(ArrayList list){
        this.list.addAll(list);
        madapter.notifyDataSetChanged();
        return this.list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long n) {
       // view.setBackgroundResource(R.color.list_show);
        Intent intent = new Intent(mAct, InformationDisplayActivity.class);
        BaseControl model = new MmsManageControl();
        model.setTitle("消息详情");
        model.setId(list.get(i).getPKID());
        intent.putExtra("class",model);
        startActivity(intent);

    }

    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
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
                    ArrayList list = JsonUtil.fromJsonList(result, MmsBean.class);
                    Log.d("lvcheng","list="+list);
                    if(madapter==null){
                        setAdapter(list);
                    }else {
                        addList(list);
                    }
                }
            }


            totle=(double)map.get("total");
            Log.d("lvcheng","totle="+totle);
            Totlepage=((int)totle/20)+1;


        }
    } ;
}
