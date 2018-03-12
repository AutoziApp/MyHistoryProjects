package com.mapuni.mobileenvironment.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.mapuni.mobileenvironment.app.DataApplication;
import com.mapuni.mobileenvironment.bean.LogonUser;
import com.mapuni.mobileenvironment.config.PathManager;
import com.mapuni.mobileenvironment.update.BaseAutoUpdate;
import com.mapuni.mobileenvironment.utils.CyptoUtils;
import com.mapuni.mobileenvironment.utils.SqliteUtil;
import com.mapuni.mobileenvironment.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class LoginModel {
    private boolean isAuto;
    private boolean isRecord;
    private String userName;
    private String passWord;
    /* *上下文 */
    private Context context;
    private static BaseAutoUpdate baseAutoUpdate;
    public LoginModel(Context context) {
        this.context = context;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            isRecord = sp.getBoolean("isRecord", false);
//            isAuto = sp.getBoolean("isAuto", false);
            userName = CyptoUtils.base64Decode(sp.getString("userName", ""));
            passWord = CyptoUtils.base64Decode(sp.getString("passWord", ""));
        }catch (Exception e){

        }
    }
    public static void update(Context context,int type) {
        if(baseAutoUpdate == null){
            baseAutoUpdate = new BaseAutoUpdate();
        }
        baseAutoUpdate.CheckUpdate(context,"http://218.246.81.181:8128/AppUpdate/version.xml",type);
        File file = new File(PathManager.SDCARD_DB_LOCAL_PATH);
        StringBuffer path = new StringBuffer(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        File file2 = new File(path.toString()+"/mobilegrid");
        if(file2.exists()){
            Log.i("Lybin","数据库成功解压");
        }else{
            Log.i("Lybin","数据库解压失败");
        }
    }

    /**
     * 登录
     * @param userName 用户名
     * @param passWord 密码
     * @return 是否成功
     */
    public boolean login(String userName, String passWord){
        if(StringUtils.isEmpty(userName)){
            Toast.makeText(context,"用户名不能为空", Toast.LENGTH_LONG).show();
            return false;
        }else if(StringUtils.isEmpty(passWord)){
            Toast.makeText(context,"密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }else{
            String passMD5 = CyptoUtils.MD5Encode(passWord);
            ArrayList<HashMap<String,Object>> map = SqliteUtil.getInstance()
                    .queryBySqlReturnArrayListHashMap("select * from Base_UserInfo where User_Account = '"+userName+"'");
            String password = (String)map.get(0).get("user_pwd");
      //      Toast.makeText(context,"password : "+password,Toast.LENGTH_LONG).show();

            if(map == null||map.size() == 0){
                Toast.makeText(context,"用户名不存在", Toast.LENGTH_LONG).show();
                return false;
            }else if(!(map.get(0).containsKey("user_pwd")&&passMD5.equals(map.get(0).get("user_pwd")))){
                Toast.makeText(context,"密码错误", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        this.userName = userName;
        this.passWord = passWord;
        initLoginUser();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isRecord", this.isRecord);
        editor.putBoolean("isAuto", this.isAuto);
        if(isRecord){
            editor.putString("userName", userName);
            editor.putString("passWord", passWord);
        }
        editor.commit();
        sp.edit();
        return true;
    }

    public void putPassWord(String userName, String passWord){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isRecord", this.isRecord);
        if(isRecord){
            editor.putString("userName", CyptoUtils.base64Encode(userName));
            editor.putString("passWord", CyptoUtils.base64Encode(passWord));
        }
        editor.commit();
        sp.edit();
    }

    /** 初始化登录用户 */
    public void initLoginUser(){
        DataApplication.LogonUser = new LogonUser();
        DataApplication.LogonUser.setUserName(userName);
        DataApplication.LogonUser.setCountyName(passWord);
    }

    public boolean isAuto() {
        return this.isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public boolean isRecord() {
        return this.isRecord;
    }

    public void setRecord(boolean isRecord) {
        this.isRecord = isRecord;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return passWord;
    }
}
