package com.jy.environment.map;


import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jy.environment.R;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.CityDB;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.WbMapUtil;
import com.jy.environment.webservice.UrlComponent;
//import com.baidu.mapapi.map.PopupOverlay;

public class MapPop_province_w extends LinearLayout {
	
	
	
	private String _snippet = "";
	
	private String _realtime = "";
	private String _windLevel = "";
	private String _windDirection = "";
	private String _weekday = "";
	private String _pm25data = "";
	private String _feeltemp = "";
	private String _temp = "";
	private String _weather = "";
	private String _date = "";
	private String _city = "";
	
	int _lowtemp = 0;
	int _hightemp = 0;
	int _feeltempval = 0;
	
//	private PopupOverlay pop=null;
	
	TextView lowtxt = null;
	TextView hightxt = null;
	TextView citytxt = null;
	TextView weathername = null;
	TextView feeltemptxt = null;
	ImageView weatherimg = null;
	
	ImageView feelimg = null;
	RelativeLayout line = null;
	
	LinearLayout main = null;
	LinearLayout cont = null;
	
	

	public MapPop_province_w(Context context,String snippet) {
		super(context);
		LayoutInflater inflater= LayoutInflater.from(context); 
		inflater.inflate(R.layout.map_popup_city_w, this);
		
		lowtxt = (TextView)findViewById(R.id.feeltemp_low);
		hightxt = (TextView)findViewById(R.id.feeltemp_high);
		citytxt = (TextView)findViewById(R.id.map_pop_wp_title);
		weathername = (TextView)findViewById(R.id.map_pop_wp_weathername);
		feeltemptxt = (TextView)findViewById(R.id.map_pop_wp_feeltemp);
		weatherimg = (ImageView)findViewById(R.id.map_pop_wp_pic);
		feelimg = (ImageView)findViewById(R.id.feeltemp_now_pic);
		line = (RelativeLayout)findViewById(R.id.temp_line);
		
		main = (LinearLayout)findViewById(R.id.map_pop_wp_background);
		cont = (LinearLayout)findViewById(R.id.map_pop_wp_cont);
		//从服务器端获取数据
		
		_snippet = snippet;
		CityDB citydb = WeiBaoApplication.getInstance().getCityDB();					
		String ccode = citydb.getCityCode(_snippet);
		String url = UrlComponent.weathernowURL+_snippet;
		WeatherTask task = new WeatherTask();
		MyLog.i(">>>>>>>>>>>>urlgghhg"+url);
		task.execute(url);
	}
	
	public MapPop_province_w(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater= LayoutInflater.from(context); 
		inflater.inflate(R.layout.map_popup_city_w, this);
		
		

	}
	
	@SuppressLint("NewApi")
	public void setTemp(int low,int high,int feel)
	{
		_lowtemp = low;
		_hightemp = high;
		_feeltempval = feel;
		
		feeltemptxt.setText("体感温度："+_feeltempval+"℃");
		
		float feelimgx = line.getX() + (float)line.getWidth()*((float)((float)_feeltempval-(float)_lowtemp)/(float)((float)_hightemp-(float)_lowtemp));
		
		feelimg.setTranslationX((int)(feelimgx-feelimg.getWidth()/2));
		feelimg.setVisibility(ImageView.VISIBLE);
		
	}
	
	
	
	/**
	 * -5℃~8℃
	 * @param temp
	 */
	private void setLowAndHighTempFromStr(String temp)
	{
		String[] res = temp.split("~");
		String low = res[0].split("℃")[0];
		String high = res[1].split("℃")[0];
		_lowtemp = Integer.parseInt(low);
		_hightemp = Integer.parseInt(high);
		
		//由于数据来源问题，low不一定是较小值，修正一下。
		int midVal = 0;
		String midStr = "";
		if(_lowtemp>_hightemp)
		{
			midVal = _lowtemp;
			_lowtemp = _hightemp;
			_hightemp = midVal;
			
			midStr = low;
			low = high;
			high = midStr;
		}
		
		lowtxt.setText(low+"℃");
		hightxt.setText(high+"℃");
		//由于数据原因feeltemp也不一定在low和high之间，修正处理一下
		if(_feeltempval<_lowtemp)
		{
			_feeltempval=_lowtemp;
		}
		if(_feeltempval>_hightemp)
		{
			_feeltempval=_hightemp;
		}
	}

	/**
	 * 任务是获取指定省会城市的天气，提取当天天气，存储至数据库，并显示至地图
	 *
	 */
	class WeatherTask extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params) {
			//params[0] url
			String ret = ApiClient.connServerForResult(params[0]);
			
			try
			{
				JSONObject jsonObject=new JSONObject(ret);
				JSONObject jsonObject2=	jsonObject.getJSONObject("weather");
				//allWeather.setCity(jsonObject2.getString("city"));
				
				_realtime = jsonObject2.getString("realTime");
				_windLevel = jsonObject2.getString("WS");
				_windDirection = jsonObject2.getString("WD");
				_weekday = jsonObject2.getString("weekday");
				_pm25data = jsonObject2.getString("PM2Dot5Data");
				_feeltemp = jsonObject2.getString("feelTemp");
				_temp = jsonObject2.getString("temp");
				_weather = jsonObject2.getString("weather");
				_date = jsonObject2.getString("date");
				_city = jsonObject2.getString("city");
				
			}
			catch(Exception e)
			{
				
				
				return null;
			}
			return null;
		}
		
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Void result) {
			//
			super.onPostExecute(result);
			
			//调整窗口页面数据
			citytxt.setText(_city);
			Drawable marker = getResources().getDrawable(WbMapUtil.getWeatherMarker(WbMapUtil.weatherName2Code(_weather)));
			weatherimg.setImageDrawable(marker);
			weathername.setText(_weather);
			
			try{    //有会接到不是int文本的情况，比如“暂无实况”。
				_feeltempval = Integer.parseInt(_feeltemp.split("℃")[0]);
				
			}catch(Exception e)
			{
				Log.v("转换失败", e.toString());
				_feeltempval=0;
			}
			setLowAndHighTempFromStr(_temp);
			
			main.setAlpha((float)1.0);
			cont.setVisibility(LinearLayout.VISIBLE);
			setTemp(_lowtemp, _hightemp, _feeltempval);
			
		}
	}
	

}
