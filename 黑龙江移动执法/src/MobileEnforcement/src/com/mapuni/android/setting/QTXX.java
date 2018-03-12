package com.mapuni.android.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.WebViewer;
import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IGrid;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.netprovider.Net;

/**
 * FileName: QTXX.java Description: ������Ϣ���Ź���
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-4 ����10:26:30
 */
public class QTXX extends BaseClass implements IGrid, Serializable {

	/**
	 * FileName: QTXX.java Description:
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2013-3-7 ����10:00:28
	 */
	private final long serialVersionUID = -4415592009782674981L;
	/** ��ʵ��������� */
	public static final String BusinessClassName = "QTXX";
	/** �Ź������ʵ���ࡪ������ϵͳ */
	public final String GYXT = "����ϵͳ";
	/** �Ź������ʵ���ࡪ�������� */
	public final String JCGX = "������";
	/** �Ź������ʵ���ࡪ��ϵͳ���� */
	public final String XTSZ = "ϵͳ����";
	/** �Ź������ʵ���ࡪ�������װ */
	public final String APKINS = "�����װ";
	/** ��ʾ�Ź�������� */
	private String TITILE = "ϵͳ����";
	/** �Ź���������ļ���Ŀǰʹ�����ݿ����ã� */
	private final String dataXMLName = "style_grid_qt.xml";

	@Override
	public String getDataXMLName() {
		return dataXMLName;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getDataList(HashMap<String, Object> fliterHashMap) {
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
			if (buttonName.equals(GYXT)) {
				String bbh = DisplayUitl.getVersionName(context);
				String otherstr = "<table width='100%' border='0' cellspacing='0' cellpadding='0'>" + "<tr>" + "<td height='100%' valign='top' align='center'>"
						+ "<table width='100%' border='0' cellspacing='0' cellpadding='0'>" +

						" <tr>" + " <td background='images/title01.gif' style='width: 100%; height: 37px; text-align: center; color: #1f6baf; font-weight: bold;'> ��������ƶ�ִ��ϵͳ</td>"
						+ "</tr>" + "<tr>" + "<td style='width: 100%; height: 37px; text-align: left; color: #1f6baf; font-weight: bold;'>"
						+ " <table border='0' cellspacing='0' cellpadding='0' align='center'>" + " <tr>"
						+ "  <td align='center' style=' width: 100%; height: 37px; color: #1f6baf; font-weight: bold;'>" + "    �汾�ţ�"
						+ bbh
						+ ""
						+ " </td>"
						+ " </tr>"
						+ " <tr>"
						+ "  <td style='width: 100%; height: 37px; color: #1f6baf; font-weight: bold;'>"
						+ "  <table width='100%' border='0' cellspacing='0' cellpadding='0'>"
						+ "   <tr>"
						// + "  <td>"
						// +
						// "    <img src='file:///android_asset/yutu.png' width='40' height='33' />"
						// + "  </td>"
						+ " <td>"
						+ "    �п���ͼ�Ƽ��ɷ����޹�˾"
						+ "         </td>"
						+ "        </tr>"
						+ "      </table>"
						+ "    </td>"
						+ " </tr>"
						+ "  <tr>"
						+ " <td style='width: 100%; height: 37px; color: #1f6baf; font-weight: bold; text-align: center;'>"
						+ "  ��������Ȩ��"
						+ " </td>"
						+ "   </tr>"
						+ "  </table>"
						+ "  </td>" + "</tr>" + "</table>" + " </td>" + " </tr>" + "</table>";
				intent = new Intent(context, WebViewer.class);
				// it.putExtra("name", "about.html");
				intent.putExtra("otherstr", otherstr);
				intent.putExtra("title", "����ϵͳ");

				/** ϵͳ���� */
			} else if (XTSZ.equals(buttonName)) {
				intent = new Intent(context, SettingActivity.class);
			} else if (JCGX.equals(buttonName)) {
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					OtherTools.showToast(context, "��ȡ����˰汾��Ϣʧ�ܣ�������������");
				} else {

				}
			} else if (APKINS.equals(buttonName)) {
				intent = new Intent(context, APKManagerActivity.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return intent;
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
		return XTWH_MODULEID;
	}
}
