package com.mapuni.administrator.activity.wdAc;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.KnowledgeListBean;
import com.mapuni.administrator.divider.DividerItemDecoration;
import com.mapuni.administrator.itemFactory.KnowledgeListFactory;
import com.mapuni.administrator.manager.MessageEvent;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.assemblyadapter.AssemblyRecyclerAdapter;
import okhttp3.Call;

import static com.mapuni.administrator.R.id.et_content;
import static com.mapuni.administrator.R.id.et_title;


public class KnowledgeBaseActivity extends BaseActivity implements KnowledgeListFactory.EventListener{

    //控件
    private AlertDialog.Builder builder;
    XRefreshView xRefreshView;
    private RecyclerView recyclerView;
    private AssemblyRecyclerAdapter adapter;//万能适配器
    private TextView mTvNoData;

    //变量
    List<Object> dataList = new ArrayList<Object>();//数据源
    private boolean isLoadMore = false;

    //参数
    String sessionId;
    String rows = "10";
    int page = 1;
    String title="";
    String content="";



    @Override
    public int setLayoutResID() {
        return R.layout.activity_knowledge_base;
    }

    @Override
    public void initView() {
        setToolbarTitle("知识库");
        builder = new AlertDialog.Builder(mContext);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)  {
                //在这里执行我们的逻辑代码
                switch (item.getItemId()){
                    case R.id.action_search:
                        viewDialog();
                        break;

                    case R.id.action_add:
                        startActivity(new Intent(KnowledgeBaseActivity.this,KnowledgeAddActivity.class));
                        break;

                }
                return false;
            }
        });

        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvNoData = (TextView) findViewById(R.id.tv_noData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(dataList);
        adapter.addItemFactory(new KnowledgeListFactory(mContext));
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

    }

    @Override
    public void initData() {
        sessionId = (String) SPUtils.getSp(mContext, "sessionId", "");
        loadData();
    }

    //加载数据
    private void loadData() {
        mSvProgressHUD.showWithStatus("正在加载...");
        NetManager.getKnowledgeList(sessionId, page + "",rows, title,content, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                mSvProgressHUD.dismiss();
                mTvNoData.setVisibility(View.GONE);
                xRefreshView.setVisibility(View.VISIBLE);
                KnowledgeListBean KnowledgeListBean = gson.fromJson(response, KnowledgeListBean.class);
                List<KnowledgeListBean.RowsBean> list = KnowledgeListBean.getRows();
                if (!isLoadMore){
                    if (list!=null&&list.size()>0){
                        adapter.setDataList(list);
                    }else{
                        adapter.setDataList(null);
                        mTvNoData.setVisibility(View.VISIBLE);
                    }
                }else{
                    adapter.addAll(list);
                }
            }
        });
    }


    /**
     * 自定义视图对话框
     */
    public void viewDialog() {
        LayoutInflater factory = LayoutInflater.from(mContext);
        View view = factory.inflate(R.layout.search_view, null);
        final EditText etTitle= (EditText) view.findViewById(et_title);
        final EditText etContent= (EditText) view.findViewById(et_content);
        builder.setView(view)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title=etTitle.getText().toString().trim();
                        content=etContent.getText().toString().trim();
                        restartLoadData();
                    }
                })
                .setNegativeButton("取消",null);
        // 对话框的创建、显示,这里显示的位置是在屏幕的最下面，但是很不推荐这个种做法，因为距底部有一段空隙
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        String roleId=(String) SPUtils.getSp(this,"roleId","");
        if(!roleId.contains("2")){//管理员
            menu.findItem(R.id.action_add).setVisible(false);   //item动态隐藏
        }
        return true;
    }

    private void restartLoadData() {
                mSvProgressHUD.dismissImmediately();
                page = 1;
                isLoadMore = false;
                loadData();
    }

    @Override
    public void onClickEdit(int position, KnowledgeListBean.RowsBean rowsBean) {
        Intent intent=new Intent(mContext,KnowledgeUpdateActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("sessionId",sessionId);
        bundle.putString("knowledgeUuid",rowsBean.getUuid());
        bundle.putString("title",rowsBean.getTitle());
        bundle.putString("content",rowsBean.getContent());
        intent.putExtra("Bundle",bundle);
        String roleId=(String) SPUtils.getSp(this,"roleId","");
        if(roleId.contains("2")){//管理员
            intent.putExtra("role",true);
        }else {
            intent.putExtra("role",false);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }

    @Override
    public void onClickDel(int position, KnowledgeListBean.RowsBean rowsBean) {
        showDelDialog(rowsBean.getUuid());
    }

    private void showDelDialog(final String uuid){

        AlertDialog.Builder normalDialog =new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("确认删除该条数据库吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delKnowledge(uuid);
                    }
                });
        normalDialog.setNegativeButton("取消",null);
        // 显示
        normalDialog.show();
    }

    public void delKnowledge(String uuid){
        mSvProgressHUD.showWithStatus("正在删除...");

        NetManager.delKnowledge(sessionId, uuid, new StringCallback() {

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
                    if (status == 200) {
                        mSvProgressHUD.showSuccessWithStatus("删除成功");

                        title="";
                        content="";
                        restartLoadData();
                    }else if(status==-1) {
                        mSvProgressHUD.showErrorWithStatus("删除失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mSvProgressHUD.showErrorWithStatus("删除失败");
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {
        String message = event.getMessage();
        if (message.equals("KnowledgeupdateSuccess")){
            restartLoadData();
        }
    }
}
