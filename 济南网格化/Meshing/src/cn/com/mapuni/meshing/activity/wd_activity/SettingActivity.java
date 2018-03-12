package cn.com.mapuni.meshing.activity.wd_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.meshing.R;
import com.mapuni.android.base.BaseActivity;

public class SettingActivity extends BaseActivity implements OnClickListener{
	
	ImageView wd_xtgl_gyxt,wd_xtgl_jcgx;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"系统管理");
		initView();
	}
	
	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wdsettingactivity_layout, null);
		middleLayout.addView(mainView);
		
		wd_xtgl_gyxt = (ImageView) mainView.findViewById(R.id.wd_xtgl_gyxt);
		wd_xtgl_jcgx = (ImageView) mainView.findViewById(R.id.wd_xtgl_jcgx);
		wd_xtgl_gyxt.setOnClickListener(this);
		wd_xtgl_jcgx.setOnClickListener(this);
		
		 
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.wd_xtgl_gyxt:
			intent = new Intent(SettingActivity.this,SettingGYXTActivity.class);
			startActivity(intent);
			break;
		case R.id.wd_xtgl_jcgx:
			
			break;
		default:
			break;
		}

	}
	
}
