package com.mapuni.android.base.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: LSGXXX.java 
 * Description: ��ʷ����
 * @author ����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ 
 * Create at: 2012-12-4 ����09:17:25
 */
public class LSGXXX extends BaseClass implements IDetailed, IList, IQuery,
		Serializable {

	/** ʵ��������� */
	public static final String BusinessClassName = "LSGXXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "LSGX";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "LSGX";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private  final String QueryStyleName = "LSGX";
	/** ��ʵ�������ڱ������ */
	private  final String primaryKey = "id";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private  final String tableName = "T_APK_Version";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private String DetailedTitleText = "��ʷ��������";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String ListTitleText = "��ʷ�����б�";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private String QueryTitleText = "��ʷ���²�ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;

	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String
	 * String
	 * @author ����
	 * Create at: 2012-12-4 ����09:29:38
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
		String order = "updatetime desc limit " + x + "," + j;
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
		return BaseClass.DBHelper.getList(tableName);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap,
				getOrder());
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(content));
		} catch (Exception e) {
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
			ExceptionManager.WriteCaughtEXP(e, "LSGXXX");
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
		return BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "LSGXXX");
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
