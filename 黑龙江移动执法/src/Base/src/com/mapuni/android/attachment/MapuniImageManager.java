package com.mapuni.android.attachment;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * ͼƬ�������������ܳ��״����������
 * 
 * @author Sahadev
 * 
 */
public class MapuniImageManager {

	/**
	 * ���ｫҪ�������غõ��ļ�·��,�����߳���ʹ��
	 * 
	 * @param guid
	 *            T_ATTACHMENT���� �ļ���GUID
	 * @return ���غõ��ļ�·��
	 */
	public static File downLoadImage(String guid) {
		String sql = "select fk_unit,extension ,filepath from T_Attachment where guid = '" + guid + "'";
		HashMap<String, Object> map = SqliteUtil.getInstance().getDataMapBySqlForDetailed(sql);

		int fk_unit = Integer.parseInt(map.get("fk_unit").toString());
		String extension = map.get("extension").toString();
		String filepath = map.get("filepath").toString();
		// ƴ��ͼƬ�ķ�������ַ
//		String strUrl = Global.getGlobalInstance().getSystemurl() + "/Attach/" + T_Attachment.transitToChinese(fk_unit) + "/" + guid + extension;
		String strUrl = Global.getGlobalInstance().getSystemurl() + filepath;
		String path = FileHelper.SDCardPath + "mapuni/MobileEnforcement/" + "Attach/" + T_Attachment.transitToChinese(fk_unit) + "/";
		String fileName = guid + extension;

		return FileHelper.fileDownloadReturnFile(strUrl, path, fileName);
	}

	/**
	 * ���������ظ��������򿪣������߳��е���
	 * 
	 * @param guid
	 * @param context
	 */
	public static void showImageFromNet(final String guid, final Context context) {
		final YutuLoading loading = new YutuLoading(context);
		loading.setLoadMsg("���ڼ��ظ��������Ե�...", "");
		loading.showDialog();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				FileHelper.openFile((File) msg.obj, context);
				loading.dismissDialog();
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = downLoadImage(guid);
				handler.sendMessage(msg);
			}
		}).start();
	}
}
