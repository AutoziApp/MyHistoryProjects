package com.mapuni.android.uitl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;

public class SumsActivity extends BaseActivity {
	ListView listView;
	TextView Text;
	TableAdapter adapter;
	List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> listData2;
	Button button, button_sum;
	CheckBox box;
	CalculatorPaiwufei calculatorPaiwufei;
	/** 判断是否清除数据 */
	private boolean isClear = false;
	private Intent intent;
	double a1, a2, a3, s1, s2;
	DecimalFormat df2 = new DecimalFormat("###0.0");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listactivity_main);
		listView = (ListView) findViewById(R.id.listview011);
		Text = (TextView) findViewById(R.id.edit_sum);
		button_sum = (Button) findViewById(R.id.button_sum);
		button = (Button) findViewById(R.id.c_button);
		box = (CheckBox) findViewById(R.id.chekbox_01);
		Text.setEnabled(false);
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		listData2 = new ArrayList<Map<String, Object>>();
		final List<Map<String, Object>> listData = (List) bundle.getSerializable("data");
		listData2.addAll(listData);
		adapter = new TableAdapter(this, listData2);
		listView.setAdapter(adapter);
		Collections.sort(listData2, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
				float fileName1 = Float.valueOf((String) lhs.get("temp9"));
				float fileName2 = Float.valueOf((String) rhs.get("temp9"));

				return (int) (fileName2 - fileName1);
			}
		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isClear = true;
				s1 = 0;
				listData2.clear();
				Text.setText("");
				adapter.updateData(listData2);

			}
		});

		button_sum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//修改总计收费金额   byk
				if (s1 == 0) {
					Text.setText("" + s1);
				} else {
					Text.setText("" + df2.format(s1));
				}
//				double temp=0;
//			for (int i = 0; i < listData2.size(); i++) {
//				String temp9 = listData2.get(i).get("temp9").toString();
//				temp += Double.parseDouble(temp9);
//			}
//			Text.setText("" + temp);
			
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1, final int arg2, long arg3) {
				isClear = true;

				AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(SumsActivity.this);
				deleteBuilder.setTitle("删除");
				deleteBuilder.setIcon(R.drawable.icon_delete);
				deleteBuilder.setMessage("你确定要删除吗");
				deleteBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						box = (CheckBox) arg1.findViewById(R.id.chekbox_01);
						if (box.isChecked()) {

							s2 = Double.parseDouble(listData2.get(arg2).get("temp9").toString());
							s1 = s1 - s2;
						}

						listData2.remove(arg2);
						adapter.updateData(listData2);

					}
				});

				deleteBuilder.setNegativeButton("取消", null);
				AlertDialog ad = deleteBuilder.create();
				ad.show();
				return false;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				box = (CheckBox) arg1.findViewById(R.id.chekbox_01);
				if (!box.isChecked()) {
					box.setChecked(true);
					s1 += Double.parseDouble(listData2.get(arg2).get("temp9").toString());
				} else {
					box.setChecked(false);
					s1 -= Double.parseDouble(listData2.get(arg2).get("temp9").toString());
				}
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		s1 = 0;
		Intent intent = new Intent();
		intent.putExtra("IS_CLEAR", isClear);
		setResult(0, intent);
		return super.onKeyDown(keyCode, event);

	}
}
