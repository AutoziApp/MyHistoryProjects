package com.mapuni.android.base.business;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.dataprovider.XmlHelper;


/**
 * FileName: BaseClass.java 
 * Description:基础类
 * @author 张信元
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2012-12-4 下午03:10:26
 */
public abstract class BaseClass {
	public final static String DATA_PATH = PathManager.SDCARD_DATA_LOCAL_PATH;

	public static SqliteUtil DBHelper = null;
	public static XmlHelper xmlHelper = null;

	private InputStream styleListInputStream = null;
	private InputStream styleDetailedInputStream = null;
	private InputStream styleQueryInputStream = null;
	private InputStream BottomMenuInputStream = null;


	public BaseClass() {
		DBHelper = SqliteUtil.getInstance();
		xmlHelper = new XmlHelper();
	}

	/**
	 * Description:获取数据库表主键
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:36:02
	 */
	public abstract String GetKeyField();

	/**
	 * Description:获取数据库表名
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:36:14
	 */
	public abstract String GetTableName();

	/**
	 * Description:获取列表样式输入流
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:36:21
	 */
	public InputStream getStyleListInputStream(Context context)
			throws IOException {
		try {
			styleListInputStream = context.getResources().getAssets()
					.open("style_list.xml");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return styleListInputStream;
	}

	/**
	 * Description:获取详情样式输入流
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:02
	 */
	public InputStream getStyleDetailedInputStream(Context context)
			throws IOException {
		styleDetailedInputStream = context.getResources().getAssets()
				.open("style_detailed.xml");
		return styleDetailedInputStream;
	}

	/**
	 * Description:获取查询样式输入流
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:32
	 */
	public InputStream getStyleQueryInputStream(Context context)
			throws IOException {
		styleQueryInputStream = context.getResources().getAssets()
				.open("style_query.xml");
		return styleQueryInputStream;
	}
	/**
	 * Description:获取编辑样式输入流
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 下午03:37:32
	 */
	public InputStream getStyleEditorInputStream(Context context)
			throws IOException {
		styleQueryInputStream = context.getResources().getAssets()
				.open("style_editor.xml");
		return styleQueryInputStream;
	}
	/**
	 * Description: 获取底部菜单配置文件的输入流
	 * @param context
	 * @return
	 * @throws IOException
	 * InputStream
	 * @author 王红娟
	 * Create at: 2012-12-19 下午04:32:34
	 */
	public InputStream getBottomMenuInputStream(Context context)
	         throws IOException {
		BottomMenuInputStream = context.getResources().getAssets()
		        .open("style_bottomMenu.xml");
        return BottomMenuInputStream;
    }

}
