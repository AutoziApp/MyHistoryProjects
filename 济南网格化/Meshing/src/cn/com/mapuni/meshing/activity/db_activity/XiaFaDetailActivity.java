package cn.com.mapuni.meshing.activity.db_activity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.mapuni.meshing.activity.xc_activity.SigninToServerActivity;
import cn.com.mapuni.meshing.activity.xc_activity.XcrwActivity;
import cn.com.mapuni.meshing.adapter.XiaFaTaskAdapter;
import cn.com.mapuni.meshing.model.XiaFaTaskModel;

import com.example.meshing.R;
import com.example.meshing.R.layout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class XiaFaDetailActivity extends BaseActivity implements
		OnClickListener {
	private View mainView;
	// �������� �������� ��Ҫ�̶� Ѳ�鷶Χ ��ʼʱ�� ����ʱ�� ��������
	private TextView tvTaskName, tvTaskType, tvImportance, tvPatrolScope,
			tvBeginTime, tvEndTime, tvRemark;
	private XiaFaTaskModel xiaFaTaskModel; // ����model
	private Button btQianShou, btFankui, btXunCha, btnYanShiShenQing;// ǩ�� ����
																		// Ѳ�鰴ť

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("�·���������");
		initView();
		initListner();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.activity_xiafadetail, null);
		middleLayout.addView(mainView);
		tvTaskName = (TextView) mainView.findViewById(R.id.tvTaskName);
		tvTaskType = (TextView) mainView.findViewById(R.id.tvTaskType);
		tvImportance = (TextView) mainView.findViewById(R.id.tvImportance);
		tvPatrolScope = (TextView) mainView.findViewById(R.id.tvPatrolScope);
		tvBeginTime = (TextView) mainView.findViewById(R.id.tvBeginTime);
		tvEndTime = (TextView) mainView.findViewById(R.id.tvEndTime);
		tvRemark = (TextView) mainView.findViewById(R.id.tvRemark);
		xiaFaTaskModel = (XiaFaTaskModel) getIntent().getSerializableExtra(
				"xiaFaTaskModel");
		tvTaskName.setText(xiaFaTaskModel.getTaskName());
		tvTaskType.setText(xiaFaTaskModel.getTaskTypeName());
		tvImportance.setText(xiaFaTaskModel.getImportanceName());
		tvPatrolScope.setText(xiaFaTaskModel.getPatrolScope());
		tvBeginTime.setText(xiaFaTaskModel.getBeginTime());
		tvEndTime.setText(xiaFaTaskModel.getEndTime());
		tvRemark.setText(xiaFaTaskModel.getRemark());
		btQianShou = (Button) mainView.findViewById(R.id.bt_qianshou);
		btFankui = (Button) mainView.findViewById(R.id.bt_fankui);
		btXunCha = (Button) mainView.findViewById(R.id.bt_xuncha);
		if (DisplayUitl.readPreferences(XiaFaDetailActivity.this,"lastuser","haveInspectorRole").equals("1")) {
			btXunCha.setVisibility(View.VISIBLE);
		}
		btnYanShiShenQing = (Button) mainView
				.findViewById(R.id.bt_yanshishenqing);
		if ("1".equals(xiaFaTaskModel.getApplyfor())) {
			btnYanShiShenQing.setVisibility(View.GONE);
		}
		if (!"0".equals(xiaFaTaskModel.getStatus())) {
			changeBtn();
		}
	}

	private void initListner() {
		// TODO Auto-generated method stub
		btQianShou.setOnClickListener(this);
		btFankui.setOnClickListener(this);
		btXunCha.setOnClickListener(this);
		btnYanShiShenQing.setOnClickListener(this);
	}

	private void changeBtn() {
		btQianShou.setVisibility(View.GONE);
		btFankui.setVisibility(View.VISIBLE);
	}

	private void popFanKuiDialog() {
		final EditText et = new EditText(this);
		new AlertDialog.Builder(this).setTitle("����������������������")
				.setIcon(android.R.drawable.ic_dialog_info).setView(et)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						String content = et.getText().toString();
						if (TextUtils.isEmpty(content)) {
							Toast.makeText(XiaFaDetailActivity.this,
									"�������ݲ���Ϊ��", 0).show();
							popFanKuiDialog();
							return;
						}
						fanKui(content);
						dialog.dismiss();
					}
				}).setNegativeButton("ȡ��", null).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_qianshou:
			qianShou();
			break;
		case R.id.bt_fankui:
			popFanKuiDialog();
			break;
		case R.id.bt_xuncha:
			Intent intent = new Intent(this, XiaFaXunChaActivity.class);
			intent.putExtra("xiaFaTaskModel", xiaFaTaskModel);
			startActivity(intent);
			break;
		case R.id.bt_yanshishenqing:
			Intent intent1 = new Intent(this, YanShiShenQingActivity.class);
			intent1.putExtra("xiaFaTaskModel", xiaFaTaskModel);
			startActivity(intent1);
			finish();
			break;
		}

	}

	// ǩ�� /task/process.do?sessionId='sessionId'&taskId='����ID'&status='0'
	/**
	 * ��ȡ��½�û��Ĵ����������ݡ��ӿ�ַַ��/task/list.do
	 */
	private void qianShou() {
		final YutuLoading yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("����ǩ�գ����Ժ�", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sessionId = DisplayUitl.readPreferences(this, "lastuser",
				"sessionId");
		String taskId = xiaFaTaskModel.getTaskId();
		String status = "0";
		String url = PathManager.QIANSHOU_URL_JINAN + "?sessionId=" + sessionId
				+ "&taskId=" + taskId + "&status=" + status;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(XiaFaDetailActivity.this, "ǩ��ʧ��", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				String resoult = String.valueOf(arg0.result);
				try {
					Gson gson = new Gson();
					JSONObject jsonObject = new JSONObject(resoult);
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						Toast.makeText(XiaFaDetailActivity.this, "ǩ�ճɹ�", 0)
								.show();
						changeBtn();
					}else {
						Toast.makeText(XiaFaDetailActivity.this, "ǩ��ʧ��", 0)
						.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * ��ȡ��½�û��Ĵ����������ݡ��ӿ�ַַ��/task/list.do
	 */
	private void fanKui(String content) {
		final YutuLoading yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("���ڷ������ݣ����Ժ�", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sessionId = DisplayUitl.readPreferences(this, "lastuser",
				"sessionId");
		String taskId = xiaFaTaskModel.getTaskId();
		String status = "1";
		String url = PathManager.QIANSHOU_URL_JINAN + "?sessionId=" + sessionId
				+ "&taskId=" + taskId + "&status=" + status + "&content="
				+ content+"&appStatus="+"app";
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(XiaFaDetailActivity.this, "����ʧ��", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				String resoult = String.valueOf(arg0.result);
				try {
					Gson gson = new Gson();
					JSONObject jsonObject = new JSONObject(resoult);
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						Toast.makeText(XiaFaDetailActivity.this, "�����ɹ�", 0)
								.show();
						finish();
					}else {
						Toast.makeText(XiaFaDetailActivity.this, "����ʧ��", 0)
						.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}
	
}
