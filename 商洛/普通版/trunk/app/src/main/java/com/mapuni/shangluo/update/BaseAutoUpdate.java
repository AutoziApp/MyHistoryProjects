package com.mapuni.shangluo.update;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mapuni.shangluo.activity.loginAc.LoginActivity;
import com.mapuni.shangluo.R;
import com.mapuni.shangluo.utils.SPUtils;
import com.mapuni.shangluo.utils.ZipUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * * FileName: AutoUpdate.java Description: 自动更新 <li>DataSync 读取config 的版本号
 * readVersion <li>Main 监测新版本 JudgeNewVerson UPdateAPK 更新 用不到了 <li>
 * LoadGridLayout 监测新版本 JudgeNewVerson UPdateAPK 更新 用不到了
 */
public class BaseAutoUpdate {
    //设置解压目的路径
    public static String OUTPUT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/shangluo";
    /**
     * 脚本升级失败
     */
    protected final int STATE_FAILURE = 0;
    /**
     * 连接服务器超时
     */
    protected final int STATE_NETWORK_ERROR = 1;
    /**
     * 脚本升级成功
     */
    protected final int STATE_SUCCESS = 2;
    /**
     * 连接不上服务器
     */
    protected final int STATE_NETWORK_FALURE = 3;
    /**
     * 已是最新
     */
    protected final int STATE_NODATA = 4;

    protected final String TAG = "BaseAutoUpdate";

    private HashMap<String, String> versonHashMap;

    private Context context;

    public BaseAutoUpdate(Context context) {
        this.context = context;
    }

    /**
     * Description: 更新APK文件
     *
     * @param apk_url 服务器端apk文件的路径
     * @param context 上下文对象
     * @author Administrator Create at: 2012-12-6 下午02:26:40
     */
    public void UPdateAPK(final String apk_url, final Context context, String content) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本，是否更新?");
        builder.setMessage("本次更新内容如下:\n\n" + content + "\n");
        builder.setCancelable(false);
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Progressdialog(context, apk_url);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog adialog = builder.create();
//		adialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        adialog.show();
    }


    /**
     * 新版本apk的备注信息
     */
    String text = "";

    /**
     * Description: 下载新版本apk
     *
     * @param context 上下文
     * @param apk_url 服务器端存放apk的文件地址
     * @author 王红娟 Create at: 2012-12-6 下午02:40:07
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Progressdialog(final Context context, final String apk_url) {
        /** 自定义进度显示Dialog */
        Toast.makeText(context, "开始下载新版本", Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.base_auto_update, null);

        final ProgressBar bar = (ProgressBar) v.findViewById(R.id.progress_horizontal);
        final TextView number = (TextView) v.findViewById(R.id.progress_number);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//            }
//
//        });
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.show();


        final Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

//				 build.addAction(R.drawable.stat_sys_download_anim0, "应急系统", pi).setContentText(msg.arg1 + "%");
//				 build.notify();
////				notify.setLatestEventInfo(context, "应急系统", msg.arg1 + "%", pi);
//				manager.notify(0, notify);
                bar.setProgress(msg.arg1);
                number.setText(msg.arg1 + "");

                if (msg.arg1 == 100) {
                    dialog.dismiss();
                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = null;
                    url = new URL(apk_url);
                    HttpURLConnection con = null;
                    con = (HttpURLConnection) url.openConnection();
                    long length = con.getContentLength();
                    InputStream in = null;
                    in = con.getInputStream();
                    FileOutputStream fos = null;
                    if (in != null) {
                        /** 如果没有目录先建立目录 */
                        File f = new File(PathManager.SDCARD_AutoUpdate_LOCAL_PATH);
                        if (!f.exists())
                            f.mkdirs();
                        /** 有目录之后建文件 */
                        File fil = new File(PathManager.SDCARD_APK_LOCAL_PATH);
                        fos = new FileOutputStream(fil);
                        byte[] bytes = new byte[1024 * 8];
                        int flag = -1;
                        /** 文件总字节长度 */
                        int count = 0;
                        int lastper = 0;
                        /** 若未读到文件末尾则一直读取 */
                        while ((flag = in.read(bytes)) != -1) {
                            fos.write(bytes, 0, flag);
                            count += flag;
                            double percent = (double) count / (double) length;
                            int per = (int) (percent * 100);
                            if (per - lastper >= 1 || per == 1) {
                                Message msg = new Message();
                                msg.arg1 = per;
                                h.sendMessage(msg);
                                lastper = per;
                            }
                        }
                        fos.flush();
                        fos.close();
                        /** 下载完成后取消进度Dialog */
                        // pd.cancel();
                        /**
                         * 下载完成后调用安装程序(打开安装界面的同时 关闭自己 防止点击安装界面的取消后重新进入闪屏界面)
                         */
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String filePath = PathManager.SDCARD_APK_LOCAL_PATH;
                        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }
                } catch (MalformedURLException e) {
                    //    ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
                    e.printStackTrace();
                } catch (IOException e) {
                    //     ExceptionManager.WriteCaughtEXP(e, "AutoUpdate");
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    public SVProgressHUD mSVProgressHUD;

    public void CheckUpdate(String versionUrl, int type, SVProgressHUD sVProgressHUD) {
//        this.mSVProgressHUD = sVProgressHUD;
//        mSVProgressHUD.showWithStatus("检查更新中...");
        new UpdateAsyncTask(context, versionUrl, type).execute(versionUrl);
    }


    class UpdateAsyncTask extends AsyncTask<String, String, String[]> {
        private Context context;
        private String versionUrl;
        private int type;

        public UpdateAsyncTask(Context context, String versionUrl, int type) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.versionUrl = versionUrl;
            this.type = type;
        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] strings = new String[4];
            if (Net.checkURL(versionUrl)) {
                int localCode = getLocalVersion(context);
                Log.i("Lybin", "versionCode----------" + localCode);
                HashMap<String, String> serverMap = readVerson(params[0]);
                if (serverMap != null && serverMap.size() > 0) {
//                    int vCode = Integer.parseInt(serverMap.get("verson"));
                    String s = serverMap.get("verson");
                    int vCode = 0;
                    try {
                        vCode = Integer.parseInt(s);
                        Log.i("Lybin", "ServerVersionCode----------" + vCode);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (vCode > localCode) {
                        /*有新版本*/
                        strings[0] = "1";
                        strings[1] = serverMap.get("updateContent");
//                        strings[2] = serverMap.get("url");
                        strings[2] = com.mapuni.shangluo.manager.PathManager.downloadApk;
                        strings[3] = serverMap.get("forcedUpdate");
                    } else {
                        /*无新版本*/
                        strings[0] = "0";
                    }
                } else {
                     /*配置文件失败*/
                    strings[0] = "-2";
                }

            } else {
                /*网络错误*/
                strings[0] = "-1";
            }
            return strings;
        }

        @Override
        protected void onPostExecute(String[] s) {
            //有新版本
            Log.i("tianfy", s[0] + "-----" + type);
            if ("1".equals(s[0])) {
//                mSVProgressHUD.dismiss();
                showDialog(context, s);
            } else if ("-1".equals(s[0]) && type > 0) {
                gotoLoginActivity("网络错误");
            } else if ("-2".equals(s[0]) && type > 0) {
                gotoLoginActivity("配置文件错误");
            } else if ("0".equals(s[0]) && type > 0) {
                gotoLoginActivity("已是最新版本");
            }
        }
    }

    private void gotoLoginActivity(final String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                hideProgressDialog();
//                mSVProgressHUD.showInfoWithStatus(msg, SVProgressHUD.SVProgressHUDMaskType.Black);
//                if (!(boolean) SPUtils.getSp(context, "isUnZip", false)) {//屏蔽解压
                if (false) {
                    unZip();
                } else {
                    gotoLoginAcitivity2();
                }
            }

        }, 1500);
    }

    private void gotoLoginAcitivity2() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mSVProgressHUD.dismiss();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                ((Activity) context).finish();
            }
        }, 1000);
    }

