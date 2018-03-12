package com.mapuni.car.mvp.login.update;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.car.R;
import com.mapuni.car.app.ConsTants;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by YZP on 2017/12/19.
 */
public class BaseAutoUpdate {
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


    /**
     * Description: 更新APK文件
     *
     * @param apk_url 服务器端apk文件的路径
     * @param context 上下文对象
     * @author Administrator Create at: 2012-12-6 下午02:26:40
     */
    public void UPdateAPK(final String apk_url, final Context context, String content, DialogInterface.OnClickListener listener) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本，是否更新?");
        builder.setMessage("本次更新内容如下:\n\n" + content + "\n");
        builder.setCancelable(false);
        builder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Progressdialog(context, apk_url);
            }
        });
        builder.setNegativeButton("取消", listener);
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
    public static void Progressdialog(final Context context, final String apk_url) {
        /** 自定义进度显示Dialog */
//        Log.e("sss",apk_url);
//        Toast.makeText(context, "开始下载新版本", Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.base_auto_update, null);

        final ProgressBar bar = (ProgressBar) v.findViewById(R.id.progress_horizontal);
        final TextView number = (TextView) v.findViewById(R.id.progress_number);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

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

                        File f = new File(ConsTants.SDCARD_AutoUpdate_LOCAL_PATH);
                        if (!f.exists())
                            f.mkdirs();
                        /** 有目录之后建文件 */
                        File fil = new File(ConsTants.SDCARD_APK_LOCAL_PATH);
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
                        /** 下载完成后调用安装程序 */
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String filePath = ConsTants.SDCARD_APK_LOCAL_PATH;
                        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
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

    //    String apkUrl;
    public void CheckUpdate(final Context context, final String versionUrl, final int type, DialogInterface.OnClickListener cancelListener) {

//        this.apkUrl = apkUrl;
        new AsyncTask<String, String, String[]>() {
            @Override
            protected void onPostExecute(String[] s) {
                //有新版本
                if ("1".equals(s[0])) {
                    Toast.makeText(context, "请升级应用，否则将无法使用", Toast.LENGTH_SHORT).show();
                    showDialog(context, s, cancelListener);
                } else if ("-1".equals(s[0]) && type > 0) {
                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                } else if ("-2".equals(s[0]) && type > 0) {
                    Toast.makeText(context, "配置文件错误", Toast.LENGTH_SHORT).show();
                } else if ("0".equals(s[0]) && type > 0) {
                    Toast.makeText(context, "暂无新版本", Toast.LENGTH_SHORT).show();
                }
//                     handler.sendEmptyMessage(1001);
            }

            @Override
            protected String[] doInBackground(String... params) {
                String[] strings = new String[3];
                if (checkURL(versionUrl)) {
                    int localCode = getLocalVersion(context);
                    Log.i("Lybin", "versionCode----------" + localCode);
                    HashMap<String, String> serverMap = readVerson(params[0]);
                    if (serverMap != null && serverMap.size() > 0) {
//                        int vCode = Integer.parseInt(serverMap.get("verson"));
                        String s = serverMap.get("verson");
                        int vCode = 0;
                        try {
                            vCode = Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (vCode > localCode) {
                            /*有新版本*/
                            strings[0] = "1";
                            strings[1] = serverMap.get("updateContent");
                            strings[2] = serverMap.get("url");
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

        }.execute(versionUrl);
    }

    public static void showDialog(final Context context, final String[] content, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本，是否更新?");
        builder.setMessage("本次更新内容如下:\n\n" + content[1] + "\n");
        builder.setCancelable(false);
        builder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Progressdialog(context, content[2]);
            }
        });
        builder.setNegativeButton("取消", listener);
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
            HashMap<String, String> map = new HashMap<>();
            InputStream in = url.openStream();
            XmlPullParser xmlparser = Xml.newPullParser();
            xmlparser.setInput(in, "utf-8");
            int event = xmlparser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("verson".equals(xmlparser.getName())) {
                            String verson = xmlparser.nextText();
                            map.put("verson", verson);
                        } else if ("updateContent".equals(xmlparser.getName())) {
                            String updateContent = xmlparser.nextText();
                            map.put("updateContent", updateContent);
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


    /**
     * 测试url是否连通
     *
     * @param url
     * @return
     */
    public boolean checkURL(String url) {
        try {
            URL Url = new URL(url);
            HttpURLConnection hc = (HttpURLConnection) Url.openConnection();
            hc.setConnectTimeout(3500);// 设置超时时间3.5秒
            hc.setReadTimeout(3500);
            if (hc.getResponseCode() == 200)
                return true;
        } catch (MalformedURLException e) {
//			e.printStackTrace();
        } catch (SocketTimeoutException e) {
            return false;
//			Toast.makeText(Login.this, "服务器连接超时，请检查网络！", Toast.LENGTH_SHORT)
//					.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    //获取版本号
//    public static String setVersion(Context context) {
//        String versionName = "";
//        try {
//            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            versionName = pi.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionName;
//    }
}
