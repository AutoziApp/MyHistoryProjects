package com.mapuni.android.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.R;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.business.RWXX;

/**
 * ��ʼ�����������Ϣ����������б�֮�����еĵ����඼�̳д���---����Ҫ������������RWXXҵ����
 * 
 * @author wanglg
 * 
 */
public abstract class BaseTaskdetailActivity extends BaseActivity {

	protected String RWBH;
	protected String RWID;
	protected HashMap<String, Object> RWDetail;
	protected Bundle RWBundle;
	protected RWXX rwxx;
	protected String qyidStr;
	protected String currentqyid;
	protected String stid;
	
	protected ArrayList<HashMap<String, Object>> rwxxAttachment;
	protected String path;

	protected RelativeLayout relativeLayout;
	protected LinearLayout middleLayout;
	protected LinearLayout bottomLayout;
	protected YutuLoading yutuLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		Intent intent = getIntent();
		RWBundle = intent.getExtras();
		currentqyid = intent.getStringExtra("qyid");
		stid = intent.getStringExtra("stid");
		rwxx = (RWXX) RWBundle.get("BusinessObj");
		if(rwxx == null){
			rwxx = new RWXX();
		}
		RWID = rwxx.getCurrentID();
		RWDetail = rwxx.getDetailed(RWID);
		
		if (RWDetail!=null && RWDetail.size()>0) {
			RWBH = RWDetail.get("rwbh").toString();
		}
		
		rwxxAttachment = rwxx.getRwxxAttachment();
		path = rwxx.getFilePath();
		qyidStr = rwxx.getTask_qyId(RWBH);
		relativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
		yutuLoading = new YutuLoading(this);
		addTitle();
		addBottom();
	}

	/** ��ӱ��� */
	public abstract void addTitle();

	/** ��ӵײ����� */
	public abstract void addBottom();

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (yutuLoading != null) {
			yutuLoading.dismissDialog();
		}
	}
}
