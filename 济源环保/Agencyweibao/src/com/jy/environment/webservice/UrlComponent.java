package com.jy.environment.webservice;

import java.util.List;

import com.jy.environment.util.EncodeUtil;
import com.jy.environment.util.MyLog;

import android.R.string;
import android.util.Log;

public class UrlComponent {
	
	public static String urlbase="http://125.46.1.154:9090/api/v1/PdsApp/";//济源ip
//	public static String urlbase="http://192.168.120.232:8099/api/v1/PdsApp/";//继帅测试ip
//	public static String urlbase="http://202.110.92.78:8090/App_Interface/api/v1/PdsApp/";
	// 测试地址token
	// public static String base = "http://59.108.92.201:8080/epservice";
	// public static String base = "http://1192.168.15.32:8080/epservice";
	// // public static String base = "http://192.168.15.53:8080/epservice";
	// public static String base = "http://192.168.15.46:8080/epservice";
	// public static String baseurl = base + "/v1.2/api/";
	// public static String token = "?token=pF1o6dVeXStFiP2PMUZa3";
	// public static String tokenNoMark = "&&token=pFo6dVeXStFiP2PMUZa3";
	// 正式地址token
	public static String token = "?token=YFJYeRKQouE0bWylekXl";// 正式地址token
	// public static String baseurl_choose1 =
	// "http://192.168.15.43:9090/epservice/v1.2/api/";// 正式地址
	public static String baseurl_choose1 = "http://www.shenbianer.com.cn:8080/epservice/v1.2/api/";// 正式地址
	public static String baseurl_choose2 = "http://www.micromap.com.cn:8080/epservice/v1.2/api/";// 正式地址
	// public static String baseurl_choose2 =
	// "http://192.168.15.50:8080/epservice/v1.2/api/";// 正式地址
	public static String tokenNoMark = "&&token=YFJYeRKQouE0bWylekXl";
	public static String baseurl = baseurl_choose2;
	public static String BASE_URL_TAG = "BASE_URL_TAG";
	public static List<String> baseUrlList;
	/**
	 * 查询精品推荐公众号
	 */
	public static String jingpin_path = UrlComponent.baseurl
			+ "app/publicaccount/getaccountinfo?token=pFo6dVeXStFiP2PMUZa3";
	public static String getUpdateVerson_Get = "http://www.micromap.com.cn:8080/hnAqi/version/versionCheck?version=1.0&key=8763apk0998";

	/**
	 * 上传错误信息
	 */
	public static String errormsg_Get = UrlComponent.baseurl + "app/errormsg" + UrlComponent.token;
	/**
	 * 上传错误信息
	 */
	public static String devicemsg_Get = UrlComponent.baseurl + "app/device" + UrlComponent.token;
	// /**
	// * 获取天气
	// */
	// public static String getWeather_Post =
	// "http://www.shenbianer.com.cn:8080/hbzh/servlet/Weather?city=%s";
	/**
	 * 获取天气(废弃)
	 */
	public static String getWeather_Post = "http://www.shenbianer.com.cn:8080/hbzh/servlet/Weather?city=%s";
	/**
	 * 提交微博上传图片
	 */
	public static String uploadPic_Post = baseurl + "app/weibo/postpic/upload" + token;
	/**
	 * 返回的安装包url
	 */
	public static String downApkUrl_Get = baseurl + "app/update" + UrlComponent.token;
	/**
	 * 返回的安装包url
	 */
	public static String weiboAgreeUrl_Post = UrlComponent.baseurl + "app/weibo/weiboAgree" + UrlComponent.token;
	/**
	 * 保存评论
	 */
	public static String saveCommentsUrl_Post = UrlComponent.baseurl + "app/weibo/saveComments" + UrlComponent.token;
	/**
	 * 获取用户信息
	 */
	public static String getAccountInfoUrl_Post = UrlComponent.baseurl + "app/publicaccount/getaccountinfo"
			+ UrlComponent.token;
	/**
	 * 传图片
	 */
	public static String postPicUrl_Post = UrlComponent.baseurl + "app/weibo/postpic" + UrlComponent.token;
	/**
	     * 
	     */
	public static String cityRankingUrl_Post = UrlComponent.baseurl + "p/air/cityranking/now" + UrlComponent.token;

