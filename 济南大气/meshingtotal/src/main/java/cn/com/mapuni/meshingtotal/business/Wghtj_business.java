package cn.com.mapuni.meshingtotal.business;

import android.content.Context;
import android.content.Intent;

import cn.com.mapuni.meshingtotal.activity.wghtj.WghtjMainActivity;
import cn.com.mapuni.meshing.base.business.BaseClass;
import cn.com.mapuni.meshing.base.interfaces.IInitData;

public class Wghtj_business extends BaseClass  implements IInitData  {
   
	/** 实体类名称 */
	public static final String BusinessClassName = "Wghtj_business";
	
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
		xc_intent = new Intent(context, WghtjMainActivity.class);
		xc_intent.putExtra("tab", 0);
		return   xc_intent;
	}

}
