package com.mapuni.android.base.interfaces;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * FileName: IGrid.java
 * Description:ҵ����Ź���ӿ�
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����03:40:48
 */
public interface IGrid {
	/**���Ź���Ȩ��*/
	String MAIN_MODULEID = "vmob";
	/**�ƶ��칫Ȩ��*/
	String YDBG_MODULEID = "vmob1A";
	/**Ӧ���ֲ�ȫ��*/
	String YJSC_MODULEID = "vmob7A";
	/**��ȾԴ��ѯȨ��*/
	String WRYCH_MODULEID = "vmob10A";
	/**���ɷ�����ϢȨ��*/
	String FLFGXX_MODULEID = "vmob11A";
	/**���鿼��Ȩ��*/
	String JCKH_MODULEID = "vmob12A";
	/**ϵͳά��Ȩ��*/
	String XTWH_MODULEID = "vmob13A";
	/**ִ�����Ȩ��*/
	String  ZFJC_MODULEID = "vmob14A";
	/**�������Ȩ��*/
	String  RWGL_MODULEID =  "vmob2A";
	
	/**
	 * ��ȡ�б���Ϣ��XML����
	 * @return �����б�����
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public String getDataXMLName();

	/**
	 * Description:��ȡ�б���Ϣ
	 * @param fliterHashMap ��������
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:43:35
	 */
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap);
	
	
	/**
	 * ��ȡ��ѯ����
	 * @return ��ѯ����
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public HashMap<String, Object> getFilter();
	
	/**
	 * �ж���ת����
	 * @param buttonName  �������İ�ť������
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public Intent setIntent(Context context,String buttonName,Handler handler);
	
	/**
	 * ��ȡ�Ź������
	 * @return �Ź�������
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public String getGridTitle();
	
	/**
	 * �þŹ����ģ��Ȩ��
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public String getModuleID();
	
}