	/**
	 * 北京天气
	 */
	public static String beijingForecastInfoUrl_Post = UrlComponent.baseurl + "p/air/forecast/getcity/101010100"
			+ UrlComponent.token;
	/**
	 * 郑州生活
	 */
	public static String zhengzhouLifeInfoUrl_Post = UrlComponent.baseurl + "app/index/" + "郑州" + UrlComponent.token;
	/**
	 * 提交微博
	 */
	public static String sharePostcontent_Post = UrlComponent.baseurl + "app/share/postcontent" + UrlComponent.token;
	/**
	 * 一键曝光
	 */
	public static String exposure_Post = UrlComponent.baseurl + "exposure/saveakeyexposure" + UrlComponent.token;
	/**
	     * 
	     */
	public static String opinion_Post = UrlComponent.baseurl + "app/opinion" + UrlComponent.token;

	/**
	 * 获取空气指数接口(日)
	 */
	public static String getAirBycity_Get(String city) {
		return baseurl + "p/air/city/" + city + "" + UrlComponent.token;
	}

	/**
	 * 获取空气指数接口（周）
	 */
	public static String getAirBycityWeek_Get(String city) {
		return baseurl + "p/air/history/" + city + "/week" + UrlComponent.token;
	}

	/**
	 * 第三方上传
	 * 
	 */
	public static String postPicUrl_Post_other = UrlComponent.baseurl + "app/user/third/login" + UrlComponent.token;

	// public static String postPicUrl_Post_other =
	// "http://59.108.37.71/epservice/v1.2/api/app/user/third/login" +
	// UrlComponent.token;

	/**
	 * 获取微博信息列表
	 */
	public static String getWeiboByInfo_Get(String province, String city, int page) {
		return UrlComponent.baseurl + "app/weibo/" + province + "/" + city + "/" + page + UrlComponent.token;
	}

	public static List<String> getBaseUrlList() {
		return baseUrlList;
	}

	public static void setBaseUrlList(List<String> baseUrlList) {
		UrlComponent.baseUrlList = baseUrlList;
	}

	public static String getBaseurl() {
		return baseurl;
	}

	public static void setBaseurl(String baseurl) {
		UrlComponent.baseurl = baseurl;
	}

	/**
	 * 获取微博信息列表
	 */
	public static String getSelfWeiboByInfo_Get(String userId, String type, int page) {
		return UrlComponent.baseurl + "app/weibo/user/" + userId + "/" + page + "/" + type + UrlComponent.token;
	}

	/**
	 * 获取消息个数
	 */
	public static String getNews_Get(String city, String time) {
		return UrlComponent.baseurl + "app/weibo/getcount/" + city + "/" + time + UrlComponent.token;
	}

	/**
	 * 删除微博
	 */
	public static String deleteWeiboByInfo_Get(String BlogId, String UserId) {
		return UrlComponent.baseurl + "app/weibo/delete/" + BlogId + "/" + UserId + UrlComponent.token;
	}

	public static String getGoggle(String url) {
		int m = url.indexOf("api/");
		return UrlComponent.getBaseurl() + url.replace(url.substring(0, m + 4), "");
	}

	/**
	 * 删除微博评论
	 */
	public static String deleteWeiboCommentByInfo_Get(String CommentId, String CommentPersonId) {
		return UrlComponent.baseurl + "app/weibo/comment/delete" + "/" + CommentId + "/" + CommentPersonId
				+ UrlComponent.token;
	}

	/**
	 * 添加城市时查询天气
	 */
	public static String getWeatherByCity_Get(String cityId) {
		return UrlComponent.baseurl + "weather/getcity/" + cityId + "" + UrlComponent.token;
	}

	public static String getGetBlogPic_Get() {
		return UrlComponent.baseurl + "app/weibo/bg/current" + UrlComponent.token;
	}

