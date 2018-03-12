package com.mapuni.shangluo.model;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mapuni.shangluo.bean.LoginResultBean;
import com.mapuni.shangluo.manager.NetManager;
import com.mapuni.shangluo.presenter.LoginPresenter;
import com.mapuni.shangluo.utils.ExampleUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;

import static com.mapuni.shangluo.app.MyApplication.getContextObject;

/**
 * Created by Administrator on 2017/8/14.
 */

public class LoginModel {

    private LoginPresenter mLoginPresenter;

    public LoginModel(LoginPresenter loginPresenter) {
        this.mLoginPresenter = loginPresenter;
    }

    public void login(final String userName, String passWord, String log, String lat) {
        //请求网络
        NetManager.requestLogin(userName, passWord,log,lat, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mLoginPresenter.onError(e.getMessage());
                Log.i("aaa", "onError:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("aaa", response.toString());
                Gson gson = new Gson();
                LoginResultBean bean = gson.fromJson(response.toString(), LoginResultBean.class);
                switch (bean.getStatus()) {
                    case 0://认证成功
                        mLoginPresenter.onSuccess(bean);
                        setAlias(userName);
                        break;
                    case -1://用户不存在
                        mLoginPresenter.onError("用户不存在");
                        break;
                    case -2://密码错误
                        mLoginPresenter.onError("密码错误");
                        break;
                    case -3://用户被禁用
                        mLoginPresenter.onError("用户被禁用");
                        break;
                    case -4://数据交互失败，请联系管理员
                        mLoginPresenter.onError("数据交互失败，请联系管理员");
                        break;
                    case -5://无登录权限
                        mLoginPresenter.onError("无登录权限");
                        break;
                }
            }
        });
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(getContextObject(), "用户名为空导致设置别名失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(getContextObject(), "用户名不规范导致设置别名失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("aaa", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("aaa", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("aaa", logs);
            }
//			ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("aaa", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getContextObject(),
                            (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.i("aaa", "Unhandled msg - " + msg.what);
            }
        }
    };

}
