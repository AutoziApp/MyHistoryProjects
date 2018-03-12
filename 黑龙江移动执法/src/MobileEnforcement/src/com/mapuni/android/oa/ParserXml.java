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

		/** 创建SAXReader对象 */
		SAXReader saxReader = new SAXReader();
		try {
			/** 使用SAXReader中的read方法读取 */
			Document document = saxReader.read(inputStream);
			/** 获取根节点 */
			Element rootElement = document.getRootElement();

			/** 获得根节点下节点名称为item的所有子节点 */
			List<Element> itemElements = rootElement.elements("item");

			for (Element element : itemElements) {
				HashMap<String, String> map = new HashMap<String, String>();
				/** 获得根节点下节点的迭代器 */
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
