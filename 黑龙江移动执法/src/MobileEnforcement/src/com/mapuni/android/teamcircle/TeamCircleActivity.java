package com.mapuni.android.teamcircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.OtherTools;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.teamcircle.XListView.IXListViewListener;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TeamCircleActivity extends BaseActivity implements
		IXListViewListener {

	private static final String TAG = "TeamCircleActivity";
	List<Messages> datas = new ArrayList<Messages>();
	private LayoutInflater _LayoutInflater;
	private View taskRegisterView;
	private XListView tc_lv;
	private mAdapter mAdapter;
	private YutuLoading load;
	public static String netUrl = ":";
	public String baseUrl = netUrl + "/api/TeamCircle/";
	private List<MessageModel> list = new ArrayList<MessageModel>();
	private List<MessageModel> myLists = new ArrayList<MessageModel>();
	private String userID;
	private long oldTime;
	// ��ҳ��������
	int pageIndex = 1;
	
	
	 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		SetBaseStyle((RelativeLayout) findViewById(R.id.parentLayout), "����Ȧ");
		_LayoutInflater = LayoutInflater.from(this);

		taskRegisterView = _LayoutInflater.inflate(R.layout.layout_team_circle,
				null);

		// ������Ӱ�ť����ʾ
		setSearchButtonVisiable(true);
		// ��ʾΪ��ӵİ�ť
		queryImg.setImageResource(R.drawable.add);
		// �¼ӷ��ذ�ť ����ͬ����ť
		setBackButtonVisiable(true);
		// ���ó���ͬ������
		setSynchronizeButtonVisiable(true);

		realName = Global.getGlobalInstance().getRealName();
		username = Global.getGlobalInstance().getUsername();
		String userid2 = Global.getGlobalInstance().getUserid();
		initNet();
		// ��ʼ���ؼ�
		initView();
		// ��ʼ������
		initListen();
	}

	private void initNet() {
		String result = "";

		try {
			result = (String) WebServiceProvider.callWebService(
					Global.NAMESPACE, "GetLeWebApiRootUrl",
					new ArrayList<HashMap<String, Object>>(), Global
							.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL,
					WebServiceProvider.RETURN_STRING, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(result)) {
			netUrl = result;
		} else {
			Toast.makeText(TeamCircleActivity.this, "�����������ã�", 0).show();
		}

	}

	private void initView() {

		_LayoutInflater = LayoutInflater.from(this);

		// չʾ�����ؼ�
		((LinearLayout) findViewById(R.id.middleLayout))
				.addView(taskRegisterView);
		tc_lv = (XListView) taskRegisterView.findViewById(R.id.tc_lv);
		tc_lv.setPullLoadEnable(true);
		tc_lv.setPullRefreshEnable(true);
		tc_lv.setXListViewListener(this);

		initData();
	}

	// ��Ӳ�������
	private void initData() {
		load = new YutuLoading(this);
		load.setLoadMsg("���ڼ��أ����Ժ�...", "");
		load.setCancelable(true);
		load.showDialog();

		String areaCode = Global.getGlobalInstance().getAreaCode();
		userID = Global.getGlobalInstance().getUserid();
		String url = netUrl + "/api/TeamCircle/SyncMessageData?regioncode="
				+ areaCode + "&pageIndex=" + pageIndex + "&pageSize=10";
		OkHttpUtils.get().url(url).build().execute(new StringCallback() {

			@Override
			public void onResponse(String arg0, int arg1) {
				
				if (arg0.length() < 4) {
					Toast.makeText(TeamCircleActivity.this, "���޸�������",
							Toast.LENGTH_SHORT).show();
				} else {
					// Toast.makeText(TeamCircleActivity.this,
					// "���ݻ�ȡ�ɹ�",Toast.LENGTH_SHORT).show();
					parseJson(arg0);
					tc_lv.setVisibility(View.VISIBLE);
				}
				load.dismissDialog();

			}

			@Override
			public void onError(Call arg0, Exception arg1, int arg2) {
				Toast.makeText(TeamCircleActivity.this, "���ݻ�ȡʧ��",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//���¼���
	@Override
	protected void onRestart() {
		super.onRestart();
		
	//	initData();
	}
	

	private void initListen() {
		// ��ת�����ͽ���
		queryImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeamCircleActivity.this,
						PublishActivity.class);
				startActivityForResult(intent, 1);
				// finish();
			}
		});

	}

	private void parseJson(String result) {

		if (result == null) {
			Toast.makeText(TeamCircleActivity.this, "û�и������ݣ�",
					Toast.LENGTH_SHORT).show();

		}
		Gson gson = new Gson();
		list.clear();
		// MessageModel fromJson = gson.fromJson(result, MessageModel.class);

		list = gson.fromJson(result, new TypeToken<ArrayList<MessageModel>>() {
		}.getType());
		for (int i = 0; i < list.size(); i++) {
			myLists.add(list.get(i));
		}
		mAdapter = new mAdapter();
		tc_lv.setAdapter(mAdapter);
		tc_lv.setSelection((pageIndex - 1) * 10);
		tc_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MessageModel messageModel = myLists.get(position - 1);
				ArrayList<String> files = messageModel.getFilePathList();
			//	ArrayList<ReplyList> replyList = messageModel.getMessageReplyList();
				Intent intent = new Intent(TeamCircleActivity.this,
						TeamCiroleInfoActivity.class);
				//intent.putStringArrayListExtra("FilePathList", files);
				intent.putStringArrayListExtra("FilePathList", files);
				intent.putExtra("Type", messageModel.getType());
				intent.putExtra("Content", messageModel.getContent());
				intent.putExtra("SenderName", messageModel.getSenderName());
				startActivity(intent);
			}
		});
	}

	private ViewHolder holder;
	private String realName;
	private String username;

	class mAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return myLists.size();
		}

		@Override
		public Object getItem(int position) {
			return myLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View contentView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (contentView == null) {
				contentView = View.inflate(TeamCircleActivity.this,
						R.layout.friends_circle_item, null);
				holder = new ViewHolder();
				holder.tv_user_name = (TextView) contentView
						.findViewById(R.id.tv_user_name);// �û���
				holder.content = (TextView) contentView
						.findViewById(R.id.content);// ��Ϣ����
				holder.tv_share_names = (TextView) contentView
						.findViewById(R.id.tv_share_names);// �����û�չʾ
				holder.im_select = (ImageView) contentView
						.findViewById(R.id.im_select);// �˵���ť ����or����
				holder.rl_good_comment = (RelativeLayout) contentView
						.findViewById(R.id.rl_good_comment); // ���޵ø�����
				holder.line = (ImageView) contentView.findViewById(R.id.line);// ��
				holder.comment_container = (LinearLayout) contentView
						.findViewById(R.id.comment_container);// ���۵���ʾ����
				holder.GridView = (GridView) contentView
						.findViewById(R.id.gridView);
				holder.im_pics = (LinearLayout) contentView
						.findViewById(R.id.im_pics);
				holder.tv_time = (TextView) contentView
						.findViewById(R.id.tv_time);// ��Ϣ����ʱ��
				holder.tv_delete = (TextView) contentView
						.findViewById(R.id.tv_delete);// ɾ����ǰ��
				
				holder.GridView=(GridView) contentView.findViewById(R.id.Team_GridView);
				
				
				contentView.setTag(holder);
			} else {
				holder = (ViewHolder) contentView.getTag();
			}
			// չʾ����
			final MessageModel msg = myLists.get(position);
			holder.tv_user_name.setText(msg.getSenderName());
			holder.content.setText(msg.getContent());
			// ����ɾ����ť��ʾ��������

			if (msg.getSenderCode().toString().trim().equals(userID)) {
				holder.tv_delete.setVisibility(View.VISIBLE);
			} else {
				holder.tv_delete.setVisibility(View.GONE);
			}
			final String deleteUrl = netUrl
					+ "/api/TeamCircle/DeleteMsgBody?msgCode=";
			holder.tv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new Builder(TeamCircleActivity.this)
							.setTitle("ɾ��")
							.setMessage("ȷ��ɾ����?")
							.setNegativeButton("ȡ��", null)
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												final DialogInterface dialog,
												int which) {
											load = new YutuLoading(
													TeamCircleActivity.this);
											load.setLoadMsg("���ڼ��أ����Ժ�...", "");
											load.setCancelable(false);
											load.showDialog();
											Map<String, String> map = new HashMap<String, String>();
											map.put("msgCode", msg.getMsgCode());
											VolleyRequestUtil
													.RequestGet(
															TeamCircleActivity.this,
															deleteUrl
																	+ msg.getMsgCode(),
															"",
															new VolleyListenerInterface(
																	TeamCircleActivity.this,
																	null, null) {

																@Override
																public void onMySuccess(
																		String result) {
																	Toast.makeText(
																			TeamCircleActivity.this,
																			"ɾ���ɹ�",
																			Toast.LENGTH_SHORT)
																			.show();
																	myLists.remove(position);
																	notifyDataSetChanged();
																	dialog.dismiss();
																	load.dismissDialog();
																}

																@Override
																public void onMyError(
																		VolleyError error) {
																	Toast.makeText(
																			TeamCircleActivity.this,
																			"ɾ��ʧ�ܣ����Ժ�����",
																			Toast.LENGTH_SHORT)
																			.show();
																	load.dismissDialog();
																}
															});

										}
									}).show();
				}
			});
			// ��յ������ݱ��⸴��BUG
			holder.comment_container.removeAllViews();
			// TODO ���õ���չʾ
			List<ReplyList> replyList = msg.getMessageReplyList();
			if (replyList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				// holder.comment_container.removeAllViews();
				for (int i = 0; i < replyList.size(); i++) {
					ReplyList replyer = replyList.get(i);
					if (replyer.Type == 2) {
						holder.rl_good_comment.setVisibility(View.VISIBLE);
						holder.line.setVisibility(View.VISIBLE);
						sb.append(replyer.ReplyPeopleName + ",");
						holder.tv_share_names.setText(sb.subSequence(0,
								sb.length() - 1));
					} else if (replyer.Type == 1) {
						// ������������
						// holder.rl_good_comment.setVisibility(View.GONE);
						// holder.line.setVisibility(View.GONE);
						View pView = View.inflate(TeamCircleActivity.this,
								R.layout.layout_pinglun, null);
						TextView tv_comment_name = (TextView) pView
								.findViewById(R.id.tv_comment_name);
						TextView tv_comment_content = (TextView) pView
								.findViewById(R.id.tv_comment_content);
						tv_comment_name.setText(replyer.ReplyPeopleName);
						tv_comment_content.setText(replyer.Content);
						holder.comment_container.addView(pView);
					}
				}
			} else {
				holder.rl_good_comment.setVisibility(View.GONE);
				holder.line.setVisibility(View.GONE);
			}
			// ���õ������popu
			holder.im_select.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					PopupWindow popupWindow = new PopupWindow();
					// ���õ����Ŀ��
					popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
					popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
					// ����popu����ʽ
					View contentView = View.inflate(TeamCircleActivity.this,
							R.layout.layout_team_dialog, null);

					popupWindow.setContentView(contentView);
					// ����������һ��͸���ı���
					popupWindow.setBackgroundDrawable(new BitmapDrawable());

					// ����һ�����Ի�ȡ����ĵ���,���Ա����
					popupWindow.setFocusable(true);
					// ���ô����ﵯ����
					popupWindow.showAsDropDown(v);

					// ����window�еĵ���¼�
					settingOnclick(contentView, position, popupWindow);

					popupWindow
							.setAnimationStyle(R.style.cricleBottomAnimation);

				}

			});
			// չʾʱ��
			String sendTimeString = msg.getSendTime();
			String replace = sendTimeString.replace("T", " ");
			String substring = replace.substring(0, 19);
			Long longToString = DataUtils.getLongToString(substring,
					"yyyy-MM-dd HH:mm:ss");
			String testPassedTime = TimeUtils.testPassedTime(
					System.currentTimeMillis(), longToString);
			holder.tv_time.setText(testPassedTime);
			
			//��������չʾ����
			ArrayList<String> filePathList = msg.getFilePathList();
			
