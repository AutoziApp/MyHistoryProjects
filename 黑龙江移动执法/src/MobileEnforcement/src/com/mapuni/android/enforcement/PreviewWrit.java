package com.mapuni.android.enforcement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.SqliteUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class PreviewWrit extends Activity {
	private WebView wView;
	
	private YutuLoading yutuLoading;
	private String RWBH;
	private String QYDM;
	private Intent intent;
	private final String KCBL_TABLENAME="T_ZFWS_KCBL";
	/** ���htmlҳ��Դ��*/
	private StringBuffer htmlSb=new StringBuffer();
	/**��ѯ���ı�¼����*/
	ArrayList<HashMap<String, Object>> wsData;
	
	/** û�д��������ź���ҵ����*/
	private final int NO_RWBH_QYDM=0;
	/** ���ݿ��ѯ����*/
	private final int DATABASEERR=1;
	/** ���ݿ�������*/
	private final int NODATA=2;
	/** ����html�ɹ�*/
	private final int SUCCESS=3;
	
	private String path=Global.SDCARD_RASK_DATA_PATH+"ImgDoc/";
	private Calendar calendar=Calendar.getInstance();
	
	protected Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NO_RWBH_QYDM:
				if(yutuLoading!=null){
					Toast.makeText(PreviewWrit.this, "��������쳣",Toast.LENGTH_SHORT ).show();
					finish();
				}
				break;
			case DATABASEERR:
				if(yutuLoading!=null){
					Toast.makeText(PreviewWrit.this, "���ݿ��ѯ�쳣",Toast.LENGTH_SHORT ).show();
					finish();
				}
			case NODATA:
				if(yutuLoading!=null){
					Toast.makeText(PreviewWrit.this, "���ݿ�������",Toast.LENGTH_SHORT ).show();
					finish();
				}	
				break;
			case SUCCESS:
				if(yutuLoading!=null){
					yutuLoading.dismissDialog();
				}
				wView.loadUrl("file://"+path+"/��1ҳ.html");
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapuni_html_main);
		wView = (WebView) findViewById(R.id.webview);
		/** ֧��webҳ������� */
		wView.getSettings().setBuiltInZoomControls(true);
		/** ֧��javaScript�ű� */
		wView.getSettings().setJavaScriptEnabled(true);
		wView.getSettings().setAllowFileAccess(true);
		wView.getSettings().setUseWideViewPort(true);
		wView.getSettings().setLoadWithOverviewMode(true);
		/** �õ��ϸ����洫�ݵ����� */
		intent = this.getIntent();
		yutuLoading=new YutuLoading(this);
		yutuLoading.setLoadMsg("���ڼ���", "");
		yutuLoading.showDialog();
		new MyThread().start();
		
	}
	
	private class MyThread extends Thread{
		@Override
		public void run() {
			RWBH=intent.getStringExtra("rwbh");
			QYDM=intent.getStringExtra("qydm");
			if(RWBH ==null || QYDM==null){
				handler.sendEmptyMessage(NO_RWBH_QYDM);
				this.destroy();
			}
			path=path+RWBH+"/"+QYDM;
			String sql="SELECT * from "+KCBL_TABLENAME+" where TaskId='"+RWBH+"' and EntCode='"+QYDM+"'";
			wsData=SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			if(wsData==null){//�ж����ݿ��Ƿ��б������
				handler.sendEmptyMessage(DATABASEERR);
				return;
			}else{
				if(wsData.size()==0){
					handler.sendEmptyMessage(NODATA);
					return;
				}
			}
			//�����ݣ���ʼƴдhtml
			startSpellHTML();
			if(writeFile(htmlSb.toString(), path)){
				handler.sendEmptyMessage(SUCCESS);
			}else{
				
			}
			
			
		}

	}

	private void startSpellHTML() {
		htmlSb.append(spellHead());
		htmlSb.append(spellStyle());
		htmlSb.append(spellBodyTitle());
		htmlSb.append(spellBodyBDName());
		htmlSb.append(spellTime());
		htmlSb.append(spellPlace());
		htmlSb.append(spellSurveyPeople());
		htmlSb.append(spellRecordPeople());
		htmlSb.append(spellbeCheckedPeople());
		htmlSb.append(spellLawPerson());
		htmlSb.append(spellDutyPeople());
		htmlSb.append(spellOtherPeopleAddress());
		htmlSb.append(spellSiteCondition());
		htmlSb.append(spellDutyPeopleAutograph());
		htmlSb.append(spellSurveyPeopleAutograph());
		htmlSb.append(spellRecordPeopleAutograph());
		htmlSb.append(spellOtherPeopleAutograph());
		htmlSb.append(spellHtmlBottom());
	}
	
	/**
	 * html ͷ
	 * @return
	 */
	private String spellHead(){
		String head="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+"<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
				"<head runat=\"server\">"+
				"<title></title>";
		return head;
	}
	/**
	 * html ��ʽ
	 * @return
	 */
	private String spellStyle(){
		String Style="<style type=\"text/css\">" +
				".divedit"+
				"{ width: 600px;line-height: 30px; background-image: url(../../Images/line.png);" +
				" background-position: top;  background-repeat: repeat;  vertical-align: middle; " +
				"word-spacing: 5px; font-size: 14pt; font-family: ����; "+
				"word-break:break-all;}"+
				"body{margin: auto;color: #000;}"+
				" tr td{ height: 22pt; color: #000; font-size: 14pt; font-family: ����;}"+
				"span.left{ text-align: left;text-decoration: underline; border: 1px solid #ff0000;width: 98%;display: block;border-bottom: 1px solid #000;color: #000;}"+
				"span {text-align: left;float: left;color: #000;font-size: 14pt;font-family: ����; }"+
				"span.lbl60 {width: 60px;float: left; color: #000;}"+
				"span.lbl80{ width: 80px;float: left;color: #000;}"+
				"span.lbl128{width: 128px;float: left;color: #000;}"+
				"span.content{width: 54px;text-align: center; float: left; border-bottom: 1px solid #000; color: #000;}"+
				"span.content120{width: 120px;float: left;border-bottom: 1px solid #000; height: 20px;color: #000;}"+
				"</style>"+
				"</head>";
		return Style;
	}
	/**
	 * html bodyͷ
	 * @return
	 */
	private String spellBodyTitle(){
     String BodyTitle="<body>"+
	  "<form id=\"form1\" runat=\"server\">"+
    	"<div id=\"msg\"> </div>"+
	  "<div id=\"printArea\" style=\"height: 100%; overflow: auto;\">";
      return BodyTitle;
		
	}
	private String spellBodyBDName(){
		String BodyBDName="<table style=\"width: 630px; margin: 5px auto; margin-top: 15px\">"+
	       "<tr style=\"height: 45px;\">"+
		   " <td style=\"text-align: center; font-size: 20px; font-weight: bold\">"+
	       "����л���������<br />"+
		   "�ֳ���飨���죩��¼<br />"+
	       "<hr style=\"width: 600px; float: left; border: 1px solid #000\" />"+
		   " </td>"+
	       "</tr>";
		return BodyBDName;
	}
	/**
	 * ƴ��ʼʱ��ͽ���ʱ��
	 * @return
	 */
	private String spellTime(){
		String startTime=wsData.get(0).get("surveystartdate").toString();
		String startTime_Year="&nbsp;";
		String startTime_Month="&nbsp;";
		String startTime_Day="&nbsp;";
		String startTime_Hours="&nbsp;";
		String startTime_Minutes="&nbsp;";
		
		if(!startTime.equals("")){
			Date startDate=DisplayUitl.ParseDate(startTime);
			calendar.setTime(startDate);
			startTime_Year=(calendar.get(Calendar.YEAR))+"";
			startTime_Month=(calendar.get(Calendar.MONTH)+1)+"";
			startTime_Day=(calendar.get(Calendar.DAY_OF_MONTH))+"";
			startTime_Hours=(calendar.get(Calendar.HOUR_OF_DAY))+"";
			startTime_Minutes=(calendar.get(Calendar.MINUTE))+"";
		}
		
		
		String endTime=wsData.get(0).get("surveyenddate").toString();
		String endTime_Hours="&nbsp;";
		String endTime_Minutes="&nbsp;";
		if(!endTime.equals("")){
			Date endDate=DisplayUitl.ParseDate(endTime);
			endTime_Hours=endDate.getHours()+"";
			endTime_Minutes=endDate.getMinutes()+"";
			
		}
		
		
		String timeStr=" <tr>"+
		"<td>"+
		" <span style=\"width: 60px;\">ʱ�䣺</span> <span class=\"content\" style=\"width: 55px\" id=\"spanyear\""+
		" runat=\"server\">"+startTime_Year+"</span><span>��</span> <span class=\"content\" id=\"spanmonth\""+
		" runat=\"server\">"+startTime_Month+"</span> <span>��</span> <span class=\"content\" id=\"spanday\" runat=\"server\">"+
		" "+startTime_Day+"</span> <span>��</span> <span class=\"content\" id=\"spantime\" runat=\"server\">"+startTime_Hours+"</span>"+
		"<span>ʱ</span> <span class=\"content\" id=\"spanfen\" runat=\"server\">"+startTime_Minutes+"</span> <span>"+
		"��&nbsp; ��</span> <span class=\"content\" id=\"endtime\"  runat=\"server\">"+
		""+endTime_Hours+"</span> <span>ʱ</span> <span class=\"content\" id=\"endfen\""+
		"  runat=\"server\">"+endTime_Minutes+"</span> <span>��</span>"+
		"</td>"+
		"</tr>";
		return timeStr;
		
		
				
		
	}
	/**ƴ��ҵ�ص�
	 * 
	 * @return
	 */
	private String spellPlace(){
		String place=wsData.get(0).get("surveryaddress").toString();
		String surveryaddress=
	    "<tr>"+
		"<td>"+
		" <span style=\"width: 60px;\">�ص㣺</span><span id=\"address\" class=\"content120\" "	+
		"runat=\"server\" style=\"width: 540px\">" +place+
		"</span>"+
		" </td>"+
		"</tr>";
		return surveryaddress;
		
		
	}
	/**
	 * ƴ�����
	 * @return
	 */
