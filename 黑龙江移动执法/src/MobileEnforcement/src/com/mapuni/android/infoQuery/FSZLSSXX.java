package com.mapuni.android.infoQuery;

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
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: PWSFXX.java Description: ��ˮ������ʩ�������
 * 
 * @author �����
 * @Version 1.4.7
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:10:31
 */
public class FSZLSSXX extends BaseClass implements Serializable, IDetailed, IQuery, IList {

	/** ʵ��������� */
	public static final String BusinessClassName = "FSZLSSXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private static final String ListStyleName = "WRY_FSZLSS";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private static final String DetailedStyleName = "WRY_FSZLSS";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private static final String QueryStyleName = "FSZLSS";
	/** ��ʵ�������ڱ������ */
	private static final String primaryKey = "Id";
	/** ��ȡ��ʵ����ײ��˵��ı��� */
	private static final String BottomMenuName = "XQ";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private static final String tableName = "T_WRY_FSZLSS";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "��ˮ������ʩ������Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private final String ListTitleText = "��ˮ������ʩ�������";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private final String QueryTitleText = "��ˮ������ʩ������Ϣ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;

	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * 
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String String
	 * @author ����� Create at: 2012-12-4 ����10:12:05
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		// String order = "wjsj desc,QYXM desc limit " + x + "," + j;
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
		String column = "Guid,QYXM,WJSJ";
		return BaseClass.DBHelper.getList(tableName, column);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		String str = "";
		// String str = getconditon();
		// String sql = "select * from " + tableName + " where XZQH in (" + str
		// + ")";
		// Object object = styleList.get("mysql");
		// String sql = object + getOrder();
		// SqliteUtil su = SqliteUtil.getInstance();
		//
		// String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		// String newSql = null;
		// if ("".equals(filterSql)) {
		// newSql = sql.replace(",,", "(" + str + ")");
		// } else {
		// newSql = sql.replace(",,", str + filterSql);
		// }
		// Log.d("newSql sql is ===>", newSql);
		String wrybh = fliterHashMap.get("QYDM").toString();
		String newSql = "SELECT a.* , b.* FROM T_WRY_FSZLSSXX as a LEFT JOIN " + "(select qymc,guid from T_WRY_QYJBXX where guid ='" + wrybh + "') as b "
				+ "ON a . WRYBH = b . guid where b.guid ='" + wrybh + "' limit ";

		newSql = newSql + getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content) throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "HBLXQKXX");
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
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName, getStyleQueryInputStream(context));
		} catch (Exception e) {
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
		// HashMap<String, String> primaryKeyMap = new HashMap<String,
		// String>();
		// primaryKeyMap.put("key", primaryKey);
		// primaryKeyMap.put("keyValue", id);
		// ArrayList<HashMap<String, Object>> styleDetailed =
		// getStyleDetailed(Global
		// .getGlobalInstance());
		// Object mySql = styleDetailed.get(styleDetailed.size() - 1).get(
		// "queryHint")
		// + SQLiteDataProvider.getInstance().getFilterForSqlDetail(
		// primaryKeyMap);
		Object mySql = "SELECT a.* , b.* FROM T_WRY_FSZLSSXX as a LEFT JOIN " + "(select qymc,qydm from T_WRY_QYJBXX where qydm = (select wrybh from T_WRY_FSZLSSXX where id = "
				+ id + " )) as b " + "ON a . WRYBH = b . QYDM where b.qydm =(select wrybh from T_WRY_FSZLSSXX where id = " + id + " ) and a.id = " + id;
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName, getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "HBLXQKXX");
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
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName, String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		String interestStr = Global.getGlobalInstance().getAdministrative();
		String[] interests = interestStr.split("��");
		StringBuffer interestStrB = new StringBuffer();
		for (String interest : interests) {
			interestStrB.append(" or '" + interest + "' ");
		}
		AdapterFileName += " where  1=1 ";
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName, querycondition);
		return spinnerdata;
	}

	/**
	 * Description: ��ȡ��ǰ�û���Ȩ��ֵ
	 * 
	 * @return �����Զ��ŷָ����ַ��� String
	 * @author ����� Create at: 2012-12-4 ����10:18:46
	 */
	public String getconditon() {
		StringBuffer str = new StringBuffer();
		String[] xzqhs = Global.getGlobalInstance().getAdministrative().split("��");
		for (String xzqh : xzqhs) {
			str.append("'" + xzqh + "',");
		}
		str.deleteCharAt(str.length() - 1).toString();
		return str.toString();
	}

	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		ArrayList<HashMap<String, Object>> BottomMenuList = null;
		try {
			BottomMenuList = XmlHelper.getMenuFromXml(context, BottomMenuName, "item", "bottommenu", getBottomMenuInputStream(context));
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
