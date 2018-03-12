package com.mapuni.android.base.interfaces;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * FileName: IGrid.java
 * Description:业务类九宫格接口
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午03:40:48
 */
public interface IGrid {
	/**主九宫格权限*/
	String MAIN_MODULEID = "vmob";
	/**移动办公权限*/
	String YDBG_MODULEID = "vmob1A";
	/**应急手册全新*/
	String YJSC_MODULEID = "vmob7A";
	/**污染源查询权限*/
	String WRYCH_MODULEID = "vmob10A";
	/**法律法规信息权限*/
	String FLFGXX_MODULEID = "vmob11A";
	/**稽查考核权限*/
	String JCKH_MODULEID = "vmob12A";
	/**系统维护权限*/
	String XTWH_MODULEID = "vmob13A";
	/**执法监察权限*/
	String  ZFJC_MODULEID = "vmob14A";
	/**任务管理权限*/
	String  RWGL_MODULEID =  "vmob2A";
	
	/**
	 * 获取列表信息的XML名称
	 * @return 所有列表数据
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public String getDataXMLName();

	/**
	 * Description:获取列表信息
	 * @param fliterHashMap 过滤条件
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:43:35
	 */
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap);
	
	
	/**
	 * 获取查询条件
	 * @return 查询条件
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public HashMap<String, Object> getFilter();
	
	/**
	 * 判断跳转方向
	 * @param buttonName  被单击的按钮的名称
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public Intent setIntent(Context context,String buttonName,Handler handler);
	
	/**
	 * 获取九宫格标题
	 * @return 九宫格名称
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public String getGridTitle();
	
	/**
	 * 该九宫格的模块权限
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public String getModuleID();
	
}
