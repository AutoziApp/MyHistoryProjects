package com.mapuni.shangluo.activity.loginAc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.manager.PathManager;
import com.mapuni.shangluo.update.BaseAutoUpdate;
import com.mapuni.shangluo.utils.SPUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yanzhenjie.permission.SettingService;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    private SVProgressHUD mSVProgressHUD;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        this.mContext = this;
        mSVProgressHUD = new SVProgressHUD(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    
    public void initData() {
        checkPremession();
    }

    private void checkPremession() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Permission.STORAGE,Permission.LOCATION)
                .rationale(new RationaleListener() {
                    
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(mContext, rationale).show();
                    }
                })
                .callback(this)
                .start();
    }

    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
    @PermissionYes(100)
    private void getPermissionYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。
//        Toast.makeText(mContext, "申请权限成功", Toast.LENGTH_SHORT).show();
//        new BaseAutoUpdate(mContext).CheckUpdate(PathManager.VERSION_URL, 1, mSVProgressHUD);
        getXml();

    }

    private void getXml() {
        //获取baseUrl
        String baseUrl= (String) SPUtils.getSp(mContext, "newIP", PathManager.BaseUrl);
        //设置baseUrl
        PathManager.setPathUrl(baseUrl);
        
        NetManager.get(PathManager.getXml, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                finish();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String versionUrl=jsonObj.optString("xmlPath","");
                    if(!TextUtils.isEmpty(versionUrl)){
                        new BaseAutoUpdate(mContext).CheckUpdate(versionUrl, 1, mSVProgressHUD);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @PermissionNo(100)
    private void getPermissionNo(List<String> deniedPermissions) {
        // TODO 申请权限失败。
        // 是否有不再提示并拒绝的权限。
        Toast.makeText(mContext, "申请权限失败", Toast.LENGTH_SHORT).show();
        ((Activity) mContext).finish();
        if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, deniedPermissions)) {
            // 第一种：用AndPermission默认的提示语。
            SettingService settingService = AndPermission.defineSettingDialog(SplashActivity.this, 400);
            // 你的dialog点击了确定调用：
            settingService.execute();
            // 你的dialog点击了取消调用：
            settingService.cancel();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 400: { // 这个400就是上面defineSettingDialog()的第二个参数。
                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
                checkPremession();
                break;
            }
        }
    }

    @Override
    protected void onStop() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.setSP(mContext, "isFirstDisplaySplashActivity", true);
    }
}
