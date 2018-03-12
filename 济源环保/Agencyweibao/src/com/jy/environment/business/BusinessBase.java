package com.jy.environment.business;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.webservice.NetworkGetCacheAPImpl;



public class BusinessBase
{
	protected Context mApplicationContext;
	protected NetworkGetCacheAPImpl				mApImpl;
	protected SmsManager						mSmsManager;
//	protected SQLiteDALModelPhoneNumLocal	mSqLiteDALModelPhoneNumLocal;

	public BusinessBase()
	{
		mApplicationContext = WeiBaoApplication.getInstance();
		mApImpl = new NetworkGetCacheAPImpl();
		mSmsManager = SmsManager.getDefault();
//		mSqLiteDALModelPhoneNumLocal = new SQLiteDALModelPhoneNumLocal(ModelPhoneNumLocal.class);
	}
	
//
//	/**
//	 * 发送短信
//	 */
//	public void sendSMS(String pPhoneNum, String pText)
//	{
//		Intent _Intent = new Intent(Intent.ACTION_SENDTO);
//		_Intent.setData(Uri.fromParts("smsto", pPhoneNum, null));
//		_Intent.putExtra("sms_body", pText);
//		_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		ApplicationExtend.getInstance().startActivity(_Intent);
//	}

//	public ResultGetVersion checkVersion() throws IOException
//	{
//		ResultGetVersion _ResultGetVersion = mApImpl.getVersion();
//
//		return _ResultGetVersion;
//	}

}