	/**
	 * 根据ID查询用户信息
	 */
	public static String getUserInfoById_Get(String id) {
		return UrlComponent.baseurl + "app/weibo/getuserinfo/" + id + UrlComponent.token;
	}

	/**
	 * 查询天气历史
	 */
	public static String getAirHistoryInfo_Get(String currentCity) {
		return UrlComponent.baseurl + "p/air/history/" + currentCity + "/one" + UrlComponent.token;
	}

	/**
	 * 查询未来的天气
	 */
	public static String getForecastInfoByCity_Get(String cityId) {
		return UrlComponent.baseurl + "p/air/forecast/getcity/" + cityId + UrlComponent.token;
	}

	/**
	 * 查询当前天气
	 */
	public static String getWeatherInfoNowByCity_Get(String cityId) {
		return UrlComponent.baseurl + "weather/now/" + cityId + "" + UrlComponent.token;
	}

	/**
	 * 查询当前天气
	 */
	public static String getLocationPmInfoByCity_Get(String lng, String lat, String city) {
		return UrlComponent.baseurl + "realtime/getnearestlocation/" + lng + "/" + lat + "/"
				+ EncodeUtil.urlEncode(city) + UrlComponent.token;
	}

	/**
	 * 查询当前天气
	 */
	public static String getKongQiZhiShuInfoByCity_Get(String cityId) {
		return UrlComponent.baseurl + "p/air/city/" + cityId + "" + UrlComponent.token;
	}

	/**
	 * 密码找回
	 */
	public static String passwordReFind_Get(String usename, String email) {
		return UrlComponent.baseurl + "user/pwdback/" + EncodeUtil.urlEncode(usename) + "/" + email
				+ UrlComponent.token;
	}

	/**
	 * 注册
	 */
	public static String register_Get(String usename, String password) {
		return UrlComponent.baseurl + "user/register/name/" + EncodeUtil.urlEncode(usename) + "/" + password
				+ UrlComponent.token;
	}

	/**
	 * 获取详细信息
	 */
	public static String getDetailBycity_Get(String city) {
		return UrlComponent.baseurl + "app/index/" + city + UrlComponent.token;
	}

	/**
	 * 获取天气详细信息
	 */
	public static String getDetailWeatherBycity_Get(String city) {
		return UrlComponent.baseurl + "app/weatherall/" + city + UrlComponent.token;
	}

	/**
	 * 
	 * @param 接收消息接口
	 * @return
	 */
	public static String getNewsPathAaccept_Get(String userId) {
		return UrlComponent.baseurl + "publicaccount/getloadmsg/all/" + userId + UrlComponent.token;
	}

	/**
	 * 取消关注服务接口
	 */
	public static String getGuanZhuPathCancel_Get(String userId, String publicID) {
		return UrlComponent.baseurl + "publicaccount/deleteAttention/" + userId + "/" + publicID + UrlComponent.token;
	}

	/**
	 * 关注服务接口
	 */
	public static String getGuanZhuPath_Get(String userId, String publicID) {
		return UrlComponent.baseurl + "publicaccount/attention/" + userId + "/" + publicID + UrlComponent.token;
	}

	/**
	 * 公众号历史消息接口
	 */
	public static String getNews_HistoPath_Get(String publicID) {
		return UrlComponent.baseurl + "app/publicaccount/historymsg/" + publicID + UrlComponent.token;
	}

	/**
	 * 用户关注的公众号
	 */
	public static String getSubscribeActivityPath(String userId) {
		return UrlComponent.baseurl + "publicaccount/getbyuser/" + userId + "/1" + UrlComponent.token;
	}

	/**
	 * 公众号与用户之间的关系
	 */
	public static String getusePathGet(String userId, String publicID) {
		return UrlComponent.baseurl + "publicaccount/backAttention/" + userId + "/" + publicID + UrlComponent.token;
	}

	/**
	 * 搜索公众号
	 */
	public static String getSearchPath_Get(String searchString) {
		return UrlComponent.baseurl + "publicaccount/getallsearch/" + searchString + "/1" + UrlComponent.token;
	}

