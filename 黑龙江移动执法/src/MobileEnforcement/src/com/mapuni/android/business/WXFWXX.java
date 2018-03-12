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
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: WXFWXX.java
 * Description: Σ�շ�����Ϣ
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����02:34:25
 */
public class WXFWXX extends BaseClass implements Serializable, IDetailed, IList {
	
	/** ʵ��������� */
	public static final String BusinessClassName = "WXFWXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "ZDWRY_WXFW";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "ZDWRY_WXFW";
	/** ��ʵ�������ڱ������ */
	private  final String primaryKey = "Guid";
	/** ������ֶΣ��˴�����ƴ��sql�����������ѯ */
	private  final String order = "wxfwmc asc";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private  final String tableName = "V_ZDWRY_WXFW";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private String DetailedTitleText = "Σ�շ�����Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String GridTitleText = "Σ�շ�����Ϣ�б�";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;
	public WXFWXX(){
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(Global.getGlobalInstance()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			ExceptionManager.WriteCaughtEXP(e, "WXFWXX");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		Object object = styleList.get("mysql");
		String sql = (String) object;
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

	/**
	 * Description: ���ò�ѯ����
	 * @param filter ��ѯ����
	 * void
	 * @author �����
	 * Create at: 2012-12-4 ����02:43:32
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
			ExceptionManager.WriteCaughtEXP(e, "WXFWXX");
			// TODO Auto-generated catch block
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
