package cn.com.mapuni.meshing.activity;

import cn.com.mapuni.meshing.activity.db_activity.DbscfkActivity;

import com.bumptech.glide.Glide;
import com.example.meshing.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PhtoActivity extends Activity {
	
	ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_ac);
		iv=(ImageView) findViewById(R.id.iv);
		Glide.with(this)
		.load(getIntent().getStringExtra("path"))
		.placeholder(R.drawable.wd_xcjl_jzz)
		.error(R.drawable.wd_xcjl_jzsb)
		.into(iv);
		
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
