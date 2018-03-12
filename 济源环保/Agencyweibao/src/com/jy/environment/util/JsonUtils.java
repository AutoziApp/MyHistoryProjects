package com.jy.environment.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.database.dal.AlarmDao;
import com.jy.environment.database.dal.DBInfo;
import com.jy.environment.database.dal.DBManager;
import com.jy.environment.database.model.ModelAlarmHistory;
import com.jy.environment.model.AQI;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.AlarmHistoryModel;
import com.jy.environment.model.AqiModel;
import com.jy.environment.model.AqiStationModel;
import com.jy.environment.model.ChatMsg;
import com.jy.environment.model.ChatMsgEntity;
import com.jy.environment.model.CityDetails;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.CurrentWeather;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.model.EnvironmentAqiModel;
import com.jy.environment.model.EnvironmentForecastWeekModel;
import com.jy.environment.model.EnvironmentForecastWeeklyModel;
import com.jy.environment.model.EnvironmentMonitorModel;
import com.jy.environment.model.GradeModel;
import com.jy.environment.model.Item;
import com.jy.environment.model.Kongqizhishu;
import com.jy.environment.model.Life;
import com.jy.environment.model.LifeItem;
import com.jy.environment.model.ListHumiDityModel;
import com.jy.environment.model.ListPolluctionModel;
import com.jy.environment.model.MainAqiData;
import com.jy.environment.model.MonitorModel;
import com.jy.environment.model.NO2;
import com.jy.environment.model.NearestPm;
import com.jy.environment.model.News;
import com.jy.environment.model.NoiseHistoryModel;
import com.jy.environment.model.NoiseItemModel;
import com.jy.environment.model.PM10Info24H;
import com.jy.environment.model.PM25;
import com.jy.environment.model.Pinglun;
import com.jy.environment.model.PmDayHourModel;
import com.jy.environment.model.PmModel;
import com.jy.environment.model.PublicService;
import com.jy.environment.model.PublicServiceItem;
import com.jy.environment.model.ResultBlogList;
import com.jy.environment.model.ResultSelfBlogList;
import com.jy.environment.model.SO2;
import com.jy.environment.model.SearchService;
import com.jy.environment.model.SearchServiceItem;
import com.jy.environment.model.Sweather;
import com.jy.environment.model.ThreeDayAqiTrendModel;
import com.jy.environment.model.ThreeDayForestModel;
import com.jy.environment.model.Trend;
import com.jy.environment.model.TrendModel;
import com.jy.environment.model.Update;
import com.jy.environment.model.UserGetUerInfoModel;
import com.jy.environment.model.UserMail;
import com.jy.environment.model.UserRegisterModel;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.model.WeatherInfo7;
import com.jy.environment.model.WeatherInfo7_tian;
import com.jy.environment.model.WeatherInfoMonth;
import com.jy.environment.model.WeatherInfoYear;
import com.jy.environment.model.Weib;
import com.jy.environment.model.WindModel;
import com.jy.environment.model.YuCeModel;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 * 
 */
public class JsonUtils {
	public static List<AQIPoint> aqicitys_fromserver = new ArrayList<AQIPoint>();
	public static List<AQIPoint> sinPoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> zerPoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> nextPoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> fourPoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> fivePoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> sixPoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> sevenPoints = new ArrayList<AQIPoint>();
	public static List<AQIPoint> eightints = new ArrayList<AQIPoint>();

