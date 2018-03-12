package com.mapuni.caremission_ens.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapuni.caremission_ens.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.mapuni.caremission_ens.bean.FileBean;
import com.mapuni.caremission_ens.config.PathManager;
import com.mapuni.caremission_ens.views.ButtonProgressBar;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yawei on 2017/4/1.
 */

public class DownLoadFile {
    private Context mContext;
    private NetControl netControl;
    private List<View> bars;
    private ButtonProgressBar bar;
    List fileNameList;
    AssemblyRecyclerAdapter adapter;
    public DownLoadFile(Context context,AssemblyRecyclerAdapter adapter){
        this.adapter = adapter;
        mContext = context;
        getListFiles();
//        Dir = context.getFilesDir()+"/pdf";
    }
//    String s = "http://d2.apk8.com:8020/soft/dingdangkuaiyao.apk";
    public void down(ButtonProgressBar progressBar, String url, int id,String fileName){
        Log.i("Lybin",url);
        Log.i("Lybin",fileName);
        Log.i("Lybin",id+"");
        bar = progressBar;
        if(netControl==null)
            netControl = new NetControl();
//        netControl.downFile(url,call,id);
        Log.e("zqq===",PathManager.Dir);
        netControl.downFile(url,new Call(progressBar, PathManager.Dir,fileName),id);
    }


      class Call extends FileCallBack{
          String fileName;
          ButtonProgressBar bar;
        public Call(final ButtonProgressBar bar,String Dir,final String fileName){
            super(Dir,fileName);
            this.fileName = fileName;
            this.bar = bar;
        }
        @Override
        public void onError(okhttp3.Call call, Exception e, int id) {
            Log.i("Lybin","onError----"+e.toString());
            bar.setStatus(ButtonProgressBar.Status.End);
            Toast.makeText(mContext,fileName+",此文件已删除",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            Log.i("Lybin","inProgress----"+progress+"");

            if (Math.round(progress * 100) < 100) {
                bar.setProgress(Math.round(progress * 100));
            } else {
                bar.setProgress(100);
            }
        }
        @Override
        public void onResponse(File response, int id) {
            Log.i("Lybin","onResponse----"+response.toString());
            bar.setStatus(ButtonProgressBar.Status.End);
            if(fileNameList==null)
                getListFiles();
            if(fileNameList.size()>0&&!fileNameList.contains(response.getName())){
                FileBean bean = new FileBean();
                    bean.setFILENAME(response.getName());
                    bean.setLocalSize(byteToMb(response.length()));
                    bean.setLENGTH(response.length()+"");
                    fileNameList.add(bean);

            }
            adapter.notifyDataSetChanged();
            Toast.makeText(mContext,fileName+",完成下载",Toast.LENGTH_SHORT).show();
        }
    }

    public List getListFiles(){
        File f = new File(PathManager.Dir);
        if (!f.exists()) {
            return null;
        }
        File fa[] = f.listFiles();
        fileNameList = new ArrayList();
        for(File file :fa){
            FileBean bean = new FileBean();
            bean.setFILENAME(file.getName());
            bean.setLocalSize(byteToMb(file.length()));
            fileNameList.add(bean);
        }
        fa = null;
        return fileNameList;
    }

    private String byteToMb(Long l){
        long mb = 1024*1024;
        return  l/mb+"";
    }

}
