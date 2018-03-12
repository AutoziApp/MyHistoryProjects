package com.jy.environment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;
/**
 * 反馈
 * @author baiyuchuan
 *
 */
public class SettingFeedbackActivity extends ActivityBase {

	private ImageView back;
	private Button send;
	private EditText et1, et2;
	String content, mail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_feedback_activity);
		back = (ImageView) findViewById(R.id.suggest);
		et1 = (EditText) findViewById(R.id.edit_text1);
		et2 = (EditText) findViewById(R.id.edit_text2);

		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				content = et1.getText().toString();
				mail = et2.getText().toString();

				if ("".equals(content)) {
					Toast.makeText(SettingFeedbackActivity.this, "你还没有填写", 0).show();
					return;
				} else {

					try {
					    FeedBackTask feedBackTask = new FeedBackTask();
					    feedBackTask.execute();
					} catch (Exception e) {
					    e.printStackTrace();
					}

				}
			}
		});

	}
	

	protected void dialogSucess() {
		AlertDialog dialog = new AlertDialog.Builder(SettingFeedbackActivity.this)
				.setTitle("提示").setMessage("发送成功，感谢您的宝贵意见")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				}).create();
		dialog.show();
	}
	
	
	class FeedBackTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			if (NetUtil.getNetworkState(SettingFeedbackActivity.this) == NetUtil.NETWORN_NONE) 
			{
				Looper.prepare();
				CommonUtil.showToast(SettingFeedbackActivity.this, "请检查网络设置");
				Looper.loop();
				return false;
			}
			String url = UrlComponent.opinion_Post;
		        BusinessSearch search = new BusinessSearch();
			DiscoverFlagModel _Result ;
			    try {
				_Result = search.feedBack(url,content,mail);
				return _Result.isFlag();
			    } catch (Exception e) {
				e.printStackTrace();
			    }
			    return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				MyLog.i("weibao result:" + result);
			super.onPostExecute(result);
			if (result) {
				dialogSucess();
			}

		} catch (Exception e) {
			MyLog.e("weibao Exception", e);
		}
		}

	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
	
}
