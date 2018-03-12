package cn.com.mapuni.meshing.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.dom4j.DocumentException;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import cn.com.mapuni.meshing.activity.gis.MapMainActivity;
import cn.com.mapuni.meshing.util.ExampleUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.example.meshing.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.interfaces.IInitData;
import com.mapuni.android.base.util.Apn;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.dataprovider.XmlHelper;
import com.tianditu.android.maps.TOfflineMapManager.MapAdminSet;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;

public class MainActivity extends TabActivity implements OnCheckedChangeListener {
	private RadioGroup mainTab;
	private TabHost mTabHost;
	Intent intent, tempIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintab);
		InitView();
		
	}

	int checkedId = -1;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checkedId != -1) {
			if (MapMainActivity.MAPACTIVITY != null) {
				MapMainActivity.MAPACTIVITY.changeView(checkedId);
			}
		}
	}

	@SuppressLint("ResourceAsColor")
	public void InitView() {
		mainTab = (RadioGroup) findViewById(R.id.main_tab);
		mainTab.setBackgroundResource(R.color.white);
		this.mTabHost = getTabHost();
		loadLayout();
		mainTab.setOnCheckedChangeListener(this);
		// //////////////////////////////////////不具有巡检功能的角色屏蔽巡检页面
		String organization_code = DisplayUitl.readPreferences(this, "lastuser", "organization_code");
		String havePatrolRole = DisplayUitl.readPreferences(this, "lastuser", "havePatrolRole");
		String haveAdminRole = DisplayUitl.readPreferences(this, "lastuser", "haveAdminRole");
		String haveLiaisonRole = DisplayUitl.readPreferences(this, "lastuser", "haveLiaisonRole");
		if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) && !"1".equals(haveLiaisonRole))
				|| (organization_code.length() == 10)) {
		} else {
			mainTab.check(1);
		}
		// ////////////////////////////////////
	}

	@Override
	public void onCheckedChanged(final RadioGroup group, final int checkedId) {
		this.checkedId = checkedId;
		RadioButton radioBtn = (RadioButton) group.findViewById(checkedId);
		@SuppressWarnings("unchecked")
		final HashMap<String, Object> menu = (HashMap<String, Object>) radioBtn.getTag();
		final String tabname = menu.get("tabname").toString();
		String btnName = menu.get("menuname").toString();
		String imagename = menu.get("imagename").toString();
		String ywl = menu.get("ywl").toString();
		Intent intent = (Intent) menu.get("intent");
		tempIntent = initdata(intent, ywl);
		setTabUpIntent(tabname, btnName, tempIntent, imagename);
		this.mTabHost.setCurrentTabByTag(tabname);
		if (MapMainActivity.MAPACTIVITY != null) {
			MapMainActivity.MAPACTIVITY.changeView(checkedId);
		}
	}

	/** 加载tab页的布局 */
	public void loadLayout() {
		ArrayList<HashMap<String, Object>> tabMenu;
		tabMenu = getMoreMenu("main_tabmenu.xml", "item");
		ArrayList<HashMap<String, Object>> tabMenus = new ArrayList<HashMap<String, Object>>();
		// //////////////////////////////////////不具有巡检功能的角色屏蔽巡检页面
		String organization_code = DisplayUitl.readPreferences(this, "lastuser", "organization_code");
		String havePatrolRole = DisplayUitl.readPreferences(this, "lastuser", "havePatrolRole");// 涓績璐熻矗浜�
		String haveAdminRole = DisplayUitl.readPreferences(this, "lastuser", "haveAdminRole");// 绠＄悊鍛�
		String haveLiaisonRole = DisplayUitl.readPreferences(this, "lastuser", "haveLiaisonRole");// 鑱旂粶鍛�
		if ((!"1".equals(havePatrolRole) && !"1".equals(haveAdminRole) && !"1".equals(haveLiaisonRole))
				|| (organization_code.length() == 10)) {
		} else {
			tabMenu.remove(0);
		}
		// ////////////////////////////////////
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
			boolean ifchecked = Boolean.parseBoolean(menu.get("ifchecked").toString());
			String tabname = menu.get("tabname").toString();
			// --------------------------暂时去掉权限----yangjunying
			// if (DisplayUitl.getAuthority(menuqxid)) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
			RadioButton Tab = new RadioButton(MainActivity.this);
			Tab.setLayoutParams(params);
			Tab.setChecked(ifchecked);
			Tab.setId(order);
			intent = prepareIntent(contextname, classname);
			tempIntent = intent;
			menu.put("intent", tempIntent);
			Tab.setTag(menu);
			setStyle(Tab, imagename);
			// mainTab.addView(Tab, order);
			mainTab.addView(Tab);// 添加tab頁
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
	 * @param rwglIntent
	 * @param ywl
	 *            所需的业务类的名字
	 * @return
	 */
	public Intent initdata(Intent ywlintent, String ywl) {
		IInitData business = null;
		Intent intent = null;
		try {
			business = (IInitData) BaseObjectFactory.createObject(ywl);
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
		radioBtn.setTextColor(Color.parseColor("#FFFFFF"));
		radioBtn.setEllipsize(TruncateAt.MARQUEE);
		radioBtn.setGravity(Gravity.CENTER_HORIZONTAL);
		radioBtn.isChecked();
		radioBtn.setButtonDrawable(android.R.color.transparent);// 去除前边的圆点
		radioBtn.setSingleLine(true);
		radioBtn.setBackgroundDrawable(getResources().getDrawable(geBitmaptRes(imagename)));
	}

	public void setTabUpIntent(String tabname, String btnName, Intent intent, String imagename) {
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
	 * @param resIcon
	 *            图标
	 * @param content
	 *            该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel, null).setContent(tempIntent);
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
		int resID = this.getResources().getIdentifier(name, "drawable", appInfo.packageName);
		return resID;
	}

	/**
	 * 获取屏幕的宽度
	 * 
	 * @param btnCount
	 * @return
	 */
	public int getWindowManagerWidth(int btnCount) {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
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
	 * @param nodeFather
	 *            根节点
	 * @return ArrayList<HashMap<String, Object>>
	 */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml, String nodename) {
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

	/**
	 * 按撤销键弹出推出对话框
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setIcon(R.drawable.base_icon_mapuni_white);
			dialog.setTitle("系统退出");
			dialog.setMessage("确定要退出系统吗？");
			dialog.setPositiveButton("是", new btnExitListener());
			dialog.setNegativeButton("否", null);
			dialog.show();
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * FileName: GridActivity.java Description: 退出系统
	 * 
	 * @author xuhuaiguang
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司 Create at: 2012-12-6 上午09:43:32
	 */
	private class btnExitListener implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
		/*	if (Build.VERSION.SDK_INT <= 10 && Global.getGlobalInstance().isUpdataapn()) {
				Apn apn = new Apn(MainActivity.this);
				apn.setReturnAPN();
			}
			myExit();*/
			finish();
		}
	}

	/**
	 * Description: 由子类退出方法调用 void
	 * 
	 * @author 王红娟 Create at: 2012-12-5 下午02:25:21
	 */
	public void myExit() {
		Intent intent = new Intent();
		intent.setAction("EXIT");
		this.sendBroadcast(intent);
//		setAlias("");
		finish();
	}

	// 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
	private void setAlias(String alias) {
		// 调用 Handler 来异步设置别名
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i("aaa", logs);
				// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i("aaa", logs);
				// 延迟 60 秒来调用 Handler 设置别名
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;
			default:
				logs = "Failed with errorCode = " + code;
				Log.e("aaa", logs);
			}
			// ExampleUtil.showToast(logs, getApplicationContext());
		}
	};
	private static final int MSG_SET_ALIAS = 1001;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d("aaa", "Set alias in handler.");
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;
			default:
				Log.i("aaa", "Unhandled msg - " + msg.what);
			}
		}
	};
}
