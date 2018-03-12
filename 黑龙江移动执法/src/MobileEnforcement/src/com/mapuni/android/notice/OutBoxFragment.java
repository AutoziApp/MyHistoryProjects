package com.mapuni.android.notice;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.business.DataSyncModel;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.base.widget.RefreshableView;
import com.mapuni.android.base.widget.RefreshableView.PullToRefreshListener;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.netprovider.Net;

@SuppressLint("ValidFragment")
public class OutBoxFragment implements OnItemClickListener, OnScrollListener {
	private static String userId;
	private ArrayList<HashMap<String, Object>> boxMsg, IsReadMsg, NoReadMsg;
	public BaseAdapter adapter;
	private RefreshableView refreshableView;
	private ListView listView;
	private NoticeActivity context;
	private static int tBasMessageTotalNumber = 0;
	private int limitSize = 0;
	Button tv;
	public final String TASK_PATH = Global.SDCARD_RASK_DATA_PATH + "Attach/";

	public OutBoxFragment(NoticeActivity context) {
		super();
		userId = Global.getGlobalInstance().getUserid();
		this.context = context;
		Cursor cursor = SqliteUtil.getInstance().queryBySql("select * from T_Bas_Message where CreateUserId = '" + userId + "' and  IsDelete = 0 order by createtime desc");
		if (null == cursor) {
			tBasMessageTotalNumber = 0;
		} else {
			tBasMessageTotalNumber = cursor.getCount();
			cursor.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		final HashMap<String, Object> map = boxMsg.get(position);
		final String msgid = map.get("msgid").toString().trim();
		AlertDialog.Builder builder = new Builder(context);
		if ("未发送".equals(((TextView) v.findViewById(R.id.notice_state)).getText().toString())) {
			builder.setTitle("您确认要发布此消息?");
			builder.setMessage(Html.fromHtml(map.get("msgtitle") + "\n\n" + map.get("msgcontent")));
			builder.setPositiveButton("发布", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 此处将数据发送到服务器，更新数据库这一列的值，否则不更新  
					context.sendObject.sendNoticeWithAttachment(msgid,true);
					//重新加载刷新适配器
					if (null!=adapter) {
						reload();
					}
				}
			});
			builder.setNegativeButton("取消", null);
		} else {
			builder.setPositiveButton("确定", null);
			builder.setTitle(map.get("msgtitle").toString());
			builder.setMessage(Html.fromHtml(map.get("msgcontent").toString()));
			builder.setNegativeButton("查看附件", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Builder b = OtherTools.getAlertDialog2(context, map.get("msgtitle") + "", null, null, null, null);
					String msgID = (String) map.get("msgid");
					String sql = "select * from T_Attachment where FK_Id = '" + msgID + "'";
					final ArrayList<HashMap<String, Object>> lists = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);

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

							com.mapuni.android.attachment.MapuniImageManager.showImageFromNet(attguid, context);
						}
					});
					b.show();
				}
			});
		}

		builder.create().show();

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
	public class CustomAsyncTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int flag = 0;
			DataSyncModel dataSync = new DataSyncModel();
			String[] tables = { "T_Bas_Message", "T_Rel_UserMsg" };
			for (int i = 0; i < tables.length; i++) {
				String tablename = tables[i];
				int j = dataSync.synchronizeFetchServerData(false, tablename);
				flag += j;
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			tv.setVisibility(View.GONE);
			refreshableView.setEnabled(true);
			if (result <= 0) {
				Toast.makeText(context, "暂无最新公告", Toast.LENGTH_SHORT).show();
			} else {
				reload();
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
				viewHolder.notice_TellUser = (TextView) convertView.findViewById(R.id.notice_telluser);

				viewHolder.notice_publisher = (TextView) convertView.findViewById(R.id.notice_publisher);
				// viewHolder.notice_publisher.setVisibility(View.GONE);
				viewHolder.notice_state = (TextView) convertView.findViewById(R.id.notice_state);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			HashMap<String, Object> userInfo = SqliteUtil.getInstance().getDataMapBySqlForDetailed("select * from PC_Users where UserID = '" + map.get("createuserid") + "'");
			IsReadMsg = new ArrayList<HashMap<String, Object>>();
			NoReadMsg = new ArrayList<HashMap<String, Object>>();
			IsReadMsg = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select b.U_RealName from t_rel_usermsg a join pc_users b on a.messageid='" + map.get("msgid") + "' and isreaded=1 and b.userid=a.userid");
			NoReadMsg = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
					"select b.U_RealName from t_rel_usermsg a join pc_users b on a.messageid='" + map.get("msgid") + "' and isreaded=0 and b.userid=a.userid");
			StringBuffer IsReadName = new StringBuffer();
			StringBuffer NoReadName = new StringBuffer();
			// 获取已读通知的用户名
			if (IsReadMsg != null && IsReadMsg.size() > 0) {
				for (int i = 0; i < IsReadMsg.size(); i++) {
					IsReadName.append(IsReadMsg.get(i).get("u_realname") + ",");
				}
				if (IsReadName.length() > 0) {
					IsReadName.deleteCharAt(IsReadName.length() - 1);
				}
			}
			// 获取未读通知的用户名
			if (NoReadMsg != null && NoReadMsg.size() > 0) {
				for (int i = 0; i < NoReadMsg.size(); i++) {
					NoReadName.append(NoReadMsg.get(i).get("u_realname") + ",");
				}
				if (NoReadName.length() > 0) {
					NoReadName.deleteCharAt(NoReadName.length() - 1);
				}
			}
			try {
				viewHolder.notice_title.setText(map.get("msgtitle") + "");
				viewHolder.notice_publisher.setText("发布人:" + userInfo.get("u_realname").toString());
				viewHolder.notice_TellUser.setText(null);
				
				if (IsReadName.length()>0) {
					viewHolder.notice_TellUser.setText(Html.fromHtml("<font color='#3A5FCD'>" + IsReadName +","+ "</font><font color='#FF0000'>" + NoReadName + "</font>"));
				}else {
					viewHolder.notice_TellUser.setText(Html.fromHtml("<font color='#3A5FCD'>" + IsReadName + "</font><font color='#FF0000'>" + NoReadName + "</font>"));

				}
				//修改 
			//	
				
				String temp = map.get("createtime").toString();
				if (temp.contains("T")) {
					temp = temp.substring(0, temp.indexOf("T"));
				}
				viewHolder.notice_publishTime.setText("创建日期:" + temp);
				if ("0".equals(map.get("issend"))) {
					viewHolder.notice_state.setTextColor(Color.RED);
					viewHolder.notice_state.setText("未发送");
				} else {
					viewHolder.notice_state.setTextColor(Color.BLACK);
					viewHolder.notice_state.setText("已发送");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	private class ViewHolder {
		TextView notice_title, notice_publishTime, notice_publisher, notice_TellUser, notice_state;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount) {
			if (boxMsg.size() < tBasMessageTotalNumber) {
				ArrayList<HashMap<String, Object>> temp = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
						"select * from T_Bas_Message where CreateUserId = '" + userId + "' and IsDelete = 0 order by createtime desc limit " + getLimit());
				if (temp.size() > 0) {
					limitSize += 5;
					boxMsg.addAll(temp);
					listView.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				}
			} else {
				//Toast.makeText(context, "数据已经加载完成", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 用于限制每次加载条数
	 * 
	 * @return
	 */
	public String getLimit() {
		return limitSize + "," + 5;
	}

	/**
	 * 重新加载
	 */
	private void reload() {
		limitSize = 0;
		boxMsg.clear();
		boxMsg = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
				"select * from T_Bas_Message where CreateUserId = '" + userId + "' and IsDelete = 0 order by createtime desc limit " + getLimit());
	
//		boxMsg = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(
//				"select * from T_Bas_Message where CreateUserId = '" + userId + "' " );
		
		limitSize += 10;// 10代表每次加载10条
		adapter.notifyDataSetChanged();
	}
}
