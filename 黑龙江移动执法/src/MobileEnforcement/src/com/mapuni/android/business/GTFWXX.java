package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: GTFWXX.java Description: �������ҵ����
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����09:14:24
 */
public class GTFWXX extends BaseClass implements Serializable, IDetailed, IList {

	/** ҵ��������� */
	public static final String BusinessClassName = "GTFWXX";
	/** ��ȡ��ҵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "ZDWRY_GTFW";
	/** ��ȡ��ҵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "ZDWRY_GTFW";
	/** ��ҵ�������ڱ������ */
	private  final String primaryKey = "Id";
	/** ���ڲ�ѯ�������ֶΣ�����ƴ��sql��� */
	private  final String order = "wxfwmc asc"; // ������ֶ�
	/** ��ѯ��ҵ����������Ϣ���õı��� */
	private  final String tableName = "V_ZDWRY_GTFW";
	/** ��ҵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "���������Ϣ����";
	/** ��ҵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private final String GridTitleText = "���������Ϣ�б�";
	/** ��ʼ����ǰ��ҵ�����б�����Ĵ�����Ŀǰ��û�õ� */
	public int ListScrollTimes;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ҵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;

	public GTFWXX() {
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(Global.getGlobalInstance()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		return GridTitleText;
	}

	@Override
	public String GetTableName() {
		return tableName;
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
		Object mySql = "SELECT a.* , b.* FROM T_WRY_GTFW as a LEFT JOIN "
				+ "(select qymc,qydm from T_WRY_QYJBXX where qydm = (select wrybh from T_WRY_GTFW where id = "
				+ id
				+ " )) as b "
				+ "ON a . WRYBH = b . QYDM where b.qydm =(select wrybh from T_WRY_GTFW where id = "
				+ id + " ) and a.id = " + id;
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(
				String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GTFWXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

	/**
	 * Description:ͨ��������ȡ���������Ϣ�б�
	 * 
	 * @param fliterHashMap
	 *            ��ȡ�б���Ϣ����������
	 * @author �����
	 * @return ArrayList<HashMap<String, Object>> �����б���Ϣ Create at:
	 *         2012-12-3����09:12:55
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		// Object object = styleList.get("mysql");
		// String sql = (String) object;
		// SqliteUtil su = SqliteUtil.getInstance();
		//
		// String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		// String newSql = null;
		// if ("".equals(filterSql)) {
		// newSql = sql.replace(",,", "");
		// } else {
		// newSql = sql.replace(",,", filterSql);
		// }
		// Log.d("newSql sql is ===>", newSql);
		String wrybh = fliterHashMap.get("qydm").toString();
		String newSql = "SELECT a.* , b.* FROM T_WRY_GTFW as a LEFT JOIN "
				+ "(select qymc,qydm from T_WRY_QYJBXX where qydm ='" + wrybh
				+ "') as b " + "ON a . WRYBH = b . QYDM where b.qydm ='"
				+ wrybh + "' limit ";

		newSql = newSql + getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	/**
	 * Description: ��ҵ����Ĳ�ѯ����������ֵ
	 * 
	 * @param filter
	 *            Ҫ��ȡ���ݵ����� void
	 * @author ����� Create at: 2012-12-3 ����09:17:57
	 */
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {

		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GTFWXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		return null;
	}

	@Override
	public String getBottomMenuName() {
		return null;
	}
}
