package com.mapuni.core.widget.takephoto.photo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.core.R;
import com.mapuni.core.widget.takephoto.photo.util.AlbumHelper;
import com.mapuni.core.widget.takephoto.photo.util.Bimp;
import com.mapuni.core.widget.takephoto.photo.util.BitmapCache;
import com.mapuni.core.widget.takephoto.photo.util.FileUtils;
import com.mapuni.core.widget.takephoto.photo.util.ImageBucket;
import com.mapuni.core.widget.takephoto.photo.util.ImageItem;
import com.mapuni.core.widget.takephoto.photo.util.PublicWay;
import com.mapuni.core.widget.takephoto.photo.util.Res;

import java.util.ArrayList;
import java.util.List;


/**
 * 棣栭〉闈ctivity
 *
 * @author king
 * @QQ:595163260
 * @version 2014骞�10鏈�18鏃�  涓嬪崍11:48:34
 */
public class SelectFileActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap ;
	private TextView finishBtn;
	public static int resultCode = 1000;
	public static String resultKey = "return_file";
	public static List<ImageBucket> contentList;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initData();
		Res.init(this);
		
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg, null);
		finishBtn = (TextView) parentView.findViewById(R.id.activity_selectimg_send);
		setContentView(parentView);
		finishBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(resultKey, new ArrayList(Bimp.tempSelectBitmap));
				intent.putExtra(resultKey, bundle);
				setResult(resultCode,intent);
				finish();
			}
			
		});
	
		Init();
		
	}
	
	private void initData(){
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				耗时操作
				takeIntent();
//				添加按钮
				bimap = BitmapFactory.decodeResource(
						getResources(),
						R.drawable.icon_addpic_unfocused);
//				加入view集合
				PublicWay.activityList.add(SelectFileActivity.this);
				getAllPhoto(getApplicationContext());
//			}
//		}).start();
		
	}
	public static void getAllPhoto(Context context){
//		获取所有本地照片
		AlbumHelper helper = AlbumHelper.getHelper();
		helper.init(context);
		contentList = helper.getImagesBucketList(true);
		int i=0;
		for(ImageBucket bucket:contentList){
			for(ImageItem item:bucket.imageList){
				i+=1;
			}
		}
		Log.i("Lybin", "相片总数------------------"+i);
	}
	
	private void takeIntent(){  
		try{
			Bundle bundle = (Bundle) getIntent().getExtras().get(resultKey);
			if(bundle!=null){
				Bimp.tempSelectBitmap.clear();
				Bimp.tempSelectBitmap.addAll((ArrayList<ImageItem>) bundle.get(resultKey));
//				Bimp.max = Bimp.tempSelectBitmap.size();
			}
		}catch(Exception e){
			return;
		}
	}

	public void Init() {
		
		pop = new PopupWindow(SelectFileActivity.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
//		Button bt2 = (Button) view
//				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
//		bt2.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent(SelectFileActivity.this,
//						AlbumActivity.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//				pop.dismiss();
//				ll_popup.clearAnimation();
//			}
//		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		noScrollgridview = (GridView)findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(SelectFileActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(SelectFileActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		private BitmapCache cache;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			cache = new BitmapCache();
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return Bimp.tempSelectBitmap.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
//			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
//				convertView.setTag( holder);
//				holder.image.setTag(position);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
			
//			int i = (Integer) holder.image.getTag();
//			Log.i("lybin", "i------------"+i);

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setScaleType(ScaleType.CENTER_INSIDE);
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_photo));
				
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setScaleType(ScaleType.CENTER_CROP);
				ImageItem item = Bimp.tempSelectBitmap.get(position);
//				holder.image.setTag(item.getImagePath());
				cache.getBitmapDelayer(holder.image,item.imagePath,callback);
			}

			return convertView;
		}
		BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
			@Override
			public void imageLoad(ImageView imageView, Bitmap bitmap,
					Object... params) {
				if (imageView != null && bitmap != null) {

					String url = (String) params[0];
//					if (url != null && url.equals((String) imageView.getTag())) {
						((ImageView) imageView).setImageBitmap(bitmap);
//					}
				} 
			}
			
		};

		public class ViewHolder {
			public ImageView image;
		
		}
		
	}
		
		
	

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg); 
			}
		};
	

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					Bimp.max = Bimp.tempSelectBitmap.size();
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
//					while (true) {
//						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
//							Message message = new Message();
//							message.what = 1;
//							handler.sendMessage(message);
//							break;
//						} else {
//							Bimp.max += 1;
//							Message message = new Message();
//							message.what = 1;
//							handler.sendMessage(message);
//						}
//					}
				}
			}).start();
		
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {

		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
				
				String fileName = Bimp.userId+String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				String path = FileUtils.saveBitmap(bm, fileName);
				
				ImageItem takePhoto = new ImageItem();
				takePhoto.setImgName(fileName);
				takePhoto.setBitmap(bm);
				takePhoto.setImagePath(path);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Bimp.tempSelectBitmap.clear();
		ShowAllPhoto.dataList.clear();
		Bimp.max = 0;
		contentList  = null;
		super.onDestroy();

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(resultKey, new ArrayList(Bimp.tempSelectBitmap));
			intent.putExtra(resultKey, bundle);
			setResult(resultCode,intent);
	
			
			for(int i=0;i<PublicWay.activityList.size();i++){
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
//			System.exit(0);
		}
		return true;
	}

}

