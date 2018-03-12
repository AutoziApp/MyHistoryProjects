package com.jy.environment.util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RecordThread extends Thread{
private AudioRecord ar;  
public double vv;
private int bs;  
private static int SAMPLE_RATE_IN_HZ = 8000;  
public static boolean isRun = false;  
Handler handler;

public RecordThread(Handler _handler) {  
	super();
	
	handler = _handler;
	
    bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,  
            AudioFormat.CHANNEL_CONFIGURATION_MONO,  
            AudioFormat.ENCODING_PCM_16BIT);
	//bs = SAMPLE_RATE_IN_HZ*2*2*3;
    ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,  
            AudioFormat.CHANNEL_CONFIGURATION_MONO,  
            AudioFormat.ENCODING_PCM_16BIT, bs);
}

public RecordThread() {  
    super();
    
    bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,  
            AudioFormat.CHANNEL_CONFIGURATION_MONO,  
            AudioFormat.ENCODING_PCM_16BIT);  
    ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,  
            AudioFormat.CHANNEL_CONFIGURATION_MONO,  
            AudioFormat.ENCODING_PCM_16BIT, bs);  
}  

public void run() {  
    super.run();  
    ar.startRecording();
    
    byte[] buffer = new byte[bs];
    isRun = true;
    int k = 0;
    while (isRun) {
        
    	int r = ar.read(buffer, 0, bs);
        int v = 0;  
        
        //将 buffer内容取出，进行平方和运算 
        for (int i = 0; i < buffer.length; i++) {  
            //这里没有做运算的优化，为了更加清晰的展示代码  
            v += buffer[i] * buffer[i];
        }
        
        //平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。  
        if(k%5==0)
        {
//        	Log.d("spl", "bs-----"+String.valueOf(bs));
//        	Log.d("spl", "v-----"+String.valueOf(v));
//        	Log.d("spl", "r-----"+String.valueOf(r));
//        	vv = 20*Math.log10(v / (double) r);
//            int int_vv = (int)vv;
        	v /= buffer.length;
        	v = (int)Math.sqrt(v);
            Message msg = new Message();
            msg.what=1;
            msg.obj =v;
            
            handler.sendMessage(msg);
            Log.d("spl", String.valueOf(v));
        }
        k++;
    }
    
    ar.stop();
    ar.release();
    ar = null;
}  

public void pause() {  
            // 在调用本线程的 Activity 的 onPause 里调用，以便 Activity 暂停时释放麦克风  
    isRun = false;
}  

public void start() {  
            // 在调用本线程的 Activity 的 onResume 里调用，以便 Activity 恢复后继续获取麦克风输入音量  
    if (!isRun) {  
        super.start();  
    }  
} 


}
