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
 * FileName: QTXX.java Description: 其他信息（九宫格）
 * 
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-4 上午10:26:30
 */
public class QTXX extends BaseClass implements IGrid, Serializable {

	/**
	 * FileName: QTXX.java Description:
	 * 
	 * @author Administrator
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-3-7 上午10:00:28
	 */
	private final long serialVersionUID = -4415592009782674981L;
	/** 该实体类的名称 */
	public static final String BusinessClassName = "QTXX";
	/** 九宫格包涵实体类——关于系统 */
	public final String GYXT = "关于系统";
	/** 九宫格包涵实体类——检查更新 */
	public final String JCGX = "检查更新";
	/** 九宫格包涵实体类——系统设置 */
	public final String XTSZ = "系统设置";
	/** 九宫格包涵实体类——软件安装 */
	public final String APKINS = "软件安装";
	/** 显示九宫格的名字 */
	private String TITILE = "系统管理";
	/** 九宫格的配置文件（目前使用数据库配置） */
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

						" <tr>" + " <td background='images/title01.gif' style='width: 100%; height: 37px; text-align: center; color: #1f6baf; font-weight: bold;'> 环境监察移动执法系统</td>"
						+ "</tr>" + "<tr>" + "<td style='width: 100%; height: 37px; text-align: left; color: #1f6baf; font-weight: bold;'>"
						+ " <table border='0' cellspacing='0' cellpadding='0' align='center'>" + " <tr>"
						+ "  <td align='center' style=' width: 100%; height: 37px; color: #1f6baf; font-weight: bold;'>" + "    版本号："
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
						+ "    中科宇图科技股份有限公司"
						+ "         </td>"
						+ "        </tr>"
						+ "      </table>"
						+ "    </td>"
						+ " </tr>"
						+ "  <tr>"
						+ " <td style='width: 100%; height: 37px; color: #1f6baf; font-weight: bold; text-align: center;'>"
						+ "  保留所有权利"
						+ " </td>"
						+ "   </tr>"
						+ "  </table>"
						+ "  </td>" + "</tr>" + "</table>" + " </td>" + " </tr>" + "</table>";
				intent = new Intent(context, WebViewer.class);
				// it.putExtra("name", "about.html");
				intent.putExtra("otherstr", otherstr);
				intent.putExtra("title", "关于系统");

				/** 系统设置 */
			} else if (XTSZ.equals(buttonName)) {
				intent = new Intent(context, SettingActivity.class);
			} else if (JCGX.equals(buttonName)) {
				if (!Net.checkURL(Global.getGlobalInstance().getSystemurl())) {
					OtherTools.showToast(context, "获取服务端版本信息失败，请检查网络设置");
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