//    private void hideProgressDialog() {
//        if (mSVProgressHUD.isShowing()) {
//            mSVProgressHUD.dismissImmediately();
//        }
//    }

    public void showDialog(final Context context, final String[] content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本，是否更新?");
        builder.setMessage("本次更新内容如下:\n\n" + content[1] + "\n");
        builder.setCancelable(false);
        builder.setIcon(context.getResources().getDrawable(R.drawable.logo));
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                
                Progressdialog(context, content[2]);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                
                // TODO Auto-generated method stub
                if ("true".equals(content[3])) {//强制更新 不更新就退出应用
                    ((Activity) context).finish();
                } else {//正常更新 可取消
                    dialog.dismiss();
                    
                    gotoLoginActivity("即将解压资源文件...");

                }
            }
        });
        AlertDialog adialog = builder.create();
        adialog.show();
    }

    /**
     * 获取本地版本
     *
     * @param context
     * @return
     */
    public int getLocalVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Description: 读取服务器apk版本号文件XML中的Verson
     *
     * @param Url 服务器放置apk版本号信息的xml文件路径
     * @return 版本号信息 HashMap<String,String>
     */
    public HashMap<String, String> readVerson(String Url) {
        try {
            URL url = new URL(Url);
            HashMap<String, String> map = new HashMap<String, String>();
            InputStream in = url.openStream();
            XmlPullParser xmlparser = Xml.newPullParser();
            xmlparser.setInput(in, "utf-8");
            int event = xmlparser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("versonCode".equals(xmlparser.getName())) {
                            String verson = xmlparser.nextText();
                            map.put("verson", verson);
                        } else if ("updateContent".equals(xmlparser.getName())) {
                            String updateContent = xmlparser.nextText();
                            map.put("updateContent", updateContent);
                        } else if ("forcedUpdate".equals(xmlparser.getName())) {//是否强制更新
                            String forcedUpdate = xmlparser.nextText();
                            map.put("forcedUpdate", forcedUpdate);
                        } else if ("url".equals(xmlparser.getName())) {
                            String _URL = xmlparser.nextText();
                            map.put("url", _URL);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = xmlparser.next();
            }
            return map;
        } catch (Exception e) {

        }
        return null;
    }


    private void unZip() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mSVProgressHUD.dismissImmediately();
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setTitle("提示");
                dialog.setMessage("正在解压文件，请稍后！");
                dialog.show();//显示对话框
                new Thread() {
                    public void run() {
                        //在新线程中以同名覆盖方式解压
                        try {
                            ZipUtils.unZip(context, "map.zip", OUTPUT_DIRECTORY, true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        SPUtils.setSP(context, "isUnZip", true);
                        dialog.cancel();//解压完成后关闭对话框
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
                        ((Activity) context).finish();
                    }
                }.start();
            }
        }, 1500);

    }
}
