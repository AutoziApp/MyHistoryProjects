package com.mapuni.android.netprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.widget.Toast;

public class HttpUploader {
	private String WebServiceUrl;
	public String getWebServiceUrl() {
		return WebServiceUrl;
	}
	public void setWebServiceUrl(String webServiceUrl) {
		WebServiceUrl = webServiceUrl;
	}
	public String getNAMESPACE() {
		return NAMESPACE;
	}
	public void setNAMESPACE(String nAMESPACE) {
		NAMESPACE = nAMESPACE;
	}
	private String NAMESPACE="http://tempuri.org/";
	
	/**
	 * 调用上传附件接口
	 * @param AttachmentJosn
	 * @param FileValue
	 * @param isEnd
	 * @return
	 */
	public Boolean upLoadAffixMethod(String AttachmentJosn, 
			String FileValue, Boolean isEnd){
		Boolean result=false;
		ArrayList<HashMap<String, Object>> Params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> _HashMap = new HashMap<String, Object>();
		_HashMap.put("AttachmentJosn", AttachmentJosn);
		_HashMap.put("FileValue", FileValue);
		_HashMap.put("isEnd", isEnd);
		Params.add(_HashMap);
		try {
			
			
			Object _Result =  WebServiceProvider.callWebService(NAMESPACE,
					"UploadFile", Params, WebServiceUrl,
					WebServiceProvider.RETURN_BOOLEAN, true);
			if(_Result!=null){
				//result=Boolean.getBoolean(_Result.toString());
				//result=(Boolean) _Result;
				result= Boolean.parseBoolean(_Result.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
		
	}
	
	/**
	 * 调用上传企业附件接口
	 * @param AttachmentJosn
	 * @param FileValue
	 * @param isEnd
	 * @return
	 */
	public Boolean upLoadAffixMethod2(String AttachmentJosn, 
			String FileValue, Boolean isEnd){
		Boolean result=false;
		ArrayList<HashMap<String, Object>> Params = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> _HashMap = new HashMap<String, Object>();
		_HashMap.put("AttachmentJosn", AttachmentJosn);
		_HashMap.put("FileValue", FileValue);
		_HashMap.put("isEnd", isEnd);
		Params.add(_HashMap);
		try {
			
			
			Object _Result =  WebServiceProvider.callWebService(NAMESPACE,
					"UploadFileEntInfo", Params, WebServiceUrl,
					WebServiceProvider.RETURN_BOOLEAN, true);
			if(_Result!=null){
				//result=Boolean.getBoolean(_Result.toString());
				//result=(Boolean) _Result;
				result= Boolean.parseBoolean(_Result.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
		
	}
}
