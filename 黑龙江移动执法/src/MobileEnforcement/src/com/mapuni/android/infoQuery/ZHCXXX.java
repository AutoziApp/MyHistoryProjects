package com.mapuni.android.infoQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.GridActivity;
import com.mapuni.android.base.ListActivity;
import com.mapuni.android.base.QueryListActivity;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.interfaces.IGrid;
import com.mapuni.android.base.interfaces.IInitData;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.business.CLYJXX;
import com.mapuni.android.business.FLFGBZXX;
import com.mapuni.android.business.PWXKZXX;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.XQZLXX;
import com.mapuni.android.helper.JYWZXX;
import com.mapuni.android.helper.WHPXX;
import com.mapuni.android.helper.YAJALXX;
import com.mapuni.android.helper.ZJKXX;
import com.mapuni.android.helper.ZYWJXX;
import com.mapuni.android.notice.NoticeActivity;
import com.mapuni.android.onlinemonitor.ZxjcActivity;
import com.mapuni.android.teamcircle.TeamCircleActivity;
import com.mapuni.android.uitl.CalculatorPaiwufei;
import com.mapuni.android.witsearch.WitSrarchActivity;

/**
 * FileName: ZHCXXX.java Description: 综合业务查询
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 下午04:42:35
 */
@SuppressWarnings("serial")
public class ZHCXXX extends BaseClass implements IGrid, Serializable, IInitData {

	/** 该实体类的名称 */
	public static final String BusinessClassName = "ZHCXXX";
	/** 九宫格包涵实体类——企业查询 */
	private final String WRYCX = "企业查询";
	/** 显示通信录 */
	private final String ADDRESSBOOK = "通讯录";
	/** 显示法规标准文件 */
	//private final String LAWREGULATION = "环保手册";
	private final String LAWREGULATION = "百事通";
	/** 九宫格包涵实体类——排污费计算 */
	private final String JCKH = "稽查考核";
	/** 九宫格包涵实体类——排污费计算 */
	private final String FWSFJSQ = "排污费计算";
	/** 九宫格包涵实体类——危化品 */
	private final String WHP = "危化品";
	/** 九宫格包涵实体类——预案及案例 */
	private final String YAJAL = "预案及案例";
	/** 九宫格包涵实体类——专家库 */
	private final String ZJK = "专家库";
	/** 九宫格包涵实体类——救援物资 */
	private final String JYWZ = "救援物资";
	/** 九宫格包涵实体类——法律法规标准 */
	private final String FLFGBZ = "法律法规标准";
	/** 九宫格包涵实体类——排污收费 */
	private final String ZFJC = "监察记录";
	/** 九宫格包涵实体类——通知公告 */
	private final String TZGG = "监察公告";
	/** 九宫格包涵实体类——在线监测 */
	private final String ZXJC = "在线监测";
	/** 九宫格包涵实体类——排污许可证 */
	private final String PWXKZ = "排污许可证";
	/** 九宫格包涵实体类——信访投诉 */
	private final String XFTS = "信访投诉";
	/** 九宫格包涵实体类——行政处罚 */
	private final String XZCF = "行政处罚";
	/** 九宫格包涵实体类——环评三同时 */
	private final String HBSTSYS = "环评三同时";
	/** 九宫格包涵实体类——限期治理 */
	private final String XQZL = "限期治理";
	/** 九宫格包涵实体类——重要文件 */
	private final String ZYWJ = "重要文件";
	/** 九宫格包涵实体类——处理意见 */
	private final String CLYJ = "处理意见";
	/** 九宫格包涵实体类——作业指导书 */
	private final String ZYZDS = "作业指导书";
	/** 九宫格包涵实体类——排污申报 */
	private final String PWSB = "排污申报";
	/** 显示九宫格的名字 */
	private final String TITILE = "信息查询";
	//wsc 2014.11.13
	private final String RYDW = "人员定位";
	//队伍圈
	private final String DWQ = "队伍圈";
	
	//
	//智能搜索
	private final String ZNSS = "智能搜索";
	
	//wsc 2014.11.13
	/** 九宫格的配置文件（目前使用数据库配置） */
	private final String dataXMLName = "style_grid_zhcx.xml";

