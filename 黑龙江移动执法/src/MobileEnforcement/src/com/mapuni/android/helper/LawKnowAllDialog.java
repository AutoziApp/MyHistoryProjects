package com.mapuni.android.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.FileHelper;

public class LawKnowAllDialog extends Dialog implements DialogInterface {

	private final String TAG = "HelperDialog";

	// private HelperController HelperC;
	private View view;
	private final String taskID = "";
	private ListView lowknow_list;
	private ArrayList<HashMap<String, Object>> dataList;
	private MyListViewAdapter adapter;
	private final String BaseWord = Global.SDCARD_RASK_DATA_PATH + "fj/ZFBST";

	public LawKnowAllDialog(Context context) {
		this(context, 0);
		// Dialog���Ի�������ȡ������
		this.setCanceledOnTouchOutside(true);
	}

	public LawKnowAllDialog(Context context, int theme) {
		super(context, theme);

	}

	protected LawKnowAllDialog(Context context, boolean cancelable,
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
		if (view == null) {
			/** ���ÿ��岻��ʾ������ */
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater factory = LayoutInflater.from(this.getContext());
			view = factory.inflate(R.layout.lawknownew, null);
			lowknow_list = (ListView) view.findViewById(R.id.lowknow_list);
			dataList = getData();
			adapter = new MyListViewAdapter(this.getContext(), dataList);
			lowknow_list.setAdapter(adapter);
			lowknow_list
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							// 1.�����ļ��С������ļ�
							String lawfilePath = Global.LAWKNOWALL_PATH;
							String taskPath = BaseWord + taskID + "/RaskFile";
							String fileName = (String) dataList.get(position)
									.get("name");
							File sourcefile = new File(lawfilePath);
							File taskfile = new File(taskPath);
							if (!sourcefile.exists()) {
								sourcefile.mkdirs();
							}
							if (!taskfile.exists()) {
								taskfile.mkdirs();
							}
							String sourceFilePath = lawfilePath + "/"
									+ fileName + ".doc";
							String targetFilePath = taskPath + "/" + fileName
									+ ".doc";
							File sourceFile = new File(sourceFilePath);
							File targetFile = new File(targetFilePath);

							if (targetFile.exists()) {
								// �ļ����ڣ�ֱ�Ӵ�
								// HelperC = new HelperController(getContext());
								HelperController
										.getInstance()
										.hideDialog(
												HelperController.HANDLE_HIDE_LawKnowAllDialog);
								DisplayUitl.openfile(targetFilePath,
										getContext());
								// 3.�򿪺������
								adapter.notifyDataSetChanged();
								return;
							}
							// �ļ������ڣ����ȸ��ƣ��ٴ�
							try {
								// HelperC = new HelperController(getContext());
								HelperController
										.getInstance()
										.hideDialog(
												HelperController.HANDLE_HIDE_LawKnowAllDialog);
								FileHelper.copyFile(sourceFile, targetFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
							// 2.���ļ�
							if (targetFile.exists()) {
								// HelperC = new HelperController(getContext());
								HelperController
										.getInstance()
										.hideDialog(
												HelperController.HANDLE_HIDE_LawKnowAllDialog);
								DisplayUitl.openfile(targetFilePath,
										getContext());
								// 3.�򿪺������
								adapter.notifyDataSetChanged();
							} else {
								Toast.makeText(getContext(), "�Բ����ļ������ڣ�", 0)
										.show();
							}
						}
					});
			lowknow_list
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							String taskPath = BaseWord + taskID;
							final String fileName = (String) dataList.get(arg2)
									.get("name");
							final String targetFilePath = taskPath
									+ "/RaskFile/" + fileName + ".doc";

							File file = new File(targetFilePath);
							if (!file.exists()) {
								Toast.makeText(getContext(), "�ĵ�û�����",
										Toast.LENGTH_SHORT).show();
								return true;
							}

							String[] items = { "�ĵ���ӡ", "ȡ���޸�" };
							AlertDialog.Builder ab = new AlertDialog.Builder(
									getContext());
							ab.setItems(items, new OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									switch (arg1) {
									case 0:
										Intent it = new Intent();

										String str1 = "file://"
												+ targetFilePath;
										System.out.println(str1);
										Uri localUri = Uri.parse(str1);
										String str3 = "application/msword";
										it.setClassName(
												"com.dynamixsoftware.printershare",
												"com.dynamixsoftware.printershare.ActivityPrintDocuments");
										it.setDataAndType(localUri, str3);
										// startActivity(it);
										break;
									case 1:
										File file = new File(targetFilePath);
										file.delete();
										adapter.notifyDataSetChanged();
										break;

									default:
										break;
									}

								}
							});

							ab.show();

							return false;
						}
					});
			super.setContentView(view);
			// super.setTitle("ִ������ͨ");
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
		super.cancel();
	}

	/**
	 * ������
	 */
	private class MyListViewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		Context context;

		public MyListViewAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			LinearLayout layout = (LinearLayout) mInflater.inflate(
					R.layout.listitem, null);

			ImageView headImageView = (ImageView) layout
					.findViewById(R.id.listitem_left_image);
			headImageView.setImageResource(R.drawable.base_icon_bst);

			TextView mTextView = (TextView) layout
					.findViewById(R.id.listitem_text);
			String fileName = (String) data.get(position).get("name");
			mTextView.setText(fileName);
			// �ж��ļ��Ƿ���ڣ���������
			String taskPath = BaseWord + taskID;
			String targetFilePath = taskPath + "/RaskFile/" + fileName + ".doc";
			if (new File(targetFilePath).exists()) {
				headImageView.setImageResource(R.drawable.icon_left_checked);
			}

			ImageView tailImageView = (ImageView) layout
					.findViewById(R.id.listitem_image);
			tailImageView.setImageResource(R.drawable.icon_arrow_yellow);

			return layout;
		}
	}

	/**
	 * ��ȡ����
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("id", "0");
		map0.put("name", "Ŀ¼");
		data.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", "1");
		map1.put("name", "�������ִ��ʵ���ֲ�");
		data.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", "2");
		map2.put("name", "��������ܶӹ������");
		data.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("id", "3");
		map3.put("name", "�淶���������������ɲ���Ȩ");
		data.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("id", "4");
		map4.put("name", "����");
		data.add(map4);

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("id", "5");
		map5.put("name", "�ղ�����");
		data.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("id", "6");
		map6.put("name", "��");
		data.add(map6);

		return data;
	}

}
