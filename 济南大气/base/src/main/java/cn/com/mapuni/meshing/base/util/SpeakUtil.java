package cn.com.mapuni.meshing.base.util;

import android.content.Context;
import android.util.Log;

import cn.com.mapuni.meshing.netprovider.Net;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;

/**
 * FileName: SpeakUtil.java
 * Description:播放语音
 * @author Liusy
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2013-2-22 上午10:37:41
 */
public class SpeakUtil implements SynthesizerPlayerListener{
	
	//语音工具实例
	private static  SpeakUtil mVoiceUtil;
	//合成对象.
	private SynthesizerPlayer mSynthesizerPlayer;
	
	private  final String VOICECLOUD_URL = "http://open.voicecloud.cn/";
	
	//APPID
	private  final String SPEAK_APPID = "5122e167";
	//角色--声音-- xiaoyan(青年女音) xiaoyu(青年男音)
	private  final String SPEAK_ROLE = "xiaoyan";
	private  final String SPEAK_ROLE_TEST = "vixk";
	private  final String SPEAK_ROLE_TEST2 = "vixl";
	
	//语速
	private  final int SPEAK_SPEED = 50;
	//音量
	private  final int SPEAK_VOICE = 50;
	//语速
	private  final String SPEAK_MUSIC = "";
	
	private  final String TAG = "SpeakUtil";
	
	public static SpeakUtil getInstance() {
		if(mVoiceUtil == null) {
			mVoiceUtil = new SpeakUtil();
		}
		return mVoiceUtil;
	}
	
	/**
	 * 使用SynthesizerPlayer合成语音，不弹出合成Dialog.
	 * @param
	 */
	public void synthetizeInSilence(Context context, String source) {
		if(source == null || source.length() == 0) {
			return;
		}
		if(!Net.checkURL(VOICECLOUD_URL)) {
			LogUtil.i(TAG, "网络不通，无法连接语音云");
			return;
		}
		
		if (null == mSynthesizerPlayer) {
			//创建合成对象.
			mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(context, "appid=" + SPEAK_APPID);
		}

		//设置合成发音人.
		mSynthesizerPlayer.setVoiceName(SPEAK_ROLE_TEST2);
		//设置发音人语速
		mSynthesizerPlayer.setSpeed(SPEAK_SPEED);
		//设置音量.
		mSynthesizerPlayer.setVolume(SPEAK_VOICE);
		//设置背景音.
		mSynthesizerPlayer.setBackgroundSound(SPEAK_MUSIC);
		
		//进行语音合成.并朗读
		mSynthesizerPlayer.playText(source, null, this);
		LogUtil.i(TAG, "Voice Done !");
	}
	
	
	@Override
	public void onBufferPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnd(SpeechError error) {
		if(error != null) {
			LogUtil.i(TAG, error.getErrorDesc());
		}
		if (null != mSynthesizerPlayer) {
			mSynthesizerPlayer.cancel();
		}
	}

	@Override
	public void onPlayBegin() {
		
	}

	@Override
	public void onPlayPaused() {
		
	}

	@Override
	public void onPlayPercent(int percent, int arg1, int arg2) {
		
	}

	@Override
	public void onPlayResumed() {
		
	}

}
