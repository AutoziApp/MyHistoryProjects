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
 * Description: ����ϸ��Ϣ���ݵĶ�ȡ
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ 
 * Create at: 2012-12-5 ����02:50:29
 */
public class BaseDetailedActivity extends BaseActivity {

	/** �����ҳ���TAG���� */
	public  final String TAG = "DetailedActivity";
	/** ������ݵ�Bundle */
	public Bundle bundle;
	/** �������鲼�� */
	public LoadDetailLayout detaillayout ;
	
	/** �ײ��˵������ļ����� */
	public String DetailedBottomMenuStyleFileName;
	/***/
	public  final String INFO_HINT = "InfoHint";
	public  final String INFO = "Info";
	public  int width = 0;

	/*** չʾ������Ҫ�Ĳ����ļ� */
	/** ui_mapuni�����ļ��������һ��Ĳ��� */
	public RelativeLayout parentLayout;
	/** �м䲼�֣�������ʾ���� */
	public LinearLayout detailedLayout;
	public MyScrollView myScrollView;
	
	/** ui_mapuni�еĵײ����� */
	public LinearLayout bottomlayout;
	/** �������صײ��˵��Ĳ����ļ���������Ϊbottomlayout */
	public LinearLayout bottom;

	/** ҵ����̳���IDetailed�ӿ� */
	public IDetailed businessObj;
	/** ���չʾ�������ʽ���� */
	public ArrayList<HashMap<String, Object>> styleDetailed;
	/** �ײ��˵����� */
	public ArrayList<HashMap<String, Object>> bottomlist;
	/** ��ǰ�����id */
	public String itemId;
	/** ��Ŷ�������ļ��� */
	public HashMap<String, Object> detaild;
	/** ���Html�����ַ��� */
	public String otherstr = "";
	/** �Ƿ����޸�״̬��Ĭ��Ϊfalse */
	public boolean isAmend = false;
	/** ��ǰ����ҳ�� */
	public int currentIndex = -1;

	/*** ����ΪȨ��ֵ���������ư�ť */
	/** �ֳ�ִ��Ȩ�� */
	public  final String QX_XCZF = "vmob6A";
	/** ���߼��Ȩ�� */
	public  final String QX_ZXJC = "vmob3A";
	/** ��������Ȩ�� */
	public  final String QX_RWXQ = "vmob2A1B";
	/** �½�����Ȩ�� */
	public  final String QX_XJRW = "vmob2A5B";
	
	public BottomBtnAdapter bottomBtnAdapter;
	DisplayMetrics dm;
	public int screenWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ui_mapuni);
	
		/** ��ȡbundle��Ϣ*/
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
		/** ����ʾ������*/
		if (titleText.equals("��������"))
			super.setCounterButtonVisiable(false);

		/** �Ƿ�Ϊ�޸� */
		if (null != bundle.getString("isAmend")) {
			isAmend = true;
		}
		detailedLayout = (LinearLayout) this.findViewById(R.id.middleLayout);

		//�ײ��˵�λ�ã�Ĭ�ϲ���ʾ
		bottomlayout = (LinearLayout) findViewById(R.id.bottomLayout);
		bottomlayout.invalidate();
		bottomlayout.setOrientation(LinearLayout.VERTICAL);
		bottomlayout.setGravity(Gravity.CENTER_VERTICAL);
		bottomlayout.setVisibility(View.VISIBLE);
		
		//�������鲼��
		  dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		/** �����ť�ĸ߿�*/
//		width = (int) (dm.widthPixels / (double) 4);
		detaillayout = new LoadDetailLayout(this, businessObj, isAmend);
		myScrollView = detaillayout.loadDetailed(itemId);
		styleDetailed = detaillayout.getStyleDetailed();
		detaild = detaillayout.getDetaild();
		
		/** ��Bundle�л�ȡ�ײ��˵���ʽ�ļ�·�� */
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
		
		/** ��ֹ����ݔ�뷨 */
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
		
	}

	
	/**
	 * Description:ɸѡ����Ȩ�޵ĵײ��˵�
	 * 
	 * @author Administrator
	 * Create at: 2012-12-24 ����10:28:07
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
	 * Description: ���صײ��˵�
	 * 
	 * @param bottomMenuStyleFileName
	 *            �ײ��˵����ļ����� void
	 * @author ����� 
	 * Create at: 2012-12-5 ����03:21:16
	 */
	public void loadDetailBottomMenu(String bottomMenuStyleFileName) {}

	/**
	 * Description:����dialog���رգ��Ա㵯����ʾ��Ϣ
	 * 
	 * @param dialog
	 *            Ҫ���õ�dialog void
	 * @author ����� 
	 * Create at: 2012-12-5 ����03:37:42
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
	 * Description: �ر�dialog
	 * 
	 * @param dialog
	 *            Ҫ���õ�dialog void
	 * @author ����� 
	 * Create at: 2012-12-5 ����03:38:00
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
	 * Description:�ײ��˵�������
	 * @author �����
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾
	 * Create at: 2012-12-21 ����11:51:52
	 */
	public class BottomBtnAdapter extends BaseAdapter{
		
		/**�ײ��˵�����*/
		ArrayList<HashMap<String, Object>> bottomList;
		Context context;
		int displayWidth;
		int width ;
		/**
		 * ���췽��
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
