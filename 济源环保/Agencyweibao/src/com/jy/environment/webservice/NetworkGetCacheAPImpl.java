package com.jy.environment.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jy.environment.database.dal.SQLiteDALModelWebServiceRequest;
import com.jy.environment.database.model.ModelWebServiceRequest;
import com.jy.environment.exception.DataNotFoundInDBException;
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
import com.jy.environment.util.ApiClient;
import com.jy.environment.util.JsonUtils;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.okUtils;

import android.content.Context;
import android.util.Log;

public class NetworkGetCacheAPImpl implements DataSalesNetworkAPI {
	private Gson mGson;
	private SQLiteDALModelWebServiceRequest mSqLiteDALModelWebServiceRequest;

	public NetworkGetCacheAPImpl() {
		mGson = new Gson();
		mSqLiteDALModelWebServiceRequest = new SQLiteDALModelWebServiceRequest(ModelWebServiceRequest.class);
	}

	public void clearModelWebServiceRequest() {
		mSqLiteDALModelWebServiceRequest.clearTable();
	}

	/**
	 * 不需要判断两次刷新时间间隔的使用本方法
	 * 
	 * @param pURL
	 * @param pBodyParamMap
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getResultFromURL(String pURL, Map<String, Object> pBodyParamMap) throws DataNotFoundInDBException {
		HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (pBodyParamMap != null) {
			_BodyParamMap.putAll(pBodyParamMap);
		}
		String _Result = null;
		try {
			_Result = ApiClient.getDataFromServer(pURL, pBodyParamMap);
			if (null == _Result || "".equals(_Result)) {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} else {
				insertOrUpdateWebServiceResult(pURL, null, pBodyParamMap, _Result);
			}

		} catch (DataNotFoundInDBException e) {
			Log.e("NetworkGetCacheAPImpl", "DataNotFoundInDBException", e);
			return _Result;
		} catch (Exception e) {
			Log.e("NetworkGetCacheAPImpl", "IOException", e);
			try {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} catch (DataNotFoundInDBException e2) {
				_Result = null;
			}
		}
		return _Result;
	}

	/**
	 * 根据时间判定，如果时间短于intervalTime，则在数据库中读取数据
	 * 
	 * @param pURL
	 * @param pBodyParamMap
	 * @param intervalTime
	 *            以秒为单位
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getResultFromURLWithTime(String pURL, Map<String, Object> pBodyParamMap, int intervalTime)
			throws DataNotFoundInDBException {
		HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (pBodyParamMap != null) {
			_BodyParamMap.putAll(pBodyParamMap);
		}
		String _Result = null;
		try {
			try {
				_Result = getWebServiceResultFromDBWithTime(pURL, null, pBodyParamMap, intervalTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == _Result || "".equals(_Result)) {
				_Result = ApiClient.getDataFromServer(pURL, pBodyParamMap);
			} else {
				return _Result;
			}
			if (null == _Result || "".equals(_Result)) {
				_Result = getWebServiceResultFromDBWithTime(pURL, null, pBodyParamMap);
			} else {
				insertOrUpdateWebServiceResult(pURL, null, pBodyParamMap, _Result);
			}
		} catch (DataNotFoundInDBException e) {
			Log.e("NetworkGetCacheAPImpl", "DataNotFoundInDBException", e);
			return _Result;
		} catch (Exception e) {
			Log.e("NetworkGetCacheAPImpl", "IOException", e);
			try {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} catch (DataNotFoundInDBException e2) {
				_Result = null;
			}
		}
		String result = _Result;
		// result = (result.substring(0,
		// (result.length()-1))+",\"time\":\""+System.currentTimeMillis()+"\"}");
		result = (result.substring(0, (result.length() - 1)) + ",\"time\":" + System.currentTimeMillis() + "}");
		return result;
	}

	/**
	 * 根据时间判定，如果时间短于intervalTime，则在数据库中读取数据
	 * 
	 * @param pURL
	 * @param pBodyParamMap
	 * @param intervalTime
	 *            以秒为单位
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getResultFromURL(String pURL, Map<String, Object> pBodyParamMap, int intervalTime)
			throws DataNotFoundInDBException {
		HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (pBodyParamMap != null) {
			_BodyParamMap.putAll(pBodyParamMap);
		}
		String _Result = null;
		try {
			try {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap, intervalTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == _Result || "".equals(_Result)) {
				_Result = ApiClient.getDataFromServer(pURL, pBodyParamMap);
			} else {
				return _Result;
			}
			if (null == _Result || "".equals(_Result)) {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} else {
				insertOrUpdateWebServiceResult(pURL, null, pBodyParamMap, _Result);
			}
		} catch (DataNotFoundInDBException e) {
			Log.e("NetworkGetCacheAPImpl", "DataNotFoundInDBException", e);
			return _Result;
		} catch (Exception e) {
			Log.e("NetworkGetCacheAPImpl", "IOException", e);
			try {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} catch (DataNotFoundInDBException e2) {
				_Result = null;
			}
		}
		return _Result;
	}

	/**
	 * 根据时间判定，如果时间短于intervalTime，则在数据库中读取数据
	 * 
	 * @param pURL
	 * @param pBodyParamMap
	 * @param intervalTime
	 *            以秒为单位
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getResultFromURLByNewPost(String pURL, Map<String, Object> pBodyParamMap, int intervalTime)
			throws DataNotFoundInDBException {
		HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (pBodyParamMap != null) {
			_BodyParamMap.putAll(pBodyParamMap);
		}
		String _Result = null;
		try {
			try {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap, intervalTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (null == _Result || "".equals(_Result)) {
				_Result = ApiClient.getDataFromServerByNewPost(pURL, pBodyParamMap);
			} else {
				return _Result;
			}
			if (null == _Result || "".equals(_Result)) {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} else {
				insertOrUpdateWebServiceResult(pURL, null, pBodyParamMap, _Result);
			}
		} catch (DataNotFoundInDBException e) {
			Log.e("NetworkGetCacheAPImpl", "DataNotFoundInDBException", e);
		} 
		catch (Exception e) {
			Log.e("NetworkGetCacheAPImpl", "IOException", e);
			try {
				_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
			} catch (DataNotFoundInDBException e2) {
			}
		}
		return _Result;
	}

	/**
	 * 从DB中读取数据
	 * 
	 * @param pURL
	 * @param pBodyParamMap
	 * @return
	 * @throws IOException
	 */
	private String getResultFromDBCache(String pURL, Map<String, Object> pBodyParamMap) throws IOException {
		HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (pBodyParamMap != null) {
			_BodyParamMap.putAll(pBodyParamMap);
		}
		String _Result = null;
		try {
			_Result = getWebServiceResultFromDB(pURL, null, pBodyParamMap);
		} catch (DataNotFoundInDBException e2) {
			_Result = null;
		}
		return _Result;
	}

