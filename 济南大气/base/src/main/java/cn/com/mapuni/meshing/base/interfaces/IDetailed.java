package cn.com.mapuni.meshing.base.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

/**
 * FileName: IDetailed.java
 * Description:ҵ��������ӿ�
 * @author ����Ԫ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����03:38:20
 */
public interface IDetailed {
	
	/**
	 * ��ȡ��ǰ�������������ʾ����
	 * @param id ����ֵ
	 * @return ��ǰ��������ֵ
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public String getDetailedTitleText();
	
	/**
	 * ��ȡ��ǰ���������ֵ
	 * @param id ����ֵ
	 * @return ��ǰ��������ֵ
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public String getCurrentID();

	/**
	 * ��ȡ���������
	 * @param id ����ֵ
	 * @return ��ϣ�����
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public HashMap<String, Object> getDetailed(String id);
	
	/**
	 * ��ȡ��ʽ�б���Ϣ
	 * @return ��ʽ�б���Ϣ
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public ArrayList<HashMap<String, Object>> getStyleDetailed(Context context);
	
	/**
	 * ��ȡ�ײ��˵���Ϣ
	 * @return �ײ��˵���Ϣ
	 * @author �����<br>  
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public ArrayList<HashMap<String, Object>> getbottomname(Context context);
	
	/**
	 * ��ȡ����ĵײ��˵���������
	 * @return 
	 * @author �����<br>
	 * Create at: 2012-12-4 ����03:37:02
	 */
	public String getBottomMenuName();
}

