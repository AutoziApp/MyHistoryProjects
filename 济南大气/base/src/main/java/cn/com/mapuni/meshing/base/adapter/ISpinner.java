package cn.com.mapuni.meshing.base.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import cn.com.mapuni.meshing.base.dataprovider.SqliteUtil;

/**
 * FileName: ISpinner.java Description: 三级或二级连动Spinner
 * 
 * @author Administrator
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-28 下午4:52:22
 */
public class ISpinner {
	/**
	 * FileName: IISpinner.SpinnerInterface Description: ISpinner
	 * 的内部接口，用于刷新listview数据
	 */

	public interface ISpinnerInterface {
		/**
		 * Description: 回调方法，当点击Spinner某一项时，调用此方法
		 * 
		 * @param filterMap
		 *            根据点击Spinner的项，生成的过滤器
		 */
		public void flushData(HashMap<String, Object> filterMap);
	}

	/** 第一个 Spinner的数据集合 */
	private List<HashMap<String, Object>> one;
	/** 填充第一个Spinner的Key和Code */
	private List<String> oneName, oneRegionCode;

	/** 第二个 Spinner的数据集合 */
	private List<HashMap<String, Object>> two;
	/** 填充第二个Spinner的Key和Code */
	private List<String> twoName, twoRegionCode;

	/** 第三个 Spinner的数据集合 */
	private List<HashMap<String, Object>> three;
	/** 填充第三个Spinner的Key和Code */
	private List<String> threeName, threeRegionCode;
	/** 第一个Spinner */
	private Spinner sOne;
	/** 第二个Spinner */
	private Spinner sTwo;
	/** 第三个Spinner */
	private Spinner sThree;
	private Context context;
	/** 三级连动表名 */
	private String tableName;
	private SqliteUtil su;
	/** 连动数，如果三级为3，二级为2 */
	private int isLinkage;
	/** 条件过滤器 */
	HashMap<String, Object> filterMap;
	/** 内部接口实例 */
	ISpinner.ISpinnerInterface ii;
	/** 要显示在Spinner里对应数据库的列名 */
	private String oneSpinnerKey[];
	/** 要显示在Spinner里对应数据库的列名 二级连动时使用 */
	private String twoSpinnerKey[];
	/** 二极连动时第三个Spinner的数据集合 */
	private List<HashMap<String, Object>> twoLinkage;

