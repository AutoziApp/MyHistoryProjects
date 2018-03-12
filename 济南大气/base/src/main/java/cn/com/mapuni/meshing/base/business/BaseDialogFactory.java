package cn.com.mapuni.meshing.base.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.Global;
import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.adapter.CityAdapter;
import cn.com.mapuni.meshing.base.dataprovider.SqliteUtil;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.interfaces.IQuery;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.ExceptionManager;


/**
 * FileName: DialogFactory.java Description: Dialog����
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-11-30 ����04:11:25
 */
public class BaseDialogFactory extends BaseClass {

	/** Dialog���� */
	private String title;
	/** ������ */
	private Context context;
	/** ��ѯ�����ļ��еĲ�ѯ���� */
	private String queryname;
	/** ��ѯҵ���� */
	private IQuery queryObj;
	/** �������� */
	private String cityName = "";
	/** �������� */
	@SuppressWarnings("unused")
	private String areaName = "";
	/** ��ȡ��ѯ�����ļ����صļ������� */
	private int itemCount;
	/** �ж��Ƿ���ʱ����Ϊ��ѯ������ */
	int ifhastime = 0;
	/** ʡ�� */
	private String province = "";

	/** �е���������ص������� */
	Spinner provinceSpinner, citySpinner;
	private EditText timetext, timetext2;

	/** �������б����б���������Ƶļ��ϣ������codeֵ�ļ��ϣ���������Ƶļ��ϣ������code�ļ��� */
	List<String> qycode, citycode, name, code, areaname, areacode;
	/** ��ȡҵ����Ĳ�ѯxmL�ļ����õ���style���� */
	List<HashMap<String, Object>> extraInfoForQuery;
	/** dialog����ӵ���ͼ */
	View view;
	/** ��ѯ������ */
	SqliteUtil sqlite = SqliteUtil.getInstance();

