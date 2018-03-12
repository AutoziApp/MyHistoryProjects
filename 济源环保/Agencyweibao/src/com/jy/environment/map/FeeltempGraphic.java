package com.jy.environment.map;



import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.environment.R;

public class FeeltempGraphic extends RelativeLayout {

	public FeeltempGraphic(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater= LayoutInflater.from(context); 
		inflater.inflate(R.layout.map_feeltemp_graph, this);
		
		lowtxt = (TextView)findViewById(R.id.feeltemp_low);
		hightxt = (TextView)findViewById(R.id.feeltemp_high);
		feelimg = (ImageView)findViewById(R.id.feeltemp_now_pic);
		line = (RelativeLayout)findViewById(R.id.temp_line);
	}

	public FeeltempGraphic(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater= LayoutInflater.from(context); 
		inflater.inflate(R.layout.map_feeltemp_graph, this);
		
		lowtxt = (TextView)findViewById(R.id.feeltemp_low);
		hightxt = (TextView)findViewById(R.id.feeltemp_high);
		feelimg = (ImageView)findViewById(R.id.feeltemp_now_pic);
		line = (RelativeLayout)findViewById(R.id.temp_line);
	}

	int _lowtemp = 0;
	int _hightemp = 0;
	int _feeltemp = 0;
	
	TextView lowtxt = null;
	TextView hightxt = null;
	ImageView feelimg = null;
	RelativeLayout line = null;
	public FeeltempGraphic(Context context) {
		super(context);
		LayoutInflater inflater= LayoutInflater.from(context); 
		inflater.inflate(R.layout.map_feeltemp_graph, this);
		
		lowtxt = (TextView)findViewById(R.id.feeltemp_low);
		hightxt = (TextView)findViewById(R.id.feeltemp_high);
		feelimg = (ImageView)findViewById(R.id.feeltemp_now_pic);
		line = (RelativeLayout)findViewById(R.id.temp_line);
	}
	
	@SuppressLint("NewApi")
	public void setTemp(int low,int high,int feel)
	{
		_lowtemp = low;
		_hightemp = high;
		_feeltemp = feel;
		float feelimgx = line.getX()+
				line.getWidth()*((_feeltemp-_lowtemp)/(_hightemp-_lowtemp));
		feelimg.setX(feelimgx);
		feelimg.setVisibility(ImageView.VISIBLE);
		
	}

}
