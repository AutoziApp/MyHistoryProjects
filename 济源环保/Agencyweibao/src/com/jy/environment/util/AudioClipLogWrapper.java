/*
 * Copyright 2012 Greg Milette and Adam Stroud
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jy.environment.util;

import java.math.BigDecimal;

import root.gast.audio.interp.LoudNoiseDetector;
import root.gast.audio.processing.ZeroCrossing;
import root.gast.audio.record.AudioClipListener;
import root.gast.audio.util.AudioUtil;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * Helps log information during {@link ClapperPlay}
 * 
 * @author Greg Milette &#60;<a
 *         href="mailto:gregorym@gmail.com">gregorym@gmail.com</a>&#62;
 */
public class AudioClipLogWrapper implements AudioClipListener {
    private TextView log;

    private int k = 0;
    private int j = 0;
    private Activity context;

    private double previousFrequency = -1;

    private Handler handler;

    public AudioClipLogWrapper(Handler handler, TextView log, Activity context) {
	this.handler = handler;
	this.log = log;
	this.context = context;
    }

    private int lastDb = 0;

    @Override
    public boolean heard(short[] audioData, int sampleRate) {
	// final double zero = ZeroCrossing.calculate(sampleRate, audioData);
	// final double volume = AudioUtil.rootMeanSquared(audioData);
	//
	// final boolean isLoudEnough = volume >
	// LoudNoiseDetector.DEFAULT_LOUDNESS_THRESHOLD;
	// // range threshold of 100
	// final boolean isDifferentFromLast = Math.abs(zero -
	// previousFrequency) > 100;
	//
	// final StringBuilder message = new StringBuilder();
	// message.append("volume: ").append((int) volume);
	// if (!isLoudEnough) {
	// message.append(" (silence) ");
	// }
	// message.append(" freqency: ").append((int) zero);
	// if (isDifferentFromLast) {
	// message.append(" (diff)");
	// }
	//
	// previousFrequency = zero;
	
	final double volume = AudioUtil.rootMeanSquared(audioData);
	  if (j % 3 == 0) {
	      Message msg = new Message();
	      int db = 20;
		db = (int) getDBByAudio(volume);
		if (volume != 0) {
		    msg.what = 1;
		    msg.obj = db;
		    handler.sendMessage(msg);
		}else{
		    j = j + 2;
		}
	  }
	  j ++ ;
	
//	int v = 0;
//
//	for (int i = 0; i < audioData.length; i++) {
//	    // 这里没有做运算的优化，为了更加清晰的展示代码
//	    v += audioData[i] * audioData[i];
//	}
//	// 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
//	// if (k % 5 == 0) {
//	// vv = 20*Math.log10(v / (double) r);
//	// int int_vv = (int)vv;
//	v /= audioData.length;
//	v = (int) Math.sqrt(v);
//	Message msg = new Message();
//	int db = 30;
//	db = (int) getDBByAudio(v);
//	if (v != 0) {
//	    msg.what = 1;
//	    msg.obj = db;
//	    if (k % 3 == 0) {
//		if(lastDb == 0){
//		    lastDb = db;
//		}
//		if(lastDb - db > 20){
//		    //延缓分贝下降速率 保证变化的平滑
//		    db = (lastDb - db)+ db - 10 ;
//		    lastDb = db;
//		}
//		handler.sendMessage(msg);
//	    }
//	} else {
//	    k = k + 2;
//	}
//	if (k > 10000) {
//	    k = 0;
//	}
//	k++;
	return false;
    }

    private double getDBByAudio(double audioData) {
   	double i = 100.00;
   	if (audioData >= 100000) {
   	    return i;
   	}
   	if (audioData >= 20000) {
   	    i = (double) (Math.round((audioData - 20000) * 100) / 100.0 / 16000.0) + 90;
   	    return i;
   	}
   	if (audioData >= 7600) {
   	    i = (double) (Math.round((audioData - 7600) * 100) / 100.0 / 2480.0) + 85;
   	    return i;
   	}
   	if (audioData >= 3000) {
   	    i = (double) (Math.round((audioData - 3000) * 100) / 100.0 / 920.0) + 80;
   	    return i;
   	}
   	if (audioData >= 800) {
   	    i = (double) (Math.round((audioData - 800) * 100) / 100.0 / 220.0) + 70;
   	    return i;
   	}
   	if (audioData >=300) {
   	    i = (double) (Math.round((audioData - 300) * 100) / 100.0 / 30.0) + 60;
   	    return i;
   	}
   	if (audioData >= 100) {
   	    i = (double) (Math.round((audioData - 100) * 100) / 100.0 / 20) + 50;
   	    return i;
   	}
   	if (audioData >= 85) {
   	    i = (double) (Math.round((audioData - 85) * 100) / 100.0 / 1.5) + 40;
   	    return i;
   	}
   	if (audioData >= 60) {
   	    i = (double) (Math.round((audioData - 60) * 100) / 100.0 / 2.5) + 30;
   	    return i;
   	}
   	if (audioData >= 0) {
   	    i = (double) (Math.round((audioData - 0) * 100) / 100.0 / 6.0) + 20;
   	    return i;
   	}
   	return 20;
       }
    private double getDBByAudio2(int audioData) {
	double i = 100.00;
	if (audioData > 2000) {
	    return i;
	}
	if (audioData > 1700) {
	    i = (double) (Math.round((audioData - 1700) * 100) / 100.0 / 30.0) + 90;
	    return i;
	}
	if (audioData > 900) {
	    i = (double) (Math.round((audioData - 900) * 100) / 100.0 / 80.0) + 80;
	    return i;
	}
	if (audioData > 350) {
	    i = (double) (Math.round((audioData - 350) * 100) / 100.0 / 55.0) + 70;
	    return i;
	}
	if (audioData > 180) {
	    i = (double) (Math.round((audioData - 180) * 100) / 100.0 / 17.0) + 60;
	    return i;
	}
	if (audioData > 105) {
	    i = (double) (Math.round((audioData - 105) * 100) / 100.0 / 7.5) + 50;
	    return i;
	}
	if (audioData > 85) {
	    i = (double) (Math.round((audioData - 85) * 100) / 100.0 / 2.0) + 40;
	    return i;
	}
	if (audioData > 60) {
	    i = (double) (Math.round((audioData - 60) * 100) / 100.0 / 2.5) + 30;
	    return i;
	}
	if (audioData > 0) {
	    i = (double) (Math.round((audioData - 0) * 100) / 100.0 / 6.0) + 20;
	    return i;
	}
	return 20;
    }
}
