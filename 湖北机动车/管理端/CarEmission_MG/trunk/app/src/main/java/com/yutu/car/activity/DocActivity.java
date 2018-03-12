package com.yutu.car.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.yutu.car.R;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.yutu.car.bean.FileBean;
import com.yutu.car.itemfactory.FileItemFactory;
import com.yutu.car.presenter.DownLoadFile;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DocActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final int READ_SD = 2;
    private YutuLoading yutuLoading;
    private NetControl mControl;
    @Bind(R.id.failedLayout)
    LinearLayout failedLayout;
    AssemblyRecyclerAdapter adapter;
    ArrayList<FileBean> data=new ArrayList<>();
    @Bind(R.id.recycle)
    RecyclerView recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doc);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.doc_file);
        ButterKnife.bind(this, linearLayout);
        setTitle("法律法规及技术标准", true, false);
        yutuLoading = new YutuLoading(this);
        mControl = new NetControl();
        yutuLoading.showDialog();
        requestPermission();
    }

    @AfterPermissionGranted(READ_SD)
    private void requestPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mControl.requestForDoc(call);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, "请求权限",
                    READ_SD, perms);
        }
    }

    private void initAdapter() {
        recycle.setLayoutManager(new LinearLayoutManager(this));
        if (adapter==null){
            adapter = new AssemblyRecyclerAdapter(data);
            recycle.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
        DownLoadFile downFile = new DownLoadFile(this, adapter);
        adapter.addItemFactory(new FileItemFactory(this, downFile));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.leftIcon:
                finish();
                break;
            case R.id.showFailed:
                yutuLoading.showDialog();
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        mControl.requestForDoc(call);
                    }
                }, 2000);
                requestAgain();
                break;
        }
    }

//    public StringCallback call = new StringCallback() {
//
//        @Override
//        public void onError(Call call, Exception e, int id) {
//            yutuLoading.dismissDialog();
//            showFailed();
//        }
//
//        @Override
//        public void onResponse(String response, int id) {
//            yutuLoading.dismissDialog();
//            data = JsonUtil.fromJsonList(response, FileBean.class);
//            Log.d("lvcheng", ">>>>response" + response);
//            if (data != null && data.size() > 0) {
//                initAdapter();
//            } else {
//                showFailed();
//            }
//        }
//    };

    public StringCallback call = new StringCallback() {

        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
        }

        @Override
        public void onResponse(String response, int id) {
            yutuLoading.dismissDialog();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if(1==jsonObject.optInt("result",-1)){  //请求成功
                    JSONArray array=jsonObject.optJSONArray("info");
                    if (array != null && array.length() > 0) {
                        for(int i=0;i<array.length();i++){
                            JSONObject obj=array.getJSONObject(i);
                            FileBean fileBean=new FileBean();
                            fileBean.setUPLOADTIME(obj.optString("UPLOADTIME"));
                            fileBean.setFILEFMT(obj.optString("FILEFMT"));
                            fileBean.setFILENAME(obj.optString("FILENAME"));
                            fileBean.setFILEDOWNLOADPATH(obj.optString("FILEDOWNLOADPATH"));
                            data.add(fileBean);
                        }
                    }
                    if (data != null && data.size() > 0) {
                        initAdapter();
                    }else{
                        showFailed();
                    }

                }else {     //请求失败
                    showFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showFailed();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mControl.requestForDoc(call);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
