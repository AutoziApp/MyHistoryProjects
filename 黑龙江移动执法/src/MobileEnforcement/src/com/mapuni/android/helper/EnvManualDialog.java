package com.mapuni.android.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.dataprovider.XmlHelper;

public class EnvManualDialog extends Dialog implements DialogInterface {

	private final String TAG = "HelperDialog";

	private EnvManualController EnvManualC;
	private TextView helper_title;
	private ArrayList<HashMap<String, Object>> listData;
	private GridView Env_gridview;

	public EnvManualDialog(Context context) {
		this(context, 0);
		// Dialog按对话框外面取消操作
		this.setCanceledOnTouchOutside(true);
	}

	public EnvManualDialog(Context context, int theme) {
		super(context, theme);

	}

	protected EnvManualDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void show() {
		/** 设置框体不显示标题栏 */
		if (Env_gridview == null) {
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			View view = factory.inflate(R.layout.helper, null);
			listData = new ArrayList<HashMap<String, Object>>();
			/** 从XML文件中读取配置 */
			listData = getMoreMenu("style_grid_flfg.xml", "item");
			helper_title = (TextView) view.findViewById(R.id.helper_title);
			helper_title.setText("环保手册");
			Env_gridview = (GridView) view.findViewById(R.id.helper_gridview);
			Env_gridview.setAdapter(new HelperListAdapter(this.getContext(),
					listData));
			Env_gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg2) {
					case 0:
						EnvManualController.getInstance().openDialog(
								EnvManualController.HBFL);
						EnvManualController.getInstance().EnvManualExit();
						break;
					case 1:
						EnvManualController.getInstance().openDialog(
								EnvManualController.HBBZ);
						EnvManualController.getInstance().EnvManualExit();
						break;
					case 2:
						EnvManualController.getInstance().openDialog(
								EnvManualController.ZDWJ);
						EnvManualController.getInstance().EnvManualExit();
						break;
					case 3:
						EnvManualController.getInstance().openDialog(
								EnvManualController.WHP);
						EnvManualController.getInstance().EnvManualExit();
						break;
					case 4:
						EnvManualController.getInstance().openDialog(
								EnvManualController.YJYA);
						EnvManualController.getInstance().EnvManualExit();
						break;
					case 5:
						EnvManualController.getInstance().openDialog(
								EnvManualController.ZJK);
						EnvManualController.getInstance().EnvManualExit();
						break;

					default:
						break;
					}
				}
			});
			super.setContentView(view);
		}
		super.show();

	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}

	/**
	 * 读取XML配置文件
	 */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml,
			String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = this.getContext().getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	/**
	 * 适配器
	 */
	public class HelperListAdapter extends BaseAdapter {

		public Context context;
		public ArrayList<HashMap<String, Object>> data;

		public HelperListAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.context = context;
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				/** 用来加载GridView的item布局 */
				convertView = LayoutInflater.from(context).inflate(
						R.layout.helper_gridview_list, null);
				/** 初始化item的组件 */
				holder.helper_main_tv = (TextView) convertView
						.findViewById(R.id.helper_main_textView);
				holder.helper_main_img = (ImageView) convertView
						.findViewById(R.id.helper_main_imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/** 通过holder来获取xmL里的数据 */
			holder.helper_main_img.setImageBitmap(getRes(data.get(position)
					.get("img").toString()));
			holder.helper_main_tv.setText(data.get(position).get("qymc")
					.toString());
			return convertView;
		}

		/** 定义一个ViewHolder，用来优化View */
		class ViewHolder {
			ImageView helper_main_img;
			TextView helper_main_tv;
		}

		/** Description: 获取列表的图片 */
		public Bitmap getRes(String name) {
			ApplicationInfo appInfo = context.getApplicationInfo();
			int resID = context.getResources().getIdentifier(name, "drawable",
					appInfo.packageName);
			return BitmapFactory.decodeResource(context.getResources(), resID);
		}

	}
}
