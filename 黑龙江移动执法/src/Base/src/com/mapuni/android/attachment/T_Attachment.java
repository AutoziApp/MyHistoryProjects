package com.mapuni.android.attachment;

public class T_Attachment {

	/** �����·� */
	public static int RWXF = 1;
	/** ����ִ�� */
	public static int RWZX = 2;
	/** ��¼ɨ��� */
	public static int BLSMJ = 3;
	/** ��ҵ���� */
	public static int QYCL = 4;
	/** ������ */
	public static int RWFK = 5;
	/** �������� */
	public static int GYLC = 10;
	/** ������Ŀ */
	public static int JSXM = 11;
	/** ��ʷ����¼ */
	public static int LSJCJL = 12;
	/** ���ɷ��� */
	public static int FLFG = 13;
	/** ������׼ */
	public static int HBBZ = 14;
	/** �ƶ��ļ� */
	public static int ZDWJ = 15;
	/** Σ����Ʒ */
	public static int WXWP = 16;
	/** Ӧ��Ԥ�� */
	public static int YJYA = 17;
	/** ר�ҿ� */
	public static int ZJK = 18;
	/** �ܱ�����ʱû�ã� */
	public static int ZB = 19;
	/** ������ҵ�˲� */
	public static int SSQYHC = 20;
	/** ��ʱû�� */
	public static int JCQX = 21;
	/** ����ظ� */
	public static int RWHF = 22;
	// / <summary>
	// / Ӫҵִ�� ��ҵ������Ϣ
	// / </summary>
	public static int YYZZ = 31;
	// / <summary>
	// / ��֯���� ��ҵ������Ϣ
	// / </summary>
	public static int ZZJGDMTP = 32;
	// / <summary>
	// / ��Ŀ�������ӵ��� ���������������
	// / </summary>
	public static int XMSPDZDA = 33;
	// / <summary>
	// / �������������ӵ��� ���������������
	// / </summary>
	public static int SSCSPDZDA = 34;
	// / <summary>
	// / �������յ��ӵ��� ���������������
	// / </summary>
	public static int HBYSDZDA = 35;
	// / <summary>
	// / ������ʩ�������_��������ͼ ��Ⱦ������ʩ��
	// / </summary>
	public static int SCSSYXQK_SCGYT = 36;
	// / <summary>
	// / ��ˮ������ʩ�������_����ͼ ��Ⱦ������ʩ��
	// / </summary>
	public static int FSZLSSYXQK_GYT = 37;
	// / <summary>
	// / ����������ʩ�������_���յ��ӵ��� ��Ⱦ������ʩ��
	// / </summary>
	public static int FQZLSSYXQK_GYDZDA = 38;
	// / <summary>
	// / ��ˮ���ۿ����_�豸 ���ۿ�
	// / </summary>
	public static int FSPWKQK_SB = 39;
	// / <summary>
	// / ��ˮ���ۿ����_������� ���ۿ�
	// / </summary>
	public static int FSPWKQK_YXQK = 40;
	// / <summary>
	// / ��ˮ���ۿ����_�ŷſ���� ���ۿ�
	// / </summary>
	public static int FSPWKQK_PFKQK = 41;
	// / <summary>
	// / �������ۿ����_�豸 ���ۿ�
	// / </summary>
	public static int FQPWKQK_SB = 42;
	// / <summary>
	// / �������ۿ����_������� ���ۿ�
	// / </summary>
	public static int FQPWKQK_YXQK = 43;
	// / <summary>
	// / �������ۿ����_�ŷſ���� ���ۿ�
	// / </summary>
	public static int FQPWKQK_PFKQK = 44;
	// / <summary>
	// / ��ʷִ�����_��������
	// / </summary>
	public static int LSZFQK_CZWT = 45;
	// / <summary>
	// / ��ʷִ�����_������
	// / </summary>
	public static int LSZFQK_CLJG = 46;
	/** ���渽�� */
	public static int TZTG = 48;
	// <summary>
    /// ���������������_����ͼ
    /// </summary>
	public static int HBSXLXQK_GYT=47;
	
	public static int PWXKZFJ = 49;
	
