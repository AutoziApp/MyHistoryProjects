package cn.com.mapuni.meshing.activity.wd_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.meshing.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.util.DisplayUitl;

public class SettingGYXTActivity extends BaseActivity{
	
	TextView set_version;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"关于系统");
		initView();
	}
	
	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.wdsettinggyxtactivity_layout, null);
		middleLayout.addView(mainView);
		
		set_version = (TextView) mainView.findViewById(R.id.set_version);
		String bbh = DisplayUitl.getVersionName(SettingGYXTActivity.this); 
		set_version.setText("版本号："+bbh);	 
	}

	
}
