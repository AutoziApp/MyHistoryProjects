package com.jy.environment.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.AsyncTask;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.NetUtil;
import com.jy.environment.webservice.UrlComponent;

public class DiscoverNearbyListActivity extends ActivityBase implements OnClickListener {

	private TextView paneltitle;
	private ImageView txtreturn;
	private TextView txttomap;
	
	private RadioButton rb_1km;
	private RadioButton rb_5km;
	private RadioButton rb_10km;
	private RadioButton rb_50km;
	private RadioButton rb_all;
	private ListView listview;
	private List<Map<String,Object>> reslist;
	private String dingweiAddress;
	private TextView dingweiTextView;
	private String cityString;
	
	private ProgressDialog prDialog;
	
	private String lng;
	private String lat;
	private String r;
	private String searchtype;
	private String fromactivity="";//标识附近模块是从哪个activity调起，是发现还是地图长按
	String i_paneltile;
	private RadioButton radion_allcity;
	private RelativeLayout layout_btm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discover_nearby_list);
		i_paneltile = getIntent().getStringExtra("titlename");
		searchtype = getIntent().getStringExtra("searchtype");

		initcontrol();
		//药店
		if(searchtype.equals("1"))
		{
			radion_allcity.setVisibility(View.GONE);
			rb_50km.setVisibility(View.GONE);
			layout_btm.setVisibility(View.GONE);
			rb_1km.setText(300+"");
			rb_5km.setText(500+"");
			rb_10km.setText(1000+"");
			
		}
		paneltitle.setText(i_paneltile);
			//其他手段获取坐标
			WeiBaoApplication mWeiBaoApplication = WeiBaoApplication.getInstance();
			dingweiAddress = mWeiBaoApplication.getXiangxidizhi();
			cityString = mWeiBaoApplication.getDingweicity();
			dingweiTextView.setText(dingweiAddress);
			fromactivity = getIntent().getStringExtra("from");
			if(fromactivity !=null && fromactivity.equals("mapMainActivity"))
			{
				lng = (double)(getIntent().getIntExtra("maplong", 0))/1E6+"";
				lat =  (double)(getIntent().getIntExtra("maplat", 0))/1E6+"";
			}
			else
			{
				MyLog.i("Application获得："+lng+lat);
				lng = mWeiBaoApplication.getCurrentCityLongitude();
				lat = mWeiBaoApplication.getCurrentCityLatitude();
			}
			
