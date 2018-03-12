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
 * Description: 代办管理（收文待办，发文待办）
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 下午01:47:32
 */
public class SWDB extends BaseClass implements Serializable,IList,IDetailed{
	
	/** 实体类的名字 */
	public static final String BusinessClassName = "SWDB";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private  String TitleText = "待办管理";
	/** 获取该实体类列表样式的标题 */
	private String ListStyleName = "YDBG";
	/** 查询该实体类的相关信息所用的表名 */
	private  final String TableName = "T_YDZF_workitem";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id值 */
	private String CurrentID = "";
	
	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-4 下午01:48:35
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
	
	/**石家庄"移动办公"需求变更
	 * Description: 获取代办公文
	 * @param userId 用户di
	 * @return 返回待办公文的列表
	 * ArrayList<HashMap<String,Object>>
	 * @author 王红娟
	 * Create at: 2012-12-4 下午01:51:30
	 */
	public ArrayList<HashMap<String, Object>> getDBGW(String userId) {
		
			String oaID = returnOAId(userId);
			String sql = "SELECT * FROM T_YDZF_workitem WHERE state = 'Running' and owner = '" + oaID + "' order by createdate desc";
			return SQLiteDataProvider.getInstance().queryBySqlReturnArrayListHashMap(sql);
		
		
	}
	
	/**
	 * Description: 将移动执法的用户id转化为移动办公里用户的id
	 * @param ydzfid 移动执法里用户的id
	 * @return 返回oa系统里的用户id
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-4 下午01:52:29
	 */
	private String returnOAId(String ydzfid){
		String userid = "";
		try {
			userid = ydzfid.substring(5, ydzfid.length());
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "TZGGXX：移动执法ID转移动办公ID");
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
