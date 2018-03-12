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
 * 
 */
public class HJFXFFSS extends BaseClass implements Serializable, IDetailed, IList{
	/**ʵ���������*/
	public static final String BusinessClassName = "HJFXFFSS";
	/**��ȡ��ʵ�����б���ʽ�ı���*/
	private static final String ListStyleName = "WRY_HJFXFFSS";
	/**��ȡ��ʵ��������������ʽ�ı���*/
	private static final String DetailedStyleName = "WRY_HJFXFFSS";
	
	/**��ʵ�������ڱ������*/
	private static final String primaryKey = "id";
	/**��ѯ��ʵ����������Ϣ���õı���*/
	private static final String tableName = "T_WRY_HJFXFFSS";
	/**��ʵ��������ʾ���������õ����ֱ���*/
	private String DetailedTitleText = "�������շ�����ʩ����";
	/**��ʵ��������ʾ�б��ʱ�����õ����ֱ���*/
	private String ListTitleText = "�������շ�����ʩ�б�";
	
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
		String sql = "SELECT * FROM T_WRY_HJFXFFSS";
		return SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {// TODO Auto-generated method stub
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
		return primaryKey;
	}

	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}

}
