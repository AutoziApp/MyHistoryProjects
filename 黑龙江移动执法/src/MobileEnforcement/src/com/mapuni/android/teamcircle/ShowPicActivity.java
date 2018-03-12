package com.mapuni.android.teamcircle;


import com.mapuni.android.MobileEnforcement.R;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class ShowPicActivity extends Activity {

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_pic);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String url = (String) extras.get("pic_url");
		
		imageView = (ImageView) findViewById(R.id.show_pic);
		
		Picasso.with(ShowPicActivity.this).load(url).into(imageView);
		
		
	}


}
