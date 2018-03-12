package cn.com.mapuni.meshing.activity.db_activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.activity.xc_activity.XcrwActivity;
import cn.com.mapuni.meshing.model.XiaFaTaskModel;

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
import com.mapuni.android.base.util.DataPickDialogUtil;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;

public class YanShiShenQingActivity extends BaseActivity implements
		OnClickListener {
	private View mainView;
	XiaFaTaskModel xiaFaTaskModel;
	private TextView tvShenqingren, tvShenqingdanwei, tvChuliren,
			tvChulidanwei, tvOrginTime, tvNewTime;
	private EditText tvDescripsion;
	private Button btn,btnUpload;
	private String processGrid,processGridName,processUser,processUserName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("延时申请");
		initView();
		getDetail();
	}

	private void initView() {
		xiaFaTaskModel = (XiaFaTaskModel) getIntent().getSerializableExtra(
				"xiaFaTaskModel");
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// 查询条件
		mainView = inflater.inflate(R.layout.activity_yanshishenqingl, null);
		middleLayout.addView(mainView);
		tvShenqingren = (TextView) mainView.findViewById(R.id.tv_shenqingren);
		tvShenqingdanwei = (TextView) mainView
				.findViewById(R.id.tv_shenqingdanwei);
		tvChuliren = (TextView) mainView.findViewById(R.id.tv_chuliren);
		tvChulidanwei = (TextView) mainView.findViewById(R.id.tv_chulidanwei);
		tvOrginTime = (TextView) mainView.findViewById(R.id.tv_orgintime);
		tvNewTime = (TextView) mainView.findViewById(R.id.tv_shenqingtime);
		tvDescripsion = (EditText) mainView.findViewById(R.id.tv_descripaion);
		tvShenqingren.setText(DisplayUitl.readPreferences(this, "lastuser",
				"user_Name"));
		tvShenqingdanwei.setText(DisplayUitl.readPreferences(this, "lastuser",
				"organization_name"));
		tvOrginTime.setText(xiaFaTaskModel.getEndTime());
		btn = (Button) mainView.findViewById(R.id.btn);
		btnUpload= (Button) mainView.findViewById(R.id.bt_shenqing);
		btn.setOnClickListener(this);
		btnUpload.setOnClickListener(this);
	}

	/**
	 * 获取延时申请详情的接口。接口址址：/task/send?id=0CDB4489-2BB2-4B94-89B3-88B46FFE5F8A
	 */
	private void getDetail() {
		final YutuLoading yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("获取详情信息，请稍候", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 5);
		utils.configTimeout(5 * 1000);//
		utils.configSoTimeout(5 * 1000);//
		String sessionId = DisplayUitl.readPreferences(this, "lastuser",
				"sessionId");
		String taskId = xiaFaTaskModel.getTaskId();
		String url = PathManager.YANSHIDETAIL_URL_JINAN + "?sessionId="
				+ sessionId + "&id=" + taskId;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(YanShiShenQingActivity.this, "获取详情失败", 0).show();
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
						tvChuliren.setText(jsonObject.getJSONObject("content")
								.getString("createName"));
						tvChulidanwei.setText(jsonObject.getJSONObject(
								"content").getString("createGridName"));
						processGrid=jsonObject.getJSONObject("content").getString("createGrid");
						processGridName=jsonObject.getJSONObject("content").getString("createGridName");
						processUser=jsonObject.getJSONObject("content").getString("createGrid");
						processUserName=jsonObject.getJSONObject("content").getString("createName");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn:
			new DataPickDialogUtil(this).TimePicKDialog(tvNewTime);
			break;
		case R.id.bt_shenqing:
			shenqing();
			break;
		}
	}

	public void shenqing() {
		String condition=xiaFaTaskModel.getId();
		String createGrid=DisplayUitl.readPreferences(this, "lastuser","organization_code");
		String createGridName=DisplayUitl.readPreferences(this, "lastuser","organization_name");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String createTime=df.format(new Date());
		String createUser=DisplayUitl.readPreferences(this, "lastuser","user");
		String createUserName=DisplayUitl.readPreferences(this, "lastuser","user_Name");
		String fieldValue=tvNewTime.getText().toString();
		if (fieldValue=="") {
			Toast.makeText(YanShiShenQingActivity.this,"请选择申请完成时间后，重新申请",Toast.LENGTH_SHORT).show();
			return ;
		}
		
		String orginValue=xiaFaTaskModel.getEndTime();
		String relatedId=xiaFaTaskModel.getTaskId();
		String title=tvDescripsion.getText().toString();

			upload( condition,  createGrid,
					 createGridName,  createTime,  createUser,
					 createUserName,  fieldValue,  orginValue,
					 processGrid,  processGridName,  processUser,
					 processUserName,  relatedId,  title);

	}

	public void upload(String condition, String createGrid,
			String createGridName, String createTime, String createUser,
			String createUserName, String fieldValue, String orginValue,
			String processGrid, String processGridName, String processUser,
			String processUserName, String relatedId, String title){
		final YutuLoading yutuLoading = new YutuLoading(this);
		yutuLoading.setCancelable(true);
		yutuLoading.setLoadMsg("提交申请中，请稍候", "");
		yutuLoading.showDialog();
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(1000 * 10);
		utils.configTimeout(10 * 1000);//
		utils.configSoTimeout(10 * 1000);//
		String sessionId = DisplayUitl.readPreferences(this, "lastuser",
				"sessionId");
		RequestParams params = new RequestParams();
		params.addBodyParameter("sessionId",sessionId);
		params.addBodyParameter("condition",condition);
		params.addBodyParameter("createGrid",createGrid);
		params.addBodyParameter("createGridName",createGridName);
		params.addBodyParameter("createTime",createTime);
		params.addBodyParameter("createUser",createUser);
		params.addBodyParameter("createUserName",createUserName);
		params.addBodyParameter("fieldValue",fieldValue);
		params.addBodyParameter("orginValue",orginValue);
		params.addBodyParameter("processGrid",processGrid);
		params.addBodyParameter("processGridName",processGridName);
		params.addBodyParameter("processUser",processUser);
		params.addBodyParameter("processUserName",processUserName);
		params.addBodyParameter("relatedId",relatedId);
		params.addBodyParameter("title",title);		
		utils.send(HttpMethod.POST, PathManager.YANSHIUPLOAD_URL_JINAN,params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(YanShiShenQingActivity.this, "提交申请失败", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}

				String resoult = String.valueOf(arg0.result);
				try {
					JSONObject jsonObject = new JSONObject(resoult);
					String status = jsonObject.getString("status");
					if (status.contains("200")) {
						Toast.makeText(YanShiShenQingActivity.this, "提交申请成功", 0).show();						
					}else{
						Toast.makeText(YanShiShenQingActivity.this, "提交申请失败", 0).show();
					}
					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(YanShiShenQingActivity.this, "提交申请失败", 0).show();
				}

			}
		});
	}

}
