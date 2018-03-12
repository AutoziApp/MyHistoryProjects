package com.jy.environment.adapter;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LineGraph.OnPointClickedListener;
import com.echo.holographlibrary.LinePoint;
import com.jy.environment.R;
import com.jy.environment.activity.DiscoverPubServiceNewsActivity;
import com.jy.environment.activity.EnvironmentDrawDialActivity;
import com.jy.environment.activity.WebviewloadActivity;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.model.CurrentWeather;
import com.jy.environment.model.LifeItem;
import com.jy.environment.model.Trend;
import com.jy.environment.util.CommonUtil;
import com.jy.environment.util.MyLog;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

public class WeatherAdapter extends BaseAdapter {
	private static final int TYPE_1 = 0;
	private static final int TYPE_2 = 1;
	private static final int TYPE_3 = 2;
	// private static final int PRECIPITATION = 0;
	private static final int SUN_MOON = 4;
	private LayoutInflater mLayoutInflater;
	private int screenHalfWidth;
	private int screenWidth;
	private int screenHalfheigh12;
	private int screenHalfheigh8;
	private List<Integer> mTags;
	LinearLayout life_cy;
	LinearLayout life_cl;
	LinearLayout life_zwx;
	LinearLayout life_ly;
	LinearLayout life_ss;
	// public static String userName = "";
	// public static String userPwd = "";
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
	boolean flagw = false;
	private Map<Integer, Boolean> isshengMap = new HashMap<Integer, Boolean>();

	private WebView webView;

	public WeatherAdapter() {
		// TODO Auto-generated constructor stub
	}

	private WeiBaoApplication mApplication = WeiBaoApplication.getInstance();
	private int[] icon = new int[] { R.drawable.icon_guide_cy,
			R.drawable.icon_guide_cl, R.drawable.icon_guide_uv,
			R.drawable.icon_guide_tr, R.drawable.icon_guide_co,
			R.drawable.icon_guide_zs };
	private int[] icon_press = new int[] { R.drawable.icon_guide_cy_press,
			R.drawable.icon_guide_cl_press, R.drawable.icon_guide_uv_press,
			R.drawable.icon_guide_tr_press, R.drawable.icon_guide_co_press };
	private TextView[] tv1 = new TextView[5];
	private TextView[] tv2 = new TextView[5];
	private TextView[] tv3 = new TextView[5];
	private ImageView[] iv1 = new ImageView[5];
	private ImageView[] iv2 = new ImageView[5];
	private ImageView[] iv3 = new ImageView[5];
	Context context;
	CurrentWeather result;
	DisplayMetrics metric = new DisplayMetrics();
	private DBManager dbManager;
	String picicon = "";
	private String[] iconshen = new String[] {
			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/discover_jiankang.png",
			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/discover_laji.png",
			"http://www.shenbianer.com.cn:8080/weibaomp/image/public/20141223060439966.PNG",
			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/day_paiming.png",
			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/discover_huanbao.png" };
	private String[] iconmicro = new String[] {
			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/discover_jiankang.png",
			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/discover_laji.png",
			"http://www.micromap.com.cn:8080/weibaomp/image/public/20141223060439966.PNG",
			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/day_paiming.png",
			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/discover_huanbao.png" };

	public WeatherAdapter(Context context, CurrentWeather result,
			int screenHalfheigh12, int screenHalfheigh8, int screenWidth) {
		dbManager = DBManager.getInstances(context);
		mTags = new ArrayList<Integer>();
		mTags.add(TYPE_1);
		mTags.add(TYPE_2);
//		mTags.add(TYPE_3);
		this.context = context;
		this.screenHalfheigh12 = screenHalfheigh12;
		this.screenHalfheigh8 = screenHalfheigh8;
		this.screenWidth = screenWidth;
		this.result = result;
		// mTags.add(SUN_MOON);
		mLayoutInflater = LayoutInflater.from(context);
		configCheckMap(false);
	}

	public int getDisplayHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int displayHeight = wm.getDefaultDisplay().getHeight();
		return displayHeight;
	}

	@Override
	public int getCount() {
		return mTags.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_1;
		}
		if (position == 1) {
			return TYPE_2;
		}

		return TYPE_3;

	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Integer getItem(int position) {
		return mTags.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			
		int itemType = getItemViewType(position);
		List<Trend> trends = result.getTrends();
		LifeItem item = result.getLife();
		switch (itemType) {
		case TYPE_1:
			/*
			 * convertView = mLayoutInflater.inflate(R.layout.activity_main,
			 * null); activity_main(convertView, trends);
			 */
			convertView = mLayoutInflater.inflate(R.layout.linegraph, parent,
					false);
			linegraph(convertView, trends);
			break;
		case TYPE_2:
			/*
			 * convertView = mLayoutInflater.inflate(R.layout.linegraph, parent,
			 * false); linegraph(convertView, trends);
			 */
			convertView = mLayoutInflater.inflate(R.layout.life, parent, false);
			life(convertView, item);
			break;
		case TYPE_3:
			convertView = mLayoutInflater.inflate(R.layout.weibao_shop, parent,
					false);
//			shop(convertView, item);
			RelativeLayout shangcheng;
			RelativeLayout cezaosheng;
			RelativeLayout huanjingnews;
			RelativeLayout baike;
			shangcheng = (RelativeLayout)convertView.findViewById(R.id.shangcheng);
			cezaosheng = (RelativeLayout)convertView.findViewById(R.id.cezaosheng);
			huanjingnews = (RelativeLayout)convertView.findViewById(R.id.huanjingnews);
			baike = (RelativeLayout)convertView.findViewById(R.id.baike);
			baike.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent7 = new Intent(context,
							DiscoverPubServiceNewsActivity.class);
					boolean picflag = false;
					String picUrl = UrlComponent.baseurl;
					picflag = picUrl.contains("shenbianer") ? false : true;
					intent7.putExtra("guanzhu", true);
					intent7.putExtra("publicID", "27");
					intent7.putExtra("name", "环保百科");
					picicon = picflag ? iconmicro[0] : iconshen[0];
					intent7.putExtra("public_photo", picicon);
					intent7.putExtra("fuction", "提供环保有关的，亲民的环境知识分享，经验交流。");
					intent7.putExtra("biaozhi", "now");
					context.startActivity(intent7);
				}
			});
			huanjingnews.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent5 = new Intent(context,
							DiscoverPubServiceNewsActivity.class);
					intent5.putExtra("guanzhu", true);
					intent5.putExtra("publicID", "32");
					boolean picflag = false;
					String picUrl = UrlComponent.baseurl;
					picflag = picUrl.contains("shenbianer") ? false : true;
					intent5.putExtra("name", "一周国际环境要闻");
					picicon = picflag ? iconmicro[0] : iconshen[0];
					intent5.putExtra("public_photo", picicon);
					intent5.putExtra(
							"fuction",
							"国际中国环境基金会的目标是通过解决中国的环境问题来确保健康的全球环境以及可持续发展的经济。基金会主要业务包含：政策建议和咨询，技术交流与合作，民间环保组织发展，以及公共环境教育。");
					intent5.putExtra("biaozhi", "now");
					context.startActivity(intent5);
					
				}
			});
			cezaosheng.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,EnvironmentDrawDialActivity.class
							);
					context.startActivity(intent);
				}
			});
			shangcheng.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							WebviewloadActivity.class);
					String url = "http://www.iweibao.com.cn?ref=2";
					intent.putExtra("url", url);
