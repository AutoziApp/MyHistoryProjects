/**
 * 
 */
package com.mapuni.android.gis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ss 地图中显示区县的数据集合
 */
public class MapCountyData {

	private ArrayList<HashMap<String, Object>> gridListMainData;
	private HashMap<String, Object> gridMap;

	public ArrayList<HashMap<String, Object>> getCountyData() {

		gridListMainData = new ArrayList<HashMap<String, Object>>();
		gridMap = new HashMap<String, Object>();
		/** 鲅鱼圈区 */
		gridMap.put("jd", "122.193379108");
		gridMap.put("wd", "40.3565174930001");
		gridMap.put("contextStr", "鲅鱼圈区");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);
		/** 白塔区 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.209466099");
		gridMap.put("wd", "41.3007454180001");
		gridMap.put("contextStr", "白塔区");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);
		/** 北票市 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "120.944742239");
		gridMap.put("wd", "42.2742602540001");
		gridMap.put("contextStr", "北票市");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);
		/** 北镇市 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "121.78293083");
		gridMap.put("wd", "41.7987862890001");
		gridMap.put("contextStr", "北镇市");
		gridMap.put("agency", "");
		gridMap.put("layer", "2");
		gridListMainData.add(gridMap);
		/** 本溪满族自治县 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.925671053");
		gridMap.put("wd", "41.5594510190001");
		gridMap.put("contextStr", "本溪满族自治县");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 昌图县 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.803921149");
		gridMap.put("wd", "43.478123843");
		gridMap.put("contextStr", "昌图县");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 长海县 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "122.559587826");
		gridMap.put("wd", "39.2532264440001");
		gridMap.put("contextStr", "长海县");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 朝阳县 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "120.28341104");
		gridMap.put("wd", "41.905723863");
		gridMap.put("contextStr", "朝阳县");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 大东区 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.478402046");
		gridMap.put("wd", "41.900402979");
		gridMap.put("contextStr", "大东区");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 大石桥市 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "122.463899299");
		gridMap.put("wd", "40.937594855");
		gridMap.put("contextStr", "大石桥市");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 大洼县 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "122.202681573");
		gridMap.put("wd", "40.7218324600001");
		gridMap.put("contextStr", "大洼县");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 灯塔市 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.114404811");
		gridMap.put("wd", "41.6099582730001");
		gridMap.put("contextStr", "灯塔市");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 调兵山市 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.658130999");
		gridMap.put("wd", "42.5223705050001");
		gridMap.put("contextStr", "调兵山市");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 东港市 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "124.154938248");
		gridMap.put("wd", "40.1818417060001");
		gridMap.put("contextStr", "东港市");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 东陵区 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "123.70722497");
		gridMap.put("wd", "41.9546234490001");
		gridMap.put("contextStr", "东陵区");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		/** 东洲区 */
		gridMap = new HashMap<String, Object>();
		gridMap.put("jd", "124.118224708");
		gridMap.put("wd", "42.0674089500001");
		gridMap.put("contextStr", "东洲区");
		gridMap.put("agency", "");
		gridMap.put("layer", "4");
		gridListMainData.add(gridMap);

		return gridListMainData;

	}
}
