package com.jy.environment.database.dal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.AQIPoint;
import com.jy.environment.model.City;
import com.jy.environment.model.LaLngData;
import com.jy.environment.model.ManageCity;
import com.jy.environment.model.NearestPm;
import com.jy.environment.model.PollutionPointModel;
import com.jy.environment.model.Province;
import com.jy.environment.model.SurfaceWaterModel;
import com.jy.environment.model.Sweather;
import com.jy.environment.util.MyLog;
import com.jy.environment.util.WbMapUtil;

//import com.baidu.platform.comapi.basestruct.GeoPoint;

public class CityDB {
	public static final String CITY_DB_NAME = "city.db";
	private static final String CITY_TABLE_NAME = "city";
	private static final String ADDCITY_TABLE_NAME = "addcity";
	private static final String LOCATION_TABLE_NAME = "location";
	private static final String CURRENTCITYPM_TABLE_NAME = "currentcitypm";
	private SQLiteDatabase db;

	public CityDB(Context context, String path) {
		db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}

	public boolean isOpen() {
		return db != null && db.isOpen();
	}

	public void close() {
		if (db != null && db.isOpen())
			db.close();
	}

	public String getSuoSuo(String city) {
		String sql = "select suoshu from city where name=?";
		Cursor c = db.rawQuery(sql, new String[] { city });
		if (c.moveToNext()) {
			String t = c.getString(c.getColumnIndex("suoshu"));
			if (t != null)

				return t;

		}
		return null;

	}

