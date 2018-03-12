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
 * Description:模板信息
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-6 下午01:36:31
 */
public class MBXX extends BaseClass implements Serializable, IList, IQuery{
	/**FileName: MBXX.java
	 * Description:
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-6 下午01:37:09
	 */
	private  final  long serialVersionUID = 1L;
	
	/** 实体类的名字 */
	public  final static String BusinessClassName = "MBXX";
	/** 获取该实体类列表样式的标题 */
	private  final  String ListStyleName = "MBXX";
	///** 获取该实体类查询样式的标题 */
	//private static final String QueryStyleName = "XZCF";
	/** 该实体类所在表的主键 */
	private  final  String primaryKey = "tid";
	/** 查询该实体类的相关信息所用的表名 */
	private  final  String tableName = "YDZF_SpecialTemplate";
	///** 该实体类在显示详情是所用的名字标题 */
	//private String DetailedTitleText = "行政处罚信息详情";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private String ListTitleText = "请选择模板";
	///** 该实体类在执行查询操作的时候所用的名字标题 */
	//private String QueryTitleText = "行政处罚信息查询";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
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
	 * 递归查询Industry表
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
	 * 使用key in ()的方式查询，此方法返回key后边的查询条件
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