	private final Calendar dateAndTime = Calendar.getInstance();
	OnDateSetListener d = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {
			/** ��PiacKer�е������ո���calendar���� */
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, month);
			dateAndTime.set(Calendar.DAY_OF_MONTH, day);
			timetext.setText(Global.getGlobalInstance().getDate(
					dateAndTime.getTime(), "yyyy-MM-dd"));
		}
	};
	OnDateSetListener t = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) {
			/** ��PiacKer�е������ո���calendar���� */
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, month);
			dateAndTime.set(Calendar.DAY_OF_MONTH, day);
			timetext2.setText(Global.getGlobalInstance().getDate(
					dateAndTime.getTime(), "yyyy-MM-dd"));
		}
	};

	/**
	 * DialogFactory���췽��
	 * 
	 * @param context
	 *            ������
	 * @param title
	 *            dialogҪ��ʾ����
	 * @param queryname
	 *            Ҫ��ѯ��ҵ������Ĳ�ѯ����
	 */
	public BaseDialogFactory(Context context, String title, String queryname,
			IQuery queryObj) {
		this.title = title;
		this.context = context;
		this.queryname = queryname;
		this.queryObj = queryObj;

	}

	public IQuery getQueryObj() {
		return queryObj;
	}

	public void setQueryObj(IQuery queryObj) {
		this.queryObj = queryObj;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getQueryname() {
		return queryname;
	}

	public void setQueryname(String queryname) {
		this.queryname = queryname;
	}

	@Override
	public String GetKeyField() {
		return null;
	}

	@Override
	public String GetTableName() {
		return null;
	}

	/**
	 * Description: ������ѯ������Dialog����
	 * 
	 * @return ����һ��Dialog AlertDialog.Builder
	 * @author ����� Create at: 2012-11-30 ����04:37:04
	 */
	public AlertDialog.Builder showResearchDialog() {
		LayoutInflater li = LayoutInflater.from(getContext());
		view = li.inflate(R.layout.research_query, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		if (title.equals("") || queryObj != null) {
			builder.setTitle(queryObj.getQueryTitleText());
		} else {
			builder.setTitle(title);
		}
		builder.setIcon(getContext().getResources().getDrawable(
				R.drawable.icon_mapuni_white));
		builder.setView(view);

		/** �� StyleHandler �еķ�����ȡ style_info.xml, �õ�������������Ҫ�� List */
		if (queryObj != null && queryname.equals("")) {
			extraInfoForQuery = queryObj.getStyleQuery(context);
		} else {
			extraInfoForQuery = getStyleQuery(context);
		}

		/** ���� TableLayout, ����ͬ Activity��, �����û������� */
		ScrollView scrollView = new ScrollView(getContext());
		LinearLayout queryLayout = (LinearLayout) view
				.findViewById(R.id.query_dialog);
		queryLayout.addView(scrollView);

		TableLayout queryTable = new TableLayout(getContext());
		scrollView.addView(queryTable);
		queryTable.setColumnStretchable(1, true);

		/** �õ���ѯ��Ŀ������ */
		itemCount = extraInfoForQuery.size();

		/** ������ TableLayout�����ؼ� */
		TableRow.LayoutParams rightLayoutParams = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLayoutParams.setMargins(0, 0, 15, 0);
		int counter = 0;

		for (int x = 0; x < itemCount; x++) {
			HashMap<String, Object> extraInfoMap = extraInfoForQuery.get(x);
			/** ��̬���EditText��Spinner�����ж� */
			if (extraInfoMap.get(XmlHelper.QUERY_EDIT_TEXT).equals("name")
					&& counter == 0) {
				counter++;
				continue;
			}
			/** ����style��name�ֶβ�������ʾ */
			TableRow row = new TableRow(context);
			TextView textView = new TextView(context);
			textView.setText(extraInfoMap.get(XmlHelper.QUERY_HINT).toString());
			textView.setGravity(Gravity.RIGHT);
			textView.setPadding(15, 0, 0, 0);
			row.addView(textView);

			if (extraInfoForQuery.get(x).get("style").equals("spinner")) {
				/** ��ò�ѯ��Ҫ���������� ,Spinner��ȡ���ڵ��е����� */
				if (extraInfoMap.get("info") != null
						&& !"".equals(extraInfoMap.get("info"))
						&& extraInfoMap.get("info").equals("area")) {
					provinceSpinner = new Spinner(context);
					citySpinner = new Spinner(context);
					qycode = new ArrayList<String>();
					citycode = new ArrayList<String>();

					// /** ��õ�ǰ��¼�û���������� */
					// String strRegion = Global.getGlobalInstance()
					// .getAdministrative();
					// String[] regions = strRegion.split("��");

					/** ����ʡ���������� */
					String LiaoCode = "140000000";
					/** ��ѯ�������������ݼ��� */
					ArrayList<HashMap<String, Object>> regionsData = new ArrayList<HashMap<String, Object>>();
					regionsData = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select * from Region where parentCode in(select RegionCode from Region where parentcode = '"
											+ LiaoCode
											+ "' ) or parentcode = '"
											+ LiaoCode + "'");

					if (regionsData != null && regionsData.size() > 0) {

						ArrayList<String> regionListStr = new ArrayList<String>();
						for (int i = 0; i < regionsData.size(); i++) {
							String regionStr = regionsData.get(i)
									.get("regioncode").toString();
							regionListStr.add(regionStr);
						}
						String regionStrs = "";
						for (int i = 0; i < regionListStr.size(); i++) {
							regionStrs += regionListStr.get(i).toString() + "��";
						}
						String[] regions = regionStrs.split("��");

						for (int i = 0; i < regions.length; i++) {
							/** ��ǰ�������Ϊ�� */
							if (regions[i].indexOf("00000") == 4) {
								citycode.add(regions[i]);
							}
							/** ��ǰ��¼�û�ӵ�е����ش��� */
							if (regions[i].indexOf("000") == 6) {
								qycode.add(regions[i]);
							}
						}

						name = new ArrayList<String>();
						code = new ArrayList<String>();
						name.add("--ѡ����--");
						code.add("--ѡ����--");

						String str = "";
						String[] strs = new String[1000];
						for (int i = 0; i < citycode.size(); i++) {
							str += citycode.get(i) + ",";
						}
						if (str != null && !"".equals(str)) {
							strs = str.split(",");
						}
						List<HashMap<String, Object>> shiadapterlist;
						try {
							if (strs.length != 0) {
								String strSql = "select RegionCode,RegionName from region where regioncode in(";
								for (int i = 0; i < strs.length; i++) {
									strSql += "'" + strs[i] + "',";
								}
								strSql += "'" + strs[strs.length - 1] + "')";
								shiadapterlist = sqlite
										.queryBySqlReturnArrayListHashMap(strSql);
								for (Map<String, Object> spinnermap : shiadapterlist) {
									if (spinnermap.get("regionname").toString()
											.length() <= 0)
										continue;
									name.add(spinnermap.get("regionname")
											.toString());
									code.add(spinnermap.get("regioncode")
											.toString());
								}
								ArrayAdapter<String> shiadapter = returnAdapter(
										shiadapterlist, "regionname",
										"regioncode", "shi");
								provinceSpinner.setAdapter(shiadapter);
							}
						} catch (SQLiteException e) {
							e.printStackTrace();
						}
						areaname = new ArrayList<String>();
						areacode = new ArrayList<String>();
						areaname.add("--ѡ����--");
						areacode.add("--ѡ����--");
						provinceSpinner
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int point, long arg3) {
										cityName = code.get(point).toString();
										String str = "";
										String[] strs = new String[100];
										for (int i = 0; i < qycode.size(); i++) {
											str += qycode.get(i) + ",";
										}
										if (str != null && !"".equals(str)) {
											strs = str.split(",");
										}
										List<HashMap<String, Object>> xianadapterlist;
										if (strs.length != 0) {
											String strSql = "select RegionCode,RegionName from region where parentcode='"
													+ cityName
													+ "' and regioncode in(";
											for (int i = 0; i < strs.length; i++) {
												strSql += "'" + strs[i] + "',";
											}
											strSql += "'"
													+ strs[strs.length - 1]
													+ "')";
											try {
												xianadapterlist = sqlite
														.queryBySqlReturnArrayListHashMap(strSql);
												for (Map<String, Object> spinnermap : xianadapterlist) {
													if (spinnermap
															.get("regionname")
															.toString()
															.length() <= 0)
														continue;
													areaname.add(spinnermap
															.get("regionname")
															.toString());
													areacode.add(spinnermap
															.get("regioncode")
															.toString());
												}
												ArrayAdapter<String> adapter = returnAdapter(
														xianadapterlist,
														"regionname",
														"regioncode", "xian");
												citySpinner.setAdapter(adapter);
											} catch (SQLiteException e) {
												e.printStackTrace();
											}
										}
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {

									}
								});
						citySpinner
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int point, long arg3) {
										areaName = areacode.get(point)
												.toString();
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
									}
								});
						provinceSpinner.setPrompt("--ѡ����--");
						citySpinner.setPrompt("--ѡ����--");
						provinceSpinner.setId(x);
						citySpinner.setId(x);
						row.addView(provinceSpinner, rightLayoutParams);

					}
				}
				/** Spinner��ȡ�������ص����� */
				else if (extraInfoMap.get("info") != null
						&& !"".equals(extraInfoMap.get("info"))
						&& extraInfoMap.get("info").equals("city")) {
					provinceSpinner = new Spinner(context);
					citySpinner = new Spinner(context);
					qycode = new ArrayList<String>();
					citycode = new ArrayList<String>();

					/** ��õ�ǰ��¼�û���������� */
					// String strRegion = Global.getGlobalInstance()
					// .getAdministrative();
					// String[] regions = strRegion.split("��");

					/** ����ʡ���������� */
					String LiaoCode = "140000000";
					/** ��ѯ�������������ݼ��� */
					ArrayList<HashMap<String, Object>> regionsData = new ArrayList<HashMap<String, Object>>();
					regionsData = SqliteUtil
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select * from Region where parentCode in(select RegionCode from Region where parentcode = '"
											+ LiaoCode
											+ "' ) or parentcode = '"
											+ LiaoCode + "'");
					if (regionsData != null && regionsData.size() > 0) {

						ArrayList<String> regionListStr = new ArrayList<String>();
						for (int i = 0; i < regionsData.size(); i++) {
							String regionStr = regionsData.get(i)
									.get("regioncode").toString();
							regionListStr.add(regionStr);
						}
						String regionStrs = "";
						for (int i = 0; i < regionListStr.size(); i++) {
							regionStrs += regionListStr.get(i).toString() + "��";
						}
						String[] regions = regionStrs.split("��");

						for (int i = 0; i < regions.length; i++) {
							/** ��ǰ�������Ϊ�� */
							if (regions[i].indexOf("00000") == 4) {
								citycode.add(regions[i]);
							}
							/** ��ǰ��¼�û�ӵ�е����ش��� */
							if (regions[i].indexOf("000") == 6) {
								qycode.add(regions[i]);
							}
						}

						name = new ArrayList<String>();
						code = new ArrayList<String>();
						name.add("--ѡ����--");
						code.add("--ѡ����--");
						String str = "";
						String[] strs = new String[1000];
						for (int i = 0; i < citycode.size(); i++) {
							str += citycode.get(i) + ",";
						}
						if (str != null && !"".equals(str)) {
							strs = str.split(",");
						}
						List<HashMap<String, Object>> shiadapterlist;
						try {
							if (strs.length != 0) {
								String strSql = "select RegionCode,RegionName from region where regioncode in(";
								for (int i = 0; i < strs.length; i++) {
									strSql += "'" + strs[i] + "',";
								}
								strSql += "'" + strs[strs.length - 1] + "')";
								shiadapterlist = sqlite
										.queryBySqlReturnArrayListHashMap(strSql);
								for (Map<String, Object> spinnermap : shiadapterlist) {
									if (spinnermap.get("regionname").toString()
											.length() <= 0)
										continue;
									name.add(spinnermap.get("regionname")
											.toString());
									code.add(spinnermap.get("regioncode")
											.toString());
								}
								ArrayAdapter<String> shiadapter = returnAdapter(
										shiadapterlist, "regionname",
										"regioncode", "shi");
								provinceSpinner.setAdapter(shiadapter);
							}
						} catch (SQLiteException e) {
							e.printStackTrace();
						}

						areaname = new ArrayList<String>();
						areacode = new ArrayList<String>();
						areaname.add("--ѡ����--");
						areacode.add("--ѡ����--");
						provinceSpinner
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int point, long arg3) {
										cityName = code.get(point).toString();
										String str = "";
										String[] strs = new String[100];
										for (int i = 0; i < qycode.size(); i++) {
											str += qycode.get(i) + ",";
										}
										if (str != null && !"".equals(str)) {
											strs = str.split(",");
										}
										List<HashMap<String, Object>> xianadapterlist;
										if (strs.length != 0) {
											String strSql = "select RegionCode,RegionName from region where parentcode='"
													+ cityName
													+ "' and regioncode in(";
											for (int i = 0; i < strs.length; i++) {
												strSql += "'" + strs[i] + "',";
											}
											strSql += "'"
													+ strs[strs.length - 1]
													+ "')";
											try {
												xianadapterlist = sqlite
														.queryBySqlReturnArrayListHashMap(strSql);
												for (Map<String, Object> spinnermap : xianadapterlist) {
													if (spinnermap
															.get("regionname")
															.toString()
															.length() <= 0)
														continue;
													areaname.add(spinnermap
															.get("regionname")
															.toString());
													areacode.add(spinnermap
															.get("regioncode")
															.toString());
												}
												ArrayAdapter<String> adapter = returnAdapter(
														xianadapterlist,
														"regionname",
														"regioncode", "xian");
												citySpinner.setAdapter(adapter);
											} catch (SQLiteException e) {
												e.printStackTrace();
											}
										}
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {

									}

								});
						citySpinner
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											int point, long arg3) {
										areaName = areacode.get(point)
												.toString();

									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {

									}
								});
						provinceSpinner.setPrompt("--ѡ����--");
						citySpinner.setPrompt("--ѡ����--");
						provinceSpinner.setId(x);
						citySpinner.setId(x);
						row.addView(citySpinner, rightLayoutParams);
					}

				} else {
					/** �ж����EditText����Spinner */
					Spinner spinner = new Spinner(context);

					spinner.setPrompt("--��ѡ��--");
					spinner.setId(x);

					List<String> name = new ArrayList<String>();
					name.add("--��ѡ��--");
					/** ��ò�ѯ������ */
					String querycondition = extraInfoMap.get(
							XmlHelper.QUERY_EDIT_TEXT).toString();
					List<HashMap<String, Object>> adapterlist = new ArrayList<HashMap<String, Object>>();
					String tablename = extraInfoMap.get("datasource").toString();
					if("V_Users".equals(tablename) && "depname".equals(querycondition)){
						Global.getGlobalInstance().setSearchCondition(true);
						String sql = "SELECT DISTINCT depname FROM V_Users where SyncDataRegionCode = '"+Global.getGlobalInstance().getAreaCode()+"'";
						adapterlist = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
					}else{
						/** ��ȡ�����б������ */
						adapterlist = getAdapterList(tablename, querycondition);
					}
					
					/** �ж������ļ��Ƿ�������ȷ */
					if (adapterlist == null) {
						Toast.makeText(context, "Spinner�����ļ������ڻ����ô���",
								Toast.LENGTH_SHORT).show();
						return null;
					}
					for (Map<String, Object> map : adapterlist) {
						if (map.get(querycondition).toString().length() <= 0)
							continue;
						name.add(map.get(querycondition).toString());
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							context, android.R.layout.simple_spinner_item, name);
					/** spinner������ */
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setAdapter(adapter);
					row.addView(spinner, rightLayoutParams);
				}
			} else if (extraInfoForQuery.get(x).get("style")
					.equals("autocomplete")) {
				/** �������ø��� EditText �� Id, ��ȡ���ݹ��� XPath ���ʽʱ���� */
				AutoCompleteTextView auto = new AutoCompleteTextView(context);
				// auto.setMaxEms(auto.getWidth());
				auto.setId(x);
				auto.setMaxEms(auto.getWidth());
				List<String> hz = new ArrayList<String>();
				List<String> psm = new ArrayList<String>();
				String querycondition = extraInfoMap.get(
						XmlHelper.QUERY_EDIT_TEXT).toString();
				List<HashMap<String, Object>> data = getAdapterList(
						extraInfoMap.get("datasource").toString(),
						querycondition);
				for (Map<String, Object> map : data) {
					if (map.get(querycondition).toString().length() <= 0)
						continue;
					hz.add(map.get(querycondition).toString());
					psm.add(map.get(querycondition).toString()
							+ " "
							+ DisplayUitl.getPinYinFirstChar(map.get(
									querycondition).toString()));
				}
				CityAdapter<String> adapter = new CityAdapter<String>(context,
						android.R.layout.simple_dropdown_item_1line, hz, psm);
				/** �����÷�����������ӵ��Զ���ȫ�ؼ��� */
				auto.setAdapter(adapter);
				auto.setThreshold(1);
				row.addView(auto, rightLayoutParams);
			} else if (extraInfoForQuery.get(x).get("style").equals("time")) {
				if (ifhastime >= 1) {
					timetext2 = new EditText(context);
					timetext2.setMaxEms(timetext2.getWidth());
					timetext2.setId(x);
					timetext2.setFocusable(false);
					row.addView(timetext2, rightLayoutParams);
					timetext2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							DatePickerDialog dpd = new DatePickerDialog(
									context, t, /** ��ʼ��Ϊ��ǰ���� */
									dateAndTime.get(Calendar.YEAR), dateAndTime
											.get(Calendar.MONTH), dateAndTime
											.get(Calendar.DAY_OF_MONTH));
							dpd.show();
						}
					});
				} else {
					timetext = new EditText(context);
					timetext.setMaxEms(timetext.getWidth());
					timetext.setId(x);
					timetext.setFocusable(false);
					row.addView(timetext, rightLayoutParams);
					timetext.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							DatePickerDialog dpd = new DatePickerDialog(
									context, d, /** ��ʼ��Ϊ��ǰ���� */
									dateAndTime.get(Calendar.YEAR), dateAndTime
											.get(Calendar.MONTH), dateAndTime
											.get(Calendar.DAY_OF_MONTH));
							dpd.show();
						}
					});
				}

				ifhastime++;
			} else {
				EditText editText = new EditText(getContext());
				editText.setMaxEms(editText.getWidth());
				editText.setId(x);
				row.addView(editText, rightLayoutParams);
			}
			queryTable.addView(row);
		}

		return builder;
	}

	public HashMap<String, Object> BuildQueryValue() {
		HashMap<String, Object> returnValueMap = new HashMap<String, Object>();

		/** ��Ϊ���ʽ����Ҫʹ���������е� and �����, ���Գ��˵�һ������֮��, ÿ����һ����������Ҫ��ǰ���" and ". */
		/** ���������ֵ��ʾ��ǰ�Ƿ���������ʽ��д���һ������. */
		int counter = 0;
		for (int index = 0; index < itemCount; index++) {
			HashMap<String, Object> tempMap = extraInfoForQuery.get(index);
			if (tempMap.get(XmlHelper.QUERY_EDIT_TEXT).equals("name")
					&& counter == 0) {
				counter++;
				continue;
			}
			boolean isSpinner = false;
			/** �Ƿ�Ϊspinner���жϱ�־ */
			Map<String, Object> stylecontent = tempMap;
			if (stylecontent.get("style").equals("spinner"))
				isSpinner = true;

			if (isSpinner) {
				Spinner sp = (Spinner) view.findViewById(index);
				if (sp.getSelectedItem() == null
						|| sp.getSelectedItem().toString().length() <= 0) {
					continue;
				} else if (!"--ѡ����--".equals(sp.getSelectedItem())
						&& !"".equals(sp.getSelectedItem())
						&& !"--ѡ����--".equals(sp.getSelectedItem())
						&& !"".equals(sp.getSelectedItem())
						&& !"--��ѡ��--".equals(sp.getSelectedItem())
						&& !"".equals(sp.getSelectedItem())) {
					/** ��ѯ����Ϊ���ؼ�����Spinner��������д */
					String conditionName = tempMap.get(
							XmlHelper.QUERY_EDIT_TEXT).toString();
					String content = null;
					for (Map<String, Object> map : getAdapterList(
							tempMap.get("datasource").toString(),
							tempMap.get("queryEditText").toString())) {
						if (map.get(tempMap.get("queryEditText").toString())
								.toString()
								.equals(sp.getSelectedItem().toString())) {

							content = map.get(
									tempMap.get("queryEditText").toString())
									.toString();
							returnValueMap.put(conditionName, content);
							break;
						}
					}
					province = (String) sp.getSelectedItem();
				} else {
					/** ֻѡ���ѯ����Ϊ��-ȥ����ѡ������� */
					returnValueMap.put("regionname2", province);
					returnValueMap.remove("regionname");

				}
			} else if (stylecontent.get("style").equals("autocomplete")) {
				AutoCompleteTextView auto = (AutoCompleteTextView) view
						.findViewById(index);
				if (auto.getText().toString().trim().length() <= 0)
					continue;
				else {
					String conditionName = tempMap.get(
							XmlHelper.QUERY_EDIT_TEXT).toString();

					returnValueMap.put(conditionName, 1 + "' or "
							+ conditionName + " like '%"
							+ auto.getText().toString().trim() + "%");
				}
			} else if (stylecontent.get("style").equals("time")) {
				EditText edittext = (EditText) view.findViewById(index);
				String date = edittext.getText().toString().trim();
				String dates;
				if (date.length() <= 0)
					continue;
				else {
					String conditionName = tempMap.get(
							XmlHelper.QUERY_EDIT_TEXT).toString();
					dates = dateformat(date);
					if (dates.length() > 0) {
						if (ifhastime > 1) {
							if (index > 2) {
								returnValueMap.put(conditionName + "<", dates);
							} else {
								returnValueMap.put(conditionName + ">", dates);
							}
						} else {
							returnValueMap.put(conditionName, 1 + "' or "
									+ conditionName + " like '" + dates + "%");
						}
					}
				}
			} else {
				EditText edittext = (EditText) view.findViewById(index);
				if (edittext.getText().toString().trim().length() <= 0)
					continue;
				else {
					String conditionName = tempMap.get(
							XmlHelper.QUERY_EDIT_TEXT).toString();

					if ("qymc".equals(conditionName)) {
						conditionName = "qymc";
					}

					returnValueMap.put(conditionName, 1 + "' or "
							+ conditionName + " like '%"
							+ edittext.getText().toString().trim() + "%");
				}
			}
		}
		return returnValueMap;
	}

	/**
	 * Description: ������autoTextcomple�ռ�ļ���
	 * 
	 * @param adapterlist
	 *            ��ѯ�����ݼ���
	 * @param name
	 *            ����
	 * @param code
	 *            ���
	 * @param info
	 *            Ҫ��װ��adapter���л�����
	 * @return ��������Ҫ��adapter ArrayAdapter<String>
	 * @author ����� Create at: 2012-11-30 ����04:42:10
	 */
	public ArrayAdapter<String> returnAdapter(
			List<HashMap<String, Object>> adapterlist, String name,
			String code, String info) {
		List<String> adaptername = new ArrayList<String>();
		List<String> adaptercode = new ArrayList<String>();
		if (info.equals("shi")) {
			adaptername.add("--ѡ����--");
			adaptercode.add("--ѡ����--");
		} else if (info.equals("xian")) {
			adaptername.add("--ѡ����--");
			adaptercode.add("--ѡ����--");
		}
		for (Map<String, Object> spinnermap : adapterlist) {
			if (spinnermap.get(name).toString().length() <= 0)
				continue;
			adaptername.add(spinnermap.get(name).toString());
			adaptercode.add(spinnermap.get(code).toString());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, adaptername);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	/**
	 * Description: ��ȡ�����б�spinner��������
	 * 
	 * @param AdapterFileName
	 *            ����
	 * @param querycondition
	 *            �ֶ���
	 * @return spinner����
	 * @author ����� Create at: 2012-12-6 ����03:51:33
	 */
	public List<HashMap<String, Object>> getAdapterList(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> result;
		if (queryObj == null) {
			result = getSpinner(AdapterFileName, querycondition);
		} else {
			result = queryObj.getSpinner(AdapterFileName, querycondition);
		}

		return result;
	}

	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		String interestStr = Global.getGlobalInstance().getAdministrative();
		String[] interests = interestStr.split("��");
		StringBuffer interestStrB = new StringBuffer();
		for (String interest : interests) {
			interestStrB.append(" or '" + interest + "' ");
		}
		AdapterFileName += " where  1=1 ";
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName,
				querycondition);
		return spinnerdata;
	}

	/**
	 * Description: ���ڸ�ʽת��
	 * 
	 * @param date
	 *            ����
	 * @return yyyy-MM-dd��ʽ������ String
	 * @author ����� Create at: 2012-12-6 ����03:51:33
	 */
	public String dateformat(String date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date day = dateFormat.parse(date);
			String dates = dateFormat.format(day);
			return dates;
		} catch (Exception e) {
			Toast.makeText(context, "�Բ�������������ڸ�ʽ���ԣ��磺2012-01-01",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return "";
	}

	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(queryname,
					getStyleQueryInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "DFFGXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}
}
