package com.mapuni.android.notice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.MobileEnforcement.R.color;
import com.mapuni.android.attachment.T_Attachment;
import com.mapuni.android.attachment.TaskFile;
import com.mapuni.android.attachment.UploadFile;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.util.LogUtil;
import com.mapuni.android.business.RWXX;
import com.mapuni.android.dataprovider.DESSecurity;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.JsonHelper;
import com.mapuni.android.dataprovider.SQLiteDataProvider;
import com.mapuni.android.dataprovider.SqliteUtil;
import com.mapuni.android.enforcement.AttachmentBaseActivity;
import com.mapuni.android.netprovider.HttpUploader;
import com.mapuni.android.netprovider.Net;
import com.mapuni.android.netprovider.WebServiceProvider;
import com.mapuni.android.taskmanager.TaskManagerModel;
import com.mapuni.android.taskmanager.TaskManagerModel.SelectAuditorAdapter;

@SuppressLint({ "ValidFragment", "ShowToast", "SimpleDateFormat" })
public class NoticeActivity extends BaseActivity {
	private ViewPager vp;
	RelativeLayout titleLayout;
	private RadioGroup radioGroup;
	private int radioButtonId[] = { R.id.vp_radio0, R.id.vp_radio1,
			R.id.vp_radio2 };
	private LinearLayout middleLayout;
	/** 把需要滑动的页卡添加到这个list中 */
	private List<View> viewList;
	RWXX rwxx;
	ArrayList<HashMap<String, Object>> attachAdapterData = new ArrayList<HashMap<String, Object>>();
	/** 附件列表适配器 */
	Attach_Adapter attachAdapter;
	ListView task_attach_list;
	public final int SELECT_SDKARD_FILE = 2;
	public final String TASK_PATH = Global.SDCARD_RASK_DATA_PATH
			+ "Attach/TZTG/";
	String rwGuid;
	String RWBH;
	String guid;
	
	private int IsCompany=0;
	private String[] splits;

	YutuLoading yutuLoading;

	public SendFragment sendObject;

	public final static int HANDLER_STATUS_SUCCESS = 10,
			HANDLER_STATUS_NET_DISABLE = 11, HANDLER_STATUS_FAILED = 12,
			HANDLER_STATUS_FAILED_SAVE = 13;

	public CustomHandler handler = new CustomHandler(NoticeActivity.this);

	private static class CustomHandler extends Handler {
		private WeakReference<NoticeActivity> weak;

