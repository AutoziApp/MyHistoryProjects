package cn.com.mapuni.meshing.base.attachment;

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
	public static int HBSC = 16;
	/** Ӧ��Ԥ�� */
	public static int YJYA = 17;
	/** Ӧ������ */
	public static int YJAL = 18;
	// /**ר�ҿ�*/
	// public static int ZJK = 18;
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
	public static int ZZJGDM = 32;
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
	// / <summary>
	// / �������֤����
	// / </summary>
	public static int PWXKZBHFJ = 48;
	// / <summary>
	// / �����շѸ���
	// / </summary>
	public static int PWSFFJ = 54;
	// / <summary>
	// / ���߼�ؽ����������
	// / </summary>
	public static int ZXJKJSQKFJ = 55;
	// / <summary>
	// / ��Ⱦ���ŷ����--��ˮ�ŷ���Ⱦ����Ϣ����
	// / </summary>
	public static int FSPFWRWFJ = 56;
	// / <summary>
	// / ��Ⱦ���ŷ����--�����ŷ���Ⱦ����Ϣ����
	// / </summary>
	public static int FQPFWRWFJ = 57;
	// / <summary>
	// / ��Ⱦ���ŷ����--�����������Ϣ����
	// / </summary>
	public static int GTFWFJ = 58;
	// / <summary>
	// / ��Ⱦ���ŷ����--������Ϣ����
	// / </summary>
	public static int ZSXXFJ = 59;
	// / <summary>
		// / ֪ͨ����
		// / </summary>
	public static int TZGG = 60;

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
		case 31:
			fk_name = "YYZZ"; // ����ظ�
			break;
		case 32:
			fk_name = "ZZJGDM"; // ����ظ�
			break;
		case 33:
			fk_name = "XMSPDZDA"; // ����ظ�
			break;
		case 34:
			fk_name = "SSCSPDZDA"; // ����ظ�
			break;
		case 35:
			fk_name = "HBYSDZDA"; // ����ظ�
			break;
		case 36:
			fk_name = "SCSSYXQK_SCGYT"; // ����ظ�
			break;
		case 37:
			fk_name = "FSZLSSYXQK_GYT"; // ����ظ�
			break;
		case 38:
			fk_name = "FQZLSSYXQK_GYDZDA"; // ����ظ�
			break;
		case 39:
			fk_name = "FSPWKQK_SB"; // ����ظ�
			break;
		case 40:
			fk_name = "FSPWKQK_YXQK"; // ����ظ�
			break;
		case 41:
			fk_name = "FSPWKQK_PFKQK"; // ����ظ�
			break;
		case 42:
			fk_name = "FQPWKQK_SB"; // ����ظ�
			break;
		case 43:
			fk_name = "FQPWKQK_YXQK"; // ����ظ�
			break;
		case 44:
			fk_name = "FQPWKQK_PFKQK"; // ����ظ�
			break;
		case 45:
			fk_name = "LSZFQK_CZWT"; // ����ظ�
			break;
		case 46:
			fk_name = "LSZFQK_CLJG"; // ����ظ�
			break;
		case 47:
			fk_name = "fj"; // ��Ԥ���Ͱ����ĸ���
			break;
		case 48:
			fk_name = "PWXKZBHFJ"; // �������֤����
			break;
		case 50:
			fk_name = "PWSBDJBFJ"; // �����걨�ǼǱ�ĸ���
			break;
		case 51:
			fk_name = "YJYAFJ"; // Ӧ��Ԥ���ĸ���
			break;
		case 52:
			fk_name = "QJSCFJ"; // ��������ĸ���
			break;
		case 53:
			fk_name = "HJXWPJFJ"; // ������Ϊ���۵ĸ���
			break;
		case 54:
			fk_name = "PWSFFJ"; // �����շѸ���
			break;
		case 55:
			fk_name = "ZXJKJSQKFJ"; // ���߼�ؽ����������
			break;
		case 56:
			fk_name = "FSPFWRWFJ"; //  ��Ⱦ���ŷ����--��ˮ�ŷ���Ⱦ����Ϣ����
			break;
		case 57:
			fk_name = "FQPFWRWFJ"; // ��Ⱦ���ŷ����--�����ŷ���Ⱦ����Ϣ����
			break;
		case 58:
			fk_name = "GTFWFJ"; // ��Ⱦ���ŷ����--�����������Ϣ����
			break;
		case 59:
			fk_name = "ZSXXFJ"; // ��Ⱦ���ŷ����--������Ϣ����
			break;
		case 60:
			fk_name = "TZGG"; // ֪ͨ����
			break;
		case 61:
			fk_name = "JCSJFJ"; // �������
			break;
		default:
			break;
		}
		return fk_name;
	}

	public int getCode(String fkunit) {
		int code = 0;

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
		if (fkunit.equals("HBSXLXQK_GYT"))
			code = 47;
		if (fkunit.equals("PWXKZBHFJ"))
			code = 48;
		if (fkunit.equals("PWSBDJBFJ"))
			code = 50;
		if (fkunit.equals("YJYAFJ"))
			code = 51;
		if (fkunit.equals("QJSCFJ"))
			code = 52;
		if (fkunit.equals("HJXWPJFJ"))
			code = 53;
		
		if (fkunit.equals("PWSFFJ"))
			code = 54;
		if (fkunit.equals("ZXJKJSQKFJ"))
			code = 55;
		if (fkunit.equals("FSPFWRWFJ"))
			code = 56;
		if (fkunit.equals("FQPFWRWFJ"))
			code = 57;
		if (fkunit.equals("GTFWFJ"))
			code = 58;
		if (fkunit.equals("ZSXXFJ"))
			code = 59;
		if (fkunit.equals("TZGG"))
			code = 60;
		if (fkunit.equals("JCSJFJ"))
			code = 61;
		return code;
	}

}
