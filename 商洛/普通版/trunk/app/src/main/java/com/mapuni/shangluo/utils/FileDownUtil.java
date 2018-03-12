package com.mapuni.shangluo.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.shangluo.manager.NetManager;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * @name shangluo
 * @class name：com.mapuni.shangluo.utils
 * @class describe
 * @anthor Tianfy
 * @time 2017/9/2 11:00
 * @change
 * @chang time
 * @class describe
 */

public class FileDownUtil {
    private Context context;
    private final SVProgressHUD mSvProgressHUD;

    private FileDownUtil(Context context) {
        this.context = context;
        mSvProgressHUD = new SVProgressHUD(context);
    }

    private static FileDownUtil fileDownUtil;

    public static FileDownUtil getInstance(Context context) {
        if (fileDownUtil == null) {
            fileDownUtil = new FileDownUtil(context);
        }

        return fileDownUtil;
    }

    public void downLoadWord(String url, String fileName) {

        File dirPath = FileUtils.createDir(Environment.getExternalStorageDirectory()+File.separator+"shangluoword");
        boolean isExists = FileUtils.fileIsExists(dirPath+File.separator+fileName);
        if (isExists) {//调用wps打开文件
            openFile(dirPath+File.separator+fileName);
        } else {//下载文件
            downLoad(url, dirPath.getPath(), fileName);
        }

    }

    private void downLoad(String url, final String dirPath, final String fileName) {
        mSvProgressHUD.showWithStatus("下载中....");
        NetManager.downLoadWord(url, new FileCallBack(dirPath, fileName) {
            @Override
            public void onError(Call call, Exception e, int id) {
                mSvProgressHUD.dismiss();
                Toast.makeText(context, "文件下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
            }

            @Override
            public void onResponse(File response, int id) {
                mSvProgressHUD.dismiss();
                //打开wps
                openFile(dirPath+File.separator+fileName);
            }
        });
    }

    private boolean openFile(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式  
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播  
        bundle.putString(WpsModel.THIRD_PACKAGE, WpsModel.PackageName.NORMAL); // 第三方应用的包名，用于对改应用合法性的验证  
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录  
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);

        File file = new File(path);
        if (file == null || !file.exists()) {
            Toast.makeText(context, "文件为空或者不存在", Toast.LENGTH_SHORT).show();
//            System.out.println("文件为空或者不存在");
            return false;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.mapuni.shangluo" + ".fileProvider", file);
            intent.setData(contentUri);
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
        }
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "请下载安装wps", Toast.LENGTH_SHORT).show();
//            System.out.println("打开wps异常：" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
