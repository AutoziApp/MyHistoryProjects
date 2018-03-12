package com.jy.environment.exception;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.jy.environment.util.ExitAppUtils;
import com.jy.environment.util.MyLog;

public class CrashHandler implements UncaughtExceptionHandler {
	private static String mIMEI; // IMEI
	public static final String TAG = "CrashHandler";

	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		savaInfoToSD(mContext, ex);
		// 提示用户程序即将退出
		showToast(mContext, "很抱歉，程序遭遇异常，即将退出！");
		// 将一些信息保存到SDcard中
		savaInfoToSD(mContext, ex);
		try {
			thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(1);

		// 完美退出程序方法
		ExitAppUtils.getInstance().exit();
		if (mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
		}

	}

	private void savaInfoToSD(Context context, Throwable ex) {// 收集设备参数信息
		// collectDeviceInfo(mContext);
		saveCrashInfo2File(ex);
	}

	/**
	 * 显示提示信息，需要在线程中显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	private void showToast(final Context context, final String msg) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}).start();
	}

	/**
	 * 为我们的应用程序设置自定义Crash处理
	 */
	public void setCustomCrashHanler(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);

		saveCrashInfo2File(ex);
		ex.printStackTrace();

		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				TelephonyManager tm = (TelephonyManager) mContext
						.getSystemService(Context.TELEPHONY_SERVICE);

				mIMEI = tm.getDeviceId(); // 设备imei
				mIMEI = mIMEI == null ? mIMEI : mIMEI.trim();
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
				infos.put("imei", mIMEI);
			}
		} catch (NameNotFoundException e) {

		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());

			} catch (Exception e) {

			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash.log";
			String path;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
				path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + "weibao";
			} else {// 如果SD卡不存在，就保存到本应用的目录下
				path = mContext.getFilesDir().getAbsolutePath()
						+ File.separator + "weibao" + File.separator + "log";
			}
			// if
			// (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			// {
			// = Environment.getExternalStorageDirectory().toString()+"/weibao/"

			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
					dir + File.separator + "crash.log")));
			// 导出发生异常的时间
			pw.println(time);
			// 导出手机信息
			dumpPhoneInfo(pw);

			pw.println();
			// 导出异常的调用栈信息
			ex.printStackTrace(pw);

			pw.close();
			return fileName;
		} catch (Exception e) {

		}

		return null;
	}

	private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
		// 应用的版本名称和版本号
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
				PackageManager.GET_ACTIVITIES);
		pw.print("App Version: ");
		pw.print(pi.versionName);
		pw.print('_');
		pw.println(pi.versionCode);

		// android版本号
		pw.print("OS Version: ");
		pw.print(Build.VERSION.RELEASE);
		pw.print("_");
		pw.println(Build.VERSION.SDK_INT);

		// 手机制造商
		pw.print("Vendor: ");
		pw.println(Build.MANUFACTURER);

		// 手机型号
		pw.print("Model: ");
		pw.println(Build.MODEL);

		// cpu架构
		pw.print("CPU ABI: ");
		pw.println(Build.CPU_ABI);
	}
}