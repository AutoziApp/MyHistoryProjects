package com.mapuni.android.dataprovider;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * FileName: JsonHelper.java Description: ����json������
 * 
 * @author ��˼Զ
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-3 ����02:01:18
 */
public class JsonHelper {
	/**
	 * ����json����
	 * 
	 * @param result
	 *            json��ʽ�ַ���
	 * @param node
	 *            json�ļ�������
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> paseJSON(String result, String[] node) {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {

			HashMap<String, Object> hashmap = null;
			JsonReader reader = new JsonReader(new StringReader(result));
			try {
				reader.beginArray(); // ��ʼ��������
				while (reader.hasNext()) {
					reader.beginObject(); // ��ʼ��������
					hashmap = new HashMap<String, Object>();
					while (reader.hasNext()) {
						String tagName = reader.nextName(); // �õ���ֵ���е�key

						for (String str : node) {
							if (tagName.equals(str)) { // keyΪEntNameʱ
								try {
									JsonToken jt = reader.peek();
									if (jt != JsonToken.NULL) {
										if (jt == JsonToken.STRING) {
											hashmap.put(tagName, reader.nextString());
										} else if (jt == JsonToken.NUMBER) {
											hashmap.put(tagName, reader.nextInt() + "");

										}
									} else {

										reader.skipValue();
										hashmap.put(tagName, "");

									}
								} catch (Exception e) {
									reader.skipValue();
									hashmap.put(str, "");

								}
								break;

							}
						}
					}
					reader.endObject();
					list.add(hashmap);
				}
				reader.endArray();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			RecordLog.WriteCaughtEXP(e, "ZXJKActivity");
			e.printStackTrace();
			Log.v("TAG", "���糬ʱ�쳣!");

			return null;
		}
		return list;// ���
	}

	public static ArrayList<HashMap<String, Object>> paseJSON(String jsonStr) {

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {

			HashMap<String, Object> hashmap = null;
			JsonReader reader = new JsonReader(new StringReader(jsonStr));
			try {
				reader.beginArray(); // ��ʼ��������
				while (reader.hasNext()) {
					reader.beginObject(); // ��ʼ��������
					hashmap = new HashMap<String, Object>();
					while (reader.hasNext()) {
						String tagName = reader.nextName(); // �õ���ֵ���е�key
						try {
							JsonToken jt = reader.peek();
							if (jt != JsonToken.NULL) {

								if (jt == JsonToken.STRING) {
									hashmap.put(tagName, reader.nextString());
								} else if (jt == JsonToken.NUMBER) {
									hashmap.put(tagName, reader.nextInt() + "");

								}
							} else {
								reader.skipValue();
								hashmap.put(tagName, "");
							}

						} catch (Exception e) {
							hashmap.put(tagName, "");

						}

					}
					reader.endObject();
					list.add(hashmap);
				}
				reader.endArray();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.v("TAG", "���糬ʱ�쳣!");

			return null;
		}
		return list;// ���
	}

	/**
	 * ��������JSON�����json�� ����node�е�Ԫ��Ӧ��nodeArrayList�ж�Ӧ�ı�ǩ�������Ӧ
	 * 
	 * @param result
	 * @param node
	 * @param nodeArrayList
	 * @return
	 */
	public static HashMap<String, ArrayList<HashMap<String, Object>>> paseJSON(String result, String[] node, ArrayList<String[]> nodeArrayList) {

		HashMap<String, ArrayList<HashMap<String, Object>>> data = new HashMap<String, ArrayList<HashMap<String, Object>>>();
		if (result == null) {
			return data;
		}
		try {
			JSONObject jobject = new JSONObject(result);
			for (int i = 0; i < node.length; i++) {
				JSONArray jarray = jobject.getJSONArray(node[i]);
				ArrayList<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
				String[] nodes = nodeArrayList.get(i);

				for (int j = 0; j < jarray.length(); j++) {
					JSONObject jobj = jarray.getJSONObject(j);
					HashMap<String, Object> map = new HashMap<String, Object>();
					for (int k = 0; k < nodes.length; k++) {
						map.put(nodes[k], jobj.get(nodes[k]) == null || jobj.get(nodes[k]).equals("null") || jobj.get(nodes[k]).equals(null) ? "" : jobj.get(nodes[k]));
					}
					mapList.add(map);
				}

				data.put(node[i], mapList);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;// ���
	}

	public static String listToJSON(ArrayList<HashMap<String, Object>> data) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (HashMap<String, Object> m : data) {
			sb.append("{");
			for (String key : m.keySet()) {
				sb.append(key).append(":'").append(m.get(key).toString()).append("'").append(",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("}").append(",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("]");

		return sb.toString();

	}
}
