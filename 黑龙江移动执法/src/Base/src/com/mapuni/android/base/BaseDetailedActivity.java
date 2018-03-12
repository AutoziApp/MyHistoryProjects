package com.mapuni.android.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuni.android.base.controls.listview.MyScrollView;
import com.mapuni.android.base.interfaces.IDetailed;
import com.mapuni.android.base.util.DisplayUitl;

/**
 * FileName: DetailedActivity.java 
 * Description: 对详细信息数据的读取
 * @author 王红娟
 * @Version 1.3.4
 * @Copyright 中科宇图天下科技有限公司 
 * Create at: 2012-12-5 下午02:50:29
 */
public class BaseDetailedActivity extends BaseActivity {

	/** 定义该页面的TAG内容 */
	public  final String TAG = "DetailedActivity";
	/** 存放数据的Bundle */
	public Bundle bundle;
	/** 加载详情布局 */
	public LoadDetailLayout detaillayout ;
	
	/** 底部菜单配置文件名称 */
	public String DetailedBottomMenuStyleFileName;
	/***/
	public  final String INFO_HINT = "InfoHint";
	public  final String INFO = "Info";
	public  int width = 0;

	/*** 展示详情需要的布局文件 */
	/** ui_mapuni布局文件中最外边一层的布局 */
	public RelativeLayout parentLayout;
	/** 中间布局，用来显示详情 */
	public LinearLayout detailedLayout;
	public MyScrollView myScrollView;
	
	/** ui_mapuni中的底部布局 */
	public LinearLayout bottomlayout;
	/** 用来加载底部菜单的布局文件，父布局为bottomlayout */
	public LinearLayout bottom;

	/** 业务类继承了IDetailed接口 */
	public IDetailed businessObj;
	/** 存放展示详情的样式数据 */
	public ArrayList<HashMap<String, Object>> styleDetailed;
	/** 底部菜单集合 */
	public ArrayList<HashMap<String, Object>> bottomlist;
	/** 当前对象的id */
	public String itemId;
	/** 存放对象详情的集合 */
	public HashMap<String, Object> detaild;
	/** 存放Html语句的字符串 */
	public String otherstr = "";
	/** 是否是修改状态，默认为false */
	public boolean isAmend = false;
	/** 当前所在页码 */
	public int currentIndex = -1;

	/*** 以下为权限值，用来控制按钮 */
	/** 现场执法权限 */
	public  final String QX_XCZF = "vmob6A";
	/** 在线监测权限 */
	public  final String QX_ZXJC = "vmob3A";
	/** 任务详情权限 */
	public  final String QX_RWXQ = "vmob2A1B";
	/** 新建任务权限 */
	public  final String QX_XJRW = "vmob2A5B";
	
	public BottomBtnAdapter bottomBtnAdapter;
	DisplayMetrics dm;
	public int screenWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ui_mapuni);
	
		/** 获取bundle信息*/
		Intent uponIntent = getIntent();
		bundle = uponIntent.getExtras();
		currentIndex = bundle.getInt("selectedindex", -1);
		if (!bundle.getBoolean("isSelf")) {
			currentIndex--;
		}
		businessObj = (IDetailed) bundle.getSerializable("BusinessObj");
		String titleText = businessObj.getDetailedTitleText();
		parentLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
		super.SetBaseStyle(parentLayout, titleText);
		itemId = businessObj.getCurrentID();
		if(itemId == null || itemId.equals("")) {
			itemId = uponIntent.getStringExtra("itemId");
		}
		/** 不显示计算器*/
		if (titleText.equals("任务详情"))
			super.setCounterButtonVisiable(false);

		/** 是否为修改 */
		if (null != bundle.getString("isAmend")) {
			isAmend = true;
		}
		detailedLayout = (LinearLayout) this.findViewById(R.id.middleLayout);

		//底部菜单位置，默认不显示
		bottomlayout = (LinearLayout) findViewById(R.id.bottomLayout);
		bottomlayout.invalidate();
		bottomlayout.setOrientation(LinearLayout.VERTICAL);
		bottomlayout.setGravity(Gravity.CENTER_VERTICAL);
		bottomlayout.setVisibility(View.VISIBLE);
		
		//加载详情布局
		  dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		/** 算出按钮的高宽*/
//		width = (int) (dm.widthPixels / (double) 4);
		detaillayout = new LoadDetailLayout(this, businessObj, isAmend);
		myScrollView = detaillayout.loadDetailed(itemId);
		styleDetailed = detaillayout.getStyleDetailed();
		detaild = detaillayout.getDetaild();
		
		/** 从Bundle中获取底部菜单样式文件路径 */
		bottomlayout.removeAllViews();
		DetailedBottomMenuStyleFileName = bundle
				.getString("DetailedBottomMenuStyleFileName");
		
