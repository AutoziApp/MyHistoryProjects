package com.mapuni.android.photograph;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mapuni.android.base.R;

/**
 * 选择图片
 * 
 * @author xuhuaiguang
 *
 */
public class CheckPicActivity extends Activity {

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private int count;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public List<Integer> drr_temp = new ArrayList<Integer>();
	public int max;

	RelativeLayout photo_relativeLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkpic_activity);
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		try {
			for (int i = 0; i < BimpHelper.bmp.size(); i++) {
				bmp.add(BimpHelper.bmp.get(i));
			}
			for (int i = 0; i < BimpHelper.drr.size(); i++) {
				drr.add(BimpHelper.drr.get(i));
			}
			max = BimpHelper.max;

			Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
			photo_bt_exit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					finish();
				}
			});
			Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
			photo_bt_del.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (listViews.size() == 1) {
						BimpHelper.bmp.clear();
						BimpHelper.drr.clear();
						BimpHelper.drr_temp1.clear();
						BimpHelper.drr_temp2.clear();
						BimpHelper.max = 0;
						FileUtils.deleteDir();
						finish();
					} else {
						String newStr = drr.get(count).substring(drr.get(count).lastIndexOf("/") + 1,
								drr.get(count).lastIndexOf("."));
						bmp.remove(count);
						drr.remove(count);
						del.add(newStr);
						max--;
						pager.removeAllViews();
						listViews.remove(count);
						adapter.setListViews(listViews);
						adapter.notifyDataSetChanged();
						
						drr_temp.add(count);
					}
				}
			});
			Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
			photo_bt_enter.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					BimpHelper.bmp = bmp;
					BimpHelper.drr = drr;
					BimpHelper.max = max;
					for (int i = 0; i < del.size(); i++) {
						FileUtils.delFile(del.get(i) + ".JPEG");
					}
					for (int j = 0; j < drr_temp.size(); j++) {
						if(BimpHelper.drr_temp1.contains(drr_temp.get(j))) {
							BimpHelper.drr_temp1.remove(drr_temp.get(j));
						} else if(BimpHelper.drr_temp2.contains(drr_temp.get(j))) {
							BimpHelper.drr_temp2.remove(drr_temp.get(j));
						}
					}
					finish();
				}
			});

			pager = (ViewPager) findViewById(R.id.viewpager);
			pager.setOnPageChangeListener(pageChangeListener);
			for (int i = 0; i < bmp.size(); i++) {
				initListViews(bmp.get(i));//
			}

			adapter = new MyPageAdapter(listViews);// 构造adapter
			pager.setAdapter(adapter);// 设置适配器
			Intent intent = getIntent();
			int id = intent.getIntExtra("ID", 0);
			pager.setCurrentItem(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);// 构造textView对象
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		listViews.add(img);// 添加view
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
