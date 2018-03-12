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

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.ui.SubStringXWBL;

public class PreviewWrit_lnxw_simple {
	private WebView wView;
	private Activity activity;
	private YutuLoading yutuLoading;
	private String RWBH;
	private String GUID;
	private String TYPE;
	private Intent intent;
	private final String KCBL_TABLENAME = "T_ZFWS_KCBL";
	private final String WRY_QYJBXX = "T_WRY_QYJBXX";
	/** 存放html页面源码 */
	private final StringBuffer htmlSb = new StringBuffer();
	/** 查询出的笔录数据 */
	ArrayList<HashMap<String, Object>> wsData;
	/** 查询出的问答数据 */
	ArrayList<HashMap<String, Object>> wdData;

	int linesTotal = 0;
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

			case SUCCESS:
				if (yutuLoading != null) {
					yutuLoading.dismissDialog();
				}

				break;
			default:
				break;
			}
		};
	};

	public PreviewWrit_lnxw_simple(String rWBH, String qyid, String tYPE, Activity a) {
		super();
		RWBH = rWBH;
		GUID = qyid;
		TYPE = tYPE;
		activity = a;
	}

	public String create() {
		if (RWBH == null || GUID == null) {
			handler.sendEmptyMessage(NO_RWBH_QYDM);
		}
		path = path + RWBH + "/" + GUID;

		String sqlxw = "select * from T_ZFWS_xwbl where taskid = '" + RWBH + "' " + " and type = '" + TYPE + "'";

		String sqlwd = "select distinct  taskid,wtnr,result  from T_ZFWS_XWJLWD where taskid = '" + RWBH + "'";

		wsData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlxw);

		wdData = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sqlwd);// 得判断是否为null
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
			InputStream is = manager.open("hljxw.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			String text = new String(buffer);
			text = pushResult1(text);
			if (writeFile(text, path)) {
				handler.sendEmptyMessage(SUCCESS);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "y";

	}

	private final int firstpageLimit = 11;
	private final int pageLimit = 26;

	/**
	 * 
	 * @param text
	 * @return
	 */
	private String pushResult1(String text) {

		// 拼头部
		String address = Global.getGlobalInstance().getUserUnit(); // 填充开始结束时间
		text = text.replace("{region}", address);
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
		String surveyaddress = wsData.get(0).get("surveyaddress").toString();
		text = text.replace("{SurveyAddress}", surveyaddress);
		// 填充 "询问人及执法证号" 和 "记录人："

		// String dutypeople = wsData.get(0).get("dutypeople").toString();

		// if (dutypeople != null && !dutypeople.equals("")) {
		// dutypeople = dutypeople.replace(",", " ");
		// }
		// String DutyPeopleRelation =
		// wsData.get(0).get("dutypeoplerelation").toString();

		// 填充"被询问人姓名及职务：" 和 "电话：
		String DutyPeopleOffice = wsData.get(0).get("askedpeopleduty").toString();
		String surveypeoplecradcode = wsData.get(0).get("surveypeoplecradcode").toString();
		text = text.replace("{SurveyPeopleCradCode}", surveypeoplecradcode);
		String xunwenren = wsData.get(0).get("surveypeoplename").toString();
		String surveypeoplestr = SubStringXWBL.SubStringSurveyPeopleName(xunwenren);
		text = text.replace("{SurveyPeopleCradCode}", surveypeoplestr);
		String SurveyPeopleCradCode = SubStringXWBL.SubStringSurveyPeopleCradCode(xunwenren, surveypeoplecradcode);

		String askpeoname = wsData.get(0).get("askedpeoplename").toString();
		text = text.replace("{AskedPeopleName}", askpeoname);
		String askpeoage = wsData.get(0).get("askedpeoplename").toString();
		text = text.replace("{AskedPeopleName}", askpeoage);
		// 这里
		// String DutyPeopleRelation =
		// wsData.get(0).get("dutypeoplerelation").toString();

		String askpepeosex = wsData.get(0).get("askedpeoplesex").toString();
		text = text.replace("{AskedPeopleSex}", askpepeosex);// 被询问人性别

		String askedpeopletel = wsData.get(0).get("askedpeopletel").toString();
		text = text.replace("{AskedPeopleDuty}", DutyPeopleOffice);
		text = text.replace("{AskedPeopleTel}", askedpeopletel);

		String askedpeoage = wsData.get(0).get("askedpeopleage").toString();
		text = text.replace("{AskedPeopleAge}", askedpeoage);

		// 填充"工作单位" 和 "地址："
		String sureyentprisename = wsData.get(0).get("surveyentprisename").toString();
		text = text.replace("{SurveyEntpriseName}", sureyentprisename);
		
		
//		String dutypeopledepartment = wsData.get(0).get("dutypeopleentprisename").toString();
//		if(dutypeopledepartment.equals("")||dutypeopledepartment==null){
//			String code =Global.getGlobalInstance().getAreaCode();
//			String str ="select * from region where regioncode = '"+code+"'";
//			ArrayList<HashMap<String, Object>> DanWeiData = new ArrayList<HashMap<String, Object>>();
//			DanWeiData=sqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(str);
//			String title=DanWeiData.get(0).get("orgtitle").toString();
//			text = text.replace("{DutyPeopleEntpriseName}",title );
//		}else{
//		
//		String dutypeopledepartments = wsData.get(0).get("dutypeopleentprisename").toString();
		text = text.replace("{DutyPeopleEntpriseName}", wsData.get(0).get("recordpeopleunit").toString());
//		}
		
		
		
		String dutypeopleaddress = wsData.get(0).get("dutypeopleaddress").toString();
		text = text.replace("{DutyPeopleAddress}", dutypeopleaddress);

		// 填充"法定代表人姓名及职务：" 和 "电话："和"邮编："
		String checkpeople = wsData.get(0).get("dutypeoplename").toString();
		if (checkpeople != null && !checkpeople.equals("")) {
			checkpeople = checkpeople.replace(",", " ");
		}

		String dutypeopletel = wsData.get(0).get("dutypeopletel").toString();
		String dutypeopleyzbm = wsData.get(0).get("dutypeopleyzbm").toString();
		text = text.replace("{DutyPeopleName}", checkpeople);
		text = text.replace("{dutypeopletel}", dutypeopletel);
		text = text.replace("{DutyPeopleYZBM}", dutypeopleyzbm);
		// 填充"营业执照注册号" 和 "组织机构代码："
		// String yyzzzch = wsData.get(0).get("yyzzzch").toString();
		// String zzjgdm = wsData.get(0).get("zzjgdm").toString();
		// text = text.replace("{yyzzzch}", yyzzzch);
		// text = text.replace("{zzjgdm}", zzjgdm);
		// 填充"其他参加人姓名及工作单位(地址)："
		// String OtherPeopleAddressStr =
		// wsData.get(0).get("otherpeopleaddress").toString();
		// text = text.replace("{OtherPeopleAddressStr}",
		// OtherPeopleAddressStr);
		//

		// 被询问自然人的姓名、年龄、性别、电话、职位、住址、与本案关系
		String gsd = Global.getGlobalInstance().getUserUnitforquestion();
		text = text.replace("{region}", gsd);
		String natruepeoname = wsData.get(0).get("otherpeoplename").toString();
		String natruepeosex = wsData.get(0).get("otherpeoplesex").toString();
		String natruepeotel = wsData.get(0).get("otherpeopletel").toString();
		String natruepeoentprisename = wsData.get(0).get("otherpeopleentprisename").toString();
		String natruepeoaddress = wsData.get(0).get("otherpeopleaddress").toString();
		String natruepeoage = wsData.get(0).get("otherpeopleage").toString();
		String natruepeoreplation = wsData.get(0).get("otherpeoplerelation").toString();
		String recordpeoplename = wsData.get(0).get("recordpeoplename").toString();
		if ("-请先选择检查人-".equals(recordpeoplename)) {
			recordpeoplename = "";
		}
		text = text.replace("{RecordPeopleName}", recordpeoplename);
		text = text.replace("{OtherPeopleName}", natruepeoname);
		text = text.replace("{OtherPeopleSex}", natruepeosex);
		text = text.replace("{OtherPeopleAge}", natruepeoage);
		text = text.replace("{OtherPeopleTel}", natruepeotel);
		text = text.replace("{OtherPeopleEntpriseName}", natruepeoentprisename);
		text = text.replace("{OtherPeopleAddress}", natruepeoaddress);
		text = text.replace("{OtherPeopleRelation}", natruepeoreplation);
		text = text.replace("{askedpeoplename}", wsData.get(0).get("askedpeoplename").toString());
		text = text.replace("{sex}", wsData.get(0).get("askedpeoplesex").toString());
		text = text.replace("{askedpeopleage}", wsData.get(0).get("askedpeopleage").toString());
		text = text.replace("{askedpeopleidentitycard}", wsData.get(0).get("askedpeopleidnumber").toString());
		text = text.replace("{askedpeopleworkplace}", wsData.get(0).get("surveyentprisename").toString());
		text = text.replace("{askedpeopleduty}", wsData.get(0).get("askedpeopleduty").toString());
		
		// 页面底部签名
		final String foot = "<div id=\"mydiv\" >" +
				"<table  style=\"text-align:center;margin:0 auto;width:820px\">" +
				"<tr><td><span style=\"width: 220px;\">被调查询问人确认意见：</span>" +
				"<span class=\"content120\" style=\"width: 580px\">&nbsp;</span>" +
				"</td></tr><tr><td><span style=\"width: 180px;\">被调查询问人签字：</span>" +
				"<span class=\"content120\" style=\"width: 370px\"id=\"data1\" >&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data2\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">年</span>" +
				"<span class=\"content120\" style=\"width: 60px;text-align: center\" id=\"data3\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data4\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span>" +
				"</td></tr><tr><td><span style=\"width:160px;\">调查询问人签字：</span>" +
				"<span class=\"content120\" style=\"width: 185px\" id=\"data5\" >&nbsp;</span>" +
				"<span style=\"width:20\">、</span>" +
				"<span class=\"content120\" style=\"width: 185px\" id=\"data5\" >&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data6\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">年</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data7\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data8\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span></td></tr><tr><td>" +
				"<span style=\"width: 120px;\">记录人签字：</span>" +
				"<span class=\"content120\" style=\"width: 430px\"id=\"data1\" >&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data2\" >&nbsp;</span> " +
				"<span style=\"width: 20px;\">年</span> " +
				"<span class=\"content120\" style=\"width: 60px;text-align: center\" id=\"data3\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data4\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span>" +
				"</td></tr><tr><td><span style=\"width: 120px;\">参加人签字：</span>" +
				"<span class=\"content120\" style=\"width: 430px\"id=\"data1\" >&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data2\" >&nbsp;</span> " +
				"<span style=\"width: 20px;\">年</span> " +
				"<span class=\"content120\" style=\"width: 60px;text-align: center\" id=\"data3\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">月</span>" +
				"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data4\" >&nbsp;</span>" +
				"<span style=\"width: 20px;\">日</span></td></tr><tr><td align=\"center\">" +
				"<hr style=\"width: 800px; float: left; border: 1px solid #000\" /><br/>" +
				"<font style=\"float:right\">第 {Page} 页共  {TotalPage} 页&nbsp;&nbsp;</font>" +
				"<br/><br/></td></tr></table></div><br/>";

		// 所有问答每一行文字的集合
		ArrayList<String> lines = new ArrayList<String>();
		if (wdData != null) {
			lines = getLines(wdData);
		}
		text = makeAText(text, address, foot, lines);

		return text;
	}

	private String makeAText(String text, String address, final String foot, ArrayList<String> lines) {
		int n = 0;
		int page = 1;
		StringBuffer sb = new StringBuffer();
		Log.i("info", "size:" + lines.size());
		int size = lines.size();
		boolean flag = false;
		if (size <= 0) {
			text = text.replace("<addWD>", question1(address) + answer1("<br/>(以下空白)"));
			text = text.replace("{Page}", "1");
		} else {
			for (int k = 0; k < lines.size(); k++) {
				String str = lines.get(k);
				int limit = page == 1 ? firstpageLimit : pageLimit;
				Log.i("info", "k:" + k + ",size-1:" + (size - 1));
				sb.append("<tr><td width=\"800px\" style=\"font-size:20px;" + "text-decoration:underline;line-height:30px; \" >" + str
						+ (k == lines.size() - 1 ? "<br/>(以下空白)" : "") + "</td></tr>");
				n++;
				if (n == limit) {
					Log.i("info", "换页");
					if (n != size) {
						sb.append("<tr><td width=\"800px\" style=\"font-size:20px\">(续下页)</td></tr>");
						flag = true;
					}
					if (page == 1) {
						size -= firstpageLimit;
						text = text.replace("<addWD>", question1(address) + answer1("") + sb.toString());
						text = text.replace("{Page}", "1");
					} else {
						size -= pageLimit;
						String s = "<div  style=\"height: 25cm;\">" + "<table style=\" margin: 5px auto; margin-top: 70px\">"
								+ "<tr><td><font style=\"font-weight:bold; text-decoration:underline;line-height:30px;\">(接上页)</font></td></tr>" + sb.toString() + "</table></div>";
						text += s;
						text += foot;
						text = text.replace("{Page}", "" + page);
					}

					sb = new StringBuffer();
					if (flag)
						page++;
					flag = false;
					n = 0;
				} else {
					if (n == size) {
						if (page != 1) {
							String s = "<div  style=\"height: 25cm;\">" + "<table style=\" margin: 5px auto; margin-top: 70px\">"
									+ "<tr><td><font style=\"font-weight:bold; text-decoration:underline;line-height:30px;\">(接上页)</font></td></tr>" + sb.toString()
									+ "</table></div>";
							text += s;
							text += foot;
							text = text.replace("{Page}", "" + page);
						} else {
							text = text.replace("<addWD>", question1(address) + answer1("") + sb.toString());
							text = text.replace("{Page}", "" + page);
						}
					}
				}
			}
		}
		text = text.replace("{TotalPage}", "" + page);
		return text;
	}

	// 将所有的问答记录，拆分成每行一条的单个语句
	private ArrayList<String> getLines(ArrayList<HashMap<String, Object>> wdData2) {
		ArrayList<String> lines = new ArrayList<String>();
		int size = wdData2.size();
		Log.i("info", "问答数量：" + size);
		final int lineLength = 40;
		for (int i = 0; i < size; i++) {

			String wtnrStr = "问： " + wdData.get(i).get("wtnr").toString();
			String result = "答： " + wdData.get(i).get("result").toString();
			Log.i("info", wtnrStr);
			Log.i("info", result);
			int n;
			if ((n = wtnrStr.length() / lineLength) > 0) {
				for (int j = 0; j < n; j++) {
					lines.add(wtnrStr.substring(j * lineLength, (j + 1) * lineLength));
				}
				if (wtnrStr.length() % lineLength > 0) {
					lines.add(wtnrStr.substring((wtnrStr.length() / lineLength * lineLength), wtnrStr.length()));
				}
			} else {
				lines.add(wtnrStr);
			}

			if ((n = result.length() / lineLength) > 0) {
				for (int j = 0; j < n; j++) {
					lines.add(result.substring(j * lineLength, (j + 1) * lineLength));
				}
				if (result.length() % lineLength > 0) {
					lines.add(result.substring((result.length() / lineLength * lineLength), result.length()));
				}
			} else {
				lines.add(result);
			}

		}

		return lines;
	}
	/**
	 * 拼"问1"
	 * 
	 * 
	 */
	private String question1(String address) {
		String a = " <tr ><td width=\"800px\">" + "<font style=\"font-weight:bold;font-size:20px\"></font>"
				+ "<font style=\"text-decoration:underline;word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">" + "问：我们是"
				+ Global.getGlobalInstance().getUserUnitforquestion() + "的执法人员，这是我们的执法证件，请过目确认。" + "今天我们依法进行监察并了解有关情况，你应当配合调查，如实回答询问和提供材料，不得拒绝、阻碍、隐瞒或者提供虚假情况。"
				+ "如果你认为我们与本案有利害关系，可能影响公正办案，可以申请我们回避，并说明理由。清楚了吗？</font>" + "  </td></tr>";
		return a;
	}

	/**
	 * 拼"答1"
	 * 
	 * 
	 */
	private String answer1(String s) {
		String a = " <tr ><td width=\"800px\">"
				+ "<font style=\"font-weight:bold;font-size:20px\"></font><font style=\"text-decoration:underline;word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "答：看清楚了。不需要回避。" + s + "</font>" + "  </td></tr>";
		return a;
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

}
