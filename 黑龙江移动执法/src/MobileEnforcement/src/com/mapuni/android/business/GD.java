package com.mapuni.android.business;

import android.content.Context;
import android.content.Intent;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IInitData;

/**
 * @author SS TabHost页面中的更多
 */
public class GD extends BaseClass implements IInitData {

	public static final String BusinessClassName = "GD";

	@Override
	public Intent InitData(Context context, Intent intent, String ywl) {
		// Intent hbdtIntent = intent;
		// /** 二级列表 */
		// hbdtIntent = new Intent(context, ExpandListSquaredActivity.class);
		// /** 稽查考核 */
		// intent = new Intent(context, JCKHActivity.class);
		return intent;

	}

	@Override
	public String GetKeyField() {
		return null;
	}

	@Override
	public String GetTableName() {
		return null;
	}

}
