package com.jy.environment.webservice;

import java.io.IOException;
import java.util.List;

import android.content.Context;

import com.jy.environment.model.AlarmHistoryModel;
import com.jy.environment.model.ChatMsg;
import com.jy.environment.model.CityDetails;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.CurrentWeather;
import com.jy.environment.model.DiscoverBlogUpLoadResult;
import com.jy.environment.model.DiscoverDeleteBlogStatueModel;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.model.EnvironmentCityWeatherModel;
import com.jy.environment.model.EnvironmentForecastWeeklyModel;
import com.jy.environment.model.Item;
import com.jy.environment.model.Kongqizhishu;
import com.jy.environment.model.Life;
import com.jy.environment.model.LoginModel;
import com.jy.environment.model.MyPostWeiboInfo;
import com.jy.environment.model.NearestPm;
import com.jy.environment.model.News;
import com.jy.environment.model.PublicService;
import com.jy.environment.model.ResultBlogList;
import com.jy.environment.model.ResultPostBlogComment;
import com.jy.environment.model.ResultSelfBlogList;
import com.jy.environment.model.SearchService;
import com.jy.environment.model.Sweather;
import com.jy.environment.model.UserGetUerInfoModel;
import com.jy.environment.model.UserMail;
import com.jy.environment.model.UserOtherLoginModel;
import com.jy.environment.model.UserRegisterModel;
import com.jy.environment.model.UserUpLoadPicResultModel;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.model.WeatherInfo7;
import com.jy.environment.model.WeatherInfo7_tian;

public interface DataSalesNetworkAPI {

	public static final String CHARSET = "UTF-8";

	public static final int TIME_OUT_LONG = 20000;
	public static final int TIME_OUT_SHORT = 5000;
	public static final int TIME_OUT_NORMAL = 10000;

	/**
	 * 环境说评论
	 * 
	 * @return
	 * @throws IOException
	 */
	ResultPostBlogComment sendBlogComment(String url, String observer,
			String content, String time, String weiboid) throws IOException;

	/**
	 * 获取环境说列表
	 * 
	 * @return
	 * @throws IOException
	 */
	ResultBlogList getBlogList(String url, String userId) throws IOException;

	/**
	 * 获取环境说消息条目数
	 * 
	 * @return
	 * @throws IOException
	 */
	News getNewsTaskCount(String url) throws IOException;

	/**
	 * 
	 * @param deleteBlogurl
	 * @return
	 * @throws IOException
	 */
	DiscoverDeleteBlogStatueModel deleteBlogInfoById(String deleteBlogurl)
			throws IOException;

	/**
	 * 
	 * @param deleteCommenturl
	 * @return
	 * @throws IOException
	 */
	DiscoverDeleteBlogStatueModel deleteCommentByInfo(String deleteCommenturl)
			throws IOException;

	/**
	 * 点赞
	 * 
	 * @param url
	 * @param userId
	 * @param islike
	 * @param weiboid
	 * @param time
	 * @return
	 * @throws IOException
	 */
	DiscoverFlagModel praise(String url, String userId, boolean islike,
			String weiboid, String time) throws IOException;

	/**
	 * 
	 * @param url
	 * @param myPostWeiboInfo
	 * @return
	 * @throws IOException
	 */
	DiscoverBlogUpLoadResult blogUpLoadTask(String url,
			MyPostWeiboInfo myPostWeiboInfo) throws IOException;

	/**
	 * 
	 * @param ur
	 * @param postid
	 * @param file_name
	 * @return
	 * @throws IOException
	 */
	DiscoverFlagModel blogpPostPic(String ur, String postid, String file_name)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	List<WeatherInfo24> getAqiDetailWeatherInfo24Hour(String url, int i)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	List<WeatherInfo7_tian> getAqiDetailWeatherInfo7Day(String url, int i)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	List<Item> getAqiDetailItem(String url, int i) throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	EnvironmentForecastWeeklyModel getAqiDetailForecastWeekly(String url, int i)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	Kongqizhishu getKongqizhishu(String url, int i) throws IOException;

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	SearchService searchPubServiceList(String path) throws IOException;

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	EnvironmentCityWeatherModel getCityWeather(String url) throws IOException;

	/**
	 * 
	 * @param getCurrentLocationurl
	 * @param dingweicity
	 * @param currentCityLongitude
	 * @param currentCityLatitude
	 * @return
	 * @throws IOException
	 */
	NearestPm getNearestPm(String getCurrentLocationurl, String dingweicity,
			String currentCityLongitude, String currentCityLatitude)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	Sweather getNowWeather(String url, String dingweicity) throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	List<WeatherInfo7> getWeatherInfo(String url, int i) throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @return
	 * @throws IOException
	 */
	List<Life> getCityWeather(String url, int i) throws IOException;

	/**
	 * 
	 * @param url
	 * @param content
	 * @param mail
	 * @return
	 * @throws IOException
	 */
	DiscoverFlagModel feedBack(String url, String content, String mail)
			throws IOException;

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws IOException
	 */
	LoginModel login(String userName, String password) throws IOException;

	/**
	 * 
	 * @param url
	 * @param userIdQQ
	 * @param qq_name
	 * @param file_name
	 * @return
	 * @throws IOException
	 */
	UserOtherLoginModel loginOther(String url, String userIdQQ, String qq_name,
			String file_name) throws IOException;

	/**
	 * 
	 * @param url
	 * @param userId
	 * @param file_name
	 * @return
	 * @throws IOException
	 */
	UserUpLoadPicResultModel upLoadUserPic(String url, String userId,
			String file_name) throws IOException;

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	UserGetUerInfoModel getUserInfo(String url) throws IOException;

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	DiscoverFlagModel findPassword(String url) throws IOException;

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	UserRegisterModel register(String url) throws IOException;

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	ChatMsg getHistoryNews(String url) throws IOException;

	PublicService getAttention(Context context, String url, String userID)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param dingweicity
	 * @param i
	 * @return
	 * @throws IOException
	 */
	Sweather getNowWeather(String url, String dingweicity, int i)
			throws IOException;

	DiscoverFlagModel updateUserinfo(String url, String content, String userid,
			String type) throws IOException;

	/**
	 * 
	 * @param url
	 * @param time
	 * @return 城市环境详情
	 * @throws IOException
	 */
	CityDetails getWeatherRankActivity(String url, String time)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param i
	 * @param city城市排名界面
	 * @return
	 * @throws IOException
	 */
	CityRank getAqiDetailItem(String url, int i, String city)
			throws IOException;

	/**
	 * 
	 * @param url
	 * @param time环境首页界面
	 * @return
	 * @throws IOException
	 */
	CurrentWeather getCurrentWeather(String url, String time)
			throws IOException;
	
	/**
	 * 
	 * @param url
	 * @param time
	 * @return
	 * @throws IOException
	 */
	String getBlogPic(String url, int i) throws IOException;

	/**
	 * 
	 * @param url
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	ResultSelfBlogList getSelfBlogList(String url, String userId)
		throws IOException;

	/**
	 * 
	 * @param url
	 * @param time
	 * @return
	 * @throws IOException
	 */
	CurrentWeather getCurrentWeatherByDb(String url, String time)
		throws IOException;
	UserMail getMailBoundResult(String userId,String mail)
	throws IOException;
	
	/**
	 * 预警历史记录
	 * @param url
	 * @param time
	 * @return
	 */
	public AlarmHistoryModel getAlarmHistory(String url, String time);

	UserOtherLoginModel loginOther(String url, String userIdQQ, String qq_name,
			String file_name, String picurl) throws IOException;
}
