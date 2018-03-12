package com.mapuni.android.infoQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapuni.android.MobileEnforcement.R;
import com.mapuni.android.base.BaseActivity;
import com.mapuni.android.base.Global;
import com.mapuni.android.base.controls.loading.YutuLoading;
import com.mapuni.android.base.interfaces.PathManager;
import com.mapuni.android.base.util.DisplayUitl;
import com.mapuni.android.base.widget.PagingListView;
import com.mapuni.android.dataprovider.FileHelper;
import com.mapuni.android.dataprovider.SqliteUtil;

/**
 * 
 * Description: 内蒙古法律法规类列表展示
 * 
 * @author 丁蒙蒙
 * @Version 1.4.7
 * @Copyright 中科宇图天下科技有限公司 Create at: 2013-09-03
 */
public class LNFLFGShow extends BaseActivity {
	/** 法律法规的分页listview */
	private PagingListView handbookListView;
	private HandbookAdapter handbookAdapter;
	private ListView mListView;
	private FileHelper fileHelper;
	/** 目标文件夹下的文件名列表 */
	ArrayList<File> filesForAdapter = new ArrayList<File>();
	ArrayList<File> fileList = null;
	String title = "";
	private String filename;
	private String filePid;
	private String remark;
	private ArrayList<HashMap<String, Object>> result;
	private ArrayList<HashMap<String, Object>> searchResult;
	private boolean isSearch;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				handbookAdapter.notifyDataSetChanged();
				break;
			case 1:
				handbookAdapter.notifyDataSetChanged();
				Toast.makeText(LNFLFGShow.this, "全部数据加载完成", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

	/** 环境监察执法手册文件目录 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_mapuni);
		initTitleData();
		Intent intent = getIntent();
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		filePid = intent.getStringExtra("pid");
		title = intent.getStringExtra("title");
		remark = intent.getStringExtra("remark");
		
		if (TextUtils.isEmpty(remark)||"NULL".equals(remark)||"null".equals(remark)) {
			String remarksqlString="select * from T_HandBookCatalog where id='"+filePid+"' and  Name='"+title+"'";
			ArrayList<HashMap<String, Object>> maps = SqliteUtil.getInstance().queryBySqlReturnArrayListHashMap(remarksqlString);
			
			if (maps.size()!=0) {
				remark=maps.get(0).get("remark").toString();
			}
		}

		fileHelper = new FileHelper();
		if (title.length()>8) {
			SetBaseStyle(titleLayout, title.substring(0, 8)+"...");
		}else{
			SetBaseStyle(titleLayout, title);
		}
		
		filename = intent.getStringExtra("filename");
		isSearch = intent.getBooleanExtra("isSearch", false);

		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
		View v = inflater.inflate(R.layout.ln_flfg_query, null);
		middleLayout.addView(v);
		super.setSearchButtonVisiable(true);

		handbookListView = (PagingListView) v.findViewById(R.id.ln_flfg_list);
		EditText hbsc_name=(EditText) v.findViewById(R.id.hbsc_name);
		EditText hbsc_remark=(EditText) v.findViewById(R.id.hbsc_remark);
		hbsc_name.setText(title);
		
		hbsc_remark.setText(remark);
		
		hbsc_name.setFocusable(false);
		hbsc_name.setEnabled(false);
		hbsc_remark.setFocusable(false);
		hbsc_remark.setEnabled(false);
		hbsc_name.setTextColor(Color.BLACK);
		hbsc_remark.setTextColor(Color.BLACK);
		totalDataList = getDataList();
		MyOnPageCountChangListener onPageCountChangListener = new MyOnPageCountChangListener();
		handbookListView.setOnPageCountChangListener(onPageCountChangListener);
		handbookAdapter = new HandbookAdapter();
		handbookListView.setAdapter(handbookAdapter);
		handbookListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (DisplayUitl.isFastDoubleClick()) {
							return;
						}
//				//TODO 		
						if(totalDataList.size()==0&&arg2==0){
							return;
						}
						
						TextView view = (TextView) arg1
								.findViewById(R.id.list_name_tv);
						String lawfilePath = view.getTag().toString();
//						String filePath = 
//								PathManager.SDCARD_RASK_DATA_PATH
//										+ "Attach/HBSC/" + lawfilePath;
						
						String[] split = lawfilePath.split("/");
						String filePath = 
								PathManager.SDCARD_RASK_DATA_PATH
								+ "Attach/HBSC/"+split[split.length-1];
						String attguid = totalDataList.get(arg2).get("guid")
								.toString(); 
						ImageView progress_image = (ImageView) arg1
								.findViewById(R.id.progress_iv);
						TextView progress_text = (TextView) arg1
								.findViewById(R.id.progress_tv);
						fileHelper.showFileByGuid_HBSC(attguid, LNFLFGShow.this,
								true, true, 20, new MyUIUpdate(progress_image, progress_text, attguid, filePath));
					}
				});
		queryImg.setVisibility(View.GONE);
//		/**
//		 * 查询功能
//		 */
//		queryImg.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LinearLayout layout = new LinearLayout(LNFLFGShow.this);
//				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//				final EditText editText = new EditText(LNFLFGShow.this);
//				TextView textview = new TextView(LNFLFGShow.this);
//				textview.setText("文件名称：");
//				layout.addView(textview);
//				layout.addView(editText, param);
//				AlertDialog.Builder dialog = new Builder(LNFLFGShow.this);
//				dialog.setIcon(LNFLFGShow.this.getResources().getDrawable(
//						R.drawable.icon_mapuni_white));
//				dialog.setTitle(title.substring(0, 8)+"..." + "文件搜索");
//				dialog.setView(layout);
//				dialog.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								isSearch = true;
//								handbookListView.setIsCompleted(false);// 重置状态
//								pagingListCount = 1;
//								filename = editText.getText().toString();
//								totalDataList = getDataList();
//								handbookAdapter = new HandbookAdapter();
//								handbookListView.setAdapter(handbookAdapter);
//								handbookAdapter.notifyDataSetChanged();
//							}
//						});
//				dialog.setNegativeButton("取消", null);
//				AlertDialog alertDialog = dialog.create();
//				((Dialog) alertDialog).setCanceledOnTouchOutside(true);
//				alertDialog.show();
//
//			}
//		});
	}

