package com.jy.environment.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.jy.environment.R;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.util.WbMapUtil;

public class MapImageView extends Activity implements ViewFactory,
		OnClickListener {
	private ImageSwitcher is;
	private Gallery gallery;
	private int downX, upX;
	private ArrayList<String> imgList = new ArrayList<String>();// 图像ID
	List<Drawable> imgs = new ArrayList<Drawable>();
	ImageView maploading;
	TextView txt_username;
	TextView txt_usercontent;
	String username;
	String usercontent;

	AnimationDrawable anim;
	Boolean animflag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_imageview_activity);
		// 设置ImageSwitcher控件
		is = (ImageSwitcher) findViewById(R.id.switcher);
		is.setFactory(MapImageView.this);
		is.setInAnimation(AnimationUtils.loadAnimation(MapImageView.this,
				android.R.anim.fade_in));
		is.setOutAnimation(AnimationUtils.loadAnimation(MapImageView.this,
				android.R.anim.fade_out));
		// 注册相关事件
		is.setOnClickListener(this);

		txt_username = (TextView) findViewById(R.id.username);
		txt_username.setOnClickListener(this);
		txt_usercontent = (TextView) findViewById(R.id.usercontent);
		txt_usercontent.setOnClickListener(this);

		maploading = (ImageView) findViewById(R.id.maploading);
		maploading.setBackgroundResource(R.drawable.map_loading);
		anim = (AnimationDrawable) maploading.getBackground();
		anim.setOneShot(false);

		// 获取传递来的数据
		Intent intent = this.getIntent();
		String[] urls = intent
				.getStringArrayExtra(WeiBaoApplication.MAP_PICTURE_LIST);
		username = intent.getStringExtra("username");
		usercontent = intent.getStringExtra("usercontent");
		txt_username.setText(username);
		txt_usercontent.setText(usercontent);
		try {
			for (int i = 0; i < urls.length; i++) {
				imgList.add(urls[i]);
			}
			getPictureAstask task = new getPictureAstask();
			task.execute("");
			startmaploading();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 设置ImgaeSwitcher
	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFFF0F0F0);

		i.setScaleType(ImageView.ScaleType.FIT_CENTER);// 居中
		i.setLayoutParams(new ImageSwitcher.LayoutParams(// 自适应图片大小
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	public class ImageAdapter extends BaseAdapter {
		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return imgList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			if (imgs.size() == 0 || position > imgs.size()
					|| imgs.get(position) == null) {
				// 显示报错图片
				return i;
			}
			Drawable newdraw = sharePicureLayoutToDrawable(
					R.layout.map_hbdt_point_sharepicture, imgs.get(position));
			i.setImageDrawable(newdraw);
			// i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return i;
		}

		private Context mContext;
	}

	/**
	 * 添加标注的覆盖物
	 * 
	 * @param layout_id
	 * @return
	 */
	private Drawable sharePicureLayoutToDrawable(int layout_id, Drawable img) {
		LayoutInflater inflator = LayoutInflater.from(this);
		View viewHelp = inflator.inflate(layout_id, null);

		ImageView imageView = (ImageView) viewHelp
				.findViewById(R.id.sharepicture);

		imageView.setImageDrawable(img);

		viewHelp.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		viewHelp.layout(0, 0, WbMapUtil.dip2px(this, 44),
				WbMapUtil.dip2px(this, 44));

		viewHelp.buildDrawingCache();
		viewHelp.setDrawingCacheEnabled(true);
		Drawable drawable = null;
		try {
			Bitmap snapshot = viewHelp.getDrawingCache();
			// viewHelp.setDrawingCacheEnabled(false);
			// Bitmap snapshot = convertViewToBitmap(viewHelp, size);
			drawable = (Drawable) new BitmapDrawable(snapshot);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return drawable;
	}

	private void startmaploading() {
		maploading.setVisibility(ImageView.VISIBLE);

		anim.stop();
		anim.start();
		animflag = true;
	}

	private void endmaploading() {
		anim.stop();
		maploading.setVisibility(ImageView.GONE);
		animflag = false;
	}

	class getPictureAstask extends AsyncTask<String, Void, List<Drawable>> {

		@Override
		protected void onPostExecute(List<Drawable> result) {
			imgs = result;
			endmaploading();
			is.setOnTouchListener(new OnTouchListener() {
				/*
				 * 在ImageSwitcher控件上滑动可以切换图片
				 */
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						downX = (int) event.getX();// 取得按下时的坐标
						return true;
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						upX = (int) event.getX();// 取得松开时的坐标
						int index = 0;
						if (upX - downX > 100)// 从左拖到右，即看前一张
						{
							// 如果是第一，则去到尾部
							if (gallery.getSelectedItemPosition() == 0)
								index = gallery.getCount() - 1;
							else
								index = gallery.getSelectedItemPosition() - 1;
						} else if (downX - upX > 100)// 从右拖到左，即看后一张
						{
							// 如果是最后，则去到第一
							if (gallery.getSelectedItemPosition() == (gallery
									.getCount() - 1))
								index = 0;
							else
								index = gallery.getSelectedItemPosition() + 1;
						} else if (Math.abs(downX - upX) < 5) {
							MapImageView.this.finish();
							return false;
						}
						// 改变gallery图片所选，自动触发ImageSwitcher的setOnItemSelectedListener
						gallery.setSelection(index, true);
						return true;
					}
					return false;
				}

			});

			// 设置gallery控件
			gallery = (Gallery) findViewById(R.id.gallery);
			gallery.setAdapter(new ImageAdapter(MapImageView.this));
			gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					is.setImageDrawable(imgs.get(position));

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
				}

			});
			super.onPostExecute(result);
		}

		@Override
		protected List<Drawable> doInBackground(String... arg0) {
			// 获取图片资源
			List<Drawable> list = new ArrayList<Drawable>();
			for (int i = 0; i < imgList.size(); i++) {
				Drawable drawable = WbMapUtil.loadImageFromNetwork(imgList
						.get(i));
				// Drawable drawable =
				// WbMapUtil.loadImageFromNetwork("http://map.mapuni.com/images/logo.jpg");
				if (drawable == null) {
					continue;
				}
				list.add(drawable);
			}
			return list;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switcher:
			MapImageView.this.finish();
			break;
		case R.id.username:
			break;
		case R.id.usercontent:
			break;
		}

	}

}
