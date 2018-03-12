package com.mapuni.android.notice;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.attachment.MapuniImageManager;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.base.widget.RefreshableView;
import com.mapuni.android.base.widget.RefreshableView.PullToRefreshListener;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;

@SuppressLint("ValidFragment")
public class InBoxFragment implements OnItemClickListener, OnScrollListener {
	private String userId;
	private ArrayList<HashMap<String, Object>> boxMsg;
	private BaseAdapter adapter;
	private RefreshableView refreshableView;
	private ListView listView;
	private NoticeActivity context;
	private int limitSize = 0;
	private static int tBasMessageTotalNumber = 0;
	Button tv;
	public final String TASK_PATH = Global.SDCARD_RASK_DATA_PATH + "Attach/";
	String[] tableadd;
	private DateFormat dateFormat;
	SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 2014-05-09
																			// 15:18:36
	@SuppressLint("ValidFragment") 
	ArrayList<HashMap<String, Object>> hash = new ArrayList<HashMap<String, Object>>();

	public InBoxFragment(NoticeActivity context) {
		super();
		userId = Global.getGlobalInstance().getUserid();
		this.context = context;
		getTotalSize();
		dateFormat = DateFormat.getDateInstance();
	}

	public void getTotalSize() {
		HashMap<String, Object> cursor = SqliteUtil.getInstance().getDataMapBySqlForDetailed("select count(1) from T_Rel_UserMsg where IsDelete = 0 and UserID= '" + userId + "'");
		tBasMessageTotalNumber = Integer.valueOf(cursor.get("count(1)").toString());
	}

