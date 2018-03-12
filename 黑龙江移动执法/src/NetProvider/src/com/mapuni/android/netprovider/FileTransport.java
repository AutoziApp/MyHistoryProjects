package com.mapuni.android.netprovider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

/**
 * �ļ������紫����
 * 
 * @author xugf@mapuni.com
 *
 */
public class FileTransport {
	
	private static final String TAG = "FileTransport";
	
	public   int currentPercent = 0; //�ϴ����Ȱٷֱ�
	
	/**
	 * ���ط���
	 * 
	 * @param downloadUrl ���صĵ�ַ
	 * @param saveFile	     �����ļ��ĵ�ַ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {  
		
        int currentSize 	= 0;   
        long totalSize 		= 0;   
        int updateTotalSize = 0;   

        HttpURLConnection httpConnection = null;   
        InputStream is = null;   
        FileOutputStream fos = null;                

        try {   
            URL url = new URL(downloadUrl);   
            httpConnection = (HttpURLConnection)url.openConnection();   
            
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");   

            if(currentSize > 0) {   
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");   
            }   

            httpConnection.setConnectTimeout(10000); //���ӳ�ʱʱ��
            httpConnection.setReadTimeout(20000);    //��ȡ���ݳ�ʱʱ��
            
            if (httpConnection.getResponseCode() == 404) {   
                throw new Exception("fail!");   
            }   
            
            updateTotalSize = httpConnection.getContentLength();   

            is  = httpConnection.getInputStream();                     
            fos = new FileOutputStream(saveFile, false);   
            byte buffer[] = new byte[4096];   
            int readsize  = 0;
            
            while((readsize = is.read(buffer)) > 0){   
                fos.write(buffer, 0, readsize);   
                totalSize += readsize;   
                currentPercent = (int)(totalSize * 100 / updateTotalSize);
            }   
        } finally {   
            if(httpConnection != null) {   
                httpConnection.disconnect();   
            }   
            if(is != null) {   
                is.close();   
            }   
            if(fos != null) {   
                fos.close();   
            }   
        } 
        Log.v(TAG, saveFile + " ������� !!!");
        return totalSize;   
    }  
}