	private String getResultFromURLNoDBCache(String pURL, Map<String, Object> pBodyParamMap) throws IOException {
		HashMap<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (pBodyParamMap != null) {
			_BodyParamMap.putAll(pBodyParamMap);
		}
		try {
			return ApiClient.getDataFromServer(pURL, pBodyParamMap);

		} catch (Exception e) {
			e.printStackTrace();
			MyLog.i(e.getMessage());
		}
		return null;
	}

	private boolean insertOrUpdateWebServiceResult(String pURL, Map<String, Object> pUrlParamMap,
			Map<String, Object> pBodyParamMap, String pResult) {
		ModelWebServiceRequest _ModelWebServiceRequest = new ModelWebServiceRequest();
		_ModelWebServiceRequest.setUrlHashCode(pURL.hashCode());
		if (null == pUrlParamMap) {
			_ModelWebServiceRequest.setUrlParamHashCode(0);
		} else { 
			_ModelWebServiceRequest.setUrlParamHashCode(pUrlParamMap.hashCode());
		}
		if (null == pBodyParamMap) {
			_ModelWebServiceRequest.setBodyParamHashCode(0);
		} else {
			_ModelWebServiceRequest.setBodyParamHashCode(pBodyParamMap.hashCode());
		}
		_ModelWebServiceRequest.setResult(pResult);
		_ModelWebServiceRequest.setEditTime((int) (System.currentTimeMillis() / 1000));

		return mSqLiteDALModelWebServiceRequest.insertOrUpdateModel(_ModelWebServiceRequest);
	}