	public int queryMaxValue(String column, String table) {

		int count = -1;
		Cursor c = null;
		String mysql = "select max(" + column + ") from " + table;

		try {
			c = db.rawQuery(mysql, null);

			if (c.moveToNext()) {
				count = c.getInt(0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return count;

	}

	public ArrayList<HashMap<String, Object>> queryBySqlReturnArrayListHashMap(
			String sql) {
		Cursor data_cursor = queryBySql(sql);
		if (data_cursor != null) {
			ArrayList<HashMap<String, Object>> initcitys = convertCursorToArrayListHashMap(data_cursor);
			if (initcitys.size() == 0) {
				HashMap<String, Object> cityhaHashMap = new HashMap<String, Object>();
				cityhaHashMap.put("name", "平顶山");
				cityhaHashMap.put("province", "河南");
				cityhaHashMap.put("number", "101181101");
				cityhaHashMap.put("pinyin", "pingdingshan");
				cityhaHashMap.put("py", "pds");
				cityhaHashMap.put("lat", "33.75");
				cityhaHashMap.put("lon", "113.29");
				cityhaHashMap.put("islocation", "1");
				cityhaHashMap.put("temp", "");
				cityhaHashMap.put("climate", "");
				City city = new City();
				city.setName("平顶山");
				city.setPinyin("pingdingshan");
				city.setProvince("河南");
				city.setNumber("101180101");
				city.setPy("pds");
				addXuanZhecity(city);
				initcitys.add(cityhaHashMap);
			}
			return initcitys;
		} else {
			HashMap<String, Object> cityhaHashMap = new HashMap<String, Object>();
			cityhaHashMap.put("name", "平顶山");
			cityhaHashMap.put("province", "河南");
			cityhaHashMap.put("number", "101180101");
			cityhaHashMap.put("pinyin", "pingdingshan");
			cityhaHashMap.put("py", "pds");
			cityhaHashMap.put("lat", "33.75");
			cityhaHashMap.put("lon", "113.29");
			cityhaHashMap.put("islocation", "1");
			ArrayList<HashMap<String, Object>> initcitys = new ArrayList<HashMap<String, Object>>();
			initcitys.add(cityhaHashMap);
			cityhaHashMap.put("climate", "");
			City city = new City();
			city.setName("平顶山");
			city.setPinyin("pingdingshan");
			city.setProvince("河南");
			city.setNumber("101180101");
			city.setPy("pds");
			addXuanZhecity(city);
			return initcitys;
		}

	}

	public String getprovicecity(String city) {
		String sql = "select province from city where name=?";
		Cursor c = db.rawQuery(sql, new String[] { city });
		if (c.moveToNext()) {
			String t = c.getString(c.getColumnIndex("province"));
			if (t.contains("北京") || t.contains("上海") || t.contains("重庆")
					|| t.contains("天津")) {
				t = t + "市";

			} else {
				t = t + "省";
			}

			return t;

		}
		return "";

	}

	public Cursor queryBySql(String sql) {

		Cursor c = null;

		try {

			c = db.rawQuery(sql, null);
		} catch (Exception e) {

		}

		return c;
	}

	public void addXuanZhecity(City city) {
		ContentValues contentValues = new ContentValues();
		// int val = queryMaxValue("id", "addcity");
		// if (TextUtils.isEmpty(val + "")) {
		// contentValues.put("id", 1);
		// } else {
		// contentValues.put("id", 1 + val);
		// }

		contentValues.put("name", city.getName());
		contentValues.put("province", city.getProvince());
		contentValues.put("number", city.getNumber());
		contentValues.put("pinyin", city.getPinyin());
		contentValues.put("py", city.getPy());
		long m = db.insert("addcity", null, contentValues);
		MyLog.i(">>>>>>>>>>>>>mmmm" + m);
	}

	public boolean isHaveNearestPm(String name, String lon, String lat, long hao) {
		String sql = "select city,datime from currentcitypm where city=? and longitude=? and latitude=? ";
		if (null == name || null == lon || null == lat) {
			return false;
		}
		Cursor c = db.rawQuery(sql, new String[] { name, lon, lat });
		if (c.moveToNext()) {
			String t = c.getString(c.getColumnIndex("datime"));
			MyLog.i("Neares new Date().getTime() :" + new Date().getTime());
			MyLog.i("Neares Long.parseLong(t) :" + Long.parseLong(t));
			MyLog.i(" Neares isHaveNearestPm :"
					+ (new Date().getTime() - Long.parseLong(t)));
			if (new Date().getTime() - Long.parseLong(t) > hao) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}

	}

	public boolean ci(String name, long hao) {
		String sql = "select city,datime from sytq where city=?";
		Cursor c = db.rawQuery(sql, new String[] { name });
		if (c.moveToNext()) {
			String t = c.getString(c.getColumnIndex("datime"));
			if (new Date().getTime() - Long.parseLong(t) > hao) {
				// String sql2 = "delete * from sytq where city='" + name + "'";
				// db.delete("sytq", "city=?", new String[] { name });

				return false;

			} else {
				return true;
			}
		} else {
			return false;
		}

	}

	public boolean isExitCurrentPm(String name) {
		String sql = "select city,datime from currentcitypm where city=?";
		Cursor c = db.rawQuery(sql, new String[] { name });
		if (c.moveToNext()) {
			return true;
		} else {
			return false;
		}

	}

	// public boolean isExitCurrentPm(String name) {
	// String sql = "select city,datime from currentcitypm where city=?";
	// Cursor c = db.rawQuery(sql, new String[] { name });
	// if (c.moveToNext()) {
	// return true;
	// } else {
	// return false;
	// }
	//
	// }
	public boolean ci(String name) {
		String sql = "select city,datime from sytq where city=?";
		Cursor c = db.rawQuery(sql, new String[] { name });
		if (c.moveToNext()) {

			return true;

		} else {
			return false;
		}

	}

	public String getNumber(String ci) {
		String sql = "select number from city where name=?";
		Cursor c = db.rawQuery(sql, new String[] { ci });
		String nu = "";
		if (c.moveToNext()) {
			String weather = c.getString(c.getColumnIndex("number"));
			nu = weather;
		}
		return nu;

	}

	public Sweather getweather(String name) {
		String sql = "select * from sytq where city=?";
		Cursor c = db.rawQuery(sql, new String[] { name });
		Sweather sweather = null;
		if (c.moveToNext()) {
			String city = c.getString(c.getColumnIndex("city"));
			String weather = c.getString(c.getColumnIndex("weather"));
			String temp = c.getString(c.getColumnIndex("temp"));
			String date = c.getString(c.getColumnIndex("date"));
			String weekday = c.getString(c.getColumnIndex("weekday"));
			String feelTemp = c.getString(c.getColumnIndex("feeltemp"));
			String realTime = c.getString(c.getColumnIndex("realtime"));
			String level = c.getString(c.getColumnIndex("level"));
			String pM2Dot5Data = c.getString(c.getColumnIndex("pmdotdata"));
			String windDirection = c.getString(c.getColumnIndex("wd"));
			String windSpeed = c.getString(c.getColumnIndex("ws"));
			String sd = c.getString(c.getColumnIndex("sd"));
			String lunar = c.getString(c.getColumnIndex("lunar"));
			String pm25 = c.getString(c.getColumnIndex("pm"));
			String pmdotdatanear = c.getString(c
					.getColumnIndex("pmdotdatanear"));
			String positionname = c.getString(c.getColumnIndex("positionname"));
			String pmnear = c.getString(c.getColumnIndex("pmnear"));
			String levelnear = c.getString(c.getColumnIndex("levelnear"));
			String positionnamenear = c.getString(c
					.getColumnIndex("positionnamenear"));

			sweather = new Sweather(weather, city, temp, weekday, feelTemp,
					realTime, date, level, windSpeed, windDirection,
					pM2Dot5Data, lunar, pm25, sd, pmdotdatanear, positionname,
					pmnear, levelnear, positionnamenear);
		}

		return sweather;

	}

	public void insertCurrentPm(NearestPm nearestPm) {
		if (isExitCurrentPm(nearestPm.getCity())) {
			ContentValues contentValues = new ContentValues();
			if (nearestPm.isFlag()) {
				contentValues.put("flag", "1");
			} else {
				contentValues.put("flag", "0");
			}
			contentValues.put("city", nearestPm.getCity());
			contentValues.put("co", nearestPm.getCo());
			contentValues.put("so2", nearestPm.getSo2());
			contentValues.put("o3", nearestPm.getO3());
			contentValues.put("no2", nearestPm.getNo2());
			contentValues.put("aqi", nearestPm.getAqi());
			contentValues.put("pm10", nearestPm.getPm10());
			contentValues.put("longitude", nearestPm.getLongitude());
			contentValues.put("pm25", nearestPm.getPm25());
			contentValues.put("latitude", nearestPm.getLatitude());
			contentValues.put("primary_pollutant",
					nearestPm.getPrimary_pollutant());
			contentValues.put("position_name", nearestPm.getPosition_name());
			contentValues.put("datime", new Date().getTime());

			int ddd = db.update(CURRENTCITYPM_TABLE_NAME, contentValues,
					"city=?", new String[] { nearestPm.getCity() });
		} else {
			ContentValues contentValues = new ContentValues();
			if (nearestPm.isFlag()) {
				contentValues.put("flag", "1");
			} else {
				contentValues.put("flag", "0");
			}
			contentValues.put("city", nearestPm.getCity());
			contentValues.put("co", nearestPm.getCo());
			contentValues.put("so2", nearestPm.getSo2());
			contentValues.put("o3", nearestPm.getO3());
			contentValues.put("no2", nearestPm.getNo2());
			contentValues.put("aqi", nearestPm.getAqi());
			contentValues.put("pm10", nearestPm.getPm10());
			contentValues.put("longitude", nearestPm.getLongitude());
			contentValues.put("pm25", nearestPm.getPm25());
			contentValues.put("latitude", nearestPm.getLatitude());
			contentValues.put("primary_pollutant",
					nearestPm.getPrimary_pollutant());
			contentValues.put("position_name", nearestPm.getPosition_name());
			contentValues.put("datime", new Date().getTime());

			long xxx = db.insert(CURRENTCITYPM_TABLE_NAME, null, contentValues);
		}

	}

	public NearestPm getNearestPm(String name) {
		String sql = "select * from " + CURRENTCITYPM_TABLE_NAME
				+ " where city=?";
		Cursor c = db.rawQuery(sql, new String[] { name });
		NearestPm nearestPm = null;
		if (c.moveToNext()) {
			String flagTag = c.getString(c.getColumnIndex("flag"));
			String co = c.getString(c.getColumnIndex("co"));
			String o3 = c.getString(c.getColumnIndex("o3"));
			String so2 = c.getString(c.getColumnIndex("so2"));
			String no2 = c.getString(c.getColumnIndex("no2"));
			String aqi = c.getString(c.getColumnIndex("aqi"));
			String pm10 = c.getString(c.getColumnIndex("pm10"));
			String longitude = c.getString(c.getColumnIndex("longitude"));
			String pm25 = c.getString(c.getColumnIndex("pm25"));
			String latitude = c.getString(c.getColumnIndex("latitude"));
			String primary_pollutant = c.getString(c
					.getColumnIndex("primary_pollutant"));
			String position_name = c.getString(c
					.getColumnIndex("position_name"));
			boolean flag = false;
			if ("1".equals(flagTag)) {
				flag = true;
			}
			nearestPm = new NearestPm(flag, name, co, so2, o3, no2, aqi, pm10,
					longitude, pm25, latitude, primary_pollutant, position_name);
		}
		return nearestPm;

	}

	public List<String> getall() {
		String sql = "select name from addcity";
		List<String> xx = new ArrayList<String>();
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("name"));

			xx.add(province);
		}
		return xx;
	}

	public void cha(Sweather sweather) {
		if (ci(sweather.getCity())) {

			ContentValues contentValues = new ContentValues();

			contentValues.put("weather", sweather.getWeather());
			contentValues.put("temp", sweather.getTemp());
			contentValues.put("date", sweather.getDate());
			contentValues.put("weekday", sweather.getWeekday());
			contentValues.put("feeltemp", sweather.getFeelTemp());
			contentValues.put("realtime", sweather.getRealTime());
			contentValues.put("level", sweather.getLevel());
			contentValues.put("pmdotdata", sweather.getPM2Dot5Data());
			contentValues.put("wd", sweather.getWindDirection());
			contentValues.put("ws", sweather.getWindSpeed());
			contentValues.put("sd", sweather.getSD());
			contentValues.put("lunar", sweather.getLunar());
			contentValues.put("datime", new Date().getTime());
			contentValues.put("pmdotdatanear", sweather.getPM2Dot5Data_near());
			contentValues.put("pm", sweather.getPm25());
			contentValues.put("pmnear", sweather.getPm25_near());
			contentValues.put("positionname", sweather.getPosition_name());
			contentValues.put("levelnear", sweather.getLevel_near());
			contentValues.put("positionnamenear",
					sweather.getPosition_name_near());

			int ddd = db.update("sytq", contentValues, "city=?",
					new String[] { sweather.getCity() });
		} else {
			ContentValues contentValues = new ContentValues();

			contentValues.put("city", sweather.getCity());
			contentValues.put("weather", sweather.getWeather());
			contentValues.put("temp", sweather.getTemp());
			contentValues.put("date", sweather.getDate());
			contentValues.put("weekday", sweather.getWeekday());
			contentValues.put("feeltemp", sweather.getFeelTemp());
			contentValues.put("realtime", sweather.getRealTime());
			contentValues.put("level", sweather.getLevel());
			contentValues.put("pmdotdata", sweather.getPM2Dot5Data());
			contentValues.put("wd", sweather.getWindDirection());
			contentValues.put("ws", sweather.getWindSpeed());
			contentValues.put("sd", sweather.getSD());
			contentValues.put("lunar", sweather.getLunar());
			contentValues.put("datime", new Date().getTime());
			contentValues.put("pmdotdatanear", sweather.getPM2Dot5Data_near());
			contentValues.put("pm", sweather.getPm25());
			contentValues.put("pmnear", sweather.getPm25_near());
			contentValues.put("positionname", sweather.getPosition_name());
			contentValues.put("levelnear", sweather.getLevel_near());
			contentValues.put("positionnamenear",
					sweather.getPosition_name_near());
			long xxx = db.insert("sytq", null, contentValues);
		}

	}

	public ArrayList<HashMap<String, Object>> convertCursorToArrayListHashMap(
			Cursor data_cursor) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> dataRow = null;
		HashMap<String, Integer> dataKey = null;

		try {
			String[] tableColumon = data_cursor.getColumnNames();// 得到所有的key
			dataKey = new HashMap<String, Integer>();// 初始化key对其在本行的位置
			Integer index = null;
			while (data_cursor.moveToNext()) {
				dataRow = new HashMap<String, Object>();
				for (String col : tableColumon) {
					/* 列值如果为null，则赋“” */
					index = dataKey.get(col);
					if (index == null) {
						index = data_cursor.getColumnIndex(col);
						dataKey.put(col, index);
					}
					String colValue = data_cursor.getString(index);// 得到当前Cursor
					// 对应key的脚标，然后取得对应的值
					if (colValue == null) {
						colValue = "";
					}
					dataRow.put(col.toLowerCase(), colValue);
				}
				data.add(dataRow);

				//
			}

		} catch (Exception e) {

			// throw new SQLiteException("用户解析游标数据出错：\n" + e.getMessage());
		} finally {
			if (data_cursor != null) {
				data_cursor.close();
			}

		}

		return data;
	}

