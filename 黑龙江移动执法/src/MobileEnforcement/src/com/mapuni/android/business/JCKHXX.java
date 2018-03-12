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
 * Description: ���鿼�˾Ź���ҵ����
 * @author ������
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-3 ����10:56:43
 */
public class JCKHXX extends BaseClass implements IGrid, Serializable {

	/** �þŹ���ҵ��������� */
	public static final String BusinessClassName = "JCKHXX";
	/** �Ź��������ҵ����--���鿼�� */
	private  final String DWJC = "���鿼��";
	/** �Ź��������ҵ����--������� */
	private  final String DWGL = "�������";
	/** �þŹ���Ҫ��ʾ�ı������� */
	private String TITILE = "�������";
	/** �þŹ���ģ��չʾģ��������ļ����� */
	private final String dataXMLName = "style_grid_jckh.xml";
	/** �ļ����������� */
	private  FileHelper fileHelper = new FileHelper();
	/** �ϴ��ļ�����webservice�ķ������� */
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
			/** ����ǻ��鿼�� */
			if (buttonName.equals(DWJC)) {
				intent = new Intent(context, JCKHActivity.class);
				intent.putExtras(bundle);
				/** ����Ƕ������ */
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
	 * Description: �ϴ��ļ�
	 * 
	 * @param path
	 *            �ϴ��ļ���·��
	 * @param bmidTime
	 *            �Բ���ID��ʱ���String
	 * @return �ϴ��Ƿ�ɹ� booleanֵ Boolean
	 * @author ������ 
	 * Create at: 2012-12-3 ����11:02:28
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
