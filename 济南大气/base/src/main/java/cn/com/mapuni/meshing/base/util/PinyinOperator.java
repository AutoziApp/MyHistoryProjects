package cn.com.mapuni.meshing.base.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * FileName: PinyinOperator.java 
 * Description: Pinyin4j ����תƴ��
 * @author ��˼Զ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ 
 * Create at: 2012-12-6 ����04:25:07
 */
public class PinyinOperator {

	/**
	 * 1.Change a char to Pinyin
	 * 
	 * @param text
	 * @return All it's Pinyin String
	 */
	public  String convertToPinyin(char text) {
		String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(text);
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < pinyinArray.length; ++i) {
			sb.append(pinyinArray[i]);
		}
		return sb.toString();
	}

	/**
	 * 2.Change a text to Pinyin with Tone Mark
	 * 
	 * @param text
	 * @return
	 */
	public  String convertToPinyinWithToneMark(char text) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		String[] pinyinArray = null;
		try {
			pinyinArray = PinyinHelper.toHanyuPinyinStringArray(text, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < pinyinArray.length; ++i) {
			sb.append(pinyinArray[i]);
		}
		return sb.toString();
	}

	/**
	 * 3.Get First Char
	 * 
	 * @param str
	 * @return
	 */
	public static  String getPinYinFirstChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * 4.Convert a long Chinese text to Pinyin String
	 * 
	 * @param text
	 * @return
	 */
	public  String convertStringToPinYin(String text) {
		return new Hanyu().getStringPinYin(text);
	}

	/**
	 * a native class to convert a chinese text to String
	 * 
	 * @author Administrator
	 * 
	 */
	private final  class Hanyu {
		private HanyuPinyinOutputFormat format = null;
		private String[] pinyin;

		public Hanyu() {
			format = new HanyuPinyinOutputFormat();
			format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
			format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			pinyin = null;
		}

		// ת�������ַ�
		public String getCharacterPinYin(char c) {
			try {
				pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
			// ���c���Ǻ��֣�toHanyuPinyinStringArray�᷵��null
			if (pinyin == null)
				return null;
			// ֻȡһ������������Ƕ����֣���ȡ��һ������
			return pinyin[0] + " ";
		}

		// ת��һ���ַ���
		public String getStringPinYin(String str) {
			StringBuilder sb = new StringBuilder();
			String tempPinyin = null;
			for (int i = 0; i < str.length(); ++i) {
				tempPinyin = getCharacterPinYin(str.charAt(i));
				if (tempPinyin == null) {
					// ���str.charAt(i)�Ǻ��֣��򱣳�ԭ��
					sb.append(str.charAt(i));
				} else {
					sb.append(tempPinyin);
				}
			}
			return sb.toString();
		}
	}

	/**
	 * get a String's first char
	 * 
	 * @param str
	 * @return
	 */
	public  String getPinYinFirstOneCharUppercase(String str) {
		char convert = 1;
		char word = str.charAt(0);
		String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
		if (pinyinArray != null) {
			convert = pinyinArray[0].charAt(0);
		} else {
			convert = str.charAt(0);
		}
		return String.valueOf(convert).toUpperCase();
	}
}
