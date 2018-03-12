package com.jy.environment.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import com.jy.environment.R;
import com.jy.environment.adapter.DiscoverFaceGridViewAdapter;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.business.BusinessSearch;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.ResultPostBlogComment;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.util.ToastUtil;
import com.jy.environment.webservice.UrlComponent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发现环境说评论
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverBlogPostCommentsActivity extends ActivityBase {

	private String observer = "正在发生";;
	private String content;
	private String time;
	private String weiboid;
	private Button sendBtn;
	private EditText contentEdt;
	private Button cancelBtn;
	/**
	 * 评论人
	 */
	private TextView pinglunren;
	private ImageView comment_image;
	private InputMethodManager imm;
	private DiscoverFaceGridViewAdapter mGVFaceAdapter;
	private GridView mGridView;
	private SendBlogCommentTask sendBlogCommentTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.discove_blog_post_comment);

		pinglunren = (TextView) findViewById(R.id.pinglunren);
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		comment_image = (ImageView) findViewById(R.id.comment_image);
		comment_image.setOnClickListener(faceClickListener);

		if (WeiBaoApplication.usename != null) {
			pinglunren.setText(WeiBaoApplication.usename);
		}
		sendBtn = (Button) findViewById(R.id.comment_send);
		sendBtn.setOnClickListener(listener);
		cancelBtn = (Button) findViewById(R.id.commont_cancel);
		cancelBtn.setOnClickListener(listener);
		contentEdt = (EditText) findViewById(R.id.comment_content);
	}

	private View.OnClickListener faceClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			showOrHideIMM();
		}
	};

	private void showOrHideIMM() {
		if (comment_image.getTag() == null) {
			// 隐藏软键盘
			imm.hideSoftInputFromWindow(contentEdt.getWindowToken(), 0);
			// 显示表情
			showFace();
		} else {
			// 显示软键盘
			imm.showSoftInput(contentEdt, 0);
			// 隐藏表情
			hideFace();
		}
	}

	private void showFace() {
		// mFace.setImageResource(R.drawable.widget_bar_keyboard);
		comment_image.setTag(1);
		createdia();
		mGridView.setVisibility(View.VISIBLE);
	}

	private void hideFace() {
		// mFace.setImageResource(R.drawable.widget_bar_face);
		comment_image.setTag(null);
		// mGridView.setVisibility(View.GONE);
	}

	void createdia() {

		AlertDialog.Builder builder = new Builder(
				DiscoverBlogPostCommentsActivity.this);
		LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.discover_face, null);
		mGVFaceAdapter = new DiscoverFaceGridViewAdapter(this);
		mGridView = (GridView) linearLayout.findViewById(R.id.tweet_pub_faces);
		mGridView.setAdapter(mGVFaceAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 插入的表情
				SpannableString ss = new SpannableString(view.getTag()
						.toString());
				Drawable d = getResources().getDrawable(
						(int) mGVFaceAdapter.getItemId(position));
				d.setBounds(0, 0, 50, 50);// 设置表情图片的显示大小
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, view.getTag().toString().length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// 在光标所在处插入表情
				contentEdt.getText().insert(contentEdt.getSelectionStart(), ss);
			}
		});

		AlertDialog dialog = builder.create();
		dialog.setView(linearLayout, 0, 0, 0, 0);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.comment_send:
				sendComment();
				break;
			case R.id.commont_cancel:
				finish();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 将bitmap转换成base64string
	 * 
	 * @param bitmap
	 * @return
	 */
	public String bitmaptoString(Bitmap bitmap) {
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		try {
			bStream.flush();
			bStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	private void sendComment() {
		content = contentEdt.getText().toString().trim();
		if ("".equals(content)) {
			ToastUtil.showLong(getApplicationContext(), "请输入发表内容");
			return;
		}
		JSONObject jsonObject = new JSONObject();
		if (WeiBaoApplication.usename != null
				&& WeiBaoApplication.usename != "") {
			observer = WeiBaoApplication.usename;

		}
		java.util.Date currentTime = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		time = sdf.format(currentTime);
		weiboid = getIntent().getStringExtra("weiboid");

		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			Toast.makeText(DiscoverBlogPostCommentsActivity.this, "请检查您的网络", 0)
					.show();
		} else {
			sendBlogCommentTask = new SendBlogCommentTask();
			sendBlogCommentTask.execute();
		}
	}

	public class SendBlogCommentTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog prDialog;

		@Override
		protected void onPreExecute() {
			prDialog = new ProgressDialog(DiscoverBlogPostCommentsActivity.this);
			prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			prDialog.setMessage("正在发布中...");

			// 进度条是否不明确
			prDialog.setIndeterminate(true);
			prDialog.setCancelable(true);
			prDialog.show();
			super.onPreExecute();
			View view = getWindow().peekDecorView();
			if (view != null) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(DiscoverBlogPostCommentsActivity.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}

		}

		@Override
		protected Boolean doInBackground(String... params) {
			String url = UrlComponent.saveCommentsUrl_Post;
			BusinessSearch search = new BusinessSearch();
			try {
				
				ResultPostBlogComment _Result = search.sendBlogComment(url,
						observer, content, time, weiboid);
				MyLog.i("_Result========================"+_Result);
				return _Result.isFlag();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			try {
				prDialog.cancel();
				if (result) {
					Intent broad = new Intent();
					broad.setAction(DiscoverBlogListActivity.MESSAGE_RECEIVED_ACTION2);
					getApplicationContext().sendBroadcast(broad);
					finish();
				} else {
					Toast.makeText(DiscoverBlogPostCommentsActivity.this, "评论失败", 0)
							.show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e("weibao Exception" + e);
			}
		
		}

	}
}
