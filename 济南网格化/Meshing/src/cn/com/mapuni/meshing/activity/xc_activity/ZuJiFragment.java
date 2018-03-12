package cn.com.mapuni.meshing.activity.xc_activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.security.auth.callback.Callback;

import cn.com.mapuni.meshing.activity.gis.MapBinder;
import cn.com.mapuni.meshing.activity.gis.LocationService.CallBackAdrrPoint;
import cn.com.mapuni.meshing.activity.photo.PicViewerActivity;
import cn.com.mapuni.meshing.model.ImageModel;
import cn.com.mapuni.meshing.model.ImageModel.ImaesBean;
import cn.com.mapuni.meshing.model.ZuJiModel;
import cn.com.mapuni.meshing.model.ZuJiModel.RowsBean;
import cn.com.mapuni.meshing.util.DateTimePickDialogUtil;
import cn.com.mapuni.meshing.util.FlowLayout;

import com.bumptech.glide.Glide;
import com.example.meshing.R;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.overlay.MarkerOverlay;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class ZuJiFragment extends BaseFragment implements OnClickListener{
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 101:
				yutuLoading = new YutuLoading(context);
				yutuLoading.setCancelable(true);
				yutuLoading.setLoadMsg("正在获取签到数据，请稍候", "");
				yutuLoading.showDialog();
				break;
			case 102:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				adapter = new myAdapter();
				lv_zuji.setAdapter(adapter);
				if (rows!=null&&rows.size()>0) {
					int size = rows.size();
					tv_unregister.setText(size+"");					
					tv_register.setText("1");					
					Toast.makeText(context, "数据获取成功!", 1000).show();
				}else {
					Toast.makeText(context, "当前时间节点没有数据", 1000).show();
				}		
				
				break;
			case 103:
				if (null != yutuLoading) {
					yutuLoading.dismissDialog();
				}
				Toast.makeText(context, "数据获取失败，请稍后重试", 1000).show();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 基础中间布局，以供子类填充d
	 */
	protected LinearLayout middleLayout;
	private View mainView;
	private ListView lv_zuji;
	private List<String> datas;
	private LinearLayout ll_time;
	private TextView tv_time;
	private DateTimePickDialogUtil util;
	private YutuLoading yutuLoading;
	
	private List<RowsBean> rows;
	private List<ImaesBean> rowsImages;
	private TextView tv_unregister,tv_register;
	private MapView mapview;
	boolean isInit=false;
	public myAdapter adapter;
	@Override
	public View initView() {
		util=new DateTimePickDialogUtil(getActivity(),null);
		View mainView=View.inflate(context,R.layout.activity_zuji,null);
		mapview = (MapView) mainView.findViewById(R.id.mapview);
		lv_zuji = (ListView) mainView.findViewById(R.id.lv_zuji);
		ll_time = (LinearLayout) mainView.findViewById(R.id.ll_time);		//时间选择
		tv_time = (TextView) mainView.findViewById(R.id.tv_time);			//时间显示
		tv_unregister = (TextView) mainView.findViewById(R.id.tv_unregister);	//每日签到
		tv_register=(TextView) mainView.findViewById(R.id.tv_register);			//最新签到
		tv_time.setText(util.getCurTime());
		ll_time.setOnClickListener(this);

		setMyPostionMaker();
		return mainView;
	}
	@Override
	public void initListener() {
		
		
	}
	private HttpUtils httpUtils;
	public void initData() {
		handler.sendEmptyMessage(101);		
		Calendar calendar=Calendar.getInstance();  //获取当前时间，作为参数shang'chu
        String year=calendar.get(Calendar.YEAR)+"";
        int i = calendar.get(Calendar.MONTH);
        int d=calendar.get(Calendar.DAY_OF_MONTH);
        String month="";
        String day="";
        if (i<9) {
			month="0"+(i+1);
		}else{
			month=i+1+"";
		} 
        if (d<10) {
			day="0"+d;
		}else {
			day=d+"";
		}
		httpUtils = new HttpUtils();
		RequestParams params=new RequestParams();
		params.addBodyParameter("sessionId", DisplayUitl.readPreferences(context,"lastuser", "sessionId"));
		params.addBodyParameter("gridCode", DisplayUitl.readPreferences(context,"lastuser", "organization_code"));
		//+"-"+calendar.get(Calendar.DAY_OF_MONTH)
		params.addBodyParameter("selectTime",year+"-"+month);
		params.addBodyParameter("dayTime",year+"-"+month+"-"+day);

		httpUtils.send(HttpMethod.POST,PathManager.ZUJI_URL,params,new RequestCallBack<String>() {		
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(103);
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String data = arg0.result.toString().toString();
				if (data!="") {
					Gson gson=new Gson();
					ZuJiModel fromJson = gson.fromJson(data,ZuJiModel.class);
					rows = fromJson.getRows();
					handler.sendEmptyMessage(102);
				}		
			}
		});		
		httpUtils.send(HttpMethod.POST,PathManager.ZUJI_IMG_URL,params,new RequestCallBack<String>() {			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String imagePath = arg0.result.toString();
				if (imagePath!="") {
					Gson gson=new Gson();
					ImageModel imageModel = gson.fromJson(imagePath,ImageModel.class);
					rowsImages = imageModel.getRows();
					
				}
				
			}
		});
		
	}
	private void initDataAgain() {
		final YutuLoading loading=new YutuLoading(context);
		Calendar calendar = util.getCalendarByInintData(tv_time.getText().toString());
		int i = calendar.get(Calendar.MONTH);
		int d=calendar.get(Calendar.DAY_OF_MONTH);
		String month="";
		String day="";
		if (i<9) {
			month="0"+(i+1);
		}else{
			month=i+1+"";
		}
		if (d<10) {
			day="0"+d;
		}else {
			day=d+"";
		}
		//联网获取数据
		HttpUtils httpUtils=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addBodyParameter("sessionId", DisplayUitl.readPreferences(context,"lastuser", "sessionId"));
		params.addBodyParameter("gridCode", DisplayUitl.readPreferences(context,"lastuser", "organization_code"));
		params.addBodyParameter("selectTime", calendar.get(Calendar.YEAR)+"-"+month.trim());
		params.addBodyParameter("dayTime",calendar.get(Calendar.YEAR)+"-"+month.trim()+"-"+day.trim());
		httpUtils.send(HttpMethod.POST,PathManager.ZUJI_URL,params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				loading.dismissDialog();					
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String data = arg0.result.toString().toString();
					Gson gson=new Gson();
					ZuJiModel fromJson = gson.fromJson(data,ZuJiModel.class);
					rows = fromJson.getRows();
					adapter.notifyDataSetChanged();
					loading.dismissDialog();
					tv_unregister.setText(rows.size()+"");
			}
		});
		
		httpUtils.send(HttpMethod.POST,PathManager.ZUJI_IMG_URL,params,new RequestCallBack<String>() {

			

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String imagePath = arg0.result.toString();
				if (imagePath!="") {
					Gson gson=new Gson();
					ImageModel imageModel = gson.fromJson(imagePath,ImageModel.class);
					rowsImages = imageModel.getRows();
				}
				
			}
		});
		
	}
	public class myAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rows.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return rows.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {	
				convertView=View.inflate(context,R.layout.zuji_list_item,null);			
				TextView tv_username=(TextView) convertView.findViewById(R.id.tv_username);
				TextView tv_jgqy=(TextView) convertView.findViewById(R.id.tv_jgqy);
				FlowLayout fl_save=(FlowLayout) convertView.findViewById(R.id.fl_save);
				final TextView tv_useraddress=(TextView) convertView.findViewById(R.id.tv_useraddress);
				LinearLayout ll_save=(LinearLayout) convertView.findViewById(R.id.ll_save);
				RowsBean model = rows.get(position);
				tv_username.setText(model.getName());		//用户名
				tv_jgqy.setText(model.getEntName());		//监管企业
				String time = util.initStringTime(model.getCreateTime());	//签到时间			
				tv_useraddress.setText(model.getAddress());					//签到地址
				LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(
						120, 120);
				params_img.gravity = Gravity.CENTER;
				params_img.setMargins(10, 1, 5, 5);
				//获取图片资源
				 final ArrayList<String> imgPaths1 = new ArrayList<String>();// 图片路径集合
				ImageView imageView;
				for (int i = 0; i <rowsImages.size(); i++) {
					if ( rowsImages.get(i).getSignedRecodId().toString().trim().equals(model.getId().toString().trim())) {					
						if(rowsImages.get(i).getImgurl()!=null) {
							ll_save.setVisibility(View.VISIBLE);
													
								imageView = new ImageView(context);
								imageView.setLayoutParams(params_img);
								imageView.setScaleType(ScaleType.CENTER_CROP);
								final String imagePath = PathManager.IMG_URL_JINAN +rowsImages.get(i).getImgurl();
								imgPaths1.add(imagePath);
								Glide.with(context)
										.load(imagePath)
										.placeholder(R.drawable.wd_xcjl_jzz)
										.error(R.drawable.wd_xcjl_jzsb).into(imageView);
								fl_save.addView(imageView);
					}
				}
				}
				fl_save.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,
								PicViewerActivity.class);
						intent.putStringArrayListExtra("imgPaths", imgPaths1);
						startActivity(intent);
					}
				});
					
					
				
			return convertView;
		}
		
	}
	public class viewHolder{
		private TextView tv_username,tv_useraddress;
		private FlowLayout fl_save;
		private LinearLayout ll_save;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_time:
			util.dateTimePicKDialog(tv_time);		//点击弹出时间选择器	
			tv_time.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					initDataAgain();
					
				}				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			break;

		default:
			break;
		}
		
	}
	private void setMyPostionMaker() {
		MapBinder.getInstance().getBinder().CallBackAdrrPoint(new CallBackAdrrPoint() {
			@Override
			public void CallBackAdrrPoint(GeoPoint mGeoPoint) {
				addMyOverlayItem(mGeoPoint);
				mapview.getController().setZoom(12);
			}
		});
	}

	private void addMyOverlayItem(GeoPoint point) {
		MarkerOverlay overlay = new MarkerOverlay();
		overlay.setPosition(point);
		overlay.setIcon(getResources().getDrawable(R.drawable.postion_icon));
		mapview.addOverlay(overlay);
	}
}
