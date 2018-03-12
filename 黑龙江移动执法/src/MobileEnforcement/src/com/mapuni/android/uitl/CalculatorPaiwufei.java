package com.mapuni.android.uitl;

import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: CalculatorActivity.java Description:计算器功能界面
 * 
 * @author 张东智
 * @Version 1.0
 * @Copyright 中科宇图天下科技有限公司<br>
 *            Create at: 2013-11-15 14:09:09
 */
public class CalculatorPaiwufei extends BaseActivity {
	private Spinner pyfspinner, wryspinner, jtspinner;
	private EditText editText_98, editText_99, editText_100, editText_111, editText_555, editText_666, editText_222, editText_333, editText_444;
	private EditText editText_123, editText_124, editText_125, editText_126, editText_140, editText_141, editText_143, editText_127;
	private Spinner spinner_144, spinner_128, spinner_129, spinner_130, spinner_21, spinner_142;
	private TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv15, tv16, tv17, tv18, tv19, tv20;
	private LinearLayout middleLayout;
	private String[] str1 = { "废水排污", "废气排污" };
	private ArrayAdapter<String> adapter_1, adapter_2, adapter_3, adapter_4, adapter_5, adapter_6, adapter_7;
	private String[] str4 = { "自动监控数据", "监督性监测", "物料衡算法", "燃料燃烧过程污染物" };
	private String[] str2 = { "第一类水污染物", "第二类水污染物", "PH值、大肠菌群数、余氯量", "色度", "畜禽养殖业、小型企业和第三产业" };
	private String[] str3 = { "first_class_wrw_values.xml", "forth_class_wrw_values.xml", "second_class_wrw_values.xml", "five_class_wrw_values.xml", "thrid_class_wrw_values.xml",
			"six_class_wrw_values.xml", "seven_class_wrw_values.xml", "eight_class_wrw_values.xml", "ninth_class_wrw_values.xml", "ten_class_wrw_values.xml",
			"eleventh_class_wrw_values.xml", "twelve_class_wrw_values.xml" };
	private List<String> pollutionType_3;
	private ArrayList<HashMap<String, Object>> list;// 从xml中读取的数据源
	private double equivalent = 0.0, equivalent1 = 0.0, equivalent2 = 0.0, equivalent3 = 0.0, equivalent4 = 0.0, equivalent5 = 0.0, equivalent7 = 0.0, equivalent8 = 0.0,
			equivalent9 = 0.0;// 分别为当量值,单位价格,污染物倍数
	private double chargeArray[] = { 0.70, 1.20 };// 用于存放单位价格
	private String currentSelectValue, currentSelectValue1, currentSelectValue2, currentSelectValue3;// 当前选择的污染物名称
	private Button sum_button, collect_button;
	private List<Map<String, Object>> listData = listData = new ArrayList<Map<String, Object>>();
	private double temp0, temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp15, temp16, temp17, temp18, temp19, temp20, temp21,
			temp22, temp23;
	private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
	private TextView text1, text2, text3, text4, text5, text6, text7, text8;
	DecimalFormat df2 = new DecimalFormat("###.0000000");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(0, 0, 0, 0);
		middleLayout.addView(oncre());
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		SetBaseStyle(titleLayout, "排污费计算");// 设置BaseActivity的标题才可以显示标题栏
		currentSelectValue2 = "燃料燃烧过程污染物";
	}

	private View oncre() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.calculator_paiwufei, null);
		pyfspinner = (Spinner) view.findViewById(R.id.arithmetic_spinner_1);
		wryspinner = (Spinner) view.findViewById(R.id.arithmetic_spinner_2);
		jtspinner = (Spinner) view.findViewById(R.id.arithmetic_spinner_3);
		text1 = (TextView) view.findViewById(R.id.unit_tv1);
		text2 = (TextView) view.findViewById(R.id.unit_tv2);
		text3 = (TextView) view.findViewById(R.id.unit_tv3);
		text4 = (TextView) view.findViewById(R.id.unit_tv4);
		text5 = (TextView) view.findViewById(R.id.unit_tv9);
		textView1 = (TextView) view.findViewById(R.id.unitHint_kilogram1);
		textView2 = (TextView) view.findViewById(R.id.unitHint_kilogram2);
		textView3 = (TextView) view.findViewById(R.id.unitHint_kilogram3);
		textView4 = (TextView) view.findViewById(R.id.unitHint_kilogram4);
		textView5 = (TextView) view.findViewById(R.id.unitHint_kilogram5);
		textView6 = (TextView) view.findViewById(R.id.unitHint_kilogram8);
		textView7 = (TextView) view.findViewById(R.id.unitHint_kilogram9);
		editText_98 = (EditText) view.findViewById(R.id.editText_98);
		editText_99 = (EditText) view.findViewById(R.id.editText_99);
		editText_100 = (EditText) view.findViewById(R.id.editText_100);
		editText_111 = (EditText) view.findViewById(R.id.editText_111);
		editText_555 = (EditText) view.findViewById(R.id.editText_555);
		editText_666 = (EditText) view.findViewById(R.id.editText_666);
		editText_222 = (EditText) view.findViewById(R.id.editText_222);
		editText_333 = (EditText) view.findViewById(R.id.editText_333);
		editText_444 = (EditText) view.findViewById(R.id.editText_444);

		editText_123 = (EditText) view.findViewById(R.id.editText_123);
		editText_124 = (EditText) view.findViewById(R.id.editText_124);
		editText_125 = (EditText) view.findViewById(R.id.editText_125);
		editText_126 = (EditText) view.findViewById(R.id.editText_126);
		editText_140 = (EditText) view.findViewById(R.id.editText_140);
		editText_141 = (EditText) view.findViewById(R.id.editText_141);
		spinner_142 = (Spinner) view.findViewById(R.id.editText_142);
		editText_143 = (EditText) view.findViewById(R.id.editText_143);
		editText_127 = (EditText) view.findViewById(R.id.editText_127);
		spinner_144 = (Spinner) view.findViewById(R.id.editText_144);
		spinner_128 = (Spinner) view.findViewById(R.id.editText_128);
		spinner_129 = (Spinner) view.findViewById(R.id.editText_129);
		spinner_130 = (Spinner) view.findViewById(R.id.editText_130);
		spinner_21 = (Spinner) view.findViewById(R.id.arithmetic_spinner_21);
		tv0 = (TextView) view.findViewById(R.id.arithmetic_name_21);
		tv1 = (TextView) view.findViewById(R.id.unitHint_kilogram11);
		tv2 = (TextView) view.findViewById(R.id.unitHint_kilogram12);
		tv3 = (TextView) view.findViewById(R.id.unitHint_kilogram13);
		tv4 = (TextView) view.findViewById(R.id.unitHint_kilogram14);
		tv5 = (TextView) view.findViewById(R.id.unitHint_kilogram20);
		tv6 = (TextView) view.findViewById(R.id.unitHint_kilogram21);
		tv7 = (TextView) view.findViewById(R.id.unitHint_kilogram22);
		tv8 = (TextView) view.findViewById(R.id.unitHint_kilogram23);
		tv9 = (TextView) view.findViewById(R.id.unitHint_kilogram24);
		tv10 = (TextView) view.findViewById(R.id.unitHint_kilogram15);
		tv11 = (TextView) view.findViewById(R.id.unitHint_kilogram16);
		tv12 = (TextView) view.findViewById(R.id.unitHint_kilogram17);
		tv13 = (TextView) view.findViewById(R.id.unitHint_kilogram18);
		tv15 = (TextView) view.findViewById(R.id.arithmetic_name_2);
		tv16 = (TextView) view.findViewById(R.id.unit_tv123);
		tv17 = (TextView) view.findViewById(R.id.unit_tv124);
		tv18 = (TextView) view.findViewById(R.id.unit_tv125);
		tv19 = (TextView) view.findViewById(R.id.unit_tv126);
		tv20 = (TextView) view.findViewById(R.id.unit_tv127);

		// box = (CheckBox) view.findViewById(R.id.unit_tv8);
		sum_button = (Button) view.findViewById(R.id.sum_button);
		collect_button = (Button) view.findViewById(R.id.collect_button);
		adapter_1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str1);
		adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pyfspinner.setAdapter(adapter_1);

		sum_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				append(listData);

			}
		});
		collect_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(CalculatorPaiwufei.this, SumsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", (Serializable) listData);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);

			}
		});

		editText_98.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp0 = Double.parseDouble(s.toString());
				if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
					temp3 = temp0 * temp1 * temp2 / 1000;
					editText_111.setText("" + temp3);
				}

			}
		});
		editText_99.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp1 = Double.parseDouble(s.toString());
				if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
					temp3 = temp0 * temp1 * temp2 / 1000;
					editText_111.setText("" + temp3);

				}
			}
		});
		editText_100.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp2 = Double.parseDouble(s.toString());
				if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
					temp3 = temp0 * temp1 * temp2 / 1000;
					editText_111.setText("" + temp3);

				}

			}

		});
		editText_111.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp15 = Double.parseDouble(s.toString());
				if (pyfspinner.getSelectedItem().toString().equals("废水排污")) {
					if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
						temp3 = temp0 * temp1 * temp2 / 1000;
						temp4 = temp3 / equivalent;
						editText_222.setText("" + temp4);
					}
				} else {
					updataMainList(5);

					if (temp15 != 0) {
						editText_222.setText("" + temp15 / equivalent7);
						editText_444.setText("" + temp15 / equivalent7 * temp6);
					}
				}
			}
		});
		editText_333.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp6 = Double.parseDouble(s.toString());
				if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
					temp3 = temp0 * temp1 * temp2 / 1000;
					temp4 = temp3 / equivalent;
					temp5 = temp6 * temp4;
					editText_444.setText("" + temp5);
				} else {
					if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
						temp3 = temp0 * temp1 * temp2 / 1000;
						temp6 = (Double) 0.7;
						temp4 = temp3 / equivalent;
						temp5 = temp6 * temp4;
						editText_444.setText("" + temp5);
					} else if (temp6 != 0 && temp13 != 0) {
						editText_444.setText("" + temp6 * temp13);
					}
				}

			}
		});
		editText_222.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp13 = Double.parseDouble(s.toString());
				if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
					temp3 = temp0 * temp1 * temp2 / 1000;
					temp6 = (Double) 0.7;
					temp4 = temp3 / equivalent;
					temp5 = temp6 * temp4;
					editText_444.setText("" + temp5);
				} else {
					if (temp0 != 0 && temp1 != 0 && temp2 != 0) {
						temp3 = temp0 * temp1 * temp2 / 1000;
						temp4 = temp3 / equivalent;
						temp5 = temp6 * temp4;
						editText_444.setText("" + temp5);
					} else {
						if (temp8 != 0) {
							temp8 = temp7 / equivalent;
							temp6 = (Double) 0.7;
							temp11 = temp8 * temp6;
							editText_444.setText("" + temp11);
						} else {
							if (temp8 != 0) {
								temp8 = temp7 / equivalent;
								temp11 = temp8 * temp6;
								editText_444.setText("" + temp11);
							} else {
								equivalent = 5.0;
								if (temp9 != 0 && temp7 != 0 && temp10 != 0) {
									temp10 = temp7 * temp9 / equivalent;
									temp6 = (Double) 0.7;
									temp12 = temp6 * temp10;
									editText_444.setText("" + temp10);
								} else {
									equivalent = 5.0;
									if (temp9 != 0 && temp7 != 0 && temp10 != 0) {
										temp10 = temp7 * temp9 / equivalent;
										temp12 = temp6 * temp10;
										editText_444.setText("" + temp10);
									} else if (temp6 != 0 && temp13 != 0) {
										editText_444.setText("" + temp6 * temp13);
									} else if (temp15 != 0) {
										temp6 = (Double) 0.7;
										editText_444.setText("" + temp15 / equivalent7 * temp6);
									} else if (temp15 != 0) {
										editText_444.setText("" + temp15 / equivalent7 * temp6);
									} else if (temp16 != 0 && temp17 != 0 && temp18 != 0) {
										editText_444.setText("" + temp16 * temp17 * temp18 * 0.000001 * temp6 / equivalent8);
									} else if (temp16 != 0 && temp17 != 0 && temp18 != 0) {
										temp6 = (Double) 0.7;
										editText_444.setText("" + temp16 * temp17 * temp18 * 0.000001 * temp6 / equivalent8);
									} else if (temp19 != 0 && temp20 != 0) {
										editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
									} else if (temp19 != 0 && temp20 != 0) {
										temp6 = (Double) 0.7;
										editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
									} else if (temp19 != 0 && temp21 != 0) {
										editText_444.setText("" + 1600 * temp21 / 100 * temp19 * temp6 / 0.95);
									} else if (temp19 != 0 && temp21 != 0) {
										temp6 = (Double) 0.7;
										editText_444.setText("" + 1600 * temp21 / 100 * temp19 * temp6 / 0.95);
									} else if (temp19 != 0 && temp22 != 0) {
										editText_444.setText("" + df2.format((1630 * temp19 * temp22 * equivalent4 + 0.000938) * temp6 / 0.95));
									} else if (temp19 != 0 && temp22 != 0) {
										temp6 = (Double) 0.7;
										editText_444.setText("" + df2.format((1630 * temp19 * temp22 * equivalent4 + 0.000938) * temp6 / 0.95));
									} else if (temp23 != 0) {
										editText_444.setText("" + 2330 * temp23 * equivalent5 * temp6 / 16.7);
									} else if (temp23 != 0) {
										temp6 = (Double) 0.7;
										editText_444.setText("" + 2330 * temp23 * equivalent5 * temp6 / 16.7);
									}
								}
							}
						}
					}
				}
			}
		});
		editText_123.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp16 = Double.parseDouble(s.toString());
				if (temp16 != 0 && temp17 != 0 && temp18 != 0) {
					updataMainList(5);
					editText_111.setText("" + temp16 * temp17 * temp18 * 0.000001);
					editText_444.setText("" + temp16 * temp17 * temp18 * 0.000001 * temp6 / equivalent8);
				}

			}
		});
		editText_124.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				updataMainList(5);
				temp17 = Double.parseDouble(s.toString());
				if (temp16 != 0 && temp17 != 0 && temp18 != 0) {
					editText_111.setText("" + temp16 * temp17 * temp18 * 0.000001);
					editText_444.setText("" + temp16 * temp17 * temp18 * 0.000001 * temp6 / equivalent8);
				}

			}
		});
		editText_125.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp18 = Double.parseDouble(s.toString());
				updataMainList(5);
				if (temp16 != 0 && temp17 != 0 && temp18 != 0) {
					editText_111.setText("" + temp16 * temp17 * temp18 * 0.000001);
					editText_444.setText("" + temp16 * temp17 * temp18 * 0.000001 * temp6 / equivalent8);
				}

			}
		});

		editText_555.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp7 = Double.parseDouble(s.toString());
				if (temp7 != 0) {
					temp8 = temp7 / equivalent;
					editText_222.setText("" + temp8);
				}
				if (temp8 != 0) {
					temp8 = temp7 / equivalent;
					temp6 = (Double) 0.7;
					temp11 = temp8 * temp6;
					editText_444.setText("" + temp11);
				} else {
					if (temp8 != 0) {
						temp8 = temp7 / equivalent;
						temp11 = temp8 * temp6;
						editText_444.setText("" + temp11);
					}
				}
			}
		});
		editText_666.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp9 = Double.parseDouble(s.toString());
				equivalent = .0;
				if (temp9 != 0 && temp7 != 0) {
					temp10 = temp7 * temp9 / equivalent;
					editText_222.setText("" + temp10);
				}
				equivalent = 5.0;
				if (temp9 != 0 && temp7 != 0 && temp10 != 0) {
					temp10 = temp7 * temp9 / equivalent;
					temp6 = (Double) 0.7;
					temp12 = temp6 * temp10;
					editText_444.setText("" + temp10);
				} else {
					equivalent = 5.0;
					if (temp9 != 0 && temp7 != 0 && temp10 != 0) {
						temp10 = temp7 * temp9 / equivalent;
						temp12 = temp6 * temp10;
						editText_444.setText("" + temp10);
					}
				}
			}
		});
		editText_126.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp19 = Double.parseDouble(s.toString());

				if (temp19 != 0 && temp20 != 0) {
					if (jtspinner.getSelectedItem().toString().equals("燃煤烟尘") || jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {

						editText_111.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3);
						editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
					}
				} else if (temp19 != 0 && temp21 != 0) {

					if (jtspinner.getSelectedItem().toString().equals("燃煤二氧化硫")) {

						editText_111.setText("" + 1600 * temp21 / 100 * temp19);
						editText_444.setText("" + 1600 * temp21 / 100 * temp19 * temp6 / 0.95);
					} else if (temp19 != 0 && temp22 != 0) {
						if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
							try {
								editText_111.setText("" + df2.format(1630 * temp19 * temp22 * equivalent4 + 0.000938));
								editText_444.setText("" + df2.format(((1630 * temp19 * temp22 * equivalent4) + 0.000938) / 0.95));

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}
					}
				}
			}
		});
		editText_127.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp20 = Double.parseDouble(s.toString());
				if (jtspinner.getSelectedItem().toString().equals("燃煤烟尘") || jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					equivalent = 2.18;
					if (temp19 != 0 && temp20 != 0) {

						editText_111.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3);
						editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
					}
				}
			}
		});
		editText_140.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;

				temp21 = Double.parseDouble(s.toString());

				if (temp19 != 0 && temp21 != 0) {
					if (jtspinner.getSelectedItem().toString().equals("燃煤二氧化硫")) {
						equivalent = 0.95;
						editText_111.setText("" + 1600 * temp21 / 100 * temp19);
						editText_444.setText("" + 1600 * temp21 / 100 * temp19 * temp6 / 0.95);
					}
				}
			}
		});
		editText_141.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString() == null || "".equals(s.toString().trim()))
					return;
				temp22 = Double.parseDouble(s.toString());
				if (temp19 != 0 && temp22 != 0) {
					if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
						equivalent = 0.95;
						try {
							editText_111.setText("" + df2.format(1630 * temp19 * temp22 * equivalent4 + 0.000938));
							editText_444.setText("" + df2.format((1630 * temp19 * temp22 * equivalent4 + 0.000938) / 0.95 * temp6));
						} catch (Exception e) {
							// TODO: handle exception\
							e.printStackTrace();
						}

					}
				}
			}
		});
		spinner_142.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				updataMainList(9);
				equivalent4 = Double.parseDouble(list.get(arg2).get("values").toString());
				if (temp19 != 0 && temp22 != 0) {
					if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
						equivalent = 0.95;
						try {
							editText_111.setText("" + df2.format(1630 * temp19 * temp22 * equivalent4 + 0.000938));
							editText_444.setText("" + df2.format((1630 * temp19 * temp22 * equivalent4 + 0.000938) / 0.95 * temp6));
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		editText_143.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString() == null || "".equals(s.toString().trim()))
					return;

				temp23 = Double.parseDouble(s.toString());
				if (temp23 != 0) {
					if (jtspinner.getSelectedItem().toString().equals("燃煤一氧化碳")) {
						equivalent = 16.7;
						editText_111.setText("" + 2330 * temp23 * equivalent5);
						editText_444.setText("" + 2330 * temp23 * equivalent5 / 16.7 * temp6);
					}
				}
			}
		});
		spinner_144.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				updataMainList(10);
				equivalent5 = Double.parseDouble(list.get(arg2).get("values").toString());
				if (temp23 != 0) {
					if (jtspinner.getSelectedItem().toString().equals("燃煤一氧化碳")) {
						equivalent = 16.7;
						editText_111.setText("" + 2330 * temp23 * equivalent5);
						editText_111.setText("" + 2330 * temp23 * equivalent5 / 16.7 * temp6);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinner_21.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				str4[3] = "燃料燃烧过程污染物";
				if (arg2 == 0 || arg2 == 2) {

					updataMainList(5);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
					textView4.setVisibility(View.VISIBLE);
					editText_111.setVisibility(View.VISIBLE);
					text4.setVisibility(View.VISIBLE);
					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv8.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					editText_555.setVisibility(View.GONE);
					textView6.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
				} else if (arg2 == 1) {
					tv1.setVisibility(View.VISIBLE);
					editText_123.setVisibility(View.VISIBLE);
					tv16.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.VISIBLE);
					editText_124.setVisibility(View.VISIBLE);
					tv17.setVisibility(View.VISIBLE);
					tv3.setVisibility(View.VISIBLE);
					editText_125.setVisibility(View.VISIBLE);
					tv18.setVisibility(View.VISIBLE);
					tv4.setVisibility(View.GONE);
					tv8.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					updataMainList(5);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
				} else if (arg2 == 3) {

					tv4.setVisibility(View.VISIBLE);
					editText_126.setVisibility(View.VISIBLE);
					tv19.setVisibility(View.VISIBLE);
					tv10.setVisibility(View.VISIBLE);
					editText_127.setVisibility(View.VISIBLE);
					tv11.setVisibility(View.VISIBLE);
					spinner_128.setVisibility(View.VISIBLE);
					tv12.setVisibility(View.VISIBLE);
					spinner_129.setVisibility(View.VISIBLE);
					tv13.setVisibility(View.VISIBLE);
					spinner_130.setVisibility(View.VISIBLE);
					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv8.setVisibility(View.GONE);

					updataMainList(11);
					adapter_4 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_4);

					updataMainList(6);
					adapter_5 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_128.setAdapter(adapter_5);

					updataMainList(7);
					adapter_6 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_129.setAdapter(adapter_6);

					updataMainList(8);
					adapter_7 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_130.setAdapter(adapter_7);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinner_128.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				updataMainList(6);
				equivalent1 = Double.parseDouble(list.get(arg2).get("values").toString());
				if (temp19 != 0 && temp20 != 0) {
					editText_111.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3);
				}
				if (jtspinner.getSelectedItem().toString().equals("燃煤烟尘") || jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					equivalent = 2.18;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤二氧化硫")) {
					equivalent = 0.95;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 0.95);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
					equivalent = 0.95;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 0.95);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤一氧化碳")) {
					equivalent = 16.7;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 16.7);
				} else if (jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 16.7);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		spinner_129.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				updataMainList(7);
				equivalent2 = Double.parseDouble(list.get(arg2).get("values").toString());
				if (temp19 != 0 && temp20 != 0) {
					editText_111.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3);

				}
				if (jtspinner.getSelectedItem().toString().equals("燃煤烟尘") || jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					equivalent = 2.18;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤二氧化硫")) {
					equivalent = 0.95;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 0.95);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
					equivalent = 0.95;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 0.95);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤一氧化碳")) {
					equivalent = 16.7;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 16.7);
				} else if (jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 16.7);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinner_130.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				updataMainList(8);
				equivalent3 = Double.parseDouble(list.get(arg2).get("values").toString());
				if (temp19 != 0 && temp20 != 0) {
					editText_111.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3);
				}
				if (jtspinner.getSelectedItem().toString().equals("燃煤烟尘") || jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					equivalent = 2.18;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 2.18);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤二氧化硫")) {
					equivalent = 0.95;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 0.95);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
					equivalent = 0.95;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 0.95);
				} else if (jtspinner.getSelectedItem().toString().equals("燃煤一氧化碳")) {
					equivalent = 16.7;
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 16.7);
				} else if (jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
					editText_444.setText("" + 1000 * temp19 * temp20 * equivalent1 * equivalent2 * equivalent3 * temp6 / 16.7);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		pyfspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					spinner_21.setVisibility(View.GONE);
					tv0.setVisibility(View.GONE);
					tv15.setVisibility(View.VISIBLE);
					editText_126.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					text5.setVisibility(View.GONE);

					wryspinner.setVisibility(View.VISIBLE);
					textView1.setVisibility(View.VISIBLE);
					editText_98.setVisibility(View.VISIBLE);
					text1.setVisibility(View.VISIBLE);
					textView2.setVisibility(View.VISIBLE);
					editText_99.setVisibility(View.VISIBLE);
					text2.setVisibility(View.VISIBLE);
					textView3.setVisibility(View.VISIBLE);
					editText_100.setVisibility(View.VISIBLE);
					text3.setVisibility(View.VISIBLE);

					adapter_2 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, str2);
					adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					wryspinner.setAdapter(adapter_2);
					editText_111.setText("");
					editText_222.setText("");
					editText_444.setText("");
				}

				else if (arg2 == 1) {
					tv0.setVisibility(View.VISIBLE);
					spinner_21.setVisibility(View.VISIBLE);
					tv15.setVisibility(View.GONE);
					wryspinner.setVisibility(View.GONE);
					textView1.setVisibility(View.GONE);
					editText_98.setVisibility(View.GONE);
					text1.setVisibility(View.GONE);
					textView2.setVisibility(View.GONE);
					editText_99.setVisibility(View.GONE);
					text2.setVisibility(View.GONE);
					textView3.setVisibility(View.GONE);
					editText_100.setVisibility(View.GONE);
					text3.setVisibility(View.GONE);

					adapter_4 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, str4);
					adapter_4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_21.setAdapter(adapter_4);
					editText_111.setText("");
					editText_222.setText("");
					editText_444.setText("");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		wryspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					updataMainList(0);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
					textView1.setVisibility(View.VISIBLE);
					textView2.setVisibility(View.VISIBLE);
					textView3.setVisibility(View.VISIBLE);
					textView4.setVisibility(View.VISIBLE);
					text1.setVisibility(View.VISIBLE);
					text2.setVisibility(View.VISIBLE);
					text3.setVisibility(View.VISIBLE);
					text4.setVisibility(View.VISIBLE);
					// box.setVisibility(View.VISIBLE);
					editText_98.setVisibility(View.VISIBLE);
					editText_99.setVisibility(View.VISIBLE);
					editText_100.setVisibility(View.VISIBLE);
					editText_111.setVisibility(View.VISIBLE);

					tv6.setVisibility(View.GONE);
					editText_141.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					tv7.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);

					text5.setVisibility(View.GONE);
					editText_555.setVisibility(View.GONE);
					textView6.setVisibility(View.GONE);
					textView7.setVisibility(View.GONE);
					editText_666.setVisibility(View.GONE);

					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);

				} else if (arg2 == 1) {
					updataMainList(1);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
					textView1.setVisibility(View.VISIBLE);
					textView2.setVisibility(View.VISIBLE);
					textView3.setVisibility(View.VISIBLE);
					textView4.setVisibility(View.VISIBLE);
					text1.setVisibility(View.VISIBLE);
					text2.setVisibility(View.VISIBLE);
					text3.setVisibility(View.VISIBLE);
					text4.setVisibility(View.VISIBLE);
					// box.setVisibility(View.VISIBLE);
					editText_98.setVisibility(View.VISIBLE);
					editText_99.setVisibility(View.VISIBLE);
					editText_100.setVisibility(View.VISIBLE);
					editText_111.setVisibility(View.VISIBLE);

					tv6.setVisibility(View.GONE);
					editText_141.setVisibility(View.GONE);
					tv7.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);

					text5.setVisibility(View.GONE);
					editText_555.setVisibility(View.GONE);
					textView6.setVisibility(View.GONE);
					textView7.setVisibility(View.GONE);
					editText_666.setVisibility(View.GONE);

					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);

				} else if (arg2 == 2) {
					updataMainList(2);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
					textView1.setVisibility(View.GONE);
					textView2.setVisibility(View.GONE);
					textView3.setVisibility(View.GONE);
					textView4.setVisibility(View.GONE);
					text1.setVisibility(View.GONE);
					text2.setVisibility(View.GONE);
					text3.setVisibility(View.GONE);
					text4.setVisibility(View.GONE);

					tv6.setVisibility(View.GONE);
					editText_141.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					tv7.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);

					editText_98.setVisibility(View.GONE);
					editText_99.setVisibility(View.GONE);
					editText_100.setVisibility(View.GONE);
					editText_111.setVisibility(View.GONE);
					text5.setVisibility(View.VISIBLE);
					editText_555.setVisibility(View.VISIBLE);
					textView6.setVisibility(View.VISIBLE);

					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);

				} else if (arg2 == 3) {
					updataMainList(3);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
					equivalent = 5.0;
					textView1.setVisibility(View.GONE);
					textView2.setVisibility(View.GONE);
					textView3.setVisibility(View.GONE);
					textView4.setVisibility(View.GONE);
					text1.setVisibility(View.GONE);
					text2.setVisibility(View.GONE);
					text3.setVisibility(View.GONE);
					text4.setVisibility(View.GONE);
					// box.setVisibility(View.GONE);
					editText_98.setVisibility(View.GONE);
					editText_99.setVisibility(View.GONE);
					editText_100.setVisibility(View.GONE);
					editText_111.setVisibility(View.GONE);
					text5.setVisibility(View.VISIBLE);
					editText_555.setVisibility(View.VISIBLE);
					textView6.setVisibility(View.VISIBLE);
					textView7.setVisibility(View.VISIBLE);
					editText_666.setVisibility(View.VISIBLE);

					tv6.setVisibility(View.GONE);
					editText_141.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					tv7.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);

					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);

				} else if (arg2 == 4) {
					updataMainList(4);
					adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
					adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jtspinner.setAdapter(adapter_3);
					textView1.setVisibility(View.VISIBLE);
					textView2.setVisibility(View.VISIBLE);
					textView3.setVisibility(View.VISIBLE);
					textView4.setVisibility(View.VISIBLE);
					text1.setVisibility(View.VISIBLE);
					text2.setVisibility(View.VISIBLE);
					text3.setVisibility(View.VISIBLE);
					text4.setVisibility(View.VISIBLE);
					// box.setVisibility(View.VISIBLE);
					editText_98.setVisibility(View.VISIBLE);
					editText_99.setVisibility(View.VISIBLE);
					editText_100.setVisibility(View.VISIBLE);
					editText_111.setVisibility(View.VISIBLE);
					text5.setVisibility(View.GONE);
					editText_555.setVisibility(View.GONE);
					textView6.setVisibility(View.GONE);
					textView7.setVisibility(View.GONE);
					editText_666.setVisibility(View.GONE);

					tv6.setVisibility(View.GONE);
					editText_141.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					tv7.setVisibility(View.GONE);
					spinner_142.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);

					tv1.setVisibility(View.GONE);
					editText_123.setVisibility(View.GONE);
					tv16.setVisibility(View.GONE);
					tv2.setVisibility(View.GONE);
					editText_124.setVisibility(View.GONE);
					tv17.setVisibility(View.GONE);
					tv3.setVisibility(View.GONE);
					editText_125.setVisibility(View.GONE);
					tv18.setVisibility(View.GONE);
					tv4.setVisibility(View.GONE);
					editText_126.setVisibility(View.GONE);
					tv19.setVisibility(View.GONE);
					tv10.setVisibility(View.GONE);
					editText_127.setVisibility(View.GONE);
					tv11.setVisibility(View.GONE);
					spinner_128.setVisibility(View.GONE);
					tv12.setVisibility(View.GONE);
					spinner_129.setVisibility(View.GONE);
					tv13.setVisibility(View.GONE);
					spinner_130.setVisibility(View.GONE);

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		jtspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (pyfspinner.getSelectedItem().toString().equals("废水排污")) {
					currentSelectValue = list.get(arg2).get("name").toString();
					equivalent = Double.parseDouble(list.get(arg2).get("values").toString());
				} else if (pyfspinner.getSelectedItem().toString().equals("废气排污")) {
					if (spinner_21.getSelectedItem().toString().equals("自动监控数据")) {
						updataMainList(5);
						currentSelectValue = list.get(arg2).get("name").toString();
						equivalent7 = Double.parseDouble(list.get(arg2).get("values").toString());
					} else if (spinner_21.getSelectedItem().toString().equals("监督性监测")) {
						updataMainList(5);
						currentSelectValue = list.get(arg2).get("name").toString();
						equivalent8 = Double.parseDouble(list.get(arg2).get("values").toString());
					} else if (spinner_21.getSelectedItem().toString().equals("物料衡算法")) {
						updataMainList(5);
						currentSelectValue = list.get(arg2).get("name").toString();
						equivalent7 = Double.parseDouble(list.get(arg2).get("values").toString());
					} else if (spinner_21.getSelectedItem().toString().equals("燃料燃烧过程污染物")) {
						updataMainList(11);
						currentSelectValue = list.get(arg2).get("name").toString();
						equivalent = Double.parseDouble(list.get(arg2).get("values").toString());

						if (jtspinner.getSelectedItem().toString().equals("燃煤烟尘")) {
							spinner_128.setVisibility(View.VISIBLE);
							spinner_129.setVisibility(View.VISIBLE);
							spinner_130.setVisibility(View.VISIBLE);
							tv11.setVisibility(View.VISIBLE);
							tv12.setVisibility(View.VISIBLE);
							tv13.setVisibility(View.VISIBLE);
							tv10.setVisibility(View.VISIBLE);
							editText_127.setVisibility(View.VISIBLE);
							tv4.setVisibility(View.VISIBLE);
							editText_126.setVisibility(View.VISIBLE);
							tv8.setVisibility(View.GONE);
							tv5.setVisibility(View.GONE);
							editText_140.setVisibility(View.GONE);
							tv20.setVisibility(View.GONE);
							tv6.setVisibility(View.GONE);
							editText_141.setVisibility(View.GONE);
							tv7.setVisibility(View.GONE);
							spinner_142.setVisibility(View.GONE);
							tv18.setVisibility(View.GONE);
							editText_143.setVisibility(View.GONE);
							tv9.setVisibility(View.GONE);
							spinner_144.setVisibility(View.GONE);

						} else if (jtspinner.getSelectedItem().toString().equals("燃煤二氧化硫")) {
							tv5.setVisibility(View.VISIBLE);
							editText_140.setVisibility(View.VISIBLE);
							tv20.setVisibility(View.VISIBLE);
							tv4.setVisibility(View.VISIBLE);
							editText_126.setVisibility(View.VISIBLE);
							editText_143.setVisibility(View.GONE);
							tv19.setVisibility(View.VISIBLE);
							tv10.setVisibility(View.GONE);
							tv8.setVisibility(View.GONE);
							editText_127.setVisibility(View.GONE);
							spinner_142.setVisibility(View.GONE);
							tv6.setVisibility(View.GONE);
							editText_141.setVisibility(View.GONE);
							tv7.setVisibility(View.GONE);
							spinner_142.setVisibility(View.GONE);
							spinner_128.setVisibility(View.GONE);
							spinner_129.setVisibility(View.GONE);
							spinner_130.setVisibility(View.GONE);
							tv11.setVisibility(View.GONE);
							tv12.setVisibility(View.GONE);
							tv13.setVisibility(View.GONE);
							tv18.setVisibility(View.GONE);
						} else if (jtspinner.getSelectedItem().toString().equals("燃煤氮氧化物")) {
							tv6.setVisibility(View.VISIBLE);
							editText_141.setVisibility(View.VISIBLE);
							tv7.setVisibility(View.VISIBLE);
							spinner_142.setVisibility(View.VISIBLE);
							tv19.setVisibility(View.VISIBLE);
							tv4.setVisibility(View.VISIBLE);
							editText_126.setVisibility(View.VISIBLE);
							tv5.setVisibility(View.GONE);
							editText_140.setVisibility(View.GONE);
							tv20.setVisibility(View.GONE);
							tv10.setVisibility(View.GONE);
							tv8.setVisibility(View.GONE);
							editText_143.setVisibility(View.GONE);
							tv9.setVisibility(View.GONE);
							spinner_144.setVisibility(View.GONE);
							editText_127.setVisibility(View.GONE);
							spinner_128.setVisibility(View.GONE);
							spinner_129.setVisibility(View.GONE);
							spinner_130.setVisibility(View.GONE);
							tv11.setVisibility(View.GONE);
							tv12.setVisibility(View.GONE);
							tv13.setVisibility(View.GONE);
							tv18.setVisibility(View.GONE);
							updataMainList(9);
							adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
							adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinner_142.setAdapter(adapter_3);
						} else if (jtspinner.getSelectedItem().toString().equals("燃煤一氧化碳")) {
							tv18.setVisibility(View.VISIBLE);
							editText_143.setVisibility(View.VISIBLE);
							tv9.setVisibility(View.VISIBLE);
							spinner_144.setVisibility(View.VISIBLE);
							tv8.setVisibility(View.VISIBLE);
							tv4.setVisibility(View.GONE);
							tv10.setVisibility(View.GONE);
							editText_127.setVisibility(View.GONE);
							editText_143.setVisibility(View.VISIBLE);
							tv19.setVisibility(View.GONE);
							updataMainList(10);
							adapter_3 = new ArrayAdapter<String>(CalculatorPaiwufei.this, android.R.layout.simple_spinner_item, pollutionType_3);
							adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinner_144.setAdapter(adapter_3);
							tv6.setVisibility(View.GONE);
							editText_141.setVisibility(View.GONE);
							spinner_142.setVisibility(View.GONE);
							editText_126.setVisibility(View.GONE);
							tv7.setVisibility(View.GONE);
							tv5.setVisibility(View.GONE);
							editText_140.setVisibility(View.GONE);
							tv20.setVisibility(View.GONE);
							spinner_128.setVisibility(View.GONE);
							spinner_129.setVisibility(View.GONE);
							spinner_130.setVisibility(View.GONE);
							tv11.setVisibility(View.GONE);
							tv12.setVisibility(View.GONE);
							tv13.setVisibility(View.GONE);
							tv18.setVisibility(View.GONE);
						} else if (jtspinner.getSelectedItem().toString().equals("焦炭一氧化碳")) {
							spinner_128.setVisibility(View.VISIBLE);
							spinner_129.setVisibility(View.VISIBLE);
							spinner_130.setVisibility(View.VISIBLE);
							tv11.setVisibility(View.VISIBLE);
							tv12.setVisibility(View.VISIBLE);
							tv13.setVisibility(View.VISIBLE);
							tv10.setVisibility(View.VISIBLE);
							editText_127.setVisibility(View.VISIBLE);
							tv4.setVisibility(View.VISIBLE);
							editText_126.setVisibility(View.VISIBLE);
							tv8.setVisibility(View.GONE);
							tv5.setVisibility(View.GONE);
							editText_140.setVisibility(View.GONE);
							tv20.setVisibility(View.GONE);
							tv6.setVisibility(View.GONE);
							editText_141.setVisibility(View.GONE);
							tv7.setVisibility(View.GONE);
							spinner_142.setVisibility(View.GONE);
							tv18.setVisibility(View.GONE);
							editText_143.setVisibility(View.GONE);
							tv9.setVisibility(View.GONE);
							spinner_144.setVisibility(View.GONE);

						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		return view;
	}

	private void updataMainList(int i) {
		pollutionType_3 = new ArrayList<String>();
		try {
			InputStream is = getResources().getAssets().open(str3[i]);
			list = XmlHelper.getDataFromXmlStream(is, "item");
			for (HashMap<String, Object> hashMap : list) {
				String temp = hashMap.get("name").toString();
				pollutionType_3.add(temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	public void append(List<Map<String, Object>> listData2) {
		if (editText_222.getText().toString() == null || editText_222.getText().toString().equals("") || editText_444.getText().toString() == null
				|| editText_444.getText().toString().equals("") || editText_111.getText().toString() == null || editText_111.getText().toString().equals("")) {
			OtherTools.showToast(CalculatorPaiwufei.this, "不允许排放量 、污染当量数、金额为空");
			return;
		}
		try {

			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("temp7", currentSelectValue);
			hashMap.put("temp8", editText_222.getText().toString());
			hashMap.put("temp9", editText_444.getText().toString());
			listData.add(hashMap);
			Toast.makeText(CalculatorPaiwufei.this, "数据已暂存", 2000).show();
		} catch (Exception e) {

		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		boolean isClear = arg2.getBooleanExtra("IS_CLEAR", false);
		if (isClear) {
			listData.clear();
		}
	}
}
