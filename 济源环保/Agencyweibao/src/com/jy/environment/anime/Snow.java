package com.jy.environment.anime;

import android.graphics.Bitmap;

public class Snow {

	/*
	 * 雪花的图片 
	 */
	public Bitmap bitmap;
	
	/**
	 * 雪花开始飘落的横坐标
	 */
	public float x;
	
	/**
	 * 雪花开始飘落的纵坐标
	 */
	public float y;
	
	/**
	 * 雪花下落的速度
	 */
	public float speed;
	
	/**
	 * 雪花下落时偏移的值
	 */
	public float offset;

	public Snow(Bitmap bitmap, float x, float y, float speed, float offset) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.offset = offset;
	}


	
	
	



}
