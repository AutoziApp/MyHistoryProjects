package com.mapuni.android.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.digitalchina.gallery.ImageGalleryActivity;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.attachment.T_BAS_Attachment;
import com.mapuni.android.base.adapter.ButtonListAdapter;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.netprovider.Net;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ButtonListDialog extends Dialog implements OnItemClickListener {
	private Context mContext;
	private ArrayList<T_BAS_Attachment> mAttachment;
	private ListView listView;
	private ButtonListAdapter adapter;
	private FileHelper helper;
	public ButtonListDialog(Context context) {
		super(context);
		this.mContext = context;
	}
	public ButtonListDialog(Context context,ArrayList<T_BAS_Attachment> attachment){
		super(context);
		this.mContext = context;
		this.mAttachment = attachment;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buttonlistdialog);
		setTitle("请选择要查看的附件");
		listView = (ListView) findViewById(R.id.list);
		adapter = new ButtonListAdapter(mContext, mAttachment);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		helper = new FileHelper();
		
	}
	
	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final T_BAS_Attachment attachment = mAttachment.get(arg2);
		String extra = "no";
		final String extension = attachment.getExtension();
		final String filepath = attachment.getFilePath();
		final String fileName = attachment.getFileName();
	/*	final String strUrl = Global.getGlobalInstance()
				.getSystemurl()+ "/Attach/" + T_Attachment.transitToChinese(Integer.parseInt(attachment.getFK_Unit())) + "/"
				+attachment.getFilePath();*/
		final String strUrl = Global
				.getGlobalInstance()
				.getSystemurl()
				+attachment.getFilePath();
		
		final String path = Environment.getExternalStorageDirectory() + "/mapuni/MobileEnforcement/" + "Attach/"
				+ T_Attachment.transitToChinese(Integer.parseInt(attachment.getFK_Unit())) + "/";

		final String p = Global.SDCARD_RASK_DATA_PATH + "Attach/"
				+ T_Attachment.transitToChinese(Integer.parseInt(attachment.getFK_Unit()))  +"/"+ fileName+extension;
		int result = 1;
		// 判断本地是否有文件，如果没有则下载。
		Log.i("info", "存在" + FileHelper.isFileExist(p));
		
		
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				File file;
				switch (msg.what) {
				
				case 0:
					cancel();
					Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
					if (extension.equalsIgnoreCase(".jpg")
							|| extension.equalsIgnoreCase(".png")
							|| extension.equalsIgnoreCase(".bmp")) {
						ArrayList<String> arrayTotal = new ArrayList<String>();
						arrayTotal.add(p);
						Intent intent = new Intent(mContext,
								ImageGalleryActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("arrayTotal", arrayTotal);
						bundle.putString("attch", T_Attachment.transitToChinese(Integer.parseInt(attachment.getFK_Unit())));
						intent.putExtras(bundle);
						mContext.startActivity(intent);	
					} else if (extension.equalsIgnoreCase(".mp4")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.VIDEO.getType(), "",
								false, mContext, file);
					} else if (extension.equalsIgnoreCase(".amr")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.AUDIO.getType(), "",
								false, mContext, file);
					} else if (extension.equalsIgnoreCase(".doc")
							|| extension.equalsIgnoreCase(".docx")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.WORD.getType(), "",
								false, mContext, file);
					} else if (extension.equalsIgnoreCase(".xlsx")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.EXCEL.getType(), "",
								false, mContext, file);
					} else if (extension.equalsIgnoreCase(".pdf")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.PDF.getType(), "",
								false, mContext, file);
					} else if (extension.equalsIgnoreCase(".ppt")
							|| extension.equalsIgnoreCase(".dps")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.PPT.getType(), "",
								false, mContext, file);
					}else if (extension.equalsIgnoreCase(".txt")) {
						file = new File(p);
						FileHelper.OpenFile(
								FileHelper.FileType.TXT.getType(), "",
								false, mContext, file);
						
					}
					
					break;
				case 1:
					Toast.makeText(mContext, "下载失败！", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(mContext, "附件不存在", Toast.LENGTH_SHORT).show();
					break;
				case 1001:
					Toast.makeText(mContext, "开始尝试下载", Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		};
		
		if (!FileHelper.isFileExist(p)) {
			if (!Net.checkURL(strUrl)) {
				Toast.makeText(mContext, "暂无附件！", Toast.LENGTH_SHORT)
						.show();
			} else {
				// extra先置为yes防止下载附件的时候弹出暂无附件提示
				extra = "yes";
				
				
				// 下载任务放在线程中进行
				new Thread() {
					public void run() {
						handler.sendEmptyMessage(1001);
						Log.i("info", "strUrl:" + strUrl);
						int res = helper.fileDownload(strUrl, path,
								fileName+extension);
						handler.sendEmptyMessage(res);
					};
				}.start();

			}
		} else {
			result = 0;
		}
		Log.i("info", "extension:" + extension);

		if (result == 1||result == 0) {
			File file;
			if (extension.equalsIgnoreCase(".jpg")
					|| extension.equalsIgnoreCase(".png")
					|| extension.equalsIgnoreCase(".bmp")) {
				ArrayList<String> arrayTotal = new ArrayList<String>();
				arrayTotal.add(p);
				Intent intent = new Intent(mContext,
						ImageGalleryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("arrayTotal", arrayTotal);
				bundle.putString("attch", T_Attachment.transitToChinese(Integer.parseInt(attachment.getFK_Unit())));
				intent.putExtras(bundle);
				mContext.startActivity(intent);	
				
			} else if (extension.equalsIgnoreCase(".mp4")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.VIDEO.getType(), "",
						false, mContext, file);
				extra = "yes";
			} else if (extension.equalsIgnoreCase(".amr")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.AUDIO.getType(), "",
						false, mContext, file);
				extra = "yes";
			} else if (extension.equalsIgnoreCase(".doc")
					|| extension.equalsIgnoreCase(".docx")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.WORD.getType(), "",
						false, mContext, file);
				extra = "yes";
			} else if (extension.equalsIgnoreCase(".xlsx")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.EXCEL.getType(), "",
						false, mContext, file);
				extra = "yes";
			} else if (extension.equalsIgnoreCase(".pdf")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.PDF.getType(), "",
						false, mContext, file);
				extra = "yes";
			} else if (extension.equalsIgnoreCase(".ppt")
					|| extension.equalsIgnoreCase(".dps")||extension.equalsIgnoreCase(".pptx")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.PPT.getType(), "",
						false, mContext, file);
				extra = "yes";
			}else if (extension.equalsIgnoreCase(".txt")) {
				file = new File(p);
				FileHelper.OpenFile(
						FileHelper.FileType.TXT.getType(), "",
						false, mContext, file);
				extra = "yes";
			}
		}

	
		
	}
	
}
