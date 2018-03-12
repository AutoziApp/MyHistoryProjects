package com.mapuni.mobileenvironment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.adapter.CompanyListAdapter;
import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.app.DataFactory;
import com.mapuni.mobileenvironment.widget.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CompanySearchResultActivity extends ActivityBase implements AdapterView.OnItemClickListener,DataFactory.FetchData {

    private ListView listView;
    private List<HashMap<String,Object>> titleList;
    private List<HashMap<String,Object>> updateTableName;
    private CompanyListAdapter adapter;
    private DataFactory fd;
    private Handler handler;
    private  int UPDATA_PAGER_SIZE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search_result);
//        mCheckMsgThread = new HandlerThread("DataBase_Update_Thread");
//        mCheckMsgThread.start();
//        handler = new Handler(mCheckMsgThread.getLooper());
        new MyThread().setName("123456");
        setTitle("企业基本信息查询",1,1,0);
        titleIconB.setImageResource(R.mipmap.sousuo);
        titleIconA.setImageResource(R.mipmap.update_icon);
        listView= (ListView) findViewById(R.id.list);
        fd = new DataFactory();
        fd.setFetchData(this);
        fd.getData("select t.entname,t.PolSorCode from PUB_BAS_EnterpriseInfo t","");
    }

    AlertDialog  _Dialog;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rightIconA:
                final AlertDialog.Builder buider = new AlertDialog.Builder(this);
                buider.setMessage("更新企业档案");
                buider.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fd.getData("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name","");
                        Toast.makeText(CompanySearchResultActivity.this,"企业档案开始更新",Toast.LENGTH_SHORT).show();
                    }
                });
                buider.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _Dialog.dismiss();
                    }
                });
                _Dialog = buider.create();
                _Dialog.show();
//                if(updateTableName!=null&&updateTableName.size()>0){
//                    Toast.makeText(this,"请勿频繁更新",Toast.LENGTH_SHORT).show();
//                    break;
//                }
                break;
            case R.id.rightIconB:
                final AlertDialog.Builder builder=new AlertDialog.Builder(CompanySearchResultActivity.this);
                builder.setCustomTitle(View.inflate(CompanySearchResultActivity.this,R.layout.alert_title,null));
                final View view1=View.inflate(CompanySearchResultActivity.this,R.layout.companysearch_dialog,null);
                Button btnYes= (Button) view1.findViewById(R.id.btn_yes);
                Button btnNo= (Button) view1.findViewById(R.id.btn_no);
                final List list = new ArrayList<String>(Arrays.asList("昌平地区"));
                NiceSpinner spinner = (NiceSpinner) view1.findViewById(R.id.spinnerA);
                final EditText et = (EditText) view1.findViewById(R.id.qyjbxx_query_qymc);
                spinner.attachDataSource(list);
                builder.setView(view1);
                final AlertDialog dialog=builder.create();
                dialog.show();
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String result = et.getText().toString();
                        if(result==null||result.equals("")){
                            dialog.dismiss();
                            return;
                        }
                        List<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
                        for(int i=0;i<titleList.size();i++){
                            HashMap map = titleList.get(i);
                            if((map.get("entname").toString()).contains(result)){
                                list.add(map);
                            }
                        }
                        if(list!=null&&list.size()>0){
                            titleList.clear();
                            titleList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap map = titleList.get(position);
        Intent intent = new Intent(this,CompanyArchivesActivity.class);
        intent.putExtra("polsorcode", (String) map.get("polsorcode"));
        intent.putExtra("name",(String)map.get("entname"));
        startActivity(intent);
    }

    @Override
    public void fetchBefore() {

    }
    @Override
    public void getData(Object obj) {
//
        if(adapter==null){
            titleList = (List<HashMap<String,Object>>) obj;
            adapter=new CompanyListAdapter(CompanySearchResultActivity.this,titleList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }else{
            updateTableName= (List<HashMap<String,Object>>) obj;
            UPDATA_PAGER_SIZE = updateTableName.size()-1;
                    for(int i=0;i<updateTableName.size()-1;i++){
                        String s = (String) updateTableName.get(i).get("name");
                        fd.updateDataBase(s,handler);
                    }
        }
    }
    @Override
    public void fetchFail() {

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
                        if(msg.what==1){
                            SuccesPagerSize+=1;
                            if(SuccesPagerSize == UPDATA_PAGER_SIZE){
                                Log.i("Lybin","更新完毕");

                                DataApplication.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        DataApplication.showToast("企业档案更新完成");
                             }
                                });
                                getLooper().quit();  //回收looper

                            }
                        }
                    }
                };
                Looper.loop(); // 死循环
        }
    }
}
