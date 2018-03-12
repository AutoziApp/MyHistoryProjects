package com.mapuni.shangluo.activity.wdAc;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapuni.shangluo.R;
import com.mapuni.shangluo.activity.BaseActivity;
import com.mapuni.shangluo.activity.loginAc.LoginActivity;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.utils.SPUtils;
import com.xw.repo.XEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class ModifyPasswordActivity extends BaseActivity {


    String oldPwd, newPwd1, newPwd2;
    XEditText mEdtOldPassword;
    XEditText mEdtNewPassword1;
    XEditText mEdtNewPassword2;
    Button mBtnSure;

    @Override
    public void initView() {
        setToolbarTitle("修改密码");
        mEdtOldPassword= (XEditText) findViewById(R.id.edt_oldPassword);
        mEdtNewPassword1= (XEditText) findViewById(R.id.edt_newPassword1);
        mEdtNewPassword2= (XEditText) findViewById(R.id.edt_newPassword2);
        mBtnSure= (Button) findViewById(R.id.btn_sure);
        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPwd();
            }
        });

    }
    @Override
    public int setLayoutResID() {
        return R.layout.activity_modify_password;
    }


    private void modifyPwd() {
        oldPwd = mEdtOldPassword.getText().toString().trim();
        newPwd1 = mEdtNewPassword1.getText().toString().trim();
        newPwd2 = mEdtNewPassword2.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            Toast.makeText(this, "请先输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPwd1)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPwd2)) {
            Toast.makeText(this, "请确认新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldPwd.equals(newPwd1)) {
            Toast.makeText(this, "新密码不能与原密码相同", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPwd1.equals(newPwd2)) {
            Toast.makeText(this, "确认新密码有误", Toast.LENGTH_SHORT).show();
            return;
        }
        String sessionId = (String) SPUtils.getSp(this, "sessionId", "");
        NetManager.modifyPassword(sessionId, oldPwd, newPwd1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(ModifyPasswordActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(0==jsonObject.optInt("status")){
                        boolean clearSuccess = SPUtils.clearLoginMsg(ModifyPasswordActivity.this);
                        if (clearSuccess) {
                            Toast.makeText(ModifyPasswordActivity.this,"密码修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ModifyPasswordActivity.this, LoginActivity.class));
                            finish();
                        }
                    }else {
                        Toast.makeText(ModifyPasswordActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ModifyPasswordActivity.this,"密码修改失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    @Override
    public void initData() {

    }


}
