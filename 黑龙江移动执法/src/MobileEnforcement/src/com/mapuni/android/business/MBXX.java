package com.mapuni.android.business;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: MBXX.java
 * Description:ģ����Ϣ
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-6 ����01:36:31
 */
public class MBXX extends BaseClass implements Serializable, IList, IQuery{
	/**FileName: MBXX.java
	 * Description:
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾
	 * Create at: 2012-12-6 ����01:37:09
	 */
	private  final  long serialVersionUID = 1L;
	
	/** ʵ��������� */
	public  final static String BusinessClassName = "MBXX";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private  final  String ListStyleName = "MBXX";
	///** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	//private static final String QueryStyleName = "XZCF";
	/** ��ʵ�������ڱ������ */
	private  final  String primaryKey = "tid";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private  final  String tableName = "YDZF_SpecialTemplate";
	///** ��ʵ��������ʾ���������õ����ֱ��� */
	//private String DetailedTitleText = "����������Ϣ����";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String ListTitleText = "��ѡ��ģ��";
	///** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	//private String QueryTitleText = "����������Ϣ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����id */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
	private HashMap<String, Object> Filter;

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
		
		
		
		return BaseClass.DBHelper.getOrderList(tableName, fliterHashMap, getOrder());
	}
	
	public String getFilterValue(String qymc){
		ArrayList<HashMap<String,Object>> myData;
		String res = null;
		ArrayList<HashMap<String,Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select Guid,QYDM,HYLB from t_wry_qyjbxx where qymc='"+ qymc + "'");
		if(result != null && result.size() > 0){
			ArrayList<HashMap<String,Object>> industries = new ArrayList<HashMap<String,Object>>();
			industries = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select CODE from Industry where CODE = '"+result.get(0).get("hylb")+"'");
//			industries = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select CODE from Industry where CODE = 'A'");
			myData = getIndustryData(industries);
			myData.addAll(industries);
			String str = getQueryString(myData, "code");
			myData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select tid from YDZF_IndustryTemplateLink where  IndustryCode "+str);
			res = getQueryString(myData, "tid");
		}
		return res;
	}
	
	/**
	 * �ݹ��ѯIndustry��
	 * @param list
	 * @return
	 */
	private ArrayList<HashMap<String,Object>> getIndustryData(ArrayList<HashMap<String,Object>> list){
		ArrayList<HashMap<String,Object>> r = new ArrayList<HashMap<String,Object>>();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<list.size();i++){
			if(i != 0)
				sb.append(",");
			sb.append("'"+list.get(i).get("code")+"'");
		}
		r = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap("select CODE from Industry where PID in ("+sb.toString()+")");
		if(r != null && r.size() > 0){
			r.addAll(getIndustryData(r));
		} 
		return r;
	}
	/**
	 * ʹ��key in ()�ķ�ʽ��ѯ���˷�������key��ߵĲ�ѯ����
	 * @param list
	 * @param key
	 * @return
	 */
	private String getQueryString(ArrayList<HashMap<String,Object>> list,String key){
		if(list == null || list.size() == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<list.size();i++){
			if(i!=0){
				sb.append(",");
			}
			sb.append("'"+list.get(i).get(key)+"'");
		}
		return sb.append(" in ("+sb.toString()+")").toString();
	}

	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count  - count; 
		int j = count;
		String order = "sortindex asc limit " + x + "," + j;
		return order;
	}
	
	@Override
	public HashMap<String, Object> getStyleList(Context context)
			throws IOException {
		HashMap<String, Object> styleList = null;
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "MBXX");
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
		// TODO Auto-generated method stub
		ListScrollTimes = listScrollTimes;
	}

	@Override
	public int GetListScrolltimes() {
		// TODO Auto-generated method stub
		return ListScrollTimes;
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

	@Override
	public String getQueryTitleText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		// TODO Auto-generated method stub
		Filter = filter;
	}

	@Override
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getListScrollTimes() {
		return ListScrollTimes;
	}

	public void setListScrollTimes(int listScrollTimes) {
		ListScrollTimes = listScrollTimes;
	}

	public  long getSerialversionuid() {
		return serialVersionUID;
	}

	public  String getListstylename() {
		return ListStyleName;
	}

	public  String getPrimarykey() {
		return primaryKey;
	}

	public  String getTablename() {
		return tableName;
	}

	public String getCurrentID() {
		return CurrentID;
	}

	public void setListTitleText(String listTitleText) {
		ListTitleText = listTitleText;
	}
	

}
