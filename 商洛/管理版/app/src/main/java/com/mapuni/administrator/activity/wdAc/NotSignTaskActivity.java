package com.mapuni.administrator.activity.wdAc;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.NoTaskSignBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.itemFactory.TaskNoSignRecyclerItemFactory;
import com.mapuni.administrator.manager.MessageEvent;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.Logger;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

/**
 *  @author Tianfy
 *  @time 2017/8/24  10:14
 *  @describe 未签收任务Activity
 */
public class NotSignTaskActivity extends BaseActivity implements TaskNoSignRecyclerItemFactory.EventListener {
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
        return R.layout.activity_no_sign_task;
    }

    @Override
    public void initView() {
        setToolbarTitle("待签收任务");
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvNoData = (TextView) findViewById(R.id.tv_noData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new TaskNoSignRecyclerItemFactory(mContext));
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
    }

    @Override
    public void initData() {

    }

    private void loadData() {
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.requestNoSignTask(sessionId, rows, page + "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Logger.e(NotSignTaskActivity.class.getSimpleName(),e.toString());
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Logger.i(NotSignTaskActivity.class.getSimpleName(),response.toString());
                mSvProgressHUD.dismiss();
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                NoTaskSignBean noTaskSignBean = gson.fromJson(response, NoTaskSignBean.class);
                List<NoTaskSignBean.RowsBean> list = noTaskSignBean.getRows();
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
    public void onClickDetail(int position, NoTaskSignBean.RowsBean rowsBean) {

        final String uuid = rowsBean.getUuid();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示：");
        builder.setMessage("确定签收该条任务吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                signTask(uuid);
                    taskCheck(uuid);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "取消签收", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void taskCheck(final String uuid){
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.taskDistributionCheck(sessionId, uuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络错误，签收失败", Toast.LENGTH_SHORT).show();
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    int status=jsonObject.optInt("status",0);
                    switch (status){
                        case 0://未签收
                            signTask(uuid);
                            break;
                        case -1://已签收
                            mSvProgressHUD.showInfoWithStatus("该任务已被他人签收");
                            restartLoadData();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signTask(String uuid) {


        NetManager.requestSignTask(sessionId, uuid, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext, "网络错误，签收失败", Toast.LENGTH_SHORT).show();
                mSvProgressHUD.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                mSvProgressHUD.dismissImmediately();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = (int) jsonObject.get("status");
                    String msg = (String) jsonObject.get("msg");
                    if (status == 0) {
                        mSvProgressHUD.showSuccessWithStatus(msg);
                        EventBus.getDefault().post(new MessageEvent("success"));
                        restartLoadData();
                    } else if (status == -1) {
                        mSvProgressHUD.showInfoWithStatus(msg);
                        restartLoadData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
}
