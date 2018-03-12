package com.jy.environment.business;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.AlarmHistoryModel;
import com.jy.environment.model.AqiModel;
import com.jy.environment.model.ChatMsg;
import com.jy.environment.model.CityDetails;
import com.jy.environment.model.CityRank;
import com.jy.environment.model.CurrentWeather;
import com.jy.environment.model.DiscoverBlogUpLoadResult;
import com.jy.environment.model.DiscoverDeleteBlogStatueModel;
import com.jy.environment.model.DiscoverFlagModel;
import com.jy.environment.model.EnvironmentAqiModel;
import com.jy.environment.model.EnvironmentCityWeatherModel;
import com.jy.environment.model.EnvironmentForecastWeeklyModel;
import com.jy.environment.model.Item;
import com.jy.environment.model.Kongqizhishu;
import com.jy.environment.model.Life;
import com.jy.environment.model.LoginModel;
import com.jy.environment.model.MainAqiData;
import com.jy.environment.model.MonitorModel;
import com.jy.environment.model.MyPostExposure;
import com.jy.environment.model.MyPostWeiboInfo;
import com.jy.environment.model.NearestPm;
import com.jy.environment.model.News;
import com.jy.environment.model.NoiseHistoryModel;
import com.jy.environment.model.PmDayHourModel;
import com.jy.environment.model.PublicService;
import com.jy.environment.model.RecordData;
import com.jy.environment.model.ResultBlogList;
import com.jy.environment.model.ResultPostBlogComment;
import com.jy.environment.model.ResultSelfBlogList;
import com.jy.environment.model.SearchService;
import com.jy.environment.model.Sweather;
import com.jy.environment.model.ThreeDayAqiTrendModel;
import com.jy.environment.model.ThreeDayForestModel;
import com.jy.environment.model.TrendModel;
import com.jy.environment.model.Update;
import com.jy.environment.model.UserGetUerInfoModel;
import com.jy.environment.model.UserMail;
import com.jy.environment.model.UserOtherLoginModel;
import com.jy.environment.model.UserRegisterModel;
import com.jy.environment.model.UserUpLoadPicResultModel;
import com.jy.environment.model.WeatherInfo24;
import com.jy.environment.model.WeatherInfo7;
import com.jy.environment.model.WeatherInfo7_tian;
import com.jy.environment.model.YuCeModel;
import com.jy.environment.model.uploadRecordresult;
import com.jy.environment.util.MyLog;

import android.content.Context;

public class BusinessSearch extends BusinessBase {

