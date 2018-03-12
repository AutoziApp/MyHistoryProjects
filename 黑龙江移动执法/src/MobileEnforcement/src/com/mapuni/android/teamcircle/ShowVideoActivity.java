package com.mapuni.android.teamcircle;

import com.mapuni.android.MobileEnforcement.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class ShowVideoActivity extends Activity {
	private com.mapuni.android.teamcircle.MyVideoView mVideoView ;
	private ImageView video_play2;
	 
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_showvideo);
	    Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String url = (String) extras.get("video_url");
	    
	    Uri uri = Uri.parse(url);
	    	
	     video_play2 = (ImageView) findViewById(R.id.video_play2);
	     video_play2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mVideoView.start();
				video_play2.setVisibility(View.GONE);
			}
		});
	    
//	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//	    	//   getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//	    	//   getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//	    	  }
	    	  mVideoView = (com.mapuni.android.teamcircle.MyVideoView) findViewById(R.id.video_view);
	    	  mVideoView.setVideoURI(uri);
	    	  mVideoView.start();
	    	  mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	    	   @Override
	    	   public void onCompletion(MediaPlayer mp) {
	    	 //   finish();
	    		   video_play2.setVisibility(View.VISIBLE);
	    		  
	    	   }
	    	  });
	    	  
//	    	  mVideoView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//				if (video_play2.isShown()) {
//					video_play2.setVisibility(View.GONE);
//					mVideoView.start();
//				}else{
//					mVideoView.pause();
//					video_play2.setVisibility(View.VISIBLE);
//				}
//				}
//			});
	    	  
	    	 }
	  
	    	}