		public CustomHandler(NoticeActivity activity) {
			super();
			this.weak = new WeakReference<NoticeActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				Toast.makeText(weak.get(), "网络不通，请检查网络设置！", Toast.LENGTH_SHORT)
						.show();
				break;
			case UploadFile.UPLOAD_CALL_BACK:
				weak.get().callBackCode();
				break;
			case HANDLER_STATUS_SUCCESS:
				weak.get().dismissDialog();
				weak.get().sendObject.clearView();
				Toast.makeText(weak.get(), "发布成功", Toast.LENGTH_SHORT).show();
				break;
			case HANDLER_STATUS_NET_DISABLE:
				weak.get().dismissDialog();
				//发送失败也清空
				weak.get().sendObject.clearView();
				Toast.makeText(weak.get(), "网络暂时不可用,已保存", Toast.LENGTH_SHORT)
						.show();
				break;
			case HANDLER_STATUS_FAILED:
				weak.get().dismissDialog();
				Toast.makeText(weak.get(), "由于服务器问题，发布失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case HANDLER_STATUS_FAILED_SAVE:
				weak.get().dismissDialog();
				//发送失败也清空
				weak.get().sendObject.clearView();
				Toast.makeText(weak.get(), "由于服务器问题，发布失败,通知已保存至发件箱",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	}

	public void dismissDialog() {
		yutuLoading.dismissDialog();
	}

	public void setViewPagerIndex(int index) {
		vp.setCurrentItem(index, true);
	}

	private void callBackCode() {
		
		sendObject.sendNotice(this, sendObject.getMsgID(), true, handler);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ui_mapuni);

		findViewById(R.id.ui_mapuni_divider).setVisibility(View.GONE);
		middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		middleLayout.setPadding(0, 0, 0, 0);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_notice_main, null);
		vp = (ViewPager) view.findViewById(R.id.vp);
		attachAdapter = new Attach_Adapter(attachAdapterData);

		radioGroup = (RadioGroup) view.findViewById(R.id.vpRadioGroup);

		viewList = new ArrayList<View>();
		viewList.add(0, getView(0));
		viewList.add(1, getView(1));
		viewList.add(2, getView(2));

		vp.setAdapter(new MyAdapter());

		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				radioGroup.check(radioButtonId[arg0]);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for (int i = 0; i < radioButtonId.length; i++) {
					if (checkedId == radioButtonId[i]) {
						vp.setCurrentItem(i, true);
						break;
					}
				}
			}
		});
		vp.setCurrentItem(1);

		middleLayout.addView(view);
		titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		SetBaseStyle(titleLayout, "监察公告");// 设置BaseActivity的标题才可以显示标题栏

		yutuLoading = new YutuLoading(this);
		yutuLoading.setLoadMsg("提交中,请稍后...", "");
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			viewList.remove(position);
			viewList.add(position, getView(position));
			container.addView(viewList.get(position), 0);
			return viewList.get(position);
		}

	}

	private View getView(int position) {
		switch (position) {
		case 0:
			return new OutBoxFragment(this).createView();
		case 1:
			return new InBoxFragment(this).createView();
		case 2:
			sendObject = new SendFragment(this);
			return sendObject.createView();
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null && requestCode == SELECT_SDKARD_FILE) {
			Uri uri = data.getData();
			String path = "";
			guid=UUID.randomUUID().toString();
			try {
				// String[] proj = { MediaStore.Images.Media.DATA };
				// Cursor actualimagecursor = this.managedQuery(uri, proj, null,
				// null, null);
				// int actual_image_column_index =
				// actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// actualimagecursor.moveToFirst();
				//
				// path =
				// actualimagecursor.getString(actual_image_column_index);
				// String fileName = path.substring(path.lastIndexOf("/") + 1);
				path = AttachmentBaseActivity.getPath(this, data.getData());
				File souceFile = new File(path);
				String fileName = souceFile.getName();
				String extension = "";
				if (path.contains(".")) {
					extension = path.substring(path.lastIndexOf("."));
					fileName = Global.getGlobalInstance().getRealName() + "_"
							+ fileName.substring(0, fileName.lastIndexOf("."));
				}
				File targetFile = new File(TASK_PATH);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				targetFile = new File(TASK_PATH + guid + extension);
				FileHelper.copyFile(souceFile, targetFile);

				String sql = "insert into T_Attachment (Guid,FileName,FilePath,Extension,FK_Unit,FK_Id) "
						+ "values ('"
						+ guid
						+ "','"
						+ fileName
						+ "','"
						+ guid
						+ extension
						+ "','"
						+ extension
						+ "','"
						+ T_Attachment.TZTG
						+ "','"
						+ sendObject.getMsgID()
						+ "')";
				SqliteUtil.getInstance().execute(sql);

			} catch (Exception e) {
				e.printStackTrace();
			}
			attachAdapterData = getAttachAdapterData(T_Attachment.TZTG + "",
					sendObject.getMsgID());
			attachAdapter.updateData(attachAdapterData);
		}
	}

	// 获取附件列表的数据
	public ArrayList<HashMap<String, Object>> getAttachAdapterData(
			String fk_unit, String fk_id) {
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("FK_Unit", fk_unit);
		conditions.put("FK_id", fk_id);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("Guid,FileName", conditions, "T_Attachment");
		return data;
	}

	// 根据数据设置listView的高度
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// ListAdapter listAdapter = listView.getAdapter();
		if (attachAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < attachAdapter.getCount(); i++) {
			View listItem = attachAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (attachAdapter.getCount() - 1));
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}

	public class SendFragment {
		public EditText notice_title;
		public EditText notice_content;
		public Button noticeToUser;
		public final int SELECT_SDKARD_FILE = 2;
		private String GUID = UUID.randomUUID().toString();
		// 添加附件上传按钮
		Button attachment_button_upload;
		private String UserAreaCode = Global.getGlobalInstance().getAreaCode(),
				userId = Global.getGlobalInstance().getUserid(),
				UserdepId = Global.getGlobalInstance().getDepId();
		StringBuffer userName;

		Date dateTemp = new Date();
		public Context context;

		public SendFragment(Context context) {
			super();
			this.context = context;
		}

		private String getMsgID() {
			return GUID;
		}

		public void setMsgID(String GUID) {
			this.GUID = GUID;
		}

		public View createView() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.sendbox_layout, null);
			noticeToUser = (Button) view.findViewById(R.id.noticeToUser);
			notice_title = (EditText) view.findViewById(R.id.notice_title);
			notice_content = (EditText) view.findViewById(R.id.notice_content);
			rb1 = (RadioButton) view.findViewById(R.id.rb1);
			rb2 = (RadioButton) view.findViewById(R.id.rb2);
			//设置监听    初始化监听
			rb1.setChecked(true);
			noticeToUser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (DisplayUitl.isFastDoubleClick()) {
						return;
					}
					//选择告知对象
					noticeToUser();
					 //noticeToUser2();
				}
			});
			//选择发送方式
			rb1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				   	rb1.setChecked(true);
	            	rb2.setChecked(false);	
	            	IsCompany=0;
	            	noticeToUser
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (DisplayUitl.isFastDoubleClick()) {
								return;
							}
							//选择告知对象
							noticeToUser();
							 //noticeToUser2();
						}
					});
				}
			});
			rb2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					IsCompany=1;
		            	rb1.setChecked(false);
		            	rb2.setChecked(true);
		            	
		            	noticeToUser 	.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (DisplayUitl.isFastDoubleClick()) {
									return;
								}
								//选择告知对象
								//noticeToDanWei();
								 noticeToUser2();
							}

						});
				}
			});
		
				

			
			
			attachment_button_upload = (Button) view
					.findViewById(R.id.attachment_button);
			task_attach_list = (ListView) view
					.findViewById(R.id.taskedit_listview);
			task_attach_list.setAdapter(attachAdapter);
			
	

			task_attach_list
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							String guid_id = ((TextView) (arg1
									.findViewById(R.id.listitem_text)))
									.getTag().toString();
							String filename = ((TextView) (arg1
									.findViewById(R.id.listitem_text)))
									.getText().toString();
							showDialog(guid_id, filename, context,
									attachAdapterData);
							return false;

						}
					});

			attachment_button_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/*");
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					try {
						((Activity) context).startActivityForResult(
								Intent.createChooser(intent, "请选择一个要启动的应用"),
								SELECT_SDKARD_FILE);
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(context, "请安装文件管理器", Toast.LENGTH_SHORT)
								.show();
					}

				}
			});


			((Button) view.findViewById(R.id.noticeSend))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// FIXME 发布消息处理，无网条件下，存储于本地数据库,待有网的情况下发布出去
							 String temp = notice_title.getText().toString();
							if ("".equals(temp)) {
								Toast.makeText(context, "公告标题不能为空",
										Toast.LENGTH_SHORT).show();
								return;
							}
							temp = notice_content.getText().toString();
							if ("".equals(temp)) {
								Toast.makeText(context, "公告内容不能为空",
										Toast.LENGTH_SHORT).show();
								return;
							}
							temp = noticeToUser.getText().toString();
							if ("".equals(temp)) {
								Toast.makeText(context, "告知对象不能为空",
										Toast.LENGTH_SHORT).show();
								return;
							}

							AlertDialog dialog = null;
							Builder builder = new Builder(context);
							builder.setTitle("确认发布此公告?");
							builder.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

									

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											splits = noticeToUser.getText().toString().split(",");
											if (IsCompany==0) {
												SqliteUtil.getInstance().insertNoTransaction(getMsgUserDatas(GUID, noticeToUser.getText().toString().split(",")), "T_Rel_UserMsg");
												SqliteUtil.getInstance().insert(getMsgDetailsDatas(GUID, notice_title.getText().toString(), notice_content, noticeToUser.getTag().toString()),
												"T_Bas_Message");
											}else if(IsCompany==1){
												SqliteUtil.getInstance().insertNoTransaction(getMsgUserDatas2(GUID, noticeToUser.getText().toString().split(",")), "T_Rel_UserMsg");
												SqliteUtil.getInstance().insert(getMsgDetailsDatas(GUID, notice_title.getText().toString(), notice_content, noticeToUser.getTag().toString()),
												"T_Bas_Message");
											}
											
										
													sendNoticeWithAttachment(GUID,false);
													
										}
									});
							builder.setNegativeButton("取消", null);
							dialog = builder.create();
							dialog.show();
						}
					});
			((Button) view.findViewById(R.id.noticeCancel))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 初始化控件
							clearView();
						}
					});

			return view;
		}

		/**
		 * 告知对象集合
		 * */
		public ArrayList<ContentValues> usersMsg=new ArrayList<ContentValues>();
		public void clearView() {
			notice_title.setText("");
			notice_content.setText("");
			noticeToUser.setText("");
			attachAdapterData.clear();
			attachAdapter.notifyDataSetChanged();
		}
   
		public void sendNoticeWithAttachment(final String GUID,boolean isoutbox) {
			
				
			
			ArrayList<TaskFile> taskFile = getAllUploadFile(T_Attachment.TZTG,
					GUID);
			//
			if (isoutbox) {
				sendObject.setMsgID(GUID);
				}
			if (taskFile != null && taskFile.size() > 0) {
				// guid byk
			//	new UploadFile().upLoadFilesMethod(taskFile, handler, context);
					upLoadFilesMethod2(taskFile,NoticeActivity.this,guid);
			} else {
				handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
			}
		}

		// 创建消息用户关系数据
		private List<ContentValues> getMsgUserDatas(String GUID,
				String...names) {
			List<ContentValues> lists = new ArrayList<ContentValues>();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (int i = 0; i < names.length; i++) {
				HashMap<String, Object> map = SqliteUtil.getInstance().getDataMapBySqlForDetailed(
								"select UserID from PC_Users where U_RealName = '"
										+ names[i].toString().trim() + "'");
				ContentValues cv = new ContentValues();
				cv.put("MessageID", GUID);
				cv.put("UserID", map.get("userid").toString());
				cv.put("IsReaded", 0);
				cv.put("IsDelete", 0);
				cv.put("UpdateTime", dateFormat.format(new Date()));
				lists.add(cv);
			}

			return lists;
		}

		// 创建消息用户关系数据
		private List<ContentValues> getMsgUserDatas2(String GUID,
				String...names) {
			List<ContentValues> lists = new ArrayList<ContentValues>();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (int i = 0; i < names.length; i++) {
				HashMap<String, Object> map = SqliteUtil.getInstance().getDataMapBySqlForDetailed(
								"select * from Region where RegionName = '"
										+ names[i].toString().trim() + "' and   ParentCode ='"+Global.getGlobalInstance().getAreaCode()+"'");
				String sql2="	select * from PC_Users where regioncode ='"+map.get("regioncode").toString()+"'and zw !=0  order by zw";
				
				 HashMap<String, Object> map2 = SqliteUtil.getInstance().getDataMapBySqlForDetailed(sql2);
				 if (map.size()!=0&&map2.size()!=0) {
					
		
				 
				ContentValues cv = new ContentValues();
				cv.put("MessageID", GUID);
				cv.put("UserID", map2.get("userid").toString());
				cv.put("IsReaded", 0);
				cv.put("IsDelete", 0);
				cv.put("UpdateTime", dateFormat.format(new Date()));
				lists.add(cv);
					}
			}

			return lists;
		}

		// 创建消息详细数据
		private ContentValues getMsgDetailsDatas(String GUID, String MsgTitle,
				EditText MsgContent, String msgTo) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			ContentValues cv = new ContentValues();
			cv.put("MsgID", GUID);
			cv.put("MsgTitle", MsgTitle);
			cv.put("MsgContent", Html.toHtml(MsgContent.getText()));
			cv.put("CreateTime", dateFormat.format(new Date()));
			cv.put("PublishDate", dateFormat.format(new Date()));
			cv.put("CreateUserID", userId);
			cv.put("Remarks", "");
			cv.put("MsgDX", msgTo);
			cv.put("IsEdit", 0);
			cv.put("IsDelete", 0);
			cv.put("IsSend", 0);
			cv.put("UpdateTime", dateFormat.format(new Date()));

			return cv;
		}
		
		/**
		 * 获取通知对象
		 * 
		 * @param view
		 */
		public void noticeToUser() {
			AlertDialog.Builder  builder = new AlertDialog.Builder(context);
			builder.setTitle("请选择告知对象");
			

			builder.setIcon(context.getResources().getDrawable(R.drawable.yutu));
			ExpandableListView listView = new ExpandableListView(context);
			builder.setView(listView);
	
			HashMap<String, Object> condition = new HashMap<String, Object>();
			condition.put("1",
					"2' or zw='1' or zw='0' or zw='2' or zw='3' or zw='4");

			ArrayList<HashMap<String, Object>> depData = new ArrayList<HashMap<String, Object>>();
			List<String> groupList = new ArrayList<String>();
			final ArrayList<ArrayList<HashMap<String, Object>>> childMapData = new ArrayList<ArrayList<HashMap<String, Object>>>();
			final LinkedList<String> linkedList = new LinkedList<String>();
			final LinkedList<String> linkedName = new LinkedList<String>();
			ArrayList<HashMap<String, Object>> login_user_data = new ArrayList<HashMap<String, Object>>();

			// FIXME 记住恢复原样
			if (UserdepId.equals("210000000departB")) {
				login_user_data = SqliteUtil
						.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select * from PC_DepartmentInfo where depid ='210000000departA'");
			} else {
				login_user_data = SqliteUtil
						.getInstance()
						.queryBySqlReturnArrayListHashMap(
								"select * from PC_DepartmentInfo where depid =(select depparentid from PC_Users LEFT JOIN PC_DepartmentInfo on PC_Users.depId = PC_DepartmentInfo.depId where userid = '"
										+ userId + "')");
			}
			String depID = login_user_data.get(0).get("depid").toString();

			HashMap<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("DepParentID", depID);
			conditions.put("syncDataRegionCode", UserAreaCode);

			SqliteUtil.getInstance().recursiveQueryOrder("PC_DepartmentInfo",
					depData, conditions, "depid,depname", false, "depid");
			for (HashMap<String, Object> map : depData) {
				String depName = map.get("depname").toString();
				if (!"审核部门".equals(depName)) {
					groupList.add(depName);

					condition.put("DepID", map.get("depid").toString());
					condition.put("syncDataRegionCode", UserAreaCode);
					ArrayList<HashMap<String, Object>> usersData = SqliteUtil
							.getInstance().getOrderList("PC_Users",
									"UserID,U_RealName", condition, "zw");
					childMapData.add(usersData);
				}
				
			}
			SelectAuditorAdapter getselectAuditorAdapter = new TaskManagerModel().getselectAuditorAdapter(
					groupList, childMapData, linkedList, linkedName, context);
			listView.setAdapter(getselectAuditorAdapter);// 父类数据，子类数据
			for(int i = 0; i < getselectAuditorAdapter.getGroupCount(); i++){  
                
				listView.expandGroup(i);  
				                          
				}  
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (DisplayUitl.isFastDoubleClick()) {
								return;
							}
							userName = new StringBuffer();
							StringBuffer sbFshr = new StringBuffer();
							for (int i = 0; i < linkedList.size(); i++) {
								sbFshr.append(linkedList.get(i) + ",");
								userName.append(linkedName.get(i) + ",");
							}
							/* 去除, */
							if (sbFshr.length() > 0) {
								sbFshr.deleteCharAt(sbFshr.length() - 1);
							}
							if (userName.length() > 0) {
								userName.deleteCharAt(userName.length() - 1);
							}
							noticeToUser.setText(userName);
							noticeToUser.setTag(sbFshr);
						}

					});

			builder.setNeutralButton("全选",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (DisplayUitl.isFastDoubleClick()) {
								return;
							}
							userName = new StringBuffer();
							StringBuffer sbFshr = new StringBuffer();
							for (int i = 0; i < childMapData.size(); i++) {
								for (int j = 0; j < childMapData.get(i).size(); j++) {
									sbFshr.append(childMapData.get(i).get(j)
											.get("userid")
											+ ",");
									userName.append(childMapData.get(i).get(j)
											.get("u_realname")
											+ ",");
								}
							}

							if (sbFshr.length() > 0) {
								sbFshr.deleteCharAt(sbFshr.length() - 1);
							}
							if (userName.length() > 0) {
								userName.deleteCharAt(userName.length() - 1);
							}
							noticeToUser.setText(userName);
							noticeToUser.setTag(sbFshr);
						}
					});
			builder.setNegativeButton("取消", null);

		AlertDialog dialog = builder.create();
			
	       dialog.show();
		}
		
		
		
