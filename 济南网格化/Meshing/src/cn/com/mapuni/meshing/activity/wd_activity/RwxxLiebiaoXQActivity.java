package cn.com.mapuni.meshing.activity.wd_activity;

import java.util.ArrayList;

import org.json.JSONObject;
import cn.com.mapuni.meshing.activity.PhtoActivity;
import cn.com.mapuni.meshing.activity.db_activity.DbscfkActivity;
import cn.com.mapuni.meshing.activity.photo.PicViewerActivity;
import cn.com.mapuni.meshing.model.Xcxq;
import cn.com.mapuni.meshing.util.FlowLayout;
import cn.com.mapuni.meshing.util.TypeUtils;

import com.bumptech.glide.Glide;
import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RwxxLiebiaoXQActivity extends BaseActivity implements
		OnClickListener {
	LinearLayout ll_wtlb, ll_wtlb_divider;
	TextView rwmc, xjdx, wgmc, wgy, wz, wtlb, wtms;
	LinearLayout ll_middle;
	FlowLayout ll_wtms;
	LinearLayout middleLayout;
	View mainView;
	String startDate = "", endDate = "";
	private Xcxq xcxq;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("Ѳ���¼");
		initView();
		initData();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.rwxxlbxqactivity_layout, null);
		middleLayout.addView(mainView);
		// ������Ϣ
		rwmc = (TextView) mainView.findViewById(R.id.rwmc);
		xjdx = (TextView) mainView.findViewById(R.id.xjdx);
		wgmc = (TextView) mainView.findViewById(R.id.wgmc);
		wgy = (TextView) mainView.findViewById(R.id.wgy);
		wz = (TextView) mainView.findViewById(R.id.wz);
		ll_wtlb = (LinearLayout) mainView.findViewById(R.id.ll_wtlb);
		ll_wtlb_divider = (LinearLayout) mainView
				.findViewById(R.id.ll_wtlb_divider);
		wtlb = (TextView) mainView.findViewById(R.id.wtlb);
		wtms = (TextView) mainView.findViewById(R.id.wtms);
		// ������������
		ll_wtms = (FlowLayout) mainView.findViewById(R.id.ll_wtms);
		// ������Ϣ����
		ll_middle = (LinearLayout) mainView.findViewById(R.id.ll_middle);
	}

	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	String id;
	private YutuLoading yutuLoading;

	private void initData() {
		id = getIntent().getStringExtra("id");
		String patrolId = getIntent().getStringExtra("patrolId");

		yutuLoading = new YutuLoading(RwxxLiebiaoXQActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ���ݣ����Ժ�", "");
		yutuLoading.showDialog();

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// ����ύ����
		String sessionId = DisplayUitl.readPreferences(
				RwxxLiebiaoXQActivity.this, LAST_USER_SP_NAME, "sessionId");

		params.addBodyParameter("sessionId", sessionId);

		String url = PathManager.BASE_URL_JINNAN + "/JiNanhuanbaoms/patrol/"
				+ patrolId + ".do";

		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(RwxxLiebiaoXQActivity.this, "��������ʧ��", 200)
						.show();
				finish();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				try {
					String result = String.valueOf(arg0.result);
					JSONObject jsonObject = new JSONObject(result);
					String message = jsonObject.getString("message");
					if (message.contains("�ɹ�")) {
						Gson gson = new Gson();
						xcxq = gson.fromJson(result, Xcxq.class);
						rwmc.setText(xcxq.getContent().getTaskName());
						xjdx.setText(xcxq.getContent().getPatrolObjectName());
						wgmc.setText(xcxq.getContent().getCreateGridName());
						wgy.setText(xcxq.getContent().getCreateUserName());
						wz.setText(xcxq.getContent().getAddress());
						wtms.setText(xcxq.getContent().getProblemDesc());

						if (xcxq.getContent().getIsHaveProblem() != null
								&& xcxq.getContent().getIsHaveProblem()
										.equals("1")) {
							if (xcxq.getContent().getProblems() != null
									&& xcxq.getContent().getProblems().size() > 0) {
								String temp = "";
								if (xcxq.getContent().getProblems().get(0)
										.getProblemName() != null) {
									temp = xcxq.getContent().getProblems()
											.get(0).getProblemName().toString();
								}
								for (int i = 1; i < xcxq.getContent()
										.getProblems().size(); i++) {
									if (!temp.equals("")
											&& xcxq.getContent().getProblems()
													.get(i).getProblemName() != null) {
										temp = temp
												+ ","
												+ xcxq.getContent()
														.getProblems().get(i)
														.getProblemName();
									}
								}
								wtlb.setText(temp);
							}
						} else {
							ll_wtlb.setVisibility(View.GONE);
							ll_wtlb_divider.setVisibility(View.GONE);
						}

						// ��������ͼƬ��ȡ
						final ArrayList<String> imgPaths1 = new ArrayList<String>();// ͼƬ·������
						LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(
								190, 190);
						params_img.gravity = Gravity.CENTER;
						params_img.setMargins(20, 10, 20, 10);
						ImageView imageView;
						for (int j = 0; j < xcxq.getContent().getProblemImgs().size(); j++) {
							final String imagePath = PathManager.IMG_URL_JINAN
									+ xcxq.getContent().getProblemImgs().get(j)
											.getImgPath();
							if(TypeUtils.isIMG(imagePath)){
								imageView = new ImageView(
										RwxxLiebiaoXQActivity.this);
								imageView.setLayoutParams(params_img);
								imageView.setScaleType(ScaleType.CENTER_CROP);
								
								imgPaths1.add(imagePath);
								Glide.with(RwxxLiebiaoXQActivity.this)
										.load(imagePath)
										.placeholder(R.drawable.wd_xcjl_jzz)
										.error(R.drawable.wd_xcjl_jzsb)
										.into(imageView);
								ll_wtms.addView(imageView);
							}
							// imageView.setOnClickListener(new
							// OnClickListener() {
							//
							// @Override
							// public void onClick(View arg0) {
							// // TODO Auto-generated method stub
							// Intent intent = new Intent(
							// RwxxLiebiaoXQActivity.this,
							// PhtoActivity.class);
							// intent.putExtra("path", imagePath);
							// startActivity(intent);
							// }
							// });
						}
						ll_wtms.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										RwxxLiebiaoXQActivity.this,
										PicViewerActivity.class);
								intent.putStringArrayListExtra("imgPaths",
										imgPaths1);
								startActivity(intent);
							}
						});
						// ��������б�
						TextView textView;
						for (int i = 0; i < xcxq.getContent().getRecords()
								.size(); i++) {
							LinearLayout top = new LinearLayout(
									RwxxLiebiaoXQActivity.this);
							top.setOrientation(LinearLayout.HORIZONTAL);
							textView = new TextView(RwxxLiebiaoXQActivity.this);
							textView.setTextSize(16);
							textView.setTextColor(Color.parseColor("#141414"));
							textView.setText("����λ��"
									+ xcxq.getContent().getRecords().get(i)
											.getHandlerGridName());
							top.addView(textView);
							textView = new TextView(RwxxLiebiaoXQActivity.this);
							textView.setTextSize(16);
							textView.setTextColor(Color.parseColor("#141414"));
							textView.setPadding(60, 0, 0, 0);
							if (xcxq.getContent().getRecords().get(i)
									.getStatus().equals("1")) {
								textView.setText("����״̬�����д���");
							} else if (xcxq.getContent().getRecords().get(i)
									.getStatus().equals("2")) {
								textView.setText("����״̬���ϱ�");
							} else if (xcxq.getContent().getRecords().get(i)
									.getStatus().equals("3")) {
								textView.setText("����״̬��ת��");
							} else if (xcxq.getContent().getRecords().get(i)
									.getStatus().equals("4")) {
								textView.setText("����״̬���˻�");
							} else if (xcxq.getContent().getRecords().get(i)
									.getStatus().equals("5")) {
								textView.setText("����״̬������");
							} else if (xcxq.getContent().getRecords().get(i)
									.getStatus().equals("6")) {
								textView.setText("����״̬�����");
							}
							// top.addView(textView);//����״̬��ʱ����
							ll_middle.addView(top);

							LinearLayout bottom = new LinearLayout(
									RwxxLiebiaoXQActivity.this);
							bottom.setOrientation(LinearLayout.HORIZONTAL);
							textView = new TextView(RwxxLiebiaoXQActivity.this);
							textView.setTextSize(16);
							textView.setTextColor(Color.parseColor("#141414"));
							textView.setText("���������"
									+ xcxq.getContent().getRecords().get(i)
											.getOpinion());
							bottom.addView(textView);
							if(!TextUtils.isEmpty(xcxq.getContent().getRecords().get(i)
											.getOpinion())){
								ll_middle.addView(bottom);
							}							
							FlowLayout ll_temp = new FlowLayout(
									RwxxLiebiaoXQActivity.this);
							final ArrayList<String> imgPaths = new ArrayList<String>();// ͼƬ·������
							ll_temp.setLayoutParams(new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT));
