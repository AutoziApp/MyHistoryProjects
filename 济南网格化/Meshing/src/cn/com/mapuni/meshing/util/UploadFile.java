package cn.com.mapuni.meshing.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.mapuni.android.base.interfaces.PathManager;
import com.tianditu.android.maps.GeoPoint;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

public class UploadFile {
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/octet-stream");

	private static final OkHttpClient client = new OkHttpClient();

	protected static final String Tag = "UPLOADFILE";

	public static void uploadFile(final Handler uphandler, List<String> list, String inspect, String head,
			String problemoftime, String thecode, String edt_task_name_value, String sp_task_type_value,
			String mbwz_eit_value, String edt_patrol_content_value, GeoPoint point, String time) throws Exception {
		String url = PathManager.XCRW_URL;
		// String
		// url="http://192.168.1.171:8080/JiNanhuanbaoms/task/insertaskFeedback.do";
		// String
		// url="http://171.8.66.103:8473/JiNanhuanbaoms/task/insertaskFeedback.do";
		// String
		// url="http://192.168.15.66:8080/JiNanhuanbaoms/task/insertaskFeedback.do";
		uphandler.sendEmptyMessage(101);
		double lat = point.getLatitudeE6();
		double lon = point.getLongitudeE6();

		lat = lat / 1000000;
		lon = lon / 1000000;
		Log.i("bai", "lat £º" + lat);
		Log.i("bai", "lon £º" + lon);
		Log.i("bai", "edt_task_name_value £º" + edt_task_name_value);
		Log.i("bai", "mbwz_eit_value £º" + mbwz_eit_value);
		Log.i("bai", "sp_task_type_value £º" + sp_task_type_value);
		Log.i("bai", "edt_patrol_content_value £º" + edt_patrol_content_value);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("EntName", edt_task_name_value).addFormDataPart("userid", "123")
				.addFormDataPart("EntAddress", mbwz_eit_value).addFormDataPart("PollutionType", sp_task_type_value)
				.addFormDataPart("WTJLSJ", time/* "2017-02-22" */).addFormDataPart("Latitude", lat + "")
				.addFormDataPart("Longitude", lon + "").addFormDataPart("WTJL", edt_patrol_content_value)
				.addFormDataPart("inspect", inspect).addFormDataPart("head", head)
				.addFormDataPart("problemoftime", problemoftime).addFormDataPart("thecode", thecode);
		for (String path : list) {
			File file = new File(path);
			builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		}

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				Log.i(Tag, "bodyStr===" + bodyStr);
				if (ok) {
					Log.i(Tag, "Ö´ÐÐ³É¹¦:" + bodyStr);
					uphandler.sendEmptyMessage(102);
				} else {
					Log.i(Tag, "Ö´ÐÐÊ§°Ü1£º" + bodyStr);
					uphandler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				Log.i(Tag, "Ö´ÐÐÊ§°Ü2£º" + e.toString());
				uphandler.sendEmptyMessage(103);
			}
		});

	}

	public static void shangBao(final Handler uphandler, List<String> list, String edt_task_name_value,
			String sp_task_type_value, String mbwz_eit_value, String edt_patrol_content_value, GeoPoint point,
			String time) throws Exception {
		String url = PathManager.SHANGBAO_URL;
		// String
		// url="http://171.8.66.103:8184/JiNanhuanbaoms/task/shangbao.do";
		// String
		// url="http://192.168.1.171:8080/JiNanhuanbaoms/task/shangbao.do";
		uphandler.sendEmptyMessage(101);
		double lat = point.getLatitudeE6();
		double lon = point.getLongitudeE6();

		lat = lat / 1000000;
		lon = lon / 1000000;
		Log.i("bai", "lat £º" + lat);
		Log.i("bai", "lon £º" + lon);
		Log.i("bai", "edt_task_name_value £º" + edt_task_name_value);
		Log.i("bai", "mbwz_eit_value £º" + mbwz_eit_value);
		Log.i("bai", "sp_task_type_value £º" + sp_task_type_value);
		Log.i("bai", "edt_patrol_content_value £º" + edt_patrol_content_value);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("EntName", edt_task_name_value).addFormDataPart("userid", "123")
				.addFormDataPart("EntAddress", mbwz_eit_value).addFormDataPart("PollutionType", sp_task_type_value)
				.addFormDataPart("WTJLSJ", time/* "2017-02-22" */).addFormDataPart("Latitude", lat + "")
				.addFormDataPart("Longitude", lon + "").addFormDataPart("WTJL", edt_patrol_content_value);
		for (String path : list) {
			File file = new File(path);
			builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		}

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				Log.i(Tag, "bodyStr===" + bodyStr);
				if (ok) {
					Log.i(Tag, "Ö´ÐÐ³É¹¦:" + bodyStr);
					uphandler.sendEmptyMessage(102);
				} else {
					Log.i(Tag, "Ö´ÐÐÊ§°Ü1£º" + bodyStr);
					uphandler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				Log.i(Tag, "Ö´ÐÐÊ§°Ü2£º" + e.toString());
				uphandler.sendEmptyMessage(103);
			}
		});

	}

	public static void uploadFile(final Handler uphandler, List<String> list, String name, String start_time,
			String end_time) throws Exception {
		String url = PathManager.XCRW_URL;
		// String
		// url="http://171.8.66.103:8473/JiNanhuanbaoms/task/insertaskFeedback.do";
		uphandler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("EntName", name).addFormDataPart("userid", start_time)
				.addFormDataPart("EntAddress", end_time);
		for (String path : list) {
			File file = new File(path);
			builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
		}

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				Log.i(Tag, "bodyStr===" + bodyStr);
				if (ok) {
					Log.i(Tag, "Ö´ÐÐ³É¹¦:" + bodyStr);
					uphandler.sendEmptyMessage(102);
				} else {
					Log.i(Tag, "Ö´ÐÐÊ§°Ü1£º" + bodyStr);
					uphandler.sendEmptyMessage(103);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				Log.i(Tag, "Ö´ÐÐÊ§°Ü2£º" + e.toString());
				uphandler.sendEmptyMessage(103);
			}
		});
	}

	public static void uploadLocation(String userID, String lon, String lag) throws Exception {
		String url = PathManager.XCRW_URL;

		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("userid", userID).addFormDataPart("lon", lon).addFormDataPart("lag", lag);

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				boolean ok = response.isSuccessful();
				Log.i(Tag, "bodyStr===" + bodyStr);
				if (ok) {
					Log.i(Tag, "Ö´ÐÐ³É¹¦:" + bodyStr);

				} else {
					Log.i(Tag, "Ö´ÐÐÊ§°Ü1£º" + bodyStr);

				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				Log.i(Tag, "Ö´ÐÐÊ§°Ü2£º" + e.toString());
			}
		});
	}

	public static void login(final Handler uphandler, String username, String password, String clientType, String x,
			String y, String address) throws Exception {
		String url = PathManager.LONGIN_URL_JINAN;
//		String url = "http://192.168.1.151:8080/JiNanhuanbaoms/user/login.do";
		uphandler.sendEmptyMessage(101);
		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("username", username).addFormDataPart("password", password)
				.addFormDataPart("clientType", clientType).addFormDataPart("x", x).addFormDataPart("y", y)
				.addFormDataPart("address", address);

		RequestBody requestBody = builder.build();

		Request request = new Request.Builder().url(url).post(requestBody).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String bodyStr = response.body().string();
				Boolean isSuccess = false;
				try {
					JSONObject jsonObject = new JSONObject(bodyStr);
					isSuccess = jsonObject.getBoolean("isSuccess");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				boolean ok = response.isSuccessful();
				Log.i(Tag, "bodyStr===" + bodyStr);
				if (ok) {
					Log.i(Tag, "µÇÂ¼³É¹¦:" + bodyStr);
					if (isSuccess) {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = bodyStr;
						uphandler.sendMessage(msg);
					} else {
						Log.i(Tag, "µÇÂ¼Ê§°Ü£º" + bodyStr);
						uphandler.sendEmptyMessage(2);
					}
					// uphandler.sendEmptyMessage(1);
				} else {
					Log.i(Tag, "µÇÂ¼Ê§°Ü£º" + bodyStr);
					uphandler.sendEmptyMessage(2);
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				// TODO Auto-generated method stub
				Log.i(Tag, "ÎÞÍøÂçÁ¬½Ó£º" + e.toString());
				uphandler.sendEmptyMessage(3);
			}
		});
	}
}
