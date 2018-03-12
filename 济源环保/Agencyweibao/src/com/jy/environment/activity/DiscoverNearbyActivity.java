package com.jy.environment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.services.LocationService;

public class DiscoverNearbyActivity extends ActivityBase{
	
	private ImageView yaodian,huanjing_company,yaodian_aikang,back;
	private String fromactivity="";//用来标识打开该窗口的源activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_nearby_first);
		LocationService.sendGetLocationBroadcast(DiscoverNearbyActivity.this);
		fromactivity = getIntent().getStringExtra("from");
		initView();
		initLisener();
	}
	
	
	public void initView()
	{
		yaodian = (ImageView) findViewById(R.id.nearby_yaodian);
		huanjing_company = (ImageView) findViewById(R.id.nearby_hjjk);
		yaodian_aikang = (ImageView) findViewById(R.id.nearby_yaodian_aikang);
		back = (ImageView) findViewById(R.id.nearby_suhscribe_back);
	}
	public void initLisener()
	{
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		yaodian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiscoverNearbyActivity.this,DiscoverNearbyListActivity.class);
				intent.putExtra("searchtype", "1");
				intent.putExtra("titlename", "药店");
				if(fromactivity != null)
				{
					if(fromactivity.equals("mapMainActivity"))
					{
						intent.putExtra("from", "mapMainActivity");
						intent.putExtra("maplat", getIntent().getIntExtra("maplat", 0));
						intent.putExtra("maplong", getIntent().getIntExtra("maplong", 0));
					}
				}
				
				startActivity(intent);
			}
		});
		huanjing_company.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiscoverNearbyActivity.this,DiscoverNearbyListActivity.class);
				intent.putExtra("searchtype", "2");
				intent.putExtra("titlename", "环境监控企业");
				if(fromactivity != null)
				{
					if(fromactivity.equals("mapMainActivity"))
					{
						intent.putExtra("from", "mapMainActivity");
						intent.putExtra("maplat", getIntent().getIntExtra("maplat", 0));
						intent.putExtra("maplong", getIntent().getIntExtra("maplong", 0));
					}
				}
				startActivity(intent);
			}
		});
		yaodian_aikang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiscoverNearbyActivity.this,DiscoverNearbyListActivity.class);
				intent.putExtra("searchtype", "3");
				intent.putExtra("titlename", "爱康体检");
				if(fromactivity != null)
				{
					if(fromactivity.equals("mapMainActivity"))
					{
						intent.putExtra("from", "mapMainActivity");
						intent.putExtra("maplat", getIntent().getIntExtra("maplat", 0));
						intent.putExtra("maplong", getIntent().getIntExtra("maplong", 0));
					}
				}
				startActivity(intent);
			}
		});
	}
	
	
	

}
