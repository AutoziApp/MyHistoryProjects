/**
 * 
 */
package com.mapuni.android.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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

import com.digitalchina.gallery.ImageGalleryActivity;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.attachment.T_BAS_Attachment;
import com.mapuni.android.base.adapter.TreeViewAdapter;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.controls.listview.MyScrollView;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

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

	ArrayList<HashMap<String, Object>> listName;
	private String fkid;
	private ArrayList<HashMap<String, Object>> styleDetailed;
	private HashMap<String, Object> detaild;
	private View view;

	private final boolean isAmend;

	public LoadDetailLayout(Context context, IDetailed businessObj,
			boolean isAmend) {
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
	public MyScrollView loadTreeDetailed(String itemId,
			ArrayList<HashMap<String, Object>> attachment, String filepath,
			Bundle RWBundle) {

		MyScrollView scrollView = loadlayout(itemId, attachment, filepath);
		addRWXXConent(itemId, RWBundle);
		return scrollView;
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
			loading.setLoadMsg("���ڸ������ݣ����Ժ�", "");
			loading.showDialog();
		}

		@Override
		protected Integer doInBackground(Void... p) {
			int flag = -1;
			if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
				flag = 0;
				return flag;
			}
			String result = null;
			String methodName = "RYDWSelectTopOne";

			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("userID", useridString);
			// String token = "";
			// try {
			// token = DESSecurity.encrypt(methodName);
			// } catch (Exception e1) {
			//
			// e1.printStackTrace();
			// }
			// param.put("token", token);
			params.add(param);

			try {
				result = (String) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_STRING, true);
				if (result != null) {
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
				Toast.makeText(context, "���粻���������޷���ȡ���ݣ�", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				if (list != null && list.size() > 0) {
					ArrayList<HashMap<String, Object>> data = SqliteUtil
							.getInstance().queryBySqlReturnArrayListHashMap(
									"select u_realname from pc_users where userid='"
											+ useridString + "'");
					Intent intent = new Intent();
					intent.setAction("RYDW");
					intent.putExtra("number", 1);
					intent.putExtra(
							"jd",
							Double.parseDouble(list.get(0).get("JingDu")
									.toString()));
					intent.putExtra(
							"wd",
							Double.parseDouble(list.get(0).get("WeiDu")
									.toString()));
					// intent.putExtra("jd", Double.parseDouble("112.513"));
					// intent.putExtra("wd", Double.parseDouble("35.625"));
					intent.putExtra("name", data.get(0).get("u_realname")
							.toString());
					intent.setClassName("com.mapuni.android.MobileEnforcement",
							"com.mapuni.android.gis.MapActivity");
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "�������ݣ�", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}

		}

	}

	private ArrayList<HashMap<String, Object>> showfujian(String guid,
			String fkunit) {
		T_Attachment t_attach = new T_Attachment();
		String sql = "select * from T_Attachment where  fk_id ='" + guid
				+ "' and fk_unit=" + t_attach.getCode(fkunit);
		ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);
		return list;
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
				/** name���Բ���Ҫ��ӵ��б� */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("mysql")) {
				counter++;
				/** mysqlΪ�Զ���sql��䣬�����Բ���Ҫ��ӵ��б� */
				continue;
			}
			if ("true".equals(map.get("finnaly_modify"))) {
				finnaly_modify = true;
			}
			row = new TableRow(context);
			TextView textView = new TextView(context);
			textView.setMaxEms(7);

			/** ��� */
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT)
					.toString();
			
			if ("ʱ��:".equals(hint)) {
				hint="����ʱ��:";
			}
			textView.setTextColor(android.graphics.Color.BLACK);
			textView.setText(hint);
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);
			EditText editText = null;
			String value = null;
			/** ��ť */
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
					button.setText("���ظ���");
				}
				rightLayoutParams = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rightLayoutParams.setMargins(0, 0, 15, 0);
				row.addView(button, rightLayoutParams);
				if ("blob".equals(map.get("type"))) {
					Global.getGlobalInstance().setItemId(itemId);
					button.setOnClickListener(new zhOnClick());
				} else if ("ZXWZ".equals(value)) {
					button.setText("�鿴");
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new CustomDwAsyncTask(detaild.get("userid")
									.toString()).execute();
						}
					});
				} else if ("GJCX".equals(value)) {
					button.setText("�鿴");
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.putExtra("userid", detaild.get("userid")
									.toString());
							intent.setClassName(
									"com.mapuni.android.MobileEnforcement",
									"com.mapuni.android.infoQuery.RYDWActivity");
							context.startActivity(intent);
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
							String attchDM = hashmap.get(button.getId())
									.toString();
							ArrayList<String> arrayTotal = new ArrayList<String>();
							if ("QYHJGLZD_FJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_QYHJGLZD where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();

								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(qybm, attchDM,
								// context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total = array.size();
								attch = attchDM;
							} else if ("HBSXLXQK_GYT".equals(attchDM)
									|| "XMSPDZDA".equals(attchDM)
									|| "SSCSPDZDA".equals(attchDM)
									|| "HBYSDZDA".equals(attchDM)
									|| "HBSXLXQK_GYLCT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_HBSXLXQK where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();

								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("SCSSYXQK_SCGYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_SCSSYXQK where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("FSZLSSYXQK_GYT".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSZLSSXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("FQZLSSYXQK_GYDZDA".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FQZLSS where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("FQPWKQK_SB".equals(attchDM)
									|| "FQPWKQK_YXQK".equals(attchDM)
									|| "FQPWKQK_PFKQK".equals(attchDM)
									|| "FQPWKQK_QTBCZL".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FQPFKXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("FSPWKQK_YXQK".equals(attchDM)
									|| "FSPWKQK_PFKQK".equals(attchDM)
									|| "FSPWKQK_SB".equals(attchDM)
									|| "FSPWKQK_QTBCZL".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_FSPFKXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("LSZFQK_CZWT".equals(attchDM)
									|| "LSZFQK_CLJG".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_LSZFQK where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else if ("GFHZ_GFZZQK".equals(attchDM)
									|| "GFHZ_LYQK".equals(attchDM)
									|| "GFHZ_QTBCZL".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_GFHZ where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								attch = attchDM;
							} else if ("WFXX_GFZZQK".equals(attchDM)
									|| "WFXX_LYQK".equals(attchDM)
									|| "WFXX_QTBCZL".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_WFXX where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								attch = attchDM;
							} else if ("HJFXFFSS_SGFSSJCLLXT".equals(attchDM)
									|| "HJFXFFSS_YJYA".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_HJFXFFSS where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								attch = attchDM;
							} else if ("exp_photo".equals(attchDM)) {
								fkid = qybm;
								attch = attchDM;
							} else if ("ZYLDFJ".equals(attchDM)) {
								String guidSql = "select guid from T_WRY_WXFWZYDA where  id = '"
										+ qybm + "'";
								ArrayList<HashMap<String, Object>> list = SqliteUtil
										.getInstance()
										.queryBySqlReturnArrayListHashMap(
												guidSql);
								fkid = list.get(0).get("guid").toString();
								// if (list != null && list.size() > 0) {
								// for (int j = 0; j < list.size(); j++) {
								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(list.get(j).get("guid").toString(),
								// attchDM, context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total += array.size();
								// }
								// }
								// total = arrayTotal.size();
								attch = attchDM;
							} else {
								fkid = qybm;

								// HashMap<String, Object> hashmap =
								// fileHelper.downFileByQyid(qybm, attchDM,
								// context);
								// ArrayList<String> array = (ArrayList<String>)
								// hashmap.get("1");
								// extra = (String) hashmap.get("2");
								// for (String str : array) {
								// arrayTotal.add(str);
								// }
								// total = array.size();
								attch = attchDM;
							}

							// if (total > 0) {
							// Intent intent = new Intent(context,
							// ImageGalleryActivity.class);
							// Bundle bundle = new Bundle();
							// bundle.putSerializable("arrayTotal", arrayTotal);
							// bundle.putString("attch", attch);
							// intent.putExtras(bundle);
							// context.startActivity(intent);
							// } else {
							// if ("no".equals(extra)) {
							// Toast.makeText(context, "���޸�����",
							// Toast.LENGTH_SHORT).show();
							// }
							//
							// }

							listName = showfujian(fkid, attchDM);

							// if (listName.size() == 1) {
							// HashMap<String, Object> hashmap =
							// fileHelper.downFileByQyid(fkid, attchDM,
							// context);
							// ArrayList<String> array = (ArrayList<String>)
							// hashmap.get("1");
							// extra = (String) hashmap.get("2");
							// for (String str : array) {
							// arrayTotal.add(str);
							// }
							// total = array.size();
							// attch = attchDM;
							// if (total > 0) {
							// Intent intent = new Intent(context,
							// ImageGalleryActivity.class);
							// Bundle bundle = new Bundle();
							// bundle.putSerializable("arrayTotal", arrayTotal);
							// bundle.putString("attch", attch);
							// intent.putExtras(bundle);
							// context.startActivity(intent);
							// } else {
							// if ("no".equals(extra)) {
							// Toast.makeText(context, "���޸�����",
							// Toast.LENGTH_SHORT).show();
							// }
							//
							// }
							//
							// }else
							if (listName.size() >= 1) {
								ArrayList<T_BAS_Attachment> attachment = new ArrayList<T_BAS_Attachment>();

								for (int i = 0; i < listName.size(); i++) {
									T_BAS_Attachment attachmentItem = new T_BAS_Attachment();
									attachmentItem.setGuid(listName.get(i)
											.get("guid").toString());
									attachmentItem.setFileName(listName.get(i)
											.get("filename") + "");
									attachmentItem.setFilePath(listName.get(i)
											.get("filepath") + "");
									attachmentItem.setExtension(listName.get(i)
											.get("extension") + "");
									attachmentItem.setFK_Id(listName.get(i)
											.get("fk_id") + "");
									attachmentItem.setFK_Unit(listName.get(i)
											.get("fk_unit") + "");
									attachmentItem.setLinkUrl(listName.get(i)
											.get("linkurl") + "");
									attachment.add(attachmentItem);
								}

								ButtonListDialog b = new ButtonListDialog(
										context, attachment);
								b.show();

							} else {
								Toast.makeText(context, "���޸�����",
										Toast.LENGTH_SHORT).show();
							}

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
							Toast.makeText(context, "�����ַΪ�գ��޷������ʼ�",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "�����ַ���Ϸ����޷������ʼ�",
									Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText("�鿴");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("")
								|| otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(),
									"���޸�����Ϣ!", Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				text.setId(index);
				text.setText(String.valueOf(detaild.get(value)));
				row.addView(text, rightLayoutParams);

			}/** ��γ�� */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final int jdIndext = index - 1;
				final TableLayout t = new TableLayout(context);
				t.setColumnStretchable(0, true);
				TableRow wdRow = new TableRow(context);
				final EditText jwdeditText = new EditText(context);
				jwdeditText.setFocusable(false);
				jwdeditText.setMaxEms(jwdeditText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				jwdeditText.setId(index);
				String textvalue = String.valueOf(detaild.get(value));
				/** ����EditText���� */
				jwdeditText.setText(textvalue);
				ImageView image = new ImageView(context);
				Drawable draw = context.getResources().getDrawable(
						R.drawable.tdfg_location);
				// image.setImageDrawable(draw);
				wdRow.addView(jwdeditText);
				wdRow.addView(image);
				t.addView(wdRow);
				row.addView(t);

				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						gislocation.locationPrepare(context);
						location = gislocation.getLocation(context);
						if (location == null) {
							Toast.makeText(context.getApplicationContext(),
									"��ʱ�޷���������ź�!", Toast.LENGTH_SHORT).show();
							return;
						}
						/** γ�� */
						String jd = location.getLongitude() + "";
						/** ���� */
						String wd = location.getLatitude() + "";
						jwdeditText.setText(wd);
						EditText jdeditext = (EditText) t
								.findViewById(jdIndext);
						jdeditext.setText(jd);

					}
				});

				/** html�������� */
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
				/** ֧�ֽű� */
				mWebSettings.setJavaScriptEnabled(true);
				/** �������� */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
				// mWebSettings.setPluginsEnabled(false);

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
							it.putExtra("title", "���������Ϣ");
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
							it.putExtra("title", "���������Ϣ");
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

			}/** ���ֱ༭�� */
			else {

				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				/** �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath */
				
				if ("updatetime".equals(value)) {
					value="worktime";
				}
				editText.setId(index);
				// editText.setm(0, 0, 15, 0);
				/** ���ʽʱ���� */
				if (finnaly_modify) {
					editText.setFocusableInTouchMode(false);
				} else if (!isAmend) {
					editText.setFocusableInTouchMode(false);
				}
				String textvalue = "";
				if ("worktime".equals(value)) {
					String worktime = detaild.get(value).toString();
					if (!TextUtils.isEmpty(worktime)||!"null".equals(worktime)||!"NUll".equals(worktime)) {
						String[] split = worktime.split("T");
						textvalue=split[0];
					}
				}else{
					textvalue = String.valueOf(detaild.get(value));
				}
				
				
				
			 if (value.equals("industryname") && textvalue != null
						&& !textvalue.equals("")) {
					// ����ҵ���Ĵ��Ż�������
					String sql = "select b.name  from T_WRY_QYJBXX a join industry b on a.hylb =  b.code and a.guid = '"
							+ qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("qygm") && textvalue != null
						&& !textvalue.equals("")) {
					// ����ҵ��ģ�Ĵ��Ż�������
					String sql = "select b.name  from T_WRY_QYJBXX a join wrygm b on a.qygm =  b.code and a.guid = '"
							+ qybm + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if ((value.equals("wastecategory") || value
						.equals("industrysources"))
						&& textvalue != null
						&& !textvalue.equals("")) {
					// Σ��-������� ��ҵ��Դ
					String sql = "select name from WXFWLB where CODE = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if ((value.equals("wastename") || value
						.equals("hazardouscharacteristics"))
						&& textvalue != null && !textvalue.equals("")) {
					// Σ��-Σ�շ�������
					String sql = "select name from WXFWMC where CODE = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("fsclff") && textvalue != null
						&& !textvalue.equals("")) {
					// ��ˮ��������ֵ��������
					String sql = "select name  from T_WRY_CLFS where type = '1'  and code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("jhff") && textvalue != null
						&& !textvalue.equals("")) {
					// ������������ֵת��������
					String sql = "select name from T_WRY_CLFS where type = '2' and code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if ((value.equals("clfs") || value.equals("processmode"))
						&& textvalue != null && !textvalue.equals("")) {
					// Σ�ϴ�������ֵת��������
					String sql = "select name from T_WRY_CLFS where type = '4' and code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssyxzt") && textvalue != null
						&& !textvalue.equals("")) {
					// ��ʩ����״̬��ֵ��������
					String sql = "select name  from YXQK where type='2' and code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("yxqk") && textvalue != null
						&& !textvalue.equals("")) {
					// ������ʩ���������������������ֵ��������
					String sql = "select name  from YXQK where type='1' and code='"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rszl") && textvalue != null
						&& !textvalue.equals("")) {
					// �������ۿ�������� ȼ������
					String sql = "select name from RSZL where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("rsfs") && textvalue != null
						&& !textvalue.equals("")) {
					// �������ۿ�������� ȼ�շ�ʽ
					String sql = "select name from RSFS where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("gkylb") && textvalue != null
						&& !textvalue.equals("")) {
//					// ����Դ���
//					String sql = "select name from GKYLB where code = '"
//							+ textvalue + "'";
					
					if (textvalue != null && !textvalue.equals("")
							&& !textvalue.equals("null")) {
						
						 if("1".equals(textvalue.trim())){
							 
								textvalue = "����";
						 }else if("2".equals(textvalue.trim())){
								textvalue = "ʡ��";
						 }else{
							 textvalue = "�п�";
						 }
						
					}
			
				} else if (value.equals("pfklx") && textvalue != null
						&& !textvalue.equals("")) {
					// �������ۿ�������� �ŷſ�����
					String sql = "select name from PFKLX where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("qjshqk") && textvalue != null
						&& !textvalue.equals("")) {
					// ���������
					String sql = "select name from QJSHQK where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if ((value.equals("sfys") || value.equals("sfazzxjksb") || value
						.equals("pfksfgfh"))
						&& textvalue != null
						&& !textvalue.equals("")) {
					// �������ۿ�������� �Ƿ�װ���߼���豸
					String sql = "select optionName from  Whether  where optionId = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("sfqxgl") && textvalue != null
						&& !textvalue.equals("")) {
					// �������ۿ�������� �Ƿ�װ���߼���豸
					String sql = "select optionName from  Whether  where optionId = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("hbqlb") && textvalue != null
						&& !textvalue.equals("")) {
					// ��ҵ������Ϣ ���������
					String sql = "select name from BHQLB where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("ssly") && textvalue != null
						&& !textvalue.equals("")) {
					// ��ҵ������Ϣ ��������
					String sql = "select valleyName from Valley where valleyCode = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("pfqxlb") && textvalue != null
						&& !textvalue.equals("")) {
					// ��ˮ���ۿ�������� �ŷ�������
					String sql = "select name from PFQXLB where code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);

				} else if (value.equals("pfgl") && textvalue != null
						&& !textvalue.equals("")
						&& !textvalue.equalsIgnoreCase("null")) {
					// ��ˮ���������ۿ�������� �ŷŹ���
					String sql = "select name from PFGL where  code = '"
							+ textvalue + "'";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				} else if (value.equals("wrylb") && textvalue != null
						&& !textvalue.equals("")) {
					// ����ȾԴ���Ĵ��Ż�������
					if (textvalue.contains(",")) {
						StringBuilder sb = new StringBuilder();
						String[] arrays = textvalue.split(",");
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

				} else if (value.equals("lsgx") && textvalue != null
						&& !textvalue.equals("")) {
					// ������ϵ�ĳɳ�����
					try {
						String sql = "select b.name  from T_WRY_QYJBXX a join lsgx b on a.lsgx =  b.code and a.guid = '"
								+ qybm + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					} catch (Exception e) {
						// TODO: handle exception
						textvalue = "";
					}

				} else if (value.equals("gxgs") && textvalue != null
						&& !textvalue.equals("")) {
					// ������ϵ�ĳɳ�����
					try {
						String sql = "select RegionName from Region where RegionCode='"+textvalue.trim()+"'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					} catch (Exception e) {
						// TODO: handle exception
						textvalue = "";
					}

				}
				//��ҵ���
//				else if (value.equals("hylb") && textvalue != null
//						&& !textvalue.equals("")) {
//					try {
//						String sql = "select NAME from Industry where CODE='"+textvalue.trim()+"'";
//						textvalue = SqliteUtil.getInstance().getDepidByDepName(
//								sql);
//					} catch (Exception e) {
//						// TODO: handle exception
//						textvalue = "";
//					}
//
//				}
				else if (value.equals("jjlx") && textvalue != null
						&& !textvalue.equals("")) {
					// ������ϵ�ĳɳ�����
					try {
						String sql = "select b.name  from T_WRY_QYJBXX a join jjlx b on a.jjlx =  b.code and a.guid = '"
								+ qybm + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					} catch (Exception e) {
						// TODO: handle exception
						textvalue = "";

					}

				} else if (value.equals("hjxxxydj") && textvalue != null
						&& !textvalue.equals("")) {
					// �������õȼ�:
					try {
						String sql = "select b.name  from T_WRY_QYJBXX a join hjxxxydj b on a.hjxxxydj =  b.code and a.guid = '"
								+ qybm + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					} catch (Exception e) {
						textvalue = "";
					}

				} else if (value.equals("molecule")) {
					// Σ��Ʒ,����ʽ:
					try {
						String sql2 = "select molecule  from T_ZSK_DangerChem where itemcode = '"
								+ qybm + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql2);
						textvalue = Html.fromHtml(textvalue).toString();
					} catch (Exception e) {
						textvalue = "";
					}
						
				} else if (value.equals("tcrq") && textvalue != null
						&& !textvalue.equals("")) {
					// ����ʱ��--tcrq
					try {
						String sql = "select tcrq  from T_WRY_QYJBXX '"+ qybm + "'";
						textvalue = SqliteUtil.getInstance().getDepidByDepName(
								sql);
					} catch (Exception e) {
						textvalue = "";
					}

				} 
				
				else if (value.equals("exp_sex") && textvalue != null
						&& !textvalue.equals("")) {
					// ר�ҿ�:�Ա�
					try {
						String sql2 = "select Exp_Sex  from PC_Exp where ExpId = '"
								+ qybm + "'";
						String textvalue2 = SqliteUtil.getInstance()
								.getDepidByDepName(sql2);
						if ("0".equals(textvalue2)) {
							textvalue = "��";
						} else if ("1".equals(textvalue2)) {
							textvalue = "Ů";
						} else {
							textvalue = "";
						}
					} catch (Exception e) {
						textvalue = "";
					}

				} else if (value.equals("exp_highedt") && textvalue != null
						&& !textvalue.equals("")) {
					// ר�ҿ�:�Ա�
					try {
						String sql2 = "select Exp_HighEdt  from PC_Exp where ExpId = '"
								+ qybm + "'";
						String textvalue2 = SqliteUtil.getInstance()
								.getDepidByDepName(sql2);
						if ("1".equals(textvalue2)) {
							textvalue = "Сѧ";
						} else if ("2".equals(textvalue2)) {
							textvalue = "����";
						} else if ("3".equals(textvalue2)) {
							textvalue = "����";
						} else if ("4".equals(textvalue2)) {
							textvalue = "��ר";
						} else if ("5".equals(textvalue2)) {
							textvalue = "��ר";
						} else if ("6".equals(textvalue2)) {
							textvalue = "����";
						} else if ("7".equals(textvalue2)) {
							textvalue = "˶ʿ";
						} else if ("8".equals(textvalue2)) {
							textvalue = "��ʿ";
						} else if ("9".equals(textvalue2)) {
								textvalue = "��ʿ��";
							}else {
							textvalue = "";
						}
					} catch (Exception e) {
						textvalue = "";
					}

				} else if (value.equals("exp_political") && textvalue != null
						&& !textvalue.equals("")) {
					// ר�ҿ�:�Ա�
					try {
						String sql2 = "select Exp_Political  from PC_Exp where ExpId = '"
								+ qybm + "'";
						String textvalue2 = SqliteUtil.getInstance()
								.getDepidByDepName(sql2);
						if ("1".equals(textvalue2)) {
							textvalue = "Ⱥ��";
						} else if ("2".equals(textvalue2)) {
							textvalue = "������Ա";
						} else if ("3".equals(textvalue2)) {
							textvalue = "Ԥ����Ա";
						} else if ("4".equals(textvalue2)) {
							textvalue = "�й���Ա";
						} else if ("5".equals(textvalue2)) {
							textvalue = "��������";
						} else {
							textvalue = "����";
						}
					} catch (Exception e) {
						textvalue = "";
					}

				}

				// ��ȡ�û�ְ��
				if ("zw".equals(value)) {
					if (detaild.get("zw").toString() != null
							&& !"".equals(detaild.get("zw").toString())) {
						textvalue = transitToChinese(Integer.parseInt(detaild
								.get("zw").toString()));
					} else {
						textvalue = "ϵͳ����Ա";
					}
				}
				// ��ȡ�û�ְ��
				if ("zz".equals(value)) {
					String sql = "select supercontext from gridusermapping where userid='"
							+ detaild.get("userid") + "' group by blamerole";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// ��֯��������
			
				// ��ȡִ����
				if ("zxr".equals(value)) {
					String rwbh = String.valueOf(detaild.get("rwbh"));
					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '"
											+ rwbh + "'");
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

					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select  SecondaryAuditUserId from ydzf_rwlc where tid='"
											+ rwbh
											+ "' and SecondaryAuditUserId <>'' group by tid");

					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("secondaryaudituserid"));
							sb.append(",");
						}
						String sbname = sb.deleteCharAt(sb.length() - 1)
								.toString();
						String[] array = sbname.split(",");
						StringBuilder sba = new StringBuilder();
						for (String a : array) {
							sba.append("'" + a + "',");
						}
						sba = sba.deleteCharAt(sba.length() - 1);
						String sql = "select U_RealName from PC_Users where UserID in ("
								+ sba.toString() + ")";
						ArrayList<HashMap<String, Object>> result1 = SqliteUtil
								.getInstance()
								.queryBySqlReturnArrayListHashMap(sql);
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
					String values = (String) textvalue.subSequence(0,
							textvalue.length() - 6);
					textvalue = values.replace("T", " ");
					if (textvalue != null && !textvalue.equals("")
							&& !textvalue.equalsIgnoreCase("null")
							&& textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				if ("exp_date".equals(value)) {
					if (null != textvalue && !"".equals(textvalue)) {
						String values = (String) textvalue.subSequence(0,
								textvalue.length() - 6);
						textvalue = values.replace("T", " ");
					}
					if (textvalue != null && !textvalue.equals("")
							&& !textvalue.equalsIgnoreCase("null")
							&& textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				try {
					if ("sfzhly".equals(value) || "sfszgqy".equals(value)
							|| "sfwbaxm".equals(value) || "zdwry".equals(value)) {
						if (textvalue != null && !textvalue.equals("")
								&& !textvalue.equals("null")) {
						//	if (1 == Integer.parseInt(textvalue.trim())||"true".equals(Integer.parseInt(textvalue.trim()))) {
								 if("1".equals(textvalue.trim())||"true".equals(textvalue.trim())){
								textvalue = "��";
							} else {
								textvalue = "��";
							}
						} else {
							textvalue = "��";
						}
					}
				} catch (Exception e) {

				}
				try {
					if ("sfwf".equals(value)
							|| "istransferofhazardouswaste".equals(value)
							|| "ishiddendangers".equals(value)
							|| "issettinghazardouswastemarks".equals(value)
							|| "ismanagementunitprocessing".equals(value)
							|| "isperformsystem".equals(value)
							|| "whetherhavegasrecoveryfacilities".equals(value)
							|| "ishazardouswastesystem".equals(value)) {
						if (textvalue != null && !textvalue.equals("")) {
							//if (1 == Integer.parseInt(textvalue.trim())) {
								 if("1".equals(textvalue.trim())||"true".equals(textvalue.trim())){
								textvalue = "��";
							} else {
								textvalue = "��";
							}
						} else {
							textvalue = "��";
						}
					}
				} catch (Exception e) {

				}
				try {
					if ("sswqwdlqy".equals(value) || "ybgyy".equals(value)) {
						if (textvalue != null && !textvalue.equals("")) {
						//	if (1 == Integer.parseInt(textvalue.trim())) {
							 if("1".equals(textvalue.trim())||"true".equals(textvalue.trim())){
							textvalue = "��";
							} else {
								textvalue = "��";
							}
						} else {
							textvalue = "��";
						}
					}
				} catch (Exception e) {

				}
				try {
					if ("sczt".equals(value)) {
						if (textvalue != null && !textvalue.equals("")) {
						//	if (1 == Integer.parseInt(textvalue.trim())) {

							 if("1".equals(textvalue.trim())||"true".equals(textvalue.trim())){
							textvalue = "����";
							} else {
								textvalue = "ͣ��";
							}
						} else {
							textvalue = "";
						}
					}
				} catch (Exception e) {

				}
				try {
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
				} catch (Exception e) {

				}

				if ("ssqx".equals(value)) {
					String xzqhStr = String.valueOf(detaild.get("xzqh"));
					String xzqhNameCounty = SqliteUtil.getInstance()
							.getDepidByDepName(
									"select regionname from region where RegionCode"
											+ " = '" + xzqhStr + "'");
					textvalue = xzqhNameCounty;
				}
				

				if ("ssxz".equals(value)) {
					String ssxzStr = String.valueOf(detaild.get("ssxz"));
					String ssxzNameCounty = SqliteUtil.getInstance()
							.getDepidByDepName(
									"select regionname from region where RegionCode"
											+ " = '" + ssxzStr + "'");
					textvalue = ssxzNameCounty;
				}
				// ����ʱ��
				try {
					if ("updatetime".equals(value) && textvalue.contains("T")) {
						String updatetime = textvalue.replace("T", " ");
						if (updatetime.contains("+")) {
							textvalue = updatetime.replace("+08:00", " ");
						} else {
							textvalue = "";
						}
					}
				} catch (Exception e) {
					textvalue = "";
				}
				// ��������
				if ("xzqh".equals(value)) {
					try {
						String xzqhStr = String.valueOf(detaild.get("xzqh"));
						String xzqhNameCounty = SqliteUtil.getInstance()
								.getDepidByDepName(
										"select regionname from region where RegionCode"
												+ " = '" + xzqhStr + "'");
						textvalue = xzqhNameCounty;
					} catch (Exception e) {
						// TODO: handle exception
						textvalue = "";
					}

				}
				try {
					// �����걨�Ƿ�Ǽ�
					if ("pwsbsfdj".equals(value)) {
						try {
							if (textvalue != null && !textvalue.equals("")) {
								if (1 == Integer.parseInt(textvalue.trim())) {
									// if("1".equals(textvalue.trim())){
									textvalue = "��";
								} else {
									textvalue = "��";
								}
							} else {
								textvalue = "��";
							}
						} catch (Exception e) {
							// TODO: handle exception
							textvalue = "��";
						}

					}
				} catch (Exception e) {

				}
				
//				if ("zdwry".equals(value)) {
//					if (textvalue != null && !textvalue.equals("")
//							&& !textvalue.equals("null")) {
//						if (1 == Integer.parseInt(textvalue.trim())||"true".equals(Integer.parseInt(textvalue.trim()))) {
//							// if("1".equals(textvalue.trim())){
//							textvalue = "��";
//						} else {
//							textvalue = "��";
//						}
//					} else {
//						textvalue = "��";
//					}
//				}
				/** ����EditText���� */
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
	}

	public View getDetailView(HashMap<String, Object> detaild) {
		scrollView = new MyScrollView(context);
		queryTable = new TableLayout(context);
		queryTable.setColumnStretchable(1, true);
		styleDetailed = businessObj.getStyleDetailed(context);

		int itemCount = styleDetailed.size();

		/** ������ TableLayout �����ؼ� */
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
				/** name���Բ���Ҫ��ӵ��б� */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("mysql")) {
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
			String hint = styleDetailed.get(index).get(XmlHelper.QUERY_HINT)
					.toString();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText(String.valueOf(detaild.get(value)));
				if (button.getText().toString().equals("")
						|| button.getText().toString() == null) {
					button.setText("��������");
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
							Toast.makeText(context, "�����ַΪ�գ��޷������ʼ�",
									Toast.LENGTH_SHORT).show();
							return;
						} else if (!checkEmail(email)) {
							Toast.makeText(context, "�����ַ���Ϸ����޷������ʼ�",
									Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				button.setId(index);
				button.setText("�鿴");
				final String otherstr = String.valueOf(detaild.get(value));
				row.addView(button, rightLayoutParams);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent it = new Intent(context, WebViewer.class);

						if (otherstr == null || otherstr.equals("")
								|| otherstr.equals("null")) {
							Toast.makeText(context.getApplicationContext(),
									"���޸�����Ϣ!", Toast.LENGTH_SHORT).show();
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
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				text.setId(index);
				text.setText(String.valueOf(detaild.get(value)));
				row.addView(text, rightLayoutParams);

			}/** ��γ�� */
			else if ("jwd".equals(map.get("style"))) {
				gislocation = GISLocation.getGISLocationInstance();
				final int jdIndext = index - 1;
				final TableLayout t = new TableLayout(context);
				t.setColumnStretchable(0, true);
				TableRow wdRow = new TableRow(context);
				final EditText jwdeditText = new EditText(context);
				jwdeditText.setFocusable(false);
				jwdeditText.setMaxEms(jwdeditText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
				jwdeditText.setId(index);
				String textvalue = String.valueOf(detaild.get(value));
				/** ����EditText���� */
				jwdeditText.setText(textvalue);
				ImageView image = new ImageView(context);
				Drawable draw = context.getResources().getDrawable(
						R.drawable.tdfg_location);
				// image.setImageDrawable(draw);
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
										"��ʱ�޷���������ź�!", Toast.LENGTH_SHORT)
										.show();
								return;
							}
							/** γ�� */
							String jd = location.getLongitude() + "";
							/** ���� */
							String wd = location.getLatitude() + "";
							jwdeditText.setText(wd);
							EditText jdeditext = (EditText) t
									.findViewById(jdIndext);
							jdeditext.setText(jd);

						}
					});
				}
				/** html�������� */
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
				/** ֧�ֽű� */
				mWebSettings.setJavaScriptEnabled(true);
				/** �������� */
				mWebSettings.setBuiltInZoomControls(false);
				mWebSettings.setLightTouchEnabled(false);
				mWebSettings.setSupportZoom(false);
				// mWebSettings.setPluginsEnabled(false);
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
							it.putExtra("title", "���������Ϣ");
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
							it.putExtra("title", "���������Ϣ");
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

			}/** ���ֱ༭�� */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
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
					textvalue = transitToChinese(Integer.parseInt(detaild.get(
							"zw").toString()));
				}
				// ��ȡִ��״̬
				if ("rwztmc".equals(value)) {
					textvalue = detaild.get("rwztmc").toString();
				}
				//��ȡ��������
				if ("rwmc".equals(value)) {
					textvalue = detaild.get("TaskName").toString();
				}
				//��ȡ��������
				if ("lxname".equals(value)) {
					textvalue = detaild.get("TaskType").toString();
				}
				//��ȡ�����̶�
				if ("cdname".equals(value)) {
					textvalue = detaild.get("Urgency").toString();
				}
				//��ȡ����ʱ��
				if ("fbsj".equals(value)) {
					textvalue = detaild.get("PublishedTime").toString();
				}
				//��ȡ�������
				if ("bjqx".equals(value)) {
					textvalue = detaild.get("TransactedTime").toString();
				}
				//��ȡ������
				if ("fbr".equals(value)) {
					textvalue = detaild.get("PublisherId").toString();
				}
