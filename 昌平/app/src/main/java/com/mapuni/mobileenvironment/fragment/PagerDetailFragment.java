package com.mapuni.mobileenvironment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.app.DataFactory;
import com.mapuni.mobileenvironment.model.PagerModel;
import com.mapuni.mobileenvironment.record.AssemblyRecyclerAdapter;
import com.mapuni.mobileenvironment.record.itemfactory.BigIconHeadFactory;
import com.mapuni.mobileenvironment.record.itemfactory.LineItemFactory;
import com.mapuni.mobileenvironment.record.itemfactory.RectHeadFactory;
import com.mapuni.mobileenvironment.record.itemfactory.RectRecyclerItemFactory;
import com.mapuni.mobileenvironment.record.itemfactory.SearchHeadFactory;
import com.mapuni.mobileenvironment.record.itemfactory.ThreeListItemFactory;
import com.mapuni.mobileenvironment.record.itemfactory.TitleHeadFactory;
import com.mapuni.mobileenvironment.record.itemfactory.TwoSpinnerHeadFactory;
import com.mapuni.mobileenvironment.record.itemfactory.WhiteRecyclerItemFactory;
import com.mapuni.mobileenvironment.utils.MyDecoration;

import java.util.ArrayList;

public class PagerDetailFragment extends SupportFragment {
    private RecyclerView mRecycle;
    private AssemblyRecyclerAdapter mAdapter;
    private PagerModel model;
    String title;
    ArrayList data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_detail_fragment,null);
        title = (String) getArguments().get("title");
        initView(view);
        initData();
        initAdapter();
        return view;
    }

    public void initView(View v){
        mRecycle = (RecyclerView) v.findViewById(R.id.recycle);
        mRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
    public void initData(){
        model = (PagerModel) getArguments().get("data");
    }
    public  void  initAdapter(){
        if(title.equals("企业基本信息")||title.equals("建设项目环评")||title.equals("建设项目验收")||
                title.equals("污染物排放分析")){
            data = model.getTwoColumnData(title);
        }else{
            data = DataFactory.getListByName(title);
        }
        if(data ==null)
            return;
        mAdapter = new AssemblyRecyclerAdapter(data);
       assemblyAdapter(mAdapter,title);
        mRecycle.setAdapter(mAdapter);
    }

    private void assemblyAdapter(AssemblyRecyclerAdapter adapter,String title){
        Context context = getActivity().getBaseContext();
        if(title.equals("企业基本信息")){
            adapter.addItemFactory(new WhiteRecyclerItemFactory(context));
        }else if(title.equals("建设项目环评")||title.equals("建设项目验收")||title.equals("项目变更")){
            adapter.addHeaderItem(new RectHeadFactory(context,title,model),"项目名称:");
            adapter.addItemFactory(new RectRecyclerItemFactory(context));
        }else if(title.equals("污染物排放分析")||title.contains("固废、危废排放分析")){
            adapter.addHeaderItem(new TwoSpinnerHeadFactory(context,title,model),"");
            adapter.addItemFactory(new LineItemFactory(context));
            mRecycle.addItemDecoration(new MyDecoration(getActivity(), LinearLayout.VERTICAL,true));
        }else if(title.equals("能源消耗分析")){
            adapter.addHeaderItem(new BigIconHeadFactory(context),"");
            adapter.addItemFactory(new LineItemFactory(context));
            mRecycle.addItemDecoration(new MyDecoration(getActivity(), LinearLayout.VERTICAL,true));
        }else if(title.contains("排污申报")){
            adapter.addHeaderItem(new TwoSpinnerHeadFactory(context,title,model),"");
            adapter.addHeaderItem(new TitleHeadFactory(context),"");
            adapter.addItemFactory(new ThreeListItemFactory(context));
            mRecycle.addItemDecoration(new MyDecoration(getActivity(), LinearLayout.VERTICAL,true));
        }else if(title.contains("排污收费")){
            mRecycle.addItemDecoration(new MyDecoration(getActivity(), LinearLayout.VERTICAL,true));
            adapter.addHeaderItem(new SearchHeadFactory(context),"");
            adapter.addHeaderItem(new TitleHeadFactory(context),"");
            adapter.addItemFactory(new ThreeListItemFactory(context));
        }else if(title.contains("来访投诉")){
            mRecycle.addItemDecoration(new MyDecoration(getActivity(), LinearLayout.VERTICAL,false));
            adapter.addHeaderItem(new TitleHeadFactory(context),"");
            adapter.addItemFactory(new ThreeListItemFactory(context));
        }
    }
}
