/**
 * 
 */
package cn.com.mapuni.meshing.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.mapuni.meshing.base.R;
import cn.com.mapuni.meshing.base.controls.loading.YutuLoading;
import cn.com.mapuni.meshing.base.dataprovider.SqliteUtil;
import cn.com.mapuni.meshing.base.dataprovider.XmlHelper;
import cn.com.mapuni.meshing.base.interfaces.IGrid;
import cn.com.mapuni.meshing.base.interfaces.PathManager;
import cn.com.mapuni.meshing.base.util.BaseAutoUpdate;
import cn.com.mapuni.meshing.base.util.DisplayUitl;
import cn.com.mapuni.meshing.base.util.ExceptionManager;
import cn.com.mapuni.meshing.netprovider.Net;


/**FileName: LoadGridLayout.java
 * Description: 加载九宫格页面的内容
 * @author wanghj
 * @Version 1.3.5
 * @Copyright 中科宇图天下科技有限公司
 * Create at: 2013-1-25 上午11:29:53
 */
public class LoadGridLayout {

	private Context context;
	private Intent intent;
	private IGrid businessObj;
	private Boolean ydbg;
	private YutuLoading yutuLoading;
	
	/** 没有更新数据 */
	private  final int NOUPDATEDATA = 0;
	/** 更新数据成功 */
	private  final int SUCCESS = 1;
	/** 更新数据失败 */
	private  final int FILE = 2;
	/** 链接服务器超时 */
	private  final int OVERTIME = 3;
	/** 对于更新结果不与提示 */
	private  final int NO = 4;
	
	private String btnName;
	
	

	
	
	
	
