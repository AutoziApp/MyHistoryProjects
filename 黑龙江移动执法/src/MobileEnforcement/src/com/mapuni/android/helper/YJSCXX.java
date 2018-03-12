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
 * Description: Ӧ���ֲᣨ�Ź���
 * @author ����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-4 ����04:08:18
 */
public class YJSCXX extends BaseClass implements IGrid, Serializable {
	
	/** ʵ��������� */
	public static final String BusinessClassName = "YJSCXX";
	/** �Ź������ʵ���ࡪ��Σ��Ʒ */
	private  final String WHP = "Σ��Ʒ";
	/** �Ź������ʵ���ࡪ��ר�ҿ� */
	private  final String ZJK = "ר�ҿ�";
	/** �Ź������ʵ���ࡪ����Ԯ���� */
	private  final String JYWZ = "��Ԯ����";

	/* public static final String YAJAL = "Ԥ��������"; */

	/** �Ź������ʵ���ࡪ��Ԥ�� */
	public  final String YA = "Ԥ��";
	/** �Ź������ʵ���ࡪ��Ԥ�� */
	public  final String AL = "����";
	/** ��ʾ�Ź�������� */
	private String TITILE = "Ӧ���ֲ�";
	/** �Ź���������ļ���Ŀǰʹ�����ݿ����ã� */
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
			/**Σ��Ʒ*/
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
				
				/**ר�ҿ�*/
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
				
				/**��Ԯ����*/
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
			// Ԥ��������
			/*
			 * else if (buttonName.equals(YAJAL)) { YAJALXX yajalxx = (YAJALXX)
			 * ObjectFactory .createObject(YAJALXX.BusinessClassName);
			 * bundle.putSerializable("BusinessObj", yajalxx);
			 * bundle.putBoolean("IsShowTitle", true); HashMap<String, Object>
			 * filterMap = new HashMap<String, Object>(); filterMap.put("cym",
			 * ""); yajalxx.setFilter(filterMap); intent = new Intent(context,
			 * QueryListActivity.class); intent.putExtras(bundle); }
			 */
			
			/**Ԥ��*/
			else if (buttonName.equals(YA)) {
				YAJALXX yajalxx = (YAJALXX) BaseObjectFactory
						.createHelperObject(YAJALXX.BusinessClassName);
				yajalxx.setFileType("Ԥ��");
				bundle.putSerializable("BusinessObj", yajalxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "Ԥ��");
				yajalxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				
				/**����*/
			} else if (buttonName.equals(AL)) {
				YAJALXX yajalxx = (YAJALXX) BaseObjectFactory
						.createHelperObject(YAJALXX.BusinessClassName);
				yajalxx.setFileType("����");
				bundle.putSerializable("BusinessObj", yajalxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "����");
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
