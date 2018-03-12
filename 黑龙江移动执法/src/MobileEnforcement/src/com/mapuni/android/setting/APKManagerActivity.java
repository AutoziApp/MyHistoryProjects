package com.mapuni.android.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.OtherTools;

public class APKManagerActivity extends BaseActivity {
	private static int INSTALLED = 0; // ��ʾ�Ѿ���װ���Ҹ��������apk�ļ���һ���汾
	private static int UNINSTALLED = 1; // ��ʾδ��װ
	private static int INSTALLED_UPDATE = 2; // ��ʾ�Ѿ���װ���汾����������汾Ҫ�ͣ����Ե����ť����
	private LinearLayout middleLayout;
	private List<APKFile> APKFiles = new ArrayList<APKFile>();
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) this.findViewById(R.id.parentLayout), "ִ��ϵͳ�������");

		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		lv = new ListView(this);
		middleLayout.addView(lv, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				APKManagerAdapter adapter = (APKManagerAdapter) parent.getAdapter();
				APKFile apkFile = (APKFile) adapter.getItem(position);

				int installedInfo = apkFile.getInstalledInfo();
				if (installedInfo == 0) {
					OtherTools.showToast(APKManagerActivity.this, "������Ѱ�װ�������ظ���װ");
				} else if (installedInfo == 1) {
					File file = new File(apkFile.getFilePath());
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					Uri uri = Uri.fromFile(file);
					intent.setDataAndType(uri, "application/vnd.android.package-archive");
					startActivity(intent);
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		APKFiles.clear();
		new APKSearchAsyncTask(this).execute(PathManager.SDCARD_DATA_LOCAL_PATH);
	}

	private class APKSearchAsyncTask extends AsyncTask<String, Void, List<APKFile>> {
		private Context context;

		public APKSearchAsyncTask(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected List<APKFile> doInBackground(String... params) {
			FindAllAPKFile(new File(params[0]));
			return APKFiles;
		}

		@Override
		protected void onPostExecute(List<APKFile> result) {
			super.onPostExecute(result);
			lv.setAdapter(new APKManagerAdapter(context, result));
		}
	}

	public class APKFile {
		private Drawable apk_icon;
		private String packageName;
		private String filePath;
		private String versionName;
		private int versionCode;
		private int type;
		private String appNameString;

		public String getAppNameString() {
			return appNameString;
		}

		public void setAppNameString(String appNameString) {
			this.appNameString = appNameString;
		}

		public Drawable getApk_icon() {
			return apk_icon;
		}

		public void setApk_icon(Drawable apk_icon) {
			this.apk_icon = apk_icon;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		/**
		 * ��ȡ�汾��
		 * 
		 * @return ��Ӧ�õİ汾��,����:3.6.1
		 */
		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public int getInstalledInfo() {
			return type;
		}

		public void setInstalled(int type) {
			this.type = type;
		}

	}

	/**
	 * @param args
	 *            ���õݹ��˼�룬�ݹ�ȥ��ÿ��Ŀ¼�����apk�ļ�
	 */
	public void FindAllAPKFile(File file) {

		// �ֻ��ϵ��ļ�,Ŀǰֻ�ж�SD���ϵ�APK�ļ�
		// file = Environment.getDataDirectory();
		// SD���ϵ��ļ�Ŀ¼
		if (file.isFile()) {
			String name_s = file.getName();
			APKFile APKFile = new APKFile();
			String apk_path = null;
			// MimeTypeMap.getSingleton()
			if (name_s.toLowerCase(Locale.getDefault()).endsWith(".apk")) {
				apk_path = file.getAbsolutePath();

				PackageManager pm = this.getPackageManager();
				PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);

				ApplicationInfo appInfo = packageInfo.applicationInfo;

				/** ��ȡapk��ͼ�� */
				appInfo.sourceDir = apk_path;
				appInfo.publicSourceDir = apk_path;
				Drawable apk_icon = appInfo.loadIcon(pm);

				APKFile.setApk_icon(apk_icon);
				/** �õ����� */
				String packageName = packageInfo.packageName;
				APKFile.setPackageName(packageName);

				try {
					CharSequence temp = pm.getApplicationLabel(appInfo);
					APKFile.setAppNameString((String) temp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				/** apk�ľ���·�� */
				APKFile.setFilePath(file.getAbsolutePath());
				/** apk�İ汾���� String */
				String versionName = packageInfo.versionName;
				APKFile.setVersionName(versionName);
				/** apk�İ汾���� int */
				int versionCode = packageInfo.versionCode;
				APKFile.setVersionCode(versionCode);
				/** ��װ�������� */
				int type = doType(pm, packageName, versionCode);
				APKFile.setInstalled(type);

				APKFiles.add(APKFile);
			}
		} else {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File file_str : files) {
					FindAllAPKFile(file_str);
				}
			}
		}
	}

	/**
	 * �жϸ�Ӧ�����ֻ��еİ�װ���
	 * 
	 * @param pm
	 *            PackageManager
	 * @param packageName
	 *            Ҫ�ж�Ӧ�õİ���
	 * @param versionCode
	 *            Ҫ�ж�Ӧ�õİ汾��
	 */
	private int doType(PackageManager pm, String packageName, int versionCode) {
		List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pi : pakageinfos) {
			String pi_packageName = pi.packageName;
			int pi_versionCode = pi.versionCode;
			if (packageName.endsWith(pi_packageName)) {
				if (versionCode <= pi_versionCode) {
					return INSTALLED;
				} else if (versionCode > pi_versionCode) {
					return INSTALLED_UPDATE;
				}
			}
		}
		return UNINSTALLED;
	}

	private class APKManagerAdapter extends BaseAdapter {
		private List<APKFile> lists = new ArrayList<APKManagerActivity.APKFile>();
		private LayoutInflater inflater;

		public APKManagerAdapter(Context context, List<APKFile> lists) {
			this.lists = lists;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			APKFile file = lists.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.apk_manager_layout, null);
			}
			ImageView appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
			TextView apkName = (TextView) convertView.findViewById(R.id.apkName);
			TextView apkInstallInfo = (TextView) convertView.findViewById(R.id.apkInstallInfo);

			appIcon.setImageDrawable(file.getApk_icon());
			apkName.setText(file.getAppNameString());

			int installInfo = file.getInstalledInfo();
			apkInstallInfo.setText(installInfo == 0 ? "�Ѱ�װ" : installInfo == 1 ? "δ��װ" : "�и���");

			return convertView;
		}
	}
}
