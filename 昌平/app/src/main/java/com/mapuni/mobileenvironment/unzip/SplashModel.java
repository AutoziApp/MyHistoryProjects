package com.mapuni.mobileenvironment.unzip;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

import com.mapuni.mobileenvironment.R;
import com.mapuni.mobileenvironment.config.PathManager;

import java.io.File;

/**
 * Created by: K on 2015/11/24.
 * Description:
 */
public class SplashModel {

    public static final String VERSION_NAME = "versionName";

    public boolean checkNewVersion(){
        return false;
    }

    /**
     * 判断本地的版本号是否与app的版本号一直
     * @param context 上下文
     * @return
     */
    public boolean isLocalVersionEqualAppVersion(Context context){
        try {
            String localVersion = getLocalVersion(context);
            String appVersion = getAppVersion(context);
            return localVersion.equals(appVersion);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    };

    /**
     * 获取版本号
     * @param context 上下文
     * @return 版本号
     */
    public String getAppVersion(Context context){
        PackageManager pm = context.getPackageManager();
        String p = context.getPackageName();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),0);
            return pm.getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取本地的version
     * @param context 上下文
     * @return 本地version
     */
    public String getLocalVersion(Context context){
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getString(VERSION_NAME,"0.0");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0.0";
    }

    /**
     * 判断是否需要解压
     * @param context 上下文
     * @return 是否需要解压
     */
    public boolean isNeedUnzip(Context context){
        if(isLocalVersionEqualAppVersion(context)){
            //app的版本号与本地版本号相同需要进行判断本地文件是否存在
            Log.i("Lybin","版本号相同");
            File file = new File(PathManager.SDCARD_DB_LOCAL_PATH);
            if(file.exists()){
                Log.i("Lybin","数据库已经存在");
                return false;
            }else{
                Log.i("Lybin","数据库不存在");
                return true;
            }
        }else{
            //app的版本号与本地版本号不同则需要解压
            Log.i("Lybin","版本号有更新");
            return true;
        }
    }

    /**
     * 更新版本号
     * @param context 上下文
     * @return 是否成功
     */
    public boolean updateLocalVersion(Context context){
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(VERSION_NAME,getAppVersion(context));
            editor.commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解压文件
     * @param context 上下文
     * @param pBarUnZip 进度显示条
     * @param unZipCallBack 回掉接口
     */
    public void unZip(final Context context, final ProgressBar pBarUnZip, final UnZipCallBack unZipCallBack){
        new AsyncTask<Void, Integer,Boolean>(){
            //准备
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            //执行
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    new FileUnZip(new UnZipProgressUpdateInterface() {
                        @Override
                        public void unzipSingleFile(String fileName, int progress) {
                            publishProgress(progress);
                        }
                        @Override
                        public void unzipGroupFile(int progress) {

                        }
                    }).unzip(context.getResources().openRawResource(R.raw.mobilegrid));
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
            //过程反馈
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                pBarUnZip.setProgress(values[0]);
            }
            //结果
            @Override
            protected void onPostExecute(Boolean isOk) {
                super.onPostExecute(isOk);
                unZipCallBack.callback(isOk);
            }
        }.execute();
    }

    /**
     * 判断解压是否完成的回调
     */
    public interface UnZipCallBack{
        /**
         * 回调返回解压是否完成
         * @param isOk 是否完成
         */
        void callback(boolean isOk);
    }

//    public String getIp(Context context){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        if(sp.contains("isAPN")){
//            if("1".equals(sp.getString("isAPN","1"))){
//                if(sp.contains("WebServiceBaseUrlAPN")){
//                    PathManager.setApnIp(sp.getString("WebServiceBaseUrlAPN", PathManager.WebServiceBaseAPNUrl));
//                }else{
//                    PathManager.setApnIp(PathManager.WebServiceBaseAPNUrl);
//                }
//            }else{
//                if(sp.contains("WebServiceBaseUrlWIFI")){
//                    PathManager.setWifiIp(sp.getString("WebServiceBaseUrlWIFI", PathManager.WebServiceBaseWIFIUrl));
//                }else{
//                    PathManager.setWifiIp(PathManager.WebServiceBaseWIFIUrl);
//                }
//            }
//        }else{
//            if(sp.contains("WebServiceBaseUrlAPN")){
//                PathManager.setApnIp(sp.getString("WebServiceBaseUrlAPN", PathManager.WebServiceBaseAPNUrl));
//            }else{
//                PathManager.setApnIp(PathManager.WebServiceBaseAPNUrl);
//            }
//        }
//        return PathManager.WebServiceBaseUrl;
//    }
}
