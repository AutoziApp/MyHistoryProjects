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
 * fileName : 在线监测建设情况
 * @author huang
 * @version 1.1.0
 */
public class ZXJKXX extends BaseClass implements Serializable, IDetailed,  IList{

	/** 实体类的名字 */
	public static final String BusinessClassName = "ZXJKXX";
	/** 获取该实体类列表样式的标题 */
	private static final String ListStyleName = "WRY_ZXJKXX";
	/** 获取该实体类数据详情样式的标题 */
	private static final String DetailedStyleName = "WRY_ZXJKXX";
	/** 获取该实体类查询样式的标题 */
	private static final String QueryStyleName = "ZXJKXX";
	/** 该实体类所在表的主键 */
	private static final String primaryKey = "Id";
	/** 获取该实体类底部菜单的标题 */
	private static final String BottomMenuName = "XQ";
	/** 查询该实体类的相关信息所用的表名 */
	private static final String tableName = "T_WRY_ZXJKXX";
	/** 该实体类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "在线监控信息详情";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private final String ListTitleText = "在线监控信息列表";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;

	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String
	 * String
	 * @author 王红娟
	 * Create at: 2012-12-3 上午09:41:54
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
