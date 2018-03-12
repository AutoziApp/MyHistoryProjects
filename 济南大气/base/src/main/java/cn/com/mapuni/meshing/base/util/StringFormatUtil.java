package cn.com.mapuni.meshing.base.util;

/**
 * <PRE>
 * �ṩ���ַ�����ȫ��-&gt;��ǣ����-&gt;ȫ��ת��
 * </PRE>
 */
public class StringFormatUtil {
	/**
	 * ASCII���пɼ��ַ���!��ʼ��ƫ��λֵΪ33(Decimal)
	 */
	static final char DBC_CHAR_START = 33; // ���!

	/**
	 * ASCII���пɼ��ַ���~������ƫ��λֵΪ126(Decimal)
	 */
	static final char DBC_CHAR_END = 126; // ���~

	/**
	 * ȫ�Ƕ�Ӧ��ASCII��Ŀɼ��ַ��ӣ���ʼ��ƫ��ֵΪ65281
	 */
	static final char SBC_CHAR_START = 65281; // ȫ�ǣ�

	/**
	 * ȫ�Ƕ�Ӧ��ASCII��Ŀɼ��ַ�����������ƫ��ֵΪ65374
	 */
	static final char SBC_CHAR_END = 65374; // ȫ�ǡ�

	/**
	 * ASCII���г��ո���Ŀɼ��ַ����Ӧ��ȫ���ַ������ƫ��
	 */
	static final int CONVERT_STEP = 65248; // ȫ�ǰ��ת�����

	/**
	 * ȫ�ǿո��ֵ����û�������ASCII�����ƫ�ƣ����뵥������
	 */
	static final char SBC_SPACE = 12288; // ȫ�ǿո� 12288

	/**
	 * ��ǿո��ֵ����ASCII��Ϊ32(Decimal)
	 */
	static final char DBC_SPACE = ' '; // ��ǿո�

	/**
	 * <PRE>
	 * ����ַ�->ȫ���ַ�ת��  
	 * ֻ����ո�!��&tilde;֮����ַ�����������
	 * </PRE>
	 */
	public static String bj2qj(String src) {
		if (src == null) {
			return src;
		}
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == DBC_SPACE) { // ����ǰ�ǿո�ֱ����ȫ�ǿո����
				buf.append(SBC_SPACE);
			} else if ((ca[i] >= DBC_CHAR_START) && (ca[i] <= DBC_CHAR_END)) { // �ַ���!��~֮��Ŀɼ��ַ�
				buf.append((char) (ca[i] + CONVERT_STEP));
			} else { // ���Կո��Լ�ascii���������ɼ��ַ�֮����ַ����κδ���
				buf.append(ca[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <PRE>
	 * ȫ���ַ�->����ַ�ת��  
	 * ֻ����ȫ�ǵĿո�ȫ�ǣ���ȫ�ǡ�֮����ַ�����������
	 * </PRE>
	 */
	public static String qj2bj(String src) {
		if (src == null) {
			return src;
		}
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < src.length(); i++) {
			if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // ���λ��ȫ�ǣ���ȫ�ǡ�������
				buf.append((char) (ca[i] - CONVERT_STEP));
			} else if (ca[i] == SBC_SPACE) { // �����ȫ�ǿո�
				buf.append(DBC_SPACE);
			} else { // ������ȫ�ǿո�ȫ�ǣ���ȫ�ǡ���������ַ�
				buf.append(ca[i]);
			}
		}
		return buf.toString();
	}
	
	/**
	 * ��ȡȫ����ascii�ַ�����
	 * */
	public static int getCount(String src) {
		if (src == null) {
			return 0;
		}
		int count = 0;
		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < src.length(); i++) {
			if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // ���λ��ȫ�ǣ���ȫ�ǡ�������
				count++;
			} else if (ca[i] == SBC_SPACE) { // �����ȫ�ǿո�
				count++;
			}
		}
		return count;
	}
}

