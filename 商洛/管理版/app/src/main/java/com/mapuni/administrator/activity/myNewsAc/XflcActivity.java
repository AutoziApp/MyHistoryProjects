package com.mapuni.administrator.activity.myNewsAc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.CreatedTaskListBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.itemFactory.CreatedTaskRecyclerItemFactory;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

public class XflcActivity extends BaseActivity implements View.OnClickListener,CreatedTaskRecyclerItemFactory.EventListener{

    private ImageButton btnAddTask;
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
        return R.layout.activity_xflc;
    }

    @Override
    public void initView() {
        setToolbarTitle("下发流程");
        btnAddTask= (ImageButton) findViewById(R.id.btn_addTask);
        btnAddTask.setOnClickListener(this);
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvNoData = (TextView) findViewById(R.id.tv_noData);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new CreatedTaskRecyclerItemFactory(mContext));
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);//解决横向移动冲突
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
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
                xRefreshView.stopLoadMore(true);
            }
        });
        loadData();

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)  {
                //在这里执行我们的逻辑代码
                switch (item.getItemId()){
                    case R.id.action_add_task:
                        startActivity(new Intent(XflcActivity.this, AddTaskActivity.class));
                        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void initData() {

    }
    private void loadData() {
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.getCreatedTaskList(sessionId, rows, page + "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Logger.e(XflcActivity.class.getSimpleName(),e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(XflcActivity.class.getSimpleName(),response.toString());
                mSvProgressHUD.dismiss();
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                CreatedTaskListBean createdTaskListBean = gson.fromJson(response, CreatedTaskListBean.class);
                List<CreatedTaskListBean.RowsBean> list = createdTaskListBean.getRows();
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
    protected void onRestart() {
        super.onRestart();
        restartLoadData();
    }

    private void restartLoadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSvProgressHUD.dismissImmediately();
                page = 1;
                isLoadMore = false;
                loadData();
            }
        }, 1500);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addTask:
                startActivity(new Intent(this, AddTaskActivity.class));
                overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                break;
        }
    }


    @Override
    public void onClickDistribute(int position, CreatedTaskListBean.RowsBean rowsBean) {
        Intent intent = new Intent(this, DistributionActivity.class);
        intent.putExtra("taskUuid",rowsBean.getUuid());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }

    @Override
    public void onClickEdit(int position, CreatedTaskListBean.RowsBean rowsBean) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("bean",rowsBean);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }

    @Override
    public void onClickDel(int position, CreatedTaskListBean.RowsBean rowsBean) {
        final String uuid = rowsBean.getUuid();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示：");
        builder.setMessage("确定删除该条任务吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delTask(uuid);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "取消删除", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void delTask(String uuid) {
        mSvProgressHUD.showWithStatus("正在加载...");

        NetManager.delTask(sessionId, uuid, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络错误，删除失败", Toast.LENGTH_SHORT).show();
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                mSvProgressHUD.dismissImmediately();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    if (status == 0) {
                        mSvProgressHUD.showSuccessWithStatus(msg);
                        restartLoadData();
                    }else if(status==-1) {
                       Toast.makeText(mContext, "任务已分配，不能删除", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "网络错误，删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_main3, menu);
        return true;
    }

}
