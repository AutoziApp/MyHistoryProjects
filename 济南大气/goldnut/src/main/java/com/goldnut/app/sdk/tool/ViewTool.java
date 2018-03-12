package com.goldnut.app.sdk.tool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ViewTool {

	public static void setListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static void setGridViewHeight(GridView gridView, int verticalSpacing) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int columnsNum = gridView.getNumColumns();

		int totalHeight = 0;
		int tempLen = listAdapter.getCount() / columnsNum;
		if (listAdapter.getCount() % columnsNum > 0) {
			tempLen++;
		}

		for (int i = 0, len = tempLen; i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + (verticalSpacing * (tempLen - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		gridView.setLayoutParams(params);
	}

	/**
	 * @根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * @得到手机的dpi
	 */
	public static float getdpi(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * @得到手机的dpi
	 */
	public static int getWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;

		return screenWidth;
	}

	public static int getHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.heightPixels;

		return screenWidth;
	}

	/**
	 * @根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 隐藏键盘
	 */
	public static void onHideInputSoftKeyboard(View editeText) {
		try {
			InputMethodManager imm = (InputMethodManager) editeText
					.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editeText.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void onHideInputSoftKeyboard(Activity context) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
			if (isOpen)
				imm.hideSoftInputFromWindow(context.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    /**
     *显示键盘
    * @author zhaolin
    * @Desc: TODO 
    * @param editeText void
     */
	public static void onShowSoftInput(View editeText) {
		try {
			InputMethodManager imm = (InputMethodManager) editeText.
					getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
			if (!isOpen)
				imm.showSoftInput(editeText, InputMethodManager.SHOW_FORCED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void onShowSoftInput(Activity context) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
			if (!isOpen)
				imm.showSoftInput(context.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 中间加横线
	 * 
	 * @param text
	 */
	public static void onTextMiddleLine(TextView text) {
		try {
			text.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 底部加横线
	 * 
	 * @param text
	 */
	public static void onTextBottomLine(TextView text) {
		try {
			text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param info
	 *            titile设置标题
	 * @param context
	 */
	public static void setTitileInfo(Activity context, String info, int id) {
		TextView setTitileInfo = (TextView) context.findViewById(id);
		setTitileInfo.setText(info);
	}

	/**
	 * 给titile设置标题和显示返回键
	 * 
	 * @param context
	 * @param info
	 *            标题
	 * @param listener
	 *            返回键的监听事件 可以传null
	 */
	public static void setTitileInfo(final Activity context, String info, OnClickListener listener, int title, int left) {
		TextView setTitileInfo = (TextView) context.findViewById(title);
		setTitileInfo.setText(info);

		View iv_left = context.findViewById(left);
		iv_left.setVisibility(View.VISIBLE);
		if (listener != null) {
			iv_left.setOnClickListener(listener);
		} else {
			iv_left.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					context.finish();
				}
			});
		}
	}

	public static String imageCompressForBytes(String originalFilePath, String newFilePath, int width) {
		if (TextUtils.isEmpty(originalFilePath))
			return "";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// 获取这个图片的宽和高
		// BitmapFactory.decodeByteArray(data, offset, length, opts)
		Bitmap bitmap = BitmapFactory.decodeFile(originalFilePath, options);// 此时返回bm为空
		if (width < 400) {
			width = 400;
		} else if (width > 800) {
			width = 800;
		}
		// 计算缩放比
		int be = (int) (options.outWidth / (float) width);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		// 重新读入图片，注意这次要把options.inJustDecodeBounds设为false哦
		bitmap = BitmapFactory.decodeFile(originalFilePath, options);
		if (bitmap == null)
			return "";
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);

		byte[] data = baos.toByteArray();

		try {
			bos.close();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 保存入sdCard

		String fileName = "opinion_" + System.currentTimeMillis() + ".jpg";

		FileTool.getFile(data, newFilePath, fileName);

		return newFilePath + "/" + fileName;

	}

	private static long lastClickTime;

	/**
	 * 是否重复点击
	 * 
	 * @return true 重复点击 不响应
	 */
	public static boolean isFastDoubleClick() {

		long time = System.currentTimeMillis();

		long timeD = time - lastClickTime;

		if (0 < timeD && timeD < 800) {

			return true;
		}

		lastClickTime = time;
		return false;
	}
}
