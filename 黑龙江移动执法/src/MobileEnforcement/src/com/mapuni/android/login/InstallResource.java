package com.mapuni.android.login;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import android.content.Context;
import android.os.AsyncTask;

import com.mapuni.android.MobileEnforcement.R;

/*
 * �첽��Դ�ͷŹ���       ֧�������ļ�����·��
 * create by frimmon
 * 2013.09.18
 * */
public class InstallResource extends AsyncTask<Integer, Integer, Integer> {
	private Context context;
	private static final String ALGORITHM = "PBEWithMD5AndDES";
	private CircleProgressDialog cdialog;
	private int fileSize;
	private Loadlisenter loadlisenter;

	public interface Loadlisenter {
		public void LoadResult(int status);
	}

	public InstallResource(Context context) {
		this.context = context;
	}

	public void SetLisenter(Loadlisenter loadlisenter) {
		this.loadlisenter = loadlisenter;
	}

	public void Start(String path) {

		// ֻ�����ݿ�İ汾��ֻҪ��ɾ�����ݿ�
		// File configFile = new
		// File(path+"/MobileEnforcement/data/config.xml");
		// RecursionDeleteFile(configFile);
		// File DBfile = new
		// File(path+"/MobileEnforcement/data/MobileEnforcement.db");
		// RecursionDeleteFile(DBfile);

		// ֻ����ͼ�İ汾��ֻҪ��ɾ����ͼ�ļ���
		// File MAPfile = new File(path+"/map");
		// RecursionDeleteFile(MAPfile);

		// ��mapuni�����ļ��еİ汾��Ҫ��ɾ�������ļ���
		// File file = new File( path );
		// RecursionDeleteFile(file);

		// ѹ����������ͳһΪmapuni.zip
		// if ( file == null || !file.exists() || !file.isDirectory() ||
		// file.listFiles() == null || file.listFiles().length == 0 )
		// {
		//ִ���첽����
		execute(R.raw.mapuni);
		// }
	}

	@Override
	// ִ��Ԥ����������UI�߳�
	protected void onPreExecute() {
		super.onPreExecute();
		cdialog = new CircleProgressDialog(context);
		cdialog.Show();
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		return unzip(params[0]);
	}

	@Override
	// ������UI�̡߳�doInBackground()��ʹ��publishProgress()����
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		cdialog.setProgress(values[0] * 100 / fileSize);
	}

	@Override
	// ������UI�̷߳���doInBackground���������
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		cdialog.Dismiss();
		cdialog = null;

		if (loadlisenter != null) {
			loadlisenter.LoadResult(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private int unzip(int resid) {
		StringBuffer path = new StringBuffer();
		String sDState = android.os.Environment.getExternalStorageState();// ��ȡ��չSD���豸״̬

		if (sDState.equals(android.os.Environment.MEDIA_MOUNTED) || sDState.equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY))// ӵ�пɶ�Ȩ��
		{
			path.append(android.os.Environment.getExternalStorageDirectory());// ��ȡSD��

			ZipArchiveInputStream zin = new ZipArchiveInputStream(context.getResources().openRawResource(resid), "GBK", true);
			int rootCount = path.length();

			ArchiveEntry zipEntry = null;
			FileOutputStream outputStream = null;
			byte[] buffer = new byte[8192];

			try {
				while ((zipEntry = zin.getNextEntry()) != null) {
					if (path.length() > rootCount) {
						path.delete(rootCount, path.length());
					}

					if (zipEntry.isDirectory()) {
						String name = zipEntry.getName();
						name = name.substring(0, name.length() - 1);
						path.append(File.separator).append(name);
						File file = new File(path.toString());
						if (!file.exists()) {
							file.mkdir();
						}
						continue;
					} else {
						path.append(File.separator).append(zipEntry.getName());

						File desFile = new File(path.toString());

						if (!desFile.exists()) {
							File fileParentDir = desFile.getParentFile();

							if (!fileParentDir.exists()) {
								fileParentDir.mkdirs();
							}

							desFile.createNewFile();
						} else {
							desFile.delete();
							desFile.createNewFile();
						}

						outputStream = new FileOutputStream(desFile);// ��ͨ��ѹ���ļ�

						fileSize = (int) zipEntry.getSize();
						fileSize = fileSize == 0 ? 1 : fileSize;
						int offset = 0, count = 0;

						while ((offset = zin.read(buffer)) != -1) {
							outputStream.write(buffer, 0, offset);
							count += offset;
							publishProgress(count);
						}

						outputStream.close();
					}
				}

				zin.close();
				return 0;
			} catch (Exception ex) {
				return -1;
			} finally {
				try {
					zin.close();

					if (outputStream != null) {
						outputStream.flush();
						outputStream.close();
					}
				} catch (IOException e) {
				}
			}
		}

		return -1;
	}

	/**
	 * �ݹ�ɾ���ļ����ļ���
	 * 
	 * @param file
	 *            Ҫɾ���ĸ�Ŀ¼
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				RecursionDeleteFile(childFiles[i]);
			}
			file.delete();
		}
	}

}
