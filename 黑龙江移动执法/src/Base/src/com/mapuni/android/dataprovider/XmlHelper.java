package com.mapuni.android.dataprovider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.mapuni.android.base.util.OtherTools;

public class XmlHelper {

	public static final String TAG = "XmlHelper";

	public static final String QUERY_HINT = "queryHint";
	public static final String QUERY_EDIT_TEXT = "queryEditText";

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"; // xml�ļ�ͷ
	public static final String NODE_ROOT = "list"; // ���ڵ�
	public static final String NODE_LEVEL1 = "item"; // ��һ���ڵ�

	public static final String ADD_BY_NODE_KEY = "nodeKey";
	public static final String ADD_BY_NODE_VAlUE = "nodeValue";

	/**
	 * ����XML����Դ�ļ���ȡ�б�����
	 * 
	 * @param inputStream
	 *            Դ�ļ�������
	 * @return �б�����
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static ArrayList<HashMap<String, Object>> getList(InputStream inputStream) throws IOException, DocumentException {
		return getList(inputStream, "item");
	}

	/**
	 * ����XML����Դ�ļ���ȡ�б�����
	 * 
	 * @param inputStream
	 *            Դ�ļ�������
	 * @param nodeName
	 *            �ڵ����� XML����Դ�ļ�
	 * @return �б�����
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> getList(InputStream inputStream, String nodeName) throws IOException, DocumentException {
		SAXReader reader = new SAXReader();
		Document document = null;
		Element root = null;
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		document = reader.read(inputStream);
		root = document.getRootElement();
		Iterator<Element> iter = (Iterator<Element>) root.elementIterator(nodeName);
		HashMap<String, Object> map;
		while (iter.hasNext()) {
			map = new HashMap<String, Object>();
			Element element = iter.next();
			List<Element> elements = (List<Element>) element.elements();

			for (Element childElement : elements) {
				String elementName = childElement.getName();
				String elementValue = childElement.getText();
				map.put(elementName, elementValue);
			}
			list.add(map);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getListStyleByName(String ListstyleName, InputStream ListstyleStream) {// ��ѯliststyle����hashmap����
		SAXReader sr = new SAXReader();// ִ�м�������ѯ
		HashMap<String, Object> m = new HashMap<String, Object>();
		;
		Document doc;
		try {
			String xpath = "/list/item[name='" + ListstyleName + "']/*";
			doc = sr.read(ListstyleStream);

			List<Element> nodes = (List<Element>) doc.selectNodes(xpath);
			for (int i = 0; i < nodes.size(); i++) {

				String name = nodes.get(i).getName();
				String text = nodes.get(i).getText();
				m.put(name, text);

			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return m;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> getStyleByName(String styleName, InputStream styleStream) {// ��ѯquerystyle����ArrayList����
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SAXReader sr = new SAXReader();// ִ�м�������ѯ
		HashMap<String, Object> m;
		Document doc;
		try {
			String xpath = "/list/item[name='" + styleName + "']/*";
			doc = sr.read(styleStream);

			List<Element> nodes = doc.selectNodes(xpath);
			for (int i = 0; i < nodes.size(); i++) {
				m = new HashMap<String, Object>();
				String name = nodes.get(i).getName();
				String text = nodes.get(i).getText();
				String style = nodes.get(i).attributeValue("style");
				String type = nodes.get(i).attributeValue("type");
				String dataresource = nodes.get(i).attributeValue("datasource");
				String info = nodes.get(i).attributeValue("info");
				String unit = nodes.get(i).attributeValue("unit");
				String finnaly_modify = nodes.get(i).attributeValue("finnaly_modify");
				String sql = nodes.get(i).attributeValue("sql");
				String notnull = nodes.get(i).attributeValue("notnull");
				String telvalidates = nodes.get(i).attributeValue("telvalidates");
				String number = nodes.get(i).attributeValue("number");
				String email = nodes.get(i).attributeValue("email");
				String show = nodes.get(i).attributeValue("show");
				String dbsavekey = nodes.get(i).attributeValue("dbsavekey");
				String identificationcard = nodes.get(i).attributeValue("identificationcard");
				m.put("style", style);
				m.put("datasource", dataresource);
				m.put("info", info);
				m.put("unit", unit);
				m.put("type", type);
				m.put("finnaly_modify", finnaly_modify);
				m.put(QUERY_HINT, text);
				m.put(QUERY_EDIT_TEXT, name);
				m.put("sql", sql);
				m.put("notnull", notnull);
				m.put("telvalidates", telvalidates);
				m.put("number", number);
				m.put("email", email);
				m.put("show", show);
				m.put("dbsavekey", dbsavekey);
				m.put("identificationcard", identificationcard);

				list.add(m);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ����XML����Դ�ļ�����ϣ���еļ�ֵ�Բ�ѯ������ѯ�����������б�����
	 * 
	 * @param dataSourceFile
	 *            XML����Դ�ļ�
	 * @param map
	 *            ��ֵ�Բ�ѯ����
	 * @return �����������б�����
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> getList(InputStream dataFileStream, Map<String, Object> map) {
		int length = map.size();
		int flag = 0;
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			Object key = entry.getKey();

			Object value = entry.getValue();
			flag++;
			if (flag < length)
				sb.append(key.toString() + "=" + "'" + value.toString() + "'" + " and ");
			else
				sb.append(key.toString() + "=" + "'" + value.toString() + "'");
		}// ƴ�Ӽ������
		SAXReader sr = new SAXReader();// ִ�м�������ѯ
		HashMap<String, Object> m;
		Document doc;
		try {
			String nodenum = "/list/item[1]/*";
			String xpath = "/list/item[" + sb + "]/*";
			doc = sr.read(dataFileStream);
			List<Element> children = (List<Element>) doc.selectNodes(nodenum);// item���ж��ٸ���
			List<Element> nodes = (List<Element>) doc.selectNodes(xpath);
			int eachlength = children.size();
			int ini = 0;
			for (int i = 0; i < nodes.size() / eachlength; i++) {
				m = new HashMap<String, Object>();
				for (int j = ini; j < ini + eachlength; j++) {

					String name = nodes.get(j).getName();
					String text = nodes.get(j).getText();

					m.put(name, text);
				}
				list.add(m);
				ini += eachlength;

			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 
	 * @param dataSourceFile
	 *            XML����Դ�ļ�
	 * @param map
	 *            ��ѯ������ֵ��
	 * @return ��������
	 */
	public static Map<String, Object> getDetailed(InputStream dataInputStream, Map<String, Object> map) {
		List<HashMap<String, Object>> list = getList(dataInputStream, map);
		if (list.size() > 0) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	// ------------------------------ @author by xugf
	// below------------------------------

	/**
	 * �� xml �ļ�����ȡ����ȡnodeFather�ڵ��µ������ӽڵ����ƺͽڵ�����
	 * 
	 * @param inputStream
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getDataFromXmlStream(InputStream inputStream, String nodeFather) {
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(inputStream, "utf-8");
			return getDataFromXml(xmlParser, nodeFather);
		} catch (XmlPullParserException e) {
			Log.e(TAG, "getDataFromXmlStream method error : \n" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * ��ƴ�ӵı�׼ xml �ַ����ж�ȡ����ȡnodeFather�ڵ��µ������ӽڵ����ƺͽڵ�����
	 * 
	 * @param xml
	 * @param nodeFather
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> getDataFromXmlString(String xml, String nodeFather) {
		try {
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(xml.getBytes());
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(tInputStringStream, "UTF-8");
			return getDataFromXml(parser, nodeFather);
		} catch (XmlPullParserException e) {
			Log.e(TAG, "getDataFromXmlString method error : \n" + e.getMessage());
		}
		return new ArrayList<HashMap<String, Object>>();
	}

	/**
	 * ��xml�ж�ȡ���ݣ���ȡnodeFather�ڵ��µ������ӽڵ����ƺͽڵ����� ������� addByNode ֵ��ѡ���Ե��������
	 * 
	 * @param xrp
	 *            xml����Դ
	 * @param nodeFather
	 *            �м����ݵĸ��ڵ�
	 * @param addByNode
	 *            ��ǩ�����ƺ�ֵ��XmlHelper.ADD_BY_NODE_KEY, XmlHelper.ADD_BY_NODE_VAlUE
	 *            �����ü�ֵ�������ִ�Сд��
	 * @return default size 0 not null
	 */
	public static ArrayList<HashMap<String, Object>> getDataFromXml(XmlPullParser xpp, String nodeFather, HashMap<String, Object>... addByNode) {
		/* �м����ݴ�� */
		ArrayList<HashMap<String, Object>> data = null;
		/* �м����ݴ�� */
		HashMap<String, Object> dataRow = null;

		boolean addRow = false; // �Ƿ���ӵ�HashMap��
		boolean addlist = false; // �Ƿ���ӵ�ArrayList��
		try {
			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (xpp.getEventType()) {
				case XmlPullParser.START_DOCUMENT:
					data = new ArrayList<HashMap<String, Object>>();
					break;
				case XmlPullParser.START_TAG:
					String tagName = xpp.getName();
					if (nodeFather.equals(tagName)) {
						addRow = false;
						addlist = true;
						dataRow = new HashMap<String, Object>();
					}
					if (addRow) {
						/* ��ȡ�ڵ����ݣ�����ŵ�HashMap�� */
						String tagValue = xpp.nextText();
						dataRow.put(tagName, tagValue);
					}
					if (nodeFather.equals(tagName)) {
						addRow = true;
					}
					break;
				case XmlPullParser.END_TAG:
					if (nodeFather.equals(xpp.getName()) && addlist) {
						if (addByNode.length > 0) {
							/* ���ݽڵ���ѡ���������� */
							String nodeKey = String.valueOf(addByNode[0].get(ADD_BY_NODE_KEY));
							String nodeValue = String.valueOf(addByNode[0].get(ADD_BY_NODE_VAlUE));

							if (dataRow.containsKey(nodeKey) && nodeValue.equals(dataRow.get(nodeKey))) {
								data.add(dataRow);
							}
						} else {
							/* Ĭ������������� */
							data.add(dataRow);
						}
					}
					break;
				}
				xpp.next();
			}
		} catch (XmlPullParserException e) {
			Log.e(TAG, "getDataFromXml method error : \n" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "getDataFromXml method error : \n" + e.getMessage());
		}
		Log.v(TAG, "getDataFromXml data size : " + data.size());
		return data;
	}

