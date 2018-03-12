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
 * FileName: Users.java Description: �û���Ϣ
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����02:21:20
 */
public class RYDW extends BaseClass implements Serializable, IQuery, IList,
		IDetailed {

	/** ʵ��������� */
	public static final String BusinessClassName = "RYDW";
	/** ��ȡ��ʵ�����б���ʽ�ı��� */
	private final String ListStyleName = "RYDW";
	/** ��ȡ��ʵ��������������ʽ�ı��� */
	private final String DetailedStyleName = "PC_RYDW";
	/** ��ȡ��ʵ�����ѯ��ʽ�ı��� */
	private final String QueryStyleName = "RYDW";
	/** ��ʵ�������ڱ������ */
	private final String primaryKey = "userid";
	/** ��ѯ��ʵ����������Ϣ���õı��� */
	private final String tableName = "V_Users";
	/** ��ʵ��������ʾ���������õ����ֱ��� */
	private final String DetailedTitleText = "��Ա��Ϣ";
	/** ��ʵ��������ʾ�б��ʱ�����õ����ֱ��� */
	private String GridTitleText = "��Ա�б�";
	/** ��ʵ������ִ�в�ѯ������ʱ�����õ����ֱ��� */
	private final String QueryTitleText = "�û���Ϣ��ѯ";
	/** ��ʼ����ǰ��ʵ�����б�����Ĵ��� */
	public int ListScrollTimes = 1;
	/** ��ǰ�����idֵ */
	private String CurrentID = "";
	/** ��ʵ�����ɸѡ�������� */
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
	 * Description: ����ʵ�ֲ�ѯ�б��ʱ����з�ҳ��ʾ
	 * 
	 * @return ����һ������ƴ�ӷ�ҳ��ʾsql�����ַ��� String String
	 * @author ����� Create at: 2012-12-4 ����02:25:19
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
		
		// ��õ�ǰ��¼�û���ְ��
		HashMap<String, Object> conditions_user = new HashMap<String, Object>();
		conditions_user = new HashMap<String, Object>();
		conditions_user.put("UserID", Global.getGlobalInstance().getUserid());
		String userZw = SqliteUtil.getInstance()
				.getList("zw", conditions_user, "PC_Users").get(0).get("zw")
				.toString();
		
		if (userZw.equals("0") || userZw.equals("1")) { // �ֳ�
			fliterHashMap.put("'1'",
					"2' or zw=0 or zw=1 or zw=2 or zw=3 or zw=4 or '2'='1");
		} else if (userZw.equals("2")) { // ���ֳ�
			
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
		} else if (userZw.equals("3")) {// �Ƴ�
			fliterHashMap.put("depid", Global.getGlobalInstance().getDepId());
		} else { // ��Ա
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
	 * Description:����
	 * 
	 * @param data
	 * @return
	 * @author Administrator<br>
	 *         Create at: 2013-1-31 ����03:52:12
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

	public List<String> getAllUser(SharedPreferences sp) {// ����sharedpreference�����е��û���
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
	 * Description: �����û�����ѯ���������Ƿ���ȷ
	 * 
	 * @param user
	 *            �û���¼����
	 * @return �����û���Ϣ ArrayList<HashMap<String,Object>>
	 * @author ����� Create at: 2012-12-4 ����02:25:51
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
	 * Description: �����û��������ѯ�������ݿ��ȡ�û���Ϣ������
	 * 
	 * @param user
	 *            �û���¼����
	 * @param pass
	 *            �û���¼����
	 * @return �����û���Ϣ ArrayList<HashMap<String,Object>>
	 * @author ����� Create at: 2012-12-4 ����02:26:35
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
	 * Description: ���ص�ǰ�û���Ȩ�޼�
	 * 
	 * @return ���ص�ǰ��¼�û���Ȩ�޼��� ArrayList<HashMap<String,Object>>
	 * @author ����� Create at: 2012-12-4 ����02:27:07
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
	 * Description: checkAuthor�������������أ���ѯ����Ȩ�޼�
	 * 
	 * @param all
	 *            �����ַ���
	 * @return ���е�Ȩ�޼� ArrayList<HashMap<String,Object>>
	 * @author ����� Create at: 2012-12-4 ����02:27:45
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
	 * Description: ͨ���û���Ų�ѯ���û���ĳһ״̬����
	 * 
	 * @param userID
	 *            �û����
	 * @param status
	 *            ����״̬
	 * @return ������Ϣ��� ArrayList<HashMap<String,Object>>
	 * @author ��˼Զ Create at: 2012-12-4 ����11:49:42
	 */
	public ArrayList<HashMap<String, Object>> getTaskByUserIDandStatus(
			String userID, String status) {
		ArrayList<HashMap<String, Object>> waitingTaskList = null;
		/****** ע�͵ĵط�����˼Զд�� ******/
		// String sql =
		// "select Guid,RWMC,RWBH,FBSJ,RWZT from T_YDZF_RWXX_USER as a "
		// + "inner join T_YDZF_RWXX as b "
		// + "on a.RWXXBH = b.RWBH where a.UserID = '"
		// + userID
		// + "' and b.RWZT='" + status + "' order by FBSJ desc";
		/************ ������޸�20130411��֪ͨ���ʹ�ִ���б������������һ�£� ****************/
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
