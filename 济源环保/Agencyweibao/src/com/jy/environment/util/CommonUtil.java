package com.jy.environment.util;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.activity.EnvironmentCityManagerActivity;

public class CommonUtil {

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static Bitmap HexStringToBitmap(String src) {

		return Bytes2Bimap(hexStringToBytes(src));
	}

	public static String BitmapToHexString(Bitmap b) {
		if (b == null) {
			return "";
		}
		return bytesToHexString(Bitmap2Bytes(b));
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

		return baos.toByteArray();

	}

	public static Bitmap Bytes2Bimap(byte[] b) {

		if (b == null || b.length == 0) {

			return null;

		}

		return BitmapFactory.decodeByteArray(b, 0, b.length);

	}

	public static String ImageToBase64(Bitmap b) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		if (b == null) {
			return "";
		}
		byte[] data = Bitmap2Bytes(b);

		// 对字节数组Base64编码
		return Base64.encodeToString(data, Base64.DEFAULT);// 返回Base64编码过的字节数组字符串
	}

	public static Bitmap Base64ToImage(String s) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		if (s == "") {
			return null;
		}
		byte[] data = Base64.decode(s, Base64.DEFAULT);

		// 对字节数组Base64编码
		return Bytes2Bimap(data);// 返回Base64编码过的字节数组字符串
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, 1).show();
	}

	public static void showToast(Context context, String msg, boolean isShow) {
		if (isShow)
			Toast.makeText(context, msg, 1).show();
	}

	/**
	 * 
	 * @param context
	 * @param newLocationCity
	 * @return
	 */
	public static boolean sendLocationCityChangeBoradcast(Context context,
			String newLocationCity) {
		try {
			if ((null != newLocationCity && !"".equals(newLocationCity))) {
				Intent intent = new Intent();
				intent.setAction(EnvironmentCityManagerActivity.LOCATIONCHANGEACTION);
				context.sendBroadcast(intent);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 图片任意的大小不变形状换算函数
	 * 
	 * @param imgW
	 *            显示的图片的真实宽度
	 * @param imgH
	 *            显示的图片的真实高度
	 * @param cvW
	 *            显示区域的宽度（像素）
	 * @param cvH
	 *            显示区域的高度（像素）
	 * @param pW
	 *            显示屏宽的像素密度， 通过context.getResources().getDisplayMetrics().ydpi得到
	 * @param pH
	 *            显示屏高的像素密度，获取方法同上
	 * @return 图片的真实显示大小，其中re[0]为宽，re[1]为高
	 */
	public static int[] getViewWH(int imgW, int imgH, int cvW, int cvH,
			float pW, float pH) {
		int[] re = new int[2];
		float ratio = imgW / imgH * pW / pH;
		if (cvW / ratio <= cvH) {
			re[0] = cvW;
			re[1] = (int) (cvW / ratio);
		} else {
			re[0] = (int) (cvH * ratio);
			re[1] = cvH;
		}
		return re;
	}

	public static String getTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
		return sdf.format(date);

	}

	public static String getTime(long date) {
		return DateFormat.format("yyyy-MM-dd kk:mm:ss", date).toString();
	}

	public static Bitmap GetCurrentScreen(Activity context) {
		Bitmap bmp = null;
		try {
			// 1.构建Bitmap
			WindowManager windowManager = context.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int w = display.getWidth();
			int h = display.getHeight();

			bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
			// 2.获取屏幕
			// View decorview = this.getParent().getWindow().getDecorView();
			View decorview = context.getWindow().getDecorView();
			decorview.setDrawingCacheEnabled(true);
			bmp = decorview.getDrawingCache();
		} catch (OutOfMemoryError e) {
			return null;
		} catch (Exception e) {
			return null;
		}

		return bmp;
	}

	public static Bitmap GetCurrentParentScreen(Activity context) {
		// 1.构建Bitmap
		WindowManager windowManager = context.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// 2.获取屏幕
		View decorview = context.getParent().getWindow().getDecorView();
		// View decorview = context.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		bmp = decorview.getDrawingCache();
		return bmp;
	}

	public static String getWeatherIconString(String climate, int m) {
		// String weatherEnd = "晴";
		// MyLog.i("climate 原始:" + climate);
		if (m == 1) {
			if (climate.contains("：")) {
				climate = climate.substring(climate.indexOf("：") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("，")) {
				climate = climate.substring(0, climate.indexOf("，"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("、")) {
				climate = climate.substring(0, climate.indexOf("、"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("夜间")) {
				climate = climate.substring(0, climate.indexOf("夜间"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("白天")) {
				climate = climate.replaceAll("白天", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("大部")) {
				climate = climate.replaceAll("大部", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("早有")) {
				climate = climate.replaceAll("早有", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("短暂")) {
				climate = climate.replaceAll("短暂", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("有风")) {
				climate = climate.replaceAll("有风", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("零星")) {
				climate = climate.replaceAll("零星", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("分散性")) {
				climate = climate.replaceAll("分散性", "");
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("转")) {
				climate = climate.substring(0, climate.indexOf("转"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("或")) {
				climate = climate.substring(0, climate.indexOf("或"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("伴有")) {
				climate = climate.substring(0, climate.indexOf("伴"));
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("有")) {
				MyLog.i("eeeeeeeeee22" + climate);
				climate = climate.substring(climate.indexOf("有") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
				MyLog.i("eeeeeeeeee33" + climate);
			}
			if (climate.contains("到")) {
				climate = climate.substring(climate.indexOf("到") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("-")) {
				climate = climate.substring(climate.indexOf("-") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("间")) {
				climate = climate.substring(climate.indexOf("间") + 1);
				if (climate.length() <= 10) {
					return climate;
				}
			}
			if (climate.contains("霾")) {
				climate = "霾";
			}
		} else {
			if (climate.contains("：")) {
				climate = climate.substring(climate.indexOf("：") + 1);
			}
			if (climate.contains("，")) {
				climate = climate.substring(0, climate.indexOf("，"));
			}
			if (climate.contains("、")) {
				climate = climate.substring(0, climate.indexOf("、"));
			}
			if (climate.contains("夜间")) {
				climate = climate.substring(0, climate.indexOf("夜间"));
			}
			if (climate.contains("白天")) {
				climate = climate.replaceAll("白天", "");
			}
			if (climate.contains("大部")) {
				climate = climate.replaceAll("大部", "");
			}
			if (climate.contains("早有")) {
				climate = climate.replaceAll("早有", "");
			}
			if (climate.contains("短暂")) {
				climate = climate.replaceAll("短暂", "");
			}
			if (climate.contains("有风")) {
				climate = climate.replaceAll("有风", "");
			}
			if (climate.contains("零星")) {
				climate = climate.replaceAll("零星", "");
			}
			if (climate.contains("分散性")) {
				climate = climate.replaceAll("分散性", "");
			}
			if (climate.contains("转")) {
				climate = climate.substring(0, climate.indexOf("转"));
			}
			if (climate.contains("或")) {
				climate = climate.substring(0, climate.indexOf("或"));
			}
			if (climate.contains("伴有")) {
				climate = climate.substring(0, climate.indexOf("伴"));
			}
			if (climate.contains("有")) {
				MyLog.i("eeeeeeeeee22" + climate);
				climate = climate.substring(climate.indexOf("有") + 1);
				MyLog.i("eeeeeeeeee33" + climate);
			}
			if (climate.contains("到")) {
				climate = climate.substring(climate.indexOf("到") + 1);
			}
			if (climate.contains("-")) {
				climate = climate.substring(climate.indexOf("-") + 1);
			}
			if (climate.contains("间")) {
				climate = climate.substring(climate.indexOf("间") + 1);
			}
			if (climate.contains("霾")) {
				climate = "霾";
			}
		}
		// MyLog.i("climate :" + climate);
		return climate;
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int StringToInt(String data) {
		int temp;
		try {
			temp = Integer.valueOf(data);
		} catch (Exception e) {
			// TODO: handle exception
			temp = 0;
		}
		return temp;
	}

	public static Dialog getCustomeDialog(Activity activity, int style,
			int customView) {
		Dialog dialog = new Dialog(activity, style);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(customView);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		return dialog;
	}
	
	public static int getColorByAQI(String Aqi) {
		try {
			double aqi = Double.parseDouble(Aqi);
			if (aqi <= 50) {
				return Color.parseColor("#39B711");
			} else if (aqi <= 100) {
				return Color.parseColor("#FFD112");
			} else if (aqi <= 150) {
				return Color.parseColor("#FF7E00");
			} else if (aqi <= 200) {
				return Color.parseColor("#FF0000");
			} else if (aqi <= 300) {
				return Color.parseColor("#99004C");
			} else {
				return Color.parseColor("#7E0000");
			}
		} catch (Exception e) {
			return Color.parseColor("#39B711");
		}
	}
	
	public static int getColorByPM10(String Aqi) {
		try {
			double aqi = Double.parseDouble(Aqi);
			if (aqi <= 50) {
				return Color.parseColor("#39B711");
			} else if (aqi <= 150) {
				return Color.parseColor("#FFD112");
			} else if (aqi <= 250) {
				return Color.parseColor("#FF7E00");
			} else if (aqi <= 350) {
				return Color.parseColor("#FF0000");
			} else if (aqi <= 420) {
				return Color.parseColor("#99004C");
			} else {
				return Color.parseColor("#7E0000");
			}
		} catch (Exception e) {
			return Color.parseColor("#39B711");
		}
	}
	
	public static int getColorByPM25(String Aqi) {
		try {
			double aqi = Double.parseDouble(Aqi);
			if (aqi <= 35) {
				return Color.parseColor("#39B711");
			} else if (aqi <= 75) {
				return Color.parseColor("#FFD112");
			} else if (aqi <= 115) {
				return Color.parseColor("#FF7E00");
			} else if (aqi <= 150) {
				return Color.parseColor("#FF0000");
			} else if (aqi <= 250) {
				return Color.parseColor("#99004C");
			} else {
				return Color.parseColor("#7E0000");
			}
		} catch (Exception e) {
			return Color.parseColor("#39B711");
		}
	}

	public static int getRIdByAQI(String Aqi) {
		try {
			double aqi = Double.parseDouble(Aqi);
			if (aqi <= 50) {
				return R.drawable.air1;
			} else if (aqi <= 100) {
				return R.drawable.air2;
			} else if (aqi <= 150) {
				return R.drawable.air3;
			} else if (aqi <= 200) {
				return R.drawable.air4;
			} else if (aqi <= 300) {
				return R.drawable.air5;
			} else {
				return R.drawable.air6;
			}
		} catch (Exception e) {
		}
		return R.drawable.air1;
	}

	public static String getDengJiByAQI(String Aqi) {
		try {
			int aqi = Integer.parseInt(Aqi);
			if (aqi <= 50) {
				return "优";
			} else if (aqi <= 100) {
				return "良";
			} else if (aqi <= 150) {
				return "轻度";
			} else if (aqi <= 200) {
				return "中度";
			} else if (aqi <= 300) {
				return "重度";
			} else {
				return "严重";
			}
		} catch (Exception e) {
		}
		return "优";
	}

	public static String getDengJiByAQII(String Aqi) {
		try {
			int aqi = Integer.parseInt(Aqi);
			if (aqi <= 50) {
				return "优";
			} else if (aqi <= 100) {
				return "良";
			} else if (aqi <= 150) {
				return "轻度污染";
			} else if (aqi <= 200) {
				return "中度污染";
			} else if (aqi <= 300) {
				return "重度污染";
			} else {
				return "严重污染";
			}
		} catch (Exception e) {
		}
		return "优";
	}
	public static String getDengJiByAQIII(String Aqi) {
		try {
			int aqi = Integer.parseInt(Aqi);
			if (aqi <= 50) {
				return "优";
			} else if (aqi <= 100) {
				return "良";
			} else if (aqi <= 150) {
				return "轻度";
			} else if (aqi <= 200) {
				return "中度";
			} else if (aqi <= 300) {
				return "重度";
			} else {
				return "严重";
			}
		} catch (Exception e) {
		}
		return "优";
	}

	
	public static int getDengJiByType(int value, String type, double covalue) {
		int drawable = R.drawable.air1;
		if (type.equals("0")) {
			if (value <= 50) {
				return R.drawable.aqi_level_1;
			} else if (value <= 150) {
				return R.drawable.aqi_level_2;
			} else if (value <= 250) {
				return R.drawable.aqi_level_3;
			} else if (value <= 350) {
				return R.drawable.aqi_level_4;
			} else if (value <= 420) {
				return R.drawable.aqi_level_5;
			} else {
				return R.drawable.aqi_level_6;
			}
		} else if (type.equals("1")) {
			if (value <= 35) {
				return R.drawable.aqi_level_1;
			} else if (value <= 75) {
				return R.drawable.aqi_level_2;
			} else if (value <= 115) {
				return R.drawable.aqi_level_3;
			} else if (value <= 150) {
				return R.drawable.aqi_level_4;
			} else if (value <= 250) {
				return R.drawable.aqi_level_5;
			} else {
				return R.drawable.aqi_level_6;
			}
		} else if (type.equals("2")) {
			if (value <= 160) {
				return R.drawable.aqi_level_1;
			} else if (value <= 200) {
				return R.drawable.aqi_level_2;
			} else if (value <= 300) {
				return R.drawable.aqi_level_3;
			} else if (value <= 400) {
				return R.drawable.aqi_level_4;
			} else if (value <= 800) {
				return R.drawable.aqi_level_5;
			} else {
				return R.drawable.aqi_level_6;
			}
		} else if (type.equals("3")) {
			if (value <= 150) {
				return R.drawable.aqi_level_1;
			} else if (value <= 500) {
				return R.drawable.aqi_level_2;
			} else if (value <= 650) {
				return R.drawable.aqi_level_3;
			} else if (value <= 800) {
				return R.drawable.aqi_level_4;
			} else if (value <= 1600) {
				return R.drawable.aqi_level_5;
			} else {
				return R.drawable.aqi_level_6;
			}
		} else if (type.equals("4")) {
			if (value <= 100) {
				return R.drawable.aqi_level_1;
			} else if (value <= 200) {
				return R.drawable.aqi_level_2;
			} else if (value <= 700) {
				return R.drawable.aqi_level_3;
			} else if (value <= 1200) {
				return R.drawable.aqi_level_4;
			} else if (value <= 2340) {
				return R.drawable.aqi_level_5;
			} else {
				return R.drawable.aqi_level_6;
			}
		} else if (type.equals("5")) {
			if (covalue <= 5) {
				return R.drawable.aqi_level_1;
			} else if (covalue <= 10) {
				return R.drawable.aqi_level_2;
			} else if (covalue <= 35) {
				return R.drawable.aqi_level_3;
			} else if (covalue <= 60) {
				return R.drawable.aqi_level_4;
			} else if (covalue <= 90) {
				return R.drawable.aqi_level_5;
			} else {
				return R.drawable.aqi_level_6;
			}
		}
		return drawable;
	}

	// 检测传过来的值是不是0或-1值
	public static boolean isCheck(String value) {
		try {
			int m = Integer.parseInt(value);
			if (m > 1000) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (value.equals("0") || value.equals("-1")) {
			return true;
		}
		return false;
	}
}
