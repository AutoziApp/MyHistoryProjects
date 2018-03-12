package com.jy.environment.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.util.WbMapUtil;
//import com.baidu.mapapi.map.Geometry;
//import com.baidu.mapapi.map.Graphic;
//import com.baidu.mapapi.map.GraphicsOverlay;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.OverlayItem;
//import com.baidu.mapapi.map.Symbol;
//import com.baidu.mapapi.map.Symbol.Color;
//import com.baidu.mapapi.map.Symbol.Stroke;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.utils.CoordinateConvert;
//import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapAboutEnterpriseDetail extends Activity implements OnClickListener {

//	MapView mMapView = null; // 地图View
	
	private ImageView img_return;
	private TextView txt_title;
	private TextView txt_tolist;
	
	private TextView com_name;
	private TextView com_address;
	private TextView com_no;
	private TextView com_type;
	private TextView com_distance;
	
	String from,lat,lng,r,searchtype;
	String[] lngs,lats,names,addrs,diss,nos,types;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
		    setContentView(R.layout.map_about_enterprise_detial);  
		} catch (Exception e) {
		    e.printStackTrace();
		    finish();
		}
		
		img_return = (ImageView)findViewById(R.id.btn_return);
		txt_title = (TextView)findViewById(R.id.panel_title);
		txt_tolist = (TextView)findViewById(R.id.btn_returntolist);
		
		img_return.setOnClickListener(this);
		txt_tolist.setOnClickListener(this);
		
		com_no = (TextView)findViewById(R.id.com_no);
		com_name = (TextView)findViewById(R.id.com_name);
		com_distance = (TextView)findViewById(R.id.com_dis);
		com_type = (TextView)findViewById(R.id.com_type);
		com_address = (TextView)findViewById(R.id.com_address);
		
		
		from = getIntent().getStringExtra("from");
		lat = getIntent().getStringExtra("lat");
		lng = getIntent().getStringExtra("lng");
		r = getIntent().getStringExtra("r");
		searchtype = getIntent().getStringExtra("searchtype");
		lngs = getIntent().getStringArrayExtra("lngs");
		lats = getIntent().getStringArrayExtra("lats");
		nos = getIntent().getStringArrayExtra("nos");
		names = getIntent().getStringArrayExtra("names");
		addrs = getIntent().getStringArrayExtra("addrs");
		diss = getIntent().getStringArrayExtra("diss");
		types = getIntent().getStringArrayExtra("types");
		
		
		
		
		
		Drawable mark= getResources().getDrawable(R.drawable.map_poi_normal);
		
		txt_title.setText(getIntent().getStringExtra("maptitle"));
		
		//默认显示第一条记录在信息面板
		com_name.setText(names[0]);
		com_no.setText("1");
		com_address.setText(addrs[0]);
		com_distance.setText(diss[0]);
		setenvitype(types[0]);



	}
	
	/**添加标注的覆盖物
	 * @param layout_id
	 * @return
	 */
	private Drawable sharePicureLayoutToDrawable( int  layout_id ,Drawable img,int no){         
	        LayoutInflater inflator = LayoutInflater.from(this);
	         View viewHelp = inflator.inflate(layout_id, null);	 
	         
	         TextView txtView=(TextView)viewHelp.findViewById(R.id.selectedpt);
	         if(no>99)
	         {
	        	 txtView.setTextSize((float)8);
	        	 
	         }
	         txtView.setText(no+"");
	         txtView.setBackgroundDrawable(img);
	         
	         viewHelp.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	      	       MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	         viewHelp.layout(0, 0, WbMapUtil.dip2px(this, 21), WbMapUtil.dip2px(this, 28));  
	         
	         viewHelp.buildDrawingCache();
	         viewHelp.setDrawingCacheEnabled(true);
		       Bitmap snapshot = viewHelp.getDrawingCache();
		       //viewHelp.setDrawingCacheEnabled(false);
	         //Bitmap snapshot = convertViewToBitmap(viewHelp, size);
		       Drawable drawable=null;
		      try {
		    	  drawable= (Drawable)new BitmapDrawable(snapshot);
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		      
	        
	        return drawable;
	     }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int vid = v.getId();
		switch(vid)
		{
		case R.id.btn_return:
			this.finish();
			break;
		case R.id.btn_returntolist:
			this.finish();
			break;
		}
				
	}
	
	private void setenvitype(String type)
	{
		if(type.equals("1")){
			com_type.setText("废水排放企业");
			}else if(type.equals("2")){
				com_type.setText("废气排放企业");
			}else if(type.equals("3")){
				com_type.setText("污水处理场");
			}else if(type.equals("4")){
				com_type.setText("重金属企业");
			}else if(type.equals("5")){
				com_type.setText("规模化畜禽养殖场");
			}
	}
	

}