//					if (WeiBaoApplication.getUsename().equals("")) {
//						url = "http://www.iweibao.com.cn?ref=2";
//						intent.putExtra("url", url);
//					} else {
//						SharedPreferences share = context.getSharedPreferences(
//								"MAP_SHARE_LOGIN_TAG", 0);
//						String type = share.getString("MAP_LOGIN_TYPE",
//								"WEIBAO");
//						String userName = share.getString("MAP_LOGIN_USERNAME",
//								"");
//						MyLog.i("xu1" + "userName" + userName);
//						MyLog.i("xu1" + "type" + type);
//						MyLog.i("xu1" + "share" + share);
//						MyLog.i("xu1" + "WeiBaoApplication.getIsEmailBind()"
//								+ WeiBaoApplication.getIsEmailBind());
//						if (type.equals("QQ")) {
//							String userIdQQ = share.getString("MAP_LOGIN_QQID",
//									"");
//							if (WeiBaoApplication.getIsEmailBind().equals("1")) {
//								url = UrlComponent.getIsBindedMailLogin(
//										userName, userIdQQ);
//							} else {
//								url = UrlComponent.getShareLogin(userName,
//										userIdQQ);
//							}
//
//						} else {
//							String pwd = share.getString("MAP_LOGIN_PASSWORD",
//									"");
//							if (WeiBaoApplication.getIsEmailBind().equals("1")) {
//								url = UrlComponent.getIsBindedMailLogin(
//										userName, getMD5Str(pwd));
//							} else {
//								url = UrlComponent.getShareLogin(userName,
//										getMD5Str(pwd));
//							}
//
//						}
//						intent.putExtra("url", url);
//					}
					MyLog.i("xu1" + "未绑定" + url);
					context.startActivity(intent);
				}
			});
			break;
		default:
			break;
		}
		// convertView.setTag(R.string.app_name, R.drawable.ic_launcher
		// + getItemViewType(position));
	} catch (Exception e) {
		MyLog.e("weibao Exception", e);
	}
		return convertView;
	}

	// @Override
	// public int getItemViewType(int position) {
	// if (position >= mTags.size())
	// return -1;
	// return mTags.get(position);
	// }
	public void configCheckMap(boolean bool) {

		for (int i = 0; i < 5; i++) {
			isCheckMap.put(i, bool);
			isshengMap.put(i, bool);
		}

	}

	// MD5
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}

	private void activity_main(View view, List<Trend> trends) {
		TextView activity_main_tv1 = (TextView) view
				.findViewById(R.id.activity_main_tv1);
		TextView activity_main_tv2 = (TextView) view
				.findViewById(R.id.activity_main_tv2);
		TextView activity_main_tv3 = (TextView) view
				.findViewById(R.id.activity_main_tv3);
		TextView activity_main_tv4 = (TextView) view
				.findViewById(R.id.activity_main_tv4);
		TextView activity_main_tv5 = (TextView) view
				.findViewById(R.id.activity_main_tv5);
		TextView activity_main_tv6 = (TextView) view
				.findViewById(R.id.activity_main_tv6);
		TextView activity_main_tv7 = (TextView) view
				.findViewById(R.id.activity_main_tv7);
		TextView activity_main_tv8 = (TextView) view
				.findViewById(R.id.activity_main_tv8);
		TextView activity_main_tv9 = (TextView) view
				.findViewById(R.id.activity_main_tv9);
		TextView activity_main_tv10 = (TextView) view
				.findViewById(R.id.activity_main_tv10);
		ImageView activity_main_iv1 = (ImageView) view
				.findViewById(R.id.activity_main_iv1);
		ImageView activity_main_iv2 = (ImageView) view
				.findViewById(R.id.activity_main_iv2);
		ImageView activity_main_iv3 = (ImageView) view
				.findViewById(R.id.activity_main_iv3);
		ImageView activity_main_iv4 = (ImageView) view
				.findViewById(R.id.activity_main_iv4);
		ImageView activity_main_iv5 = (ImageView) view
				.findViewById(R.id.activity_main_iv5);
		activity_main_tv1.setText(trends.get(1).getWeek());
		activity_main_tv6.setText(trends.get(1).getDate());
		activity_main_tv2.setText(trends.get(2).getWeek());
		activity_main_tv7.setText(trends.get(2).getDate());
		activity_main_tv3.setText(trends.get(3).getWeek());
		activity_main_tv8.setText(trends.get(3).getDate());
		activity_main_tv4.setText(trends.get(4).getWeek());
		activity_main_tv9.setText(trends.get(4).getDate());
		activity_main_tv5.setText(trends.get(5).getWeek());
		activity_main_tv10.setText(trends.get(5).getDate());
		activity_main_iv1.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(1).getWeather())));
		activity_main_iv2.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(2).getWeather())));
		activity_main_iv3.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(3).getWeather())));
		activity_main_iv4.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(4).getWeather())));
		activity_main_iv5.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(5).getWeather())));
	}

	private void linegraph(View view, List<Trend> trends) {
		TextView linegraph_tv1 = (TextView) view
				.findViewById(R.id.linegraph_tv1);
		TextView linegraph_tv2 = (TextView) view
				.findViewById(R.id.linegraph_tv2);
		TextView linegraph_tv3 = (TextView) view
				.findViewById(R.id.linegraph_tv3);
		TextView linegraph_tv4 = (TextView) view
				.findViewById(R.id.linegraph_tv4);
		TextView linegraph_tv5 = (TextView) view
				.findViewById(R.id.linegraph_tv5);
		TextView linegraph_tv6 = (TextView) view
				.findViewById(R.id.linegraph_tv6);
		TextView linegraph_tv7 = (TextView) view
				.findViewById(R.id.linegraph_tv7);
		TextView linegraph_tv8 = (TextView) view
				.findViewById(R.id.linegraph_tv8);
		TextView linegraph_tv9 = (TextView) view
				.findViewById(R.id.linegraph_tv9);
		TextView linegraph_tv10 = (TextView) view
				.findViewById(R.id.linegraph_tv10);
		TextView linegraph_tv11 = (TextView) view
				.findViewById(R.id.linegraph_tv11);
		TextView linegraph_tv12 = (TextView) view
				.findViewById(R.id.linegraph_tv12);
		ImageView activity_main_iv0 = (ImageView) view
				.findViewById(R.id.activity_main_iv0);
		ImageView activity_main_iv1 = (ImageView) view
				.findViewById(R.id.activity_main_iv1);
		ImageView activity_main_iv2 = (ImageView) view
				.findViewById(R.id.activity_main_iv2);
		ImageView activity_main_iv3 = (ImageView) view
				.findViewById(R.id.activity_main_iv3);
		ImageView activity_main_iv4 = (ImageView) view
				.findViewById(R.id.activity_main_iv4);
		ImageView activity_main_iv5 = (ImageView) view
				.findViewById(R.id.activity_main_iv5);
		linegraph_tv1.setText(trends.get(0).getWeek());
		linegraph_tv2.setText(trends.get(1).getWeek());
		linegraph_tv3.setText(trends.get(2).getWeek());
		linegraph_tv4.setText(trends.get(3).getWeek());
		linegraph_tv5.setText(trends.get(4).getWeek());
		linegraph_tv6.setText(trends.get(0).getDate());
		linegraph_tv7.setText(trends.get(1).getDate());
		linegraph_tv8.setText(trends.get(2).getDate());
		linegraph_tv9.setText(trends.get(3).getDate());
		linegraph_tv10.setText(trends.get(4).getDate());
		linegraph_tv11.setText(trends.get(5).getWeek());
		linegraph_tv12.setText(trends.get(5).getDate());
		activity_main_iv0.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(0).getWeather())));
		activity_main_iv1.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(1).getWeather())));
		activity_main_iv2.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(2).getWeather())));
		activity_main_iv3.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(3).getWeather())));
		activity_main_iv4.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(4).getWeather())));
		activity_main_iv5.setImageDrawable(context.getResources().getDrawable(
				getWeatherIcon(trends.get(5).getWeather())));
		/*
		 * int[] location0 = new int[2]; int[] location1 = new int[2]; int[]
		 * location2 = new int[2]; int[] location3 = new int[2]; int[] location4
		 * = new int[2]; int[] location5 = new int[2];
		 * activity_main_iv0.getLocationOnScreen(location0);
		 * MyLog.i("==================location0"+location0[0]);
		 * activity_main_iv1.getLocationOnScreen(location1);
		 * activity_main_iv2.getLocationOnScreen(location2);
		 * activity_main_iv3.getLocationOnScreen(location3);
		 * activity_main_iv4.getLocationOnScreen(location4);
		 * activity_main_iv5.getLocationOnScreen(location5); int[] location_all
		 * = new int[6]; location_all[0] = location0[0]; location_all[1] =
		 * location1[0]; location_all[2] = location2[0]; location_all[3] =
		 * location3[0]; location_all[4] = location4[0]; location_all[5] =
		 * location5[0];
		 */
		drawBitmap(trends, view);
	}

	private void life(View view, LifeItem item) {
		final TextView life_cy_tv2 = ((TextView) view
				.findViewById(R.id.life_cy_tv2));
		life_cy_tv2.setText("穿衣指数");
		final TextView life_cy_tv1 = ((TextView) view
				.findViewById(R.id.life_cy_tv1));
		life_cy_tv1.setText(item.getIndex_cy());
		final TextView life_cy_tv3 = (TextView) view
				.findViewById(R.id.life_cy_tv3);
		life_cy_tv3.setText(item.getIndex_cy_xs());
		final TextView life_cl_tv2 = ((TextView) view
				.findViewById(R.id.life_cl_tv2));
		life_cl_tv2.setText("晨练指数");
		final TextView life_cl_tv1 = ((TextView) view
				.findViewById(R.id.life_cl_tv1));
		life_cl_tv1.setText(item.getIndex_cl());
		final TextView life_cl_tv3 = (TextView) view
				.findViewById(R.id.life_cl_tv3);
		life_cl_tv3.setText(item.getIndex_cl_xs());
		final TextView life_zwx_tv2 = ((TextView) view
				.findViewById(R.id.life_zwx_tv2));
		life_zwx_tv2.setText("紫外线指数");
		final TextView life_zwx_tv1 = ((TextView) view
				.findViewById(R.id.life_zwx_tv1));
		life_zwx_tv1.setText(item.getIndex_uv());
		final TextView life_zwx_tv3 = (TextView) view
				.findViewById(R.id.life_zwx_tv3);
		life_zwx_tv3.setText(item.getIndex_uv_xs());
		final TextView life_ly_tv2 = ((TextView) view
				.findViewById(R.id.life_ly_tv2));
		life_ly_tv2.setText("旅游指数");
		final TextView life_ly_tv1 = ((TextView) view
				.findViewById(R.id.life_ly_tv1));
		life_ly_tv1.setText(item.getIndex_tr());
		final TextView life_ly_tv3 = (TextView) view
				.findViewById(R.id.life_ly_tv3);
		life_ly_tv3.setText(item.getIndex_tr_xs());
		final TextView life_ss_tv2 = ((TextView) view
				.findViewById(R.id.life_ss_tv2));
		life_ss_tv2.setText("舒适指数");
		final TextView life_ss_tv1 = ((TextView) view
				.findViewById(R.id.life_ss_tv1));
		life_ss_tv1.setText(item.getIndex_co());
		((TextView) view.findViewById(R.id.life_ss_tv3)).setText(item
				.getIndex_co_xs());
		final TextView life_ss_tv3 = (TextView) view
				.findViewById(R.id.life_ss_tv3);
		life_ss_tv3.setText(item.getIndex_co_xs());
		final ImageView life_cy_height = (ImageView) view
				.findViewById(R.id.life_cy_height);
		final ImageView life_cy_iv1 = (ImageView) view
				.findViewById(R.id.life_cy_iv1);
		life_cy_iv1.setImageDrawable(context.getResources()
				.getDrawable(icon[0]));
		final ImageView life_cy_iv2 = (ImageView) view
				.findViewById(R.id.life_cy_iv2);
		final ImageView life_cl_height = (ImageView) view
				.findViewById(R.id.life_cl_height);
		final ImageView life_cl_iv1 = (ImageView) view
				.findViewById(R.id.life_cl_iv1);
		life_cl_iv1.setImageDrawable(context.getResources()
				.getDrawable(icon[1]));
		final ImageView life_cl_iv2 = (ImageView) view
				.findViewById(R.id.life_cl_iv2);
		final ImageView life_zwx_height = (ImageView) view
				.findViewById(R.id.life_zwx_height);
		final ImageView life_zwx_iv1 = (ImageView) view
				.findViewById(R.id.life_zwx_iv1);
		life_zwx_iv1.setImageDrawable(context.getResources().getDrawable(
				icon[2]));
		final ImageView life_zwx_iv2 = (ImageView) view
				.findViewById(R.id.life_zwx_iv2);
		final ImageView life_ly_height = (ImageView) view
				.findViewById(R.id.life_ly_height);
		final ImageView life_ly_iv1 = (ImageView) view
				.findViewById(R.id.life_ly_iv1);
		life_ly_iv1.setImageDrawable(context.getResources()
				.getDrawable(icon[3]));
		final ImageView life_ly_iv2 = (ImageView) view
				.findViewById(R.id.life_ly_iv2);
		final ImageView life_ss_height = (ImageView) view
				.findViewById(R.id.life_ss_height);
		final ImageView life_ss_iv1 = (ImageView) view
				.findViewById(R.id.life_ss_iv1);
		life_ss_iv1.setImageDrawable(context.getResources()
				.getDrawable(icon[4]));
		final ImageView life_ss_iv2 = (ImageView) view
				.findViewById(R.id.life_ss_iv2);
		life_cy = (LinearLayout) view.findViewById(R.id.life_cy);
		life_cl = (LinearLayout) view.findViewById(R.id.life_cl);
		life_zwx = (LinearLayout) view.findViewById(R.id.life_zwx);
		life_ly = (LinearLayout) view.findViewById(R.id.life_ly);
		life_ss = (LinearLayout) view.findViewById(R.id.life_ss);
		{
			showSmallView(life_cy_tv3, life_cy_iv1, life_cy_height,
					life_cy_iv2, 0);
			showSmallView(life_cl_tv3, life_cl_iv1, life_cl_height,
					life_cl_iv2, 1);
			showSmallView(life_zwx_tv3, life_zwx_iv1, life_zwx_height,
					life_zwx_iv2, 2);
			showSmallView(life_ly_tv3, life_ly_iv1, life_ly_height,
					life_ly_iv2, 3);
			showSmallView(life_ss_tv3, life_ss_iv1, life_ss_height,
					life_ss_iv2, 4);
		}
		tv1[0] = life_cy_tv1;
		tv1[1] = life_cl_tv1;
		tv1[2] = life_zwx_tv1;
		tv1[3] = life_ly_tv1;
		tv1[4] = life_ss_tv1;
		tv2[0] = life_cy_tv2;
		tv2[1] = life_cl_tv2;
		tv2[2] = life_zwx_tv2;
		tv2[3] = life_ly_tv2;
		tv2[4] = life_ss_tv2;
		tv3[0] = life_cy_tv3;
		tv3[1] = life_cl_tv3;
		tv3[2] = life_zwx_tv3;
		tv3[3] = life_ly_tv3;
		tv3[4] = life_ss_tv3;
		iv1[0] = life_cy_iv1;
		iv1[1] = life_cl_iv1;
		iv1[2] = life_zwx_iv1;
		iv1[3] = life_ly_iv1;
		iv1[4] = life_ss_iv1;
		iv2[0] = life_cy_height;
		iv2[1] = life_cl_height;
		iv2[2] = life_zwx_height;
		iv2[3] = life_ly_height;
		iv2[4] = life_ss_height;
		iv3[0] = life_cy_iv2;
		iv3[1] = life_cl_iv2;
		iv3[2] = life_zwx_iv2;
		iv3[3] = life_ly_iv2;
		iv3[4] = life_ss_iv2;
		life_cy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shenghuoquan(life_cy_tv1, life_cy_tv2, life_cy_tv3,
						life_cy_iv1, life_cy_height, life_cy_iv2, 0);
			}
		});
		life_cl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(context, "HJLifeIdxClick");
				shenghuoquan(life_cl_tv1, life_cl_tv2, life_cl_tv3,
						life_cl_iv1, life_cl_height, life_cl_iv2, 1);
			}
		});
		life_zwx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(context, "HJLifeIdxClick");
				shenghuoquan(life_zwx_tv1, life_zwx_tv2, life_zwx_tv3,
						life_zwx_iv1, life_zwx_height, life_zwx_iv2, 2);
			}
		});
		life_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(context, "HJLifeIdxClick");
				shenghuoquan(life_ly_tv1, life_ly_tv2, life_ly_tv3,
						life_ly_iv1, life_ly_height, life_ly_iv2, 3);
			}
		});
		life_ss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(context, "HJLifeIdxClick");
				shenghuoquan(life_ss_tv1, life_ss_tv2, life_ss_tv3,
						life_ss_iv1, life_ss_height, life_ss_iv2, 4);
			}
		});
	}

