package com.mapuni.android.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mapuni.android.assessment.JCKHActivity;
import com.mapuni.android.assessment.TeamManageActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.interfaces.IGrid;
import com.mapuni.android.dataprovider.FileHelper;



/**
 * FileName: JCKHXX.java 
 * Description: 稽查考核九宫格业务类
 * @author 王留庚
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午10:56:43
 */
public class JCKHXX extends BaseClass implements IGrid, Serializable {

	/** 该九宫格业务类的名称 */
	public static final String BusinessClassName = "JCKHXX";
	/** 九宫格包涵的业务类--稽查考核 */
	private  final String DWJC = "稽查考核";
	/** 九宫格包涵的业务类--队伍管理 */
	private  final String DWGL = "队伍管理";
	/** 该九宫格要显示的标题名称 */
	private String TITILE = "稽查管理";
	/** 该九宫格模块展示模块的配置文件名称 */
	private final String dataXMLName = "style_grid_jckh.xml";
	/** 文件操作帮助类 */
	private  FileHelper fileHelper = new FileHelper();
	/** 上传文件调用webservice的方法名称 */
	private  final String METHOD_NAME = "ReleaseSingleFileByString";

	@Override
	public String getDataXMLName() {
		return dataXMLName;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		return null;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		return null;
	}

	@Override
	public Intent setIntent(Context context, String buttonName, Handler handler) {
		Bundle bundle = new Bundle();
		Intent intent = null;
		try {
			/** 如果是稽查考核 */
			if (buttonName.equals(DWJC)) {
				intent = new Intent(context, JCKHActivity.class);
				intent.putExtras(bundle);
				/** 如果是队伍管理 */
			} else if (buttonName.equals(DWGL)) {
				DWGLXX dwglxx = (DWGLXX) BaseObjectFactory
						.createObject(DWGLXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", dwglxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				dwglxx.setFilter(filterMap);
				intent = new Intent(context, TeamManageActivity.class);
				intent.putExtras(bundle);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return intent;
	}

	/**
	 * Description: 上传文件
	 * 
	 * @param path
	 *            上传文件的路径
	 * @param bmidTime
	 *            以部门ID加时间的String
	 * @return 上传是否成功 boolean值 Boolean
	 * @author 王留庚 
	 * Create at: 2012-12-3 上午11:02:28
	 */
	public Boolean upLoadFile(String path, String bmidTime) {

		Global global = Global.getGlobalInstance();

		return fileHelper.uploadFileToBase64String(path, Global.NAMESPACE,
				global.getSystemurl() + Global.WEBSERVICE_URL, METHOD_NAME, bmidTime);

	}

	@Override
	public String getGridTitle() {
		return TITILE;
	}

	@Override
	public String GetKeyField() {
		return null;
	}

	@Override
	public String GetTableName() {
		return null;
	}

	@Override
	public String getModuleID() {
		return JCKH_MODULEID;
	}

}