	public View createView() {

		// FIXME 在此加入下拉刷新

		RelativeLayout relativeLayout = new RelativeLayout(context);
		tv = new Button(context);
		tv.setText(" ");
		tv.setVisibility(View.GONE);
		tv.setBackgroundColor(Color.argb(0, 0, 0, 0));
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		refreshableView = new RefreshableView(context);
		refreshableView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// FIXME 修改这里,直接查询
		boxMsg = new ArrayList<HashMap<String, Object>>();
		adapter = new CustomBaseAdapter();
		listView.setAdapter(adapter);
		reload();
		listView.setOnItemClickListener(this);
		listView.setCacheColorHint(Color.TRANSPARENT);
		if (boxMsg.size() < 1) {
			listView.setVisibility(View.GONE);
		}

		TextView textView = new TextView(context);
		textView.setText("无数据\n请下拉刷新");
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(40);
		textView.setTextColor(Color.rgb(204, 204, 204));
		textView.setClickable(true);
		textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		refreshableView.addView(listView);
		refreshableView.addView(textView);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {// 执行下拉刷新操作
				if (!Net.checkNet(context)) {
					refreshableView.finishRefreshing();
					Toast.makeText(context, "由于网络问题同步失败", Toast.LENGTH_SHORT).show();
				} else {
					// 在此执行数据更新
					tv.setVisibility(View.VISIBLE);
					refreshableView.setEnabled(false);
					new CustomAsyncTask().execute();
				}
			}
		}, 0);

		listView.setOnScrollListener(this);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);

		relativeLayout.addView(refreshableView, layoutParams);
		relativeLayout.addView(tv, layoutParams);

		return relativeLayout;
	}

	/**
	 * 数据同步异步任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class CustomAsyncTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int flag = 0;
			DataSyncModel dataSync = new DataSyncModel();
			String[] tables = { "T_Bas_Message", "T_Rel_UserMsg", "T_Attachment" };
			for (int i = 0; i < tables.length; i++) {
				String tablename = tables[i];
				int j = dataSync.synchronizeFetchServerData(true, tablename);
				flag += j;
			}
			reload();
			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			tv.setVisibility(View.GONE);
			refreshableView.setEnabled(true);
			adapter.notifyDataSetChanged();
			if (result <= 0) {
				Toast.makeText(context, "暂无最新公告", Toast.LENGTH_SHORT).show();
			} else {
				if (boxMsg.size() < 1) {
					listView.setVisibility(View.GONE);
				} else {
					listView.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
				Toast.makeText(context, "已同步到最新", Toast.LENGTH_SHORT).show();
			}
			refreshableView.finishRefreshing();
		}

	}

	/**
	 * 自定义适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class CustomBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return boxMsg.size();
		}

		@Override
		public Object getItem(int position) {
			return boxMsg.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HashMap<String, Object> map = boxMsg.get(position);
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = ((LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.emailbox_item_layout, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.notice_title = (TextView) convertView.findViewById(R.id.notice_title);
				viewHolder.notice_publishTime = (TextView) convertView.findViewById(R.id.notice_publishTime);
				viewHolder.notice_publisher = (TextView) convertView.findViewById(R.id.notice_publisher);
				viewHolder.notice_publisher.setTextColor(Color.BLACK);
				viewHolder.notice_TellUser = (TextView) convertView.findViewById(R.id.notice_telluser);
				viewHolder.notice_TellUser.setVisibility(View.GONE);
				viewHolder.notice_state = (TextView) convertView.findViewById(R.id.notice_state);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			HashMap<String, Object> userInfo = SqliteUtil.getInstance().getDataMapBySqlForDetailed(
					"select u_realname from PC_Users where UserID = '" + map.get("createuserid") + "'");
			try {
				viewHolder.notice_title.setText(map.get("msgtitle") + "");
				viewHolder.notice_publisher.setText("发布人：" + userInfo.get("u_realname").toString());
				String temp = map.get("publishdate").toString();
				if (temp.contains("T")) {
					temp = temp.substring(0, temp.indexOf("T"));
				} else {
					dateFormat.format(formater.parse(temp));
				}
				viewHolder.notice_publishTime.setText("发布日期:" + temp);
				if ("1".equals(map.get("isreaded"))) {
					viewHolder.notice_state.setTextColor(Color.BLACK);
					viewHolder.notice_state.setText("已读");
				} else {
					viewHolder.notice_state.setTextColor(Color.RED);
					viewHolder.notice_state.setText("未读");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	private class ViewHolder {
		TextView notice_title, notice_publisher, notice_publishTime, notice_TellUser, notice_state;
	}
	
	
	Handler handler=new Handler();

	/*
	 * (non-Javadoc) listView的点击监听事件
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
		final HashMap<String, Object> map = (HashMap<String, Object>) parent.getAdapter().getItem(position);

		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(map.get("msgtitle") + "");
		builder.setMessage(Html.fromHtml(map.get("msgcontent").toString()));

		String msgID = (String) map.get("msgid");
		String sql = "select guid,filename from T_Attachment where FK_Id = '" + msgID + "'";
		final ArrayList<HashMap<String, Object>> lists = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
		if (lists.size() > 0) {
			builder.setNegativeButton("附件列表", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if ("未读".equals(((TextView) view.findViewById(R.id.notice_state)).getText().toString())) {
						
						new Thread(){
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								super.run();
								try {
									if (SqliteUtil.getInstance().ExecSQL(
											"update T_Rel_UserMsg set IsReaded = 1 where MessageId = '" + map.get("msgid").toString() + "'and UserID = '" + userId + "'")) {

										// 调用后台服务更改后台中通知的状态为已读
										String methodName = "UpdateStatusAboutMyWatingMessage";
										ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
										HashMap<String, Object> param = new HashMap<String, Object>();
										param.put("messageId", map.get("msgid").toString());
										param.put("userId", userId);
										params.add(param);
										Boolean result = (Boolean) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl()
												+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_BOOLEAN, true);
										if (result == null) {
											result = false;
										}
										reload();
										
										handler.post(new Runnable() {
											
											@Override
											public void run() {
												adapter.notifyDataSetChanged();
											}
										});
										
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}.start();
				
					}
					

					Builder b = OtherTools.getAlertDialog2(context, map.get("msgtitle") + "", null, null, null, null);

					String[] items = new String[lists.size()];
					for (int i = 0; i < lists.size(); i++) {
						HashMap<String, Object> m = lists.get(i);
						items[i] = m.get("filename").toString();
					}
			
					b.setItems(items, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, final int which) {
							HashMap<String, Object> map = lists.get(which);
							final String attguid = map.get("guid").toString();
							MapuniImageManager.showImageFromNet(attguid, context);
						}
					});
					b.show();
				}
			});
		}

		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// FIXME
				// 保存已阅数据到数据库,启动数据库监视器,需要重新处理保存的数据,当点击确认的时候，先判断有没有这条数据在数据库中，如果没有，插入，如果有，修改IsReaded的值
				if ("未读".equals(((TextView) view.findViewById(R.id.notice_state)).getText().toString())) {
					
					new Thread(){
						
						@Override
						public void run() {
							super.run();
							try {
								if (SqliteUtil.getInstance().ExecSQL(
										"update T_Rel_UserMsg set IsReaded = 1 where MessageId = '" + map.get("msgid").toString() + "'and UserID = '" + userId + "'")) {

									// 调用后台服务更改后台中通知的状态为已读
									String methodName = "UpdateStatusAboutMyWatingMessage";
									ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
									HashMap<String, Object> param = new HashMap<String, Object>();
									param.put("messageId", map.get("msgid").toString());
									param.put("userId", userId);
									params.add(param);
									Boolean result = (Boolean) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl()
											+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_BOOLEAN, true);
									if (result == null) {
										result = false;
									}
									reload();
									
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											adapter.notifyDataSetChanged();
										}
									});
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					}.start();
				
				}
			}
		}

		);

		builder.setNeutralButton("回复发布人", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if ("未读".equals(((TextView) view.findViewById(R.id.notice_state)).getText().toString())) {
					
					new Thread(){
						
						public void run() {
							try {
								if (SqliteUtil.getInstance().ExecSQL(
										"update T_Rel_UserMsg set IsReaded = 1 where MessageId = '" + map.get("msgid").toString() + "'and UserID = '" + userId + "'")) {

									// 调用后台服务更改后台中通知的状态为已读
									String methodName = "UpdateStatusAboutMyWatingMessage";
									ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
									HashMap<String, Object> param = new HashMap<String, Object>();
									param.put("messageId", map.get("msgid").toString());
									param.put("userId", userId);
									params.add(param);
									Boolean result = (Boolean) WebServiceProvider.callWebService(Global.NAMESPACE, methodName, params, Global.getGlobalInstance().getSystemurl()
											+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_BOOLEAN, true);
									if (result == null) {
										result = false;
									}
									reload();
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											adapter.notifyDataSetChanged();
										}
									});
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						};
					}.start();
					
				
				}
				
				
				
				context.setViewPagerIndex(2);
				context.sendObject.notice_title.setText("Re:" + map.get("msgtitle"));

				String u_realname = (String) SqliteUtil.getInstance()
						.getDataMapBySqlForDetailed("select u_realname from PC_Users where UserID = '" + map.get("createuserid") + "'").get("u_realname");

				context.sendObject.notice_content.setText("\n\n--------- " + u_realname + " 在通知中写道 ---------\n" + Html.fromHtml(map.get("msgcontent").toString()));
				context.sendObject.noticeToUser.setText(u_realname);
				
				HashMap<String, Object> map = SqliteUtil.getInstance().getDataMapBySqlForDetailed(
						"select UserID from PC_Users where U_RealName = '"
								+ u_realname.trim() + "'");
				context.sendObject.noticeToUser.setEnabled(false);
				context.sendObject.noticeToUser.setTag(map.get("userid").toString());
				
				context.sendObject.setMsgID(UUID.randomUUID().toString());
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		try {
			Class<?> mAlert = dialog.getClass();
			Field field = mAlert.getDeclaredField("mAlert");
			field.setAccessible(true);

			Field mTitleView = field.get(dialog).getClass().getDeclaredField("mTitleView");
			mTitleView.setAccessible(true);
			Object AlertController = field.get(dialog);

			dialog.show();
			Object obj = mTitleView.get(AlertController);
			TextView textView = (TextView) obj;
			textView.setSingleLine(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount) {
			if (boxMsg.size() < tBasMessageTotalNumber) {
				ArrayList<HashMap<String, Object>> temp = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select * FROM  T_Bas_Message A left join T_Rel_UserMsg B on A.MsgID =B.MessageID  WHERE B.UserID='" + userId
								+ "' and B.[IsDelete]= 0 ORDER BY A.Createtime desc limit " + getLimit());
				if (temp.size() > 0) {
					limitSize += 10;
					boxMsg.addAll(temp);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * 用于限制每次加载条数
	 * 
	 * @return
	 */
	public String getLimit() {
		int size = 10;
		if ((size = tBasMessageTotalNumber - boxMsg.size()) >= 10) {
			size = 10;
		}
		return limitSize + "," + size;
	}

	/**
	 * 重新加载
	 */
	private void reload() {
		limitSize = 0;
		boxMsg.clear();
		getTotalSize();
		boxMsg = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * FROM  T_Bas_Message A left join T_Rel_UserMsg B on A.MsgID =B.MessageID  WHERE B.UserID='" + userId
						+ "' and B.[IsDelete]= 0 ORDER BY A.Createtime desc limit " + getLimit());
		limitSize += 10;// 10代表每次加载10条
	}

	// 获取附件列表的数据
	public ArrayList<HashMap<String, Object>> getAttachAdapterData(String fk_unit, String fk_id) {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("FK_Unit", fk_unit);
		conditions.put("FK_id", fk_id);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance().getList("Guid,FileName", conditions, "T_Attachment");
		return data;
	}

	/**
	 * 附件列表的适配器
	 * 
	 * */
	public class Attach_Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> attachmentdata;

		public Attach_Adapter(ArrayList<HashMap<String, Object>> attachAdapterData) {
			this.attachmentdata = attachAdapterData;
		}

		@Override
		public int getCount() {
			int size = attachmentdata.size();
			return size;
		}

		@Override
		public Object getItem(int position) {
			return attachmentdata.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return attachmentdata;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.listitem, null);

			}
			ImageView rw_icon = (ImageView) convertView.findViewById(R.id.listitem_left_image);
			rw_icon.setImageResource(R.drawable.icon_table);
			TextView rwmc_text = (TextView) convertView.findViewById(R.id.listitem_text);
			rwmc_text.setText(attachmentdata.get(position).get("filename").toString());
			rwmc_text.setTextSize(20);
			rwmc_text.setTag(attachmentdata.get(position).get("guid").toString());

			return convertView;
		}
	}

}
