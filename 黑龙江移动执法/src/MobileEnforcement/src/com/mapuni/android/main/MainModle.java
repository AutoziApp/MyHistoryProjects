/**
 * FileName: httpDownloader.java
 * Description:������
 * @author ������
 * @Version 1.0
 * @Copyright �п���ͼ���¿Ƽ����޹�˾<br>
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
