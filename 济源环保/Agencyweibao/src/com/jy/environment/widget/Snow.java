package com.jy.environment.widget;

import android.graphics.Bitmap;

public class Snow {
	/*
	 * ѩ����ͼƬ 
	 */
	Bitmap bitmap;
	
	/**
	 * ѩ����ʼƮ��ĺ�����
	 */
	float x;
	
	/**
	 * ѩ����ʼƮ���������
	 */
	float y;
	
	/**
	 * ѩ��������ٶ�
	 */
	float speed;
	
	/**
	 * ѩ������ʱƫ�Ƶ�ֵ
	 */
	float offset;

	public Snow(Bitmap bitmap, float x, float y, float speed, float offset) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.offset = offset;
	}


	
	
	

}
