package com.mapuni.administrator.activity.gridMgAc;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.AreaBean;
import com.mapuni.administrator.bean.TaskNuberOfGridNumberBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.fragment.TaskStatisticsFragment;
import com.mapuni.administrator.itemFactory.TaskCountOfGridNumberItemFactory;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.utils.TimePickerUtil;
import com.mapuni.administrator.utils.TimeUtil;
import com.mapuni.administrator.view.treeViewHolder.CustomViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

public class GridNameActivity extends BaseActivity implements View.OnClickListener{
    //时间选择
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private ImageView mBtnQuery;
    private TimePickerUtil mTimePickerUtil;
    //区域选择
    private TextView mBtnShowArea;
    private String mSessionId;
    private Gson mGson;
    private TreeNode mRoot;
    private AlertDialog mDialog;
    private AndroidTreeView mAndroidTreeView;

    //tab
    private RadioGroup mRadioGroup;

    //recyclerView
    XRefreshView xRefreshView;
    String rows="10";
    int page=1;
    private boolean isLoadMore = false;
    private TextView mTvNoData;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    private RecyclerView recyclerView;
    List<Object> dataList = new ArrayList<Object>();//数据源
    TaskCountOfGridNumberItemFactory factory;
    //接口参数
    String startTime="";
    String endTime="";
    String gridUuid="";

    //数据
//    List<TaskNuberOfGridNumberBean.RowsBean> list1=new ArrayList<>();
//    List<TaskNuberOfGridNumberBean.RowsBean> list2=new ArrayList<>();
//    List<TaskNuberOfGridNumberBean.RowsBean> list3=new ArrayList<>();
    @Override
    public int setLayoutResID() {
        return R.layout.activity_grid_name;
    }

    @Override
    public void initView() {
        setToolbarTitle("网格员");
        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        mTvNoData = (TextView) findViewById(R.id.tv_noData);
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
                requestData();
                xRefreshView.stopRefresh();
            }


