package com.mapuni.android.attachment;

public class T_Attachment {

	/** 任务下发 */
	public static int RWXF = 1;
	/** 任务执行 */
	public static int RWZX = 2;
	/** 笔录扫描件 */
	public static int BLSMJ = 3;
	/** 企业材料 */
	public static int QYCL = 4;
	/** 任务反馈 */
	public static int RWFK = 5;
	/** 工艺流程 */
	public static int GYLC = 10;
	/** 建设项目 */
	public static int JSXM = 11;
	/** 历史监察记录 */
	public static int LSJCJL = 12;
	/** 法律法规 */
	public static int FLFG = 13;
	/** 环保标准 */
	public static int HBBZ = 14;
	/** 制度文件 */
	public static int ZDWJ = 15;
	/** 危险物品 */
	public static int WXWP = 16;
	/** 应急预案 */
	public static int YJYA = 17;
	/** 专家库 */
	public static int ZJK = 18;
	/** 周报（暂时没用） */
	public static int ZB = 19;
	/** 上市企业核查 */
	public static int SSQYHC = 20;
	/** 暂时没用 */
	public static int JCQX = 21;
	/** 任务回复 */
	public static int RWHF = 22;
	// / <summary>
	// / 营业执照 企业基本信息
	// / </summary>
	public static int YYZZ = 31;
	// / <summary>
	// / 组织机构 企业基本信息
	// / </summary>
	public static int ZZJGDMTP = 32;
	// / <summary>
	// / 项目审批电子档案 环保手续履行情况
	// / </summary>
	public static int XMSPDZDA = 33;
	// / <summary>
	// / 试生产审批电子档案 环保手续履行情况
	// / </summary>
	public static int SSCSPDZDA = 34;
	// / <summary>
	// / 环保验收电子档案 环保手续履行情况
	// / </summary>
	public static int HBYSDZDA = 35;
	// / <summary>
	// / 生产设施运行情况_生产工艺图 污染治理设施物
	// / </summary>
	public static int SCSSYXQK_SCGYT = 36;
	// / <summary>
	// / 废水治理设施运行情况_工艺图 污染治理设施物
	// / </summary>
	public static int FSZLSSYXQK_GYT = 37;
	// / <summary>
	// / 废气治理设施运行情况_工艺电子档案 污染治理设施物
	// / </summary>
	public static int FQZLSSYXQK_GYDZDA = 38;
	// / <summary>
	// / 废水排污口情况_设备 排污口
	// / </summary>
	public static int FSPWKQK_SB = 39;
	// / <summary>
	// / 废水排污口情况_运行情况 排污口
	// / </summary>
	public static int FSPWKQK_YXQK = 40;
	// / <summary>
	// / 废水排污口情况_排放口情况 排污口
	// / </summary>
	public static int FSPWKQK_PFKQK = 41;
	// / <summary>
	// / 废气排污口情况_设备 排污口
	// / </summary>
	public static int FQPWKQK_SB = 42;
	// / <summary>
	// / 废气排污口情况_运行情况 排污口
	// / </summary>
	public static int FQPWKQK_YXQK = 43;
	// / <summary>
	// / 废气排污口情况_排放口情况 排污口
	// / </summary>
	public static int FQPWKQK_PFKQK = 44;
	// / <summary>
	// / 历史执法情况_存在问题
	// / </summary>
	public static int LSZFQK_CZWT = 45;
	// / <summary>
	// / 历史执法情况_处理结果
	// / </summary>
	public static int LSZFQK_CLJG = 46;
	/** 公告附件 */
	public static int TZTG = 48;
	// <summary>
    /// 环保手续履行情况_工艺图
    /// </summary>
	public static int HBSXLXQK_GYT=47;
	
	public static int PWXKZFJ = 49;
	