	/** 定义一个进度框 */
//	private ProgressDialog pd = null;
	private BaseAutoUpdate autoUpdate = null;
	/** UI线程Handler */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			/**如果是移动办公*/
			if (ydbg) {
				
				switch (msg.what) {

				case NOUPDATEDATA:
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					Toast.makeText(context, "暂没有查询的数据！",
							Toast.LENGTH_SHORT).show();
					break;
				case SUCCESS:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					// Toast.makeText(context, "更新数据成功！",
					// Toast.LENGTH_SHORT).show();
					context.startActivity(intent);
					break;
				case FILE:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					Toast.makeText(context, "更新数据失败，请检查网络设置！",
							Toast.LENGTH_SHORT).show();
					context.startActivity(intent);
					break;
				case OVERTIME:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					/*
					 * Toast.makeText(context, "链接服务器超时无法更新数据，请稍后再试！",
					 * Toast.LENGTH_SHORT).show();
					 */
					context.startActivity(intent);
					break;
				case NO:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					context.startActivity(intent);
					break;
				}

			} else {
				yutuLoading.dismissDialog();
				switch (msg.what) {
				case 0:
					Toast.makeText(context, "网络异常，请稍后重试！", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					String url = Global.getGlobalInstance().getSystemurl();
					autoUpdate.UPdateAPK(url + PathManager.APK_DOWN_URL,
							context, url + PathManager.APK_CODE_URL);
					break;
				case 2:
					Toast.makeText(context, "暂无更新信息！",Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(context, "网络不通,请检查网络设置!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		}
	};
	
	public LoadGridLayout(Context context,Intent intent,IGrid businessObj,Boolean ydbg){
		this.context = context;
		this.intent = intent;
		this.businessObj = businessObj;
		this.ydbg = ydbg;
	}
	
	public GridView loadGridLayout(){
		
		/** 存放九宫格菜单信息的集合 */
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		try {
			/** 封装当前页面所要显示的样式数据的List */
			ArrayList<HashMap<String, Object>> dataXMLList = null;
			boolean isStyleFromDB = !Global.isTestStyle;
			if (isStyleFromDB) {
				String serverName = Global.getGlobalInstance().getSystemname()
						.toLowerCase();
				String moduleID = businessObj.getModuleID();
				String styleSql = "select orderid as id,img,modulename as qymc,moduleid as qxid,ywl from appsystemmodule where appsystemcode='"
						+ serverName
						+ "' and parentid='"
						+ moduleID
						+ "' order by orderid";
				dataXMLList = SqliteUtil.getInstance()
						.queryBySqlReturnArrayListHashMap(styleSql);
			} else {
//				dataXMLList = (ArrayList<HashMap<String, Object>>) XmlHelper
//						.getList(
//								context.getResources().getAssets().open(
//										businessObj.getDataXMLName()), "item");
				String systemtype = Global.getGlobalInstance().getSystemtype().toUpperCase();
				dataXMLList = (ArrayList<HashMap<String, Object>>) XmlHelper
				.getMenuFromXml(context,systemtype,"system","item",
						context.getResources().getAssets().open(
								businessObj.getDataXMLName()));
			}

		
			
			HashMap<String, Object> map =null;
			/** 定义一个九宫格按钮图片在资源文件中的ID，默认为背景图片资源文件ID */
			int returnValue = R.drawable.base_background;//九宫格默认背景
			for (HashMap<String, Object> dataMap : dataXMLList) {
				if ("".equals(dataMap.get("qxid"))
						|| DisplayUitl.getAuthority(dataMap.get("qxid")//遍历配置文件，取得有权限的模块名字和图片名字
								.toString())) {
					
					map=new HashMap<String, Object>();
					String img = (String) dataMap.get("img");
					String name = (String) dataMap.get("qymc");
					returnValue=context.getResources().getIdentifier(img,
							"drawable", context.getPackageName());//通过图片名字获取图片ID
					map.put("img", returnValue);
					map.put("qymc", name);
					meumList.add(map);
					
				}
			}
		
		
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "GridActivity");
			e.printStackTrace();
		}

		/** 定义一个九宫格对象 */
		GridView gridview = new GridView(context);
		/** 设置九宫格的列数 */
		if(businessObj.getGridTitle().equals("移动执法")){
			gridview.setNumColumns(2);	
		}else
			gridview.setNumColumns(3);
		
		/** 设置九宫格的行距 */
		gridview.setVerticalSpacing(40);

		/**
		 * 九宫格的是配置，里边的参数包括： 上下文，菜单数据源 ，按钮的布局文件资源ID ，对应的数据Map的Key ，按钮
		 * 布局文件中对应的R的ID
		 */
		SimpleAdapter saMenuItem = new SimpleAdapter(context, meumList,
				R.layout.base_ui_grid_item, new String[] { "img", "qymc" },
				new int[] { R.id.ItemImage, R.id.ItemText });

		/** 给九宫格添加Item到网格中 */
		gridview.setAdapter(saMenuItem);
		/** 去除单击效果 */
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//防止重复点击按钮
				if(DisplayUitl.isFastDoubleClick()) {
					return;
				}
				
				/** 获取单击组件的名称*/
				TextView text = (TextView) arg1.findViewById(R.id.ItemText);
				String str = (String) text.getText();
				
				int i = str.indexOf(":");
				btnName = str.substring(i + 1, str.length());
				
				if (!ydbg) {
					if (btnName.equals("检查更新")) {
//						pd = new ProgressDialog(context);
//						pd.setCancelable(false);
//						Updata();
//						if(!Net.checkURL(Global.getGlobalInstance().getSystemurl())){
//							Toast.makeText(context, "网络不通,请检查网络设置!", Toast.LENGTH_SHORT).show();
//							return;
//						}
//						Intent intent = new Intent();
//						intent.setAction("com.mapuni.android.checkUpdate");
//						context.sendBroadcast(intent);
						
						yutuLoading = new YutuLoading(context);
						yutuLoading.showDialog();
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(!Net.checkURL(Global.getGlobalInstance().getSystemurl())){
									handler.sendEmptyMessage(3);
									return;
								}
								
								String url = Global.getGlobalInstance().getSystemurl();
								autoUpdate = new BaseAutoUpdate();
								int result = autoUpdate.JudgeNewVerson(url
										+ PathManager.APK_CODE_URL, context);
								
								handler.sendEmptyMessage(result);
							}
						}).start();
						return;
					}
					
				}
				
				if (btnName.equals("移动监测") || btnName.equals("移动OA")) {
					Toast.makeText(context, "暂无内容", Toast.LENGTH_SHORT).show();
					return;
				}

				yutuLoading = new YutuLoading(context);
				yutuLoading.showDialog();

				new Thread(new Runnable() {
					@Override
					public void run() {
						if (ydbg) {
							intent = businessObj.setIntent(context,
									btnName, handler);
						} else {
							intent = businessObj.setIntent(context, btnName, handler);
							context.startActivity(intent);
							try {
								Thread.sleep(500);//休眠  使新Activity可以加载出界面
							} catch (Exception e) {
								// TODO: handle exception
							}
//							yutuLoading.dismissDialog();
							Message msg = handler.obtainMessage();
							msg.what = 4;
							handler.handleMessage(msg);
						}

					}
				}).start();
				
			}
		});
		return gridview;
	}
	
	/**
	 * FileName: GridActivity.java
	 * Description: 适配器（目前不再用）
	 * @author 柳思远
	 * @Version 1.3.4
	 * @Copyright 中科宇图天下科技有限公司
	 * Create at: 2012-12-6 上午09:44:45
	 */
	/*private class MyListViewAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> data;
		Context context;

		public MyListViewAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			LinearLayout layout = (LinearLayout) mInflater.inflate(
					R.layout.listitem, null);

			ImageView headImageView = (ImageView) layout
					.findViewById(R.id.listitem_left_image);
			headImageView.setImageResource(R.drawable.icon_left_not_checked);

			TextView mTextView = (TextView) layout
					.findViewById(R.id.listitem_text);
			String text = (String) data.get(position).get("timing");
			mTextView.setText(text);

			return layout;
		}
	}*/
	
	/**
	 * Description: 弹出进度条，更新应用程序。
	 * 
	 * void
	 * @author 柳思远
	 * Create at: 2012-12-6 上午09:41:28
	 */
	private  void Updata() {
//		handler = new Handler() {
//			public void handleMessage(android.os.Message msg) {
//				if (msg.arg1 != 0) {
//					int a = msg.arg1;
//					pd.setProgress(a);
//				}
//			};
//		};

		/** 更新检测 */
		 /*网络存在则监察更新*/
		if (Net.checkNet(context)) {
			
			//Base 的九宫格不会出现检查更新
		
			/**获取系统的服务器地址*/
			/*String url = Global.getGlobalInstance().getSystemurl();
			
			if (Net.checkURL(url)) {
				AutoUpdate autoUpdate = new AutoUpdate();
				int result = autoUpdate.JudgeNewVerson(url
						+ PathManager.APK_CODE_URL, context);
				switch (result) {
				case 1:
					autoUpdate.UPdateAPK(url + PathManager.APK_DOWN_URL,
							context, url + PathManager.APK_CODE_URL);
					break;
				case 2:
					Toast.makeText(context, "已是最新版本无需更新！",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(context, "网络异常，请稍后重试！",
							Toast.LENGTH_SHORT).show();
					break;
				}
			} else {
				Toast.makeText(context, "网络链接超时，请检查网络设置！",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "网络链接超时，请检查网络设置！",
					Toast.LENGTH_SHORT).show();
		}*/
			}
	}
}
