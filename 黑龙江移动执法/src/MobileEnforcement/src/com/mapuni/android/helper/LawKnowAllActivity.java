package com.mapuni.android.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.FileHelper;

/**
 * FileName: SiteRecordActivity.java Description:现场笔录 需要参数任务编号
 * 
 * @author 钟学梅
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-08-01 下午14:48:40
 */
public class LawKnowAllActivity extends BaseActivity {
	// 父页面布局
	private RelativeLayout fatherRelativeLayout;
	private LinearLayout middleLayout;

	/**
	 * 任务编号
	 */
	private String taskID = "";
	private ListView mListView;
	private ArrayList<HashMap<String, Object>> dataList;
	private MyListViewAdapter adapter;
	private final String BaseWord = Global.SDCARD_RASK_DATA_PATH + "fj/ZFBST";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 屏幕状态
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.ui_mapuni);

		fatherRelativeLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		SetBaseStyle(fatherRelativeLayout, "执法百事通");
		setTitleLayoutVisiable(true);

		taskID = getIntent().getStringExtra("taskid");
		mListView = new ListView(this);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		dataList = getData();
		adapter = new MyListViewAdapter(this, dataList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 1.创建文件夹、复制文件
				String lawfilePath = Global.LAWKNOWALL_PATH;
				String taskPath = BaseWord + taskID + "/RaskFile";
				String fileName = (String) dataList.get(position).get("name");
				File sourcefile = new File(lawfilePath);
				File taskfile = new File(taskPath);
				if (!sourcefile.exists()) {
					sourcefile.mkdirs();
				}
				if (!taskfile.exists()) {
					taskfile.mkdirs();
				}
				String sourceFilePath = lawfilePath + "/" + fileName + ".doc";
				String targetFilePath = taskPath + "/" + fileName + ".doc";
				File sourceFile = new File(sourceFilePath);
				File targetFile = new File(targetFilePath);

				if (targetFile.exists()) {
					// 文件存在，直接打开
					DisplayUitl.openfile(targetFilePath,
							LawKnowAllActivity.this);
					// 3.打开后做标记
					adapter.notifyDataSetChanged();
					return;
				}
				// 文件不存在，则先复制，再打开
				try {
					FileHelper.copyFile(sourceFile, targetFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 2.打开文件
				if (targetFile.exists()) {
					DisplayUitl.openfile(targetFilePath,
							LawKnowAllActivity.this);
					// 3.打开后做标记
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(LawKnowAllActivity.this, "对不起，文件不存在！", 0)
							.show();
				}
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String taskPath = BaseWord + taskID;
				final String fileName = (String) dataList.get(arg2).get("name");
				final String targetFilePath = taskPath + "/RaskFile/"
						+ fileName + ".doc";

				File file = new File(targetFilePath);
				if (!file.exists()) {
					Toast.makeText(LawKnowAllActivity.this, "文档没有添加",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				String[] items = { "文档打印", "取消修改" };
				AlertDialog.Builder ab = new AlertDialog.Builder(
						LawKnowAllActivity.this);
				ab.setItems(items, new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						switch (arg1) {
						case 0:
							Intent it = new Intent();

							String str1 = "file://" + targetFilePath;
							System.out.println(str1);
							Uri localUri = Uri.parse(str1);
							String str3 = "application/msword";
							it.setClassName("com.dynamixsoftware.printershare",
									"com.dynamixsoftware.printershare.ActivityPrintDocuments");
							it.setDataAndType(localUri, str3);
							startActivity(it);
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

		middleLayout.addView(mListView);
	}

	/**
	 * FileName: SiteWriteRecordActivity.java Description:现场笔录列表适配器
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 *            Create at: 2012-12-4 上午11:16:12
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
			// 判断文件是否存在，存在则标记
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
	 * 获取数据
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("id", "0");
		map0.put("name", "目录");
		data.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", "1");
		map1.put("name", "环境监察执法实务手册");
		data.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", "2");
		map2.put("name", "环境监察总队工作简介");
		data.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("id", "3");
		map3.put("name", "规范环境行政处罚自由裁量权");
		data.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("id", "4");
		map4.put("name", "规章");
		data.add(map4);

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("id", "5");
		map5.put("name", "普查条例");
		data.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("id", "6");
		map6.put("name", "序");
		data.add(map6);

		return data;
	}

}
