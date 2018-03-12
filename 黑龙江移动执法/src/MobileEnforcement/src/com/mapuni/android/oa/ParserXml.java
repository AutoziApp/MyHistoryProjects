/**
 * 
 */
package com.mapuni.android.oa;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author SS
 * @description
 * @param
 * 
 */
public class ParserXml {

	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, String>> parserXml(
			InputStream inputStream) {

		ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();

		/** ����SAXReader���� */
		SAXReader saxReader = new SAXReader();
		try {
			/** ʹ��SAXReader�е�read������ȡ */
			Document document = saxReader.read(inputStream);
			/** ��ȡ���ڵ� */
			Element rootElement = document.getRootElement();

			/** ��ø��ڵ��½ڵ�����Ϊitem�������ӽڵ� */
			List<Element> itemElements = rootElement.elements("item");

			for (Element element : itemElements) {
				HashMap<String, String> map = new HashMap<String, String>();
				/** ��ø��ڵ��½ڵ�ĵ����� */
				Iterator<Element> iter = element.elementIterator();
				while (iter.hasNext()) {
					Element itemElement = iter.next();
					map.put(itemElement.getName(), itemElement.getText());
				}
				maps.add(map);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maps;
	}
}
