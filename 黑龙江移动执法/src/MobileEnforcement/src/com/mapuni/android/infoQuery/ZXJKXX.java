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
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;
/**
 * fileName : ���߼�⽨�����
 * @author huang
 * @version 1.1.0
 */
public class ZXJKXX extends BaseClass implements Serializable, IDetailed,  IList{

	/** ʵ��������� */
	public static final String BusinessClassName = "ZXJKXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private static final String ListStyleName = "WRY_ZXJKXX";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private static final String DetailedStyleName = "WRY_ZXJKXX";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private static final String QueryStyleName = "ZXJKXX";
	/** ��ʵ�������ڱ������ */
	private static final String primaryKey = "Id";
	/** ��ȡ��ʵ����ײ��˵��ı��� */
	private static final String BottomMenuName = "XQ";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private static final String tableName = "T_WRY_ZXJKXX";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "���߼����Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private final String ListTitleText = "���߼����Ϣ�б�";
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
	 * @author �����
	 * Create at: 2012-12-3 ����09:41:54
	 */
	public String getOrder() {
		 int count = Global.getGlobalInstance().getListNumber(); 
		int x=GetListScrolltimes()*count-count;
		int j = count;
//		String order="wjsj desc limit "+x+","+j;
		String order = x + "," + j;
		return order;
	}
	
	@Override
	public String getListTitleText() {
		return ListTitleText;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		// TODO Auto-generated method stub
		this.CurrentID=currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		String sql = "SELECT   * FROM T_WRY_ZXJKXX";
		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		SqliteUtil su = SqliteUtil.getInstance();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("WRYBH", fliterHashMap.get("QYDM"));
		return su.getList(tableName, hashMap);
	}

	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
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
	public void setListScrolltimes(int listScrollTimes) {
		this.ListScrollTimes=listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		return ListScrollTimes;
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
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBottomMenuName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return primaryKey;
	}

	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}

}
