package com.mapuni.android.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.adapter.CityAdapter;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: CalculatorActivity.java Description:计算器功能界面 
 * <li>暂时弃用，希望后期能加入一些业务算法重新启用
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 *            Create at: 2012-12-4 下午04:04:07
 */
public class CalculatorActivity extends BaseActivity {
	/** 界面组件 */
	protected ScrollView scrollView = null;
	protected LinearLayout queryMidLayout = null;
	protected TableLayout queryTable = null;

	/** 全局集合数据 */
	protected ArrayList<HashMap<String, Object>> dataXMLList;
	protected ArrayList<HashMap<String, Object>> extraInfoForCount;

	protected int itemCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.ui_mapuni);
		RelativeLayout linearLayout = (RelativeLayout) this
				.findViewById(R.id.parentLayout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Intent it=this.getIntent();
		// bundle=it.getExtras();
		String titleText = "计算工具";
		super.SetBaseStyle(linearLayout, titleText);
		// boolean isShowTitle = bundle.getBoolean("IsShowTitle");
		// super.setTitleLayoutVisiable(isShowTitle);
		LinearLayout queryLayout = (LinearLayout) this
				.findViewById(R.id.topLayout);

		try {
			dataXMLList = (ArrayList<HashMap<String, Object>>) XmlHelper
					.getList(
							this.getResources().getAssets()
									.open("style_count.xml"), "item");
			TableLayout nameTable = new TableLayout(this);
			nameTable.setColumnStretchable(1, true);
			queryLayout.addView(nameTable);

			TableRow titleRow = new TableRow(this);
			nameTable.setBackgroundResource(R.drawable.bg_title_datasync);
			TextView textView = new TextView(this);
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText("算法名称:");
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			titleRow.addView(textView);

			Spinner spinner = new Spinner(this);

			// 逐行往 TableLayout 里填充控件
			TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightLayoutParams.setMargins(0, 0, 15, 0);
			List<String> name = new ArrayList<String>();
			for (HashMap<String, Object> dataMap : dataXMLList) {
				name.add(dataMap.get("sfmc").toString());
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, name);// spinner适配器
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			titleRow.addView(spinner);
			nameTable.addView(titleRow);

			// 建立 TableLayout, 将其同 Activity 绑定, 并设置基本属性

			queryMidLayout = (LinearLayout) CalculatorActivity.this
					.findViewById(R.id.middleLayout);
			queryMidLayout.invalidate();
			scrollView = new ScrollView(CalculatorActivity.this);
			queryMidLayout.addView(scrollView);
			queryTable = new TableLayout(CalculatorActivity.this);
			scrollView.addView(queryTable);
			queryTable.setColumnStretchable(1, true);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					try {

						// 用 StyleHandler 中的方法读取 style_count.xml, 得到建立布局所需要的
						// List
						extraInfoForCount = XmlHelper.getStyleByName(
								dataXMLList.get(arg2).get("name").toString(),
								CalculatorActivity.this.getResources()
										.getAssets().open("style_count.xml"));
						queryTable.removeAllViews();
						setCount(extraInfoForCount);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description:设置计算布局界面
	 * 
	 * @param data
	 * @author Administrator<br>
	 *         Create at: 2012-12-4 下午04:09:20
	 */
	public void setCount(ArrayList<HashMap<String, Object>> data) {
		itemCount = data.size();
		// 逐行往 TableLayout 里填充控件
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		for (int index = 0; index < itemCount; index++) {
			// 动态添加EditText和Spinner，需判断
			if (data.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name")
					|| data.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
							.equals("sfmc")) {
				continue;
			}
			// 遇到style的name字段不进行显示
			TableRow row = new TableRow(this);
			TextView textView = new TextView(this);
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(data.get(index).get(XmlHelper.QUERY_HINT)
					.toString());
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);

			if (data.get(index).get("style").equals("spinner")) {
				// 判断添加EditText还是Spinner
				Spinner spinner = new Spinner(this);

				spinner.setPrompt("--请选择--");
				spinner.setId(index);

				List<String> name = new ArrayList<String>();
				name.add("--请选择--");
				String querycondition = data.get(index)
						.get(XmlHelper.QUERY_EDIT_TEXT).toString();
				List<HashMap<String, Object>> adapterlist = getAdapterList(data
						.get(index).get("datasource").toString(),
						querycondition);
				if (adapterlist == null) {// 判断配置文件是否配置正确
					Toast.makeText(this, "Spinner配置文件不存在或配置错误",
							Toast.LENGTH_LONG).show();
					return;

				}
				for (Map<String, Object> map : adapterlist) {
					if (map.get(querycondition).toString().length() <= 0)
						continue;
					name.add(map.get(querycondition).toString());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, name);// spinner适配器
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
				row.addView(spinner, rightLayoutParams);
			} else if (data.get(index).get("style").equals("autocomplete")) {
				// 这里设置各个 EditText 的 Id, 读取数据构造 XPath 表达式时调用
				AutoCompleteTextView auto = new AutoCompleteTextView(this);
				// auto.setMaxEms(auto.getWidth());
				auto.setId(index);
				auto.setMaxEms(auto.getWidth());
				List<String> hz = new ArrayList<String>();
				List<String> psm = new ArrayList<String>();
				String querycondition = data.get(index)
						.get(XmlHelper.QUERY_EDIT_TEXT).toString();
				List<HashMap<String, Object>> autodata = getAdapterList(data
						.get(index).get("datasource").toString(),
						querycondition);
				for (Map<String, Object> map : autodata) {
					if (map.get(querycondition).toString().length() <= 0)
						continue;
					hz.add(map.get(querycondition).toString());
					psm.add(map.get(querycondition).toString()
							+ " "
							+ DisplayUitl.getPinYinFirstChar(map.get(
									querycondition).toString()));
				}
				CityAdapter<String> adapter = new CityAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, hz, psm);
				auto.setAdapter(adapter);// 最后调用方法将数据添加到自动补全控件中
				auto.setThreshold(1);
				row.addView(auto, rightLayoutParams);
			} else {
				EditText editText = new EditText(this);
				editText.setMaxEms(editText.getWidth());
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				editText.setId(index);
				row.addView(editText, rightLayoutParams);
			}
			queryTable.addView(row);
		}
		// 建立提交按钮及其监听器
		TableRow row = new TableRow(this);
		TextView textView = new TextView(this);
		textView.setText("");
		textView.setGravity(Gravity.RIGHT);
		textView.setPadding(15, 0, 0, 0);
		row.addView(textView);

		ImageView imgButton = new ImageView(this);
		imgButton.setImageBitmap(Global.getRes(this, "btn_search"));

		row.addView(imgButton);
		queryTable.addView(row);
		imgButton.setOnClickListener(new buttonToSubmitListener());
	}

	/**
	 * Description:获取用户输入的值
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午04:09:57
	 */
	public HashMap<String, Object> BuildQueryValue() {
		HashMap<String, Object> returnValueMap = new HashMap<String, Object>();

		// 因为表达式中需要使用条件并列的 and 运算符, 所以除了第一个条件之外, 每增加一个条件都需要在前面加" and ".
		// 用这个布尔值表示当前是否正在向表达式中写入第一个条件.
		for (int index = 0; index < itemCount; index++) {

			if (extraInfoForCount.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("name")
					|| extraInfoForCount.get(index)
							.get(XmlHelper.QUERY_EDIT_TEXT).equals("sfmc")) {
				continue;
			}
			boolean isSpinner = false;// 是否为spinner的判断标志
			Map<String, Object> stylecontent = extraInfoForCount.get(index);
			if (stylecontent.get("style").equals("spinner"))
				isSpinner = true;

			if (isSpinner) {
				Spinner sp = (Spinner) findViewById(index);
				if (sp.getSelectedItem() == null
						|| sp.getSelectedItem().toString().length() <= 0) {
					continue;
				} else {
					String conditionName = extraInfoForCount.get(index)
							.get(XmlHelper.QUERY_EDIT_TEXT).toString();
					String content = null;
					for (Map<String, Object> map : getAdapterList(
							extraInfoForCount.get(index).get("datasource")
									.toString(), extraInfoForCount.get(index)
									.get("queryEditText").toString())) {
						if (map.get(
								extraInfoForCount.get(index)
										.get("queryEditText").toString())
								.toString()
								.equals(sp.getSelectedItem().toString())) {

							content = map.get(
									extraInfoForCount.get(index)
											.get("queryEditText").toString())
									.toString();
							returnValueMap.put(conditionName, content);
							break;
						}
					}

				}
			} else if (stylecontent.get("style").equals("autocomplete")) {
				AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(index);
				if (auto.getText().toString().trim().length() <= 0)
					continue;
				else {
					String conditionName = extraInfoForCount.get(index)
							.get(XmlHelper.QUERY_EDIT_TEXT).toString();

					returnValueMap.put(conditionName, auto.getText().toString()
							.trim());
				}
			} else {
				EditText edittext = (EditText) findViewById(index);
				if (edittext.getText().toString().trim().length() <= 0)
					continue;
				else {
					String conditionName = extraInfoForCount.get(index)
							.get(XmlHelper.QUERY_EDIT_TEXT).toString();

					returnValueMap.put(conditionName, edittext.getText()
							.toString().trim());
				}
			}
		}
		return returnValueMap;
	}

	/**
	 * FileName: CalculatorActivity.java
	 * Description:计算按钮的单击监听事件
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司<br>
	 * Create at: 2012-12-4 下午04:10:18
	 */
	class buttonToSubmitListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			HashMap<String, Object> count = BuildQueryValue();
			TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightLayoutParams.setMargins(0, 0, 15, 0);
			if (extraInfoForCount.get(0).get("queryHint").toString()
					.equals("ZLSSCLSL")) {
				if (count.size() < 3) {
					Toast.makeText(CalculatorActivity.this, "输入错误无法计算",
							Toast.LENGTH_LONG).show();
					return;
				}
				Double zlssclsl = Double.parseDouble(count.get("xsclnl")
						.toString())
						* Double.parseDouble(count.get("ryxsj").toString())
						* Double.parseDouble(count.get("yxts").toString());
				TableRow row = new TableRow(CalculatorActivity.this);
				TextView textView = new TextView(CalculatorActivity.this);
				textView.setTextColor(android.graphics.Color.BLACK);
				textView.setText("处理水量：");
				textView.setGravity(Gravity.RIGHT);
				textView.setPadding(15, 0, 0, 0);
				row.addView(textView);
				EditText editText = new EditText(CalculatorActivity.this);
				editText.setMaxEms(editText.getWidth());
				editText.setText(String.valueOf(zlssclsl));
				editText.setFocusableInTouchMode(false);
				row.addView(editText, rightLayoutParams);
				queryTable.addView(row);
			}
			if (extraInfoForCount.get(0).get("queryHint").toString()
					.equals("WLHSF")) {
				if (count.size() < 3) {
					Toast.makeText(CalculatorActivity.this, "输入错误无法计算",
							Toast.LENGTH_LONG).show();
					return;
				}
				Double rlrseyhlpfl = Double.parseDouble(count.get("rlmxhl")
						.toString())
						* Double.parseDouble(count.get("hll").toString())
						* 0.8
						* 2
						* (1 - Double.parseDouble(count.get("tll").toString()));
				TableRow row = new TableRow(CalculatorActivity.this);
				TextView textView = new TextView(CalculatorActivity.this);
				textView.setTextColor(android.graphics.Color.BLACK);
				textView.setText("二氧化硫排放量：");
				textView.setGravity(Gravity.RIGHT);
				textView.setPadding(15, 0, 0, 0);
				row.addView(textView);
				EditText editText = new EditText(CalculatorActivity.this);
				editText.setMaxEms(editText.getWidth());
				editText.setText(String.valueOf(rlrseyhlpfl));
				editText.setFocusableInTouchMode(false);
				row.addView(editText, rightLayoutParams);
				queryTable.addView(row);
			}
			if (extraInfoForCount.get(0).get("queryHint").toString()
					.equals("CPWXSF")) {
				ArrayList<HashMap<String, Object>> cwxs = getCWXS(
						count.get("cpmc").toString(), count.get("ylmc")
								.toString(), count.get("gymc").toString(),
						count.get("gmdj").toString(), count.get("wrwzb")
								.toString());
				if (cwxs.size() <= 0 || count.size() < 6) {
					Toast.makeText(CalculatorActivity.this, "输入错误无法计算",
							Toast.LENGTH_LONG).show();
					return;
				}
				double cwl = Double.parseDouble(count.get("cpl").toString())
						* Double.parseDouble(cwxs.get(0).get("cwxs").toString());
				double pwl = cwl
						* Double.parseDouble(cwxs.get(0).get("pwxs").toString());
				TableRow row = new TableRow(CalculatorActivity.this);
				TextView textView = new TextView(CalculatorActivity.this);
				textView.setTextColor(android.graphics.Color.BLACK);
				textView.setText("产污量：");
				textView.setGravity(Gravity.RIGHT);
				textView.setPadding(15, 0, 0, 0);
				row.addView(textView);
				EditText editText = new EditText(CalculatorActivity.this);
				editText.setMaxEms(editText.getWidth());
				editText.setText(String.valueOf(cwl));
				editText.setFocusableInTouchMode(false);
				row.addView(editText, rightLayoutParams);
				queryTable.addView(row);

				TableRow.LayoutParams pwrightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				TableRow pwrow = new TableRow(CalculatorActivity.this);
				TextView pwtextView = new TextView(CalculatorActivity.this);
				pwtextView.setTextColor(android.graphics.Color.BLACK);
				pwtextView.setText("排污量：");
				pwtextView.setGravity(Gravity.RIGHT);
				pwtextView.setPadding(15, 0, 0, 0);
				pwrow.addView(pwtextView);
				EditText pweditText = new EditText(CalculatorActivity.this);
				pweditText.setMaxEms(editText.getWidth());
				pweditText.setText(String.valueOf(pwl));
				pweditText.setFocusableInTouchMode(false);
				pwrow.addView(pweditText, pwrightLayoutParams);
				queryTable.addView(pwrow);
			}
		}
	}

	/**
	 * Description:获取spinner和autocomplete数据
	 * @param table
	 * @param querycondition
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午04:10:46
	 */
	private List<HashMap<String, Object>> getAdapterList(String table,
			String querycondition) {
		List<HashMap<String, Object>> result = BaseClass.DBHelper.getList(
				table, querycondition);
		return result;
	}

	private ArrayList<HashMap<String, Object>> getCWXS(String cpmc,
			String ylmc, String gymc, String gmdj, String wrwzb) {
		try {
			ArrayList<HashMap<String, Object>> cwxslist = SqliteUtil
					.getInstance().queryBySqlReturnArrayListHashMap(
							"select * from Computing_equipment"
									+ " where cpmc='" + cpmc + "' and ylmc='"
									+ ylmc + "' and gymc='" + gymc
									+ "' and gmdj='" + gmdj + "' and wrwzb='"
									+ wrwzb + "'");
			return cwxslist;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return null;
	}
}