	public static int QYZPMSYT = 99;
	public static int SCGYLCT = 98;
	
	
	
	
	public static String transitToChinese(int fk_unit) {
		/** 模块名称 */
		String fk_name = "";
		switch (fk_unit) {
		case 1:
			fk_name = "RWXF"; // 任务下发
			break;
		case 2:
			fk_name = "RWZX"; // 任务执行
			break;
		case 3:
			fk_name = "BLSMJ"; // 笔录扫描件
			break;
		case 4:
			fk_name = "QYCL"; // 企业材料
			break;
		case 5:
			fk_name = "RWFK"; // 任务反馈
			break;
		case 10:
			fk_name = "GYLC"; // 工艺流程
			break;
		case 11:
			fk_name = "JSXM"; // 建设项目
			break;
		case 12:
			fk_name = "LSJCJL"; // 历史监察记录
			break;
		case 13:
			fk_name = "FLFG"; // 法律法规
			break;
		case 14:
			fk_name = "HBBZ"; // 环保标准
			break;
		case 15:
			fk_name = "ZDWJ"; // 制度文件
			break;
		case 16:
			fk_name = "HBSC"; // 环保手册
			break;
		case 17:
			fk_name = "YJYA"; // 应急预案
			break;
		case 18:
			fk_name = "ZJK"; // 专家库
			break;
		case 19:
			fk_name = "ZB"; // 周报（暂时没用）
			break;
		case 20:
			fk_name = "SSQYHC"; // 上市企业核查
			break;
		case 21:
			fk_name = "JCQX"; // 暂时没用
			break;
		case 22:
			fk_name = "RWHF"; // 任务回复
			break;
		case 31:
			fk_name = "YYZZ"; // 营业执照
			break;
		case 32:
			fk_name = "ZZJGDMTP"; // 组织机构
			break;
		case 33:
			fk_name = "XMSPDZDA"; // 项目审批电子档案
			break;
		case 34:
			fk_name = "SSCSPDZDA"; // 试生产审批电子档案
			break;
		case 35:
			fk_name = "HBYSDZDA"; // 环保验收电子档案
			break;
		case 36:
			fk_name = "SCSSYXQK_SCGYT"; //生产设施运行情况_生产工艺图
			break;
		case 37:
			fk_name = "FSZLSSYXQK_GYT"; // 废水治理设施运行情况_工艺图
			break;
		case 38:
			fk_name = "FQZLSSYXQK_GYDZDA"; // 废气治理设施运行情况_工艺电子档案
			break;
		case 39:
			fk_name = "FSPWKQK_SB"; // 废水排污口情况_设备
			break;
		case 40:
			fk_name = "FSPWKQK_YXQK"; // 废水排污口情况_运行情况
			break;
		case 41:
			fk_name = "FSPWKQK_PFKQK"; // 废水排污口情况_排放口情况
			break;
		case 42:
			fk_name = "FQPWKQK_SB"; // 废气排污口情况_设备
			break;
		case 43:
			fk_name = "FQPWKQK_YXQK"; //  废气排污口情况_运行情况
			break;
		case 44:
			fk_name = "FQPWKQK_PFKQK"; // 废气排污口情况_排放口情况
			break;
		case 45:
			fk_name = "LSZFQK_CZWT"; // 历史执法情况_存在问题
			break;
		case 46:
			fk_name = "LSZFQK_CLJG"; //历史执法情况_处理结果
			break;
		case 47:
			fk_name = "HBSXLXQK_GYT"; //  环保手续履行情况_工艺图
			break;
		case 48:
			fk_name = "TZTG";//公告附件
			break;
			
		case 49:
			fk_name = "PWXKZFJ";//排污许可证附件
			break;
		case 50:
			fk_name = "HBSXLXQK_GYLCT";//环保手续履行情况_工艺流程图
			break;
		case 51:
			fk_name = "HJFXFFSS_SGFSSJCLLXT";//环境风险防范设施_事故废水收集处置路线图
			break;
		case 52:
			fk_name = "LADJ_B";//环境监察档案立案登记表
			break;
		case 53:
			fk_name = "LADJ_QT";//环境监察档案_立案登记其他
			break;
		case 54:
			fk_name = "TZGZCL_DCQKB";//环境监察档案_听证告知材料_环境案件调查情况表
			break;
		case 55:
			fk_name = "TZGZCL_ZJCL";//环境监察档案_听证告知材料_现场询问笔录等证据材料
			break;
		case 56:
			fk_name = "TZGZCL_SPD";//环境监察档案_听证告知材料_行政处罚听证告知书审批单
			break;
		case 57:
			fk_name = "TZGZCL_GZS";//环境监察档案_听证告知材料_行政处罚听证告知书
			break;
		case 58:
			fk_name = "TZGZCL_XQD";//环境监察档案_听证告知材料_听证告知书特快邮件详情单
			break;
		case 59:
			fk_name = "TZGZCL_QT";//环境监察档案_听证告知材料_其他
			break;
		case 60:
			fk_name = "CFJDCL_SPD";//环境监察档案_处罚决定材料_行政处罚决定审批单
			break;
		case 61:
			fk_name = "CFJDCL_JDS";//环境监察档案_处罚决定材料_行政处罚决定书
			break;
		case 62:
			fk_name = "CFJDCL_XQD";//环境监察档案_处罚决定材_处罚决定书特快邮件详情单
			break;
		case 63:
			fk_name = "CFJDCL_QT";//环境监察档案_处罚决定材料_其他
			break;
		case 64:
			fk_name = " FKJNPJ_FKJNPJ";//环境监察档案_罚款缴纳票据_罚款缴纳票据
			break;
		case 65:
			fk_name = "FKJNPJ_QT";//环境监察档案_罚款缴纳票据_其他
			break;
		case 66:
			fk_name = "XT_CGTZS";//环境监察档案_选填_催告通知书
			break;
		case 67:
			fk_name = "XT_QZZXWS";//环境监察档案_选填_强制执行文书
			break;
		case 68:
			fk_name = "XT_QT";//环境监察档案_选填_其他
			break;
		case 69:
			fk_name = "QYPMT";//企业_平面图
			break;
		case 70:
			fk_name = "QYHJGLZD_FJ";//企业环境管理制度_附件
			break;
		case 71:
			fk_name = "ZYLDFJ";//危废物转移档案-转移联单 附件
			break;
		case 72:
			fk_name = "HJFXFFSS_YJYA";//环境风险防范设施_应急预案
			break;
		case 73:
			fk_name = "FSPWKQK_QTBCZL";//废水排污口情况——其他补充资料
			break;
		case 74:
			fk_name = "FQPWKQK_QTBCZL";//废气排污口情况——其他补充资料
			break;
		case 75:
			fk_name = "GFHZ_GFZZQK";//固废灰渣_规范整治情况
			break;
		case 76:
			fk_name = "GFHZ_LYQK";//固废灰渣—利用情况
			break;
		case 77:
			fk_name = "GFHZ_QTBCZL";//固废灰渣—其他补充资料
			break;
		case 78:
			fk_name = "WFXX_GFZZQK";//危废信息_规范整治情况
			break;
		case 79:
			fk_name = "WFXX_LYQK";//危废信息—利用情况
			break;
		case 80:
			fk_name = "WFXX_QTBCZL";//危废信息—其他补充资料
			break;
		case 98:
			fk_name = "SCGYLCT";//环保手续履行情况_工艺流程图
			break;
		case 99:
			fk_name = "QYZPMSYT";//企业_平面图
			break;
			
		default:
			break;
		}
		return fk_name;
	}