	public void addXuanZhecity(String city, boolean isdingwei) {
		ContentValues contentValues = new ContentValues();
		int val = queryMaxValue("id", "addcity");
		if (TextUtils.isEmpty(val + "")) {
			contentValues.put("id", 1);
		} else {
			contentValues.put("id", 1 + val);
		}

		contentValues.put("name", city);
		db.insert("addcity", null, contentValues);

	}

	public void addXuanZhecity1(String city, String climate, String temp,
			boolean isLocation) {
		ContentValues contentValues = new ContentValues();
		int val = queryMaxValue("id", "addcity");
		if (TextUtils.isEmpty(val + "")) {
			contentValues.put("id", 1);
		} else {
			contentValues.put("id", 1 + val);
		}

		contentValues.put("name", city);
		contentValues.put("climate", climate);
		contentValues.put("temp", temp);
		if (isLocation) {
			contentValues.put("islocation", "1");
		} else {
			contentValues.put("islocation", "0");
		}
		db.insert("addcity", null, contentValues);
		WeiBaoApplication.getInstance().setIsUpdate(true);
	}

	public void update(String city, String climate, String temp) {
		// ContentValues contentValues=new ContentValues();
		// contentValues.put("climate", climate);
		// contentValues.put("temp", temp);
		// int xx= db.update("addcity", contentValues, "name", new
		// String[]{city});
		String sql = "update addcity set climate='" + climate + "' , temp='"
				+ temp + "' where name='" + city + "'";
		db.execSQL(sql);
		// ContentValues contentValues=new ContentValues();
		// int val=queryMaxValue("id", "addcity");
		// if (TextUtils.isEmpty(val+"")) {
		// contentValues.put("id", 1);
		// }
		// else {
		// contentValues.put("id", 1+val);
		// }
		//
		//
		//
		// contentValues.put("name", city);
		// contentValues.put("climate", climate);
		// contentValues.put("temp", temp);
		// db.insert("addcity", null, contentValues);

	}

	public void updateLocation(String city) {
		deleteLocationCity();
		String sql = "update addcity set islocation='1' where name='" + city
				+ "'";
		db.execSQL(sql);
	}

	public void deleteLocationCity() {
		db.execSQL("delete  from addcity where islocation='1'");
		WeiBaoApplication.getInstance().setIsUpdate(true);
	}

	public void deleteSelectedCity(String city) {
		db.execSQL("delete  from addcity where name='" + city + "'");
		WeiBaoApplication.getInstance().setIsUpdate(true);
	}

