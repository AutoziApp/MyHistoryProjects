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
	private static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
	private static int UNINSTALLED = 1; // 表示未安装
	private static int INSTALLED_UPDATE = 2; // 表示已经安装，版本比现在这个版本要低，可以点击按钮更新
	private LinearLayout middleLayout;
	private List<APKFile> APKFiles = new ArrayList<APKFile>();
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) this.findViewById(R.id.parentLayout), "执法系统软件管理");

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
					OtherTools.showToast(APKManagerActivity.this, "该软件已安装，请勿重复安装");
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
		 * 获取版本号
		 * 
		 * @return 该应用的版本号,例如:3.6.1
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
	 *            运用递归的思想，递归去找每个目录下面的apk文件
	 */
	public void FindAllAPKFile(File file) {

		// 手机上的文件,目前只判断SD卡上的APK文件
		// file = Environment.getDataDirectory();
		// SD卡上的文件目录
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

				/** 获取apk的图标 */
				appInfo.sourceDir = apk_path;
				appInfo.publicSourceDir = apk_path;
				Drawable apk_icon = appInfo.loadIcon(pm);

				APKFile.setApk_icon(apk_icon);
				/** 得到包名 */
				String packageName = packageInfo.packageName;
				APKFile.setPackageName(packageName);

				try {
					CharSequence temp = pm.getApplicationLabel(appInfo);
					APKFile.setAppNameString((String) temp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				/** apk的绝对路劲 */
				APKFile.setFilePath(file.getAbsolutePath());
				/** apk的版本名称 String */
				String versionName = packageInfo.versionName;
				APKFile.setVersionName(versionName);
				/** apk的版本号码 int */
				int versionCode = packageInfo.versionCode;
				APKFile.setVersionCode(versionCode);
				/** 安装处理类型 */
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
	 * 判断该应用在手机中的安装情况
	 * 
	 * @param pm
	 *            PackageManager
	 * @param packageName
	 *            要判断应用的包名
	 * @param versionCode
	 *            要判断应用的版本号
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
			apkInstallInfo.setText(installInfo == 0 ? "已安装" : installInfo == 1 ? "未安装" : "有更新");

			return convertView;
		}
	}
}