	/**
	 * @param pURL
	 * @param pUrlParamMap
	 * @param pBodyParamMap
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getWebServiceResultFromDBWithTime(String pURL, Map<String, Object> pUrlParamMap,
			Map<String, Object> pBodyParamMap) throws DataNotFoundInDBException {
		int pUrlParamMapHashCode = 0;
		int pBodyParamMapHashCode = 0;
		if (null == pUrlParamMap) {
			pUrlParamMapHashCode = 0;
		} else {
			pUrlParamMapHashCode = pUrlParamMap.hashCode();
		}
		if (null == pBodyParamMap) {
			pBodyParamMapHashCode = 0;
		} else {
			pBodyParamMapHashCode = pBodyParamMap.hashCode();
		}
		ModelWebServiceRequest _ModelWebServiceRequest = mSqLiteDALModelWebServiceRequest.select(pURL.hashCode(),
				pUrlParamMapHashCode, pBodyParamMapHashCode);

		if (_ModelWebServiceRequest != null) {
			String result = _ModelWebServiceRequest.getResult();
			// result = (result.substring(0,
			// (result.length()-1))+",\"time\":\""+_ModelWebServiceRequest.getEditTime()*1000+"\"}")
			result = (result.substring(0, (result.length() - 1)) + ",\"time\":"
					+ _ModelWebServiceRequest.getEditTime() * 1000 + "}");
			;
			return result;
		} else {
			throw new DataNotFoundInDBException();
		}
	}

	/**
	 * @param pURL
	 * @param pUrlParamMap
	 * @param pBodyParamMap
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getWebServiceResultFromDB(String pURL, Map<String, Object> pUrlParamMap,
			Map<String, Object> pBodyParamMap) throws DataNotFoundInDBException {
		int pUrlParamMapHashCode = 0;
		int pBodyParamMapHashCode = 0;
		if (null == pUrlParamMap) {
			pUrlParamMapHashCode = 0;
		} else {
			pUrlParamMapHashCode = pUrlParamMap.hashCode();
		}
		if (null == pBodyParamMap) {
			pBodyParamMapHashCode = 0;
		} else {
			pBodyParamMapHashCode = pBodyParamMap.hashCode();
		}
		ModelWebServiceRequest _ModelWebServiceRequest = mSqLiteDALModelWebServiceRequest.select(pURL.hashCode(),
				pUrlParamMapHashCode, pBodyParamMapHashCode);

		if (_ModelWebServiceRequest != null) {
			return _ModelWebServiceRequest.getResult();
		} else {
			throw new DataNotFoundInDBException();
		}
	}

	/**
	 * 根据时间长短判断是否要取出来数据
	 * 
	 * @param pURL
	 * @param pUrlParamMap
	 * @param pBodyParamMap
	 * @param intervalTime
	 *            以秒为单位
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getWebServiceResultFromDBWithTime(String pURL, Map<String, Object> pUrlParamMap,
			Map<String, Object> pBodyParamMap, int intervalTime) throws DataNotFoundInDBException {
		int pUrlParamMapHashCode = 0;
		int pBodyParamMapHashCode = 0;
		if (null == pUrlParamMap) {
			pUrlParamMapHashCode = 0;
		} else {
			pUrlParamMapHashCode = pUrlParamMap.hashCode();
		}
		if (null == pBodyParamMap) {
			pBodyParamMapHashCode = 0;
		} else {
			pBodyParamMapHashCode = pBodyParamMap.hashCode();
		}
		ModelWebServiceRequest _ModelWebServiceRequest = mSqLiteDALModelWebServiceRequest.select(pURL.hashCode(),
				pUrlParamMapHashCode, pBodyParamMapHashCode);

		if (_ModelWebServiceRequest != null) {
			if (System.currentTimeMillis() / 1000 - _ModelWebServiceRequest.getEditTime() <= (intervalTime)) {
				String result = _ModelWebServiceRequest.getResult();
				// result = (result.substring(0,
				// (result.length()-1))+",\"time\":\""+_ModelWebServiceRequest.getEditTime()*1000+"\"}");
				result = (result.substring(0, (result.length() - 1)) + ",\"time\":"
						+ _ModelWebServiceRequest.getEditTime() * 1000 + "}");
				return result;
			} else {
				return null;
			}
		} else {
			throw new DataNotFoundInDBException();
		}
	}

	/**
	 * 根据时间长短判断是否要取出来数据
	 * 
	 * @param pURL
	 * @param pUrlParamMap
	 * @param pBodyParamMap
	 * @param intervalTime
	 *            以秒为单位
	 * @return
	 * @throws DataNotFoundInDBException
	 */
	private String getWebServiceResultFromDB(String pURL, Map<String, Object> pUrlParamMap,
			Map<String, Object> pBodyParamMap, int intervalTime) throws DataNotFoundInDBException {
		int pUrlParamMapHashCode = 0;
		int pBodyParamMapHashCode = 0;
		if (null == pUrlParamMap) {
			pUrlParamMapHashCode = 0;
		} else {
			pUrlParamMapHashCode = pUrlParamMap.hashCode();
		}
		if (null == pBodyParamMap) {
			pBodyParamMapHashCode = 0;
		} else {
			pBodyParamMapHashCode = pBodyParamMap.hashCode();
		}
		ModelWebServiceRequest _ModelWebServiceRequest = mSqLiteDALModelWebServiceRequest.select(pURL.hashCode(),
				pUrlParamMapHashCode, pBodyParamMapHashCode);

		if (_ModelWebServiceRequest != null) {
			if (System.currentTimeMillis() / 1000 - _ModelWebServiceRequest.getEditTime() <= (intervalTime)) {
				return _ModelWebServiceRequest.getResult();
			} else {
				return null;
			}
		} else {
			throw new DataNotFoundInDBException();
		}
	}

