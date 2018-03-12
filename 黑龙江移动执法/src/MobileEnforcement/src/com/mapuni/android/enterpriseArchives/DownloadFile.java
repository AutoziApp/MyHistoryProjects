package com.mapuni.android.enterpriseArchives;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.mapuni.android.base.util.DisplayUitl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadFile extends AsyncTask<String, Void, Integer> {
	private InputStream inputStream;
	private URLConnection connection;
	private HttpURLConnection conn;
	private OutputStream outputStream;
	private int DownedFileLength = 0;
	private int FileLength;
	String savePathString;
	ProgressDialog progressBar;
	ProgressBar bar;
	Context context;

	public DownloadFile(Context context) {

		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressBar = new ProgressDialog(context);
		progressBar.setCanceledOnTouchOutside(false);
		progressBar.setMessage("正在拼命加载中..........");
		progressBar.show();

	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			URL url = new URL(params[0]);
			conn = (HttpURLConnection) url.openConnection();
			try {
				inputStream = conn.getInputStream();
			} catch (Exception e) {
				e.printStackTrace();
				outputStream = null;
				return 0;
			}

			if (inputStream == null) {
				return 1;
			}

			String savePAth = params[1].substring(0, params[1].lastIndexOf("/"));
			File file1 = new File(savePAth);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			savePathString = params[1];

			File file = new File(savePathString);

			file.createNewFile();

			outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			while (true) {
				int temp = inputStream.read(buffer, 0, buffer.length);
				if (temp == -1) {
					break;
				}
				outputStream.write(buffer, 0, temp);
			}
			outputStream.flush();
			return 2;
		} catch (Exception e) {
			conn.disconnect();
			return 0;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			conn.disconnect();
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		progressBar.dismiss();
		switch (result) {

		case 0:
			Toast.makeText(context, "网络异常或服务器连接地址有误！", 0).show();
			break;
		case 1:
			Toast.makeText(context, "服务器端不存在该附件", 0).show();
			break;
		case 2:
			DisplayUitl.openfile(savePathString, context);
			break;
		default:
			break;
		}
	}

}
