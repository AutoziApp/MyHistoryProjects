package com.mapuni.android.base.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

/**
 * FileName: IList.java
 * Description:�б�ҵ����Ľӿ�
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����03:34:52
 */
public interface IList {
	/**
	 * Description:��ȡ��ǰ���������ֵ
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:34:41
	 */
	public String getListTitleText();
	
	/**
	 * Description:���õ�ǰ����ֵ
	 * @param currentIDValue
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:34:31
	 */
	public void setCurrentID(String currentIDValue);
	
	/**
	 * Description:��ȡ��ʽ�б���Ϣ
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:34:02
	 */
	public ArrayList<HashMap<String, Object>> getDataList();

	/**
	 * Description:			��ȡ��ʽ�б���Ϣ
	 * @param fliterHashMap	��������
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:33:56
	 */
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap);
	
	/**
	 * Description:��ȡ��ʽ�б���Ϣ
	 * @param context
	 * @return
	 * @throws IOException
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:33:48
	 */
	public HashMap<String,Object> getStyleList(Context context) throws IOException;

	/**
	 * Description:��ȡ��ѯ����
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:33:40
	 */
	public HashMap<String, Object> getFilter();
	
	/**
	 * Description:listivew�����¼�����
	 * @param listScrollTimes
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:33:30
	 */
	public void setListScrolltimes(int listScrollTimes) ;
	
	/**
	 * Description:listivew�����¼�����
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:33:15
	 */
	public int GetListScrolltimes() ;
	
}