	public static int QYZPMSYT = 99;
	public static int SCGYLCT = 98;
	
	
	
	
	public static String transitToChinese(int fk_unit) {
		/** ģ������ */
		String fk_name = "";
		switch (fk_unit) {
		case 1:
			fk_name = "RWXF"; // �����·�
			break;
		case 2:
			fk_name = "RWZX"; // ����ִ��
			break;
		case 3:
			fk_name = "BLSMJ"; // ��¼ɨ���
			break;
		case 4:
			fk_name = "QYCL"; // ��ҵ����
			break;
		case 5:
			fk_name = "RWFK"; // ������
			break;
		case 10:
			fk_name = "GYLC"; // ��������
			break;
		case 11:
			fk_name = "JSXM"; // ������Ŀ
			break;
		case 12:
			fk_name = "LSJCJL"; // ��ʷ����¼
			break;
		case 13:
			fk_name = "FLFG"; // ���ɷ���
			break;
		case 14:
			fk_name = "HBBZ"; // ������׼
			break;
		case 15:
			fk_name = "ZDWJ"; // �ƶ��ļ�
			break;
		case 16:
			fk_name = "HBSC"; // �����ֲ�
			break;
		case 17:
			fk_name = "YJYA"; // Ӧ��Ԥ��
			break;
		case 18:
			fk_name = "ZJK"; // ר�ҿ�
			break;
		case 19:
			fk_name = "ZB"; // �ܱ�����ʱû�ã�
			break;
		case 20:
			fk_name = "SSQYHC"; // ������ҵ�˲�
			break;
		case 21:
			fk_name = "JCQX"; // ��ʱû��
			break;
		case 22:
			fk_name = "RWHF"; // ����ظ�
			break;
		case 31:
			fk_name = "YYZZ"; // Ӫҵִ��
			break;
		case 32:
			fk_name = "ZZJGDMTP"; // ��֯����
			break;
		case 33:
			fk_name = "XMSPDZDA"; // ��Ŀ�������ӵ���
			break;
		case 34:
			fk_name = "SSCSPDZDA"; // �������������ӵ���
			break;
		case 35:
			fk_name = "HBYSDZDA"; // �������յ��ӵ���
			break;
		case 36:
			fk_name = "SCSSYXQK_SCGYT"; //������ʩ�������_��������ͼ
			break;
		case 37:
			fk_name = "FSZLSSYXQK_GYT"; // ��ˮ������ʩ�������_����ͼ
			break;
		case 38:
			fk_name = "FQZLSSYXQK_GYDZDA"; // ����������ʩ�������_���յ��ӵ���
			break;
		case 39:
			fk_name = "FSPWKQK_SB"; // ��ˮ���ۿ����_�豸
			break;
		case 40:
			fk_name = "FSPWKQK_YXQK"; // ��ˮ���ۿ����_�������
			break;
		case 41:
			fk_name = "FSPWKQK_PFKQK"; // ��ˮ���ۿ����_�ŷſ����
			break;
		case 42:
			fk_name = "FQPWKQK_SB"; // �������ۿ����_�豸
			break;
		case 43:
			fk_name = "FQPWKQK_YXQK"; //  �������ۿ����_�������
			break;
		case 44:
			fk_name = "FQPWKQK_PFKQK"; // �������ۿ����_�ŷſ����
			break;
		case 45:
			fk_name = "LSZFQK_CZWT"; // ��ʷִ�����_��������
			break;
		case 46:
			fk_name = "LSZFQK_CLJG"; //��ʷִ�����_������
			break;
		case 47:
			fk_name = "HBSXLXQK_GYT"; //  ���������������_����ͼ
			break;
		case 48:
			fk_name = "TZTG";//���渽��
			break;
			
		case 49:
			fk_name = "PWXKZFJ";//�������֤����
			break;
		case 50:
			fk_name = "HBSXLXQK_GYLCT";//���������������_��������ͼ
			break;
		case 51:
			fk_name = "HJFXFFSS_SGFSSJCLLXT";//�������շ�����ʩ_�¹ʷ�ˮ�ռ�����·��ͼ
			break;
		case 52:
			fk_name = "LADJ_B";//������쵵�������ǼǱ�
			break;
		case 53:
			fk_name = "LADJ_QT";//������쵵��_�����Ǽ�����
			break;
		case 54:
			fk_name = "TZGZCL_DCQKB";//������쵵��_��֤��֪����_�����������������
			break;
		case 55:
			fk_name = "TZGZCL_ZJCL";//������쵵��_��֤��֪����_�ֳ�ѯ�ʱ�¼��֤�ݲ���
			break;
		case 56:
			fk_name = "TZGZCL_SPD";//������쵵��_��֤��֪����_����������֤��֪��������
			break;
		case 57:
			fk_name = "TZGZCL_GZS";//������쵵��_��֤��֪����_����������֤��֪��
			break;
		case 58:
			fk_name = "TZGZCL_XQD";//������쵵��_��֤��֪����_��֤��֪���ؿ��ʼ����鵥
			break;
		case 59:
			fk_name = "TZGZCL_QT";//������쵵��_��֤��֪����_����
			break;
		case 60:
			fk_name = "CFJDCL_SPD";//������쵵��_������������_������������������
			break;
		case 61:
			fk_name = "CFJDCL_JDS";//������쵵��_������������_��������������
			break;
		case 62:
			fk_name = "CFJDCL_XQD";//������쵵��_����������_�����������ؿ��ʼ����鵥
			break;
		case 63:
			fk_name = "CFJDCL_QT";//������쵵��_������������_����
			break;
		case 64:
			fk_name = " FKJNPJ_FKJNPJ";//������쵵��_�������Ʊ��_�������Ʊ��
			break;
		case 65:
			fk_name = "FKJNPJ_QT";//������쵵��_�������Ʊ��_����
			break;
		case 66:
			fk_name = "XT_CGTZS";//������쵵��_ѡ��_�߸�֪ͨ��
			break;
		case 67:
			fk_name = "XT_QZZXWS";//������쵵��_ѡ��_ǿ��ִ������
			break;
		case 68:
			fk_name = "XT_QT";//������쵵��_ѡ��_����
			break;
		case 69:
			fk_name = "QYPMT";//��ҵ_ƽ��ͼ
			break;
		case 70:
			fk_name = "QYHJGLZD_FJ";//��ҵ���������ƶ�_����
			break;
		case 71:
			fk_name = "ZYLDFJ";//Σ����ת�Ƶ���-ת������ ����
			break;
		case 72:
			fk_name = "HJFXFFSS_YJYA";//�������շ�����ʩ_Ӧ��Ԥ��
			break;
		case 73:
			fk_name = "FSPWKQK_QTBCZL";//��ˮ���ۿ��������������������
			break;
		case 74:
			fk_name = "FQPWKQK_QTBCZL";//�������ۿ��������������������
			break;
		case 75:
			fk_name = "GFHZ_GFZZQK";//�̷ϻ���_�淶�������
			break;
		case 76:
			fk_name = "GFHZ_LYQK";//�̷ϻ������������
			break;
		case 77:
			fk_name = "GFHZ_QTBCZL";//�̷ϻ�����������������
			break;
		case 78:
			fk_name = "WFXX_GFZZQK";//Σ����Ϣ_�淶�������
			break;
		case 79:
			fk_name = "WFXX_LYQK";//Σ����Ϣ���������
			break;
		case 80:
			fk_name = "WFXX_QTBCZL";//Σ����Ϣ��������������
			break;
		case 98:
			fk_name = "SCGYLCT";//���������������_��������ͼ
			break;
		case 99:
			fk_name = "QYZPMSYT";//��ҵ_ƽ��ͼ
			break;
			
		default:
			break;
		}
		return fk_name;
	}

