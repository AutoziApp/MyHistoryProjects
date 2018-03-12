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
 * 图片管理，类名并不能彻底代表本类的作用
 * 
 * @author Sahadev
 * 
 */
public class MapuniImageManager {

	/**
	 * 这里将要返回下载好的文件路径,在子线程中使用
	 * 
	 * @param guid
	 *            T_ATTACHMENT表中 文件的GUID
	 * @return 下载好的文件路径
	 */
	public static File downLoadImage(String guid) {
		String sql = "select fk_unit,extension ,filepath from T_Attachment where guid = '" + guid + "'";
		HashMap<String, Object> map = SqliteUtil.getInstance().getDataMapBySqlForDetailed(sql);

		int fk_unit = Integer.parseInt(map.get("fk_unit").toString());
		String extension = map.get("extension").toString();
		String filepath = map.get("filepath").toString();
		// 拼接图片的服务器地址
//		String strUrl = Global.getGlobalInstance().getSystemurl() + "/Attach/" + T_Attachment.transitToChinese(fk_unit) + "/" + guid + extension;
		String strUrl = Global.getGlobalInstance().getSystemurl() + filepath;
		String path = FileHelper.SDCardPath + "mapuni/MobileEnforcement/" + "Attach/" + T_Attachment.transitToChinese(fk_unit) + "/";
		String fileName = guid + extension;

		return FileHelper.fileDownloadReturnFile(strUrl, path, fileName);
	}

	/**
	 * 从网络下载附件，并打开，在主线程中调用
	 * 
	 * @param guid
	 * @param context
	 */
	public static void showImageFromNet(final String guid, final Context context) {
		final YutuLoading loading = new YutuLoading(context);
		loading.setLoadMsg("正在加载附件，请稍等...", "");
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
