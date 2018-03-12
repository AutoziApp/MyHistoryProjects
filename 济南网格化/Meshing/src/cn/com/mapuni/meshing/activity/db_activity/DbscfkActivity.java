package cn.com.mapuni.meshing.activity.db_activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.mapuni.meshing.activity.PhtoActivity;
import cn.com.mapuni.meshing.activity.photo.PicViewerActivity;
import cn.com.mapuni.meshing.activity.wd_activity.RwxxXcjlActivity;
import cn.com.mapuni.meshing.activity.wd_activity.WdMainActivity;
import cn.com.mapuni.meshing.activity.xc_activity.SaveXcrwActivity;
import cn.com.mapuni.meshing.activity.xc_activity.XcrwActivity;
import cn.com.mapuni.meshing.adapter.MyExpandableListViewAdapter;
import cn.com.mapuni.meshing.model.PotrlObject;
import cn.com.mapuni.meshing.model.ShangBaoBuMen;
import cn.com.mapuni.meshing.model.Xcxq;
import cn.com.mapuni.meshing.model.Xcxq.ContentBean.RecordsBean;
import cn.com.mapuni.meshing.util.FlowLayout;
import cn.com.mapuni.meshing.util.FlowRadioGroup;
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
import com.mapuni.android.photograph.BimpHelper;
import com.mapuni.android.photograph.Cameras;
import com.mapuni.android.photograph.CheckPicActivity;
import com.mapuni.android.photograph.GridAdapter;
import com.mapuni.android.photograph.PhotoGridView;
import com.mapuni.android.photograph.PhotoPopupWindows;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DbscfkActivity extends BaseActivity implements OnClickListener {
	private Button submit_bu;
	LinearLayout ll_wtlb, ll_wtlb_divider;
	TextView rwmc, xjdx, wgmc, wgy, wz, wtlb, wtms;
	RadioGroup rg_clyj;
	RadioButton rb_self_solve, rb_shangbao, rb_huitui, rb_zhuanban, rb_fuhe, rb_foujue, rb_baocun;
	LinearLayout ll_middle, ll_zbbm, ll_sbbm;
	FlowLayout ll_wtms;
	Spinner sp_zbbm;
	public EditText edt_chuli_yijian;
	LinearLayout middleLayout;
	View mainView;
	String startDate = "", endDate = "";
	/** ���Ԥ���б� */
	private PhotoGridView myGridView;
	private GridAdapter adapter;
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** ����ü� */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	Cameras cameras;
	private Xcxq xcxq;
	TextView sp_choose_shangbao;// ѡ���ϱ����ŵ�����
	String haveFreeRole;
	private String haveLiaisonRole; // �Ƿ�Ϊת����������Ա��
	private String currenStatus; // ��ǰ����״̬
	private String organization_code;
	private String judgeTura = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("����");
		cameras = new Cameras(DbscfkActivity.this);
		initView();
		initPhotoGrideView();
		getBrothers();
		getShangBaoBuMen();
		initData();
	}

	private void initView() {
		organization_code = DisplayUitl.readPreferences(this, LAST_USER_SP_NAME, "organization_code");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.dbscfkactivity_layout, null);
		middleLayout.addView(mainView);
		// ������Ϣ
		rwmc = (TextView) mainView.findViewById(R.id.rwmc);
		xjdx = (TextView) mainView.findViewById(R.id.xjdx);
		wgmc = (TextView) mainView.findViewById(R.id.wgmc);
		wgy = (TextView) mainView.findViewById(R.id.wgy);
		wz = (TextView) mainView.findViewById(R.id.wz);
		ll_wtlb = (LinearLayout) mainView.findViewById(R.id.ll_wtlb);
		ll_wtlb_divider = (LinearLayout) mainView.findViewById(R.id.ll_wtlb_divider);
		wtlb = (TextView) mainView.findViewById(R.id.wtlb);
		wtms = (TextView) mainView.findViewById(R.id.wtms);
		// ������������
		ll_wtms = (FlowLayout) mainView.findViewById(R.id.ll_wtms);

		ll_middle = (LinearLayout) mainView.findViewById(R.id.ll_middle);

		rg_clyj = (RadioGroup) mainView.findViewById(R.id.rg_clyj);
		rb_self_solve = (RadioButton) mainView.findViewById(R.id.rb_self_solve);
		rb_shangbao = (RadioButton) mainView.findViewById(R.id.rb_shangbao);
		rb_huitui = (RadioButton) mainView.findViewById(R.id.rb_huitui);
		rb_zhuanban = (RadioButton) mainView.findViewById(R.id.rb_zhuanban);
		rb_fuhe = (RadioButton) mainView.findViewById(R.id.rb_fuhe);
		rb_foujue = (RadioButton) mainView.findViewById(R.id.rb_foujue);
		rb_baocun = (RadioButton) mainView.findViewById(R.id.rb_baocun);
		String havePatrolRole = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "havePatrolRole");
		String haveAdminRole = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "haveAdminRole");
		haveFreeRole = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "haveFreeRole");
		haveLiaisonRole = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "haveLiaisonRole");
		String haveInspectorRole = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME,
				"haveInspectorRole");

		String user_Account = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "user_Account");
		rg_clyj.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				String haveFreeRole = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME,
						"haveFreeRole");
				if (arg1 == R.id.rb_zhuanban) {
					ll_zbbm.setVisibility(View.VISIBLE);
				} else if (haveFreeRole != null && haveFreeRole.equals("1") && arg1 == R.id.rb_shangbao) {
					ll_sbbm.setVisibility(View.VISIBLE);
				} else {
					ll_sbbm.setVisibility(View.GONE);
				}
			}
		});

		ll_zbbm = (LinearLayout) mainView.findViewById(R.id.ll_zbbm);
		ll_sbbm = (LinearLayout) mainView.findViewById(R.id.shangbao_layout);
		sp_zbbm = (Spinner) mainView.findViewById(R.id.sp_zbbm);

		edt_chuli_yijian = (EditText) mainView.findViewById(R.id.edt_chuli_yijian);

		submit_bu = (Button) mainView.findViewById(R.id.submit_bu);
		submit_bu.setOnClickListener(this);
		sp_choose_shangbao = (TextView) mainView.findViewById(R.id.sp_choose_shangbao);
		sp_choose_shangbao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectShangbaoBuMenData();
			}
		});

		if (haveLiaisonRole != null && haveLiaisonRole.equals("1")) {// ����Ա
			rb_self_solve.setVisibility(View.VISIBLE);
			rb_self_solve.setText("�������ύ");
			rb_shangbao.setVisibility(View.GONE);
			// rb_shangbao.setText("�������ύ");
			rb_huitui.setVisibility(View.VISIBLE);
			rb_zhuanban.setVisibility(View.GONE);
			rb_foujue.setVisibility(View.GONE); // ���ط����ť
			rb_fuhe.setVisibility(View.GONE);
		} else if (haveAdminRole != null && haveAdminRole.equals("1")) {// ���ĸ�����
			if (organization_code.length() < 8) {// 1��
				if (judgeTura.equals("5")) {
					rb_shangbao.setVisibility(View.GONE);
					rb_self_solve.setVisibility(View.GONE);
					rb_baocun.setVisibility(View.VISIBLE);
					rb_huitui.setVisibility(View.GONE);
					rb_foujue.setVisibility(View.VISIBLE);
					rb_fuhe.setVisibility(View.VISIBLE);
					rb_zhuanban.setVisibility(View.GONE);
				} else {
					rb_shangbao.setVisibility(View.GONE);
					rb_self_solve.setVisibility(View.VISIBLE);
					rb_baocun.setVisibility(View.VISIBLE);
					rb_huitui.setVisibility(View.VISIBLE);
					rb_foujue.setVisibility(View.VISIBLE);
					rb_fuhe.setVisibility(View.GONE);
					rb_zhuanban.setVisibility(View.VISIBLE);

				}
				return;

			} else if (organization_code.length() < 10) {// ����
				if (judgeTura.equals("5")) {
					rb_shangbao.setVisibility(View.GONE);
					rb_self_solve.setVisibility(View.GONE);
					rb_baocun.setVisibility(View.VISIBLE);
					rb_huitui.setVisibility(View.GONE);
					rb_foujue.setVisibility(View.VISIBLE);
					rb_fuhe.setVisibility(View.VISIBLE);
					rb_zhuanban.setVisibility(View.GONE);
				} else {
					rb_shangbao.setVisibility(View.VISIBLE);
					rb_self_solve.setVisibility(View.VISIBLE);
					rb_baocun.setVisibility(View.VISIBLE);
					rb_huitui.setVisibility(View.VISIBLE);
					rb_foujue.setVisibility(View.VISIBLE);
					rb_fuhe.setVisibility(View.GONE);
					rb_zhuanban.setVisibility(View.VISIBLE);
				}

				return;
			} else {// ����
				rb_shangbao.setVisibility(View.VISIBLE);
				rb_self_solve.setVisibility(View.VISIBLE);
				rb_baocun.setVisibility(View.VISIBLE);
				rb_huitui.setVisibility(View.VISIBLE);
				rb_foujue.setVisibility(View.VISIBLE);
				rb_fuhe.setVisibility(View.GONE);
				rb_zhuanban.setVisibility(View.GONE);
			}
		} else if (havePatrolRole != null && havePatrolRole.equals("1")) {// ����Ա
			rb_shangbao.setVisibility(View.VISIBLE);
			rb_self_solve.setVisibility(View.VISIBLE);
			rb_baocun.setVisibility(View.VISIBLE);
			rb_huitui.setVisibility(View.VISIBLE);
			rb_foujue.setVisibility(View.VISIBLE);
			rb_fuhe.setVisibility(View.GONE);
			rb_zhuanban.setVisibility(View.GONE);

		} else if (haveFreeRole != null && haveFreeRole.equals("1")) {// ����Ѳ��Ա
			rb_self_solve.setVisibility(View.VISIBLE);
			rb_shangbao.setVisibility(View.VISIBLE);
			rb_huitui.setVisibility(View.GONE);
			rb_zhuanban.setVisibility(View.GONE);
		} else if (haveInspectorRole != null && haveInspectorRole.equals("1")) {// �ļ�Ѳ��Ա
			rb_shangbao.setVisibility(View.VISIBLE);
			rb_self_solve.setVisibility(View.VISIBLE);
			rb_baocun.setVisibility(View.VISIBLE);
			rb_huitui.setVisibility(View.GONE);
			rb_foujue.setVisibility(View.GONE);
			rb_fuhe.setVisibility(View.GONE);
			rb_zhuanban.setVisibility(View.GONE);
		}
	}

	private void initPhotoGrideView() {
		adapter = new GridAdapter(this);
		adapter.update();
		myGridView = (PhotoGridView) mainView.findViewById(R.id.noScrollgridview);
		myGridView.setAdapter(adapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				if (arg2 == BimpHelper.bmp.size()) {
					new PhotoPopupWindows(DbscfkActivity.this, myGridView, cameras);
				} else {
					Intent intent = new Intent(DbscfkActivity.this, CheckPicActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * ��ȡת�첿���嵥 http://192.168.1.174:8080/JiNanhuanbaoms/grid/37010403/brothers?
	 */
	List<String> code = new ArrayList<String>();

	private void getBrothers() {
		final YutuLoading yutuLoading = new YutuLoading(DbscfkActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ���ݣ����Ժ�", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// ����ύ����
		String sessionId = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "sessionId");
		params.addBodyParameter("sessionId", sessionId);
		String handlerGrid = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "organization_code");
		String url = PathManager.BASE_URL_JINNAN + "/JiNanhuanbaoms/grid/" + handlerGrid + "/brothers?sessionId="
				+ sessionId;
		utils.send(HttpMethod.POST, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(DbscfkActivity.this, "��������ʧ��", 200).show();
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
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						JSONArray jsonArray = jsonObject.getJSONArray("rows");
						if (jsonArray.length() > 0) {
							code = new ArrayList<String>();
							List<String> list = new ArrayList<String>();
							for (int i = 0; i < jsonArray.length(); i++) {
								code.add(jsonArray.getJSONObject(i).getString("code"));
								list.add(jsonArray.getJSONObject(i).getString("name"));
							}
							initSpinner(sp_zbbm, list);
						} else {
							rb_zhuanban.setVisibility(View.GONE);
							rb_fuhe.setVisibility(View.GONE);
						}
					} else {
						code = new ArrayList<String>();
						List<String> list = new ArrayList<String>();
						code.add("");
						list.add("����");
						initSpinner(sp_zbbm, list);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(DbscfkActivity.this, "���ݽ���ʧ��1", 200).show();
					finish();
				}
			}
		});
	}

	// spinner��ʼ���򵥷�װ
	private void initSpinner(Spinner spinner, List<String> data) {

		// ����Adapter���Ұ�����Դ
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	String id;

	private void initData() {
		id = getIntent().getStringExtra("id");
		currenStatus = getIntent().getStringExtra("currenStatus");
		String patrolId = getIntent().getStringExtra("patrolId");
		judgeTura = getIntent().getStringExtra("judgeTura");
		yutuLoading = new YutuLoading(DbscfkActivity.this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڻ�ȡ���ݣ����Ժ�", "");
		yutuLoading.showDialog();

		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		RequestParams params = new RequestParams();// ����ύ����
		String sessionId = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "sessionId");
		// String sessionId="8a8f87ac5abc443b015abc48d5120002";

		params.addBodyParameter("sessionId", sessionId);

		String url = PathManager.BASE_URL_JINNAN + "/JiNanhuanbaoms/patrol/" + patrolId + ".do";
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(DbscfkActivity.this, "��������ʧ��", 200).show();
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
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						Gson gson = new Gson();
						xcxq = gson.fromJson(result, Xcxq.class);
						rwmc.setText(xcxq.getContent().getTaskName());
						xjdx.setText(xcxq.getContent().getPatrolObjectName());
						wgmc.setText(xcxq.getContent().getCreateGridName());
						wgy.setText(xcxq.getContent().getCreateUserName());
						wz.setText(xcxq.getContent().getAddress());
						wtms.setText(xcxq.getContent().getProblemDesc());

						if (xcxq.getContent().getIsHaveProblem() != null
								&& xcxq.getContent().getIsHaveProblem().equals("1")) {
							if (xcxq.getContent().getProblems() != null && xcxq.getContent().getProblems().size() > 0) {
								String temp = "";
								if (xcxq.getContent().getProblems().get(0).getProblemName() != null) {
									temp = xcxq.getContent().getProblems().get(0).getProblemName().toString();
								}
								for (int i = 1; i < xcxq.getContent().getProblems().size(); i++) {
									if (!temp.equals("")
											&& xcxq.getContent().getProblems().get(i).getProblemName() != null) {
										temp = temp + "," + xcxq.getContent().getProblems().get(i).getProblemName();
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
						LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(190, 190);
						params_img.gravity = Gravity.CENTER;
						params_img.setMargins(20, 10, 20, 10);
						ImageView imageView;
						for (int j = 0; j < xcxq.getContent().getProblemImgs().size(); j++) {
							String imgPath = xcxq.getContent().getProblemImgs().get(j).getImgPath();
							if(TypeUtils.isIMG(imgPath)){
							imageView = new ImageView(DbscfkActivity.this);
							imageView.setLayoutParams(params_img);
							imageView.setScaleType(ScaleType.CENTER_CROP);
							final String imagePath = PathManager.IMG_URL_JINAN
									+ xcxq.getContent().getProblemImgs().get(j).getImgPath();
							imgPaths1.add(imagePath);
							Glide.with(DbscfkActivity.this).load(imagePath).placeholder(R.drawable.wd_xcjl_jzz)
									.error(R.drawable.wd_xcjl_jzsb).into(imageView);
							ll_wtms.addView(imageView);
							}
							// imageView.setOnClickListener(new
							// OnClickListener() {
							// @Override
							// public void onClick(View arg0) {
							// // TODO Auto-generated method stub
							// Intent intent = new Intent(DbscfkActivity.this,
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
								Intent intent = new Intent(DbscfkActivity.this, PicViewerActivity.class);
								intent.putStringArrayListExtra("imgPaths", imgPaths1);
								startActivity(intent);
							}
						});
						// ��������б�
						TextView textView;
						for (int i = 0; i < xcxq.getContent().getRecords().size(); i++) {
							RecordsBean recordsBean = xcxq.getContent().getRecords().get(i);
							LinearLayout top = new LinearLayout(DbscfkActivity.this);
							top.setOrientation(LinearLayout.HORIZONTAL);
							textView = new TextView(DbscfkActivity.this);
							textView.setTextSize(16);
							textView.setTextColor(Color.parseColor("#141414"));
							textView.setText("����λ��" + recordsBean.getHandlerGridName());
							top.addView(textView);
							textView = new TextView(DbscfkActivity.this);
							textView.setTextSize(16);
							textView.setTextColor(Color.parseColor("#141414"));
							textView.setPadding(60, 0, 0, 0);
							if (recordsBean.getStatus().equals("1")) {
								textView.setText("����״̬�����д���");
							} else if (recordsBean.getStatus().equals("2")) {
								textView.setText("����״̬���ϱ�");
							} else if (recordsBean.getStatus().equals("3")) {
								textView.setText("����״̬��ת��");
							} else if (recordsBean.getStatus().equals("4")) {
								textView.setText("����״̬���˻�");
							} else if (recordsBean.getStatus().equals("5")) {
								textView.setText("����״̬������");
							} else if (recordsBean.getStatus().equals("6")) {
								textView.setText("����״̬�����");
							}
							if (currenStatus.equals("5")) {
								if (!haveLiaisonRole.equals("1")) {// ����Ա
									rb_shangbao.setVisibility(View.VISIBLE);
									rb_self_solve.setVisibility(View.VISIBLE);
									rb_baocun.setVisibility(View.VISIBLE);
									rb_huitui.setVisibility(View.GONE);
									rb_foujue.setVisibility(View.GONE);
									rb_fuhe.setVisibility(View.GONE);
									rb_zhuanban.setVisibility(View.GONE);
									rb_shangbao.setText("���ͨ��");
									rb_self_solve.setText("��˲�ͨ��");
								}
							}
							String judgeTura1=recordsBean.getJudgeTura();
							if(!TextUtils.isEmpty(judgeTura1)&&judgeTura1.equals("5")){
								if (!haveLiaisonRole.equals("1")) {// ����Ա
									rb_shangbao.setVisibility(View.VISIBLE);
									rb_self_solve.setVisibility(View.VISIBLE);
									rb_baocun.setVisibility(View.VISIBLE);
									rb_huitui.setVisibility(View.GONE);
									rb_foujue.setVisibility(View.GONE);
									rb_fuhe.setVisibility(View.GONE);
									rb_zhuanban.setVisibility(View.GONE);
									rb_shangbao.setText("���ͨ��");
									rb_self_solve.setText("��˲�ͨ��");
								}
							}
							
							// top.addView(textView);//����״̬��ʱ����
							ll_middle.addView(top);

							LinearLayout bottom = new LinearLayout(DbscfkActivity.this);
							bottom.setOrientation(LinearLayout.HORIZONTAL);
							textView = new TextView(DbscfkActivity.this);
							textView.setTextSize(16);
							textView.setTextColor(Color.parseColor("#141414"));
							textView.setText("���������" + xcxq.getContent().getRecords().get(i).getOpinion());
							bottom.addView(textView);
							if (!TextUtils.isEmpty(xcxq.getContent().getRecords().get(i).getOpinion())) {
								ll_middle.addView(bottom);
							}
							FlowLayout ll_temp = new FlowLayout(DbscfkActivity.this);
							final ArrayList<String> imgPaths = new ArrayList<String>();// ͼƬ·������
							ll_temp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT));
							// ll_temp.setOrientation(LinearLayout.HORIZONTAL);
							// ll_temp.setGravity(Gravity.CENTER);
							for (int j = 0; j < xcxq.getContent().getRecords().get(i).getImgs().size(); j++) {
								final String path = PathManager.IMG_URL_JINAN
										+ xcxq.getContent().getRecords().get(i).getImgs().get(j).getImgPath();
								if(TypeUtils.isIMG(path)){
									imageView = new ImageView(DbscfkActivity.this);
									imageView.setLayoutParams(params_img);
									imageView.setScaleType(ScaleType.CENTER_CROP);
									imgPaths.add(path);
									Glide.with(DbscfkActivity.this).load(path).placeholder(R.drawable.wd_xcjl_jzz)
											.error(R.drawable.wd_xcjl_jzsb).into(imageView);
									ll_temp.addView(imageView);
								}
								

								// imageView.setOnClickListener(new
								// OnClickListener() {
								//
								// @Override
								// public void onClick(View arg0) {
								// // TODO Auto-generated method stub
								// Intent intent = new
								// Intent(DbscfkActivity.this,
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
									Intent intent = new Intent(DbscfkActivity.this, PicViewerActivity.class);
									intent.putStringArrayListExtra("imgPaths", imgPaths);
									startActivity(intent);
								}
							});
							ll_middle.addView(ll_temp);

							ImageView dividerImageView = new ImageView(DbscfkActivity.this);
							dividerImageView
									.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
							dividerImageView.setBackgroundColor(Color.BLACK);
							ll_middle.addView(dividerImageView);
						}

					} else {
						Toast.makeText(DbscfkActivity.this, "��������ʧ��", 200).show();
						finish();
					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(DbscfkActivity.this, "���ݽ���ʧ��2", 200).show();
					finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_GETIMAGE_BYCAMERA:
			if (BimpHelper.drr.size() < 12 && resultCode == -1) {
				BimpHelper.drr.add(cameras.protraitPath);
			}
			// cameras.startActionCrop(cameras.photoUri);// ���պ�ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYCROP:
			cameras.startActionCrop(data.getData());// ѡͼ��ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:
			if (BimpHelper.drr.size() < 12 && resultCode == -1) {
				BimpHelper.drr.add(cameras.protraitPath);
			}
			break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		adapter.update();
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	private YutuLoading yutuLoading;
	private Handler uphandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(DbscfkActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("�����ϴ����ݣ����Ժ�", "");
				yutuLoading.showDialog();
				break;
			case 102:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				Toast.makeText(DbscfkActivity.this, "ִ�гɹ�", 1000).show();
				finish();
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(DbscfkActivity.this, "ִ��ʧ��", 1000).show();
				break;
			default:
				break;
			}
		}
	};
	String value1;
	String value2;

	@Override
	public void onClick(View view) {
		if (DisplayUitl.isFastDoubleClick()) {
			return;
		}

		switch (view.getId()) {
		case R.id.submit_bu:
			value1 = "";
			value2 = edt_chuli_yijian.getText() + "";
			try {
				upload(uphandler, BimpHelper.drr, value1, value2, getIntent().getStringExtra("id"));
				// upload(uphandler, BimpHelper.drr, "1111", "aaaa",
				// getIntent().getStringExtra("id"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private void upload(final Handler uphandler, List<String> list, String processingtime, String resultcode, String id)
			throws Exception {
		String url = PathManager.SHANGCHUANFANKUI_URL_JINAN;// �ύ

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);

		// ���ʱ�����ϴ�ͼƬ
		if (rb_self_solve.isChecked()) {
			if (BimpHelper.bmp.size() < 1) {
				Toast.makeText(DbscfkActivity.this, "��������ͼƬ����Ϊ��", 0).show();
				return;
			}
		}

		String status = "1";
		String handlerGrid = "";
		if (rb_self_solve.isChecked()) {
			if (rb_self_solve.getText().equals("��˲�ͨ��")) {
				status = "51";
			} else {
				status = "1";
			}

		} else if (rb_shangbao.isChecked()) {
			if (rb_shangbao.getText().equals("���ͨ��")) {
				status = "5";
			} else {
				status = "2";
			}
		} else if (rb_huitui.isChecked()) {
			status = "4";
		} else if (rb_zhuanban.isChecked()) {
			status = "3";
			handlerGrid = code.get(sp_zbbm.getSelectedItemPosition());
		} else if (rb_fuhe.isChecked()) {
			status = "5";
		} else if (rb_foujue.isChecked()) {
			status = "9";
		} else if (rb_baocun.isChecked()) {
			status = "7";
		}
		if ("1".equals(haveFreeRole)) {
			handlerGrid = hg;
		}
		uphandler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("recordId", id).addFormDataPart("status", status)
				.addFormDataPart("opinion", edt_chuli_yijian.getText() + "").addFormDataPart("handlerGrid", handlerGrid)
				// ת��ʱѡ���ֵܵ�λcode
				// .addFormDataPart("Latitude",
				// String.valueOf(xcxq.getContent().getY()))//����
				// .addFormDataPart("Longitude",
				// String.valueOf(xcxq.getContent().getX()))//γ��
				// .addFormDataPart("taskId",
				// xcxq.getContent().getTaskId().toString())//������
				// .addFormDataPart("patrolObjectId", "")//��ܶ������
				// .addFormDataPart("isHaveProblem", "")//�Ƿ������� ��1��0
				// .addFormDataPart("problemDesc", "")//��������
				// .addFormDataPart("status", status)//����״̬
				// .addFormDataPart("opinion", edt_chuli_yijian.getText() +
				// "")//�������
				// .addFormDataPart("handlerGrid",
				// DisplayUitl.readPreferences(DbscfkActivity.this,
				// LAST_USER_SP_NAME, "organization_code"))//�������
				// .addFormDataPart("createId",
				// DisplayUitl.readPreferences(DbscfkActivity.this,
				// LAST_USER_SP_NAME, "user_id"))//������
				// .addFormDataPart("patrolRecordId", str)//����ʱ��
				// .addFormDataPart("problemCode", "")//�������
				.addFormDataPart("sessionId",
						DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "sessionId"))// sessionId
				.addFormDataPart("createTime", str);// ����ʱ��

		for (String path : list) {
			File file = new File(path);
			builder.addFormDataPart("file", file.getName(),
					RequestBody.create(MediaType.parse("application/octet-stream"), file));
		}

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = builder.build();
		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();

				boolean ok = response.isSuccessful();
				if (ok) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(bodyStr);
						String status = jsonObject.getString("status");
						if (status.contains("200")) {
							uphandler.sendEmptyMessage(102);
						} else {
							uphandler.sendEmptyMessage(103);

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					finish();
				} else {
					Log.i("qqq", "ִ��ʧ��");
					uphandler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				Log.i("qqq", "ִ��ʧ��");
				uphandler.sendEmptyMessage(103);
			}

		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BimpHelper.drr.clear();
		BimpHelper.bmp.clear();
		BimpHelper.max = 0;
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

	public void selectShangbaoBuMenData() {
		Builder builder = new Builder(DbscfkActivity.this);
		builder.setTitle("��ѡ���ϱ�����:");
		LayoutInflater inflater = LayoutInflater.from(DbscfkActivity.this);
		View v = inflater.inflate(R.layout.set_txjcr, null);
		final ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.expList_txjcr);
		builder.setView(v);
		myAdapter = new MyExpandableListViewAdapter(childrenListMap, parentList, DbscfkActivity.this);
		listView.setAdapter(myAdapter);// �������ݣ���������
		final AlertDialog alertDialog = builder.create();
		alertDialog.setCancelable(true);
		alertDialog.show();
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {
				// TODO Auto-generated method stub
				hg = shangBaoBuMen.getRows().get(groupPosition).getChilds().get(childPosition).getCode();
				sp_choose_shangbao.setText(childrenListMap.get(parentList.get(groupPosition)).get(childPosition));
				alertDialog.dismiss();
				return true;
			}
		});
	}

	String hg = "";
	ShangBaoBuMen shangBaoBuMen;
	MyExpandableListViewAdapter myAdapter;
	List<String> parentList = new ArrayList<String>();// ���˵�
	Map<String, List<String>> childrenListMap = new HashMap<String, List<String>>();// �Ӳ˵�

	/**
	 * ��ȡ����Ѳ��Ա�ϱ���λ����
	 * http://192.168.1.151:8080/JiNanhuanbaoms/grid/level/2?sessionId=
	 * 8a8f87ac5ac115e3015ac712158b0099
	 */
	private void getShangBaoBuMen() {
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sessionId = DisplayUitl.readPreferences(DbscfkActivity.this, LAST_USER_SP_NAME, "sessionId");
		Log.i("aaa", sessionId);
		String url = PathManager.BASE_URL_JINNAN + "/JiNanhuanbaoms/grid/level/2" + "?sessionId=" + sessionId;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(DbscfkActivity.this, "��������ʧ��", 200).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String resoult = String.valueOf(arg0.result);
				Gson gson = new Gson();
				shangBaoBuMen = gson.fromJson(resoult, ShangBaoBuMen.class);
				List<ShangBaoBuMen.RowsBean> list = new ArrayList<ShangBaoBuMen.RowsBean>();
				list = shangBaoBuMen.getRows();

				for (ShangBaoBuMen.RowsBean bean : list) {
					parentList.add(bean.getName());
					List<String> children = new ArrayList<String>();
					List<String> childrenCode = new ArrayList<String>();
					for (ShangBaoBuMen.RowsBean.ChildsBean b : bean.getChilds()) {
						children.add(b.getName());
						childrenCode.add(b.getCode());
					}
					childrenListMap.put(bean.getName(), children);
				}
			}
		});
	}
}
