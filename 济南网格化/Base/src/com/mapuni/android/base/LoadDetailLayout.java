/**
 * 
 */
package com.mapuni.android.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalchina.gallery.ImageGalleryActivity;
import com.mapuni.android.base.adapter.TreeViewAdapter;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.controls.listview.MyScrollView;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: DetailLayout Description:加载详情页面的布局
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-1-15 下午03:50:49
 */
public class LoadDetailLayout {
	private final Context context;
	private final IDetailed businessObj;
	private Location location;
	private GISLocation gislocation;
	private String qybm;
	private String attchUnit;
	private HashMap<Integer, String> hashmap = new HashMap<Integer, String>();

	TableLayout queryTable;
	TableLayout queryTable1;
	TableLayout queryTable2;
	/** 滚动视图 */
	MyScrollView scrollView;
	LinearLayout allLayout;
	LinearLayout treeLayout;

	TreeViewAdapter adapter;

	private ArrayList<HashMap<String, Object>> styleDetailed;
	private HashMap<String, Object> detaild;
	private View view;

	private final boolean isAmend;
    private static final String TAG = "LoadDetailLayout";
	public LoadDetailLayout(Context context, IDetailed businessObj, boolean isAmend) {
		this.context = context;
		this.businessObj = businessObj;
		this.isAmend = isAmend;
	}

	/**
	 * @return the styleDetailed 返回详情样式
	 */
	public ArrayList<HashMap<String, Object>> getStyleDetailed() {
		return styleDetailed;
	}

	/**
	 * @param styleDetailed
	 *            the styleDetailed to set
	 */
	public void setStyleDetailed(ArrayList<HashMap<String, Object>> styleDetailed) {
		this.styleDetailed = styleDetailed;
	}

	/**
	 * @return the detaild
	 */
	public HashMap<String, Object> getDetaild() {
		return detaild;
	}

	/**
	 * @param detaild
	 *            the detaild to set
	 */
	public void setDetaild(HashMap<String, Object> detaild) {
		this.detaild = detaild;
	}

	/**
	 * Description:加载详情页面的布局
	 * 
	 * @param itemId
	 *            当前对象的id
	 * @return MyScrollView 视图
	 * @author 王红娟 Create at: 2013-1-15 下午05:27:05
	 */
	public MyScrollView loadDetailed(String itemId) {
		queryTable = new TableLayout(context);
		/** 滚动视图 */
		scrollView = new MyScrollView(context);
		scrollView.addView(queryTable);
		queryTable.setColumnStretchable(1, true);
		/** 得到查询项目的数量 */
		addConent(itemId);
		return scrollView;
	}

	/**
	 * 加载带二级树的详情布局
	 * 
	 * @param itemId
	 * @param mGestureDetector
	 * @return
	 */
	public MyScrollView loadTreeDetailed(String itemId, ArrayList<HashMap<String, Object>> attachment, String filepath, Bundle RWBundle) {

		MyScrollView scrollView = loadlayout(itemId, attachment, filepath);
		addRWXXConent(itemId, RWBundle);
		return scrollView;
	}

