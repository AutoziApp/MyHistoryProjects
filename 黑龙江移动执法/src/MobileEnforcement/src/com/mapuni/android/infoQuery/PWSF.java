package com.mapuni.android.infoQuery;

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
 * �����շ�
 *
 */
public class PWSF extends BaseClass implements Serializable, IDetailed,IList {
	
	/**ʵ���������*/
	public static final String BusinessClassName = "PWSF";
	/**��ȡ��ʵ�����б���ʽ�ı���*/
	private static final String ListStyleName = "WRY_PWSF";
	/**��ȡ��ʵ��������������ʽ�ı���*/
	private static final String DetailedStyleName = "WRY_PWSF";
	
	/**��ʵ�������ڱ������*/
	private static final String primaryKey = "id";
	/** ��ȡ��ʵ����ײ��˵��ı��� */
	private static final String BottomMenuName = "XQ";
	/**��ѯ��ʵ����������Ϣ���õı���*/
	private static final String tableName = "T_WRY_PWSF";
	/**��ʵ��������ʾ���������õ����ֱ���*/
	private String DetailedTitleText = "�����շ�����";
	/**��ʵ��������ʾ�б��ʱ�����õ����ֱ���*/
	private String ListTitleText = "�����շ�";
	
	/**��ʼ����ǰ��ʵ�����б�����Ĵ���*/
	public int  ListScrollTimes=1;
	/**��ǰ�����idֵ*/
	private String CurrentID = "";
	/**��ʵ�����ɸѡ��������*/
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
//		return BaseClass.DBHelper.getList(tableName);
		String sql = "SELECT   * FROM T_WRY_PWSF";
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
	public HashMap<String, Object> getStyleList(Context content)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "PWSF");
			e.printStackTrace();
		}
		return styleList;
	}


	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
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
			ExceptionManager.WriteCaughtEXP(e, "PWSF");
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
	public ArrayList<HashMap<String, Object>> getbottomname(Context context) {
		ArrayList<HashMap<String, Object>> BottomMenuList = null;
		try {
			BottomMenuList = XmlHelper.getMenuFromXml(context, BottomMenuName,"item","bottommenu",getBottomMenuInputStream(context));
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
