package cn.com.mapuni.meshing.netprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
		LinkedHashMap<String, Object> _HashMap = new LinkedHashMap<String, Object>();
		_HashMap.put("AttachmentJosn", AttachmentJosn);
		_HashMap.put("FileValue", FileValue);
		_HashMap.put("isEnd", isEnd);
		Params.add(_HashMap);
		try {
			Object _Result =  WebServiceProvider.callWebService(NAMESPACE,
					"UploadFile", Params, WebServiceUrl,
					WebServiceProvider.RETURN_BOOLEAN, true);
			if(_Result!=null){
				result=(Boolean) _Result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
		
	}
}