/*			lng = getIntent().getDoubleExtra("lng", 0)+"";
			lat = getIntent().getDoubleExtra("lat", 0)+"";*/
			//药店
			r = "500";
			if(searchtype.equals("1"))
			{
			    r = "300";
			}
			showresbyloc(lng,lat,r);
	}
	
	/**
	 * @param lng jing
	 * @param lat wei
	 * @param r banjing
	 */
	private void showresbyloc(String lng,String lat,String r)
	{
		String url ="";
		if(searchtype.equals("1"))
		{
			url = UrlComponent.searchComByLocURL +"pharmacy/baidu/"+ lat+"/"+lng+"/"+r+UrlComponent.token;
			
		}else if(searchtype.equals("2"))
		{
			url = UrlComponent.searchComByLocURL + "company/baidu/"+ lat+"/"+lng+"/"+r+UrlComponent.token;
			
		}else if(searchtype.equals("3"))
		{
			url = UrlComponent.searchComByLocURL + "/baidu/"+ lat+"/"+lng+"/"+r+UrlComponent.token;
			
		}
		if (NetUtil.getNetworkState(DiscoverNearbyListActivity.this) == NetUtil.NETWORN_NONE) {
		    Toast.makeText(DiscoverNearbyListActivity.this,
			    "请检查您的网络", 0).show();
		    return;
		}
		myTask task = new myTask();
		Log.v("url", url);
		task.execute(url);
	}
	
	private void initcontrol()
	{
		paneltitle = (TextView)findViewById(R.id.panel_title);
		txtreturn = (ImageView)findViewById(R.id.btn_return);
		txttomap = (TextView)findViewById(R.id.btn_returntomap);
		rb_1km = (RadioButton)findViewById(R.id.radion_500);
		rb_5km = (RadioButton)findViewById(R.id.radion_1000);
		rb_10km = (RadioButton)findViewById(R.id.radion_2000);
		rb_50km = (RadioButton)findViewById(R.id.radion_5000);
		rb_all = (RadioButton)findViewById(R.id.radion_allcity);
		listview = (ListView)findViewById(R.id.list_reslist);
		layout_btm  = (RelativeLayout)findViewById(R.id.layout_btm);
		dingweiTextView = (TextView) findViewById(R.id.nearby_xiangxidizhi);
		radion_allcity = (RadioButton) findViewById(R.id.radion_allcity);		
		
		txtreturn.setOnClickListener(this);
		txttomap.setOnClickListener(this);
		rb_1km.setOnClickListener(this);
		rb_5km.setOnClickListener(this);
		rb_10km.setOnClickListener(this);
		rb_50km.setOnClickListener(this);
		rb_all.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		int vid = v.getId();
		switch(vid)
		{
		case R.id.radion_500:
			radioclick(v);
			break;
		case R.id.radion_1000:
			radioclick(v);
			break;
		case R.id.radion_2000:
			radioclick(v);
			break;
		case R.id.radion_5000:
			radioclick(v);
			break;
		case R.id.radion_allcity:
			radioclick(v);
			break;
		case R.id.btn_return:
			this.finish();
			break;
		case R.id.btn_returntomap:
			showonmap();
			break;
		
		}
	}
	
	
	/**
	 * radio组的控制
	 * @param v
	 */
	private void radioclick(View v)
	{
		int vid = v.getId();
		
		rb_1km.setChecked(false);
		rb_5km.setChecked(false);
		rb_10km.setChecked(false);
		rb_50km.setChecked(false);
		rb_all.setChecked(false);
		
		switch(vid)
		{
		case R.id.radion_500:
			if(searchtype.equals("1"))
			{
			    r="300";  
			}else{
			    r="500";
			}
			rb_1km.setChecked(true);
			break;
		case R.id.radion_1000:
		    if(searchtype.equals("1"))
			{
			    r="500";  
			}else{
			    r="1000";
			}
			rb_5km.setChecked(true);
			break;
		case R.id.radion_2000:
		    if(searchtype.equals("1"))
			{
			    r="1000";  
			}else{
			    r="2000";
			}
			rb_10km.setChecked(true);
			break;
		case R.id.radion_5000:
			r="5000";
			rb_50km.setChecked(true);
			break;
		case R.id.radion_allcity:
			r="@"+cityString;
			rb_all.setChecked(true);
			break;
		
		}
		
		//并执行异步查询
		showresbyloc(lng,lat,r);
	}
	
	private void showonmap()
	{
		Intent intent = new Intent();
		intent.setClass(DiscoverNearbyListActivity.this, MapAboutEnterpriseDetail.class);
		intent.putExtra("from", "Activity_mapsearch_pollcom");
		intent.putExtra("maptitle", i_paneltile);
		intent.putExtra("lng",lng );
		intent.putExtra("lat",lat );
		intent.putExtra("r",r );
		//传递点结果 集
		if(reslist==null)
		{
			Toast.makeText(DiscoverNearbyListActivity.this,"没有要展示的数据！",0).show();
			return;
		}else if(reslist.size()==1)
		{
			if(reslist.get(0).get("comname").toString().equals("未查询到相关结果！"))
			{
				Toast.makeText(DiscoverNearbyListActivity.this,"没有要展示的数据！",0).show();
				return;
			}
			
		}
		int num = reslist.size();
		
		String[] lngs = new String[num];
		String[] lats = new String[num];
		String[] enti_no = new String[num];
		String[] enti_names = new String[num];
		String[] enti_addrs = new String[num];//地址
		String[] enti_diss = new String[num];//距离
		String[] enti_type = new String[num];//如果企业有分类的话，放在这个数组
		for(int i=0;i<num;i++)
		{
			lngs[i]=(reslist.get(i).get("lng")).toString();
			lats[i]=(reslist.get(i).get("lat")).toString();
			enti_no[i]=i+"";
			enti_names[i]=(String)(reslist.get(i).get("comname"));
			enti_addrs[i]=(String)(reslist.get(i).get("address"));
			try
			{
				enti_diss[i]=getDistance(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(lats[i]), Double.parseDouble(lngs[i]));
			}catch(Exception e)
			{
				Toast.makeText(this, "解析数据有误！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			enti_type[i]=(String)(reslist.get(i).get("type"));
		}
		intent.putExtra("lngs", lngs);
		intent.putExtra("lats", lats);
		intent.putExtra("nos", enti_no);
		intent.putExtra("names", enti_names);
		intent.putExtra("addrs", enti_addrs);
		intent.putExtra("diss", enti_diss);
		intent.putExtra("types", enti_type);
		intent.putExtra("searchtype", searchtype);
		startActivity(intent);
	}
	
	private String getDistance(double lat1, double lng1, double lat2, double lng2)
	{
		double EARTH_RADIUS = 6378.137;//地球半径
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
				Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		   

		return s+"";
	}
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	} 
	
	class myTask extends AsyncTask<String, Void, List<Map<String,Object>>>
	{
		

		@Override
		protected void onPreExecute() {
		    prDialog = new ProgressDialog(DiscoverNearbyListActivity.this);
		    prDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    prDialog.setMessage("查询中……");

		    // 进度条是否不明确
		    prDialog.setIndeterminate(true);
		    prDialog.setCancelable(true);
		    prDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<Map<String,Object>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			reslist = result;
			if(result == null)
			{
				result = new ArrayList<Map<String,Object>>();
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("comname", "未查询到相关结果！");
				result.add(map);
				listviewadapter adapter = new listviewadapter(result);
				
				listview.setAdapter(adapter);
				return;
			}
			
			//listview显示列表
			listviewadapter adapter = new listviewadapter(result);
			
			listview.setAdapter(adapter);
			//listview.refreshDrawableState();
			
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 * params[0] url
		 */
		@Override
		protected List<Map<String,Object>> doInBackground(String... params) {
			Log.v("url", params[0]);
			MyLog.i(params[0]);
			String ret = ApiClient.connServerForResult(params[0]);
			List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
			try {
				if(searchtype.equals("1"))//药店
				{
					JSONObject jsonobj = new JSONObject(ret);
					boolean flag = jsonobj.getBoolean("flag");
					if(!flag)
					{
						prDialog.cancel();
						return null;
					}
					JSONArray jsonarr = jsonobj.getJSONArray("areas");
					for(int i=0;i<jsonarr.length();i++)
					{
						JSONObject itemobj = jsonarr.getJSONObject(i);
						String comname = itemobj.getString("name");
						String address = itemobj.getString("address");
						String type = itemobj.getString("type");
						double lng = itemobj.getDouble("x");
						double lat = itemobj.getDouble("y");
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("comname", comname);
						map.put("address", address);
						map.put("type", type);
						map.put("lng", lng);
						map.put("lat", lat);
						res.add(map);
					}
				}else if(searchtype.equals("2"))//环境监控企业
				{
					JSONObject jsonobj = new JSONObject(ret);
					boolean flag = jsonobj.getBoolean("flag");
					if(!flag)
					{
						prDialog.cancel();
						return null;
					}
					JSONArray jsonarr = jsonobj.getJSONArray("areas");
					for(int i=0;i<jsonarr.length();i++)
					{
						JSONObject itemobj = jsonarr.getJSONObject(i);
						String comname = itemobj.getString("name");
						String address = itemobj.getString("address");
						String type = itemobj.getString("type");
						double lng = itemobj.getDouble("x");
						double lat = itemobj.getDouble("y");
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("comname", comname);
						map.put("address", address);
						map.put("type", type);
						map.put("lng", lng);
						map.put("lat", lat);
						res.add(map);
					}
				}else if(searchtype.equals("3"))//爱康体检
				{
					JSONObject jsonobj = new JSONObject(ret);
					boolean flag = jsonobj.getBoolean("flag");
					if(!flag)
					{
						prDialog.cancel();
						return null;
					}
					JSONArray jsonarr = jsonobj.getJSONArray("areas");
					for(int i=0;i<jsonarr.length();i++)
					{
						JSONObject itemobj = jsonarr.getJSONObject(i);
						String comname = itemobj.getString("name");
						String address = itemobj.getString("address");
						String type = itemobj.getString("type");
						double lng = itemobj.getDouble("x");
						double lat = itemobj.getDouble("y");
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("comname", comname);
						map.put("address", address);
						map.put("type", type);
						map.put("lng", lng);
						map.put("lat", lat);
						res.add(map);
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				prDialog.cancel();
				return null;
			}
			prDialog.cancel();
			return res;
		}
		
	}
	
	public class listviewadapter extends BaseAdapter
	{
		private List<Map<String,Object>> listitems;
		

		public listviewadapter(List<Map<String,Object>> res) {
			super();
			// TODO Auto-generated constructor stub
			listitems = res;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listitems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listitems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int idx, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Map<String,Object> map = listitems.get(idx);
			view = View.inflate(DiscoverNearbyListActivity.this,R.layout.discover_nearby_buttom, null);
			initviewitem(idx,view,map);
			return view;
		}
		
		private void initviewitem(int idx,View v,Map map)
		{
			final String comname = (String)map.get("comname");
			final String comcity = (String)map.get("city");//仅限用于环境监测站
			final String addr = (String)map.get("addr");//仅限用于环境监测站
			final String type = (String) map.get("type");
			TextView txtid = (TextView)v.findViewById(R.id.txt_id);
			TextView txtname = (TextView)v.findViewById(R.id.txt_itemname);
			TextView txtmore = (TextView)v.findViewById(R.id.txt_moredetial);
			ImageView tubiaoView = (ImageView) v.findViewById(R.id.huanjingjk_tubiao);
			if(comname.equals("未查询到相关结果！"))
			{
				txtid.setText("");
				txtname.setText((String)map.get("comname"));
				txtmore.setVisibility(TextView.GONE);
				return;
			}
			//txtid.setText(idx+1+"");
			txtid.setText((String)map.get("comname"));
			txtname.setText((String)map.get("address"));
/*			if(r.contains("@")){
				txtmore.setText("当前城市所有");
			}else {
				txtmore.setText(r+"米");
			}*/
			if(type.equals("1")){
			tubiaoView.setBackgroundResource(R.drawable.huanjingjk01);
			}else if(type.equals("2")){
			tubiaoView.setBackgroundResource(R.drawable.huanjingjk02);
			}else if(type.equals("3")){
			tubiaoView.setBackgroundResource(R.drawable.huanjingjk03);
			}else if(type.equals("4")){
				tubiaoView.setBackgroundResource(R.drawable.huanjingjk04);
			}else if(type.equals("5")){
				tubiaoView.setBackgroundResource(R.drawable.huanjingjk05);
		}

		}
		
	}

}

