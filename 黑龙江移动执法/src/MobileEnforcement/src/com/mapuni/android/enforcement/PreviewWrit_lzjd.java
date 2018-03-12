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

public class PreviewWrit_lzjd {
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
//				 wView.loadUrl("file://" + path + "/第1页.html");
				break;
			default:
				break;
			}
		};
	};
	private String bh;
	private String sjdr;
	private String tksj;
	private String lxfs;
	private String txdz;
	private String sjbm;
	private String yzbm;
	private String jdtsdh;

	public PreviewWrit_lzjd(String rWBH, String qyid,String guid, String tYPE, Activity a) {
		super();
		RWBH = rWBH;
		GUID = qyid;
		TYPE = tYPE;
		this.guid = guid;
		activity = a;
	}
	
	public void setVaues(String bh,String sjdr,String tksj,String lxfs,String txdz,String sjbm,String yzbm,String jdtsdh ){
		
		this.bh=bh;
		this.sjdr=sjdr;
		this.tksj=tksj;
		this.lxfs=lxfs;
		this.txdz=txdz;
		this.sjbm=sjbm;
		this.yzbm=yzbm;
		this.jdtsdh=jdtsdh;
		
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
		String sql = "select * from Supervision where billcode = '" + guid
				+ "'";

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
			InputStream is = manager.open("lzjd.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			String text = new String(buffer);
			text = pushResult(text);

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

	//	[{address=, billcode=5987fb00-c5c5-41c5-a096-d834c354d185, complainttel=, 
		//phone=, makeouttime=, createdby=0, receivingdepartment=, updatedby=,
		//id=45, supervisedpeople=, updatedtime=, postcode=, createdtime=, supervisenumber=}]
		
//		String address = wsData.get(0).get("address").toString();
//		String supervisenumber = wsData.get(0).get("supervisenumber").toString();
//		String phone = wsData.get(0).get("phone").toString();
//		String postcode = wsData.get(0).get("postcode").toString();
//		String supervisedpeople = wsData.get(0).get("supervisedpeople").toString();
//		String receivingdepartment = wsData.get(0).get("receivingdepartment").toString();
//		String complainttel = wsData.get(0).get("complainttel").toString();
//		String makeouttime = wsData.get(0).get("makeouttime").toString();
//		String[] split = makeouttime.split("T");
		
		
		String address = txdz;
		String supervisenumber = bh;
		String phone = lxfs;
		String postcode = yzbm;
		String supervisedpeople = sjdr;
		String receivingdepartment = sjbm;
		String complainttel = jdtsdh;
		String makeouttime = tksj;
		
		text = text.replace("{Address}",address);
		text = text.replace("{SuperviseNumber}",supervisenumber);
		//text = text.replace("{MakeOutTime}",split[0]);
		text = text.replace("{MakeOutTime}",makeouttime);
		text = text.replace("{SupervisedPeople}",supervisedpeople);
		text = text.replace("{AskedPeopleName}",phone);
		text = text.replace("{ReceivingDepartment}",receivingdepartment);
		text = text.replace("{PostCode}",postcode);
		text = text.replace("{ComplaintTel}",complainttel);

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
		//	File f = new File(filePath + "/" + "第" + 1 + "页" + ".html");
			File f = new File(filePath + "/第1页.html");
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