	/**
	 * 获取附近
	 */
	public static String searchComByLocURL = UrlComponent.baseurl + "app/area/location/";

	/**
	 * 消息网址链接
	 */
	public static String getNewsDetail_Get(String id) {
		// if (baseurl.contains("shenbianer")) {
		// return
		// "http://www.shenbianer.com.cn:8080/epservicemgr/weibaopublicPlatform/msgdetail.jsp?id="
		// + id;
		// }
		return "http://www.micromap.com.cn:8080/weibaomp/jsp/publicMsgDetail.jsp?id=" + id;
	}

	/*
	 * public static String getNewsDetail_Get(String id) { return
	 * "http://59.108.92.201:8080/epservicemgr/weibaopublicPlatform/msgdetail.jsp?id="
	 * + id; }
	 */

	/**
	 * 上传消息接口
	 */
	public static String getNewsChat_Post() {
		return UrlComponent.baseurl + "app/user/newpublicmsg" + UrlComponent.token;

	}

	/**
	 * 修改用户信息
	 */
	public static String updateUserInfo() {
		return UrlComponent.baseurl + "app/userinfo/update" + UrlComponent.token;
	}

	/**
	 * 获取用户等级
	 */
	public static String gradeGet(String userId) {
		return UrlComponent.baseurl + "getlevel/" + userId + UrlComponent.token;
	}

	/**
	 * 环境首页
	 */
	public static String currentWeatherGet(String city, String lat, String lng) {
		return UrlComponent.baseurl + "weather/getAll/" + city + "/" + lng + "/" + lat + UrlComponent.token;
	}

	/**
	 * 环境城市详情
	 */
	public static String currentWeatherDetailsGet(String city, String lat, String lng) {
		return UrlComponent.baseurl + "p/air/getAll/" + city + "/" + lng + "/" + lat + UrlComponent.token;
	}

	public static String SmsGet(String user, String pwd, String mobile, String msg) {
		return "http://dx.it1199.com:83/ApiService.asmx/Send?" + "account=" + user + "&" + "pwd=" + pwd + "&"
				+ "product=9" + "&mobile=" + mobile + "&message=" + EncodeUtil.urlEncode(msg, "utf-8");
	}

