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
 * FileName: JYWZXX.java 
 * Description: ��Ԯ����ҵ����
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ 
 * Create at: 2012-12-3 ����11:10:35
 */
public class JYWZXX extends BaseClass implements IDetailed, IList, IQuery,
		Serializable {

	/** ҵ��������� */
	public static final String BusinessClassName = "JYWZXX";
	/** ��ȡ��ҵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "HJYJ_JYWZ";
	/** ��ȡ��ҵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "HJYJ_JYWZ";
	/** ��ȡ��ҵ�����ѯ��ʽ�ı��� */
	private  final String QueryStyleName = "JYWZ";
	/** ��ҵ�������ڱ������ */
	private  final String primaryKey = "Guid";
	/** ��ѯ��ҵ����������Ϣ���õı��� */
	private  final String tableName = "T_ZSK_EnterSoluAndSave";
	/** ��ҵ��������ʾ���������õ����ֱ��� */
	private String DetailedTitleText = "��Ԯ������Ϣ����";
	/** ��ҵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String ListTitleText = "��Ԯ������Ϣ�б�";
	/** ��ҵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private String QueryTitleText = "��Ԯ������Ϣ��ѯ";
	/** ��ʼ����ǰ��ҵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ҵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;
	public JYWZXX(){
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
	 * Create at: 2012-12-3 ����11:13:20
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
//		String order = "soluname asc limit " + x + "," + j;
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
		String column = "Guid,SoLuName,EntName";
		return BaseClass.DBHelper.getList(tableName, column);
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
		Log.d("newSql sql is ===>", newSql);
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
			ExceptionManager.WriteCaughtEXP(e, "JYWZXX");
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
			ExceptionManager.WriteCaughtEXP(e, "JYWZXX");
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
			ExceptionManager.WriteCaughtEXP(e, "JYWZXX");
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
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName,
				querycondition);
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
