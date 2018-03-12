package com.goldnut.app.sdk.dialog;

import java.util.List;

import com.goldnut.app.sdk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DialogHelper {

	/**
	 * @param context
	 *            仅有一个确定按钮对话框
	 * @return 仅一个按钮的
	 */
	public static AlertDialog getDialogWithOneBtn(Context context,
			String title, String info, final DialogClickListener okOnClickListener,
			boolean cancelable) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		// 消除了AlertDialog背后的黑框
		dialog.show();
		Window view = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		view.setContentView(R.layout.dialog_select_city_info);

		TextView tv_msg_info = (TextView) view.findViewById(R.id.tv_msg_info);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		if (info != null) {
			tv_msg_info.setText(Html.fromHtml(info));
		}
		if (title != null) {
			tv_title.setText(title);
		} else {
			// 没有title的类型
			view.findViewById(R.id.rl_title).setVisibility(View.GONE);
		}

		View tv_ok = view.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okOnClickListener != null)
					okOnClickListener.onOK();
				dialog.dismiss();
			}
		});
		// dialog.setView(view, 0, 0, 0, 0);
		dialog.setCancelable(cancelable);
		return dialog;
	}

	/**
	 * @param context
	 *            默认可以取消
	 * 
	 * @return 2个按钮的对话框
	 */
	public static AlertDialog getDialogWithTwoBtn(Context context,
			String title, String info,
			final DialogClickListener clickListener) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		// 消除了AlertDialog背后的黑框
		dialog.show();
		Window view = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		view.setContentView(R.layout.dialog_base_blue_info);

		TextView tv_msg_info = (TextView) view.findViewById(R.id.tv_msg_info);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		if (info != null) {
			tv_msg_info.setText(info);
		}

		if (!TextUtils.isEmpty(title)) {
			tv_title.setText(title);
		} else {
			// 没有title的类型
			view.findViewById(R.id.rl_title).setVisibility(View.GONE);
		}

		View tv_ok = view.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onOK();
				dialog.dismiss();
			}
		});
		View tv_later = view.findViewById(R.id.tv_cancle);
		tv_later.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onCancel();
				dialog.dismiss();
			}
		});

		// dialog.setView(view, 0, 0, 0, 0);

		return dialog;
	}
	
	public interface DialogClickListener{
		public void onOK();
		public void onCancel();
	}


	/**
	 * @param context
	 *            可设置为不可取消的
	 * 
	 * @return
	 */
	public static AlertDialog getDialogWithTwoBtnDialog(Context context,
			String title, String info,
			final  DialogClickListener listener, boolean cancelable) {

		AlertDialog dialog = getDialogWithTwoBtn(context, title, info,
				listener);
		dialog.setCancelable(cancelable);

		return dialog;
	}

	/**
	 * @param context
	 *            默认可以取消
	 * 
	 * @return APK更新的对话框
	 */
	public static Dialog getDialogUploadApkDialog(Context context,
			String title, String info,
			final OnClickListener cancleOnClickListener,
			final OnClickListener okOnClickListener, final boolean isForce) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		dialog.setCancelable(!isForce);
		dialog.show();

		Window view = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		view.setContentView(R.layout.dialog_upload_apk);

		TextView tv_msg_info = (TextView) view.findViewById(R.id.tv_msg_info);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

		if (isForce) {
			view.findViewById(R.id.tv_later).setVisibility(View.GONE);
			view.findViewById(R.id.iv_line).setVisibility(View.GONE);
		}

		if (info != null) {
			tv_msg_info.setText(info);
		}

		if (title != null) {
			tv_title.setText(title);
		} else {
			// 没有title的类型
			view.findViewById(R.id.rl_title).setVisibility(View.GONE);
		}

		View tv_ok = view.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okOnClickListener != null)
					okOnClickListener.onClick(v);
				if (!isForce) {
					dialog.dismiss();
				}
			}
		});
		View tv_later = view.findViewById(R.id.tv_later);
		tv_later.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cancleOnClickListener != null)
					cancleOnClickListener.onClick(v);
				dialog.dismiss();
			}
		});

		return dialog;
	}

	/**
	 * @param context
	 * 
	 * @return 选择发送
	 */
	public static AlertDialog getPhotoDialog(Context context,
			final OnClickListener albumClick, final OnClickListener photoClick) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		// 消除了AlertDialog背后的黑框
		dialog.show();
		Window view = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		view.setContentView(R.layout.dialog_select_photo);

		TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
		TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);

		View tv_ok = view.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		tv_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (photoClick != null)
					photoClick.onClick(v);
				dialog.dismiss();
			}
		});

		tv_album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (albumClick != null)
					albumClick.onClick(v);
				dialog.dismiss();
			}
		});

		return dialog;
	}
	
	/**
	 * 垂直布局的多个View对话框
	 * @param context
	 * @param edits
	 * @param onClick
	 * @return
	 */
	public static AlertDialog getDialogWithViews(Context context,
			List<View> views,
			final DialogClickListener onClick) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		// 消除了AlertDialog背后的黑框
		dialog.show();
		Window view = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		view.setContentView(R.layout.dialog_textedits);
		LinearLayout layout = (LinearLayout)view.findViewById(R.id.ll_edits);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
		for (View v : views) {
			layout.addView(v, params);
		}
		
		view.findViewById(R.id.tv_cancle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClick.onCancel();
				dialog.dismiss();
			}
		});
		
		view.findViewById(R.id.tv_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClick.onOK();
				dialog.dismiss();
			}
		});
		
		return dialog;
	}



	/**
	 * 获得自定义TranslateAnimation 动画
	 * 
	 * @param context
	 * @param msg
	 * @param type
	 *            1代表红色提示，2代表浅蓝色；
	 */
	public static void showToastAnimation(Activity context, String msg, int type) {

		final Dialog webDialog = new Dialog(context, R.style.dialog_translate);// 创建自定义样式dialog

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.translate_animation_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.translate_all_layout);// 加载布局
		RelativeLayout layout_image = (RelativeLayout) v
				.findViewById(R.id.translate_layout);// 背景
		ImageView image = (ImageView) v.findViewById(R.id.translate_image);// 提示类型小图标
		TextView tipTextView = (TextView) v
				.findViewById(R.id.translate_prompt_content);// 提示文字
		if (type == 1) {
			tipTextView.setText(msg);
		} else if (type == 2) {
			layout_image.setBackgroundResource(R.drawable.ic_login_regist_);
			image.setBackgroundResource(R.drawable.ic_life_dynactivity_icon);
			tipTextView.setText(msg);
		}
		// 加载动画
		final Animation translateAnimation = new TranslateAnimation(0, 0, 0,
				100);
		// 设置动画时间
		translateAnimation.setDuration(3000);
		layout_image.setAnimation(translateAnimation);
		tipTextView.setText(msg);// 设置加载信息
		translateAnimation.startNow();// 开始动画
		translateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (translateAnimation != null)
					translateAnimation.cancel();
				if (webDialog != null) {
					webDialog.dismiss();
				}
			}
		});

		webDialog.setCanceledOnTouchOutside(false);// 点击外部不可以取消关闭窗体
		webDialog.setCancelable(true);// 不可以用“返回键”取消
		Window window = webDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.5f;
		window.setGravity(Gravity.TOP);
		window.setAttributes(lp);
		webDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		webDialog.show();
	}

	public static AlertDialog getDialogWithTwoBtn(Context context,
												  String title, String info,String okText,String cancelText,
												  final DialogClickListener clickListener) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();

		// 消除了AlertDialog背后的黑框
		dialog.show();
		Window view = dialog.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		view.setContentView(R.layout.dialog_base_blue_info);

		TextView tv_msg_info = (TextView) view.findViewById(R.id.tv_msg_info);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		if (info != null) {
			tv_msg_info.setText(info);
		}

		if (!TextUtils.isEmpty(title)) {
			tv_title.setText(title);
		} else {
			// 没有title的类型
			view.findViewById(R.id.rl_title).setVisibility(View.GONE);
		}

		TextView tv_ok = (TextView)view.findViewById(R.id.tv_ok);
		if(okText !=null){
			tv_ok.setText(okText);
		}
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onOK();
				dialog.dismiss();
			}
		});
		TextView tv_later = (TextView)view.findViewById(R.id.tv_cancle);
		if(cancelText !=null){
			tv_later.setText(cancelText);
		}
		tv_later.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null)
					clickListener.onCancel();
				dialog.dismiss();
			}
		});

		// dialog.setView(view, 0, 0, 0, 0);

		return dialog;
	}

}
