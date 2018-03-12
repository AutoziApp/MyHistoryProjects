package com.mapuni.android.business;

import java.io.FileNotFoundException;
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
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: SWDB.java
 * Description: ����������Ĵ��죬���Ĵ��죩
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����01:47:32
 */
public class SWDB extends BaseClass implements Serializable,IList,IDetailed{
	
	/** ʵ��������� */
	public static final String BusinessClassName = "SWDB";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private  String TitleText = "�������";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private String ListStyleName = "YDBG";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private  final String TableName = "T_YDZF_workitem";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	
	/**
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String
	 * String
	 * @author �����
	 * Create at: 2012-12-4 ����01:48:35
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber(); 
		int x = GetListScrolltimes() * count  - count;
		int j = count;
		String order = "createdate desc limit " + x + "," + j;
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
		// TODO Auto-generated method stub
		return TitleText;
	}
	@Override
	public void setCurrentID(String currentIDValue) {
		// TODO Auto-generated method stub
		this.CurrentID=currentIDValue;
		
	}
	@Override
	public ArrayList<HashMap<String, Object>> getDataList() {
		// TODO Auto-generated method stub
        HashMap<String, Object> condition=new HashMap<String, Object>();
        condition.put("state", "Running");
		return SQLiteDataProvider.getInstance().getOrderList("T_YDZF_workitem",condition,getOrder());
	}
	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		// TODO Auto-generated method stub

		return null;
	}
	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		// TODO Auto-generated method stub
		return XmlHelper.getListStyleByName(ListStyleName, getStyleListInputStream(context));
	}
	@Override
	public HashMap<String, Object> getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return TableName;
	}
	/********/
	@Override
	public String getDetailedTitleText() {
		// TODO Auto-generated method stub
		return TitleText;
	}
	@Override
	public String getCurrentID() {
		// TODO Auto-generated method stub
		return CurrentID;
	}
	@Override
	public HashMap<String, Object> getDetailed(String id) {
		// TODO Auto-generated method stub
		HashMap<String, Object> data;
		HashMap<String, Object> condition=new HashMap<String, Object>();
        condition.put("topic", id);
        ArrayList<HashMap<String, Object>> result=SQLiteDataProvider.getInstance().getList("T_YDZF_workitem", condition);
        data=result.get(0);
        if(data==null)
        	return new HashMap<String, Object>();
        else
		return data;
	}
	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		// TODO Auto-generated method stub
		try {
			return XmlHelper.getStyleByName("YDBGXX", getStyleDetailedInputStream(context));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**ʯ��ׯ"�ƶ��칫"������
	 * Description: ��ȡ���칫��
	 * @param userId �û�di
	 * @return ���ش��칫�ĵ��б�
	 * ArrayList<HashMap<String,Object>>
	 * @author �����
	 * Create at: 2012-12-4 ����01:51:30
	 */
	public ArrayList<HashMap<String, Object>> getDBGW(String userId) {
		
			String oaID = returnOAId(userId);
			String sql = "SELECT * FROM T_YDZF_workitem WHERE state = 'Running' and owner = '" + oaID + "' order by createdate desc";
			return SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		
		
	}
	
	/**
	 * Description: ���ƶ�ִ�����û�idת��Ϊ�ƶ��칫���û���id
	 * @param ydzfid �ƶ�ִ�����û���id
	 * @return ����oaϵͳ����û�id
	 * String
	 * @author �����
	 * Create at: 2012-12-4 ����01:52:29
	 */
	private String returnOAId(String ydzfid){
		String userid = "";
		try {
			userid = ydzfid.substring(5, ydzfid.length());
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "TZGGXX���ƶ�ִ��IDת�ƶ��칫ID");
		}
		return userid;
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
