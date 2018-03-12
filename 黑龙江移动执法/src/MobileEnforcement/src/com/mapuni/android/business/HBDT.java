package com.mapuni.android.business;

import android.content.Context;
import android.content.Intent;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IInitData;
import com.mapuni.android.gis.MapActivity;

/**
 * 
 */

/**
 * @author SS
 * 
 */
public class HBDT extends BaseClass implements IInitData {

	/** 实体类名称 */
	public static final String BusinessClassName = "HBDT";

	@Override
	public Intent InitData(Context context, Intent intent, String ywl) {
		Intent hbdtIntent = intent;
		hbdtIntent = new Intent(context, MapActivity.class);
		intent.putExtra("number", 1);
		intent.setAction("Gis");
		return hbdtIntent;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mapuni.android.base.business.BaseClass#GetKeyField()
	 */
	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mapuni.android.base.business.BaseClass#GetTableName()
	 */
	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
