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
 * FileName: ZHCXXX.java Description: �ۺ�ҵ���ѯ
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����04:42:35
 */
@SuppressWarnings("serial")
public class ZHCXXX extends BaseClass implements IGrid, Serializable, IInitData {

	/** ��ʵ��������� */
	public static final String BusinessClassName = "ZHCXXX";
	/** �Ź������ʵ���ࡪ����ҵ��ѯ */
	private final String WRYCX = "��ҵ��ѯ";
	/** ��ʾͨ��¼ */
	private final String ADDRESSBOOK = "ͨѶ¼";
	/** ��ʾ�����׼�ļ� */
	//private final String LAWREGULATION = "�����ֲ�";
	private final String LAWREGULATION = "����ͨ";
	/** �Ź������ʵ���ࡪ�����۷Ѽ��� */
	private final String JCKH = "���鿼��";
	/** �Ź������ʵ���ࡪ�����۷Ѽ��� */
	private final String FWSFJSQ = "���۷Ѽ���";
	/** �Ź������ʵ���ࡪ��Σ��Ʒ */
	private final String WHP = "Σ��Ʒ";
	/** �Ź������ʵ���ࡪ��Ԥ�������� */
	private final String YAJAL = "Ԥ��������";
	/** �Ź������ʵ���ࡪ��ר�ҿ� */
	private final String ZJK = "ר�ҿ�";
	/** �Ź������ʵ���ࡪ����Ԯ���� */
	private final String JYWZ = "��Ԯ����";
	/** �Ź������ʵ���ࡪ�����ɷ����׼ */
	private final String FLFGBZ = "���ɷ����׼";
	/** �Ź������ʵ���ࡪ�������շ� */
	private final String ZFJC = "����¼";
	/** �Ź������ʵ���ࡪ��֪ͨ���� */
	private final String TZGG = "��칫��";
	/** �Ź������ʵ���ࡪ�����߼�� */
	private final String ZXJC = "���߼��";
	/** �Ź������ʵ���ࡪ���������֤ */
	private final String PWXKZ = "�������֤";
	/** �Ź������ʵ���ࡪ���ŷ�Ͷ�� */
	private final String XFTS = "�ŷ�Ͷ��";
	/** �Ź������ʵ���ࡪ���������� */
	private final String XZCF = "��������";
	/** �Ź������ʵ���ࡪ��������ͬʱ */
	private final String HBSTSYS = "������ͬʱ";
	/** �Ź������ʵ���ࡪ���������� */
	private final String XQZL = "��������";
	/** �Ź������ʵ���ࡪ����Ҫ�ļ� */
	private final String ZYWJ = "��Ҫ�ļ�";
	/** �Ź������ʵ���ࡪ��������� */
	private final String CLYJ = "�������";
	/** �Ź������ʵ���ࡪ����ҵָ���� */
	private final String ZYZDS = "��ҵָ����";
	/** �Ź������ʵ���ࡪ�������걨 */
	private final String PWSB = "�����걨";
	/** ��ʾ�Ź�������� */
	private final String TITILE = "��Ϣ��ѯ";
	//wsc 2014.11.13
	private final String RYDW = "��Ա��λ";
	//����Ȧ
	private final String DWQ = "����Ȧ";
	
	//
	//��������
	private final String ZNSS = "��������";
	