/**
 * 获取 下属单位
 * */		private StringBuffer select_names=new StringBuffer();
			private StringBuffer select_codes= new  StringBuffer();
			
			private List<String> select_names2=new ArrayList<String>();
			private List<String> select_codes2=new ArrayList<String>();
			
			
		private void noticeToDanWei() {
			String sql="select * from Region where ParentCode ='"+Global.getGlobalInstance().getAreaCode()+"'";
			ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			List<String> regionnames=new ArrayList<String>();
			List<String> regioncodes=new ArrayList<String>();
			
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> hashMap = list.get(i);
				String regionname = hashMap.get("regionname").toString();
				String regioncode = hashMap.get("regioncode").toString();
				
				regionnames.add(regionname);
				regioncodes.add(regioncode);
			}
			
		
			final String[] names = regionnames.toArray(new String[regionnames.size()]);
			final String[] codes = regioncodes.toArray(new String[regioncodes.size()]);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					NoticeActivity.this);
			builder.setIcon(R.drawable.yutu);
			builder.setTitle("请选择告知单位");
			
			builder.setMultiChoiceItems(names, null,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							if (DisplayUitl.isFastDoubleClick()) {
								return;
							}
							if (isChecked) {
								select_names2.add(names[which]);
								select_codes2.add(codes[which]);
							}else{
								select_names2.remove(names[which]);
								select_codes2.remove(codes[which]);
							}
					
//							noticeToUser.setText(userName);
//							noticeToUser.setTag(select_codes);
						}
					});
			
			
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (DisplayUitl.isFastDoubleClick()) {
								return;
							}
							if (select_names2.size()==0) {
								Toast.makeText(NoticeActivity.this,
										"请至少选择一位告知对象!", 0).show();
								return;
							}
							
