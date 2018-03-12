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
 * FileName: FLFGXX.java
 * Description: 法律法规九宫格业务类
 * @author 包坤
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-3 上午08:46:11
 */
public class FLFGXX extends BaseClass implements IGrid, Serializable {
	
	/**业务类的名字*/
	public  final String BusinessClassName = "FLFGXX";
	/**显示九宫格的名字*/
	private String TITILE = "法规标准";
	/**九宫格包涵业务类——环保法律*/
	private  final String HBFL = "环保法律";
	/**九宫格包涵业务类——环保标准*/
	private  final String HBBZ = "环保标准";
	/**九宫格包涵业务类——行政法规*/
	private  final String XZFG = "行政法规";
	/**九宫格包涵业务类——部委规章*/
	private  final String BWGZ = "部委规章";
	/**九宫格包涵业务类——环境质量标准*/
	private  final String HJZLBZ = "环境质量标准";
	/**九宫格包涵业务类——排放标准*/
	private  final String PFBZ = "排放标准";
	/**九宫格包涵业务类——监察指南*/
	private  final String JCZN = "监察指南";
	/**九宫格包涵业务类——相关法律法规*/
	private  final String XGFLFG = "相关法律法规";
	/**九宫格包涵业务类——重要文件*/
	public  final String ZYWJ = "重要文件";
	
	/**九宫格包涵业务类——法律法规（石家庄特有）*/
	private  final String FLFG = "法律法规";
	/**九宫格包涵业务类——地方法规*/
	private  final String DFFG = "地方法规";
	/**九宫格的配置文件（目前使用数据库配置）*/
	private final String dataXMLName = "style_grid_flfg.xml";
	
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

	/**
	 * 九宫格业务类初始化
	 */
	@Override
	public Intent setIntent(Context context, String buttonName, Handler handler) {
		Bundle bundle = new Bundle();
		Intent intent = null;
		try {
			    /**初始化环保法律*/
			if (buttonName.equals(HBFL)) {
				HBFLXX hbflxx = (HBFLXX) BaseObjectFactory
						.createHelperObject(HBFLXX.BusinessClassName);
				hbflxx.setFileType("环保法律");
				bundle.putSerializable("BusinessObj", hbflxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "环保法律");
				hbflxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化行政法规*/
			} else if (buttonName.equals(XZFG)) {
				XZFGXX zxfgxx = (XZFGXX) BaseObjectFactory
						.createHelperObject(XZFGXX.BusinessClassName);
				zxfgxx.setFileType("行政法规");
				bundle.putSerializable("BusinessObj", zxfgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "行政法规");
				zxfgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
			}  /**初始化法律法规*/
			if (buttonName.equals(FLFG)) {
				HBFLXX hbflxx = (HBFLXX) BaseObjectFactory
						.createHelperObject(HBFLXX.BusinessClassName);
				hbflxx.setFileType("法律法规");
				bundle.putSerializable("BusinessObj", hbflxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "law");
				hbflxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化环保标准*/
			} else if (buttonName.equals(HBBZ)) {
				XZFGXX zxfgxx = (XZFGXX) BaseObjectFactory
						.createHelperObject(XZFGXX.BusinessClassName);
				zxfgxx.setFileType("环保标准");
				bundle.putSerializable("BusinessObj", zxfgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "envpro");
				zxfgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化部委规章*/
			} else if (buttonName.equals(BWGZ)) {
				BWGZXX bwgzxx = (BWGZXX) BaseObjectFactory
						.createHelperObject(BWGZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", bwgzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "部委规章");
				bwgzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化地方法规*/
			} else if (buttonName.equals(DFFG)) {
				DFFGXX dffgxx = (DFFGXX) BaseObjectFactory
						.createHelperObject(DFFGXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", dffgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "地方性法规");
				dffgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化环境质量标准*/
			} else if (buttonName.equals(HJZLBZ)) {
				HJZLBZXX hjzlbzxx = (HJZLBZXX) BaseObjectFactory
						.createHelperObject(HJZLBZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", hjzlbzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "环境质量标准");
				hjzlbzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化排放标准*/
			} else if (buttonName.equals(PFBZ)) {
				PFBZXX pfbzxx = (PFBZXX) BaseObjectFactory
						.createHelperObject(PFBZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", pfbzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "污染物排放标准");
				pfbzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化监察指南*/
			} else if (buttonName.equals(JCZN)) {
				JCZNXX jcznxx = (JCZNXX) BaseObjectFactory
						.createHelperObject(JCZNXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", jcznxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "监察指南");
				jcznxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化相关法律法规*/
			} else if (buttonName.equals(XGFLFG)) {
				XGFLFGXX xgflfgxx = (XGFLFGXX) BaseObjectFactory
						.createHelperObject(XGFLFGXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xgflfgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "相关法律、法规");
				xgflfgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**初始化重要文件*/
			} else if (buttonName.equals(ZYWJ)) {
				ZYWJXX zywjxx = (ZYWJXX) BaseObjectFactory
						.createHelperObject(ZYWJXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", zywjxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				zywjxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
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
		return FLFGXX_MODULEID;
	}

}
