package com.mapuni.android.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseDetailedActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.BaseObjectFactory;
import com.mapuni.android.base.controls.listview.HorizontialListView;
import com.mapuni.android.base.util.ExceptionManager;
import com.mapuni.android.business.MBXX;
import com.mapuni.android.business.QYJBXX;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.business.SpinnerItem;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.QDJCActivity;
import com.mapuni.android.enterpriseArchives.EnterpriseArchivesActivitySlide;
import com.mapuni.android.taskmanager.TaskRegisterActivity;

/**
 * FileName: DetailedActivity.java Description: ����ϸ��Ϣ���ݵĶ�ȡ
 * 
 * @author �����
 * @Version 1.3.4
 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-5 ����02:50:29
 */
public class DetailedActivity extends BaseDetailedActivity {

	/**
	 * Description: ���صײ��˵�
	 * 
	 * @param bottomMenuStyleFileName
	 *            �ײ��˵����ļ����� void
	 * @author ����� Create at: 2012-12-5 ����03:21:16
	 */
	@Override
	public void loadDetailBottomMenu(String bottomMenuStyleFileName) {
		if (bottomMenuStyleFileName.equals("������ͬʱ������Ϣ����") || bottomMenuStyleFileName.equals("����������Ϣ����") || bottomMenuStyleFileName.equals("�ŷ�Ͷ����Ϣ����")
				|| bottomMenuStyleFileName.equals("����������Ϣ����") || bottomMenuStyleFileName.equals("�����շ���Ϣ����") || bottomMenuStyleFileName.equals("�������֤��Ϣ����")) {
			if (detaild.get("qymc") != null && !"".equals(detaild.get("qymc").toString())) {
				if (bottomBtnAdapter == null || bottomBtnAdapter.getCount() == 0) {
					bottomlayout.setVisibility(View.GONE);
				} else {
					HorizontialListView listview = new HorizontialListView(DetailedActivity.this);
					listview.setAdapter(bottomBtnAdapter);
					listview.setOnItemClickListener(bottomMenuItemClickListener);
					// listview.setD
					listview.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 30));
					bottomlayout.addView(listview);
				}
			} else {
				bottomlayout.setVisibility(View.GONE);
			}
		} else {
			bottomlayout.setVisibility(View.GONE);
		}
	}

	OnItemClickListener bottomMenuItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			ArrayList<HashMap<String, Object>> result = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select * from T_WRY_QYJBXX where QYMC='" + detaild.get("qymc").toString() + "'");
			final String Guid = result.get(0).get("guid").toString();
			final String qydm = result.get(0).get("qydm").toString();
			Bundle nextBundle = new Bundle();
			QYJBXX qyjbxx;
			switch (view.getId()) {
			case 1:// �ֳ�ִ��
				/** ִ��ģ�� */
				MBXX mbxx = new MBXX();
				HashMap<String, Object> fliterHashMap = new HashMap<String, Object>();
				fliterHashMap.put("status", "1");
				ArrayList<HashMap<String, Object>> mbArrayList = mbxx.getDataList(fliterHashMap);
				AlertDialog.Builder ab = new AlertDialog.Builder(DetailedActivity.this);
				ab.setTitle("�ֳ�ִ��ģ��");
				ab.setIcon(getResources().getDrawable(R.drawable.icon_mapuni_white));
				LinearLayout ll = new LinearLayout(DetailedActivity.this);
				TextView tv = new TextView(DetailedActivity.this);
				tv.setText("��ѡ��ģ�壺");
				ll.addView(tv);
				final Spinner spinner = new Spinner(DetailedActivity.this);
				ll.addView(spinner);
				List<SpinnerItem> mbList = new ArrayList<SpinnerItem>();
				SpinnerItem item = new SpinnerItem("", "--��ѡ��--", 0);
				mbList.add(item);
				int i = 1;
				for (Map<String, Object> map : mbArrayList) {
					String code = map.get("tid").toString();
					String name = map.get("tname").toString();
					item = new SpinnerItem(code, name, i);
					mbList.add(item);
					i++;
				}
				ArrayAdapter<SpinnerItem> mbAdapter = new ArrayAdapter<SpinnerItem>(DetailedActivity.this, android.R.layout.simple_spinner_item, mbList);
				mbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(mbAdapter);
				ab.setView(ll);
				ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						distoryDialog(dialog);
					}
				});
				ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						keepDialog(arg0);
						SpinnerItem mbitem = (SpinnerItem) spinner.getSelectedItem();
						String tid = mbitem.getCode();
						if (tid.equals("") || tid == null) {
							Toast.makeText(DetailedActivity.this, "��ѡģ��!", Toast.LENGTH_SHORT).show();
						} else {
							try {
								RWXX rwxxmanager = new RWXX();
								String guid = UUID.randomUUID().toString();
								int cont = 0;

								String ssks = Global.getGlobalInstance().getDepId();
								String useridStr = Global.getGlobalInstance().getUserid();
								String bjqx = "";
								if (detaild.get("qymc").toString() != null) {
									String rwbh = rwxxmanager.insertRWXX("", bjqx, false, guid, cont, "", detaild.get("qymc").toString(), detaild.get("qymc").toString(), ssks, "",
											useridStr, tid, "xczf", 0, "", "");
									if (rwbh != null && !"".equals(rwbh)) {
										/** ������ҵ�����õ�ǰ���������guid */
										ArrayList<HashMap<String, Object>> listTask = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
												"select guid from T_YDZF_RWXX where RWBH='" + rwbh + "'");
										String rwid = (String) listTask.get(0).get("guid");
										Intent intent = new Intent(DetailedActivity.this, QDJCActivity.class);
										Bundle bundle = new Bundle();
										RWXX rwxx = new RWXX();
										rwxx.setCurrentID(rwid);
										bundle.putSerializable("BusinessObj", rwxx);
										intent.putExtras(bundle);
										startActivity(intent);
										distoryDialog(arg0);
									} else {
										Toast.makeText(DetailedActivity.this, "��������ʧ��!", Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(DetailedActivity.this, "��ҵ��ϢΪ�գ�", 0).show();
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				});
				ab.show();
				break;
			case 2:// ��ҵ����
				try {
					qyjbxx = (QYJBXX) BaseObjectFactory.createObject(QYJBXX.BusinessClassName);
					if (Guid != null) {
						qyjbxx.setCurrentID(Guid);
					} else {
						Toast.makeText(DetailedActivity.this, "��ҵ��ϢΪ�գ�", 0).show();
					}
					nextBundle.putSerializable("BusinessObj", qyjbxx);
					nextBundle.putString("DetailedBottomMenuStyleFileName", "");
					Intent intent = null;
					intent = new Intent(DetailedActivity.this, EnterpriseArchivesActivitySlide.class);
					intent.putExtra("qydm", qydm);
					intent.putExtras(nextBundle);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					ExceptionManager.WriteCaughtEXP(e, "MapActivity");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					ExceptionManager.WriteCaughtEXP(e, "MapActivity");
					e.printStackTrace();
				} catch (InstantiationException e) {
					ExceptionManager.WriteCaughtEXP(e, "MapActivity");
					e.printStackTrace();
				}
				break;
			case 3:// ���߼��
				break;
			case 4:// �½�����
				Bundle bundle = new Bundle();// ��ʼ������bundle����bundleװ��Intent������תҳ��
				// bundle.putBoolean("IsShowTitle", true);
				bundle.putString("TitleText", "����༭");
				if (detaild.get("qymc").toString() != null || detaild.get("qymc").toString().equals("")) {
					bundle.putString("qymc", detaild.get("qymc").toString());
					bundle.putString("qyid", Guid);

				} else {
					Toast.makeText(DetailedActivity.this, "��ҵ��ϢΪ�գ�", 0).show();
				}
				Intent _Intent = new Intent(DetailedActivity.this, TaskRegisterActivity.class);
				_Intent.putExtras(bundle);
				startActivity(_Intent);
				break;
			}
		}
	};

	/**
	 * FileName: DetailedActivity.java Description:�ײ��˵�������
	 * 
	 * @author �����
	 * @Version 1.3.4
	 * @Copyright �п���ͼ���¿Ƽ����޹�˾ Create at: 2012-12-21 ����11:51:52
	 */
	public class BottomBtnAdapter extends BaseAdapter {

		/** �ײ��˵����� */
		ArrayList<HashMap<String, Object>> bottomList;
		Context context;
		int displayWidth;
		int width;

		/**
		 * ���췽��
		 */
		public BottomBtnAdapter(Context context, ArrayList<HashMap<String, Object>> bottomList) {
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
			layout.setMinimumWidth(displayWidth / (getCount() < 4 ? getCount() : 4));
			TextView btnName = null;
			if (bottomList.size() == 0) {
				bottomlayout.setVisibility(View.GONE);
			} else {
				ImageView splite = new ImageView(context);
				splite.setScaleType(ScaleType.FIT_XY);
				splite.setLayoutParams(new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.FILL_PARENT));
				splite.setBackgroundResource(R.drawable.bg_bottombutton_splite);
				btnName = new TextView(DetailedActivity.this);
				btnName.setId(position);
				btnName.setText(bottomList.get(position).get("btnname").toString());
				btnName.getPaint().setFakeBoldText(true);
				btnName.setTextColor(Color.WHITE);
				btnName.setGravity(Gravity.CENTER);
				btnName.setWidth(width);
				btnName.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.FILL_PARENT));
				if (position != 0) {
					layout.addView(splite);
				}
				layout.addView(btnName);
			}
			return layout;
		}

	}
}
