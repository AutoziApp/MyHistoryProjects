/**
 * 
 */
package cn.com.mapuni.meshing.base;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.R.string;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.adapter.TreeViewAdapter;
import cn.com.mapuni.meshing.base.business.BaseClass;
import cn.com.mapuni.meshing.base.controls.listview.MyScrollView;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.dataprovider.DESSecurity;
import cn.com.mapuni.meshing.base.dataprovider.FileHelper;
import cn.com.mapuni.meshing.base.dataprovider.JsonHelper;
import cn.com.mapuni.meshing.base.dataprovider.SQLiteDataProvider;
import cn.com.mapuni.meshing.base.dataprovider.SqliteUtil;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.interfaces.IDetailed;
import cn.com.mapuni.meshing.base.util.DateTimeHelper;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.Gps;
import cn.com.mapuni.meshing.base.util.LogUtil;
import cn.com.mapuni.meshing.base.util.PositionUtil;
import cn.com.mapuni.meshing.netprovider.Net;
import cn.com.mapuni.meshing.netprovider.WebServiceProvider;


/**
 * FileName: DetailLayout Description:加载详情页面的布局
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-1-15 下午03:50:49
 */
public class LoadDetailLayout {

	private static final String TAG = "ZHANGRAN";
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
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public LoadDetailLayout(Context context, IDetailed businessObj,
			boolean isAmend) {
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
	public void setStyleDetailed(
			ArrayList<HashMap<String, Object>> styleDetailed) {
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
	public MyScrollView loadTreeDetailed(String itemId,
			ArrayList<HashMap<String, Object>> attachment, String filepath,
			Bundle RWBundle) {

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
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			attchUnit = styleDetailed.get(index).get("queryEditText")
					.toString();
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("name")
					&& counter == 0) {
				counter++;
				/** name属性不需要添加到列表 */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("mysql")) {
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
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT)
					.toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			/**
			 * textview
			 */
			row.addView(textView);
			LogUtil.i(TAG, textView.getText().toString());
			EditText editText = null;
			String value = null;
			/** 按钮 */
			if ("button".equals(map.get("style")) && !isAmend) {
				final Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("")
						|| button.getText().toString().equals("null")
						|| button.getText().toString() == null) {
					button.setText("下载附件");
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(button, rightLayoutParams);
				if ("blob".equals(map.get("type"))) {
					Global.getGlobalInstance().setItemId(itemId);
					button.setOnClickListener(new zhOnClick());
				} else if ("networkfj".equals(value)) {
					button.setText("查看");
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							new CustomAsyncTask(String.valueOf(detaild
									.get("id")), String.valueOf(detaild
									.get("tablename"))).execute();
						}
					});
				} else if("ZXWZ".equals(value)) {
					button.setText("查看");
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							new CustomDwAsyncTask(detaild.get("userid").toString()).execute();
						}
					});
				} else if("GJCX".equals(value)){
					button.setText("查看");
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.putExtra("userid", detaild.get("userid").toString());
							intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.infoQuery.RYDWActivity" );
							context.startActivity(intent);
						}
					});
				} else {
					hashmap.put(index, attchUnit);
					button.setOnClickListener(new OnClick() {
						public void onClick(View v) {
							int k = 0;
							FileHelper fileHelper = new FileHelper();
							String attchDM = hashmap.get(button.getId())
									.toString();
							if("PWSBDJBFJ".equals(attchDM) || "YJYAFJ".equals(attchDM) ||
									"QJSCFJ".equals(attchDM) || "HJXWPJFJ".equals(attchDM) ||
									"JCSJFJ".equals(attchDM)){
								fileHelper.showFileByGuid(qybm, context);
							} else if ("YYZZFBTP".equals(attchDM)) {
								attchDM = "YYZZ";
								k = fileHelper.showFileByQyid(qybm, attchDM,
										context);
							} else if ("XMSPDZDA".equals(attchDM)
									|| "SSCSPDZDA".equals(attchDM)
									|| "HBYSDZDA".equals(attchDM)
									|| "HBSXLXQK_GYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_HBSXLXQK where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}

							} else if ("SCSSYXQK_SCGYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_SCSSYXQK where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else if ("FSZLSSYXQK_GYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSZLSSXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else if ("FQZLSSYXQK_GYDZDA".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FQZLSS where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else if ("FSPWKQK_SB".equals(attchDM)
									|| "FSPWKQK_YXQK".equals(attchDM)
									|| "FSPWKQK_PFKQK".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else if ("FSPFWRWFJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKPFWRWXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							}   else if ("FSPFWRWFJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKPFWRWXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							}   else if ("FQPFWRWFJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FQPFKPFWRWXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else if ("GTFWFJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_GTFW where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else if ("ZSXXFJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_ZSY where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								if (list != null && list.size() > 0) {
									for (int j = 0; j < list.size(); j++) {
										k = fileHelper.showFileByQyid(
												list.get(j).get("guid")
														.toString(), attchDM,
												context);
									}
								}
							} else {
								k = fileHelper.showFileByQyid(qybm, attchDM,
										context);
							}
							LogUtil.i(TAG, "attch" + attchDM);
//							if (k == 0) {
//								Toast.makeText(context, "附件不存在",
//										Toast.LENGTH_SHORT).show();
//							} else if (k == 5 || k == 2) {
//								Toast.makeText(context, "网络不通,请检查网络！",
//										Toast.LENGTH_SHORT).show();
//							}
							// else{
							// Toast.makeText(context, "文件下载失败！",
							// Toast.LENGTH_SHORT).show();
							// }
						}
					});

				}
			} else if ("call".equals(map.get("style")) && !isAmend) {
				final Button callbutton = new Button(context);

				callbutton.setMaxEms(callbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				callbutton.setId(index);
				String callStr = String.valueOf(detaild.get(value));
				callbutton.setText(callStr.equals("null") ? "" : callStr);
				callbutton.setGravity(Gravity.LEFT);
				callbutton.setGravity(Gravity.CENTER_VERTICAL);
				// button.setText("sdcard/mapuni/task/log.txt");
				row.addView(callbutton, rightLayoutParams);
				callbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tocall(callbutton.getText().toString());
					}
				});
				/** 邮件 */
			} else if ("email".equals(map.get("style")) && !isAmend) {
				final Button emailbutton = new Button(context);

				emailbutton.setMaxEms(emailbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				emailbutton.setId(index);
				String emailStr = String.valueOf(detaild.get(value));
				if (emailStr.equals(null) || emailStr == null
						|| emailStr.equals("null")) {
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
							Toast.makeText(context, "邮箱地址为空，无法发送邮件",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "邮箱地址不合法，无法发送邮件",
									Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText("查看");
				String temString = "";
				if ("method".equals(value)) {
					String result = "";
					try {
						InputStream in = context.getResources().getAssets()
								.open("whp.html");
						// 获取文件的字节数
						int lenght = in.available();
						// 创建byte数组
						byte[] buffer = new byte[lenght];
						// 将文件中的数据读到byte数组中
						in.read(buffer);
						result = EncodingUtils.getString(buffer, "gbk");
					} catch (Exception e) {
						e.printStackTrace();
					}

					result = result.replace("{0}",
							String.valueOf(detaild.get("circumstance_health")));
					result = result.replace("{1}", String.valueOf(detaild
							.get("circumstance_toxicologically")));
					result = result.replace("{2}",
							String.valueOf(detaild.get("circumstance_sum_up")));
					result = result.replace("{3}",
							String.valueOf(detaild.get("monitoring_locale")));
					result = result.replace("{4}", String.valueOf(detaild
							.get("monitoring_laboratory")));
					result = result.replace("{5}", String.valueOf(detaild
							.get("circumstance_criterion")));
					result = result.replace("{6}",
							String.valueOf(detaild.get("lash_up_step")));
					result = result.replace("{7}",
							String.valueOf(detaild.get("protection_step")));
					result = result.replace("{8}",
							String.valueOf(detaild.get("first_aid_step")));
					result = result.replace("{9}",
							String.valueOf(detaild.get("fireplug")));

					temString = result;
				} else {
					temString = String.valueOf(detaild.get(value));
				}
				final String otherstr = temString;
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("")
								|| otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(),
									"暂无更多信息!", Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				text.setId(index);
				text.setText(String.valueOf(detaild.get(value)));
				row.addView(text, rightLayoutParams);

			}/** 经纬度 */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final int jdIndext = index - 1;
				final TableLayout t = new TableLayout(context);
				t.setColumnStretchable(0, true);
				TableRow wdRow = new TableRow(context);
				final EditText jwdeditText = new EditText(context);
				jwdeditText.setMaxEms(jwdeditText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				jwdeditText.setId(index);
				String textvalue = String.valueOf(detaild.get(value));
				/** 设置EditText内容 */
				jwdeditText.setText(textvalue);
				ImageView image = new ImageView(context);
				Drawable draw = context.getResources().getDrawable(
						R.drawable.tdfg_location);
				image.setImageDrawable(draw);
				wdRow.addView(jwdeditText);
				wdRow.addView(image);
				t.addView(wdRow);
				row.addView(t);
				if (!isAmend) {
					jwdeditText.setFocusableInTouchMode(false);
					image.setOnClickListener(null);
				} else {
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							gislocation.locationPrepare(context);
							location = gislocation.getLocation(context);
							if (location == null) {
								Toast.makeText(context.getApplicationContext(),
										"暂时无法获得卫星信号!", Toast.LENGTH_SHORT)
										.show();
								return;
							}
							/** 纬度 */
							String jd = location.getLongitude() + "";
							/** 经度 */
							String wd = location.getLatitude() + "";
							jwdeditText.setText(wd);
							EditText jdeditext = (EditText) t
									.findViewById(jdIndext);
							jdeditext.setText(jd);

						}
					});
				}
				/** html语句的内容 */
			} else if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex)
						.get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(context);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				/** 支持脚本 */
				mWebSettings.setJavaScriptEnabled(true);
				/** 不可缩放 */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