	/**
	 * 加载详情页面的布局
	 * 
	 * @param itemId
	 * @param queryTable
	 */
	public void addConent(String itemId) {
		styleDetailed = businessObj.getStyleDetailed(context);

		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		/** 通过id查询出数据 */
		detaild = businessObj.getDetailed(itemId);
		int itemCount = styleDetailed.size();
		// if(36 == itemId.length() || 12 == itemId.length()){
		qybm = itemId;
		// }
		/** 逐行往 TableLayout 里填充控件 */
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			attchUnit = styleDetailed.get(index).get("queryEditText").toString();
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter == 0) {
				counter++;
				/** name属性不需要添加到列表 */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				/** mysql为自定义sql语句，此属性不需要添加到列表 */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			// textView.setMaxEms(5);

			/** 左侧 */
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			row.setGravity(Gravity.CENTER_VERTICAL);
			EditText editText = null;
			String value = null;
			/** 按钮 */
			if ("button".equals(map.get("style")) && !isAmend) {
				final Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("") || button.getText().toString().equals("null") || button.getText().toString() == null) {
					button.setText("下载附件");
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(button, rightLayoutParams);
				
				if ("blob".equals(map.get("type"))) {
					Global.getGlobalInstance().setItemId(itemId);
					button.setOnClickListener(new zhOnClick());
				} else if ("fsdx".equals(map.get("type"))) {
					button.setText("发送短信");
					Global.getGlobalInstance().setItemId(itemId);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String temp = "";
							if (detaild.get("chinesename") != null && !detaild.get("chinesename").toString().trim().equals("")) {
								temp = detaild.get("chinesename").toString().trim();
							}
							if (detaild.get("stabilityandrisk") != null && !detaild.get("stabilityandrisk").toString().trim().equals("")) {
								temp = temp + "," + detaild.get("stabilityandrisk").toString().trim();
							}
							if (detaild.get("emergencymonitor") != null && !detaild.get("emergencymonitor").toString().trim().equals("")) {
								temp = temp + "," + detaild.get("emergencymonitor").toString().trim();
							}
							if (detaild.get("emergencytreatment") != null && !detaild.get("emergencytreatment").toString().trim().equals("")) {
								temp = temp + "," + detaild.get("emergencytreatment").toString().trim();
							}

							Intent intent = new Intent();
							intent.putExtra("content", temp);
							intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.helper.FsdxActivity");
							context.startActivity(intent);

							// Intent sendIntent = new
							// Intent(Intent.ACTION_VIEW);
							// sendIntent.putExtra("sms_body", temp);
							// sendIntent.setType("vnd.android-dir/mms-sms");
							// context.startActivity(sendIntent);
						}
					});
				} else {
					hashmap.put(index, attchUnit);
					button.setOnClickListener(new OnClick() {
						public void onClick(View v) {
							int k = 0;
							int total = 0;
							String extra = "no";
							String attch;
							// ArrayList array = new ArrayList<String>();
							FileHelper fileHelper = new FileHelper();
							String attchDM = hashmap.get(button.getId()).toString();
							ArrayList<String> arrayTotal = new ArrayList<String>();
							if ("YYZZFBTP".equals(attchDM)) {
								attchDM = "YYZZ";
								HashMap<String, Object> hashmap = fileHelper.downFileByQyid(qybm, attchDM, context);
								ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
								extra = (String) hashmap.get("2");
								for (String str : array) {
									arrayTotal.add(str);
								}
								total = array.size();
								attch = attchDM;
							} else if ("XMSPDZDA".equals(attchDM) || "SSCSPDZDA".equals(attchDM) || "HBYSDZDA".equals(attchDM) || "ZYGY".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_HBSXLXQK where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("SCSSYXQK_SCGYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_SCSSYXQK where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("FSZLSSYXQK_GYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSZLSSXX where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("FQZLSSYXQK_GYDZDA".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FQZLSS where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("FQPWKQK_SB".equals(attchDM) || "FQPWKQK_YXQK".equals(attchDM) || "FQPWKQK_PFKQK".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FQPFKXX where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("estuarylongitude".equals(attchDM) || "estuarylatitude".equals(attchDM) || "sb".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKXX where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("FSPWKQK_SB".equals(attchDM) || "FSPWKQK_YXQK".equals(attchDM) || "FSPWKQK_PFKQK".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKXX where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else if ("estuarylongitude".equals(attchDM) || "estuarylatitude".equals(attchDM) || "sb".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKXX where  id = '" + qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(guidSql);

								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										HashMap<String, Object> hashmap = fileHelper.downFileByQyid(list.get(j).get("guid").toString(), attchDM, context);
										ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
										extra = (String) hashmap.get("2");
										for (String str : array) {
											arrayTotal.add(str);
										}
										total += array.size();
									}
								}
								total = arrayTotal.size();
								attch = attchDM;
							} else {
								HashMap<String, Object> hashmap = fileHelper.downFileByQyid(qybm, attchDM, context);
								ArrayList<String> array = (ArrayList<String>) hashmap.get("1");
								extra = (String) hashmap.get("2");
								for (String str : array) {
									arrayTotal.add(str);
								}
								total = array.size();
								attch = attchDM;
							}

							if (total > 0) {
								Intent intent = new Intent(context, ImageGalleryActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("arrayTotal", arrayTotal);
								bundle.putString("attch", attch);
								intent.putExtras(bundle);
								context.startActivity(intent);
							} else {
								if ("no".equals(extra)) {
									Toast.makeText(context, "暂无附件！", Toast.LENGTH_SHORT).show();
								}

							}
						}
					});

				}
			} else if ("call".equals(map.get("style")) && !isAmend) {
				final Button callbutton = new Button(context);

				callbutton.setMaxEms(callbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				callbutton.setId(index);
				String callStr = String.valueOf(detaild.get(value));
				callbutton.setText(callStr.equals("null") ? "" : callStr);
				callbutton.setGravity(Gravity.LEFT);
				callbutton.setGravity(Gravity.CENTER_VERTICAL);
				// button.setText("sdcard/mapuni/task/log.txt");
				row.addView(callbutton, rightLayoutParams);
				Log.e(TAG, "------------editext----------字段："+value+"----value:"+callStr);
				
				// callbutton.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// tocall(callbutton.getText().toString());
				// }
				// });

				callbutton.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						tocall(callbutton.getText().toString());
						return false;
					}
				});
				/** 邮件 */
			} else if ("email".equals(map.get("style")) && !isAmend) {
				final Button emailbutton = new Button(context);

				emailbutton.setMaxEms(emailbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				emailbutton.setId(index);
				String emailStr = String.valueOf(detaild.get(value));
				if (emailStr.equals(null) || emailStr == null || emailStr.equals("null")) {
					emailStr = "";
				}
				emailbutton.setText(emailStr);
				emailbutton.setGravity(Gravity.LEFT);
				emailbutton.setGravity(Gravity.CENTER_VERTICAL);
				row.addView(emailbutton, rightLayoutParams);
				emailbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String email = emailbutton.getText().toString();
						if (email.equals("")) {
							Toast.makeText(context, "邮箱地址为空，无法发送邮件", Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "邮箱地址不合法，无法发送邮件", Toast.LENGTH_SHORT).show();
							return;
						} else {
							toemail(email);
						}

					}
				});
				/** web页 */
			} else if ("webview".equals(map.get("style")) && !isAmend) {
				Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText("查看");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("") || otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(), "暂无更多信息!", Toast.LENGTH_SHORT).show();
							return;
						}
						it.putExtra("otherstr", otherstr);
						it.putExtra("title", "详细内容");
						context.startActivity(it);
					}
				});

			}
			/** 文字视图 */
			else if ("textview".equals(map.get("style"))) {
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				String textvalue = String.valueOf(detaild.get(value));
				TextView text = new TextView(context);
				text.setMaxEms(text.getWidth());
				text.setId(index);
				try {
					if(value.equals("dangersigns") && textvalue != null && !textvalue.equals("")){//危险标记
						String html = " ";
						if(textvalue != null &&  !"".equals(textvalue)){
							String images[] = textvalue.split(":");
						    String packageName = context.getPackageName();
							for (int i = 0; i < images.length; i++) {
								  html += "<img  src='" + context.getResources().getIdentifier("img_"+images[i], "drawable", packageName) + "'/>"; 
							}
	                    }
						  ImageGetter imgGetter = new ImageGetter() {  
					            @Override  
					            public Drawable getDrawable(String source) {  
					                int id = Integer.parseInt(source);
					                Drawable d = context.getResources().getDrawable(id);  
//					                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());  
					                d.setBounds(0, 0, 100, 100); 
					                return d;  
					            }  
					        };  
					        CharSequence charSequence = Html.fromHtml(html, imgGetter, null); 
					        text.setText(charSequence);
					}else {
						text.setText(String.valueOf(detaild.get(value)));
					}
					row.addView(text, rightLayoutParams);
				} catch (Exception e) {
					// TODO: handle exception
				}
			
			}/** 经纬度 */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final TextView wdeditText = new EditText(context);
				wdeditText.setTextColor(Color.BLACK);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** 设置EditText内容 */
				wdeditText.setText(String.valueOf(detaild.get("jd")) + "," + String.valueOf(detaild.get("wd")));
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(wdeditText, rightLayoutParams);
				/** html语句的内容 */
			} else if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(context);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				/** 支持脚本 */
				mWebSettings.setJavaScriptEnabled(true);
				/** 不可缩放 */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
				// mWebSettings.setPluginsEnabled(true);
				// TODO setPluginsEnabled已经弃用，改用下面的
				mWebSettings.setPluginState(PluginState.ON);
				mWebSettings.setNeedInitialFocus(false);
				wv.setHapticFeedbackEnabled(false);
				wv.setScrollBarStyle(0);
				wv.setHorizontalScrollBarEnabled(false);
				wv.setVerticalScrollBarEnabled(false);
				row.requestFocus();
				LinearLayout topLin = new LinearLayout(context);
				wv.setClickable(false);
				if (otherstr != null && !"".equals(otherstr) && otherstr != "null") {
					row.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(context, WebViewer.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "其他相关信息");
							context.startActivity(it);
						}
					});
					rightLayoutParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
					topLin.setLayoutParams(rightLayoutParams);
					topLin.setBackgroundColor(android.graphics.Color.TRANSPARENT);
					topLin.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(context, WebViewer.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "其他相关信息");
							context.startActivity(it);
						}
					});
					wv.loadDataWithBaseURL("", otherstr, "text/html", "utf-8", "");
					rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 300);
					rightLayoutParams.setMargins(40, 40, 40, 40);
					wv.setLayoutParams(rightLayoutParams);
				} else {
					continue;
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 400);
				FrameLayout fl = new FrameLayout(context);
				fl.setLayoutParams(rightLayoutParams);

				LinearLayout lin = new LinearLayout(context);
				lin.setHorizontalGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				lin.setLayoutParams(rightLayoutParams);
				lin.setBackgroundColor(android.graphics.Color.BLACK);
				LinearLayout lin1 = new LinearLayout(context);
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 398);
				rightLayoutParams.setMargins(1, 1, 1, 1);
				lin1.setLayoutParams(rightLayoutParams);
				lin1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				lin1.setBackgroundColor(android.graphics.Color.WHITE);
				lin.addView(lin1);
				lin1.addView(wv);
				fl.addView(lin);
				fl.addView(topLin);
				topLin.requestFocus();
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 400);
				row.addView(fl, rightLayoutParams);
				rightLayoutParams.setMargins(2, 2, 17, 10);

			} else if ("spinner".equals(map.get("style"))) {
				try {
					ArrayList<HashMap<String, Object>> res = XmlHelper.getDataFromXmlStream(context.getResources().getAssets().open("ent_wry_data_from.xml"),
							"item");
					String[] temp = new String[res.size()];
					for (int i = 0; i < temp.length; i++) {
						temp[i] = res.get(i).get("code").toString();
					}

					Spinner spinner = new Spinner(context);
					row.addView(spinner, rightLayoutParams);

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, temp);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setAdapter(adapter);
					value = detaild.get(styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString()).toString();

					int i = 0;
					for (String string : temp) {
						if (string.equals(value)) {
							spinner.setSelection(i);
							break;
						}
						i++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			/** 文字编辑框 */
			else {

				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** 这里设置各个 EditText 的 Id, 读取数据构造 XPath */
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** 表达式时调用 */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = String.valueOf(detaild.get(value));
				if (value.equals("industryname") && textvalue != null && !textvalue.equals("")) {
					// 把行业类别的代号换成文字
					String sql = "select b.name  from T_WRY_QYJBXX a join industry b on a.hylb =  b.code and a.guid = '" + qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("qygm") && textvalue != null && !textvalue.equals("")) {
					// 把企业规模的代号换成文字
					String sql = "select b.name  from T_WRY_QYJBXX a join wrygm b on a.qygm =  b.code and a.guid = '" + qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("fsclff") && textvalue != null && !textvalue.equals("")) {
					// 废水处理方法的值换成文字
					String sql = "select name  from T_WRY_CLFS where type = '1'  and code='" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("jhff") && textvalue != null && !textvalue.equals("")) {
					// 废气处理方法的值转换成文字
					String sql = "select name from T_WRY_CLFS where type = '2' and code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssyxzt") && textvalue != null && !textvalue.equals("")) {
					// 设施运行状态的值换成文字
					String sql = "select name  from YXQK where type='2' and code='" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("yxqk") && textvalue != null && !textvalue.equals("")) {
					// 生产设施履行情况里面的运行情况的值换成文字
					String sql = "select name  from YXQK where type='1' and code='" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rszl") && textvalue != null && !textvalue.equals("")) {
					// 废气排污口情况详情 燃料种类
					String sql = "select name from RSZL where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rsfs") && textvalue != null && !textvalue.equals("")) {
					// 废气排污口情况详情 燃烧方式
					String sql = "select name from RSFS where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfklx") && textvalue != null && !textvalue.equals("")) {
					// 废气排污口情况详情 排放口类型
					String sql = "select name from PFKLX where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("sfazzxjksb") && textvalue != null && !textvalue.equals("")) {
					// 废气排污口情况详情 是否安装在线检测设备
					String sql = "select optionName from  Whether  where optionId = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("sfqxgl") && textvalue != null && !textvalue.equals("")) {
					// 废气排污口情况详情 是否安装在线检测设备
					String sql = "select optionName from  Whether  where optionId = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("hbqlb") && textvalue != null && !textvalue.equals("")) {
					// 企业基本信息 保护区类别
					String sql = "select name from BHQLB where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssly") && textvalue != null && !textvalue.equals("")) {
					// 企业基本信息 所属流域
					String sql = "select valleyName from Valley where valleyCode = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfqxlb") && textvalue != null && !textvalue.equals("")) {
					// 废水排污口情况详情 排放情况类别
					String sql = "select name from PFQXLB where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("pfgl") && textvalue != null && !textvalue.equals("") && !textvalue.equalsIgnoreCase("null")) {
					// 废水、废气排污口情况详情 排放规律
					String sql = "select name from PFGL where  code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("wrylb") && textvalue != null && !textvalue.equals("")) {
					// 把污染源类别的代号换成文字
					if (textvalue.contains(";")) {
						StringBuilder sb = new StringBuilder();
						String[] arrays = textvalue.split(";");
						for (String a : arrays) {
							String sql = "select name  from  wrylb where code = '" + a + "'";
							String name = SqliteUtil.getInstance().getDepidByDepName(sql);
							sb.append(name + ",");
						}
						String sbstr = sb.toString();
						textvalue = sbstr.substring(0, sbstr.length() - 1);
					} else {
						String sql = "select name  from  wrylb where code = '" + textvalue + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
					}

				}else if(value.equals("lsgx") && textvalue != null && !textvalue.equals("")){//隶属关系
					String sql = "select name from LSGX where  code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}else if(value.equals("jjlx") && textvalue != null && !textvalue.equals("")){//经济类型
					String sql = "select name from JJLX where  code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}else if(value.equals("xzqh") && textvalue != null && !textvalue.equals("")){//行政区划
					String sql = "select * from Region where  regioncode = '"+textvalue+"'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// 获取用户职务
				if ("zw".equals(value)) {
					textvalue = transitToChinese(Integer.parseInt(detaild.get("zw").toString()));
				}
				// 获取用户职责
				if ("zz".equals(value)) {
					String sql = "select supercontext from gridusermapping where userid='" + detaild.get("userid") + "' group by blamerole";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// 执法仪
				if ("zfy".equals(value)) {
					String userid = detaild.get("userid").toString();
					String sql = "select username as zfy from ZhiFaYi_Users where userid = '" + userid + "'";
					ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
					if (data != null && data.size() > 0) {
						textvalue = data.get(0).get("zfy").toString();
					} else {
						textvalue = "";
					}
				}
				// 获取执行人
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));
					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '" + rwbh + "'");
					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("u_realname"));
							sb.append("，");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// 获取协办人
				if ("xbr".equals(value)) {
					String rwbh = detaild.get("rwbh").toString();

					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select  SecondaryAuditUserId from ydzf_rwlc where tid='" + rwbh + "' and SecondaryAuditUserId <>'' group by tid");

					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("secondaryaudituserid"));
							sb.append(",");
						}
						String sbname = sb.deleteCharAt(sb.length() - 1).toString();
						String[] array = sbname.split(",");
						StringBuilder sba = new StringBuilder();
						for (String a : array) {
							sba.append("'" + a + "',");
						}
						sba = sba.deleteCharAt(sba.length() - 1);
						String sql = "select U_RealName from PC_Users where UserID in (" + sba.toString() + ")";
						ArrayList<HashMap<String, Object>> result1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
						StringBuilder sb1 = new StringBuilder();
						if (result1 != null && result1.size() > 0) {
							for (HashMap<String, Object> item : result1) {
								sb1.append(item.get("u_realname").toString());
								sb1.append(",");
							}
						}
						sb1 = sb1.deleteCharAt(sb1.length() - 1);
						textvalue = sb1.toString();
					}
				}
				String unit = "";
				if (null != map.get("unit") && textvalue.length() > 0) {
					unit = map.get("unit").toString();
				}

				if ("time".equals(map.get("style")) && textvalue.contains("T")) {
					String values = (String) textvalue.subSequence(0, textvalue.length() - 6);
					textvalue = values.replace("T", " ");
					if (textvalue != null && !textvalue.equals("") && !textvalue.equalsIgnoreCase("null") && textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				
				if("approvaldate".equals(value)){
					textvalue = formatAM_PM_TIME(textvalue);
					Log.i(TAG, "-----approvaldate-------"+textvalue);
				}
				
				if("jgxz".equals(value)){
					//国控企业（I类）、省控及重点监管企业（II类）、其他常规监管企业（III类）、“挂牌”督办企业、其他企业
					if (textvalue != null && !textvalue.equals("")) {
						if ("1".equals(textvalue.trim())) {
							textvalue = "国控企业（I类）";
						}else if("2".equals(textvalue.trim())){
							textvalue = "省控及重点监管企业（II类）";
						}else if("3".equals(textvalue.trim())){
							textvalue = "其他常规监管企业（III类）";
						}else if("4".equals(textvalue.trim())){
							textvalue = "“挂牌”督办企业";
						}else if("5".equals(textvalue.trim())){
							textvalue = "其他企业";
						}
					} else {
						textvalue = "";
					}
				}
				
				if ("zdwry".equals(value)) {
					if (textvalue != null && !textvalue.equals("")) {
						if (1 == Integer.parseInt(textvalue.trim())) {
							// if("1".equals(textvalue.trim())){
							textvalue = "是";
						} else {
							textvalue = "否";
						}
					} else {
						textvalue = "";
					}
				}
				if ("sczt".equals(value)) {
					if (textvalue != null && !textvalue.equals("")) {
						if (1 == Integer.parseInt(textvalue.trim())) {
							// if("1".equals(textvalue.trim())){
							textvalue = "正常";
						} else {
							textvalue = "停产";
						}
					} else {
						textvalue = "";
					}
				}
				if ("ssds".equals(value)) {
					String xzqhStr = String.valueOf(detaild.get("xzqh"));
					String xzqhNameCity = SqliteUtil.getInstance().getDepidByDepName(
							"select RegionName from region where RegionCode" + " = (select ParentCode from region where RegionCode = '" + xzqhStr + "')");
					textvalue = xzqhNameCity;
					// TODO 2015.11.07
					// 顺德的固定了
					textvalue = "佛山市";
				}

				if ("ssqx".equals(value)) {
					String xzqhStr = String.valueOf(detaild.get("xzqh"));
					String xzqhNameCounty = SqliteUtil.getInstance().getDepidByDepName(
							"select regionname from region where RegionCode" + " = '" + xzqhStr + "'");
					textvalue = xzqhNameCounty;
					// TODO 2015.11.07
					// 顺德的固定了
					textvalue = "顺德区";
				}
				if ("ssjd".equals(value)) {
					String xzqhStr = String.valueOf(detaild.get("ssjd"));
					String xzqhNameStreet = SqliteUtil.getInstance().getDepidByDepName(
							"select streetname from street where RegionCode" + " = '" + xzqhStr + "'");
					textvalue = xzqhNameStreet;
				}
				// if("qxsj".equals(value)){
				// textvalue = textvalue.substring(0,10);
				// }
				/** 设置EditText内容 */
				if (textvalue != null && !textvalue.equals("") && !textvalue.equals("null") && !textvalue.equals("NULL")) {
					  if(value.equals("molecularformula") && textvalue != null && !textvalue.equals("")){
						   editText.setText(Html.fromHtml(textvalue));
					  }else if(value.equals("density") && textvalue != null && !textvalue.equals("")){
						  editText.setText(Html.fromHtml(textvalue));
					  }else if(value.equals("laboratorymonitormethods") && textvalue != null && !textvalue.equals("")){
						  editText.setText(Html.fromHtml(textvalue));
					  }else if(value.equals("environmentalstandards") && textvalue != null && !textvalue.equals("")){
						  editText.setText(Html.fromHtml(textvalue));
					  }else if(value.equals("emergencytreatment") && textvalue != null && !textvalue.equals("")){
						  editText.setText(Html.fromHtml(textvalue));
					  }else{
						  editText.setText(textvalue + " " + unit);
					   }
					  
					  
					  
					  
				}
				else {
					editText.setText("");
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(editText, rightLayoutParams);

			}
			queryTable.invalidate();
			queryTable.addView(row);
		}
	}

	public View getDetailView(HashMap<String, Object> detaild) {
		scrollView = new MyScrollView(context);
		queryTable = new TableLayout(context);
		queryTable.setColumnStretchable(1, true);
		styleDetailed = businessObj.getStyleDetailed(context);

		int itemCount = styleDetailed.size();

		/** 逐行往 TableLayout 里填充控件 */
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter == 0) {
				counter++;
				/** name属性不需要添加到列表 */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				/** mysql为自定义sql语句，此属性不需要添加到列表 */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			// textView.setMaxEms(5);

			/** 左侧 */
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			EditText editText = null;
			String value = null;
			/** 按钮 */
			if ("button".equals(map.get("style")) && !isAmend) {
				Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("") || button.getText().toString() == null) {
					button.setText("暂无内容");
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClick());

			} else if ("call".equals(map.get("style")) && !isAmend) {
				final Button callbutton = new Button(context);

				callbutton.setMaxEms(callbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				callbutton.setId(index);
				String callStr = String.valueOf(detaild.get(value));
				callbutton.setText(callStr.equals("null") ? "" : callStr);
				callbutton.setGravity(Gravity.LEFT);
				callbutton.setGravity(Gravity.CENTER_VERTICAL);
				// button.setText("sdcard/mapuni/task/log.txt");
				row.addView(callbutton, rightLayoutParams);
				// callbutton.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// tocall(callbutton.getText().toString());
				// }
				// });
				callbutton.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						tocall(callbutton.getText().toString());
						return false;
					}
				});
				/** 邮件 */
			} else if ("email".equals(map.get("style")) && !isAmend) {
				final Button emailbutton = new Button(context);

				emailbutton.setMaxEms(emailbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				emailbutton.setId(index);
				emailbutton.setText(String.valueOf(detaild.get(value)));
				emailbutton.setGravity(Gravity.LEFT);
				emailbutton.setGravity(Gravity.CENTER_VERTICAL);
				row.addView(emailbutton, rightLayoutParams);
				emailbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String email = emailbutton.getText().toString();
						if (email.equals("")) {
							Toast.makeText(context, "邮箱地址为空，无法发送邮件", Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "邮箱地址不合法，无法发送邮件", Toast.LENGTH_SHORT).show();
							return;
						} else {
							toemail(email);
						}

					}
				});
				/** web页 */
			} else if ("webview".equals(map.get("style")) && !isAmend) {
				Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText("查看");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("") || otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(), "暂无更多信息!", Toast.LENGTH_SHORT).show();
							return;
						}
						it.putExtra("otherstr", otherstr);
						it.putExtra("title", "详细内容");
						context.startActivity(it);
					}
				});

			}
			/** 文字视图 */
			else if ("textview".equals(map.get("style"))) {
				TextView text = new TextView(context);
				text.setMaxEms(text.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				text.setId(index);
				text.setText(String.valueOf(detaild.get(value)));
				row.addView(text, rightLayoutParams);

			}/** 经纬度 */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final EditText jwdeditText = new EditText(context);
				jwdeditText.setFocusable(false);
				jwdeditText.setMaxEms(jwdeditText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				jwdeditText.setId(index);
				String textvalue = String.valueOf(detaild.get(value));
				/** 设置EditText内容 */
				jwdeditText.setText(textvalue);
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(jwdeditText, rightLayoutParams);
				if (!isAmend) {
					jwdeditText.setFocusableInTouchMode(false);
				} else {
				}
				/** html语句的内容 */
			} else if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(context);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				/** 支持脚本 */
				mWebSettings.setJavaScriptEnabled(true);
				/** 不可缩放 */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
				// mWebSettings.setPluginsEnabled(true);
				// TODO setPluginsEnabled已经弃用，改用下面的
				mWebSettings.setPluginState(PluginState.ON);
				mWebSettings.setNeedInitialFocus(false);
				wv.setHapticFeedbackEnabled(false);
				wv.setScrollBarStyle(0);
				wv.setHorizontalScrollBarEnabled(false);
				wv.setVerticalScrollBarEnabled(false);
				row.requestFocus();
				LinearLayout topLin = new LinearLayout(context);
				wv.setClickable(false);
				if (otherstr != null && !"".equals(otherstr) && otherstr != "null") {
					row.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(context, WebViewer.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "其他相关信息");
							context.startActivity(it);
						}
					});
					rightLayoutParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
					topLin.setLayoutParams(rightLayoutParams);
					topLin.setBackgroundColor(android.graphics.Color.TRANSPARENT);
					topLin.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(context, WebViewer.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "其他相关信息");
							context.startActivity(it);
						}
					});
					wv.loadDataWithBaseURL("", otherstr, "text/html", "utf-8", "");
					rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 300);
					rightLayoutParams.setMargins(40, 40, 40, 40);
					wv.setLayoutParams(rightLayoutParams);
				} else {
					continue;
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 400);
				FrameLayout fl = new FrameLayout(context);
				fl.setLayoutParams(rightLayoutParams);

				LinearLayout lin = new LinearLayout(context);
				lin.setHorizontalGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				lin.setLayoutParams(rightLayoutParams);
				lin.setBackgroundColor(android.graphics.Color.BLACK);
				LinearLayout lin1 = new LinearLayout(context);
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 398);
				rightLayoutParams.setMargins(1, 1, 1, 1);
				lin1.setLayoutParams(rightLayoutParams);
				lin1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				lin1.setBackgroundColor(android.graphics.Color.WHITE);
				lin.addView(lin1);
				lin1.addView(wv);
				fl.addView(lin);
				fl.addView(topLin);
				topLin.requestFocus();
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 400);
				row.addView(fl, rightLayoutParams);
				rightLayoutParams.setMargins(2, 2, 17, 10);

			}/** 文字编辑框 */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** 这里设置各个 EditText 的 Id, 读取数据构造 XPath */
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** 表达式时调用 */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = String.valueOf(detaild.get(value));
				// 获取用户职务
				if ("zw".equals(value)) {
					textvalue = transitToChinese(Integer.parseInt(detaild.get("zw").toString()));
				}
				// 获取用户职责
				if ("zz".equals(value)) {
					String sql = "select supercontext from gridusermapping where userid='" + detaild.get("userid") + "' group by blamerole";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// 获取执行人
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));

					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '" + rwbh
									+ "'");

					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("u_realname"));
							sb.append("，");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// 获取协办人
				if ("xbr".equals(value)) {
					String rwbh = detaild.get("rwbh").toString();

					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select SecondaryAuditUserId from ydzf_rwlc where tid='" + rwbh + "' and NodeId = '3' and SecondaryAuditUserId <> '';");

					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("secondaryaudituserid"));
							sb.append(",");
						}
						String sbname = sb.deleteCharAt(sb.length() - 1).toString();
						String[] array = sbname.split(",");
						StringBuilder sba = new StringBuilder();
						for (String a : array) {
							sba.append("'" + a + "',");
						}
						sba = sba.deleteCharAt(sba.length() - 1);
						String sql = "select U_RealName from PC_Users where UserID in (" + sba.toString() + ")";
						ArrayList<HashMap<String, Object>> result1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
						StringBuilder sb1 = new StringBuilder();
						if (result1 != null && result1.size() > 0) {
							for (HashMap<String, Object> item : result1) {
								sb1.append(item.get("u_realname").toString());
								sb1.append(",");
							}
						}
						sb1 = sb1.deleteCharAt(sb1.length() - 1);
						textvalue = sb1.toString();
					}
				}
				String unit = "";
				if (null != map.get("unit") && textvalue.length() > 0) {
					unit = map.get("unit").toString();
				}
				if ("time".equals(map.get("style")) && textvalue.contains("T")) {
					String values = (String) textvalue.subSequence(0, textvalue.length() - 6);
					textvalue = values.replace("T", " ");
					if (textvalue != null && !textvalue.equals("") && !textvalue.equalsIgnoreCase("null") && textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				/** 设置EditText内容 */
				if (textvalue != null && !textvalue.equals("") && !textvalue.equals("null") && !textvalue.equals("NULL")) {
					editText.setText(textvalue + " " + unit);
				} else {
					editText.setText("");
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(editText, rightLayoutParams);
			}
			queryTable.invalidate();
			queryTable.addView(row);
		}
		scrollView.addView(queryTable);
		return scrollView;

	}

	/**
	 * 加载任务详情页面的布局
	 * 
	 * @param itemId
	 * @param queryTable
	 */
	public void addRWXXConent(String itemId, Bundle RWBundle) {
		styleDetailed = businessObj.getStyleDetailed(context);

		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		/** 通过id查询出数据 */
		detaild = businessObj.getDetailed(itemId);
		int itemCount = styleDetailed.size();

		/** 逐行往 TableLayout 里填充控件 */
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter == 0) {
				counter++;
				/** name属性不需要添加到列表 */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				/** mysql为自定义sql语句，此属性不需要添加到列表 */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			/** 左侧 */
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			EditText editText = null;
			String value = null;
			if ("listview".equals(map.get("style"))) {
				TableRow qylbrow = new TableRow(context);
				View qylbview = View.inflate(context, R.layout.add_qylb, null);
				qylbrow.addView(qylbview);

				queryTable1.invalidate();
				queryTable1.addView(qylbrow);

			}/** 文字编辑框 */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** 这里设置各个 EditText 的 Id, 读取数据构造 XPath */
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** 表达式时调用 */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = String.valueOf(detaild.get(value));
				// 获取执行人
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));
					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '" + rwbh
									+ "'");
					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("u_realname"));
							sb.append("，");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// 获取协办人
				if ("xbr".equals(value)) {
					String rwbh = detaild.get("rwbh").toString();
					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select distinct SecondaryAuditUserId from ydzf_rwlc where tid='" + rwbh + "' and SecondaryAuditUserId <>'' group by tid");
					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("secondaryaudituserid"));
							sb.append(",");
						}
						String sbname = sb.deleteCharAt(sb.length() - 1).toString();
						String[] array = sbname.split(",");
						StringBuilder sba = new StringBuilder();
						for (String a : array) {
							sba.append("'" + a + "',");
						}
						sba = sba.deleteCharAt(sba.length() - 1);
						String sql = "select U_RealName from PC_Users where UserID in (" + sba.toString() + ")";
						ArrayList<HashMap<String, Object>> result1 = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
						StringBuilder sb1 = new StringBuilder();
						if (result1 != null && result1.size() > 0) {
							for (HashMap<String, Object> item : result1) {
								sb1.append(item.get("u_realname").toString());
								sb1.append(",");
							}
						}
						sb1 = sb1.deleteCharAt(sb1.length() - 1);
						textvalue = sb1.toString();
					}
				}
				String unit = "";
				if (null != map.get("unit") && textvalue.length() > 0) {
					unit = map.get("unit").toString();
				}

				if ("time".equals(map.get("style")) && textvalue.contains("T")) {
					String values = (String) textvalue.subSequence(0, textvalue.length() - 6);
					textvalue = values.replace("T", " ");
					if (textvalue != null && !textvalue.equals("") && !textvalue.equalsIgnoreCase("null") && textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				/** 设置EditText内容 */
				if (textvalue != null && !textvalue.equals("") && !textvalue.equals("null") && !textvalue.equals("NULL")) {
					editText.setText(textvalue + " " + unit);
				} else {
					editText.setText("");
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(editText, rightLayoutParams);
				if (value.equals("rwmc") || value.equals("rwbh")) {
					queryTable.invalidate();
					queryTable.addView(row);
				} else {
					queryTable2.invalidate();
					queryTable2.addView(row);
				}
			}
		}
	}

	/**
	 * 加载带附件的布局
	 */
	protected MyScrollView loadlayout(String itemID, ArrayList<HashMap<String, Object>> attachment, String filepath) {
		queryTable = new TableLayout(context);
		queryTable1 = new TableLayout(context);// 存放企业列表
		queryTable2 = new TableLayout(context);
		scrollView = new MyScrollView(context);
		scrollView.setScrollContainer(true);
		scrollView.setFocusable(true);
		allLayout = new LinearLayout(context);// 滚动条上加的总布局
		allLayout.setOrientation(LinearLayout.VERTICAL);// 给总布局加载垂直布局
		LinearLayout tableLayout = new LinearLayout(context);// 方表格的布局
		tableLayout.setOrientation(LinearLayout.VERTICAL);// 给总布局加载垂直布局
		treeLayout = new LinearLayout(context);// 放树的布局
		treeLayout.setGravity(Gravity.TOP);
		allLayout.setGravity(Gravity.CENTER);
		tableLayout.setGravity(Gravity.CENTER);
		scrollView.addView(allLayout);
		tableLayout.addView(queryTable);
		tableLayout.addView(queryTable2);
		tableLayout.addView(queryTable1);
		allLayout.addView(tableLayout);
		queryTable.setColumnStretchable(1, true);
		queryTable2.setColumnStretchable(1, true);
		queryTable1.setColumnStretchable(0, true);
		Tree(scrollView, allLayout, treeLayout, context, itemID, attachment, filepath);
		return scrollView;
	}

	// 装载树的方法
	void Tree(MyScrollView scrollView, LinearLayout allLayout, LinearLayout treeLayout, final Context context, String itemId,
			ArrayList<HashMap<String, Object>> attachment, final String path) {
		scrollView.removeAllViews();
		ExpandableListView.LayoutParams ELayoutParams = new ExpandableListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ExpandableListView jcjlExpandable = new ExpandableListView(context);
		jcjlExpandable.setGroupIndicator(context.getResources().getDrawable(R.layout.expandablelistviewselector));
		jcjlExpandable.setLayoutParams(ELayoutParams);
		adapter = new TreeViewAdapter(context, 30);
		List<TreeViewAdapter.TreeNode> treeNode = adapter.GetTreeNode();
		int rowcount = 2;
		String[] groups = { "   附件" };
		for (int i = 0; i < groups.length; i++) {
			if (attachment != null) {

				TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
				node.parent = groups[i];
				// 将解析出来的数据遍历加载到树上
				for (HashMap<String, Object> treemap : attachment) {
					if (treemap != null) {
						if (treemap.size() > 0) {
							HashMap<String, Object> child = new HashMap<String, Object>();
							child.put("title", treemap.get("fileattachmentname").toString());
							child.put("id", treemap.get("fileattachmentpath").toString());
							node.childs.add(child);
							rowcount++;
						}

					}

				}
				treeNode.add(node);
			}

		}

		adapter.UpdateTreeNode(treeNode);
		jcjlExpandable.setAdapter(adapter);
		jcjlExpandable.expandGroup(0);
		treeLayout.addView(jcjlExpandable);
		treeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, TreeViewAdapter.ItemHeight * rowcount));// 树的布局参数
		// 判断是否有附件，如果有，就让他显示树级列表
		if (attachment != null && attachment.size() > 0) {
			allLayout.addView(treeLayout);
		}
		scrollView.addView(allLayout);
		// 给树添加单击事件
		jcjlExpandable.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView expandablelistview, View view, int pid, int cid, long l) {
				TextView text = (TextView) view;
				CharSequence name = text.getText();
				String filepath = text.getTag().toString();
				DisplayUitl.openfile(path + filepath, context);
				return true;
			}
		});

	}

	/**
	 * FileName: DetailLayout Description: 判断打开不同的附件
	 * 
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-5 下午03:34:42
	 */
	class OnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			String filepash = "";
			BaseClass baseclass = (BaseClass) businessObj;
			String[] tableadd = baseclass.GetTableName().split("_");
			if (button.getText().toString().equals("暂无内容")) {
				Toast.makeText(context, "暂无内容!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (button.getTag() != null) {
				filepash = Global.getGlobalInstance().SDCARD_FJ_LOCAL_PATH + tableadd[tableadd.length - 1] + "/" + (String) button.getTag();
			} else {
				filepash = Global.getGlobalInstance().SDCARD_FJ_LOCAL_PATH + tableadd[tableadd.length - 1] + "/" + (String) button.getText();
			}

			DisplayUitl.openfile(filepash, context);
		}

	}

	/**
	 * FileName: DetailLayout Description: 判断转换二进制文件成普通文件
	 * 
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-5 下午03:35:12
	 */
	class zhOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			BaseClass baseclass = (BaseClass) businessObj;
			String[] tableadd = baseclass.GetTableName().split("_");
			if (button.getText().toString().equals("暂无内容")) {
				Toast.makeText(context, "暂无内容!", Toast.LENGTH_SHORT).show();
				return;
			}
			// String itemId=Global.getGlobalInstance().getItemId();
			// String
			// filepash="/sdcard/mapuni/MobileEnforcement/fj/"+tableadd[tableadd.length-1]+"/"+(String)button.getText();
			// //将二进制解析成文件
			// FileHelper fh=new FileHelper();
			// ArrayList<HashMap<String, Object>>
			// list=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from t_zsk_yajal where guid='"+itemId+"'");
			// fh.convertBase64StringToLocalFile(list.get(0).get("fjswf").toString(),filepash,(String)button.getText());
			// //打开存好的文件
			// DisplayUitl.openfile(filepash, DetailedActivity.this);

			String path = Global.getGlobalInstance().SDCARD_FJ_LOCAL_PATH + tableadd[tableadd.length - 1] + "/";
			String filepash = (String) button.getText();
			File fl = new File(path);
			File nameFl = new File(path + filepash);
			try {
				if (!fl.exists()) {
					fl.mkdirs();
				}
				if (!nameFl.exists()) {
					nameFl.createNewFile();
				}
				System.out.println("path-------------" + path);
				System.out.println("filepash-------------" + filepash);
				System.out.println("path+filepash-------------" + path + filepash);
				// FileOutputStream os =
				// openFileOutput((String)button.getText(),Context.MODE_WORLD_READABLE
				// + Context.MODE_WORLD_WRITEABLE);
				FileOutputStream os = new FileOutputStream(nameFl);
				byte[] red_buf;
				String itemId = Global.getGlobalInstance().getItemId();
				Cursor cur = SqliteUtil.getInstance().queryBySql("select * from t_zsk_yajal where guid='" + itemId + "'");
				int k = 0;
				cur.moveToFirst();
				while (!cur.isAfterLast()) {
					red_buf = cur.getBlob(1);
					os.write(red_buf);
					k++;
					cur.moveToNext();
				}
				os.flush();
				os.close();

				// File ffl=new
				// File("/data/data/com.mapuni.android.MobileEnforcement/files/"+(String)button.getText()+"");
				// File tfl=new
				// File("/sdcard/mapuni/MobileEnforcement/fj/"+tableadd[tableadd.length-1]+"/"+(String)button.getText());
				// String toUrl=sdCard+"/TDFGHCDIMAGE/"+key+"/"+value+".jpg";
				// String
				// fromUrl=sdCard+"/mapuni/MobileEnforcement/data/task/"+key+"/RaskImage/"+value+".jpg";
				// /*******/
				// File resFile = new File(fromUrl);
				// File distFile = new File(toUrl);
				// if (ffl.isDirectory()) { // 目录时
				// FileUtils.copyDirectoryToDirectory(ffl, tfl);
				// } else if (ffl.isFile()) { // 文件时
				// FileUtils.copyFileToDirectory(ffl, tfl);
				// }

				DisplayUitl.openfile("/data/data/com.mapuni.android.MobileEnforcement/files/" + (String) button.getText() + "", context);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

		}
	}

	/**
	 * Description: 拨打电话
	 * 
	 * @param tal
	 *            电话号码 void
	 * @author 王红娟 Create at: 2012-12-5 下午03:35:31
	 */
	public void tocall(String tal) {
		if (tal.equals("")) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tal));
		context.startActivity(intent); // 开始意图(及拨打电话)
	}

	/**
	 * Description: 跳转到发送邮件页面
	 * 
	 * @param add
	 *            邮件地址 void
	 * @author 王红娟 Create at: 2012-12-5 下午03:35:50
	 */
	public void toemail(String add) {
		Uri uri = Uri.parse("mailto:" + add);

		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "邮件无法发送，请下载相关软件", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}

	/**
	 * Description: 判断邮箱地址合不合法
	 * 
	 * @param email
	 *            邮箱地址
	 * @return 合法返回true
	 * @author Administrator
	 * @Create at: 2013-4-15 下午4:22:33
	 */
	public boolean checkEmail(String email) {

		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	// 根据用户职务编号获取中文名称
	public String transitToChinese(int nodeid) {
		String position = "";
		switch (nodeid) {
		case 0:
			position = "办公室";
			break;
		case 1:
			position = "局长";
			break;
		case 2:
			position = "分管局长";
			break;
		case 3:
			position = "科长";
			break;
		default:
			position = "执行人";
			break;
		}
		return position;
	}

	
	public   String formatAM_PM_TIME(String time) {
		try {
			if (time != null
					&& (time.contains("AM") || time.contains("am")
							|| time.contains("PM") || time.contains("pm"))) {
				String time1 = time.toLowerCase();
				String[] str = time1.split(" ");
				time = str[2]+"-" + str[1]+"-" + str[0]/*+" " + str[3]*/;
			}
			if(time != null && time.contains("T")){

				String values = (String) time.subSequence(0, time.length() - 6);
				time = values.replace("T", " ");
				if (time != null && !time.equals("") && !time.equalsIgnoreCase("null") && time.length() > 11) {
					time = time.substring(0, 11);
				}
				if (time.contains("1900")) {
					time = "";
				}
			}
			
		} catch (Exception e) {
		}
		return time;
	}
}
