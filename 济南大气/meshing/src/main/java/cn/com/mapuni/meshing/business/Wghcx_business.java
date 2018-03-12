package cn.com.mapuni.meshing.business;

import android.content.Context;
import android.content.Intent;
import cn.com.mapuni.meshing.activity.wghcx_activity.WghcxMainActivity;
import cn.com.mapuni.meshing.base.business.BaseClass;
import cn.com.mapuni.meshing.base.interfaces.IInitData;

public class Wghcx_business extends BaseClass  implements IInitData  {
   
	/** 实体类名称 */
	public static final String BusinessClassName = "Wghcx_business";
	
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
		Intent xc_intent = intent;
		xc_intent = new Intent(context, WghcxMainActivity.class);
		xc_intent.putExtra("tab", 0);
		return   xc_intent;
	}

}