            @Override
            public void onLoadMore(boolean isSilence) {//上拉加载
                super.onLoadMore(isSilence);
                isLoadMore = true;
                page++;
                requestData();
                xRefreshView.stopLoadMore(true);
            }
        });
        mTvStartTime = (TextView) findViewById(R.id.tv_startTime);
        mTvEndTime = (TextView) findViewById(R.id.tv_endTime);
        mBtnShowArea = (TextView) findViewById(R.id.btn_showArea);
        mBtnQuery = (ImageView) findViewById(R.id.btn_query);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
         factory=new TaskCountOfGridNumberItemFactory(this);
        adapter.addItemFactory(factory);
        recyclerView.setAdapter(adapter);
        mRadioGroup= (RadioGroup) findViewById(R.id.rg_grid);
        mRadioGroup.check(R.id.rb_haveToDo);
        initListener();
        //获取前7天的时间-当天当前时间 默认请求网络
        mTvStartTime.setText(TimeUtil.getDateBefore(7));
        mTvEndTime.setText(TimeUtil.getStrToday());
    }


    private void initListener() {
        mBtnShowArea.setOnClickListener(this);
        mTvStartTime.setOnClickListener(this);
        mTvEndTime.setOnClickListener(this);
        mBtnQuery.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                adapter.setDataList(null);
                isLoadMore = false;
                page = 1;
                xRefreshView.stopRefresh();
                switch (checkedId){
                    case R.id.rb_haveToDo:
                        requestBanJie();
                        break;
                    case R.id.rb_upload:
                        requestShangBaoData();
                        break;
                    case R.id.rb_qiandao:
                        requestQianDao();
                        break;
                }
            }
        });
    }

    private void requestData(){
        mTvNoData.setVisibility(View.GONE);
        int id=mRadioGroup.getCheckedRadioButtonId();
        switch (id){
            case R.id.rb_haveToDo:
                requestBanJie();
                break;
            case R.id.rb_upload:
                requestShangBaoData();
                break;
            case R.id.rb_qiandao:
                requestQianDao();
                break;
        }
    }

    private void requestQianDao() {
        startTime=mTvStartTime.getText().toString();
        endTime=mTvEndTime.getText().toString();
        mSvProgressHUD.showWithStatus("数据正在加载...");
        NetManager.getQianDaoStatics(mSessionId, startTime, endTime, gridUuid,page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaaa",e.getMessage());
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaaa",response);
                mSvProgressHUD.dismiss();
                TaskNuberOfGridNumberBean taskNuberOfGridNumberBean=mGson.fromJson(response,TaskNuberOfGridNumberBean.class);
                List<TaskNuberOfGridNumberBean.RowsBean> list=taskNuberOfGridNumberBean.getRows();
//                adapter.setDataList(list);
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

    //请求办结的数据
    private void requestBanJie() {
        startTime=mTvStartTime.getText().toString();
        endTime=mTvEndTime.getText().toString();
        mSvProgressHUD.showWithStatus("数据正在加载...");
        NetManager.getBanJieStatics(mSessionId, startTime, endTime, gridUuid,page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaaa",e.getMessage());
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaaa",response);
                mSvProgressHUD.dismiss();
                TaskNuberOfGridNumberBean taskNuberOfGridNumberBean=mGson.fromJson(response,TaskNuberOfGridNumberBean.class);
                List<TaskNuberOfGridNumberBean.RowsBean> list=taskNuberOfGridNumberBean.getRows();
//                adapter.setDataList(list);
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
    public void initData() {
        mGson = new Gson();
        mSessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        mTimePickerUtil = new TimePickerUtil(mContext);
        //初始化城市筛选条件
        mRoot = TreeNode.root();
        initCityData(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_startTime:
                mTimePickerUtil.showTimePicker(mTvStartTime);
                break;
            case R.id.tv_endTime:
                mTimePickerUtil.showTimePicker(mTvEndTime);
                break;
            case R.id.btn_showArea:
                //弹出Dialog
                showDialog();
                break;
            case R.id.btn_query:
                isLoadMore = false;
                page = 1;
                xRefreshView.stopRefresh();
                requestData();
                break;
        }

    }


    private void requestShangBaoData() {
        startTime=mTvStartTime.getText().toString();
        endTime=mTvEndTime.getText().toString();
        mSvProgressHUD.showWithStatus("数据正在加载...");
        NetManager.getReportAndHandeledRecordCountByCondition(mSessionId, startTime, endTime, gridUuid, page,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.i("aaa",e.getMessage());
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa",response);
                mSvProgressHUD.dismiss();
                TaskNuberOfGridNumberBean taskNuberOfGridNumberBean=mGson.fromJson(response,TaskNuberOfGridNumberBean.class);
                List<TaskNuberOfGridNumberBean.RowsBean> list=taskNuberOfGridNumberBean.getRows();
//                adapter.setDataList(list);
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


    /**
     * @param id
     * @author Tianfy
     * @time 2017/9/14  11:25
     * @describe 初始化城市筛选条件
     */
    //默认id为空
    private String id = "";
    TreeNode mParentNode;
    private void initCityData(String id) {

        NetManager.requestCityData(id, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(TaskStatisticsFragment.class.getSimpleName(), e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(TaskStatisticsFragment.class.getSimpleName(), response.toString());
                List<AreaBean> areaBeanList = mGson.fromJson(response, new TypeToken<List<AreaBean>>() {
                }.getType());
                if (areaBeanList != null && areaBeanList.size() > 0) {
                    //请求父节点填充父级目录
                    mParentNode = new TreeNode(areaBeanList.get(0)).setViewHolder(new CustomViewHolder(mContext));

                    mRoot.addChild(mParentNode);
                    mAndroidTreeView = new AndroidTreeView(mContext, mRoot);
                    //打开复选框开关
                    mAndroidTreeView.setSelectionModeEnabled(true);
                    initAreaType(areaBeanList);
                    requestData();
                    initDialog();
                    mAndroidTreeView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
                        @Override
                        public void onClick(TreeNode node, Object value) {
                            AreaBean areaBean= (AreaBean) value;
                            //bean中的isLeaf和node中的isLeaf相反
                            if (!areaBean.isLeaf()){//有子
                                if(node.isLeaf()){//有子
                                    requestChildNode(((AreaBean) value).getId(),node);
                                }

                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * @param
     * @param areaBeanList
     * @author Tianfy
     * @time 2017/9/18  8:52
     * @describe 初始化区域类型
     */
    private void initAreaType(List<AreaBean> areaBeanList) {
        String areaStr = areaBeanList.get(0).getText();
        gridUuid=areaBeanList.get(0).getId();
        mBtnShowArea.setText(areaStr);
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/15  14:41
     *  @describe 请求子节点数据并添加到父节点的TreeView中
     *  @param id 父节点id
     *  @param parentNode 父节点
     */
    private void requestChildNode(String id, final TreeNode parentNode) {

        NetManager.requestCityData(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Logger.e(TaskStatisticsFragment.class.getSimpleName(), e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(TaskStatisticsFragment.class.getSimpleName(), response.toString());
                List<AreaBean> areaBeanList = mGson.fromJson(response, new TypeToken<List<AreaBean>>() {
                }.getType());
                if (areaBeanList != null && areaBeanList.size() > 0) {
                    //请求子节点填充父节点
                    for (int i = 0; i <areaBeanList.size() ; i++) {
                        TreeNode childNode = new TreeNode(areaBeanList.get(i)).setViewHolder(new CustomViewHolder(mContext));
                        parentNode.addChild(childNode);
                    }
                    //再次打开所有树，解决点击一次子叶子不出现的bug
                    mAndroidTreeView.expandNode(parentNode);
                }else{

                }
            }
        });
    }
    /**
     *  @author Tianfy
     *  @time 2017/9/15  14:42
     *  @describe 初始化Dialog
     */
    private void initDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("选择城市");
        LinearLayout contanier=new LinearLayout(mContext);
        contanier.setVerticalGravity(LinearLayout.VERTICAL);
        contanier.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View view = mAndroidTreeView.getView();
        view.setPadding(30,0,0,0);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        contanier.addView(view);
        builder.setView(contanier);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(mContext, mAndroidTreeView.getSelected().size() + " selected", Toast.LENGTH_SHORT).show();
                if(mAndroidTreeView.getSelected().size()>0){
                    AreaBean bean= (AreaBean) mAndroidTreeView.getSelected().get(0).getValue();
                    gridUuid=bean.getId();
                    mBtnShowArea.setText(bean.getText());

                    isLoadMore = false;
                    page = 1;
                    xRefreshView.stopRefresh();
                    requestData();
                }
            }
        });
        mDialog = builder.create();
    }

    private void showDialog() {
        mDialog.show();
    }

}


