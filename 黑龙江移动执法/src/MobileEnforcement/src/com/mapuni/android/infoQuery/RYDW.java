package com.mapuni.android.infoQuery;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.interfaces.IQuery;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.base.util.PinyinOperator;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: Users.java Description: 用户信息
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 下午02:21:20
 */
public class RYDW extends BaseClass implements Serializable, IQuery, IList,
		IDetailed {

	/** 实体类的名字 */
	public static final String BusinessClassName = "RYDW";
	/** 获取该实体类列表样式的标题 */
	private final String ListStyleName = "RYDW";
	/** 获取该实体类数据详情样式的标题 */
	private final String DetailedStyleName = "PC_RYDW";
	/** 获取该实体类查询样式的标题 */
	private final String QueryStyleName = "RYDW";
	/** 该实体类所在表的主键 */
	private final String primaryKey = "userid";
	/** 查询该实体类的相关信息所用的表名 */
	private final String tableName = "V_Users";
	/** 该实体类在显示详情是所用的名字标题 */
	private final String DetailedTitleText = "人员信息";
	/** 该实体类在显示列表的时候所用的名字标题 */
	private String GridTitleText = "人员列表";
	/** 该实体类在执行查询操作的时候所用的名字标题 */
	private final String QueryTitleText = "用户信息查询";
	/** 初始化当前该实体类列表滚动的次数 */
	public int ListScrollTimes = 1;
	/** 当前对象的id值 */
	private String CurrentID = "";
	/** 该实体类的筛选条件集合 */
	private HashMap<String, Object> Filter;
	HashMap<String, Object> styleList = null;

	public RYDW(Context context) {
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(context));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RYDW() {

	}

	/**
	 * Description: 用来实现查询列表的时候进行分页显示
	 * 
	 * @return 返回一个用来拼接分页显示sql语句的字符串 String String
	 * @author 王红娟 Create at: 2012-12-4 下午02:25:19
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = GetListScrolltimes() * count - count;
		int j = count;
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

	public void setListTitleText(String GridTitleText) {
		this.GridTitleText = GridTitleText;
	}

	@Override
	public String getListTitleText() {
		return GridTitleText;
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
		Object object = styleList.get("mysql");
		String sql = object + getOrder();
		SqliteUtil su = SqliteUtil.getInstance();

		
		if(fliterHashMap == null){
			fliterHashMap = new HashMap<String, Object>();
		}
		
		// 获得当前登录用户的职务
		HashMap<String, Object> conditions_user = new HashMap<String, Object>();
		conditions_user = new HashMap<String, Object>();
		conditions_user.put("UserID", Global.getGlobalInstance().getUserid());
		String userZw = SqliteUtil.getInstance()
				.getList("zw", conditions_user, "PC_Users").get(0).get("zw")
				.toString();
		
		if (userZw.equals("0") || userZw.equals("1")) { // 局长
			fliterHashMap.put("'1'",
					"2' or zw=0 or zw=1 or zw=2 or zw=3 or zw=4 or '2'='1");
		} else if (userZw.equals("2")) { // 副局长
			
			HashMap<String, Object> user = new HashMap<String, Object>();
			user = new HashMap<String, Object>();
			user.put("fgleader", Global.getGlobalInstance().getUserid());
			
			List<HashMap<String, Object>> list = SqliteUtil.getInstance().getList("depid", user, "PC_DepartmentInfo");
			
			String string = "";
			for (int i = 0; i < list.size(); i++) {
				string += "depid='"+list.get(i).get("depid").toString()+"'";
				if(i != list.size() - 1) {
					string += " or ";
				}
			}
			
			string += " or userid='"+Global.getGlobalInstance().getUserid()+"'";
			
			fliterHashMap.put("depid",
					"2' or "+ string + " or '2'='1");
		} else if (userZw.equals("3")) {// 科长
			fliterHashMap.put("depid", Global.getGlobalInstance().getDepId());
		} else { // 科员
			fliterHashMap.put("userid", Global.getGlobalInstance().getUserid());
		}
		
		
		String filterSql = BaseClass.DBHelper.getFilterForSql(fliterHashMap);
		String newSql = null;
		if ("".equals(filterSql)) {
			newSql = sql.replace(",,", "");
		} else {
			newSql = sql.replace(",,", filterSql);
		}
		LogUtil.d("newSql sql is ===>", newSql);
		ArrayList<HashMap<String, Object>> data = su
				.queryBySqlReturnArrayListHashMap(newSql);
		// data = orderListByFirstPin(data);
		return data;
	}

	/**
	 * Description:排序
	 * 
	 * @param data
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-31 下午03:52:12
	 */
	private ArrayList<HashMap<String, Object>> orderListByFirstPin(
			ArrayList<HashMap<String, Object>> data) {
		int num = data.size();
		HashMap<String, Object>[] datas = new HashMap[num];
		for (int i = 0; i < num; i++) {
			datas[i] = data.get(i);
		}

		for (int i = 0; i < num - 1; i++) {
			for (int j = 0; j < num - i - 1; j++) {
				String namePinyin1 = PinyinOperator
						.getPinYinFirstChar((String) datas[j].get("u_realname"));
				String namePinyin2 = PinyinOperator
						.getPinYinFirstChar((String) datas[j + 1]
								.get("u_realname"));
				if (namePinyin1.compareTo(namePinyin2) > 0) {
					HashMap<String, Object> tempMap = datas[j];
					datas[j] = datas[j + 1];
					datas[j + 1] = tempMap;
				}
			}
		}
		// change back to List
		data.clear();
		for (HashMap<String, Object> map : datas) {
			data.add(map);
		}
		return data;
	}

	@Override
	public HashMap<String, Object> getStyleList(Context content)
			throws IOException {
		try {
			styleList = XmlHelper.getListStyleByName(ListStyleName,
					getStyleListInputStream(content));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "PWSFXX");
			e.printStackTrace();
		}
		return styleList;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return Filter;
	}

	@Override
	public String getQueryTitleText() {
		return QueryTitleText;
	}

	@Override
	public HashMap<String, Object> getQuery() {
		return null;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(QueryStyleName,
					getStyleQueryInputStream(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return styleDetailList;
	}

	@Override
	public void setFilter(HashMap<String, Object> filter) {
		Filter = filter;
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
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition) {
		List<HashMap<String, Object>> spinnerdata = null;
		spinnerdata = BaseClass.DBHelper.getList(AdapterFileName,
				querycondition);
		return spinnerdata;
	}

	public List<String> getAllUser(SharedPreferences sp) {// 返回sharedpreference中所有的用户名
		List<String> list = new ArrayList<String>();
		HashMap<String, String> map = (HashMap<String, String>) sp.getAll();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			list.add(key);
		}
		return list;
	}

	/**
	 * Description: 根据用户名查询输入密码是否正确
	 * 
	 * @param user
	 *            用户登录名称
	 * @return 返回用户信息 ArrayList<HashMap<String,Object>>
	 * @author 王红娟 Create at: 2012-12-4 下午02:25:51
	 */
	public ArrayList<HashMap<String, Object>> checkPwd(String user) {
		ArrayList<HashMap<String, Object>> userl = null;
		try {
			userl = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select u_password from PC_Users where 1=1 and U_LoginName='"
							+ user + "'");
		} catch (SQLiteException e1) {
			// datebaseError();
			e1.printStackTrace();
		}
		return userl;
	}

	/**
	 * Description: 根据用户名密码查询本地数据库获取用户信息并返回
	 * 
	 * @param user
	 *            用户登录名称
	 * @param pass
	 *            用户登录密码
	 * @return 返回用户信息 ArrayList<HashMap<String,Object>>
	 * @author 王红娟 Create at: 2012-12-4 下午02:26:35
	 */
	public ArrayList<HashMap<String, Object>> checkUsers(String user,
			String pass) {
		ArrayList<HashMap<String, Object>> userl = null;
		try {
			userl = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from PC_Users where 1=1 and U_LoginName='" + user
							+ "' and U_password='" + pass + "'");
		} catch (SQLiteException e1) {
			// datebaseError();
			e1.printStackTrace();
		}
		return userl;
	}

	/**
	 * Description: 加载当前用户的权限集
	 * 
	 * @return 返回当前登录用户的权限集合 ArrayList<HashMap<String,Object>>
	 * @author 王红娟 Create at: 2012-12-4 下午02:27:07
	 */
	public HashMap<String, Object> checkAuthor() {
		ArrayList<HashMap<String, Object>> authoritylist = null;
		HashMap<String, Object> authormap = new HashMap<String, Object>();
		try {
			authoritylist = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from PC_UserModelPession where 1=1 and userid='"
									+ Global.getGlobalInstance().getUserid()
											.toString() + "'");
			for (HashMap<String, Object> map : authoritylist) {

				authormap.put((String) map.get("modelid"), true);
			}
			Global.getGlobalInstance().setAuthoritylist(authormap);
		} catch (SQLiteException e1) {
			// datebaseError();
			e1.printStackTrace();
		}
		return authormap;
	}

	/**
	 * Description: checkAuthor（）方法的重载，查询所有权限集
	 * 
	 * @param all
	 *            任意字符串
	 * @return 所有的权限集 ArrayList<HashMap<String,Object>>
	 * @author 王红娟 Create at: 2012-12-4 下午02:27:45
	 */
	public HashMap<String, Object> checkAuthor(String all) {
		ArrayList<HashMap<String, Object>> authoritylist = null;

		HashMap<String, Object> authormap = new HashMap<String, Object>();
		try {
			authoritylist = SqliteUtil.getInstance()
					.queryBySqlReturnArrayListHashMap(
							"select * from PC_UserModelPession where 1=1");
			for (HashMap<String, Object> map : authoritylist) {

				authormap.put((String) map.get("modelid"), true);
			}
			Global.getGlobalInstance().setAuthoritylist(authormap);
		} catch (SQLiteException e1) {
			// datebaseError();
			e1.printStackTrace();
		}
		return authormap;
	}

	public String getRealNameByUserID(String userID) {
		String realName = "";
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select u_realname from PC_Users where userid='"
								+ userID + "'");
		if (data != null && data.size() > 0) {
			realName = data.get(0).get("u_realname").toString();
		}
		return realName;
	}

	/**
	 * Description: 通过用户编号查询该用户的某一状态任务
	 * 
	 * @param userID
	 *            用户编号
	 * @param status
	 *            任务状态
	 * @return 任务信息结合 ArrayList<HashMap<String,Object>>
	 * @author 柳思远 Create at: 2012-12-4 上午11:49:42
	 */
	public ArrayList<HashMap<String, Object>> getTaskByUserIDandStatus(
			String userID, String status) {
		ArrayList<HashMap<String, Object>> waitingTaskList = null;
		/****** 注释的地方是柳思远写的 ******/
		// String sql =
		// "select Guid,RWMC,RWBH,FBSJ,RWZT from T_YDZF_RWXX_USER as a "
		// + "inner join T_YDZF_RWXX as b "
		// + "on a.RWXXBH = b.RWBH where a.UserID = '"
		// + userID
		// + "' and b.RWZT='" + status + "' order by FBSJ desc";
		/************ 王红娟修改20130411（通知栏和待执行列表的任务条数不一致） ****************/
		String sql = "select distinct(a.RWBH),a.Guid,a.RWMC,a.FBSJ,a.RWZT,a.JJCD,a.BJQX,a.BZ from T_YDZF_RWXX a "
				+ "left join T_YDZF_RWXX_USER b on b.[RWXXBH]=a.rwbh where a.RWLX<>'003' "
				+ "and (b.[UserID]= '"
				+ userID
				+ "' or a.[FBR] = '"
				+ userID
				+ "') and rwzt = '" + status + "'";

		waitingTaskList = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sql);

		return waitingTaskList;
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
		ArrayList<HashMap<String, Object>> styleDetailed = getStyleDetailed(Global
				.getGlobalInstance());
		Object mySql = styleDetailed.get(styleDetailed.size() - 1).get(
				"queryHint")
				+ SqliteUtil.getInstance().getFilterForSqlDetail(primaryKeyMap);
		return SqliteUtil.getInstance().getDataMapBySqlForDetailed(
				String.valueOf(mySql));
	}

	@Override
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context) {
		ArrayList<HashMap<String, Object>> styleDetailList = null;
		try {
			styleDetailList = XmlHelper.getStyleByName(DetailedStyleName,
					getStyleDetailedInputStream(context));
		} catch (Exception e) {
			ExceptionManager.WriteCaughtEXP(e, "PWSFXX");
			e.printStackTrace();
		}
		return styleDetailList;
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
