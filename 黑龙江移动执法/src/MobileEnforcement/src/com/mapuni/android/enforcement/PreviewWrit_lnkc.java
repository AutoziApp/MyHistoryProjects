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
	/** ���htmlҳ��Դ�� */
	private final StringBuffer htmlSb = new StringBuffer();
	/** ��ѯ���ı�¼���� */
	ArrayList<HashMap<String, Object>> wsData;

	/** û�д��������ź���ҵ���� */
	private final int NO_RWBH_QYDM = 0;
	/** ���ݿ��ѯ���� */
	private final int DATABASEERR = 1;
	/** ���ݿ������� */
	private final int NODATA = 2;
	/** ����html�ɹ� */
	private final int SUCCESS = 3;
	/** ���html��·�� */
	private String path = Global.SDCARD_RASK_DATA_PATH + "ImgDoc/";
	private final Calendar calendar = Calendar.getInstance();
	/** �ֳ���� */
	private final String SiteConditionStr = "";
	/** ��һҳʣ��ġ��ֳ������ */
	String residueText = null;
	/** �Ƿ��ҳ */
	private final Boolean ispaging = false;
	/** ��ҳ��ʶ������� */
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
				// wView.loadUrl("file://" + path + "/��1ҳ.html");
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
		if (wsData == null) {// �ж����ݿ��Ƿ��б������
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
			// "word-wrap:break-word;line-height:35px;letter-spacing:2px;font-size:20px \"><font style=\"font-weight:bold;\">(����ҳ)</font><br/>"
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
				+ "�ʣ�������" + Global.getGlobalInstance().getUserUnitforquestion()
				+ "��ִ����Ա���������ǵ�ִ��֤�������Ŀȷ�ϡ�"
				+ "���������������м�첢�˽��й��������Ӧ����ϵ��飬��ʵ�ش�ѯ�ʺ��ṩ���ϣ����þܾ����谭�����������ṩ��������"
				+ "�������Ϊ�����뱾����������ϵ������Ӱ�칫���참�������������ǻرܣ���˵�����ɡ��������</font>"
				+ "  </td></tr>";
		return a;
	}

	/**
	 * �ѽ����䵽html��
	 * 
	 * @param text
	 * @return
	 */
	private String pushResult(String text) {

		String foot = "<div id=\"mydiv\" >" +
				"<table  style=\"text-align:center;margin:0 auto;width:880px\">" +
				"<tr><td><span style=\"width: 400px;\">����飨���죩�˻��ֳ�������ȷ�������</span>" +
				"<span class=\"content120\" style=\"width: 480px\"id=\"QYMC\"  runat=\"server\"></span>" +
				"</td></tr><tr><td><span style=\"width: 360px;\">����飨���죩�˻��ֳ�������ǩ�֣�</span>" +
				"<span class=\"content120\" style=\"width: 220px\"id=\"data1\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span><span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data2\" runat=\"server\">" +
				"&nbsp;</span><span style=\"width: 20px;\">��</span><span class=\"content120\" style=\"width: 80px;" +
				"text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span><span style=\"width: 20px;\">��</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data4\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span></td></tr><tr><td><span style=\"width:220px;\">��飨���죩��ǩ�֣�</span>" +
				"<span class=\"content120\" style=\"width: 170px\" id=\"data5\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width:20\">��</span>" +
				"<span class=\"content120\" style=\"width: 170px\" id=\"data5\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data6\" runat=\"server\">&nbsp;</span> " +
				"<span style=\"width: 20px;\">��</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data7\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data8\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span></td></tr><tr><td>" +
				"<span style=\"width: 140px;\">��¼��ǩ�֣�</span>" +
				"<span class=\"content120\" style=\"width: 440px\"id=\"data1\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data2\" runat=\"server\">&nbsp;</span> " +
				"<span style=\"width: 20px;\">��</span>" +
				"<span class=\"content120\" style=\"width: 80px;text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data4\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span>" +
				"</td></tr><tr><td>" +
				"<span style=\"width: 140px;\">�μ���ǩ�֣�</span>" +
				"<span class=\"content120\" style=\"width: 440px\"id=\"data1\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 10px;\">&nbsp;</span>" +
				"<span class=\"content120\" style=\"width: 80px; text-align: center\" id=\"data2\" runat=\"server\">&nbsp;</span> " +
				"<span style=\"width: 20px;\">��</span> " +
				"<span class=\"content120\" style=\"width: 80px;text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span>" +
				"<span class=\"content120\" style=\"width: 70px; text-align: center\" id=\"data4\" runat=\"server\">&nbsp;</span>" +
				"<span style=\"width: 20px;\">��</span></td></tr><tr>" +
				"<td align=\"center\">" +
				"<hr style=\"width: 880px; float: left; border: 1px solid #000\" /><br/>" +
				"<font style=\"float:right\">�� {currentpage} ҳ��  {TotalPage} ҳ&nbsp;&nbsp;</font>" +
				"<br/><br/></td></tr></table></div>";
		// ƴͷ��
		String topString = sqlTops.get(0).get("regionname").toString().trim();
		if ("������ʡ".equals(topString)) {
			text = text.replace("{region}",topString+"����������" );
		}else{
			text = text.replace("{region}", topString+"����������");
		}


		
		// ��俪ʼ����ʱ��
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
		// ���ص�
		String surveyaddress = wsData.get(0).get("surveryaddress").toString();
		text = text.replace("{SurveyAddress}", surveyaddress);
		// ���"���(����)��������ִ��֤��"
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

		// ��¼������
		String recordpeoplename = wsData.get(0).get("recordpeoplename")
				.toString();
		text = text.replace("{RecordPeopleName}", recordpeoplename);
		// ��¼��ִ��֤��
//		String recodepeoplezfzh = wsData.get(0).get("recordpeopleid").toString();
//		text = text.replace("{zhifazhenghao}", recodepeoplezfzh);
		
		String qrzw = wsData.get(0).get("surveypeopleunit")
				.toString();
		text = text.replace("{QRZW}", qrzw);
		
		String AskedPeopleName = wsData.get(0).get("checkpeople")
				.toString();
		text = text.replace("{AskedPeopleName}", AskedPeopleName);
		
		
		
		// ���"����鵥λ" �� "��ַ��"
		String dutypeopledepartment = wsData.get(0).get("dutypeopledepartment")
				.toString();

		text = text.replace("{DutyPeopleDepartment}", dutypeopledepartment);
//		String dutypeopleaddress = wsData.get(0).get("dutypeopleaddress")
//				.toString();
//		text = text.replace("{dutypeopleaddress}", dutypeopleaddress);

		// ���"�ֳ�������������ְ��" �� "�绰��"��"�뱾����ϵ"
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
		// ���"����������������ְ��" �� "�绰��"��"�ʱࣺ"
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
		// ���"Ӫҵִ��ע���" �� "��֯�������룺"
//		String yyzzzch = wsData.get(0).get("yyzzzch").toString();
//		String zzjgdm = wsData.get(0).get("zzjgdm").toString();
//		text = text.replace("{yyzzzch}", yyzzzch);
//		text = text.replace("{zzjgdm}", zzjgdm);
		// ���"�����μ���������������λ(��ַ)��"
		String OtherPeopleAddressStr = wsData.get(0).get("otherpeopleaddress")
				.toString();
		text = text.replace("{OtherPeopleAddress}", OtherPeopleAddressStr);
		// ���"ִ����Ա"
//		String qrzw = wsData.get(0).get("qrzw").toString();
//		text = text.replace("{qrzw}", question1(""));
		// ���"�ֳ���飨���죩���"

		
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
			sb.append("<tr><td align=\"left\" width=\"800px\" style=\"font-size:20px;line-height:30px; \" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(���¿հ�)</td></tr>");
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
					+ (k == lines.size() - 1 ? "<br/>(���¿հ�)" : "")
					+ "</td></tr>");
			n++;
			if (n == limit) {
				Log.i("info", "��ҳ");
				if (k != lines.size() - 1)
					sb.append("<tr><td width=\"880px\" style=\"font-size:20px\">(����ҳ)</td></tr>");
				if (page == 1) {
					size -= firstpageLimit;
					text = text.replace("{sitecondition}", sb.toString());
					text = text.replace("{currentpage}", "1");
				} else {
					size -= pageLimit;
					String s = "<div  style=\"height: 25cm;\">"
							+ "<table style=\" margin: 5px auto; margin-top: 70px\">"
							+ "<tr><td><font style=\"line-height:30px;\">(����ҳ)</font></td></tr>"
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
							+ "<tr><td><font style=\"line-height:30px;\">(����ҳ)</font></td></tr>"
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
	 * @description ��һ���ַ���ת��Ϊ�������,���ϵ�Ԫ��Ϊһ��<br/>ÿ��Ϊ42�������ֵĳ���,<font color="red">��Ҫ������Ӣ�ļ���㳤�ȵ�����</font>
	 * @return ArrayList<String>
	 * @date 2016-10-27
	 * @param siteConditionStr2 ��Ҫ���е��ַ���
	 * */
	private ArrayList<String> toLinesFromString(String siteConditionStr2) {
		ArrayList<String> lines = new ArrayList<String>();
		if (TextUtils.isEmpty(siteConditionStr2)) {
			return lines;
		}
		String wtnrStr = siteConditionStr2.replace("<br/>","�m").replace("\n","�m").replace("\r\n","�m");
		//�г��ȶ�ֵ
		int lineLength = 44;
		//�г��ȶ�ֵ,Ĭ��Ϊ42�������ֵĳ���,84��
		int lineSpaces = 88;
		//ָ��
		int pos = 0;
		int stringLength = wtnrStr.length();
		//ƥ�����ļ����ı��
		String regEx3 = "[\u3000\u00A0\u0020\u4e00-\u9fa5\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b\uff01\uff20\u0021\u0040\u003f]";
		//ƥ������
		String regEx="[\u4e00-\u9fa5]";
		//ƥ��ո�
		String regEx5="[\u3000\u00A0\u0020]";
		//ƥ�����ı��
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
			//�ж��Ƿ񳬳�,��ֹԽ��
			index = (index >= stringLength ? stringLength : index);
			String line1 = wtnrStr.substring(pos,index);
			line = line + line1;
			if (line.contains("�m")) {
				int posi = line.indexOf("�m")+1;
				if (posi > stringLength) {
					//ƥ��ո�
					String regEx4="[\u3000\u00A0\u0020]";
					Pattern pattern4 = Pattern.compile(regEx4); 
					Matcher matcher4 = pattern4.matcher(line);
					line = matcher4.replaceAll("&nbsp;");
					lines.add(line.replace("�m", ""));
					pos = stringLength;
					continue;
				}else {
					line = line.substring(0, posi).replace("�m", "");
					//ƥ��ո�
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
			//ȥ��ƥ�䵽���ַ�
            String line4 = matcher.replaceAll("");
            matcher5 = pattern5.matcher(line);
            String kong = matcher5.replaceAll("");
			l=(line4.length()+(line.length()-line4.length())*2)+ (line.length() - kong.length())/4;
			//ѭ��ƴ��,ֱ������Ϊ84��
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
	            //�ж��Ƿ񳬳�,��ֹԽ��
				index2 = (index2 >= stringLength ? stringLength : index2);
				String exString = wtnrStr.substring(pos, index2);
				line = line + exString;
				if (line.contains("�m")) {
					int posi = line.indexOf("�m")+1;
					if (posi > stringLength) {
						lines.add(line.replace("�m", ""));
						pos = stringLength;
						continue;
					}else {
						lines.add(line.substring(0, posi).replace("�m", ""));
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
			//ƥ��ո�
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
	 * Description:дHTML�ļ�
	 * 
	 * @author Administrator<br>
	 *         Create at: 2013-1-28 ����03:46:29
	 */
	private Boolean writeFile(String htmlInfo, String filePath) {
		Boolean result = false;
		File current_task_dir = new File(filePath);
		if (!current_task_dir.exists()) {
			current_task_dir.mkdirs();
		}
		try {
			File f = new File(filePath + "/" + "��" + 1 + "ҳ" + ".html");

			if (!f.exists()) {
				f.createNewFile();// �������򴴽�
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
	 * ƴ"��1"
	 * 
	 * 
	 */
	private String question() {
		String a = " <tr ><td width=\"880px\" style=\"text-indent:2em;\">" + "<font style=\"font-weight:bold;font-size:20px\"></font>"
				+ "<font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "���죬�����������м�鲢�˽��й��������Ӧ����ϵ��飬��ʵ�ش�ѯ�ʺ��ṩ���ϣ����þܾ����谭�����ػ����ṩ���������������Ϊ�����뱾����������ϵ������Ӱ�칫���참�����������ǻرܣ���˵�����ɡ�</font>" + "  </td></tr>";
		return a;
	}

	/**
	 * ƴ"��1"
	 * 
	 * 
	 */
	private String answer() {
		String a = " <tr ><td width=\"880px\">"
				+ "<font style=\"font-weight:bold;font-size:20px\"></font><font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "�ʣ�������ִ����Ա�ر���</font>" + "  </td></tr>";
		String b = " <tr ><td width=\"880px\">"
				+ "<font style=\"font-weight:bold;font-size:20px\"></font><font style=\"word-break: break-all; word-wrap:break-word;line-height:35px;font-size:20px;\">"
				+ "�������,����Ҫ�رܡ�"  + "</font>" + "  </td></tr>";
		return a+b;
	}

	
	
	
	
}
