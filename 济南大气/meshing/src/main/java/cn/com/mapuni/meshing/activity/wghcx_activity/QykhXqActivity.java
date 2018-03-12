package cn.com.mapuni.meshing.activity.wghcx_activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.mapuni.meshing.base.BaseActivity;

import cn.com.mapuni.meshingtotal.R;

public class QykhXqActivity extends BaseActivity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni1);
		setBACK_ISSHOW(true);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout),"区域考核详情");
		initView();
	}
	
	private void initView() {
		middleLayout = ((LinearLayout) findViewById(R.id.middleLayout1));
		LayoutInflater inflater = LayoutInflater.from(this);
		View mainView = inflater.inflate(R.layout.qykhxqactivity_layout, null);
		middleLayout.addView(mainView);
		
		SerializableMap map = (SerializableMap) getIntent().getExtras().get("item");
		HashMap<String, Object> hashMap = (HashMap<String, Object>) map.getMap();
		
		TextView text_gridCode = (TextView) mainView.findViewById(R.id.text_gridCode);
		TextView text_checkNum = (TextView) mainView.findViewById(R.id.text_checkNum);
		TextView text_msnNum = (TextView) mainView.findViewById(R.id.text_msnNum);
		TextView text_doneNum = (TextView) mainView.findViewById(R.id.text_doneNum);
		TextView text_userId = (TextView) mainView.findViewById(R.id.text_userId);
		
		text_gridCode.setText(hashMap.get("gridCode").toString());
		text_checkNum.setText(hashMap.get("checkNum").toString());
		text_msnNum.setText(hashMap.get("msnNum").toString());
		text_doneNum.setText(hashMap.get("doneNum").toString());
		text_userId.setText(hashMap.get("userId").toString());
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
//		case R.id.ll_wclaj:
//			break;
		default:
			break;
		}
	}

	/**
	 * 序列化map供Bundle传递map使用 Created on 13-12-9.
	 */
	private class SerializableMap implements Serializable {

		private Map<String, Object> map;

		public Map<String, Object> getMap() {
			return map;
		}

		public void setMap(Map<String, Object> map) {
			this.map = map;
		}
	}

}
