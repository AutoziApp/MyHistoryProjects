package cn.com.mapuni.meshing.base.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

/**
 * FileName: IDetailed.java
 * Description:业务类详情接口
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午03:38:20
 */
public interface IDetailed {
	
	/**
	 * 获取当前对象详情界面显示标题
	 * @param id 主键值
	 * @return 当前对象主键值
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public String getDetailedTitleText();
	
	/**
	 * 获取当前对象的主键值
	 * @param id 主键值
	 * @return 当前对象主键值
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public String getCurrentID();

	/**
	 * 获取对象的详情
	 * @param id 主键值
	 * @return 哈希表对象
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public HashMap<String, Object> getDetailed(String id);
	
	/**
	 * 获取样式列表信息
	 * @return 样式列表信息
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context);
	
	/**
	 * 获取底部菜单信息
	 * @return 底部菜单信息
	 * @author 王红娟<br>  
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public ArrayList<HashMap<String, Object>> getbottomname(Context context);
	
	/**
	 * 获取对象的底部菜单配置名称
	 * @return 
	 * @author 王红娟<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public String getBottomMenuName();
}

