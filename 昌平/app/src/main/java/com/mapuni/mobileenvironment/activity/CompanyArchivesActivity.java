package com.mapuni.mobileenvironment.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.TabViewpagerAdapter;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.app.DataFactory;
import com.mapuni.mobileenvironment.fragment.PagerBaseFragment;
import com.mapuni.mobileenvironment.fragment.SupportFragment;
import com.mapuni.mobileenvironment.model.PagerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompanyArchivesActivity extends ActivityBase implements DataFactory.FetchData {
    private TabLayout tab_FindFragment_title;                            //定义TabLayout
    private ViewPager vp_FindFragment_pager;                             //定义viewPager
    private TabViewpagerAdapter fAdapter;                               //定义adapter
    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;//tab名称列表
    private final int QUERY_PAGER_SIZE = 4;
    private Handler handler;
    private PagerModel model;
    private  DataFactory fd;
    private String ComPanyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_archives);
        fd = new DataFactory();
        fd.setFetchData(this);
        ComPanyCode = (String) getIntent().getExtras().get("polsorcode");
        new MyThread();
//        fd.updateDataBase();

    }
    /**
     * 初始化各控件
     */
    private void initControls() {
        setTitle((String) getIntent().getExtras().get("name"));
        tab_FindFragment_title = (TabLayout) findViewById(R.id.tab_FindFragment_title);
        vp_FindFragment_pager = (ViewPager) findViewById(R.id.vp_FindFragment_pager);
        vp_FindFragment_pager.addOnPageChangeListener(new OnChangeSelect());
        vp_FindFragment_pager.setOffscreenPageLimit(1);
        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PagerBaseFragment fragment = new PagerBaseFragment();
//            model.setTitleList(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",model);
            bundle.putInt("index",i);
            fragment.setArguments(bundle);
            list_fragment.add(fragment);
        }

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
//        list_title = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.pager_title)));
//        change
//          list_title = new ArrayList<>(Arrays.asList("企业基本信息","建设项目管理","环境统计"));
        list_title = new ArrayList<>(Arrays.asList("企业基本信息"));
        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
        for(int i = 0;i<list_title.size();i++){
            TabLayout.Tab tab = tab_FindFragment_title.newTab().setText(list_title.get(i));
            tab.setTag(i);
            tab_FindFragment_title.addTab(tab);
        }

        fAdapter = new TabViewpagerAdapter(getSupportFragmentManager(),list_fragment,list_title,this);
        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
    }

    public void setDetailCurrentFragment(Fragment detailCurrentFragment) {
        this.detailCurrentFragment = detailCurrentFragment;
    }
    public SupportFragment getDetaiCurrentFragment(){
        return  (SupportFragment) detailCurrentFragment;
    }
    private Fragment detailCurrentFragment;
    PagerBaseFragment fragment;
    int currentPosition =100;

    @Override
    public void fetchBefore() {

    }

    @Override
    public void getData(Object obj) {
        Object o = obj;
//        initControls();
    }

    @Override
    public void fetchFail() {

    }

    private class OnChangeSelect implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if(fragment==null){
                fragment = (PagerBaseFragment) list_fragment.get(position);
            }
            if(!fragment.isResumed()){
                return;
            }
            if(currentPosition==position){
                fragment=null;
                return;
            }
            currentPosition = position;
            FragmentTransaction ft = fragment.getChildFragmentManager().beginTransaction();
            if(detailCurrentFragment==null){
                detailCurrentFragment = fragment.showFragment();
                ft.show(fragment.showFragment()).commit();
            }else{
                ft.hide(detailCurrentFragment).show(fragment.showFragment()).commit();
                detailCurrentFragment = fragment.showFragment();
            }
            fragment = null;
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    class MyThread extends Thread {
        int SuccesPagerSize = 0;
        {
            start();
        }
        @Override
        public void run() {
            Looper.prepare(); // 创建该线程的Looper对象
            handler = new Handler(Looper.myLooper()) {
                public void handleMessage(Message msg) {
                    if(model == null){
                        model = new PagerModel(handler);
                        model.setComPanyCode(ComPanyCode);
                    }
                    if(msg.what==DataFactory.TWO_COLUMN){
                        SuccesPagerSize+=1;
                        String s = (String) msg.getData().get("tableName");
                        Object obj = msg.getData().get("data");
                        model.setTwoColumnData(obj,s);
                    }else if(msg.what == DataFactory.ONE_SELECT) {
                        SuccesPagerSize += 1;
                        String s = (String) msg.getData().get("tableName");
                        Object obj = msg.getData().get("data");
                        model.setOneSelectHeadData(obj, s);
                    }else if(msg.what == DataFactory.LEFT_SELECT){
                        SuccesPagerSize += 1;
                        String s = (String) msg.getData().get("tableName");
                        Object obj = msg.getData().get("data");
                        model.setTwoSelectHeadData(obj, s,"nf");
                    }
                    if(SuccesPagerSize == QUERY_PAGER_SIZE){  //控制数据分层检索
                            fd.getDetailData(model.getSelectValue(),handler);
                    }else if(SuccesPagerSize == 7){
                            DataApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    initControls();
                                }
                            });
                            getLooper().quit();  //回收looper
                    }
                }
            };
            fd.getData(ComPanyCode,handler);
            Looper.loop(); // 死循环，此后代码无效
        }
    }
}
