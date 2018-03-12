package com.yutu.car.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.yutu.car.itemfactory.DetailItemFactory;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.CarInfoControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.MyDecoration;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class DetailSearchActivity extends BaseActivity {
    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    List data;
    private ImageView backView,rightIcon;
    private TextView titleView;
    private BaseControl control;
    private YutuLoading yutuLoading;
    private AssemblyRecyclerAdapter adapter;
    private RadioButton radioButton;
    private String radioContent,pkid;
    private NetControl netControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        control = (BaseControl) getIntent().getExtras().get("class");
        pkid=control.getId();
        if(control.getTitle().equals("环保检测详细信息")||control.getTitle().equals("路检抽检详细信息")){
            setTitle(control.getTitle(),true,true);
            rightIcon = (ImageView) findViewById(R.id.rightIconA);
            rightIcon.setImageResource(R.mipmap.jiahao);
            rightIcon.setOnClickListener(this);
        }else {
            setTitle(control.getTitle(),true,false);
        }

        control.requestData(call);
    }

    private void initAdapter() {
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));
        adapter = new AssemblyRecyclerAdapter(data);
        adapter.addItemFactory(new DetailItemFactory(this));
        recycle.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showFailed:
                yutuLoading.showDialog();
                delayedPost(new Runnable() {
                    @Override
                    public void run() {
                        control.requestData(call);
                    }
                }, 2000);
                requestAgain();
                break;
            case R.id.leftIcon:
                finish();
                break;
            case R.id.rightIconA:
                radioContent="2";
                if(control.getTitle().equals("环保检测详细信息")) {
                    LayoutInflater layoutInflater = LayoutInflater.from(this);
                    View myLoginView = layoutInflater.inflate(R.layout.dialog_car, null);
                    View titleView=layoutInflater.inflate(R.layout.title_en_dialog,null);
                    RadioGroup radioGroup = (RadioGroup) myLoginView.findViewById(R.id.radiogroup);
                    final RadioButton radioButton1 = (RadioButton) myLoginView.findViewById(R.id.radioButton1);
                    final RadioButton radioButton2 = (RadioButton) myLoginView.findViewById(R.id.radioButton2);
                    final EditText editText = (EditText) myLoginView.findViewById(R.id.check_no);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                            if (checkedId == radioButton1.getId()) {
                                radioContent = "1";
                                editText.setText("");
                                editText.setFocusable(false);
                                editText.setFocusableInTouchMode(false);
                            } else if (checkedId == radioButton2.getId()) {
                                radioContent = "2";
                                editText.setFocusableInTouchMode(true);
                                editText.setFocusable(true);
                                editText.requestFocus();
                            }
                        }
                    });

                    Dialog alertDialog = new AlertDialog.Builder(this).
                            setCustomTitle(titleView).setView(myLoginView).
                            setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DetailSearchActivity.this, CheckStationActivity.class);
                                    if (radioContent.equals("2")) {
                                        if (editText.getText().toString().equals("")) {
                                            Toast.makeText(DetailSearchActivity.this,"不通过原因不能为空",Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            intent.putExtra("checkstation", radioContent);
                                            intent.putExtra("noPassReason", editText.getText().toString());
                                            intent.putExtra("userId", "baixg");
                                            intent.putExtra("pkid", pkid);
                                        }
                                    } else {
                                        intent.putExtra("checkstation", radioContent);
                                        intent.putExtra("userId", "baixg");
                                        intent.putExtra("pkid", pkid);
                                    }


                                    startActivity(intent);
                                }
                            }).
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();
                }else if (control.getTitle().equals("路检抽检详细信息")){
                    LayoutInflater layoutInflater2 = LayoutInflater.from(this);
                    View myLoginView2 = layoutInflater2.inflate(R.layout.check_no_dialog, null);
                    View CheckView=layoutInflater2.inflate(R.layout.title_check_dialog, null);
                    final EditText editText2=(EditText)myLoginView2.findViewById(R.id.check_no_content);
                    Dialog alertDialog = new AlertDialog.Builder(this).
                            setCustomTitle(CheckView).setView(myLoginView2).
                            setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(editText2.getText().toString().equals("")){
                                        Toast.makeText(DetailSearchActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                                        return;
                                    }else {
                                        String checkTime=control.getId();
                                        String str=checkTime.substring(0,checkTime.length()-9).trim();
                                        netControl=new NetControl();
                                        CarInfoControl carInfoControl=new CarInfoControl();
                                        netControl.requestForRowCheckNoQualified(editText2.getText().toString(),control.getPkid(),control.getCheckResultid(),str,callback);




                                    }
                                }
                            }).
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();
                }
        }

    }
    public StringCallback call = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
//            String s = e.toString();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng","response>>>>>>>>>>"+response);
            Map map = JsonUtil.jsonToMap(response);
            String flag = map.get("flag").toString().substring(0,1);
            yutuLoading.dismissDialog();

            if(flag.equals("1")){
                data = control.transData(response);
                if(data!=null&&data.size()>0)
                    initAdapter();
            }else if(flag.equals("0")){
                String error = map.get("error").toString();
                recycle.setVisibility(View.GONE);
                tvDetail.setText(error);
            }else{
                showFailed();
            }
        }
    } ;


    public StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            yutuLoading.dismissDialog();
            showFailed();
//            String s = e.toString();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("lvcheng","response>>>>>>>>>>"+response);
            Map map = JsonUtil.jsonToMap(response);
            String flag = map.get("flag").toString().substring(0,1);
            yutuLoading.dismissDialog();

            if(flag.equals("1")){
                if(map.get("info").toString().equals("操作成功")){
                    Toast.makeText(DetailSearchActivity.this,"上传成功",Toast.LENGTH_LONG).show();
                }else if(map.get("info").toString().equals("操作失败")){
                    Toast.makeText(DetailSearchActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                }
            }else{
                showFailed();
            }
        }
    } ;
}
