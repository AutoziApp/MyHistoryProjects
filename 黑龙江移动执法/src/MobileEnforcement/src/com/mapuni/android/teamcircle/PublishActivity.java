package com.mapuni.android.teamcircle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;




import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.provider.MediaStore.Video.VideoColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class PublishActivity extends BaseActivity implements OnClickListener {

	private Button tv_publish;
	private EditText et_body;
	private ImageView im_add_p;
	private ImageView im_add_p1;
	private ImageView im_add_p2;
	private ImageView im_add_p3;
	private ImageView im_add_p4;
	private static String path = "/sdcard/myHead/";// sd·��

	int cishu = 0;
	private Bitmap head;
	private LayoutInflater _LayoutInflater;
	private View taskRegisterView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.layout_publish);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "����");
		_LayoutInflater = LayoutInflater.from(this);
		
		taskRegisterView = _LayoutInflater.inflate(R.layout.layout_publish,
				null);
		
		// �¼ӷ��ذ�ť ����ͬ����ť
		setBackButtonVisiable(true);

		/*tv_publish = (Button) taskRegisterView.findViewById(R.id.fabu);*/
		et_body = (EditText) taskRegisterView.findViewById(R.id.et_body);
		im_add_p = (ImageView) taskRegisterView.findViewById(R.id.im_add_p);
		im_add_p1 = (ImageView) taskRegisterView.findViewById(R.id.im_add_p1);
		im_add_p2 = (ImageView) taskRegisterView.findViewById(R.id.im_add_p2);
		im_add_p3 = (ImageView) taskRegisterView.findViewById(R.id.im_add_p3);
		im_add_p4 = (ImageView) taskRegisterView.findViewById(R.id.im_add_p4);
		iv_video = (ImageView) taskRegisterView.findViewById(R.id.iv_video);
		
		video_play3 = (ImageView) taskRegisterView.findViewById(R.id.video_play3);
		im_add_p.setScaleType(ScaleType.FIT_XY);
		im_add_p1.setOnClickListener(this);
		im_add_p2.setOnClickListener(this);
		im_add_p3.setOnClickListener(this);
		im_add_p4.setOnClickListener(this);
		iv_video.setOnClickListener(this);
		

		/*// ����
		 tv_publish.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
			 if (et_body.getText().toString().trim()==null) {
				Toast.makeText(PublishActivity.this, "������Ϣ����Ϊ��", Toast.LENGTH_SHORT).show();				
			 }else{
				 showPublishDialog(); 
			 }
		 }	
		 });*/

		im_add_p.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����ѡ��dialog
				showTypeDialog();
			}
		});
		// չʾ
		((LinearLayout) findViewById(R.id.middleLayout))
				.addView(taskRegisterView);
	}

	public String sendUrl = TeamCircleActivity.netUrl+"/api/TeamCircle/SendMessage";
	public String upLoadUrl=TeamCircleActivity.netUrl+"/api/TeamCircle/UploadFile";
	// ���������dialog
	private void showPublishDialog() {
		// ����������
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // ���ò���
        builder
				.setTitle("��ʾ")
				// ���öԻ������
				.setMessage("ȷ��������?")
				// ������ʾ������
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {// ���ȷ����ť
					@Override
					public void onClick(DialogInterface dialog,
							int which) {// ȷ����ť����Ӧ�¼�
						// TODO �����ϴ�����
						
						final YutuLoading load=new YutuLoading(PublishActivity.this);
						load.setLoadMsg("���ڷ������ݣ����Ժ�...", "");
						load.setCancelable(true);
						load.showDialog();
						//��ȡ��Ϣ							
						String body = et_body.getText().toString();	
													
				
					//�ϴ����ֺ��û���Ϣ
					String msgCode = UUID.randomUUID().toString();
					final Map<String, String> msgMap=new HashMap<String, String>();
					msgMap.put("Content",body);
					msgMap.put("SenderCode",Global.getGlobalInstance().getUserid());
					msgMap.put("msgCode",msgCode);
					msgMap.put("RegionCode",Global.getGlobalInstance().getAreaCode());
					int type = 0;
					if (files.size()>0||files2.size()>0) {					
				
					if (files2.size()>0) {
						type=3;
					}else if(files.size()>0){
						type=2;
					}else{
						type=1;
					}			
					StringCallback callback2=new StringCallback() {
						
						@Override
						public void onResponse(String arg0, int arg1) {
//							// TODO Auto-generated method stub
//							load.dismissDialog();
//							Toast.makeText(PublishActivity.this, "������Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
////							setResult(1);
////									
////						//	PublishActivity.this.finish();
							// TODO Auto-generated method stub										
							load.dismissDialog();
							Toast.makeText(PublishActivity.this, "��Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
							setResult(1);
							PublishActivity.this.finish();	
						
						}
						
						@Override
						public void onError(Call arg0, Exception arg1, int arg2) {
							 OkHttpUtils.get().url(sendUrl).build().cancel();
							load.dismissDialog();
							Toast.makeText(PublishActivity.this, "��������ԭ��,��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
							
						}
					};
					//�ϴ�����	
					Map<String, String> params = new HashMap<String, String>();
					params.put("BizCode",msgCode);
					params.put("Type",type+"");
					if (files.size()==1) {
						if (files.get(0).exists()) {
							OkHttpUtils.post()
						
							.addFile("image", files.get(0).getName()+".jpg",files.get(0))
							.url(upLoadUrl)
							.params(params)
							.build()
							.execute(callback2);
							
//							msgMap.put("Type","2");
//							OkHttpUtils.post()
//										.url(sendUrl)
//										.params(msgMap)
//										.build()
//										
//										.execute(new StringCallback() {
//											
//											public void onResponse(String arg0, int arg1) {
//												load.dismissDialog();
//												setResult(1);
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
//												PublishActivity.this.finish();	
//											}
//											
//											@Override
//											public void onError(Call arg0, Exception arg1, int arg2) {
//												load.dismissDialog();
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
//												
//											}
//										});
						}						
						}
						if (files.size()==2) {						
					        OkHttpUtils.post()//  
		                    .addFile("image", files.get(0).getName()+".jpg", files.get(0))//  
		                    .addFile("image", files.get(0).getName()+".jpg", files.get(1))//  
		                    .url(upLoadUrl)  
		                    .params(params)//  
		                    .build()//  
		                 
		                    .execute(callback2);  
//					    	msgMap.put("Type","2");
//							OkHttpUtils.post()
//										.url(sendUrl)
//										.params(msgMap)
//										.build()
//										
//										.execute(new StringCallback() {
//											
//											public void onResponse(String arg0, int arg1) {
//												load.dismissDialog();
//												setResult(1);
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
//												PublishActivity.this.finish();	
//											}
//											
//											@Override
//											public void onError(Call arg0, Exception arg1, int arg2) {
//												load.dismissDialog();
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
//												
//											}
//										});				
						}	
						if (files.size()==3) {						
							OkHttpUtils.post()
										.url(upLoadUrl)
										.params(params)
										.addFile("image", files.get(0).getName()+".jpg",files.get(0))
										.addFile("image", files.get(1).getName()+".jpg",files.get(1))
										.addFile("image", files.get(2).getName()+".jpg",files.get(2))
										.build()
										.execute(callback2);
//							msgMap.put("Type","2");
//							OkHttpUtils.post()
//										.url(sendUrl)
//										.params(msgMap)
//										.build()
//										
//										.execute(new StringCallback() {
//											
//											public void onResponse(String arg0, int arg1) {
//												load.dismissDialog();
//												setResult(1);
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
//												PublishActivity.this.finish();	
//											}
//											
//											@Override
//											public void onError(Call arg0, Exception arg1, int arg2) {
//												load.dismissDialog();
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
//												
//											}
//										});
									}	
						if (files.size()==4) {						
							OkHttpUtils.post()
										.url(upLoadUrl)
										.params(params)
										.addFile("image", files.get(0).getName()+".jpg",files.get(0))
										.addFile("image", files.get(1).getName()+".jpg",files.get(1))
										.addFile("image", files.get(2).getName()+".jpg",files.get(2))
										.addFile("image", files.get(3).getName()+".jpg",files.get(3))
										.build()
										.execute(callback2);
									}
					if (files2.size()>0) {															
							OkHttpUtils.post()
										.url(upLoadUrl)
										.params(params)
										.addFile("image",files2.get(0).getName()+".mp4",files2.get(0))
										.build()
										
										.execute(new StringCallback(){

											@Override
											public void onError(Call arg0,
													Exception arg1, int arg2) {
												
												Toast.makeText(PublishActivity.this, "������Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
											}

											@Override
											public void onResponse(String arg0,
													int arg1) {
												load.dismissDialog();
											Toast.makeText(PublishActivity.this, "�����ɹ�!", Toast.LENGTH_SHORT).show();
////												setResult(1);
////														
												PublishActivity.this.finish();
											}
											
											
											
											
										});
							
							
//							msgMap.put("Type","3");
//							OkHttpUtils.post()
//										.url(sendUrl)
//										.params(msgMap)
//										.build()
//										.execute(new StringCallback() {
//											
//											public void onResponse(String arg0, int arg1) {
//												load.dismissDialog();
//												setResult(1);
//												finish();	
//											}
//											
//											@Override
//											public void onError(Call arg0, Exception arg1, int arg2) {
//												load.dismissDialog();
//												Toast.makeText(PublishActivity.this, "��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
//												
//											}
//										});
							LogUtil.i("xxoo", files2.get(0).getName());
							
					}		
							 
							}else{
								
								msgMap.put("Type","1");
								OkHttpUtils.post()
											.url(sendUrl)
											.params(msgMap)
											.build()
											.execute(new StringCallback() {
												
												public void onResponse(String arg0, int arg1) {
													// TODO Auto-generated method stub										
													load.dismissDialog();
													Toast.makeText(PublishActivity.this, "��Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
													setResult(1);
													finish();	
												}
												
												@Override
												public void onError(Call arg0, Exception arg1, int arg2) {
													// TODO Auto-generated method stub
													load.dismissDialog();
													Toast.makeText(PublishActivity.this, "��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
													
												}
											});
							}	
					
					
					msgMap.put("Type",type+"");
					OkHttpUtils.post()
								.url(sendUrl)
								.params(msgMap)
								.build()
								.execute(new StringCallback() {
									
									public void onResponse(String arg0, int arg1) {
//										// TODO Auto-generated method stub										
//										load.dismissDialog();
//										Toast.makeText(PublishActivity.this, "��Ϣ�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
//										setResult(1);
//										finish();	
									}
									
									@Override
									public void onError(Call arg0, Exception arg1, int arg2) {
										 OkHttpUtils.get().url(upLoadUrl).build().cancel();
										load.dismissDialog();
										Toast.makeText(PublishActivity.this, "��������ԭ��,��Ϣ�ϴ�ʧ��", Toast.LENGTH_SHORT).show();
										
									}
								});
					
					
					
						}
				}
						)
				.setNegativeButton("����", new DialogInterface.OnClickListener() {// ��ӷ��ذ�ť
							public void onClick(DialogInterface dialog,
									int which) {// ��Ӧ�¼�
								dialog.dismiss();
							}
						});
        builder.create().show(); 
        
	}

	// ����ѡ��Ի���
	private void showTypeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		View view = View.inflate(this, R.layout.dialog_select_photo, null);
		TextView tv_select_gallery = (TextView) view
				.findViewById(R.id.tv_select_gallery);
		TextView tv_select_camera = (TextView) view
				.findViewById(R.id.tv_select_camera);
		TextView tv_luxiang = (TextView) view.findViewById(R.id.tv_luxiang);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		tv_select_gallery.setOnClickListener(new OnClickListener() {// �������ѡȡ
					@Override
					public void onClick(View v) {
						Intent intent1 = new Intent(Intent.ACTION_PICK, null);
						intent1.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent1, 1);

						dialog.dismiss();
					}
				});
		tv_select_camera.setOnClickListener(new OnClickListener() {// ���������
					@Override
					public void onClick(View v) {
						Intent intent2 = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										"head.jpg")));
						startActivityForResult(intent2, 2);// ����ForResult��
						dialog.dismiss();
					}
				});

		tv_luxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.5);
				startActivityForResult(mIntent, 3);
				dialog.dismiss();

			}
		});
		dialog.setCancelable(false);
		dialog.show();
		
	}

	List<File> files = new ArrayList<File>();
	List<File> files2 = new ArrayList<File>();
	private Bitmap bitmap = null;
	private ImageView iv_video;
	private ImageView video_play3;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			if (uri==null||uri.equals("")) {
				im_add_p1.setVisibility(View.GONE);
				return;
			}

			if (uri != null || uri.equals("")) {

				if (cishu == 0 && uri != null && !"".equals(uri)) {
					im_add_p1.setVisibility(View.VISIBLE);
					im_add_p1.setImageURI(uri);
					cishu = 1;
					// ��uriת����file

					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = managedQuery(uri, proj, null,
							null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					File file = new File(img_path);
					LogUtil.i("PublishActivity", img_path);
					files.add(file);
				
				} else if (cishu == 1 && uri != null && !"".equals(uri)) {
					im_add_p2.setVisibility(View.VISIBLE);
					im_add_p2.setImageURI(uri);
					cishu = 2;
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = managedQuery(uri, proj, null,
							null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					File file = new File(img_path);
					LogUtil.i("PublishActivity", img_path);
					files.add(file);
				} else if (cishu == 2 && uri != null && !"".equals(uri)) {
					im_add_p3.setVisibility(View.VISIBLE);
					im_add_p3.setImageURI(uri);
					cishu = 3;
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = managedQuery(uri, proj, null,
							null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					File file = new File(img_path);
					LogUtil.i("PublishActivity", img_path);
					files.add(file);
				} else if (cishu == 3 && uri != null && !"".equals(uri)) {
					im_add_p4.setVisibility(View.VISIBLE);
					im_add_p4.setImageURI(uri);
					cishu = 0;
					Toast.makeText(PublishActivity.this, "����������ͼƬ", 0).show();
					im_add_p.setVisibility(View.GONE);
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = managedQuery(uri, proj, null,
							null, null);
					int actual_image_column_index = actualimagecursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor
							.getString(actual_image_column_index);
					File file = new File(img_path);
					LogUtil.i("PublishActivity", img_path);
					files.add(file);
				}
			}

			break;
		case 2:
			
			if (resultCode==RESULT_OK) {
				
		
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/head.jpg");
			if (cishu == 0) {
				im_add_p1.setVisibility(View.VISIBLE);
				im_add_p1.setImageURI(Uri.fromFile(temp));
				cishu = 1;
				files.add(temp);
				im_add_p1.setClickable(false);
				
			} else if (cishu == 1 && temp != null) {
				im_add_p2.setVisibility(View.VISIBLE);
				im_add_p2.setImageURI(Uri.fromFile(temp));
				cishu = 2;
				files.add(temp);
				im_add_p2.setClickable(false);
			} else if (cishu == 2 && temp != null) {
				im_add_p3.setVisibility(View.VISIBLE);
				im_add_p3.setImageURI(Uri.fromFile(temp));
				cishu = 3;
				files.add(temp);
				im_add_p3.setClickable(false);
			} else if (cishu == 3 && temp != null) {
				im_add_p4.setVisibility(View.VISIBLE);
				im_add_p4.setImageURI(Uri.fromFile(temp));
				cishu = 0;
				Toast.makeText(PublishActivity.this, "����������ͼƬ", 0).show();
				im_add_p.setVisibility(View.GONE);
				files.add(temp);
				im_add_p4.setClickable(false);

			}
			
			}
			break;
		case 3:
			if (data == null) {
				return;
			}
			im_add_p1.setVisibility(View.GONE);
			im_add_p2.setVisibility(View.GONE);
			im_add_p3.setVisibility(View.GONE);
			im_add_p4.setVisibility(View.GONE);
			im_add_p.setVisibility(View.GONE);
			iv_video.setVisibility(View.VISIBLE);
			iv_video.setClickable(false);
			
			video_play3.setVisibility(View.VISIBLE);
			
		
			final Uri uri3 = data.getData();
			
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor actualimagecursor = managedQuery(uri3, proj, null,
					null, null);
			int actual_image_column_index = actualimagecursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			actualimagecursor.moveToFirst();
			String img_path = actualimagecursor
					.getString(actual_image_column_index);
			File file = new File(img_path);
			files2.add(file);
			
			// ¼����Ƶ���
			try {
				AssetFileDescriptor videoAsset = getContentResolver()
						.openAssetFileDescriptor(data.getData(), "r");
				FileInputStream fis = videoAsset.createInputStream();
				File tmpFile = new File(
						Environment.getExternalStorageDirectory(),
						"recordvideo.mp4");
				FileOutputStream fos = new FileOutputStream(tmpFile);

				byte[] buf = new byte[1024];
				int len;
				while ((len = fis.read(buf)) > 0) {
					fos.write(buf, 0, len);
				}
				fis.close();
				fos.close();
				// �ļ�д��֮��ɾ��/sdcard/dcim/CAMERA/XXX.MP4
				deleteDefaultFile(data.getData());
			} catch (Exception e) {
				e.printStackTrace();
			}
			iv_video.setImageBitmap(bitmap);	
			
	video_play3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ToVideoActivity(uri3.toString());
				}
			});
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
		
	
	private void ToVideoActivity(String url) {
		Intent intent = new Intent(PublishActivity.this,
				ShowVideoActivity.class);
		intent.putExtra("video_url", url);

		startActivity(intent);
	}
	
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// �����ļ���
		String fileName = path + "head.jpg";// ͼƬ����
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// �ر���
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void fabu(View view) {
		if (TextUtils.isEmpty(et_body.getText())&&files.size()==0&&files2.size()==0) {
			Toast.makeText(PublishActivity.this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
		}else{
			showPublishDialog();
		}			
	}

	// ɾ����/sdcard/dcim/Camera/Ĭ�����ɵ��ļ�
	private void deleteDefaultFile(Uri uri) {
		String fileName = null;
		if (uri != null) {
			// content
			Log.d("Scheme", uri.getScheme().toString());
			if (uri.getScheme().toString().equals("content")) {
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				if (cursor.moveToNext()) {
					int columnIndex = cursor
							.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
					fileName = cursor.getString(columnIndex);
					// ��ȡ����ͼid
					int id = cursor.getInt(cursor
							.getColumnIndex(VideoColumns._ID));
					bitmap = Thumbnails.getThumbnail(getContentResolver(), id,
							Thumbnails.MICRO_KIND, null);

					if (!fileName.startsWith("/mnt")) {
						fileName = "/mnt/" + fileName;
					}
					Log.d("fileName", fileName);
				}
			}
		}
		// ɾ���ļ�
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
			Log.d("delete", "ɾ���ɹ�");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.im_add_p1:
			if (cishu==0) {
				
			
			cishu=0;
			files.remove(0);
			showTypeDialog();}
			break;
		case R.id.im_add_p2:
			if (cishu==0) {
			cishu=1;
			files.remove(1);
			showTypeDialog();}
			break;
		case R.id.im_add_p3:
			if (cishu==1) {
			cishu=2;
			files.remove(2);
			showTypeDialog();}
			break;
		case R.id.im_add_p4:
			if (cishu==2) {
			cishu=3;
			files.remove(3);
			showTypeDialog();}
			break;
		case R.id.iv_video:
			files2.clear();
			showTypeDialog();
			break;
		}
		
	}

}
