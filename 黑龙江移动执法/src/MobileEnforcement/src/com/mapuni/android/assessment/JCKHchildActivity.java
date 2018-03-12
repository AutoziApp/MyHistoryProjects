package com.mapuni.android.assessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.dataprovider.SqliteUtil;

public class JCKHchildActivity extends BaseActivity implements OnItemClickListener{
	
	Intent intent;
	LayoutInflater li;
	RelativeLayout rl, childrl;
	LinearLayout middlelayout;
	ListView zhtree;
	private final String TableName="EvaluatTypes";
	ArrayList<HashMap<String, Object>> mdata;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ui_mapuni);
		
		intent =getIntent();
		int id=intent.getIntExtra("id", 1);
		String sql="select * from EvaluatTypes where PID is null and CheckTableID ='01'";
		try {
			mdata=	SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		li=LayoutInflater.from(this);
		View mainview = li.inflate(R.layout.jckh_childview1, null);
		rl = (RelativeLayout) findViewById(R.id.parentLayout);
		//childrl = (RelativeLayout) mainview.findViewById(R.id.father_scroll);
		LinearLayout layout = (LinearLayout)findViewById(R.id.ui_mapuni_divider);
		layout.setVisibility(View.GONE);
		
		middlelayout = (LinearLayout) findViewById(R.id.middleLayout);
		middlelayout.addView(mainview);
		
		// ListView Ê÷
		zhtree = (ListView) mainview.findViewById(R.id.jckh_zhzd_tree);
		zhtree.setDivider(getResources().getDrawable(R.drawable.list_divider));
		SetBaseStyle(rl, "ÏÖ³¡±ÊÂ¼");
		setTitleLayoutVisiable(true);
		ArrayList<String> list=new ArrayList<String>();
		for(HashMap<String, Object> map:mdata){
			list.add(map.get("checktypename").toString());
		}
		
		zhtree.setAdapter(new MyAdapter(list,this));
		zhtree.setOnItemClickListener(this);
		
	}
	
	public  class MyAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private List<String> root;
		
		public MyAdapter(List<String> root, Context context) {
			this.root = root;
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return root.size();
		}

		@Override
		public Object getItem(int position) {
			return root.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            View views = mInflater.inflate(R.layout.listitem, null); 
            ImageView imageleft = (ImageView) views.findViewById(R.id.listitem_left_image);
			imageleft.setImageResource(R.drawable.icon_left_not_checked);
			
			TextView tv = (TextView) views.findViewById(R.id.listitem_text);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(5, 5, 0, 0);
			tv.setText(root.get(position));
			
			ImageView imageRight = (ImageView) views.findViewById(R.id.listitem_image);
			imageRight.setPadding(5, 5, 0, 0);

		    
		    return views;
		}
		    
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//arg1.get
		
	}

}
