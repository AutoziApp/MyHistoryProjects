package com.mapuni.android.helper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: YAJALXX.java Description: Ӧ��Ԥ����Ϣ
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����03:17:30
 */
public class YAJALXX extends BaseClass implements Serializable, IDetailed, IQuery, IList {

	/** ʵ��������� */
	public static final String BusinessClassName = "YAJALXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private final String ListStyleName = "HJYJ_YAJAL";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private final String DetailedStyleName = "HJYJ_YAJAL";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private final String QueryStyleName = "YAJAL";
	/** ��ʵ�������ڱ������ */
	private final String primaryKey = "guid";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private final String tableName = "V_ZSK_YAJAL";
	/** ��ʵ������������ */
	private String FileType = "Ӧ��Ԥ��";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private String DetailedTitleText = "��Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String ListTitleText = "��Ϣ�б�";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private String QueryTitleText = "��Ϣ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����id */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;

	HashMap<String, Object> styleList = null;

	public YAJALXX() {
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(Global.getGlobalInstance()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * 
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String String
	 * @author ����� Create at: 2012-12-4 ����03:25:55
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		// String order = "bt asc limit " + x + "," + j;
		String order = x + "," + j;
		return order;
	}

	public String getFileType() {
		return FileType;
	}

	public void setFileType(String fileType) {
		FileType = fileType;
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
		return getFileType() + ListTitleText;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		String column = "Guid,BT";
		return BaseClass.DBHelper.getList(tableName, column);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		/*
		 * if(String.valueOf(DisplayUitl.getSettingValue(Global.getGlobalInstance
		 * ().getCtx(), "menustyle", 0)).equals("1")){ if(fliterHashMap==null){
		 * fliterHashMap = new HashMap<String, Object>(); }
		 * fliterHashMap.put("type",
		 * Global.getGlobalInstance().getType_DB().get("type")); }
		 */
		Object object = styleList.get("mysql");
		String sql = object + getOrder();
		SqliteUtil su = SqliteUtil.getInstance();

		String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		String newSql = null;
		if ("".equals(filterSql)) {
			newSql = sql.replace(",,", "");
		} else {
			newSql = sql.replace(",,", filterSql);
		}
		Log.d("newSql sql is ===>", newSql);
		return su.queryBySqlReturnArrayListHashMap(newSql);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content) throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "YAJALXX");
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
		return getFileType() + QueryTitleText;
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
		return getFileType() + DetailedTitleText;
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
		ArrayList<HashMap<String, Object>> styleDetailed = getStyleDetailed(Global.getGlobalInstance());
		Object mySql = styleDetailed.get(styleDetailed.size() - 1).get("queryHint") + SQLiteDataProvider.getInstance().getFilterForSqlDetail(primaryKeyMap);
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName, getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "YAJALXX");
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
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName, querycondition);
		return spinnerdata;
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
