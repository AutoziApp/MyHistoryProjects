package cn.com.mapuni.meshing.activity.wd_activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.mapuni.meshing.activity.db_activity.DbscfkActivity;
import cn.com.mapuni.meshing.util.UploadFile;

import com.example.meshing.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.photograph.BimpHelper;
import com.mapuni.android.photograph.Cameras;
import com.mapuni.android.photograph.CheckPicActivity;
import com.mapuni.android.photograph.GridAdapter;
import com.mapuni.android.photograph.PhotoGridView;
import com.mapuni.android.photograph.PhotoPopupWindows;

public class RwxxChuliActivity extends BaseActivity implements OnClickListener {
	LinearLayout middleLayout;
	private Button submit_bu;
	EditText start_time, end_time, rwmc, mbwz_eit;
	String startDate = "", endDate = "";
	HashMap<String, Object> arr;
	View mainView;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cameras=new Cameras(RwxxChuliActivity.this);
		SetBaseStyle("������");
		initView();
		initPhotoGrideView();
	}

	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		// ��ѯ����
		mainView = inflater.inflate(R.layout.wd_rwxx_chuli_layout, null);
		middleLayout.addView(mainView);
		arr = (HashMap<String, Object>) getIntent().getSerializableExtra("arr");

		submit_bu = (Button) mainView.findViewById(R.id.submit_bu);
		rwmc = (EditText) mainView.findViewById(R.id.rwmc);
		rwmc.setText(arr.get("entname") + "");
		start_time = (EditText) mainView.findViewById(R.id.start_time);
		start_time.setOnClickListener(new TaskStartTextListener());
		start_time.setText(arr.get("xcwtsj") + "");
		end_time = (EditText) mainView.findViewById(R.id.end_time);
		mbwz_eit = (EditText) mainView.findViewById(R.id.mbwz_eit);
		// ��ǰʱ��
		// ��ȡϵͳ��ǰʱ��
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String time = formatter.format(curDate);
		end_time.setText(time);
		end_time.setOnClickListener(new TaskChuliTextListener());
		submit_bu.setOnClickListener(this);
	}

	/** ��ʼʱ�� **/
	private class TaskStartTextListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(RwxxChuliActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {

							int flag = 0;
							if (year > year1) { // ��������ڵ�ǰ�ֱ꣬�����ã������ж������
								start_time.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
								flag = 1;
							} else if (year == year1) {
								// ��������ڵ�ǰ�꣬�����¿�ʼ�ж���
								if (monthOfYear > month1) {
									// �����´��ڵ�ǰ�£�ֱ�����ã������ж������
									flag = 1;
									start_time.setText(year + "-"
											+ (monthOfYear + 1) + "-" + day1);
								} else if (monthOfYear == month1) {
									// �����µ��ڵ�ǰ�£������¿�ʼ�ж���
									if (dayOfMonth > day1) {
										// �����մ��ڵ�ǰ�գ�ֱ�����ã������ж������
										flag = 1;
										start_time.setText(year + "-"
												+ (monthOfYear + 1) + "-"
												+ dayOfMonth);
									}/*
									 * else { // ������С�� �����ǵ�ǰ�գ���ʾ�������� flag = 3;
									 * start_time.setText(""); Toast.makeText(
									 * RwxxChuliActivity.this,
									 * "����ʱ��Ӧ�ô��ڴ�������,����������", 2000) .show(); }
									 */
								} /*
								 * else { // ������С�ڵ�ǰ�£���ʾ�������� flag = 3;
								 * start_time.setText("");
								 * Toast.makeText(RwxxChuliActivity.this,
								 * "����ʱ��Ӧ�ô��ڴ�������,����������", 2000).show(); }
								 */
							} /*
							 * else { // ������С�ڵ�ǰ�꣬��ʾ�������� flag = 3;
							 * start_time.setText("");
							 * Toast.makeText(RwxxChuliActivity.this,
							 * "����ʱ��Ӧ�ô��ڴ�������,����������", 2000).show(); }
							 */
							if (flag != 3) {
								Calendar nowDate = Calendar.getInstance(), newDate = Calendar
										.getInstance();
								nowDate.setTime(new Date());// ����Ϊ��ǰϵͳʱ��
								newDate.set(year, monthOfYear, dayOfMonth);// ����Ϊ1990�꣨6����29��
								long timeNow = nowDate.getTimeInMillis();
								long timeNew = newDate.getTimeInMillis();
								long dd = (timeNew - timeNow)
										/ (1000 * 60 * 60 * 24);// ��Ϊ��
								startDate = year + "-" + (monthOfYear + 1)
										+ "-" + dayOfMonth;
								start_time.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
							} else {
								start_time.setText("");
							}
						}

					}, year1, month1, day1).show();

		}
	}

	/** ����ʱ�� **/
	private class TaskChuliTextListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Calendar c = Calendar.getInstance();
			final int year1 = c.get(Calendar.YEAR);
			final int month1 = c.get(Calendar.MONTH);
			final int day1 = c.get(Calendar.DAY_OF_MONTH);

			new DatePickerDialog(RwxxChuliActivity.this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {

							int flag = 0;
							if (year > year1) { // ��������ڵ�ǰ�ֱ꣬�����ã������ж������
								end_time.setText(year + "-" + (monthOfYear + 1)
										+ "-" + dayOfMonth);
								flag = 1;
							} else if (year == year1) {
								// ��������ڵ�ǰ�꣬�����¿�ʼ�ж���
								if (monthOfYear > month1) {
									// �����´��ڵ�ǰ�£�ֱ�����ã������ж������
									flag = 1;
									end_time.setText(year + "-"
											+ (monthOfYear + 1) + "-" + day1);
								} else if (monthOfYear == month1) {
									// �����µ��ڵ�ǰ�£������¿�ʼ�ж���
									if (dayOfMonth > day1) {
										// �����մ��ڵ�ǰ�գ�ֱ�����ã������ж������
										flag = 1;
										end_time.setText(year + "-"
												+ (monthOfYear + 1) + "-"
												+ dayOfMonth);
									}/*
									 * else { // ������С�� �����ǵ�ǰ�գ���ʾ�������� flag = 3;
									 * end_time.setText(""); Toast.makeText(
									 * RwxxChuliActivity.this,
									 * "����ʱ��Ӧ�ô��ڴ�������,����������", 2000) .show(); }
									 */
								} /*
								 * else { // ������С�ڵ�ǰ�£���ʾ�������� flag = 3;
								 * end_time.setText("");
								 * Toast.makeText(RwxxChuliActivity.this,
								 * "����ʱ��Ӧ�ô��ڴ�������,����������", 2000).show(); }
								 */
							} /*
							 * else { // ������С�ڵ�ǰ�꣬��ʾ�������� flag = 3;
							 * end_time.setText("");
							 * Toast.makeText(RwxxChuliActivity.this,
							 * "����ʱ��Ӧ�ô��ڴ�������,����������", 2000).show(); }
							 */
							if (flag != 3) {
								Calendar nowDate = Calendar.getInstance(), newDate = Calendar
										.getInstance();
								nowDate.setTime(new Date());// ����Ϊ��ǰϵͳʱ��
								newDate.set(year, monthOfYear, dayOfMonth);// ����Ϊ1990�꣨6����29��
								long timeNow = nowDate.getTimeInMillis();
								long timeNew = newDate.getTimeInMillis();
								long dd = (timeNew - timeNow)
										/ (1000 * 60 * 60 * 24);// ��Ϊ��
								endDate = year + "-" + (monthOfYear + 1) + "-"
										+ dayOfMonth;
								end_time.setText(year + "-" + (monthOfYear + 1)
										+ "-" + dayOfMonth);
							} else {
								end_time.setText("");
							}
						}

					}, year1, month1, day1).show();

		}
	}

	private void initPhotoGrideView() {
		adapter = new GridAdapter(this);
		adapter.update();
		myGridView = (PhotoGridView) mainView
				.findViewById(R.id.noScrollgridview);
		myGridView.setAdapter(adapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == BimpHelper.bmp.size()) {
					new PhotoPopupWindows(RwxxChuliActivity.this, myGridView,cameras);
				} else {
					Intent intent = new Intent(RwxxChuliActivity.this,
							CheckPicActivity.class);
					intent.putExtra("ID", arg2);
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
			cameras.startActionCrop(
					cameras.photoUri,cameras);// ���պ�ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYCROP:
			cameras.startActionCrop(data.getData(),cameras);// ѡͼ��ü�
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:
			if (BimpHelper.drr.size() < 8 && resultCode == -1) {
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
				yutuLoading = new YutuLoading(RwxxChuliActivity.this);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("�����ϴ����ݣ����Ժ�", "");
				yutuLoading.showDialog();
				break;
			case 102:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(RwxxChuliActivity.this, "ִ�гɹ�", 1000).show();
				finish();
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(RwxxChuliActivity.this, "ִ��ʧ��", 1000).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_bu:
			try {
				upload(uphandler, BimpHelper.drr,
						String.valueOf(end_time.getText()),
						String.valueOf(mbwz_eit.getText()),
						String.valueOf(arr.get("id")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	private void upload(final Handler uphandler, List<String> list,
			String processingtime, String resultcode, String id)
			throws Exception {
		String url=PathManager.DBUPLOAD_URL;
		//String url = "http://171.8.66.103:8473/JiNanhuanbaoms/task/shangChuanFanKiu.do";
		uphandler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("processingtime", processingtime)
				.addFormDataPart("resultCode", resultcode)
				.addFormDataPart("id", id);
		for (String path : list) {
			File file = new File(path);
			builder.addFormDataPart(
					"file",
					file.getName(),
					RequestBody.create(
							MediaType.parse("application/octet-stream"), file));
		}
		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = builder.build();
		Request request = new Request.Builder().url(url).post(requestBody)
				.build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();

				if (ok) {
					Log.i("qqq", "ִ�гɹ�");
					uphandler.sendEmptyMessage(102);
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
}
