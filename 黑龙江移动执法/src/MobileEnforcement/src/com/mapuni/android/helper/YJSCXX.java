package com.mapuni.android.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mapuni.android.base.QueryListActivity;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.interfaces.IGrid;
/**
 * FileName: YJSCXX.java
 * Description: 应急手册（九宫格）
 * @author 包坤
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 下午04:08:18
 */
public class YJSCXX extends BaseClass implements IGrid, Serializable {
	
	/** 实体类的名字 */
	public static final String BusinessClassName = "YJSCXX";
	/** 九宫格包涵实体类——危化品 */
	private  final String WHP = "危化品";
	/** 九宫格包涵实体类——专家库 */
	private  final String ZJK = "专家库";
	/** 九宫格包涵实体类——救援物资 */
	private  final String JYWZ = "救援物资";

	/* public static final String YAJAL = "预案及案例"; */

	/** 九宫格包涵实体类——预案 */
	public  final String YA = "预案";
	/** 九宫格包涵实体类——预案 */
	public  final String AL = "案例";
	/** 显示九宫格的名字 */
	private String TITILE = "应急手册";
	/** 九宫格的配置文件（目前使用数据库配置） */
	private final String dataXMLName = "style_grid_yjsc.xml";

	@Override
	public String getDataXMLName() {
		// TODO Auto-generated method stub
		return dataXMLName;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(
			HashMap<String, Object> fliterHashMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent setIntent(Context context, String buttonName, Handler handler) {
		Bundle bundle = new Bundle();
		Intent intent = null;
		try {
			/**危化品*/
			if (buttonName.equals(WHP)) {
				WHPXX whpxx;
				whpxx = (WHPXX) BaseObjectFactory
						.createHelperObject(WHPXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", whpxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				whpxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				
				/**专家库*/
			} else if (buttonName.equals(ZJK)) {
				ZJKXX zjkxx = (ZJKXX) BaseObjectFactory
						.createHelperObject(ZJKXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", zjkxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				zjkxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				
				/**救援物资*/
			} else if (buttonName.equals(JYWZ)) {
				JYWZXX jywzxx = (JYWZXX) BaseObjectFactory
						.createHelperObject(JYWZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", jywzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				jywzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
			}
			// 预案及案例
			/*
			 * else if (buttonName.equals(YAJAL)) { YAJALXX yajalxx = (YAJALXX)
			 * ObjectFactory .createObject(YAJALXX.BusinessClassName);
			 * bundle.putSerializable("BusinessObj", yajalxx);
			 * bundle.putBoolean("IsShowTitle", true); HashMap<String, Object>
			 * filterMap = new HashMap<String, Object>(); filterMap.put("cym",
			 * ""); yajalxx.setFilter(filterMap); intent = new Intent(context,
			 * QueryListActivity.class); intent.putExtras(bundle); }
			 */
			
			/**预案*/
			else if (buttonName.equals(YA)) {
				YAJALXX yajalxx = (YAJALXX) BaseObjectFactory
						.createHelperObject(YAJALXX.BusinessClassName);
				yajalxx.setFileType("预案");
				bundle.putSerializable("BusinessObj", yajalxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "预案");
				yajalxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				
				/**案例*/
			} else if (buttonName.equals(AL)) {
				YAJALXX yajalxx = (YAJALXX) BaseObjectFactory
						.createHelperObject(YAJALXX.BusinessClassName);
				yajalxx.setFileType("案例");
				bundle.putSerializable("BusinessObj", yajalxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "案例");
				yajalxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
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

	@Override
	public String getGridTitle() {
		// TODO Auto-generated method stub
		return TITILE;
	}

	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleID() {
		// TODO Auto-generated method stub
		return YJSC_MODULEID;
	}

}