//				//��ȡ��������
				if ("rwms".equals(value)) {
					textvalue = detaild.get("Remark").toString();
				}
				// ��ȡ�û�ְ��
				if ("zz".equals(value)) {
					String sql = "select supercontext from gridusermapping where userid='"
							+ detaild.get("userid") + "' group by blamerole";
					textvalue = SqliteUtil.getInstance().getDepidByDepName(sql);
				}
				// ��ȡ��������
//				if ("lxname".equals(value) && "".equals(textvalue)) {
//					String rwlx = detaild.get("rwlx").toString();
//					if (rwlx != null && !rwlx.equals("")) {
//						String[] tempString = rwlx.split(";");
//						for (int i = 0; i < tempString.length; i++) {
//							String sql = "select name from t_ydzf_rwlx where code='"
//									+ tempString[i] + "'";
//							if (i == 0) {
//								textvalue = SqliteUtil.getInstance()
//										.getDepidByDepName(sql);
//							} else {
//								textvalue = textvalue
//										+ ";"
//										+ SqliteUtil.getInstance()
//												.getDepidByDepName(sql);
//							}
//						}
//					}
//				}
				// ��ȡִ����
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
							sb.append("��");
						}
						sb.deleteCharAt(sb.length() - 1);
						textvalue = sb.toString();
					}
				}
				// ��ȡЭ����
				if ("xbr".equals(value)) {
					String rwbh = detaild.get("rwbh").toString();

					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select SecondaryAuditUserId from ydzf_rwlc where tid='"
											+ rwbh
											+ "' and NodeId = '3' and SecondaryAuditUserId <> '';");

					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("secondaryaudituserid"));
							sb.append(",");
						}
						String sbname = sb.deleteCharAt(sb.length() - 1)
								.toString();
						String[] array = sbname.split(",");
						StringBuilder sba = new StringBuilder();
						for (String a : array) {
							sba.append("'" + a + "',");
						}
						sba = sba.deleteCharAt(sba.length() - 1);
						String sql = "select U_RealName from PC_Users where UserID in ("
								+ sba.toString() + ")";
						ArrayList<HashMap<String, Object>> result1 = SqliteUtil
								.getInstance()
								.queryBySqlReturnArrayListHashMap(sql);
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
					String values = (String) textvalue.subSequence(0,
							textvalue.length() - 6);
					textvalue = values.replace("T", " ");
					if (textvalue != null && !textvalue.equals("")
							&& !textvalue.equalsIgnoreCase("null")
							&& textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				/** ����EditText���� */
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
				/** name���Բ���Ҫ��ӵ��б� */
				continue;
			}
			if (styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
					.equals("mysql")) {
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

			}/** ���ֱ༭�� */
			else {
				editText = new EditText(context);
				editText.setMaxEms(editText.getWidth());
				value = styleDetailed.get(index).get(XmlHelper.QUERY_EDIT_TEXT)
						.toString();
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
					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select distinct b.U_RealName from T_YDZF_RWXX_USER a left join PC_Users b on a.[UserID] = b.UserID where a.RWXXBH = '"
											+ rwbh + "'");
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
					ArrayList<HashMap<String, Object>> result = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select distinct SecondaryAuditUserId from ydzf_rwlc where tid='"
											+ rwbh
											+ "' and SecondaryAuditUserId <>'' group by tid");
					if (result != null && result.size() > 0) {
						StringBuilder sb = new StringBuilder();
						for (HashMap<String, Object> item : result) {
							sb.append(item.get("secondaryaudituserid"));
							sb.append(",");
						}
						String sbname = sb.deleteCharAt(sb.length() - 1)
								.toString();
						String[] array = sbname.split(",");
						StringBuilder sba = new StringBuilder();
						for (String a : array) {
							sba.append("'" + a + "',");
						}
						sba = sba.deleteCharAt(sba.length() - 1);
						String sql = "select U_RealName from PC_Users where UserID in ("
								+ sba.toString() + ")";
						ArrayList<HashMap<String, Object>> result1 = SqliteUtil
								.getInstance()
								.queryBySqlReturnArrayListHashMap(sql);
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
					String values = (String) textvalue.subSequence(0,
							textvalue.length() - 6);
					textvalue = values.replace("T", " ");
					if (textvalue != null && !textvalue.equals("")
							&& !textvalue.equalsIgnoreCase("null")
							&& textvalue.length() > 11) {
						textvalue = textvalue.substring(0, 11);
					}
					if (textvalue.contains("1900")) {
						textvalue = "";
					}
				}
				/** ����EditText���� */
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
	 * ���ش������Ĳ���
	 */
	protected MyScrollView loadlayout(String itemID,
			ArrayList<HashMap<String, Object>> attachment, String filepath) {
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
		Tree(scrollView, allLayout, treeLayout, context, itemID, attachment,
				filepath);
		return scrollView;
	}

	// װ�����ķ���
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
				TreeViewAdapter.ItemHeight * rowcount));// ���Ĳ��ֲ���
		// �ж��Ƿ��и���������У���������ʾ�����б�
		if (attachment != null && attachment.size() > 0) {
			allLayout.addView(treeLayout);
		}
		scrollView.addView(allLayout);
		// ������ӵ����¼�
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
				// if (ffl.isDirectory()) { // Ŀ¼ʱ
				// FileUtils.copyDirectoryToDirectory(ffl, tfl);
				// } else if (ffl.isFile()) { // �ļ�ʱ
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
			Toast.makeText(context, "�ʼ��޷����ͣ�������������", Toast.LENGTH_SHORT)
					.show();
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

		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
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

}