	@Override
	public String getDataXMLName() {

		return dataXMLName;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {

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
			/** 危化品 */
			if (buttonName.equals(WHP)) {
				WHPXX whpxx = (WHPXX) BaseObjectFactory.createHelperObject(WHPXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", whpxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				whpxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 通信录 */
			} else if (buttonName.equals(ADDRESSBOOK)) {
				BaseUsers users = new BaseUsers(context);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cmy", "");
				filterMap.put("syncDataRegionCode", Global.getGlobalInstance().getAreaCode());
				users.setFilter(filterMap);
				bundle.putBoolean("IsShowTitle", true);
				bundle.putSerializable("BusinessObj", users);
				bundle.putString("isTongxunlu", "1");
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				
				/** 环保手册 */
				/**
				 * 
				 * 百事通
				 * */
			} else if (buttonName.equals(LAWREGULATION)) {
				/** 环境监察执法手册文件目录 */
//				String fjpath = Global.HJJCZFSC_PATH;
//				File files = new File(fjpath);
//				String fjs[] = files.list();
//				if (fjs == null) {
//					Toast.makeText(context, "环保手册文件夹为空！", 0).show();
//					// return;
//				} else {
					intent = new Intent();
					intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.helper.HelpActivity");
//				}	

				/** 预案及案例 */
			} else if (buttonName.equals(YAJAL)) {
				YAJALXX yajalxx = (YAJALXX) BaseObjectFactory.createHelperObject(YAJALXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", yajalxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				yajalxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 救援物资 */
			} else if (buttonName.equals(JYWZ)) {
				JYWZXX jywzxx = (JYWZXX) BaseObjectFactory.createObject(JYWZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", jywzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				jywzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 专家库 */
			} else if (buttonName.equals(ZJK)) {
				ZJKXX zjkxx = (ZJKXX) BaseObjectFactory.createHelperObject(ZJKXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", zjkxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				zjkxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 法律法规标准 */
			} else if (buttonName.equals(FLFGBZ)) {
				FLFGBZXX flfgbzxx = (FLFGBZXX) BaseObjectFactory.createObject(FLFGBZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", flfgbzxx);
				bundle.putBoolean("IsShowTitle", true);
				intent = new Intent(context, QueryListActivity.class);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				flfgbzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 在线监测 */
			} else if (buttonName.equals(ZXJC)) {
				intent = new Intent(context, ZxjcActivity.class);
				/** 排污许可证 */
			} else if (buttonName.equals(PWXKZ)) {
				PWXKZXX pwxkzxx = (PWXKZXX) BaseObjectFactory.createObject(PWXKZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", pwxkzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				pwxkzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 信访投诉 */
			} else if (buttonName.equals(XFTS)) {
				XFTSXX xftsxx = (XFTSXX) BaseObjectFactory.createinfoQueryObject(XFTSXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xftsxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				xftsxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 行政处罚 */
			} else if (buttonName.equals(XZCF)) {
				XZCFXX xzcfxx = (XZCFXX) BaseObjectFactory.createinfoQueryObject(XZCFXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xzcfxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				xzcfxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 环评三同时 */
			} else if (buttonName.equals(HBSTSYS)) {
				HBSTSYSXX hbstsysxx = (HBSTSYSXX) BaseObjectFactory.createinfoQueryObject(HBSTSYSXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", hbstsysxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				hbstsysxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 企业查询 */
			} else if (buttonName.equals(WRYCX)) {
				intent = new Intent(context, CompanySearchActivity.class);

				/** 限期治理 */
			} else if (buttonName.equals(XQZL)) {
				XQZLXX xqzl = (XQZLXX) BaseObjectFactory.createObject(XQZLXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xqzl);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				xqzl.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 重要文件 */
			} else if (buttonName.equals(ZYWJ)) {
				ZYWJXX zywjxx = (ZYWJXX) BaseObjectFactory.createObject(ZYWJXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", zywjxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				zywjxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 处理意见 */
			} else if (buttonName.equals(CLYJ)) {

				CLYJXX clyjxx = (CLYJXX) BaseObjectFactory.createObject(CLYJXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", clyjxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				clyjxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 排污收费 */
			} else if (buttonName.equals(PWSB)) {
				QYJBXX qyjbxx = (QYJBXX) BaseObjectFactory.createObject(QYJBXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", qyjbxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cymm", "");
				qyjbxx.setFilter(filterMap);
				qyjbxx.setListTitleText("企业列表");
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** 排污收费计算器 */
			} else if (buttonName.equals(FWSFJSQ)) {
				intent = new Intent(context, CalculatorPaiwufei.class);
				/** 稽查考核 */
			} else if (buttonName.equals(JCKH)) {
				intent = new Intent(context, JCKHSearchActivity.class);
				/** 通知公告 */
			} else if (buttonName.equals(TZGG)) {
				intent = new Intent(context, NoticeActivity.class);
				/**
				 * 人员定位
				 * */
			}else if (buttonName.equals(RYDW)) {
				RYDW rydw = new RYDW(context);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("syncDataRegionCode", Global.getGlobalInstance()
						.getAreaCode());
				rydw.setFilter(filterMap);
				bundle.putBoolean("IsShowTitle", true);
				bundle.putSerializable("BusinessObj", rydw);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				//队伍圈
			}else if (buttonName.equals(DWQ)) {
				intent=new Intent(context,TeamCircleActivity.class);
					
					
			}else if(buttonName.equals(ZNSS)){
				intent=new Intent(context,WitSrarchActivity.class);
			}

		} catch (ClassNotFoundException e) {
			ExceptionManager.WriteCaughtEXP(e, "ZHCXXX");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			ExceptionManager.WriteCaughtEXP(e, "ZHCXXX");
			e.printStackTrace();
		} catch (InstantiationException e) {
			ExceptionManager.WriteCaughtEXP(e, "ZHCXXX");
			e.printStackTrace();
		}
		return intent;

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
	public String getGridTitle() {

		return TITILE;
	}

	@Override
	public String getModuleID() {
		return WRYCH_MODULEID;
	}

	@Override
	public Intent InitData(Context context, Intent intent, String ywl) {

		Intent zhcxintent = intent;

		zhcxintent = new Intent(context, GridActivity.class);
		intent.putExtra("isShortcut", true);
		intent.putExtra("ywl", "ZHCXXX");
		intent.putExtra("packageName", "infoQuery");

		zhcxintent.setAction("ZHCX");
		Bundle nextbundle = new Bundle();
		ZHCXXX zhcxxx = null;
		try {
			zhcxxx = (ZHCXXX) BaseObjectFactory.createinfoQueryObject(ZHCXXX.BusinessClassName);
			nextbundle.putSerializable("BusinessObj", zhcxxx);
			nextbundle.putBoolean("IsShowTitle", true);
			nextbundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_LEFTICON_VISIBLE, true);
			zhcxintent.putExtras(nextbundle);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return zhcxintent;
	}
}