	//wsc 2014.11.13
	/** �Ź���������ļ���Ŀǰʹ�����ݿ����ã� */
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
			/** Σ��Ʒ */
			if (buttonName.equals(WHP)) {
				WHPXX whpxx = (WHPXX) BaseObjectFactory.createHelperObject(WHPXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", whpxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				whpxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ͨ��¼ */
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
				
				/** �����ֲ� */
				/**
				 * 
				 * ����ͨ
				 * */
			} else if (buttonName.equals(LAWREGULATION)) {
				/** �������ִ���ֲ��ļ�Ŀ¼ */
//				String fjpath = Global.HJJCZFSC_PATH;
//				File files = new File(fjpath);
//				String fjs[] = files.list();
//				if (fjs == null) {
//					Toast.makeText(context, "�����ֲ��ļ���Ϊ�գ�", 0).show();
//					// return;
//				} else {
					intent = new Intent();
					intent.setClassName("com.mapuni.android.MobileEnforcement", "com.mapuni.android.helper.HelpActivity");
//				}	

				/** Ԥ�������� */
			} else if (buttonName.equals(YAJAL)) {
				YAJALXX yajalxx = (YAJALXX) BaseObjectFactory.createHelperObject(YAJALXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", yajalxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				yajalxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ��Ԯ���� */
			} else if (buttonName.equals(JYWZ)) {
				JYWZXX jywzxx = (JYWZXX) BaseObjectFactory.createObject(JYWZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", jywzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				jywzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ר�ҿ� */
			} else if (buttonName.equals(ZJK)) {
				ZJKXX zjkxx = (ZJKXX) BaseObjectFactory.createHelperObject(ZJKXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", zjkxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				zjkxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ���ɷ����׼ */
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

				/** ���߼�� */
			} else if (buttonName.equals(ZXJC)) {
				intent = new Intent(context, ZxjcActivity.class);
				/** �������֤ */
			} else if (buttonName.equals(PWXKZ)) {
				PWXKZXX pwxkzxx = (PWXKZXX) BaseObjectFactory.createObject(PWXKZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", pwxkzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				pwxkzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** �ŷ�Ͷ�� */
			} else if (buttonName.equals(XFTS)) {
				XFTSXX xftsxx = (XFTSXX) BaseObjectFactory.createinfoQueryObject(XFTSXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xftsxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				xftsxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** �������� */
			} else if (buttonName.equals(XZCF)) {
				XZCFXX xzcfxx = (XZCFXX) BaseObjectFactory.createinfoQueryObject(XZCFXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xzcfxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				xzcfxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ������ͬʱ */
			} else if (buttonName.equals(HBSTSYS)) {
				HBSTSYSXX hbstsysxx = (HBSTSYSXX) BaseObjectFactory.createinfoQueryObject(HBSTSYSXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", hbstsysxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				hbstsysxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ��ҵ��ѯ */
			} else if (buttonName.equals(WRYCX)) {
				intent = new Intent(context, CompanySearchActivity.class);

				/** �������� */
			} else if (buttonName.equals(XQZL)) {
				XQZLXX xqzl = (XQZLXX) BaseObjectFactory.createObject(XQZLXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xqzl);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				xqzl.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ��Ҫ�ļ� */
			} else if (buttonName.equals(ZYWJ)) {
				ZYWJXX zywjxx = (ZYWJXX) BaseObjectFactory.createObject(ZYWJXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", zywjxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				zywjxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** ������� */
			} else if (buttonName.equals(CLYJ)) {

				CLYJXX clyjxx = (CLYJXX) BaseObjectFactory.createObject(CLYJXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", clyjxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cym", "");
				clyjxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** �����շ� */
			} else if (buttonName.equals(PWSB)) {
				QYJBXX qyjbxx = (QYJBXX) BaseObjectFactory.createObject(QYJBXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", qyjbxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("cymm", "");
				qyjbxx.setFilter(filterMap);
				qyjbxx.setListTitleText("��ҵ�б�");
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);

				/** �����շѼ����� */
			} else if (buttonName.equals(FWSFJSQ)) {
				intent = new Intent(context, CalculatorPaiwufei.class);
				/** ���鿼�� */
			} else if (buttonName.equals(JCKH)) {
				intent = new Intent(context, JCKHSearchActivity.class);
				/** ֪ͨ���� */
			} else if (buttonName.equals(TZGG)) {
				intent = new Intent(context, NoticeActivity.class);
				/**
				 * ��Ա��λ
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
				//����Ȧ
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
