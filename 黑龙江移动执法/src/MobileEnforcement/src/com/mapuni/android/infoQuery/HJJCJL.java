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

public class HJJCJL extends BaseClass implements Serializable, IDetailed,IList{

	/**实体类的名字*/
	public static final String BusinessClassName = "LSZFQK";
	/**获取该实体类列表样式的标题*/
	private static final String ListStyleName = "WRY_LSZFQK";
	/**获取该实体类数据详情样式的标题*/
	private static final String DetailedStyleName = "WRY_LSZFQK";
	
	/**该实体类所在表的主键*/
	private static final String primaryKey = "id";
	/**查询该实体类的相关信息所用的表名*/
	private static final String tableName = "T_WRY_LSZFQK";
	/**该实体类在显示详情是所用的名字标题*/
	private String DetailedTitleText = "环境监察记录";
	/**该实体类在显示列表的时候所用的名字标题*/
	private String ListTitleText = "环境监察记录";
	
	/**初始化当前该实体类列表滚动的次数*/
	public int  ListScrollTimes=1;
	/**当前对象的id值*/
	private String CurrentID = "";
	/**该实体类的筛选条件集合*/
	private HashMap<String, Object> Filter;
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