	/*
	 * 邮箱验证接口
	 */
	public static String getEmail_Get(String userId, String mail) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/user/email/val/" + userId + "/" + mail + "/0" + UrlComponent.token;

	}

	// 签到接口
	public static String getRegister(String userId) {
		return UrlComponent.baseurl + "check/checksave/" + userId + UrlComponent.token;
	}

	// 查看是否签到0代表没有签到 1代表签到过

	public static String getIsRegister(String userId) {
		return UrlComponent.baseurl + "check/ischeckorlottery/" + userId + UrlComponent.token;
	}

	// 抽奖接口
	public static String getlottery(String userId) {
		return UrlComponent.baseurl + "lottery/lotterysave/" + userId + UrlComponent.token;
	}

	// 百科接口
	public static String getBaike(String city) {
		return UrlComponent.baseurl + "cyclopaedia/findcyclopaedia/" + city + UrlComponent.token;
	}

	/**
	 * 解除邮箱绑定接口
	 */
	public static String getEmail_UnBind(String userId, String mail) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/user/email/val/" + userId + "/" + mail + "/1" + UrlComponent.token;

	}

	/**
	 * 手机验证接口
	 */
	public static String getPhone_Get(String userId, String phone) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/user/phone/val/" + userId + "/" + phone + "/0" + UrlComponent.token;

	}

	/**
	 * 手机解绑验证接口
	 */
	public static String getUnbindPhone_Get(String userId, String phone) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/user/phone/val/" + userId + "/" + phone + "/1" + UrlComponent.token;

	}

	/**
	 * 手机绑定
	 */
	public static String getBound_Get(String userId, String code) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/user/phone/bind/" + userId + "/" + code + "/1" + UrlComponent.token;

	}

	/**
	 * 手机解绑
	 */
	public static String getUnbind_Get(String userId, String code) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/user/phone/bind/" + userId + "/" + code + "/0" + UrlComponent.token;

	}

	/**
	 * 获取用户信息接口
	 */
	public static String getUserInfo_Get(String userId) {
		/*
		 * return "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/"
		 * + userId+"/" + mail + UrlComponent.token;
		 */
		return UrlComponent.baseurl + "app/weibo/getuserinfo/" + userId + UrlComponent.token;
		// http://59.108.37.71:8080/epservice/v1.2/api/app/weibo/getuserinfo/9434?token=YFJYeRKQouE0bWylekXl
	}

	/**
	 * /** 上传噪声数据
	 */
	public static String uploadRecordData() {
		return UrlComponent.baseurl + "p/noise/save/one" + UrlComponent.token;
	}

	/**
	 * 根据用户id返回分页噪声历史记录
	 */
	public static String getNoiseHistory(int userid, int page) {
		return UrlComponent.baseurl + "p/noises/oneperson/paged/" + userid + "/" + page + UrlComponent.token;
	}

	// 消息接收端口
	public static String news_path = UrlComponent.baseurl + "publicaccount/getloadmsg/";

	// // 上传消息接口
	// public static String news_chat =
	// "http://192.168.15.34:8080/epservice/v1.2/api/app/user/newpublicmsg"
	// + UrlComponent.token;

	// public static String news_path_accept = UrlComponent.baseurl
	// + "api/publicaccount/getloadmsg/all/7" + UrlComponent.tokenNoMark;
	// public static String news_path_accept =
	// "http://59.108.37.71:8080/epservice/v1.2/api/publicaccount/getloadmsg/all/7?token=pFo6dVeXStFiP2PMUZa3";

	/**
	 * 根据用户id返回分页噪声历史记录
	 */
	public static String getAlarmHistory(String province, String city) {
		// String url = "http://www.shenbianer.com.cn:8080/epservice"
		// + "/api/app/push/history";
		String url = UrlComponent.baseurl + "app/push/history";
		if (null != province && !province.equals("")) {
			url += "/all";
		} else {
			url += "/" + province;
		}
		url += "/" + city + UrlComponent.token;
		MyLog.i(">>>>>>city" + url);
		return url;
	}

	// 分享邀请手机 联系人接口
	public static String getShare(String userId, String level) {
		return UrlComponent.baseurl + "level/addexpandcoin/" + userId + "/" + level + UrlComponent.token;
	}

	public static String getYuCe(String cityName) {
		return "http://www.micromap.com.cn:8080/hnAqi/v1.0/api/air/forecast?city=" + cityName;
	}

	// 登陆微保的账号
	public static String getShareLogin(String userName, String userPwd) {
		/*
		 * return "http://www.iweibao.com.cn/mobile/login.jsp?ref=2&username=" +
		 * userName + "&password=" + userPwd;
		 */
		return "http://www.iweibao.com.cn/mobile/login.jsp?username=" + userName + "&password=" + userPwd + "&ref=2";
	}

	// 登陆微保商城首页
	public static String getIsBindedMailLogin(String userName, String userPwd) {
		return "http://www.iweibao.com.cn?username=" + userName + "&password=" + userPwd + "&ref=2";
		// http://www.iweibao.com.cn/mobile/login.jsp?username=wanwan3&password=1bbd886460827015e5d605ed44252251&ref=2
	}

	// 地图界面的接口

	// aqi数据服务地址----shang
	// 新版调用城市aqi数据服务地址 注：一次可调用多个城市
	public static String AQIqueryURL_V2 = UrlComponent.baseurl + "p/air/multi/cities/aqi/";

	public static String AQIqueryURL_V2_POST = UrlComponent.baseurl + "p/air/multi/cities/aqi/post";
	// public static String AQIqueryURL_V2_POST =
	// "http://192.168.0.139:8080/epservice/v1.2/api/"
	// + "p/air/multi/cities/aqi/post";
	public static String AQIqueryURL_V2_POST_NEW = UrlComponent.baseurl
			+ "http://www.micromap.com.cn:8080/hnAqi/v1.0/api/air/getCityAndStationAqi";

	// 调用天气的服务地址----shang
	
	
	
	
	public static String weatherURL = UrlComponent.baseurl + "weather/weathernow/";
	// 新版调用城市天气的服务接口 注：一次可调用多个天气 替代wertherURL
	public static String weatherURL_V2 = UrlComponent.baseurl + "weather/multi/cities/";

	public static String surfaceWaterURL_V2 = UrlComponent.baseurl + "p/air/multi/cities/surface";
	public static String pullotion_URL_V2 = UrlComponent.baseurl + "p/air/multi/cities/minitor";
	// 当前城市天气，地图弹窗中调用-----shang
	public static String weathernowURL = UrlComponent.baseurl + "weather/now/";
	// 当前点城市质量历史记录
	public static String aqihistoryURL = UrlComponent.baseurl + "p/air/history/";
	/**
	 * 参数 maxB/minB/maxL/minL/count
	 */
	public static String getMapPicByBLURL = UrlComponent.baseurl + "app/share/showAllLocation/";
	// 累积浓度值old
	public static String getNongDuValue_old = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/cityreport";
	// 累积浓度值
	public static String getNongDuValue = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/airreport";
	// 三天预测
	public static String getThreeDayForest = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/forecast";

	public static String getThreeDayForest2 = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/airDistribution";
	// http://www.micromap.com.cn:8080/hnAqi/v1.0/api/air/forecast
	// 实时
	public static String getLeiJiNongDuValue = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/realtimeAqi";
	public static String getRealTimeValue = urlbase+"getRealTimeHour";
	// 首页预测数据和aqi值
	public static String getThreeDayAqiTrend = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getHomePageAll";
	// 日报
	public static String getRiBaoValue = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/dayAqi";
	public static String getDateTimeValue=urlbase+"getRealTimeDay?datetime=";

	// public static String getRiBaoValue =
	// "http://192.168.15.107:8080/hnAqi/v1.0/api/air/dayAqi";
	// 预报报表
	public static String getYuBaoURL = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getForecastReport";
	// 获取未来三天空气质量趋势
	public static String getWeiLaiURL = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/airDistribution";
	// 日报
	public static String getMapHis = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getCityHistory";
	// 空气质量详情
