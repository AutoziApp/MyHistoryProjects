package cn.com.mapuni.meshing.business;

import android.content.Context;
import android.content.Intent;
import cn.com.mapuni.meshing.activity.wd_activity.WdMainActivity;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IInitData;

public class Wd_business extends BaseClass  implements IInitData  {
	/** 实体类名称 */
	public static final String BusinessClassName = "Wd_business";
	 
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
		Intent wd_intent = intent;
		wd_intent = new Intent(context, WdMainActivity.class);
		return   wd_intent;
	}

}
