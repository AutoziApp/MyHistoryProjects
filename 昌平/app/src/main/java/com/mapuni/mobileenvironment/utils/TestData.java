package com.mapuni.mobileenvironment.utils;

import com.mapuni.mobileenvironment.bean.PollutionMode;
import com.mapuni.mobileenvironment.bean.PollutionModel;
import com.mapuni.mobileenvironment.model.ItemDetailModel;
import com.mapuni.mobileenvironment.model.Trend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TestData {
	
	private static String airData="{\"rows\":[{\"STATIONNAME\":\"武汉市\",\"STATIONCODE\":\"522600\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":114.3118310000,\"LATITUDE\":30.5984280000,\"SO2\":9.0,\"NO2\":17.0,\"PM10\":54.0,\"PM25\":46.0,\"O3\":61.0,\"CO\":0.609,\"PRIMARYPOLLUTANT\":\"PM10\"},{\"STATIONNAME\":\"黄石市\",\"STATIONCODE\":\"520400\",\"AQI\":54.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":115.0454330000,\"LATITUDE\":30.2053360000,\"SO2\":16.0,\"NO2\":16.0,\"PM10\":60.0,\"PM25\":10.0,\"O3\":82.0,\"CO\":0.488,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"十堰市\",\"STATIONCODE\":\"522200\",\"AQI\":41.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":110.8045400000,\"LATITUDE\":32.6350420000,\"SO2\":14.0,\"NO2\":23.0,\"PM10\":48.0,\"PM25\":10.0,\"O3\":32.0,\"CO\":2.266,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"宜昌市\",\"STATIONCODE\":\"520300\",\"AQI\":57.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":111.2929710000,\"LATITUDE\":30.6976020000,\"SO2\":12.0,\"NO2\":24.0,\"PM10\":25.0,\"PM25\":12.0,\"O3\":46.0,\"CO\":0.588,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"襄阳市\",\"STATIONCODE\":\"522400\",\"AQI\":62.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":112.1290890000,\"LATITUDE\":32.0147890000,\"SO2\":17.0,\"NO2\":21.0,\"PM10\":73.0,\"PM25\":19.0,\"O3\":62.0,\"CO\":0.670,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"鄂州市\",\"STATIONCODE\":\"522700\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":114.9015570000,\"LATITUDE\":30.3965220000,\"SO2\":10.0,\"NO2\":17.0,\"PM10\":59.0,\"PM25\":24.0,\"O3\":51.0,\"CO\":0.329,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"荆门市\",\"STATIONCODE\":\"522300\",\"AQI\":52.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":112.2058430000,\"LATITUDE\":31.0417920000,\"SO2\":12.0,\"NO2\":23.0,\"PM10\":46.0,\"PM25\":24.0,\"O3\":21.0,\"CO\":1.426,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"孝感市\",\"STATIONCODE\":\"520100\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":113.9229620000,\"LATITUDE\":30.9307120000,\"SO2\":17.0,\"NO2\":4.0,\"PM10\":66.0,\"PM25\":27.0,\"O3\":79.0,\"CO\":0.727,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"荆州市\",\"STATIONCODE\":\"520200\",\"AQI\":52.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":112.2472200000,\"LATITUDE\":30.3406060000,\"SO2\":22.0,\"NO2\":15.0,\"PM10\":61.0,\"PM25\":45.0,\"O3\":60.0,\"CO\":1.344,\"PRIMARYPOLLUTANT\":\"PM10\"}]}";
	private static String airData_Time="{\"rows\":[{\"STATIONNAME\":\"武汉市\",\"STATIONCODE\":\"522600\",\"AQI\":48.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":114.3118310000,\"LATITUDE\":30.5984280000,\"SO2\":10.0,\"NO2\":15.0,\"PM10\":51.0,\"PM25\":43.0,\"O3\":60.0,\"CO\":0.609,\"PRIMARYPOLLUTANT\":\"PM10\"},{\"STATIONNAME\":\"黄石市\",\"STATIONCODE\":\"520400\",\"AQI\":49.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":115.0454330000,\"LATITUDE\":30.2053360000,\"SO2\":10.0,\"NO2\":19.0,\"PM10\":44.0,\"PM25\":8.0,\"O3\":80.0,\"CO\":0.468,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"十堰市\",\"STATIONCODE\":\"522200\",\"AQI\":38.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":110.8045400000,\"LATITUDE\":32.6350420000,\"SO2\":14.0,\"NO2\":23.0,\"PM10\":49.0,\"PM25\":10.0,\"O3\":32.0,\"CO\":2.266,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"宜昌市\",\"STATIONCODE\":\"520300\",\"AQI\":66.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":111.2929710000,\"LATITUDE\":30.6976020000,\"SO2\":13.0,\"NO2\":24.0,\"PM10\":25.0,\"PM25\":12.0,\"O3\":46.0,\"CO\":0.588,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"襄阳市\",\"STATIONCODE\":\"522400\",\"AQI\":48.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":112.1290890000,\"LATITUDE\":32.0147890000,\"SO2\":11.0,\"NO2\":21.0,\"PM10\":73.0,\"PM25\":19.0,\"O3\":62.0,\"CO\":0.670,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"鄂州市\",\"STATIONCODE\":\"522700\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":114.9015570000,\"LATITUDE\":30.3965220000,\"SO2\":10.0,\"NO2\":17.0,\"PM10\":59.0,\"PM25\":24.0,\"O3\":51.0,\"CO\":0.329,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"荆门市\",\"STATIONCODE\":\"522300\",\"AQI\":52.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":112.2058430000,\"LATITUDE\":31.0417920000,\"SO2\":12.0,\"NO2\":23.0,\"PM10\":46.0,\"PM25\":24.0,\"O3\":21.0,\"CO\":1.426,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"孝感市\",\"STATIONCODE\":\"520100\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":113.9229620000,\"LATITUDE\":30.9307120000,\"SO2\":17.0,\"NO2\":4.0,\"PM10\":66.0,\"PM25\":27.0,\"O3\":79.0,\"CO\":0.727,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"荆州市\",\"STATIONCODE\":\"520200\",\"AQI\":52.0,\"MONIDATE\":\"2016/11/10 13:00:00\",\"LONGITUDE\":112.2472200000,\"LATITUDE\":30.3406060000,\"SO2\":22.0,\"NO2\":15.0,\"PM10\":61.0,\"PM25\":45.0,\"O3\":60.0,\"CO\":1.344,\"PRIMARYPOLLUTANT\":\"PM10\"}]}";
	private static String weatherData="{\"flag\":true,\"nowWeather\":[{\"city\":\"郑州\",\"weather\":\"多云转阴\",\"temp\":\"16℃~7℃\",\"date\":\"11/16\",\"weekday\":\"星期三\",\"feelTemp\":\"16℃\",\"realTime\":\"今天 13:00发布\",\"level\":\"轻度污染\",\"PM2Dot5Data\":\"144\",\"pm25\":\"110\",\"position_name\":\"郑州\",\"level_near\":\"轻度污染\",\"PM2Dot5Data_near\":\"144\",\"pm25_near\":\"110\",\"position_name_near\":\"郑州\",\"WD\":\"西风\",\"WS\":\"2级\",\"SD\":\"50%\",\"Lunar\":\"农历十月十七\"}],\"weather\":[{\"temp\":\"16℃~7℃\",\"weather\":\"多云转阴\",\"date\":\"11/16\",\"week\":\"今天\"},{\"temp\":\"14℃~9℃\",\"weather\":\"阴转小雨\",\"date\":\"11/17\",\"week\":\"明天\"},{\"temp\":\"16℃~10℃\",\"weather\":\"小雨转多云\",\"date\":\"11/18\",\"week\":\"周五\"},{\"temp\":\"19℃~8℃\",\"weather\":\"晴转阴\",\"date\":\"11/19\",\"week\":\"周六\"},{\"temp\":\"14℃~4℃\",\"weather\":\"小雨\",\"date\":\"11/20\",\"week\":\"周日\"},{\"temp\":/\"7℃~2℃\",\"weather\":\"小雨\",\"date\":\"11/21\",\"week\":\"周一\"}],\"index\":[{\"index_cy\":\"较冷\",\"index_cy_xs\":\"建议着厚外套加毛衣等服装。\",\"index_uv\":\"弱\",\"index_uv_xs\":\"外出请采取带防护帽和太阳镜等防护措施\",\"index_tr\":\"视情况而定\",\"index_tr_xs\":\"天气不错，可以出去旅游一下的\",\"index_co\":\"一般\",\"index_co_xs\":\"基本无污染，天气情况较好，总体感觉比较舒适\",\"index_cl\":\"不适宜\",\"index_cl_xs\":\"今天的天气状况不适宜晨练\",\"index_ls\":\"\",\"index_ls_xs\":\"基本无污染，天气情况较好，适宜晾晒\",\"index_xc\":\"不宜\",\"index_xc_xs\":\"今天的天气状况不适宜洗车\"}]}";
	private static String airString="{\"rows\":[{\"STATIONNAME\":\"东湖梨园 \",\"STATIONCODE\":\"522600\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3815360000,\"LATITUDE\":30.5800700000,\"SO2\":3.0,\"NO2\":17.0,\"PM10\":60.0,\"PM25\":87.0,\"O3\":3.0,\"CO\":1.284,\"PRIMARYPOLLUTANT\":\"PM10\"},{\"STATIONNAME\":\"汉阳月湖\",\"STATIONCODE\":\"520400\",\"AQI\":40.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.2531650000,\"LATITUDE\":30.5693050000,\"SO2\":2.0,\"NO2\":19.0,\"PM10\":60.0,\"PM25\":84.0,\"O3\":2.0,\"CO\":1.1110,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"汉口花桥\",\"STATIONCODE\":\"522200\",\"AQI\":33.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.2932240000,\"LATITUDE\":30.6211720000,\"SO2\":14.0,\"NO2\":18.0,\"PM10\":64.0,\"PM25\":10.0,\"O3\":2.0,\"CO\":0.914,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"武昌紫阳\",\"STATIONCODE\":\"520300\",\"AQI\":57.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3081540000,\"LATITUDE\":30.5414100000,\"SO2\":4.0,\"NO2\":22.0,\"PM10\":56.0,\"PM25\":84.0,\"O3\":3.0,\"CO\":1.404,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"青山钢花\",\"STATIONCODE\":\"522400\",\"AQI\":62.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3877230000,\"LATITUDE\":30.6249480000,\"SO2\":3.0,\"NO2\":18.0,\"PM10\":61.0,\"PM25\":103.0,\"O3\":2.0,\"CO\":1.142,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"沌口新区\",\"STATIONCODE\":\"522700\",\"AQI\":45.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.1911310000,\"LATITUDE\":30.5176850000,\"SO2\":3.0,\"NO2\":19.0,\"PM10\":62.0,\"PM25\":97.0,\"O3\":5.0,\"CO\":1.208,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"汉口江滩\",\"STATIONCODE\":\"522300\",\"AQI\":33.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3265470000,\"LATITUDE\":30.6161920000,\"SO2\":2.0,\"NO2\":19.0,\"PM10\":59.0,\"PM25\":72.0,\"O3\":2.0,\"CO\":0.968,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"东湖高新\",\"STATIONCODE\":\"520100\",\"AQI\":72.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.4229520000,\"LATITUDE\":30.4991570000,\"SO2\":5.0,\"NO2\":23.0,\"PM10\":74.0,\"PM25\":109.0,\"O3\":2.0,\"CO\":1.543,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"吴家山\",\"STATIONCODE\":\"520200\",\"AQI\":46.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.1491650000,\"LATITUDE\":30.6281260000,\"SO2\":2.0,\"NO2\":14.0,\"PM10\":51.0,\"PM25\":100.0,\"O3\":4.0,\"CO\":1.395,\"PRIMARYPOLLUTANT\":\"PM10\"}]}";
	private static String airString_Time="{\"rows\":[{\"STATIONNAME\":\"东湖梨园 \",\"STATIONCODE\":\"522600\",\"AQI\":59.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3815360000,\"LATITUDE\":30.5800700000,\"SO2\":3.0,\"NO2\":17.0,\"PM10\":60.0,\"PM25\":87.0,\"O3\":3.0,\"CO\":1.284,\"PRIMARYPOLLUTANT\":\"PM10\"},{\"STATIONNAME\":\"汉阳月湖\",\"STATIONCODE\":\"520400\",\"AQI\":40.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.2531650000,\"LATITUDE\":30.5693050000,\"SO2\":2.0,\"NO2\":19.0,\"PM10\":60.0,\"PM25\":84.0,\"O3\":2.0,\"CO\":1.1110,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"汉口花桥\",\"STATIONCODE\":\"522200\",\"AQI\":33.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.2932240000,\"LATITUDE\":30.6211720000,\"SO2\":14.0,\"NO2\":18.0,\"PM10\":64.0,\"PM25\":10.0,\"O3\":2.0,\"CO\":0.914,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"武昌紫阳\",\"STATIONCODE\":\"520300\",\"AQI\":57.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3081540000,\"LATITUDE\":30.5414100000,\"SO2\":4.0,\"NO2\":22.0,\"PM10\":56.0,\"PM25\":84.0,\"O3\":3.0,\"CO\":1.404,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"青山钢花\",\"STATIONCODE\":\"522400\",\"AQI\":62.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3877230000,\"LATITUDE\":30.6249480000,\"SO2\":3.0,\"NO2\":18.0,\"PM10\":61.0,\"PM25\":103.0,\"O3\":2.0,\"CO\":1.142,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"沌口新区\",\"STATIONCODE\":\"522700\",\"AQI\":45.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.1911310000,\"LATITUDE\":30.5176850000,\"SO2\":3.0,\"NO2\":19.0,\"PM10\":62.0,\"PM25\":97.0,\"O3\":5.0,\"CO\":1.208,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"汉口江滩\",\"STATIONCODE\":\"522300\",\"AQI\":33.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.3265470000,\"LATITUDE\":30.6161920000,\"SO2\":2.0,\"NO2\":19.0,\"PM10\":59.0,\"PM25\":72.0,\"O3\":2.0,\"CO\":0.968,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"东湖高新\",\"STATIONCODE\":\"520100\",\"AQI\":72.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.4229520000,\"LATITUDE\":30.4991570000,\"SO2\":5.0,\"NO2\":23.0,\"PM10\":74.0,\"PM25\":109.0,\"O3\":2.0,\"CO\":1.543,\"PRIMARYPOLLUTANT\":\"--\"},{\"STATIONNAME\":\"吴家山\",\"STATIONCODE\":\"520200\",\"AQI\":46.0,\"MONIDATE\":\"2016/11/18 13:00:00\",\"LONGITUDE\":114.1491650000,\"LATITUDE\":30.6281260000,\"SO2\":2.0,\"NO2\":14.0,\"PM10\":51.0,\"PM25\":100.0,\"O3\":4.0,\"CO\":1.395,\"PRIMARYPOLLUTANT\":\"PM10\"}]}";
//	private static List<WaterModel> waterData;
	public static String getAirData(){
		return airString;
	}
	public static String getNowAirData(){
		return airString_Time;
	}
	public static List<Trend> getWeatherData() {
//		return weatherData;
		List<Trend> list = new ArrayList<Trend>();
		for(int i=0;i<6;i++){
			Trend trend = new Trend();
			switch(i){
				case 0:
					trend.setDate("11/25");
					trend.setTemp("5℃~-5℃");
					trend.setWeather("小雨");
					trend.setWeek("今天");
					break;
				case 1:
					trend.setDate("11/26");
					trend.setTemp("8℃~-4℃");
					trend.setWeather("多云");
					trend.setWeek("明天");
					break;
				case 2:
					trend.setDate("11/27");
					trend.setTemp("7℃~-5℃");
					trend.setWeather("小雨");
					trend.setWeek("周日");
					break;
				case 3:
					trend.setDate("11/28");
					trend.setTemp("7℃~-4℃");
					trend.setWeather("小雨");
					trend.setWeek("周一");
					break;
				case 4:
					trend.setDate("11/29");
					trend.setTemp("3℃~-4℃");
					trend.setWeather("小雨");
					trend.setWeek("周二");
					break;
				case 5:
					trend.setDate("11/30");
					trend.setTemp("7℃~-2℃");
					trend.setWeather("小雨");
					trend.setWeek("周三");
					break;
			}
			list.add(trend);
		}
		return list; 
	}
	
//	public static List<WaterModel> getWaterData(Context context){
//		waterData = new ArrayList<WaterModel>();
//		for(int i=0;i<9;i++){
//			WaterModel _model = new WaterModel();
//			switch (i) {
//			case 0:
////				30.3199830000,114.0923570000
//				_model.setLATITUDE(30.3199830000);
//				_model.setLONGITUDE(114.0923570000);
//				_model.setArea("武汉市");
//				_model.setMuther("长江");
//				_model.setStationName("纱帽");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			case 1:
////				30.5585410000,114.6067080000
//				_model.setLATITUDE(30.5585410000);
//				_model.setLONGITUDE(114.6067080000);
//				_model.setArea("武汉市");
//				_model.setMuther("长江");
//				_model.setStationName("白浒山");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			case 2:
////				30.5404400000,114.2347010000
//				_model.setLATITUDE(30.5404400000);
//				_model.setLONGITUDE(114.2347010000);
//				_model.setArea("武汉市");
//				_model.setMuther("长江");
//				_model.setStationName("杨泗港");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			case 3:
////				30.7580090000,113.9644220000
//				_model.setLATITUDE(30.7580090000);
//				_model.setLONGITUDE(113.9644220000);
//				_model.setArea("武汉市");
//				_model.setMuther("汉江");
//				_model.setStationName("郭家台");
//				_model.setLevel(2);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img2));
//				break;
//			case 4:
////				30.2429660000,114.1121220000
//				_model.setLATITUDE(30.2429660000);
//				_model.setLONGITUDE(114.1121220000);
//				_model.setArea("武汉市");
//				_model.setMuther("汉江");
//				_model.setStationName("新港");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			case 5:
////				30.5875680000,114.2331970000
//				_model.setLATITUDE(30.5875680000);
//				_model.setLONGITUDE(114.2331970000);
//				_model.setArea("武汉市");
//				_model.setMuther("汉江");
//				_model.setStationName("宗关");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			case 6:
////				30.5717860000,114.2979740000
//				_model.setLATITUDE(30.5717860000);
//				_model.setLONGITUDE(114.2979740000);
//				_model.setArea("武汉市");
//				_model.setMuther("汉江");
//				_model.setStationName("龙王庙");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			case 7:
////				30.6786910000,113.9553640000
//				_model.setLATITUDE(30.6786910000);
//				_model.setLONGITUDE(113.9553640000);
//				_model.setArea("孝感市");
//				_model.setMuther("汉江");
//				_model.setStationName("汉川小河");
//				_model.setLevel(2);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img2));
//				break;
//			case 8:
////				30.3119610000,113.3556200000
//				_model.setLATITUDE(30.2020520000);
//				_model.setLONGITUDE(115.2117260000);
//				_model.setArea("仙桃市");
//				_model.setMuther("汉江");
//				_model.setStationName("仙桃汉南");
//				_model.setLevel(3);
//				_model.setmDrawable(context.getResources().getDrawable(R.drawable.water_img3));
//				break;
//			default:
//				break;
//			}
//			waterData.add(_model);
//		}
//
//		return waterData;
//	}
	
	public static List<HashMap<String,Object>> getWaterSource(){
		List<HashMap<String,Object>> list  =  new ArrayList<HashMap<String,Object>>();
		String[] s = new String[]{"重工铸锻有限责任公司","污水处理有限公司","钢电股份有限公司","黄金口污水处理有限公司",
		"木兰汉北集团有限公司","蒙牛乳制品责任有限公司","毅丰印染责任有限公司","华润雪花啤酒有限责任公司","高新热电有限责任公司"};
		
		for(int i=0;i<9;i++){
			HashMap<String,Object> map = new HashMap<>();
			map.put("entname",s[i]);
			list.add(map);
		}
		return list;
	}
	public static List<String> getAirSource(){
		List<String> list  =  new ArrayList<String>();
		list.add("武汉重工铸锻有限责任公司");
		list.add("木兰汉北有限公司");
		list.add("武汉钢电股份有限公司");
		list.add("武汉高新热电有限责任公司");
		list.add("华能发电有限公司");
		
		return list;
	}
	
	public static List<String> getCityWaterSource(){
		List<String> list  =  new ArrayList<String>();
		list.add("武汉宇奥科灵水处理有限公司");
		list.add("武汉汉西污水处理有限公司");
		list.add("中国石油化工有限公司武汉分公司");
		list.add("黄金口污水处理有限公司");
		list.add("二郎庙污水处理厂");
		list.add("蒙牛乳制品武汉责任有限公司");
		list.add("武汉毅丰印染责任有限公司");
		list.add("三金潭污水处理厂");
		list.add("武汉市城市排水发展有限公司");
		list.add("太子龙湖污水处理厂");
		list.add("城市排水发展有限公司汤逊湖污水处理厂");
		return list;
	}
	public static List<String> getCityAirSource(){
		List<String> list  =  new ArrayList<String>();
		list.add("武汉重工铸锻有限责任公司");
		list.add("武汉钢电股份有限公司");
		list.add("中国石油化工有限公司武汉分公司");
		
		return list;
	}
//
	public static List<PollutionMode> getPollutionInfo(){
		List<PollutionMode> list  =  new ArrayList<PollutionMode>();
		for(int i=0;i<11;i++){
			PollutionMode model = new PollutionMode();
			switch(i){
			case 0:
				model.setDate("2016-10-01");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 1:
				model.setDate("2016-10-02");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 2:
				model.setDate("2016-10-03");
				model.setLevel("25");
				model.setValue("20");
				break;
			case 3:
				model.setDate("2016-10-04");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 4:
				model.setDate("2016-10-05");
				model.setLevel("25");
				model.setValue("24");
				break;
			case 5:
				model.setDate("2016-10-06");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 6:
				model.setDate("2016-10-07");
				model.setLevel("25");
				model.setValue("21");
				break;
			case 7:
				model.setDate("2016-10-08");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 8:
				model.setDate("2016-10-09");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 9:
				model.setDate("2016-10-10");
				model.setLevel("25");
				model.setValue("23");
				break;
			case 10:
				model.setDate("2016-10-11");
				model.setLevel("25");
				model.setValue("19");
				break;
			}
			list.add(model);
		}
		return list;
	}
	public static List<PollutionMode> getPollutionItem(String s){
			PollutionMode model = new PollutionMode();
			if(s.equals("2016-10-01")){
				model.setDate("2016-10-01");
				model.setLevel("25");
				model.setValue("23");
			}else if(s.equals("2016-10-02")){
				model.setDate("2016-10-02");
				model.setLevel("25");
				model.setValue("23");
			}else if(s.equals("2016-10-03")){
				model.setDate("2016-10-03");
				model.setLevel("25");
				model.setValue("20");

			}else if(s.equals("2016-10-04")){
				model.setDate("2016-10-04");
				model.setLevel("25");
				model.setValue("21");

			}else if(s.equals("2016-10-05")){
				model.setDate("2016-10-05");
				model.setLevel("25");
				model.setValue("20");

			}else if(s.equals("2016-10-06")){
				model.setDate("2016-10-06");
				model.setLevel("25");
				model.setValue("20");

			}else if(s.equals("2016-10-07")){
				model.setDate("2016-10-07");
				model.setLevel("25");
				model.setValue("24");

			}else if(s.equals("2016-10-08")){
				model.setDate("2016-10-08");
				model.setLevel("25");
				model.setValue("23");

			}else if(s.equals("2016-10-09")){
				model.setDate("2016-10-09");
				model.setLevel("25");
				model.setValue("23");

			}else if(s.equals("2016-10-10")){
				model.setDate("2016-10-10");
				model.setLevel("25");
				model.setValue("23");

			}else if(s.equals("2016-10-11")){
				model.setDate("2016-10-11");
				model.setLevel("25");
				model.setValue("19");

			}else{
			    int max=24;
		        int min=15;
		        Random random = new Random();
		        int i = random.nextInt(max)%(max-min+1) + min;
				model.setDate(s);
				model.setLevel("25");
				model.setValue(""+i);
			}

		List<PollutionMode> list = new ArrayList<PollutionMode>();
		list.add(model);
		return list;
	}

	public static List<PollutionModel> getPollutionData(){
		List<PollutionModel> list = new ArrayList<PollutionModel>();
		for(int i=0;i<9;i++){
			PollutionModel model = new PollutionModel();
			switch(i){
				case 0:
//					30.6446570000,114.4759290000
					model.setName("武汉钢电股份有限公司");
					model.setLATITUDE(30.6446570000);
					model.setLONGITUDE(114.4759290000);
					model.setCod("--");
					model.setNh("--");
					model.setNOx("10");
					model.setSO2("20");
					break;
				case 1:
//					30.5850630000,114.4613200000
					model.setName("武汉重工铸锻公司");
					model.setLATITUDE(30.5850630000);
					model.setLONGITUDE(114.4613200000);
					model.setCod("19");
					model.setNh("13");
					model.setNOx("--");
					model.setSO2("--");
					break;
				case 2:
//					30.5943050000,114.3633280000
					model.setName("城市排水发展有限公司");
					model.setLATITUDE(30.5943050000);
					model.setLONGITUDE(114.3633280000);
					model.setCod("16");
					model.setNh("17");
					model.setNOx("--");
					model.setSO2("--");
					break;
				case 3:
//					30.7177910000,114.5755660000
					model.setName("武汉毅丰印染责任有限公司");
					model.setLATITUDE(30.7177910000);
					model.setLONGITUDE(114.5755660000);
					model.setCod("25");
					model.setNh("27");
					model.setNOx("--");
					model.setSO2("--");
					break;
				case 4:
//					30.7022240000,114.1496090000
					model.setName("蒙牛乳制品武汉分公司");
					model.setLATITUDE(30.7022240000);
					model.setLONGITUDE(114.1496090000);
					model.setCod("--");
					model.setNh("--");
					model.setNOx("28");
					model.setSO2("15");
					break;
				case 5:
//					30.5938990000,114.3628150000
					model.setName("二郎庙污水处理厂");
					model.setLATITUDE(30.5938990000);
					model.setLONGITUDE(114.3628150000);
					model.setCod("15");
					model.setNh("20");
					model.setNOx("--");
					model.setSO2("--");
					break;
				case 6:
//					30.5699440000,114.1503040000
					model.setName("黄金口污水处理厂");
					model.setLATITUDE(30.5699440000);
					model.setLONGITUDE(114.1503040000);
					model.setCod("15");
					model.setNh("25");
					model.setNOx("--");
					model.setSO2("--");
					break;
				case 7:
//					30.6446570000,114.4759290000
					model.setName("武汉钢电股份有限公司");
					model.setLATITUDE(30.6446570000);
					model.setLONGITUDE(114.4759290000);
					model.setCod("--");
					model.setNh("--");
					model.setNOx("15");
					model.setSO2("12");
					break;
				case 8:
//					30.6941440000,114.5577430000
					model.setName("华能发电有限公司");
					model.setLATITUDE(30.6941440000);
					model.setLONGITUDE(114.5577430000);
					model.setCod("15");
					model.setNh("28");
					model.setNOx("--");
					model.setSO2("--");
					break;

			}
			list.add(model);
		}
		return list;
	}
	public static List<ItemDetailModel> getXunChaData(){
		List<ItemDetailModel> list=new ArrayList<>();
		list.add(new ItemDetailModel("北京市裕顺丰商有限责任公司","2017-01-12"));
		list.add(new ItemDetailModel("北京市龙泽杨威科技开发中心","2017-01-11"));
		list.add(new ItemDetailModel("童欣欢乐（北京）玩具有限公司","2017-01-09"));
		list.add(new ItemDetailModel("北京中科帅霓虹电器厂","2017-01-07"));
		list.add(new ItemDetailModel("北京豪润杰贸易有限公司","2017-01-05"));
		return list;
	}
	public static List<ItemDetailModel> getChuZhiData(){
		List<ItemDetailModel> list=new ArrayList<>();
		list.add(new ItemDetailModel("北京坞镭材料厂","2017-01-12"));
		list.add(new ItemDetailModel("北京白皂化纤厂","2017-01-11"));
		list.add(new ItemDetailModel("北京博园粉末冶金公司","2017-01-09"));
		list.add(new ItemDetailModel("北京振中玻璃厂","2017-01-07"));
		list.add(new ItemDetailModel("北京风宝水泥厂","2017-01-05"));
		return list;
	}
}
