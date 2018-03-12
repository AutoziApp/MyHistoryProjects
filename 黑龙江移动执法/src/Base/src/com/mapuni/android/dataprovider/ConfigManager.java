package com.mapuni.android.dataprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.mapuni.android.base.interfaces.PathManager;



public class ConfigManager {
	
	public final static String url = PathManager.SDCARD_CONFIG_LOCAL_PATH;
	
	 /**
	  * typeName 要查询的typename名
	  * itemNames 要的到结果的名
	  */
	public static  String getValue(String typeName,String itemName) {
		String returnValue=null ;
		try {
			FileInputStream fis = new FileInputStream(url);
			ArrayList<HashMap<String, Object>> dataXMLList = XmlHelper.getList(fis, "item");
				for (HashMap<String, Object> dataMap : dataXMLList) {
					if (typeName.equalsIgnoreCase(dataMap.get("typename").toString())) {
						returnValue = String.valueOf(dataMap.get(itemName));
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return returnValue;
	}
	
	/**
	 * Description:修改config.xml中的内容
	 * @param map 要修改的xml标签作为key，要设置的值作为value
	 * @author 王振洋
	 * Create at: 2012-12-7 下午2:43:17
	 */
	public static  void setConfigValues(HashMap<String,String> map){
		SAXReader reader = new SAXReader();
		org.dom4j.Document document;
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		File file = new File(url);
		
		try {
			document = reader.read(file);
			org.dom4j.Element root = document.getRootElement();
			List<org.dom4j.Element> elements = root.elements("item");
			for(org.dom4j.Element ele : elements){
				Set<Map.Entry<String, String>> entrySet = map.entrySet();
				for(Iterator iter = entrySet.iterator();iter.hasNext();){
					Entry<String,String> entry = (Entry<String, String>) iter.next();
					org.dom4j.Element e = ele.element(entry.getKey());
					if(e != null){
						e.setText(entry.getValue());
						iter.remove();
					}
				}
				if(entrySet.isEmpty()){
					break;
				}
			}
			XMLWriter writer = new XMLWriter(new FileWriter(file), format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 获取系统设置信息
	 * @param typeName typename对应的名称
	 * @return 对应的HashMap
	 */
	public  ArrayList<HashMap<String, Object>> getAllCongfigNodes(){
		try {
		FileInputStream fis;
		fis = new FileInputStream(url);
		
		ArrayList<HashMap<String, Object>> dataXMLList = (ArrayList<HashMap<String, Object>>) XmlHelper
					.getList(fis, "item");
			return dataXMLList;
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (DocumentException e) {
		e.printStackTrace();
	}
		return null;
	}
	
}
