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
 * Description: ���ɷ���Ź���ҵ����
 * @author ����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����08:46:11
 */
public class FLFGXX extends BaseClass implements IGrid, Serializable {
	
	/**ҵ���������*/
	public  final String BusinessClassName = "FLFGXX";
	/**��ʾ�Ź��������*/
	private String TITILE = "�����׼";
	/**�Ź������ҵ���ࡪ����������*/
	private  final String HBFL = "��������";
	/**�Ź������ҵ���ࡪ��������׼*/
	private  final String HBBZ = "������׼";
	/**�Ź������ҵ���ࡪ����������*/
	private  final String XZFG = "��������";
	/**�Ź������ҵ���ࡪ����ί����*/
	private  final String BWGZ = "��ί����";
	/**�Ź������ҵ���ࡪ������������׼*/
	private  final String HJZLBZ = "����������׼";
	/**�Ź������ҵ���ࡪ���ŷű�׼*/
	private  final String PFBZ = "�ŷű�׼";
	/**�Ź������ҵ���ࡪ�����ָ��*/
	private  final String JCZN = "���ָ��";
	/**�Ź������ҵ���ࡪ����ط��ɷ���*/
	private  final String XGFLFG = "��ط��ɷ���";
	/**�Ź������ҵ���ࡪ����Ҫ�ļ�*/
	public  final String ZYWJ = "��Ҫ�ļ�";
	
	/**�Ź������ҵ���ࡪ�����ɷ��棨ʯ��ׯ���У�*/
	private  final String FLFG = "���ɷ���";
	/**�Ź������ҵ���ࡪ���ط�����*/
	private  final String DFFG = "�ط�����";
	/**�Ź���������ļ���Ŀǰʹ�����ݿ����ã�*/
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
	 * �Ź���ҵ�����ʼ��
	 */
	@Override
	public Intent setIntent(Context context, String buttonName, Handler handler) {
		Bundle bundle = new Bundle();
		Intent intent = null;
		try {
			    /**��ʼ����������*/
			if (buttonName.equals(HBFL)) {
				HBFLXX hbflxx = (HBFLXX) BaseObjectFactory
						.createHelperObject(HBFLXX.BusinessClassName);
				hbflxx.setFileType("��������");
				bundle.putSerializable("BusinessObj", hbflxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "��������");
				hbflxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ����������*/
			} else if (buttonName.equals(XZFG)) {
				XZFGXX zxfgxx = (XZFGXX) BaseObjectFactory
						.createHelperObject(XZFGXX.BusinessClassName);
				zxfgxx.setFileType("��������");
				bundle.putSerializable("BusinessObj", zxfgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "��������");
				zxfgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
			}  /**��ʼ�����ɷ���*/
			if (buttonName.equals(FLFG)) {
				HBFLXX hbflxx = (HBFLXX) BaseObjectFactory
						.createHelperObject(HBFLXX.BusinessClassName);
				hbflxx.setFileType("���ɷ���");
				bundle.putSerializable("BusinessObj", hbflxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "law");
				hbflxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ��������׼*/
			} else if (buttonName.equals(HBBZ)) {
				XZFGXX zxfgxx = (XZFGXX) BaseObjectFactory
						.createHelperObject(XZFGXX.BusinessClassName);
				zxfgxx.setFileType("������׼");
				bundle.putSerializable("BusinessObj", zxfgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("type", "envpro");
				zxfgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ����ί����*/
			} else if (buttonName.equals(BWGZ)) {
				BWGZXX bwgzxx = (BWGZXX) BaseObjectFactory
						.createHelperObject(BWGZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", bwgzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "��ί����");
				bwgzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ���ط�����*/
			} else if (buttonName.equals(DFFG)) {
				DFFGXX dffgxx = (DFFGXX) BaseObjectFactory
						.createHelperObject(DFFGXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", dffgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "�ط��Է���");
				dffgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ������������׼*/
			} else if (buttonName.equals(HJZLBZ)) {
				HJZLBZXX hjzlbzxx = (HJZLBZXX) BaseObjectFactory
						.createHelperObject(HJZLBZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", hjzlbzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "����������׼");
				hjzlbzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ���ŷű�׼*/
			} else if (buttonName.equals(PFBZ)) {
				PFBZXX pfbzxx = (PFBZXX) BaseObjectFactory
						.createHelperObject(PFBZXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", pfbzxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "��Ⱦ���ŷű�׼");
				pfbzxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ�����ָ��*/
			} else if (buttonName.equals(JCZN)) {
				JCZNXX jcznxx = (JCZNXX) BaseObjectFactory
						.createHelperObject(JCZNXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", jcznxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "���ָ��");
				jcznxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ����ط��ɷ���*/
			} else if (buttonName.equals(XGFLFG)) {
				XGFLFGXX xgflfgxx = (XGFLFGXX) BaseObjectFactory
						.createHelperObject(XGFLFGXX.BusinessClassName);
				bundle.putSerializable("BusinessObj", xgflfgxx);
				bundle.putBoolean("IsShowTitle", true);
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("sjlb", "��ط��ɡ�����");
				xgflfgxx.setFilter(filterMap);
				intent = new Intent(context, QueryListActivity.class);
				intent.putExtras(bundle);
				/**��ʼ����Ҫ�ļ�*/
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
