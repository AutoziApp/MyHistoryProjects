package com.mapuni.android.base;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.TunnelRefusedException;
import org.dom4j.DocumentException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mapuni.android.base.adapter.SortAdapter;
import com.mapuni.android.base.business.BaseDataSync;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.business.BaseUsers;
import com.mapuni.android.base.interfaces.IList;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.dataprovider.XmlHelper;

/**
 * FileName: BaseActivity.java Description: �������Activity
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����11:35:37
 */

@SuppressLint("ResourceAsColor")
public class BaseActivity extends FragmentActivity {
	/** ����һ��Tag���� */
	private final String TAG = "BaseActivity";

	/** �����Activity���貼�� */
	public RelativeLayout mapuniLinearLayout = null;
	private RelativeLayout relativeLayoutTitle = null;

	/** ȫ��Dialog�������ť���ֵȴ��� */
	protected Dialog dl = null;

	/** ���ϽǵĿ�ݰ�ť */
	public ImageView backImg = null;
	/** ���ϽǵĿ�ݰ�ť */
	public ImageView syncImg = null;
	/** �������ϵĲ�ѯͼƬ��ť */
	public ImageView queryImg = null;

	/** �������༭ͼ�� */
	public ImageView editImg = null;
	/** ����������ͼ�� */
	public ImageView saveImg = null;
	/** ����������ͼͼ�� */
	// public ImageView processImg = null;

	/** ���ÿ�ݼ���TableRow��������ˢ�£������������ҡ�����������������������ݰ�ť��ҪȨ�޿��� */
	public TableRow refurbish_tb, counterTableRow, queryTableRow;
	/** ����һ�����ֲ��� */
	public RelativeLayout.LayoutParams paramsRelativeLayout;

	/** �Ƿ���ʾˢ�°�ť��Ĭ��Ϊfalse */
	private boolean refurbishbool = false;
	/** �Ƿ���ʾ��������Ĭ��ֵΪfalse */
	private boolean showCounter = false;

	/** �Ƿ���ʾ��ݲ˵���ѯ��Ĭ��ֵΪfalse */
	private boolean showQuery = false;
	/** �Ƿ���ʾ�༭��ť��Ĭ��ֵΪfalse */
	private boolean showEdit = false;

	/** �Ƿ�����������Ĺ��ܣ�Ĭ��Ϊfalse�����Ϊtrue���ѯ���ܱ�Ϊ������ӹ��� */
	public boolean isAddTask = false;
	/** �Ƿ�������༭ */
	public boolean showedittask = false;
	/** �Ƿ��������ҵ */
	public boolean isAddCompany = false;
	/** �Ƿ���ʾ�����ҵ */
	private final boolean isShowAddCompany = false;

	/** ȫ�ֲ��ҵ�Ȩ�� */
	private final String QJCZ_QX = "vmob15A";

	/** ���ɷ��棬Ӧ���ֲᣬȫ������ */
	TableRow fagui_tb, yingji_tb;

	/** ������Layout */
	private SlideLayout slideLayout;
	/** �����ListView */
	private ListView sortListView;
	/** dialog��listView������ */
	public ArrayList<HashMap<String, Object>> listData;
	private TextView username;
	private String userName;

