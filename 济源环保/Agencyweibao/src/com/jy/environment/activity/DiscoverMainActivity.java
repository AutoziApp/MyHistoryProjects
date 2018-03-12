package com.jy.environment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jy.environment.R;
import com.jy.environment.base.ActivityBase;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.services.NewsPushService;
import com.jy.environment.util.SharedPreferencesUtil;
import com.jy.environment.webservice.UrlComponent;
import com.umeng.analytics.MobclickAgent;

/**
 * 发现首页
 * 
 * @author baiyuchuan
 * 
 */
public class DiscoverMainActivity extends ActivityBase {
	
}
//		OnClickListener {
//
//	private RelativeLayout discover_huanjingshuo_layout;
//	private RelativeLayout discover_baoguang_layout;
//	private RelativeLayout discover_jiankang, discover_laji, discover_huanbao,
//			discover_more, discover_news, discover_paiming;
//	private String username;
//	private TextView explorer_tv1, explorer_tv2, explorer_tv3, explorer_tv4,
//			explorer_tv5;
//	private String userId, accept_news;
//	private DBManager dbManager;
//	private SharedPreferencesUtil util;
//	private String[] iconshen = new String[] {
//			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/discover_jiankang.png",
//			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/discover_laji.png",
//			"http://www.shenbianer.com.cn:8080/weibaomp/image/public/20141223060439966.PNG",
//			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/day_paiming.png",
//			"http://www.shenbianer.com.cn:8080/epservice/image/img/publicaccountinfo/discover_huanbao.png" };
//	private String[] iconmicro = new String[] {
//			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/discover_jiankang.png",
//			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/discover_laji.png",
//			"http://www.micromap.com.cn:8080/weibaomp/image/public/20141223060439966.PNG",
//			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/day_paiming.png",
//			"http://www.micromap.com.cn:8080/epservice/image/img/publicaccountinfo/discover_huanbao.png" };
//
//	/*
//	 * private News aNews; private MyAdapter adapter; private ListView explorer;
//	 * private long exitTime = 0; private String state; private boolean newsflag
//	 * = false; private SharedPreferencesUtil mSpUtil; private List<Explorer>
//	 * list = new ArrayList<Explorer>(); private int[] images = new int[] {
//	 * R.drawable.livingl, R.drawable.invitel, R.drawable.servicl,
//	 * R.drawable.fujin }; private String[] titles = new String[] { "环境说",
//	 * "一键曝光", "公众服务"}; private String[] contents = new String[] { "随时随地分享你的点滴",
//	 * "忍无可忍，让你发泄的时候到了！环境问题，一键曝光", "最贴心的生活咨询"}; private List<Explorer> list1 =
//	 * new ArrayList<Explorer>(); private int[] images1 = new int[] {
//	 * R.drawable.livingl, R.drawable.invitel, R.drawable.servicl,
//	 * R.drawable.fujin }; private String[] titles1 = new String[] { "环境说",
//	 * "一键曝光", "公众服务"}; private String[] contents1 = new String[] {
//	 * "随时随地分享你的点滴", "忍无可忍，让你发泄的时候到了！环境问题，一键曝光", "最贴心的生活咨询"}; private String
//	 * userID = ""; private String city, time, new_path, cityhistory; private
//	 * LinearLayout explorer_layout;
//	 * 
//	 * private getNewsTaskCount nTask; Handler handler = new Handler() { public
//	 * void handleMessage(android.os.Message msg) { switch (msg.arg1) { case 1:
//	 * if (null == adapter) { adapter = new MyAdapter(); adapter.bindData(list);
//	 * explorer.setAdapter(adapter); } else { adapter.notifyDataSetChanged(); }
//	 * 
//	 * break; case 2: News news = new News(); news.setCount(0);
//	 * list.get(0).setNews(news); if (null == adapter) { adapter = new
//	 * MyAdapter(); adapter.bindData(list); explorer.setAdapter(adapter); } else
//	 * { adapter.notifyDataSetChanged(); } break; default: break; } }; };
//	 */
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.discover_main_activity);
//		dbManager = DBManager.getInstances(DiscoverMainActivity.this);
//		util = SharedPreferencesUtil.getInstance(DiscoverMainActivity.this);
//		initview();
//		initListener();
//	}
//
//	private void initview() {
//		discover_huanjingshuo_layout = (RelativeLayout) findViewById(R.id.discover_huanjingshuo);
//		discover_baoguang_layout = (RelativeLayout) findViewById(R.id.discover_baoguang);
//		discover_jiankang = (RelativeLayout) findViewById(R.id.discover_jiankang);
//		discover_laji = (RelativeLayout) findViewById(R.id.discover_laji);
//		discover_huanbao = (RelativeLayout) findViewById(R.id.discover_huanbao);
//		discover_more = (RelativeLayout) findViewById(R.id.discover_more);
//		discover_news = (RelativeLayout) findViewById(R.id.discover_news);
//		discover_paiming = (RelativeLayout) findViewById(R.id.discover_paiming);
//		explorer_tv1 = (TextView) findViewById(R.id.explorer_tv1);
//		explorer_tv2 = (TextView) findViewById(R.id.explorer_tv2);
//		explorer_tv3 = (TextView) findViewById(R.id.explorer_tv3);
//		explorer_tv4 = (TextView) findViewById(R.id.explorer_tv4);
//		explorer_tv5 = (TextView) findViewById(R.id.explorer_tv5);
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		userId = WeiBaoApplication.getUserId();
//		if (userId.equals(""))
//			userId = "0";
//		query(explorer_tv1, userId, "32");
//		query(explorer_tv2, userId, "30");
//		query(explorer_tv3, userId, "28");
//		query(explorer_tv4, userId, "29");
//		query(explorer_tv5, userId, "27");
//		Intent intent = new Intent(DiscoverMainActivity.this,
//				NewsPushService.class);
//		startService(intent);
//
//	}
//
//	private void query(TextView t1, String userId, String publicId) {
//		accept_news = userId + "*" + publicId;
//		boolean flag = util.get(accept_news, true);
//		if (flag) {
//			// 从数据库里查找数据库中的未读信息
//			int m = 0;
//			if (userId.equals("0")) {
//				m = dbManager.selectUic(DiscoverMainActivity.this, "nouic",
//						userId, publicId);
//			} else {
//				m = dbManager.selectUic(DiscoverMainActivity.this, "uic",
//						userId, publicId);
//			}
//			if (m == 0) {
//				t1.setVisibility(View.GONE);
//			} else {
//				t1.setVisibility(View.VISIBLE);
//				t1.setText(m + "");
//			}
//		} else {
//			t1.setVisibility(View.GONE);
//		}
//	}
//
//	private void initListener() {
//		discover_huanjingshuo_layout.setOnClickListener(this);
//		discover_baoguang_layout.setOnClickListener(this);
//		discover_jiankang.setOnClickListener(this);
//		discover_laji.setOnClickListener(this);
//		discover_huanbao.setOnClickListener(this);
//		discover_more.setOnClickListener(this);
//		discover_news.setOnClickListener(this);
//		discover_paiming.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		boolean picflag = false;
//		String picUrl = UrlComponent.baseurl;
//		picflag = picUrl.contains("shenbianer") ? false : true;
//		String picicon = "";
//		switch (v.getId()) {
//		case R.id.discover_huanjingshuo:
//			// 点过之后消息条目数隐藏
//			TextView explorer_news = (TextView) findViewById(R.id.explorer_news);
//			explorer_news.setVisibility(View.GONE);
//			MobclickAgent.onEvent(DiscoverMainActivity.this, "FXShowHJSPanel");
//			Intent intent = new Intent(DiscoverMainActivity.this,
//					DiscoverBlogListActivity.class);
//			startActivity(intent);
//			break;
//		case R.id.discover_baoguang:
//
//			username = WeiBaoApplication.getInstance().getUsename();
//			if (username.equals("") || username.equals("游客")) {
//				Toast.makeText(DiscoverMainActivity.this, "请先登录", 0).show();
//				new Thread() {
//					public void run() {
//						try {
//							currentThread().sleep(500);
//							Intent intent = new Intent(
//									DiscoverMainActivity.this,
//									UserLoginActivity.class);
//							intent.putExtra("from", "discover");
//							startActivity(intent);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					};
//				}.start();
//				return;
//			}
//			MobclickAgent.onEvent(DiscoverMainActivity.this, "FXShowOne");
//			Intent intent2 = new Intent(DiscoverMainActivity.this,
//					DiscoverExposureListActivity.class);
//			startActivity(intent2);
//			break;
//		case R.id.discover_jiankang:
//			Intent intent3 = new Intent(DiscoverMainActivity.this,
//					DiscoverPubServiceNewsActivity.class);
//			intent3.putExtra("guanzhu", true);
//			intent3.putExtra("publicID", "28");
//			intent3.putExtra("name", "健康养生");
//			picicon = picflag ? iconmicro[0] : iconshen[0];
//			intent3.putExtra("public_photo", picicon);
//			intent3.putExtra("fuction", "健康养生之道，养生保健知识，健康小常识");
//			intent3.putExtra("biaozhi", "now");
//			startActivity(intent3);
//			break;
//		case R.id.discover_laji:
//			Intent intent4 = new Intent(DiscoverMainActivity.this,
//					DiscoverPubServiceNewsActivity.class);
//			intent4.putExtra("guanzhu", true);
//			intent4.putExtra("publicID", "29");
//			intent4.putExtra("name", "垃圾分类");
//			picicon = picflag ? iconmicro[0] : iconshen[0];
//			intent4.putExtra("public_photo", picicon);
//			intent4.putExtra(
//					"fuction",
//					"垃圾分类就是将垃圾分别地投放，并通过分类地清运和回收使之重新变成资源。从国内外各城市对生活垃圾分类的方法来看，大致都是根据垃圾的成分构成、产生量，结合本地垃圾的资源利用和处理方式来进行分类的。");
//			intent4.putExtra("biaozhi", "now");
//			startActivity(intent4);
//			break;
//		case R.id.discover_news:
//			Intent intent5 = new Intent(DiscoverMainActivity.this,
//					DiscoverPubServiceNewsActivity.class);
//			intent5.putExtra("guanzhu", true);
//			intent5.putExtra("publicID", "32");
//			intent5.putExtra("name", "一周国际环境要闻");
//			picicon = picflag ? iconmicro[0] : iconshen[0];
//			intent5.putExtra("public_photo", picicon);
//			intent5.putExtra(
//					"fuction",
//					"国际中国环境基金会的目标是通过解决中国的环境问题来确保健康的全球环境以及可持续发展的经济。基金会主要业务包含：政策建议和咨询，技术交流与合作，民间环保组织发展，以及公共环境教育。");
//			intent5.putExtra("biaozhi", "now");
//			startActivity(intent5);
//			break;
//		case R.id.discover_paiming:
//			Intent intent6 = new Intent(DiscoverMainActivity.this,
//					DiscoverPubServiceNewsActivity.class);
//			intent6.putExtra("guanzhu", true);
//			intent6.putExtra("publicID", "30");
//			intent6.putExtra("name", "每日空气排名");
//			picicon = picflag ? iconmicro[0] : iconshen[0];
//			intent6.putExtra("public_photo", picicon);
//			intent6.putExtra("fuction", "看看你所在的城市能排到第几名？");
//			intent6.putExtra("biaozhi", "now");
//			startActivity(intent6);
//			break;
//		case R.id.discover_huanbao:
//			Intent intent7 = new Intent(DiscoverMainActivity.this,
//					DiscoverPubServiceNewsActivity.class);
//			intent7.putExtra("guanzhu", true);
//			intent7.putExtra("publicID", "27");
//			intent7.putExtra("name", "环保百科");
//			picicon = picflag ? iconmicro[0] : iconshen[0];
//			intent7.putExtra("public_photo", picicon);
//			intent7.putExtra("fuction", "提供环保有关的，亲民的环境知识分享，经验交流。");
//			intent7.putExtra("biaozhi", "now");
//			startActivity(intent7);
//			break;
//		case R.id.discover_more:
//			Intent intent8 = new Intent(DiscoverMainActivity.this,
//					DiscoverPubServiceSearchActivity.class);
//			startActivity(intent8);
//			break;
//		default:
//			break;
//		}
//
//	}
//}
