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
 * Description: ���ؾŹ���ҳ�������
 * @author wanghj
 * @Version 1.3.5
 * @Copyright �п���ͼ���¿Ƽ����޹�˾
 * Create at: 2013-1-25 ����11:29:53
 */
public class LoadGridLayout {

	private Context context;
	private Intent intent;
	private IGrid businessObj;
	private Boolean ydbg;
	private YutuLoading yutuLoading;
	
	/** û�и������� */
	private  final int NOUPDATEDATA = 0;
	/** �������ݳɹ� */
	private  final int SUCCESS = 1;
	/** ��������ʧ�� */
	private  final int FILE = 2;
	/** ���ӷ�������ʱ */
	private  final int OVERTIME = 3;
	/** ���ڸ��½��������ʾ */
	private  final int NO = 4;
	
	private String btnName;
	
	

	
	
	
	
	/** ����һ�����ȿ� */
//	private ProgressDialog pd = null;
	private BaseAutoUpdate autoUpdate = null;
	/** UI�߳�Handler */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			/**������ƶ��칫*/
			if (ydbg) {
				
				switch (msg.what) {

				case NOUPDATEDATA:
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					Toast.makeText(context, "��û�в�ѯ�����ݣ�",
							Toast.LENGTH_SHORT).show();
					break;
				case SUCCESS:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					// Toast.makeText(context, "�������ݳɹ���",
					// Toast.LENGTH_SHORT).show();
					context.startActivity(intent);
					break;
				case FILE:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					Toast.makeText(context, "��������ʧ�ܣ������������ã�",
							Toast.LENGTH_SHORT).show();
					context.startActivity(intent);
					break;
				case OVERTIME:
					intent = (Intent) msg.obj;
					if (yutuLoading != null)
						yutuLoading.dismissDialog();
					/*
					 * Toast.makeText(context, "���ӷ�������ʱ�޷��������ݣ����Ժ����ԣ�",
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
					Toast.makeText(context, "�����쳣�����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					String url = Global.getGlobalInstance().getSystemurl();
					autoUpdate.UPdateAPK(url + PathManager.APK_DOWN_URL,
							context, url + PathManager.APK_CODE_URL);
					break;
				case 2:
					Toast.makeText(context, "���޸�����Ϣ��",Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(context, "���粻ͨ,������������!", Toast.LENGTH_SHORT).show();
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
		
		/** ��žŹ���˵���Ϣ�ļ��� */
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();
		try {
			/** ��װ��ǰҳ����Ҫ��ʾ����ʽ���ݵ�List */
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
			/** ����һ���Ź���ťͼƬ����Դ�ļ��е�ID��Ĭ��Ϊ����ͼƬ��Դ�ļ�ID */
			int returnValue = R.drawable.base_background;//�Ź���Ĭ�ϱ���
			for (HashMap<String, Object> dataMap : dataXMLList) {
				if ("".equals(dataMap.get("qxid"))
						|| DisplayUitl.getAuthority(dataMap.get("qxid")//���������ļ���ȡ����Ȩ�޵�ģ�����ֺ�ͼƬ����
								.toString())) {
					
					map=new HashMap<String, Object>();
					String img = (String) dataMap.get("img");
					String name = (String) dataMap.get("qymc");
					returnValue=context.getResources().getIdentifier(img,
							"drawable", context.getPackageName());//ͨ��ͼƬ���ֻ�ȡͼƬID
					map.put("img", returnValue);
					map.put("qymc", name);
					meumList.add(map);
					
				}
			}
		
		
		} catch (IOException e) {
			ExceptionManager.WriteCaughtEXP(e, "GridActivity");
			e.printStackTrace();
		}

		/** ����һ���Ź������ */
		GridView gridview = new GridView(context);
		/** ���þŹ�������� */
		if(businessObj.getGridTitle().equals("�ƶ�ִ��")){
			gridview.setNumColumns(2);	
		}else
			gridview.setNumColumns(3);
		
		/** ���þŹ�����о� */
		gridview.setVerticalSpacing(40);

		/**
		 * �Ź���������ã���ߵĲ��������� �����ģ��˵�����Դ ����ť�Ĳ����ļ���ԴID ����Ӧ������Map��Key ����ť
		 * �����ļ��ж�Ӧ��R��ID
		 */
		SimpleAdapter saMenuItem = new SimpleAdapter(context, meumList,
				R.layout.base_ui_grid_item, new String[] { "img", "qymc" },
				new int[] { R.id.ItemImage, R.id.ItemText });

		/** ���Ź������Item�������� */
		gridview.setAdapter(saMenuItem);
		/** ȥ������Ч�� */
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//��ֹ�ظ������ť
				if(DisplayUitl.isFastDoubleClick()) {
					return;
				}
				
				/** ��ȡ�������������*/
				TextView text = (TextView) arg1.findViewById(R.id.ItemText);
				String str = (String) text.getText();
				
				int i = str.indexOf(":");
				btnName = str.substring(i + 1, str.length());
				
				if (!ydbg) {
					if (btnName.equals("������")) {
//						pd = new ProgressDialog(context);
//						pd.setCancelable(false);
//						Updata();
//						if(!Net.checkURL(Global.getGlobalInstance().getSystemurl())){
//							Toast.makeText(context, "���粻ͨ,������������!", Toast.LENGTH_SHORT).show();
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
				
				if (btnName.equals("�ƶ����") || btnName.equals("�ƶ�OA")) {
					Toast.makeText(context, "��������", Toast.LENGTH_SHORT).show();
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
								Thread.sleep(500);//����  ʹ��Activity���Լ��س�����
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
	 * Description: ��������Ŀǰ�����ã�
	 * @author ��˼Զ
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾
	 * Create at: 2012-12-6 ����09:44:45
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
	 * Description: ����������������Ӧ�ó���
	 * 
	 * void
	 * @author ��˼Զ
	 * Create at: 2012-12-6 ����09:41:28
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

		/** ���¼�� */
		 /*��������������*/
		if (Net.checkNet(context)) {
			
			//Base �ľŹ��񲻻���ּ�����
		
			/**��ȡϵͳ�ķ�������ַ*/
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
					Toast.makeText(context, "�������°汾������£�",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(context, "�����쳣�����Ժ����ԣ�",
							Toast.LENGTH_SHORT).show();
					break;
				}
			} else {
				Toast.makeText(context, "�������ӳ�ʱ�������������ã�",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "�������ӳ�ʱ�������������ã�",
					Toast.LENGTH_SHORT).show();
		}*/
			}
	}
}
