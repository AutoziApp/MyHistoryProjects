package com.mapuni.administrator.activity.wdAc;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.administrator.R;
import com.mapuni.administrator.activity.BaseActivity;
import com.mapuni.administrator.bean.ContactBean;
import com.mapuni.administrator.manager.NetManager;
import com.mapuni.administrator.utils.SPUtils;
import com.mapuni.administrator.view.recyclerTreeView.bean.Dir;
import com.mapuni.administrator.view.recyclerTreeView.bean.File;
import com.mapuni.administrator.view.recyclerTreeView.viewbinder.DirectoryNodeBinder;
import com.mapuni.administrator.view.recyclerTreeView.viewbinder.FileNodeBinder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class ContactMethodActivity extends BaseActivity {
    private RecyclerView rv;
    private TreeViewAdapter adapter;
    List<TreeNode> nodes;
    String sessionId;
    @Override
    public int setLayoutResID() {
        return R.layout.activity_contact_method;
    }

    @Override
    public void initView() {
        setToolbarTitle("联系方式");
        sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        rv = (RecyclerView) findViewById(R.id.rv);
    }

    @Override
    public void initData() {
        nodes = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new FileNodeBinder(), new DirectoryNodeBinder()));
        rv.setAdapter(adapter);

        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                boolean isDir=node.getContent() instanceof Dir;
                if(isDir){//还有子
                        loadChild(node);
                }else {//无子  直接跳转详情页面
                    File file= (File) node.getContent();
                    if(!file.fileName.equals("该组织下暂无人员")){
                        Intent intent=new Intent(ContactMethodActivity.this,ContactDetailActivity.class);
                        intent.putExtra("id",file.uuid);
                        startActivity(intent);
                    }
                }
                return false;
            }

            private void loadChild(final TreeNode node1) {
            Dir dir= (Dir) node1.getContent();
            NetManager.queryContact(sessionId, dir.uuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa",e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Type type = new TypeToken<ArrayList<ContactBean>>() {}.getType();
                List<ContactBean> list=new Gson().fromJson(response.toString(),type);
                node1.setChildList(null);
                if(list!=null && list.size()>0) {
                    for (ContactBean bean : list) {
                        if(TextUtils.isEmpty(bean.getState())){
                            node1.addChild(new TreeNode<>(new File(bean.getText(), bean.getId(),"")));
                        }else {
                            node1.addChild(new TreeNode<>(new Dir(bean.getText(), bean.getId(), bean.getState())).addChild(new TreeNode<>(new File("该组织下暂无人员","",""))));
                        }
                    }
                }else {
                    node1.addChild(new TreeNode<>(new File("该组织下暂无人员","","")));
                }
                adapter.refresh(nodes);
            }
        });

            }


            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });

        loadPreChild();
    }



    private void loadPreChild() {
        NetManager.queryContact(sessionId, "-1", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("aaa",e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa",response);
                Type type = new TypeToken<ArrayList<ContactBean>>() {}.getType();
                List<ContactBean> list=new Gson().fromJson(response.toString(),type);
                List<TreeNode> treeNodelist=new ArrayList<>();
                if(list!=null && list.size()>0){
                    for (ContactBean bean:list){
                        treeNodelist.add(new TreeNode<>(new Dir(bean.getText(),bean.getId(),bean.getState())).addChild(new TreeNode<>(new File("该组织下暂无人员","",""))));
                    }
                    nodes.addAll(treeNodelist);
                    adapter.refresh(nodes);
                }
            }
        });
    }
}
