/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.mapuni.meshing.base.interfaces;

import android.content.Context;
import android.content.Intent;


/**
 *准备功能模块跳转的数据
 */

public interface  IInitData { 
	
	/**
	 * Description: 初始化所需模块点击跳转的数据
	 * @param context
	 * @param intent
	 * @param obj
	 * @return
	 * @author 王红娟
	 * Create at: 2013-4-19 下午14:32:34
	 */
	public Intent InitData(Context context,Intent intent,String ywl);

}