//	private void shop(View view, LifeItem item) {
//
//		
//	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	private int getWeatherIcon(String climate) {
		int weatherIcon = R.drawable.weather_icon_qingtian;
		String climateString = CommonUtil.getWeatherIconString(climate,0);
		if (mApplication.getWeatherIconMap().containsKey(climateString)) {
			weatherIcon = mApplication.getWeatherIconMap().get(climateString);
		}
		return weatherIcon;
	}

	private void showSmallView(TextView life_cy_tv3, ImageView life_cy_iv1,
			ImageView life_cy_height, ImageView life_cy_iv2, int i) {

		LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) life_cy_iv1
				.getLayoutParams();
		life_cy_iv1.getLayoutParams();
		lParams.width = screenHalfheigh12;
		lParams.height = screenHalfheigh12;
		life_cy_iv1.setLayoutParams(lParams);
		LinearLayout.LayoutParams lParamsheight = (LinearLayout.LayoutParams) life_cy_height
				.getLayoutParams();
		life_cy_height.getLayoutParams();
		lParamsheight.width = 1;
		lParamsheight.height = screenHalfheigh12;
		life_cy_height.setLayoutParams(lParamsheight);
		life_cy_iv2.setBackgroundResource(R.drawable.life_down);
		life_cy_tv3.setVisibility(View.GONE);
	}

	private void shenghuoquan(TextView life_cy_tv1, TextView life_cy_tv2,
			TextView life_cy_tv3, ImageView life_cy_iv1,
			ImageView life_cy_height, ImageView life_cy_iv2, int i) {
		for (int j = 0; j < 5; j++) {
			if (isshengMap.get(j)) {
				if (i == j) {
					break;
				}
				shenghuoquan(tv1[j], tv2[j], tv3[j], iv1[j], iv2[j], iv3[j], j);
			}
		}
		if (life_cy_tv3.getVisibility() == View.GONE) {
			// TranslateAnimation mShowAction = new TranslateAnimation(
			// Animation.RELATIVE_TO_SELF, 0.5f,
			// Animation.RELATIVE_TO_SELF, 0.0f,
			// Animation.RELATIVE_TO_SELF, -1.0f,
			// Animation.RELATIVE_TO_SELF, 0.0f);
			isshengMap.put(i, true);
			TranslateAnimation mShowAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(500);
			life_cy_tv3.startAnimation(mShowAction);
			life_cy_tv3.setVisibility(View.VISIBLE);
			int height_in_pixels = life_cy_tv3.getLineHeight(); // approx
			// height
			// text
			/*
			 * LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)
			 * life_cy_iv1 .getLayoutParams(); life_cy_iv1.getLayoutParams();
			 */
			// life_cy_iv1.setImageDrawable(context.getResources().getDrawable(
			// icon_press[i]));
			// life_cy_tv3.setTextColor(Color.parseColor("#4bbdff"));
			// life_cy_tv1.setTextColor(Color.parseColor("#4bbdff"));
			// life_cy_tv2.setTextColor(Color.parseColor("#4bbdff"));
			/*
			 * lParams.width = screenHalfheigh8; lParams.height =
			 * screenHalfheigh8;
			 */

			// if (life_cy_tv3.getText().length() > 30) {
			// int times = (life_cy_tv3.getText().length() / 15 + 1);
			// life_cy_tv3.setHeight(height_in_pixels * times);
			// }
			// life_cy_iv1.setLayoutParams(lParams);

			LinearLayout.LayoutParams lParamsheight = (LinearLayout.LayoutParams) life_cy_height
					.getLayoutParams();
			life_cy_height.getLayoutParams();
			lParamsheight.width = 1;
			lParamsheight.height = screenHalfheigh12;
			// if (life_cy_tv3.getText().length() > 30) {
			// lParamsheight.height = (screenHalfheigh12 + (height_in_pixels *
			// 3));
			// }
			life_cy_height.setLayoutParams(lParamsheight);
			life_cy_iv2.setBackgroundResource(R.drawable.life_up);
		} else {
			isshengMap.put(i, false);
			life_cy_iv1.setImageDrawable(context.getResources().getDrawable(
					icon[i]));
			life_cy_tv1.setTextColor(Color.WHITE);
			life_cy_tv2.setTextColor(Color.WHITE);
			LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) life_cy_iv1
					.getLayoutParams();
			life_cy_iv1.getLayoutParams();
			lParams.width = screenHalfheigh12;
			lParams.height = screenHalfheigh12;
			life_cy_iv1.setLayoutParams(lParams);
			LinearLayout.LayoutParams lParamsheight = (LinearLayout.LayoutParams) life_cy_height
					.getLayoutParams();
			life_cy_height.getLayoutParams();
			lParamsheight.width = 1;
			lParamsheight.height = screenHalfheigh12;
			life_cy_height.setLayoutParams(lParamsheight);
			life_cy_iv2.setBackgroundResource(R.drawable.life_down);
			life_cy_tv3.setVisibility(View.GONE);
		}
	}



	/**
	 * 得到相差绝对值
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private float getMaxFloat(String min, String max) {
		return (float) Math.max(Float.valueOf(min), Float.valueOf(max));
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private void drawBitmap(List<Trend> weatherLists, View forcastView) {
		String tempArray[] = weatherLists.get(0).getTemp().replace("℃", "")
				.split("~");
		
		MyLog.i("bai tempArray[0] "+ tempArray[0]  +"bai tempArray[1] "+ tempArray[1]    ); 
		int high = 0;
		int low = 0;
		Line l1 = new Line();
		int x_index = screenWidth / 6;
		int x_index_half = x_index / 2;
		LinePoint p1 = new LinePoint(); // 高温曲线
		LinePoint p2 = new LinePoint(); // 低温曲线
		p1.setX(x_index_half);
		p1.setY(high = Math.max(Math.max(
				Math.max(CommonUtil.StringToInt(tempArray[0]),
						CommonUtil.StringToInt(tempArray[1])),
				CommonUtil.StringToInt(tempArray[1])), CommonUtil
				.StringToInt(tempArray[1])));
		l1.addPoint(p1);
		Line l2 = new Line();
		p2.setX(x_index_half);
		p2.setY(low = Math.min(CommonUtil.StringToInt(tempArray[1]),
				CommonUtil.StringToInt(tempArray[0])));
		l2.addPoint(p2);

		String tempArray2[] = weatherLists.get(1).getTemp().replace("℃", "")
				.split("~");
		p1 = new LinePoint();
		p1.setX(x_index_half + x_index);

		p1.setY(Math.max(CommonUtil.StringToInt(tempArray2[0]),
				CommonUtil.StringToInt(tempArray2[1])));
		high = high > Math.max(
				Math.max(CommonUtil.StringToInt(tempArray2[0]),
						CommonUtil.StringToInt(tempArray2[1])),
				CommonUtil.StringToInt(tempArray2[1])) ? high : Math.max(
				Math.max(CommonUtil.StringToInt(tempArray2[0]),
						CommonUtil.StringToInt(tempArray2[1])),
				CommonUtil.StringToInt(tempArray2[1]));
		l1.addPoint(p1);
		p2 = new LinePoint();
		p2.setX(x_index_half + x_index);

		MyLog.i("bai tempArray2[0] "+ tempArray2[0]  +"bai tempArray2[1] "+ tempArray2[1]    ); 
		p2.setY(Math.min(CommonUtil.StringToInt(tempArray2[1]),
				CommonUtil.StringToInt(tempArray2[0])));
		l2.addPoint(p2);
		low = low < Math.min(CommonUtil.StringToInt(tempArray2[1]),
				CommonUtil.StringToInt(tempArray2[0])) ? low : Math.min(
				CommonUtil.StringToInt(tempArray2[1]),
				CommonUtil.StringToInt(tempArray2[0]));

		String tempArray3[] = weatherLists.get(2).getTemp().replace("℃", "")
				.split("~");
		p1 = new LinePoint();
		p1.setX(x_index_half + x_index * 2);
		p1.setY(Math.max(
				Math.max(CommonUtil.StringToInt(tempArray3[0]),
						CommonUtil.StringToInt(tempArray3[1])),
				CommonUtil.StringToInt(tempArray3[1])));
		l1.addPoint(p1);
		high = high > Math.max(
				Math.max(CommonUtil.StringToInt(tempArray3[0]),
						CommonUtil.StringToInt(tempArray3[1])),
				CommonUtil.StringToInt(tempArray3[1])) ? high : Math.max(
				Math.max(CommonUtil.StringToInt(tempArray3[0]),
						CommonUtil.StringToInt(tempArray3[1])),
				CommonUtil.StringToInt(tempArray3[1]));
		p2 = new LinePoint();
		p2.setX(x_index_half + x_index * 2);

		p2.setY(Math.min(CommonUtil.StringToInt(tempArray3[1]),
				CommonUtil.StringToInt(tempArray3[0])));
		l2.addPoint(p2);
		low = low < Math.min(CommonUtil.StringToInt(tempArray3[1]),
				CommonUtil.StringToInt(tempArray3[0])) ? low : Math.min(
				CommonUtil.StringToInt(tempArray3[1]),
				CommonUtil.StringToInt(tempArray3[0]));

		MyLog.i("bai tempArray3[0] "+ tempArray3[0]  +"bai tempArray3[1] "+ tempArray3[1]    ); 
		String tempArray4[] = weatherLists.get(3).getTemp().replace("℃", "")
				.split("~");
		MyLog.i("bai tempArray4[0] "+ tempArray4[0]  +"bai tempArray4[1] "+ tempArray4[1]    ); 
		p1 = new LinePoint();
		p1.setX(x_index_half + x_index * 3);

		p1.setY(Math.max(
				Math.max(CommonUtil.StringToInt(tempArray4[0]),
						CommonUtil.StringToInt(tempArray4[1])),
				CommonUtil.StringToInt(tempArray4[1])));
		l1.addPoint(p1);
		high = high > Math.max(
				Math.max(CommonUtil.StringToInt(tempArray4[0]),
						CommonUtil.StringToInt(tempArray4[1])),
				CommonUtil.StringToInt(tempArray4[1])) ? high : Math.max(
				Math.max(CommonUtil.StringToInt(tempArray4[0]),
						CommonUtil.StringToInt(tempArray4[1])),
				CommonUtil.StringToInt(tempArray4[1]));
		p2 = new LinePoint();
		p2.setX(x_index_half + x_index * 3);

		p2.setY(Math.min(CommonUtil.StringToInt(tempArray4[1]),
				CommonUtil.StringToInt(tempArray4[0])));
		l2.addPoint(p2);
		low = low < Math.min(CommonUtil.StringToInt(tempArray4[1]),
				CommonUtil.StringToInt(tempArray4[0])) ? low : Math.min(
				CommonUtil.StringToInt(tempArray4[1]),
				CommonUtil.StringToInt(tempArray4[0]));

		String tempArray5[] = weatherLists.get(4).getTemp().replace("℃", "")
				.split("~");
		p1 = new LinePoint();
		p1.setX(x_index_half + x_index * 4);

		p1.setY(Math.max(
				Math.max(CommonUtil.StringToInt(tempArray5[0]),
						CommonUtil.StringToInt(tempArray5[1])),
				CommonUtil.StringToInt(tempArray5[1])));
		l1.addPoint(p1);
		high = high > Math.max(
				Math.max(CommonUtil.StringToInt(tempArray5[0]),
						CommonUtil.StringToInt(tempArray5[1])),
				CommonUtil.StringToInt(tempArray5[1])) ? high : Math.max(
				Math.max(CommonUtil.StringToInt(tempArray5[0]),
						CommonUtil.StringToInt(tempArray5[1])),
				CommonUtil.StringToInt(tempArray5[1]));
		p2 = new LinePoint();
		p2.setX(x_index_half + x_index * 4);

		p2.setY(Math.min(CommonUtil.StringToInt(tempArray5[1]),
				CommonUtil.StringToInt(tempArray5[0])));
		l2.addPoint(p2);
		low = low < Math.min(CommonUtil.StringToInt(tempArray5[1]),
				CommonUtil.StringToInt(tempArray5[0])) ? low : Math.min(
				CommonUtil.StringToInt(tempArray5[1]),
				CommonUtil.StringToInt(tempArray5[0]));

		String tempArray6[] = weatherLists.get(5).getTemp().replace("℃", "")
				.split("~");
		p1 = new LinePoint();
		p1.setX(x_index_half + x_index * 5);

		p1.setY(Math.max(
				Math.max(CommonUtil.StringToInt(tempArray6[0]),
						CommonUtil.StringToInt(tempArray6[1])),
				CommonUtil.StringToInt(tempArray6[1])));
		l1.addPoint(p1);
		high = high > Math.max(
				Math.max(CommonUtil.StringToInt(tempArray6[0]),
						CommonUtil.StringToInt(tempArray6[1])),
				CommonUtil.StringToInt(tempArray6[1])) ? high : Math.max(
				Math.max(CommonUtil.StringToInt(tempArray6[0]),
						CommonUtil.StringToInt(tempArray6[1])),
				CommonUtil.StringToInt(tempArray6[1]));
		p2 = new LinePoint();
		p2.setX(x_index_half + x_index * 5);

		p2.setY(Math.min(CommonUtil.StringToInt(tempArray6[1]),
				CommonUtil.StringToInt(tempArray6[0])));
		l2.addPoint(p2);
		low = low < Math.min(CommonUtil.StringToInt(tempArray6[1]),
				CommonUtil.StringToInt(tempArray6[0])) ? low : Math.min(
				CommonUtil.StringToInt(tempArray6[1]),
				CommonUtil.StringToInt(tempArray6[0]));

		// l1.setColor(Color.parseColor("#FF9900"));
		l1.setColor(Color.parseColor("#FFFFFF"));

		LineGraph li1 = (LineGraph) forcastView
				.findViewById(R.id.linegraph_graph);
		// li1.setPointYellow(true);
		l1.setHigh(true);
		li1.addLine(l1);
		li1.setRangeY(low - 10, high + 5);
		li1.setOnPointClickedListener(new OnPointClickedListener() {
			@Override
			public void onClick(int lineIndex, int pointIndex) {
			}
		});

		// l2.setColor(Color.parseColor("#0099CC"));
		l2.setColor(Color.parseColor("#FFFFFF"));

		LineGraph li2 = (LineGraph) forcastView
				.findViewById(R.id.linegraph_graph);
		// li2.setPointYellow(false);
		l2.setHigh(false);
		li2.addLine(l2);
		li2.setRangeY(low - 10, high + 5);

		li2.setOnPointClickedListener(new OnPointClickedListener() {
			@Override
			public void onClick(int lineIndex, int pointIndex) {
			}
		});
		MyLog.i("bai l1:" + l1);
		MyLog.i("bai l2:" + l2);
	}
}