	public int getCode(String fkunit) {
		int code = 0;

		
		// 任务下发
		if (fkunit.equals("RWXF"))
			code = 1;
		// 任务执行
		if (fkunit.equals("RWZX"))
			code = 2;
		 // 笔录扫描件
		if (fkunit.equals("BLSMJ"))
			code = 3;
		// 企业材料
		if (fkunit.equals("QYCL"))
			code = 4;
		 // 任务反馈
		if (fkunit.equals("RWFK"))
			code = 5;
		 // 工艺流程
		if (fkunit.equals("GYLC"))
			code = 10;
		 // 建设项目
		if (fkunit.equals("JSXM"))
			code = 11;
		 // 历史监察记录
		if (fkunit.equals("LSJCJL"))
			code = 12;
		
		// 法律法规
		if (fkunit.equals("FLFG"))
			code = 13;
		 // 环保标准
		if (fkunit.equals("HBBZ"))
			code = 14;
		 // 制度文件
		if (fkunit.equals("ZDWJ"))
			code = 15;
		// 环保手册
		if (fkunit.equals("HBSC")||fkunit.equals("exp_photo"))//exp_photo是专家库中的照片
			code = 16;
		 // 应急预案
		if (fkunit.equals("YJYA"))
			code = 17;
		
		 // 专家库
		if (fkunit.equals("ZJK"))
			code = 18;
	
		
		
		// / <summary>
		// / 营业执照 企业基本信息
		// / </summary>
		if (fkunit.equals("YYZZ"))
			code = 31;
		// / <summary>
		// / 组织机构 企业基本信息
		// / </summary>
		if (fkunit.equals("ZZJGDMTP"))
			code = 32;
		// / <summary>
		// / 项目审批电子档案 环保手续履行情况
		// / </summary>
		if (fkunit.equals("XMSPDZDA"))
			code = 33;
		// / <summary>
		// / 试生产审批电子档案 环保手续履行情况
		// / </summary>
		if (fkunit.equals("SSCSPDZDA"))
			code = 34;
		// / <summary>
		// / 环保验收电子档案 环保手续履行情况
		// / </summary>
		if (fkunit.equals("HBYSDZDA"))
			code = 35;
		// / <summary>
		// / 生产设施运行情况_生产工艺图 污染治理设施物
		// / </summary>
		if (fkunit.equals("SCSSYXQK_SCGYT"))
			code = 36;
		// / <summary>
		// / 废水治理设施运行情况_工艺图 污染治理设施物
		// / </summary>
		if (fkunit.equals("FSZLSSYXQK_GYT"))
			code = 37;
		// / <summary>
		// / 废气治理设施运行情况_工艺电子档案 污染治理设施物
		// / </summary>
		if (fkunit.equals("FQZLSSYXQK_GYDZDA"))
			code = 38;
		// / <summary>
		// / 废水排污口情况_设备 排污口
		// / </summary>
		if (fkunit.equals("FSPWKQK_SB"))
			code = 39;
		// / <summary>
		// / 废水排污口情况_运行情况 排污口
		// / </summary>
		if (fkunit.equals("FSPWKQK_YXQK"))
			code = 40;
		// / <summary>
		// / 废水排污口情况_排放口情况 排污口
		// / </summary>
		if (fkunit.equals("FSPWKQK_PFKQK"))
			code = 41;
		// / <summary>
		// / 废气排污口情况_设备 排污口
		// / </summary>
		if (fkunit.equals("FQPWKQK_SB"))
			code = 42;
		// / <summary>
		// / 废气排污口情况_运行情况 排污口
		// / </summary>
		if (fkunit.equals("FQPWKQK_YXQK"))
			code = 43;
		// / <summary>
		// / 废气排污口情况_排放口情况 排污口
		// / </summary>
		if (fkunit.equals("FQPWKQK_PFKQK"))
			code = 44;
		// / <summary>
		// / 历史执法情况_存在问题
		// / </summary>
		if (fkunit.equals("LSZFQK_CZWT"))
			code = 45;
		// / <summary>
		// / 历史执法情况_处理结果
		// / </summary>
		if (fkunit.equals("LSZFQK_CLJG"))
			code = 46;
		
	
		//  环保手续履行情况_工艺图
		if (fkunit.equals("HBSXLXQK_GYT"))
			code = 47;
		
		/// <summary>
        /// 通知通告
        /// </summary>
		if (fkunit.equals("TZTG"))
			code = 48;
        /// <summary>
        /// 排污许可证附件
        /// </summary>
        		if (fkunit.equals("PWXKZFJ"))
        			code = 49;
        /// <summary>
        /// 环保手续履行情况_工艺流程图
        /// </summary>
        		if (fkunit.equals("HBSXLXQK_GYLCT"))
        			code = 50;
        /// <summary>
        /// 环境风险防范设施_事故废水收集处置路线图
        /// </summary>
        		if (fkunit.equals("HJFXFFSS_SGFSSJCLLXT"))
        			code = 51;
        /// <summary>
        /// 环境监察档案立案登记表
        /// </summary>
        		if (fkunit.equals("LADJ_B"))
        			code = 52;
        /// <summary>
        /// 环境监察档案_立案登记其他
        /// </summary>
        		if (fkunit.equals("LADJ_QT"))
        			code = 53;
        /// <summary>
        /// 环境监察档案_听证告知材料_环境案件调查情况表
        /// </summary>
        		if (fkunit.equals("TZGZCL_DCQKB"))
        			code = 54;
        /// <summary>
        /// 环境监察档案_听证告知材料_现场询问笔录等证据材料
        /// </summary>
        		if (fkunit.equals("TZGZCL_ZJCL"))
        			code = 55;
        /// <summary>
        /// 环境监察档案_听证告知材料_行政处罚听证告知书审批单
        /// </summary>
        		if (fkunit.equals("TZGZCL_SPD"))
        			code = 56;
        /// <summary>
        /// 环境监察档案_听证告知材料_行政处罚听证告知书
        /// </summary>
        		if (fkunit.equals("TZGZCL_GZS"))
        			code = 57;
        /// <summary>
        /// 环境监察档案_听证告知材料_听证告知书特快邮件详情单
        /// </summary>
        		if (fkunit.equals("TZGZCL_XQD"))
        			code = 58;
        /// <summary>
        /// 环境监察档案_听证告知材料_其他
        /// </summary>
        		if (fkunit.equals("TZGZCL_QT"))
        			code = 59;
        /// <summary>
        /// 环境监察档案_处罚决定材料_行政处罚决定审批单
        /// </summary>
        		if (fkunit.equals("CFJDCL_SPD"))
        			code = 60;
        /// <summary>
        /// 环境监察档案_处罚决定材料_行政处罚决定书
        /// </summary>
        		if (fkunit.equals("CFJDCL_JDS"))
        			code = 61;
        /// <summary>
        /// 环境监察档案_处罚决定材_处罚决定书特快邮件详情单
        /// </summary>
        		if (fkunit.equals("CFJDCL_XQD"))
        			code = 62;
        /// <summary>
        /// 环境监察档案_处罚决定材料_其他
        /// </summary>
        		if (fkunit.equals("CFJDCL_QT"))
        			code = 63;
        /// <summary>
        /// 环境监察档案_罚款缴纳票据_罚款缴纳票据
        /// </summary>
        		if (fkunit.equals("FKJNPJ_FKJNPJ"))
        			code = 64;
        /// <summary>
        /// 环境监察档案_罚款缴纳票据_其他
        /// </summary>
        		if (fkunit.equals("FKJNPJ_QT"))
        			code = 65;
        /// <summary>
        /// 环境监察档案_选填_催告通知书
        /// </summary>
        		if (fkunit.equals("XT_CGTZS"))
        			code = 66;
        /// <summary>
        /// 环境监察档案_选填_强制执行文书
        /// </summary>
        		if (fkunit.equals("XT_QZZXWS"))
        			code = 67;
        /// <summary>
        /// 环境监察档案_选填_其他
        /// </summary>
        		if (fkunit.equals("XT_QT"))
        			code = 68;
        /// <summary>
        /// 企业_平面图
        /// </summary>
        		if (fkunit.equals("QYPMT"))
        			code = 69;
        /// <summary>
        /// 企业环境管理制度_附件
        /// </summary>
        		if (fkunit.equals("QYHJGLZD_FJ"))
        			code = 70;
        /// <summary>
        /// 危废物转移档案-转移联单 附件
        /// </summary>
        		if (fkunit.equals("ZYLDFJ"))
        			code = 71;
        /// <summary>
        /// 环境风险防范设施_应急预案
        /// </summary>
        		if (fkunit.equals("HJFXFFSS_YJYA"))
        			code = 72;
        /// <summary>
        /// 废水排污口情况——其他补充资料
        /// </summary>
        		if (fkunit.equals("FSPWKQK_QTBCZL"))
        			code = 73;
        /// <summary>
        /// 废气排污口情况——其他补充资料
        /// </summary>
        		if (fkunit.equals("FQPWKQK_QTBCZL"))
        			code = 74;
        /// <summary>
        /// 固废灰渣_规范整治情况
        /// </summary>
                if (fkunit.equals("GFHZ_GFZZQK"))
               	code = 75;
        /// <summary>
        /// 固废灰渣—利用情况
        /// </summary>
               if (fkunit.equals("GFHZ_LYQK"))
                    code = 76;
             /// <summary>
               /// 固废灰渣—其他补充资料
               /// </summary>
                      if (fkunit.equals("GFHZ_QTBCZL"))
                           code = 77;
                      /// <summary>
                      /// 危废信息_规范整治情况
                      /// </summary>
                              if (fkunit.equals("WFXX_GFZZQK"))
                             	code = 78;
                      /// <summary>
                      /// 危废信息—利用情况
                      /// </summary>
                             if (fkunit.equals("WFXX_LYQK"))
                                  code = 79;
                           /// <summary>
                             /// 危废信息—其他补充资料
                             /// </summary>
                                    if (fkunit.equals("WFXX_QTBCZL"))
                                         code = 80;
//                                    
//                                	public static int QYZPMSYT = 99;
//                                	public static int SCGYLCT = 98;
                                    
                                    if (fkunit.equals("QYZPMSYT")) {
                                        code = 99;
									}
                                    
                                    if (fkunit.equals("SCGYLCT")) {
                                        code = 98;
									}
                                    
                                    
		return code;
	}

}