/*	private String spellSurveyPeople(){
		String SurveyPeople1=wsData.get(0).get("surveypeoplename").toString();
		String SurveyPeopleCradCode1=wsData.get(0).get("surveypeoplecradcode").toString();
		String SurveyPeopleUnit1=wsData.get(0).get("surveypeopleunit").toString();
		
		String SurveyPeople2=wsData.get(0).get("surverypeople2name").toString();
		String SurveyPeopleCradCode2=wsData.get(0).get("surverypeople2cradcode").toString();
		String SurveyPeopleUnit2=wsData.get(0).get("surverypeople2unit").toString();
		
		String SurveyPeopleStr=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 155px;\">��飨���죩�ˣ�</span><span class=\"content120\""	+
		"style=\"width: 75px;\" id=\"KCR\" runat=\"server\"> "+SurveyPeople1+"</span> <span style=\"width: 110px;\">ִ��֤�ţ�</span><span"+
		"class=\"content120\" style=\"width: 65px; color: #000000\" id=\"KCRNum\""+
		"runat=\"server\">"+SurveyPeopleCradCode1+" </span> <span style=\"width: 100px;\">������λ��</span><span class=\"content120\""+
		"style=\"width: 95px\" id=\"KCRGZDW\"  runat=\"server\">"+SurveyPeopleUnit1+"</span>"+
		" </td>"+
		" </tr>"+
		"<tr>"+
		" <td>"+
		" <span style=\"width: 155px;\">��飨���죩�ˣ�</span><span class=\"content120\" "+
		" style=\"width: 75px;\" id=\"KCR2\" runat=\"server\">"+SurveyPeople2+"</span> <span style=\"width: 110px;\">ִ��֤�ţ�</span><span"+
		" class=\"content120\" style=\"width: 65px; color: #000000\" id=\"KCR2NUM\" "+
		" runat=\"server\">"+SurveyPeopleCradCode2+"</span> <span style=\"width: 100px;\">������λ��</span><span class=\"content120\""+
		"style=\"width: 95px\" id=\"KCR2GZDW\"  runat=\"server\">"+SurveyPeopleUnit2+"</span>"+
		"</td>"+
		"</tr>";
		
		return SurveyPeopleStr;
	}*/
	/**
	 * ƴ����� ������
	 * @return
	 */
	private String spellSurveyPeople(){
		String SurveyPeople1=wsData.get(0).get("surveypeoplename").toString();
		String SurveyPeopleCradCode1=wsData.get(0).get("surveypeoplecradcode").toString();
		String SurveyPeopleUnit1=wsData.get(0).get("surveypeopleunit").toString();
		
		String SurveyPeople2=wsData.get(0).get("surverypeople2name").toString();
		String SurveyPeopleCradCode2=wsData.get(0).get("surverypeople2cradcode").toString();
		String SurveyPeopleUnit2=wsData.get(0).get("surverypeople2unit").toString();
		
		String SurveyPeopleStr=
		"<tr>"+
		"<td>"+	
		"<span style=\"width: 155px;\">��飨���죩�ˣ�</span><span class=\"content120\" "+
		"style=\"width: 70px;\" id=\"KCR\" runat=\"server\">"+SurveyPeople1+"</span> <span style=\"width: 190px;\">������λ��ִ��֤�ţ�</span>"+
		"<span class=\"content120\" style=\"width: 185px\" id=\"KCRGZDW\" "+
		" runat=\"server\"> "+SurveyPeopleUnit1+" "+SurveyPeopleCradCode1+"</span>"+
		"</td>"+
		" </tr>"+
		"<tr>"+
		"<td>"+	
		"<span style=\"width: 155px;\">��飨���죩�ˣ�</span><span class=\"content120\" "+
		"style=\"width: 70px;\" id=\"KCR\" runat=\"server\">"+SurveyPeople2+"</span> <span style=\"width: 190px;\">������λ��ִ��֤�ţ�</span>"+
		"<span class=\"content120\" style=\"width: 185px\" id=\"KCRGZDW\" "+
		" runat=\"server\"> "+SurveyPeopleUnit2+" "+SurveyPeopleCradCode2+"</span>"+
		"</td>"+
		" </tr>";
		return SurveyPeopleStr;
	}
	/**
	 * ƴ��¼��
	 * @return
	 */
	/*private String spellRecordPeople(){
		
		String recordpeoplename=wsData.get(0).get("recordpeoplename").toString();
		String  RecPeopleCradCode=wsData.get(0).get("recpeoplecradcode").toString();
		String  RecordPeopleUnit= wsData.get(0).get("recordpeopleunit").toString();
		
		String RecordPeople=
		"<tr>"+
		" <td>"+
		" <span style=\"width: 80px;\">��¼��������</span><span class=\"content120\" "	+
		"style=\"width: 150px;\" id=\"JLR\" runat=\"server\">"+recordpeoplename+"</span> <span style=\"width: 110px;\">ִ��֤�ţ�</span><span"+
		"class=\"content120\" style=\"width: 65px; color: #000000\" id=\"NUM1\" "+
		"runat=\"server\">"+RecPeopleCradCode+"</span> <span style=\"width: 100px;\">������λ��</span><span class=\"content120\""+
		"style=\"width: 95px\" id=\"GZDW1\"  runat=\"server\">"+RecordPeopleUnit+"</span>"+
		"</td>"+
		"</tr>";		
		return RecordPeople;
	}*/
	/**
	 * ƴ��¼�� �޸ĺ�
	 * @return
	 */
   private String spellRecordPeople(){
		
		String recordpeoplename=wsData.get(0).get("recordpeoplename").toString();
		String  RecPeopleCradCode=wsData.get(0).get("recpeoplecradcode").toString();
		String  RecordPeopleUnit= wsData.get(0).get("recordpeopleunit").toString();
		
		String RecordPeople=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 80px;\">��¼�ˣ�</span><span class=\"content120\" "+
		"style=\"width: 145px;\" id=\"JLR\" runat=\"server\">"+recordpeoplename+"</span> <span style=\"width: 190px;\">������λ��ִ��֤�ţ�</span>"+
		"<span class=\"content120\" style=\"width: 187px\" id=\"GZDW1\"  runat=\"server\">"+
		""+RecordPeopleUnit+" "+RecPeopleCradCode+"</span>"+
		"</td>"+
		"</tr>";
		return RecordPeople;
	}
	/**
	 * ƴ����������� ʵ��Ϊ��ҵ����
	 * @return
	 */
	private String spellbeCheckedPeople(){
		
		String beCheckedPeopleName=wsData.get(0).get("entprisename").toString();
		String beCheckedPeople=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 200px;\">����������ƻ�������</span><span class=\"content120\" style=\"width: 400px\""+
		"id=\"QYMC\" runat=\"server\">"+beCheckedPeopleName+"</span>"+
		"</td>"+
		"</tr>";
		return beCheckedPeople;
		
	}
	/**
	 * ƴ���˴���
	 * @return
	 */
	private String spellLawPerson(){
		String LawPersonName=wsData.get(0).get("checkpeople").toString();
		String LawPerson=
		" <tr>"	+
		"<td>"+
		"<span style=\"width: 255px;\">���������ˣ������ˣ�������</span><span class=\"content120\" style=\"width: 345px\""+
		" id=\"DBR\"  runat=\"server\">"+LawPersonName+"</span>"+
		" </td>"+
		" </tr>";
		return LawPerson;
	}
	
	/**
	 * ƴ�ֳ�������
	 * @return
	 */
	private String spellDutyPeople(){
		String DutyPeopleName=wsData.get(0).get("dutypeople").toString();
		String DutyPeopleAge=wsData.get(0).get("dutypeopleage").toString();
		String DutyPeopleCode=wsData.get(0).get("dutypeoplecode").toString();
		String DutyPeopleDepartment=wsData.get(0).get("dutypeopledepartment").toString();
		String DutyPeopleOffice=wsData.get(0).get("dutypeopleoffice").toString();
		String DutyPeopleRelation=wsData.get(0).get("dutypeoplerelation").toString();
		String DutyPeopleAddress=wsData.get(0).get("dutypeopleaddress").toString();
		String DutyPeopleTel=wsData.get(0).get("dutypeopletel").toString();
		String DutyPeopleYZBM=wsData.get(0).get("dutypeopleyzbm").toString();


		String DutyPeople=
		"<tr>"	+
		"<td>"+
		" <span style=\"width: 155px;\">�ֳ�������������</span> <span class=\"content120\" "+
		" style=\"width: 75px\" id=\"FZR\" runat=\"server\">"+DutyPeopleName+"</span><span style=\"\">���䣺</span>"+
		" <span id=\"AGE\" class=\"content120\"  runat=\"server\" style=\"width: 20px\">"+
		" "+DutyPeopleAge+"</span><span style=\"width: 140px;\">������ݺ��룺</span><span class=\"content120\" style=\"width: 155px\""+
		"id=\"ZJH\"  runat=\"server\">"+DutyPeopleCode+"</span>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+
		"<span style=\"width: 100px;\">������λ��</span><span class=\"content120\" "+
		"style=\"width: 100px\" id=\"GZDW2\" runat=\"server\">"+DutyPeopleDepartment+"</span> <span style=\"width: 65px;\">"+
		" ְ��</span><span class=\"content120\" style=\"width: 120px\" id=\"ZW\" "+
		" runat=\"server\">"+DutyPeopleOffice+"</span> <span style=\"width: 120px;\">�뱾����ϵ��</span><span class=\"content120\""+
		" style=\"width: 96px\" id=\"BAGX\" runat=\"server\">"+DutyPeopleRelation+"</span>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+
		"<span style=\"width: 65px;\">��ַ��</span><span class=\"content120\" style=\"width: 240px\""+
		"id=\"DZ\"  runat=\"server\">"+DutyPeopleAddress+"</span> <span style=\"width: 65px;\">"+
		"�绰��</span><span class=\"content120\" style=\"width: 100px\" id=\"Phone\" "+
		" runat=\"server\">"+DutyPeopleTel+"</span><span style=\"width: 65px;\"> �ʱࣺ</span><span class=\"content120\""+
		"style=\"width: 65px\" id=\"YZBM\"  runat=\"server\">"+DutyPeopleYZBM+"</span>"+
		"</td>"+
		"</tr>";
		return DutyPeople;
	}
	/**
	 * ƴ�����μ���Ա�����Լ�������λ
	 * @return
	 */
	private String spellOtherPeopleAddress(){
		
		String OtherPeopleAddressStr=wsData.get(0).get("otherpeopleaddress").toString();
		String OtherPeopleAddress=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 250px;\">�����μ���������������λ��</span>"+
		"</td>"+
		"</tr>"+
		" <tr>"+
		"<td>"+
		" <div class=\"divedit\"  style=\"width: 600px;\" id=\"OtherPeopleAddress\""+
		"runat=\"server\">&nbsp;&nbsp;<u>"+OtherPeopleAddressStr+
		" </u></div>"+
		"</td>"+
		"</tr>";
                   
		return OtherPeopleAddress;
	}
	/**
	 * ƴ�ֳ����
	 * @return
	 */
	private String spellSiteCondition(){
		String SiteConditionStr=wsData.get(0).get("sitecondition").toString();
		String SiteCondition=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 200px\">�ֳ������</span>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+
		"<div class=\"divedit\"  style=\"width: 600px;\" id=\"XCQK\" runat=\"server\">"+
		"&nbsp;&nbsp;<u>"+SiteConditionStr+"</u>"+
		"</div>"+
		"</td>"+
		"</tr>"+
		"<tr>"+
		"<td>"+
		"���ϱ�¼��������"+
		"</td>"+
		"</tr>";		
		return SiteCondition;
	}
	/**
	 * ƴ�ֳ�������ǩ��
	 * @return
	 */
	private String spellDutyPeopleAutograph(){
		String DutyPeopleAutograph=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 155px;\">�ֳ�������ǩ����</span><span class=\"content120\" style=\"width: 185px\""+
		"id=\"data1\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data2\" runat=\"server\">"+
		"&nbsp;</span> <span style=\"width: 10px;\">��</span> <span class=\"content120\" style=\"width: 60px;"+
		"text-align: center\" id=\"data3\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data4\" runat=\"server\">"+
		"&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"</td>"+
		"</tr>";	
		return DutyPeopleAutograph;
	}
	/**
	 * ƴ�����ǩ��
	 * @return
	 */
	private String spellSurveyPeopleAutograph(){
		String SurveyPeopleAutograph=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 190px;\">��飨���죩��ǩ����</span><span class=\"content120\" style=\"width: 150px\""+
		"id=\"data5\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data6\" runat=\"server\">"+
		"&nbsp;</span> <span style=\"width: 10px;\">��</span> <span class=\"content120\" style=\"width: 60px;"+
		"text-align: center\" id=\"data7\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data8\" runat=\"server\">"+
		"&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"</td>"+
		"</tr>";	
		return SurveyPeopleAutograph;
	}
	/**
	 * ƴ��¼��ǩ��
	 * @return
	 */
	private String spellRecordPeopleAutograph(){
		String RecordPeopleAutograph=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 120px;\">��¼��ǩ����</span><span class=\"content120\" style=\"width: 220px\""+
		"id=\"data9\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data10\" runat=\"server\">"+
		" &nbsp;</span> <span style=\"width: 10px;\">��</span> <span class=\"content120\" style=\"width: 60px;"+
		" text-align: center\" id=\"data11\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data12\" runat=\"server\">"+
		"&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"</td>"+
		"</tr>";	
		return RecordPeopleAutograph;
	}
	/**
	 * ƴ�����μ���Աǩ��
	 * @return
	 */
	private String spellOtherPeopleAutograph(){
		String OtherPeopleAutograph=
		"<tr>"+
		"<td>"+
		"<span style=\"width: 150px;\">�����μ���ǩ����</span><span class=\"content120\" style=\"width: 190px\""+
		"id=\"data13\" runat=\"server\">&nbsp;</span><span style=\"width: 50px;\">&nbsp;</span>"+
		"<span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data14\" runat=\"server\">"+
		" &nbsp;</span> <span style=\"width: 10px;\">��</span> <span class=\"content120\" style=\"width: 60px;"+
		"text-align: center\" id=\"data15\" runat=\"server\">&nbsp;</span><span style=\"width: 10px;\">��</span>"+
		" <span class=\"content120\" style=\"width: 60px; text-align: center\" id=\"data16\" runat=\"server\">"+
		" &nbsp;</span><span style=\"width: 10px;\">��</span>"+
		"</td>"+
		"</tr>";	
		return OtherPeopleAutograph;
	}
	/**
	 * ƴhtml bottom
	 * @return
	 */
	private String spellHtmlBottom(){
		String HtmlBottom=
		"<tr>"+
		"<td>"+
		"<hr style=\"width: 600px; float: left; border: 1px solid #000\" />"+
		"</td>"+
		"</tr>"+
		"</table>"+
		"</div>"+
		"<div style=\"height: 30px; padding-left: 10px; float: right; margin-top: 20px; margin-right: 30px\""+
		"id=\"print\" runat=\"server\">"+
		" </div>"+
		"</form>"+
		"</body>"+
		"</html>";
		return HtmlBottom;
	}
       /**
		 * Description:дHTML�ļ�
		 * 
		 * @author Administrator<br>
		 * Create at: 2013-1-28 ����03:46:29
		 */
		private Boolean writeFile(String htmlInfo, String filePath) {
			Boolean result=false;
			File current_task_dir = new File(filePath);
			if (!current_task_dir.exists()) {
				current_task_dir.mkdirs();
			}
			try {
				File f = new File(filePath+"/" +"��"+ 1+"ҳ" + ".html");
				
				if (!f.exists()) {
					f.createNewFile();// �������򴴽�
				}
				BufferedWriter output = new BufferedWriter(new FileWriter(f));
				output.write(htmlInfo);
				output.close();
				result=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	
}
