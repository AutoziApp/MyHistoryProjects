package cn.com.mapuni.meshing.business;

import android.content.Context;
import android.content.Intent;
import cn.com.mapuni.meshing.activity.gis.MapMainActivity;

import com.mapuni.android.base.business.BaseClass;
import com.mapuni.android.base.interfaces.IInitData;

public class Xc_business extends BaseClass  implements IInitData  {
   
	/** 实体类名称 */
	public static final String BusinessClassName = "Xc_business";
	
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
		xc_intent = new Intent(context, MapMainActivity.class);
		xc_intent.putExtra("tab", 0);
		return   xc_intent;
	}

}