	/**
	 * �����м䲼�֣��Թ��������
	 */
	protected LinearLayout middleLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		OtherTools.showLog("---------------"
				+ getClass().getPackage().getName() + "."
				+ this.getClass().getSimpleName() + "---------------");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Description: ���ñ��Ⲽ���Ƿ�ɼ�
	 * 
	 * @param isShow
	 *            �Ƿ�ɼ�������true���ɼ���false�����ɼ� void
	 * @author ����� Create at: 2012-12-5 ����01:53:41
	 */
	public void setTitleLayoutVisiable(boolean isShow) {
		if (!isShow) {
			relativeLayoutTitle.setVisibility(View.GONE);
		} else {
			relativeLayoutTitle.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Description: ���ÿ�ݲ˵��Ƿ�ɼ�
	 * 
	 * @param isShow
	 *            ture���ɼ���false�����ɼ� void
	 * @author ����� Create at: 2012-12-5 ����01:54:29
	 */
	public void setSynchronizeButtonVisiable(boolean isShow) {
		if (!isShow) {
			syncImg.setVisibility(View.INVISIBLE);
			// syncImg.setVisibility(View.VISIBLE);
			Log.i("SSSS", "meiyou");
		} else {
			syncImg.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Description: ���ñ�������ѯ��ť�Ƿ�ɼ�--Search
	 * 
	 * @param isShow
	 *            ture���ɼ���false�����ɼ� void
	 * @author ����� Create at: 2012-12-5 ����01:56:02
	 */
	public void setSearchButtonVisiable(boolean isShow) {
		if (!isShow) {
			queryImg.setVisibility(View.GONE);
		} else {
			queryImg.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Description: ���ü������˵��Ƿ�ɼ�
	 * 
	 * @param isShow
	 *            ture���ɼ���false�����ɼ� void
	 * @author ����� Create at: 2012-12-5 ����01:56:47
	 */
	public void setCounterButtonVisiable(boolean isShow) {
		showCounter = isShow;
	}

	/**
	 * Description: ����ˢ�°�ť�Ƿ�ɼ�
	 * 
	 * @param isShow
	 *            ture���ɼ���false�����ɼ� void
	 * @author ����� Create at: 2012-12-5 ����01:57:20
	 */
	public void setrefurbishButtonVisiable(boolean isShow) {
		refurbishbool = isShow;
	}

	/*
	 * ֪ʶ�⣬Ŀǰû���� public void setknowledgeButtonVisiable(boolean isShow) {
	 * showKnowledge = isShow; }
	 */

	/**
	 * Description: ���ÿ�ݲ˵���ѯ--Query
	 * 
	 * @param isShow
	 *            ture���ɼ���false�����ɼ� void
	 * @author ����� Create at: 2012-12-5 ����01:57:20
	 */
	public void setQueryButtonVisiable(boolean isShow) {
		if (DisplayUitl.getAuthority(QJCZ_QX)) {
			showQuery = isShow;
		}

	}

	/**
	 * Description:�����Ƿ���ʾ�༭��ť
	 * 
	 * @param isShow
	 * @author ����� Create at: 2012-12-6 ����11:21:42
	 */
	public void setEditButtonVisiable(boolean isShow) {
		showEdit = isShow;
		editImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	/**
	 * Description:�༭��ť��Ӽ����¼�
	 * 
	 * @param clickListener
	 * @author ����� Create at: 2012-12-6 ����1:05:29
	 */
	public void setEditButtonListener(OnClickListener clickListener) {
		editImg.setOnClickListener(clickListener);
	}

	/**
	 * ���ñ���ļ��׷���
	 * 
	 * @param title
	 */
	public void SetBaseStyle(String title) {
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), title);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
	}

	/**
	 * Description: �������������������
	 * 
	 * LinearLayout(top) RelativeLayout TextView(title) Button(syncBtn)
	 * RelativeLayout LinearLayout(top)
	 * 
	 * @param mapuniLayout
	 *            ����
	 * @param title
	 *            Ҫ��ʾ�ı������� void
	 * @author ����� Create at: 2012-12-5 ����01:58:53
	 */
	public void SetBaseStyle(RelativeLayout mapuniLayout, String title) {
		mapuniLinearLayout = mapuniLayout;

		/** ���������� */
		LinearLayout topLayout = (LinearLayout) mapuniLinearLayout
				.findViewById(R.id.topLayout);
		topLayout.setGravity(Gravity.CENTER_VERTICAL);
		topLayout.setBackgroundResource(R.color.topbar_background);
		;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		int queryImgId = 156189146;
		int syncImgId = 156189147;
		int titleId = 156189148;

		/** ����Ӳ������� */
		relativeLayoutTitle = new RelativeLayout(this);
		relativeLayoutTitle.setPadding(15, 0, 0, 0);

		relativeLayoutTitle.setGravity(RelativeLayout.CENTER_IN_PARENT);
		topLayout.addView(relativeLayoutTitle, params);

		/** �����ı� */
		paramsRelativeLayout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
		// paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsRelativeLayout.addRule(RelativeLayout.LEFT_OF, queryImgId);

		TextView titleTextView = new TextView(this);
		titleTextView.setText(title);
		titleTextView.setId(titleId);
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setTextSize(20.0f);
		titleTextView.setMaxLines(1);
		titleTextView.setEllipsize(TruncateAt.END);
		/** �Ӵ� */
		// titleTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		/** �Ӵ�--����ͬ�� */
		// titleTextView.getPaint().setFakeBoldText(true);
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		ll.gravity = Gravity.CLIP_VERTICAL;
		relativeLayoutTitle.addView(titleTextView, paramsRelativeLayout);

		/** ������ť */
		paramsRelativeLayout = new RelativeLayout.LayoutParams(80, 80);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.setMargins(0, 0, 80, 0);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		// paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsRelativeLayout.addRule(RelativeLayout.LEFT_OF, syncImgId);

		queryImg = new ImageView(this);
		queryImg.setId(queryImgId);
		/** �������ͼ�� */
		if (isAddTask) {
			queryImg.setImageDrawable(getResources().getDrawable(
					R.drawable.base_add_task_new));
		} else {
			/** ���򣬲�ѯͼ�� */
			queryImg.setImageDrawable(getResources().getDrawable(
					R.drawable.base_icon_menu_query_new));
		}
		queryImg.setVisibility(View.INVISIBLE);
		queryImg.setPadding(10, 7, 10, 7);
		relativeLayoutTitle.addView(queryImg, paramsRelativeLayout);

		// editImg = new ImageView(this);
		// editImg.setId(queryImgId);
		// editImg.setImageResource(R.drawable.base_icon_edit);
		// editImg.setVisibility(View.GONE);
		// relativeLayoutTitle.addView(editImg, paramsRelativeLayout);

		paramsRelativeLayout = new RelativeLayout.LayoutParams(80, 80);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);

		syncImg = new ImageView(this);
		syncImg.setId(syncImgId);
		if (SYNCIAM) {
			syncImg.setVisibility(View.VISIBLE);
		} else {
			syncImg.setVisibility(View.INVISIBLE);
		}
		syncImg.setImageDrawable(getResources().getDrawable(R.drawable.more));
		syncImg.setPadding(10, 5, 10, 5);
		/** ����ݰ�ť��Ӽ����¼� */
		syncImg.setOnClickListener(syncBtnListener);
		relativeLayoutTitle.addView(syncImg, paramsRelativeLayout);

		paramsRelativeLayout = new RelativeLayout.LayoutParams(70, 80);
		paramsRelativeLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsRelativeLayout.addRule(RelativeLayout.CENTER_VERTICAL);
		// paramsRelativeLayout.addRule(RelativeLayout.LEFT_OF, titleId);

		backImg = new ImageView(this);
		if (BACK_ISSHOW) {
			backImg.setVisibility(View.VISIBLE);
		} else {
			backImg.setVisibility(View.GONE);
		}
		backImg.setImageDrawable(getResources().getDrawable(R.drawable.back));
		backImg.setPadding(0, 5, 10, 5);
		/** ����ݰ�ť��Ӽ����¼� */
		backImg.setOnClickListener(backBtnListener);
		relativeLayoutTitle.addView(backImg, paramsRelativeLayout);
	}

	private boolean SYNCIAM = false;

	public void SetSYNCIAM(boolean isshow) {
		this.SYNCIAM = isshow;
	}

	private boolean BACK_ISSHOW = true;

	public void setBACK_ISSHOW(boolean isShow) {
		this.BACK_ISSHOW = isShow;
	}

	/**
	 * ��ݲ˵����ܵ���¼����ذ�ť
	 */
	private final OnClickListener backBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/**
	 * Description: ���صײ����ܲ˵�
	 * 
	 * @param dataBottomMenuList
	 *            �ײ��˵�����Դ
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 *             void
	 * @author ����� Create at: 2012-12-5 ����02:05:20
	 */
	public void loadBottomMenu(
			ArrayList<HashMap<String, Object>> dataBottomMenuList)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException {
		TableLayout bottomMenuTable = new TableLayout(this);

		LinearLayout bottomLayout = (LinearLayout) mapuniLinearLayout
				.findViewById(R.id.bottomLayout);
		bottomLayout.setVisibility(0);
		bottomLayout.addView(bottomMenuTable);

		bottomMenuTable.setGravity(Gravity.BOTTOM);
		bottomMenuTable.setColumnStretchable(1, true);
		TableRow row = new TableRow(this);
		row.setGravity(Gravity.CENTER_HORIZONTAL);
		bottomMenuTable.addView(row);
		ImageView imageView;
		for (int i = 0; i < dataBottomMenuList.size(); i++) {
			Map<String, Object> menuItemHashMap = dataBottomMenuList.get(i);
			String img = (String) menuItemHashMap.get("img");
			final String aimType = (String) menuItemHashMap.get("aimType");
			final String entityclass = (String) menuItemHashMap
					.get("entityclass");
			imageView = new ImageView(this);
			imageView.setId(i);
			int imageID = this.getResources().getIdentifier(img, "drawable",
					this.getPackageName());
			imageView.setImageResource(imageID);

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Class<?> aimClassDefinition = null;
					try {
						aimClassDefinition = Class.forName(aimType);
					} catch (ClassNotFoundException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					}
					Intent intent = new Intent();

					Class<?> entityClassDefinition = null;
					try {
						entityClassDefinition = Class.forName(entityclass);
					} catch (ClassNotFoundException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					}
					IList iList = null;
					try {
						iList = (IList) entityClassDefinition.newInstance();
					} catch (InstantiationException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
						e.printStackTrace();
					}

					intent.setClass(BaseActivity.this, aimClassDefinition);
					startActivity(intent);

				}
			});
			row.addView(imageView);
		}
	}

	/**
	 * ��ݼ������ļ����¼�
	 */
	class counterBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			/*
			 * Intent intent = new Intent(BaseActivity.this,
			 * CalculatorActivity.class); startActivity(intent);
			 */
			dl.cancel();
		}
	};

	/**
	 * ˢ�°�ť�ļ����¼�
	 */
	class refurbishBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			refurbish();
			dl.cancel();
		}
	};

	/**
	 * ���ҿ�ݼ��ļ����¼�
	 */
	class queryBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			queryDate();
			dl.cancel();
		}
	};

	/**
	 * ͨѶ¼��ݼ��ļ����¼�
	 */
	class communicateBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			/*
			 * Intent intent=new Intent();
			 * intent.setClassName("com.mapuni.android.MobileEnforcement",
			 * "com.mapuni.android.MobileEnforcement.MainActivity");
			 * 
			 * SharedPreferences mPreferences =
			 * BaseActivity.this.getSharedPreferences("app_info",
			 * Context.MODE_WORLD_WRITEABLE); HashMap<String, String> appInfoMap
			 * = (HashMap<String, String>) mPreferences.getAll();
			 * intent.putExtra("appinfo", appInfoMap);
			 * 
			 * intent.putExtra("ywl", "ͨѶ¼"); intent.putExtra("isbase", true);
			 * startActivity(intent);
			 */

			Intent intent;
			BaseUsers users = new BaseUsers(BaseActivity.this);
			;
			HashMap<String, Object> filterMap = new HashMap<String, Object>();
			filterMap.put("cmy", "");
			users.setFilter(filterMap);
			Bundle bundle = new Bundle();
			bundle.putBoolean("IsShowTitle", true);
			bundle.putSerializable("BusinessObj", users);
			intent = new Intent(BaseActivity.this, QueryListActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			dl.cancel();
		}
	};

	/**
	 * ������ҳ��ݼ��ļ����¼�
	 */
	class backmainBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.cancel();
			Intent intent = new Intent("com.mapuni.android.workservice");
			/** ���͹㲥 */
			BaseActivity.this.sendBroadcast(intent);
		}
	};

	/**
	 * �����׼��ݼ��ļ����¼�
	 */
	class LawBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			/** �����׼ */
			Intent intent = new Intent(BaseActivity.this, GridActivity.class);
			intent.putExtra("isShortcut", true);
			intent.putExtra("ywl", "FLFGXX");
			intent.putExtra("packageName", "helper");
			Bundle nextbundle = new Bundle();
			nextbundle.putBoolean("IsShowTitle", true);
			nextbundle.putBoolean("IsMain", false);
			nextbundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE, false);
			intent.putExtras(nextbundle);
			intent.setAction("FGBZ");
			startActivity(intent);
		}
	};

	/**
	 * Ӧ���ֲ��ݼ��ļ����¼�
	 */
	class YinjiBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			/** Ӧ���ֲ� */
			dl.dismiss();
			Intent intent = new Intent(BaseActivity.this, GridActivity.class);
			intent.putExtra("isShortcut", true);
			intent.putExtra("ywl", "YJSCXX");
			intent.putExtra("packageName", "helper");
			Bundle nextbundle = new Bundle();
			nextbundle.putBoolean("IsShowTitle", true);
			nextbundle.putBoolean("IsMain", false);
			nextbundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE, false);
			intent.putExtras(nextbundle);
			intent.setAction("FGBZ");
			startActivity(intent);
		}
	};

	/**
	 * ����ֲ��ݼ��ļ����¼�
	 */
	class addCompanyBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// Intent intent = new Intent();
			// intent.setClass(BaseActivity.this, AddCompanyActivity.class);
			// startActivity(intent);
		}
	};

	/**
	 * ȫ��������ݼ��ļ����¼�
	 */
	class queryAllBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (dl != null) {
				dl.dismiss();
			}
			// Intent intent = new Intent();
			// intent.setClassName("com.mapuni.android.MobileEnforcement",
			// "com.mapuni.android.ui.GlobalSearchActivity");
			// startActivity(intent);
		}
	};

	/**
	 * ִ������ͨ��ݼ��ļ����¼�
	 */
	class LawKnowBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			Intent intent = new Intent();
			intent.setClassName("com.mapuni.android.MobileEnforcement",
					"com.mapuni.android.helper.LawKnowAllActivity");
			startActivity(intent);
		}
	};

	/**
	 * ϵͳ�����ݼ��ļ����¼�
	 */
	class settingBtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dl.dismiss();
			Intent intent = new Intent(BaseActivity.this, GridActivity.class);
			Bundle bundle = new Bundle();
			intent.putExtra("isShortcut", true);
			intent.putExtra("ywl", "QTXX");
			intent.putExtra("packageName", "setting");
			bundle.putBoolean("IsShowTitle", true);
			bundle.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_DATE_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE, true);
			bundle.putBoolean(ListActivity.IS_LEFTICON_VISIBLE, true);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	/**
	 * ����
	 */
	public void queryDate() {

	}

	/**
	 * ˢ��
	 */
	public void refurbish() {

	}

	/**
	 * ��ݲ˵����ܵ���¼�
	 */
	private final OnClickListener syncBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initListData();
			slideLayout.snapToScreen(1, false);
		}
	};

	/**
	 * Descreption:����_��¼��Ϣ����
	 * 
	 * @1.������ʼ���Զ��廬�������ݼ���
	 * @2.���û���¼�����û�����ֵ��������listview��ͷ��
	 * @author ��ѧ÷
	 * */
	public void initListData() {
		/** ��ʼ���Զ��廬����� */
		slideLayout = (SlideLayout) findViewById(R.id.slidelayout);
		sortListView = (ListView) findViewById(R.id.sort_listview);
		/** ��ȡ�û���¼�����ƣ���������_��¼�����username */
		username = (TextView) findViewById(R.id.username);
		userName = Global.getGlobalInstance().getUsername();
		username.setText(userName);

		/** ��listview������� */
		listData = new ArrayList<HashMap<String, Object>>();
		listData = getMoreMenu("style_MoreMenu.xml", "item");

		ArrayList<HashMap<String, Object>> moreMenu = new ArrayList<HashMap<String, Object>>();

		sortListView.setAdapter(new SortAdapter(BaseActivity.this, listData));

		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					try {

						// ����ͬ��
						BaseDataSync dataSync = (BaseDataSync) BaseObjectFactory
								.createBaseObject(BaseDataSync.BusinessClassName);
						/** ��ʼ������bundle����bundleװ��Intent������תҳ�� */
						Bundle bundle = new Bundle();
						bundle.putBoolean("IsShowTitle", true);
						bundle.putBoolean("IsShowSyncBtn", false);
						bundle.putString("TitleText", "ͬ������");
						bundle.putSerializable("BusinessObj", dataSync);
						bundle.putBoolean("isShowCheckBox", true);
						bundle.putBoolean("isShowDate", false);
						bundle.putBoolean("isShowRighticon", false);

						Intent intent = new Intent(BaseActivity.this,
								DataSyncActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);

					} catch (ClassNotFoundException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
					} catch (IllegalAccessException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
					} catch (InstantiationException e) {
						ExceptionManager.WriteCaughtEXP(e, "BaseActivity");
					}

					break;
				case 1:
					// ͨѶ¼
					/*
					 * Intent intent; BaseUsers users = new
					 * BaseUsers(BaseActivity.this); HashMap<String, Object>
					 * filterMap = new HashMap<String, Object>();
					 * filterMap.put("cmy", ""); users.setFilter(filterMap);
					 * Bundle bundle = new Bundle();
					 * bundle.putBoolean("IsShowTitle", true);
					 * bundle.putSerializable("BusinessObj", users); intent =
					 * new Intent(BaseActivity.this, QueryListActivity.class);
					 * intent.putExtras(bundle); startActivity(intent); break;
					 */
					Intent intent5 = new Intent(BaseActivity.this,
							GridActivity.class);
					Bundle bundle5 = new Bundle();
					intent5.putExtra("isShortcut", true);
					intent5.putExtra("ywl", "QTXX");
					intent5.putExtra("packageName", "setting");
					bundle5.putBoolean("IsShowTitle", true);
					bundle5.putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
					bundle5.putBoolean(ListActivity.IS_DATE_VISIBLE, true);
					bundle5.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE, true);
					bundle5.putBoolean(ListActivity.IS_LEFTICON_VISIBLE, true);
					intent5.putExtras(bundle5);
					startActivity(intent5);
					break;

				case 2:
					Intent intent2 = new Intent();
					intent2.setClassName(
							"com.mapuni.android.MobileEnforcement",
							"com.mapuni.android.enterpriseArchives.AddBusinessActivity");
					startActivity(intent2);
					break;
				//
				// Intent intent2 = new Intent(BaseActivity.this,
				// GridActivity.class);
				// intent2.putExtra("isShortcut", true);
				// intent2.putExtra("ywl", "FLFGXX");
				// intent2.putExtra("packageName", "helper");
				// Bundle nextbundle = new Bundle();
				// nextbundle.putBoolean("IsShowTitle", true);
				// nextbundle.putBoolean("IsMain", false);
				// nextbundle
				// .putBoolean(ListActivity.IS_CONTENT_VISIBLE, true);
				// nextbundle.putBoolean(ListActivity.IS_DATE_VISIBLE,
				// false);
				// intent2.putExtras(nextbundle);
				// intent2.setAction("FGBZ");
				// startActivity(intent2);
				// break;
				/** �������ִ���ֲ��ļ�Ŀ¼ */
				/*
				 * String fjpath = Global.HJJCZFSC_PATH; File files = new
				 * File(fjpath); String fjs[] = files.list(); if (fjs == null) {
				 * Toast.makeText(BaseActivity.this, "���ɷ����׼�ļ���Ϊ�գ�", 0) .show();
				 * break; } else { Intent intent2 = new Intent();
				 * intent2.setClassName( "com.mapuni.android.MobileEnforcement",
				 * "com.mapuni.android.uitl.LNFLFGExpandListActivity");
				 * startActivity(intent2); break; }
				 */

				// case 3:
				// // Ӧ���ֲ�
				// Intent intent3 = new Intent(BaseActivity.this,
				// GridActivity.class);
				// intent3.putExtra("isShortcut", true);
				// intent3.putExtra("ywl", "YJSCXX");
				// intent3.putExtra("packageName", "helper");
				// Bundle nextbundle1 = new Bundle();
				// nextbundle1.putBoolean("IsShowTitle", true);
				// nextbundle1.putBoolean("IsMain", false);
				// nextbundle1.putBoolean(ListActivity.IS_CONTENT_VISIBLE,
				// true);
				// nextbundle1.putBoolean(ListActivity.IS_DATE_VISIBLE,
				// false);
				// intent3.putExtras(nextbundle1);
				// intent3.setAction("FGBZ");
				// startActivity(intent3);
				// break;

				case 3:
					/*
					 * // ִ������ͨ Intent intent6 = new Intent();
					 * intent6.setClassName(
					 * "com.mapuni.android.MobileEnforcement",
					 * "com.mapuni.android.helper.LawKnowAllActivity");
					 * startActivity(intent6);
					 * 
					 * break;
					 */
					// ϵͳ����
					/*
					 * Intent intent5 = new Intent(BaseActivity.this,
					 * GridActivity.class); Bundle bundle5 = new Bundle();
					 * intent5.putExtra("isShortcut", true);
					 * intent5.putExtra("ywl", "QTXX");
					 * intent5.putExtra("packageName", "setting");
					 * bundle5.putBoolean("IsShowTitle", true);
					 * bundle5.putBoolean(ListActivity.IS_CONTENT_VISIBLE,
					 * true); bundle5.putBoolean(ListActivity.IS_DATE_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_LEFTICON_VISIBLE,
					 * true); intent5.putExtras(bundle5);
					 * startActivity(intent5); break;
					 */
					break;
				case 4:
					// ϵͳ����
					/*
					 * Intent intent5 = new Intent(BaseActivity.this,
					 * GridActivity.class); Bundle bundle5 = new Bundle();
					 * intent5.putExtra("isShortcut", true);
					 * intent5.putExtra("ywl", "QTXX");
					 * intent5.putExtra("packageName", "setting");
					 * bundle5.putBoolean("IsShowTitle", true);
					 * bundle5.putBoolean(ListActivity.IS_CONTENT_VISIBLE,
					 * true); bundle5.putBoolean(ListActivity.IS_DATE_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_RIGHTICON_VISIBLE,
					 * true);
					 * bundle5.putBoolean(ListActivity.IS_LEFTICON_VISIBLE,
					 * true); intent5.putExtras(bundle5);
					 * startActivity(intent5);
					 */
					/*
					 * Intent intent5 = new Intent(); intent5.setClassName(
					 * "com.mapuni.android.MobileEnforcement",
					 * "com.mapuni.android.setting.QTXX");
					 * startActivity(intent5);
					 */
					break;

				default:
					finish();
					break;
				}
				slideLayout.snapToScreen(0, false);
			}

		});

	}

	/**
	 * Description: ��ݲ˵���������Dialog
	 * 
	 * void
	 * 
	 * @author ����� Create at: 2012-12-5 ����02:08:19 �޸�ʱ�䣺2013-04-17
	 */
	public void dialog() {

		LayoutInflater li = LayoutInflater.from(BaseActivity.this);
		View va = li.inflate(R.layout.base_dialog, null);
		dl = new Dialog(BaseActivity.this, R.style.FullScreenDialog);
		/** ��ȡ����˵��������ļ� */
		ArrayList<HashMap<String, Object>> moreMenu = getMoreMenu(
				"style_MoreMenu.xml", "item");
		/** ������ */
		int count = 0;
		/** dialog���� */
		LinearLayout gd_menu = (LinearLayout) va.findViewById(R.id.icon_gd);
		/** �õ�����˵�����Ϣ����ѭ����ӵ�dialog�Ĳ����У�������Ӽ����¼� */
		for (HashMap<String, Object> menu : moreMenu) {
			if (menu.get("menuname").toString().equals("�����ҵ")
					&& !isShowAddCompany
					|| menu.get("menuname").toString().equals("������")
					&& !showCounter
					|| menu.get("menuname").toString().equals("ˢ��")
					&& !refurbishbool) {
				continue;
			}
			/** ����ǵ�һ����ť����ӷָ�����ͼ */
			if (count != 0) {
				View view = new View(this);
				view.setBackgroundResource(R.drawable.base_pop_line);
				view.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, 1,
						(float) 1 / 48));
				gd_menu.addView(view);
			}

			TableRow tableRow = new TableRow(this);
			tableRow.setOrientation(LinearLayout.HORIZONTAL);
			tableRow.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 12, (float) 1.0));
			tableRow.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			linearLayout.setPadding(15, 15, 0, 0);
			ImageView leftimg = new ImageView(this);
			leftimg.setImageBitmap(geBitmaptRes(menu.get("lefticon").toString()));
			linearLayout.addView(leftimg);
			try {
				/** ͨ���õ������������֣��õ����Ӧ�ļ����ڲ������ */
				try {
					linearLayout.setOnClickListener((OnClickListener) Class
							.forName(
									"com.mapuni.android.base.BaseActivity$"
											+ menu.get("listenername")
													.toString())
							.getDeclaredConstructors()[0].newInstance(this));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

			TextView textView = new TextView(this);
			textView.setText(menu.get("menuname").toString());
			textView.setTextColor(getColorRes(menu.get("textcolor").toString()));
			textView.setTextSize(Float.parseFloat(menu.get("textsize")
					.toString()));
			/** �Ӵ�--����ͬ�� */
			textView.getPaint().setFakeBoldText(
					Boolean.parseBoolean(menu.get("fakeboldtext").toString()));
			linearLayout.addView(textView);

			tableRow.addView(linearLayout);
			gd_menu.setPadding(0, 15, 0, 15);
			gd_menu.addView(tableRow);
			count++;
		}
		gd_menu.setLayoutParams(new LinearLayout.LayoutParams(250, count * 80,
				(float) 0.0));

		/** ����dialog��λ�� */
		Window win = dl.getWindow();
		android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
		/** ��ȡ�ֻ��ֱ��� */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		/** ����ֻ��ĸ߿� */
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		LinearLayout toplayout = (LinearLayout) findViewById(R.id.topLayout);
		int topHeight = toplayout.getHeight();

		params.x = width / 2 - 125 - 10 + 300;
		params.y = (int) -((double) height / 2 - topHeight - count * 80 / 2 - (topHeight - 58) * 1);
		// params.y=-470;
		win.setAttributes(params);
		dl.setCanceledOnTouchOutside(true);
		dl.setContentView(va);
		dl.show();

		if (!Global.getGlobalInstance().isShowGd) {
			System.out.println(Global.getGlobalInstance().isShowGd
					+ "�Ƿ���ʾ����������ӹ���");
			fagui_tb.setVisibility(View.GONE);
			yingji_tb.setVisibility(View.GONE);

		}
	}

	/**
	 * Description: �������˳��������� void
	 * 
	 * @author ����� Create at: 2012-12-5 ����02:25:21
	 */
	public void myExit() {
		Intent intent = new Intent();
		intent.setAction("EXIT");
		this.sendBroadcast(intent);
		finish();
	}

	/**
	 * Description: ��ȡ����˵������ļ�����
	 * 
	 * @param xml
	 *            xml�ļ�������
	 * @param nodeFather
	 *            ���ڵ�
	 * @return ArrayList<HashMap<String, Object>>
	 * @author ����� Create at: 2013-4-15 ����11:20:34
	 */
	protected ArrayList<HashMap<String, Object>> getMoreMenu(String xml,
			String nodename) {
		ArrayList<HashMap<String, Object>> MoreMenu = null;
		InputStream stream = null;
		try {
			stream = BaseActivity.this.getResources().getAssets().open(xml);
			MoreMenu = XmlHelper.getList(stream, nodename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return MoreMenu;

	}

	/**
	 * Description: ��ȡ�б��ͼƬ
	 * 
	 * @param name
	 *            ��Ƭ������
	 * @return ����������Ƭ Bitmap
	 * @author xgf Create at: 2012-11-30 ����11:30:37
	 */
	public Bitmap geBitmaptRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "drawable",
				appInfo.packageName);
		return BitmapFactory.decodeResource(this.getResources(), resID);
	}

	/**
	 * Description: ��ȡ��ɫ����Դid
	 * 
	 * @param name
	 *            ��ɫ������
	 * @return ����������ɫ��Դid int
	 * @author ����� Create at: 2012-11-30 ����11:30:37
	 */
	public int getColorRes(String name) {
		ApplicationInfo appInfo = this.getApplicationInfo();
		int resID = this.getResources().getIdentifier(name, "color",
				appInfo.packageName);
		return resID;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

}