//							ll_temp.setOrientation(LinearLayout.HORIZONTAL);
//							ll_temp.setGravity(Gravity.CENTER);
							for (int j = 0; j < xcxq.getContent().getRecords()
									.get(i).getImgs().size(); j++) {
								final String path = PathManager.IMG_URL_JINAN
										+ xcxq.getContent().getRecords().get(i)
												.getImgs().get(j).getImgPath();
								if(TypeUtils.isIMG(path)){

									imageView = new ImageView(
											RwxxLiebiaoXQActivity.this);
									imageView.setLayoutParams(params_img);
									imageView.setScaleType(ScaleType.CENTER_CROP);
									
									imgPaths.add(path);
									Glide.with(RwxxLiebiaoXQActivity.this)
											.load(path)
											.placeholder(R.drawable.wd_xcjl_jzz)
											.error(R.drawable.wd_xcjl_jzsb)
											.into(imageView);
									ll_temp.addView(imageView);
								}
								

								// imageView
								// .setOnClickListener(new OnClickListener() {
								//
								// @Override
								// public void onClick(View arg0) {
								// // TODO Auto-generated method
								// // stub
								// Intent intent = new Intent(
								// RwxxLiebiaoXQActivity.this,
								// PhtoActivity.class);
								// intent.putExtra("path", path);
								// startActivity(intent);
								// }
								// });
							}
							ll_temp.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											RwxxLiebiaoXQActivity.this,
											PicViewerActivity.class);
									intent.putStringArrayListExtra("imgPaths",
											imgPaths);
									startActivity(intent);
								}
							});
							ll_middle.addView(ll_temp);

							ImageView dividerImageView = new ImageView(
									RwxxLiebiaoXQActivity.this);
							dividerImageView
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 1));
							dividerImageView.setBackgroundColor(Color.BLACK);
							ll_middle.addView(dividerImageView);
						}

					} else {
						Toast.makeText(RwxxLiebiaoXQActivity.this, "��������ʧ��",
								200).show();
						finish();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(RwxxLiebiaoXQActivity.this, "���ݽ���ʧ��", 200)
							.show();
					finish();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_bu:
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
