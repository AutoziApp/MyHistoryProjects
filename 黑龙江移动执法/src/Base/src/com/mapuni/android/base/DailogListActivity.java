package com.mapuni.android.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * FileName: DailogListActivity.java
 * Description: ListActivity��Dialog��ʽչʾ���˴����ڸ���˵��
 * @author ����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2012-12-5 ����02:32:04
 */
public class DailogListActivity extends ListActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setSynchronizeButtonVisiable(false);
		
	}

	@Override
	public void listItemClick(String idValue, String content) {
		String otherstr=dataList.get(Integer.valueOf(idValue)-1).get("bz").toString();
		Bundle nextBundle=new Bundle();
		nextBundle=bundle;
		Intent intent = new Intent();
//		intent.setClassName("com.mapuni.android.more", "com.mapuni.android.more.WebViewer");
		intent.setClass(DailogListActivity.this, WebViewer.class);
		intent.putExtra("otherstr", otherstr);
		intent.putExtra("title", "��ϸ����");
		intent.putExtras(nextBundle);
		startActivity(intent);
		this.finish();
	}

}