//			if (filePathList.size()!=0) {
//				holder.GridView.setVisibility(View.VISIBLE);
//				
//				GridViewAdapter  gridViewAdapter= new GridViewAdapter(filePathList);
//				
//				holder.GridView.setAdapter(gridViewAdapter);
//				
//			}
			
			
			
			return contentView;
		}
	}

	static class ViewHolder {
		TextView tv_user_name, content, tv_share_names, tv_time, tv_delete;
		ImageView im_select;
		RelativeLayout rl_good_comment;
		ImageView line;
		LinearLayout comment_container, im_pics;

		GridView GridView;
		ImageView im_pic1, im_pic2, im_pic3, im_pic4;

	}

	// չʾͼƬ
	private void ToPicActivity(String url) {
		Intent intent = new Intent(TeamCircleActivity.this,
				ShowPicActivity.class);
		intent.putExtra("pic_url", url);

		startActivity(intent);
	}

	// ����popu��
	private void settingOnclick(View contentView, final int position,
			final PopupWindow popupWindow) {
		ImageView good_img = (ImageView) contentView
				.findViewById(R.id.good_img);
		ImageView comment_img = (ImageView) contentView
				.findViewById(R.id.comment_img);
		// ���õ���
		good_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindow.dismiss();

				final MessageModel msg = myLists.get(position);

				final List<ReplyList> messageReplyList = msg
						.getMessageReplyList();
				// �ж��Ƿ��Ѿ�����

				for (int i = 0; i < messageReplyList.size(); i++) {

					ReplyList replyList = messageReplyList.get(i);
					if (replyList.Type == 2
							&& replyList.ReplyPeopleName.equals(realName)) {

						OtherTools
								.showToast(TeamCircleActivity.this, "�Ѿ��������!");

						return;
					}

				}

				String postUrl = netUrl + "/api/TeamCircle/ReplyMessage?";
				Map<String, String> map = new HashMap<String, String>();
				// map.put("MessageReplyModel", json);
				map.put("MsgCode", msg.getMsgCode());
				map.put("ReplyPeopleCode", userID);
				map.put("ReplyPeopleId", 0 + "");
				map.put("ReplyPeopleName", Global.getGlobalInstance()
						.getRealName());
				map.put("ReplyTime",
						DataUtils.getTime(System.currentTimeMillis()));
				map.put("Type", 2 + "");
				// JsonHelper.listToJSONXin(data)
				VolleyRequestUtil.RequestPost(TeamCircleActivity.this, postUrl,
						"post", map, new VolleyListenerInterface(
								TeamCircleActivity.this, null, null) {
							@Override
							public void onMySuccess(String result) {
								ReplyList replyList = new ReplyList();
								replyList.MsgCode = msg.getMsgCode();
								replyList.ReplyPeopleCode = userID;
								replyList.ReplyPeopleId = 0;
								replyList.ReplyPeopleName = Global
										.getGlobalInstance().getRealName();
								replyList.ReplyTime = DataUtils.getTime(System
										.currentTimeMillis());
								replyList.Type = 2;
								messageReplyList.add(replyList);

								if (mAdapter != null) {
									mAdapter.notifyDataSetChanged();
								}

								Toast.makeText(TeamCircleActivity.this,
										"���޳ɹ�!", 0).show();
							}

							@Override
							public void onMyError(VolleyError error) {
								// TODO Auto-generated method stub
								Toast.makeText(TeamCircleActivity.this,
										"����ʧ��,������������!", 0).show();
							}
						});
				// initData();
			}
		});

		comment_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showdialogP2(position);
				popupWindow.dismiss();
			}
		});
	}

	public void showdialogP2(final int position) {
		final EditText inputServer = new EditText(this);
		inputServer.setFocusable(true);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setIcon(R.drawable.icon_mapuni_white).setTitle("����������").setView(inputServer)
				.setNegativeButton("ȡ��", null);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				final String mPing = inputServer.getText().toString();
				if (TextUtils.isEmpty(mPing)) {
					Toast.makeText(TeamCircleActivity.this, "���۲���Ϊ��", 0).show();

				} else {
					// ���������Ϣ
					final MessageModel msg = myLists.get(position);

					final List<ReplyList> messageReplyList = msg
							.getMessageReplyList();
					String postUrl = netUrl + "/api/TeamCircle/ReplyMessage?";
					final Map<String, String> map = new HashMap<String, String>();
					// map.put("MessageReplyModel", json);
					map.put("MsgCode", msg.getMsgCode());
					map.put("ReplyPeopleCode", userID);
					map.put("ReplyPeopleId", 0 + "");
					map.put("ReplyPeopleName", Global.getGlobalInstance()
							.getRealName());
					map.put("ReplyTime",
							DataUtils.getTime(System.currentTimeMillis()));
					map.put("Type", 1 + "");
					map.put("Content", mPing);
					VolleyRequestUtil.RequestPost(TeamCircleActivity.this,
							postUrl, "post", map, new VolleyListenerInterface(
									TeamCircleActivity.this, null, null) {

								@Override
								public void onMySuccess(String result) {
									ReplyList replyList = new ReplyList();
									replyList.MsgCode = msg.getMsgCode();
									replyList.ReplyPeopleCode = userID;
									replyList.ReplyPeopleId = 0;
									replyList.ReplyPeopleName = Global
											.getGlobalInstance().getRealName();
									replyList.ReplyTime = DataUtils
											.getTime(System.currentTimeMillis());
									replyList.Type = 1;
									replyList.Content = mPing;
									messageReplyList.add(replyList);
									if (mAdapter != null) {
										mAdapter.notifyDataSetChanged();
									}

									// initData();
									Toast.makeText(TeamCircleActivity.this,
											"���۳ɹ�!", 0).show();
								}

								@Override
								public void onMyError(VolleyError error) {
									Toast.makeText(TeamCircleActivity.this,
											"����ʧ��,������������!", 0).show();
								}
							});
				}
			}
		});
		builder.show();

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg1) {
		case 1:
			onRefresh();
			break;

		default:
			break;
		}
	}

	private void ToVideoActivity(String url) {
		Intent intent = new Intent(TeamCircleActivity.this,
				ShowVideoActivity.class);
		intent.putExtra("video_url", url);

		startActivity(intent);
	}

	// ����ˢ��
	public void onRefresh() {

		if (!"".equals(SpUtils.getLong(this, "old"))
				&& SpUtils.getLong(this, "old") != 0) {
			String time = TimeUtils.testPassedTime(System.currentTimeMillis(),
					SpUtils.getLong(this, "old"));
			if (time.equals("47��ǰ")) {
				tc_lv.setRefreshTime("");
			} else {
				tc_lv.setRefreshTime(time);
			}

		} else {
			tc_lv.setRefreshTime("");
		}

		pageIndex = 1;
		myLists.clear();
		//tc_lv.setVisibility(View.GONE);
		initData();

		oldTime = System.currentTimeMillis();
		SpUtils.putLong(TeamCircleActivity.this, "old", oldTime);
		tc_lv.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		++pageIndex;
		initData();
		tc_lv.stopLoadMore();
	}

	public void netImageToView(String path, ImageView view) {
		Picasso.with(TeamCircleActivity.this).load(path).placeholder(R.drawable.yutu).into(view);
	}

	/**
	 * ����������url��ͨ��urlȥ��ȡ��Ƶ�ĵ�һ֡ Android ԭ���������ṩ��һ��MediaMetadataRetriever��
	 * �ṩ�˻�ȡurl��Ƶ��һ֡�ķ���,����Bitmap����
	 * 
	 * @param videoUrl
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap getNetVideoBitmap(String videoUrl) {
		Bitmap bitmap = null;

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			// ����url��ȡ����ͼ
			retriever.setDataSource(videoUrl, new HashMap());
			// ��õ�һ֡ͼƬ
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			retriever.release();
		}
		return bitmap;
	}

	
	public class GridViewAdapter  extends  BaseAdapter{

		
		private ArrayList<String> filepath;

		public GridViewAdapter(){
			
		}
		
		public GridViewAdapter(ArrayList<String> filepath){
			
			this.filepath=filepath;
			
		}
		
		@Override
		public int getCount() {
			return filepath.size();
		}

		@Override
		public Object getItem(int position) {
			return filepath.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView team_imager;
			if (convertView==null) {
				convertView=View.inflate(TeamCircleActivity.this, R.layout.images, null);
				 team_imager =(ImageView) convertView.findViewById(R.id.team_imager);
				convertView.setTag(team_imager);
			}else{
				team_imager=(ImageView) convertView.getTag();
			}

				String url = netUrl+filepath.get(position);
				
				Picasso.with(TeamCircleActivity.this).load(url).placeholder(R.drawable.yutu).into(team_imager);
			return convertView;
		}
		
		
	}
	
}
