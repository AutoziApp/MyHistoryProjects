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
		// Dialog���Ի�������ȡ������
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
		/** ���ÿ��岻��ʾ������ */
		if (Env_gridview == null) {
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			View view = factory.inflate(R.layout.helper, null);
			listData = new ArrayList<HashMap<String, Object>>();
			/** ��XML�ļ��ж�ȡ���� */
			listData = getMoreMenu("style_grid_flfg.xml", "item");
			helper_title = (TextView) view.findViewById(R.id.helper_title);
			helper_title.setText("�����ֲ�");
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
	 * ��ȡXML�����ļ�
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
	 * ������
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
				/** ��������GridView��item���� */
				convertView = LayoutInflater.from(context).inflate(
						R.layout.helper_gridview_list, null);
				/** ��ʼ��item����� */
				holder.helper_main_tv = (TextView) convertView
						.findViewById(R.id.helper_main_textView);
				holder.helper_main_img = (ImageView) convertView
						.findViewById(R.id.helper_main_imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			/** ͨ��holder����ȡxmL������� */
			holder.helper_main_img.setImageBitmap(getRes(data.get(position)
					.get("img").toString()));
			holder.helper_main_tv.setText(data.get(position).get("qymc")
					.toString());
			return convertView;
		}

		/** ����һ��ViewHolder�������Ż�View */
		class ViewHolder {
			ImageView helper_main_img;
			TextView helper_main_tv;
		}

		/** Description: ��ȡ�б��ͼƬ */
		public Bitmap getRes(String name) {
			ApplicationInfo appInfo = context.getApplicationInfo();
			int resID = context.getResources().getIdentifier(name, "drawable",
					appInfo.packageName);
			return BitmapFactory.decodeResource(context.getResources(), resID);
		}

	}
}
