package cn.com.mapuni.meshing.base.controls.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.mapuni.meshing.base.R;

public class YutuLoading extends LinearLayout {

	private final Context mContext;
	private TextView textView;
	private ImageView imageView;
	private AnimationDrawable ad;
	private String loadingMsg = "加载中，请稍等...", failMsg = "暂无数据";
	private Dialog dialog;
	// private int textColor = Color.rgb(25, 108, 200);
	private int textColor = Color.BLACK;
	private LinearLayout layout;
	private Boolean IsCancelable=false;

	private boolean flag = true;

	public YutuLoading(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public YutuLoading(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater.from(mContext).inflate(
				R.layout.base_controls_yutuloading, this);
		layout = (LinearLayout) findViewById(R.id.yutu_loadling_out);
		textView = (TextView) findViewById(R.id.loadMessage);
		imageView = (ImageView) findViewById(R.id.imageView);
		ad = (AnimationDrawable) imageView.getBackground();
		textView.setText(loadingMsg);
		textView.setTextColor(textColor);
		// layout.setBackgroundResource(R.drawable.gis_toolbar_bg);
		layout.setBackgroundResource(R.drawable.base_yutuloading_bg);
	}

	/**
	 * 设置加载失败
	 */
	public void setFailed() {
		ad.stop();
		flag = false;
		textView.setText(failMsg);
		imageView.setBackgroundResource(R.drawable.earth_2);
		layout.setBackgroundColor(Color.TRANSPARENT);
	}
	/**
	 * 设置对话框是否可以返回取消
	 * @param IsCancelable
	 */
	public void setCancelable(Boolean IsCancelable){
		this.IsCancelable=IsCancelable;
	}

	/**
	 * 显示对话框
	 */
	public void showDialog() {
		if (dialog == null) {
			layout.setBackgroundResource(R.drawable.base_yutuloading_bg);
			dialog = new Dialog(mContext, R.style.base_loadingDialog);
			// dialog = new Dialog(mContext, R.style.FullScreenDialog);
			/*
			 * WindowManager.LayoutParams lp =
			 * dialog.getWindow().getAttributes(); lp.alpha = 0.5f; // 0.0-1.0
			 * dialog.getWindow().setAttributes(lp);
			 */
			dialog.setCancelable(IsCancelable);
			dialog.setContentView(this);
		}
		if (!dialog.isShowing())
			dialog.show();
	}

	/**
	 * 消失对话框
	 */
	public void dismissDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus) {
			if (flag) {
				ad.stop();
				ad.start();
			}
		}
		super.onWindowFocusChanged(hasWindowFocus);
	}

	/**
	 * 设置加载时和加载失败时提示消息
	 * 
	 * @param loadingMsg
	 *            加载时提示消息
	 * @param failMsg
	 *            加载失败时提示消息
	 */
	public void setLoadMsg(String loadingMsg, String failMsg) {
		this.loadingMsg = loadingMsg;
		this.failMsg = failMsg;
		textView.setTextColor(textColor);
		textView.setText(this.loadingMsg);
	}

	/**
	 * 设置加载时和加载失败时提示消息以及显示字体颜色
	 * 
	 * @param loadingMsg
	 *            加载时提示消息
	 * @param failMsg
	 *            加载失败时提示消息
	 * @param color
	 *            设置显示字体颜色
	 */
	public void setLoadMsg(String loadingMsg, String failMsg, int color) {
		this.loadingMsg = loadingMsg;
		this.failMsg = failMsg;
		this.textColor = color;
		textView.setTextColor(textColor);
		textView.setText(this.loadingMsg);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (GONE == visibility) {
			ad.stop();
		}
	}

	/**
	 * 加载失败时重新开始加载
	 */
	public void restart() {
		if (!flag) {
			imageView.setBackgroundResource(R.drawable.base_loadinganim);
			ad = (AnimationDrawable) imageView.getBackground();
			textView.setTextColor(textColor);
			textView.setText(loadingMsg);
			flag = true;
			onWindowFocusChanged(true);
		}
	}

}
