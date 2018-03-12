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
 * Description:������
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����03:10:26
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
	 * Description:��ȡ���ݿ������
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:36:02
	 */
	public abstract String GetKeyField();

	/**
	 * Description:��ȡ���ݿ����
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:36:14
	 */
	public abstract String GetTableName();

	/**
	 * Description:��ȡ�б���ʽ������
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:36:21
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
	 * Description:��ȡ������ʽ������
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public InputStream getStyleDetailedInputStream(Context context)
			throws IOException {
		styleDetailedInputStream = context.getResources().getAssets()
				.open("style_detailed.xml");
		return styleDetailedInputStream;
	}

	/**
	 * Description:��ȡ��ѯ��ʽ������
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:32
	 */
	public InputStream getStyleQueryInputStream(Context context)
			throws IOException {
		styleQueryInputStream = context.getResources().getAssets()
				.open("style_query.xml");
		return styleQueryInputStream;
	}
	/**
	 * Description:��ȡ�༭��ʽ������
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:32
	 */
	public InputStream getStyleEditorInputStream(Context context)
			throws IOException {
		styleQueryInputStream = context.getResources().getAssets()
				.open("style_editor.xml");
		return styleQueryInputStream;
	}
	/**
	 * Description: ��ȡ�ײ��˵������ļ���������
	 * @param context
	 * @return
	 * @throws IOException
	 * InputStream
	 * @author �����
	 * Create at: 2012-12-19 ����04:32:34
	 */
	public InputStream getBottomMenuInputStream(Context context)
	         throws IOException {
		BottomMenuInputStream = context.getResources().getAssets()
		        .open("style_bottomMenu.xml");
        return BottomMenuInputStream;
    }

}
