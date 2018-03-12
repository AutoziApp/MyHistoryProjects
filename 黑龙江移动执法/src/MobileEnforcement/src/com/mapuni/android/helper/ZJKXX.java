package com.mapuni.android.helper;

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
 * FileName: ZJKXX.java Description: ר�ҿ���Ϣ
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����04:54:02
 */
public class ZJKXX extends BaseClass implements IDetailed, IList, IQuery, Serializable {

	/** ʵ��������� */
	public static final String BusinessClassName = "ZJKXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private final String ListStyleName = "HJYJ_ZJK";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private final String DetailedStyleName = "HJYJ_ZJK";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private final String QueryStyleName = "ZJK";
	/** ��ʵ�������ڱ������ */
	private final String primaryKey = "ExpID";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private final String tableName = "PC_Exp";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "ר�ҿ���Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private final String ListTitleText = "ר�ҿ���Ϣ�б�";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private final String QueryTitleText = "ר�ҿ���Ϣ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;

	HashMap<String, Object> styleList = null;

	public ZJKXX() {
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
	 * @author ����� Create at: 2012-12-4 ����04:56:48
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
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
		String coulm = "ExpID,Exp_Name,Exp_Sex,Exp_Date,Exp_Political,Exp_Native,Exp_Place,Exp_Photo,Exp_WorkUnit,Exp_Job,Exp_HighEdt,Exp_Major,Exp_Tel,Exp_ExpertType,UpdateTime";
		String limit = getOrder();
		return DBHelper.getLimitDataList(tableName, coulm, null, limit, null);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		String coulm = "ExpID,Exp_Name,Exp_Sex,Exp_Date,Exp_Political,Exp_Native,Exp_Place,Exp_Photo,Exp_WorkUnit,Exp_Job,Exp_HighEdt,Exp_Major,Exp_Tel,Exp_ExpertType,UpdateTime";
		String limit = getOrder();
		return DBHelper.getLimitDataList(tableName, coulm, fliterHashMap, limit, null);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content) throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "ZJKXX");
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
			ExceptionManager.WriteCaughtEXP(e, "ZJKXX");
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
		return SqliteUtil.getInstance().getDetailed(tableName, primaryKeyMap);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName, getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "ZJKXX");
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
