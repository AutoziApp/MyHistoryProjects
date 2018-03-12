package com.jy.environment.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodeUtil {

	/**
	 * URL编码,默认utf-8
	 * 
	 * @param content
	 *            编码内容
	 * @return
	 */
	public static String urlEncode(String content) {
		content = urlEncode(content, "UTF-8");
		return content;
	}

	/**
	 * URL编码
	 * 
	 * @param content
	 *            编码内容
	 * @param encoding
	 *            编码方式
	 * @return
	 */
	public static String urlEncode(String content, String encoding) {
		try {
			content = URLEncoder.encode(content, encoding);
		} catch (UnsupportedEncodingException e) {
		}
		return content;
	}
}
