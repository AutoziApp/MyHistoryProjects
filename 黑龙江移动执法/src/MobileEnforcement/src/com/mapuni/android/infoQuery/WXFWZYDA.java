package com.mapuni.android.infoQuery;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

public class WXFWZYDA extends BaseClass implements Serializable, IDetailed,IList{

	/**ʵ���������*/
	public static final String BusinessClassName = "WXFWZYDA";
	/**��ȡ��ʵ�����б���ʽ�ı���*/
	private static final String ListStyleName = "WRY_WXFWZYDA";
	/**��ȡ��ʵ��������������ʽ�ı���*/
	private static final String DetailedStyleName = "WRY_WXFWZYDA";
	
	/**��ʵ�������ڱ������*/
	private static final String primaryKey = "id";
	/**��ѯ��ʵ����������Ϣ���õı���*/
	private static final String tableName = "T_WRY_WXFWZYDA";
	/**��ʵ��������ʾ���������õ����ֱ���*/
	private String DetailedTitleText = "Σ�շ���ת�Ƶ���";
	/**��ʵ��������ʾ�б��ʱ�����õ����ֱ���*/
	private String ListTitleText = "Σ�շ���ת�Ƶ���";
	
	/**��ʼ����ǰ��ʵ�����б�����Ĵ���*/
	public int  ListScrollTimes=1;
	/**��ǰ�����idֵ*/
	private String CurrentID = "";
	/**��ʵ�����ɸѡ��������*/
	private HashMap<String, Object> Filter;
	private HashMap<String, Object> dataMap;
	@Override
	public String getListTitleText() {
		// TODO Auto-generated method stub
		return ListTitleText;
	}

	@Override
	public void setCurrentID(String currentIDValue) {
		this.CurrentID=currentIDValue;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		
		return DBHelper.getList(tableName);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		String qydm = fliterHashMap.get("QYDM").toString();
		
		String sql = "select * from T_WRY_WXFWZYDA where QYDM = '"+ qydm +"'";
		return BaseClass.DBHelper.queryBySqlReturnArrayListHashMap(sql);
	
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
		// TODO Auto-generated method stub
		return Filter;
	}

	@Override
	public void setListScrolltimes(int listScrollTimes) {
		this.ListScrollTimes=listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		// TODO Auto-generated method stub
		return ListScrollTimes;
	}

	@Override
	public String getDetailedTitleText() {
		// TODO Auto-generated method stub
		return DetailedTitleText;
	}

	@Override
	public String getCurrentID() {
		// TODO Auto-generated method stub
		return CurrentID;
	}

	@Override
	public HashMap<String, Object> getDetailed(String id) {

		HashMap<String, String> primaryKeyMap = new HashMap<String, String>();
		primaryKeyMap.put("key", primaryKey);
		primaryKeyMap.put("keyValue", id);
	
		dataMap  = SqliteUtil.getInstance().getDetailed(tableName, primaryKeyMap);
		
		dataMap.put("zysj", dateFormat(dataMap.get("zysj").toString()));
		
		return dataMap;
	
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
		return primaryKey;
	}

	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}
	
	/** ��ʽ��ʱ�� */
	private String dateFormat(String str) {
		String [] strs = str.split("T");
		String[] times = strs[1].split(":");
		return strs[0] + " " + times[0] + ":" + times[1] ;
	}


}
