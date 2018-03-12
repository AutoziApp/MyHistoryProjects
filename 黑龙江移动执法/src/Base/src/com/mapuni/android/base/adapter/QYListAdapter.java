package com.mapuni.android.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.android.base.R;

/**
 * FileName: 
 * QYListAdapter
 * 
 * @author �����
 * @Version 1.4.7
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-11-30 ����10:39:46
 */
public class QYListAdapter extends BaseAdapter {
	
	public Context context;
	public ArrayList<HashMap<String, Object>> data;
	public Bundle RWBundle;
	
	public QYListAdapter(Context context,ArrayList<HashMap<String, Object>> data,Bundle RWBundle){
		this.context = context;
		this.data = data;
		this.RWBundle = RWBundle;
	}
	

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			/**��ҵִ��״̬0��δִ�У�1������ɣ�2��ִ����*/
			View view = View.inflate(context, R.layout.qyitem, null);
			ImageView image = (ImageView)view.findViewById(R.id.img);
			TextView qymc_text = (TextView)view.findViewById(R.id.qymc_text);
			TextView qyzt = (TextView)view.findViewById(R.id.qyzt);
			Button zf = (Button)view.findViewById(R.id.imgzf);
/*			Button ck = (Button)view.findViewById(R.id.ck);
*/			final HashMap<String, Object> map = data.get(position);
			String qymc = map.get("qymc").toString();
			if(qymc.equals("")){
				qymc_text.setText("���޸���ҵ������Ϣ");
			}else{
				qymc_text.setText(qymc);
			}
			
			final String qyzts = map.get("isexcute").toString();
			//BYK rwzt
			if(qyzts.equals("1")||qyzts.equals("")||qyzts==null){
				qyzt.setText("δִ��");

				zf.setVisibility(View.VISIBLE);
			}
			if(qyzts.equals("3")){
				qyzt.setText("�����");
				zf.setVisibility(View.GONE);
			//	ck.setVisibility(View.VISIBLE);
			}
			if(qyzts.equals("2")){
				qyzt.setText("ִ����");
			}
			/**
			 * ִ����ť��ӵ���¼�
			 */
			zf.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String qyid = map.get("qyid").toString();
					String rwbh = map.get("rwbh").toString();
					Intent intent = new Intent();
					intent.setClassName("com.mapuni.android.MobileEnforcement",
							"com.mapuni.android.taskmanager.QdjcnlActivity");
					intent.putExtra("qyid", qyid);
					intent.putExtra("rwbh", rwbh);
					intent.putExtra("qy_rwzt", qyzts);
					intent.putExtras(RWBundle);
					context.startActivity(intent);
					
				}
			});
			/**
			 * �鿴��ť����¼�
			 */
	/*		ck.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String qyid = map.get("qyid").toString();
					String rwbh = map.get("rwbh").toString();
					Intent intent = new Intent();
					intent.setClassName("com.mapuni.android.MobileEnforcement",
							"com.mapuni.android.taskmanager.QdjcnlActivity");
					intent.putExtra("qyid", qyid);
					intent.putExtra("rwbh", rwbh);
					intent.putExtra("qy_rwzt", qyzts);
					context.startActivity(intent);
					
				}
			});*/
			view.setTag(map.get("qyid"));
			convertView = view;
		}
		return convertView;
	}
	
}