//		bottomlist = XmlHelper.getbuttomMenuFromXml(this,"XQ");
		bottomlist = businessObj.getbottomname(this);
		if(bottomlist!=null){
			checkBottomMenuList(bottomlist);
			bottomBtnAdapter = new BottomBtnAdapter(this,bottomlist);
		}
		if (DetailedBottomMenuStyleFileName != null || isAmend) {
			loadDetailBottomMenu(DetailedBottomMenuStyleFileName);
		} else {
			loadDetailBottomMenu(businessObj.getDetailedTitleText());
		}
		
		detailedLayout.addView(myScrollView);
		
		/** 禁止彈出輸入法 */
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
		
	}

	
	/**
	 * Description:筛选出有权限的底部菜单
	 * 
	 * @author Administrator
	 * Create at: 2012-12-24 上午10:28:07
	 */
	public void checkBottomMenuList(ArrayList<HashMap<String, Object>> bottomlist){
		Iterator iter = bottomlist.iterator();
		while(iter.hasNext()){
			HashMap<String,Object> map = (HashMap<String, Object>) iter.next();
			if("".equals(String.valueOf(map.get("qxid"))) || "null".equals(String.valueOf(map.get("qxid")))){
				continue;
			}
			if(!DisplayUitl.getAuthority(String.valueOf(map.get("qxid")))){
				iter.remove();
			}
		}
//		for(HashMap<String,Object> map : bottomlist){
//			if("".equals(String.valueOf(map.get("qxid"))) || "null".equals(String.valueOf(map.get("qxid")))){
//				continue;
//			}
//			if(!DisplayUitl.getAuthority(String.valueOf(map.get("qxid")))){
//				bottomlist.remove(map);
//			}
//		}
	}

	/**
	 * Description: 加载底部菜单
	 * 
	 * @param bottomMenuStyleFileName
	 *            底部菜单的文件名称 void
	 * @author 王红娟 
	 * Create at: 2012-12-5 下午03:21:16
	 */
	public void loadDetailBottomMenu(String bottomMenuStyleFileName) {}

	/**
	 * Description:保持dialog不关闭，以便弹出提示信息
	 * 
	 * @param dialog
	 *            要作用的dialog void
	 * @author 王红娟 
	 * Create at: 2012-12-5 下午03:37:42
	 */

	public void keepDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  void bottomMenuonclick() {
		
	}


	/**
	 * Description: 关闭dialog
	 * 
	 * @param dialog
	 *            要作用的dialog void
	 * @author 王红娟 
	 * Create at: 2012-12-5 下午03:38:00
	 */
	public void distoryDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * FileName: DetailedActivity.java
	 * Description:底部菜单适配器
	 * @author 王红娟
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-21 上午11:51:52
	 */
	public class BottomBtnAdapter extends BaseAdapter{
		
		/**底部菜单集合*/
		ArrayList<HashMap<String, Object>> bottomList;
		Context context;
		int displayWidth;
		int width ;
		/**
		 * 构造方法
		 */
		public BottomBtnAdapter(Context context, ArrayList<HashMap<String, Object>> bottomList){
			this.bottomList = bottomList;
			this.context = context;
			displayWidth = context.getResources().getDisplayMetrics().widthPixels;
			width = bottomList.size() > 4 ? (int) (screenWidth / (double) 4) : (int) (screenWidth / (double) bottomList.size());
		}

		@Override
		public int getCount() {
			return bottomList.size();
		}

		@Override
		public Object getItem(int position) {
			return bottomList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout layout = new LinearLayout(context);
			layout.setId(Integer.parseInt(bottomList.get(position).get("id").toString()));
			layout.setMinimumWidth(displayWidth/(getCount()<4?getCount():4));
			TextView btnName = null ;
			if(bottomList.size()==0){
				bottomlayout.setVisibility(View.GONE);	
			}else{
				ImageView splite = new ImageView(context);
				splite.setScaleType(ScaleType.FIT_XY);
				splite.setLayoutParams(new LinearLayout.LayoutParams(2,
						LinearLayout.LayoutParams.FILL_PARENT));
				splite.setBackgroundResource(R.drawable.base_bg_bottombutton_splite);
				btnName = new TextView(BaseDetailedActivity.this);
				btnName.setId(position);
				btnName.setText(bottomList.get(position).get("btnname").toString());
				btnName.getPaint().setFakeBoldText(true);
				btnName.setTextColor(Color.WHITE);
				btnName.setGravity(Gravity.CENTER);
				btnName.setWidth(width);
				btnName.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT));
				if(position != 0){
					layout.addView(splite);
				}
				layout.addView(btnName);
			}
			return layout;
		}
		
	}
}
