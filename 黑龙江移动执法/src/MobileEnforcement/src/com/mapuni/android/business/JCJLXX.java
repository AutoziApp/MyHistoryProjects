package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: JCJLXX.java Description: ����¼��ҵ����
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����10:49:35
 */
public class JCJLXX extends BaseClass implements Serializable, IDetailed, IList {

	/** ҵ��������� */
	public static final String BusinessClassName = "JCJLXX";
	/** ��ȡ��ҵ�����б���ʽ�ı��� */
	private  final String ListStyleName = "JCJL_RWXX";
	/** ��ȡ��ҵ��������������ʽ�ı��� */
	private  final String DetailedStyleName = "RWXX";
	/** ��ҵ�������ڱ������ */
	private  final String primaryKey = "Id";
	/** ���ڲ�ѯ�������ֶΣ�����ƴ��sql��� */
	private  final String order = "FBSJ asc";
	/** ��ѯ��ҵ����������Ϣ���õı��� */
	private  final String tableName = "T_YDZF_RWXX";
	/** ��ҵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "����¼��Ϣ����";
	/** ��ҵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private final String ListTitleText = "����¼��Ϣ�б�";
	/** ��ʼ����ǰ��ҵ�����б�����Ĵ�����Ŀǰ��û�õ� */
	public int ListScrollTimes;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ҵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;
	/** ��ҵ�����ѯ�����б��� */
	private ArrayList<HashMap<String, Object>> datalist;

	@Override
	public void setListScrolltimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
	}

	public void setDatalist(ArrayList<HashMap<String, Object>> datalist) {
		this.datalist = datalist;
	}

	@Override
	public String getListTitleText() {

		return ListTitleText;
	}

	@Override
	public String getCurrentID() {

		return CurrentID;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		this.CurrentID = currentIDValue;

	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {

		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap, order);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			/**
			 * ͨ������style_list.xml�����ø�ʽ ��styleList�������洢����������
			 */
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "JCJLXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {

		return Filter;
	}

	/**
	 * Description: Ϊ�ö������ò�ѯ������
	 * 
	 * @param filter
	 *            ��ѯ���������� void
	 * @author ����� Create at: 2012-12-3 ����10:54:21
	 */
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
	}

	@Override
	public String getDetailedTitleText() {

		return DetailedTitleText;
	}

	/**
	 * ͨ����BaseClass.DBHelper.getDetailed������ȡ�����ϸ��Ϣ
	 */
	@Override
	public HashMap<String, Object> getDetailed(String id) {
		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
		return BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
	}

	/**
	 * ͨ��XmlHelper.getStyleByName()�������õ���ȡ����ʽ��Ϣ ���ظ�һ��styleDetailList����
	 */
	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "RWXX");
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public String GetKeyField() {
		return null;
	}

	@Override
	public String GetTableName() {
		return null;
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
