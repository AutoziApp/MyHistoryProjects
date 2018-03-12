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
 * FileName: GLJBQK.java
 * Description:��¯�������ҵ����
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����09:04:53
 */
public class GLJBQK extends BaseClass implements Serializable, IDetailed,
		 IList {
	
	/**ҵ���������*/
	public  final static String BusinessClassName = "GLJBQK";
	/**��ȡ��ҵ�����б���ʽ�ı���*/
	private  final String ListStyleName = "ZDWRY_GLJBQK";
	/**��ȡ��ҵ��������������ʽ�ı���*/
	private  final String DetailedStyleName = "ZDWRY_GLJBQK";
	/**��ҵ�������ڱ������*/
	private  final String primaryKey = "Guid";
	/**���ڲ�ѯ�������ֶΣ�����ƴ��sql���*/
	private  final String order="qymc asc";										     //������ֶ�
	/**��ѯ��ҵ����������Ϣ���õı���*/
	private  final String tableName = "V_ZDWRY_GLJBQK";//����
	/**��ҵ��������ʾ���������õ����ֱ���*/
	private String DetailedTitleText = "��¯�����������";
	/**��ҵ��������ʾ�б��ʱ�����õ����ֱ���*/
	private String GridTitleText = "��¯��������б�";
	/**��ʼ����ǰ��ҵ�����б�����Ĵ�����Ŀǰ��û�õ�*/
	public int  ListScrollTimes;
	/**��ǰ�����idֵ*/
	private String CurrentID = "";
	/**��ҵ�����ɸѡ��������*/
	private HashMap<String, Object> Filter;
	
	
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
		return BaseClass.DBHelper.getDetailed(tableName, primaryKeyMap);
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			//��ȡ��Ҫ��ʾ����Ϣ
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GLJBQK");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return styleDetailList;
	}


	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		return BaseClass.DBHelper.getList(tableName);
	}

    /**
     *  Description:ͨ��������ȡ��¯��Ϣ�б�
     * @param fliterHashMap ��ȡ�б���Ϣ����������
     * @author �����
     * @return ArrayList<HashMap<String, Object>> �����б���Ϣ
     * Create at: 2012-12-3 ����09:12:55
     */
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap,order);
	}
	
	/**
	 * Description: ��ҵ����Ĳ�ѯ����������ֵ
	 * @param filter Ҫ��ȡ���ݵ�����
	 * void 
	 * @author �����
	 * Create at: 2012-12-3 ����09:12:55
	 */
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
		}


	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {

		HashMap<String, Object> styleList = null;
		try {
			//��ȡ��ʽ
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "GLJBQK");
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
