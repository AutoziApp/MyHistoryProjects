/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.mapuni.meshing.base.interfaces;

import android.content.Context;
import android.content.Intent;


/**
 *׼������ģ����ת������
 */

public interface  IInitData { 
	
	/**
	 * Description: ��ʼ������ģ������ת������
	 * @param context
	 * @param intent
	 * @param obj
	 * @return
	 * @author �����
	 * Create at: 2013-4-19 ����14:32:34
	 */
	public Intent InitData(Context context,Intent intent,String ywl);

}
