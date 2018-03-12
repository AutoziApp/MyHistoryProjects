package com.mapuni.android.base.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;

/**
 * FileName: IQuery.java
 * Description:ʵ�����ѯ�ӿ�
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����03:41:44
 */
public interface IQuery {
	
	/**
	 * Description:	��ȡ��ǰ�����ѯ����
	 * @return		����
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:48:55
	 */
	public String getQueryTitleText();
	
	/**
	 * Description:	��ȡ��ѯ����
	 * @return		��ѯ����
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:49:42
	 */
	public HashMap<String, Object> getQuery();
	
	/**
	 * Description:	��ȡ��ѯ��ʽ��Ϣ
	 * @param context
	 * @return		��ʽ��Ϣ
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:50:23
	 */
	public ArrayList<HashMap<String, Object>> getStyleQuery(Context context);
	
	/**
	 * Description:	���ù�������
	 * @param filter	
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:44:42
	 */
	public void setFilter(HashMap<String, Object> filter);
	
	/**
	 * Description:	��������б�����
	 * @param AdapterFileName
	 * @param querycondition
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:51:20
	 */
	public List<HashMap<String, Object>> getSpinner(String AdapterFileName,
			String querycondition);
}