//							for (int i = 0; i < select_names2.size(); i++) {
//								select_names.append(select_names2.get(i)+",");
//							}
//							for (int i = 0; i < select_codes2.size(); i++) {
//								select_codes.append(select_codes2.get(i)+",");
//							}
//							/* 去除, */
//							if (select_names.length() > 0) {
//								select_names.deleteCharAt(select_names.length() - 1);
//							}
//							if (select_codes.length() > 0) {
//								select_codes.deleteCharAt(select_codes.length() - 1);
//							}
							
							
							noticeToUser.setText(select_names2.toString());
							noticeToUser.setTag(select_codes2.toString());
							String string = select_names2.toString();
							select_names2.clear();
							select_codes2.clear();
						}
					});
			
			builder.setNegativeButton("取消",
					null);
			//builder.create().show();
	//	builder.show();
		
		
		
			
		}

		ArrayList<String> selected = new ArrayList<String>();
		ArrayList<String> selected_userId = new ArrayList<String>();
		private RadioButton rb1;
		private RadioButton rb2;

		/**
		 * 获取通知对象2
		 * 
		 * @param view
		 */

		public void noticeToUser2() {
			
			String sql="select * from Region where ParentCode ='"+Global.getGlobalInstance().getAreaCode()+"'";
			ArrayList<HashMap<String, Object>> list = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(sql);
			List<String> regionnames=new ArrayList<String>();
			List<String> regioncodes=new ArrayList<String>();
			
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> hashMap = list.get(i);
				String regionname = hashMap.get("regionname").toString();
				String regioncode = hashMap.get("regioncode").toString();
				
				regionnames.add(regionname);
				regioncodes.add(regioncode);
			}
			
		
			final String[] names = regionnames.toArray(new String[regionnames.size()]);
			final String[] codes = regioncodes.toArray(new String[regioncodes.size()]);
			
            AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
            builder.setIcon(R.drawable.yutu);
            builder.setTitle("选择告知单位");
            builder.setMultiChoiceItems(names, null, new DialogInterface.OnMultiChoiceClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked)
                {
                	
            
			
			
                    if(isChecked)
                    {
                        select_names2.add(names[which].toString());
						select_codes2.add(codes[which].toString());
                    }else{
                    	select_names2.remove(names[which]);
						select_codes2.remove(codes[which]);
                    }
//                   noticeToUser.setText(sb.delete(0, sb.length()-1));
//                   noticeToUser.setTag(sb2.delete(0, sb2.length()-1));
                }
            });
            
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    StringBuffer sb1 = new StringBuffer();
                    StringBuffer sb2 = new StringBuffer();
                    
                    for (int i = 0; i < select_names2.size(); i++) {
                    	sb1.append(select_names2.get(i)+",");
                    	sb2.append(select_codes2.get(i)+",");
					}
                    
                    if (sb1.toString().length()>0) {
                    	sb1=sb1.deleteCharAt(sb1.toString().length()-1);
					}
                    
                    if (sb2.toString().length()>0) {
						sb2=sb2.deleteCharAt(sb2.toString().length()-1);
					}
                   noticeToUser.setText(sb1.toString());
                   noticeToUser.setTag(sb2.toString());
                   select_names2.clear();
                   select_codes2.clear();
                }
            });
            
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    
                }
            });
            builder.show();
		}

		/**
		 * 执行通知发送
		 * 
		 * @param noticeMessageMap
		 *            消息体
		 * @param UserMap
		 *            消息关系体
		 * @return
		 */
		public Boolean requestTaskExecute(
				HashMap<String, Object> noticeMessageMap,
				ArrayList<HashMap<String, Object>> msgUserMap) {
			Boolean result = false;
			String methodName = "InsertNoticeMessage";// 上传的方法名称
			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> param = new HashMap<String, Object>();
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("MsgID", noticeMessageMap.get("msgid"));
			hashMap.put("MsgTitle", noticeMessageMap.get("msgtitle"));
			hashMap.put("MsgContent", noticeMessageMap.get("msgcontent"));
			hashMap.put("CreateTime", noticeMessageMap.get("createtime"));
			hashMap.put("PublishDate", noticeMessageMap.get("publishdate"));
			hashMap.put("CreateUserID", noticeMessageMap.get("createuserid"));
			hashMap.put("Remarks", noticeMessageMap.get("remarks"));
			
			//TODO  测试  BUG BYK
			hashMap.put("MsgDX", noticeMessageMap.get("msgdx"));
		//	hashMap.put("MsgDX", noticeToUser.getTag().toString().trim());
			//
			hashMap.put("IsEdit", noticeMessageMap.get("isedit"));
			hashMap.put("IsDelete", noticeMessageMap.get("isdelete"));
			hashMap.put("UpdateTime", noticeMessageMap.get("updatetime"));
			hashMap.put("IsSend", noticeMessageMap.get("issend"));
			list.add(hashMap);

			String noticeMessageJson = JsonHelper.listToJSON(list);

			list.clear();
			//for (int i = 0; i < splits.length; i++) {
				for (int i = 0; i < msgUserMap.size(); i++) {
				hashMap = new HashMap<String, Object>();
				HashMap<String, Object> map = msgUserMap.get(i);
				hashMap.put("MessageID", map.get("messageid"));
				
				if (IsCompany==1) {
					ArrayList<HashMap<String, Object>> list_iscompany = SQLiteDataProvider
							.getInstance()
							.queryBySqlReturnArrayListHashMap(
									"select * from PC_Users where UserID = '"
											+ map.get("userid")+ "'");
					hashMap.put("UserID", list_iscompany.get(0).get("regioncode"));
				}else{
					hashMap.put("UserID", map.get("userid"));
				}
			
				hashMap.put("IsReaded", map.get("isreaded"));
				hashMap.put("IsDelete", map.get("isdelete"));
				hashMap.put("UpdateTime", noticeMessageMap.get("updatetime"));
				
				
				list.add(hashMap);
					
			}

			String usersJson = JsonHelper.listToJSON(list);

			param.put("NoticeMessageJson", noticeMessageJson);
			param.put("UsersJson", usersJson);

			String token = "";
			try {
				token = DESSecurity.encrypt(methodName);
			} catch (Exception e1) {

				e1.printStackTrace();
			}
			param.put("token", token);
			param.put("IsCompany", IsCompany);
			params.add(param);

			try {
				result = ((Boolean) WebServiceProvider.callWebService(
						Global.NAMESPACE, methodName, params, Global
								.getGlobalInstance().getSystemurl()
								+ Global.WEBSERVICE_URL,
						WebServiceProvider.RETURN_BOOLEAN, true));
				if (result == null) {
					result = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
			return result;
		}

		/**
		 * 用于发布通知
		 * 
		 * @param context
		 *            上下文
		 * @param MsgID
		 *            消息ID
		 * @param flag
		 *            用于判断是发件箱发送还是发布公告界面发送
		 * @return
		 */
		public void sendNotice(final Context context, final String MsgID,
				final boolean flag, final Handler handler) {
			yutuLoading.showDialog();
			new Thread(new Runnable() {

				@Override
				public void run() {
					boolean result = false;
					if (Net.checkNet(context)) {
						// 发送信息至服务器
						ArrayList<HashMap<String, Object>> list = SQLiteDataProvider
								.getInstance()
								.queryBySqlReturnArrayListHashMap(
										"select * from T_Bas_Message where MsgID = '"
												+ MsgID + "'");

						ArrayList<HashMap<String, Object>> list2 = SQLiteDataProvider
								.getInstance()
								.queryBySqlReturnArrayListHashMap(
										"select * from T_Rel_UserMsg where MessageID = '"
												+ MsgID + "'");
						if (result = requestTaskExecute(list.get(0), list2)) {
							SQLiteDataProvider.getInstance().ExecSQL(
									"update T_Bas_Message set IsSend = 1 where MsgID='"
											+ MsgID + "'");
						}else{
							SQLiteDataProvider.getInstance().ExecSQL(
									"update T_Bas_Message set IsSend = 0 where MsgID='"
											+ MsgID + "'");
						}
						if (result) {
							handler.sendEmptyMessage(NoticeActivity.HANDLER_STATUS_SUCCESS);
						} else {
							if (flag) {
								handler.sendEmptyMessage(NoticeActivity.HANDLER_STATUS_FAILED_SAVE);
							} else {
								handler.sendEmptyMessage(NoticeActivity.HANDLER_STATUS_FAILED);
							}
						}
					} else {
						handler.sendEmptyMessage(NoticeActivity.HANDLER_STATUS_NET_DISABLE);
					}

				}
			}).start();
		}

		public boolean deleteFile(String guid) {
			HashMap<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("guid", guid);
			ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
					.getList("FilePath,Extension,FK_Unit", conditions,
							"T_Attachment");
			if (data != null && data.size() == 1) {
				HashMap<String, Object> map = data.get(0);
				String fk_unit = map.get("fk_unit").toString();
				String extension = map.get("extension").toString();
				T_Attachment.transitToChinese(Integer.valueOf(fk_unit));
				String filepath = Global.SDCARD_RASK_DATA_PATH
						+ "Attach/"
						+ T_Attachment.transitToChinese(Integer
								.valueOf(fk_unit)) + "/" + guid + extension;
				File file = new File(filepath);
				if (file.exists()) {
					file.delete();
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}

		}


	}

	protected void showDialog(final String guid, final String filenameString,
			final Context context,
			final ArrayList<HashMap<String, Object>> attachAdapterData) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		String selections[] = { "删除" };
		dialog.setItems(selections, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(
							context);
					deleteBuilder.setTitle("删除");
					deleteBuilder.setIcon(R.drawable.icon_delete);
					deleteBuilder.setMessage("你确定要删除" + filenameString + "吗");
					deleteBuilder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									sendObject.deleteFile(guid);
									String sql = "delete from T_Attachment"
											+ " where guid = '" + guid + "'";
									SqliteUtil.getInstance().execute(sql);
									upDataAttathListView();
								}
							});
					deleteBuilder.setNegativeButton("取消", null);
					AlertDialog ad = deleteBuilder.create();
					ad.show();
					break;
				}

			}
		}).show();
	}

	public class Attach_Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> attachAdapterData;

		public Attach_Adapter(
				ArrayList<HashMap<String, Object>> attachAdapterData) {
			this.attachAdapterData = attachAdapterData;
		}

		@Override
		public int getCount() {
			int size = attachAdapterData.size();
			return size;
		}

		@Override
		public Object getItem(int position) {
			return attachAdapterData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void addData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.attachAdapterData.addAll(CompanyAdapterData);
			notifyDataSetChanged();
		}

		public void updateData(
				ArrayList<HashMap<String, Object>> CompanyAdapterData) {
			this.attachAdapterData = CompanyAdapterData;
			setListViewHeightBasedOnChildren(task_attach_list);
			notifyDataSetChanged();
		}

		public ArrayList<HashMap<String, Object>> getData() {
			return attachAdapterData;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(NoticeActivity.this,
						R.layout.listitem, null);

			}
			ImageView rw_icon = (ImageView) convertView
					.findViewById(R.id.listitem_left_image);
			rw_icon.setImageResource(R.drawable.icon_table);
			TextView rwmc_text = (TextView) convertView
					.findViewById(R.id.listitem_text);
			rwmc_text.setText(attachAdapterData.get(position).get("filename")
					.toString());
			rwmc_text.setTextSize(20);
			rwmc_text.setTag(attachAdapterData.get(position).get("guid")
					.toString());

			return convertView;
		}
	}

	public ArrayList<TaskFile> getAllUploadFile(int fk_unit, String fk_id) {
		ArrayList<TaskFile> _ListFile = new ArrayList<TaskFile>();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("FK_Unit", fk_unit + "");
		condition.put("fk_id", fk_id);
		ArrayList<HashMap<String, Object>> fileLists = SqliteUtil.getInstance()
				.getList(" * ", condition, "T_Attachment");
		if (fileLists != null || fileLists.size() > 0) {

			for (HashMap<String, Object> map : fileLists) {
				TaskFile taskFile = new TaskFile();
				String absolutePath = Global.SDCARD_RASK_DATA_PATH + "Attach/"
						+ T_Attachment.transitToChinese(fk_unit) + "/"
						+ map.get("filepath").toString();
				taskFile.setGuid(map.get("guid").toString());
				taskFile.setFileName(map.get("filename").toString());
				taskFile.setFilePath(map.get("filepath").toString());
				taskFile.setAbsolutePath(absolutePath);
				taskFile.setUnitId(map.get("fk_unit").toString());
				taskFile.setExtension(map.get("extension").toString());
				_ListFile.add(taskFile);
			}
		}

		return _ListFile;
	}

	public void upDataAttathListView() {
		if (attachAdapter != null) {
			attachAdapter.updateData(getAttachAdapterData(T_Attachment.TZTG
					+ "", sendObject.getMsgID()));
		}

	}
	
	
	public void upLoadFilesMethod2(ArrayList<TaskFile> fileList,  Context context,String rwGuid) 
	{
		
		
		new TaskUploadAsync(fileList, context,rwGuid).execute();
	}

	class TaskUploadAsync extends AsyncTask<Object, Integer, Object> {

		ArrayList<TaskFile> listAllFile;
		/** 上传附件进度条 */
		ProgressDialog pdialog;
		private int index;
		/** 附件是否已经上传过 */
		Boolean isUpload = false;
		private Context mcontext;
		private String rwGuid;

		TaskUploadAsync(ArrayList<TaskFile> listAllFile, Context mcontext, String rwGuid) {
			this.listAllFile = listAllFile;
			this.mcontext = mcontext;
			this.rwGuid=rwGuid;
			pdialog = new ProgressDialog(mcontext, 0);
			pdialog.setCancelable(false);
			pdialog.setMax(100);
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			pdialog.cancel();
			if (null != result && !"".equals(result))
				Toast.makeText(mcontext, (CharSequence) result, Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreExecute() {
			if (listAllFile == null || listAllFile.size() == 0) {
				Toast.makeText(mcontext, "没有选择附件！", Toast.LENGTH_SHORT).show();
			} else {
				String fileName = listAllFile.get(index).getFileName() + listAllFile.get(index).getExtension();
				pdialog.setMessage(fileName + " 正在上传...");
				pdialog.show();
			}

		}

		protected void onProgressUpdate(Integer... values) {
			String fileName = listAllFile.get(index).getFileName() + listAllFile.get(index).getExtension();
			if (values[0] == 100) {
				if (isUpload) {
					pdialog.setMessage(fileName + " 已经上传");
					isUpload = false;
				} else {
					pdialog.setMessage(fileName + " 上传成功");
				}

			} else {
				pdialog.setMessage(fileName + " 正在上传...");

			}
			pdialog.setProgress(values[0]);
		}

		@Override
		protected Object doInBackground(Object... params) {
			String serverTime = DisplayUitl.getServerTime();// 首先更新附件表中的更新时间
			if (serverTime != null && !serverTime.equals("")) {
				for (TaskFile taskFile : listAllFile) {// 更新updatetime
														// 确保其他人能够同步此数据
					String guid = taskFile.getGuid();
					ContentValues updateValues = new ContentValues();
					updateValues.put("UpdateTime", serverTime);
					String[] whereArgs = { guid };
					try {
						SqliteUtil.getInstance().update("T_Attachment", updateValues, "guid=?", whereArgs);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} else {
				return "网络连接异常，请检查网络设置后重试！";
			}
			HttpUploader httpUploader = new HttpUploader();

			httpUploader.setWebServiceUrl(Global.getGlobalInstance().getSystemurl() + Global.WEBSERVICE_URL);

			for (int n = 0; n < listAllFile.size(); n++) {

				TaskFile taskFile = listAllFile.get(n);

				ArrayList<HashMap<String, Object>> params0 = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> param0 = new HashMap<String, Object>();
				String path = T_Attachment.transitToChinese(Integer.valueOf(taskFile.getUnitId())) + "/" + taskFile.getFilePath();
				param0.put("Path", path);
				param0.put("type", 0);
				params0.add(param0);

				int finishblocks = 0;// 断点包数

				try {
					Object resultResponseObj0 = WebServiceProvider.callWebService(Global.NAMESPACE, "GetUploadFileCount", params0, Global.getGlobalInstance().getSystemurl()
							+ Global.WEBSERVICE_URL, WebServiceProvider.RETURN_INT, true);
					if (null != resultResponseObj0) {
						finishblocks = Integer.parseInt(resultResponseObj0.toString());
					} else {
						return "获取附件断点信息异常";
					}
					if (finishblocks == -1) {
						return "获取附件断点信息失败";
					}

					if (finishblocks == 20000) {
						isUpload = true;
						publishProgress(100);
						continue;

					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

				FileInputStream fis = null;

				try {

					File absFile = new File(taskFile.getAbsolutePath());
					fis = new FileInputStream(absFile);

					boolean isEnd = false;

					int totalblocks = (int) absFile.length() / (1024 * 500) + 1;
					for (int i = 0; i < totalblocks; i++) {
						if (i < finishblocks) {
							byte[] buffers = new byte[1024 * 500];
							fis.read(buffers);
							continue;
						}
						String attachmentData = "";
						if (i == totalblocks - 1) {
							isEnd = true;
							byte[] buffers = new byte[(int) absFile.length() % (1024 * 500)];

							fis.read(buffers);
							attachmentData = Base64.encodeToString(buffers, Base64.DEFAULT);

						} else {
							byte[] buffer = new byte[1024 * 500];

							fis.read(buffer);
							attachmentData = Base64.encodeToString(buffer, Base64.DEFAULT);

						}
							//获取附件
						String AttachmentJosn = "[" + GetFujian(taskFile, i + "",rwGuid).toString() + "]";

						Boolean resultBoolean = httpUploader.upLoadAffixMethod(AttachmentJosn, attachmentData, isEnd);
						if (resultBoolean) {
							index = n;
							publishProgress((i + 1) * 100 / totalblocks);
						} else {
							return "上传附件失败,请检查网络是否正常！";
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					return "上传附件出现异常";
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
					}
				}
			}
			if (handler != null) {
				handler.sendEmptyMessage(UploadFile.UPLOAD_CALL_BACK);
			}
			return null;

		}

	}

	private JSONObject GetFujian(TaskFile taskFile, String i,String rwGuid) {
		JSONObject _JsonObject = new JSONObject();
		HashMap<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("guid", taskFile.getGuid());

		ArrayList<HashMap<String, Object>> _TaskList = SqliteUtil.getInstance().getList("T_Attachment", conditions);
		//得到billid
		HashMap<String, Object> conditions2 = new HashMap<String, Object>();
		conditions.put("guid", rwGuid);
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.getList("T_YDZF_RWXX", conditions2);
		try {
			HashMap<String, Object> _HashMapTemp = _TaskList.get(0);
			Set _Iterator = _HashMapTemp.entrySet();

			for (Iterator iter = _Iterator.iterator(); iter.hasNext();) {

				Map.Entry entry = (Map.Entry) iter.next();

				String _Key = entry.getKey().toString();
				if (_Key.contains("filepath")) {
					String fileNameWithPath = entry.getValue().toString();
					fileNameWithPath = fileNameWithPath + "." + i;
					_JsonObject.put("fileNameWithPath", fileNameWithPath);
				}
				_JsonObject.put(_Key, entry.getValue().toString());
			
				if (_Key.equals("FK_ID")||_Key.equals("fk_id")) {
					
					String fk_id = entry.getValue().toString();
					_JsonObject.put("billid",fk_id );
				}
				
			
				_JsonObject.put("biztype", "17");
				
			}

		} catch (JSONException ex) {
			LogUtil.v("Json", ex.getMessage());
		}
		return _JsonObject;
	}
}
