package com.mapuni.android.enforcement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;

public class PreviewWrit_lnkc {
	private WebView wView;
	String name ="";
	private YutuLoading yutuLoading;
	private String RWBH;
	private Activity activity;
	private String GUID;
	private String TYPE;
	private Intent intent;
	private final String KCBL_TABLENAME = "T_ZFWS_KCBL";
	private final String WRY_QYJBXX = "T_WRY_QYJBXX";
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
	/** 第一页剩余的“现场情况” */
	String residueText = null;
	/** 是否分页 */
	private final Boolean ispaging = false;
	/** 分页标识最大字数 */
	private final int MaxWordNum = 100;
	long NOWTIME = System.currentTimeMillis();
    private String guid = "";
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
	private ArrayList<HashMap<String, Object>> sqlTops;

	public PreviewWrit_lnkc(String rWBH, String qyid,String guid, String tYPE, Activity a) {
		super();
		RWBH = rWBH;
		GUID = qyid;
		TYPE = tYPE;
		this.guid = guid;
		activity = a;
	}

	public String create() {
		if (RWBH == null || GUID == null) {
			handler.sendEmptyMessage(NO_RWBH_QYDM);
		}
		path = path + RWBH + "/" + GUID;
		// String sql = "SELECT a.*,b.yyzzdm,b.zzjgdn,b.frdbdh,b.yzbm  from "
		// +
		// "T_ZFWS_KCBL a inner join T_WRY_QYJBXX b  on  a.entcode=  b.guid where a.TaskId='"
		// + RWBH + "' and a.entcode='" + GUID + "' and type ='"
		// + TYPE + "'";
		String sql = "select * from T_ZFWS_KCBL where taskid = '" + RWBH
				+ "' and entcode = '" + GUID + "'" + " and type = '" + TYPE
				+ "' and guid = '"+guid+"'";

		wsData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
		
		String sqlTop="select RegionName from Region where RegionCode ='"+Global.getGlobalInstance().getAreaCode().toString().trim()+"'";
		
	 sqlTops = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlTop);	
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
			InputStream is = manager.open("hljkc.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			String text = new String(buffer);
			text = pushResult(text);

			// if (residueText != null) {
			// String contextTwo =
			// "<div id=\"printArea\" style=\"height: 25cm;\">" +
			// "<table style=\" margin: 5px auto; margin-top: 65px\">"
			// +
			// "<tr ><td width=\"880px\"  ><font style=\"text-decoration:underline;word-break: break-all; "
			// +
			// "word-wrap:break-word;line-height:35px;letter-spacing:2px;font-size:20px \"><font style=\"font-weight:bold;\">(接上页)</font><br/>"
			// + residueText
			// + "</font></td>" + "</tr></table></div>";
			// text = text.replace("<addTwoPage>", contextTwo + foot);
			// text = text.replace("{TotalPage}", "2");
			// } else {
			// text = text.replace("{TotalPage}", "1");
			// }

			if (writeFile(text, path)) {
				handler.sendEmptyMessage(SUCCESS);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "y";
	}

	private String question1(String address) {
		String a = " <tr ><td width=\"880px\">"
				+ "<font style=\"font-weight:bold;font-size:20px\"></font>"
				+ "<font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "问：我们是" + Global.getGlobalInstance().getUserUnitforquestion()
				+ "的执法人员，这是我们的执法证件，请过目确认。"
				+ "今天我们依法进行监察并了解有关情况，你应当配合调查，如实回答询问和提供材料，不得拒绝、阻碍、隐瞒或者提供虚假情况。"
				+ "如果你认为我们与本案有利害关系，可能影响公正办案，可以申请我们回避，并说明理由。清楚了吗？</font>"
				+ "  </td></tr>";
		return a;
	}

	/**
	 * 把结果填充到html中
	 * 
	 * @param text
	 * @return
	 */
	private String pushResult(String text) {

		String foot = "<div id=\"mydiv\" >" +
				"<table  style=\"text-align:center;margin:0 auto;width:880px\">" +
				"<tr><td><span style=\"width: 400px;\">被检查（勘察）人或现场负责人确认意见：</span>" +
				"<span class=\"content120\" style=\"width: 480px\"id=\"QYMC\"  runat=\"server\"></span>" +
				"</td></tr><tr><td><span style=\"width: 360px;\">被检查（勘察）人或现场负责人签字：</span>" +
				"<span class=\"content120\" style=\"width: 220px\"id=\"data1\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span><span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data2\" runat=\"server\">" +
				"&nbsp;</span><span style=\"width: 20px;\">年</span><span class=\"content120\" style=\"width: 80px;" +
				"text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span><span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data4\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span></td></tr><tr><td><span style=\"width:220px;\">检查（勘察）人签字：</span>" +
				"<span class=\"content120\" style=\"width: 170px\" id=\"data5\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width:20\">、</span>" +
				"<span class=\"content120\" style=\"width: 170px\" id=\"data5\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data6\" runat=\"server\">&nbsp;</span> " +
				"<span style=\"width: 20px;\">年</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data7\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data8\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span></td></tr><tr><td>" +
				"<span style=\"width: 140px;\">记录人签字：</span>" +
				"<span class=\"content120\" style=\"width: 440px\"id=\"data1\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data2\" runat=\"server\">&nbsp;</span> " +
				"<span style=\"width: 20px;\">年</span>" +
				"<span class=\"content120\" style=\"width: 80px;text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data4\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span>" +
				"</td></tr><tr><td>" +
				"<span style=\"width: 140px;\">参加人签字：</span>" +
				"<span class=\"content120\" style=\"width: 440px\"id=\"data1\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data2\" runat=\"server\">&nbsp;</span> " +
				"<span style=\"width: 20px;\">年</span> " +
				"<span class=\"content120\" style=\"width: 80px;text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data4\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span></td></tr><tr>" +
				"<td align=\"center\">" +
				"<hr style=\"width: 880px; float: left; border: 1px solid #000\" /><br/>" +
				"<font style=\"float:right\">第 {currentpage} 页共  {TotalPage} 页&nbsp;&nbsp;</font>" +
				"<br/><br/></td></tr></table></div>";
		// 拼头部
		String topString = sqlTops.get(0).get("regionname").toString().trim();
		if ("黑龙江省".equals(topString)) {
			text = text.replace("{region}",topString+"环境保护厅" );
		}else{
			text = text.replace("{region}", topString+"环境保护局");
		}


		
		// 填充开始结束时间
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
		// text = text.replaceAll("{year}", startTime_Year);
		text = text.replace("{year}", startTime_Year);
		text = text.replace("{month}", startTime_Month);
		text = text.replace("{day}", startTime_Day);
		text = text.replace("{hour1}", startTime_Hours);
		text = text.replace("{min1}", startTime_Minutes);
		text = text.replace("{hour2}", endTime_Hours);
		text = text.replace("{min2}", endTime_Minutes);
		// 填充地点
		String surveyaddress = wsData.get(0).get("surveryaddress").toString();
		text = text.replace("{SurveyAddress}", surveyaddress);
		// 填充"检查(勘察)人姓名及执法证号"
		String SurveyPeople1 = wsData.get(0).get("surveypeopleid").toString();
		text = text.replace("{SurveyPeopleID}", SurveyPeople1);
		String SurveyPeopleName = wsData.get(0).get("surveypeoplename").toString();
		text = text.replace("{SurveyPeopleName}", SurveyPeopleName);
		String SurveyPeopleCradCode = wsData.get(0).get("surveypeoplecradcode").toString();
		text = text.replace("{SurveyPeopleCradCode}", SurveyPeopleCradCode);
		text = text.replace("{postcode}",wsData.get(0).get("dutypeopleyzbm").toString());
//		StringBuilder xunwen1 = new StringBuilder();
//	    
//		if (SurveyPeople1 != null && !SurveyPeople1.equals("")) {
//			String[] xunwen = SurveyPeople1.split(",");
//			if (xunwen.length > 0) {
//				for (int i = 0; i < xunwen.length; i++) {
//					xunwen1.append(xunwen[i]);
//					xunwen1.append(","); 
//				}
//			}
//			name= xunwen1.substring(0, xunwen1.length()-1);
//			text = text.replace("{SurveyPeople1}", name);
//		}else{
//			text = text.replace("{SurveyPeople1}", SurveyPeople1);
//		}
	    

//		String Surveyzh = wsData.get(0).get("surveypeoplecradcode").toString();
//		String code=null;
//		StringBuilder zfzhenghao = new StringBuilder();
//		if (Surveyzh != null && !Surveyzh.equals("")&&!Surveyzh.equals(",")) {
//			String[] zfzhao = Surveyzh.split(",");
//			if (zfzhao.length > 0) {
//				for (int i = 0; i < zfzhao.length; i++) {
//					zfzhenghao.append(zfzhao[i]);
//					zfzhenghao.append(",");
//				}
//			}
//			code=zfzhenghao.substring(0, zfzhenghao.length()-1);
//			text = text.replace("{jiancharenzhifazhenghao}", code);
//		}else{
//			text = text.replace("{jiancharenzhifazhenghao}", Surveyzh);
//		}

		// 记录人姓名
		String recordpeoplename = wsData.get(0).get("recordpeoplename")
				.toString();
		text = text.replace("{RecordPeopleName}", recordpeoplename);
		// 记录人执法证号
//		String recodepeoplezfzh = wsData.get(0).get("recordpeopleid").toString();
//		text = text.replace("{zhifazhenghao}", recodepeoplezfzh);
		
		String qrzw = wsData.get(0).get("surveypeopleunit")
				.toString();
		text = text.replace("{QRZW}", qrzw);
		
		String AskedPeopleName = wsData.get(0).get("checkpeople")
				.toString();
		text = text.replace("{AskedPeopleName}", AskedPeopleName);
		
		
		
		// 填充"被检查单位" 和 "地址："
		String dutypeopledepartment = wsData.get(0).get("dutypeopledepartment")
				.toString();

		text = text.replace("{DutyPeopleDepartment}", dutypeopledepartment);
//		String dutypeopleaddress = wsData.get(0).get("dutypeopleaddress")
//				.toString();
//		text = text.replace("{dutypeopleaddress}", dutypeopleaddress);

		// 填充"现场负责人姓名及职务：" 和 "电话："和"与本案关系"
		String dutypeople = wsData.get(0).get("dutypeople").toString();

//		if (dutypeople != null && !dutypeople.equals("")) {
//			dutypeople = dutypeople.replace(",", " ");
//		}
		String DutyPeopleRelation = wsData.get(0).get("dutypeoplerelation")
				.toString();
		String DutyPeopleCode = wsData.get(0).get("dutypeoplecode")
				.toString();
		text = text.replace("{DutyPeople}", dutypeople);

		text = text.replace("{DutyPeopleCode}", DutyPeopleCode);
		text = text.replace("{DutyPeopleRelation}", DutyPeopleRelation);
		// 填充"法定代表人姓名及职务：" 和 "电话："和"邮编："
		String checkpeople = wsData.get(0).get("checkpeople").toString();
//		if (checkpeople != null && !checkpeople.equals("")) {
//			checkpeople = checkpeople.replace(",", " ");
//		}

		String DutyPeopleOffice = wsData.get(0).get("dutypeopleoffice")
				.toString();
		text = text.replace("{DutyPeopleOffice}", DutyPeopleOffice);
		
		String DutyPeopleAddress = wsData.get(0).get("dutypeopleaddress")
				.toString();
		text = text.replace("{DutyPeopleAddress}", DutyPeopleAddress);
		
		String dutypeopletel = wsData.get(0).get("dutypeopletel").toString();
//		String dutypeopleyzbm = wsData.get(0).get("dutypeopleyzbm").toString();
		text = text.replace("{CheckPeople}", checkpeople);
		text = text.replace("{DutyPeopleTel}", dutypeopletel);
//		text = text.replace("{dutypeopleyzbm}", dutypeopleyzbm);
		// 填充"营业执照注册号" 和 "组织机构代码："
//		String yyzzzch = wsData.get(0).get("yyzzzch").toString();
//		String zzjgdm = wsData.get(0).get("zzjgdm").toString();
//		text = text.replace("{yyzzzch}", yyzzzch);
//		text = text.replace("{zzjgdm}", zzjgdm);
		// 填充"其他参加人姓名及工作单位(地址)："
		String OtherPeopleAddressStr = wsData.get(0).get("otherpeopleaddress")
				.toString();
		text = text.replace("{OtherPeopleAddress}", OtherPeopleAddressStr);
		// 填充"执法人员"
//		String qrzw = wsData.get(0).get("qrzw").toString();
//		text = text.replace("{qrzw}", question1(""));
		// 填充"现场检查（勘察）情况"

		
		text = text.replace("{question1answer}", question()+answer());
		
		
		Log.i("info", "hahhahhahaahs");
		String SiteConditionStr = wsData.get(0).get("sitecondition").toString();

		ArrayList<String> lines = toLinesFromString(SiteConditionStr);

		int firstpageLimit = 13;
		int pageLimit = 26;

		int page = 1;
		int size = lines.size();
		int n = 0;

		StringBuffer sb = new StringBuffer();
		if (size <= firstpageLimit) {
			for (int k = 0; k < size; k++) {
				sb.append("<tr><td align=\"left\" width=\"800px\" style=\"font-size:20px;line-height:30px; \" >"+lines.get(k)+"</td></tr>");
			}
			sb.append("<tr><td align=\"left\" width=\"800px\" style=\"font-size:20px;line-height:30px; \" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(以下空白)</td></tr>");
			text = text.replace("{sitecondition}", sb.toString());
			text = text.replace("{currentpage}", "1");
			text = text.replace("{TotalPage}", "" + page);
			return text;
		}
		for (int k = 0; k < lines.size(); k++) {
			String str = lines.get(k);
			int limit = page == 1 ? firstpageLimit : pageLimit;
			sb.append("<tr><td width=\"880px\" style=\"font-size:20px;"
					+ "line-height:30px; \" >" + str
					+ (k == lines.size() - 1 ? "<br/>(以下空白)" : "")
					+ "</td></tr>");
			n++;
			if (n == limit) {
				Log.i("info", "换页");
				if (k != lines.size() - 1)
					sb.append("<tr><td width=\"880px\" style=\"font-size:20px\">(续下页)</td></tr>");
				if (page == 1) {
					size -= firstpageLimit;
					text = text.replace("{sitecondition}", sb.toString());
					text = text.replace("{currentpage}", "1");
				} else {
					size -= pageLimit;
					String s = "<div  style=\"height: 25cm;\">"
							+ "<table style=\" margin: 5px auto; margin-top: 70px\">"
							+ "<tr><td><font style=\"line-height:30px;\">(接上页)</font></td></tr>"
							+ sb.toString() + "</table></div>";
					text += s;
					text += foot;
					text = text.replace("{currentpage}", "" + page);
				}

				sb = new StringBuffer();
				page++;
				n = 0;
			} else {
				if (n == size) {
					String s = "<div  style=\"height: 25cm;\">"
							+ "<table style=\" margin: 5px auto; margin-top: 70px\">"
							+ "<tr><td><font style=\"line-height:30px;\">(接上页)</font></td></tr>"
							+ sb.toString() + "</table></div>";
					text += s;
					text += foot;
					text = text.replace("{currentpage}", "" + page);
				}
			}
		}
		text = text.replace("{totalpage}", "" + page);
		text = text.replace("{TotalPage}", "" + page);
		return text;
	}

//	private ArrayList<String> toLinesFromString(String siteConditionStr2) {
//		int n;
//		ArrayList<String> lines = new ArrayList<String>();
//		String wtnrStr = siteConditionStr2;
//		int lineLength = 44;
//		if ((n = wtnrStr.length() / lineLength) > 0) {
//			for (int j = 0; j < n; j++) {
//				lines.add(wtnrStr.substring(j * lineLength, (j + 1)
//						* lineLength));
//			}
//			if (wtnrStr.length() % lineLength > 0) {
//				lines.add(wtnrStr.substring(
//						(wtnrStr.length() / lineLength * lineLength),
//						wtnrStr.length()));
//			}
//		} else {
//			lines.add(wtnrStr);
//		}
//		return lines;
//	}
	/**
	 * @author duzp
	 * @description 将一段字符串转化为集合输出,集合的元素为一行<br/>每行为42个中文字的长度,<font color="red">需要考虑中英文及标点长度的问题</font>
	 * @return ArrayList<String>
	 * @date 2016-10-27
	 * @param siteConditionStr2 需要断行的字符串
	 * */
	private ArrayList<String> toLinesFromString(String siteConditionStr2) {
		ArrayList<String> lines = new ArrayList<String>();
		if (TextUtils.isEmpty(siteConditionStr2)) {
			return lines;
		}
		String wtnrStr = siteConditionStr2.replace("<br/>","卪").replace("\n","卪").replace("\r\n","卪");
		//行长度定值
		int lineLength = 44;
		//行长度定值,默认为42个中文字的长度,84格
		int lineSpaces = 88;
		//指针
		int pos = 0;
		int stringLength = wtnrStr.length();
		//匹配中文及中文标点
		String regEx3 = "[\u3000\u00A0\u0020\u4e00-\u9fa5\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b\uff01\uff20\u0021\u0040\u003f]";
		//匹配中文
		String regEx="[\u4e00-\u9fa5]";
		//匹配空格
		String regEx5="[\u3000\u00A0\u0020]";
		//匹配中文标点
		String regEx2 = "[\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b\uff01\uff20\u0021\u0040\u003f]";
		Pattern pattern2 = Pattern.compile(regEx2);  
		Pattern pattern = Pattern.compile(regEx); 
		Pattern pattern3 = Pattern.compile(regEx3); 
		Pattern pattern5 = Pattern.compile(regEx5); 
		Matcher matcher;
		Matcher matcher2;
		Matcher matcher3;
		Matcher matcher5;
		for (;pos < stringLength;) {
			String line = "";
			int index = pos+lineLength;
			//判断是否超出,防止越界
			index = (index >= stringLength ? stringLength : index);
			String line1 = wtnrStr.substring(pos,index);
			line = line + line1;
			if (line.contains("卪")) {
				int posi = line.indexOf("卪")+1;
				if (posi > stringLength) {
					//匹配空格
					String regEx4="[\u3000\u00A0\u0020]";
					Pattern pattern4 = Pattern.compile(regEx4); 
					Matcher matcher4 = pattern4.matcher(line);
					line = matcher4.replaceAll("&nbsp;");
					lines.add(line.replace("卪", ""));
					pos = stringLength;
					continue;
				}else {
					line = line.substring(0, posi).replace("卪", "");
					//匹配空格
					String regEx4="[\u3000\u00A0\u0020]";
					Pattern pattern4 = Pattern.compile(regEx4); 
					Matcher matcher4 = pattern4.matcher(line);
					line = matcher4.replaceAll("&nbsp;");
					lines.add(line);
					pos = pos + posi;
					continue;
				}
			}
			pos=index;
			int l = 0;
			matcher = pattern.matcher(line);  
			//去掉匹配到的字符
            String line4 = matcher.replaceAll("");
            matcher5 = pattern5.matcher(line);
            String kong = matcher5.replaceAll("");
			l=(line4.length()+(line.length()-line4.length())*2)+ (line.length() - kong.length())/4;
			//循环拼接,直到长度为84格
			for (;l < lineSpaces && pos < stringLength;) {
	            matcher = pattern.matcher(line);  
	            String line2 = matcher.replaceAll("");
	            matcher2 = pattern2.matcher(line);
	            String biaodian = matcher2.replaceAll("");
	            matcher5 = pattern5.matcher(line);
	            String kongge = matcher5.replaceAll("");
	            int i = (line.length() - biaodian.length());
	            int ex = (lineSpaces - (line2.length()*8/7)-(line.length()-line2.length())*2 )/2-i/2;
	            if (ex <=0) {
					l=lineSpaces;
					continue;
				}
	            int index2 = pos+ex+(line.length() - kongge.length())/4;
	            //判断是否超出,防止越界
				index2 = (index2 >= stringLength ? stringLength : index2);
				String exString = wtnrStr.substring(pos, index2);
				line = line + exString;
				if (line.contains("卪")) {
					int posi = line.indexOf("卪")+1;
					if (posi > stringLength) {
						lines.add(line.replace("卪", ""));
						pos = stringLength;
						continue;
					}else {
						lines.add(line.substring(0, posi).replace("卪", ""));
						pos = pos + posi;
						continue;
					}
				}
				pos = index2;
	            matcher3 = pattern3.matcher(line);  
	            line2 = matcher3.replaceAll("");
	            matcher5 = pattern5.matcher(line);
	            kongge = matcher5.replaceAll("");
	            l=(line2.length()+(line.length() - kongge.length())/4+(line.length()-line2.length())*2+i/6*2);
			}
			//匹配空格
			String regEx4="[\u3000\u00A0\u0020]";
			Pattern pattern4 = Pattern.compile(regEx4); 
			Matcher matcher4 = pattern4.matcher(line);
			line = matcher4.replaceAll("&nbsp;");
			lines.add(line);
		}
		
//		if ((n = wtnrStr.length() / lineLength) > 0) {
//			for (int j = 0; j < n; j++) {
//				lines.add(wtnrStr.substring(j * lineLength, (j + 1) * lineLength));
//			}
//			if (wtnrStr.length() % lineLength > 0) {
//				lines.add(wtnrStr.substring((wtnrStr.length() / lineLength * lineLength), wtnrStr.length()));
//			}
//		} else {
//			lines.add(wtnrStr);
//		}
		return lines;
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

	public String getPath() {
		return path;
	}

	
	/**
	 * 拼"问1"
	 * 
	 * 
	 */
	private String question() {
		String a = " <tr ><td width=\"880px\" style=\"text-indent:2em;\">" + "<font style=\"font-weight:bold;font-size:20px\"></font>"
				+ "<font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "今天，我们依法进行检查并了解有关情况，你应当配合调查，如实回答询问和提供材料，不得拒绝、阻碍、隐藏或者提供虚假情况。如果你认为我们与本案有利害关系，可能影响公正办案可以申请我们回避，并说明理由。</font>" + "  </td></tr>";
		return a;
	}

	/**
	 * 拼"答1"
	 * 
	 * 
	 */
	private String answer() {
		String a = " <tr ><td width=\"880px\">"
				+ "<font style=\"font-weight:bold;font-size:20px\"></font><font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "问：你申请执法人员回避吗？</font>" + "  </td></tr>";
		String b = " <tr ><td width=\"880px\">"
				+ "<font style=\"font-weight:bold;font-size:20px\"></font><font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "答：清楚了,不需要回避。"  + "</font>" + "  </td></tr>";
		return a+b;
	}

	
	
	
	
}
