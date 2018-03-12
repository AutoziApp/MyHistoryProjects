package cn.com.mapuni.meshingtotal.activity.wghtj;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.mapuni.chart.meshingtotal.activity.DepCheckInfoActivity;
import cn.com.mapuni.chart.meshingtotal.activity.TongjituActivity;
import cn.com.mapuni.meshing.base.BaseActivity;
import cn.com.mapuni.meshingtotal.R;
import cn.com.mapuni.meshingtotal.activity.common.CommonListActivity;

public class WghtjMainActivity extends BaseActivity implements OnClickListener{
	
	TextView xxcx_xccx,xxcx_ajczjg,xxcx_qykh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		setBACK_ISSHOW(false);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"网格化统计");
		initView();
	}
	
	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wghtjmainactivity_layout, null);
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
			intent = new Intent(WghtjMainActivity.this,TongjituActivity.class);
			startActivity(intent);
			break;
		case R.id.xxcx_ajczjg:
			intent = new Intent(WghtjMainActivity.this, CommonListActivity.class);
			startActivity(intent);
			break;
		case R.id.xxcx_qykh:
//			intent = new Intent(WghtjMainActivity.this,AreaExamActivity.class);
			intent = new Intent(WghtjMainActivity.this,DepCheckInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