	/**
	 * ��װ�ɱ�׼ xml �����ַ���
	 * 
	 * @param data
	 * @return
	 */
	public static String getXmlFromData(ArrayList<HashMap<String, Object>> data) {
		StringBuilder xml = new StringBuilder(XML_HEADER);
		xml.append("<" + NODE_ROOT + ">");
		xml.append(getXmlItemFromItemData(data, NODE_LEVEL1));
		xml.append("</" + NODE_ROOT + ">");
		return xml.toString();
	}

	/**
	 * ��װ xml ��ǩ �м�����
	 * 
	 * @param data
	 * @return
	 */
	public static String getXmlItemFromItemData(ArrayList<HashMap<String, Object>> data, String item) {
		StringBuilder xmlItem = new StringBuilder();
		for (HashMap<String, Object> mapItem : data) {
			xmlItem.append("<" + item + ">");
			Iterator<Map.Entry<String, Object>> iterator = mapItem.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> rowData = iterator.next();
				xmlItem.append("<" + rowData.getKey() + ">" + rowData.getValue() + "</" + rowData.getKey() + ">");
			}
			xmlItem.append("</" + item + ">");
		}
		return xmlItem.toString();
	}

	/**
	 * �������ݵ�ͬʱ�����ݽ���¼������
	 * 
	 * @param updateOrFetchAllData
	 *            true�������������ݣ�false������ȫ������
	 * @param table
	 *            Ҫ�������ݵı���
	 * @param inputStream
	 *            �������ַ���
	 * @param nodeFather
	 *            Ҫ�������ݵĸ��ڵ�
	 * @return ������������
	 * @throws XmlPullParserException
	 */
	public static int insertTableDataWhenXmlPullParser(String table, InputStream inputStream, String nodeFather, boolean updateOrFetchAllData) {

		/* �м����ݴ�� */
		ArrayList<HashMap<String, Object>> data = null;
		/* �м����ݴ�� */
		HashMap<String, Object> dataRow = null;

		int count = 0;
		try {
			XmlPullParser xpp = Xml.newPullParser();
			xpp.setInput(inputStream, "utf-8");

			int maxInsertNum = 150; // һ�����������ݿ������

			boolean addRow = false; // �Ƿ���ӵ�HashMap��
			boolean addlist = false; // �Ƿ���ӵ�ArrayList��

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (xpp.getEventType()) {
				case XmlPullParser.START_DOCUMENT:
					data = new ArrayList<HashMap<String, Object>>();
					break;
				case XmlPullParser.START_TAG:
					String tagName = xpp.getName();
					if (nodeFather.equals(tagName)) {
						addRow = false;
						addlist = true;
						dataRow = new HashMap<String, Object>();
					}
					if (addRow) {
						/* ��ȡ�ڵ����ݣ�����ŵ�HashMap�� */
						String tagValue = xpp.nextText();
						dataRow.put(tagName, tagValue);
					}
					if (nodeFather.equals(tagName)) {
						addRow = true;
					}
					break;
				case XmlPullParser.END_TAG:
					if (nodeFather.equals(xpp.getName()) && addlist) {
						dataRow.remove("num");
						data.add(dataRow);
						if (data.size() == maxInsertNum) {
							count += synchronizeOneTableUpdateOrFetchAll(data, table, updateOrFetchAllData);
						}
					}
					break;
				}
				xpp.next();
			}
			if (data.size() > 0) {
				count += synchronizeOneTableUpdateOrFetchAll(data, table, updateOrFetchAllData);
			}
		} catch (XmlPullParserException e) {
			OtherTools.showLog("---XmlPullParserException---");
			return 0;
		} catch (IOException e) {
			OtherTools.showLog("---IOException---");
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return count;
		}
		OtherTools.showLog("insertTableDataWhenXmlPullParser data size : " + count);
		return count;
	}

	private static int synchronizeOneTableUpdateOrFetchAll(ArrayList<HashMap<String, Object>> data, String table, boolean updateOrFetchAllData) throws Exception {
		int count = 0;
		if (SQLiteDataProvider.synchronizeOneTableUpdateOrFetchAll(data, table, updateOrFetchAllData)) {
			count += data.size();
			HashMap<String, Object> timeData = data.get(data.size() - 1);
			String updateTime = (String) timeData.get("UpdateTime");
			if (updateTime == null || "".equals(updateTime)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				updateTime = sdf.format(new Date());
			}

			if (synchronizeLogTableUpdateState(table, updateTime)) {
				OtherTools.showLog("�Ѹ������ݿ�����ʱ��-----" + updateTime);
			}
		} else {
			throw new Exception("���ݿ�����쳣,����ͣ����");
		}
		data.clear();
		return count;
	}

	/**
	 * ��������ͬ��ʱ���
	 * 
	 * @param updateTable
	 *            ����
	 * @param updateTime
	 *            ����ʱ��
	 * @return trueΪ���³ɹ�,falseΪʧ��
	 */
	private static boolean synchronizeLogTableUpdateState(String updateTable, String updateTime) {
		boolean flag = false;
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("TABLENAME", updateTable);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("SYNCHRONIZE_LOG", conditions);
		ContentValues updateValues = new ContentValues();
		updateValues.put("UPDATETIME", updateTime);
		if (data != null && data.size() > 0) {// ���ڣ�ִ�и��²���
			String[] whereArgs = { updateTable };
			try {
				if (SqliteUtil.getInstance().update("SYNCHRONIZE_LOG", updateValues, "TABLENAME=?", whereArgs) > 0) {
					flag = true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {// �����ڣ�ִ�в������
			int id = SqliteUtil.getInstance().queryMaxValue("id", "SYNCHRONIZE_LOG");
			updateValues.put("ID", id + 1);
			updateValues.put("TABLENAME", updateTable);
			if (SqliteUtil.getInstance().insert(updateValues, "SYNCHRONIZE_LOG") > 0) {
				flag = true;
			}
		}
		return flag;
	}

	public static void write(Context context, String url, ArrayList<HashMap<String, Object>> data) {
		try {
			String xmlStr = XmlHelper.getXmlFromData(data);
			String xml = xmlStr;
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				String fileName = "/config.xml";
				File f = new File(url + fileName);
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f));
				osw.write(xml);
				osw.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ������Ƶ�����ļ�����ȡ��Ƶ��Ϣ fileName ��ʾ�ļ�·��
	 * 
	 * @param fileName
	 * @return List(��������ţ���)
	 */
	public static List<HashMap<String, String>> getConfigInfoFromXml(InputStream in) {
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// InputStream in = this.getResources().getAssets().open(fileName);
			org.w3c.dom.Document doc = builder.parse(in);
			// ��ýڵ��б�
			NodeList nl = doc.getElementsByTagName("item");
			for (int i = 0; i < nl.getLength(); i++) {
				item = new HashMap<String, String>();
				String qymc = doc.getElementsByTagName("qymc").item(i).getFirstChild().getNodeValue();
				String id = doc.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();
				String ip = doc.getElementsByTagName("ip").item(i).getFirstChild().getNodeValue();
				String user = doc.getElementsByTagName("user").item(i).getFirstChild().getNodeValue();
				String pass = doc.getElementsByTagName("pass").item(i).getFirstChild().getNodeValue();
				String port = doc.getElementsByTagName("port").item(i).getFirstChild().getNodeValue();
				String status = doc.getElementsByTagName("status").item(i).getFirstChild().getNodeValue();
				String entid = doc.getElementsByTagName("entid").item(i).getFirstChild().getNodeValue();
				String channel = doc.getElementsByTagName("channel").item(i).getFirstChild().getNodeValue();

				item.put("qymc", qymc);
				item.put("ip", ip);
				item.put("id", id);
				item.put("user", user);
				item.put("pass", pass);
				item.put("port", port);
				item.put("status", status);
				item.put("entid", entid);
				item.put("channel", channel);

				dataList.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	/**
	 * ��ѯXML��ȡ��Ϣ
	 */
	public static ArrayList<HashMap<String, Object>> getInfoFromXml(InputStream is, String listName) {
		// ��ѯquerystyle����ArrayList����
		SAXReader sr = new SAXReader();// ִ�м�������ѯ
		HashMap<String, Object> map = null;
		Document doc = null;
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			String xpath = "/list/item[name='" + listName + "']/*";
			doc = sr.read(is);
			List<Element> nodes = doc.selectNodes(xpath);
			for (int i = 0; i < nodes.size(); i++) {
				map = new HashMap<String, Object>();
				String text = nodes.get(i).getText();
				String id = nodes.get(i).attributeValue("id");
				String condition = nodes.get(i).attributeValue("condition");
				String style = nodes.get(i).attributeValue("style");
				map.put("text", text);
				map.put("id", id);
				map.put("condition", condition);
				map.put("style", style);
				if (i != 0) {
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Description:
	 * 
	 * @param context
	 * @param fileName
	 * @param ItemName
	 * @return
	 * @author Administrator Create at: 2012-12-21 ����9:34:07
	 */
	public static ArrayList<HashMap<String, Object>> getMenuFromXml(Context context, String ItemName, String rootelement, String childelement, InputStream inptuStream) {
		// ��ѯquerystyle����ArrayList����
		SAXReader reader = new SAXReader();
		org.dom4j.Document document;
		ItemName.toUpperCase();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		try {
			InputStream is = inptuStream;
			if (is == null)
				return null;
			document = reader.read(is);
			Element root = document.getRootElement();
			// List node = root.selectNodes(rootelement);

			List<Element> elements = root.elements(rootelement);
			Log.v("SSS", elements.size() + "");
			if (elements.size() == 0) {
				Iterator<Element> iter = (Iterator<Element>) root.elementIterator("item");
				HashMap<String, Object> map;
				while (iter.hasNext()) {
					map = new HashMap<String, Object>();
					Element element = iter.next();
					List<Element> elementss = (List<Element>) element.elements();

					for (Element childElement : elementss) {
						String elementName = childElement.getName();
						String elementValue = childElement.getText();
						map.put(elementName, elementValue);
					}
					list.add(map);
				}
			} else {
				for (Element ele : elements) {
					String name = ele.attributeValue("name");
					if (ItemName.equals(name)) {
						List<Element> menus = ele.elements(childelement);
						for (Element e : menus) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							Iterator iter = e.elementIterator();
							while (iter.hasNext()) {
								Element el = (Element) iter.next();
								map.put(el.getName(), el.getText());
							}
							list.add(map);
						}
						break;
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return list;
	}

}
