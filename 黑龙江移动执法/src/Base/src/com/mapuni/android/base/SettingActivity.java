package com.mapuni.android.base;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * FileName: SettingActivity.java
 * Description:设置页面
 * @author 王振洋
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2012-12-4 上午9:17:52
 */
public class SettingActivity extends BaseActivity {

	//SharedPreference中存变量对应的key
	private  final String SYNCDATA = "sync_data"; //同步数据
	private  final String STATUS_BAR_TIPS = "status_bar_tips"; //状态栏任务提示
	private  final String VOICE = "voice"; //语音播报
	private  final String AUTOLOGIN = "auto_login"; //自动登录
//	private static final String REFRESHDATA = "refresh_data"; //同步数据
	private  final String MENUSTYLE = "menustyle"; //菜单样式
	private  final String TEXTSIZE = "textsize"; //列表字体大小
	private  final String LISTLOADNUM = "listloadnum"; //列表一次加载数据条数
	private  final String SYNCDATANUM = "syncdatanum"; //同步数据一次请求条数
	private  final String AUTOASYNCDURATION = "autosyncduration"; //自动同步时间间隔
	private  final String AUTOCLEANSYSGRABAGE = "autosysgrabage";//自动清理系统垃圾
	
	//九宫格对应常量
	private static final int STYLE_GRID = 1;	
	//滑动切换对应常量
	private static final int STYLE_TABS = 2;
	
	//各项设置项对应的选项
	private  final String[] menuStyles = {"九宫格","滑动切换"};//菜单样式
	private  final String[] listLoadSelections = {"30", "50", "100"};//列表一次加载数据条数
	private  final String[] requestNums = {"1000", "2000", "3000", "5000"};//同步数据一次请求条数
	private  final String[] requestDurations = {"5", "10", "30", "60"};//请求时间间隔
	private  final String[] cleanDurations = {"7","15","21","30"};//清理系统垃圾的时间周期
	private  final String[] listTextSize = {"22", "24", "26", "28"};//列表字体大小
	private  final String[] listTextSizeName = {"极小", "小", "中", "大"};//列表字体大小选项
	
	String[] names1 = {"是否自动下载数据", "是否退出后状态提示", "语音播报", "自动登录"};//第一个ListView显示的内容
	String[] keys1 = {SYNCDATA, STATUS_BAR_TIPS, VOICE, AUTOLOGIN};//第一个ListView设置项的key数组
	String[] names2 = {"菜单样式", /*"任务管理样式",*/ "列表字体大小"};//第二个ListView显示的内容
	String[] keys2 = {MENUSTYLE, /*TASKMANAGERSTYLE,*/ TEXTSIZE};//第二个ListView设置项的key数组
	String[] names3 = {"列表一次加载数据条数", "同步数据一次请求条数", "自动同步时间间隔", "清理系统垃圾的时间期限"};//第三个ListView显示的内容
	String[] keys3 = {LISTLOADNUM, SYNCDATANUM, AUTOASYNCDURATION, AUTOCLEANSYSGRABAGE};//第三个ListView设置项的key数组
	boolean[] defValues1 = {false, true, true, false};//第一个ListView的默认值数组
	int[] defValues2 = {STYLE_GRID, /*STYLE_TABS,*/ 26};//第二个ListView的默认值数组
	int[] defValues3 = {30, 5000, 60, 15};//第三个ListView的默认值数组
	//第二和第三个ListView中每一项对应的弹出选项数组
	String[][] selections2 = {menuStyles, /*menuStyles,*/ listTextSizeName};//listview2的选项
	String[][] selections3 = {listLoadSelections, requestNums, requestDurations, cleanDurations};//listview3的选项
	
