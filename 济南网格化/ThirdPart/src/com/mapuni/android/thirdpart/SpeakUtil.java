package com.mapuni.android.thirdpart;

import android.content.Context;
import android.util.Log;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.mapuni.android.netprovider.Net;


/**
 * FileName: SpeakUtil.java
 * Description:��������
 * @author Liusy
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
 * Create at: 2013-2-22 ����10:37:41
 */
public class SpeakUtil implements SynthesizerPlayerListener{
	
	//��������ʵ��
	private static SpeakUtil mVoiceUtil;
	//�ϳɶ���.
	private SynthesizerPlayer mSynthesizerPlayer;
	
	private static final String VOICECLOUD_URL = "http://open.voicecloud.cn/";
	
	//APPID
	private static final String SPEAK_APPID = "5122e167";
	//��ɫ--����-- xiaoyan(����Ů��) xiaoyu(��������)
	private static final String SPEAK_ROLE = "xiaoyan";
	private static final String SPEAK_ROLE_TEST = "vixk";
	private static final String SPEAK_ROLE_TEST2 = "vixl";
	
	//����
	private static final int SPEAK_SPEED = 50;
	//����
	private static final int SPEAK_VOICE = 50;
	//����
	private static final String SPEAK_MUSIC = "";
	
	private static final String TAG = "SpeakUtil";
	
	public static SpeakUtil getInstance() {
		if(mVoiceUtil == null) {
			mVoiceUtil = new SpeakUtil();
		}
		return mVoiceUtil;
	}
	
	/**
	 * ʹ��SynthesizerPlayer�ϳ��������������ϳ�Dialog.
	 * @param
	 */
	public void synthetizeInSilence(Context context, String source) {
		if(source == null || source.length() == 0) {
			return;
		}
		if(!Net.checkURL(VOICECLOUD_URL)) {
			Log.i(TAG, "���粻ͨ���޷�����������");
			return;
		}
		
		if (null == mSynthesizerPlayer) {
			//�����ϳɶ���.
			mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(context, "appid=" + SPEAK_APPID);
		}

		//���úϳɷ�����.
		mSynthesizerPlayer.setVoiceName(SPEAK_ROLE_TEST2);
		//���÷���������
		mSynthesizerPlayer.setSpeed(SPEAK_SPEED);
		//��������.
		mSynthesizerPlayer.setVolume(SPEAK_VOICE);
		//���ñ�����.
		mSynthesizerPlayer.setBackgroundSound(SPEAK_MUSIC);
		
		//���������ϳ�.���ʶ�
		mSynthesizerPlayer.playText(source, null, this);
		Log.i(TAG, "Voice Done !");
	}
	
	
	@Override
	public void onBufferPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnd(SpeechError error) {
		if(error != null) {
			Log.i(TAG, error.getErrorDesc());
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
