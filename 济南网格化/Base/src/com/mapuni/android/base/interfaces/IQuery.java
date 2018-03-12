package com.mapuni.android.base.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;

/**
 * FileName: IQuery.java
 * Description:实体类查询接口
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午03:41:44
 */
public interface IQuery {
	
	/**
	 * Description:	获取当前对象查询标题
	 * @return		标题
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:48:55
	 */
	public String getQueryTitleText();
	
	/**
	 * Description:	获取查询数据
	 * @return		查询集合
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:49:42
	 */
	public HashMap<String, Object> getQuery();
	
	/**
	 * Description:	获取查询样式信息
	 * @param context
	 * @return		样式信息
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:50:23
	 */
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context);
	
	/**
	 * Description:	设置过滤条件
	 * @param filter	
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:44:42
	 */
	public void setFilter(HashMap<String, Object> filter);
	
	/**
	 * Description:	获得下拉列表数据
	 * @param AdapterFileName
	 * @param querycondition
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:51:20
	 */
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition);
}

