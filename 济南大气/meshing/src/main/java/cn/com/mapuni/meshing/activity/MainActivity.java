package cn.com.mapuni.meshing.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.dom4j.DocumentException;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import cn.com.mapuni.meshing.base.business.BaseObjectFactory;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.interfaces.IInitData;

import cn.com.mapuni.meshingtotal.R;

public class MainActivity extends TabActivity implements
		OnCheckedChangeListener {
	private RadioGroup mainTab;
	private TabHost mTabHost;
	Intent intent, tempIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintab);
		InitView();
	}

	@SuppressLint("ResourceAsColor")
	public void InitView() {
		mainTab = (RadioGroup) findViewById(R.id.main_tab);
		mainTab.setBackgroundResource(R.color.white);
		this.mTabHost = getTabHost();
		loadLayout();
		mainTab.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(final RadioGroup group, final int checkedId) {
		RadioButton radioBtn = (RadioButton) group.findViewById(checkedId);
		@SuppressWarnings("unchecked")
		final HashMap<String, Object> menu = (HashMap<String, Object>) radioBtn
				.getTag();
		final String tabname = menu.get("tabname").toString();
		String btnName = menu.get("menuname").toString();
		String imagename = menu.get("imagename").toString();
		String ywl = menu.get("ywl").toString();
		Intent intent = (Intent) menu.get("intent");
		tempIntent = initdata(intent, ywl);
		setTabUpIntent(tabname, btnName, tempIntent, imagename);
		this.mTabHost.setCurrentTabByTag(tabname);
	}

	/** 加载tab页的布局 */
	public void loadLayout() {
		ArrayList<HashMap<String, Object>> tabMenu;
		tabMenu = getMoreMenu("main_tabmenu.xml", "item");
		ArrayList<HashMap<String, Object>> tabMenus = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> map : tabMenu) {
			String menuqxid = map.get("qxid").toString();
			// --------------------------暂时去掉权限----yangjunying
			// if (DisplayUitl.getAuthority(menuqxid)) {
			tabMenus.add(map);
			// }
		}
		int width = getWindowManagerWidth(tabMenus.size());
		for (HashMap<String, Object> menu : tabMenus) {
			int order = Integer.parseInt(menu.get("order").toString());
			String btnName = menu.get("menuname").toString();
			String menuqxid = menu.get("qxid").toString();
			String imagename = menu.get("imagename").toString();
			String contextname = menu.get("contextname").toString();
			String classname = menu.get("classname").toString();
			String ywl = menu.get("ywl").toString();
			boolean ifchecked = Boolean.parseBoolean(menu.get("ifchecked")
					.toString());
			String tabname = menu.get("tabname").toString();
			// --------------------------暂时去掉权限----yangjunying
			// if (DisplayUitl.getAuthority(menuqxid)) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					width, LayoutParams.WRAP_CONTENT);
			RadioButton Tab = new RadioButton(MainActivity.this);
			Tab.setLayoutParams(params);
			Tab.setChecked(ifchecked);
			Tab.setId(order);
			intent = prepareIntent(contextname, classname);
			tempIntent = intent;
			menu.put("intent", tempIntent);
			Tab.setTag(menu);
			setStyle(Tab, imagename);
			mainTab.addView(Tab, order);
			setImg(Tab, btnName);
			tempIntent = initdata(intent, ywl);
			setTabUpIntent(tabname, btnName, tempIntent, imagename);
			// } else {
			// continue;
			// }
		}
	}

	/**
	 * 初始化按钮的数据
	 *
	 * @param ywl
	 *            所需的业务类的名字
	 * @return
	 */
	public Intent initdata(Intent ywlintent, String ywl) {
		IInitData business = null;
		Intent intent = null;
		try {
//			business = (IInitData) BaseObjectFactory.createObject(ywl);
			Object obj = null;
			Class<?> businessClass = Class.forName("cn.com.mapuni.meshing.business." + ywl);
			obj = businessClass.newInstance();
			business = (IInitData) obj;
			intent = business.InitData(this, ywlintent, ywl);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return intent;
	}

	/**
	 * 添加图片
	 * 
	 */
	private void setImg(RadioButton radioBtn, String btnName) {
		radioBtn.setText(btnName);
		radioBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * 给radionButton设置样式
	 * 
	 * @param radioBtn
	 */
	public void setStyle(RadioButton radioBtn, String imagename) {
		radioBtn.setTextSize(0);
		radioBtn.setTextColor(Color.parseColor("#FFFFFF"));
		radioBtn.setEllipsize(TruncateAt.MARQUEE);
		radioBtn.setGravity(Gravity.CENTER_HORIZONTAL);
		radioBtn.isChecked();
		radioBtn.setPadding(0, 2, 0, 0);
		radioBtn.setButtonDrawable(android.R.color.transparent);// 去除前边的圆点
		radioBtn.setSingleLine(true);
		radioBtn.setBackgroundDrawable(getResources().getDrawable(
				geBitmaptRes(imagename)));
	}

	public void setTabUpIntent(String tabname, String btnName, Intent intent,
			String imagename) {
		setupIntent(tabname, btnName, tempIntent);
	}

	private void setupIntent(String tabname, String menuname, Intent intent) {

		TabHost localTabHost = this.mTabHost;
		localTabHost.addTab(buildTabSpec(tabname, menuname, tempIntent));
	}

	/**
	 * 构建TabHost的Tab页
	 * 
	 * @param tag
	 *            标记
	 * @param resLabel
	 *            标签
	 *            图标
	 * @param content
	 *            该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel, null)
				.setContent(tempIntent);
	}

	/**
	 * Description: 获取列表的图片id
	 * 
	 * @param name
	 *            照片的名字
	 * @return 返回所需照片id Bitmap
	 * @author xgf Create at: 2012-11-30 上午11:30:37
	 */
	public int geBitmaptRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return resID;
	}

	/**
	 * 获取屏幕的宽度
	 * 
	 * @param btnCount
	 * @return
	 */
	public int getWindowManagerWidth(int btnCount) {
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		if (width != 0 && btnCount != 0) {
			return width / btnCount;
		}
		return 0;
	}

	/**
	 * Description: 获取更多菜单配置文件数据
	 * 
	 * @param xml
	 *            xml文件的名称
	 *            根节点
	 * @return ArrayList<HashMap<String, Object>>
	 */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml,
			String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = MainActivity.this.getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	/**
	 * 准备tab的内容Intent
	 */
	private Intent prepareIntent(String pakegeName, String classname) {// 实例化子spec的intent
		Class<?> obj = null;
		try {
			obj = Class.forName(pakegeName + "." + classname);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this, obj);
		return intent;
	}

}