	private ListView listView1, listView2, listView3;
	private ArrayList<HashMap<String,Object>> data1, data2, data3;
	private SharedPreferences sp;
	private MyAdapter adapter1, adapter2, adapter3;
	private ArrayList<HashMap<String, Object>> oldValues;
	private SqliteUtil su;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);
		
		
		
		RelativeLayout linearLayout = (RelativeLayout) this
				.findViewById(R.id.parentLayout);
		super.SetBaseStyle(linearLayout, "系统设置");
		View view=this.getLayoutInflater().inflate(R.layout.setting, null);	
		view.setBackgroundColor(Color.TRANSPARENT);
		
	
	
		listView1 = (ListView)  view.findViewById(R.id.listView1);
		listView2 = (ListView)  view.findViewById(R.id.listView2);
		listView3 = (ListView)  view.findViewById(R.id.listView3);
		
		((LinearLayout)findViewById(R.id.middleLayout)).addView(view);
		
		getOldValues();
		//初始化页面显示
		initData();
		adapter1 = new MyAdapter(data1);
		adapter2 = new MyAdapter(data2);
		adapter3 = new MyAdapter(data3);
		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
		listView3.setAdapter(adapter3);
		listView1.setOnItemClickListener(listener);
		listView2.setOnItemClickListener(listener);
		listView3.setOnItemClickListener(listener);
	}

	/**
	 * 获得数据库T_USER_SETTING表中所有的设置项
	 */
	private void getOldValues() {
		su = SqliteUtil.getInstance();
		oldValues = su.queryBySqlReturnArrayListHashMap("select * from T_USER_SETTING where SYSCODE = '"+Global.getGlobalInstance().getSystemtype()+"'");
	}
	/**
	 * 获取原来的值
	 * @param key
	 * @return
	 */
	private Object getValue(String key){
		if(key == null || "".equals(key)){
			return null;
		}
		Object values = null;
		if(oldValues != null && oldValues.size() > 0){
			for(HashMap<String,Object> map : oldValues){
				if(key.equalsIgnoreCase(String.valueOf(map.get("setkey")))){
					values = map.get("setvalue");
					break;
				}
			}
		}
		return values;
	}

	/**
	 * Description:初始化页面需要显示的数据
	 * 
	 * @author 王振洋
	 * Create at: 2012-12-4 上午10:57:47
	 */
	private void initData() {
		sp = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
		data1 = new ArrayList<HashMap<String,Object>>(); 
		data2 = new ArrayList<HashMap<String,Object>>(); 
		data3 = new ArrayList<HashMap<String,Object>>(); 
		int len = names1.length;
		SharedPreferences settingSp = getSharedPreferences("setting", MODE_WORLD_READABLE);
		if(!Boolean.parseBoolean(String.valueOf(getValue(AUTOLOGIN))) || "".equals(settingSp.getString("user", "").trim()) || "".equals(settingSp.getString("pass", "").trim())){
			len -= 1;
		}
		//初始化data1，包括类型，key和value
		for(int i=0;i<len;i++){
			Object value = getValue(keys1[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", 1);
			map.put("name", names1[i]);
			map.put("value", value == null ? defValues1[i] : value);
			data1.add(map);
		}
		//初始化data2，包括类型，key和value
		for(int i=0;i<names2.length;i++){
			Object value = getValue(keys2[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", 2);
			map.put("name", names2[i]);
			map.put("value", value == null ? defValues2[i] : value);
			data2.add(map);
		}
		//初始化data3，包括类型，key和value
		for(int i=0;i<names3.length;i++){
			Object value = getValue(keys3[i]);
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("type", 3);
			map1.put("name", names3[i]);
			map1.put("value", value == null ? defValues3[i] : value);
			data3.add(map1);
		}
	}
	
	/**
	 * 三个ListView的onItemClick点击事件
	 */
	OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap<String,Object> map = null;
			int parentID=parent.getId();
			if(parentID==R.id.listView1){
				map = data1.get(position);
				boolean result = Boolean.parseBoolean(String.valueOf(map.get("value")));
				//切换开关的显示图片，并保存设置结果
				if(result){
					view.findViewById(R.id.switchLayout).setBackgroundResource(R.drawable.switch_off);
					view.findViewById(R.id.onText).setVisibility(View.INVISIBLE);
					view.findViewById(R.id.offText).setVisibility(View.VISIBLE);
					map.put("value", false);
					saveSettings(R.id.listView1, keys1[position], false);
				} else {
					view.findViewById(R.id.switchLayout).setBackgroundResource(R.drawable.switch_on);
					view.findViewById(R.id.onText).setVisibility(View.VISIBLE);
					view.findViewById(R.id.offText).setVisibility(View.INVISIBLE);
					map.put("value", true);
					saveSettings(R.id.listView1, keys1[position], true);
				
				}
			}else if(parentID==R.id.listView2){
				map = data2.get(position);
				showSelectDialog(R.id.listView2, position);
				
			}else if(parentID==R.id.listView3){
				map = data3.get(position);
				showSelectDialog(R.id.listView3, position);
			}
			
		}

	};
	
	/**
	 * Description:弹出选项对话框
	 * @param viewId 对应ListView的ID
	 * @param position 点击位置
	 * @author 王振洋
	 * Create at: 2012-12-4 上午11:02:00
	 */
	private void showSelectDialog(final int viewId, final int position) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(R.drawable.icon_mapuni_white);
		HashMap<String,Object> map = null;
		String[] items = null;	//要显示的选项
		int defIndex = 0;	//默认选择项
		if(viewId == R.id.listView2){
			map = data2.get(position);
			dialog.setTitle("选择"+names2[position]);
			
			items = selections2[position];
			
		} else if(viewId == R.id.listView3) {
			map = data3.get(position);
			dialog.setTitle("选择"+names3[position]);
			
			items = selections3[position];
		}
		final String[] items1 = items;
		String value = String.valueOf(map.get("value"));
		boolean flag = false;//标记是否设置了要显示的值了
		for(int i=0;i<items.length;i++){
			if(value.equals(items[i])){
				defIndex = i;
				flag = true;
				break;
			}
		}
		if(!flag){
			if(position == 1/*字体大小的位置*/ && viewId == R.id.listView2)	//设置列表字体大小弹出选项时的初始选项
				defIndex = getElementIndex(listTextSize, String.valueOf(map.get("value")));
			else
				defIndex = Integer.parseInt(value)-1;
		}
		dialog.setSingleChoiceItems(items, defIndex, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(viewId == R.id.listView2){
					if(position == 0 /*|| position == 1*/){	//菜单样式
						data2.get(position).put("value", which+1);
						adapter2.notifyDataSetChanged();
						saveSettings(viewId, keys2[position], which+1);
					} else if(position == 1) { //列表字体大小
						data2.get(position).put("value", listTextSize[which]);
						adapter2.notifyDataSetChanged();
						saveSettings(viewId, keys2[position], listTextSize[which]);
					}
				} else if (viewId == R.id.listView3){
					data3.get(position).put("value", items1[which]);
					adapter3.notifyDataSetChanged();
					saveSettings(viewId, keys3[position], items1[which]);
				}
//				Toast.makeText(SettingActivity.this, items1[which], 0).show();
			}
		});
		dialog.show();
	}
	
	/**
	 * Description:保存设置结果
	 * @param viewId 点击的ListView的ID
	 * @param key 
	 * @param value
	 * @author Administrator
	 * Create at: 2012-12-4 上午11:13:08
	 */
	private void saveSettings(int viewId, String key, Object value){
//		Editor editor = sp.edit();
//		int num=0;
//		if(viewId == R.id.listView1){
//			editor.putBoolean(key, (Boolean)value);
//		} else {
//			num = Integer.parseInt(value.toString());
//			editor.putInt(key, num);
//		}
////		Toast.makeText(this, key+"=="+value.toString(), 0).show();
//		editor.commit();
		
		String sql ;
		Object oldValue = getValue(key);
		if(oldValue == null){
			sql = "insert into T_USER_SETTING('SYSCODE','SETKEY','SETVALUE') VALUES('"
					+Global.getGlobalInstance().getSystemtype()+"','"
					+key+"','"
					+value+"')";
		} else {
			sql = "update T_USER_SETTING set SETVALUE = '"
					+value+"' where SETKEY = '"+key+"' and SYSCODE = '"+Global.getGlobalInstance().getSystemtype()+"'";
		}
//		Toast.makeText(this, key+"=="+value.toString(), 0).show();
		su.execute(sql);
		getOldValues();
	}

	/**
	 * FileName: SettingActivity.java
	 * Description:ListView适配器
	 * @author 王振洋
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-4 上午11:18:19
	 */
	class MyAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> data;
		private LayoutInflater inflater;
		
		public MyAdapter(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			inflater = getLayoutInflater();
		}
		
		@Override
		public int getCount() {
			return data.size();
		}
		@Override
		public Object getItem(int position) {
			return data.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.setting_item, null);
			}
			HashMap<String,Object> map = data.get(position);
			int type = (Integer) map.get("type");
			TextView textView = (TextView) convertView.findViewById(R.id.textView1);
			TextView valueText = (TextView) convertView.findViewById(R.id.valueText1);
			LinearLayout switchLayout = (LinearLayout) convertView.findViewById(R.id.switchLayout);
			TextView onText = (TextView) convertView.findViewById(R.id.onText);
			TextView offText = (TextView) convertView.findViewById(R.id.offText);
			textView.setText(String.valueOf(map.get("name")));
			if(type == 1){
				valueText.setVisibility(View.GONE);
				switchLayout.setVisibility(View.VISIBLE);
				if(Boolean.parseBoolean(String.valueOf(map.get("value")))){
					switchLayout.setBackgroundResource(R.drawable.switch_on);
					onText.setVisibility(View.VISIBLE);
					offText.setVisibility(View.INVISIBLE);
				} else {
					switchLayout.setBackgroundResource(R.drawable.switch_off);
					onText.setVisibility(View.INVISIBLE);
					offText.setVisibility(View.VISIBLE);
				}
			} else {
				valueText.setVisibility(View.VISIBLE);
				switchLayout.setVisibility(View.GONE);
				if(type == 2){
					if(position == 0)//没屏蔽任务菜单样式选项之前if(position == 0 || position == 1)
						valueText.setText(Integer.parseInt(String.valueOf(map.get("value")))==1?"九宫格":"滑动切换");
					else 
						valueText.setText(listTextSizeName[getElementIndex(listTextSize, String.valueOf(map.get("value")))]);
				} else {
					if(position==3){
						valueText.setText(String.valueOf(map.get("value"))+"天之前"); 
					}else
					valueText.setText(String.valueOf(map.get("value"))+(position == 2 ? "分钟" : "条"));
				}
			}
			return convertView;
		}
	}
	/**
	 * 根据元素找到对应数组的位置并返回
	 * @param array
	 * @param element
	 * @return
	 */
	private int getElementIndex(String[] array, String element){
		for(int i=0;i<array.length;i++){
			if(element.equals(array[i])){
				return i;
			}
		}
		return 0;
	}
	


	
	
}
