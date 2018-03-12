package cn.com.mapuni.meshing.activity.wghcx_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.mapuni.meshing.base.BaseActivity;

import cn.com.mapuni.meshingtotal.R;

public class WghcxMainActivity extends BaseActivity implements OnClickListener{
	
	TextView xxcx_xccx,xxcx_ajczjg,xxcx_qykh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		setBACK_ISSHOW(false);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"Íø¸ñ»¯²éÑ¯");
		initView();
	}
	
	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wghcxmainactivity_layout, null);
		middleLayout.addView(mainView);
		
		xxcx_xccx = (TextView) mainView.findViewById(R.id.xxcx_xccx);
		xxcx_xccx.setOnClickListener(this);
		
		xxcx_ajczjg = (TextView) mainView.findViewById(R.id.xxcx_ajczjg);
		xxcx_ajczjg.setOnClickListener(this);
		
		xxcx_qykh = (TextView) mainView.findViewById(R.id.xxcx_qykh);
		xxcx_qykh.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.xxcx_xccx:
			intent = new Intent(WghcxMainActivity.this,XccxMainActivity.class);
			startActivity(intent);
			break;
		case R.id.xxcx_ajczjg:
			intent = new Intent(WghcxMainActivity.this,RwxxSlideActivity.class);
			startActivity(intent);
			break;
		case R.id.xxcx_qykh:
			intent = new Intent(WghcxMainActivity.this,QykhActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
