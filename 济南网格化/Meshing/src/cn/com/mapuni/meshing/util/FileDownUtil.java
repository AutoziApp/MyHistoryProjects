package cn.com.mapuni.meshing.util;

import java.io.File;

import com.mapuni.android.base.controls.loading.YutuLoading;
import com.zhy.http.okhttp.callback.FileCallBack;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import cn.com.mapuni.meshing.manager.NetManager;
import okhttp3.Call;

public class FileDownUtil {
	private Context context;
    private final YutuLoading yutuLoading;

    private FileDownUtil(Context context) {
        this.context = context;
        yutuLoading = new YutuLoading(context);
    }

    private static FileDownUtil fileDownUtil;

    public static FileDownUtil getInstance(Context context) {
        if (fileDownUtil == null) {
            fileDownUtil = new FileDownUtil(context);
        }

        return fileDownUtil;
    }

    public void downLoadWord(String url, String fileName) {

        File dirPath = FileUtils.createDir(Environment.getExternalStorageDirectory()+File.separator+"jinanmeshingword");
        boolean isExists = FileUtils.fileIsExists(dirPath+File.separator+fileName);
        if (isExists) {//����wps���ļ�
            openFile(dirPath+File.separator+fileName);
        } else {//�����ļ�
            downLoad(url, dirPath.getPath(), fileName);
        }

    }

    private void downLoad(String url, final String dirPath, final String fileName) {
    	yutuLoading.setLoadMsg("������....", "");
        NetManager.downLoadWord(url, new FileCallBack(dirPath, fileName) {
            @Override
            public void onError(Call call, Exception e, int id) {
            	yutuLoading.dismissDialog();
                Toast.makeText(context, "�ļ�����ʧ��", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
            }

            @Override
            public void onResponse(File response, int id) {
            	yutuLoading.dismissDialog();
                //��wps
                openFile(dirPath+File.separator+fileName);
            }
        });
    }

    private boolean openFile(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // ��ģʽ  
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // �ر�ʱ�Ƿ��͹㲥  
        bundle.putString(WpsModel.THIRD_PACKAGE, WpsModel.PackageName.NORMAL); // ������Ӧ�õİ��������ڶԸ�Ӧ�úϷ��Ե���֤  
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// ����򿪼�¼  
        // bundle.putBoolean(CLEAR_FILE, true); //�رպ�ɾ�����ļ�  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);

        File file = new File(path);
        if (file == null || !file.exists()) {
            Toast.makeText(context, "�ļ�Ϊ�ջ��߲�����", Toast.LENGTH_SHORT).show();
//            System.out.println("�ļ�Ϊ�ջ��߲�����");
            return false;
        }

        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "�����ذ�װwps", Toast.LENGTH_SHORT).show();
//            System.out.println("��wps�쳣��" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
