package com.mapuni.android.base.interfaces;

import android.view.View;

/**
 * FileName: IViewFlowAsync.java
 * Description:��������Ľӿ�
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2012-12-4 ����03:53:24
 */
public interface IViewFlowAsync {
	/**
	 * �ڿ����߳��е������ں�ʱ����
	 */
	public Object callBackground(Object arg);
	
	/**
	 * Description:	�����̵߳��ã���������UI
	 * @param position
	 * @param result
	 * @return
	 * @author Administrator<br>
	 * Create at: 2012-12-4 ����03:52:03
	 */
	public View callOnUI(int position, Object result);
	
	
}
