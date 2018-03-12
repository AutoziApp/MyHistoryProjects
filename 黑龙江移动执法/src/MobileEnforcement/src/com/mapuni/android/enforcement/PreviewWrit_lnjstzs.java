package com.mapuni.android.enforcement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;

public class PreviewWrit_lnjstzs {

	private YutuLoading yutuLoading;
	private String RWBH;
	private Activity activity;
	private String GUID;
	private Intent intent;

	/** 存放html页面源码 */
	private final StringBuffer htmlSb = new StringBuffer();
	/** 查询出的笔录数据 */
	ArrayList<HashMap<String, Object>> wsData;

	/** 没有传入任务编号和企业代码 */
	private final int NO_RWBH_QYDM = 0;
	/** 数据库查询错误 */
	private final int DATABASEERR = 1;
	/** 数据库无数据 */
	private final int NODATA = 2;
	/** 生成html成功 */
	private final int SUCCESS = 3;
	/** 存放html的路径 */
	private String path = Global.SDCARD_RASK_DATA_PATH + "ImgDoc/";
	private final Calendar calendar = Calendar.getInstance();
	/** 现场情况 */
	private final String SiteConditionStr = "";

	/** 是否分页 */
	private final Boolean ispaging = false;
	/** 分页标识最大字数 */
	private final int MaxWordNum = 100;
	long NOWTIME = System.currentTimeMillis();

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NO_RWBH_QYDM:
				if (yutuLoading != null) {

				}
				break;
			case DATABASEERR:
				if (yutuLoading != null) {

				}
			case NODATA:
				if (yutuLoading != null) {

				}
				break;
			case SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}
				// wView.loadUrl("file://" + path + "/第1页.html");
				break;
			default:
				break;
			}
		};
	};

	public PreviewWrit_lnjstzs(String rWBH, String qyid, Activity a) {
		super();
		RWBH = rWBH;
		GUID = qyid;
		activity = a;
	}

	public String create() {
		if (RWBH == null || GUID == null) {
			handler.sendEmptyMessage(NO_RWBH_QYDM);
		}
		path = path + RWBH + "/" + GUID;
		String sql = "select * from Survey_JSTZS where taskid = '" + RWBH + "' and entid = '" + GUID + "'";

		wsData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
		if (wsData == null) {// 判断数据库是否有表或数据
			handler.sendEmptyMessage(DATABASEERR);
			return "n";
		} else {
			if (wsData.size() == 0) {
				handler.sendEmptyMessage(NODATA);
				return "n";
			}
		}

		try {
			AssetManager manager = activity.getAssets();
			InputStream is = manager.open("lnjstzs.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			String text = new String(buffer);
			text = pushResult(text);

			if (writeFile(text, path)) {
				handler.sendEmptyMessage(SUCCESS);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "y";
	}

	/**
	 * 把结果填充到html中
	 * 
	 * @param text
	 * @return
	 */
	private String pushResult(String text) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 填充编号
		String noticeno = wsData.get(0).get("noticeno").toString();
		text = text.replace("{noticeno}", noticeno);
		// 填充企业名称
		String entname = wsData.get(0).get("entname").toString();
		text = text.replace("{entname}", entname);
		// 填充签名时间
		String signtimestr = wsData.get(0).get("signtime").toString();
		try {
			text = text.replace("{signtime}", DateFormat.getDateInstance().format(sdf.parse(signtimestr)));
		} catch (ParseException e) {
		}
		// 填充违法行为

		String breakbehavior = wsData.get(0).get("breakbehavior").toString();
		text = text.replace("{违法行为}", breakbehavior);

		// 填充携带材料
		String takematerial = wsData.get(0).get("takematerial").toString();
		String[] temp = takematerial.split(",");
		for (String string : temp) {
			text = text.replace("{" + string + "}", "checked");
		}
		String qtzl = wsData.get(0).get("qtzl").toString();
		if (!"".equals(qtzl)) {
			text = text.replace("{其它}", "checked");
			text = text.replace("{qtzl}", qtzl);
		} else {
			text = text.replace("{其它}", "");
			text = text.replace("{qtzl}", "");// 当其它取消时不应有勾
		}

		// 填充联系人
		String contactperson = wsData.get(0).get("contactperson").toString();
		text = text.replace("{现场检查人}", contactperson);

		// 填充联系电话
		String phone = wsData.get(0).get("phone").toString();
		text = text.replace("{现场检查人联系电话}", phone);
		// 填充地址
		String address = wsData.get(0).get("address").toString();
		text = text.replace("{现场检查人联系地址}", address);

		String fj = wsData.get(0).get("fj").toString();
		text = text.replace("{fj}", "".equals(fj) ? "" : fj);
		text = text.replace("{科室}", wsData.get(0).get("ks").toString());
		String temp1 = wsData.get(0).get("surveytime").toString();
		try {
			temp1 = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.DEFAULT, java.text.DateFormat.MEDIUM, Locale.CHINESE).format(sdf.parse(temp1));
			text = text.replace("{接受调查时间}", temp1.substring(0, temp1.indexOf(":")).replace("AM", "").replace("PM", ""));
		} catch (ParseException e) {
		}
		text = text.replace("{违反法律}", wsData.get(0).get("wffl").toString());

		text = text.replace("{被查单位地址}", wsData.get(0).get("bjcdz").toString());
		text = text.replace("{现场接收人}", wsData.get(0).get("xcjsr").toString());
		text = text.replace("{联系电话}", wsData.get(0).get("bjclxdh").toString());
		text = text.replace("{unit}", Global.getGlobalInstance().getUserUnit());

		return text;
	}

	public void startSpellHTML() {
		htmlSb.append(spellHead());
		htmlSb.append(spellStyle());
		htmlSb.append(spellBodyTitle());
		htmlSb.append(spellBodyBDName());
		htmlSb.append(spellTime());
		htmlSb.append(spellPlace());
		htmlSb.append(spell1());// 拼"检查(勘察)人姓名及执法证号" 和 "记录人："
		htmlSb.append(spell2());// 拼"被检查单位" 和 "地址："
		htmlSb.append(spell3());// 拼"现场负责人姓名及职务：" 和 "电话："和"与本案关系"
		htmlSb.append(spell4());// 拼"法定代表人姓名及职务：" 和 "电话："和"邮编："
		htmlSb.append(spell5());// 拼"营业执照注册号" 和 "组织机构代码："
		htmlSb.append(spell6());// 拼"其他参加人姓名及工作单位(地址)："
		htmlSb.append(spellhr());
		htmlSb.append(spell7());// 拼"执法人员"
		htmlSb.append(spell8());// 拼"现场检查（勘察）情况"
		htmlSb.append(bottom());
	}

	/**
	 * html 头
	 * 
	 * @return
	 */
	private String spellHead() {
		String head = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">" + "<head runat=\"server\">" + "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />"
				+ "<title></title>";
		return head;
	}

	/**
	 * html 样式
	 * 
	 * @return
	 */
	private String spellStyle() {
		String Style = "<style type=\"text/css\">" + ".divedit" + "{ width: 800px;line-height: 30px; background-image: url(../../Images/line.png);"
				+ " background-position: top;  background-repeat: repeat;  vertical-align: middle; " + "word-spacing: 5px; font-size: 18px; font-family: 宋体; "
				+ "word-break:break-all;}" + "body{margin: auto;color: #000;font-size: 18px;font-family: 宋体;}"
				+ " tr td{ height: 22pt; color: #000; font-size: 18px; font-family: 宋体;}"
				+ "span.left{ text-align: left;text-decoration: underline; border: 1px solid #ff0000;width: 98%;display: block;border-bottom: 1px solid #000;color: #000;}"
				+ "span {text-align: left;float: left;color: #000;font-size: 18px;font-family: 宋体; }" + "span.lbl60 {width: 60px;float: left; color: #000;}"
				+ "span.lbl80{ width: 80px;float: left;color: #000;}" + "span.lbl128{width: 128px;float: left;color: #000;}"
				+ "span.content{width: 80px;text-align: center; float: left; border-bottom: 1px solid #000; color: #000;}"
				+ "span.content120{width: 250px;float: left;border-bottom: 1px solid #000; height: 20px;color: #000;}" + "</style>" + "</head>";
		return Style;
	}

	/**
	 * html body头
	 * 
	 * @return
	 */
	private String spellBodyTitle() {
		String BodyTitle = "<body>" + "<form id=\"form1\" runat=\"server\">" + "<div id=\"msg\"> </div>"
				+ "<div id=\"printArea\" style=\"height: 34cm; min-height: 100%; overflow: auto;padding-bottom: 60px;\">";
		return BodyTitle;

	}

	private String spellBodyBDName() {
		String sn = Global.getGlobalInstance().getSystemname();
		String bodyName = "鸡西市环境保护局";
//		if (sn.equalsIgnoreCase("tianjin")) {
//			bodyName = "天津市环境保护局";
//		} else if (sn.equalsIgnoreCase("liaoning")) {
//			bodyName = "黑龙江省环境保护厅";
//		}

		String BodyBDName = "<table style=\" margin: 5px auto; margin-top: 65px\">" + "<tr style=\"height: 45px;\">" + " <td style=\"text-align: center;\">"
				+ " <font style=\"font-size: 20px; font-weight: 700;letter-spacing:4px\">" + bodyName + "</font>" + "<br /><br />"
				+ "<font style=\"font-size: 30px; font-weight: 900;letter-spacing:8px\">" + "现场检查（勘察）笔录</font><br /><br />"
				+ "<hr style=\"width: 800px; float: left; border: 1px solid #000\" />" + " </td>" + "</tr>";
		return BodyBDName;
	}

	/**
	 * 拼开始时间和结束时间
	 * 
	 * @return
	 */
	private String spellTime() {
		String startTime = wsData.get(0).get("surveystartdate").toString();
		String startTime_Year = "&nbsp;";
		String startTime_Month = "&nbsp;";
		String startTime_Day = "&nbsp;";
		String startTime_Hours = "&nbsp;";
		String startTime_Minutes = "&nbsp;";

		if (!startTime.equals("")) {
			Date startDate = DisplayUitl.ParseDate(startTime);
			calendar.setTime(startDate);
			startTime_Year = (calendar.get(Calendar.YEAR)) + "";
			startTime_Month = (calendar.get(Calendar.MONTH) + 1) + "";
			startTime_Day = (calendar.get(Calendar.DAY_OF_MONTH)) + "";
			startTime_Hours = (calendar.get(Calendar.HOUR_OF_DAY)) + "";
			startTime_Minutes = (calendar.get(Calendar.MINUTE)) + "";
		}

		String endTime = wsData.get(0).get("surveyenddate").toString();
		String endTime_Hours = "&nbsp;";
		String endTime_Minutes = "&nbsp;";
		if (!endTime.equals("")) {
			Date endDate = DisplayUitl.ParseDate(endTime);
			endTime_Hours = endDate.getHours() + "";
			endTime_Minutes = endDate.getMinutes() + "";

		}

		String timeStr = " <tr>" + "<td>" + " <span style=\"width: 98px;\">检查时间：</span> <span class=\"content\"  id=\"spanyear\"" + " runat=\"server\">" + startTime_Year
				+ "</span><span>年</span> <span class=\"content\" id=\"spanmonth\"" + " runat=\"server\">" + startTime_Month
				+ "</span> <span>月</span> <span class=\"content\" id=\"spanday\" runat=\"server\">" + " " + startTime_Day
				+ "</span> <span>日</span> <span class=\"content\" id=\"spantime\" runat=\"server\">" + startTime_Hours + "</span>"
				+ "<span>时</span> <span class=\"content\" id=\"spanfen\" runat=\"server\">" + startTime_Minutes + "</span> <span>"
				+ "分至</span> <span class=\"content\" id=\"endtime\" contenteditable=\"true\" runat=\"server\">" + "" + endTime_Hours
				+ "</span> <span>时</span> <span class=\"content\" id=\"endfen\"" + "  runat=\"server\">" + endTime_Minutes + "</span> <span>分</span>" + "</td>" + "</tr>";
		return timeStr;

	}

	/**
	 * 拼企业地点
	 * 
	 * @return
	 */
	private String spellPlace() {
		String place = wsData.get(0).get("surveryaddress").toString();
		String surveryaddress = "<tr>" + "<td>" + " <span style=\"width: 95px;\">检查地点：</span><span id=\"address\" class=\"content120\" "
				+ "runat=\"server\" style=\"width: 705px\">" + place + "</span>" + " </td>" + "</tr>";
		return surveryaddress;

	}

	/**
	 * 拼"检查(勘察)人姓名及执法证号" 和 "记录人："
	 * 
	 * 
	 */

	private String spell1() {
		String SurveyPeople1 = wsData.get(0).get("surveypeoplename").toString();
		String SurveyPeopleCradCode1 = wsData.get(0).get("surveypeoplecradcode").toString();
		String recordpeoplename = wsData.get(0).get("recordpeoplename").toString();
		String a = " <tr>" + "<td>" + "<span style=\"width: 260px;\">检查(勘察)人姓名及执法证号：</span>" + "<span class=\"content120\" contenteditable=\"true\" "
				+ "style=\"width: 360px;\" id=\"JLR\" runat=\"server\">" + SurveyPeople1 + " " + SurveyPeopleCradCode1 + "</span>" + " <span style=\"width: 80px;\">记录人：</span>"
				+ "<span class=\"content120\" style=\"width: 100px; color: #000000\" id=\"NUM1\" contenteditable=\"true\" runat=\"server\">" + recordpeoplename + "</span>"
				+ "</td> </tr>";
		return a;
	}

	/**
	 * 拼"被检查单位" 和 "地址："
	 * 
	 * 
	 */
	private String spell2() {
		String entprisename = wsData.get(0).get("entprisename").toString();
		String place = wsData.get(0).get("surveryaddress").toString();
		String a = " <tr>" + "<td>" + "<span style=\"width: 110px;\">被检查单位：</span>" + "<span class=\"content120\" contenteditable=\"true\" "
				+ "style=\"width: 360px;\" id=\"JLR\" runat=\"server\">" + entprisename

				+ "</span>" + " <span style=\"width: 60px;\">地址：</span>"
				+ " <span class=\"content120\" style=\"width: 205px\" id=\"GZDW1\" contenteditable=\"true\" runat=\"server\">" + place

				+ "</span><span class=\"content120\" style=\"width: 65px; color: #000000\" id=\"NUM1\" contenteditable=\"true\" runat=\"server\"></span>" + "</td> </tr>";
		return a;
	}

	/**
	 * 拼"现场负责人姓名及职务：" 和 "电话："和"与本案关系"
	 * 
	 * 
	 */
	private String spell3() {
		String DutyPeopleName = wsData.get(0).get("dutypeople").toString();
		String DutyPeopleOffice = wsData.get(0).get("dutypeopleoffice").toString();
		String DutyPeopleRelation = wsData.get(0).get("dutypeoplerelation").toString();
		String DutyPeopleTel = wsData.get(0).get("dutypeopletel").toString();
		String a = " <tr>" + "        <td>" + " <span style=\"width: 205px;\">现场负责人姓名及职务：</span> <span class=\"content120\" contenteditable=\"true\""
				+ " style=\"width: 210px\" id=\"FZR\" runat=\"server\">" + DutyPeopleName + " " + DutyPeopleOffice + "</span><span style=\"\">电话：</span>"
				+ " <span id=\"AGE\" class=\"content120\" contenteditable=\"true\" runat=\"server\" style=\"width: 115px\">" + DutyPeopleTel
				+ " </span><span style=\"width: 110px;\">与本案关系：</span><span class=\"content120\" style=\"width: 107px\"" + " id=\"ZJH\" contenteditable=\"true\" runat=\"server\">"
				+ DutyPeopleRelation + "</span>   </td>        </tr>";
		return a;
	}

	/**
	 * 拼"法定代表人姓名及职务：" 和 "电话："和"邮编："
	 * 
	 * 
	 */
	private String spell4() {
		String LawPersonName = wsData.get(0).get("checkpeople").toString();
		String frdbdh = wsData.get(0).get("frdbdh").toString();
		String yzbm = wsData.get(0).get("yzbm").toString();
		String a = " <tr>" + "        <td>" + " <span style=\"width: 205px;\">法定代表人姓名及职务：</span> <span class=\"content120\" contenteditable=\"true\""
				+ " style=\"width: 245px\" id=\"FZR\" runat=\"server\">" + LawPersonName + "</span><span style=\"\">电话：</span>"
				+ " <span id=\"AGE\" class=\"content120\" contenteditable=\"true\" runat=\"server\" style=\"width: 170px\">" + frdbdh
				+ " </span><span style=\"width: 60px;\">邮编：</span><span class=\"content120\" style=\"width: 67px\"" + " id=\"ZJH\" contenteditable=\"true\" runat=\"server\">"
				+ yzbm + "</span>   </td>        </tr>";
		return a;
	}

	/**
	 * 拼"营业执照注册号" 和 "组织机构代码："
	 * 
	 * 
	 */
	private String spell5() {
		String yyzzdm = wsData.get(0).get("yyzzdm").toString();
		String zzjgdn = wsData.get(0).get("zzjgdn").toString();
		String a = " <tr>" + "<td>" + "<span style=\"width: 150px;\">营业执照注册号：</span>" + "<span class=\"content120\" contenteditable=\"true\" "
				+ "style=\"width: 300px;\" id=\"JLR\" runat=\"server\">" + yyzzdm + "</span>" + " <span style=\"width: 130px;\">组织机构代码：</span>"
				+ " <span class=\"content120\" style=\"width: 155px\" id=\"GZDW1\" contenteditable=\"true\" runat=\"server\">" + zzjgdn
				+ "</span><span class=\"content120\" style=\"width: 65px; color: #000000\" id=\"NUM1\" contenteditable=\"true\" runat=\"server\"></span>" + "</td> </tr>";
		return a;
	}

	/**
	 * 拼"其他参加人姓名及工作单位(地址)："
	 * 
	 * 
	 */
	private String spell6() {
		String OtherPeopleAddressStr = wsData.get(0).get("otherpeopleaddress").toString();
		String a = " <tr><td>" + " <span style=\"width: 290px;\">其他参加人姓名及工作单位(地址)：</span><span class=\"content120\" style=\"width: 510px\""
				+ "                     id=\"QYMC\" contenteditable=\"true\" runat=\"server\">" + OtherPeopleAddressStr + "</span>  </td>   </tr>";
		return a;
	}

	/**
	 * 拼"hr横线"
	 * 
	 * 
	 */
	private String spellhr() {
		String a = "<tr>" + "<td>" + "<hr style=\"width: 800px; float: left; border: 1px solid #000\" />" + "</td>" + "</tr>";
		return a;
	}

	/**
	 * 拼"执法人员"
	 * 
	 * 
	 */
	private String spell7() {
		String qrzw = wsData.get(0).get("qrzw").toString();
		// String qrzw = "";
		String a = "<tr ><td width=\"800px\">" + "<font style=\"font-weight:bold;font-size:20px\">执法人员：</font>"
				+ "<font style=\"text-decoration:underline;word-break: break-all; word-wrap:break-word;line-height:35px;letter-spacing:2px;font-size:20px \">" + qrzw + "</font>"
				+ "</td></tr>";
		return a;
	}

	/**
	 * 拼"现场检查（勘察）情况"
	 * 
	 * 
	 */
	private String spell8() {
		String SiteConditionStr = wsData.get(0).get("sitecondition").toString();
		String a = " <tr ><td width=\"800px\">" + "<font style=\"font-weight:bold;font-size:20px\">现场检查(勘察)情况：</font>"
				+ "<font style=\"text-decoration:underline;word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">" + SiteConditionStr + "</font>"
				+ "  </td></tr>";
		return a;
	}

	/**
	 * 拼"底部"
	 * 
	 * 
	 */
	private String bottom() {
		String a = "</table>" + "</div>" + "<div style=\"position: relative; margin-top: -60px; height: 60px; clear:both;text-align:center;\">第1页/共1页 </div>" + "</form>"
				+ "</body>" + "</html>";
		return a;
	}

	/**
	 * 拼检查人
	 * 
	 * @return
	 */
	/*
	 * private String spellSurveyPeople(){ String
	 * SurveyPeople1=wsData.get(0).get("surveypeoplename").toString(); String
	 * SurveyPeopleCradCode1
	 * =wsData.get(0).get("surveypeoplecradcode").toString(); String
	 * SurveyPeopleUnit1=wsData.get(0).get("surveypeopleunit").toString();
	 * 
	 * String SurveyPeople2=wsData.get(0).get("surverypeople2name").toString();
	 * String
	 * SurveyPeopleCradCode2=wsData.get(0).get("surverypeople2cradcode").toString
	 * (); String
	 * SurveyPeopleUnit2=wsData.get(0).get("surverypeople2unit").toString();
	 * 
	 * String SurveyPeopleStr= "<tr>"+ "<td>"+
	 * "<span style=\"width: 155px;\">检查（勘察）人：</span><span class=\"content120\""
	 * + "style=\"width: 75px;\" id=\"KCR\" runat=\"server\"> "+SurveyPeople1+
	 * "</span> <span style=\"width: 110px;\">执法证号：</span><span"+
	 * "class=\"content120\" style=\"width: 65px; color: #000000\" id=\"KCRNum\""
	 * + "runat=\"server\">"+SurveyPeopleCradCode1+
	 * " </span> <span style=\"width: 100px;\">工作单位：</span><span class=\"content120\""
	 * +
	 * "style=\"width: 95px\" id=\"KCRGZDW\"  runat=\"server\">"+SurveyPeopleUnit1
	 * +"</span>"+ " </td>"+ " </tr>"+ "<tr>"+ " <td>"+
	 * " <span style=\"width: 155px;\">检查（勘察）人：</span><span class=\"content120\" "
	 * + " style=\"width: 75px;\" id=\"KCR2\" runat=\"server\">"+SurveyPeople2+
	 * "</span> <span style=\"width: 110px;\">执法证号：</span><span"+
	 * " class=\"content120\" style=\"width: 65px; color: #000000\" id=\"KCR2NUM\" "
	 * + " runat=\"server\">"+SurveyPeopleCradCode2+
	 * "</span> <span style=\"width: 100px;\">工作单位：</span><span class=\"content120\""
	 * +
	 * "style=\"width: 95px\" id=\"KCR2GZDW\"  runat=\"server\">"+SurveyPeopleUnit2
	 * +"</span>"+ "</td>"+ "</tr>";
	 * 
	 * return SurveyPeopleStr; }
	 */
	/**
	 * 拼检查人 调整后
	 * 
	 * @return
	 */
	private String spellSurveyPeople() {
		String SurveyPeople1 = wsData.get(0).get("surveypeoplename").toString();
		String SurveyPeopleCradCode1 = wsData.get(0).get("surveypeoplecradcode").toString();
		String SurveyPeopleUnit1 = wsData.get(0).get("surveypeopleunit").toString();

		String SurveyPeople2 = wsData.get(0).get("surverypeople2name").toString();
		String SurveyPeopleCradCode2 = wsData.get(0).get("surverypeople2cradcode").toString();
		String SurveyPeopleUnit2 = wsData.get(0).get("surverypeople2unit").toString();

		String SurveyPeopleStr = "<tr>" + "<td>" + "<span style=\"width: 145px;\">检查（勘察）人：</span><span class=\"content120\" contenteditable=\"true\""
				+ "style=\"width: 130px;\" id=\"KCR\" runat=\"server\">"
				+ SurveyPeople1
				+ "</span> <span style=\"width: 180px;\">工作单位及执法证号：</span>"
				+ "<span class=\"content120\" style=\"width: 280px\" id=\"KCRGZDW\" contenteditable=\"true\""
				+ " runat=\"server\"> "
				+ SurveyPeopleUnit1
				+ " "
				+ SurveyPeopleCradCode1
				+ "</span><span class=\"content120\" style=\"width: 65px; color: #000000\""
				+ " id=\"KCRNum\" contenteditable=\"true\" runat=\"server\"></span>"
				+ "</td>"
				+ " </tr>"
				+ "<tr>"
				+ "<td>"
				+ "<span style=\"width: 145px;\">检查（勘察）人：</span><span class=\"content120\" contenteditable=\"true\""
				+ "style=\"width: 130px;\" id=\"KCR\" runat=\"server\">"
				+ SurveyPeople2
				+ "</span> <span style=\"width: 180px;\">工作单位及执法证号：</span>"
				+ "<span class=\"content120\" style=\"width: 280px\" id=\"KCRGZDW\" contenteditable=\"true\""
				+ " runat=\"server\"> "
				+ SurveyPeopleUnit2
				+ " "
				+ SurveyPeopleCradCode2
				+ "</span><span class=\"content120\" style=\"width: 65px; color: #000000\""
				+ " id=\"KCRNum\" contenteditable=\"true\" runat=\"server\"></span>" + "</td>" + " </tr>";
		return SurveyPeopleStr;
	}

	/**
	 * 拼记录人
	 * 
	 * @return
	 */
	/*
	 * private String spellRecordPeople(){
	 * 
	 * String recordpeoplename=wsData.get(0).get("recordpeoplename").toString();
	 * String
	 * RecPeopleCradCode=wsData.get(0).get("recpeoplecradcode").toString();
	 * String RecordPeopleUnit=
	 * wsData.get(0).get("recordpeopleunit").toString();
	 * 
	 * String RecordPeople= "<tr>"+ " <td>"+
	 * " <span style=\"width: 80px;\">记录人姓名：</span><span class=\"content120\" "
	 * +
	 * "style=\"width: 150px;\" id=\"JLR\" runat=\"server\">"+recordpeoplename+
	 * "</span> <span style=\"width: 110px;\">执法证号：</span><span"+
	 * "class=\"content120\" style=\"width: 65px; color: #000000\" id=\"NUM1\" "
	 * + "runat=\"server\">"+RecPeopleCradCode+
	 * "</span> <span style=\"width: 100px;\">工作单位：</span><span class=\"content120\""
	 * +
	 * "style=\"width: 95px\" id=\"GZDW1\"  runat=\"server\">"+RecordPeopleUnit
	 * +"</span>"+ "</td>"+ "</tr>"; return RecordPeople; }
	 */
	/**
	 * 拼记录人 修改后
	 * 
	 * @return
	 */
	private String spellRecordPeople() {

		String recordpeoplename = wsData.get(0).get("recordpeoplename").toString();
		String RecPeopleCradCode = wsData.get(0).get("recpeoplecradcode").toString();
		String RecordPeopleUnit = wsData.get(0).get("recordpeopleunit").toString();

		String RecordPeople = "<tr>" + "<td>" + "<span style=\"width: 80px;\">记录人：</span><span class=\"content120\" contenteditable=\"true\""
				+ "style=\"width: 195px;\" id=\"JLR\" runat=\"server\">" + recordpeoplename + "</span> <span style=\"width: 180px;\">工作单位及执法证号：</span>"
				+ "<span class=\"content120\" style=\"width: 280px\" id=\"GZDW1\" contenteditable=\"true\" runat=\"server\">" + "" + RecordPeopleUnit + " " + RecPeopleCradCode
				+ "</span><span class=\"content120\" style=\"width: 65px; color: #000000\" id=\"NUM1\" contenteditable=\"true\"" + "runat=\"server\"></span>" + "</td>" + "</tr>";
		return RecordPeople;
	}

	/**
	 * 拼被检查人姓名 实际为企业名称
	 * 
	 * @return
	 */
	private String spellbeCheckedPeople() {

		String beCheckedPeopleName = wsData.get(0).get("entprisename").toString();
		String beCheckedPeople = "<tr>" + "<td>" + "<span style=\"width: 180px;\">被检查人名称或姓名：</span><span class=\"content120\" style=\"width: 620px\""
				+ "id=\"QYMC\" runat=\"server\">" + beCheckedPeopleName + "</span>" + "</td>" + "</tr>";
		return beCheckedPeople;

	}

	/**
	 * 拼法人代表
	 * 
	 * @return
	 */
	private String spellLawPerson() {
		String LawPersonName = wsData.get(0).get("checkpeople").toString();
		String LawPerson = " <tr>" + "<td>" + "<span style=\"width: 240px;\">法定代表人（负责人）姓名：</span><span class=\"content120\" style=\"width: 560px\""
				+ " id=\"DBR\"  runat=\"server\">" + LawPersonName + "</span>" + " </td>" + " </tr>";
		return LawPerson;
	}

	/**
	 * 拼现场负责人
	 * 
	 * @return
	 */
	private String spellDutyPeople() {
		String DutyPeopleName = wsData.get(0).get("dutypeople").toString();
		String DutyPeopleAge = wsData.get(0).get("dutypeopleage").toString();
		String DutyPeopleCode = wsData.get(0).get("dutypeoplecode").toString();
		String DutyPeopleDepartment = wsData.get(0).get("dutypeopledepartment").toString();
		String DutyPeopleOffice = wsData.get(0).get("dutypeopleoffice").toString();
		String DutyPeopleRelation = wsData.get(0).get("dutypeoplerelation").toString();
		String DutyPeopleAddress = wsData.get(0).get("dutypeopleaddress").toString();
		String DutyPeopleTel = wsData.get(0).get("dutypeopletel").toString();
		String DutyPeopleYZBM = wsData.get(0).get("dutypeopleyzbm").toString();

		String DutyPeople = "<tr>" + "<td>" + " <span style=\"width: 145px;\">现场负责人姓名：</span> <span class=\"content120\" contenteditable=\"true\""
				+ " style=\"width: 95px\" id=\"FZR\" runat=\"server\">"
				+ DutyPeopleName
				+ "</span><span style=\"\">年龄：</span>"
				+ " <span id=\"AGE\" class=\"content120\" contenteditable=\"true\" runat=\"server\" style=\"width: 50px\">"
				+ " "
				+ DutyPeopleAge
				+ "</span><span style=\"width: 130px;\">公民身份号码：</span><span class=\"content120\" style=\"width: 327px\""
				+ "id=\"ZJH\" contenteditable=\"true\" runat=\"server\">"
				+ DutyPeopleCode
				+ "</span>"
				+ "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>"
				+ "<span style=\"width: 100px;\">工作单位：</span><span class=\"content120\" contenteditable=\"true\""
				+ "style=\"width: 175px\" id=\"GZDW2\" runat=\"server\">"
				+ DutyPeopleDepartment
				+ "</span> <span style=\"width: 55px;\">"
				+ " 职务：</span><span class=\"content120\" style=\"width: 160px\" id=\"ZW\" contenteditable=\"true\""
				+ " runat=\"server\">"
				+ DutyPeopleOffice
				+ "</span> <span style=\"width: 120px;\">与本案关系：</span><span class=\"content120\""
				+ "contenteditable=\"true\" style=\"width: 190px\" id=\"BAGX\" runat=\"server\">"
				+ DutyPeopleRelation
				+ "</span>"
				+ "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>"
				+ "<span style=\"width: 65px;\">地址：</span><span class=\"content120\" style=\"width: 280px\""
				+ "id=\"DZ\" contenteditable=\"true\" runat=\"server\">"
				+ DutyPeopleAddress
				+ "</span> <span style=\"width: 55px;\">"
				+ "电话：</span><span class=\"content120\" style=\"width: 135px\" id=\"Phone\" contenteditable=\"true\""
				+ " runat=\"server\">"
				+ DutyPeopleTel
				+ "</span><span style=\"width: 55px;\"> 邮编：</span><span class=\"content120\""
				+ "style=\"width: 210px\" id=\"YZBM\" contenteditable=\"true\" runat=\"server\">"
				+ DutyPeopleYZBM + "</span>" + "</td>" + "</tr>";
		return DutyPeople;
	}

	/**
	 * 拼其他参加人员姓名以及工作单位
	 * 
	 * @return
	 */
	private String spellOtherPeopleAddress() {

		String OtherPeopleAddressStr = wsData.get(0).get("otherpeopleaddress").toString();
		String OtherPeopleAddress = "<tr>" + "<td>" + "<span style=\"width: 260px;\">其他参加人姓名及工作单位：</span>" + "</td>" + "</tr>" + " <tr>" + "<td>"
				+ " <div class=\"divedit\" contenteditable=\"true\" style=\"width: 800px;\" id=\"OtherPeopleAddress\"" + "runat=\"server\">&nbsp;&nbsp;<u>" + OtherPeopleAddressStr
				+ " </u></div>" + "</td>" + "</tr>";

		return OtherPeopleAddress;
	}

	/**
	 * 拼现场情况
	 * 
	 * @return
	 */
	private String spellSiteCondition() {
		String SiteConditionStr = wsData.get(0).get("sitecondition").toString();
		if (SiteConditionStr.length() > 100) {// 做分页

		}
		String SiteCondition = "<tr>" + "<td>" + "<span style=\"width: 200px\">现场情况：</span>" + "</td>" + "</tr>" + "<tr>" + "<td>"
				+ "<div class=\"divedit\" contenteditable=\"true\" style=\"width: 800px;\" id=\"XCQK\" runat=\"server\">" + "&nbsp;&nbsp;<u>" + SiteConditionStr + "</u>"
				+ "</div>" + "</td>" + "</tr>" + "<tr>" + "<td>" + "以上笔录已阅无误" + "</td>" + "</tr>";
		return SiteCondition;
	}

	/**
	 * 拼现场负责人签字
	 * 
	 * @return
	 */
	private String spellDutyPeopleAutograph() {
		String DutyPeopleAutograph = "<tr>" + "<td>" + "<span style=\"width: 155px;\">现场负责人签名：</span><span class=\"content120\" style=\"width: 375px\""
				+ "id=\"data1\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data2\" runat=\"server\">"
				+ "&nbsp;</span> <span style=\"width: 10px;\">年</span> <span class=\"content120\" style=\"width: 60px;"
				+ "text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">月</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data4\" runat=\"server\">" + "&nbsp;</span><span style=\"width: 10px;\">日</span>"
				+ "</td>" + "</tr>";
		return DutyPeopleAutograph;
	}

	/**
	 * 拼检查人签字
	 * 
	 * @return
	 */
	private String spellSurveyPeopleAutograph() {
		String SurveyPeopleAutograph = "<tr>" + "<td>" + "<span style=\"width: 180px;\">检查（勘察）人签名：</span><span class=\"content120\" style=\"width: 350px\""
				+ "id=\"data5\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data6\" runat=\"server\">"
				+ "&nbsp;</span> <span style=\"width: 10px;\">年</span> <span class=\"content120\" style=\"width: 60px;"
				+ "text-align: center\" id=\"data7\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">月</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data8\" runat=\"server\">" + "&nbsp;</span><span style=\"width: 10px;\">日</span>"
				+ "</td>" + "</tr>";
		return SurveyPeopleAutograph;
	}

	/**
	 * 拼记录人签字
	 * 
	 * @return
	 */
	private String spellRecordPeopleAutograph() {
		String RecordPeopleAutograph = "<tr>" + "<td>" + "<span style=\"width: 110px;\">记录人签名：</span><span class=\"content120\" style=\"width: 420px\""
				+ "id=\"data9\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data10\" runat=\"server\">"
				+ " &nbsp;</span> <span style=\"width: 10px;\">年</span> <span class=\"content120\" style=\"width: 60px;"
				+ " text-align: center\" id=\"data11\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">月</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data12\" runat=\"server\">" + "&nbsp;</span><span style=\"width: 10px;\">日</span>"
				+ "</td>" + "</tr>";
		return RecordPeopleAutograph;
	}

	/**
	 * 拼其他参加人员签字
	 * 
	 * @return
	 */
	private String spellOtherPeopleAutograph() {
		String OtherPeopleAutograph = "<tr>" + "<td>" + "<span style=\"width: 150px;\">其他参加人签名：</span><span class=\"content120\" style=\"width: 380px\""
				+ "id=\"data13\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"
				+ "<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data14\" runat=\"server\">"
				+ " &nbsp;</span> <span style=\"width: 10px;\">年</span> <span class=\"content120\" style=\"width: 60px;"
				+ "text-align: center\" id=\"data15\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">月</span>"
				+ " <span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data16\" runat=\"server\">" + " &nbsp;</span><span style=\"width: 10px;\">日</span>"
				+ "</td>" + "</tr>";
		return OtherPeopleAutograph;
	}

	/**
	 * 拼html bottom
	 * 
	 * @return
	 */
	private String spellHtmlBottom() {
		String HtmlBottom = "<tr>" + "<td>" + "<hr style=\"width: 800px; float: left; border: 1px solid #000\" />" + "</td>" + "</tr>" + "</table>" + "</div>"
				+ "<div style=\"height: 30px; padding-left: 10px; float: right; margin-top: 20px; margin-right: 30px\"" + "id=\"print\" runat=\"server\">" + " </div>" + "</form>"
				+ "</body>" + "</html>";
		return HtmlBottom;
	}

	/**
	 * Description:写HTML文件
	 * 
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 下午03:46:29
	 */
	private Boolean writeFile(String htmlInfo, String filePath) {
		Boolean result = false;
		File current_task_dir = new File(filePath);
		if (!current_task_dir.exists()) {
			current_task_dir.mkdirs();
		}
		try {
			File f = new File(filePath + "/" + "第" + 1 + "页" + ".html");

			if (!f.exists()) {
				f.createNewFile();// 不存在则创建
			}

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(htmlInfo);
			output.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 写数据到SD中的文件
	private void writeFileSdcardFile(String fileName, String write_str) {
		try {

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

}
