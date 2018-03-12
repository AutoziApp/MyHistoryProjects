package com.mapuni.android.ui;

public class SubStringTimeUtil {
	/**
	 * 截取时间格式
	 * 
	 * @param str
	 * @return
	 */

	public static String substring(String str) {
		String datestr = "", dateString = "";

		if (str.contains("T") && str != null && str.trim().length() > 0) {
			try {
				datestr = str.replace("T", " ");
				if (datestr != null && datestr.contains("+08:00") && datestr.trim().length() > 0) {
					dateString = datestr.replace("+08:00", "");
				}
			} catch (Exception e) {
				return null;
			}
			//
		}

		return dateString;
	}
}
