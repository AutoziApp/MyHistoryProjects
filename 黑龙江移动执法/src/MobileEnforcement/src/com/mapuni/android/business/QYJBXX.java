package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.listsildedel.MyAdapter;
import com.example.listsildedel.SideBar;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: QYJBXX.java Description: 企业基本信息
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:35:20
 */
public class QYJBXX extends BaseClass implements Serializable, IList, IDetailed, IQuery {

	/** 实体类的名字 */
	public static final String BusinessClassName = "QYJBXX";
	/** 获取该实体类列表样式的标题 */
	private final String ListStyleName = "WRY_QYJBXX";
	/** 获取该实体类数据详情样式的标题 */
	private final String DetailedStyleName = "WRY_QYJBXX";
	/** 获取该实体类查询样式的标题 */
	private final String QueryStyleName = "JBXX";
	/** 该实体类所在表的主键 */
	private final String primaryKey = "Guid";
	/** 查询该实体类的相关信息所用的表名 */
	private final String tableName = "V_WHY_QYJBXX";
	/** 该实体类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "企业信息详情";
	/** 该实体类在显示列表的时候所用的名字标题 */
	public String ListTitleText = "企业信息列表";
	/** 该实体类在执行查询操作的时候所用的名字标题 */
	private final String QueryTitleText = "企业信息查询";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;
   public static  String xzqhss =null;
	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * 
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String String
	 * @author 王红娟 Create at: 2012-12-4 上午10:36:04
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		// String order = "UpdateTime desc limit " + x + "," + j;
		String order = x + "," + j;
		return order;
	}

	@Override
	public void setListScrolltimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
	}

	@Override
	public String GetKeyField() {
		return primaryKey;
	}

	@Override
	public String getCurrentID() {
		return CurrentID;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public String getDetailedTitleText() {
		return DetailedTitleText;
	}

	@Override
	public String getListTitleText() {
		return ListTitleText;
	}

	public void setListTitleText(String ListTitleText) {
		this.ListTitleText = ListTitleText;
	}

	@Override
	public String getQueryTitleText() {
		return QueryTitleText;
	}

	@Override
	public String GetTableName() {
		return tableName;
	}

	/**
	 * 获取控制级别 国控、省控、市控、无级别
	 * 
	 * @return
	 */
	public ArrayList<String> getControlTypes() {
		ArrayList<String> result = new ArrayList<String>();
		SqliteUtil sqliteUtil = SqliteUtil.getInstance();
		String sql = "select AttenName from Attention";
		Cursor cursor = sqliteUtil.queryBySql(sql);
		while (cursor.moveToNext()) {
			String str = cursor.getString(0);
			result.add(str);
		}
		cursor.close();
		return result;
	}

	/**
	 * 企业信息详情
	 */
	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		System.out.println("primaryKeyMap+" + primaryKeyMap);
		// return BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
		// wsc 替换行业类型代码为行业类型名称
		HashMap<String, Object> companyInfo = BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
		String companyIndustry = "";
		if (companyInfo != null && companyInfo.size() > 0)
			companyIndustry = companyInfo.get("hylb").toString();

		// wsc 把行业名称代码转换成汉字
		String sindustryName = "";
		if (!"".equals(companyIndustry)) {

			ArrayList<HashMap<String, Object>> industryName = BaseClass.DBHelper.queryBySqlReturnArrayListHashMap("select t.name from Industry t  where t.code='" + companyIndustry
					+ "'");
			if (industryName != null && industryName.size() > 0) {
				sindustryName = industryName.get(0).get("name").toString();// 行业名称,取行业类别
			}
		}

		companyInfo.put("hylb", sindustryName);

		return companyInfo;
	}

	/**
	 * 列表样式
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName, getStyleDetailedInputStream(context));

		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "QYJBXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

	/**
	 * 查询样式
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName, getStyleQueryInputStream(context));
			System.out.println("styleDetailList11+>>>>" + styleDetailList);
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "QYJBXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public HashMap<String, Object> getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据行政区划获取信息列表
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		String column = "guid,qymc,frdb,qydz";
		return getDataList(column);

	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		String column = "guid,qymc,frdb,qydz";

		return getDataList(fliterHashMap, column);
	}
	public ArrayList<HashMap<String, Object>> getDataLists(HashMap<String, Object> fliterHashMap,String str) {
		String column = "guid,qymc,frdb,qydz";
        this.xzqhss=str;
		return getDataListy(fliterHashMap, column,str);
	}
	
	
	public ArrayList<HashMap<String, Object>> getDataListy(HashMap<String, Object> fliterHashMap, String column,String str) {

		String sqlstr = "select " + column + " from T_WRY_QYJBXX as a left join Region " + "b on a.xzqh=b.RegionCode  where 1=1 ";
		return DBHelper.getUnionQuerys(sqlstr, fliterHashMap, null, null,str);

	}
	
	
	
	
	
	
	public ArrayList<HashMap<String, Object>> getDataList(String column) {
		String sqlstr = "select " + column + " from T_WRY_QYJBXX as a left join Region " + "b on a.xzqh=b.RegionCode  where 1=1 ";
		return DBHelper.getUnionQuery(sqlstr, null, getOrder(), null);

	}

	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap, String column) {

		String sqlstr = "select " + column + " from T_WRY_QYJBXX as a left join Region " + "b on a.xzqh=b.RegionCode  where 1=1 ";
		return DBHelper.getUnionQuery(sqlstr, fliterHashMap, getOrder(), null);

	}

	@Override
	public HashMap<String, Object> getStyleList(Context context) throws IOException {

		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(context));
			System.out.println("styleList+>>>>>" + styleList);
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "QYJBXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName, String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		String interestStr = Global.getGlobalInstance().getAdministrative();

		System.out.println("interestStr+++++++>>>>>>>" + interestStr);
		String[] interests = interestStr.split("、");
		StringBuffer interestStrB = new StringBuffer();
		for (String interest : interests) {
			interestStrB.append(" or '" + interest + "' ");
		}
		AdapterFileName += " where  1=1 ";
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName, querycondition);
		return spinnerdata;
	}

	/**
	 * Description: 获取当前用户的所管辖区域
	 * 
	 * @return 返回以逗号分隔的字符串 String
	 * @author 王红娟 Create at: 2012-12-4 上午10:38:29
	 */
	public String getconditon() {
		StringBuffer str = new StringBuffer();
		String[] xzqhs = Global.getGlobalInstance().getAdministrative().split("、");
		for (String xzqh : xzqhs) {
			str.append("'" + xzqh + "',");
		}
		str.deleteCharAt(str.length() - 1).toString();
		return str.toString();
	}

	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		return null;
	}

	@Override
	public String getBottomMenuName() {
		return null;
	}

	/**
	 * 通过企业代码获得企业ID
	 * 
	 * @param id
	 * @return
	 */
	public String getQYID(String qydm) {
		HashMap<String, String> primaryKey = new HashMap<String, String>();
		primaryKey.put("key", "qydm");
		primaryKey.put("keyValue", qydm);
		HashMap<String, Object> qybmmap = BaseClass.DBHelper.getDetailed("T_WRY_QYJBXX", primaryKey);
		String qyid = (String) qybmmap.get("guid");
		return qyid;
	}

	/**
	 * 通过企业id获得企业代码
	 * 
	 * @param id
	 * @return
	 */
	public String getQYDM(String guid) {
		HashMap<String, String> primaryKey = new HashMap<String, String>();
		primaryKey.put("key", "guid");
		primaryKey.put("keyValue", guid);
		HashMap<String, Object> qybmmap = BaseClass.DBHelper.getDetailed("T_WRY_QYJBXX", primaryKey);
		String qydm = (String) qybmmap.get("qydm");
		return qydm;
	}

	/**
	 * 通过企业代码获得企业名称
	 * 
	 * @param id
	 * @return
	 */
	public String getQYMC(String qydm) {
		HashMap<String, String> primaryKey = new HashMap<String, String>();
		primaryKey.put("key", "qydm");
		primaryKey.put("keyValue", qydm);
		HashMap<String, Object> qybmmap = BaseClass.DBHelper.getDetailed("T_WRY_QYJBXX", primaryKey);
		String qymc = (String) qybmmap.get("qymc");
		return qymc;
	}

	/**
	 * 显示企业查询dialog
	 * 
	 * @param context
	 * @param aecordCondition
	 *            条件集
	 * @return
	 */
	public Builder showQueryDialog(final Context context, final HashMap<String, Object> aecordCondition) {

		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setTitle("查询企业");
		ab.setIcon(context.getResources().getDrawable(R.drawable.base_icon_mapuni_white));

		ab.setView(getLiaoNingQueryView(context, aecordCondition));

		return ab;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param aecordCondition
	 * @return
	 */
	public View getLiaoNingQueryView(Context context, HashMap<String, Object> aecordCondition) {
		String JiXiCode = "230000000";// 不同项目设置不同的值
		return getQueryView(context, aecordCondition, JiXiCode, true);
	}

	/**
	 * 
	 * @param context
	 * @param aecordCondition
	 *            查询条件集
	 * @param parentCode
	 *            父级code值
	 * @param isTwoLevel
	 *            是否为两级 一级只有选择县 二级有选择市 选择县
	 * @return
	 */
	public View getQueryView(final Context context, final HashMap<String, Object> aecordCondition, String parentCode, Boolean isTwoLevel) {
		Boolean haveLastQYMC = false;
		Boolean haveLastParentCode = false;
		final Boolean haveLastXzqh;
		if (aecordCondition.containsKey("xzqh")) {
			haveLastXzqh = true;
		} else {
			haveLastXzqh = false;
		}

		if (aecordCondition.containsKey("'1'")) {
			haveLastQYMC = true;
		}
		if (aecordCondition.containsKey("parentcode")) {
			haveLastParentCode = true;
		}
		
		String strArea = Global.getGlobalInstance().getAreaCode();
		String pcode = "230000000";
		if ((null != strArea) && !"".equals(strArea)) {
			pcode = strArea.substring(0, 4) + "00000";
		}

		final LayoutInflater layoutInflater = LayoutInflater.from(context);
		final View dialogView = layoutInflater.inflate(R.layout.qyjbxx_query, null);
		final EditText qyjbxx_query_qymc = (EditText) dialogView.findViewById(R.id.qyjbxx_query_qymc);
		Spinner qyjbxx_query_city_name = (Spinner) dialogView.findViewById(R.id.qyjbxx_query_city_name);
		
		final Spinner qyjbxx_query_county_name = (Spinner) dialogView.findViewById(R.id.qyjbxx_query_county_name);
		Spinner qyjbxx_query_control_name = (Spinner) dialogView.findViewById(R.id.qyjbxx_query_control_name);
				
if (!"230000000".equals(Global.getGlobalInstance().getAreaCode())) {
	qyjbxx_query_city_name.setEnabled(false);
	qyjbxx_query_county_name.setEnabled(false);
	qyjbxx_query_control_name.setEnabled(false);
	}
		final Button qyxxcx_hylb = (Button) dialogView.findViewById(R.id.qyxxcx_hylb);
		qyxxcx_hylb.setOnClickListener(new OnClickListener() {
			private ListView mListView;
			private SideBar indexBar;
			private WindowManager mWindowManager;
			private TextView mDialogText;
			private View head;

			@Override
			public void onClick(View v) {
				View hylbView = layoutInflater.inflate(R.layout.qyxx_hylb_layout, null);

				mListView = (ListView) hylbView.findViewById(R.id.list);
				indexBar = (SideBar) hylbView.findViewById(R.id.sideBar);
				mDialogText = (TextView) layoutInflater.inflate(R.layout.list_position, null);
				head = layoutInflater.inflate(R.layout.head, null);
				head.setVisibility(View.GONE);
				mListView.addHeaderView(head);
				mDialogText.setVisibility(View.INVISIBLE);
				mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
				mWindowManager.addView(mDialogText, lp);
				indexBar.setTextView(mDialogText);
				// 初始化数据

				ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select name,code,pid from Industry a where a.[USEFLAG] = '1' order by code;");
				// 实例化自定义内容适配类
				MyAdapter adapter = new MyAdapter(context, list);
				// 为listView设置适配
				mListView.setAdapter(adapter);
				// 设置SideBar的ListView内容实现点击a-z中任意一个进行定位
				indexBar.setListView(mListView);

				Builder builder = new Builder(context);
				builder.setTitle("行业类别选择");
				builder.setView(hylbView);
				final AlertDialog dialog = builder.create();

				mListView.setOnItemClickListener(new OnItemClickListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						HeaderViewListAdapter ha = (HeaderViewListAdapter) parent.getAdapter();
						MyAdapter ad = (MyAdapter) ha.getWrappedAdapter();

						HashMap<String, Object> map = (HashMap<String, Object>) ad.getItem(position - 1);
						qyxxcx_hylb.setText(map.get("name").toString());
						aecordCondition.put("hylb", map.get("code"));
						dialog.dismiss();
						dialogView.findViewById(R.id.clear).setVisibility(View.VISIBLE);
					}
				});
				dialog.show();

			}
		});

		dialogView.findViewById(R.id.clear).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				aecordCondition.remove("hylb");
				qyxxcx_hylb.setText("");
				v.setVisibility(View.GONE);
			}
		});
		qyjbxx_query_qymc.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String qymc = qyjbxx_query_qymc.getText().toString();
				aecordCondition.put("'1'", "2' or qymc like '%" + qymc.replace(" ", "%") + "%");
			}
		});
		if (haveLastQYMC) {// 加载上次查询结果
			String value = aecordCondition.get("'1'").toString();
			int startIndex = value.indexOf("%");
			int endIndex = value.lastIndexOf("%");
			qyjbxx_query_qymc.setText(value.substring(startIndex + 1, endIndex));
		}
		qyjbxx_query_city_name.setPrompt("-请选择-");
		qyjbxx_query_county_name.setPrompt("-请选择-");
		qyjbxx_query_control_name.setPrompt("-请选择-");
		ArrayList<String> cityList = new ArrayList<String>();// 市adapter数据

		final ArrayList<HashMap<String, Object>> cityAllList = getRegionList(parentCode);// 城市的全部信息
		final ArrayList<HashMap<String, Object>> countyAllList = new ArrayList<HashMap<String, Object>>();// 区县的信息
		final ArrayList<String> countyList = new ArrayList<String>();// 区县adapter数据
		countyList.add("选择县(全部)");

		if (isTwoLevel) {// 是否二级查询
			cityList.add("选择市(全部)");
			int parentCodeIndex = 0;// 选择市默认选中位置
			for (int i = 0; i < cityAllList.size(); i++) {
				HashMap<String, Object> map = cityAllList.get(i);
				cityList.add(map.get("regionname").toString());
				if (map.get("regioncode").equals(pcode)) {
					parentCodeIndex = i + 1;
					// 设置下拉列表值不可修改
					qyjbxx_query_city_name.setClickable(false);
					haveLastParentCode = false;// 重置
				} else if (haveLastParentCode) {
					if (map.get("regioncode").equals(aecordCondition.get("parentcode"))) {// 如果条件集中已经存在--市的条件，默认选中该市
						parentCodeIndex = i + 1;
						haveLastParentCode = false;// 重置
					}

				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cityList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			qyjbxx_query_city_name.setAdapter(adapter);
			qyjbxx_query_city_name.setSelection(parentCodeIndex);
			qyjbxx_query_city_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					countyAllList.clear();
					countyList.clear();
					countyList.add("选择县(全部)");
					if (arg2 == 0) {// 清空县级adapter数据

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countyList);
						qyjbxx_query_county_name.setAdapter(adapter);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						if (aecordCondition.containsKey("parentcode")) {
							aecordCondition.remove("parentcode");
							if (aecordCondition.containsKey("xzqh")) {
								aecordCondition.remove("xzqh");
							}
						}
					} else {
						String cityCode = cityAllList.get(arg2 - 1).get("regioncode").toString();// 得到选择城市的code
						countyAllList.addAll(getRegionList(cityCode));
						int countyCodeIndex = 0;

						for (int i = 0; i < countyAllList.size(); i++) {
							HashMap<String, Object> map = countyAllList.get(i);
							countyList.add(map.get("regionname").toString());

							if (map.get("regioncode").equals(Global.getGlobalInstance().getAreaCode())) {
								countyCodeIndex = i + 1;
								// 设置下拉列表值不可修改
								qyjbxx_query_county_name.setClickable(false);

							}
						}
						if (haveLastXzqh) {// 上次查询有记录
							for (int i = 0; i < countyAllList.size(); i++) {
								HashMap<String, Object> map = countyAllList.get(i);
								if (aecordCondition.get("xzqh") == null) {// 如果已经清除xzqh
																			// 跳过
									break;
								}
								if (map.get("regioncode").toString().equals(aecordCondition.get("xzqh").toString())) {// 得到上次查询的数据，确定位置
									countyCodeIndex = i + 1;
									break;
								}
								if (i == countyAllList.size() - 1) {// 最后一位仍不满足以上条件。清空aecordCondition
																	// xzqh数据
									aecordCondition.remove("xzqh");
								}

							}

						} else {
							if (aecordCondition.containsKey("xzqh")) {// 清除上次选择县的条件
								aecordCondition.remove("xzqh");
							}
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countyList);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						qyjbxx_query_county_name.setAdapter(adapter);
						qyjbxx_query_county_name.setSelection(countyCodeIndex);
						String code=cityCode.substring(0, 4);
						aecordCondition.put("parentcode", cityCode + "' or xzqh like '" + code );
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});

		} else {// 只有选择县 cityAllList 即为县的数据
			((View) qyjbxx_query_city_name.getParent()).setVisibility(View.GONE);// 选择市的spinner隐藏
			countyAllList.clear();
			countyAllList.addAll(cityAllList);
			for (int i = 0; i < countyAllList.size(); i++) {
				HashMap<String, Object> map = countyAllList.get(i);
				countyList.add(map.get("regionname").toString());

			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countyList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			qyjbxx_query_county_name.setAdapter(adapter);

			if (haveLastXzqh) {// 上次查询有记录
				int countyCodeIndex = 0;
				for (int i = 0; i < countyAllList.size(); i++) {
					HashMap<String, Object> map = countyAllList.get(i);
					if (aecordCondition.get("xzqh") == null) {// 如果已经清除xzqh 跳过
						break;
					}
					if (map.get("regioncode").toString().equals(aecordCondition.get("xzqh").toString())) {// 得到上次查询的数据，确定位置
						countyCodeIndex = i + 1;
						break;
					}
					if (i == countyAllList.size() - 1) {// 最后一位仍不满足以上条件。清空aecordCondition
														// xzqh数据
						aecordCondition.remove("xzqh");
					}

				}
				qyjbxx_query_county_name.setSelection(countyCodeIndex); // 加载上次选择

			}

		}

		qyjbxx_query_county_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					if (aecordCondition.containsKey("xzqh")) {
						aecordCondition.remove("xzqh");
					}
				} else {
					xzqhss= countyAllList.get(arg2 - 1).get("regioncode").toString();
					aecordCondition.put("xzqh", xzqhss);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// final ArrayList<HashMap<String, Object>>
		// attentionList=getAttentionList();//控制级别数据
		// (attentionList!=null){
		// final ArrayList<HashMap<String, Object>> attentionList=new
		// ArrayList<HashMap<String,Object>>();
		ArrayList<String> attionList = new ArrayList<String>();
		attionList.add("管理属性");
		int attentionIndex = 0;
		/*
		 * for(int i=0;i<attentionList.size();i++){ HashMap<String, Object>
		 * map=attentionList.get(i);
		 * attionList.add(map.get("attenname").toString()); if(haveLastKzlb){
		 * if(map.get("attencode").equals(aecordCondition.get("wrylb"))){
		 * attentionIndex=i+1; } } }
		 */
		attionList.add("重点源");
		attionList.add("风险源");// 写死数据 后续修改
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, attionList);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		qyjbxx_query_control_name.setAdapter(arrayAdapter);
		qyjbxx_query_control_name.setSelection(attentionIndex);
		qyjbxx_query_control_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					if (aecordCondition.containsKey("sfzdy")) {
						aecordCondition.remove("sfzdy");
					} else if (aecordCondition.containsKey("sffxy")) {
						aecordCondition.remove("sffxy");
					}
				} else {
					if (arg2 == 1) {
						if (aecordCondition.containsKey("sffxy")) {
							aecordCondition.remove("sffxy");
						}
						aecordCondition.put("sfzdy", "是");
					} else {
						if (aecordCondition.containsKey("sfzdy")) {
							aecordCondition.remove("sfzdy");
						}
						aecordCondition.put("sffxy", "是");
					}
					/*
					 * String kzjb=attentionList.get(arg2-1).get("attencode"
					 * ).toString(); aecordCondition.put("wrylb", kzjb);
					 */
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// }

		return dialogView;

	}

	/**
	 * 根据父地区code查询所有的第一级子code 和名称
	 * 
	 * @param parentCode
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getRegionList(String parentCode) {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("parentCode", parentCode);
		ArrayList<HashMap<String, Object>> cityList = SqliteUtil.getInstance().getList("RegionCode,RegionName", conditions, "Region");
		return cityList;

	}

	public ArrayList<HashMap<String, Object>> getAttentionList() {
		return SqliteUtil.getInstance().getList("Attention");
	}

	/** 根据用户所属区县得到该区县的父节点 **/
	private ArrayList<HashMap<String, Object>> getParentByAreacode(String areacode) {
		ArrayList<HashMap<String, Object>> pData = null;

		if (!areacode.equals("")) {
			String sql = "select parentcode from region where regioncode='" + areacode + "'";
			pData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			return pData;
		} else {
			return pData;
		}
	}
}
