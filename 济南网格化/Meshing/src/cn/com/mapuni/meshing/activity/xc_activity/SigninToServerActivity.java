package cn.com.mapuni.meshing.activity.xc_activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.meshing.R;
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
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.overlay.MarkerOverlay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.mapuni.meshing.util.ImageUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SigninToServerActivity extends BaseActivity implements OnClickListener {
	/** ����¼���û���ϢSP name */
	private final String LAST_USER_SP_NAME = "lastuser";
	View mainView;
	/** ���Ԥ���б� */
	private PhotoGridView myGridView; // ��Ƭ�ؼ�
	private GridAdapter adapter; // ��Ƭ������
	private PhotoGridView myGridView1; // ��Ƭ�ؼ�
	private GridAdapter adapter1; // ��Ƭ������
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** ������� */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** ����ü� */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	private GeoPoint point; // ��λ��Ϣ
	Cameras cameras; // ���������
	int flag = 0;// �������������Դ
	private String CameraAddress;
	private Button bt_signin;
	private String name;// ����Ա����
	private String gridName;// ��������
	private String gridCode;// �������
	private String location;// ��ַ
	private String latitude;// γ��
	private String longitude;// ����
	private String entName;// ��������
	private String phone;// ��ϵ�绰
	private String createId;// ǩ����ԱID user_Id
	private String createTime;// ǩ��ʱ��
	private TextView tv_time;// ʱ��
	private TextView tv_location;// �ص�
	private TextView tv_address;//�����ҵ
	private TextView tv_type;  //��ҵ����
	private String entNameAddress; //�����ҵ��ַ

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetBaseStyle("ǩ��");
		cameras = new Cameras(SigninToServerActivity.this);
		initView();
		initData();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.activity_signin_to_server, null);
		middleLayout.addView(mainView);
		tv_time = (TextView) mainView.findViewById(R.id.tv_time);
		tv_address=(TextView) mainView.findViewById(R.id.tv_address);
		tv_location = (TextView) mainView.findViewById(R.id.tv_location);
		bt_signin = (Button) mainView.findViewById(R.id.bt_signin);
		tv_type=(TextView) mainView.findViewById(R.id.tv_type);
		bt_signin.setOnClickListener(this);
		initPhotoGrideView();

	}

	private void initData() {
		
		createTime = getIntent().getStringExtra("createTime");
		latitude = getIntent().getStringExtra("latitude");
		longitude = getIntent().getStringExtra("longitude");
		location = getIntent().getStringExtra("location");
		entName = getIntent().getStringExtra("entName");
		type = getIntent().getStringExtra("industryType");
		entNameAddress=getIntent().getStringExtra("address");
	    superviseType=getIntent().getStringExtra("superviseType");
		name = DisplayUitl.readPreferences(SigninToServerActivity.this, LAST_USER_SP_NAME, "user_Name");
		gridName = DisplayUitl.readPreferences(SigninToServerActivity.this, LAST_USER_SP_NAME, "organization_name");
		gridCode = DisplayUitl.readPreferences(SigninToServerActivity.this, LAST_USER_SP_NAME, "organization_code");
		phone = DisplayUitl.readPreferences(SigninToServerActivity.this, LAST_USER_SP_NAME, "phone");
		createId = DisplayUitl.readPreferences(SigninToServerActivity.this, LAST_USER_SP_NAME, "user_id");
		tv_time.setText(createTime);
		tv_location.setText(location);
		tv_address.setText(entName);
		tv_type.setText(type);
	}

	private void initPhotoGrideView() {
		adapter = new GridAdapter(this, 1);
		// adapter.update();
		myGridView = (PhotoGridView) mainView.findViewById(R.id.noScrollgridview);
		myGridView.setAdapter(adapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (DisplayUitl.isFastDoubleClick()) {
					return;
				}
				if (BimpHelper.bmp.size() > Cameras.num) {
					Toast.makeText(SigninToServerActivity.this, "���ѡ��" + Cameras.num + "��ͼƬ", 400).show();
					return;
				}
				flag = 1;
				BimpHelper.currentFlag = 1;
				if (arg2 == BimpHelper.drr_temp1.size()) {
					new PhotoPopupWindows(SigninToServerActivity.this, myGridView, cameras,1);
				} else {
					Intent intent = new Intent(SigninToServerActivity.this, CheckPicActivity.class);
					intent.putExtra("ID", BimpHelper.drr_temp1.get(arg2));
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_GETIMAGE_BYCAMERA:
			if (BimpHelper.drr.size() < 24 && resultCode == -1) {
				try {
					// Bitmap bitmap =
					// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
					// cameras.cropUri);
					Bitmap bitmap = ImageUtil.getThumbnail(SigninToServerActivity.this, cameras.cropUri, 500);
					Bitmap drawTextToCenter = ImageUtil.drawTextToLeftBottom2(SigninToServerActivity.this, bitmap,
							ImageUtil.getNowTime(), 8, Color.parseColor("#ffffff"), 0, 0);
					File file = new File(cameras.protraitPath);
					FileOutputStream fos = new FileOutputStream(file);
					drawTextToCenter.compress(CompressFormat.JPEG, 50, fos);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				BimpHelper.drr.add(cameras.protraitPath);
				if (flag == 1) {
					BimpHelper.drr_temp1.add(BimpHelper.drr.size() - 1);
				} else {
					BimpHelper.drr_temp2.add(BimpHelper.drr.size() - 1);
				}
			}
			// cameras.startActionCrop(cameras.photoUri, cameras);// ���պ�ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYCROP:
			cameras.startActionCrop(data.getData(), cameras);// ѡͼ��ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:
			if (BimpHelper.drr.size() < 24 && resultCode == -1) {
				BimpHelper.drr.add(cameras.protraitPath);
				if (flag == 1) {
					BimpHelper.drr_temp1.add(BimpHelper.drr.size() - 1);
				} else {
					BimpHelper.drr_temp2.add(BimpHelper.drr.size() - 1);
				}
			}
			break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (flag == 1) {
			adapter.update();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		adapter.notifyDataSetChanged();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BimpHelper.clear();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bt_signin:
			bt_signin.setEnabled(false);
			List<String> drr1 = new ArrayList<String>();
			for (int i = 0; i < BimpHelper.drr_temp1.size(); i++) {
				drr1.add(BimpHelper.drr.get(BimpHelper.drr_temp1.get(i)));
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz");
			Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
			String createTime = formatter.format(curDate);
			// String patrolRecordId = str;
			/*Intent intent=new Intent(SigninToServerActivity.this,ZuJiActivity.class);
			startActivity(intent);*/
			try {
				if (BimpHelper.drr.size()<1) {
					Toast.makeText(SigninToServerActivity.this,"�������ϴ�һ��ͼƬ",Toast.LENGTH_SHORT).show();
					bt_signin.setEnabled(true);
					return ;
				}else {
					signin(uphandler, name, gridName, gridCode, location, latitude, longitude, entName,
							phone, createId, createTime, drr1);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				bt_signin.setEnabled(true);
			}
			break;

		default:
			break;
		}

	}

	public void signin(final Handler uphandler, String name, String gridName, String gridCode, String address,
		String Latitude, String Longitude, String entName, String phone, String createId, String createTime,
		List<String> list1) throws Exception {
		MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/octet-stream");
		/*OkHttpClient client = new OkHttpClient();*/
		//�˴��޸�    ���ó�ʱ����ʱ��
		OkHttpClient client=new OkHttpClient.Builder()
				    .connectTimeout(30L,TimeUnit.SECONDS)
					.readTimeout(30L,TimeUnit.SECONDS)
					.build();
		uphandler.sendEmptyMessage(101);
		double lat = 0, lon = 0;
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("name", name)// ����Ա����
				.addFormDataPart("gridName", gridName)// ��������
				.addFormDataPart("gridCode", gridCode)// �������
				.addFormDataPart("address", address)// ��ַ
				.addFormDataPart("entNameAddress", entNameAddress)// ��ַ
				.addFormDataPart("Latitude", Latitude)// γ��
				.addFormDataPart("Longitude", Longitude)// ����
				.addFormDataPart("entName", entName)// ��������
				.addFormDataPart("phone", phone)// ��ϵ�绰
				.addFormDataPart("createId", createId)// ǩ����ԱID
				.addFormDataPart("superviseType",superviseType)//��ܶ�������
				.addFormDataPart("sessionId",
						DisplayUitl.readPreferences(SigninToServerActivity.this, LAST_USER_SP_NAME, "sessionId"))// ����
				.addFormDataPart("createTime", createTime);// ʱ��
		for (String path : list1) {
			File file = new File(path);
			builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		}

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(PathManager.QIANDAO_SIGNIN_URL).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				// Log.i(Tag,"bodyStr==="+bodyStr);
				if (ok) {
					// Log.i(Tag,":"+bodyStr);

					try {
						JSONObject sywrwJsonObject = new JSONObject(bodyStr);
						String status = sywrwJsonObject.getString("status");
						if (status.contains("200")) {
							uphandler.sendEmptyMessage(102);
						} else {
							uphandler.sendEmptyMessage(103);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					// Log.i(Tag,"ִ��ʧ��1��"+bodyStr);
					uphandler.sendEmptyMessage(104);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				// Log.i(Tag,"ִ��ʧ��2��"+e.toString());
				uphandler.sendEmptyMessage(105);
			}
		});
		bt_signin.setEnabled(true);
	}

	private YutuLoading yutuLoading;
	private Handler uphandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(SigninToServerActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("�����ϴ�ǩ�����ݣ����Ժ�", "");
				yutuLoading.showDialog();
				break;
			case 102:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(SigninToServerActivity.this, "ǩ���ɹ�", 1000).show();
				DisplayUitl.writePreferences(SigninToServerActivity.this,"QD", "qd_key", getDate());
				finish();
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(SigninToServerActivity.this, "ǩ��ʧ�ܣ������ϴ���Ϣ�����ԣ�", 1000).show();
				break;
			case 104:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(SigninToServerActivity.this, "ǩ��ʧ�ܣ�����ϵ��̨����֧�֣���", 1000).show();
				break;
			case 105:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(SigninToServerActivity.this, "�����쳣����ˢ����������ԣ�����", 1000).show();
				break;
			default:
				break;
			}
		}
	};
	private String type;
	private String superviseType;
	private String getDate() {
		DateFormat format = new DateFormat();
		return format.format("yyyy��MM��dd��  HH��mm��ss��", new Date()).toString();
	}

}