	public List<Float> selectStation(String station) {
		List<Float> stationList = new ArrayList<Float>();
		String sql = "select * from location where  area ='" + station+"'";
		MyLog.i(">>>>>>>>sqlgllgl"+sql);
		Cursor c = db.rawQuery(sql, null);
		float lat=0,lng =0;
		if (c.moveToFirst()) {
			lat = c.getFloat(c.getColumnIndex("lat"));
			lng = c.getFloat(c.getColumnIndex("lng"));
		}
		stationList.add(lat);
		stationList.add(lng);
		return stationList;
	}

	public List<City> getAllCity() {
		List<City> list = new ArrayList<City>();
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("province"));
			String city = c.getString(c.getColumnIndex("name"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("pinyin"));
			String allFirstPY = c.getString(c.getColumnIndex("py"));
			// String isLocation = c.getString(c.getColumnIndex("islocation"));
			City item = new City(province, city, number, allPY, allFirstPY);
			list.add(item);
		}
		return list;
	}

	public City getCity(String city) {
		if (TextUtils.isEmpty(city))
			return null;
		City item = getCityInfo(city);// 先全部搜索
		if (item == null) {
			item = getCityInfo(parseName(city));// 处理一下之后再搜索
		}
		return item;
	}

	public String getCityCode(String city) {
		String code = "";
		Cursor c = db.rawQuery("SELECT * FROM " + CITY_TABLE_NAME
				+ " where name=?", new String[] { city });
		if (c.moveToFirst()) {
			code = c.getString(c.getColumnIndex("number"));
		}

		return code;
	}

	/***
	 * 返回应用当前用到的城市的名称
	 * 
	 * @return 城市名称
	 */
	public String getRealtimeCityName() {
		String name = "";
		Cursor c = db.rawQuery("SELECT * FROM " + ADDCITY_TABLE_NAME
				+ " where id>=?", new String[] { "0" });
		if (c.moveToFirst()) {
			name = c.getString(c.getColumnIndex("name"));
		}

		return name;

	}

	// public getallcity(){
	//
	// String x="select "
	// }

	public void updateCityAqi(List<AQIPoint> plist) {
		for (int i = 0; i < plist.size(); i++) {
			AQIPoint pt = plist.get(i);
			if (pt == null) {
				continue;
			}
			ContentValues vc = new ContentValues();
			vc.put("AQI", pt.getAqi());
			vc.put("PM25", pt.getPm2_5());
			vc.put("PM10", pt.getPM10());
			vc.put("SO2", pt.getSO2());
			vc.put("NO2", pt.getNO2());
			vc.put("CO", pt.getCO());
			vc.put("O3", pt.getO3());
			vc.put("TIMEPOINT", pt.getUpdateTime());
			// 先查询是否已经有该监测站的数据记录
			Cursor c = db
					.rawQuery(
							"select * from MoniSiteRealVAL where SITENAME=? and CITY=?",
							new String[] { pt.getJiancedian(), pt.getCity() });
			if (c.getCount() > 0)// 有，更新记录
			{
				db.update("MoniSiteRealVAL", vc, "CITY=? and SITENAME=?",
						new String[] { pt.getCity(), pt.getJiancedian() });
			} else// 无，插入记录
			{
				vc.put("SITENAME", pt.getJiancedian());
				vc.put("CITY", pt.getCity());
				db.insert("MoniSiteRealVAL", null, vc);
			}
			c.close();

		}
	}

	public void updateCityPollutionPoints(List<PollutionPointModel> plist) {
		try {

			for (int i = 0; i < plist.size(); i++) {
				PollutionPointModel pt = plist.get(i);
				if (pt == null) {
					continue;
				}
				MyLog.i("" + pt.getUsid() + " +++  city:" + pt.getCity());
				ContentValues vc = new ContentValues();
				vc.put("name", pt.getName());
				vc.put("type", pt.getType());
				vc.put("sort", pt.getSort());
				vc.put("district", pt.getDistrict());
				vc.put("address", pt.getAddress());
				vc.put("lat", pt.getLat());
				vc.put("lng", pt.getLng());
				vc.put("updatetime", WbMapUtil.Date2Str(pt.getUpdateTime()));
				// 先查询是否已经有该监测站的数据记录
				Cursor c = db
						.rawQuery(
								"select * from pollution_point where usid=? and CITY=?",
								new String[] { pt.getUsid(), pt.getCity() });
				if (c.getCount() > 0)// 有，更新记录
				{
					db.update("pollution_point", vc, "CITY=? and usid=?",
							new String[] { pt.getCity(), pt.getUsid() });
				} else// 无，插入记录
				{
					vc.put("usid", pt.getUsid());
					vc.put("city", pt.getCity());
					db.insert("pollution_point", null, vc);
				}
				c.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateCitySurfaceWater(List<SurfaceWaterModel> plist) {
		for (int i = 0; i < plist.size(); i++) {

			SurfaceWaterModel pt = plist.get(i);
			if (pt == null) {
				continue;
			}
			MyLog.i("pt.getCity() :" + pt.getCity());
			ContentValues vc = new ContentValues();
			vc.put("targetquality", pt.getTarget_quality());
			vc.put("mowquality", pt.getMow_quality());
			vc.put("monitorpoint", pt.getMonitor_point());
			vc.put("baidulngd", pt.getBaidu_lng_d());
			vc.put("baidulatd", pt.getBaidu_lat_d());
			vc.put("cfunction", pt.getC_function());
			vc.put("criver", pt.getC_river());
			vc.put("cwater", pt.getC_water());
			vc.put("resource", pt.getResource());
			vc.put("rtime", pt.getR_time());
			vc.put("rweek", pt.getR_week());
			vc.put("updatetime", WbMapUtil.Date2Str(pt.getUpdateTime()));
			// 先查询是否已经有该监测站的数据记录
			Cursor c = db.rawQuery(
					"select * from surface_water where code=? and CITY=?",
					new String[] { pt.getCode(), pt.getCity() });
			if (c.getCount() > 0)// 有，更新记录
			{
				db.update("surface_water", vc, "CITY=? and code=?",
						new String[] { pt.getCity(), pt.getCode() });
			} else// 无，插入记录
			{
				vc.put("code", pt.getCode());
				vc.put("city", pt.getCity());
				db.insert("surface_water", null, vc);
			}
			c.close();

		}
	}

	public void updateWeather(String city, int weathercode) {

		ContentValues cv = new ContentValues();
		cv.put("weatherCode", weathercode);
		cv.put("weatherTime", WbMapUtil.Date2Str(new Date()));

		// 更新数据
		db.update("city", cv, "name = ?", new String[] { city });

	}

	public void updateSurfaceWaterTime(String city) {
		MyLog.i("city :" + city);
		ContentValues cv = new ContentValues();
		cv.put("surfacewaterTime", WbMapUtil.Date2Str(new Date()));
		// 更新数据
		db.update("city", cv, "name = ?", new String[] { city });

	}

	public void updatepollutionTime(String city) {
		MyLog.i("city :" + city);
		ContentValues cv = new ContentValues();
		cv.put("pollutionTime", WbMapUtil.Date2Str(new Date()));
		// 更新数据
		db.update("city", cv, "name = ?", new String[] { city });

	}

	/**
	 * 根据城市名称获取省会天气
	 * 
	 * @param city
	 * @return
	 */
	public Province getProvinceByCity(String city) {
		Province p = new Province();
		String sql = "select provinceLoc.province,"
				+ "provinceLoc.city,"
				+ "provinceLoc.lat,"
				+ "provinceLoc.long,"
				+ "city.weatherCode,"
				+ "city.weatherTime,"
				+ "weather_type_map.name,"
				+ "weather_type_map.ch_name "
				+ "from provinceLoc,city,weather_type_map "
				+ "where provinceLoc.city=city.name and city.weatherCode=weather_type_map.code "
				+ "and city.name=";
		Cursor c = db.rawQuery(sql + "?", new String[] { city });

		String provinceStr = "";

		float lat = 0;
		float lon = 0;
		int weathercode = 0;
		Date date = new Date();
		String weathername = "";
		String weatherchname = "";

		if (c.moveToFirst()) {

			lat = c.getFloat(c.getColumnIndex("lat"));
			lon = c.getFloat(c.getColumnIndex("long"));

			provinceStr = c.getString(c.getColumnIndex("province"));
			weathercode = c.getInt(c.getColumnIndex("weatherCode"));
			date = strToDate(c.getString(c.getColumnIndex("weatherTime")));
			weathername = c.getString(c.getColumnIndex("name"));
			weatherchname = c.getString(c.getColumnIndex("ch_name"));

			p.setName(provinceStr);
			p.setCity(city);
			p.setLocation(new LatLng(lat, lon));
			p.setWeather(weathercode);
			p.setWeatherUpdateTime(date);
			p.setWeather_name(weathername);
			p.setWeather_chname(weatherchname);

		}
		c.close();
		return p;
	}

	public City getCityByCityname(String city) {
		City ci = new City();
		String sql = "select a.province,a.name,a.weatherCode,a.weatherTime,a.surfacewaterTime,a.pollutionTime,a.lat,a.lon,b.name as wname,b.ch_name "
				+ "from city a,weather_type_map b "
				+ "where a.weatherCode = b.code " + "and a.name =";
		Cursor c = db.rawQuery(sql + "?", new String[] { city });
		String provinceStr = "";
		float lat = 0;
		float lon = 0;
		int weathercode = 0;
		Date date = new Date();
		Date dateWater = new Date();
		Date datePollution = new Date();
		String weathername = "";
		String weatherchname = "";
		if (c.moveToFirst()) {

			lat = c.getFloat(c.getColumnIndex("lat"));
			lon = c.getFloat(c.getColumnIndex("lon"));
			provinceStr = c.getString(c.getColumnIndex("province"));
			weathercode = c.getInt(c.getColumnIndex("weatherCode"));
			date = strToDate(c.getString(c.getColumnIndex("weatherTime")));
			dateWater = strToDate(c.getString(c
					.getColumnIndex("surfacewaterTime")));
			datePollution = strToDate(c.getString(c
					.getColumnIndex("pollutionTime")));
			weathername = c.getString(c.getColumnIndex("wname"));
			weatherchname = c.getString(c.getColumnIndex("ch_name"));

			ci.setProvince(provinceStr);
			ci.setName(city);
			ci.setLocation(new LatLng(lat, lon));
			ci.setWeather(weathercode);
			ci.setWeatherUpdateDate(date);
			ci.setWeathername(weathername);
			ci.setWeatherchname(weatherchname);
			ci.setSurfaceWaterUpdateDate(dateWater);
			ci.setPollutionUpdateDate(datePollution);

		}
		c.close();
		return ci;
	}

	/**
	 * 以城市为单位获取，只要城市坐标在地图范围内，返回该城市所有 监测点，如果城市不在，监测点都不返回
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @return
	 */
	public List<AQIPoint> getAQIPointByMapExt(double left, double right,
			double bottom, double top) {
		List<AQIPoint> result = new ArrayList<AQIPoint>();
		List<AQIPoint> citys = getAQICityByMapExt(left, right, bottom, top);
		for (int i = 0; i < citys.size(); i++) {
			List<AQIPoint> citypoints = getAQIPointsByCity(citys.get(i)
					.getCity(), "point");
			result.addAll(citypoints);
		}
		return result;
	}

	/**
	 * 以城市为单位获取，只要城市坐标在地图范围内，返回该城市所有 监测点，如果城市不在，监测点都不返回
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @return
	 */
	public List<SurfaceWaterModel> getSurfaceWaterPointsByMapExt(double left,
			double right, double bottom, double top) {
		List<SurfaceWaterModel> result = new ArrayList<SurfaceWaterModel>();
		List<AQIPoint> citys = getAQICityByMapExt(left, right, bottom, top);
		for (int i = 0; i < citys.size(); i++) {
			List<SurfaceWaterModel> citypoints = getSurfaceWaterPointsByCity(citys
					.get(i).getCity());
			result.addAll(citypoints);
		}
		return result;
	}

	/**
	 * 根据城市名称和查询类型获取检测数据点列表
	 * 
	 * @param name
	 *            城市名称
	 * @return
	 */
	public List<SurfaceWaterModel> getSurfaceWaterPointsByCity(String name) {
		List<SurfaceWaterModel> points = new ArrayList<SurfaceWaterModel>();
		String sql = "";
		sql = "select * from surface_water where city=?";
		Cursor c = db.rawQuery(sql, new String[] { name });
		int rowNum = c.getCount();

		String target_quality;
		String code;
		String mow_quality;
		String monitor_point;
		String baidu_lng_d;
		String baidu_lat_d;
		String c_function;
		String c_river;
		String c_water;
		String city;
		String resource;
		String r_time;
		String r_week;
		Date uptDate;

		if (rowNum > 0) {
			c.moveToFirst();
			for (int i = 0; i < rowNum; i++) {
				target_quality = c.getString(c.getColumnIndex("targetquality"));
				code = c.getString(c.getColumnIndex("code"));
				mow_quality = c.getString(c.getColumnIndex("mowquality"));
				monitor_point = c.getString(c.getColumnIndex("monitorpoint"));
				baidu_lng_d = c.getString(c.getColumnIndex("baidulngd"));
				baidu_lat_d = c.getString(c.getColumnIndex("baidulatd"));
				c_function = c.getString(c.getColumnIndex("cfunction"));
				c_river = c.getString(c.getColumnIndex("criver"));
				c_water = c.getString(c.getColumnIndex("cwater"));
				city = c.getString(c.getColumnIndex("city"));
				resource = c.getString(c.getColumnIndex("resource"));
				r_time = c.getString(c.getColumnIndex("rtime"));
				r_week = c.getString(c.getColumnIndex("rweek"));
				if (c.getString(c.getColumnIndex("updatetime")) == null) {
					uptDate = strToDate("2000-01-01 12:00:00");
				} else {
					uptDate = strToDate(c.getString(c
							.getColumnIndex("updatetime")));
				}

				c.moveToNext();
				SurfaceWaterModel surfaceWaterModel = new SurfaceWaterModel(
						target_quality, code, mow_quality, monitor_point,
						baidu_lng_d, baidu_lat_d, c_function, c_river, c_water,
						city, resource, r_time, r_week);
				surfaceWaterModel.setUpdateTime(uptDate);
				points.add(surfaceWaterModel);
			}
		}
		c.close();
		return points;

	}

	/**
	 * 根据城市名称和查询类型获取检测数据点列表
	 * 
	 * @param name
	 *            城市名称
	 * @return
	 */
	public List<PollutionPointModel> getPollutionPointByCity(String city) {
		try {
			List<PollutionPointModel> points = new ArrayList<PollutionPointModel>();
			String sql = "";
			sql = "select * from pollution_point where city=?";
			Cursor c = db.rawQuery(sql, new String[] { city });
			int rowNum = c.getCount();
			MyLog.i("rowNum" + rowNum);
			String name;
			String type;
			String sort;
			String district;
			String address;
			String lat;
			String lng;
			String usid;
			Date uptDate;

			if (rowNum > 0) {
				c.moveToFirst();
				for (int i = 0; i < rowNum; i++) {
					name = c.getString(c.getColumnIndex("name"));
					type = c.getString(c.getColumnIndex("type"));
					sort = c.getString(c.getColumnIndex("sort"));
					district = c.getString(c.getColumnIndex("district"));
					address = c.getString(c.getColumnIndex("address"));
					lat = c.getString(c.getColumnIndex("lat"));
					lng = c.getString(c.getColumnIndex("lng"));
					usid = c.getString(c.getColumnIndex("usid"));
					if (c.getString(c.getColumnIndex("updatetime")) == null) {
						uptDate = strToDate("2000-01-01 12:00:00");
					} else {
						uptDate = strToDate(c.getString(c
								.getColumnIndex("updatetime")));
					}
					c.moveToNext();
					PollutionPointModel surfaceWaterModel = new PollutionPointModel(
							name, type, sort, city, district, address, lat,
							lng, usid);
					MyLog.i("PollutionPointModel" + surfaceWaterModel);
					surfaceWaterModel.setUpdateTime(uptDate);
					points.add(surfaceWaterModel);
				}
			}
			c.close();
			return points;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 根据城市名称和查询类型获取检测数据点列表
	 * 
	 * @param name
	 *            城市名称
	 * @param type
	 *            两种情况："city"-一个城市只返回一个点;"point"-返回该城市所有 的监测点
	 * @return
	 */
	public List<AQIPoint> getAQIPointsByCity(String name, String type) {
		List<AQIPoint> points = new ArrayList<AQIPoint>();
		String sql = "";
		if (type == "city") {
			sql = "select a.lat,a.lng,a.area,a.city,b.* from location a "
					+ "left join MoniSiteRealVAL b "
					+ "on a.area=b.SITENAME and a.city = b.city "
					+ "where b.city=b.sitename and a.city=?";
		} else if (type == "point") {
			sql = "select a.lat,a.lng,a.area,a.city,b.* from location a "
					+ "left join MoniSiteRealVAL b "
					+ "on a.area=b.SITENAME and a.city = b.city "
					+ "where a.city=?";
		}
		Cursor c = db.rawQuery(sql, new String[] { name });
		int rowNum = c.getCount();
		float lat = 0;
		float lon = 0;
		String sitename;
		String city;
		String aqival;
		String pm25val;
		String pm10val;
		String no2val;
		String so2val;
		String coval;
		String o3val;
		Date uptDate;
		if (rowNum > 0) {
			c.moveToFirst();
			for (int i = 0; i < rowNum; i++) {
				lat = c.getFloat(c.getColumnIndex("lat"));
				lon = c.getFloat(c.getColumnIndex("lng"));
				sitename = c.getString(c.getColumnIndex("area"));
				city = c.getString(c.getColumnIndex("city"));
				aqival = c.getString(c.getColumnIndex("AQI"));
				pm25val = c.getString(c.getColumnIndex("PM25"));
				pm10val = c.getString(c.getColumnIndex("PM10"));
				no2val = c.getString(c.getColumnIndex("NO2"));
				so2val = c.getString(c.getColumnIndex("SO2"));
				coval = c.getString(c.getColumnIndex("CO"));
				o3val = c.getString(c.getColumnIndex("O3"));
				if (c.getString(c.getColumnIndex("TIMEPOINT")) == null) {
					uptDate = strToDate("2000-01-01 12:00:00");
				} else {
					uptDate = strToDate(c.getString(c
							.getColumnIndex("TIMEPOINT")));
				}

				c.moveToNext();

				if (sitename.equals(name) && type.equals("point"))// 如果具体监测点名称中包含城市名称本身的，去掉。
				{

					continue;
				}

				AQIPoint pt = new AQIPoint(lon, lat, aqival, sitename, pm25val,
						so2val, no2val, o3val, coval, o3val, pm10val);
				pt.setCity(city);
				pt.setUpdateTime(uptDate+"");
				points.add(pt);
			}
		}
		c.close();
		return points;

	}

	public List<AQIPoint> getAQICityByMapExt(double left, double right,
			double bottom, double top) {
		float fleft = (float) left;
		float fright = (float) right;
		float fbottom = (float) bottom;
		float ftop = (float) top;
		List<AQIPoint> points = new ArrayList<AQIPoint>();
		String sql = "select * from location where area in(select  distinct city from location)";
		// + "where a.lat>? and a.lat<? and a.lng>? and a.lng<?";
		Cursor c = db.rawQuery(sql, null);
		int rowNum = c.getCount();
		MyLog.i("rowNum :" + rowNum);
		float lat = 0;
		float lon = 0;
		String sitename;
		String city;
		if (rowNum > 0) {
			c.moveToFirst();
			for (int i = 0; i < rowNum; i++) {
				lat = c.getFloat(c.getColumnIndex("lat"));
				lon = c.getFloat(c.getColumnIndex("lng"));
				sitename = c.getString(c.getColumnIndex("area"));
				city = c.getString(c.getColumnIndex("city"));
				c.moveToNext();
				AQIPoint pt = new AQIPoint((double) lon, (double) lat, city,
						sitename);
				points.add(pt);
			}
		}
		c.close();
		return points;

	}

	public List<City> getCitysByMapExt(double fbottom, double ftop,
			double fleft, double fright) {
		try {
			List<City> citys = new ArrayList<City>();
			String sql = "select a.province,a.name,a.weatherCode,a.weatherTime,a.surfacewaterTime,a.pollutionTime,a.lon,a.lat,c.name as wname,c.ch_name "
					+ "from city a,weather_type_map c  "
					+ "where a.weatherCode = c.code "
					+ "and a.lat >? and a.lat < ? and a.lon >? and a.lon < ? ";
			Cursor c = db.rawQuery(sql, new String[] { fbottom + "", ftop + "",
					fleft + "", fright + "" });
			int rowNum = c.getCount();
			String provinceStr = "";
			String city = "";
			float lat = 0;
			float lon = 0;
			int weathercode = 0;
			Date date = new Date();
			Date dateWater = new Date();
			Date datePollution = new Date();
			String weathername = "";
			String weatherchname = "";
			if (rowNum > 0) {
				c.moveToFirst();
				for (int i = 0; i < rowNum; i++) {
					lat = c.getFloat(c.getColumnIndex("lat"));
					lon = c.getFloat(c.getColumnIndex("lon"));

					provinceStr = c.getString(c.getColumnIndex("province"));
					city = c.getString(c.getColumnIndex("name"));
					weathercode = c.getInt(c.getColumnIndex("weatherCode"));
					date = strToDate(c.getString(c
							.getColumnIndex("weatherTime")));
					dateWater = strToDate(c.getString(c
							.getColumnIndex("surfacewaterTime")));
					datePollution = strToDate(c.getString(c
							.getColumnIndex("pollutionTime")));
					weathername = c.getString(c.getColumnIndex("wname"));
					weatherchname = c.getString(c.getColumnIndex("ch_name"));
					c.moveToNext();

					City ci = new City();
					ci.setProvince(provinceStr);
					ci.setName(city);
					ci.setLocation(new LatLng(lat, lon));
					ci.setWeather(weathercode);
					ci.setWeatherUpdateDate(date);
					ci.setWeathername(weathername);
					ci.setWeatherchname(weatherchname);
					ci.setSurfaceWaterUpdateDate(dateWater);
					ci.setPollutionUpdateDate(datePollution);
					citys.add(ci);
				}
			}
			c.close();
			return citys;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<City> getCitysWeatherByMapExt(float fleft, float fright,
			float fbottom, float ftop) {

		List<City> citys = new ArrayList<City>();
		String sql = "select a.province,a.name,a.weatherCode,a.weatherTime,a.surfacewaterTime,a.pollutionTime,a.lon,a.lat,c.name as wname,c.ch_name "
				+ "from city a,weather_type_map c  "
				+ "where a.weatherCode = c.code and a.province <> a.name "
				+ "and a.lat >? and a.lat < ? and a.lon >? and a.lon < ? ";
		Cursor c = db.rawQuery(sql, new String[] { fbottom + "", ftop + "",
				fleft + "", fright + "" });
		int rowNum = c.getCount();
		String provinceStr = "";
		String city = "";
		float lat = 0;
		float lon = 0;
		int weathercode = 0;
		Date date = new Date();
		Date dateWater = new Date();
		Date datePollution = new Date();
		String weathername = "";
		String weatherchname = "";
		if (rowNum > 0) {
			c.moveToFirst();
			for (int i = 0; i < rowNum; i++) {
				lat = c.getFloat(c.getColumnIndex("lat"));
				lon = c.getFloat(c.getColumnIndex("lon"));

				provinceStr = c.getString(c.getColumnIndex("province"));
				city = c.getString(c.getColumnIndex("name"));
				weathercode = c.getInt(c.getColumnIndex("weatherCode"));
				date = strToDate(c.getString(c.getColumnIndex("weatherTime")));
				dateWater = strToDate(c.getString(c
						.getColumnIndex("surfacewaterTime")));
				datePollution = strToDate(c.getString(c
						.getColumnIndex("pollutionTime")));
				weathername = c.getString(c.getColumnIndex("wname"));
				weatherchname = c.getString(c.getColumnIndex("ch_name"));
				c.moveToNext();

				City ci = new City();
				ci.setProvince(provinceStr);
				ci.setName(city);
				ci.setLocation(new LatLng(lat, lon));
				ci.setWeather(weathercode);
				ci.setWeatherUpdateDate(date);
				ci.setWeathername(weathername);
				ci.setWeatherchname(weatherchname);
				ci.setSurfaceWaterUpdateDate(dateWater);
				ci.setPollutionUpdateDate(datePollution);
				citys.add(ci);
			}
		}
		c.close();
		return citys;
	}

	public List<City> getMaincitysWeatherByMapExt(float fleft, float fright,
			float fbottom, float ftop) {

		List<City> maincitys = new ArrayList<City>();
		String sql = "select a.province,a.name,a.weatherCode,a.weatherTime,a.surfacewaterTime,a.pollutionTime,a.lon,a.lat,c.name as wname,c.ch_name "
				+ "from city a,weather_type_map c  "
				+ "where (a.number like '%0100' or a.number like '%01') "
				+ "and a.weatherCode = c.code "
				+ "and a.lat >? and a.lat < ? and a.lon >? and a.lon < ? ";
		Cursor c = db.rawQuery(sql, new String[] { fbottom + "", ftop + "",
				fleft + "", fright + "" });
		int rowNum = c.getCount();
		String provinceStr = "";
		String city = "";
		float lat = 0;
		float lon = 0;
		int weathercode = 0;
		Date date = new Date();
		Date dateWater = new Date();
		Date datePollution = new Date();
		String weathername = "";
		String weatherchname = "";
		if (rowNum > 0) {
			c.moveToFirst();
			for (int i = 0; i < rowNum; i++) {
				lat = c.getFloat(c.getColumnIndex("lat"));
				lon = c.getFloat(c.getColumnIndex("lon"));

				provinceStr = c.getString(c.getColumnIndex("province"));
				city = c.getString(c.getColumnIndex("name"));
				weathercode = c.getInt(c.getColumnIndex("weatherCode"));
				date = strToDate(c.getString(c.getColumnIndex("weatherTime")));
				dateWater = strToDate(c.getString(c
						.getColumnIndex("surfacewaterTime")));
				datePollution = strToDate(c.getString(c
						.getColumnIndex("pollutionTime")));
				weathername = c.getString(c.getColumnIndex("wname"));
				weatherchname = c.getString(c.getColumnIndex("ch_name"));
				c.moveToNext();

				City ci = new City();
				ci.setProvince(provinceStr);
				ci.setName(city);
				ci.setLocation(new LatLng(lat, lon));
				ci.setWeather(weathercode);
				ci.setWeatherUpdateDate(date);
				ci.setWeathername(weathername);
				ci.setWeatherchname(weatherchname);
				ci.setSurfaceWaterUpdateDate(dateWater);
				ci.setPollutionUpdateDate(datePollution);
				maincitys.add(ci);
			}
		}
		c.close();
		return maincitys;
	}

	/**
	 * 从数据库中获取在地图可见范围内的省集合，包含天气信息目前。
	 * 
	 * @return
	 */
	public List<Province> getProvincesWeatherByMapExt(float left, float right,
			float bottom, float top) {
		List<Province> province = new ArrayList<Province>();
		String sql = "select provinceLoc.province,"
				+ "provinceLoc.city,"
				+ "provinceLoc.lat,"
				+ "provinceLoc.long,"
				+ "city.weatherCode,"
				+ "city.weatherTime,"
				+ "weather_type_map.name,"
				+ "weather_type_map.ch_name "
				+ "from provinceLoc,city,weather_type_map "
				+ "where provinceLoc.city=city.name and city.weatherCode=weather_type_map.code";
		Cursor c = db.rawQuery(sql, null);
		int rowNum = c.getCount();

		String provinceStr = "";
		String city = "";
		float lat = 0;
		float lon = 0;
		int weathercode = 0;
		Date date = new Date();
		String weathername = "";
		String weatherchname = "";

		if (rowNum > 0) {
			c.moveToFirst();
			for (int i = 0; i < rowNum; i++) {
				lat = c.getFloat(c.getColumnIndex("lat"));
				lon = c.getFloat(c.getColumnIndex("long"));

				provinceStr = c.getString(c.getColumnIndex("province"));
				city = c.getString(c.getColumnIndex("city"));
				weathercode = c.getInt(c.getColumnIndex("weatherCode"));
				date = strToDate(c.getString(c.getColumnIndex("weatherTime")));
				weathername = c.getString(c.getColumnIndex("name"));
				weatherchname = c.getString(c.getColumnIndex("ch_name"));
				c.moveToNext();

				if (lat < bottom || lat > top || lon < left || lon > right) {

					continue;
				}

				Province p = new Province();
				p.setName(provinceStr);
				p.setCity(city);
				p.setLocation(new LatLng(lat, lon));
				p.setWeather(weathercode);
				p.setWeatherUpdateTime(date);
				p.setWeather_name(weathername);
				p.setWeather_chname(weatherchname);
				province.add(p);

			}
		}
		c.close();
		return province;
	}

	/**
	 * 获取城市坐标
	 * 
	 * @param city
	 * @return float[2]{经度，纬度}
	 */
	public float[] getLocationOfCity(String city) {
		float[] loc = new float[2];
		Cursor c = db.rawQuery("SELECT lon,lat FROM city" + " WHERE name = ?",
				new String[] { city });
		if (c.moveToFirst()) {
			loc[0] = c.getFloat(c.getColumnIndex("lon"));
			loc[1] = c.getFloat(c.getColumnIndex("lat"));
		}

		return loc;
	}

	public Boolean addCityExist(String cityName) {
		Cursor cursor = db.rawQuery("select * from addcity where name='"
				+ cityName + "'", null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	public Boolean addCityExistAndLocation(String cityName) {
		// Cursor cursor = db.rawQuery("select * from addcity where name='"
		// + cityName + "'", null);
		Cursor cursor = db.rawQuery("select * from addcity where name='"
				+ cityName + "'and islocation='1'", null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	public Boolean isHaveLocation() {
		// Cursor cursor = db.rawQuery("select * from addcity where name='"
		// + cityName + "'", null);
		Cursor cursor = db.rawQuery(
				"select * from addcity where islocation='1'", null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	/**
	 * @param str
	 *            ---"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	private Date strToDate(String str) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	public City getCityInfo(String city) {
		City item = null;
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where name=?", new String[] { city });
		if (c.moveToFirst()) {
			String province = c.getString(c.getColumnIndex("province"));
			String name = c.getString(c.getColumnIndex("name"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("pinyin"));
			String allFirstPY = c.getString(c.getColumnIndex("py"));
			// String isLocation = c.getString(c.getColumnIndex("islocation"));
			item = new City(province, name, number, allPY, allFirstPY);
		}
		c.close();
		return item;
	}

	/**
	 * 去掉市或县搜索
	 * 
	 * @param city
	 * @return
	 */
	private String parseName(String city) {
		city = city.replaceAll("市$", "").replaceAll("县$", "")
				.replaceAll("区$", "");
		return city;
	}

	/**
	 * 默认为不是定位城市
	 * 
	 * @param citys1
	 */
	public void chongxin(List<ManageCity> citys1) {
		// TODO Auto-generated method stub
		String sql = "delete  from addcity";
		db.execSQL(sql);
		for (int i = 0; i < citys1.size(); i++) {
			addXuanZhecity1(citys1.get(i).getCityName(), citys1.get(i)
					.getClimate(), citys1.get(i).getTemp(), false);

		}
		// db.delete(table, whereClause, whereArgs)

	}
	
	/**
	 * 从数据库中获取河南城市坐标；
	 * @param arraycity 河南城市名称的集合（不含“市”字）
	 * @return
	 */
	public HashMap<String,LaLngData> getProvinceCityLatLng(String[] arraycity) {
		
		HashMap<String,LaLngData> map = new HashMap<String, LaLngData>();
		Cursor c = null;
		for (int i = 0; i < arraycity.length; i++) {
			c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME
					+ " where name=?", new String[]{arraycity[i]});
			if(c.moveToFirst()){
				//经度
				double lon = c.getDouble(c.getColumnIndex("lon"));
				//纬度
				double lat = c.getDouble(c.getColumnIndex("lat"));
				LaLngData laLngData = new LaLngData(arraycity[i], lat, lon);
				map.put(arraycity[i], laLngData);
			}
			c.close();
		}
		return map;
	}
	
	
	
	
	
	
	
}