//				mWebSettings.setPluginsEnabled(false);
//				mWebSettings.setPluginState(WebSettings.PluginState.OFF);
				mWebSettings.setNeedInitialFocus(false);
				wv.setHapticFeedbackEnabled(false);
				wv.setScrollBarStyle(0);
				wv.setHorizontalScrollBarEnabled(false);
				wv.setVerticalScrollBarEnabled(false);
				row.requestFocus();
				LinearLayout topLin = new LinearLayout(context);
				wv.setClickable(false);
				if (otherstr != null && !"".equals(otherstr)
						&& otherstr != "null") {
					row.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(context, WebViewer.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "其他相关信息");
							context.startActivity(it);
						}
					});
					rightLayoutParams = new TableRow.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
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
					wv.loadDataWithBaseURL("", otherstr, "text/html", "utf-8",
							"");
					rightLayoutParams = new TableRow.LayoutParams(
							LayoutParams.MATCH_PARENT, 300);
					rightLayoutParams.setMargins(40, 40, 40, 40);
					wv.setLayoutParams(rightLayoutParams);
				} else {
					continue;
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, 400);
				FrameLayout fl = new FrameLayout(context);
				fl.setLayoutParams(rightLayoutParams);

				LinearLayout lin = new LinearLayout(context);
				lin.setHorizontalGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);
				lin.setLayoutParams(rightLayoutParams);
				lin.setBackgroundColor(android.graphics.Color.BLACK);
				LinearLayout lin1 = new LinearLayout(context);
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, 398);
				rightLayoutParams.setMargins(1, 1, 1, 1);
				lin1.setLayoutParams(rightLayoutParams);
				lin1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);
				lin1.setBackgroundColor(android.graphics.Color.WHITE);
				lin.addView(lin1);
				lin1.addView(wv);
				fl.addView(lin);
				fl.addView(topLin);
				topLin.requestFocus();
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, 400);
				row.addView(fl, rightLayoutParams);
				rightLayoutParams.setMargins(2, 2, 17, 10);

			}/** 文字编辑框 */
			else {

				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
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

				if (value.equals("industryname") && textvalue != null
						&& !textvalue.equals("")) {
					// 把行业类别的代号换成文字
					String sql = "select b.name  from T_WRY_QYJBXX a join industry b on a.hylb =  b.code and a.guid = '"
							+ qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("qygm") && textvalue != null
						&& !textvalue.equals("") && "1234".contains(textvalue)) {
					// 把企业规模的代号换成文字
					String sql = "select b.name  from T_WRY_QYJBXX a join wrygm b on a.qygm =  b.code and a.guid = '"
							+ qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("fsclff") && textvalue != null
						&& !textvalue.equals("")) {
					// 废水处理方法的值换成文字
					String sql = "select name  from T_WRY_CLFS where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("clfs") && textvalue != null
						&& !textvalue.equals("")) {
					// 处理方式的值换成文字
					String sql = "select name  from T_WRY_CLFS where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfklx") && textvalue != null
						&& !textvalue.equals("")) {
					// 排放口类型换成文字
					String sql = "select name  from PFKLX where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("jhff") && textvalue != null
						&& !textvalue.equals("")) {

					String sql = "select name  from T_WRY_CLFS where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfqxlb") && textvalue != null
						&& !textvalue.equals("")) {
					// 排放去向类别
					String sql = "select name  from PFQXLB where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("pfgl") && textvalue != null
						&& !textvalue.equals("")) {
					// 排放规律
					String sql = "select name  from PFGL where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rszl") && textvalue != null
						&& !textvalue.equals("")) {
					// 燃烧种类
					String sql = "select name  from RSZL where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rsfs") && textvalue != null
						&& !textvalue.equals("")) {
					// 燃烧方式
					String sql = "select name  from RSFS where code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssly") && textvalue != null
						&& !textvalue.equals("")) {
					// 所属流域
					String sql = "select valleyname from valley where valleycode = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("lsgx") && textvalue != null
						&& !textvalue.equals("")) {
					// 隶属关系
					String sql = "select name from LSGX where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("jjlx") && textvalue != null
						&& !textvalue.equals("")) {
					// 经济类型
					String sql = "select name from JJLX where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssyxzt") && textvalue != null
						&& !textvalue.equals("")) {
					// 设施运行状态的值换成文字
					String sql = "select name  from YXQK where type='2' and code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("yxqk") && textvalue != null
						&& !textvalue.equals("")) {
					// 生产设施履行情况里面的运行情况的值换成文字
					String sql = "select name  from YXQK where type='1' and code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("wrylb") && textvalue != null
						&& !textvalue.equals("")) {
					// 把污染源类别的代号换成文字
					if (textvalue.contains(";")) {
						StringBuilder sb = new StringBuilder();
						String[] arrays = textvalue.split(";");
						for (String a : arrays) {
							String sql = "select name  from  wrylb where code = '"
									+ a + "'";
							String name = SqliteUtil.getInstance()
									.getDepidByDepName(sql);
							sb.append(name + ",");
						}
						String sbstr = sb.toString();
						textvalue = sbstr.substring(0, sbstr.length() - 1);
					} else {
						String sql = "select name  from  wrylb where code = '"
								+ textvalue + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					}
				} else if (value.equals("qjshqk") && textvalue != null
						&& !textvalue.equals("")) {
					String sql = "select name  from OptionField where  code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// 获取执行人
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));
					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '"
											+ rwbh + "'");
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
				String unit = "";
				if (null != map.get("unit") && textvalue.length() > 0) {
					unit = map.get("unit").toString();
				}