	public int getCode(String fkunit) {
		int code = 0;

		
		// �����·�
		if (fkunit.equals("RWXF"))
			code = 1;
		// ����ִ��
		if (fkunit.equals("RWZX"))
			code = 2;
		 // ��¼ɨ���
		if (fkunit.equals("BLSMJ"))
			code = 3;
		// ��ҵ����
		if (fkunit.equals("QYCL"))
			code = 4;
		 // ������
		if (fkunit.equals("RWFK"))
			code = 5;
		 // ��������
		if (fkunit.equals("GYLC"))
			code = 10;
		 // ������Ŀ
		if (fkunit.equals("JSXM"))
			code = 11;
		 // ��ʷ����¼
		if (fkunit.equals("LSJCJL"))
			code = 12;
		
		// ���ɷ���
		if (fkunit.equals("FLFG"))
			code = 13;
		 // ������׼
		if (fkunit.equals("HBBZ"))
			code = 14;
		 // �ƶ��ļ�
		if (fkunit.equals("ZDWJ"))
			code = 15;
		// �����ֲ�
		if (fkunit.equals("HBSC")||fkunit.equals("exp_photo"))//exp_photo��ר�ҿ��е���Ƭ
			code = 16;
		 // Ӧ��Ԥ��
		if (fkunit.equals("YJYA"))
			code = 17;
		
		 // ר�ҿ�
		if (fkunit.equals("ZJK"))
			code = 18;
	
		
		
		// / <summary>
		// / Ӫҵִ�� ��ҵ������Ϣ
		// / </summary>
		if (fkunit.equals("YYZZ"))
			code = 31;
		// / <summary>
		// / ��֯���� ��ҵ������Ϣ
		// / </summary>
		if (fkunit.equals("ZZJGDMTP"))
			code = 32;
		// / <summary>
		// / ��Ŀ�������ӵ��� ���������������
		// / </summary>
		if (fkunit.equals("XMSPDZDA"))
			code = 33;
		// / <summary>
		// / �������������ӵ��� ���������������
		// / </summary>
		if (fkunit.equals("SSCSPDZDA"))
			code = 34;
		// / <summary>
		// / �������յ��ӵ��� ���������������
		// / </summary>
		if (fkunit.equals("HBYSDZDA"))
			code = 35;
		// / <summary>
		// / ������ʩ�������_��������ͼ ��Ⱦ������ʩ��
		// / </summary>
		if (fkunit.equals("SCSSYXQK_SCGYT"))
			code = 36;
		// / <summary>
		// / ��ˮ������ʩ�������_����ͼ ��Ⱦ������ʩ��
		// / </summary>
		if (fkunit.equals("FSZLSSYXQK_GYT"))
			code = 37;
		// / <summary>
		// / ����������ʩ�������_���յ��ӵ��� ��Ⱦ������ʩ��
		// / </summary>
		if (fkunit.equals("FQZLSSYXQK_GYDZDA"))
			code = 38;
		// / <summary>
		// / ��ˮ���ۿ����_�豸 ���ۿ�
		// / </summary>
		if (fkunit.equals("FSPWKQK_SB"))
			code = 39;
		// / <summary>
		// / ��ˮ���ۿ����_������� ���ۿ�
		// / </summary>
		if (fkunit.equals("FSPWKQK_YXQK"))
			code = 40;
		// / <summary>
		// / ��ˮ���ۿ����_�ŷſ���� ���ۿ�
		// / </summary>
		if (fkunit.equals("FSPWKQK_PFKQK"))
			code = 41;
		// / <summary>
		// / �������ۿ����_�豸 ���ۿ�
		// / </summary>
		if (fkunit.equals("FQPWKQK_SB"))
			code = 42;
		// / <summary>
		// / �������ۿ����_������� ���ۿ�
		// / </summary>
		if (fkunit.equals("FQPWKQK_YXQK"))
			code = 43;
		// / <summary>
		// / �������ۿ����_�ŷſ���� ���ۿ�
		// / </summary>
		if (fkunit.equals("FQPWKQK_PFKQK"))
			code = 44;
		// / <summary>
		// / ��ʷִ�����_��������
		// / </summary>
		if (fkunit.equals("LSZFQK_CZWT"))
			code = 45;
		// / <summary>
		// / ��ʷִ�����_������
		// / </summary>
		if (fkunit.equals("LSZFQK_CLJG"))
			code = 46;
		
	
		//  ���������������_����ͼ
		if (fkunit.equals("HBSXLXQK_GYT"))
			code = 47;
		
		/// <summary>
        /// ֪ͨͨ��
        /// </summary>
		if (fkunit.equals("TZTG"))
			code = 48;
        /// <summary>
        /// �������֤����
        /// </summary>
        		if (fkunit.equals("PWXKZFJ"))
        			code = 49;
        /// <summary>
        /// ���������������_��������ͼ
        /// </summary>
        		if (fkunit.equals("HBSXLXQK_GYLCT"))
        			code = 50;
        /// <summary>
        /// �������շ�����ʩ_�¹ʷ�ˮ�ռ�����·��ͼ
        /// </summary>
        		if (fkunit.equals("HJFXFFSS_SGFSSJCLLXT"))
        			code = 51;
        /// <summary>
        /// ������쵵�������ǼǱ�
        /// </summary>
        		if (fkunit.equals("LADJ_B"))
        			code = 52;
        /// <summary>
        /// ������쵵��_�����Ǽ�����
        /// </summary>
        		if (fkunit.equals("LADJ_QT"))
        			code = 53;
        /// <summary>
        /// ������쵵��_��֤��֪����_�����������������
        /// </summary>
        		if (fkunit.equals("TZGZCL_DCQKB"))
        			code = 54;
        /// <summary>
        /// ������쵵��_��֤��֪����_�ֳ�ѯ�ʱ�¼��֤�ݲ���
        /// </summary>
        		if (fkunit.equals("TZGZCL_ZJCL"))
        			code = 55;
        /// <summary>
        /// ������쵵��_��֤��֪����_����������֤��֪��������
        /// </summary>
        		if (fkunit.equals("TZGZCL_SPD"))
        			code = 56;
        /// <summary>
        /// ������쵵��_��֤��֪����_����������֤��֪��
        /// </summary>
        		if (fkunit.equals("TZGZCL_GZS"))
        			code = 57;
        /// <summary>
        /// ������쵵��_��֤��֪����_��֤��֪���ؿ��ʼ����鵥
        /// </summary>
        		if (fkunit.equals("TZGZCL_XQD"))
        			code = 58;
        /// <summary>
        /// ������쵵��_��֤��֪����_����
        /// </summary>
        		if (fkunit.equals("TZGZCL_QT"))
        			code = 59;
        /// <summary>
        /// ������쵵��_������������_������������������
        /// </summary>
        		if (fkunit.equals("CFJDCL_SPD"))
        			code = 60;
        /// <summary>
        /// ������쵵��_������������_��������������
        /// </summary>
        		if (fkunit.equals("CFJDCL_JDS"))
        			code = 61;
        /// <summary>
        /// ������쵵��_����������_�����������ؿ��ʼ����鵥
        /// </summary>
        		if (fkunit.equals("CFJDCL_XQD"))
        			code = 62;
        /// <summary>
        /// ������쵵��_������������_����
        /// </summary>
        		if (fkunit.equals("CFJDCL_QT"))
        			code = 63;
        /// <summary>
        /// ������쵵��_�������Ʊ��_�������Ʊ��
        /// </summary>
        		if (fkunit.equals("FKJNPJ_FKJNPJ"))
        			code = 64;
        /// <summary>
        /// ������쵵��_�������Ʊ��_����
        /// </summary>
        		if (fkunit.equals("FKJNPJ_QT"))
        			code = 65;
        /// <summary>
        /// ������쵵��_ѡ��_�߸�֪ͨ��
        /// </summary>
        		if (fkunit.equals("XT_CGTZS"))
        			code = 66;
        /// <summary>
        /// ������쵵��_ѡ��_ǿ��ִ������
        /// </summary>
        		if (fkunit.equals("XT_QZZXWS"))
        			code = 67;
        /// <summary>
        /// ������쵵��_ѡ��_����
        /// </summary>
        		if (fkunit.equals("XT_QT"))
        			code = 68;
        /// <summary>
        /// ��ҵ_ƽ��ͼ
        /// </summary>
        		if (fkunit.equals("QYPMT"))
        			code = 69;
        /// <summary>
        /// ��ҵ���������ƶ�_����
        /// </summary>
        		if (fkunit.equals("QYHJGLZD_FJ"))
        			code = 70;
        /// <summary>
        /// Σ����ת�Ƶ���-ת������ ����
        /// </summary>
        		if (fkunit.equals("ZYLDFJ"))
        			code = 71;
        /// <summary>
        /// �������շ�����ʩ_Ӧ��Ԥ��
        /// </summary>
        		if (fkunit.equals("HJFXFFSS_YJYA"))
        			code = 72;
        /// <summary>
        /// ��ˮ���ۿ��������������������
        /// </summary>
        		if (fkunit.equals("FSPWKQK_QTBCZL"))
        			code = 73;
        /// <summary>
        /// �������ۿ��������������������
        /// </summary>
        		if (fkunit.equals("FQPWKQK_QTBCZL"))
        			code = 74;
        /// <summary>
        /// �̷ϻ���_�淶�������
        /// </summary>
                if (fkunit.equals("GFHZ_GFZZQK"))
               	code = 75;
        /// <summary>
        /// �̷ϻ������������
        /// </summary>
               if (fkunit.equals("GFHZ_LYQK"))
                    code = 76;
             /// <summary>
               /// �̷ϻ�����������������
               /// </summary>
                      if (fkunit.equals("GFHZ_QTBCZL"))
                           code = 77;
                      /// <summary>
                      /// Σ����Ϣ_�淶�������
                      /// </summary>
                              if (fkunit.equals("WFXX_GFZZQK"))
                             	code = 78;
                      /// <summary>
                      /// Σ����Ϣ���������
                      /// </summary>
                             if (fkunit.equals("WFXX_LYQK"))
                                  code = 79;
                           /// <summary>
                             /// Σ����Ϣ��������������
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