	/**
	 * 构造方法
	 * 
	 * @param oneSpinnerDataList
	 *            第一个Spinner数据集合
	 * @param tableName
	 *            数据库表名
	 * @param context
	 *            上下文
	 * @param isLinkage
	 *            连动数，三级为3，二级为2
	 * @param twoLinkage
	 *            如果为三级连动，此参数为null,如是二级连动，此参数为第三个Spinner的数据集合
	 * @param ii
	 *            为此类内部接口实例
	 * @param oneSpinnerKey
	 *            要显示在Spinner里对应数据库的列名，如{"regioncode","regionname"} 必须将code放在第一
	 * @param twoSpinnerKey
	 *            二级连动时要显示在第三个Spinner里对应数据库的列名，如{"attenname"}
	 */
	public ISpinner(List<HashMap<String, Object>> oneSpinnerDataList,
			String tableName, Context context, int isLinkage,
			List<HashMap<String, Object>> twoLinkage,
			ISpinner.ISpinnerInterface ii, String[] oneSpinnerKey,
			String[] twoSpinnerKey) {
		this.oneSpinnerKey = oneSpinnerKey;
		this.twoSpinnerKey = twoSpinnerKey;
		this.tableName = tableName;
		this.context = context;
		this.one = oneSpinnerDataList;
		this.twoLinkage = twoLinkage;
		this.isLinkage = isLinkage;
		this.ii = ii;
		su = SqliteUtil.getInstance();
		sOne = new Spinner(context);
		sTwo = new Spinner(context);
		sThree = new Spinner(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		sOne.setLayoutParams(params);
		sTwo.setLayoutParams(params);
		sThree.setLayoutParams(params);
		oneName = new ArrayList<String>();
		oneName.add("请选择");
		oneRegionCode = new ArrayList<String>();
		oneRegionCode.add("");
		filterMap = new HashMap<String, Object>();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LinearLayout returnSpinnerLayout() {
		LinearLayout lin = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lin.setLayoutParams(params);
		if (tableName.equals("Region")) {
			for (int i = 0; i < one.size(); i++) {
				for (String key : one.get(i).keySet()) {
					if (key.equals(oneSpinnerKey[1])) {
						oneName.add(String.valueOf(one.get(i).get(key)));
					}
					if (key.equals(oneSpinnerKey[0])) {
						oneRegionCode.add(String.valueOf(one.get(i).get(key)));
					}
				}
			}
		}
		if (tableName.equals("pc_departmentinfo")) {
			for (int i = 0; i < one.size(); i++) {
				for (String key : one.get(i).keySet()) {
					if (key.equals(oneSpinnerKey[1])) {
						oneName.add(String.valueOf(one.get(i).get(key)));
					}
					if (key.equals(oneSpinnerKey[0])) {
						oneRegionCode.add(String.valueOf(one.get(i).get(key)));
					}
				}
			}
		}
		
		ArrayAdapter oneAdapter = new ArrayAdapter(context,
				android.R.layout.simple_spinner_item, oneName);
		oneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sOne.setAdapter(oneAdapter);
		//sOne.set
		twoName = new ArrayList<String>();
		twoRegionCode = new ArrayList<String>();
		final ArrayAdapter twoAdapter = new ArrayAdapter(context,
				android.R.layout.simple_spinner_item, twoName);
		twoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sTwo.setAdapter(twoAdapter);
		threeName = new ArrayList<String>();
		threeRegionCode = new ArrayList<String>();
		if (isLinkage == 2) {
			for (int i = 0; i < twoLinkage.size(); i++) {
				for (String key : twoLinkage.get(i).keySet()) {
					if (key.equals("attenname")) {
						threeName.add(String
								.valueOf(twoLinkage.get(i).get(key)));
					}
				}
			}
		}

		final ArrayAdapter threeAdapter = new ArrayAdapter(context,
				android.R.layout.simple_spinner_item, threeName);
		sThree.setAdapter(threeAdapter);
		threeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sOne.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				twoName.clear();
				twoName.add("请选择");
				twoRegionCode.clear();
				twoRegionCode.add("");
				if (tableName.equals("Region")) {
					two = su.queryBySqlReturnArrayListHashMap("select * from region where parentcode = '"
							+ oneRegionCode.get(position) + "'");
				}
				if (tableName.equals("pc_departmentinfo")) {
					two = su.queryBySqlReturnArrayListHashMap("select * from pc_departmentinfo where depparentid = '"
							+ oneRegionCode.get(position) + "'");
				}
				for (int i = 0; i < two.size(); i++) {
					for (String key : two.get(i).keySet()) {
						if (key.equals(oneSpinnerKey[1])) {
							twoName.add(String.valueOf(two.get(i).get(key)));
						}
						if (key.equals(oneSpinnerKey[0])) {
							twoRegionCode.add(String.valueOf(two.get(i)
									.get(key)));
						}
					}
				}
				twoAdapter.notifyDataSetChanged();
				// filterMap.put("regionname", oneName.get(position));
				filterMap.put(oneSpinnerKey[0], oneRegionCode.get(position));
				// if (tableName.equals("pc_departmentinfo")) {
				// twoName.clear();
				// twoName.add("请选择");
				// twoRegionCode.clear();
				// twoRegionCode.add("");
				// two =
				// su.queryBySqlReturnArrayListHashMap("select depid,depname from pc_departmentinfo where depparentid = '"
				// + oneRegionCode.get(position) + "'");
				// for (int i = 0; i < two.size(); i++) {
				// for (String key : two.get(i).keySet()) {
				// if (key.equals("depname")) {
				// twoName.add(String.valueOf(two.get(i).get(key)));
				// }
				// if (key.equals("depid")) {
				// twoRegionCode.add(String.valueOf(two.get(i)
				// .get(key)));
				// }
				// }
				// }
				// twoAdapter.notifyDataSetChanged();
				// filterMap.put("depname", oneName.get(position));
				// filterMap.put("depid", oneRegionCode.get(position));
				// }
				ii.flushData(filterMap);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		sTwo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unused")
				HashMap<String, Object> threeLinkage = new HashMap<String, Object>();
				if (isLinkage == 3) {
					threeLinkage = threeLinkage(position, threeAdapter);
					ii.flushData(filterMap);
				}
				if (isLinkage == 2) {
					filterMap.put(oneSpinnerKey[0], twoRegionCode.get(position));
					ii.flushData(filterMap);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		sThree.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (isLinkage == 2) {
					filterMap.put(twoSpinnerKey[0], threeName.get(position));
				}
				if (isLinkage == 3) {
					filterMap.put(oneSpinnerKey[0],
							threeRegionCode.get(position));
				}
				ii.flushData(filterMap);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		lin.addView(sOne);
		lin.addView(sTwo);
		lin.addView(sThree);
		return lin;
	}

	private HashMap<String, Object> threeLinkage(int position,
			@SuppressWarnings("rawtypes") ArrayAdapter adapter) {
		threeName.clear();
		threeName.add("请选择");
		threeRegionCode.clear();
		threeRegionCode.add("");
		if (tableName.equals("Region")) {
			three = su
					.queryBySqlReturnArrayListHashMap("select * from region where parentcode = '"
							+ twoRegionCode.get(position) + "'");
		}
		if (tableName.equals("pc_departmentinfo")) {
			three = su
					.queryBySqlReturnArrayListHashMap("select * from pc_departmentinfo where depparentid = '"
							+ twoRegionCode.get(position) + "'");
		}
		for (int i = 0; i < three.size(); i++) {
			for (String key : three.get(i).keySet()) {
				if (key.equals(oneSpinnerKey[1])) {
					threeName.add(String.valueOf(three.get(i).get(key)));
				}
				if (key.equals(oneSpinnerKey[0])) {
					threeRegionCode.add(String.valueOf(three.get(i).get(key)));
				}
			}
		}
		adapter.notifyDataSetChanged();
		// filterMap.put("regionname", twoName.get(position));
		filterMap.put(oneSpinnerKey[0], twoRegionCode.get(position));
		// if (tableName.equals("pc_departmentinfo")) {
		// threeName.clear();
		// threeName.add("请选择");
		// threeRegionCode.clear();
		// threeRegionCode.add("");
		// three =
		// su.queryBySqlReturnArrayListHashMap("select depid,depname from pc_departmentinfo where depparentid = '"
		// + twoRegionCode.get(position) + "'");
		// for (int i = 0; i < three.size(); i++) {
		// for (String key : three.get(i).keySet()) {
		// if (key.equals("depname")) {
		// threeName.add(String.valueOf(three.get(i).get(key)));
		// }
		// if (key.equals("depid")) {
		// threeRegionCode.add(String.valueOf(three.get(i)
		// .get(key)));
		// }
		// }
		// }
		// adapter.notifyDataSetChanged();
		// filterMap.put("depname", twoName.get(position));
		// filterMap.put("depid", twoRegionCode.get(position));
		// }
		return filterMap;
	}

}