//	public static String getMapHiss = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getAll";
	public static String getMapHiss = urlbase+"getAll";
	// 监测站24小时
//	public static String getMonitor = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/city/citytime";
	public static String getMonitor = urlbase+"getStationIndex";
	// 河南所有监测点数据
	public static String monitorData = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getCityAndStationAqi";
	// 获取各个市的监测站24小时aqi值
//	public static String monitor = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getStationMonthAQI";
	public static String monitor = urlbase+"getStationMonthAQI";
	// 获取各个市的监测站pm25,pm10的24小时与30天值
//	public static String monitor_hour_day = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getPollutantTrend";
	public static String monitor_hour_day = urlbase+"getPollutantTrend";
	// 获取单个监测站的pm25,pm10的24小时与30天值
//	public static String station_hour_day = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getStationPollutantTrend";
	public static String station_hour_day = urlbase+"getStationPollutantTrend";
//	public static String get24hTrendURL = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getStationDayPollutant";
	public static String get24hTrendURL = urlbase+"getStationDayPollutant";
//	public static String getTrendURL = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getDetailsByTime";
	public static String getTrendURL = urlbase+"getDetailsByTime/";
	
	//TODO YYF
	public static String getHeNanCityValueUrl = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/realtimeAqi";
	public static String getHeNanPointValueUrl = "http://1.192.88.18:8115/hnAqi/v1.0/api/air/getCityStationDetail";
	public static String getAllCityValueUrl = "";
	//获取微站数据列表
	public static String getAppMicroStation=urlbase+"getAppMicroStation";
	public static String getMonthDataUrl=urlbase+"getMonthlyCumulative?datetime=";
	public static String getYearTimeData=urlbase+"getYearCumulative?";
	//获取同环比数据
	public static String getTongHuanBi=urlbase+"getSimultaneousCuts?timetype=";
	public static String getDataOfMap=urlbase+"getAppMapStationList";
}
