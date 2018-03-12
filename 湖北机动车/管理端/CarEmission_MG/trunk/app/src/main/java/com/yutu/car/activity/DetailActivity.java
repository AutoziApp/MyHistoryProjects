package com.yutu.car.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yutu.car.R;
import com.yutu.car.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.yutu.car.itemfactory.DetailItemFactory;
import com.yutu.car.presenter.BaseControl;
import com.yutu.car.presenter.CarDBInfoControl;
import com.yutu.car.presenter.NetControl;
import com.yutu.car.utils.JsonUtil;
import com.yutu.car.views.MyDecoration;
import com.yutu.car.views.YutuLoading;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class DetailActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    List data;
    private ImageView backView,rightIcon;
    private BaseControl control;
    private YutuLoading yutuLoading;
    private AssemblyRecyclerAdapter adapter;
    private RadioButton radioButton;
    private String radioContent,pkid;
    private NetControl netControl;
    private String checkResultid;
    private String uname;
    private String mCarNum;
    private String mVin;
    private CarDBInfoControl mDBInfoControl;

    private TextView tvFailed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        tvFailed= (TextView) findViewById(R.id.textView);
        yutuLoading = new YutuLoading(this);
        yutuLoading.showDialog();
        SharedPreferences sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        uname = sp.getString("uname", "");
        Log.e("zqq==DetailActivity", uname);
        control = (BaseControl) getIntent().getExtras().get("class");
        if(control.getTitle().equals("环保检测详细信息")){
            setTitle(control.getTitle(),true,true);
            rightIcon = (ImageView) findViewById(R.id.rightIconA);
            rightIcon.setImageResource(R.mipmap.jiahao);
            rightIcon.setOnClickListener(this);
        }else if (control.getTitle().equals("车辆公开信息")){
            setTitle(control.getTitle(),true,true);
            titleView.setVisibility(View.GONE);
            rightIcon = (ImageView) findViewById(R.id.rightIconA);
            Intent intent = getIntent();
            rightIcon.setVisibility(View.GONE);
            mCarNum = intent.getStringExtra("carNum");
            mVin = intent.getStringExtra("vin");
            mDBInfoControl = new CarDBInfoControl(mVin,mCarNum);
            initSpinner();
        }else {
            setTitle(control.getTitle().trim(),true,false);
        }

        control.requestData(call);
    }

    private void initSpinner() {
        mTitleSpinner.setVisibility(View.VISIBLE);
        
        String[] mItems = getResources().getStringArray(R.array.carMassage);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.my_simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner .setAdapter(adapter);
        mTitleSpinner.setOnItemSelectedListener(this);
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
                        if ("车辆达标信息".equals(carMessage)){
                            mDBInfoControl.requestData(call);
                        }else{
                            control.requestData(call);
                        }
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
                                    Intent intent = new Intent(DetailActivity.this, CheckStationActivity.class);
                                    if (radioContent.equals("2")) {
                                        if (editText.getText().toString().equals("")) {
                                            Toast.makeText(DetailActivity.this,"不通过原因不能为空",Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            intent.putExtra("jg", radioContent);
                                            intent.putExtra("nr", editText.getText().toString());
                                            intent.putExtra("jgbh",control.getId());
                                        }
                                    } else {
                                        intent.putExtra("jg", radioContent);
                                        intent.putExtra("nr", "");
                                        intent.putExtra("jgbh", control.getId());
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
                }else if (control.getTitle().equals("车辆公开信息")){
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
                                        Toast.makeText(DetailActivity.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                                        return;
                                    }else {
                                        String checkTime=control.getId();
                                         pkid = control.getPkid();
                                        String str=checkTime.substring(0,checkTime.length()-9).trim();
                                        netControl=new NetControl();
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
            tvFailed.setText("加载失败，点击重试");
            showFailed();
//            String s = e.toString();
        }

        @Override
        public void onResponse(String response, int id) {
            
            yutuLoading.dismissDialog();
            try {
                JSONObject jsonObject=new JSONObject(response.toString());
                String result = jsonObject.getString("result");
                JSONArray info = jsonObject.getJSONArray("info");
                if ("1".equals(result)&&info.length()>0){
                    JSONObject object = info.getJSONObject(0);
                     if ("车辆达标信息".equals(carMessage)){
                         data= mDBInfoControl.transDataJX(object);
                     }else if("车辆公开信息".equals(carMessage)){
                         mDBInfoControl.setVin(object.optString("VIN",""));
                         data = control.transDataJX(object);
                     }else{
                        data = control.transDataJX(object);
                    }
                    if (data != null && data.size() > 0)
                        initAdapter();
                } else {
                    tvFailed.setText("暂无数据");
                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.showFailed);
                    layout.setOnClickListener(null);
                    layout.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
//            Log.d("lvcheng","response>>>>>>>>>>"+response);
//            Map map = JsonUtil.jsonToMap(response);
//            String flag = map.get("flag").toString().substring(0,1);
////            LinkedTreeMap _Map = (LinkedTreeMap) map.get("data");
////           checkResultid=_Map.get("CHECKRESULTID").toString();
//            yutuLoading.dismissDialog();
//            if(flag.equals("1")){
//                DetailActivity.this.data = control.transData(response);
//                if(DetailActivity.this.data !=null&& DetailActivity.this.data.size()>0)
//                    initAdapter();
//            }else if(flag.equals("0")){
//                String error = map.get("error").toString();
//                recycle.setVisibility(View.GONE);
//                tvDetail.setVisibility(View.VISIBLE);
//                tvDetail.setText(error);
//            }else{
//                showFailed();
//            }
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
                if(map.get("info").toString().equals("意见提交成功")){
                    Toast.makeText(DetailActivity.this,"上传成功",Toast.LENGTH_LONG).show();
                }else if(map.get("info").toString().equals("意见提交失败")){
                    Toast.makeText(DetailActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                }
            }else{
                showFailed();
            }
        }
    } ;
    
    private String carMessage="";
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        requestAgain();
        switch (position){
            case 0://车辆公开信息
                yutuLoading.showDialog();
                carMessage="车辆公开信息";
                control.requestData(call);
                break;
            case 1://车辆达标信息
                yutuLoading.showDialog();

                carMessage="车辆达标信息";
                mDBInfoControl.requestData(call);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
