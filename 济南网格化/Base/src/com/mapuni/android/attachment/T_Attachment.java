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
	public static int ZZJGDM = 32;
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
		// case 16:
		// fk_name = "WXWP"; //危险物品
		// break;
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
		case 31:
			fk_name = "YYZZ"; // 任务回复
			break;
		case 32:
			fk_name = "ZZJGDM"; // 任务回复
			break;
		case 33:
			fk_name = "XMSPDZDA"; // 任务回复
			break;
		case 34:
			fk_name = "SSCSPDZDA"; // 任务回复
			break;
		case 35:
			fk_name = "HBYSDZDA"; // 任务回复
			break;
		case 36:
			fk_name = "SCSSYXQK_SCGYT"; // 任务回复
			break;
		case 37:
			fk_name = "FSZLSSYXQK_GYT"; // 任务回复
			break;
		case 38:
			fk_name = "FQZLSSYXQK_GYDZDA"; // 任务回复
			break;
		case 39:
			fk_name = "FSPWKQK_SB"; // 任务回复
			break;
		case 40:
			fk_name = "FSPWKQK_YXQK"; // 任务回复
			break;
		case 41:
			fk_name = "FSPWKQK_PFKQK"; // 任务回复
			break;
		case 42:
			fk_name = "FQPWKQK_SB"; // 任务回复
			break;
		case 43:
			fk_name = "FQPWKQK_YXQK"; // 任务回复
			break;
		case 44:
			fk_name = "FQPWKQK_PFKQK"; // 任务回复
			break;
		case 45:
			fk_name = "LSZFQK_CZWT"; // 任务回复
			break;
		case 46:
			fk_name = "LSZFQK_CLJG"; // 任务回复
			break;
		default:
			break;
		}
		return fk_name;
	}

	public int getCode(String fkunit) {
		int code = 0;

		// / <summary>
		// / 专家库,预案库
		// / </summary>
		if (fkunit.equals("HBSC"))
			code = 16;
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

		return code;
	}

}
