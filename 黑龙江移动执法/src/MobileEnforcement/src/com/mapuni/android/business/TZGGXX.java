package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
 * FileName: TZGGXX.java
 * Description: ֪ͨ��ʾ��Ϣ
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����02:15:17
 */
public class TZGGXX extends BaseClass implements Serializable, IList,
		IDetailed, IQuery {
	
	/** ʵ��������� */
	public static final String BusinessClassName = "TZGGXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "TZGGLB";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "TZGGXX";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private  final String QueryStyleName = "TZGGCX";
	/** ��ʵ�������ڱ������ */
	private  final String primaryKey = "id";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private  final String TableName = "T_YDZF_TZGG";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private String DetailedTitleText = "֪ͨ��ʾ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private  final String TitleName = "֪ͨ��ʾ�б�";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private  String QueryTitleText = "֪ͨ��ʾ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";

	HashMap<String, Object> styleList = null;
	public TZGGXX(){
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
	 * Create at: 2012-12-4 ����02:19:42
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
//		String order = "faburiqi desc limit " + x + "," + j;
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
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		// TODO Auto-generated method stub

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
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public String getListTitleText() {
		return TitleName;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		CurrentID = currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		Object object = styleList.get("mysql");
		String sql = object+getOrder();
		SqliteUtil su = SqliteUtil.getInstance();
		String newSql = sql.replace(",,", "");
		LogUtil.d("newSql sql is ===>", newSql);
		return su.queryBySqlReturnArrayListHashMap(newSql);
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
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "TZGGXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return null;
	}

	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GetTableName() {

		return TableName;
	}

	/* ʯ��ׯ"�ƶ��칫"������ */
	/**
	 * Description: ��ȡ����֮�ڵ�֪ͨ����
	 * @return ֪ͨ������Ϣ
	 * ArrayList<HashMap<String,Object>>
	 * @author �����
	 * Create at: 2012-12-4 ����02:20:01
	 */
	public ArrayList<HashMap<String, Object>> getLastThreeDaysTZGGXX() {
		
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -3);
			String oldDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar
					.getTime());
			String sql = "SELECT * FROM T_YDZF_TZGG WHERE FABURIQI > '"
					+ oldDate + "'";
			return SQLiteDataProvider.getInstance()
					.queryBySqlReturnArrayListHashMap(sql);
		
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