	// 公众服务精品推荐
	public static PublicService jsonService(Context context, String json) {
		PublicService p1 = new PublicService();
		try {
			JSONObject jsonObject = new JSONObject(json);
			boolean flag = jsonObject.getBoolean("flag");
			MyLog.i(">>>>>>>>>flag" + flag);
			p1.setFlag(jsonObject.getBoolean("flag"));
			if (flag) {
				JSONArray jsonArray = new JSONObject(json).getJSONArray("datalist");
				DBManager manager = DBManager.getInstances(context);
				manager.deleteSQLiteTable(context, "jingpin");
				List<PublicServiceItem> list = new ArrayList<PublicServiceItem>();

				for (int i = 0; i < jsonArray.length(); i++) {
					PublicServiceItem p2 = new PublicServiceItem();
					JSONObject j1 = jsonArray.getJSONObject(i);
					p2.setFuction(j1.getString("fuction"));
					p2.setName(j1.getString("name"));
					p2.setPublic_photo(j1.getString("public_photo"));
					p2.setId(j1.getString("id"));
					p2.setUser_type(j1.getString("user_type"));
					ContentValues values = new ContentValues();
					values.put("fuction", j1.getString("fuction"));
					values.put("publicID", j1.getString("id"));
					values.put("name", j1.getString("name"));
					values.put("public_photo", j1.getString("public_photo"));
					values.put("user_type", j1.getString("user_type"));

					manager.insertSQLite(context, "jingpin", null, values);
					list.add(p2);
				}
				MyLog.i(">>>>>>listghgh" + list);
				p1.setList(list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p1;
	}

	// 用户关注的公众号
	public static PublicService jsonService1(String json, Context context, String userId) {
		PublicService p1 = new PublicService();
		try {
			JSONObject jsonObject = new JSONObject(json);
			boolean flag = jsonObject.getBoolean("flag");
			p1.setFlag(jsonObject.getBoolean("flag"));
			if (flag) {
				DBManager manager = DBManager.getInstances(context);
				if (userId.equals("0")) {
					manager.deleteSQLiteTable(context, DBInfo.TABLE_NOUSERPUBLIC);
				} else {
					manager.deleteSQLiteTable(context, DBInfo.TABLE_USERPUBLIC);
				}
				JSONArray jsonArray = new JSONObject(json).getJSONArray("lists");
				List<PublicServiceItem> list = new ArrayList<PublicServiceItem>();
				// DBManager dbManager = DBManager.getInstances(context);
				// ContentValues values = new ContentValues();
				// values.put("publicID", j1.getString("id"));
				// values.put("fuction", j1.getString("fuction"));
				// values.put("public_photo", j1.getString("public_photo"));
				// values.put("user_type", j1.getString("user_type"));
				// values.put("name", j1.getString("name"));
				// values.put("userID", userId);
				// dbManager.insertSQLite(context, DBInfo.TABLE_USERPUBLIC,
				// null, values);
				for (int i = 0; i < jsonArray.length(); i++) {
					PublicServiceItem p2 = new PublicServiceItem();
					JSONObject j1 = jsonArray.getJSONObject(i);
					p2.setFuction(j1.getString("fuction"));
					p2.setId(j1.getString("id"));
					p2.setName(j1.getString("name"));
					p2.setPublic_photo(j1.getString("public_photo"));
					p2.setUser_type(j1.getString("user_type"));
					ContentValues values = new ContentValues();
					DBManager dbManager = DBManager.getInstances(context);
					values.put("publicID", j1.getString("id"));
					values.put("fuction", j1.getString("fuction"));
					values.put("public_photo", j1.getString("public_photo"));
					values.put("user_type", j1.getString("user_type"));
					values.put("name", j1.getString("name"));
					values.put("userID", userId);
					if (userId.equals("0")) {
						dbManager.insertSQLite(context, DBInfo.TABLE_NOUSERPUBLIC, null, values);
					} else {
						dbManager.insertSQLite(context, DBInfo.TABLE_USERPUBLIC, null, values);
					}

					list.add(p2);
				}
				p1.setList(list);
				MyLog.i(">>>>>>>>>>gghhglist" + list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p1;
	}

	public static SearchService jsonSerch(String json) {
		SearchService s1 = new SearchService();
		try {
			JSONObject jsonObject = new JSONObject(json);
			s1.setFlag(jsonObject.getBoolean("flag"));
			if (jsonObject.getBoolean("flag")) {
				JSONArray jsonArray = jsonObject.getJSONArray("lists");
				List<SearchServiceItem> list = new ArrayList<SearchServiceItem>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject j1 = jsonArray.getJSONObject(i);
					SearchServiceItem item = new SearchServiceItem();
					item.setId(j1.getString("id"));
					item.setName(j1.getString("name"));
					item.setPublic_photo(j1.getString("public_photo"));
					item.setUser_type(j1.getString("user_type"));
					item.setFuction(j1.getString("fuction"));
					list.add(item);
				}
				s1.setList(list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s1;
	}

	// 生活馆解析
	public static List<Life> getCityWeather(String json) {
		List<Life> list = new ArrayList<Life>();
		try {
			JSONObject j1 = new JSONObject(json).getJSONObject("index");
			Life life1 = new Life();
			life1.setIndex_tv1(j1.getString("index_cy"));
			life1.setIndex_tv2(j1.getString("index_cy_xs"));
			life1.setIndex_tv0("穿衣指数");
			list.add(life1);
			Life life2 = new Life();
			life2.setIndex_tv1(j1.getString("index_cl"));
			life2.setIndex_tv2(j1.getString("index_cl_xs"));
			life2.setIndex_tv0("晨练指数");
			list.add(life2);
			Life life3 = new Life();
			life3.setIndex_tv1(j1.getString("index_uv"));
			life3.setIndex_tv2(j1.getString("index_uv_xs"));
			life3.setIndex_tv0("紫外线强度");
			list.add(life3);
			Life life4 = new Life();
			life4.setIndex_tv1(j1.getString("index_tr"));
			life4.setIndex_tv2(j1.getString("index_tr_xs"));
			life4.setIndex_tv0("旅游指数");
			list.add(life4);
			Life life5 = new Life();
			life5.setIndex_tv1(j1.getString("index_co"));
			life5.setIndex_tv2(j1.getString("index_co_xs"));
			life5.setIndex_tv0("舒适指数");
			list.add(life5);
			Life life6 = new Life();
			int max = 65;
			int min = 30;
			Random random = new Random();
			int s = random.nextInt(max) % (max - min + 1) + min;
			MyLog.i(">>>>>产生的随机数" + s);
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int hour = c.get(Calendar.HOUR_OF_DAY);
			MyLog.i(">>>>>hour" + hour);
			if (hour > 7 && hour < 21) {
				if (s >= 30 && s < 45) {
					life6.setIndex_tv1("较吵");
					life6.setIndex_tv2("干扰交谈、通讯、思考，引起心理烦躁");
					life6.setIndex_tv0("室外噪声强度");
				} else {
					life6.setIndex_tv1("很吵");
					life6.setIndex_tv2("分散注意力，开始损伤听力神经");
					life6.setIndex_tv0("室外噪声强度");
				}

			} else {
				if (s >= 30 && s < 45) {
					life6.setIndex_tv1("较静");
					life6.setIndex_tv2("大于50分贝的噪声，会影响休息");
					life6.setIndex_tv0("室外噪声强度");
				} else {
					life6.setIndex_tv1("安静");
					life6.setIndex_tv2("适宜睡眠");
					life6.setIndex_tv0("室外噪声强度");
				}

			}
			if (WeiBaoApplication.selectedCity.equals(WeiBaoApplication.getInstance().getDingweicity())) {
				list.add(life6);
			}

			MyLog.i("生活馆>>>>" + list);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	// 用户关注的公众号的消息 1代表读过 0代表没读
	public static ChatMsg jsonServiceXiao(Context context, String userId, String json, boolean flag1) {
		ChatMsg chat = new ChatMsg();
		try {
			JSONObject j1 = new JSONObject(json);
			boolean flag = j1.getBoolean("flag");
			chat.setFlag(j1.getBoolean("flag"));
			List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
			if (flag) {
				JSONArray array = j1.getJSONArray("lists");
				for (int i = 0; i < array.length(); i++) {
					JSONObject j2 = array.getJSONObject(i);
					ChatMsgEntity e1 = new ChatMsgEntity();
					e1.setAccount_id(j2.getString("account_id"));
					e1.setAccount_id_num(j2.getString("account_id_num"));
					e1.setAuthor(j2.getString("author"));
					// e1.setContent(j2.getString("content"));
					e1.setCreate_time(j2.getString("publish_time"));
					e1.setFace_pic_url(j2.getString("face_pic_url"));
					e1.setSummary(j2.getString("summary"));
					e1.setTitle(j2.getString("title"));
					System.out.println(">>>>>>>title" + j2.getString("title"));
					e1.setXiaoxi_id(j2.getString("id"));
					ContentValues values = new ContentValues();
					DBManager dbManager = DBManager.getInstances(context);
					values.put("userID", userId);
					values.put("account_id_num", j2.getString("account_id_num"));
					values.put("account_id", j2.getString("account_id"));
					values.put("xiaoxi_id", j2.getString("id"));
					values.put("title", j2.getString("title"));
					values.put("author", j2.getString("author"));
					// values.put("content", j2.getString("content"));
					values.put("face_pic_url", j2.getString("face_pic_url"));
					values.put("summary", j2.getString("summary"));
					values.put("isread", "0");
					values.put("isfirst", "1");
					// values.put("ishistory", "0");
					values.put("publish_time", j2.getString("publish_time"));
					if (userId != null) {
						dbManager.insertSQLite(context, "uic", null, values);
					}

					list.add(e1);
				}
				chat.setList(list);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return chat;
	}

	// 解析历史消息
	public static ChatMsg jsonServiceHXiao(String json) {
		ChatMsg chat = new ChatMsg();
		try {
			JSONObject j1 = new JSONObject(json);
			boolean flag = j1.getBoolean("flag");
			chat.setFlag(j1.getBoolean("flag"));
			List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
			if (flag) {
				JSONArray array = j1.getJSONArray("lists");
				for (int i = 0; i < array.length(); i++) {
					JSONObject j2 = array.getJSONObject(i);
					ChatMsgEntity e1 = new ChatMsgEntity();
					e1.setAccount_id(j2.getString("account_id"));
					e1.setAccount_id_num(j2.getString("account_id_num"));
					e1.setAuthor(j2.getString("author"));
					e1.setCreate_time(j2.getString("publish_time"));
					e1.setFace_pic_url(j2.getString("face_pic_url"));
					e1.setSummary(j2.getString("summary"));
					e1.setTitle(j2.getString("title"));
					System.out.println(">>>>>>>title" + j2.getString("title"));
					e1.setXiaoxi_id(j2.getString("id"));
					list.add(e1);
				}
				chat.setList(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return chat;
	}

	// 解析消息条目数
	public static News jsonNewsCounts(String json) {
		News news = new News();
		try {
			JSONObject jsonObject = new JSONObject(json);
			boolean flag = jsonObject.getBoolean("flag");
			if (!flag) {
				news.setFlag(flag);
			} else {
				news.setFlag(flag);
				news.setCount(jsonObject.getInt("count"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return news;
	}

	public static ResultBlogList getBlogList(String json, String userId) {
		ResultBlogList resultBlogList = new ResultBlogList();
		List<Weib> weibs = new ArrayList<Weib>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			resultBlogList.setFlag(jsonObject.getBoolean("flag"));
			if (!jsonObject.getBoolean("flag")) {
				resultBlogList.setWeibs(weibs);
				return resultBlogList;
			}
			String sertime = jsonObject.getString("servertime");

			JSONArray array = jsonObject.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {

				JSONObject jsonObject2 = array.getJSONObject(i);
				JSONArray pinglun = jsonObject2.getJSONArray("pinglun");
				List<Pinglun> pingl = new ArrayList<Pinglun>();
				for (int j = pinglun.length() - 1; j >= 0; j--) {
					if (!"".equals(pinglun.get(j))) {
						String pinglunnc = "";
						try {
							pinglunnc = pinglun.getJSONObject(j).getString("nc");
						} catch (Exception e) {
							pinglunnc = "";
						}
						Pinglun pinglun2 = new Pinglun(pinglunnc, pinglun.getJSONObject(j).getString("commentId"),
								pinglun.getJSONObject(j).getString("user"),
								pinglun.getJSONObject(j).getString("content"),
								pinglun.getJSONObject(j).getString("commentPersonId"));
						pingl.add(pinglun2);
					}

				}

				JSONArray smallpic = jsonObject2.getJSONArray("small_pics");

				/**
				 * 小图片地址列表
				 */
				List<String> small_pics = new ArrayList<String>();
				for (int j = 0; j < smallpic.length(); j++) {
					if (!"".equals(smallpic.getString(j))) {
						small_pics.add(smallpic.getString(j));
					}

				}
				/**
				 * 大图片地址列表
				 */
				List<String> bgpic = new ArrayList<String>();
				JSONArray bigpic = jsonObject2.getJSONArray("big_pics");
				for (int j = 0; j < bigpic.length(); j++) {
					if (!"".equals(bigpic.getString(j))) {
						bgpic.add(bigpic.getString(j));
					}

				}
				/**
				 * 点赞人列表
				 */
				List<String> dianzhanren = new ArrayList<String>();
				List<String> dianzhanrenIds = new ArrayList<String>();
				List<String> dianzhanrenNcs = new ArrayList<String>();
				JSONArray dzr = jsonObject2.getJSONArray("dianz_users");
				JSONArray dzrId = jsonObject2.getJSONArray("dianz_userids");
				JSONArray dzNcs = jsonObject2.getJSONArray("dianz_ncs");
				for (int j = 0; j < dzr.length(); j++) {
					if (!"".equals(dzr.getString(j))) {
						dianzhanren.add(dzr.getString(j));
						dianzhanrenIds.add(dzrId.getString(j));
						dianzhanrenNcs.add(dzNcs.getString(j));
					}

				}
				/**
				 * 曝光新增字段
				 */
				/*
				 * //是否一键曝光 String isAkey = jsonObject2.getString("isAkey");
				 * //污染类型 String pollutionType =
				 * jsonObject2.getString("pollutionType"); //是否匿名 String
				 * isanonymous = jsonObject2.getString("isanonymous");
				 */
				String status = jsonObject2.getString("status");
				String useridcontentid = jsonObject2.getString("userid");
				if (("".equals(userId) && status.equals("0"))
						|| (!userId.equals(useridcontentid) && status.equals("0"))) {
					continue;
				}

				String userNc = "";
				try {
					userNc = jsonObject2.getString("nc");
					MyLog.i("userNc :" + userNc);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Weib we = new Weib(pingl, jsonObject2.getString("id"), jsonObject2.getString("username"), userNc,
						jsonObject2.getString("icon"), jsonObject2.getString("time"), jsonObject2.getString("content"),
						jsonObject2.getString("status"), small_pics, bgpic, jsonObject2.getString("dianz"), dianzhanren,
						jsonObject2.getString("zhuanfa"), jsonObject2.getString("showlevel"),
						jsonObject2.getString("env_state"), jsonObject2.getString("userid"), dianzhanrenIds,
						jsonObject2.getString("area"), jsonObject2.getString("longitude"),
						jsonObject2.getString("latitude"), dianzhanrenNcs, jsonObject2.getString("isAkey"),
						jsonObject2.getString("pollutionType"), jsonObject2.getString("isanonymous"), 0, 0, 0);
				we.setHestory(false);
				weibs.add(we);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		resultBlogList.setWeibs(weibs);
		return resultBlogList;
	}

	public static ResultSelfBlogList getSelfBlogList(String json, String userId) {
		ResultSelfBlogList resultBlogList = new ResultSelfBlogList();
		List<Weib> weibs = new ArrayList<Weib>();
		MyLog.i("getWeiboByInfo_Get load url:" + json);
		try {
			JSONObject jsonObject = new JSONObject(json);
			resultBlogList.setFlag(jsonObject.getBoolean("flag"));
			if (!jsonObject.getBoolean("flag")) {
				resultBlogList.setWeibs(weibs);
				return resultBlogList;
			}
			String sertime = jsonObject.getString("servertime");

			JSONArray array = jsonObject.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {

				JSONObject jsonObject2 = array.getJSONObject(i);
				JSONArray pinglun = jsonObject2.getJSONArray("pinglun");
				List<Pinglun> pingl = new ArrayList<Pinglun>();
				for (int j = pinglun.length() - 1; j >= 0; j--) {
					if (!"".equals(pinglun.get(j))) {
						String pinglunnc = "";
						try {
							pinglunnc = pinglun.getJSONObject(j).getString("nc");
						} catch (Exception e) {
							// TODO: handle exception
						}
						Pinglun pinglun2 = new Pinglun(pinglunnc, pinglun.getJSONObject(j).getString("commentId"),
								pinglun.getJSONObject(j).getString("user"),
								pinglun.getJSONObject(j).getString("content"),
								pinglun.getJSONObject(j).getString("commentPersonId"));
						pingl.add(pinglun2);
					}

				}

				JSONArray smallpic = jsonObject2.getJSONArray("small_pics");

				/**
				 * 小图片地址列表
				 */
				List<String> small_pics = new ArrayList<String>();
				for (int j = 0; j < smallpic.length(); j++) {
					if (!"".equals(smallpic.getString(j))) {
						small_pics.add(smallpic.getString(j));
					}

				}
				/**
				 * 大图片地址列表
				 */
				List<String> bgpic = new ArrayList<String>();
				JSONArray bigpic = jsonObject2.getJSONArray("big_pics");
				for (int j = 0; j < bigpic.length(); j++) {
					if (!"".equals(bigpic.getString(j))) {
						bgpic.add(bigpic.getString(j));
					}

				}
				/**
				 * 点赞人列表
				 */
				List<String> dianzhanren = new ArrayList<String>();
				List<String> dianzhanrenIds = new ArrayList<String>();
				List<String> dianzhanrenNcs = new ArrayList<String>();
				JSONArray dzr = jsonObject2.getJSONArray("dianz_users");
				JSONArray dzrId = jsonObject2.getJSONArray("dianz_userids");
				JSONArray dzNcs = jsonObject2.getJSONArray("dianz_ncs");
				for (int j = 0; j < dzr.length(); j++) {
					if (!"".equals(dzr.getString(j))) {
						dianzhanren.add(dzr.getString(j));
						dianzhanrenIds.add(dzrId.getString(j));
						dianzhanrenNcs.add(dzNcs.getString(j));
					}

				}
				String status = jsonObject2.getString("status");
				String useridcontentid = jsonObject2.getString("userid");
				// if (("".equals(userId) && status.equals("0"))
				// || (!userId.equals(useridcontentid) && status
				// .equals("0"))) {
				// continue;
				// }

				String userNc = "";
				try {
					userNc = jsonObject2.getString("nc");
					MyLog.i("userNc :" + userNc);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Weib we = new Weib(pingl, jsonObject2.getString("id"), jsonObject2.getString("username"), userNc,
						jsonObject2.getString("icon"), jsonObject2.getString("time"), jsonObject2.getString("content"),
						jsonObject2.getString("status"), small_pics, bgpic, jsonObject2.getString("dianz"), dianzhanren,
						jsonObject2.getString("zhuanfa"), jsonObject2.getString("showlevel"),
						jsonObject2.getString("env_state"), jsonObject2.getString("userid"), dianzhanrenIds,
						jsonObject2.getString("area"), jsonObject2.getString("longitude"),
						jsonObject2.getString("latitude"), dianzhanrenNcs, jsonObject2.getString("isakey"), "", "",
						jsonObject.getInt("imagesCount"), jsonObject.getInt("commentCount"),
						jsonObject.getInt("postsCount"));
				we.setHestory(false);
				we.setImagesCount(jsonObject.getInt("imagesCount"));
				we.setCommentCount(jsonObject.getInt("commentCount"));
				weibs.add(we);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		resultBlogList.setWeibs(weibs);
		return resultBlogList;
	}

	public static List<WeatherInfo24> getAqiDetailWeatherInfo24Hour(String _Json) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(_Json);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		List<WeatherInfo24> weatherLists = new ArrayList<WeatherInfo24>();
		if (null == jsonArray || jsonArray.length() < 1) {
			for (int i = 0; i < 24; i++) {
				WeatherInfo24 weatherInfo = new WeatherInfo24();
				weatherInfo.setAqi(1);
				weatherInfo.setUpdate_time("2014-03-27 20:00:00");
				weatherInfo.setLevel("良");
				weatherLists.add(weatherInfo);
			}
		} else {
			for (int i = 0; i < jsonArray.length(); i++) {
				WeatherInfo24 weatherInfo = new WeatherInfo24();
				JSONObject everyDayWeather;
				try {
					everyDayWeather = (JSONObject) jsonArray.get(i);

					weatherInfo.setAqi(Integer.parseInt(everyDayWeather.getString("aqi")));

					weatherInfo.setUpdate_time(everyDayWeather.getString("update_time"));
					weatherInfo.setLevel(everyDayWeather.getString("level"));
					weatherLists.add(weatherInfo);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
		return weatherLists;
	}

	public static List<WeatherInfo7_tian> getAqiDetailWeatherInfo7Day(String _Json) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(_Json);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		List<WeatherInfo7_tian> lists_7tian = new ArrayList<WeatherInfo7_tian>();
		if (null == jsonArray || jsonArray.length() < 1) {
			for (int i = 0; i < 7; i++) {

				WeatherInfo7_tian weatherInfo = new WeatherInfo7_tian();
				weatherInfo.setAqi(1);
				weatherInfo.setUpdate_time("2014-03-27 20:00:00");
				lists_7tian.add(weatherInfo);
			}
		} else {
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					WeatherInfo7_tian weatherInfo = new WeatherInfo7_tian();
					JSONObject everyDayWeather = (JSONObject) jsonArray.get(i);
					weatherInfo.setAqi(everyDayWeather.getInt("aqi"));
					weatherInfo.setUpdate_time(everyDayWeather.getString("update_time"));
					lists_7tian.add(weatherInfo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return lists_7tian;
	}

	public static List<Item> getAqiDetailItem(String _Json) throws JSONException {
		JSONArray jsonArray = null;
		try {
			JSONObject jsonObject = new JSONObject(_Json);
			jsonArray = new JSONArray(jsonObject.getString("paiming"));
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Item item = new Item();
			item.setCityName(jsonArray.getJSONObject(i).getString("city_name"));
			item.setCityName(jsonArray.getJSONObject(i).getString("city_name"));
			item.setRank((i + 1) + "");
			String di = jsonArray.getJSONObject(i).getString("aqi");
			item.setIndex(di.substring(0, di.lastIndexOf(".")));
			items.add(item);
		}
		return items;
	}

	public static CityRank getAqiDetailItem(String _Json, String city) throws JSONException {
		CityRank rank = new CityRank();
		int position = 0;
		JSONArray jsonArray = null;
		long time;
		try {
			JSONObject jsonObject = new JSONObject(_Json);
			jsonArray = new JSONArray(jsonObject.getString("paiming"));
			MyLog.i("jsonObject.getLong" + jsonObject.getString("time"));
			MyLog.i("jsonObject.getLong" + jsonObject.getInt("time"));
			MyLog.i("jsonObject.getLong" + jsonObject.getLong("time"));
			time = jsonObject.getLong("time");
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}

		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Item item = new Item();
			if (city.equals(jsonArray.getJSONObject(i).getString("city_name"))) {
				position = i;
			}
			item.setCityName(jsonArray.getJSONObject(i).getString("city_name"));
			item.setCityName(jsonArray.getJSONObject(i).getString("city_name"));
			item.setRank((i + 1) + "");
			String di = jsonArray.getJSONObject(i).getString("aqi");
			item.setIndex(di.substring(0, di.lastIndexOf(".")));
			items.add(item);
		}
		rank.set_Result(items);
		rank.setRank(position);
		rank.setTime(time);
		return rank;
	}

	public static Kongqizhishu getKongqizhishu(String _Json) throws JSONException {
		Kongqizhishu kongqizhishu;
		JSONObject jsonObject = new JSONObject(_Json);
		String lev = jsonObject.getString("level");

		if (lev.equals("优")) {

			kongqizhishu = new Kongqizhishu(jsonObject.getString("aqi"), jsonObject.getString("level"),
					jsonObject.getString("pm25"), jsonObject.getString("pm10"), jsonObject.getString("no2"),
					jsonObject.getString("so2"), jsonObject.getString("co"), jsonObject.getString("o3"),
					"空气质量令人满意，基本无空气污染", "各类人群可正常活动", jsonObject.getString("mingri"));
		} else if (lev.equals("良")) {

			kongqizhishu = new Kongqizhishu(jsonObject.getString("aqi"), jsonObject.getString("level"),
					jsonObject.getString("pm25"), jsonObject.getString("pm10"), jsonObject.getString("no2"),
					jsonObject.getString("so2"), jsonObject.getString("co"), jsonObject.getString("o3"),
					"空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响", "极少数异常敏感人群应减少户外活动", jsonObject.getString("mingri"));

		} else if (lev.equals("轻度污染")) {

			kongqizhishu = new Kongqizhishu(jsonObject.getString("aqi"), jsonObject.getString("level"),
					jsonObject.getString("pm25"), jsonObject.getString("pm10"), jsonObject.getString("no2"),
					jsonObject.getString("so2"), jsonObject.getString("co"), jsonObject.getString("o3"),
					"易感人群症状有轻度加剧，健康人群出现刺激症状", "儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼", jsonObject.getString("mingri"));

		} else if (lev.equals("中度污染")) {

			kongqizhishu = new Kongqizhishu(jsonObject.getString("aqi"), jsonObject.getString("level"),
					jsonObject.getString("pm25"), jsonObject.getString("pm10"), jsonObject.getString("no2"),
					jsonObject.getString("so2"), jsonObject.getString("co"), jsonObject.getString("o3"),
					"进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响", "儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外运动",
					jsonObject.getString("mingri"));
		} else {

			kongqizhishu = new Kongqizhishu(jsonObject.getString("aqi"), jsonObject.getString("level"),
					jsonObject.getString("pm25"), jsonObject.getString("pm10"), jsonObject.getString("no2"),
					jsonObject.getString("so2"), jsonObject.getString("co"), jsonObject.getString("o3"),
					"健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病", "儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动",
					jsonObject.getString("mingri"));
		}
		return kongqizhishu;
	}

	public static NearestPm getNearestPm(String _Json, String dingweicity, String currentCityLongitude,
			String currentCityLatitude) throws JSONException {
		JSONObject jsonObject = new JSONObject(_Json);
		NearestPm nearestPm = new NearestPm(jsonObject.getBoolean("flag"), dingweicity, jsonObject.getString("co"),
				jsonObject.getString("so2"), jsonObject.getString("o3"), jsonObject.getString("no2"),
				jsonObject.getString("aqi"), jsonObject.getString("pm10"), currentCityLongitude + "",
				jsonObject.getString("pm2.5"), currentCityLatitude + "", jsonObject.getString("primary_pollutant"),
				jsonObject.getString("position_name"));
		return nearestPm;
	}

	public static Sweather getNowWeather(String _Json, String dingweicity) throws JSONException {
		JSONObject jsonObject = new JSONObject(_Json);
		JSONObject jsonObject2 = jsonObject.getJSONObject("weather");
		Sweather sweather = new Sweather(jsonObject2.getString("weather"), dingweicity, jsonObject2.getString("temp"),
				jsonObject2.getString("weekday"), jsonObject2.getString("feelTemp"), jsonObject2.getString("realTime"),
				jsonObject2.getString("date"), jsonObject2.getString("level"), jsonObject2.getString("WS"),
				jsonObject2.getString("WD"), jsonObject2.getString("PM2Dot5Data"), jsonObject2.getString("Lunar"),
				jsonObject2.getString("pm25"), jsonObject2.getString("SD"));
		return sweather;
	}

	public static List<WeatherInfo7> getWeatherInfo(String _Json) throws JSONException {
		List<WeatherInfo7> info7s = new ArrayList<WeatherInfo7>();
		JSONObject jsonObject = new JSONObject(_Json);
		JSONArray jsonArray = jsonObject.getJSONArray("weather");
		for (int i = 0; i < jsonArray.length(); i++) {
			WeatherInfo7 weatherInfo7 = new WeatherInfo7();
			JSONObject everyDayWeather = (JSONObject) jsonArray.get(i);
			weatherInfo7.setTemp(everyDayWeather.getString("temp"));
			weatherInfo7.setWind(everyDayWeather.getString("wind"));
			weatherInfo7.setWeather(everyDayWeather.getString("weather"));
			weatherInfo7.setTodayTime(everyDayWeather.getString("week"));

			info7s.add(weatherInfo7);
		}
		return info7s;
	}

	public static ChatMsg jsonNewsAccept(Context context, String json, String userId) {
		SharedPreferencesUtil mSpUtil;
		mSpUtil = SharedPreferencesUtil.getInstance(context);
		ChatMsg chat = new ChatMsg();
		try {
			JSONObject j1 = new JSONObject(json);
			boolean flag = j1.getBoolean("flag");
			chat.setFlag(j1.getBoolean("flag"));
			List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
			if (flag) {
				JSONArray array = j1.getJSONArray("lists");
				for (int i = 0; i < array.length(); i++) {
					JSONObject j2 = array.getJSONObject(i);
					String type = j2.getString("type");
					if (type.equals("1")) {
						ChatMsgEntity e1 = new ChatMsgEntity();
						e1.setAccount_id(j2.getString("account_id"));
						e1.setAccount_id_num(j2.getString("account_id_num"));
						e1.setAuthor(j2.getString("author"));
						e1.setCreate_time(j2.getString("publish_time"));
						e1.setFace_pic_url(j2.getString("face_pic_url"));
						e1.setSummary(j2.getString("summary"));
						e1.setTitle(j2.getString("title"));
						e1.setXiaoxi_id(j2.getString("id"));
						ContentValues values = new ContentValues();
						DBManager dbManager = DBManager.getInstances(context);
						values.put("userID", userId);
						values.put("account_id_num", j2.getString("account_id_num"));
						values.put("account_id", j2.getString("account_id"));
						values.put("xiaoxi_id", j2.getString("id"));
						values.put("title", j2.getString("title"));
						values.put("author", j2.getString("author"));
						// values.put("content", j2.getString("content"));
						values.put("face_pic_url", j2.getString("face_pic_url"));
						values.put("summary", j2.getString("summary"));
						values.put("isread", "0");
						values.put("isfirst", "1");
						// values.put("ishistory", "0");
						values.put("publish_time", j2.getString("publish_time"));
						String userpublicID = userId + "*" + j2.getString("account_id");
						if (userId.equals("0")) {
							dbManager.insertSQLite(context, DBInfo.TABLE_NAME_NOUIC, null, values);
						} else {
							if (mSpUtil.getSwitchButton(userpublicID)) {
								dbManager.insertSQLite(context, "uic", null, values);
							}
						}
						list.add(e1);
					}

					else if (type.equals("3")) {
						MyLog.i(">>>>>type" + "3");
						ChatMsgEntity e1 = new ChatMsgEntity();
						e1.setXiaoxi_id(j2.getString("id"));
						e1.setContent(j2.getString("content"));
						MyLog.i(">>>>>>content" + j2.getString("content"));
						e1.setAccount_id(j2.getString("account_id"));
						e1.setCreate_time(j2.getString("publish_time"));
						e1.setAccount_id_num(j2.getString("account_id_num"));
						ContentValues values = new ContentValues();
						DBManager dbManager = DBManager.getInstances(context);
						values.put("userID", userId);
						values.put("account_id_num", j2.getString("account_id_num"));
						values.put("account_id", j2.getString("account_id"));
						values.put("xiaoxi_id", j2.getString("id"));
						values.put("content", j2.getString("content"));
						values.put("isread", "0");
						values.put("isfirst", "1");
						values.put("publish_time", j2.getString("publish_time"));
						if (userId.equals("0")) {
							dbManager.insertSQLite(context, DBInfo.TABLE_NAME_NOUIC, null, values);
						} else {
							dbManager.insertSQLite(context, "uic", null, values);
						}
						list.add(e1);
					} else {
						continue;
					}

				}
				MyLog.i(">>>>>listsize" + list.size());
				chat.setList(list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return chat;
	}

	public static ChatMsg jsonNewsLeast(Context context, String json, String userId) {
		SharedPreferencesUtil mSpUtil;
		mSpUtil = SharedPreferencesUtil.getInstance(context);
		ChatMsg chat = new ChatMsg();
		try {
			JSONObject j1 = new JSONObject(json);
			boolean flag = j1.getBoolean("flag");
			chat.setFlag(j1.getBoolean("flag"));
			List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
			if (flag) {
				JSONArray array = j1.getJSONArray("lists");
				for (int i = 0; i < array.length(); i++) {
					JSONObject j2 = array.getJSONObject(i);
					ChatMsgEntity e1 = new ChatMsgEntity();
					e1.setAccount_id(j2.getString("account_id"));
					e1.setAccount_id_num(j2.getString("account_id_num"));
					e1.setAuthor(j2.getString("author"));
					e1.setCreate_time(j2.getString("publish_time"));
					e1.setFace_pic_url(j2.getString("face_pic_url"));
					e1.setSummary(j2.getString("summary"));
					e1.setTitle(j2.getString("title"));
					e1.setXiaoxi_id(j2.getString("id"));
					ContentValues values = new ContentValues();
					DBManager dbManager = DBManager.getInstances(context);
					values.put("userID", userId);
					values.put("account_id_num", j2.getString("account_id_num"));
					values.put("account_id", j2.getString("account_id"));
					values.put("xiaoxi_id", j2.getString("id"));
					values.put("title", j2.getString("title"));
					values.put("author", j2.getString("author"));
					values.put("face_pic_url", j2.getString("face_pic_url"));
					values.put("summary", j2.getString("summary"));
					values.put("isread", "1");
					values.put("isfirst", "1");
					values.put("publish_time", j2.getString("publish_time"));
					String userpublicID = userId + "*" + j2.getString("account_id");
					if (userId.equals("0")) {
						dbManager.insertSQLite(context, DBInfo.TABLE_NAME_NOUIC, null, values);
					} else {
						if (mSpUtil.getSwitchButton(userpublicID)) {
							dbManager.insertSQLite(context, "uic", null, values);
						}
					}
					list.add(e1);
					chat.setList(list);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return chat;
	}

	public static DiscoverFlagModel findPassword(String _Json) throws JSONException {

		DiscoverFlagModel discoverFlagModel = new DiscoverFlagModel();
		try {
			JSONObject jsonObject = new JSONObject(_Json);
			if ((jsonObject.getString("flag")).equals("ok")) {
				discoverFlagModel.setFlag(true);
			} else {
				discoverFlagModel.setFlag(false);
			}
		} catch (Exception e) {
			discoverFlagModel.setFlag(false);
		}
		return discoverFlagModel;
	}

	public static UserRegisterModel register(String _Json) throws JSONException {

		UserRegisterModel userRegisterModel = new UserRegisterModel();
		try {
			JSONObject jsonObject = new JSONObject(_Json);
			if ((jsonObject.getString("flag")).equals("ok")) {
				userRegisterModel.setFlag(true);
				userRegisterModel.setMsg("");
			} else {
				userRegisterModel.setFlag(false);
				userRegisterModel.setMsg(jsonObject.getString("msg"));
			}
		} catch (Exception e) {
			userRegisterModel.setFlag(false);
		}
		return userRegisterModel;
	}

	public static EnvironmentForecastWeeklyModel getAqiDetailForecastWeekly(String _Json) throws JSONException {
		MyLog.i("getAqiDetailForecastWeekly");
		EnvironmentForecastWeeklyModel environmentForecastWeeklyModel = new EnvironmentForecastWeeklyModel();
		JSONObject parseWeeks = new JSONObject(_Json);
		environmentForecastWeeklyModel.setFlag(parseWeeks.getBoolean("flag"));
		environmentForecastWeeklyModel.setAqi_level_1(parseWeeks.getString("aqi_level_1"));
		environmentForecastWeeklyModel.setAqi_level_2(parseWeeks.getString("aqi_level_2"));
		environmentForecastWeeklyModel.setAqi_level_3(parseWeeks.getString("aqi_level_3"));
		environmentForecastWeeklyModel.setAqi_level_4(parseWeeks.getString("aqi_level_4"));
		environmentForecastWeeklyModel.setAqi_level_5(parseWeeks.getString("aqi_level_5"));
		environmentForecastWeeklyModel.setAqi_level_6(parseWeeks.getString("aqi_level_6"));
		environmentForecastWeeklyModel.setAqi_1(parseWeeks.getString("aqi_1"));
		environmentForecastWeeklyModel.setAqi_2(parseWeeks.getString("aqi_2"));
		environmentForecastWeeklyModel.setAqi_3(parseWeeks.getString("aqi_3"));
		environmentForecastWeeklyModel.setAqi_4(parseWeeks.getString("aqi_4"));
		environmentForecastWeeklyModel.setAqi_5(parseWeeks.getString("aqi_5"));
		environmentForecastWeeklyModel.setAqi_6(parseWeeks.getString("aqi_6"));

		environmentForecastWeeklyModel.setWeek1(parseWeeks.getString("week1"));
		environmentForecastWeeklyModel.setWeek2(parseWeeks.getString("week2"));
		environmentForecastWeeklyModel.setWeek3(parseWeeks.getString("week3"));
		environmentForecastWeeklyModel.setWeek4(parseWeeks.getString("week4"));
		environmentForecastWeeklyModel.setWeek5(parseWeeks.getString("week5"));
		environmentForecastWeeklyModel.setWeek6(parseWeeks.getString("week6"));
		MyLog.i("getAqiDetailForecastWeekly :" + environmentForecastWeeklyModel);
		return environmentForecastWeeklyModel;
	}

	public static CityDetails jsonWeatherRank(String _Json) {
		// TODO Auto-generated method stub
		CityDetails details = new CityDetails();
		try {
			JSONObject jsonObject = new JSONObject(_Json);
			boolean flag = jsonObject.getBoolean("flag");
			details.setFlag(flag);
			if (!flag) {
				return details;
			} else {
				details.setRanking(jsonObject.getInt("ranking") + "");
				// details.setPosition_name(jsonObject.getString("position_name"));
				JSONObject object = jsonObject.getJSONArray("lists1").getJSONObject(0);
				boolean weekFlag = object.getBoolean("flag");
				if (weekFlag) {
					EnvironmentForecastWeekModel weekModel = new EnvironmentForecastWeekModel();
					weekModel.setWeek1(object.getString("week1"));
					weekModel.setWeek2(object.getString("week2"));
					weekModel.setWeek3(object.getString("week3"));
					weekModel.setWeek4(object.getString("week4"));
					weekModel.setWeek5(object.getString("week5"));
					weekModel.setWeek6(object.getString("week6"));
					weekModel.setAqi_level_1(object.getString("aqi_level_1"));
					weekModel.setAqi_level_2(object.getString("aqi_level_2"));
					weekModel.setAqi_level_3(object.getString("aqi_level_3"));
					weekModel.setAqi_level_4(object.getString("aqi_level_4"));
					weekModel.setAqi_level_5(object.getString("aqi_level_5"));
					weekModel.setAqi_level_6(object.getString("aqi_level_6"));
					details.setWeekModel(weekModel);
				}
				JSONArray array = jsonObject.getJSONArray("lists2");
				List<WeatherInfo24> info24s = new ArrayList<WeatherInfo24>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					WeatherInfo24 in = new WeatherInfo24();
					in.setAqi(object2.getInt("aqi"));
					in.setUpdate_time(object2.getString("update_time"));
					info24s.add(in);
				}
				details.setInfo24s(info24s);
				JSONObject object3 = jsonObject.getJSONArray("lists3").getJSONObject(0);
				Kongqizhishu zhishu = new Kongqizhishu();
				zhishu.setAqi(object3.getString("aqi"));
				zhishu.setCo(object3.getString("co"));
				zhishu.setNo2(object3.getString("no2"));
				zhishu.setO3(object3.getString("o3"));
				zhishu.setPm10(object3.getString("pm10"));
				zhishu.setPm25(object3.getString("pm25"));
				zhishu.setSo2(object3.getString("so2"));
				String lev = object3.getString("level");
				String qingkuang;
				if (lev.equals("优")) {
					qingkuang = "空气质量令人满意，基本无空气污染 ,各类人群可正常活动。";
				} else if (lev.equals("良")) {
					qingkuang = "空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响,极少数异常敏感人群应减少户外活动。";
				} else if (lev.equals(object3)) {
					qingkuang = "易感人群症状有轻度加剧，健康人群出现刺激症状,极少数异常敏感人群应减少户外活动。";
				} else if (lev.equals("中度污染")) {
					qingkuang = "进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响。儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼。";
				} else {
					qingkuang = "健康人群运动耐受力降低，有明显强烈症状，提前出现某些疾病。儿童、老年人和病人应当留在室内，避免体力消耗，一般人群应避免户外活动。";
				}
				zhishu.setQingkuang(qingkuang);
				details.setZhishu(zhishu);
				JSONArray array2 = jsonObject.getJSONArray("lists4");
				List<WeatherInfo7_tian> weInfo7 = new ArrayList<WeatherInfo7_tian>();
				List<WeatherInfo7_tian> weInfo7_Rell = new ArrayList<WeatherInfo7_tian>();
				for (int i = 0; i < array2.length(); i++) {
					try {
						JSONObject object2 = array2.getJSONObject(i);
						WeatherInfo7_tian tian = new WeatherInfo7_tian();
						tian.setAqi(object2.getInt("aqi_w"));
						tian.setUpdate_time(object2.getString("update_time_w"));
						weInfo7.add(tian);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				// {"position":"万寿西宫","aqi":"127","pm25":"96","pm10":"102","no2":"71","so2":"2","co":"1.1","o3":"30"}
				JSONArray stationArray = jsonObject.getJSONArray("lists5");
				List<AqiStationModel> aqiStationModels = new ArrayList<AqiStationModel>();
				for (int i = 0; i < stationArray.length(); i++) {
					JSONObject stationDetail = stationArray.getJSONObject(i);
					AqiStationModel aqiStationModel = new AqiStationModel(stationDetail.getString("position"),
							stationDetail.getInt("aqi"), stationDetail.getString("pm25"),
							stationDetail.getString("pm10"), stationDetail.getString("no2"),
							stationDetail.getString("so2"), stationDetail.getString("co"),
							stationDetail.getString("o3"));
					MyLog.i("aqiStationModel:" + aqiStationModel);
					aqiStationModels.add(aqiStationModel);
				}
				details.setAqiStationModels(aqiStationModels);
				if (null != weInfo7 && weInfo7.size() != 7) {
					for (int i = 0; i < 7; i++) {
						if (weInfo7.size() > i && null != weInfo7.get(i)) {
							weInfo7_Rell.add(weInfo7.get(i));
						} else {
							WeatherInfo7_tian info7_tian = new WeatherInfo7_tian();
							info7_tian.setAqi(0);
							info7_tian.setUpdate_time("0");
							weInfo7_Rell.add(info7_tian);
						}

					}
				}
				details.setWeInfo7(weInfo7);
				details.setPosition_name(jsonObject.getString("position_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		MyLog.i("details:" + details);
		return details;
	}

	public static CityDetails parseCity(String _json) {
		CityDetails details = new CityDetails();
		try {
			JSONObject object = new JSONObject(_json);
			boolean flag = object.getBoolean("flag");
			details.setFlag(flag);
			if (!flag)
				return null;
			JSONObject object2 = object.getJSONObject("detail");
			details.setAqi(object2.getJSONObject("listfirst").getString("aqi"));
			details.setAirlevel(object2.getJSONObject("listfirst").getString("airlevel"));
			List<WeatherInfo7_tian> weInfo7 = new ArrayList<WeatherInfo7_tian>();
			JSONArray array = object2.getJSONArray("list7d");
			for (int i = array.length() - 1; i >= 0; i--) {
				JSONObject object3 = array.getJSONObject(i);
				WeatherInfo7_tian info7_tian = new WeatherInfo7_tian();
				info7_tian.setAqi(Integer.parseInt(object3.getString("aqi")));
				info7_tian.setUpdate_time(object3.getString("time"));
				weInfo7.add(info7_tian);
			}
			details.setWeInfo7(weInfo7);
			List<WeatherInfo24> weInfo24 = new ArrayList<WeatherInfo24>();
			JSONArray array2 = object2.getJSONArray("list24h");
			for (int i = 0; i < array2.length(); i++) {
				JSONObject object3 = array2.getJSONObject(i);
				WeatherInfo24 info24 = new WeatherInfo24();
				info24.setAqi(Integer.parseInt(object3.getString("aqi")));
				info24.setUpdate_time(object3.getString("time"));
				weInfo24.add(info24);
			}
			details.setInfo24s(weInfo24);
			List<PM10Info24H> pm10Info24H = new ArrayList<PM10Info24H>();
			JSONArray array8 = object2.getJSONArray("listpm10");
			for (int i = 0; i < array8.length(); i++) {
				JSONObject object4 = array8.getJSONObject(i);
				PM10Info24H infoPM10 = new PM10Info24H();
				infoPM10.setPm10(Integer.parseInt(object4.getString("pm10")));
				infoPM10.setTime(object4.getString("time"));
				pm10Info24H.add(infoPM10);
			}
			details.setPm10Info24Hs(pm10Info24H);
			List<PM25> pm25Info24H = new ArrayList<PM25>();
			JSONArray array9 = object2.getJSONArray("listpm25");
			for (int i = 0; i < array9.length(); i++) {
				JSONObject object5 = array9.getJSONObject(i);
				PM25 infoPM25 = new PM25();
				infoPM25.setPm25(Integer.parseInt(object5.getString("pm25")));
				infoPM25.setTime(object5.getString("time"));
				pm25Info24H.add(infoPM25);
			}
			details.setPm25Info24Hs(pm25Info24H);
			List<WeatherInfoMonth> weInfomInfoMonths = new ArrayList<WeatherInfoMonth>();
			JSONArray array3 = object2.getJSONArray("list30d");
			for (int i = 0; i < array3.length(); i++) {
				JSONObject object3 = array3.getJSONObject(i);
				WeatherInfoMonth infoMonth = new WeatherInfoMonth();
				infoMonth.setAqi(Integer.parseInt(object3.getString("aqi")));
				infoMonth.setUpdate_time(object3.getString("monidate"));
				weInfomInfoMonths.add(infoMonth);
			}
			details.setWeInfoMonth(weInfomInfoMonths);
			List<WeatherInfoYear> weatherInfoYears = new ArrayList<WeatherInfoYear>();
//			JSONArray array4 = object2.getJSONArray("listyear");
//			if(array4!=null&&array4.length()>0){
//				for (int i = 0; i < array4.length(); i++) {
//					JSONObject object3 = array4.getJSONObject(i);
//					WeatherInfoYear infoYear = new WeatherInfoYear();
//					infoYear.setAqi(Integer.parseInt(object3.getString("aqi")));
//					infoYear.setUpdate_time(object3.getString("monidate"));
//					weatherInfoYears.add(infoYear);
//				}
//			}
			details.setWeInfoYear(weatherInfoYears);
			List<AqiStationModel> aqiStationModels = new ArrayList<AqiStationModel>();
			try {
				JSONArray array5 = object2.getJSONArray("listaqi");
				for (int i = 0; i < array5.length(); i++) {
					JSONObject object3 = array5.getJSONObject(i);
					AqiStationModel aqiStationModel = new AqiStationModel();
					aqiStationModel.setAqi(Integer.parseInt(object3.getString("aqi")));
					aqiStationModel.setPosition(object3.getString("stationname"));
					aqiStationModel.setStationcode(object3.getString("stationcode"));
					aqiStationModel.setMonidate(object3.getString("monidate"));
					aqiStationModels.add(aqiStationModel);
				}
				details.setAqiStationModels(aqiStationModels);
				JSONObject object3 = object2.getJSONObject("listpollution");
				ListPolluctionModel model = new ListPolluctionModel();
				model.setTime(object3.getString("time"));
				model.setAqi(object3.getInt("aqi"));
				model.setPm25(object3.getInt("pm25"));
				model.setPm10(object3.getInt("pm10"));
				model.setSo2(object3.getInt("so2"));
				model.setNo2(object3.getInt("no2"));
				model.setCo(object3.getDouble("co"));
				model.setO3(object3.getInt("o3"));
				model.setAirlevel(object3.getString("airlevel"));
				model.setPrimarypollutant(object3.optString("primarypollutant",""));
				details.setPolluctionModel(model);

			} catch (Exception e) {
				// TODO: handle exception
			}
			List<WindModel> windModels = new ArrayList<WindModel>();
			JSONArray array6 = object2.getJSONArray("listwind");
			for (int i = 0; i < array6.length(); i++) {
				JSONObject object4 = array6.getJSONObject(i);
				WindModel windModel = new WindModel();
				windModel.setTime(object4.getString("time"));
				windModel.setWind((float) (object4.getDouble("wind") * 10));
				windModels.add(windModel);
			}
			details.setWindModels(windModels);
			List<ListHumiDityModel> humiDityModels = new ArrayList<ListHumiDityModel>();
			JSONArray array7 = object2.getJSONArray("listhumidity");
			for (int i = 0; i < array7.length(); i++) {
				JSONObject object5 = array7.getJSONObject(i);
				ListHumiDityModel humiDityModel = new ListHumiDityModel();
				humiDityModel.setTimne(object5.getString("time"));
				humiDityModel.setHumidity(object5.getInt("humidity"));
				humiDityModels.add(humiDityModel);
			}
			details.setHumiDityModels(humiDityModels);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return details;
	}

	public static TrendModel jsonTrendModel(String _json) throws JSONException {
		TrendModel details = new TrendModel();
		Gson gson = new Gson();
		JSONObject object = new JSONObject(_json);
		List<AQI> listAQI7day = new ArrayList<AQI>();
		List<PM10Info24H> listPM10Info24Hs = new ArrayList<PM10Info24H>();
		List<AQI> listAQI24h = new ArrayList<AQI>();
		List<AQI> listAQI30d = new ArrayList<AQI>();
		List<PM25> listPM25Of24h = new ArrayList<PM25>();
		List<SO2> listSO2Of24h = new ArrayList<SO2>();
		List<NO2> listNO2Of24h = new ArrayList<NO2>();
		boolean flag = object.getBoolean("flag");
		if (!flag) {
			return null;
		}
		JSONObject object2 = object.getJSONObject("detail");
		String jsonAQI7day = object2.getString("list7d");
		listAQI7day = gson.fromJson(jsonAQI7day, new TypeToken<List<AQI>>() {
		}.getType());
		String jsonPM10Info24H = object2.getString("listpm10");
		listPM10Info24Hs = gson.fromJson(jsonPM10Info24H, new TypeToken<List<PM10Info24H>>() {
		}.getType());
		String jsonAQI24h = object2.getString("list24h");
		listAQI24h = gson.fromJson(jsonAQI24h, new TypeToken<List<AQI>>() {
		}.getType());
		String jsonAQI30d = object2.getString("list30d");
		listAQI30d = gson.fromJson(jsonAQI30d, new TypeToken<List<AQI>>() {
		}.getType());

		String jsonPM25Of24h = object2.getString("listpm25");
		listPM25Of24h = gson.fromJson(jsonPM25Of24h, new TypeToken<List<PM25>>() {
		}.getType());
		String jsonSO2Of24h = object2.getString("listso2");
		listSO2Of24h = gson.fromJson(jsonSO2Of24h, new TypeToken<List<SO2>>() {
		}.getType());
		String jsonNO2Of24h = object2.getString("listno2");
		listNO2Of24h = gson.fromJson(jsonNO2Of24h, new TypeToken<List<NO2>>() {
		}.getType());
		details.setListAQI7day(listAQI7day);
		details.setPm10Info24Hs(listPM10Info24Hs);
		details.setListAQI24h(listAQI24h);
		details.setListAQI30d(listAQI30d);
		details.setListPM25Of24h(listPM25Of24h);
		details.setListSO2Of24h(listSO2Of24h);
		details.setListNO2Of24h(listNO2Of24h);
		return details;
	}

	public static CurrentWeather parseCurrentWeather(String _json) {
		CurrentWeather weather = null;
		try {
			JSONObject jsonObject = new JSONObject(_json);
			boolean flag = jsonObject.getBoolean("flag");
			weather = new CurrentWeather();
			weather.setFlag(flag);
			if (!flag) {
				return weather;
			} else {
				JSONObject object = jsonObject.getJSONArray("nowWeather").getJSONObject(0);
				Sweather sweather = new Sweather();
				sweather.setCity(object.getString("city"));
				sweather.setRealTime(object.getString("realTime"));
				sweather.setDate(object.getString("date"));
				sweather.setWeekday(object.getString("weekday"));
				sweather.setLunar(object.getString("Lunar"));
				// sweather.setFeelTemp(object.getString("feelTemp").substring(0,
				// object.getString("feelTemp").length() - 1));
				sweather.setFeelTemp(object.getString("feelTemp").substring(0, object.getString("feelTemp").length()));
				sweather.setWeather(object.getString("weather"));
				sweather.setPm25(object.getString("pm25"));
				sweather.setPM2Dot5Data(object.getString("PM2Dot5Data"));
				sweather.setLevel(object.getString("level"));
				sweather.setTemp(object.getString("temp"));
				sweather.setWindDirection(object.getString("WD") + object.getString("WS"));
				sweather.setSD(object.getString("SD"));
				sweather.setPm25_near(object.getString("pm25_near"));
				sweather.setPM2Dot5Data_near(object.getString("PM2Dot5Data_near"));
				sweather.setLevel_near(object.getString("level_near"));
				sweather.setPosition_name_near(object.getString("position_name_near"));
				sweather.setPosition_name(object.getString("position_name"));

				weather.setSweather(sweather);
				List<Trend> trends = new ArrayList<Trend>();
				JSONArray array = jsonObject.getJSONArray("weather");
				for (int i = 0; i < array.length(); i++) {
					Trend trend = new Trend();
					JSONObject object2 = array.getJSONObject(i);
					trend.setTemp(object2.getString("temp"));
					trend.setWeather(object2.getString("weather"));
					trend.setWeek(object2.getString("week"));
					trend.setDate(object2.getString("date"));
					trends.add(trend);
				}
				weather.setTrends(trends);
				JSONObject object3 = jsonObject.getJSONArray("index").getJSONObject(0);
				LifeItem life = new LifeItem();
				life.setIndex_cl(object3.getString("index_cl"));
				life.setIndex_cl_xs(object3.getString("index_cl_xs"));
				life.setIndex_co(object3.getString("index_co"));
				life.setIndex_co_xs(object3.getString("index_co_xs"));
				life.setIndex_ls(object3.getString("index_ls"));
				life.setIndex_ls_xs(object3.getString("index_ls_xs"));
				life.setIndex_xc(object3.getString("index_xc"));
				life.setIndex_xc_xs(object3.getString("index_xc_xs"));
				life.setIndex_cy(object3.getString("index_cy"));
				life.setIndex_cy_xs(object3.getString("index_cy_xs"));
				life.setIndex_uv(object3.getString("index_uv"));
				life.setIndex_uv_xs(object3.getString("index_uv_xs"));
				life.setIndex_tr(object3.getString("index_tr"));
				life.setIndex_tr_xs(object3.getString("index_tr_xs"));
				weather.setLife(life);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weather;
	}

	public static String getBlogPic(String _Json) {
		JSONObject jsonObject;
		String url = null;
		try {
			jsonObject = new JSONObject(_Json);
			boolean flag = jsonObject.getBoolean("flag");
			if (flag) {
				url = jsonObject.getString("url");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return url;

	}

	// public static UserMail parseUserMail(String _json) {
	// UserMail userMail = null;
	// try {
	// JSONObject jsonObject = new JSONObject(_json);
	// userMail = new UserMail();
	// userMail.setFlag(jsonObject.getBoolean("flag"));
	// userMail.setMsg(jsonObject.getString("message"));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return userMail;
	// }

	public static UserMail jsonEmail(String _Json) {
		// TODO Auto-generated method stub
		UserMail mail = null;
		try {
			JSONObject object = new JSONObject(_Json);
			mail = new UserMail();
			mail.setFlag(object.getString("flag"));
			mail.setMessage(object.getString("message"));
			MyLog.i(">>>>>>>>>>>>>>mail" + mail);
			return mail;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 把用户信息的json串转化为UserGetUerInfoModel对象
	 * 
	 * @param _Json
	 * @return 用户信息
	 */
	public static UserGetUerInfoModel jsonUserInfo(String _Json) {
		// TODO Auto-generated method stub
		UserGetUerInfoModel userGetUerInfoModel = null;
		try {
			JSONObject object = new JSONObject(_Json);
			userGetUerInfoModel = new UserGetUerInfoModel();
			userGetUerInfoModel.setPhone(object.getString("phone"));
			userGetUerInfoModel.setIsPhoneBind(object.getString("isPhoneBind"));
			userGetUerInfoModel.setIsEmailBind(object.getString("isEmailBind"));
			MyLog.i(">>>>>>>>>>>>>>userGetUerInfoModel" + userGetUerInfoModel);
			return userGetUerInfoModel;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 把噪声历史数据的json串转化为NoiseHistoryModel对象
	 * 
	 */
	public static NoiseHistoryModel jsonNoise(String _Json) {
		NoiseHistoryModel noiseHistoryModel = null;
		List<NoiseItemModel> list = new ArrayList<NoiseItemModel>();
		try {
			JSONObject object = new JSONObject(_Json);
			noiseHistoryModel = new NoiseHistoryModel();
			noiseHistoryModel.setFlag(object.getString("flag"));
			JSONArray array = object.getJSONArray("list");
			for (int i = 0; i < array.length(); i++) {
				NoiseItemModel noiseModel = new NoiseItemModel();
				JSONObject json = array.getJSONObject(i);
				noiseModel.setId(json.getString("id"));
				noiseModel.setAddress(json.getString("adress"));
				noiseModel.setCreate_time(json.getString("create_time"));
				noiseModel.setLatitude(json.getString("latitude"));
				noiseModel.setLongitude(json.getString("longitude"));
				noiseModel.setUpdate_time(json.getString("update_time"));
				noiseModel.setUser_id(json.getString("user_id"));
				noiseModel.setValue(json.getString("value"));
				list.add(noiseModel);
			}
			noiseHistoryModel.setList(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return noiseHistoryModel;
	}

	public static GradeModel jsonGrade(String _Json) {
		GradeModel model = new GradeModel();
		try {
			JSONObject object = new JSONObject(_Json);
			model.setLevel(object.getString("level"));
			model.setCoin(object.getString("coin"));
			model.setExp(object.getString("exp"));
			model.setFull(object.getString("full"));
			model.setNeed(object.getString("need"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;
	}

	public static AlarmHistoryModel parseAlarmHistory(String _Json) {
		AlarmHistoryModel alarm = new AlarmHistoryModel();
		try {
			JSONObject jsonObject = new JSONObject(_Json);
			boolean flag = jsonObject.getBoolean("flag");
			alarm.setFlag(flag);
			if (flag) {
				JSONArray array = jsonObject.getJSONArray("list");
				List<ModelAlarmHistory> list = new ArrayList<ModelAlarmHistory>();
				for (int i = 0; i < array.length(); i++) {
					ModelAlarmHistory item = new ModelAlarmHistory();
					JSONObject object = array.getJSONObject(i);
					item.setProvince(object.getString("province"));
					item.setTown(object.getString("town"));
					item.setTitle(object.getString("title"));
					item.setMessage(object.getString("message"));
					item.setTime(object.getString("time"));
					item.setUrl(object.getString("url"));
					item.setAlarm(object.getString("alarm"));
					list.add(item);
				}
			}
		} catch (JSONException e) {
			alarm = new AlarmHistoryModel();
		}
		return alarm;
	}

	public static void jsonAlarm(Context context, String json) {
		try {
			JSONObject object = new JSONObject(json);
			boolean flag = object.getBoolean("flag");
			if (!flag) {
				return;
			} else {
				List<ModelAlarmHistory> modelList = new ArrayList<ModelAlarmHistory>();
				JSONArray array = object.getJSONArray("list");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					ModelAlarmHistory model = new ModelAlarmHistory();
					model.setProvince(object2.getString("province"));
					model.setTown(object2.getString("town"));
					model.setTitle(object2.getString("title"));
					model.setMessage(object2.getString("message"));
					model.setTime(object2.getString("time"));
					model.setUrl(object2.getString("url"));
					model.setAlarm(object2.getString("alarm"));
					if (object2.getString("alarm") != null && !object2.getString("alarm").equals("")) {
						modelList.add(model);
					}
				}
				AlarmDao dao = new AlarmDao(context);
				dao.saveContactList(modelList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<AqiModel> parseMonitoringAqi(String _Json, int isliebiao) {
		// TODO Auto-generated method stub
		List<AqiModel> aqiModels = new ArrayList<AqiModel>();
		String PRIMARYPOLLUTANT = "";
		try {
			JSONObject object = new JSONObject(_Json);
			boolean flag = object.getBoolean("flag");
			if (!flag)
				return null;
			JSONArray array = object.getJSONArray("data");
			if (isliebiao == 1) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject modelObject = array.getJSONObject(i);
					AqiModel model = new AqiModel();
					model.setTime(object.getString("time"));
					int pm10 = modelObject.getInt("pm10");
					int pm25 = modelObject.getInt("pm25");
					int aqi = modelObject.getInt("aqi");
					if (pm10 > 1000) {
						pm10 = 0;
					}
					if (pm25 > 1000) {
						pm25 = 0;
					}
					if (aqi > 1000) {
						aqi = 0;
					}
					model.setCITY(modelObject.getString("city"));
					model.setPM10(pm10 + "");
					model.setPM25(pm25 + "");
					model.setAQI(aqi + "");
					try {
						PRIMARYPOLLUTANT = modelObject.getString("primary");
						if ("-".equals(PRIMARYPOLLUTANT)) {
							PRIMARYPOLLUTANT = "--";
						}
						model.setPRIMARYPOLLUTANT(PRIMARYPOLLUTANT);
					} catch (Exception e) {
						// TODO: handle exception
					}
					aqiModels.add(model);
				}
				return aqiModels;
			} else if (isliebiao == 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject modelObject = array.getJSONObject(i);
					AqiModel model = new AqiModel();
					int pm10 = modelObject.getInt("PM10");
					int pm25 = modelObject.getInt("PM25");
					int aqi = modelObject.getInt("AQI");
					if (pm10 > 1000) {
						pm10 = 0;
					}
					if (pm25 > 1000) {
						pm25 = 0;
					}
					if (aqi > 1000) {
						aqi = 0;
					}
					model.setCITY(modelObject.getString("CITY"));
					model.setPM10(pm10 + "");
					model.setPM25(pm25 + "");
					model.setAQI(aqi + "");
					try {
						PRIMARYPOLLUTANT = modelObject.getString("PRIMARYPOLLUTANT");
						if ("-".equals(PRIMARYPOLLUTANT)) {
							PRIMARYPOLLUTANT = "--";
						}
						model.setPRIMARYPOLLUTANT(PRIMARYPOLLUTANT);
						model.setAIRLEVEL(modelObject.getString("AIRLEVEL"));
						model.setMONIDATE(modelObject.getString("MONIDATE"));
					} catch (Exception e) {
						// TODO: handle exception
					}
					aqiModels.add(model);
				}
				return aqiModels;
			} else {
				JSONArray array2 = object.getJSONArray("lastyear");
				for (int i = 0; i < array.length(); i++) {
					JSONObject modelObject = array.getJSONObject(i);
					JSONObject lastObject = array2.getJSONObject(i);
					AqiModel model = new AqiModel();
					model.setCITY(modelObject.getString("city"));
					String pm10 = modelObject.getInt("pm10") + "";
					String lastpm10 = lastObject.getInt("pm10") + "";
					String pm25 = modelObject.getInt("pm25") + "";
					String lastpm25 = lastObject.getInt("pm25") + "";
					String cnt = modelObject.getInt("cnt") + "";
					String lastcnt = lastObject.getInt("cnt") + "";
					String o3 = modelObject.getInt("o3") + "";
					String lasto3 = lastObject.getInt("o3") + "";
					if (CommonUtil.isCheck(pm10)) {
						pm10 = "--";
					}
					if (CommonUtil.isCheck(lastpm10)) {
						lastpm10 = "--";
					}
					if (CommonUtil.isCheck(pm25)) {
						pm25 = "--";
					}
					if (CommonUtil.isCheck(lastpm25)) {
						lastpm25 = "--";
					}
					if (CommonUtil.isCheck(cnt)) {
						cnt = "--";
					}
					if (CommonUtil.isCheck(lastcnt)) {
						lastcnt = "--";
					}
					if (CommonUtil.isCheck(o3)) {
						o3 = "--";
					}
					if (CommonUtil.isCheck(lasto3)) {
						lasto3 = "--";
					}
					model.setPM10(pm10 + "/" + lastpm10);
					model.setPM25(pm25 + "/" + lastpm25);
					model.setCNT(cnt + "/" + lastcnt);
					model.setO3(o3 + "/" + lasto3);
					model.setYear(object.getString("lastStart"));
					try {
						model.setNowyear(object.getString("lastEnd"));
					} catch (Exception e) {
						// TODO: handle exception
					}
					aqiModels.add(model);
				}
				MyLog.i(">>>>>>>>>>>>>>>cnt" + aqiModels);
				return aqiModels;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<AqiModel> parseMonitoringAqi2(String _Json, int isliebiao) {
		// TODO Auto-generated method stub
		List<AqiModel> aqiModels = new ArrayList<AqiModel>();
		String PRIMARYPOLLUTANT = "";
		try {
			JSONObject object = new JSONObject(_Json);
			boolean flag = object.getBoolean("flag");
			if (!flag)
				return null;
//			JSONArray array = object.getJSONArray("data");
			JSONObject jsonObject = object.getJSONObject("detail");
		    String updateTime = object.getString("updatetime");
			JSONArray array = jsonObject.getJSONArray("municipalities");
			if (isliebiao == 1) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject modelObject = array.getJSONObject(i);
					AqiModel model = new AqiModel();
					model.setTime(object.getString("time"));
					int pm10 = modelObject.getInt("pm10");
					int pm25 = modelObject.getInt("pm25");
					int aqi = modelObject.getInt("aqi");
					if (pm10 > 1000) {
						pm10 = 0;
					}
					if (pm25 > 1000) {
						pm25 = 0;
					}
					if (aqi > 1000) {
						aqi = 0;
					}
					model.setCITY(modelObject.getString("city"));
					model.setPM10(pm10 + "");
					model.setPM25(pm25 + "");
					model.setAQI(aqi + "");
					try {
						PRIMARYPOLLUTANT = modelObject.getString("primary");
						if ("-".equals(PRIMARYPOLLUTANT)) {
							PRIMARYPOLLUTANT = "--";
						}
						model.setPRIMARYPOLLUTANT(PRIMARYPOLLUTANT);
					} catch (Exception e) {
						// TODO: handle exception
					}
					aqiModels.add(model);
				}
				return aqiModels;
			} else if (isliebiao == 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject modelObject = array.getJSONObject(i);
					jsonMethod(aqiModels, updateTime, modelObject);
				}
				JSONObject cityMeanObject = jsonObject.getJSONObject("citymean");
				jsonMethod(aqiModels, updateTime, cityMeanObject);
				JSONArray straightcountyArray = jsonObject.getJSONArray("straightcounty");
				for (int i = 0; i < straightcountyArray.length(); i++) {
					JSONObject straightcountyObject = straightcountyArray.getJSONObject(i);
					jsonMethod(aqiModels, updateTime, straightcountyObject);
				}
				JSONObject countymeanObject = jsonObject.getJSONObject("countymean");
				jsonMethod(aqiModels, updateTime, countymeanObject);
				
				return aqiModels;
			} else {
				JSONArray array2 = object.getJSONArray("lastyear");
				for (int i = 0; i < array.length(); i++) {
					JSONObject modelObject = array.getJSONObject(i);
					JSONObject lastObject = array2.getJSONObject(i);
					AqiModel model = new AqiModel();
					model.setCITY(modelObject.getString("city"));
					String pm10 = modelObject.getInt("pm10") + "";
					String lastpm10 = lastObject.getInt("pm10") + "";
					String pm25 = modelObject.getInt("pm25") + "";
					String lastpm25 = lastObject.getInt("pm25") + "";
					String cnt = modelObject.getInt("cnt") + "";
					String lastcnt = lastObject.getInt("cnt") + "";
					String o3 = modelObject.getInt("o3") + "";
					String lasto3 = lastObject.getInt("o3") + "";
					if (CommonUtil.isCheck(pm10)) {
						pm10 = "--";
					}
					if (CommonUtil.isCheck(lastpm10)) {
						lastpm10 = "--";
					}
					if (CommonUtil.isCheck(pm25)) {
						pm25 = "--";
					}
					if (CommonUtil.isCheck(lastpm25)) {
						lastpm25 = "--";
					}
					if (CommonUtil.isCheck(cnt)) {
						cnt = "--";
					}
					if (CommonUtil.isCheck(lastcnt)) {
						lastcnt = "--";
					}
					if (CommonUtil.isCheck(o3)) {
						o3 = "--";
					}
					if (CommonUtil.isCheck(lasto3)) {
						lasto3 = "--";
					}
					model.setPM10(pm10 + "/" + lastpm10);
					model.setPM25(pm25 + "/" + lastpm25);
					model.setCNT(cnt + "/" + lastcnt);
					model.setO3(o3 + "/" + lasto3);
					model.setYear(object.getString("lastStart"));
					try {
						model.setNowyear(object.getString("lastEnd"));
					} catch (Exception e) {
						// TODO: handle exception
					}
					aqiModels.add(model);
				}
				MyLog.i(">>>>>>>>>>>>>>>cnt" + aqiModels);
				return aqiModels;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void jsonMethod(List<AqiModel> aqiModels, String updateTime,
			JSONObject jsonObject) throws JSONException {
		String PRIMARYPOLLUTANT;
		AqiModel model = new AqiModel();
		String pm10 = jsonObject.getString("PM10");
		String pm25 = jsonObject.getString("PM25");
		String aqi = jsonObject.getString("AQI");

		model.setCITY(jsonObject.getString("CITYNAME"));
		model.setPM10(pm10);
		model.setPM25(pm25);
		model.setAQI(aqi);
		try {
			PRIMARYPOLLUTANT = jsonObject.getString("PRIMARYPOLLUTANT");
			if ("-".equals(PRIMARYPOLLUTANT)) {
				PRIMARYPOLLUTANT = "--";
			}
			model.setPRIMARYPOLLUTANT(PRIMARYPOLLUTANT);
			model.setAIRLEVEL(jsonObject.getString("AIRLEVEL"));
			model.setMONIDATE(updateTime);
		} catch (Exception e) {
			// TODO: handle exception
		}
		aqiModels.add(model);
	}

	public static List<ThreeDayForestModel> jsonGetThreeForestDataList(String json) throws JSONException {
		Gson gson = new Gson();
		List<ThreeDayForestModel> listThreeDayForestModel = new ArrayList<ThreeDayForestModel>();
		JSONObject jsonObject = new JSONObject(json);
		boolean flag = jsonObject.getBoolean("flag");
		if (flag) {
			String jsonData = jsonObject.getString("data");
			if ((jsonObject.getJSONArray("data")).length() > 0) {
				listThreeDayForestModel = gson.fromJson(jsonData, new TypeToken<List<ThreeDayForestModel>>() {
				}.getType());
			}
		}
		return listThreeDayForestModel;
	}

	//

	@SuppressLint("NewApi")
	public static MainAqiData jsonGetMianAqiData(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		boolean flag = jsonObject.getBoolean("flag");
		MainAqiData aqiData = new MainAqiData();
		if (flag) {
			List<ThreeDayAqiTrendModel> dayAqiTrendModels = new ArrayList<ThreeDayAqiTrendModel>();
			// JSONArray array = jsonObject.getJSONArray("list");
			JSONObject object = jsonObject.getJSONObject("detail");
			aqiData.setAqi(object.getInt("aqi"));
			aqiData.setPrimary(object.getString("primary"));
			aqiData.setUpdatetime(object.getString("updatetime"));
			try {
				JSONObject object2 = object.getJSONObject("forecast");
				ThreeDayAqiTrendModel model = new ThreeDayAqiTrendModel();
				// model.setAqi(jsonObject.getString("aqi"));
				model.setMINAIRLEVEL(object2.getInt("minairlevel"));
				model.setMAXAIRLEVEL(object2.getInt("maxairlevel"));
				model.setMONITORTIME(object2.getString("monitortime"));
				model.setFORECASTTIME(object2.getString("forecasttime"));
				dayAqiTrendModels.add(model);
				aqiData.setForecast(dayAqiTrendModels);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return aqiData;
		} else {
			return null;
		}
	}

	@SuppressLint("NewApi")
	public static List<ThreeDayAqiTrendModel> jsonThreeDayAqiTrendModelList(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		boolean flag = jsonObject.getBoolean("flag");
		if (flag) {
			List<ThreeDayAqiTrendModel> dayAqiTrendModels = new ArrayList<ThreeDayAqiTrendModel>();
			// JSONArray array = jsonObject.getJSONArray("forecast");
			JSONArray array = jsonObject.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				ThreeDayAqiTrendModel model = new ThreeDayAqiTrendModel();
				// model.setAqi(jsonObject.getString("aqi"));
				model.setMINAIRLEVEL(object.getInt("MINAIRLEVEL"));
				model.setMAXAIRLEVEL(object.getInt("MAXAIRLEVEL"));
				model.setMONITORTIME(object.getString("MONITORTIME"));
				model.setFORECASTTIME(object.getString("FORECASTTIME"));
				model.setMINAIRLEVEL1(object.getString("MINAIRLEVEL1"));
				model.setMAXAIRLEVEL1(object.getString("MAXAIRLEVEL1"));
				dayAqiTrendModels.add(model);
			}
			return dayAqiTrendModels;
		} else {
			return null;
		}
	}

	public static List<YuCeModel> jsonYuCe(String _Json) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(_Json);
			boolean flag = object.getBoolean("flag");
			if (!flag)
				return null;
			JSONArray array = object.getJSONArray("forecast");
			List<YuCeModel> yuCeModels = new ArrayList<YuCeModel>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				YuCeModel model = new YuCeModel();
				model.setCity(object2.getString("city"));
				JSONArray array2 = object2.getJSONArray("list");
				List<ThreeDayAqiTrendModel> trendModels = new ArrayList<ThreeDayAqiTrendModel>();
				for (int j = 0; j < array2.length(); j++) {
					ThreeDayAqiTrendModel model2 = new ThreeDayAqiTrendModel();
					JSONObject object3 = array2.getJSONObject(j);
					String FORECASTTIME = object3.getString("FORECASTTIME");
					FORECASTTIME = FORECASTTIME.substring(5);
					if (FORECASTTIME.startsWith("0")) {
						FORECASTTIME = FORECASTTIME.substring(1);
					}
					model2.setFORECASTTIME(FORECASTTIME);
					model2.setMINAIRLEVEL(object3.getInt("MINAIRLEVEL"));
					model2.setMAXAIRLEVEL(object3.getInt("MAXAIRLEVEL"));
					model2.setMAXAIRLEVEL1(object3.getString("MAXAIRLEVEL1"));
					model2.setMINAIRLEVEL1(object3.getString("MINAIRLEVEL1"));
					trendModels.add(model2);
				}
				model.setTrendModels(trendModels);
				yuCeModels.add(model);
			}
			return yuCeModels;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static MonitorModel jsonWeatherTask(String _Json) {
		// TODO Auto-generated method stub
		MonitorModel model = new MonitorModel();
		try {
			JSONObject object = new JSONObject(_Json);
			JSONObject object2 = object.getJSONObject("top");
			model.setCITYCODE(object2.getString("CITYCODE"));
			model.setMONIDATE(object2.getString("MONIDATE"));
			model.setSO2(object2.getInt("SO2"));
			model.setNO2(object2.getInt("NO2"));
			model.setCO(object2.getDouble("CO"));
			model.setO3(object2.getInt("O3"));
			model.setPM25(object2.getInt("PM25"));
			model.setPM10(object2.getInt("PM10"));
			model.setAQI(object2.getInt("AQI"));
			model.setPRIMARYPOLLUTANT(object2.getString("PRIMARYPOLLUTANT"));
			model.setAIRLEVEL(object2.getString("AIRLEVEL"));
			model.setSTATIONTYPE(object2.getString("STATIONTYPE"));
			JSONArray array = object.getJSONArray("list");
			List<WeatherInfo24> weatherInfo24s = new ArrayList<WeatherInfo24>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object3 = array.getJSONObject(i);
				WeatherInfo24 info = new WeatherInfo24();
				info.setAqi(object3.getInt("aqi"));
				info.setUpdate_time(object3.getString("monidate"));
				weatherInfo24s.add(info);
			}
			model.setWeatherInfo24s(weatherInfo24s);
			List<PM10Info24H> pm10Info24H = new ArrayList<PM10Info24H>();
			JSONArray arrayPM10 = object.getJSONArray("listPM10");
			for (int i = 0; i < arrayPM10.length(); i++) {
				JSONObject objectPM10 = arrayPM10.getJSONObject(i);
				PM10Info24H infoPM10 = new PM10Info24H();
				infoPM10.setPm10(Integer.parseInt(objectPM10.getString("pm10")));
				infoPM10.setTime(objectPM10.getString("time"));
				pm10Info24H.add(infoPM10);
			}
			model.setPm10Info24Hs(pm10Info24H);
			List<PM25> pm25Info24H = new ArrayList<PM25>();
			JSONArray arrayPM25 = object.getJSONArray("listPM25");
			for (int i = 0; i < arrayPM25.length(); i++) {
				JSONObject objectPM25 = arrayPM25.getJSONObject(i);
				PM25 infoPM25 = new PM25();
				infoPM25.setPm25(Integer.parseInt(objectPM25.getString("pm25")));
				infoPM25.setTime(objectPM25.getString("time"));
				pm25Info24H.add(infoPM25);
			}
			model.setPm25Info24Hs(pm25Info24H);
			List<SO2> so2Info24H = new ArrayList<SO2>();//YYF 获取so2数据---------
			JSONArray arraySO2 = object.getJSONArray("listso2");
			for (int i = 0; i < arraySO2.length(); i++) {
				JSONObject objectSO2 = arraySO2.getJSONObject(i);
				SO2 infoSO2 = new SO2();
				infoSO2.setSo2(Integer.parseInt(objectSO2.getString("so2")));
				infoSO2.setTime(objectSO2.getString("time"));
				so2Info24H.add(infoSO2);
			}
			model.setSO2Info24Hs(so2Info24H);
			List<NO2> no2Info24H = new ArrayList<NO2>();//YYF 获取no2数据---------
			JSONArray arrayNO2 = object.getJSONArray("listno2");
			for (int i = 0; i < arrayNO2.length(); i++) {
				JSONObject objectNO2 = arrayNO2.getJSONObject(i);
				NO2 infoNO2 = new NO2();
				infoNO2.setNo2(Integer.parseInt(objectNO2.getString("no2")));
				infoNO2.setTime(objectNO2.getString("time"));
				no2Info24H.add(infoNO2);
			}
			model.setNO2Info24Hs(no2Info24H);
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<AQIPoint> jsonUpdateAqi(String _Json) {
		// TODO Auto-generated method stub
		List<AQIPoint> aqiPoints = new ArrayList<AQIPoint>();
		try {
			JSONObject object = new JSONObject(_Json);
			boolean flag = object.getBoolean("flag");
			if (!flag)
				return null;
			JSONArray array = object.getJSONObject("detail").getJSONArray("stations");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				AQIPoint point = new AQIPoint(object2.getDouble("LONGITUDE"), object2.getDouble("LATITUDE"),
						object2.getString("AQI"), object2.getString("STATIONNAME"), "", "", "", "", "", "", "");
				aqiPoints.add(point);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aqiPoints;
	}

	public static EnvironmentAqiModel jsonMonitor(String _Json) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(_Json);
			boolean flag = object.getBoolean("flag");
			if (!flag)
				return null;
			JSONObject object2 = object.getJSONObject("detail");
			EnvironmentAqiModel model = new EnvironmentAqiModel();
			model.setAqi(object2.getJSONObject("city").getInt("aqi"));
			model.setLevel(object2.getJSONObject("city").getString("level"));
			model.setUpdatetime(object2.getJSONObject("city").getString("updatetime"));
			model.setPollutant(object2.getJSONObject("city").getString("pollutant"));
			model.setPm10(object2.getJSONObject("avg").getString("pm10"));
			model.setPm25(object2.getJSONObject("avg").getString("pm25"));
			JSONArray array = object2.getJSONArray("stations");
			List<EnvironmentMonitorModel> monitorModels = new ArrayList<EnvironmentMonitorModel>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject object3 = array.getJSONObject(i);
				EnvironmentMonitorModel model2 = new EnvironmentMonitorModel();
				model2.setStation(object3.getString("station"));
				model2.setStationtype(object3.getString("stationtype"));
				JSONArray array2 = object3.getJSONArray("list");
				List<WeatherInfoMonth> infoMonths = new ArrayList<WeatherInfoMonth>();
				for (int j = 0; j < array2.length(); j++) {
					WeatherInfoMonth month = new WeatherInfoMonth();
//					month.setAqi(array2.getJSONObject(j).getInt("aqi"));
					month.setPm25(array2.getJSONObject(j).getInt("pm25"));
					month.setPm10(array2.getJSONObject(j).getInt("pm10"));
					month.setO3(array2.getJSONObject(j).getInt("o3"));
					month.setSo2(array2.getJSONObject(j).getInt("so2"));
					month.setNo2(array2.getJSONObject(j).getInt("no2"));
					month.setCo(array2.getJSONObject(j).getDouble("co"));
					month.setAqi(array2.getJSONObject(j).getInt("aqi"));
					month.setUpdate_time(array2.getJSONObject(j).getString("monidate"));
					infoMonths.add(month);
				}
				model2.setInfoMonths(infoMonths);
				monitorModels.add(model2);
			}
			model.setMonitorModels(monitorModels);
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 省内省外数据合并
	 * @param _Json
	 * @return
	 */
	public static List<AQIPoint> jsonShengHui(String _Json) {
		// TODO Auto-generated method stub
		String[] cityArrays = new String[] { "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河", "三门峡",
				"南阳", "商丘", "信阳", "周口", "驻马店", "济源", "巩义", "兰考县", "汝州", "滑县", "长垣县", "邓州", "永城", "固始县", "鹿邑县", "新蔡县" };
		String cityString = cityArrays.toString();
		MyLog.i(">>>>>>>>.tostring" + cityString);
		try {
			JSONArray cityarray = new JSONArray(_Json);
			for (int i = 0; i < cityarray.length(); i++) {
				JSONObject cityObject = cityarray.getJSONObject(i);
				String name = cityObject.getString("city");
				MyLog.i("baiMap name :" + name);
				JSONObject mainobj = null;// 每个城市 都会返回所有监测点，该对象保存与城市名称相同的那一个
				JSONArray pointlist = cityObject.getJSONArray("val");
				for (int j = 0; j < pointlist.length(); j++)// 遍历城市中的监测站点列表信息，找到有城市名的那个赋给mainobj.
				{
					String pointname = ((JSONObject) pointlist.get(j)).getString("station");
					if (!cityString.contains(name)) {
						//省外城市
						if (name.equals(pointname)) {
							mainobj = (JSONObject) pointlist.get(j);
							AQIPoint pt = new AQIPoint(mainobj.getDouble("longitude"), mainobj.getDouble("latitude"),
									mainobj.getString("aqi"), mainobj.getString("station"), mainobj.getString("pm25"),
									mainobj.getString("so2"), mainobj.getString("no2"), mainobj.getString("o3"),
									mainobj.getString("co"), mainobj.getString("o3"), mainobj.getString("pm10"));
							pt.setCity(name);
							// pt.setUpdateTime(WbMapUtil.strToDate(jsonObject.getString("update_time")));
							pt.setUpdateTime(new Date() + "");
							aqicitys_fromserver.add(pt);
						} else {
							continue;
						}
					} else if (cityString.contains(name)) {
						String pointnamem = ((JSONObject) pointlist.get(j)).getString("station");
						//省内监测点
						if (!name.equals(pointnamem)) {
							mainobj = (JSONObject) pointlist.get(j);
							AQIPoint pt = new AQIPoint(mainobj.getDouble("longitude"), mainobj.getDouble("latitude"),
									mainobj.getString("aqi"), mainobj.getString("station"), mainobj.getString("pm25"),
									mainobj.getString("so2"), mainobj.getString("no2"), mainobj.getString("o3"),
									mainobj.getString("co"), mainobj.getString("o3"), mainobj.getString("pm10"));
							pt.setCity(name);
							// pt.setUpdateTime(WbMapUtil.strToDate(jsonObject.getString("update_time")));
							pt.setUpdateTime(new Date() + "");
							aqicitys_fromserver.add(pt);
						}
					}
				}
				if (mainobj == null) {
					continue;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
		if (aqicitys_fromserver != null && aqicitys_fromserver.size() > 0) {
			for (int i = 0; i < aqicitys_fromserver.size(); i++) {
				AQIPoint point = aqicitys_fromserver.get(i);
				if (i % 8 == 0) {
					zerPoints.add(point);
				} else if (i % 8 == 1) {
					sinPoints.add(point);
				} else if (i % 8 == 2) {
					nextPoints.add(point);
				} else if (i % 8 == 3) {
					fourPoints.add(point);
				} else if (i % 8 == 4) {
					fivePoints.add(point);
				} else if (i % 8 == 5) {
					sinPoints.add(point);
				} else if (i % 8 == 6) {
					sevenPoints.add(point);
				} else if (i % 8 == 7) {
					eightints.add(point);
				}
			}
		}
		MyLog.i(">>>>>>>>>>>>>>>>>>>>citys_fromserver" + aqicitys_fromserver);
		return aqicitys_fromserver;
	}

	public static PmDayHourModel jsonMonitorDayHour(String _Json, boolean comFlag) {
		// TODO Auto-generated method stub
		try {
			PmDayHourModel model = new PmDayHourModel();
			JSONObject object = new JSONObject(_Json);
			boolean flag = object.getBoolean("flag");
//			if (!flag)
//				return null;
			if (comFlag) {
				model.setLevel(object.getJSONObject("detail").getJSONObject("city").getString("level"));
				model.setPollutant(object.getJSONObject("detail").getJSONObject("city").getString("pollutant"));
				model.setTime(object.getJSONObject("detail").getJSONObject("city").getString("time"));
				model.setPm10(object.getJSONObject("detail").getJSONObject("avg").getString("pm10"));
				model.setPm25(object.getJSONObject("detail").getJSONObject("avg").getString("pm25"));
				if(!"null".equals(object.getJSONObject("detail").getString("avg30d"))){
					model.setPmday10(object.getJSONObject("detail").getJSONObject("avg30d").getString("pm10"));
					model.setPmday25(object.getJSONObject("detail").getJSONObject("avg30d").getString("pm25"));
				}	
			} else {
				model.setPollutant(object.getJSONObject("detail").getJSONObject("station").getString("pollutant"));
				model.setTime(object.getJSONObject("detail").getJSONObject("station").getString("time"));
				model.setPm10(object.getJSONObject("detail").getJSONObject("avg").getString("pm10"));
				model.setPm25(object.getJSONObject("detail").getJSONObject("avg").getString("pm25"));
				if(!"null".equals(object.getJSONObject("detail").getString("avg30d"))){
					model.setPmday10(object.getJSONObject("detail").getJSONObject("avg30d").getString("pm10"));
					model.setPmday25(object.getJSONObject("detail").getJSONObject("avg30d").getString("pm25"));
				}	
			}
			JSONArray hourArrays = object.getJSONObject("detail").getJSONArray("list24h");
			List<PmModel> hourModels = new ArrayList<PmModel>();
			for (int i = 0; i < hourArrays.length(); i++) {
				JSONObject object2 = hourArrays.getJSONObject(i);
				PmModel model2 = new PmModel();
				if (comFlag) {
					model2.setTIME(object2.getString("TIME").substring(11, 16));
				} else {
					model2.setTIME(object2.getString("MONIDATE").substring(11, 16));
				}
				model2.setPM25(object2.getString("PM25"));
				model2.setPM10(object2.getString("PM10"));
				model2.setO3(object2.getString("O3"));
				model2.setSO2(object2.getString("SO2"));
				model2.setNO2(object2.getString("NO2"));
				model2.setCO(object2.getString("CO"));
				hourModels.add(model2);
			}
			model.setHourModels(hourModels);
			//YYF 抓取分钟数据---------------0
			boolean isNotList12m = object.getJSONObject("detail").isNull("list12m");
			if(!isNotList12m){
			JSONArray minuteArrays = object.getJSONObject("detail").getJSONArray("list12m");
			List<PmModel> minuteModels = new ArrayList<PmModel>();
			for (int i = 0; i < minuteArrays.length(); i++) {
				JSONObject object2 = minuteArrays.getJSONObject(i);
				PmModel model2 = new PmModel();
				if (comFlag) {
					model2.setTIME(object2.getString("TIME").substring(11, 16));
				} else {
					model2.setTIME(object2.getString("MONIDATE").substring(11, 16));
				}
				model2.setPM25(object2.getString("PM25"));
				model2.setPM10(object2.getString("PM10"));
				model2.setO3(object2.getString("O3"));
				model2.setSO2(object2.getString("SO2"));
				model2.setNO2(object2.getString("NO2"));
				model2.setCO(object2.getString("CO"));
				minuteModels.add(model2);
			}
			model.setMinuteModels(minuteModels);
			}
			//YYF ---------------1
			JSONArray dayModelsArray = object.getJSONObject("detail").getJSONArray("list30d");
			List<PmModel> dayModels = new ArrayList<PmModel>();
			for (int i = 0; i < dayModelsArray.length(); i++) {
				JSONObject object2 = dayModelsArray.getJSONObject(i);
				PmModel model2 = new PmModel();
				if (comFlag) {
					model2.setTIME(object2.getString("TIME").substring(5, 10));
				} else {
					model2.setTIME(object2.getString("MONIDATE").substring(5, 10));
				}
				model2.setPM25(object2.getString("PM25"));
				model2.setPM10(object2.getString("PM10"));
				model2.setO3(object2.getString("O3"));
				model2.setSO2(object2.getString("SO2"));
				model2.setNO2(object2.getString("NO2"));
				model2.setCO(object2.getString("CO"));
				dayModels.add(model2);
			}
			model.setDayModels(dayModels);
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Update getupdate(String string) {
		Update result = new Update();
		try {
			JSONObject jsonObject = new JSONObject(string);
			if (jsonObject.getBoolean("flag")) {
				result.setDownloadUrl(jsonObject.getString("url"));
				result.setVersionCode(jsonObject.getDouble("versionCode"));
				result.setUpdateLog(jsonObject.getString("msg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}