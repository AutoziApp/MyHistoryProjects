package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: PWXKZXX.java
 * Description: �������֤��Ϣ
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����10:20:57
 */
public class PWXKZXX extends BaseClass implements Serializable, IDetailed,
		IQuery, IList {
	
	/** ʵ��������� */
	public static final String BusinessClassName = "PWXKZXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "WRY_PWXKZ";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "WRY_PWXKZ";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private  final String QueryStyleName = "PWXKZ";
	/** ��ʵ�������ڱ������ */
	private  final String primaryKey = "Guid";
	/** ��ȡ��ʵ����ײ��˵��ı��� */
	private  final String BottomMenuName = "XQ";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private  final String tableName = "V_WRY_PWXKZ";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private String DetailedTitleText = "�������֤��Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String ListTitleText = "�������֤��Ϣ�б�";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private String QueryTitleText = "�������֤��Ϣ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;
	public PWXKZXX(){
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(Global.getGlobalInstance()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String
	 * String
	 * @author �����
	 * Create at: 2012-12-4 ����10:21:41
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
//		String order = "zycp desc limit " + x + "," + j;
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
	public String getListTitleText() {
		return ListTitleText;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		// return BaseClass.DBHelper.getList(tableName);
		return SqliteUtil
				.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select T_WRY_PWXKZ.Guid as Guid,T_WRY_PWXKZ.QYDM as QYDM,FZRQ,XKZBH,BZ,QYMC "
								+ "from T_WRY_PWXKZ left join T_WRY_QYJBXX ON T_WRY_PWXKZ.QYDM = T_WRY_QYJBXX.QYDM");
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		Object object = styleList.get("mysql");
		String sql = object+getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		
		String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		String newSql = null;
		if("".equals(filterSql)){
			newSql = sql.replace(",,", "");
		}else{
			newSql = sql.replace(",,", filterSql);
		}
		LogUtil.d("newSql sql is ===>", newSql);
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "PWXKZXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public String getQueryTitleText() {
		return QueryTitleText;
	}

	@Override
	public HashMap<String, Object> getQuery() {
		return null;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName,
					getStyleQueryInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "PWXKZXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public String getDetailedTitleText() {
		return DetailedTitleText;
	}

	@Override
	public String getCurrentID() {
		return CurrentID;
	}

	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		ArrayList<HashMap<String, Object>> styleDetailed = getStyleDetailed(Global
				.getGlobalInstance());
		Object mySql = styleDetailed.get(styleDetailed.size() - 1).get("queryHint")
				+ SQLiteDataProvider.getInstance().getFilterForSqlDetail(
						primaryKeyMap);
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "PWXKZXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public String GetKeyField() {
		return primaryKey;
	}

	@Override
	public String GetTableName() {
		return tableName;
	}

	@Override
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		String interestStr = Global
				.getGlobalInstance().getAdministrative();
		String[] interests = interestStr.split("��");
		StringBuffer interestStrB = new StringBuffer();
		for (String interest : interests) {
			interestStrB.append(" or '" + interest + "' ");
		}
		AdapterFileName += " where  xzqh='1'" + interestStrB.toString();
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName,
				querycondition);
		return spinnerdata;
	}
	
	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		ArrayList<HashMap<String, Object>> BottomMenuList = null;
		try {
			BottomMenuList = XmlHelper.getMenuFromXml(context, BottomMenuName,"item","bottommenu",getBottomMenuInputStream(context));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BottomMenuList;
	}
	
	@Override
	public String getBottomMenuName() {
		return BottomMenuName;
	}
}