//				if ("yxqk".equals(value) && textvalue != null
//						&& !textvalue.equals("")) {
//					// 把运行情况的代号换成文字
//					String sql = "select name  from YXQK where id = "
//							+ textvalue + "";
//
//					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
//
//				}

				if ("hbqlb".equals(value) && textvalue != null
						&& !textvalue.equals("")) {
					// 把保护区类别的代号换成文字
					String sql = "select name  from BHQLB where code = "
							+ textvalue + "";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}

				if ("time".equals(map.get("style")) && textvalue.contains("T")) {
//					String values = (String) textvalue.subSequence(0,
//							textvalue.length() - 6);
//					textvalue = values.replace("T", " ");
//					if (textvalue.contains("1900")) {
//						textvalue = "";
//					}
					
					textvalue = DateTimeHelper.formatToStringT(textvalue);
				}
				
				if ("updatetime".equals(value)) {
					textvalue = DateTimeHelper.formatToStringT(textvalue);
				}
				// 排污申报是否登记
				if ("pwsbsfdj".equals(value)) {
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
				// 是否安装在线监控
				if ("sfazzxjksb".equals(value)) {
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
				// 污染物排放情况的固体废物信息 “是否危废”
				if ("sfwf".equals(value)) {
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

				if ("hjxxxydj".equals(value)) {
					if (textvalue != null && !textvalue.equals("")) {
						String sql = "select Name from HJXXXYDJ where code = '"
								+ textvalue + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					} else {
						textvalue = "";
					}
				}
				if ("zdwry".equals(value)) {
					if (textvalue != null && !textvalue.equals("")) {
						if (1 == Integer.parseInt(textvalue.trim())) {
							// if("1".equals(textvalue.trim())){
							textvalue = "国控";
						} else if(2 == Integer.parseInt(textvalue.trim())) {
							textvalue = "省控";
						} else if(3 == Integer.parseInt(textvalue.trim())) {
							textvalue = "市控";
						} else {
							textvalue = "其它";
						}
					} else {
						textvalue = "";
					}
				}
				if ("sfys".equals(value)) {
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
				if ("sfqxgl".equals(value)) {
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
					String xzqhNameCity = SqliteUtil
							.getInstance()
							.getDepidByDepName(
									"select RegionName from region where RegionCode"
											+ " = (select ParentCode from region where RegionCode = '"
											+ xzqhStr + "')");
					textvalue = xzqhNameCity;

				}

				if ("ssqx".equals(value)) {
					String xzqhStr = String.valueOf(detaild.get("xzqh"));
					String xzqhNameCounty = SqliteUtil.getInstance()
							.getDepidByDepName(
									"select regionname from region where RegionCode"
											+ " = '" + xzqhStr + "'");
					textvalue = xzqhNameCounty;
				}

				// if("qxsj".equals(value)){
				// textvalue = textvalue.substring(0,10);
				// }
				/** 设置EditText内容 */
				if (textvalue != null && !textvalue.equals("")
						&& !textvalue.equals("null")
						&& !textvalue.equals("NULL")) {

					editText.setText(textvalue + " " + unit);
				} else {
					editText.setText("");
				}

				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(editText, rightLayoutParams);

				LogUtil.i(TAG, "textvalue--" + textvalue + "--index--" + index);
				LogUtil.i(TAG, "value--" + value + "--attch--");

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
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("name")
					&& counter == 0) {
				counter++;
				/** name属性不需要添加到列表 */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("mysql")) {
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
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT)
					.toString();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("")
						|| button.getText().toString() == null) {
					button.setText("暂无内容");
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClick());

			} else if ("call".equals(map.get("style")) && !isAmend) {
				final Button callbutton = new Button(context);

				callbutton.setMaxEms(callbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				callbutton.setId(index);
				String callStr = String.valueOf(detaild.get(value));
				callbutton.setText(callStr.equals("null") ? "" : callStr);
				callbutton.setGravity(Gravity.LEFT);
				callbutton.setGravity(Gravity.CENTER_VERTICAL);
				// button.setText("sdcard/mapuni/task/log.txt");
				row.addView(callbutton, rightLayoutParams);
				callbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tocall(callbutton.getText().toString());
					}
				});
				/** 邮件 */
			} else if ("email".equals(map.get("style")) && !isAmend) {
				final Button emailbutton = new Button(context);

				emailbutton.setMaxEms(emailbutton.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
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
							Toast.makeText(context, "邮箱地址为空，无法发送邮件",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "邮箱地址不合法，无法发送邮件",
									Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText("查看");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("")
								|| otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(),
									"暂无更多信息!", Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				text.setId(index);
				text.setText(String.valueOf(detaild.get(value)));
				row.addView(text, rightLayoutParams);

			}/** 经纬度 */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final int jdIndext = index - 1;
				final TableLayout t = new TableLayout(context);
				t.setColumnStretchable(0, true);
				TableRow wdRow = new TableRow(context);
				final EditText jwdeditText = new EditText(context);
				jwdeditText.setMaxEms(jwdeditText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				jwdeditText.setId(index);
				String textvalue = String.valueOf(detaild.get(value));
				/** 设置EditText内容 */
				jwdeditText.setText(textvalue);
				ImageView image = new ImageView(context);
				Drawable draw = context.getResources().getDrawable(
						R.drawable.tdfg_location);
				image.setImageDrawable(draw);
				wdRow.addView(jwdeditText);
				wdRow.addView(image);
				t.addView(wdRow);
				row.addView(t);
				if (!isAmend) {
					jwdeditText.setFocusableInTouchMode(false);
					image.setOnClickListener(null);
				} else {
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							gislocation.locationPrepare(context);
							location = gislocation.getLocation(context);
							if (location == null) {
								Toast.makeText(context.getApplicationContext(),
										"暂时无法获得卫星信号!", Toast.LENGTH_SHORT)
										.show();
								return;
							}
							/** 纬度 */
							String jd = location.getLongitude() + "";
							/** 经度 */
							String wd = location.getLatitude() + "";
							jwdeditText.setText(wd);
							EditText jdeditext = (EditText) t
									.findViewById(jdIndext);
							jdeditext.setText(jd);

						}
					});
				}
				/** html语句的内容 */
			} else if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex)
						.get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(context);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				/** 支持脚本 */
				mWebSettings.setJavaScriptEnabled(true);
				/** 不可缩放 */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
