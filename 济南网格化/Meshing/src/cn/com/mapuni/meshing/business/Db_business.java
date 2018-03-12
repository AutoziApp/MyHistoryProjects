package cn.com.mapuni.meshing.business;

import android.content.Context;
import android.content.Intent;
import cn.com.mapuni.meshing.activity.gis.MapMainActivity;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IInitData;

public class Db_business extends BaseClass implements IInitData {
	/** 实体类名称 */
	public static final String BusinessClassName = "Db_business";

	@Override
	public String GetKeyField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GetTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent InitData(Context context, Intent intent, String ywl) {
		Intent db_intent = intent;
		db_intent = new Intent(context, MapMainActivity.class);
		db_intent.putExtra("tab", 1);
		return db_intent;
	}

}
