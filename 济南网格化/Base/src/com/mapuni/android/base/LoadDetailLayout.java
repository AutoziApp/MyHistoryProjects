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
 * FileName: DetailLayout Description:��������ҳ��Ĳ���
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2013-1-15 ����03:50:49
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
	/** ������ͼ */
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
	 * @return the styleDetailed ����������ʽ
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
	 * Description:��������ҳ��Ĳ���
	 * 
	 * @param itemId
	 *            ��ǰ�����id
	 * @return MyScrollView ��ͼ
	 * @author ����� Create at: 2013-1-15 ����05:27:05
	 */
	public MyScrollView loadDetailed(String itemId) {
		queryTable = new TableLayout(context);
		/** ������ͼ */
		scrollView = new MyScrollView(context);
		scrollView.addView(queryTable);
		queryTable.setColumnStretchable(1, true);
		/** �õ���ѯ��Ŀ������ */
		addConent(itemId);
		return scrollView;
	}

	/**
	 * ���ش������������鲼��
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
	 * ��������ҳ��Ĳ���
	 * 
	 * @param itemId
	 * @param queryTable
	 */
	public void addConent(String itemId) {
		styleDetailed = businessObj.getStyleDetailed(context);

		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		/** ͨ��id��ѯ������ */
		detaild = businessObj.getDetailed(itemId);
		int itemCount = styleDetailed.size();
		// if(36 == itemId.length() || 12 == itemId.length()){
		qybm = itemId;
		// }
		/** ������ TableLayout �����ؼ� */
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
				/** name���Բ���Ҫ��ӵ��б� */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				/** mysqlΪ�Զ���sql��䣬�����Բ���Ҫ��ӵ��б� */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			// textView.setMaxEms(5);

			/** ��� */
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			row.setGravity(Gravity.CENTER_VERTICAL);
			EditText editText = null;
			String value = null;
			/** ��ť */
			if ("button".equals(map.get("style")) && !isAmend) {
				final Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("") || button.getText().toString().equals("null") || button.getText().toString() == null) {
					button.setText("���ظ���");
				}
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(button, rightLayoutParams);
				
				if ("blob".equals(map.get("type"))) {
					Global.getGlobalInstance().setItemId(itemId);
					button.setOnClickListener(new zhOnClick());
				} else if ("fsdx".equals(map.get("type"))) {
					button.setText("���Ͷ���");
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
									Toast.makeText(context, "���޸�����", Toast.LENGTH_SHORT).show();
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
				Log.e(TAG, "------------editext----------�ֶΣ�"+value+"----value:"+callStr);
				
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
				/** �ʼ� */
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
							Toast.makeText(context, "�����ַΪ�գ��޷������ʼ�", Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "�����ַ���Ϸ����޷������ʼ�", Toast.LENGTH_SHORT).show();
							return;
						} else {
							toemail(email);
						}

					}
				});
				/** webҳ */
			} else if ("webview".equals(map.get("style")) && !isAmend) {
				Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText("�鿴");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("") || otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(), "���޸�����Ϣ!", Toast.LENGTH_SHORT).show();
							return;
						}
						it.putExtra("otherstr", otherstr);
						it.putExtra("title", "��ϸ����");
						context.startActivity(it);
					}
				});

			}
			/** ������ͼ */
			else if ("textview".equals(map.get("style"))) {
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				String textvalue = String.valueOf(detaild.get(value));
				TextView text = new TextView(context);
				text.setMaxEms(text.getWidth());
				text.setId(index);
				try {
					if(value.equals("dangersigns") && textvalue != null && !textvalue.equals("")){//Σ�ձ��
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
			
			}/** ��γ�� */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final TextView wdeditText = new EditText(context);
				wdeditText.setTextColor(Color.BLACK);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** ����EditText���� */
				wdeditText.setText(String.valueOf(detaild.get("jd")) + "," + String.valueOf(detaild.get("wd")));
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(wdeditText, rightLayoutParams);
				/** html�������� */
			} else if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(context);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				/** ֧�ֽű� */
				mWebSettings.setJavaScriptEnabled(true);
				/** �������� */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
				// mWebSettings.setPluginsEnabled(true);
				// TODO setPluginsEnabled�Ѿ����ã����������
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
							it.putExtra("title", "���������Ϣ");
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
							it.putExtra("title", "���������Ϣ");
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
			/** ���ֱ༭�� */
			else {

				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath */
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** ���ʽʱ���� */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = String.valueOf(detaild.get(value));
				if (value.equals("industryname") && textvalue != null && !textvalue.equals("")) {
					// ����ҵ���Ĵ��Ż�������
					String sql = "select b.name  from T_WRY_QYJBXX a join industry b on a.hylb =  b.code and a.guid = '" + qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("qygm") && textvalue != null && !textvalue.equals("")) {
					// ����ҵ��ģ�Ĵ��Ż�������
					String sql = "select b.name  from T_WRY_QYJBXX a join wrygm b on a.qygm =  b.code and a.guid = '" + qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("fsclff") && textvalue != null && !textvalue.equals("")) {
					// ��ˮ��������ֵ��������
					String sql = "select name  from T_WRY_CLFS where type = '1'  and code='" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("jhff") && textvalue != null && !textvalue.equals("")) {
					// ������������ֵת��������
					String sql = "select name from T_WRY_CLFS where type = '2' and code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssyxzt") && textvalue != null && !textvalue.equals("")) {
					// ��ʩ����״̬��ֵ��������
					String sql = "select name  from YXQK where type='2' and code='" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("yxqk") && textvalue != null && !textvalue.equals("")) {
					// ������ʩ���������������������ֵ��������
					String sql = "select name  from YXQK where type='1' and code='" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rszl") && textvalue != null && !textvalue.equals("")) {
					// �������ۿ�������� ȼ������
					String sql = "select name from RSZL where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rsfs") && textvalue != null && !textvalue.equals("")) {
					// �������ۿ�������� ȼ�շ�ʽ
					String sql = "select name from RSFS where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfklx") && textvalue != null && !textvalue.equals("")) {
					// �������ۿ�������� �ŷſ�����
					String sql = "select name from PFKLX where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("sfazzxjksb") && textvalue != null && !textvalue.equals("")) {
					// �������ۿ�������� �Ƿ�װ���߼���豸
					String sql = "select optionName from  Whether  where optionId = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("sfqxgl") && textvalue != null && !textvalue.equals("")) {
					// �������ۿ�������� �Ƿ�װ���߼���豸
					String sql = "select optionName from  Whether  where optionId = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("hbqlb") && textvalue != null && !textvalue.equals("")) {
					// ��ҵ������Ϣ ���������
					String sql = "select name from BHQLB where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssly") && textvalue != null && !textvalue.equals("")) {
					// ��ҵ������Ϣ ��������
					String sql = "select valleyName from Valley where valleyCode = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfqxlb") && textvalue != null && !textvalue.equals("")) {
					// ��ˮ���ۿ�������� �ŷ�������
					String sql = "select name from PFQXLB where code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("pfgl") && textvalue != null && !textvalue.equals("") && !textvalue.equalsIgnoreCase("null")) {
					// ��ˮ���������ۿ�������� �ŷŹ���
					String sql = "select name from PFGL where  code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("wrylb") && textvalue != null && !textvalue.equals("")) {
					// ����ȾԴ���Ĵ��Ż�������
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

				}else if(value.equals("lsgx") && textvalue != null && !textvalue.equals("")){//������ϵ
					String sql = "select name from LSGX where  code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}else if(value.equals("jjlx") && textvalue != null && !textvalue.equals("")){//��������
					String sql = "select name from JJLX where  code = '" + textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}else if(value.equals("xzqh") && textvalue != null && !textvalue.equals("")){//��������
					String sql = "select * from Region where  regioncode = '"+textvalue+"'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// ��ȡ�û�ְ��
				if ("zw".equals(value)) {
					textvalue = transitToChinese(Integer.parseInt(detaild.get("zw").toString()));
				}
				// ��ȡ�û�ְ��
				if ("zz".equals(value)) {
					String sql = "select supercontext from gridusermapping where userid='" + detaild.get("userid") + "' group by blamerole";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// ִ����
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
				// ��ȡִ����
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));
					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '" + rwbh + "'");
					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("u_realname"));
							sb.append("��");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// ��ȡЭ����
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
					//������ҵ��I�ࣩ��ʡ�ؼ��ص�����ҵ��II�ࣩ��������������ҵ��III�ࣩ�������ơ�������ҵ��������ҵ
					if (textvalue != null && !textvalue.equals("")) {
						if ("1".equals(textvalue.trim())) {
							textvalue = "������ҵ��I�ࣩ";
						}else if("2".equals(textvalue.trim())){
							textvalue = "ʡ�ؼ��ص�����ҵ��II�ࣩ";
						}else if("3".equals(textvalue.trim())){
							textvalue = "������������ҵ��III�ࣩ";
						}else if("4".equals(textvalue.trim())){
							textvalue = "�����ơ�������ҵ";
						}else if("5".equals(textvalue.trim())){
							textvalue = "������ҵ";
						}
					} else {
						textvalue = "";
					}
				}
				
				if ("zdwry".equals(value)) {
					if (textvalue != null && !textvalue.equals("")) {
						if (1 == Integer.parseInt(textvalue.trim())) {
							// if("1".equals(textvalue.trim())){
							textvalue = "��";
						} else {
							textvalue = "��";
						}
					} else {
						textvalue = "";
					}
				}
				if ("sczt".equals(value)) {
					if (textvalue != null && !textvalue.equals("")) {
						if (1 == Integer.parseInt(textvalue.trim())) {
							// if("1".equals(textvalue.trim())){
							textvalue = "����";
						} else {
							textvalue = "ͣ��";
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
					// ˳�µĹ̶���
					textvalue = "��ɽ��";
				}

				if ("ssqx".equals(value)) {
					String xzqhStr = String.valueOf(detaild.get("xzqh"));
					String xzqhNameCounty = SqliteUtil.getInstance().getDepidByDepName(
							"select regionname from region where RegionCode" + " = '" + xzqhStr + "'");
					textvalue = xzqhNameCounty;
					// TODO 2015.11.07
					// ˳�µĹ̶���
					textvalue = "˳����";
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
				/** ����EditText���� */
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

		/** ������ TableLayout �����ؼ� */
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter == 0) {
				counter++;
				/** name���Բ���Ҫ��ӵ��б� */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				/** mysqlΪ�Զ���sql��䣬�����Բ���Ҫ��ӵ��б� */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			// textView.setMaxEms(5);

			/** ��� */
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT).toString();
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			EditText editText = null;
			String value = null;
			/** ��ť */
			if ("button".equals(map.get("style")) && !isAmend) {
				Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("") || button.getText().toString() == null) {
					button.setText("��������");
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
				/** �ʼ� */
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
							Toast.makeText(context, "�����ַΪ�գ��޷������ʼ�", Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "�����ַ���Ϸ����޷������ʼ�", Toast.LENGTH_SHORT).show();
							return;
						} else {
							toemail(email);
						}

					}
				});
				/** webҳ */
			} else if ("webview".equals(map.get("style")) && !isAmend) {
				Button button = new Button(context);
				button.setMaxEms(button.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				button.setId(index);
				button.setText("�鿴");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("") || otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(), "���޸�����Ϣ!", Toast.LENGTH_SHORT).show();
							return;
						}
						it.putExtra("otherstr", otherstr);
						it.putExtra("title", "��ϸ����");
						context.startActivity(it);
					}
				});

			}
			/** ������ͼ */
			else if ("textview".equals(map.get("style"))) {
				TextView text = new TextView(context);
				text.setMaxEms(text.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				text.setId(index);
				text.setText(String.valueOf(detaild.get(value)));
				row.addView(text, rightLayoutParams);

			}/** ��γ�� */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final EditText jwdeditText = new EditText(context);
				jwdeditText.setFocusable(false);
				jwdeditText.setMaxEms(jwdeditText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				jwdeditText.setId(index);
				String textvalue = String.valueOf(detaild.get(value));
				/** ����EditText���� */
				jwdeditText.setText(textvalue);
				rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(jwdeditText, rightLayoutParams);
				if (!isAmend) {
					jwdeditText.setFocusableInTouchMode(false);
				} else {
				}
				/** html�������� */
			} else if ("zhengwen".equals(map.get("style"))) {
				int zwindex = index;

				String value1 = styleDetailed.get(zwindex).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				final String otherstr = String.valueOf(detaild.get(value1));
				WebView wv = new WebView(context);
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				wv.setId(index);
				WebSettings mWebSettings = wv.getSettings();
				/** ֧�ֽű� */
				mWebSettings.setJavaScriptEnabled(true);
				/** �������� */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
				// mWebSettings.setPluginsEnabled(true);
				// TODO setPluginsEnabled�Ѿ����ã����������
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
							it.putExtra("title", "���������Ϣ");
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
							it.putExtra("title", "���������Ϣ");
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

			}/** ���ֱ༭�� */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath */
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** ���ʽʱ���� */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = String.valueOf(detaild.get(value));
				// ��ȡ�û�ְ��
				if ("zw".equals(value)) {
					textvalue = transitToChinese(Integer.parseInt(detaild.get("zw").toString()));
				}
				// ��ȡ�û�ְ��
				if ("zz".equals(value)) {
					String sql = "select supercontext from gridusermapping where userid='" + detaild.get("userid") + "' group by blamerole";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// ��ȡִ����
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));

					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '" + rwbh
									+ "'");

					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("u_realname"));
							sb.append("��");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// ��ȡЭ����
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
				/** ����EditText���� */
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
	 * ������������ҳ��Ĳ���
	 * 
	 * @param itemId
	 * @param queryTable
	 */
	public void addRWXXConent(String itemId, Bundle RWBundle) {
		styleDetailed = businessObj.getStyleDetailed(context);

		HashMap<String, Object> querytj = new HashMap<String, Object>();
		querytj.put("id", itemId);
		/** ͨ��id��ѯ������ */
		detaild = businessObj.getDetailed(itemId);
		int itemCount = styleDetailed.size();

		/** ������ TableLayout �����ؼ� */
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		TableRow row = null;
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			boolean finnaly_modify = false;
			Map<String, Object> map = styleDetailed.get(index);
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("name") && counter == 0) {
				counter++;
				/** name���Բ���Ҫ��ӵ��б� */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).equals("mysql")) {
				counter++;
				/** mysqlΪ�Զ���sql��䣬�����Բ���Ҫ��ӵ��б� */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			/** ��� */
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

			}/** ���ֱ༭�� */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT).toString();
				/** �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath */
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** ���ʽʱ���� */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = String.valueOf(detaild.get(value));
				// ��ȡִ����
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));
					ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
							"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '" + rwbh
									+ "'");
					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("u_realname"));
							sb.append("��");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// ��ȡЭ����
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
				/** ����EditText���� */
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
	 * ���ش������Ĳ���
	 */
	protected MyScrollView loadlayout(String itemID, ArrayList<HashMap<String, Object>> attachment, String filepath) {
		queryTable = new TableLayout(context);
		queryTable1 = new TableLayout(context);// �����ҵ�б�
		queryTable2 = new TableLayout(context);
		scrollView = new MyScrollView(context);
		scrollView.setScrollContainer(true);
		scrollView.setFocusable(true);
		allLayout = new LinearLayout(context);// �������ϼӵ��ܲ���
		allLayout.setOrientation(LinearLayout.VERTICAL);// ���ܲ��ּ��ش�ֱ����
		LinearLayout tableLayout = new LinearLayout(context);// �����Ĳ���
		tableLayout.setOrientation(LinearLayout.VERTICAL);// ���ܲ��ּ��ش�ֱ����
		treeLayout = new LinearLayout(context);// �����Ĳ���
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

	// װ�����ķ���
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
		String[] groups = { "   ����" };
		for (int i = 0; i < groups.length; i++) {
			if (attachment != null) {

				TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
				node.parent = groups[i];
				// ���������������ݱ������ص�����
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
		treeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, TreeViewAdapter.ItemHeight * rowcount));// ���Ĳ��ֲ���
		// �ж��Ƿ��и���������У���������ʾ�����б�
		if (attachment != null && attachment.size() > 0) {
			allLayout.addView(treeLayout);
		}
		scrollView.addView(allLayout);
		// ������ӵ����¼�
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
	 * FileName: DetailLayout Description: �жϴ򿪲�ͬ�ĸ���
	 * 
	 * @author �����
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����03:34:42
	 */
	class OnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			String filepash = "";
			BaseClass baseclass = (BaseClass) businessObj;
			String[] tableadd = baseclass.GetTableName().split("_");
			if (button.getText().toString().equals("��������")) {
				Toast.makeText(context, "��������!", Toast.LENGTH_SHORT).show();
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
	 * FileName: DetailLayout Description: �ж�ת���������ļ�����ͨ�ļ�
	 * 
	 * @author �����
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����03:35:12
	 */
	class zhOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			BaseClass baseclass = (BaseClass) businessObj;
			String[] tableadd = baseclass.GetTableName().split("_");
			if (button.getText().toString().equals("��������")) {
				Toast.makeText(context, "��������!", Toast.LENGTH_SHORT).show();
				return;
			}
			// String itemId=Global.getGlobalInstance().getItemId();
			// String
			// filepash="/sdcard/mapuni/MobileEnforcement/fj/"+tableadd[tableadd.length-1]+"/"+(String)button.getText();
			// //�������ƽ������ļ�
			// FileHelper fh=new FileHelper();
			// ArrayList<HashMap<String, Object>>
			// list=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select * from t_zsk_yajal where guid='"+itemId+"'");
			// fh.convertBase64StringToLocalFile(list.get(0).get("fjswf").toString(),filepash,(String)button.getText());
			// //�򿪴�õ��ļ�
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
				// if (ffl.isDirectory()) { // Ŀ¼ʱ
				// FileUtils.copyDirectoryToDirectory(ffl, tfl);
				// } else if (ffl.isFile()) { // �ļ�ʱ
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
	 * Description: ����绰
	 * 
	 * @param tal
	 *            �绰���� void
	 * @author ����� Create at: 2012-12-5 ����03:35:31
	 */
	public void tocall(String tal) {
		if (tal.equals("")) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tal));
		context.startActivity(intent); // ��ʼ��ͼ(������绰)
	}

	/**
	 * Description: ��ת�������ʼ�ҳ��
	 * 
	 * @param add
	 *            �ʼ���ַ void
	 * @author ����� Create at: 2012-12-5 ����03:35:50
	 */
	public void toemail(String add) {
		Uri uri = Uri.parse("mailto:" + add);

		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "�ʼ��޷����ͣ�������������", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}

	/**
	 * Description: �ж������ַ�ϲ��Ϸ�
	 * 
	 * @param email
	 *            �����ַ
	 * @return �Ϸ�����true
	 * @author Administrator
	 * @Create at: 2013-4-15 ����4:22:33
	 */
	public boolean checkEmail(String email) {

		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	// �����û�ְ���Ż�ȡ��������
	public String transitToChinese(int nodeid) {
		String position = "";
		switch (nodeid) {
		case 0:
			position = "�칫��";
			break;
		case 1:
			position = "�ֳ�";
			break;
		case 2:
			position = "�ֳֹܾ�";
			break;
		case 3:
			position = "�Ƴ�";
			break;
		default:
			position = "ִ����";
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