	public News getNewsTaskCount(String url) throws IOException {
		News _Result;
		try {
			_Result = mApImpl.getNewsTaskCount(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public ResultPostBlogComment sendBlogComment(String url, String observer, String content, String time,
			String weiboid) throws IOException {
		ResultPostBlogComment _Result;
		try {
			_Result = mApImpl.sendBlogComment(url, observer, content, time, weiboid);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverFlagModel updateUserInfo(String url, String userid, String content, String type) throws IOException {
		DiscoverFlagModel _Result;
		try {
			_Result = mApImpl.updateUserinfo(url, userid, content, type);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	// public ResultBlogList getBlogList(String url, int intervalTime)
	// throws IOException {
	// ResultBlogList _Result;
	// try {
	// _Result = mApImpl.getBlogList(url, intervalTime);
	// } catch (JsonSyntaxException e) {
	// throw new IOException(e.getMessage());
	// }
	// return _Result;
	// }
	public ResultBlogList getBlogList(String url, String userId) throws IOException {
		ResultBlogList _Result;
		try {
			_Result = mApImpl.getBlogList(url, userId);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public ResultSelfBlogList getSelfBlogList(String url, String userId) throws IOException {
		ResultSelfBlogList _Result;
		try {
			_Result = mApImpl.getSelfBlogList(url, userId);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverDeleteBlogStatueModel deleteBlogInfoById(String deleteBlogurl) throws IOException {
		DiscoverDeleteBlogStatueModel _Result;
		try {
			_Result = mApImpl.deleteBlogInfoById(deleteBlogurl);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverDeleteBlogStatueModel deleteCommentByInfo(String deleteCommenturl) throws IOException {
		DiscoverDeleteBlogStatueModel _Result;
		try {
			_Result = mApImpl.deleteCommentByInfo(deleteCommenturl);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverFlagModel praise(String url, String userId, boolean islike, String weiboid, String time)
			throws IOException {
		DiscoverFlagModel _Result;
		try {
			_Result = mApImpl.praise(url, userId, islike, weiboid, time);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverBlogUpLoadResult blogUpLoadTask(String url, MyPostWeiboInfo myPostWeiboInfo) throws IOException {
		DiscoverBlogUpLoadResult _Result;
		try {
			_Result = mApImpl.blogUpLoadTask(url, myPostWeiboInfo);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverBlogUpLoadResult exposureUpLoadTask(String url, MyPostExposure myPostExposure) throws IOException {
		DiscoverBlogUpLoadResult _Result;
		try {
			_Result = mApImpl.exposureUpLoadTask(url, myPostExposure);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverFlagModel blogpPostPic(String ur, String postid, String file_name) throws IOException {
		DiscoverFlagModel _Result;
		try {
			_Result = mApImpl.blogpPostPic(ur, postid, file_name);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public List<WeatherInfo24> getAqiDetailWeatherInfo24Hour(String url, int i) throws IOException {
		List<WeatherInfo24> _Result;
		try {
			_Result = mApImpl.getAqiDetailWeatherInfo24Hour(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public List<WeatherInfo7_tian> getAqiDetailWeatherInfo7Day(String url, int i) throws IOException {
		List<WeatherInfo7_tian> _Result;
		try {
			_Result = mApImpl.getAqiDetailWeatherInfo7Day(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public List<Item> getAqiDetailItem(String url, int i) throws IOException {
		List<Item> _Result;
		try {
			_Result = mApImpl.getAqiDetailItem(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public CityRank getAqiDetailItem(String url, int i, String city) throws IOException {
		CityRank _Result;
		try {
			_Result = mApImpl.getAqiDetailItem(url, i, city);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public EnvironmentForecastWeeklyModel getAqiDetailForecastWeekly(String url, int i) throws IOException {
		EnvironmentForecastWeeklyModel _Result;
		try {
			_Result = mApImpl.getAqiDetailForecastWeekly(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public Kongqizhishu getKongqizhishu(String url, int i) throws IOException {
		Kongqizhishu _Result;
		try {
			_Result = mApImpl.getKongqizhishu(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public SearchService searchPubServiceList(String path) throws IOException {
		SearchService _Result;
		try {
			_Result = mApImpl.searchPubServiceList(path);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	// 精品推荐
	public void competitive(Context context, String path) throws IOException {
		try {
			mApImpl.competitive(context, path);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
	}

	public EnvironmentCityWeatherModel getCityWeather(String url) throws IOException {
		EnvironmentCityWeatherModel _Result;
		try {
			_Result = mApImpl.getCityWeather(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public NearestPm getNearestPm(String getCurrentLocationurl, String dingweicity, String currentCityLongitude,
			String currentCityLatitude) throws IOException {
		NearestPm _Result;
		try {
			_Result = mApImpl.getNearestPm(getCurrentLocationurl, dingweicity, currentCityLongitude,
					currentCityLatitude);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public Sweather getNowWeather(String url, String dingweicity) throws IOException {
		Sweather _Result;
		try {
			_Result = mApImpl.getNowWeather(url, dingweicity);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public Sweather getNowWeather(String url, String dingweicity, int i) throws IOException {
		Sweather _Result;
		try {
			_Result = mApImpl.getNowWeather(url, dingweicity, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public List<WeatherInfo7> getWeatherInfo(String url, int i) throws IOException {
		List<WeatherInfo7> _Result;
		try {
			_Result = mApImpl.getWeatherInfo(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public List<Life> getCityWeather(String url, int i) throws IOException {
		List<Life> _Result;
		try {
			_Result = mApImpl.getCityWeather(url, i);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverFlagModel feedBack(String url, String content, String mail) throws IOException {
		DiscoverFlagModel _Result;
		try {
			_Result = mApImpl.feedBack(url, content, mail);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public LoginModel login(String userName, String password) throws IOException {
		LoginModel _Result;
		try {
			_Result = mApImpl.login(userName, password);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public UserOtherLoginModel loginOther(String url, String userIdQQ, String qq_name, String file_name, String picurl)
			throws IOException {
		UserOtherLoginModel _Result;
		try {
			_Result = mApImpl.loginOther(url, userIdQQ, qq_name, file_name, picurl);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public UserUpLoadPicResultModel upLoadUserPic(String url, String userId, String file_name) throws IOException {
		UserUpLoadPicResultModel _Result;
		try {
			_Result = mApImpl.upLoadUserPic(url, userId, file_name);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public UserGetUerInfoModel getUserInfo(String url) throws IOException {
		UserGetUerInfoModel _Result;
		try {
			_Result = mApImpl.getUserInfo(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public DiscoverFlagModel findPassword(String url) throws IOException {
		DiscoverFlagModel _Result;
		try {
			_Result = mApImpl.findPassword(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public UserRegisterModel register(String url) throws IOException {
		UserRegisterModel _Result;
		try {
			_Result = mApImpl.register(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public ChatMsg getHistoryNews(String url) throws IOException {
		ChatMsg _Result;
		try {
			_Result = mApImpl.getHistoryNews(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public PublicService attention(Context context, String url, String userID) throws IOException {
		PublicService _Result;
		try {
			_Result = mApImpl.getAttention(context, url, userID);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public CityDetails getWeatherRankActivity(String url, String city, String type) throws IOException {
		CityDetails _Result;
		try {
			_Result = mApImpl.getWeatherRankActivity(url, city, type);
			MyLog.i("url===<<<<" + url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public TrendModel getTrendModel(String url, String city, String time) throws IOException {
		TrendModel _Result;
		try {
			_Result = mApImpl.geTrendModel(url, city, time);
			MyLog.i("urlTrend===<<<<" + url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public CurrentWeather getCurrentWeather(String url, String time) throws IOException {
		CurrentWeather _Result;
		try {
			_Result = mApImpl.getCurrentWeather(url, time);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public String getBlogPic(String url) throws IOException {
		String _Result;
		try {
			_Result = mApImpl.getBlogPic(url, 3600);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public CurrentWeather getCurrentWeatherByDb(String url, String time) throws IOException {
		CurrentWeather _Result;
		try {
			_Result = mApImpl.getCurrentWeatherByDb(url, time);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public uploadRecordresult uploadrecordpost(String url, RecordData recordData) throws IOException {
		uploadRecordresult _Result;
		try {
			_Result = mApImpl.postRecordData(url, recordData);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public UserMail getEmail(String url) throws IOException {
		UserMail _Result;
		try {
			_Result = mApImpl.getEmail(url);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public NoiseHistoryModel getNoiseHistory(String url) {
		NoiseHistoryModel _Result;
		_Result = mApImpl.getNoiseHistory(url);
		return _Result;
	}

	public AlarmHistoryModel getAlarmHistory(String url, String time) throws IOException {
		AlarmHistoryModel _Result;
		try {
			_Result = mApImpl.getAlarmHistory(url, time);
		} catch (JsonSyntaxException e) {
			throw new IOException(e.getMessage());
		}
		return _Result;
	}

	public List<AqiModel> getMonitoringAqi(String url, int isliebiao, String dateStar, String dateEnd, String month) {

		// TODO Auto-generated method stub
		List<AqiModel> _Result = null;
		try {
			_Result = mApImpl.getMonitoringAqi(url, isliebiao, dateStar, dateEnd, month);
		} catch (Exception e) {
		}
		return _Result;
	}

	public List<ThreeDayForestModel> getThreeDayForestModel(String url, String city) {
		// TODO Auto-generated method stub
		List<ThreeDayForestModel> _Result = null;
		try {
			_Result = mApImpl.getThreeDayForestModel(url, city);
		} catch (Exception e) {
		}
		return _Result;
	}

	public List<ThreeDayAqiTrendModel> getThreeDayAqiTrendModels(String url, String city) {
		// TODO Auto-generated method stub
		List<ThreeDayAqiTrendModel> _Result = null;
		try {
			_Result = mApImpl.getThreeDayAqiTrendModel(url, city);
		} catch (Exception e) {
		}
		return _Result;
	}

	public List<YuCeModel> getYuCeAqi(String url) {
		// TODO Auto-generated method stub
		List<YuCeModel> _Result = null;
		try {
			_Result = mApImpl.getYuCeAqi(url);
		} catch (Exception e) {
		}
		return _Result;
	}

	public void getThreeDayAqiTrendModels2(String url, String city) {
		// TODO Auto-generated method stub
		try {
			mApImpl.getThreeDayAqiTrendModel2(url, city);
		} catch (Exception e) {
		}
	}

	public MainAqiData GetMianAqiData(String url, String city) {
		// TODO Auto-generated method stub
		MainAqiData _Result = null;
		try {
			_Result = mApImpl.GetMianAqiData(url, city);
		} catch (Exception e) {
		}
		return _Result;
	}

	public MonitorModel getWeatherTask(String url, String code) {
		// TODO Auto-generated method stub
		MonitorModel _Result = null;
		try {
			_Result = mApImpl.GetWeatherTask(url, code);
		} catch (Exception e) {
		}
		return _Result;
	}

	public List<AQIPoint> getUpdateAqi(String url) {
		// TODO Auto-generated method stub
		List<AQIPoint> _Result = null;
		try {
			_Result = mApImpl.getUpdateAqi(url);
		} catch (Exception e) {
		}
		return _Result;
	}

	public EnvironmentAqiModel getMonitor(String url, String city) {
		// TODO Auto-generated method stub
		EnvironmentAqiModel _Result = null;
		try {
			_Result = mApImpl.getMonitor(url, city);
		} catch (Exception e) {
		}
		return _Result;
	}

	public List<AQIPoint> getShengHuiAqi(String url, String pinjieCity) {
		// TODO Auto-generated method stub
		List<AQIPoint> _Result = null;
		try {
			_Result = mApImpl.getShengHuiAqi(url, pinjieCity);
		} catch (Exception e) {
		}
		return _Result;
	}

	public PmDayHourModel getMonitorDayHour(String url, boolean comFlag) {
		// TODO Auto-generated method stub
		PmDayHourModel _Result = null;
		try {
			_Result = mApImpl.getMonitorDayHour(url, comFlag);
		} catch (Exception e) {
		}
		return _Result;
	}

	public Update getupdate(String url) {
		Update loginModel = new Update();
		try {
			loginModel = mApImpl.getupdate(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginModel;
	}
}