//				mWebSettings.setPluginsEnabled(false);
				mWebSettings.setNeedInitialFocus(false);
				wv.setHapticFeedbackEnabled(false);
				wv.setScrollBarStyle(0);
				wv.setHorizontalScrollBarEnabled(false);
				wv.setVerticalScrollBarEnabled(false);
				row.requestFocus();
				LinearLayout topLin = new LinearLayout(context);
				wv.setClickable(false);
				if (otherstr != null && !"".equals(otherstr)
						&& otherstr != "null") {
					row.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent it = new Intent(context, WebViewer.class);
							it.putExtra("otherstr", otherstr);
							it.putExtra("title", "其他相关信息");
							context.startActivity(it);
						}
					});
					rightLayoutParams = new TableRow.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
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
					wv.loadDataWithBaseURL("", otherstr, "text/html", "utf-8",
							"");
					rightLayoutParams = new TableRow.LayoutParams(
							LayoutParams.MATCH_PARENT, 300);
					rightLayoutParams.setMargins(40, 40, 40, 40);
					wv.setLayoutParams(rightLayoutParams);
				} else {
					continue;
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, 400);
				FrameLayout fl = new FrameLayout(context);
				fl.setLayoutParams(rightLayoutParams);

				LinearLayout lin = new LinearLayout(context);
				lin.setHorizontalGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);
				lin.setLayoutParams(rightLayoutParams);
				lin.setBackgroundColor(android.graphics.Color.BLACK);
				LinearLayout lin1 = new LinearLayout(context);
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, 398);
				rightLayoutParams.setMargins(1, 1, 1, 1);
				lin1.setLayoutParams(rightLayoutParams);
				lin1.setHorizontalGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);
				lin1.setBackgroundColor(android.graphics.Color.WHITE);
				lin.addView(lin1);
				lin1.addView(wv);
				fl.addView(lin);
				fl.addView(topLin);
				topLin.requestFocus();
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, 400);
				row.addView(fl, rightLayoutParams);
				rightLayoutParams.setMargins(2, 2, 17, 10);

			}/** 文字编辑框 */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
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
					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '"
											+ rwbh + "'");
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
				// 发布时间
				if ("fbsj".equals(value) && !textvalue.equals("")) {
					try {
						if(String.valueOf(detaild.get("rwly")).equals("013")) {
							textvalue = textvalue.split(" ")[0].replace("/", "-");
							textvalue = dateFormat.format(dateFormat
									.parse(textvalue));
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 办结期限
				if ("bjqx".equals(value) && !textvalue.equals("")) {
					try {
						textvalue = textvalue.split(" ")[0].replace("/", "-");
						textvalue = dateFormat.format(dateFormat
								.parse(textvalue));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				String unit = "";
				if (null != map.get("unit") && textvalue.length() > 0) {
					unit = map.get("unit").toString();
				}

				if ("time".equals(map.get("style")) && textvalue.contains("T")) {
					String values = (String) textvalue.subSequence(0,
							textvalue.length() - 6);
					textvalue = values.replace("T", " ");
				}
				/** 设置EditText内容 */
				if (textvalue != null && !textvalue.equals("")
						&& !textvalue.equals("null")
						&& !textvalue.equals("NULL")) {
					editText.setText(textvalue + " " + unit);
				} else {
					editText.setText("");
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("name")
					&& counter == 0) {
				counter++;
				/** name属性不需要添加到列表 */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("mysql")) {
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
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT)
					.toString();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
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
					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '"
											+ rwbh + "'");
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
				String unit = "";
				if (null != map.get("unit") && textvalue.length() > 0) {
					unit = map.get("unit").toString();
				}

				if ("time".equals(map.get("style")) && textvalue.contains("T")) {
					String values = (String) textvalue.subSequence(0,
							textvalue.length() - 6);
					textvalue = values.replace("T", " ");
				}
				/** 设置EditText内容 */
				if (textvalue != null && !textvalue.equals("")
						&& !textvalue.equals("null")
						&& !textvalue.equals("NULL")) {
					editText.setText(textvalue + " " + unit);
				} else {
					editText.setText("");
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
	protected MyScrollView loadlayout(String itemID,
			ArrayList<HashMap<String, Object>> attachment, String filepath) {
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
		Tree(scrollView, allLayout, treeLayout, context, itemID, attachment,
				filepath);
		return scrollView;
	}

	// 装载树的方法
	void Tree(MyScrollView scrollView, LinearLayout allLayout,
			LinearLayout treeLayout, final Context context, String itemId,
			ArrayList<HashMap<String, Object>> attachment, final String path) {
		scrollView.removeAllViews();
		ExpandableListView.LayoutParams ELayoutParams = new ExpandableListView.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ExpandableListView jcjlExpandable = new ExpandableListView(context);
		jcjlExpandable.setGroupIndicator(context.getResources().getDrawable(
				R.layout.expandablelistviewselector));
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
							child.put("title", treemap
									.get("fileattachmentname").toString());
							child.put("id", treemap.get("fileattachmentpath")
									.toString());
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
		treeLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				TreeViewAdapter.ItemHeight * rowcount));// 树的布局参数
		// 判断是否有附件，如果有，就让他显示树级列表
		if (attachment != null && attachment.size() > 0) {
			allLayout.addView(treeLayout);
		}
		scrollView.addView(allLayout);
		// 给树添加单击事件
		jcjlExpandable.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView expandablelistview,
					View view, int pid, int cid, long l) {
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
				filepash = Global.getGlobalInstance().SDCARD_FJ_LOCAL_PATH
						+ tableadd[tableadd.length - 1] + "/"
						+ (String) button.getTag();
			} else {
				filepash = Global.getGlobalInstance().SDCARD_FJ_LOCAL_PATH
						+ tableadd[tableadd.length - 1] + "/"
						+ (String) button.getText();
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

			String path = Global.getGlobalInstance().SDCARD_FJ_LOCAL_PATH
					+ tableadd[tableadd.length - 1] + "/";
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
				System.out.println("path+filepash-------------" + path
						+ filepash);
				// FileOutputStream os =
				// openFileOutput((String)button.getText(),Context.MODE_WORLD_READABLE
				// + Context.MODE_WORLD_WRITEABLE);
				FileOutputStream os = new FileOutputStream(nameFl);
				byte[] red_buf;
				String itemId = Global.getGlobalInstance().getItemId();
				Cursor cur = SqliteUtil.getInstance()
						.queryBySql(
								"select * from t_zsk_yajal where guid='"
										+ itemId + "'");
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

				DisplayUitl.openfile(
						"/data/data/com.mapuni.android.MobileEnforcement/files/"
								+ (String) button.getText() + "", context);
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
			Toast.makeText(context, "邮件无法发送，请下载相关软件", Toast.LENGTH_SHORT)
					.show();
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

		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	private class CustomAsyncTask extends AsyncTask<Void, Void, Integer> {
		private YutuLoading yutuLoading;
		private String idString;
		private String tablenameString;
		private String pString = "";

		public CustomAsyncTask(String idString, String tablenameString) {
			// TODO Auto-generated constructor stub
			this.idString = idString;
			this.tablenameString = tablenameString;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			yutuLoading = new YutuLoading(context);
			yutuLoading.setCancelable(false);
			yutuLoading.showDialog();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... paramss) {
			// TODO Auto-generated method stub
			int flag = -1;
			String methodName = "GetFileDoc";
			if (Net.checkNet(context)) {
				try {
					/** 设置webserice中的参数 */
					ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();

					/** 表名 */
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("tableName", tablenameString);
					params.add(param);

					/** id */
					param = new HashMap<String, Object>();
					param.put("id", idString);
					params.add(param);

					/** token */
					param = new HashMap<String, Object>();
					String token = "";
					try {
						token = DESSecurity.encrypt(methodName);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					param.put("token", token);
					params.add(param);

					String jsonStr = "";
					try {
						jsonStr = String
								.valueOf(WebServiceProvider.callWebService(
										Global.NAMESPACE, methodName, params,
										Global.getGlobalInstance()
												.getSystemurl()
												+ Global.WEBSERVICE_URL,
										WebServiceProvider.RETURN_STRING, true));
					} catch (Exception e1) {
						e1.printStackTrace();
						flag = 1;
						return flag;
					}

					if (jsonStr != null && !"".equals(jsonStr)) {
						String[] string = jsonStr.split(";");
						if(string.length == 2){
							String path = Global.SDCARD_FJ_LOCAL_PATH + "cache/";
							String filepash = "d90b3b1f-17ae-46e8-adc7-c1b82e658bbc"+string[1];
							File fl = new File(path);
							File nameFl = new File(path + filepash);
							try {
								if (!fl.exists()) {
									fl.mkdirs();
								}

								FileOutputStream os = new FileOutputStream(nameFl);

								String[] ss = string[0].split(",");
								byte[] red_buf = new byte[ss.length];
								for (int i = 0; i < ss.length; i++) {
									int value = Integer.valueOf(ss[i]);
//									os.write(value);
									red_buf[i] = (byte)value;
								}

								os.write(red_buf);
								os.flush();
								os.close();
								pString = nameFl.getAbsolutePath();
								flag = 3;
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							flag = 2;
						}
					} else {

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				flag = 0;
			}

			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (yutuLoading != null) {
				yutuLoading.dismissDialog();
			}

			switch (result) {
			case -1:
				Toast.makeText(context, "内容无法解析", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(context, "网络暂时不可用", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(context, "连接服务器异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(context, "暂无内容", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				DisplayUitl.openfile(pString, context);
				break;
			default:
				break;
			}

			super.onPostExecute(result);
		}

	}
	
	private class CustomDwAsyncTask extends AsyncTask<Void, Void, Integer> {
		private YutuLoading loading;
		private String useridString;
		private ArrayList<HashMap<String, Object>> list;
		
		public CustomDwAsyncTask(String useridString) {
			// TODO Auto-generated constructor stub
			this.useridString = useridString;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading = new YutuLoading(context);
			loading.setCancelable(false);
			loading.setLoadMsg("正在更新数据，请稍候", "");
			loading.showDialog();
		}

		@Override
		protected Integer doInBackground(Void... p) {
			int flag = -1;
			if (!Net.checkURL(Global.getGlobalInstance()
					.getSystemurl())) {
				flag = 0;
				return flag;
			}
			String result = null;
			String methodName = "RYDWSelectTopOne";
			
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userID", useridString);
//			String token = "";
//			try {
//				token = DESSecurity.encrypt(methodName);
//			} catch (Exception e1) {
//
//				e1.printStackTrace();
//			}
//			param.put("token", token);
			params.add(param);
			
			try {
				result = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
				if(result != null) {
					list = JsonHelper.paseJSON(result);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			flag = 1;
			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			loading.dismissDialog();
			switch (result) {
			case 0:
				Toast.makeText(context, "网络不给力啊，无法获取数据！", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				if(list != null && list.size() > 0){
					ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select u_realname from pc_users where userid='"+useridString+"'");
					Intent intent = new Intent();
					intent.setAction("RYDW");
					intent.putExtra("number", 1);
					intent.putExtra("jd", Double.parseDouble(list.get(0).get("WeiDu").toString()));
					intent.putExtra("wd", Double.parseDouble(list.get(0).get("JingDu").toString()));
//					intent.putExtra("jd", Double.parseDouble("112.513"));
//					intent.putExtra("wd", Double.parseDouble("35.625"));
					intent.putExtra("name", data.get(0).get("u_realname").toString());
					intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.gis.MapActivity" );
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "暂无数据！", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
			
		}

	}

}