	@Override
	public ResultPostBlogComment sendBlogComment(String url, String observer, String content, String time,
			String weiboid) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		_BodyParamMap.put("observer", observer);
		_BodyParamMap.put("content", content);
		_BodyParamMap.put("time", time);
		_BodyParamMap.put("weiboid", weiboid);

		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
			MyLog.i(">>>>>>>>>>>_json" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * 有缓存
		 */
		// try {
		// _Json = getResultFromURL(url, _BodyParamMap);
		// } catch (DataNotFoundInDBException e1) {
		// e1.printStackTrace();
		// _Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		// }
		MyLog.i(_Json);
		ResultPostBlogComment _Result = null;
		try {
			_Result = mGson.fromJson(_Json, ResultPostBlogComment.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public ResultBlogList getBlogList(String url, String userId) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		/**
		 * 无缓存
		 */
		// try {
		// _Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		/**
		 * 有缓存
		 */
		try {
			_Json = getResultFromURL(url, _BodyParamMap);
			// _Json = getResultFromURL(url, _BodyParamMap, intervalTime);
		} catch (DataNotFoundInDBException e1) {
			e1.printStackTrace();
			MyLog.i(e1.getMessage());
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		}
		MyLog.i(_Json);
		ResultBlogList _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, ResultPostBlogComment.class);
			_Result = JsonUtils.getBlogList(_Json, userId);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public News getNewsTaskCount(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * 有缓存
		 */
		// try {
		// // _Json = getResultFromURL(url, _BodyParamMap);
		/**
		 * 有时间限制
		 */
		// _Json = getResultFromURL(url, _BodyParamMap, intervalTime);
		// } catch (DataNotFoundInDBException e1) {
		// e1.printStackTrace();
		// MyLog.i(e1.getMessage());
		// _Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		// }
		MyLog.i(_Json);
		News _Result = null;
		try {
			_Result = mGson.fromJson(_Json, News.class);
			// _Result = JsonUtils.getBlogList(_Json);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverDeleteBlogStatueModel deleteBlogInfoById(String deleteBlogurl) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(deleteBlogurl, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * 有缓存
		 */
		// try {
		// // _Json = getResultFromURL(url, _BodyParamMap);
		/**
		 * 有时间限制
		 */
		// _Json = getResultFromURL(url, _BodyParamMap, intervalTime);
		// } catch (DataNotFoundInDBException e1) {
		// e1.printStackTrace();
		// MyLog.i(e1.getMessage());
		// _Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		// }
		MyLog.i(_Json);
		DiscoverDeleteBlogStatueModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverDeleteBlogStatueModel.class);
			// _Result = JsonUtils.getBlogList(_Json);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverDeleteBlogStatueModel deleteCommentByInfo(String deleteCommenturl) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(deleteCommenturl, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/**
		 * 有缓存
		 */
		// try {
		// // _Json = getResultFromURL(url, _BodyParamMap);
		/**
		 * 有时间限制
		 */
		// _Json = getResultFromURL(url, _BodyParamMap, intervalTime);
		// } catch (DataNotFoundInDBException e1) {
		// e1.printStackTrace();
		// MyLog.i(e1.getMessage());
		// _Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		// }
		MyLog.i(_Json);
		DiscoverDeleteBlogStatueModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverDeleteBlogStatueModel.class);
			// _Result = JsonUtils.getBlogList(_Json);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverFlagModel praise(String url, String userId, boolean islike, String weiboid, String time)
			throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("userId", userId);
		_BodyParamMap.put("islike", islike);
		_BodyParamMap.put("weiboid", weiboid);
		_BodyParamMap.put("time", time);
		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverFlagModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverBlogUpLoadResult blogUpLoadTask(String url, MyPostWeiboInfo myPostWeiboInfo) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("isshare", myPostWeiboInfo.getIsShare());
		_BodyParamMap.put("lat", myPostWeiboInfo.getLat());
		_BodyParamMap.put("lng", myPostWeiboInfo.getLng());
		_BodyParamMap.put("user_name", myPostWeiboInfo.getUser_name());
		_BodyParamMap.put("content", myPostWeiboInfo.getContent());
		_BodyParamMap.put("env", myPostWeiboInfo.getEnv());
		_BodyParamMap.put("star", myPostWeiboInfo.getStar());
		_BodyParamMap.put("province", myPostWeiboInfo.getProvince());
		_BodyParamMap.put("info_city", myPostWeiboInfo.getInfo_city());
		_BodyParamMap.put("address", myPostWeiboInfo.getAddress());
		_BodyParamMap.put("isopen", myPostWeiboInfo.getIsopen());
		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverBlogUpLoadResult _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverBlogUpLoadResult.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	// 一键曝光
	public DiscoverBlogUpLoadResult exposureUpLoadTask(String url, MyPostExposure myPostExposure) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("isshare", myPostExposure.getIsShare());
		_BodyParamMap.put("lat", myPostExposure.getLat());
		_BodyParamMap.put("lng", myPostExposure.getLng());
		_BodyParamMap.put("user_name", myPostExposure.getUser_name());
		_BodyParamMap.put("content", myPostExposure.getContent());
		_BodyParamMap.put("env", myPostExposure.getEnv());
		_BodyParamMap.put("star", myPostExposure.getStar());
		_BodyParamMap.put("province", myPostExposure.getProvince());
		_BodyParamMap.put("info_city", myPostExposure.getInfo_city());
		_BodyParamMap.put("address", myPostExposure.getAddress());
		_BodyParamMap.put("isopen", myPostExposure.getIsopen());
		_BodyParamMap.put("pollutionType", myPostExposure.getPollutionType());
		_BodyParamMap.put("isanonymous", myPostExposure.getIsanonymous());
		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverBlogUpLoadResult _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverBlogUpLoadResult.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverFlagModel blogpPostPic(String ur, String postid, String file_name) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("postid", postid);
		_BodyParamMap.put("file_name", file_name);
		MyLog.i(">>>>>>>>>>>>>>>picture" + ur + ">>>>>>>>postid" + postid + ">>>>>>>>file_name" + file_name);
		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(ur, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverFlagModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public List<WeatherInfo24> getAqiDetailWeatherInfo24Hour(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<WeatherInfo24> _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
			_Result = JsonUtils.getAqiDetailWeatherInfo24Hour(_Json);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public List<WeatherInfo7_tian> getAqiDetailWeatherInfo7Day(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<WeatherInfo7_tian> _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
			_Result = JsonUtils.getAqiDetailWeatherInfo7Day(_Json);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public List<Item> getAqiDetailItem(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<Item> _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
			_Result = JsonUtils.getAqiDetailItem(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public CityRank getAqiDetailItem(String url, int i, String city) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, i);
			_Json = getResultFromURLWithTime(url, _BodyParamMap, i);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(">>>>>>>>>>>>cityRank" + _Json);
		CityRank _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
			_Result = JsonUtils.getAqiDetailItem(_Json, city);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public EnvironmentForecastWeeklyModel getAqiDetailForecastWeekly(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		EnvironmentForecastWeeklyModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, EnvironmentForecastWeeklyModel.class);
			// _Result = JsonUtils.getAqiDetailForecastWeekly(_Json);
			MyLog.i(_Result.toString());
		} catch (Exception e) {
			_Result = null;
			MyLog.i(e.toString());
		}
		return _Result;
	}

	@Override
	public Kongqizhishu getKongqizhishu(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		Kongqizhishu _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
			_Result = JsonUtils.getKongqizhishu(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public SearchService searchPubServiceList(String path) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(path, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		SearchService _Result = null;
		try {
			_Result = mGson.fromJson(_Json, SearchService.class);
			// _Result = JsonUtils.getKongqizhishu(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public void competitive(Context context, String path) {
		// TODO Auto-generated method stub
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(path, null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i("competitive>>>>>" + _Json);
		try {
			JsonUtils.jsonService(context, _Json);
			// _Result = JsonUtils.getKongqizhishu(_Json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public EnvironmentCityWeatherModel getCityWeather(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, 3600);
			MyLog.i(">>>>>>>df" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		EnvironmentCityWeatherModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, EnvironmentCityWeatherModel.class);
			// _Result = JsonUtils.getKongqizhishu(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public NearestPm getNearestPm(String getCurrentLocationurl, String dingweicity, String currentCityLongitude,
			String currentCityLatitude) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(getCurrentLocationurl, _BodyParamMap, 3600);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		NearestPm _Result = null;
		try {
			// _Result = mGson.fromJson(_Json,
			// EnvironmentCityWeatherModel.class);
			_Result = JsonUtils.getNearestPm(_Json, dingweicity, currentCityLongitude, currentCityLatitude);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public Sweather getNowWeather(String url, String dingweicity) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		// try {
		// _Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		try {
			_Json = getResultFromURL(url, _BodyParamMap, 3600);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		Sweather _Result = null;
		try {
			_Result = JsonUtils.getNowWeather(_Json, dingweicity);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public Sweather getNowWeather(String url, String dingweicity, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		Sweather _Result = null;
		try {
			_Result = JsonUtils.getNowWeather(_Json, dingweicity);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public List<WeatherInfo7> getWeatherInfo(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, 3600);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<WeatherInfo7> _Result = null;
		try {
			_Result = JsonUtils.getWeatherInfo(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public List<Life> getCityWeather(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, 3600);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<Life> _Result = null;
		try {
			_Result = JsonUtils.getCityWeather(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverFlagModel feedBack(String url, String content, String mail) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("content", content);
		_BodyParamMap.put("mail", mail);
		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverFlagModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public LoginModel login(String userName, String password) throws IOException {
		LoginModel _Result = null;
		try {
			_Result = ApiClient.logins(userName, password);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Result.toString());
		return _Result;
	}

	@Override
	public UserOtherLoginModel loginOther(String url, String userIdQQ, String qq_name, String file_name, String picurl)
			throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		_BodyParamMap.put("openId", userIdQQ);
		_BodyParamMap.put("nickname", qq_name);
		_BodyParamMap.put("file_name", file_name);
		_BodyParamMap.put("picurl", picurl);
		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		UserOtherLoginModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, UserOtherLoginModel.class);
			MyLog.i(">>>>>>>>>>hghgqq" + _Result);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public UserUpLoadPicResultModel upLoadUserPic(String url, String userId, String file_name) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		_BodyParamMap.put("user_Id", userId);
		_BodyParamMap.put("file_name", file_name);

		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(">>>>>>>>>>>picurl" + _Json);
		UserUpLoadPicResultModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, UserUpLoadPicResultModel.class);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public UserGetUerInfoModel getUserInfo(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		UserGetUerInfoModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, UserGetUerInfoModel.class);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverFlagModel findPassword(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverFlagModel _Result = null;
		try {
			_Result = JsonUtils.findPassword(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public UserRegisterModel register(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		UserRegisterModel _Result = null;
		try {
			_Result = JsonUtils.register(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public ChatMsg getHistoryNews(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap, 14400);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		ChatMsg _Result = null;
		try {
			_Result = JsonUtils.jsonServiceHXiao(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public PublicService getAttention(Context context, String url, String userID) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;

		try {
			_Json = getResultFromURL(url, null);
		} catch (DataNotFoundInDBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MyLog.i("_json>>>>>" + _Json);
		PublicService _Result = null;
		try {
			_Result = JsonUtils.jsonService1(_Json, context, userID);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public DiscoverFlagModel updateUserinfo(String url, String userid, String content, String type) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		_BodyParamMap.put("content", content);
		_BodyParamMap.put("type", type);
		_BodyParamMap.put("userid", userid);

		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		DiscoverFlagModel _Result = null;
		try {
			_Result = mGson.fromJson(_Json, DiscoverFlagModel.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public CityDetails getWeatherRankActivity(String url, String city, String type) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (!type.equals("1")) {
//			_BodyParamMap.put("city", city);
		}
		String _Json = null;
		MyLog.i("_json>>>>>trace" + "fdfffffdh");
		try {
			MyLog.i("_json>>>>>trace" + "111");
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 0);
		} catch (Exception e1) {
			e1.printStackTrace();
			MyLog.i("_json>>>>>trace" + e1.toString());
		}

		MyLog.i("_json>>>>>tracegsggssgs" + _Json);
		CityDetails _Result = null;
		try {
			if (type.equals("1")) {
				_Result = JsonUtils.jsonWeatherRank(_Json);
			} else {
				_Result = JsonUtils.parseCity(_Json);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return _Result;
	}

	public TrendModel geTrendModel(String url, String city, String time) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		_BodyParamMap.put("city", city);
		_BodyParamMap.put("time", time);
		String _Json = null;
		MyLog.i("_jsonTrend>>>>>city" + city);
		MyLog.i("_jsonTrend>>>>>time" + time);
		String newUrl=url+time.replace("-", "");
		try {
			MyLog.i("jsonTrend>>>>>trace" + "111");
//			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 0);
			_Json=okUtils.getJsonByGet(newUrl);
		} catch (Exception e1) {
			e1.printStackTrace();
			MyLog.i("jsonTrend>>>>>trace" + e1.toString());
		}

		MyLog.i("jsonTrend>>>>>json" + _Json);
		TrendModel _Result = null;

		try {
			_Result = JsonUtils.jsonTrendModel(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public CurrentWeather getCurrentWeather(String url, String time) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;

		try {
			_Json = getResultFromURL(url, null, Integer.parseInt(time));
		} catch (DataNotFoundInDBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MyLog.i("_json>>>>>" + _Json);
		CurrentWeather _Result = null;
		try {
			_Result = JsonUtils.parseCurrentWeather(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public String getBlogPic(String url, int i) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		String _Json = null;
		try {
			_Json = getResultFromURL(url, null, i);
		} catch (DataNotFoundInDBException e1) {
			e1.printStackTrace();
		}
		MyLog.i("_json>>>>>" + _Json);
		String _Result = null;
		try {
			_Result = JsonUtils.getBlogPic(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public ResultSelfBlogList getSelfBlogList(String url, String userId) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		/**
		 * 有缓存
		 */
		try {
			_Json = getResultFromURL(url, _BodyParamMap);
		} catch (DataNotFoundInDBException e1) {
			e1.printStackTrace();
			MyLog.i(e1.getMessage());
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		}
		MyLog.i(_Json);
		ResultSelfBlogList _Result = null;
		try {
			// _Result = mGson.fromJson(_Json, ResultPostBlogComment.class);
			_Result = JsonUtils.getSelfBlogList(_Json, userId);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public CurrentWeather getCurrentWeatherByDb(String url, String time) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;

		try {
			_Json = getResultFromDBCache(url, null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		MyLog.i("_json>>>>>" + _Json);
		CurrentWeather _Result = null;
		try {
			_Result = JsonUtils.parseCurrentWeather(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public UserMail getMailBoundResult(String userId, String mail) throws IOException {
		String url = "http://192.168.15.200/epservice/v1.2/api/app/user/email/val/" + userId + mail
				+ "?token=YFJYeRKQouE0bWylekXl";
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, _BodyParamMap);
			// _Json = getResultFromURL(url, _BodyParamMap, intervalTime);
		} catch (DataNotFoundInDBException e1) {
			e1.printStackTrace();
			MyLog.i(e1.getMessage());

		}
		// UserMail _Result = JsonUtils.parseUserMail(_Json);
		return null;
	}

	public UserMail getEmail(String url) throws IOException {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		String _Json = null;

		try {
			_Json = getResultFromURL(url, null, 0);
		} catch (DataNotFoundInDBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MyLog.i("_json>>>>>" + _Json);
		UserMail _Result = null;
		try {
			_Result = JsonUtils.jsonEmail(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public uploadRecordresult postRecordData(String url, RecordData recordData) {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("user_id", recordData.getUserid());
		_BodyParamMap.put("value", recordData.getValue());
		_BodyParamMap.put("address", recordData.getAddress());
		_BodyParamMap.put("longitude", recordData.getLng());
		_BodyParamMap.put("latitude", recordData.getLat());
		_BodyParamMap.put("update_time", recordData.getUpdate_time());
		String _Json = null;
		/**
		 * 无缓存
		 */
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		uploadRecordresult _Result = null;
		try {
			_Result = mGson.fromJson(_Json, uploadRecordresult.class);
		} catch (JsonSyntaxException e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public NoiseHistoryModel getNoiseHistory(String url) {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		String _Json = null;

		try {
			_Json = getResultFromURL(url, null, 0);
		} catch (DataNotFoundInDBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MyLog.i("_json>>>>>" + _Json);
		NoiseHistoryModel _Result = null;
		try {
			_Result = JsonUtils.jsonNoise(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public AlarmHistoryModel getAlarmHistory(String url, String time) {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();

		String _Json = null;
		try {
			_Json = getResultFromURL(url, null, Integer.parseInt(time));
		} catch (DataNotFoundInDBException e1) {
			_Json = "";
			e1.printStackTrace();
		}

		MyLog.i("_json>>>>>" + _Json);
		AlarmHistoryModel _Result = null;
		try {
			_Result = JsonUtils.parseAlarmHistory(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public UserOtherLoginModel loginOther(String url, String userIdQQ, String qq_name, String file_name)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AqiModel> getMonitoringAqi(String url, int isliebiao, String dateStar, String dateEnd, String month) {
		// TODO Auto-generated method stub
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		if (isliebiao == 1) {
			_BodyParamMap.put("time", dateStar);
		} else if (isliebiao == 2) {
			_BodyParamMap.put("start", dateStar);
			_BodyParamMap.put("end", dateEnd);
		} else if (isliebiao == 5) {
			_BodyParamMap.put("month", month);
		}
		String _Json = null;
		try {
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 60);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(">>>>>>>>>>_Jsonperson" + url + ">>>>>>>dateend" + dateStar + ">>>>>>>>>>>_JSON" + _Json);
		List<AqiModel> _Result = null;
		try {
			_Result = JsonUtils.parseMonitoringAqi(_Json, isliebiao);
			MyLog.i(">>>>>>>>>>_Result" + _Result);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public List<ThreeDayForestModel> getThreeDayForestModel(String url, String city) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("city", city);
		MyLog.i("ThreeDay url====" + url);
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 240);
			MyLog.i("ThreeDay _josn====three" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
			MyLog.i("ThreeDay _josn==Exception" + e1.getMessage());
		}
		MyLog.i(_Json);
		List<ThreeDayForestModel> _Result = null;
		try {
			_Result = JsonUtils.jsonGetThreeForestDataList(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public List<ThreeDayAqiTrendModel> getThreeDayAqiTrendModel(String url, String city) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("city", city);
		MyLog.i("ThreeDay details:" + ">>>>>>>>>>1" + city + ">>>>>>>url" + url);
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 300);
			MyLog.i("ThreeDay _josn====three" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
			MyLog.i("ThreeDay _josn==Exception" + e1.getMessage());
		}
		MyLog.i(_Json);
		List<ThreeDayAqiTrendModel> _Result = null;
		try {
			_Result = JsonUtils.jsonThreeDayAqiTrendModelList(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public List<YuCeModel> getYuCeAqi(String url) {
		// TODO Auto-generated method stub
		
		MyLog.i("ThreeDay url====" + url);
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		try {
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 300);
			MyLog.i("ThreeDay _josn====three" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<YuCeModel> _Result = null;
		try {
			_Result = JsonUtils.jsonYuCe(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public void getThreeDayAqiTrendModel2(String url, String city) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("city", city);
		MyLog.i("getThreeDayAqiTrendModel2 details:" + ">>>>>>>>>>1" + city + ">>>>>>>url" + url);
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 300);
			MyLog.i("getThreeDayAqiTrendModel2 _josn====three" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
			MyLog.i("getThreeDayAqiTrendModel2 _josn==Exception" + e1.getMessage());
		}
		MyLog.i(_Json);
		// List<ThreeDayAqiTrendModel> _Result = null;
		// try {
		// _Result = JsonUtils.jsonThreeDayAqiTrendModelList(_Json);
		// } catch (Exception e) {
		// _Result = null;
		// e.printStackTrace();
		// }
		// return _Result;
	}

	public MainAqiData GetMianAqiData(String url, String city) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		 _BodyParamMap.put("city", city);
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			_Json = getResultFromURLByNewPost(url + "?city=" + city, _BodyParamMap, 30);
			MyLog.i("ThreeDay _josn====three" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
			MyLog.i("ThreeDay _josn==Exception" + e1.getMessage());
		}
		MyLog.i(_Json);
		MainAqiData _Result = null;
		try {
			_Result = JsonUtils.jsonGetMianAqiData(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	@Override
	public CityDetails getWeatherRankActivity(String url, String time) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public MonitorModel GetWeatherTask(String url, String code) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("code", code);
		String newUrl=url+"?code="+code;
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
//			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 0);
			_Json=okUtils.getJsonByGet(newUrl);
			MyLog.i("getWeatherPost>>>>>>>>>>" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		MonitorModel _Result = null;
		try {
			_Result = JsonUtils.jsonWeatherTask(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public List<AQIPoint> getUpdateAqi(String url) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 60);
			MyLog.i("getWeatherPost>>>>>>>>>>" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<AQIPoint> _Result = null;
		try {
			_Result = JsonUtils.jsonUpdateAqi(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public EnvironmentAqiModel getMonitor(String url, String city) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
//		_BodyParamMap.put("city", city);
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 0);
			MyLog.i("getWeatherPost>>>>>>>>>>" + url + ">>>" + city + ">>>" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		EnvironmentAqiModel _Result = null;
		try {
			_Result = JsonUtils.jsonMonitor(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	//TODO YYF 全国数据
	public List<AQIPoint> getShengHuiAqi(String url, String pinjieCity) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		_BodyParamMap.put("city", pinjieCity);
		try {
			// _Json = getResultFromURL(url, _BodyParamMap, 240);
			MyLog.i("getWeatherPost>>>>>>>>>>dsfgege" + url + ">>>>>city" + pinjieCity);
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 500);
			MyLog.i("getWeatherPost>>>>>>>>>>post" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		List<AQIPoint> _Result = null;
		try {
			_Result = JsonUtils.jsonShengHui(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public PmDayHourModel getMonitorDayHour(String url, boolean comFalg) {
		// TODO Auto-generated method stub
		String _Json = null;
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		try {
			_Json = getResultFromURLByNewPost(url, _BodyParamMap, 0);
			MyLog.i("getWeatherPost>>>>>>>>>>post" + url + ">>>>>>" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		PmDayHourModel _Result = null;
		try {
			_Result = JsonUtils.jsonMonitorDayHour(_Json, comFalg);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}

	public Update getupdate(String url) {
		Map<String, Object> _BodyParamMap = new HashMap<String, Object>();
		String _Json = null;
		try {
			_Json = getResultFromURLNoDBCache(url, _BodyParamMap);
			MyLog.i(">>>>>>>" + _Json);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		MyLog.i(_Json);
		Update _Result = null;
		try {
			_Result = JsonUtils.getupdate(_Json);
		} catch (Exception e) {
			_Result = null;
			e.printStackTrace();
		}
		return _Result;
	}
}