package cn.com.mapuni.meshing.multimeidia;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import cn.com.mapuni.meshing.base.Global;
import cn.com.mapuni.meshing.base.attachment.T_Attachment;
import cn.com.mapuni.meshing.base.dataprovider.SqliteUtil;
import cn.com.mapuni.meshing.base.util.ExceptionManager;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.R;

/**
 * FileName: SiteRecorder.java Description:�ֳ�ȡ֤ : ���ա�����¼��
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 *            Create at: 2012-12-4 ����11:07:33
 */
public class SiteRecorder extends Activity {
	private MediaRecorder mr;
	private ImageButton stop_record;
	boolean sdCardExit;
	boolean recordIng = false;
	private File myRecAudioFile;
	private TextView second_tv, mini_tv;
	private Timer timer;
	private TimerTask tt;
	private int second = 0;
	private int mini = 0;
	private String FName = null;
	private String currentTaskID = null;
	private String qyid = null;
	private String fk_id;
	private Boolean isClickable = true;

	final Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {// handler��ʱ��
			second++;
			if (second > 0 && (second % 60) == 0) {
				mini++;
				mini_tv.setText("0" + String.valueOf(mini));
			}
			if (((second % 60)) < 10)
				second_tv.setText(String.valueOf("0" + (second % 60)));
			else
				second_tv.setText(String.valueOf((second % 60)));
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record_evidence);
		stop_record = (ImageButton) findViewById(R.id.ibtnStopRecord);

		currentTaskID = getIntent().getStringExtra("currentTaskID");
		qyid = getIntent().getStringExtra("qyid");
		fk_id = currentTaskID + "_" + qyid;
		second_tv = (TextView) findViewById(R.id.sec_TextView);
		mini_tv = (TextView) findViewById(R.id.mini_TextView);

		tt = new TimerTask() {// ��ʱ��handler���ݲ���
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				h.sendMessage(msg);
			}
		};

		timer = new Timer(true);// ��ʱ����ʼ��ʱ
		timer.schedule(tt, 1000, 1000);

		sdCardExit = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		RecordAmr();// ��ʼ¼��
		stop_record.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (isClickable) {
//					isClickable = false;
//					if (mr != null && recordIng) {
//						mr.stop();
//						mr.release();
//					}
//					Intent it = new Intent(SiteRecorder.this, SiteEvidenceActivity.class);
//					it.putExtra("filename", FName);
//					setResult(SiteEvidenceActivity.RECORD_AUDIONS, it);
//					SiteRecorder.this.finish();
//				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isClickable) {
				isClickable = false;
				if (mr != null && recordIng) {
					mr.stop();
					mr.release();
				}
//				Intent it = new Intent(SiteRecorder.this, SiteEvidenceActivity.class);
//				it.putExtra("filename", FName);
//				setResult(SiteEvidenceActivity.RECORD_AUDIONS, it);
//				SiteRecorder.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Description:¼��
	 * 
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 ����11:08:02
	 */
	private void RecordAmr() {
		try {

			if (!sdCardExit) {
				Toast.makeText(SiteRecorder.this, "�����SD Card", Toast.LENGTH_LONG).show();
				return;
			}
			Date now = new Date();
			DateFormat dateFormat = new SimpleDateFormat("MMddhhmmss");
			String fileName = "mapuni" + dateFormat.format(now);
			SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = formatdate.format(new Date());
			String guid = UUID.randomUUID().toString();
			String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) " + "values ('" + guid + "','" + fileName + "','" + guid + ".amr','.amr','"
					+ T_Attachment.RWZX + "','" + fk_id + "')";
			SqliteUtil.getInstance().execute(sql);
			
			
			//24Сʱ�Զ��ύ
			// Timertask  �жϱ����Ƿ���ڸ�������   ������  ����   ���� ������
			//�����������ж��Ƿ���ڸ�����
			String sql_rw = "select * from Timertask where RwbhID ='"+ currentTaskID +"'";
			ArrayList<HashMap<String, Object>> arraylist = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql_rw);
			if(arraylist.size()<=0){
				String sqlT = "insert into Timertask (Guid,RwbhID,UpdateTime) "
						+ "values ('"
						+ qyid
						+ "','"
						+ currentTaskID
						+ "','"
						+ time + "')";
				SqliteUtil.getInstance().execute(sqlT);
			}
			
			//T_YDZF_QZZL
//			String sqlQ = "insert into T_YDZF_QZZL (Guid,PSSJ,RWBH,QYBH) " + "values ('" + guid + "','" + time+ "','" + currentTaskID + "','" + qyid + "')";
//			SqliteUtil.getInstance().execute(sqlQ); 
			/* ����¼���� */
			String dataPath = Global.SDCARD_RASK_DATA_PATH + "Attach/RWZX/";
			File file = new File(dataPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			myRecAudioFile = new File(dataPath + guid + ".amr");
			FName = fileName + ".amr";
			mr = new MediaRecorder();
			/* �趨¼����ԴΪ��˷� */
			mr.setAudioSource(MediaRecorder.AudioSource.MIC);
			mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mr.setOutputFile(myRecAudioFile.getAbsolutePath());
			mr.prepare();

			mr.start();
			recordIng = true;

		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "SiteRecorder");
			e.printStackTrace();
		}
	}
}