	/** fk_id与name的集合 求出title */
	private HashMap<String, Object> titleData = new HashMap<String, Object>();

	/**
	 * 初始化fk_id 与name的集合
	 */
	private void initTitleData() {
		ArrayList<HashMap<String, Object>> data = SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(
						"select id ,name from t_handbookcatalog");
		for (int i = 0; i < data.size(); i++) {
			String key = data.get(i).get("id").toString();
			String value = data.get(i).get("name").toString();
			if (key != null && !"".equals(key))
				titleData.put(key, value);
		}
	}

	/**
	 * FileName: LNFLFGShow.java Description:环保手册分页adapter
	 * 
	 * @author 刘少凯
	 */
	private class HandbookAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public HandbookAdapter() {
			mInflater = LayoutInflater.from(LNFLFGShow.this);
		}

		@Override
		public int getCount() {
			if (totalDataList.size() == 0)
				return 1;
			return totalDataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return totalDataList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (totalDataList.size() == 0) {
				/** 没有数据，替换布局 */
				YutuLoading loading = new YutuLoading(LNFLFGShow.this);
				loading.setLoadMsg("加载中", "对不起，暂无附件！", Color.BLACK);
//				loading.setFocusable(false);
//				loading.setClickable(false);
//				loading.setEnabled(false);
				loading.setFailed();
				loading.setLayoutParams(new android.widget.AbsListView.LayoutParams(
						android.widget.AbsListView.LayoutParams.FILL_PARENT,
						android.widget.AbsListView.LayoutParams.FILL_PARENT));
				loading.setFocusable(false);
				loading.setClickable(false);
				loading.setEnabled(false);
				return loading;
			}
			ViewHolder holder;
			if (convertView == null) {
				convertView = (RelativeLayout) mInflater.inflate(
						R.layout.list_item_tool, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.listitem_left_image);
				holder.mTextView = (TextView) convertView
						.findViewById(R.id.list_name_tv);
				holder.mTextView.setSelected(true);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String fileName = (String) totalDataList.get(position).get(
					"filename");
			String fk_id = totalDataList.get(position).get("fk_id").toString();
			holder.mTextView
					.setTag(totalDataList.get(position).get("filepath"));
			holder.imageView.setImageResource(R.drawable.icon_down);

			if (fk_id != null && !"".equals(fk_id)
					&& titleData.get(fk_id) != null
					&& fileName.contains(titleData.get(fk_id).toString() + "-")) {
				holder.mTextView.setText(fileName.substring(titleData
						.get(fk_id).toString().length() + 1));
			} else {
				holder.mTextView.setText(fileName);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView imageView;
			TextView mTextView;
		}
	}

	/** 法律法规数据页数 */
	private int pagingListCount = 1;
	/** 法律法规已读取的数据 */
	private ArrayList<HashMap<String, Object>> totalDataList = new ArrayList<HashMap<String, Object>>();

	/**
	 * 分页加载监听，开启线程查询分页数据然后发送消息通知界面刷新
	 */
	private class MyOnPageCountChangListener implements
			PagingListView.PageCountChangListener {

		@Override
		public void onAddPage(AbsListView view) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					++pagingListCount;
					Log.e("hello", pagingListCount + "");
					ArrayList<HashMap<String, Object>> newData = null;
					newData = getDataList();
					totalDataList.addAll(newData);
					if (newData.size() < Global.getGlobalInstance()
							.getListNumber()) {
						handbookListView.setIsCompleted(true);
						handler.sendEmptyMessage(1);
						return;
					}
					handler.sendEmptyMessage(0);
				}
			}).start();
		}
	}

	/**
	 * 获取每次从数据库中读取的数据
	 * 
	 * @return 每次加载的数据
	 */
	private ArrayList<HashMap<String, Object>> getDataList() {
		String sqlstr;
		if (isSearch) {
			if (filePid != null && !"".equals(filePid))
				sqlstr = "select guid, filename, filepath, fk_id from T_Attachment where FK_Id = '"
						+ filePid
						+ "' and FileName like '%"
						+ filename
						+ "%' and FK_Unit='16' limit " + getOrder();
			else
				sqlstr = "select guid, filename, filepath, fk_id from T_Attachment where FileName like '%"
						+ filename + "%' and FK_Unit='16' limit " + getOrder();
		} else
			//if(!"null".equals(remark)){
				if(TextUtils.isEmpty(remark)&&!"NULL".equals(remark)&&!"null".equals(remark)){
			sqlstr = "select guid, filename, filepath, fk_id from T_Attachment where FK_Id = '"
					+ filePid
					+ "' and FK_Unit='15' and remark = '"
					+ remark
					+ "' limit " + getOrder();
			}else {
				sqlstr = "select guid, filename, filepath, fk_id from T_Attachment where FK_Id = '"
						+ filePid
						+ "' and FK_Unit='15' "
						+ " limit " + getOrder();
			}
		return SqliteUtil.getInstance()
				.queryBySqlReturnArrayListHashMap(sqlstr);
	}

	/**
	 * 分页限制
	 */
	public String getOrder() {
		int count = Global.getGlobalInstance().getListNumber();
		int x = pagingListCount * count - count;
		int j = count;
		String order = x + "," + j;
		return order;
	}

	/**
	 * 实现FileHelper中接口，在下载完成后更新界面
	 * 
	 * @author 刘少凯
	 */
	class MyUIUpdate implements FileHelper.UIUpdate {

		private long progressNum = 0;
		private ImageView progressImage;
		private TextView progressText;
		private AnimationDrawable animationDrawable;
		private String filePath;
		private String attGuid;

		public MyUIUpdate(ImageView progressImage, TextView progressText,
				String attGuid, String filePath) {
			this.progressImage = progressImage;
			this.progressText = progressText;
			this.animationDrawable = (AnimationDrawable) progressImage.getDrawable();
			this.attGuid = attGuid;
			this.filePath = filePath;
		}

		/**
		 * 文件不存在设置下载进度可见
		 */
		public void setProgressViewVisible() {
			this.progressImage.setVisibility(View.VISIBLE);
			this.progressText.setVisibility(View.VISIBLE);
			this.progressText.setText(" 0%");
			animationDrawable.start();
		}

		/**
		 * 文件下载结束 设置下载进度不可见
		 */
		public void setProgressViewGone() {
			animationDrawable.start();
			this.progressImage.setVisibility(View.GONE);
			this.progressText.setText("");
			this.progressText.setVisibility(View.GONE);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.mapuni.android.dataprovider.FileHelper.UIUpdate#update(int)
		 * result 0：下载成功显示下载内容 1：文件不存在或发生异常 2：由于网络问题下载失败 4：文件过大提醒后选择继续
		 * 5：文件过大提醒后选择取消
		 */
		@Override
		public void update(long result) {
			setProgressViewGone();
			if (result == 0) {
				File file = new File(filePath);
				fileHelper.openFile(file, LNFLFGShow.this);
			} else {
				if (result == 1) {
					Toast.makeText(LNFLFGShow.this, "文件不存在", 1000).show();
				} else if (result == 2) {
					Toast.makeText(LNFLFGShow.this, "由于网络原因文件下载失败", 1000)
							.show();
				} else if (result == 3) {
				} else if (result == 4) {
					setProgressViewVisible();
				} else if (result == 5) {
				}
			}
		}

		@Override
		public void updateProgress(long progress, long fileSize) {
			progressNum += progress;
			if (progressNum * 100 / fileSize < 10)
				progressText.setText(" " + progressNum * 100 / fileSize + "%");
			else
				progressText.setText("" + progressNum * 100 / fileSize + "%");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fileHelper.stopTaskByFileName();
	}
}