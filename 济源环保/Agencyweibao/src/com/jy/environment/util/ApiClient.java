package com.jy.environment.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.jy.environment.controls.WeiBaoApplication;
import com.jy.environment.model.LoginModel;
import com.jy.environment.webservice.UrlComponent;

public class ApiClient {

	public static String getDataFromServer(String url,
			Map<String, Object> pBodyParamMap)
			throws UnsupportedEncodingException {
		if (null == pBodyParamMap || pBodyParamMap.size() <= 0) {
			return connServerForResult(url);
		} else {
			JSONObject jsonObject = new JSONObject();
			for (String _Key : pBodyParamMap.keySet()) {
				if (!TextUtils.isEmpty(_Key)) {
					Object _Value = pBodyParamMap.get(_Key);
					MyLog.i("_Key" + _Key.toString());
					MyLog.i("_Value" + _Value.toString());
					try {
						jsonObject.put(_Key, _Value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			return PostToServerForResult(url, jsonObject.toString());
		}

	}

	public static String getDataFromServerByNewPost(String url,
			Map<String, Object> pBodyParamMap)
			throws UnsupportedEncodingException {
		if (null == pBodyParamMap || pBodyParamMap.size() <= 0) {
			return connServerForResult(url);
		} else {
			String param = "";
			JSONObject jsonObject = new JSONObject();
			for (String _Key : pBodyParamMap.keySet()) {
				if (!TextUtils.isEmpty(_Key)) {
					Object _Value = pBodyParamMap.get(_Key);
					try {
						
						if (url.contains("p/air/multi/cities/aqi/post")) {
							String value = (String)_Value;
							if(value.startsWith(","))
							{
								value = value.substring(1);
							}
							jsonObject.put("citys", value);
							param = jsonObject.toString();
						} else {
							jsonObject.put(_Key, _Value);
							param += ("&" + _Key + "=" + _Value);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			MyLog.i(">>>>>>>>>>>request"+param);
			if (url.contains("p/air/multi/cities/aqi/post"))
			{
				return ApiClient.PostToServerForResult(url, param);
			}
			return sendPostRequest(url, param);
		}
	}

	public static String connServerForResult(String url) {
		MyLog.i(url);
		if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) == NetUtil.NETWORN_NONE)
			return "";
		String strResult = "";
		String urlString = url;
		// for (int i = 0; i < UrlComponent.getBaseUrlList().size(); i++) {
		HttpGet httpRequest = new HttpGet(urlString);
		MyLog.i("string111" + urlString);
		try {
			// HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			// 请求超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			// 读取超时
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 15000);
			// 获得HttpResponse对象
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			// 取得返回的数据
			{
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 出错会返回空数据，注意处理
			strResult = "";
		}
		// if (!strResult.equals("")) {
		// String urlString1 = UrlComponent.getGoggle(url);
		// int m = urlString1.indexOf("api/");
		// if (m != -1) {
		// UrlComponent.setBaseurl(urlString1.substring(0, m + 4));
		// }
		// break;
		// } else {
		// if (i == 0) {
		// UrlComponent.setBaseurl(UrlComponent.getBaseUrlList().get(
		// i + 1));
		// urlString = UrlComponent.getGoggle(url);
		// } else {
		// UrlComponent.setBaseurl(UrlComponent.getBaseUrlList()
		// .get(0));
		// }
		// }
		// }
		return strResult;
	}

	public static String PostToServerForResult(String url, String content)
			throws UnsupportedEncodingException {
		String resultJson = "";
		if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) == NetUtil.NETWORN_NONE)
			return null;
		content = URLEncoder.encode(content, HTTP.UTF_8);
		HttpResponse httpResponse = null;
		// for (int i = 0; i < UrlComponent.getBaseUrlList().size(); i++) {
		try {
			// 第1步：创建HttpPost对象
			// HttpPost httpPost = new HttpPost(UrlComponent.getGoggle(url));
			MyLog.i(">>>>>>>>>apilicent" + url);
			MyLog.i(">>>>>>>>>content " + content);
			HttpPost httpPost = new HttpPost(url);
			// 添加http头信息
			httpPost.addHeader("Authorization", "your token"); // 认证token
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.addHeader("User-Agent", "imgfornote");
			httpPost.setEntity(new StringEntity(content, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			// 请求超时
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			// 读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					15000);
			httpResponse = client.execute(httpPost);
			// 第2步：使用execute方法发送HTTP
			// httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第3步：使用getEntity方法获得返回结果
				resultJson = EntityUtils.toString(httpResponse.getEntity());
				// 去掉返回结果中的"\r"字符，
			} else {
				Log.i("apiclient", "接口异常："
						+ httpResponse.getStatusLine().getStatusCode());
				resultJson = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 出错会返回空数据，注意处理
			resultJson = "";
		}
		// if (!resultJson.equals("")) {
		// String urlString = UrlComponent.getGoggle(url);
		// int m = urlString.indexOf("api/");
		// if (m != -1) {
		// UrlComponent.setBaseurl(urlString.substring(0, m + 4));
		// }
		// break;
		// } else {
		// if (i == 0) {
		// UrlComponent.setBaseurl(UrlComponent.getBaseUrlList().get(
		// i + 1));
		// } else {
		// UrlComponent.setBaseurl(UrlComponent.getBaseUrlList()
		// .get(0));
		// }
		// }
		// }
		return resultJson; // 返回结果
	}

	// 登录
	public static LoginModel logins(String username, String password) {
		LoginModel loginModel = null;
		String resultJson = "";
		StringBuilder uri = new StringBuilder();
		uri.append(UrlComponent.baseurl + "user/login/");
		uri.append(EncodeUtil.urlEncode(username));
		uri.append("/");
		uri.append(getMD5Str(password) + UrlComponent.token);
		String url = uri.toString();
		for (int i = 0; i < UrlComponent.getBaseUrlList().size(); i++) {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				// 请求超时
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				// 读取超时
				httpClient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 5000);
				HttpGet httpRequest = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpRequest);
				MyLog.i("login uri:" + uri);

				String jsonforString = null;
				String result = null;
				String login = null;
				// 返回json报文
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					jsonforString = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject jsonObject = new JSONObject(jsonforString);
					// result = jsonObject.getString("flag");
					if (httpClient != null) {
						httpClient.getConnectionManager().shutdown();
					}
					/*
					 * loginModel = new
					 * LoginModel(jsonObject.getString("username"), password,
					 * jsonObject.getString("userid"),
					 * jsonObject.getBoolean("flag"),
					 * jsonObject.getString("userpic"));
					 */
					loginModel = new LoginModel(jsonObject.getBoolean("flag"),
							jsonObject.getString("userid"),
							jsonObject.getString("username"),
							jsonObject.getString("nicheng"),
							jsonObject.getString("userpic"),
							jsonObject.getString("emailbind"),
							jsonObject.getString("email"));
					loginModel.setNiCheng(jsonObject.getString("nicheng"));
					resultJson = loginModel.toString();
				} else {
					resultJson = "";
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultJson = "";
			}
			if (!resultJson.equals("")) {
				String urlString = UrlComponent.getGoggle(url);
				int m = urlString.indexOf("api/");
				if (m != -1) {
					UrlComponent.setBaseurl(urlString.substring(0, m + 4));
				}
				break;
			} else {
				if (i == 0) {
					UrlComponent.setBaseurl(UrlComponent.getBaseUrlList().get(
							i + 1));
				} else {
					UrlComponent.setBaseurl(UrlComponent.getBaseUrlList()
							.get(0));
				}
			}
		}

		return loginModel;
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

	/**
	 * 采用POST进行发送数据，然后接收到的字符串返回
	 * 
	 * @param url
	 *            发送的URL地址
	 * @param postData
	 *            发送的数据，采用map进行封装
	 * @param encoding
	 *            接收的数据，采用什么类型进行接收，如"GBK" or "UTF-8"
	 * @return
	 */
	public static String sendPostData(String url, Map<String, Object> postData,
			String encoding) {
		HttpURLConnection con = null;
		String data = null;
		try {
			URL dataUrl = new URL(url);
			con = (HttpURLConnection) dataUrl.openConnection();
			// 设置连接的头部信息
			con.setRequestProperty("accept", "*/*");
			con.setRequestProperty("connection", "Keep-Alive");
			con.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 得到发送的数据
			String post = getDataBySendData(postData);
			// 进行发送数据
			sendConnectinData(con, post);
			// 进行接收数据
			data = getConnectionData(con, encoding);
		} catch (MalformedURLException e) {
			// ex.printStackTrace();
		} catch (IOException e) {
			// ex.printStackTrace();
		} finally {
			con.disconnect();
		}
		return data;
	}

	private static String getDataBySendData(Map<String, Object> map) {
		String data = "";
		Set<Entry<String, Object>> set = map.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			data += entry.getKey() + "=" + entry.getValue() + "&";
		}
		if (data.length() > 0) {
			data = data.substring(0, data.length() - 1);
		}
		return data;
	}

	private static void sendConnectinData(HttpURLConnection con, String postData) {
		try {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			// 进行发送数据
			OutputStream os = con.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.write(postData.getBytes());
			dos.flush();
			dos.close();
		} catch (ProtocolException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	private static String getConnectionData(HttpURLConnection con,
			String encoding) {
		String data = "";
		try {
			InputStream in = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(in, encoding);
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (temp.equals("")) {
					data += "\r\n";
				} else {
					data += temp;
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
			data = "";
		}
		return data;
	}

	// 验证邮箱
	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	// 验证手机号码
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * Extends the size of an array.
	 * 
	 * @return
	 */
	public static String sendPostRequest(String path, String data) {
		try {
			// Send the request
			if (NetUtil.getNetworkState(WeiBaoApplication.getInstance()) == NetUtil.NETWORN_NONE) {
				ToastUtil.showShort(WeiBaoApplication.getInstance(), "请检查您的网络");
				return "";
			}
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream(), "utf-8");

			// write parameters
			writer.write(data);
			writer.flush();
			
			// Get the response
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			String s = null;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}
			writer.close();
			reader.close();
			MyLog.i(answer + "");
			return answer.toString();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "";
	}

}
