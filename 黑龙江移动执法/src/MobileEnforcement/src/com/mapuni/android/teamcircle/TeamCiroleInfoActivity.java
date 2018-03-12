package com.mapuni.android.teamcircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.dataprovider.FileHelper;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamCiroleInfoActivity extends BaseActivity{
	public String netUrl=TeamCircleActivity.netUrl;
	private TextView tv_user_name;
	private TextView content;
	private ImageView im_select;
	private ImageView line;
	private LinearLayout comment_container;
	private GridView gridView;
	private ImageView im_pic1;
	private ImageView im_pic2;
	private ImageView im_pic3;
	private ImageView im_pic4;
	private ImageView video_play;
	private String path;
	private String guid;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

			setContentView(R.layout.friends_circle_info_item);
			tv_user_name = (TextView)
					findViewById(R.id.tv_user_name);
			content = (TextView)
					findViewById(R.id.content);
			im_select = (ImageView) 
					
					findViewById(R.id.im_select);
			line = (ImageView) findViewById(R.id.line);
			comment_container = (LinearLayout) 
					findViewById(R.id.comment_container);
			gridView = (GridView) 
					findViewById(R.id.gridView);
			im_pic1 = (ImageView)
					findViewById(R.id.im_pic1);
			im_pic2 = (ImageView) 
					findViewById(R.id.im_pic2);
			im_pic3 = (ImageView)
					findViewById(R.id.im_pic3);
			im_pic4 = (ImageView) 
					findViewById(R.id.im_pic4);
			
			video_play = (ImageView) 
					findViewById(R.id.video_play);
			ArrayList<String> FilePathList = getIntent().getStringArrayListExtra("FilePathList");
			int type = getIntent().getIntExtra("Type",0);
			String Content=getIntent().getStringExtra("Content");
			String SenderName=getIntent().getStringExtra("SenderName");
			if (FilePathList==null) {
				FilePathList=new ArrayList<String>();
			}
			
			path = FileHelper.SDCardPath + "mapuni/MobileEnforcement/" + "Attach/"  +"DWQ"+ "/";	
			guid = UUID.randomUUID().toString();
			initView(FilePathList,type,Content,SenderName);	
		}
		
		
		
		
		
		private void initView(final ArrayList<String> filePathList, int type, String contents, String senderName) {
				tv_user_name.setTag(senderName);
				tv_user_name.setText(senderName);
				content.setText(contents);
			if (type==2) {
				if (filePathList!=null) {									
				if (filePathList.size()==1) {
					netImageToView(netUrl+filePathList.get(0), im_pic1);
					im_pic1.setVisibility(View.VISIBLE);
					im_pic2.setVisibility(View.GONE);
					im_pic3.setVisibility(View.GONE);
					im_pic4.setVisibility(View.GONE);
					
				}else if(filePathList.size()==2){
					netImageToView(netUrl+filePathList.get(0), im_pic1);
					netImageToView(netUrl+filePathList.get(1), im_pic2);
					im_pic1.setVisibility(View.VISIBLE);
					im_pic2.setVisibility(View.VISIBLE);
					im_pic3.setVisibility(View.GONE);
					im_pic4.setVisibility(View.GONE);
					
				}else if(filePathList.size()==3){
					netImageToView(netUrl+filePathList.get(0), im_pic1);
					netImageToView(netUrl+filePathList.get(1), im_pic2);
					netImageToView(netUrl+filePathList.get(2), im_pic3);
					im_pic1.setVisibility(View.VISIBLE);
					im_pic2.setVisibility(View.VISIBLE);
					im_pic3.setVisibility(View.VISIBLE);
					im_pic4.setVisibility(View.GONE);
					
				}else if(filePathList.size()==4){
					netImageToView(netUrl+filePathList.get(0), im_pic1);
					netImageToView(netUrl+filePathList.get(1), im_pic2);
					netImageToView(netUrl+filePathList.get(2), im_pic3);
					netImageToView(netUrl+filePathList.get(3), im_pic4);
					im_pic1.setVisibility(View.VISIBLE);
					im_pic2.setVisibility(View.VISIBLE);
					im_pic3.setVisibility(View.VISIBLE);
					im_pic4.setVisibility(View.VISIBLE);					
				}else{
					im_pic1.setVisibility(View.GONE);
					im_pic2.setVisibility(View.GONE);
					im_pic3.setVisibility(View.GONE);
					im_pic4.setVisibility(View.GONE);
				}
				// 图片设置点击事件
				im_pic1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToPicActivity(netUrl+filePathList.get(0));
					}
				});
				im_pic2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToPicActivity(netUrl+filePathList.get(1));
					}
				});
				im_pic3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToPicActivity(netUrl+filePathList.get(2));
					}
				});
				im_pic4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ToPicActivity(netUrl+filePathList.get(3));
					}
				});
				}
			}else if (type == 3) {
				im_pic1.setVisibility(View.VISIBLE);
				video_play.setVisibility(View.VISIBLE);
			
				
				LayoutParams layoutParams = im_pic1.getLayoutParams();
				layoutParams.height=300;
				layoutParams.width=300;
				
				im_pic1.setLayoutParams(layoutParams);
				//网络视频
				String url = filePathList.get(0);
				Bitmap videoBitmap = getNetVideoBitmap(netUrl+url);
				//设置视频路径
				im_pic1.setImageBitmap(videoBitmap);
			
				//设置跳转页面 播放视频
				//holder.tc_video.start();
				im_pic1.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						String url=netUrl+filePathList.get(0);
						
							ToVideoActivity(url);
						
					}
			
				});
			}
			
		}
		public void netImageToView(String path,ImageView view) {
			Picasso.with(TeamCiroleInfoActivity.this)
			.load(path)
			.placeholder(R.drawable.yutu)
			.into(view);
		}
		private void ToVideoActivity(String url) {
			Intent intent = new Intent(TeamCiroleInfoActivity.this,
					ShowVideoActivity.class);
			intent.putExtra("video_url", url);

			startActivity(intent);
		}
		// 展示图片
		private void ToPicActivity(String url) {
			Intent intent = new Intent(TeamCiroleInfoActivity.this,
					ShowPicActivity.class);
			intent.putExtra("pic_url", url);

			startActivity(intent);
		}
		 /**  
	     *  服务器返回url，通过url去获取视频的第一帧  
	     *  Android 原生给我们提供了一个MediaMetadataRetriever类
	     *  提供了获取url视频第一帧的方法,返回Bitmap对象
	     * 	
	     *  @param videoUrl  
	     *  @return  
	     */  
	@SuppressLint("NewApi") 
	public static Bitmap getNetVideoBitmap(String videoUrl) {  
	        Bitmap bitmap = null;  

	        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
	        try {  
	            //根据url获取缩略图  
	            retriever.setDataSource(videoUrl, new HashMap());  
	            //获得第一帧图片  
	            bitmap = retriever.getFrameAtTime();  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } finally {  
	            retriever.release();  
	        }  
	        return bitmap;  
	    }
		
}
