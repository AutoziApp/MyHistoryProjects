/**
 * FileName: httpDownloader.java
 * Description:基础类
 * @author 刘海兵
 * @Version 1.0
 * @Copyright 中科宇图天下科技有限公司<br>
 * Create at: 2013-08-27
 */

package com.mapuni.android.main;

public class MainModle {
	
	private  final String TAG = "MainModle";
	
	private  MainModle singleMainModle = null;

	
    public MainModle() {
    }

	public synchronized  MainModle getInstance() {
		if (singleMainModle == null) {
			singleMainModle = new MainModle();
			}
			return singleMainModle;
		}
}
